package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Deliveredfreg extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private  LayoutInflater inflater;
    private View layout;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected DeliveredAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private ArrayList<Transit_record> transit=new ArrayList<Transit_record>(20);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        //   initDataset();
        inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)getActivity().findViewById((R.id.custom_toast_container)));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new DeliveredAdapter(transit);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

       /* mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });

        mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });*/

        if(mAdapter.getItemCount()==0)
            rootView = inflater.inflate(R.layout.no_delivered_booking, container, false);

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        int  shipment_id=transit.get(position).getshipmentNo();
                        Intent intent=new Intent(getActivity(),DeliveredDetail.class);
                        intent.putExtra("shipment_id", shipment_id);
                        startActivity(intent);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {

        SharedPreferences userid=getActivity().getSharedPreferences("U_id",getActivity().MODE_PRIVATE);


        String localuserid=userid.getString("userid","null");

        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=delivered"+"&userid="+localuserid;
        GrabURL Excecute=new GrabURL();
        Excecute.execute(url);
    }


    private   class GrabURL extends AsyncTask<String, Void, String>
    {

        private String content =  null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        private Transit_record  temprary_record;

        private volatile String Date_Time=null;
        private volatile String Truck_type=null;
        private volatile String From=null;
        private volatile String To=null;
        private volatile String shipmentno=null;
        private String local;


        protected void onPreExecute()
        {
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }


        protected String doInBackground(String... urls)
        {

            // String URL = null;
            BufferedReader bufferinput =null;
            HttpURLConnection con=null;

            try
            {

                URL url = new URL(urls[0]);
                System.out.println(urls[0]);
                System.out.println("data hit");
                con = (HttpURLConnection) url.openConnection();
                bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
                try
                {
                    local = bufferinput.readLine();
                    System.out.println("json data"+local);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    System.out.println("json data not fetched");
                }
                Log.i("Json data received..",local);


            }
            catch(IOException e)
            {
                e.printStackTrace();
                Log.i("Erro", "data downloading erro");

            }

            try
            {
                bufferinput.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            con.disconnect();

            return local;

        }

        /*  protected void onCancelled()
          {
           dialog.dismiss();
           Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, 25, 400);
           toast.show();
           }
    */
        protected void onPostExecute(String content)
        {
            // dialog.dismiss();

            displayCountryList(content);

            dialog.dismiss();
        }

    }

    private void displayCountryList(String response){

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("transit");  //.getJSONArray("transit");

            int length=countryListObj.length();

            if(length==0)
            {
                Toast toast = Toast.makeText(getActivity(), "No Data Available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
               // toast.show();
            }


            System.out.println("json array length="+length);


            if(!transit.isEmpty())
            {
                transit.removeAll(transit);
            }

            for (int i=0; i<length; i++)
            {

                JSONObject jsonChildNode=countryListObj.getJSONObject(i);
                String  Date_time=jsonChildNode.optString("date_time");
                String  truck_type=jsonChildNode.optString("truck_type");
                String  to=jsonChildNode.optString("from");
                String  from = jsonChildNode.optString("to");
                int shipmentNo=Integer.parseInt(jsonChildNode.optString("shipmentno").toString());
                Transit_record tempclass=new Transit_record(Date_time,truck_type,from,to,shipmentNo);
                transit.add(tempclass);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }
    }


// closing AsyncTask


}
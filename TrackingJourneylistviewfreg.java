package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TrackingJourneylistviewfreg extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private  LayoutInflater inflater;
    private View layout;
    private ProgressDialog pDialog;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Trackinglvadapter mAdapter;
    private ArrayList<Tracklistdataclass> transit;

  //  protected String[] mDataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transit =new ArrayList<Tracklistdataclass>(20);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new Trackinglvadapter(getActivity(),transit);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        // int  shipment_id=transit.get(position).getshipmentNo();
                        //  Intent intent=new Intent(getActivity(),ConfirmDetail.class);
                        //   intent.putExtra("shipment_id", shipment_id);
                        //   startActivity(intent);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        initDataset();
       /* inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)getActivity().findViewById((R.id.custom_toast_container)));*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        if(mAdapter.getItemCount()==0)
            rootView = inflater.inflate(R.layout.no_data_availble, container, false);

        return rootView;
    }


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


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void initDataset() {
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=trackjourneyllistview&userid=214&vehicleid=tracking_v_id";
        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        transit.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                Tracklistdataclass localdata = new Tracklistdataclass(object.getString("vehicle_number"),object.getString("vehicl_description"),object.getString("driver_name"),object.getString("driverimageurl"));

                                transit.add(localdata);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
       // AppController.getInstance().addToRequestQueue(req);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);

    }


  /*  private   class GrabURL extends AsyncTask<String, Void, String>
    {

        private String content =  null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(getActivity());

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
                  e.printStackTrace();
                }



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

        *//*  protected void onCancelled()
          {
           dialog.dismiss();
           Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, 25, 400);
           toast.show();
           }
    *//*
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

            *//*if(length==0)
            {
                Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
                toast.show();
            }*//*

            System.out.println("json array length="+length);


            if(!transit.isEmpty())
            {
                transit.removeAll(transit);
            }

            for (int i=0; i<length; i++)
            {

                JSONObject jsonChildNode=countryListObj.getJSONObject(i);
                String  vehicleno=jsonChildNode.optString("vehicle_no");
                String  vehicledescription=jsonChildNode.optString("description");

                String  driver_name=jsonChildNode.optString("driver_name");
                String  driverimageurl = jsonChildNode.optString("driverimageurl");

                Tracklistdataclass dataobjent=new Tracklistdataclass(vehicleno,vehicledescription,driver_name,driverimageurl);

                transit.add(dataobjent);



            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }
    }
*/

// closing AsyncTask





}



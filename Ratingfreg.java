package com.example.sushilverma.mavync;


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


public class Ratingfreg extends Fragment {

    private static final String TAG = "RatingFragment";
    private  LayoutInflater inflater;
    private View layout;
    protected RecyclerView mRecyclerView;
    protected Ratingadapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Ratingt_report_class> transit;
    private  String localuserid;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        transit=new ArrayList<Ratingt_report_class>(20);
       // Ratingt_report_class tempclass=new Ratingt_report_class("tyety","sfj","delhi","local",123,"http://121.241.125.91/cc/mavyn/online/","2.0","2.0");
      //  transit.add(tempclass);
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Ratingadapter(transit,getActivity());
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                       /* int  shipment_id=transit.get(position).getshipmentNo();
                        Intent intent=new Intent(getActivity(),ConfirmDetail.class);
                        intent.putExtra("shipment_id", shipment_id);
                        startActivity(intent);
*/
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

        return rootView;
    }






    private void initDataset() {

        SharedPreferences userid=getActivity().getSharedPreferences("U_id",getActivity().MODE_PRIVATE);
        localuserid=userid.getString("userid","null");
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=ratingreport"+"&userid="+localuserid;
        GrabURL Excecute=new GrabURL();
        Excecute.execute(url);
    }


    private   class GrabURL extends AsyncTask<String, Void, String>
    {

        private String content =  null;
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


            BufferedReader bufferinput =null;
            HttpURLConnection con=null;

            try
            {

                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();
                bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
                local = bufferinput.readLine();
                bufferinput.close();
                con.disconnect();

            }
            catch(IOException e)
            {
                e.printStackTrace();

            }

            return local;

        }


        protected void onPostExecute(String content)
        {

            displayCountryList(content);

            dialog.dismiss();
        }

    }

    private void displayCountryList(String response){

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("transit");
            int length=countryListObj.length();


            for (int i=0; i<length; i++)
            {

                JSONObject jsonChildNode=countryListObj.getJSONObject(i);
                String  Date_time=jsonChildNode.optString("date_time");
                String  truck_type=jsonChildNode.optString("truck_type");
                String  to=jsonChildNode.optString("from");
                String  from = jsonChildNode.optString("to");
                String  driverimg = jsonChildNode.optString("driverimg");
                String  driverrating = jsonChildNode.optString("driverrating");
                String  companyrating = jsonChildNode.optString("companyrating");
                int shipmentNo=Integer.parseInt(jsonChildNode.optString("shipmentno").toString());
                Ratingt_report_class tempclass=new Ratingt_report_class(Date_time,truck_type,to,from,shipmentNo,"http://121.241.125.91/cc/mavyn/online/"+driverimg,driverrating,companyrating);
                transit.add(tempclass);
            }

          /* Ratingt_report_class tempclass=new Ratingt_report_class("tyety","sfj","delhi","local",123,"http://121.241.125.91/cc/mavyn/online/","2.0","2.0");
             transit.add(tempclass); */


            mAdapter.notifyDataSetChanged();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }
    }


// closing AsyncTask


}
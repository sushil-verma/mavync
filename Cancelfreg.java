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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.google.android.gms.analytics.internal.zzy.e;


public class Cancelfreg extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected CancelledAdapter mAdapter;
    private ArrayList<Transit_record> transit=new ArrayList<>(20);
    private View rootView=null;
    private LayoutInflater inflate=null;

    private boolean dataavailabletodisplay=false;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences userid=getActivity().getSharedPreferences("U_id",getActivity().MODE_PRIVATE);
        String localuserid=userid.getString("userid","null");
        String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=Cancel"+"&userid="+localuserid;
        downloaddata(url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CancelledAdapter(transit);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                      /*  int  shipment_id=transit.get(position).getshipmentNo();
                        Intent intent=new Intent(getActivity(),DeliveredDetail.class);
                        intent.putExtra("shipment_id", shipment_id);
                        startActivity(intent);*/

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        if (dataavailabletodisplay)
        {
            inflate = getActivity().getLayoutInflater();
            rootView = inflate.inflate(R.layout.no_cancelled_booking, null, false);
        }

        return rootView;
    }


    private void downloaddata(String url){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                displayCountryList(s);

            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        volleyError.printStackTrace();
                    }


                })

        {


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }





    private void displayCountryList(String response){

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("transit");  //.getJSONArray("transit");

            int length = countryListObj.length();
            if (length>0) {


                if (!transit.isEmpty()) {
                    transit.removeAll(transit);
                }






            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);
                String Date_time = jsonChildNode.optString("date_time");
                String truck_type = jsonChildNode.optString("truck_type");
                String to = jsonChildNode.optString("from");
                String from = jsonChildNode.optString("to");
                int shipmentNo = Integer.parseInt(jsonChildNode.optString("shipmentno").toString());
                Transit_record tempclass = new Transit_record(Date_time, truck_type, from, to, shipmentNo);
                transit.add(tempclass);
            }


            mAdapter.notifyDataSetChanged();

            }
            else
                dataavailabletodisplay = true;

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

    }

}
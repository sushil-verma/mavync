package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Trackinglistviewfreg extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private  LayoutInflater inflater;
    private View layout;
    private ProgressDialog pDialog;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Trackinglvadapter mAdapter;
    private ArrayList<Tracklistdataclass> transit;

  //  protected String[] mDataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog=new ProgressDialog(getActivity());
        transit =new ArrayList<Tracklistdataclass>(20);
        mAdapter = new Trackinglvadapter(getActivity(),transit);
        initDataset();
       /* inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)getActivity().findViewById((R.id.custom_toast_container)));*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);



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

      /*  if(mAdapter.getItemCount()==0)
            rootView = inflater.inflate(R.layout.no_data_availble, container, false);*/

        return rootView;
    }




    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.

        super.onSaveInstanceState(savedInstanceState);
    }


    private void initDataset() {

        String userid=getActivity().getSharedPreferences("U_id", MODE_PRIVATE).getString("userid", null);
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=trackalllist&userid="+userid;
        pDialog.setMessage("Downloading json...");
        pDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();
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



    private void displayCountryList(String response) {

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("currentstatus");

            int length = countryListObj.length();


            if (length != 0) {


                if (!transit.isEmpty()) {
                    transit.removeAll(transit);
                }

                for (int i = 0; i < length; i++) {

                    JSONObject jsonChildNode = countryListObj.getJSONObject(i);
                    String vehicleno = jsonChildNode.optString("Vehicleno");
                    String drivername = jsonChildNode.optString("drivername");
                    String driverimgurl = jsonChildNode.optString("driverimg");
                    String currentaddress = jsonChildNode.optString("Address");
                    Tracklistdataclass tempclass = new Tracklistdataclass(vehicleno, currentaddress, drivername, "http://121.241.125.91/cc/mavyn/online/" + driverimgurl);
                    transit.add(tempclass);

                }

                mAdapter.notifyDataSetChanged();

            }
          }
            catch(JSONException e)
            {
                e.printStackTrace();
                System.out.println("Error in reading json object");
            }

        }

    }





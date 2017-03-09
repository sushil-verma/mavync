package com.example.sushilverma.mavync;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TrackingJourneylistviewfreg extends Fragment {

    private static final String TAG = "LiveJourneyFragment";
    private  LayoutInflater inflater;
    private View layout;
    private ProgressDialog pDialog;
    private  RecyclerView mRecyclerView;
    private  RecyclerView.LayoutManager livelayoutmanager;
    private  LiveTrackinglvadapter mAdapter;
    private ArrayList<String> transit;
    private ArrayList<JourneytableviewObject> journeytablearray;
    private static volatile MaterialBetterSpinner vehiclelist;
    private Confirm_vehicle_no_Db vehiclenodatabase=null;
    private ArrayAdapter<String> spinneradapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pDialog=new ProgressDialog(getActivity());
        vehiclenodatabase=new Confirm_vehicle_no_Db(getActivity());
        transit =new ArrayList<String>(20);
        transit.addAll(vehiclenodatabase.getAlldata());
        journeytablearray= new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;

        if (transit.size() <= 0) {
            rootView = inflater.inflate(R.layout.no_data_availble, container, false);
            return rootView;

         }

        else {


            rootView = inflater.inflate(R.layout.livetrackrecycler_view_frag, container, false);
            rootView.setTag(TAG);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.liveView);
            livelayoutmanager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(livelayoutmanager);
            mAdapter = new LiveTrackinglvadapter(getActivity(),journeytablearray);
            mRecyclerView.setAdapter(mAdapter);
            vehiclelist = (MaterialBetterSpinner) rootView.findViewById(R.id.livevehiclelist);
            spinneradapter = new ArrayAdapter<String>(getActivity(),R.layout.spinnertextbox, transit);
            spinneradapter.setDropDownViewResource(R.layout.spinnertextboxdropdown);
            vehiclelist.setAdapter(spinneradapter);
            vehiclelist.setHint("select vehicle");
            vehiclelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    String vehicle_no=transit.get(arg2);
                    initDataset(vehicle_no);
                }
            });

            return rootView;
        }
    }






    private void initDataset(String vehilcno) {
        String userid=getActivity().getSharedPreferences("U_id", MODE_PRIVATE).getString("userid", null);
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=journeytableview&userid="+ userid +"&vehicleno="+vehilcno;
        pDialog.setMessage("Downloading json...");
        pDialog.show();

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.hide();

                       /*
                        test code
                        JourneytableviewObject tabledata=new JourneytableviewObject();
                        tabledata.setDate_time("12/12/12:5:30");
                        tabledata.setCurrent_address("new delhi");
                        journeytablearray.add(tabledata);

                        mAdapter.notifyDataSetChanged();
                        */




                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonarray=object.getJSONArray("journeytableview");

                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject localobject = jsonarray.getJSONObject(i);
                                    JourneytableviewObject tabledata=new JourneytableviewObject();
                                    tabledata.setDate_time(localobject.getString("datetime"));
                                    tabledata.setCurrent_address(localobject.getString("address"));
                                    journeytablearray.add(tabledata);

                                }
                            pDialog.hide();

                            mAdapter.notifyDataSetChanged();

                           } catch (JSONException e) {
                            e.printStackTrace();
                           }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
            }
        });



        // Adding request to request queue
       // AppController.getInstance().addToRequestQueue(req);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);

    }








}



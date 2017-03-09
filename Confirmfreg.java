package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;




public class Confirmfreg extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
   // private  LayoutInflater inflater;
   // private View layout;
    protected RecyclerView mRecyclerView;
    protected ConfirmAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Transit_record> transit=new ArrayList<Transit_record>(20);
    private String  localuserid=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userid=getActivity().getSharedPreferences("U_id",getActivity().MODE_PRIVATE);
        localuserid=userid.getString("userid","null");
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=Confirm"+"&userid="+localuserid;
        downloaddata(url);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);
        //inflater = getActivity().getLayoutInflater();
       // layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)getActivity().findViewById((R.id.custom_toast_container)));
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ConfirmAdapter(getActivity(),transit);
        mRecyclerView.setAdapter(mAdapter);



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        int  shipment_id=transit.get(position).getshipmentNo();
                        Intent intent=new Intent(getActivity(),ConfirmDetail.class);
                        intent.putExtra("shipment_id", shipment_id);
                        intent.putExtra("user_id",localuserid);
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

      /*  if(mAdapter.getItemCount()==0)
            rootView = inflater.inflate(R.layout.no_confirmed_booking, container, false);*/

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


            for (int i=0; i<countryListObj.length(); i++)
            {

                JSONObject jsonChildNode=countryListObj.getJSONObject(i);
                String  Date_time = jsonChildNode.optString("date_time");
                String  truck_type = jsonChildNode.optString("truck_type");
                String  to = jsonChildNode.optString("from");
                String  from = jsonChildNode.optString("to");
                String  driverimg = jsonChildNode.optString("driverimg");
                int shipmentNo = Integer.parseInt(jsonChildNode.optString("shipmentno").toString());
                Transit_record tempclass = new Transit_record(Date_time,truck_type,from,to,shipmentNo,"http://121.241.125.91/cc/mavyn/online/"+driverimg);
                transit.add(tempclass);
            }



            mAdapter.notifyDataSetChanged();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }
    }



}
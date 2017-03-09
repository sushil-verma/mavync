package com.example.sushilverma.mavync;



import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class Confrimvehicledownloadservice extends IntentService {

    private Confirm_vehicle_no_Db vehilcedatabase=null;

    public Confrimvehicledownloadservice()
    {
        super(Confrimvehicledownloadservice.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        vehilcedatabase=new Confirm_vehicle_no_Db(getApplicationContext());
        SharedPreferences userid=getSharedPreferences("U_id",MODE_PRIVATE);
        String localuserid=userid.getString("userid","null");
        String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=trackalllist&userid="+localuserid;;
        
        downloadVehicleno(url);

        this.stopSelf();
    }

    
    
private void downloadVehicleno(String url){
        
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if(vehilcedatabase.numberOfRows()>0)
                            vehilcedatabase.deletetb();;

                        try {
                            JSONObject jsonResponse = new JSONObject(s);

                            JSONArray jsonMainNode=jsonResponse.optJSONArray("currentstatus");

                            if(jsonMainNode.length()>0) {

                                for (int i = 0; i < jsonMainNode.length(); i++) {
                                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                    String vehicleno = jsonChildNode.optString("Vehicleno").toString();
                                    vehilcedatabase.insert(vehicleno);

                                }
                            }


                        } catch (JSONException e)

                        {

                            e.printStackTrace();

                        }



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
 
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
   
    }
}

   


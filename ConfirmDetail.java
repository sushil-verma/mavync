package com.example.sushilverma.mavync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

public class ConfirmDetail extends AppCompatActivity {

	private TextView shipment_date_time;
	private TextView truck_type;
	private TextView distance;
	private TextView time;
	private TextView origin;
	private TextView destination;
	private TextView driver_name;
	private TextView  mobile_no;
	private TextView  shipment_no;
	private TextView  myeta;
	private TextView f_bill; 
	private TextView truck_no;
	private  Toolbar toolbar;
	
	 private SharedPreferences sharedpreferences;
	   private SharedPreferences.Editor editor;
	   public static final String MyPREFERENCES = "MyPrefs" ;
	   private String userid=null;
	   private String shipment_id=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		 sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	     userid= sharedpreferences.getString("customer_id", "null");
	     
	     shipment_id=getIntent().getStringExtra("shipment_id");
		
		
		
		
	    String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=confirmdetail&userid="+userid+"&shipmentid="+shipment_id;
		new GrabURL().execute(url);
		setContentView(R.layout.confirm_box);
		
		shipment_no=(TextView)findViewById(R.id.shipment_no);
		shipment_date_time=(TextView)findViewById(R.id.ship_date_time);
		truck_no=(TextView)findViewById(R.id.truck_no);
		truck_type=(TextView)findViewById(R.id.truck_type);
		driver_name=(TextView)findViewById(R.id.driver_name);
		mobile_no=(TextView)findViewById(R.id.driver_no);
		origin=(TextView)findViewById(R.id.origin);
		destination=(TextView)findViewById(R.id.destination);
		distance=(TextView)findViewById(R.id.travel_distance);
		time=(TextView)findViewById(R.id.travel_time);
		myeta=(TextView)findViewById(R.id.eta);
	    f_bill=(TextView)findViewById(R.id.freight_bill);
		
		/*ActionBar actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
		actionBar.setCustomView(R.layout.confirmshipdetail);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
		        | ActionBar.DISPLAY_SHOW_HOME); 
		
		   actionBar.setDisplayShowCustomEnabled(true);
		   actionBar.setDisplayHomeAsUpEnabled(false);
		   actionBar.setHomeButtonEnabled(true);
		   getActionBar().setDisplayShowHomeEnabled(true);
           actionBar.setIcon(R.drawable.newback);*/
           //actionBar.setHideOnContentScrollEnabled(true); //need api 21

		    // actionBar.set

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		   
	    }
	
	private class GrabURL extends AsyncTask<String, Void, String> 
    {
    	  private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    	  private static final int WAIT_TIMEOUT = 30 * 1000;
    	  private final HttpURLConnection httpconnection = null;
    	  private String content =  null;
    	  private boolean error = false;
    	  private ProgressDialog dialog = new ProgressDialog(ConfirmDetail.this);
    	  private int resCode = -1;
    	  
    	  private Transit_record  temprary_record;
    	 /* private volatile String Date_Time=null;
  		  private volatile String Truck_type=null;
  	      private volatile String From=null;
  	      private volatile String To=null;
  	*/
    	  protected void onPreExecute() {
    	   dialog.setMessage("Getting your data... Please wait...");
    	   dialog.show();
    	  }

    	 
    	  protected String doInBackground(String... urls) {

    	   String URL = null;
    	   BufferedReader bufferinput =null;
    	   String local=null;
    	   HttpURLConnection con=null;
    	   
    	   try
    	   {
    		  URL url = new URL(urls[0]);   
              con = (HttpURLConnection) url.openConnection();
              JSONObject jsonResponse; 
              System.out.println("data uploades");
              bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
           
			try {
				local = bufferinput.readLine();
				 Log.i("Json data received..",local);
			     } 
			 catch (Exception e) 
			  { 
				
		       Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
			  }

    	   }
    	   catch(Exception e)
    	   {
    		   
    	   }
    	
    	   try 
    	   {
			bufferinput.close();
			con.disconnect();
		   } 
    	   
    	   catch (IOException e) 
    	   {
		    e.printStackTrace();
			System.out.println("Reading Buffer closing error");
		   }
  
    	return local; 
   }

    	  protected void onCancelled() 
    	  {
    	   dialog.dismiss();
    	   Toast toast = Toast.makeText(ConfirmDetail.this,"Error connecting to Server", Toast.LENGTH_LONG);
    	   toast.setGravity(Gravity.TOP, 25, 400);
    	   toast.show();
    	   }

    	  protected void onPostExecute(String content) 
    	  {
    	   dialog.dismiss();
    	   Toast toast;
    	     if (error) 
    	      {
    	      toast = Toast.makeText(ConfirmDetail.this,content, Toast.LENGTH_LONG);
    	      toast.setGravity(Gravity.TOP, 25, 400);
    	      toast.show();
    	      
    	      }
    	      else 
    	     {
    	       displayjsondata(content);
    	     }
    	   }

       }

    	 private void displayjsondata(String response){

    	  JSONObject responseObj = null; 
    	  Gson gson = new Gson();

    	  try {

    	   responseObj = new JSONObject(response); 
    	   JSONArray countryListObj = responseObj.getJSONArray("transit");
    	  
    	   for (int i=0; i<countryListObj.length(); i++)
    	   {

    	    String countryInfo = countryListObj.getJSONObject(i).toString();
    	    //create java object from the JSON object
    	    Transit_record jsondata = gson.fromJson(countryInfo, Transit_record.class);
    	   
    	    shipment_no.setText(jsondata.getShipmentno());
    	    shipment_date_time.setText(jsondata.getDate_time());
    	    truck_type.setText(jsondata.getTruck_type());
    	    truck_no.setText(jsondata.getTruckNo());
    	    driver_name.setText(jsondata.getDriverName());
    	    mobile_no.setText(jsondata.getDriverMobileNo());
    	    origin.setText(jsondata.getFrom());
    	    destination.setText(jsondata.getTo());
    	    distance.setText(jsondata.getDistance());
    		time.setText(jsondata.getTime());
    		myeta.setText(jsondata.getETA());
    		f_bill.setText(jsondata.getBookingDate());
    
    	   }

    	  } 
    	  catch (JSONException e) 
    	  {
    	   e.printStackTrace();
    	  }
    }
    	 
    	 
   
    class Transit_record
    {
    	 public String getShipmentno() {
			return shipmentno;
		}
		public String getDate_time() {
			return date_time;
		}
		public String getTruckNo() {
			return truckno;
		}
		public String getTruck_type() {
			return truck_type;
		}
		public String getFrom() {
			return from;
		}
		public String getTo() {
			return to;
		}
		public String getETA() {
			return ETA;
		}
		public String getDistance() {
			return distance;
		}
		public String getTime() {
			return time;
		}
		public String getDriverName() {
			return DriverName;
		}
		public String getDriverMobileNo() {
			return DriverMobileNo;
		}
		public String getBookingDate() {
			return freightbill;
		}
	 	private String shipmentno=null;   	
    	 private String date_time=null;
    	 private String truckno=null;
    	 private String truck_type=null;
    	 private String DriverName=null;
    	 private String DriverMobileNo=null;
    	 private String from=null;
    	 private String to=null;
    	 private String distance=null;
    	 private String time=null;
    	 private String ETA=null;
    	 private String freightbill=null;
    	
    } 
    	   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                   // Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

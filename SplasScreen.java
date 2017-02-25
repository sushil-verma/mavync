package com.example.sushilverma.mavync;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// The splash Screen reads the value from the file and launches the activity that is last stopped by mistake
public class SplasScreen extends AppCompatActivity {


	private SQLiteDatabase myDataBase;

	private final int SPLASH_DISPLAY_LENGTH = 3000;
	private Intent first_activity;
	private Intent second_activity;// not used
	private Intent third_activity;
	private Intent forth_activity;
	private Intent fifth_activity;

	private int loginstatus_value=0;
	private  Intent myservice;
	private boolean isGPSEnabled=true;
	private ArrayList<String> citylist=new ArrayList(30);

	private Download_Vehicle_category hit_category;
	private Download_Vehicle_categoryclose hit_categoryclose;
	private Download_First_Open_Vehicle hit_vehicle_open;
	private Download_First_Close_Vehicle hit_vehicle_close;
	private DownloadState citydownload;
	//private Context context=getApplicationContext();


	private static volatile String first_open_v_id=null;
	private static volatile String first_close_v_id=null;

	//open all databases


	private City_list_Dbhlper city_db;
	private Open_Catrgy_Dbhlper open_ctrgy_db;
	private Open_vehicle_Db open_ctrgy_vehicle_db;

	private Cls_Catrgy_Dbhlper closed_ctrgy_db;
	private Close_vehicle_Db close_ctrgy_vehicle_db;
	private Context context;
	private ProgressDialog dialog ;

	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	public static final String MyPREFERENCES = "MyPrefs" ;
	private String userid=null;
	private Timer timer;
	private mycallable callab;
	private Thread callabtask;


    // variable for the profile module



	@Override
	public void onPause()
	{
	   super.onPause();
	   //finish();
	}
	
	private void writestateToInternalStorage() 
	{
            byte b=0;
	        try {
	       
	        FileOutputStream fos = openFileOutput("loginstatus.txt", Context.MODE_PRIVATE);
	   
	        fos.write(b);
	        fos.close();
	        
	            } 
	        catch (Exception e) 
	        {
	         Log.i("Error_In_login", e.getMessage());
	      
	        }
	 }


	@Override
	protected void onStart() {
		super.onStart();
		context=getApplicationContext();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);


		closed_ctrgy_db=new Cls_Catrgy_Dbhlper(SplasScreen.this);
		open_ctrgy_vehicle_db=new Open_vehicle_Db(SplasScreen.this);
		close_ctrgy_vehicle_db=new Close_vehicle_Db(SplasScreen.this);

		city_db=new City_list_Dbhlper(SplasScreen.this);

		open_ctrgy_db=new Open_Catrgy_Dbhlper(SplasScreen.this);


		if(closed_ctrgy_db.numberOfRows()>0)
			closed_ctrgy_db.deletetb();

		if(city_db.numberOfRows()>0)
			city_db.deletetb();


		if(open_ctrgy_db.numberOfRows()>0)
			open_ctrgy_db.deletetb();



		if(open_ctrgy_vehicle_db.numberOfRows()>0)
			open_ctrgy_vehicle_db.deletetb();


		if(close_ctrgy_vehicle_db.numberOfRows()>0)
			close_ctrgy_vehicle_db.deletetb();




		first_activity = new Intent(this,LoginActivity.class);
		second_activity = new Intent(this,HomeActivity.class);

		second_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		third_activity = new Intent(this,OrderActivity.class);

		third_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		forth_activity = new Intent(this,ConfirmShipment.class);

		forth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		fifth_activity = new Intent(this,UserRegister.class);

		fifth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

		// calling this function to write 0 to the status file so as to start the app from zero.
		// writestateToInternalStorage();

		// overridePendingTransition(R.anim.view_show123,0);
		//overridePendingTransition(R.anim.vanish,0);

	    try 
	    {
			loginstatus_value=ReadstatusFromInternalStorage();
		} 
	    
	    catch (FileNotFoundException e)
		{

		}


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                hit_category = new Download_Vehicle_category(context);

                Thread open_category = new Thread(hit_category);
                open_category.start();
                try {
                    open_category.join();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                hit_categoryclose = new Download_Vehicle_categoryclose(context);
                Thread close_category = new Thread(hit_categoryclose);
                close_category.start();
                try {
                    close_category.join();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }


                hit_vehicle_open = new Download_First_Open_Vehicle(SplasScreen.this);

                // temporarly these hits are closed on trial mode

                Thread open_vehucle = new Thread(hit_vehicle_open);
                open_vehucle.start();
                try {
                    open_vehucle.join();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

				hit_vehicle_close = new Download_First_Close_Vehicle(SplasScreen.this);

				Thread close_vehucle = new Thread(hit_vehicle_close);
				close_vehucle.start();
				try {
					close_vehucle.join();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}



		DownloadState c = new DownloadState(getApplicationContext());

					Thread t = new Thread(c);
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				        if(loginstatus_value==1) {

							startActivity(first_activity);
							finish();

						}


				  else
						if(!Checkpendingrating())
				        {
							startActivity(second_activity);
							finish();

						}


			}
		},3000);

		                                                                                              ;
	  }











    private void writeUrl_addressToInternalStorage(String localurl)
    {
        String test=localurl;
        try {

            FileOutputStream fos = openFileOutput("url1.txt", Context.MODE_PRIVATE);

            fos.write(test.getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            Log.e("Error_In_status_saveTo", e.getMessage());

        }
    }


    private String ReadulrFromInternalStorage()
    {

        String urllocal=null;
        StringBuffer buffer = new StringBuffer();

        try{

            FileInputStream fis = openFileInput("url1.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            if (fis!=null)
                urllocal = reader.readLine();

            fis.close();

        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return urllocal;
    }
	
	
	 private int ReadstatusFromInternalStorage() throws FileNotFoundException 
	    {
         
	    	int status=0;
	    	FileInputStream fos = openFileInput("loginstatus.txt");
	        
	        try {
	        	status=fos.read();
				fos.close();
			    } catch (IOException e) {
				
			    	e.printStackTrace();
		       }
	        
	        return status;
	      }
	 
	   boolean isOnline() 
	    {
		    ConnectivityManager cm =
		                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    return netInfo != null && netInfo.isConnectedOrConnecting();
		}
	   
	   
	   boolean isGpson()
	   {
		   LocationManager  locationManager=(LocationManager)this.getSystemService(LOCATION_SERVICE);
		   isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		   return isGPSEnabled;
	   }



	private class DownloadState implements Runnable
	{
		Context localcontext;

		private String local;
		private BufferedReader bufferinput =null;
		private HttpURLConnection con=null;
		private ArrayList<String> citylistlocal=new ArrayList(30);
		JSONObject responseObj;

		public DownloadState(Context c)
		{

		}


		ArrayList<String> getlistdata()
		{
			return citylistlocal;
		}


		@Override
		public void run() {

			try
			{

				URL url = new URL("http://121.241.125.91/cc/mavyn/online/customerfindlocation.php?msg=location");
				con = (HttpURLConnection) url.openConnection();
				bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
				try
				{
					local = bufferinput.readLine();
					System.out.println("json data"+local);
				}
				catch (Exception e)
				{

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

			try {


				responseObj = new JSONObject(local);
				JSONArray countryListObj = responseObj.optJSONArray("location");  //.getJSONArray("transit");

				int length=countryListObj.length();

				System.out.println("json array length="+length);

				for (int i=0; i<length; i++)
				{

					JSONObject jsonChildNode=countryListObj.getJSONObject(i);


					String Citydata=null;
					String lat=null;
					String lon=null;


					try {

						Citydata = jsonChildNode.optString("city").toString();
						lat=jsonChildNode.optString("lat").toString();
						lon=jsonChildNode.optString("long").toString();

					    }

					catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//citylistlocal.add(Citydata);
					city_db.insertContact(Citydata,lat,lon,String.valueOf(i+1));

				}

				//city_db.insertContact("naraina","12.232323","12.32323");

			}
			catch (JSONException e)
			{
				e.printStackTrace();
				System.out.println("Error in reading json object");
			}

		}

	}


	private class Download_Vehicle_category implements Runnable
	{
		Context localcontext;
		private String local=null;
		BufferedReader bufferinput =null;
		HttpURLConnection con=null;
		URL url=null;

		public Download_Vehicle_category(Context c)
		{
			localcontext=c;

			try

			{

				url = new URL("http://121.241.125.91/cc/mavyn/online/customerloginafter.php?msg=open");
			}

			catch(IOException e)
			{
				e.printStackTrace();

			}
		}


		@Override
		public void run() {

			try
			{


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

			Display_Vehicle_list_category(local);
	    	}

		}


	private void Display_Vehicle_list_category(String response){

		JSONObject responseObj;



		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("categorydata");  //.getJSONArray("transit");

			int length=countryListObj.length();

			System.out.println("json array length="+length);


			for (int i=0; i<length; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);


				String weight=null;
				String categoryname=null;
				String categoryid=null;

				try {


					weight = jsonChildNode.optString("weight").toString();
					categoryname =jsonChildNode.optString("categoryname").toString();
					categoryid=jsonChildNode.optString("categoryid").toString();

				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// store the vehicle id of first open category vehicle type to be downloaded
				if(i==0)
					first_open_v_id=categoryid;

				open_ctrgy_db.insertCategry(categoryid,categoryname,weight);

			}



		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

		//open_ctrgy_db.close();

	}



	private class Download_Vehicle_categoryclose implements Runnable
	{

		Context localcontext;
		private String local=null;
		BufferedReader bufferinput =null;
		HttpURLConnection con=null;
		URL url=null;

		public Download_Vehicle_categoryclose(Context c)
		{
			localcontext=c;

			try

			{

				url = new URL("http://121.241.125.91/cc/mavyn/online/customerloginafter.php?msg=Closed");
			}

			catch(IOException e)
			{
				e.printStackTrace();

			}
		}


		@Override
		public void run() {

			try
			{


				con = (HttpURLConnection) url.openConnection();
				bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
				try
				{
					local = bufferinput.readLine();

				}
				catch (Exception e)
				{

					e.printStackTrace();
				}


			}

			catch(IOException e)
			{
				e.printStackTrace();

			}

			try
			{
				bufferinput.close();
				con.disconnect();
			}
			catch (IOException e)
			{

				e.printStackTrace();
			}

			Display_Vehicle_list_categoryclose(local);
		}




	}


	private void Display_Vehicle_list_categoryclose(String response){

		JSONObject responseObj;



		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("categorydata");  //.getJSONArray("transit");

			int length=countryListObj.length();

			System.out.println("json array length="+length);

			for (int i=0; i<length; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);


				String weight=null;
				String categoryname=null;
				String categoryid=null;

				try {

					weight = jsonChildNode.optString("weight").toString();
					categoryname =jsonChildNode.optString("categoryname").toString();
					categoryid=jsonChildNode.optString("categoryid").toString();
				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(i==0)
					first_close_v_id=categoryid;

				closed_ctrgy_db.insertCategry(categoryid,categoryname,weight);

			}



		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}


	private class Download_First_Open_Vehicle implements  Runnable
	{

		Context localcontext;
		private String local=null;
		BufferedReader bufferinput =null;
		HttpURLConnection con=null;
		URL url=null;

		public Download_First_Open_Vehicle(Context c)
		{
			localcontext=c;

			try

			{

				url = new URL("http://121.241.125.91/cc/mavyn/online/customerloginafter.php?vehicletype=opened&v_id="+first_open_v_id);
			}

			catch(IOException e)
			{
				e.printStackTrace();

			}
		}


		@Override
		public void run() {

			try
			{


				con = (HttpURLConnection) url.openConnection();
				bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
				try
				{
					local = bufferinput.readLine();

				}
				catch (Exception e)
				{

					e.printStackTrace();
				}


			}

			catch(IOException e)
			{
				e.printStackTrace();

			}

			try
			{
				bufferinput.close();
				con.disconnect();
			}
			catch (IOException e)
			{

				e.printStackTrace();
			}

			Display_Vehicle_list_open(local);
		}



	}


	private void Display_Vehicle_list_open(String response){

		JSONObject responseObj;



		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");

			int length=countryListObj.length();

			System.out.println("json array length="+length);

			if(length==0)
			{
				// Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
			/*	Toast toast = Toast.makeText(this,"No Vehicle Available.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.setView(layout);
				toast.show();*/
			}



			for (int i=0; i<length; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);


				// String id=null;
				String longitute=null;
				String latitute=null;
				String description=null;

				try {


					// id = jsonChildNode.optString("id").toString();
					latitute=jsonChildNode.optString("latitute").toString();
					longitute =jsonChildNode.optString("longitude").toString();
					description=jsonChildNode.optString("description").toString();


				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				open_ctrgy_vehicle_db.insertCategry(latitute,longitute,description);

			}




		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}


	private class Download_First_Close_Vehicle implements Runnable
	{
		Context localcontext;
		private String local=null;
		BufferedReader bufferinput =null;
		HttpURLConnection con=null;
		URL url=null;

		public Download_First_Close_Vehicle(Context c)
		{
			localcontext=c;

			try

			{

				url = new URL("http://121.241.125.91/cc/mavyn/online/customerloginafter.php?vehicletype=closed&v_id="+first_close_v_id);
			}

			catch(IOException e)
			{
				e.printStackTrace();

			}
		}


		@Override
		public void run() {

			try
			{


				con = (HttpURLConnection) url.openConnection();
				bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
				try
				{
					local = bufferinput.readLine();

				}
				catch (Exception e)
				{

					e.printStackTrace();
				}


			}

			catch(IOException e)
			{
				e.printStackTrace();

			}

			try
			{
				bufferinput.close();
				con.disconnect();
			}
			catch (IOException e)
			{

				e.printStackTrace();
			}

			Display_Vehicle_list_close(local);


		}

	}


	private void Display_Vehicle_list_close(String response){

		JSONObject responseObj;



		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");

			int length=countryListObj.length();

			System.out.println("json array length="+length);

			if(length==0)
			{
				/*// Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
				Toast toast = Toast.makeText(this,"No Vehicle Available.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.setView(layout);
				toast.show();*/
			}



			for (int i=0; i<length; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);



				String longitute=null;
				String latitute=null;
				String description=null;

				try {



					latitute=jsonChildNode.optString("latitute").toString();
					longitute =jsonChildNode.optString("longitude").toString();
					description=jsonChildNode.optString("description").toString();


				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				close_ctrgy_vehicle_db.insertCategry(latitute,longitute,description);

			}


		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}

	/*private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){

			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}
*/

	private boolean Checkpendingrating()
	{


        boolean rating_status=false;
		sharedpreferences = getSharedPreferences(MyPREFERENCES,SplasScreen.MODE_PRIVATE);
		userid= sharedpreferences.getString("customer_id", "null");

		if( isOnline())
		{

			callab=new mycallable(userid);
			callabtask=new Thread(callab);
			callabtask.start();
			try {
				callabtask.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Thread Interrupted");
			}
			Log.i("data sent...", " ");


			int statusformobile=callab.getstatusformobile();
			if(statusformobile ==1)

			{
				String shipment_id = callab.getRating_shipment_id();
				String from = callab.getFrom();
				String to = callab.getTo();
				String date = callab.getDelivery_date();


				Intent myrating_screen=new Intent(this,Rating_popup.class);
				myrating_screen.putExtra("shipment_id",shipment_id);
				myrating_screen.putExtra("from",from);
				myrating_screen.putExtra("to",to);
				myrating_screen.putExtra("date",date);


				myrating_screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myrating_screen);

				rating_status=true;

			}

		}

		return rating_status;

	}

	class mycallable implements Runnable
	{

		private volatile String  shipment_id=null;
		private volatile String   customer_id=null;
		private volatile String  delivery_date=null;
		private volatile String  from=null;
		private volatile String to=null;
		private volatile int statusformobile =0;


		public int getstatusformobile() {
			return statusformobile;
		}

		public String getDelivery_date() {
			return delivery_date;
		}


		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public String getRating_shipment_id()

		{
			return shipment_id;
		}


		mycallable(String customer_id)
		{
			this.customer_id=customer_id;
		}

		public void run()
		{

			try {

				final String request="http://121.241.125.91/cc/mavyn/online/customerapp.php?rating=ratingpending"+"&"+"userid="+customer_id;
				JSONObject jsonResponse;
				Log.d("request", ""+request);

				URL url = new URL(request);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();

				System.out.println("data uploades");

				BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
				String local=null;
				try
				{
					local =br.readLine();
					Log.i("Json data received..",local);
				}

				catch (Exception e)
				{

					// Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
				}

				System.out.println("the recived url"+local);

				try {
					jsonResponse = new JSONObject(local);

					JSONArray jsonMainNode=jsonResponse.optJSONArray("ratingpending");

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);

					statusformobile=Integer.parseInt(jsonChildNode.optString("statusformobile").toString());

					if(statusformobile==1)
					{

						shipment_id=jsonChildNode.optString("shipment_no").toString();
						from=jsonChildNode.optString("from_location").toString();
						to=jsonChildNode.optString("to_location").toString();
						delivery_date=jsonChildNode.optString("stagesupdate_date").toString();

					 }




				}

				catch(JSONException js)
				{
					js.printStackTrace();
				}

				br.close();
				con.disconnect();



			}

			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}

			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}

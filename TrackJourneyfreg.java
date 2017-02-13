package com.example.sushilverma.mavync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackJourneyfreg extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnItemSelectedListener, OnMarkerClickListener {

   Context context;
   private Confirmd_Dbhlper db;
   private static ArrayList<Cnfrmd_Vehicle_record> Cnfrmd_Vehicl_data=new ArrayList<Cnfrmd_Vehicle_record>(10);
   private View view;
   MapView mMapView;
   private static volatile GoogleMap googleMap;
   private List<String>  vehicle_no_arraylist=new ArrayList<String>(); 
   private List<String>  vehicle_id_arraylist=new ArrayList<String>();
   private TextView pickupaddress,destinationaddress;
   private Marker driver_marker;
   private SharedPreferences sharedpreferences;
   private SharedPreferences.Editor editor;
   public static final String MyPREFERENCES = "MyPrefs" ;
   private String userid=null;
   private String url;  // this is for source and destination bullo n url
   private String url1; // this is for all lat long of moving vehicle
   private double source_lat=0.0;
   private double source_long=0.0;
   private int size=0;
   private	LinearLayout to_from;
	private LinearLayout vehicle_flag;
   private Download_vehicle_number	download_vehicle_no;
   
   // polyline declaration
   private ArrayList<LatLng> points = null;
   private	PolylineOptions lineOptions = null;
   

   

	 @Override
     public void onViewCreated(View view, Bundle savedInstanceState) 
	 {
		  super.onViewCreated(view, savedInstanceState);
		
	 }

	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	 {
	        super.onCreate(savedInstanceState);

		   /* db=new Confirmd_Dbhlper(context);
		 // if database is empty download from server or use database to show vehicle no in the spinner to be fast performance

	        if(db.numberOfRows()>0)
			{
				vehicle_no_arraylist=db.getAllVehicle_no();
				vehicle_id_arraylist=db.getVehicleid();
			}

		 else*/

			{
				download_vehicle_no=new Download_vehicle_number(context);
				url ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=track"+"&userid="+userid+"&vehicleno=";
				download_vehicle_no.execute();



			}

	      
	 }



 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
    {


		view = inflater.inflate(R.layout.journeyfregment, container, false);
		mMapView = (MapView) view.findViewById(R.id.journeymap);
		mMapView.onCreate(savedInstanceState);

		// comment on 12/1/2016

		to_from=(LinearLayout)view.findViewById(R.id.to_from);
		to_from.setVisibility(View.INVISIBLE);



		vehicle_flag=(LinearLayout)view.findViewById(R.id.vehicle_flag_jrny);
		vehicle_flag.setVisibility(View.INVISIBLE);

		Spinner spinner = (Spinner)view.findViewById(R.id.trucklist);

		pickupaddress=(TextView)view.findViewById(R.id.livepickuppoint);
		destinationaddress=(TextView)view.findViewById(R.id.livedestinationppoint);

        // temperorly blocked for testing reason

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.spinnertextbox, vehicle_no_arraylist);

		adapter.setDropDownViewResource(R.layout.spinnertextboxdropdown);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
     
   
     try {
         MapsInitializer.initialize(getActivity().getApplicationContext());
     } catch (Exception e) {
         e.printStackTrace();
     }
     mMapView.getMapAsync(this);

     return view;
    }
 
     
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       
      /* sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       userid= sharedpreferences.getString("customer_id", "null");

       */
		// for polylines
       
         points = new ArrayList<LatLng>(100);
		 lineOptions = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);
     
    }

     
   
    
    
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
  
  @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
  
  @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {



		   	String vehicle_id=vehicle_id_arraylist.get(position);

		   // this hit is for gettng source and destination address
		   url ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=track"+"&userid="+userid+"&vehicleno="+vehicle_id;
		   Datadownload Excecute=new Datadownload(context);
		   Excecute.execute(url);
		   
		   // this hit is for moving vehicle tracking that is longitute and latitute
		   url1 ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=track1"+"&userid="+userid+"&vehicleno="+vehicle_id;
		   Datadownload1 Excecute1=new Datadownload1(context);
		   Excecute1.execute(url1);

		/*  putmarkers("28.4472608","77.49970436");
		    putmarkers("28.43895909","77.50451088");
		    putmarkers("28.43126056","77.50880241");
		    putmarkers("28.42250468","77.51395226");
		    putmarkers("28.41012445","77.51875877");
		    putmarkers("28.39049429","77.52940178");
		*/
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onMapReady(GoogleMap gMap) {

		googleMap=gMap;


		// commented on 12/1/17

	/*	if(vehicle_id_arraylist.size()==0)

		{

			vehicle_flag.setVisibility(View.VISIBLE);
		}*/
		googleMap.setOnMarkerClickListener(this);

	}


	private class Datadownload extends AsyncTask<String, Void, String>
	{
		Context localcontext;
		private ProgressDialog dialog ;
		private String local="abc";

		public Datadownload(Context c)
		{
			localcontext=c;
		}

		protected void onPreExecute()
		{
			dialog = new ProgressDialog(localcontext);
			dialog.setMessage("Getting your data... Please wait...");
			dialog.show();
		}


		protected String doInBackground(String... urls)
		{

			BufferedReader bufferinput =null;
			HttpURLConnection con=null;

			try {

				URL url = new URL(urls[0]);
				con = (HttpURLConnection) url.openConnection();
				bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
				try {
					local = bufferinput.readLine();
					System.out.println("json data" + local);
				} catch (Exception e) {

					System.out.println("json data not fetched");
				}

				Log.i("Json data received..", local);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.i("Erro", "data downloading erro");

			} catch (IOException e) {
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
			JSONArray countryListObj = responseObj.optJSONArray("tracking");  //.getJSONArray("transit");

			int length=countryListObj.length();

			System.out.println("json array length="+length);

			for (int i=0; i<length; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);

				String myaddress=jsonChildNode.optString("address").toString();
				double destination_latitute=0.0;
				double destination_longitute=0.0;
				try {
					destination_latitute = Double.valueOf(jsonChildNode.optString("lat").toString());
					destination_longitute = Double.valueOf(jsonChildNode.optString("long").toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(i==0)
				{

					pickupaddress.setText(myaddress) ;
					driver_marker= googleMap.addMarker(new MarkerOptions().position(new LatLng(destination_latitute,destination_longitute)).icon(BitmapDescriptorFactory.fromResource(R.drawable.gbaloon)));

					source_lat=destination_latitute;
					source_long=destination_longitute;
				}
				else
				{

					destinationaddress.setText(myaddress);
					driver_marker= googleMap.addMarker(new MarkerOptions().position(new LatLng(destination_latitute,destination_longitute)).icon(BitmapDescriptorFactory.fromResource(R.drawable.rbaloon)));

				}

			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}


	private class Datadownload1 extends AsyncTask<String, Void, String>
	{
		Context localcontext;
		private ProgressDialog dialog ;
		private String local="abc";

		public Datadownload1(Context c)
		{
			localcontext=c;
		}

		protected void onPreExecute()
		{
			dialog = new ProgressDialog(localcontext);
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

			displayCountryList1(content);

			dialog.dismiss();
		}

	}


	private void displayCountryList1(String response){

		JSONObject responseObj;
		LatLng latlong0=null;
		LatLng templatlng2=null;
		LatLng templatlng1=null;


		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("tracking1");  //.getJSONArray("transit");

			int length=countryListObj.length();
			int x=length-1;

			System.out.println("json array length="+length);

			for (int i=0; i<length-1; i++)
			{
				JSONObject jsonChildNode=countryListObj.getJSONObject(i);
				double destination_latitute=0.0;
				double destination_longitute=0.0;
				String address=null;
				String date_time=null;
				try {

					destination_latitute = Double.valueOf(jsonChildNode.optString("lat").toString());
					destination_longitute = Double.valueOf(jsonChildNode.optString("long").toString());
				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				templatlng1=new LatLng(destination_latitute,destination_longitute);
				points.add(templatlng1);

			}

			try {
				lineOptions.addAll(points);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			googleMap.addPolyline(lineOptions);
			driver_marker= googleMap.addMarker(new MarkerOptions().position(templatlng1).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));
			CameraPosition cameraPosition = new CameraPosition.Builder().target(templatlng1).zoom(13.0f).build();
			CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
			googleMap.moveCamera(cameraUpdate);

		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}


	private class Download_vehicle_number extends AsyncTask<String, Void, String>
	{
		Context localcontext;
		private ProgressDialog dialog ;
		private String local="abc";

		public Download_vehicle_number(Context c)
		{
			localcontext=c;
		}

		protected void onPreExecute()
		{
			dialog = new ProgressDialog(localcontext);
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

			getjson_vehicle_no(content);

			dialog.dismiss();
		}

	}


	private void getjson_vehicle_no(String response){

		JSONObject responseObj;
		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("vehicle_no");  //.getJSONArray("transit");

			int length=countryListObj.length();
			if (length>0) {
				to_from.setVisibility(View.VISIBLE);

				vehicle_flag.setVisibility(View.VISIBLE);

			}

			System.out.println("json array length="+length);

			for (int i=0; i<length-1; i++)
			{

				JSONObject jsonChildNode=countryListObj.getJSONObject(i);
				String id=null;
				String vehicle_no=null;

				try
				{

					id =jsonChildNode.optString("id").toString();
					vehicle_no = jsonChildNode.optString("vehicle_no").toString();
				}

				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				vehicle_no_arraylist.add(vehicle_no);
				vehicle_id_arraylist.add(id);

				//db.insertContact(id,vehicle_no);

			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}


	}



	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}





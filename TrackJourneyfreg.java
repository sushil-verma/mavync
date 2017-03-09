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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import static android.content.Context.MODE_PRIVATE;

public class TrackJourneyfreg extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,  OnMarkerClickListener {

   private Context context;
   private Confirmd_Dbhlper db;
   private static ArrayList<Cnfrmd_Vehicle_record> Cnfrmd_Vehicl_data=new ArrayList<Cnfrmd_Vehicle_record>(10);
   private View view;
   private MapView mMapView;
   private static volatile GoogleMap googleMap;
   private List<String>  vehicle_no_arraylist=new ArrayList<String>(); 
   private List<String>  vehicle_id_arraylist=new ArrayList<String>();
   private TextView pickupaddress,destinationaddress;
   private Marker driver_marker;

   private String userid=null;
   private String url;  // this is for source and destination bullo n url
   private String url1; // this is for all lat long of moving vehicle
   private double source_lat=0.0;
   private double source_long=0.0;
   private int size=0;
   private	LinearLayout to_from;
	private LinearLayout vehicle_flag;

   // polyline declaration
   private ArrayList<LatLng> points = null;
   private	PolylineOptions lineOptions = null;
	private static volatile MaterialBetterSpinner vehiclelist;
	private ArrayAdapter<String> spinneradapter;
     private Confirm_vehicle_no_Db vehiclenodatabase=null;
	public static final String MyPREFERENCES = "U_id" ;

	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 context= getActivity();

		 vehiclenodatabase=new Confirm_vehicle_no_Db(getActivity());

		 vehicle_no_arraylist.addAll(vehiclenodatabase.getAlldata());

		 points = new ArrayList<LatLng>(100);
		 lineOptions = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);


	 }



 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
    {


		view = inflater.inflate(R.layout.journeyfregment, container, false);
		mMapView = (MapView) view.findViewById(R.id.journeymap);
		mMapView.onCreate(savedInstanceState);

		// comment on 12/1/2016

		to_from=(LinearLayout)view.findViewById(R.id.to_from);
		//to_from.setVisibility(View.INVISIBLE);



		vehicle_flag=(LinearLayout)view.findViewById(R.id.vehicle_flag_jrny);



		vehiclelist = (MaterialBetterSpinner) view.findViewById(R.id.vehiclelist);
		if(vehicle_no_arraylist.size()<=0) {
			vehicle_flag.setVisibility(View.VISIBLE);
			vehiclelist.setVisibility(View.INVISIBLE);
		  }
		else
			vehicle_flag.setVisibility(View.INVISIBLE);


		pickupaddress=(TextView)view.findViewById(R.id.livepickuppoint);
		destinationaddress=(TextView)view.findViewById(R.id.livedestinationppoint);

		spinneradapter = new ArrayAdapter<String>(getActivity(),R.layout.spinnertextbox, vehicle_no_arraylist);

		spinneradapter.setDropDownViewResource(R.layout.spinnertextboxdropdown);
		vehiclelist.setAdapter(spinneradapter);
		vehiclelist.setHint("Select vehicle");

		vehiclelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				String vehicle_no=vehicle_no_arraylist.get(arg2);

				// this hit is for gettng source and destination address
				url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=track"+"&userid="+getActivity().getSharedPreferences(MyPREFERENCES,SplasScreen.MODE_PRIVATE).getString("userid", "null") +"&vehicleno="+vehicle_no;
				Download_pickup_destination Excecute=new Download_pickup_destination(context);
				Excecute.execute(url);

				// this hit is for moving vehicle tracking that is longitute and latitute
				url1 ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=track1"+"&userid="+getActivity().getSharedPreferences(MyPREFERENCES,SplasScreen.MODE_PRIVATE).getString("userid", "null")+"&vehicleno="+vehicle_no;
				DownloadPollylines Excecute1=new DownloadPollylines(context);
				Excecute1.execute(url1);

			}
		});

     
   
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
    public void onLowMemory() {        super.onLowMemory();
        mMapView.onLowMemory();
    }

	/*@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {



		  String vehicle_no=vehicle_no_arraylist.get(position);

		   // this hit is for gettng source and destination address
		   url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=track"+"&userid="+userid+"&vehicleno="+vehicle_no;
		   Download_pickup_destination Excecute=new Download_pickup_destination(context);
		   Excecute.execute(url);
		   
		   // this hit is for moving vehicle tracking that is longitute and latitute
		   url1 ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=track1"+"&userid="+userid+"&vehicleno="+vehicle_no;
		   DownloadPollylines Excecute1=new DownloadPollylines(context);
		   Excecute1.execute(url1);

		*//*  putmarkers("28.4472608","77.49970436");
		    putmarkers("28.43895909","77.50451088");
		    putmarkers("28.43126056","77.50880241");
		    putmarkers("28.42250468","77.51395226");
		    putmarkers("28.41012445","77.51875877");
		    putmarkers("28.39049429","77.52940178");
		*//*


	}*/


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


		googleMap.setOnMarkerClickListener(null);

	}


	private class Download_pickup_destination extends AsyncTask<String, Void, String>
	{
		Context localcontext;
		private ProgressDialog dialog ;
		private String local="abc";

		public Download_pickup_destination(Context c)
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


		protected void onPostExecute(String content)
		{
			// dialog.dismiss();

			displayCountryList(content);

			dialog.dismiss();
		}

	}


	private void displayCountryList(String response){

		JSONObject responseObj;
		double destination_latitute=0.0;
		double destination_longitute=0.0;


		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("tracking");  //.getJSONArray("transit");

			int length=countryListObj.length();

			if(length>0)
			{

				/*to_from.setVisibility(View.VISIBLE);

				vehicle_flag.setVisibility(View.VISIBLE);
*/
				for (int i=0; i<length; i++)
				{

					JSONObject jsonChildNode=countryListObj.getJSONObject(i);

					String myaddress=jsonChildNode.optString("address").toString();

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

				CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(destination_latitute,destination_longitute)).zoom(10.0f).build();
				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
				googleMap.moveCamera(cameraUpdate);


			}



		}
		catch (JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}


	private class DownloadPollylines extends AsyncTask<String, Void, String>
	{
		Context localcontext;
		private ProgressDialog dialog ;
		private String local="abc";

		public DownloadPollylines(Context c)
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

			if(x>0)
			{

				for (int i=0; i<length-1; i++)
				{
					JSONObject jsonChildNode=countryListObj.getJSONObject(i);
					double destination_latitute=0.0;
					double destination_longitute=0.0;
					String address=null;
					String date_time=null;
					destination_latitute = Double.valueOf(jsonChildNode.optString("lat").toString());
					destination_longitute = Double.valueOf(jsonChildNode.optString("long").toString());
					templatlng1=new LatLng(destination_latitute,destination_longitute);
					points.add(templatlng1);

				}
				/*points.add(new LatLng(28.4472608,77.49970436));
				points.add(new LatLng(28.43895909,77.50451088));
				points.add(new LatLng(28.43126056,77.50880241));
				points.add(new LatLng(28.42250468,77.51395226));
				points.add(new LatLng(28.41012445,77.51875877));
				points.add(new LatLng(28.39049429,77.52940178));
*/


				lineOptions.addAll(points);
				googleMap.addPolyline(lineOptions);
				driver_marker= googleMap.addMarker(new MarkerOptions().position(templatlng1).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));
				CameraPosition cameraPosition = new CameraPosition.Builder().target(templatlng1).zoom(10.0f).build();
				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
				googleMap.moveCamera(cameraUpdate);

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


	/*private void initDataset() {

		String userid=getActivity().getSharedPreferences("U_id", MODE_PRIVATE).getString("userid", null);
		String url ="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?massg=trackalllist&userid="+userid;



		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {

				Parsejsonvehicle(s);

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

	}*/



	/*private void Parsejsonvehicle(String response) {

		JSONObject responseObj;


		try {


			responseObj = new JSONObject(response);
			JSONArray countryListObj = responseObj.optJSONArray("currentstatus");

			int length = countryListObj.length();


			if (length != 0) {



				for (int i = 0; i < length; i++) {

					JSONObject jsonChildNode = countryListObj.getJSONObject(i);
					String vehicleno = jsonChildNode.optString("Vehicleno");
					vehicle_no_arraylist.add(vehicleno);


				}

				spinneradapter.notifyDataSetChanged();

			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			System.out.println("Error in reading json object");
		}

	}*/

}







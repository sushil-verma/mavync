package com.example.sushilverma.mavync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Trackingmapview extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private View view;
    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Download_Vehicle hit_vehicle;
    private Marker driver_marker;
    private Location location;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private LayoutInflater inflater;
    private View layout;
    private static MyMarker myMarker;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private LinearLayout vehicle_flag;


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*  inflater = getActivity().getLayoutInflater();
          layout = inflater.inflate(R.layout.toast_layout,(ViewGroup)getActivity().findViewById((R.id.custom_toast_container)));
        */

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMarkersHashMap = new HashMap<Marker, MyMarker>();

        hit_vehicle = new Download_Vehicle(getActivity());
        hit_vehicle.execute("http://121.241.125.91/cc/mavyn/online/customerloginafter.php?msg=trackall&userid=");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapfregment, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);

        vehicle_flag = (LinearLayout) view.findViewById(R.id.vehicle_flag);
        vehicle_flag.setVisibility(View.INVISIBLE);
        mMapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);


        return view;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        if (mMarkersHashMap.isEmpty()) {

            vehicle_flag.setVisibility(View.VISIBLE);


        }


    }


    private class Download_Vehicle extends AsyncTask<String, Void, String> {
        Context localcontext;
        private ProgressDialog dialog;
        private String local = "abc";

        public Download_Vehicle(Context c) {
            localcontext = c;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(localcontext);
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }


        protected String doInBackground(String... urls) {

            BufferedReader bufferinput = null;
            HttpURLConnection con = null;

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
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Erro", "data downloading erro");

            }

            try {
                bufferinput.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            con.disconnect();

            return local;
        }


        protected void onPostExecute(String content) {
            dialog.dismiss();

            Display_Vehicle_list(content);


        }

    }


    private void Display_Vehicle_list(String response) {

        JSONObject responseObj;

        double longitute = 0.0;
        double latitute = 0.0;
        String description = null;
        LatLng temp_latlong = null;

        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");
            int length = countryListObj.length();

            if (length > 0) {
                vehicle_flag.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);

                try {

                    longitute = Double.valueOf(jsonChildNode.optString("longitude").toString());
                    latitute = Double.valueOf(jsonChildNode.optString("latitute").toString());
                    description = jsonChildNode.optString("description").toString();

                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

           /*     mMyMarkersArray.add(new MyMarker(description));
                myMarker=new MyMarker(description);
                mydescription=description;
                my_map_vehicle.add(new Vehicle_Data(latitute,longitute,description));
*/
                temp_latlong = new LatLng(latitute, longitute);
                driver_marker = googleMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));
                mMarkersHashMap.put(driver_marker, new MyMarker(description));
                googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }


        try {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            MyMarker myMarker = mMarkersHashMap.get(marker);


            TextView markerLabel = (TextView) v.findViewById(R.id.vehiclelable);

            TextView anotherLabel = (TextView) v.findViewById(R.id.addresslabel);


            markerLabel.setText("HR4523");
            anotherLabel.setText(myMarker.getAddress());

            return v;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {


            longitude=location.getLongitude();
            latitude= location.getLatitude();
            //latlong=new LatLng(latitude, longitude);
           /* LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude,longitude,getActivity(), new GeocoderHandler());*/
            LatLng temp_latlong_local=new LatLng(latitude,longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong_local).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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



 
}

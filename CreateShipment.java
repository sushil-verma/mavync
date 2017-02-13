package com.example.sushilverma.mavync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.wobs.TimeInterval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class CreateShipment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private MapView m;
    private LinearLayoutManager layoutManager;

    private static volatile CategoryAdapter mycategoryadapter;
    private static volatile CategoryAdapterclose mycategoryadapterclose;
    private ArrayList<Vehicle_category> myvehicle_category = new ArrayList(50);
    private ArrayList<Vehicle_category> myvehicle_categoryclose = new ArrayList(50);
    private ArrayList<Vehicle_Data> my_map_vehicle = new ArrayList<Vehicle_Data>(50);
    private Marker driver_marker;
    private LatLng temp_latlong;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private static volatile MaterialBetterSpinner pick_up_address;
    private static volatile MaterialBetterSpinner destination_address;
    private RecyclerView recyclerView;
    private String url = null;
    private Button open_vehicle, closed_vehicle, book_shipment;
    private boolean checked = false;
    private Intent intent;
    private static volatile Vehicle_category vdata, vdataclose;
    private static volatile boolean open_button_presses = true;
    private static volatile boolean close_button_pressed = false;
    private LatLng latlong;
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    double longitude;
    double latitude;
    //these four variabls are as flag when user selece source and destination
    private static volatile String source = null;          // initilised at line no 428;
    private static volatile String truck_staus = "open";   // initilised at line no 498;
    private static volatile String destination = null;     // initilised at line no 447;
    private static volatile String v_id = null;            // initilised at line no 337;
    private View myLocationButton;

    private View v;
    private String fulladdress = null;
    private static volatile ArrayList<String> citystatelist = new ArrayList<>(100);
    private boolean destination_button_clicked = false;
    private Animation animShow, animHide;

    private static volatile String first_open_v_id = null;

    private TextView netconnection_hint;
    private LinearLayout header;
    private LinearLayout top;
    private boolean network_connection_status = true;
    private LayoutInflater inflater;
    private View layout;
    private ArrayAdapter<String> adapter;
    private Open_Catrgy_Dbhlper open_ctrgy_db;
    private Cls_Catrgy_Dbhlper closed_ctrgy_db;
    private City_state_Dbhlper city_list;
    //private City_state_Dbhlper city_db;

    private Open_Catrgy_vehicle_for_Dbhlper open_ctrgy_vehicle_db;


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        System.out.print("create called");
        Handler handler = new Handler();


        city_list = new City_state_Dbhlper(getActivity());
        open_ctrgy_db = new Open_Catrgy_Dbhlper(getActivity());

        closed_ctrgy_db = new Cls_Catrgy_Dbhlper(getActivity());


        mMarkersHashMap = new HashMap<Marker, MyMarker>();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        writestateToInternalStorage();

        citystatelist.addAll(city_list.getAlldata());

        myvehicle_category.addAll(open_ctrgy_db.getAlldata());

        myvehicle_categoryclose.addAll(closed_ctrgy_db.getAlldata());


     /*   ArrayList <String> temp=new ArrayList<String>(20);

        city_list.getAlldata();*/


        if (!isOnline()) {

            network_connection_status = false;
        } else

        {

            mycategoryadapter = new CategoryAdapter(myvehicle_category, getActivity());
            mycategoryadapter.setSelected(0);

            mycategoryadapter.notifyDataSetChanged();

            mycategoryadapterclose = new CategoryAdapterclose(myvehicle_categoryclose, getActivity());
            mycategoryadapterclose.setSelected(0);

            mycategoryadapterclose.notifyDataSetChanged();


        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
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


            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //latlong=new LatLng(latitude, longitude);
           /* LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude,longitude,getActivity(), new GeocoderHandler());*/
            LatLng temp_latlong_local = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong_local).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Show_No_net_off_msg();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Show_No_net_off_msg();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initAnimation();
        inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById((R.id.custom_toast_container)));
        v = inflater.inflate(R.layout.activity_create_shipment, container, false);
        m = (MapView) v.findViewById(R.id.map);

       /* MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                v.findViewById(R.id.android_material_design_spinner);

        materialDesignSpinner.setAdapter(arrayAdapter);*/


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        m.onCreate(savedInstanceState);
        m.onResume();
        m.getMapAsync(this);

        header = (LinearLayout) v.findViewById(R.id.header);
        top = (LinearLayout) v.findViewById(R.id.top);
        netconnection_hint = (TextView) v.findViewById(R.id.net_connection);


        pick_up_address = (MaterialBetterSpinner) v.findViewById(R.id.pick_up_address);
        pick_up_address.setHint("Select Origin District");

        if (network_connection_status) {

            Collections.sort(citystatelist);

            // String [] city=new String[20];
            // String [] city={"ram","shyam"};

            //   city=citystatelist.toArray( );


            adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertextbox, citystatelist);

            adapter.setDropDownViewResource(R.layout.spinnertextboxdropdown);

            pick_up_address.setAdapter(adapter);

            pick_up_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    source = (String) arg0.getItemAtPosition(arg2);

                    if (network_connection_status) {

                        // String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?bodytype=" + truck_staus + "&vehicletype=" + 9 + "&flocation=" + source;
                        String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?bodytype=" + truck_staus + "&vehicletype=" + v_id + "&flocation=" + source;
                        UpdateMap updateMapfirst = new UpdateMap(getActivity());
                        updateMapfirst.execute(url);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setView(layout);
                        toast.show();
                    }

                }
            });


            destination_address = (MaterialBetterSpinner) v.findViewById(R.id.destination_address);
            //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            destination_address.setAdapter(adapter);

            destination_address.setVisibility(View.INVISIBLE);

            destination_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    destination = (String) arg0.getItemAtPosition(arg2);
                    destination_button_clicked = true;

                    if (network_connection_status) {

                        UpdateMap_destination updateMapsecond = new UpdateMap_destination(getActivity());
                        //String url="http://121.241.125.91/cc/mavyn/online/customerloginafter.php?bodytype="+truck_staus+"&vehicletype="+3+"&fromlocation="+source+"&tolocation="+destination;
                        String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?bodytype=" + truck_staus + "&vehicletype=" + v_id + "&flocation=" + source + "&tolocation=" + destination;
                        updateMapsecond.execute(url);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setView(layout);
                        toast.show();
                    }

                }
            });

        }


        open_vehicle = (Button) v.findViewById(R.id.open_v);
        closed_vehicle = (Button) v.findViewById(R.id.close_v);
        book_shipment = (Button) v.findViewById(R.id.booking_button);
        open_vehicle.setOnClickListener(this);
        closed_vehicle.setOnClickListener(this);
        book_shipment.setOnClickListener(this);
        book_shipment.setVisibility(View.INVISIBLE);


        if (network_connection_status) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView = (RecyclerView) v.findViewById(R.id.vehicle_list);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mycategoryadapter);

            mycategoryadapter.setSelected(0);

            // v_id=mycategoryadapter.getitem(0).getId();

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {


                            // hitting sever for vehicle detail to show on the map..

                            if (open_button_presses == true) {


                                if (network_connection_status) {
                                    vdata = mycategoryadapter.getitem(position);
                                    mycategoryadapter.setSelected(position);
                                    Download_Vehicle hit_vehicle_local_open = new Download_Vehicle(getActivity());
                                    String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?vehicletype=open&v_id=" + mycategoryadapter.getitem(position).getId();
                                    hit_vehicle_local_open.execute(url);
                                    mMap.clear();
                                    v_id = mycategoryadapter.getitem(position).getId();
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setView(layout);
                                    toast.show();
                                }
                            }

                            if (close_button_pressed == true) {


                                if (network_connection_status) {
                                    vdataclose = mycategoryadapterclose.getitem(position);
                                    mycategoryadapterclose.setSelected(position);
                                    Download_Vehicle hit_vehicle_local = new Download_Vehicle(getActivity());
                                    String url = "http://121.241.125.91/cc/mavyn/online/customerloginafter.php?vehicletype=closed&v_id=" + mycategoryadapterclose.getitem(position).getId();
                                    hit_vehicle_local.execute(url);
                                    v_id = mycategoryadapterclose.getitem(position).getId();
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setView(layout);
                                    toast.show();
                                }

                            }

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );

        } else

            Show_No_net_off_msg();

        return v;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        mMap.setMyLocationEnabled(true);

        mMap.setPadding(150, 350, 0, 0);

        if (!network_connection_status)
            mMap.setPadding(150, 0, 0, 0);

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.open_v:

                if (checked == true) {

                    if (book_shipment.getVisibility() == View.VISIBLE)
                        hide_Booking_button();
                    mMap.clear();

                    //pick_up_address.setText("");
                    //destination_address.setText("");


                    destination_address.setVisibility(View.INVISIBLE);
                    checked = false;
                    open_vehicle.setTextColor(Color.parseColor("#ffffff"));
                    ColorStateList colorStateList1 = new ColorStateList(new int[][]{{0}}, new int[]{0xFFd84c00});
                    open_vehicle.setBackgroundTintList(colorStateList1);
                    closed_vehicle.setTextColor(Color.parseColor("#d84c00"));
                    ColorStateList colorStateList2 = new ColorStateList(new int[][]{{0}}, new int[]{0xFFffffff}); // 0xAARRGGBB
                    closed_vehicle.setBackgroundTintList(colorStateList2);
                    recyclerView.setAdapter(mycategoryadapter);
                    open_button_presses = true;
                    truck_staus = "open";

                }

                break;

            case R.id.close_v:


                if (checked == false) {

                    checked = true;

                    if (book_shipment.getVisibility() == View.VISIBLE)
                        hide_Booking_button();
                    mMap.clear();

                    //   pick_up_address.setHint("zero");
                    //  destination_address.setHint("hero");
                    //  citystatelist.clear();
                    //  adapter.notifyDataSetChanged();


                    destination_address.setVisibility(View.INVISIBLE);

                    open_vehicle.setTextColor(Color.parseColor("#d84c00"));
                    ColorStateList colorStateList0 = new ColorStateList(new int[][]{{0}}, new int[]{0xFFffffff}); // 0xAARRGGBB
                    open_vehicle.setBackgroundTintList(colorStateList0);
                    closed_vehicle.setTextColor(Color.parseColor("#ffffff"));
                    ColorStateList colorStateList3 = new ColorStateList(new int[][]{{0}}, new int[]{0xFFd84c00}); // 0xAARRGGBB
                    closed_vehicle.setBackgroundTintList(colorStateList3);
                    recyclerView.setAdapter(mycategoryadapterclose);
                    close_button_pressed = true;
                    open_button_presses = false;
                    truck_staus = "close";


                }
                break;


            case R.id.booking_button:

                Intent booking = new Intent(getActivity(), OrderActivity.class);
                booking.putExtra("source", source);
                booking.putExtra("destination", destination);
                try {
                    if (open_button_presses == true)
                        booking.putExtra("vehicle_id", vdata.getId());
                    else if (close_button_pressed == true)
                        booking.putExtra("vehicle_id", vdataclose.getId());

                    startActivity(booking);
                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setView(layout);
                    toast.show();
                }
                break;

            default:

                break;

        }

    }


    @Override
    public void onDestroy()

    {
        super.onDestroy();

        if (network_connection_status) {

            myvehicle_category.removeAll(myvehicle_category);

            mycategoryadapter.notifyDataSetChanged();

            myvehicle_categoryclose.removeAll(myvehicle_categoryclose);

            mycategoryadapterclose.notifyDataSetChanged();

            citystatelist.removeAll(citystatelist);


        }

        mGoogleApiClient.disconnect();


    }

    private class UpdateMap extends AsyncTask<String, Void, String> {
        private Context localcontext;
        private ProgressDialog dialog;
        private String local = null;
        private BufferedReader bufferinput = null;
        private HttpURLConnection con = null;
        private URL url;

        public UpdateMap(Context c) {
            localcontext = c;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(localcontext);
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }


        protected String doInBackground(String... urls) {

            try {

                url = new URL(urls[0]);
                Log.i("URL", urls[0]);
                con = (HttpURLConnection) url.openConnection();
                bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
                try {
                    local = bufferinput.readLine();

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

        /*  protected void onCancelled()
          {
           dialog.dismiss();
           Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, 25, 400);
           toast.show();
           }
    */
        protected void onPostExecute(String content) {
            dialog.dismiss();

            update_Vehicle_on_map(content);


        }

    }


    private void update_Vehicle_on_map(String response) {

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");

            int length = countryListObj.length();

            System.out.println("json array length=" + length);


            if (length > 0) {


                destination_address.setVisibility(View.VISIBLE);

                // destination_address.setTextColor(Color.parseColor("#000fff"));
            }


            if (length == 0) {
                //Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
               /* book_shipment.setText("No Vehicle In This Route!!!");
                book_shipment.setVisibility(View.VISIBLE);*/

                Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
                toast.show();

            }

        /*    if(length>0) {
                book_shipment.setVisibility(View.VISIBLE);

            }

*/

            my_map_vehicle.removeAll(my_map_vehicle);
            mMap.clear();


            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);


                // String id=null;
                double longitute = 0.0;
                double latitute = 0.0;
                String description = null;

                try {


                    // id = jsonChildNode.optString("id").toString();
                    latitute = Double.valueOf(jsonChildNode.optString("latitute").toString());
                    longitute = Double.valueOf(jsonChildNode.optString("longitude").toString());

                    description = jsonChildNode.optString("description").toString();


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                my_map_vehicle.add(new Vehicle_Data(latitute, longitute, description));
                temp_latlong = new LatLng(my_map_vehicle.get(i).getLatitute(), my_map_vehicle.get(i).getLongitute());
                driver_marker = mMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)).title("Description").snippet(description));


              /*  mMarkersHashMap.put(driver_marker, new MyMarker(description));
                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());*/


            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }

        try {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);

            mMap.getUiSettings().setZoomControlsEnabled(true);
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
            mMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // for testing of app
        /* my_map_vehicle.removeAll(my_map_vehicle);
        mMap.clear();
        temp_latlong = new LatLng(23.72501174,80.66162109);
        driver_marker = mMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));*/

    }


    //...........................................


    private class UpdateMap_destination extends AsyncTask<String, Void, String> {
        private Context localcontext;
        private ProgressDialog dialog;
        private String local = null;
        private BufferedReader bufferinput = null;
        private HttpURLConnection con = null;
        private URL url;

        public UpdateMap_destination(Context c) {
            localcontext = c;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(localcontext);
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }


        protected String doInBackground(String... urls) {

            try {

                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();
                bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
                try {
                    local = bufferinput.readLine();

                } catch (Exception e) {
                    e.printStackTrace();

                }


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

        /*  protected void onCancelled()
          {
           dialog.dismiss();
           Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, 25, 400);
           toast.show();
           }
    */
        protected void onPostExecute(String content) {
            dialog.dismiss();

            update_Vehicle_on_map_destination(content);


        }

    }


    private void update_Vehicle_on_map_destination(String response) {

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");

            int length = countryListObj.length();

            System.out.println("json array length=" + length);


            if (length == 0) {
                //Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
                // book_shipment.setText("No Vehicle In This Route!!!");
                // show_Booking_button();
                //   book_shipment.setVisibility(View.VISIBLE)
                Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
                toast.show();


            }


            if (length > 0) {

                // book_shipment.setText("Create Shipment!!!");

                show_Booking_button();

                // book_shipment.setVisibility(View.VISIBLE);

            }


            my_map_vehicle.removeAll(my_map_vehicle);
            mMap.clear();


            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);


                // String id=null;
                double longitute = 0.0;
                double latitute = 0.0;
                String description = null;

                try {


                    // id = jsonChildNode.optString("id").toString();
                    latitute = Double.valueOf(jsonChildNode.optString("latitute").toString());
                    longitute = Double.valueOf(jsonChildNode.optString("longitude").toString());

                    description = jsonChildNode.optString("description").toString();


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                my_map_vehicle.add(new Vehicle_Data(latitute, longitute, description));
                temp_latlong = new LatLng(my_map_vehicle.get(i).getLatitute(), my_map_vehicle.get(i).getLongitute());
                driver_marker = mMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)).title("Description").snippet(description));


              /*  mMarkersHashMap.put(driver_marker, new MyMarker(description));
                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());*/


            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }

        try {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);

            mMap.getUiSettings().setZoomControlsEnabled(true);
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
            mMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // for testing of app
        /* my_map_vehicle.removeAll(my_map_vehicle);
        mMap.clear();
        temp_latlong = new LatLng(23.72501174,80.66162109);
        driver_marker = mMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));*/

    }


    private void writestateToInternalStorage() {
        byte b = 2;
        try {

            FileOutputStream fos = getActivity().openFileOutput("loginstatus.txt", Context.MODE_PRIVATE);

            fos.write(b);
            fos.close();

        } catch (Exception e) {
            Log.i("Error_In_login", e.getMessage());

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


            //markerLabel.setText(myMarker.getVehicleno());
            anotherLabel.setText(myMarker.getAddress());

            return v;
        }
    }


    private class DownloadState implements Runnable {
        Context localcontext;
        private ProgressDialog dialog;
        private String local;
        private BufferedReader bufferinput = null;
        private HttpURLConnection con = null;
        private ArrayList<String> citylistlocal = new ArrayList(30);
        JSONObject responseObj;

        public DownloadState(Context c) {
            localcontext = c;
			/*dialog = new ProgressDialog(localcontext);
			dialog.setMessage("Getting your data... Please wait...");
			dialog.show();*/
        }


        ArrayList<String> getlistdata() {
            return citylistlocal;
        }


        @Override
        public void run() {

            try {

                URL url = new URL("http://121.241.125.91/cc/mavyn/online/customerfindlocation.php?msg=location");
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

            try {


                responseObj = new JSONObject(local);
                JSONArray countryListObj = responseObj.optJSONArray("location");  //.getJSONArray("transit");

                int length = countryListObj.length();

                System.out.println("json array length=" + length);

                for (int i = 0; i < length; i++) {

                    JSONObject jsonChildNode = countryListObj.getJSONObject(i);


                    String Citydata = null;


                    try {

                        Citydata = jsonChildNode.optString("city").toString();

                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    citylistlocal.add(Citydata);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error in reading json object");
            }

        }

    }

    void Show_No_net_off_msg() {

        header.setVisibility(View.INVISIBLE);
        top.setVisibility(View.INVISIBLE);
        netconnection_hint.setVisibility(View.VISIBLE);


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

                URL url = new URL(urls[0] + first_open_v_id);
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

        /*  protected void onCancelled()
          {
           dialog.dismiss();
           Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, 25, 400);
           toast.show();
           }
    */
        protected void onPostExecute(String content) {
            dialog.dismiss();

            Display_Vehicle_list(content);


        }

    }


    private void Display_Vehicle_list(String response) {

        JSONObject responseObj;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("vehicledata");  //.getJSONArray("transit");

            int length = countryListObj.length();

            System.out.println("json array length=" + length);

            if (length == 0) {
                // Toast.makeText(getActivity(),"No Vehicle",Toast.LENGTH_SHORT).show();
                Toast toast = Toast.makeText(getActivity(), "No Vehicle Available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
                toast.show();
            }


            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);


                // String id=null;
                double longitute = 0.0;
                double latitute = 0.0;
                String description = null;

                try {


                    // id = jsonChildNode.optString("id").toString();
                    longitute = Double.valueOf(jsonChildNode.optString("longitude").toString());
                    latitute = Double.valueOf(jsonChildNode.optString("latitute").toString());
                    description = jsonChildNode.optString("description").toString();


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mMyMarkersArray.add(new MyMarker(description));


                my_map_vehicle.add(new Vehicle_Data(latitute, longitute, description));
                temp_latlong = new LatLng(my_map_vehicle.get(i).getLatitute(), my_map_vehicle.get(i).getLongitute());
                driver_marker = mMap.addMarker(new MarkerOptions().position(temp_latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));
                mMap.getMyLocation();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }


        try {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(temp_latlong).zoom(9.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(cameraUpdate);

            mMap.getUiSettings().setZoomControlsEnabled(true);
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
            mMap.setMyLocationEnabled(true);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }


    private void initAnimation()
    {
        animShow= AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        animHide=AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
    }

    boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void show_Booking_button()
    {
        book_shipment.setVisibility(View.VISIBLE);
        book_shipment.startAnimation(animShow);

    }


    public void hide_Booking_button()
    {
        book_shipment.setVisibility(View.INVISIBLE);
        book_shipment.startAnimation(animHide);

    }




}

package com.example.sushilverma.mavync;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {


    private DatePicker datePicker;
    private Calendar calendar;
    private Button dateView, timeView, cancelshipment, place_my_order;
    private int year, month, day;
    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;
    private static volatile double originlate, originlong, destinationlate, destinationlong;

    private TextView source, destination;
    private AutoCompleteTextView name, mobile, address;
    private String distance;
    private Intent intent, intentlocal;
    private View focusView = name;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        intent = new Intent(this, ConfirmShipment.class);

        dateView = (Button) findViewById(R.id.date);
        timeView = (Button) findViewById(R.id.orderingtime);
        cancelshipment = (Button) findViewById(R.id.cancelshipment);
        place_my_order = (Button) findViewById(R.id.place_order);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        source = (TextView) findViewById(R.id.source);
        destination = (TextView) findViewById(R.id.destination);
        name = (AutoCompleteTextView) findViewById(R.id.name);
        mobile = (AutoCompleteTextView) findViewById(R.id.mobile);
        address = (AutoCompleteTextView) findViewById(R.id.address);
        place_my_order.setOnClickListener(this);
        cancelshipment.setOnClickListener(this);

        intentlocal = getIntent();
        source.setText(intentlocal.getStringExtra("source"));
        destination.setText(intentlocal.getStringExtra("destination"));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private boolean cheking() {

        boolean cancel = false;

        String name1 = name.getText().toString();
        String mobile_no1 = mobile.getText().toString();
        String address1 = address.getText().toString();
        String date = dateView.getText().toString();
        String time = timeView.getText().toString();
        int  k=date.compareToIgnoreCase("Select Booking Date");
        int  l=time.compareToIgnoreCase("Select Booking Time");


        if (TextUtils.isEmpty(name1)) {
            name.setError("Consinor name required.");
            focusView = name;
            cancel = true;
        } else if (TextUtils.isEmpty(mobile_no1)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }
           else if (mobile_no1.charAt(0)=='0') {
            mobile.setError("first no 0 is invalid");
            focusView = mobile;
            cancel = true;
           }

             else
                 if (!ismobile_noValid(mobile_no1)) {
                     mobile.setError("No invalid");
                    focusView = mobile;
                    cancel = true;
                }





      /*  if (!ismobile_noValid(mobile_no1)) {
            mobile.setError("Invalid Mob. No.");
            focusView = mobile;
            cancel = true;
        }
        */

        else if (TextUtils.isEmpty(address1)) {
            address.setError("PickUP Address Required");
            focusView = address;
            cancel = true;
        } else if (k==0) {
            dateView.setError("Date Required");
            Toast.makeText(this, "Date Required", Toast.LENGTH_SHORT).show();
            focusView = dateView;
            cancel = true;
        } else if (l==0) {
            timeView.setError("Time Required.");
            Toast.makeText(this, "Time Required", Toast.LENGTH_SHORT).show();
            focusView = timeView;
            cancel = true;
        }


        if (cancel) {

            focusView.requestFocus();
        }


        return cancel;


    }

    private boolean ismobile_noValid(String mobile_no)
    {

            return mobile_no.length()==10;
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(TIME_DIALOG_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {


            distance = "400";

            return new DatePickerDialog(this, myDateListener, year, month, day);
        }


        if (id == 1111) {
            return new TimePickerDialog(this, timePickerListener, hour, minute,
                    false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

        }

    };


    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

        dateView.setError(null);
    }


    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeView.setText(aTime);
        timeView.setError(null);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.place_order:

                Intent i = getIntent();
                String vehicle_id = i.getStringExtra("vehicle_id");

                SharedPreferences userid = getSharedPreferences("U_id", MODE_PRIVATE);
                String customer_id = userid.getString("userid", null);
                hitserver hit = new hitserver(this);

                if (cheking() == false) {

                    hit.execute("http://121.241.125.91/cc/mavyn/online/customerbooking.php?customer_id=" + customer_id + "&" + "source=" + source.getText() + "&" + "source_lat=" + originlate + "&" + "source_long=" + originlong + "&" + "destination=" + destination.getText() + "&" + "destination_lat=" + destinationlate + "&" + "destination_long=" + destinationlong + "&" + "user_name=" + name.getText() + "&" + "user_mobile=" + mobile.getText() + "&" + "user_address=" + address.getText() + "&" + "booking_date=" + dateView.getText() + "&" + "booking_time=" + timeView.getText() + "&" + "vehicletype_id=" + vehicle_id);

                }

                break;


            case R.id.cancelshipment:

                finish();
                break;
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Order Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sushilverma.mavync/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Order Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sushilverma.mavync/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class hitserver extends AsyncTask<String, Void, String> {
        Context localcontext;
        private ProgressDialog dialog;
        private String local = "abc";

        public hitserver(Context c) {
            localcontext = c;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(localcontext);
            dialog.setMessage("... Please wait...");
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

            ReceiveJsonData(content);


        }

    }


    private void ReceiveJsonData(String response) {

        JSONObject responseObj;
        String shipmentno = null;
        String expectedtime_normal = null;
        String expecteddistance_normal = null;
        String expectedfreight_normal = null;
        String expetedtimeexpress = null;
        String expecteddistanceexpress = null;
        String expectedfreightexpress = null;


        try {


            responseObj = new JSONObject(response);
            JSONArray countryListObj = responseObj.optJSONArray("bookingdata");  //.getJSONArray("transit");

            int length = countryListObj.length();

            System.out.println("json array length=" + length);

            for (int i = 0; i < length; i++) {

                JSONObject jsonChildNode = countryListObj.getJSONObject(i);


                try {


                    shipmentno = jsonChildNode.optString("shipmentno").toString();
                    expectedtime_normal = jsonChildNode.optString("Etnormal").toString();
                    expecteddistance_normal = jsonChildNode.optString("Tdistancenormal").toString();
                    expectedfreight_normal = jsonChildNode.optString("Efreightnormal").toString();
                    expetedtimeexpress = jsonChildNode.optString("Etexpress").toString();
                    expecteddistanceexpress = jsonChildNode.optString("Tdistanceexpress").toString();
                    expectedfreightexpress = jsonChildNode.optString("Efreightexpress").toString();


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error in reading json object");
        }


        intent.putExtra("shipmentno", shipmentno);
        intent.putExtra("exptimenormal", expectedtime_normal);
        intent.putExtra("exptimeexpress", expetedtimeexpress);

        intent.putExtra("expdistance", expecteddistance_normal);
        intent.putExtra("expdistanceexp", expecteddistanceexpress);

        intent.putExtra("expectedfreight_normal", expectedfreight_normal);
        intent.putExtra("expectedfreightexpress", expectedfreightexpress);

        intent.putExtra("source", intentlocal.getStringExtra("source"));
        intent.putExtra("destination", intentlocal.getStringExtra("destination"));
        intent.putExtra("address", address.getText());
        intent.putExtra("shipment_date", dateView.getText());
        intent.putExtra("shipment_time", timeView.getText());

        startActivity(intent);


    }


   /* private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/


    class Distance implements Runnable {

        HttpURLConnection myurl;
        String getdata;
        String distance;

        String getDistance() {
            return distance;
        }


        public void run() {


            String urlstring = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + originlate + "," + originlong + "&destinations=" + destinationlong + "," + destinationlate + "&key=AIzaSyAjXMDXFt-fum9sPD1sSFF_YLBzkBYCUTk";


            try {
                URL url = new URL(urlstring);
                HttpURLConnection myurl = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(myurl.getInputStream()));


                try {
                    getdata = br.readLine();
                    System.out.println("distace received=" + getdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                JSONObject jsonRespRouteDistance = new JSONObject(getdata)
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("distance");

                distance = jsonRespRouteDistance.get("text").toString();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}

package com.example.sushilverma.mavync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Rating_popup extends AppCompatActivity implements View.OnClickListener{

    private Button submit;
    private Button no_thanks;
    private RatingBar ratingBar_driver;
    private TextView rateMessage_driver;
    private  RatingBar ratingBar_owner;
    private TextView rateMessage_owner;
    private EditText driver_comment;
    private EditText mavyn_comment;
    private int ratedValue_driver=0;
    private int ratedValue_mavyn=0;

    private static volatile String driver_cmt=null;
    private static volatile String mavyn_cmt=null;
    private View focusView = driver_comment;
    private String usr_id;
    private String shipment_id;
    private String from;
    private String to;
    private String date;
    private static  int stars_driver=0;
    private static  int stars_mavyn=0;
    private Intent second_activity;

    private TextView delivary_date;
    private TextView delivered_shipment_id;
    private TextView from_locaiton;
    private TextView to_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_layout);


        delivary_date = (TextView) findViewById(R.id.date_of_delivery);
        delivered_shipment_id = (TextView) findViewById(R.id.delivered_shipment_id);
        from_locaiton = (TextView) findViewById(R.id.from_location);
        to_location = (TextView) findViewById(R.id.to_lacation);


        driver_comment=(EditText)findViewById(R.id.driver_comment_box);
        mavyn_comment=(EditText)findViewById(R.id.mavyn_comment_box);
        submit = (Button) findViewById(R.id.submit_rating);
        no_thanks = (Button) findViewById(R.id.no_thank);


        Intent localintent=getIntent();

        shipment_id=localintent.getStringExtra("shipment_id");
        from=localintent.getStringExtra("from");
        to=localintent.getStringExtra("to");
        date=localintent.getStringExtra("date");

        //String dateonly = date.length();

        String dateonly123=new String();
        dateonly123.split(date,11);

        delivered_shipment_id.setText(shipment_id);
        from_locaiton.setText(from);
        to_location.setText(to);
        delivary_date.setText(dateonly123.toString());





         submit.setOnClickListener(this);
         no_thanks.setOnClickListener(this);
         ratingBar_driver = (RatingBar) findViewById(R.id.driver_rating_bar);
         rateMessage_driver = (TextView) findViewById(R.id.rateMessage);

         ratingBar_driver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar_driver.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    stars_driver = (int)starsf + 1;
                    ratingBar_driver.setRating(stars_driver);

                    switch(String.valueOf(stars_driver))
                    {

                        case "1":

                            rateMessage_driver.setText("Terrible");
                            break;

                        case "2":

                            rateMessage_driver.setText("Bad");
                            break;

                        case "3":

                            rateMessage_driver.setText("Ok");
                            break;

                        case "4":

                            rateMessage_driver.setText("Good");
                            break;


                        case "5":

                            rateMessage_driver.setText("Excellent");
                            break;

                    }


                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }

                return true;
            }});


         ratingBar_owner = (RatingBar) findViewById(R.id.mayvy_rating_bar);
         rateMessage_owner = (TextView) findViewById(R.id.company_comment);
          ratingBar_owner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar_driver.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    stars_mavyn = (int)starsf + 1;
                    ratingBar_owner.setRating(stars_mavyn);

                    switch(String.valueOf(stars_mavyn))
                    {

                        case "1":

                            rateMessage_owner.setText("Terrible");
                            break;

                        case "2":

                            rateMessage_owner.setText("Bad");
                            break;

                        case "3":

                            rateMessage_owner.setText("Ok");
                            break;

                        case "4":

                            rateMessage_owner.setText("Good");
                            break;


                        case "5":

                            rateMessage_owner.setText("Excellent");
                            break;





                    }


                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }




                return true;
            }});



        driver_comment.setError(null);
        mavyn_comment.setError(null);

        SharedPreferences userid=getSharedPreferences("U_id",MODE_PRIVATE);
        usr_id=userid.getString("U_id",null);

    }

    @Override
    public void onClick(View view)

    {

        boolean cancel = false;

        switch(view.getId())
        {

            case R.id.submit_rating:

                driver_cmt = driver_comment.getText().toString();
                mavyn_cmt =  mavyn_comment.getText().toString();

               // Toast.makeText(Rating_popup.this,driver_cmt+"#"+mavyn_cmt+"#"+stars_driver+"#"+stars_mavyn, Toast.LENGTH_SHORT).show();

                 if (TextUtils.isEmpty(mavyn_cmt)) {

                    if(stars_mavyn<2) {
                        mavyn_comment.setError(getString(R.string.write_comment));
                        focusView = mavyn_comment;
                        cancel = true;
                    }
                }

                 else  if (TextUtils.isEmpty(driver_cmt)) {


                    if (stars_driver <2) {
                        driver_comment.setError(getString(R.string.write_comment));
                        focusView = driver_comment;
                        cancel = true;
                    }
                }

                if (cancel) {

                    focusView.requestFocus();
                }

                else {
                    if (stars_mavyn > 0 & stars_driver > 0) {

                        Hitdata hit = new Hitdata();
                        Thread t = new Thread(hit);
                        t.start();

                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Toast.makeText(Rating_popup.this,driver_cmt+"#"+mavyn_cmt+"#"+stars_driver+"#"+stars_mavyn, Toast.LENGTH_SHORT).show();


                        if (hit.getResult() == 1) {

                            Toast.makeText(Rating_popup.this, "submitted", Toast.LENGTH_SHORT).show();

                            second_activity = new Intent(this, HomeActivity.class);

                            second_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(second_activity);

                        } else
                            Toast.makeText(Rating_popup.this, "not submitted", Toast.LENGTH_SHORT).show();


                        finish();


                    }

                    else
                        Toast.makeText(Rating_popup.this, "Rating missing", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.no_thank:

                second_activity = new Intent(this,HomeActivity.class);

                second_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(second_activity);

                Rating_popup.this.finish();


                break;

            default:

                break;

        }






    }


   /* void GetText() throws UnsupportedEncodingException
    {


      //  BufferedReader reader=null;
        Toast.makeText(Rating_popup.this,"submitted",Toast.LENGTH_SHORT).show();

        *//*new Thread(new Runnable(

        ) {
            @Override
            public void run() {

                try
                {

                    // Defined URL  where to send data
                    URL url = new URL("http://121.241.125.91/cc/mavyn/online/customerapp.php");

                    // Send POST data request

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("rating", "rating");
                    postDataParams.put("shipment_id", "1001");
                    postDataParams.put("driverrate",stars_mavyn);
                    postDataParams.put("drivercommant",driver_cmt);
                    postDataParams.put("companyrate", stars_mavyn);
                    postDataParams.put("companycommant", mavyn_cmt);



                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 *//**//* milliseconds *//**//*);
                    conn.setConnectTimeout(15000 *//**//* milliseconds *//**//*);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));


                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode=conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {


                        Rating_popup.this.finish();

                        *//**//*BufferedReader in=new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuffer sb = new StringBuffer("");
                        String line="";

                        while((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();*//**//*




                    }

                    else

                        Toast.makeText(Rating_popup.this,"Not submitted",Toast.LENGTH_SHORT).show();

                }
                catch(Exception e){
                    e.printStackTrace();
                }


            }
        }).start();*//*



    }*/


    class Hitdata implements Runnable
    {

        int result=0;

        public int getResult()
        {
            return result;
        }

        public Hitdata(){}

        @Override
        public void run() {
            try
            {

                // Defined URL  where to send data
                URL url = new URL("http://121.241.125.91/cc/mavyn/online/customerapp.php");

                // Send POST data request

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("rating", "rating");
                postDataParams.put("shipment_id", "1001");
                postDataParams.put("driverrate",stars_mavyn);
                postDataParams.put("drivercommant",driver_cmt);
                postDataParams.put("companyrate", stars_mavyn);
                postDataParams.put("companycommant", mavyn_cmt);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = null;
                try {
                    os = conn.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));


                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    result=1;


                }

                else

                    Toast.makeText(Rating_popup.this,"Not submitted",Toast.LENGTH_SHORT).show();

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }




    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    }


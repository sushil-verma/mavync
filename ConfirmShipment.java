package com.example.sushilverma.mavync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class ConfirmShipment extends AppCompatActivity implements View.OnClickListener{
    private TextView shipmentno,shipmentnoexp,source,destination,date,time,tt,distance,freight;
    private TextView sourceexp,destinationexp,dateexp,timeexp,ttexp,distanceexp,freightexp;
    private Button cancel,normal_booking;
    private Button cancelexp,express_booking;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_shipment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        shipmentno=(TextView)findViewById(R.id.shipment_no);
        shipmentnoexp=(TextView)findViewById(R.id.shipment_noexp);
        source=(TextView)findViewById(R.id.fromcity);
        destination=(TextView)findViewById(R.id.tocity);
        date=(TextView)findViewById(R.id.shipment_date);
        time=(TextView)findViewById(R.id.shipment_time);
        tt=(TextView)findViewById(R.id.tt);
        distance=(TextView)findViewById(R.id.distance);
        freight=(TextView)findViewById(R.id.freight);


        sourceexp=(TextView)findViewById(R.id.fromcityexe);
        destinationexp=(TextView)findViewById(R.id.tocityexp);
        dateexp=(TextView)findViewById(R.id.shipment_dateexp);
        timeexp=(TextView)findViewById(R.id.shipment_Timeexp);
        distanceexp=(TextView)findViewById(R.id.distanceexe);
        freightexp=(TextView)findViewById(R.id.freightexe);
        ttexp=(TextView)findViewById(R.id.tteexe);

        cancel=(Button)findViewById(R.id.cancel);

        normal_booking=(Button)findViewById(R.id.confirm_normal_shipment);
        express_booking=(Button)findViewById(R.id.confirm_express_shipment);

        normal_booking.setOnClickListener(this);
        express_booking.setOnClickListener(this);
        cancel.setOnClickListener(this);

        intent=getIntent();

        String ss=intent.getStringExtra("source");
        String dd=intent.getStringExtra("destination");

        shipmentno.setText(intent.getStringExtra("shipmentno"));
        source.setText(ss);
        destination.setText(dd);
        time.setText(intent.getStringExtra("shipment_time"));
        date.setText(intent.getStringExtra("shipment_date"));
        tt.setText(intent.getStringExtra("exptimenormal"));
        distance.setText(intent.getStringExtra("expdistance"));
        freight.setText(intent.getStringExtra("expectedfreight_normal"));



        shipmentnoexp.setText(intent.getStringExtra("shipmentno"));
        sourceexp.setText(ss);
        destinationexp.setText(dd);
        timeexp.setText(intent.getStringExtra("shipment_time"));
        dateexp.setText(intent.getStringExtra("shipment_date"));
        ttexp.setText(intent.getStringExtra("exptimeexpress"));
        distanceexp.setText(intent.getStringExtra("expdistanceexp"));
        freightexp.setText(intent.getStringExtra("expectedfreightexpress"));

    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.confirm_normal_shipment:

                sendconfirmstatus("normalshipment");


                break;

            case R.id.confirm_express_shipment:

                sendconfirmstatus("expressshipment");
                break;

            case R.id.cancel:

                sendconfirmstatus("cancelshipment");


                             finish();

                                break;

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
    }


   void sendconfirmstatus(final String Status)
    {

        SharedPreferences userid = getSharedPreferences("U_id", MODE_PRIVATE);
        String customer_id = userid.getString("userid", null);
        String url="http://121.241.125.91/cc/mavyn/online/customerbooking.php?shipment_no="+shipmentno.getText().toString()+"&"+Status+"=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                startActivity(new Intent(getApplicationContext(),BookingConfirmation.class).putExtra("bookingtype",Status));


            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getApplicationContext(),"Response error",Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                })

        {


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}




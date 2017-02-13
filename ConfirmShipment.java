package com.example.sushilverma.mavync;

import android.content.Intent;
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

import org.w3c.dom.Text;

public class ConfirmShipment extends AppCompatActivity implements View.OnClickListener{
    private TextView shipmentno,shipmentnoexp,source,destination,date,time,tt,distance,freight;
    private TextView sourceexp,destinationexp,dateexp,timeexp,ttexp,distanceexp,freightexp;
    private Button cancel,pay;
    private Button cancelexp,payexp;

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

        pay=(Button)findViewById(R.id.confirm_my_shipment);
        payexp=(Button)findViewById(R.id.book_shipmentexe);

        pay.setOnClickListener(this);
        payexp.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent intent=getIntent();

        String ss=intent.getStringExtra("source");
        String dd=intent.getStringExtra("destination");

        shipmentno.setText(intent.getStringExtra("shipmentno"));
        source.setText(ss);
        destination.setText(dd);
        time.setText(intent.getStringExtra("shipment_time"));
        date.setText(intent.getStringExtra("shipment_date"));
        tt.setText(intent.getStringExtra("expdistance"));
        distance.setText(intent.getStringExtra("expdistance"));
        freight.setText(intent.getStringExtra("expectedfreight_normal"));



        shipmentnoexp.setText(intent.getStringExtra("shipmentno"));
        sourceexp.setText(ss);
        destinationexp.setText(dd);
        timeexp.setText(intent.getStringExtra("shipment_time"));
        dateexp.setText(intent.getStringExtra("shipment_date"));
        ttexp.setText(intent.getStringExtra("exptimeexpress"));
        distanceexp.setText(intent.getStringExtra("expecteddistanceexpress"));
        freightexp.setText(intent.getStringExtra("expectedfreightexpress"));

    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.confirm_my_shipment:

                                    Intent intentnormal = new Intent(getApplicationContext(), PayMentGateWay.class);
                                    intentnormal.putExtra("FIRST_NAME","sushil verma");
                                    intentnormal.putExtra("PHONE_NUMBER","9911427348");
                                    intentnormal.putExtra("EMAIL_ADDRESS","sushil@verma");
                                   // intentnormal.putExtra("RECHARGE_AMT",intentnormal.getStringExtra("expectedfreight_normal"));
                                    intentnormal.putExtra("RECHARGE_AMT","500");
                                    startActivity(intentnormal);
                break;

            case R.id.book_shipmentexe:

                                     Intent intentexpress = new Intent(getApplicationContext(), PayMentGateWay.class);
                                     intentexpress.putExtra("FIRST_NAME","sushil verma");
                                     intentexpress.putExtra("PHONE_NUMBER","9911427348");
                                     intentexpress.putExtra("EMAIL_ADDRESS","sushil@verma");
                                   //  intentexpress.putExtra("RECHARGE_AMT",intentexpress.getStringExtra("expectedfreightexpress"));
                                     intentexpress.putExtra("RECHARGE_AMT","555");
                                     startActivity(intentexpress);

                break;

            case R.id.cancel:


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
}

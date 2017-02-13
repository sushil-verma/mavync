package com.example.sushilverma.mavync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Myprofile extends Fragment{

    FileInputStream fis=null;
    private Bitmap pics=null;
    private SharedPreferences shipper_data=null;
    private ImageView profile_image;
    private TextView name;
    private TextView email;
    private TextView mobile;
    private TextView bankname;
    private TextView acno;
    private TextView ifccode;

    private FileInputStream fos=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.myprofilecontent2, null);
        name=(TextView)root.findViewById(R.id.myprofilename);
        email=(TextView)root.findViewById(R.id.myprofileemail);
        mobile=(TextView)root.findViewById(R.id.myprofilemobile);
        bankname=(TextView)root.findViewById(R.id.bankname);
        acno=(TextView)root.findViewById(R.id.acno);
        ifccode=(TextView)root.findViewById(R.id.ifccode);
        profile_image=(ImageView)root.findViewById(R.id.imageView_round);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        shipper_data = getActivity().getSharedPreferences("profiledata", Context.MODE_PRIVATE);
        name.setText(shipper_data.getString("first_name", "null"));
        email.setText(shipper_data.getString("email_temp", "null"));
        mobile.setText(shipper_data.getString("Mobileno", "null"));
        bankname.setText(shipper_data.getString("bank_name", "null"));
        acno.setText(shipper_data.getString("acc_no", "null"));
        ifccode.setText(shipper_data.getString("ifccode_temp", "null"));
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);

        try {
            fos = getActivity().openFileInput("desiredFilename1.png");
        }
        catch (FileNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if(fos!=null) {

            pics = BitmapFactory.decodeStream(fos);
            try {
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

            profile_image.setImageBitmap(pics);
        }


    }



    public boolean isOnline()
    {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();


    }









}

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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

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
        profile_image=(ImageView)root.findViewById(R.id.shipperprofileimage);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        shipper_data = getActivity().getSharedPreferences("profiledata", Context.MODE_PRIVATE);
        name.setText(shipper_data.getString("first_name", "Name"));
        email.setText(shipper_data.getString("email_temp", "Email Id"));
        mobile.setText(shipper_data.getString("Mobileno", "Mobile No"));
        bankname.setText(shipper_data.getString("bank_name", "Account Name"));
        acno.setText(shipper_data.getString("acc_no", "Bank Account Number"));
        ifccode.setText(shipper_data.getString("ifccode_temp", "IFSC Code"));

        try {
            fos = getActivity().openFileInput("desiredFilename1.png");
            if(fos!=null) {

                pics = BitmapFactory.decodeStream(fos);
                fos.close();
            }

        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
         catch (IOException e) {

            e.printStackTrace();
        }



            Glide.with(this).load(pics).asBitmap().centerCrop().into(new BitmapImageViewTarget(profile_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profile_image.setImageDrawable(circularBitmapDrawable);
                }
            });



        }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}

package com.example.sushilverma.mavync;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class LogOutAlert extends Activity {

	   Intent i=null;


		 @Override
		 protected void onCreate(Bundle savedInstanceState) {
		     super.onCreate(savedInstanceState);
		     getActionBar().hide();


			 getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
             getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			 i=new Intent (this,LoginActivity.class);
		  

		   
             
		     alertDialogBuilder
		             .setMessage("Do you want to Logout")
		             .setCancelable(false)
		             .setNegativeButton("No", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int id) {
			                    
		                	  dialog.cancel();
							  startActivity(i);
		                	  
		                 	  finish();
		                 }
		             })
		             
		             
		             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int id) {


							 writestateToInternalStorage();
							 dialog.cancel();

		                      finish();
		                 }
		             });
		             
		     
		     alertDialogBuilder.show();
		 }

	public boolean isOnline()
	{
	   ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	   NetworkInfo netInfo = cm.getActiveNetworkInfo();
	   return netInfo != null && netInfo.isConnectedOrConnecting();
    }
		 
		
	 @Override
	  public boolean onTouchEvent(MotionEvent event) 
	 {
	    // If we've received a touch notification that the user has touched
	    // outside the app, finish the activity.
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	      //finish();
	      return false;
	    }

	    // Delegate everything else to Activity.
	    return super.onTouchEvent(event);
	  }
	 
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
		    if(keyCode == KeyEvent.KEYCODE_BACK)
		   {
		   
		    return true;
		   }
		    
		    return false;
		}
	 
	 @Override
		protected void onResume()
		{
			super.onResume();
		}	
		
	 
	 @Override
		protected void onDestroy()
		{
			super.onDestroy();
			
		}

	private void writestateToInternalStorage()
	{
		byte b=0;
		try {

			FileOutputStream fos = openFileOutput("loginstatus.txt", Context.MODE_PRIVATE);

			fos.write(b);
			fos.close();

		}
		catch (Exception e)
		{
			Log.i("Error_In_login", e.getMessage());

		}
	}
}

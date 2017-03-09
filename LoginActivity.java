package com.example.sushilverma.mavync;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mobileno;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Intent intent;
    private  TextView terms_conditn,forgetpass;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Timer timer;
    private mycallable callab;
    private Thread callabtask;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String userid=null;
    private String Imagelinkurl=null;
    private String Image_url_Received=null;
    private String previous_url_Received=null;
    private String saved_url =null;
    boolean result=false;
    private FileInputStream fis=null;
    private	Bitmap pics=null;
    private ImageView dfltpics;
    private static volatile int x=1;
    private   Imageurl_download_thread imageurl_with_data;
    private SharedPreferences shipper_data;

    private static volatile boolean profile_pics_changed=false;
    private  Hit_image1 hitimage1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();

        shipper_data=getSharedPreferences("profiledata",MODE_PRIVATE);
        editor=shipper_data.edit();




        intent=new Intent(this,HomeActivity.class);
        mobileno = (AutoCompleteTextView) findViewById(R.id.mobileno);
        mPasswordView = (EditText) findViewById(R.id.password);
       // terms_conditn=(TextView)findViewById(R.id.login_tc) ;
       // forgetpass=(TextView)findViewById(R.id.forgetpass) ;

        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);



       /* terms_conditn.setOnClickListener(this);
        forgetpass.setOnClickListener(this);*/

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mobileno.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String mobile_no = mobileno.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;

        View focusView = mobileno;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid mobile_no address.
        if (TextUtils.isEmpty(mobile_no)) {
            mobileno.setError(getString(R.string.error_field_required));
            focusView = mobileno;
            cancel = true;
        } else if (!ismobile_noValid(mobile_no)) {
            mobileno.setError("Invalid Mob. No.");
            focusView = mobileno;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {

            mProgressView.setVisibility(View.VISIBLE);
            //showProgress(true);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mobileno.getWindowToken(), 0);

           // hideSoftKeyboard(focusView);

            mAuthTask = new UserLoginTask(mobile_no, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean ismobile_noValid(String mobile_no) {
        //TODO: Replace this with your own logic
        return mobile_no.length()==10;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >2;
    }

    public void hideSoftKeyboard(View view) {
        if(view.isFocused()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
           /* case R.id.login_tc:

                            break;
            case R.id.forgetpass:
*/
                           //break;
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private  String mmobile_no=null;
        private  String mPassword=null;

        UserLoginTask(String mno, String psword) {
            mmobile_no = mno;
            mPassword = psword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
           boolean result=false;

            hit_server hit=new hit_server(mmobile_no,mPassword);
            Thread t=new Thread(hit);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int status=hit.getMyuser_status();
            if(status==1)
                result=true;
            else

                result=false;

            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            mProgressView.setVisibility(View.INVISIBLE);
            if (success) {

                writestateToInternalStorage();
                if(!RatingHit())
                {
                    startService(new Intent(getApplicationContext(),Confrimvehicledownloadservice.class));
                    startActivity(intent);
                }

                finish();


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            mProgressView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgressView.setVisibility(View.INVISIBLE);
        }
    }

    private class hit_server implements Runnable
    {
        private String myuserid=null;
        private int myuser_status;
        private String local=null;
        private String mobile_no;
        private String password;
        private String url;
        private HttpURLConnection con=null;
        private BufferedReader bufferinput =null;
        private JSONObject responseObj;

        public String getMyuserid() {
            return myuserid;
        }
        public int getMyuser_status() {
            return myuser_status;
        }



        public hit_server(String mob,String pass)
        {
            mobile_no=mob;
            password=pass;
            url ="http://121.241.125.91/cc/mavyn/online/customerlogin.php?username="+mobile_no+"&password="+password;
        }


        @Override
        public void run()
        {

            try
            {

                URL urll = new URL(url);
                con = (HttpURLConnection) urll.openConnection();
                bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
                local = bufferinput.readLine();
                System.out.println("json data"+local);
                responseObj = new JSONObject(local);
                JSONArray countryListObj = responseObj.optJSONArray("loginstatus");  //.getJSONArray("transit");
                int length=countryListObj.length();

                for (int i=0; i<length; i++)
                {

                    JSONObject jsonChildNode=countryListObj.getJSONObject(i);
                    myuser_status=Integer.parseInt(jsonChildNode.optString("status").toString());
                    myuserid =jsonChildNode.optString("userid");

                    if(myuserid!=null)
                    {
                        SharedPreferences userid=getSharedPreferences("U_id",MODE_PRIVATE);
                        SharedPreferences.Editor editor=userid.edit();

                        editor.putString("userid",myuserid);
                        editor.commit();

                        downloadprofiledata(myuserid);

                    }

                }

                bufferinput.close();
                con.disconnect();

            }
            catch(IOException e)
            {
                e.printStackTrace();
                Log.i("Erro", "data downloading erro");

            }

            catch (JSONException e)
            {
                e.printStackTrace();
                System.out.println("Error in reading json object");
            }

        }

    }

    public void downloadprofiledata(String userid)
    {


        Imagelinkurl="http://121.241.125.91/cc/mavyn/online/shipperprofile?msg="+userid;
        imageurl_with_data=new Imageurl_download_thread(Imagelinkurl);
        Thread t= new Thread(imageurl_with_data);
        t.start();
        try
        { //
            t.join();
        } catch (InterruptedException e) {

            //Toast.makeText(this,"Error in downloading",Toast.LENGTH_LONG).show();
        }

        Image_url_Received=imageurl_with_data.getImageurl_received_local();


        if(Image_url_Received!=null)
        {

            if(profile_pics_changed)
            {

                hitimage1=new Hit_image1(Image_url_Received);
                Thread tt= new Thread(hitimage1);
                tt.start();
                try
                { //
                    tt.join();
                }
                catch (InterruptedException e)
                {

                    Toast.makeText(this,"Image download error",Toast.LENGTH_SHORT).show();
                }



            }
        }

    }

    class Hit_image1 implements Runnable
    {
        Bitmap bitmap=null;
        InputStream iStream = null;
        String strUrl=null;
        String text=null;

        public Hit_image1(String myurl)
        {
            strUrl=myurl;
        }


        @Override
        public void run()
        {


            text=strUrl;

            text=strUrl;
            try{
                URL url = new URL(strUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                iStream = urlConnection.getInputStream();

                if(iStream!=null)

                    bitmap = BitmapFactory.decodeStream(iStream);

                saveImageToInternalStorage(bitmap);
                iStream.close();

                Log.i("image saved","image savaed");

            }
            catch(Exception e)
            {
                Log.d("Exception downloading", e.toString());

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.id09);
                saveImageToInternalStorage(bitmap);
            }

        }
    }

    class Imageurl_download_thread implements Runnable {

        private volatile String imageurl_received_local=null;
        private volatile String  first_name;
        private volatile String  email_temp;
        private volatile String  Mobileno;
        private volatile String acc_no;
        private volatile String ifccode_temp;
        private volatile String bank_name;
        private final String localurl;
        private JSONObject jsonResponse;
        public String getImageurl_received_local() {
            return imageurl_received_local;
        }


        public Imageurl_download_thread(final String localString)
        {
            localurl=localString;
        }

        @Override
        public void run() {
            try {
                URL myurl=new URL(localurl);
                HttpURLConnection connection_4_image_url=(HttpURLConnection)myurl.openConnection();
                if (connection_4_image_url.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP error code : "+ connection_4_image_url.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((connection_4_image_url.getInputStream())));
                String local=null;
                try {
                    local = br.readLine();
                } catch (Exception e) {

                    //Toast.makeText(getActivity(),"Error in image Downloading",Toast.LENGTH_LONG).show();
                }

                System.out.println("the recived url"+local);

                if(local!=null)
                {



                    try {
                        jsonResponse = new JSONObject(local);

                        JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");


                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                        imageurl_received_local=jsonChildNode.optString("imageurl").toString();
                        first_name =jsonChildNode.optString("firstname");
                        email_temp=jsonChildNode.optString("email");
                        Mobileno = jsonChildNode.optString("mobileno");
                        bank_name = jsonChildNode.optString("Bankname");
                        ifccode_temp=jsonChildNode.optString("IFSC");
                        acc_no=jsonChildNode.optString("Accountno");


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if(imageurl_received_local!=null)
                    {

                        previous_url_Received=ReadulrFromInternalStorage();

                        if(previous_url_Received==null)
                        {

                            writeUrl_addressToInternalStorage(imageurl_received_local);
                            profile_pics_changed=true;

                        }

                        else
                        {

                            if(!previous_url_Received.equals(imageurl_received_local))
                            {
                                writeUrl_addressToInternalStorage(imageurl_received_local);

                                profile_pics_changed=true;
                            }


                        }

                        if(previous_url_Received==null || !previous_url_Received.equals(imageurl_received_local)) {

                            editor.putString("first_name", first_name);
                            editor.putString("email_temp", email_temp);
                            editor.putString("Mobileno", Mobileno);
                            editor.putString("bank_name", bank_name);
                            editor.putString("ifccode_temp", ifccode_temp);
                            editor.putString("acc_no", acc_no);
                            editor.commit();
                        }
                    }


                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    public void saveImageToInternalStorage(Bitmap image)
    {

        try {



            FileOutputStream fos = openFileOutput("desiredFilename1.png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());

        }
    }


    private void writeUrl_addressToInternalStorage(String localurl)
    {
        String test=localurl;
        try {

            FileOutputStream fos = openFileOutput("url1.txt", Context.MODE_PRIVATE);
            fos.write(test.getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            Log.e("Error_In_status_saveTo", e.getMessage());

        }
    }
    private String ReadulrFromInternalStorage()
    {

        String urllocal=null;
        StringBuffer buffer = new StringBuffer();

        try{

            FileInputStream fis = openFileInput("url1.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            if (fis!=null)
                urllocal = reader.readLine();

            fis.close();

        }catch(IOException e)
        {
            e.printStackTrace();
            return urllocal;
        }

        return urllocal;
    }

    private void writestateToInternalStorage()
    {
        byte b=1;
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



    private boolean RatingHit()
    {


        boolean rating_status=false;
        sharedpreferences = getSharedPreferences("U_id",SplasScreen.MODE_PRIVATE);
        userid= sharedpreferences.getString("userid", "null");

        if( isOnline())
        {

            callab=new mycallable(userid);
            callabtask=new Thread(callab);
            callabtask.start();
            try {
                callabtask.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                System.out.println("Thread Interrupted");
            }
            Log.i("data sent...", " ");


            int statusformobile=callab.getstatusformobile();
            if(statusformobile ==1)

            {
                String shipment_id = callab.getRating_shipment_id();
                String from = callab.getFrom();
                String to = callab.getTo();
                String date = callab.getDelivery_date();


                Intent myrating_screen=new Intent(this,Rating_popup.class);
                myrating_screen.putExtra("shipment_id",shipment_id);
                myrating_screen.putExtra("from",from);
                myrating_screen.putExtra("to",to);
                myrating_screen.putExtra("date",date);


                myrating_screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myrating_screen);

                rating_status=true;

            }

        }

        return rating_status;

    }


    boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class mycallable implements Runnable
    {

        private volatile String  shipment_id=null;
        private volatile String   customer_id=null;
        private volatile String  delivery_date=null;
        private volatile String  from=null;
        private volatile String to=null;
        private volatile int statusformobile =0;

        public int getstatusformobile() {
            return statusformobile;
        }

        public String getDelivery_date() {
            return delivery_date;
        }


        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public String getRating_shipment_id()

        {
            return shipment_id;
        }


        mycallable(String customer_id)
        {
            this.customer_id=customer_id;
        }

        public void run()
        {

            try {

                final String request="http://121.241.125.91/cc/mavyn/online/customerapp.php?rating=ratingpending"+"&"+"userid="+userid;
                JSONObject jsonResponse;
                Log.d("request", ""+request);

                URL url = new URL(request);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                System.out.println("data uploades");

                BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                String local=null;
                try
                {
                    local =br.readLine();
                    Log.i("Json data received..",local);
                }

                catch (Exception e)
                {

                    // Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
                }

                System.out.println("the recived url"+local);

                try {
                    jsonResponse = new JSONObject(local);
                    JSONArray jsonMainNode=jsonResponse.optJSONArray("ratingpending");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);


                    statusformobile=Integer.parseInt(jsonChildNode.optString("statusformobile").toString());

                    if(statusformobile==1)
                    {

                        shipment_id=jsonChildNode.optString("shipment_no").toString();
                        from=jsonChildNode.optString("from_location").toString();
                        to=jsonChildNode.optString("to_location").toString();
                        delivery_date=jsonChildNode.optString("stagesupdate_date").toString();

                    }



                }

                catch(JSONException js)
                {
                    js.printStackTrace();
                }

                br.close();
                con.disconnect();



            }

            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


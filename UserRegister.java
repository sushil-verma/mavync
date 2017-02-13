package com.example.sushilverma.mavync;


import android.app.IntentService;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

   private TextInputEditText firstname;
   private TextInputEditText lasttname;
   private  TextInputEditText emailid;
   private  TextInputEditText mobileno;
   private  EditText password;
   private Button register_button;
   private  SendRegister object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setupActionBar();

        firstname=(TextInputEditText)findViewById(R.id.firstname);
        lasttname=(TextInputEditText)findViewById(R.id.lastname);
        emailid=(TextInputEditText)findViewById(R.id.register_email);
        mobileno=(TextInputEditText)findViewById(R.id.register_mobileno);
        password=(EditText)findViewById(R.id.register_password);
        register_button=(Button)findViewById(R.id.register_button);

        register_button.setOnClickListener(this);
        object =new SendRegister(firstname.getText().toString(),lasttname.getText().toString(),emailid.getText().toString(),mobileno.getText().toString(),password.getText().toString());

    }

    @Override
    public void onClick(View view) {

        new Thread(object).start();

    }

    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
        }
    }



    private  class SendRegister implements Runnable
    {
        private volatile String firstname;
        private volatile String lastname;
        private volatile String emailid;
        private volatile String mobileno;
        private volatile String password;
        private String json_string;
        private JSONObject jsonResponse;
        private int status=0;
        private HttpURLConnection con;
        private URL url = null;
        private   BufferedReader br;

        public SendRegister(String fname,String lname,String email,String mobileno,String password)
        {

            firstname=fname;
            lastname=lname;
            email=email;
            mobileno=mobileno;
            password=password;
        }

        public void run()
        {

            try {

                Log.i("ready to hit the server", " ....");

               final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+"%23"+"request_document";
                Log.d("request for second page", ""+request);

                try {
                    url = new URL(request);
                    con = (HttpURLConnection) url.openConnection();
                    br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                    json_string = br.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    jsonResponse = new JSONObject(json_string);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                JSONArray jsonMainNode=jsonResponse.optJSONArray("Register");

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);


                status=Integer.parseInt(jsonChildNode.optString("invoice").toString());

                if(status==1)
                    Toast.makeText(getApplicationContext(),"Registration Successfull",Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getApplicationContext(),"Registration Successfull",Toast.LENGTH_SHORT).show();

            }
            catch(JSONException js)
            {
                js.printStackTrace();
            }


            try {
                  br.close();
                  con.disconnect();
               } catch (IOException e) {
                  e.printStackTrace();
               }


        }
    }


}

package com.example.sushilverma.mavync;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class First extends AppCompatActivity implements View.OnClickListener{

    private Button signin;
    private Button singup;
    private Intent signscreen;
    private Intent signupscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getSupportActionBar().hide();

        //getSupportActionBar().hide();

        signin=(Button)findViewById(R.id.signin);
        singup=(Button)findViewById(R.id.SignUp);

        signin.setOnClickListener(this);
        singup.setOnClickListener(this);
        signscreen=new Intent(this,LoginActivity.class);
        signupscreen=new Intent(this,UserRegister.class);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {

            case R.id.SignUp:
                            startActivity(signscreen);

                            break;

            case R.id.signin:

                            startActivity(signupscreen);

                            break;

        }

    }
}

package com.example.sushilverma.mavync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Halting_charges extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halting_charges);
        WebView wv= (WebView)findViewById(R.id.webview);
        wv.loadUrl("file:///android_asset/Mavyn.html");
    }
}

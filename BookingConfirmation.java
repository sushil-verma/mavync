package com.example.sushilverma.mavync;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class BookingConfirmation extends AppCompatActivity {

   private  Button okbutton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        BookingConfirmation.CustomDialog.Builder alertDialogBuilder = new BookingConfirmation.CustomDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>Booking Status</font>"));

        // set dialog message
        alertDialogBuilder
                .setMessage(Html.fromHtml("<font color='#FF7F27'>Booking Confirmed</font>"))
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Ok</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        dialog.cancel();

                        finish();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    @Override
    protected void onResume()
    {
        super.onResume();

    }

private static class CustomDialog extends AlertDialog
{

    private CustomDialog(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Resources res = getContext().getResources();
        final int id = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = findViewById(id);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(Color.RED);
        }
    }

}

}

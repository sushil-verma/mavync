package com.example.sushilverma.mavync;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.FileOutputStream;

/**
 * Created by sushil.verma on 11/19/2016.
 */


public  class App_exit_alert_freg extends DialogFragment {

    public static App_exit_alert_freg newInstance(int title) {
        App_exit_alert_freg frag = new App_exit_alert_freg();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        LayoutInflater inflater = getActivity().getLayoutInflater();//getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogtitle_for_exit, null);



        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert)
                .setTitle(title)
                .setCustomTitle(view)
                .setMessage(" ")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                                ((HomeActivity)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                dismiss();
                            }
                        }
                )
                .create();


    }


}
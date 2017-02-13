package com.example.sushilverma.mavync;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;

/**
 * Created by sushil.verma on 11/19/2016.
 */


public  class Logoutfreg extends DialogFragment {

    public static Logoutfreg newInstance(int title) {
        Logoutfreg frag = new Logoutfreg();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        LayoutInflater inflater = getActivity().getLayoutInflater();//getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogtitle, null);



        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert)
                .setTitle(title)
                .setCustomTitle(view)
                .setMessage(" ")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                writestateToInternalStorage();
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

    private void writestateToInternalStorage()
    {
        byte b=1;
        try {

            FileOutputStream fos = getActivity().openFileOutput("loginstatus.txt", Context.MODE_PRIVATE);

            fos.write(b);
            fos.close();

        }
        catch (Exception e)
        {
            Log.i("Error_In_login", e.getMessage());

        }
    }
}
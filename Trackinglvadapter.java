package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;


public class Trackinglvadapter extends RecyclerView.Adapter<Trackinglvadapter.ViewHolder> {

    private static final String TAG = "Trackinglvadapter";
    private ArrayList<Tracklistdataclass> transit ;//
    private Context context;


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  final TextView trucknumber;
        private  final TextView decription;
        private  final TextView drivername;
        private  final ImageView driverImage;


        public TextView getTrucknumber() {
            return trucknumber;
        }

        public TextView getDecription() {
            return decription;
        }

        public TextView getDrivername() {
            return drivername;
        }

        public ImageView getDriverimage() {
            return driverImage;
        }

        public ViewHolder(View v) {
            super(v);

            trucknumber=(TextView)v.findViewById(R.id.vehicle_number);
            decription=(TextView)v.findViewById(R.id.description);
            drivername=(TextView)v.findViewById(R.id.driver_sahab_name);
            driverImage=(ImageView)v.findViewById(R.id.tracklistdriverimage);


        }


    }


    public Trackinglvadapter(Context context,ArrayList<Tracklistdataclass> dataSet) {
        transit = dataSet;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tracklistview, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");


        viewHolder.getTrucknumber().setText(transit.get(position).getVehicle_number());
        viewHolder.getDecription().setText(transit.get(position).getVehicle_description());
        viewHolder.getDrivername().setText(transit.get(position).getDriver_name());
        final ImageView tempoerimage=viewHolder.driverImage;
        Glide.with(context).load(transit.get(position).getDriver_image_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.driverImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                tempoerimage.setImageDrawable(circularBitmapDrawable);
            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transit.size();
    }
}

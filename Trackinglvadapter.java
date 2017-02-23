package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class Trackinglvadapter extends RecyclerView.Adapter<Trackinglvadapter.ViewHolder> {
    private static final String TAG = "ConfirmAdapter";



    private ArrayList<Tracklistdataclass> transit ;//
    private Context mContext;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView trucknumber=null;
        private  TextView decription=null;
        private  TextView drivername=null;
        private  TextView current_date_time=null;
        private  RoundImage driverImage=null;


        public TextView getTrucknumber() {
            return trucknumber;
        }

        public TextView getDecription() {
            return decription;
        }

        public TextView getDrivername() {
            return drivername;
        }

        public TextView getCurrentDataTime() {
            return current_date_time;
        }

        public RoundImage getDriverimage() {
            return driverImage;
        }

        public ViewHolder(View v) {
            super(v);

            /*// Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });*/


            trucknumber=(TextView)v.findViewById(R.id.vehicle_number);
            decription=(TextView)v.findViewById(R.id.description);
            drivername=(TextView)v.findViewById(R.id.driver_sahab_name);
            driverImage=(RoundImage)v.findViewById(R.id.tracklistdriverimage);


        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public Trackinglvadapter(Context context,ArrayList<Tracklistdataclass> dataSet) {
        transit = dataSet;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tracklistview, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");


        viewHolder.getTrucknumber().setText(transit.get(position).getVehicle_number());
        viewHolder.getDecription().setText(transit.get(position).getVehicle_description());
        viewHolder.getDrivername().setText(transit.get(position).getDriver_name());

        Glide.with(mContext).load(transit.get(position).getDriver_image_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.driverImage);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transit.size();
    }
}

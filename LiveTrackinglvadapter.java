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
import java.util.ArrayList;


public class LiveTrackinglvadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "LiveTrackinglvadapter";
    private static final int EMPTY_VIEW = 10;
    private ArrayList<JourneytableviewObject> transit;
    private Context context;

    public LiveTrackinglvadapter(Context context, ArrayList<JourneytableviewObject> dataSet) {
        transit = dataSet;
        this.context=context;
    }


    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);



        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private  final TextView vehiclecurrentlocation;
        private  final TextView vehiclecurrenttime;


        public TextView getVehicleCurrentLocation() {
            return vehiclecurrentlocation;
        }
        public TextView getVehicleCurrentTime() {
            return vehiclecurrenttime;
        }

        public ViewHolder(View v) {
            super(v);

            vehiclecurrentlocation=(TextView)v.findViewById(R.id.currentlocation);
            vehiclecurrenttime =(TextView)v.findViewById(R.id.currenttime);
        }


    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {



        View v;


        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.no_data_availble, viewGroup, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }


       v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.livetracklistview, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Holder, int position) {


        if (Holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) Holder;
            viewHolder.getVehicleCurrentLocation().setText(transit.get(position).getCurrent_address());
            viewHolder.getVehicleCurrentTime().setText(transit.get(position).getDate_time());
        }


        }



    @Override
    public int getItemCount() {

        return  transit.size() > 0 ? transit.size() : 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (transit.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }
}

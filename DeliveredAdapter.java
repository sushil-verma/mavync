package com.example.sushilverma.mavync;


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


public class DeliveredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ConfirmAdapter";
    private static final int EMPTY_VIEW =10 ;
    private Context context;
    private ArrayList<Transit_record> transit=new ArrayList<>();
    private  LayoutInflater inflater;

    public DeliveredAdapter(Context context,ArrayList<Transit_record> dataSet) {
        transit = dataSet;
        this.context=context;
    }


    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);



            }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView myshipment;
        private final TextView date_time;
        private final TextView truck_type;
        private final TextView from;
        private final TextView to;
        private final ImageView driver_image;

        public TextView myshipment() {
            return myshipment;
        }
        public TextView date_time() {
            return date_time;
        }
        public TextView truck_type() {
            return truck_type;
        }
        public TextView from() {
            return from;
        }

        public TextView to() {
            return to;
        }


        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });


            myshipment=(TextView)v.findViewById(R.id.shipmentno1);
            date_time=(TextView)v.findViewById(R.id.booked_datetime);
            truck_type=(TextView)v.findViewById(R.id.vehiclnumber);
            from=(TextView)v.findViewById(R.id.fromshipment);
            to=(TextView)v.findViewById(R.id.tocityreport);
            driver_image=(ImageView) v.findViewById(R.id.driver_image);

        }

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        View v;


        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.no_delivered_booking, viewGroup, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }


           v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_row_item, viewGroup, false);
           return new ViewHolder(v);



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Holder, int position) {

        if (Holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) Holder;


            viewHolder.myshipment().setText(String.valueOf(transit.get(position).getshipmentNo()));

            viewHolder.date_time().setText(transit.get(position).getDate_time());
            viewHolder.truck_type().setText(transit.get(position).getTruck_type());
            viewHolder.from().setText(transit.get(position).getFrom());
            viewHolder.to().setText(transit.get(position).getTo());

            final ImageView tempoerimage=viewHolder.driver_image;



            String driverurl=transit.get(position).getDriverimg();
            if (driverurl == null) {

                Glide.with(context).load(R.drawable.driver).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.driver_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        tempoerimage.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }

            else

                Glide.with(context).load(transit.get(position).getDriverimg()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.driver_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        tempoerimage.setImageDrawable(circularBitmapDrawable);
                    }
                });

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

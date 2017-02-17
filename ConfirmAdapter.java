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
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;


public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.ViewHolder> {
    private static final String TAG = "ConfirmAdapter";
    private  Context context;



    private ArrayList<Transit_record> transit=new ArrayList<Transit_record>(20);

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView myshipment;
        private final TextView date_time;
        private final TextView vehiclenumber;
        private final TextView from;
        private final TextView to;
        private final ImageView driver_image;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });


            myshipment=(TextView)v.findViewById(R.id.shipmentno1);
            date_time=(TextView)v.findViewById(R.id.booked_datetime);
            vehiclenumber=(TextView)v.findViewById(R.id.vehiclnumber);
            from=(TextView)v.findViewById(R.id.fromshipment);
            to=(TextView)v.findViewById(R.id.tocityreport);
            driver_image=(ImageView) v.findViewById(R.id.driver_image);

        }

        public TextView myshipment() {
            return myshipment;
        }

        public TextView date_time() {
            return date_time;
        }
        public TextView vehiclenumber() {
            return vehiclenumber;
        }

        public TextView from() {
            return from;
        }

        public TextView to() {
            return to;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ConfirmAdapter(Context context, ArrayList<Transit_record> dataSet) {
        transit = dataSet;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");


        viewHolder.myshipment().setText(String.valueOf(transit.get(position).getshipmentNo()));
        viewHolder.date_time().setText(transit.get(position).getDate_time());
        viewHolder.vehiclenumber().setText(transit.get(position).getTruck_type());
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transit.size();
    }
}

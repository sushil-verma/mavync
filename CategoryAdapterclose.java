package com.example.sushilverma.mavync;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapterclose extends RecyclerView.Adapter<CategoryAdapterclose.ViewHolder> {
    private ArrayList<Vehicle_category> mDataset;
    private int selectedItem = 0;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder



    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView vehicle_weight;
        public TextView vehicle_tyre_no;
        public ImageView image;
        LinearLayout list_row;

        public ViewHolder(View v) {
            super(v);
            v.setClickable(true);
            vehicle_weight = (TextView) v.findViewById(R.id.vehicle_weight);
            vehicle_tyre_no = (TextView) v.findViewById(R.id.vehicle_tyre_no);
            image=(ImageView)v.findViewById(R.id.vehicle_image);
               /* v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Redraw the old selection and the new
                        notifyItemChanged(selectedItem);
                        selectedItem = getLayoutPosition();
                        notifyItemChanged(selectedItem);
                    }
                });*/
            list_row = (LinearLayout) v.findViewById(R.id.adapter_layout);
        }
    }

    public void add(int position, Vehicle_category item)
    {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }


    public Vehicle_category getitem(int position)
    {
        return  mDataset.get(position);

    }

    public void remove(Vehicle_category item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapterclose(ArrayList<Vehicle_category> myDataset, Context context) {
        mDataset = myDataset;


        mPref = context.getSharedPreferences("personclose", Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryAdapterclose.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_adapter_xml, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(selectedItem == 0)
        {
           // holder.itemView.setSelected(true);
            mDataset.get(0).setSelected(true);

        }

        else
            mDataset.get(0).setSelected(false);
            final Vehicle_category data = mDataset.get(position);

        int vehicle_type_id=Integer.parseInt(data.getId());

        switch (vehicle_type_id)
        {
            case 47:
                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id047);
                } else {
                    holder.image.setImageResource(R.drawable.id47);
                }
                break;
            case 78:
                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id047);
                } else {
                    holder.image.setImageResource(R.drawable.id47);
                }
                break;

            case 92:
                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id092);
                } else {
                    holder.image.setImageResource(R.drawable.id92);
                }
                break;

            case 66:
                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id089);
                } else {
                    holder.image.setImageResource(R.drawable.id89);
                }
                break;

            case 89:
                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id089);
                } else {
                    holder.image.setImageResource(R.drawable.id89);
                }
                break;


            default:


                holder.vehicle_weight.setText(data.getWeight_of_truck());
                holder.vehicle_tyre_no.setText(data.getNo_of_tyre());
                if (mDataset.get(position).isSelected()) {
                    holder.image.setImageResource(R.drawable.id046);
                } else {
                    holder.image.setImageResource(R.drawable.id46);
                }

        }



           /* holder.vehicle_weight.setText(data.getWeight_of_truck());
            holder.vehicle_tyre_no.setText(data.getNo_of_tyre());


           if (mDataset.get(position).isSelected()) {
              holder.image.setImageResource(R.drawable.selected_vehicle);
           } else

           {
             holder.image.setImageResource(R.drawable.roundtruck);
           }
*/
        // holder.txtFooter.setText("Footer: " + mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setSelected(int pos) {

        selectedItem=pos;

        try {
            if (mDataset.size() > 1) {
                mDataset.get(mPref.getInt("position", 0)).setSelected(false);
                mEditor.putInt("position", pos);
                mEditor.commit();
            }
            mDataset.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
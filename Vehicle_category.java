package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 9/24/2016.
 */
public class Vehicle_category {

    private String id;
    private String no_of_tyre;
    private String weight_of_truck;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }



    public Vehicle_category(String id, String no_of_tyre, String weight_of_truck) {
        this.id = id;

        this.no_of_tyre = no_of_tyre;
        this.weight_of_truck = weight_of_truck;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getNo_of_tyre() {
        return no_of_tyre;
    }

    public void setNo_of_tyre(String no_of_tyre) {
        this.no_of_tyre = no_of_tyre;
    }

    public String getWeight_of_truck() {
        return weight_of_truck;
    }

    public void setWeight_of_truck(String weight_of_truck) {
        this.weight_of_truck = weight_of_truck;
    }


}

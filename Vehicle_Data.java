package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 9/24/2016.
 */
public class Vehicle_Data {

    //private String id;
    private double latitute;
    private double longitute;
    private String description;

    public Vehicle_Data(double latitute,double longitute, String description) {
        //this.id = id;
        this.longitute = longitute;
        this.latitute = latitute;
        this.description=description;


    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

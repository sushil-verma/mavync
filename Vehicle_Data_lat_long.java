package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 9/24/2016.
 */
public class Vehicle_Data_lat_long {

    //private String id;
    private String latitute;
    private String longitute;
    private String description;

    public Vehicle_Data_lat_long(String latitute, String longitute, String description) {
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

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

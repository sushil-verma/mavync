package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 2/23/2017.
 */

class Tracklistdataclass{

    String vehicle_number=null;
    String vehicle_description=null;

    String driver_name=null;
    String driver_image_url=null;

    public Tracklistdataclass(String vehicleno,String address,String drivername,String driverimage ) {

        vehicle_number=vehicleno;
        vehicle_description=address;
        driver_name=drivername;
        driver_image_url=driverimage;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }
    public String getVehicle_description() {
        return vehicle_description;
    }
    public String getDriver_name() {
        return driver_name;
    }
    public String getDriver_image_url() {
        return driver_image_url;
    }


}

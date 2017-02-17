package com.example.sushilverma.mavync;

/**
 * Created by sushil.verma on 11/15/2016.
 */

class Transit_record {

    String date_time = null;
    String truck_type = null;
    String to = null;
    String from = null;
    String driverimg = null;
    int shipmentNo = 0;

    public Transit_record(String date_time, String truck_type, String to, String from, int shipment ,String driverimg) {
        this.date_time = date_time;
        this.truck_type = truck_type;
        this.to = to;
        this.from = from;
        shipmentNo = shipment;
        this.driverimg=driverimg;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }



    public int getshipmentNo() {
        return shipmentNo;
    }

    public String getDriverimg() {
        return driverimg;
    }
}
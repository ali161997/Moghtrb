package com.moghtrb.models;

public class MyLatLong {
    private double lat = 0.0;
    private double lon = 0.0;

    public MyLatLong() {
    }

    public MyLatLong(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return lat + " " + lon;
    }

}

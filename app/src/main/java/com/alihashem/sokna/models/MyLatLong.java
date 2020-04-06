package com.alihashem.sokna.models;

public class MyLatLong {
    private double lat;
    private double lon;

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

}

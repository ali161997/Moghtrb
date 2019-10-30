package com.example.sokna.models;


public class Place_require {

    private String Place_requireName;
    private double latitude;
    private double longitude;

    public Place_require(String place_requireName, double latitude, double longitude) {
        Place_requireName = place_requireName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlace_requireName() {
        return Place_requireName;
    }

    public void setPlace_requireName(String place_requireName) {
        Place_requireName = place_requireName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Place_require() {
        Place_requireName = "default";
        latitude = 27.180134;
        longitude = 31.189283;
    }


}

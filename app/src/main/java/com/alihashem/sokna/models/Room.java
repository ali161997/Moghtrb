package com.alihashem.sokna.models;


public class Room {
    private double price; //
    private int rate;//
    private int num_reviews;//
    private String street;//
    private int number_beds;//
    private String hostId;
    private String city;
    private String region;
    private String urlImage1;
    private String departId;
    private String roomId;
    private String arAddress;
    private MyLatLong latLng;
    private Integer cityIndex;
    private Integer regionIndex;
    private String gender;

    public Room() {

    }

    public Room(double price, int rate, int num_reviews, String street, int number_beds, String hostId, String city,
                String region, String urlImage1, String departId,
                String roomId, String arAddress,
                MyLatLong latLng, String gender,
                Integer regionIndex, Integer cityIndex) {
        this.price = price;
        this.rate = rate;
        this.num_reviews = num_reviews;
        this.street = street;
        this.number_beds = number_beds;
        this.hostId = hostId;
        this.city = city;
        this.region = region;
        this.urlImage1 = urlImage1;
        this.departId = departId;
        this.roomId = roomId;
        this.arAddress = arAddress;
        this.latLng = latLng;
        this.gender = gender;
        this.regionIndex = regionIndex;
        this.cityIndex = cityIndex;
    }

    public Integer getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(Integer cityIndex) {
        this.cityIndex = cityIndex;
    }

    public Integer getRegionIndex() {
        return regionIndex;
    }

    public void setRegionIndex(Integer regionIndex) {
        this.regionIndex = regionIndex;
    }

    public String getGender() {
        return gender;
    }

    public double getPrice() {
        return price;
    }

    public int getRate() {
        return rate;
    }

    public int getNum_reviews() {
        return num_reviews;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber_beds() {
        return number_beds;
    }

    public String getHostId() {
        return hostId;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getUrlImage1() {
        return urlImage1;
    }

    public String getDepartId() {
        return departId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getArAddress() {
        return arAddress;
    }

    public MyLatLong getLatLng() {
        return latLng;
    }


}

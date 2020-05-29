package com.alihashem.moghtrb.models;


public class Room {
    private Double price; //
    private Integer rate;//
    private Integer num_reviews;//
    private String street;//
    private Integer bedsAvailable;//
    private String hostId;
    private String city;
    private String region;
    private String urlImage1;
    private String departId;
    private String roomId;
    private String arAddress;
    private MyLatLong latLng;
    private Integer totalBeds;
    private Integer cityIndex;
    private Integer regionIndex;
    private String gender;
    public Room() {

    }

    public Room(double price,
                int rate,
                int num_reviews,
                String street,
                int bedsAvailable,
                Integer totalBeds,
                String hostId,
                String city,
                String region,
                String urlImage1,
                String departId,
                String roomId, String arAddress,
                MyLatLong latLng, String gender,
                Integer regionIndex, Integer cityIndex) {
        this.price = price;
        this.rate = rate;
        this.num_reviews = num_reviews;
        this.street = street;
        this.bedsAvailable = bedsAvailable;
        this.totalBeds = totalBeds;
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

    public void setBedsAvailable(Integer bedsAvailable) {
        this.bedsAvailable = bedsAvailable;
    }

    public Integer getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(Integer totalBeds) {
        this.totalBeds = totalBeds;
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public int getNum_reviews() {
        return num_reviews;
    }

    public void setNum_reviews(Integer num_reviews) {
        this.num_reviews = num_reviews;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getbedsAvailable() {
        return bedsAvailable;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUrlImage1() {
        return urlImage1;
    }

    public void setUrlImage1(String urlImage1) {
        this.urlImage1 = urlImage1;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getArAddress() {
        return arAddress;
    }

    public void setArAddress(String arAddress) {
        this.arAddress = arAddress;
    }

    public MyLatLong getLatLng() {
        return latLng;
    }

    public void setLatLng(MyLatLong latLng) {
        this.latLng = latLng;
    }


}

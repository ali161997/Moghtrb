package com.moghtrb.models;

public class BookingsModel {
    private String roomId;
    private String from;
    private String to;
    private String cashPayed;
    private String total;
    private String numGuests;
    private String city;
    private String region;
    private MyLatLong latLng;

    public MyLatLong getLatLng() {
        return latLng;
    }

    public void setLatLng(MyLatLong latLng) {
        this.latLng = latLng;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
    }

    private String hostPhone;

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

    public BookingsModel() {
    }

    public BookingsModel(String roomId, String from, String to,
                         String total, String cashPayed, String numGuests, String city, String region
            , String hostPhone, MyLatLong latLng) {
        this.hostPhone = hostPhone;
        this.latLng = latLng;
        this.roomId = roomId;
        this.from = from;
        this.to = to;
        this.total = total;
        this.numGuests = numGuests;
        this.cashPayed = cashPayed;
        this.city = city;
        this.region = region;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(String numGuests) {
        this.numGuests = numGuests;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCashPayed() {
        return cashPayed;
    }

    public void setCashPayed(String cashPayed) {
        this.cashPayed = cashPayed;
    }
}

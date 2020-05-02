package com.alihashem.moghtreb.models;

public class BookingsModel {
    private String roomId;
    private String dates;
    private Double cashPayed;

    public BookingsModel() {
    }

    public BookingsModel(String roomId, String dates, Double cashPayed) {
        this.roomId = roomId;
        this.dates = dates;
        this.cashPayed = cashPayed;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public Double getCashPayed() {
        return cashPayed;
    }

    public void setCashPayed(Double cashPayed) {
        this.cashPayed = cashPayed;
    }
}

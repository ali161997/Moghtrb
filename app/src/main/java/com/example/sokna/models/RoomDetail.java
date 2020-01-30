package com.example.sokna.models;

public class RoomDetail {
    private Double bedPrice;
    private int numBedsInRoom;
    private int numRoomsInDepart;
    private int numBath;
    private double hostRate;
    private String location;
    private boolean haveBalcony;
    private int departOrder;
    private boolean internetAvailable;
    private boolean naturalGasAvailable;
    private boolean periodCleaning;
    private String moreLink;

    public RoomDetail() {
    }

    public RoomDetail(Double bedPrice, int numBedsInRoom, int numRoomsInDepart, int numBath, double hostRate, String location, boolean haveBalcony,
                      int departOrder, boolean internetAvailable, boolean naturalGasAvailable, boolean periodCleaning, String moreLink) {
        this.bedPrice = bedPrice;
        this.numBedsInRoom = numBedsInRoom;
        this.numRoomsInDepart = numRoomsInDepart;
        this.numBath = numBath;
        this.hostRate = hostRate;
        this.location = location;
        this.haveBalcony = haveBalcony;
        this.departOrder = departOrder;
        this.internetAvailable = internetAvailable;
        this.naturalGasAvailable = naturalGasAvailable;
        this.periodCleaning = periodCleaning;
        this.moreLink = moreLink;
    }


    public Double getBedPrice() {
        return bedPrice;
    }

    public void setBedPrice(Double bedPrice) {
        this.bedPrice = bedPrice;
    }

    public int getNumBedsInRoom() {
        return numBedsInRoom;
    }

    public void setNumBedsInRoom(int numBedsInRoom) {
        this.numBedsInRoom = numBedsInRoom;
    }

    public int getNumRoomsInDepart() {
        return numRoomsInDepart;
    }

    public void setNumRoomsInDepart(int numRoomsInDepart) {
        this.numRoomsInDepart = numRoomsInDepart;
    }

    public int getNumBath() {
        return numBath;
    }

    public void setNumBath(int numBath) {
        this.numBath = numBath;
    }

    public double getHostRate() {
        return hostRate;
    }

    public void setHostRate(double hostRate) {
        this.hostRate = hostRate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isHaveBalacon() {
        return haveBalcony;
    }

    public void setHaveBalacon(boolean haveBalcony) {
        this.haveBalcony = haveBalcony;
    }

    public int getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(int departOrder) {
        this.departOrder = departOrder;
    }

    public boolean isInternetAvailable() {
        return internetAvailable;
    }

    public void setInternetAvailable(boolean internetAvailable) {
        this.internetAvailable = internetAvailable;
    }

    public boolean isNaturalGasAvailable() {
        return naturalGasAvailable;
    }

    public void setNaturalGasAvailable(boolean naturalGasAvailable) {
        this.naturalGasAvailable = naturalGasAvailable;
    }

    public boolean isPeriodCleaning() {
        return periodCleaning;
    }

    public void setPeriodCleaning(boolean periodCleaning) {
        this.periodCleaning = periodCleaning;
    }

    public String getMoreLink() {
        return moreLink;
    }

    public void setMoreLink(String moreLink) {
        this.moreLink = moreLink;
    }


}

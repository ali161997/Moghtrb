package com.alihashem.moghtreb.models;

import java.util.HashMap;
import java.util.List;

public class RoomDetail {
    private int numRoomsInDepart;
    private int numBath;
    private int bedsAvailable;
    private double hostRate;
    private boolean haveBalcony;
    private int departOrder;
    private HashMap<String, Boolean> services;
    private List<String> urlsImage;

    public RoomDetail() {
    }

    public RoomDetail(int numRoomsInDepart, int numBath, int bedsAvailable,
                      double hostRate, boolean haveBalcony, int departOrder, HashMap<String, Boolean> services, List<String> urlsImage) {
        this.numRoomsInDepart = numRoomsInDepart;
        this.numBath = numBath;
        this.bedsAvailable = bedsAvailable;
        this.hostRate = hostRate;
        this.haveBalcony = haveBalcony;
        this.departOrder = departOrder;
        this.services = services;
        this.urlsImage = urlsImage;
    }


    public int getNumRoomsInDepart() {
        return numRoomsInDepart;
    }

    public int getNumBath() {
        return numBath;
    }

    public int getBedsAvailable() {
        return bedsAvailable;
    }

    public double getHostRate() {
        return hostRate;
    }

    public boolean isHaveBalcony() {
        return haveBalcony;
    }

    public int getDepartOrder() {
        return departOrder;
    }

    public HashMap<String, Boolean> getServices() {
        return services;
    }

    public List<String> getUrlsImage() {
        return urlsImage;
    }
}

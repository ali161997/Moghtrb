package com.moghtrb.models;

import java.util.HashMap;
import java.util.List;

public class RoomDetailModel {
    private int numRoomsInDepart;
    private int numBath;
    private double hostRate;
    private Boolean haveBalcony;
    private int departOrder;
    private HashMap<String, Boolean> services;
    private HashMap<String, Integer> floors;
    private List<String> urlsImage;
    private String hostComment;

    public RoomDetailModel() {
        floors = new HashMap<>();
        services = new HashMap<>();
    }

    public RoomDetailModel(int numRoomsInDepart, int numBath,
                           double hostRate, boolean haveBalcony, int departOrder, HashMap<String, Boolean> services, List<String> urlsImage) {
        this.numRoomsInDepart = numRoomsInDepart;
        this.numBath = numBath;
        this.hostRate = hostRate;
        this.haveBalcony = haveBalcony;
        this.departOrder = departOrder;
        this.services = services;
        this.urlsImage = urlsImage;
    }

    public String getHostComment() {
        return hostComment;
    }

    public void setHostComment(String hostComment) {
        this.hostComment = hostComment;
    }

    public HashMap<String, Integer> getFloors() {
        return floors;
    }

    public void setFloors(HashMap<String, Integer> floors) {
        this.floors = floors;
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

    public boolean isHaveBalcony() {
        return haveBalcony;
    }

    public void setHaveBalcony(boolean haveBalcony) {
        this.haveBalcony = haveBalcony;
    }

    public int getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(int departOrder) {
        this.departOrder = departOrder;
    }

    public HashMap<String, Boolean> getServices() {
        return services;
    }

    public void setServices(HashMap<String, Boolean> services) {
        this.services = services;
    }

    public List<String> getUrlsImage() {
        return urlsImage;
    }

    public void setUrlsImage(List<String> urlsImage) {
        this.urlsImage = urlsImage;
    }
}

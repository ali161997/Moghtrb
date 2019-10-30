package com.example.sokna.models;

public class Filter {
    private Double price_start;
    private Double price_end;
    private Boolean shared;
    private Roomate roomate;

    public Filter(Double price_start, Double price_end, Boolean shared, Roomate roomate) {
        this.price_start = price_start;
        this.price_end = price_end;
        this.shared = shared;
        this.roomate = roomate;
    }

    public Filter() {

        price_start = 0.0;
        price_end = 1000.0;
        shared = false;
        roomate = null;
    }


    public Double getPrice_start() {
        return price_start;
    }

    public void setPrice_start(Double price_start) {
        this.price_start = price_start;
    }

    public Double getPrice_end() {
        return price_end;
    }

    public void setPrice_end(Double price_end) {
        this.price_end = price_end;
    }

    public Boolean isSharedOrprivate() {
        return shared;
    }

    public void setSharedOrprivate(Boolean shared) {
        this.shared = shared;
    }

    public Roomate getRoomate() {
        return roomate;
    }

    public void setRoomate(Roomate roomate) {
        this.roomate = roomate;
    }


}

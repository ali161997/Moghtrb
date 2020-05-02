package com.alihashem.moghtreb.models;

public class Roomate {
    public Roomate(boolean noSmoking, String faculty) {
        this.noSmoking = noSmoking;
        this.faculty = faculty;
    }

    public Roomate() {
        noSmoking = true;
        faculty = null;
    }

    public boolean isNoSmoking() {
        return noSmoking;
    }

    public void setNoSmoking(boolean noSmoking) {
        this.noSmoking = noSmoking;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    private boolean noSmoking;
    private String faculty;

}

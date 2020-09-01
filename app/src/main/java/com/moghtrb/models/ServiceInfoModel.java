package com.moghtrb.models;

public class ServiceInfoModel {
    private String name;
    private String phone;
    private MyLatLong location;
    private String infoImage;
    private int likes;
    private int disLikes;

    public ServiceInfoModel() {
    }

    public ServiceInfoModel(String name, String phone, MyLatLong location, int likes, int disLikes, String infoImage) {
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.likes = likes;
        this.disLikes = disLikes;
        this.infoImage = infoImage;
    }

    public String getInfoImage() {
        return infoImage;
    }

    public void setInfoImage(String infoImage) {
        this.infoImage = infoImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MyLatLong getLocation() {
        return location;
    }

    public void setLocation(MyLatLong location) {
        this.location = location;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(int disLikes) {
        this.disLikes = disLikes;
    }
}

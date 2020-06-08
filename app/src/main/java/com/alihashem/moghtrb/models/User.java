package com.alihashem.moghtrb.models;

public class User {
    private String name;
    private String phone;
    private Integer collegeIndex;
    private String email;
    private String birthDate;
    private String photoUrl;
    private String gender;

    public User() {

    }

    public User(String name, String phone, Integer collegeIndex, String email, String birthDate, String gender, String photoUrl) {
        this.name = name;
        this.phone = phone;
        this.collegeIndex = collegeIndex;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCollege() {
        return collegeIndex;
    }

    public void setCollege(Integer college) {
        this.collegeIndex = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getbirthDate() {
        return birthDate;
    }

    public void setbirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

package com.alihashem.moghtrb.models;

public class User {
    private String name;
    private String phone;
    private Integer collegeIndex;
    private String email;
    private String birthdate;
    private String photoUrl;
    private String gender;

    public User() {

    }

    public User(String name, String phone, Integer collegeIndex, String email, String birthdate, String gender, String photoUrl) {
        this.name = name;
        this.phone = phone;
        this.collegeIndex = collegeIndex;
        this.email = email;
        this.birthdate = birthdate;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

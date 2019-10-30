package com.example.sokna.models;

import android.os.Parcel;
import android.os.Parcelable;

public class user implements Parcelable {
    private String name;
    private String phone;
    private String college;
    private String email;
    private String birthdate;


    public user() {

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;

    protected user(Parcel in) {
        name = in.readString();
        phone = in.readString();
        college = in.readString();
        email = in.readString();
        birthdate = in.readString();
        gender = in.readString();
        photoUrl = in.readString();

    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;

    public user(String name, String phone, String college, String email, String birthdate, String gender, String photoUrl) {
        this.name = name;
        this.phone = phone;
        this.college = college;
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(college);
        dest.writeString(email);
        dest.writeString(birthdate);
        dest.writeString(gender);
        dest.writeString(photoUrl);
    }

    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };


}

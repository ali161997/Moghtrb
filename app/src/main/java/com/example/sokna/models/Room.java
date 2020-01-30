package com.example.sokna.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Room implements Parcelable {
    private double price; //
    private int rate;//
    private int num_reviews;//
    private String street;//
    private int number_beds;//
    private String detailLink;
    private ArrayList<String> urlsImage;

    public Room() {
    }


    public ArrayList<String> geturlsImage() {
        return urlsImage;
    }

    public void seturlsImage(ArrayList<String> url) {
        this.urlsImage = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getNum_reviews() {
        return num_reviews;
    }

    public void setNum_reviews(int num_reviews) {
        this.num_reviews = num_reviews;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber_beds() {
        return number_beds;
    }

    public void setNumber_beds(int number_beds) {
        this.number_beds = number_beds;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setComments(String detailLink) {
        this.detailLink = detailLink;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeInt(rate);
        dest.writeInt(num_reviews);
        dest.writeString(street);
        dest.writeInt(number_beds);
        dest.writeString(detailLink);
        dest.writeList(urlsImage);


    }

    public Room(double price, int rate, int num_reviews, String street, int number_beds, String detailLink, ArrayList<String> urlsImage) {
        this.price = price;
        this.rate = rate;
        this.num_reviews = num_reviews;
        this.street = street;
        this.number_beds = number_beds;
        this.detailLink = detailLink;
        this.urlsImage = urlsImage;
    }

    public Room(Parcel in) {
        price = in.readDouble();
        rate = in.readInt();
        num_reviews = in.readInt();
        street = in.readString();
        number_beds = in.readInt();
        detailLink = in.readString();
        urlsImage = in.readArrayList(null);


    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}

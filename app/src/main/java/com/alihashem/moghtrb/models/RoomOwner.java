package com.alihashem.moghtrb.models;

import android.graphics.drawable.BitmapDrawable;

import java.util.List;

public class RoomOwner {
    private String name;
    private int age;
    private int rate;
    private BitmapDrawable bitmapDrawable;
    private List<Room> rooms_list;

    public RoomOwner() {
    }

    public RoomOwner(String name, int age, int rate, BitmapDrawable bitmapDrawable, List<Room> rooms_list) {
        this.name = name;
        this.age = age;
        this.rate = rate;
        this.bitmapDrawable = bitmapDrawable;
        this.rooms_list = rooms_list;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getRate() {
        return rate;
    }

    public BitmapDrawable getBitmapDrawable() {
        return bitmapDrawable;
    }

    public List<Room> getRooms_list() {
        return rooms_list;
    }
}

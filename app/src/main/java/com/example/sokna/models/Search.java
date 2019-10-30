package com.example.sokna.models;

public class Search {


    private Place_require place;
    private int num_guests;

    public Search(Place_require place, int num_guests, Date_require date_require, Filter filter) {
        this.place = place;
        this.num_guests = num_guests;
        this.date_require = date_require;
        this.filter = filter;
    }

    private Date_require date_require;
    private Filter filter;


    public Place_require getPlace() {
        return place;
    }

    public void setPlace(Place_require place) {
        this.place = place;
    }

    public int getNum_guests() {
        return num_guests;
    }

    public void setNum_guests(int num_guests) {
        this.num_guests = num_guests;
    }

    public Date_require getDate_require() {
        return date_require;
    }

    public void setDate_require(Date_require date_require) {
        this.date_require = date_require;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }


    public Search() {
        num_guests = 1;
        place = new Place_require();
        date_require = new Date_require();
        filter = new Filter();
    }


}

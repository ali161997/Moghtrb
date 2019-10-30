package com.example.sokna.models;

import java.util.Date;

public class Date_require {

    private Date checkIn;
    private Date checkOut;
    private String student_date;

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public String getStudent_date() {
        return student_date;
    }

    public void setStudent_date(String student_date) {
        this.student_date = student_date;
    }


    public Date_require() {
        checkIn = null;
        checkOut = null;
        student_date = "";
    }


    public Date_require(String student_date) {
        this.student_date = student_date;
    }


}

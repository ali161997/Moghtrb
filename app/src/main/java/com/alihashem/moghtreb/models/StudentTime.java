package com.alihashem.moghtreb.models;

import androidx.annotation.NonNull;

import com.google.type.Date;

public class StudentTime {
    private boolean semester1;
    private boolean semester2;
    private boolean semester3;

    public StudentTime() {

    }

    public boolean isSemester1() {
        return semester1;
    }

    public void setSemester1(boolean semester1) {
        this.semester1 = semester1;
    }

    public boolean isSemester2() {
        return semester2;
    }

    public void setSemester2(boolean semester2) {
        this.semester2 = semester2;
    }

    public boolean isSemester3() {
        return semester3;
    }

    public void setSemester3(boolean semester3) {
        this.semester3 = semester3;
    }

    public void selectAll(boolean s) {
        semester1 = s;
        semester2 = s;
        semester3 = s;
    }

    @NonNull
    @Override
    public String toString() {
        String date = "";
        if (semester1 && semester2 && semester3)
            date = "1/9/" + Date.getDefaultInstance().getYear() + "- 15/8/" + Date.getDefaultInstance().getYear() + 1;
        else if (semester1 && semester2)
            date = "1/9/" + Date.getDefaultInstance().getYear() + "- 15/6/" + Date.getDefaultInstance().getYear() + 1;
        else if (semester2 && semester3)
            date = "1/2/" + Date.getDefaultInstance().getYear() + "- 15/6/" + Date.getDefaultInstance().getYear();
        else if (semester1 && semester3)
            date = "semester1-semester3";
        else if (semester1)
            date = "1/9/" + Date.getDefaultInstance().getYear() + "- 30/1/" + Date.getDefaultInstance().getYear() + 1;
        else if (semester2)
            date = "1/2/" + Date.getDefaultInstance().getYear() + "- 15/6/" + Date.getDefaultInstance().getYear();
        else if (semester3)
            date = "1/7/" + Date.getDefaultInstance().getYear() + "- 15/8/" + Date.getDefaultInstance().getYear();
        return date;
    }

}

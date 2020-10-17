package com.moghtrb.models;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class StudentTime {
    private boolean semester1;
    private boolean semester2;
    private boolean semester3;
    private boolean semester12;

    public StudentTime() {

    }

    public boolean isSemester12() {
        return semester12;
    }

    public void setSemester12(boolean semester12) {
        this.semester12 = semester12;
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


    @NonNull
    @Override
    public String toString() {
        String date = "";
        if (semester12)
            date = "1/9/" + Calendar.getInstance().get(Calendar.YEAR) + "- 1/6/" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
        else if (semester1)
            date = "1/9/" + Calendar.getInstance().get(Calendar.YEAR) + "- 31/1/" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
        else if (semester2)
            date = "1/2/" + Calendar.getInstance().get(Calendar.YEAR) + "- 1/6/" + Calendar.getInstance().get(Calendar.YEAR);
        else if (semester3)
            date = "1/7/" + Calendar.getInstance().get(Calendar.YEAR) + "- 1/9/" + Calendar.getInstance().get(Calendar.YEAR);
        return date;
    }

}

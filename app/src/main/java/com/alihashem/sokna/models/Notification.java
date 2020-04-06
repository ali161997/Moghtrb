package com.alihashem.sokna.models;

public class Notification {
    private String artitle;
    private String arbody;
    private String entitle;
    private String enbody;

    public Notification() {
    }

    public Notification(String artitle, String arbody, String entitle, String enbody) {
        this.artitle = artitle;
        this.arbody = arbody;
        this.entitle = entitle;
        this.enbody = enbody;
    }

    public String getArtitle() {
        return artitle;
    }

    public void setArtitle(String artitle) {
        this.artitle = artitle;
    }

    public String getArbody() {
        return arbody;
    }

    public void setArbody(String arbody) {
        this.arbody = arbody;
    }

    public String getEntitle() {
        return entitle;
    }

    public void setEntitle(String entitle) {
        this.entitle = entitle;
    }

    public String getEnbody() {
        return enbody;
    }

    public void setEnbody(String enbody) {
        this.enbody = enbody;
    }
}

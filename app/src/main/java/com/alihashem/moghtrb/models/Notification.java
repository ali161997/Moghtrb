package com.alihashem.moghtrb.models;

import com.google.firebase.Timestamp;

public class Notification {
    private String artitle;
    private String arbody;
    private String entitle;
    private String enbody;
    private Integer approve;
    private String requestId;
    private Timestamp time = new Timestamp(0, 0);

    public Notification() {

    }

    public Notification(String artitle, String arbody, String entitle, String enbody, Timestamp timestamp, Integer approve, String requestID) {
        this.artitle = artitle;
        this.arbody = arbody;
        this.entitle = entitle;
        this.enbody = enbody;
        this.time = timestamp;
        this.approve = approve;
        this.requestId = requestID;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

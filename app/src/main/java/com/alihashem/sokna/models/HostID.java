package com.alihashem.sokna.models;

public class HostID {
    private static final HostID ourInstance = new HostID();
    private String hostId;
    private Double Lat;
    private Double lon;

    private HostID() {
    }

    public static HostID getInstance() {
        return ourInstance;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}

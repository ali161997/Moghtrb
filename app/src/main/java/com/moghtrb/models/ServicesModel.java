package com.moghtrb.models;

public class ServicesModel {
    String serviceName;
    int serviceIconId;

    public ServicesModel(String serviceName, int serviceIconId) {
        this.serviceName = serviceName;
        this.serviceIconId = serviceIconId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceIconId() {
        return serviceIconId;
    }

    public void setServiceIconId(int serviceIconId) {
        this.serviceIconId = serviceIconId;
    }
}

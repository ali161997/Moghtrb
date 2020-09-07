package com.moghtrb.models;

public class paymentsModel {
    double monthPrice;
    double advance;
    double services;
    double commission = 50.0;
    double promoCode = 0.0;
    int numGuests;
    double dayCost;
    double commissionDays = 20.0;

    public paymentsModel(double monthPrice, double advance, double services, double commission, double total, double minimumPay, double promoCode) {
        this.monthPrice = monthPrice;
        this.advance = advance;
        this.services = services;
        this.commission = commission;
        this.promoCode = promoCode;
    }

    public paymentsModel() {
    }

    public double getCommissionDays() {
        return commissionDays;
    }

    public void setCommissionDays(double commissionDays) {
        this.commissionDays = commissionDays;
    }

    public double getDayCost() {
        return dayCost;
    }

    public void setDayCost(double dayCost) {
        this.dayCost = dayCost;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public double getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public double getServices() {
        return services;
    }

    public void setServices(double services) {
        this.services = services;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }


    public double getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(double promoCode) {
        this.promoCode = promoCode;
    }


}

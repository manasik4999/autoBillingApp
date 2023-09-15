package com.example.qrbarcodescannerdemo;

public class TotalModel {

    private int tcost;
    private int tweight;

    public TotalModel() {
    }

    public TotalModel(int tcost, int tweight) {
        this.tcost = tcost;
        this.tweight = tweight;
    }

    public int getTcost() {
        return tcost;
    }

    public void setTcost(int tcost) {
        this.tcost = tcost;
    }

    public int getTweight() {
        return tweight;
    }

    public void setTweight(int tweight) {
        this.tweight = tweight;
    }
}

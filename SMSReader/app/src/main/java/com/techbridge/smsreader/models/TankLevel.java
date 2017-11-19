package com.techbridge.smsreader.models;

public class TankLevel {
    private String level;
    private String time;
    private String uId;

    public String getuId() {
        return this.uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setTimes(String time) {
        this.time = time;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTimes() {
        return this.time;
    }

    public String getLevel() {
        return this.level;
    }
}

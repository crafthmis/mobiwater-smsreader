package com.techbridge.smsreader.models;

public class Setting {
    private String baseArea;
    private String lowerLimit;
    private String phoneNumber;
    private String settingId;
    private String tankName;
    private String topHeight;
    private String upperLimit;

    public Setting(){}

    public Setting(String settingId, String tankName, String topHeight, String upperLimit, String lowerLimit, String phoneNumber, String baseArea) {
        this.settingId = settingId;
        this.tankName = tankName;
        this.topHeight = topHeight;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.phoneNumber = phoneNumber;
        this.baseArea = baseArea;
    }

    public String getSettingId() {
        return this.settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getTankName() {
        return this.tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    public String getTopHeight() {
        return this.topHeight;
    }

    public void setTopHeight(String topHeight) {
        this.topHeight = topHeight;
    }

    public String getUpperLimit() {
        return this.upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getLowerLimit() {
        return this.lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBaseArea() {
        return this.baseArea;
    }

    public void setBaseArea(String baseArea) {
        this.baseArea = baseArea;
    }
}

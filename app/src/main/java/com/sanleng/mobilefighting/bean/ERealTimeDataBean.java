package com.sanleng.mobilefighting.bean;

/**
 *智能电气火灾实时数据信息
 *
 * @author qiaoshi
 */
public class ERealTimeDataBean {
    private String address;
    private String temperature;
    private String temperaturelimit;
    private String residualcurrent;
    private String currentlimit;
    private String state;
    private String number;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperaturelimit() {
        return temperaturelimit;
    }

    public void setTemperaturelimit(String temperaturelimit) {
        this.temperaturelimit = temperaturelimit;
    }

    public String getResidualcurrent() {
        return residualcurrent;
    }

    public void setResidualcurrent(String residualcurrent) {
        this.residualcurrent = residualcurrent;
    }

    public String getCurrentlimit() {
        return currentlimit;
    }

    public void setCurrentlimit(String currentlimit) {
        this.currentlimit = currentlimit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

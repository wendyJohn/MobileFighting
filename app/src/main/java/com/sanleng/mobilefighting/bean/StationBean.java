package com.sanleng.mobilefighting.bean;

import java.io.Serializable;

/**
 * 附近应急站
 *
 * @author qiaoshi
 */
public class StationBean implements Serializable {
    private String name;
    private String id;
    private String address;
    private String distance;
    private double E_mylatitude;
    private double E_mylongitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getE_mylatitude() {
        return E_mylatitude;
    }

    public void setE_mylatitude(double e_mylatitude) {
        E_mylatitude = e_mylatitude;
    }

    public double getE_mylongitude() {
        return E_mylongitude;
    }

    public void setE_mylongitude(double e_mylongitude) {
        E_mylongitude = e_mylongitude;
    }
}

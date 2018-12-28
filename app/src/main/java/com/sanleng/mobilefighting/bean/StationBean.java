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
    private double distance;
    private double E_mylatitude;
    private double E_mylongitude;
    private String number;
    private String image_type;

    private int type;//布局样式

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }
}

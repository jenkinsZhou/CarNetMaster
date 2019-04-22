package com.tourcoo.carnet.utils;

/**
 * @author :zhoujian
 * @description :坐标点的经纬度和名字
 * @company :翼迈科技
 * @date 2019年 04月 18日 22时37分
 * @Email: 971613168@qq.com
 */
public class Location {

    private double longitude;
    private double latitude;
    private String name = "";

    public Location() {
    }

    public Location(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LocationUtils{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                '}';
    }
}

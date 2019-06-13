package com.tourcoo.carnet.entity.obd;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :行驶报告信息
 * @company :途酷科技
 * @date 2019年06月13日16:40
 * @Email: 971613168@qq.com
 */
public class DriveReportInfo implements Serializable {
    /**
     * avgFuelConsumption : 6.24
     * endLat : 31.828225
     * endLng : 117.163206
     * endTime : 1560346459000
     * fuelConsumption : 0.78
     * id : 28
     * startLat : 31.85639
     * startLng : 117.247943
     * startTime : 1560345336000
     * tripMileage : 12.9
     */

    private double avgFuelConsumption;
    private double endLat;
    private double endLng;
    private String endTime;
    private double fuelConsumption;
    private String id;
    private double startLat;
    private double startLng;
    private String startTime;
    private double tripMileage;

    public double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    public void setAvgFuelConsumption(double avgFuelConsumption) {
        this.avgFuelConsumption = avgFuelConsumption;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getTripMileage() {
        return tripMileage;
    }

    public void setTripMileage(double tripMileage) {
        this.tripMileage = tripMileage;
    }
}

package com.tourcoo.carnet.entity.obd;

import java.io.Serializable;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :行驶报告分页实体
 * @company :途酷科技
 * @date 2019年06月13日16:38
 * @Email: 971613168@qq.com
 */
public class DriveReportEntity implements Serializable {


    /**
     * currentPage : 1
     * elements : [{"avgFuelConsumption":6.24,"endLat":31.828225,"endLng":117.163206,"endTime":"1560346459000","fuelConsumption":0.78,"id":"28","startLat":31.85639,"startLng":117.247943,"startTime":"1560345336000","tripMileage":12.9},{"avgFuelConsumption":10.2,"endLat":31.856305,"endLng":117.247955,"endTime":"1560336033000","fuelConsumption":0.51,"id":"27","startLat":31.846866,"startLng":117.218037,"startTime":"1560334816000","tripMileage":5.09},{"avgFuelConsumption":9.24,"endLat":31.84696,"endLng":117.218047,"endTime":"1560325766000","fuelConsumption":0.84,"id":"26","startLat":31.879906,"startLng":117.266088,"startTime":"1560324348000","tripMileage":9.51},{"avgFuelConsumption":9.2,"endLat":31.879907,"endLng":117.266035,"endTime":"1560320302000","fuelConsumption":0.46,"id":"25","startLat":31.854745,"startLng":117.248552,"startTime":"1560319385000","tripMileage":5.06},{"avgFuelConsumption":8.46,"endLat":31.854719,"endLng":117.248425,"endTime":"1560311518000","fuelConsumption":0.47,"id":"24","startLat":31.845782,"startLng":117.216047,"startTime":"1560310665000","tripMileage":5.48},{"avgFuelConsumption":100,"endLat":31.844557,"endLng":117.215801,"endTime":"1560300726000","fuelConsumption":0.02,"id":"23","startLat":31.844791,"startLng":117.215878,"startTime":"1560300503000","tripMileage":0.02},{"avgFuelConsumption":9.1,"endLat":31.844739,"endLng":117.215873,"endTime":"1560297296000","fuelConsumption":0.7,"id":"22","startLat":31.828543,"startLng":117.162974,"startTime":"1560295906000","tripMileage":7.87},{"avgFuelConsumption":20,"endLat":31.82824,"endLng":117.163119,"endTime":"1560249883000","fuelConsumption":0.02,"id":"19","startLat":31.827504,"startLng":117.163582,"startTime":"1560249849000","tripMileage":0.1},{"avgFuelConsumption":9.63,"endLat":31.827504,"endLng":117.163582,"endTime":"1560249501000","fuelConsumption":1.07,"id":"18","startLat":31.800786,"startLng":117.249296,"startTime":"1560247129000","tripMileage":11.32},{"avgFuelConsumption":9.69,"endLat":31.800765,"endLng":117.249574,"endTime":"1560232558000","fuelConsumption":0.57,"id":"17","startLat":31.814251,"startLng":117.219431,"startTime":"1560231613000","tripMileage":5.8}]
     * pages : 2
     * totalElements : 18
     */

    private int currentPage;
    private int pages;
    private String totalElements;
    private List<DriveReportInfo> elements;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public List<DriveReportInfo> getElements() {
        return elements;
    }

    public void setElements(List<DriveReportInfo> elements) {
        this.elements = elements;
    }


}

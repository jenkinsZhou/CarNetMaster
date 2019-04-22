package com.tourcoo.carnet.entity;

/**
 * @author :JenkinsZhou
 * @description :保险公司实体类
 * @company :途酷科技
 * @date 2019年03月18日16:55
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyDetail {

    /**
     * address :
     * alarmPhone :
     * companyBatchName :
     * companyProfile :
     * distance : 0
     * id : 0
     * insuranceName :
     * lat : 0
     * lng : 0
     * teleAreaCode :
     * telephone :
     */

    private String address;
    private String alarmPhone;
    private String companyBatchName;
    private String companyProfile;
    private int distance;
    private int id;
    private String insuranceName;
    private double lat;
    private double lng;
    private String teleAreaCode;
    private String telephone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlarmPhone() {
        return alarmPhone;
    }

    public void setAlarmPhone(String alarmPhone) {
        this.alarmPhone = alarmPhone;
    }

    public String getCompanyBatchName() {
        return companyBatchName;
    }

    public void setCompanyBatchName(String companyBatchName) {
        this.companyBatchName = companyBatchName;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public String getTeleAreaCode() {
        return teleAreaCode;
    }

    public void setTeleAreaCode(String teleAreaCode) {
        this.teleAreaCode = teleAreaCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

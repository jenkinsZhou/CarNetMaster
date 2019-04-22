package com.tourcoo.carnet.entity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :保险公司
 * @company :途酷科技
 * @date 2019年04月19日15:51
 * @Email: 971613168@qq.com
 */
public class InsuranceCompany {


    /**
     * currentPage : 0
     * elements : [{"address":"","alarmPhone":"","companyBatchName":"","companyProfile":"","distance":0,"id":0,"insuranceName":"","lat":0,"lng":0,"teleAreaCode":"","telephone":""}]
     * pages : 0
     * totalElements : 0
     */

    private int currentPage;
    private int pages;
    private int totalElements;
    private List<CompanyInfo> elements;

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

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<CompanyInfo> getElements() {
        return elements;
    }

    public void setElements(List<CompanyInfo> elements) {
        this.elements = elements;
    }

    public static class CompanyInfo {
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
        private String distance;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
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
}

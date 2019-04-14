package com.tourcoo.carnet.entity.garage;

import java.io.Serializable;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :修理厂实体类
 * @company :途酷科技
 * @date 2019年04月09日16:32
 * @Email: 971613168@qq.com
 */
public class GarageEntity implements Serializable {


    /**
     * total : 1
     * current : 1
     * pages : 1
     * size : 10
     * garageList : [{"id":"26","name":"数据库加的修理厂","address":"安徽省合肥市蜀山区三里庵","position":"117.26061,31.8512","avatar":"https://wx.qlogo.cn/mmopen/vi_32/QMFEHxzXrJtiaEgIxKNrfDGIgUMlTmLDtBicO085khyaHdticQPAoyK7sWwz41p9I5f6mGIPdC6ZfAXRScPyuVPrg/132","image":"img/2019-04-08-0f36a10b-2231-4f17-964f-817517c64f8f.png","images":"img/2019-04-08-840d2205-d707-46d4-8ce9-f0e2c3837afc.png,img/2019-04-08-5f318f7e-709a-4839-a519-7222e8aa7c4c.png,img/2019-04-09-73b8cfd3-5dd4-47b6-8057-7a23086676b8.png","username":"照明胧","mobile":"15665418878","openid":"o2Z5r5Xxer5ekr2ycKxjb3biRpi4","unionid":"","level":0,"visit":27,"idcardImage1":"img/2019-04-08-66652eef-0b82-4220-a4ff-b23bf4c5d1ba.png","idcardImage2":"img/2019-04-08-4258ecb8-27fc-481b-bb3a-717aba2e3fe0.png","licenseImage":"img/2019-04-08-c5c40032-013e-4cfa-a201-1e6782d7bf26.png","licenseCode":"188888888888","balance":0,"status":"normal","createTime":"2019-04-08 10:14:10","createUser":"0","updateTime":"2019-04-09 15:33:07","updateUser":"26","tags":"4","synopsis":"数据库添加的信息","distance":4.9,"tagNames":["轮胎专修"]}]
     */

    private String total;
    private String current;
    private String pages;
    private String size;
    private List<GarageInfo> garageList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<GarageInfo> getGarageList() {
        return garageList;
    }

    public void setGarageList(List<GarageInfo> garageList) {
        this.garageList = garageList;
    }



}

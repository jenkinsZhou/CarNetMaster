package com.tourcoo.carnet.wxapi;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月16日14:25
 * @Email: 971613168@qq.com
 */
public class WeiXinUserInfo {

    /**
     * openid : oQjxZ6EEmZcY05LX-vfCBBiqg7WE
     * nickname : 一个板凳
     * sex : 1
     * language : zh_CN
     * city : Hefei
     * province : Anhui
     * country : CN
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/wsRmxcKeyV3kicvQ4cwrNiaDLqYEIib21FhL4WgnrVZRpaGAMtcswggAlLBZyUiaI2EnGb1NuzEBnAGicP5HgqPkKMA/132
     * privilege : []
     * unionid : oPkNd1HGtSc7Vq21DgSxIWnLEJWI
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}

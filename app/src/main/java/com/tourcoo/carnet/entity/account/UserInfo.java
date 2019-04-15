package com.tourcoo.carnet.entity.account;

import org.litepal.crud.LitePalSupport;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月12日16:22
 * @Email: 971613168@qq.com
 */
public class UserInfo extends LitePalSupport {
    /**
     * avatar :
     * createTime : 2019-04-03 11:53:44
     * createUser : 0
     * driverAge :
     * driverLicense :
     * id : 6
     * mobile : 18256070563
     * nickname :
     * openid :
     * password : 1376bfbe0230476d922d0bf682fb3816
     * salt : 0cww1
     * status :
     * unionid :
     * updateTime : 2019-04-03 11:53:44
     * updateUser :
     * username :
     */
    /**
     * 预留的备注字段
     */
    private String remark = "";

    private String avatar;
    private String createTime;
    private String createUser;
    private String driverAge;
    private String driverLicense;
    private long id;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private String mobile;
    private String nickname;
    private String openid;
    private String password;
    private String salt;
    private String status;
    private String unionid;
    private String updateTime;
    private String updateUser;
    private String username;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(String driverAge) {
        this.driverAge = driverAge;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

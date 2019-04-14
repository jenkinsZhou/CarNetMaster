package com.tourcoo.carnet.entity.account;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :用户信息实体
 * @company :途酷科技
 * @date 2019年04月03日14:16
 * @Email: 971613168@qq.com
 */
public class UserInfoEntity extends LitePalSupport implements Serializable {
    /**
     * userInfo : {"avatar":"","createTime":"2019-04-03 11:53:44","createUser":"0","driverAge":"","driverLicense":"","id":"6","mobile":"18256070563","nickname":"","openid":"","password":"1376bfbe0230476d922d0bf682fb3816","salt":"0cww1","status":"","unionid":"","updateTime":"2019-04-03 11:53:44","updateUser":"","username":""}
     * token : 297f898f-bab2-4f33-8acb-6a3ff66b544d
     */

    private UserInfo userInfo;
    /**
     * 预留的tag字段
     */
    private String tag;
    private String token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

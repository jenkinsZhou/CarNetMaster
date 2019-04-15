package com.tourcoo.carnet.entity.account;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月15日15:18
 * @Email: 971613168@qq.com
 */
public class Test {

    /**
     * userInfo : {"id":"27","username":"","mobile":"18256070563","nickname":"开了个","avatar":"img/2019-04-15-0a538738-66eb-4096-b1c9-e61ec6e14258.png","openid":"","unionid":"","driverAge":1,"driverLicense":"9794994","status":"normal","createTime":"2019-04-12 19:02:20","createUser":"0","updateTime":"2019-04-15 15:16:33","updateUser":"27"}
     */

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * id : 27
         * username :
         * mobile : 18256070563
         * nickname : 开了个
         * avatar : img/2019-04-15-0a538738-66eb-4096-b1c9-e61ec6e14258.png
         * openid :
         * unionid :
         * driverAge : 1
         * driverLicense : 9794994
         * status : normal
         * createTime : 2019-04-12 19:02:20
         * createUser : 0
         * updateTime : 2019-04-15 15:16:33
         * updateUser : 27
         */

        private String id;
        private String username;
        private String mobile;
        private String nickname;
        private String avatar;
        private String openid;
        private String unionid;
        private int driverAge;
        private String driverLicense;
        private String status;
        private String createTime;
        private String createUser;
        private String updateTime;
        private String updateUser;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public int getDriverAge() {
            return driverAge;
        }

        public void setDriverAge(int driverAge) {
            this.driverAge = driverAge;
        }

        public String getDriverLicense() {
            return driverLicense;
        }

        public void setDriverLicense(String driverLicense) {
            this.driverLicense = driverLicense;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
    }
}

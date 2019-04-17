package com.tourcoo.carnet.wxapi;

/**
 * @author :JenkinsZhou
 * @description :微信登录获取的token
 * @company :途酷科技
 * @date 2019年04月16日13:52
 * @Email: 971613168@qq.com
 */
public class WeiXinLoginInfo {

    /**
     * access_token : 20_4L3xPB7qCNyzKzAgVkecZnMcIVDxR54Yg8UCwwtMMuIXC2EgMfqIO153cHstHiX3seIuVugA_K9YL0qmJUQYg-Tahk-7e3TFC9gpWRac5SU
     * expires_in : 7200
     * refresh_token : 20_Q5-oomW0Q4cRGb48cY4S8YoLq8ebYObyfcTp6SGBnO3J0PxBqnveccFxbP42hxjKMdKYVtrCANl8okd29V7fGsTXbue__fBy6tqtw1hgvgA
     * openid : oQjxZ6EEmZcY05LX-vfCBBiqg7WE
     * scope : snsapi_userinfo
     * unionid : oPkNd1HGtSc7Vq21DgSxIWnLEJWI
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}

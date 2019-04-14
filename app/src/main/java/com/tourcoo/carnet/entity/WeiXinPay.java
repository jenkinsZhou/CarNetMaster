package com.tourcoo.carnet.entity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月09日10:32
 * @Email: 971613168@qq.com
 */
public class WeiXinPay {

    /**
     * appId :
     * extra :
     * nonceStr :
     * partnerId :
     * paymentStr :
     * sign :
     * timeStr :
     */

    private String appId;
    private String extra;
    private String nonceStr;
    private String partnerId;
    private String paymentStr;
    private String sign;
    private String timeStr;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPaymentStr() {
        return paymentStr;
    }

    public void setPaymentStr(String paymentStr) {
        this.paymentStr = paymentStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}

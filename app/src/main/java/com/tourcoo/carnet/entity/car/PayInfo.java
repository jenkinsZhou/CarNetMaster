package com.tourcoo.carnet.entity.car;

import com.tourcoo.carnet.entity.pay.AliPay;
import com.tourcoo.carnet.entity.WeiXinPay;

/**
 * @author :JenkinsZhou
 * @description :支付信息
 * @company :途酷科技
 * @date 2019年04月08日20:16
 * @Email: 971613168@qq.com
 */
public class PayInfo {
    private WeiXinPay wxPayConf;
    private AliPay aliPayConf;
    /**
     * orderId : 0
     * orderPrice : 0
     * outTradeNo :
     * payType : 0
     */

    private int orderId;
    private double orderPrice;
    private String outTradeNo;
    private int payType;


    public WeiXinPay getWxPayConf() {
        return wxPayConf;
    }

    public void setWxPayConf(WeiXinPay wxPayConf) {
        this.wxPayConf = wxPayConf;
    }

    public AliPay getAliPayConf() {
        return aliPayConf;
    }

    public void setAliPayConf(AliPay aliPayConf) {
        this.aliPayConf = aliPayConf;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}

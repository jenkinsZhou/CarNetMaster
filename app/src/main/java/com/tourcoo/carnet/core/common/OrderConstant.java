package com.tourcoo.carnet.core.common;

/**
 * @author :zhoujian
 * @description :订单相关常量
 * @company :翼迈科技
 * @date 2019年 04月 20日 00时38分
 * @Email: 971613168@qq.com
 */
public class OrderConstant {

    /**
     * 上门服务的TAG
     */
    public static final String EXTRA_ORDER_TAG_SERVICE = "EXTRA_ORDER_TAG_SERVICE";

    public static final int ORDER_TAG_SERVICE_ALL = 7;
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    /**
     * 上门服务订单
     */
    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";


    /**
     * 订单状态: 1,待接单 2,服务中 3,服务已完成 4,待支付 5,待评价 6,订单关闭 7,订单已取消
     * /**
     * 故障报修订单
     */
    public static final String TYPE_REPAIR = "1";
    /**
     * 服务订单
     */
    public static final String TYPE_SERVICE = "2";


    /**
     * 上门维修
     */
    public static final int TYPE_CAR_REPAIR = 3;
    /**
     * 上门洗车
     */
    public static final int TYPE_CAR_WASH = 4;
    /**
     * 上门保养
     */
    public static final int TYPE_CAR_CURING = 5;


    public static final int PAY_TYPE_WEI_XIN_PAY = 1;

    public static final int PAY_TYPE_ALI_PAY = 0;

}

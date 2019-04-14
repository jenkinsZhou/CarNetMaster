package com.tourcoo.carnet.core.common;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 01日 21时51分
 * @Email: 971613168@qq.com
 */
public class CommonConstant {
    public static final String TAG = "NaViMaster";
    public static final String TAG_PRE_SUFFIX = "NaViConfig";
    public static final long TOAST_DURATION_DEFAULT = 2000L;
    public final static String EXCEPTION_NOT_INIT = "You've to call static method init() first in Application";
    public final static String EXCEPTION_NOT_INIT_FAST_MANAGER = "You've to call static method init(Application) first in Application";
    public final static String EXCEPTION_EMPTY_URL = "请求参数为空!";
    public static final String SUFFIX_APK = ".apk";

    /**
     * 车主类型
     */
    public static final String TYPE_USER_CAR_OWER = "0";

    public static final String PREF_KEY_ACCOUNT = "PREF_KEY_ACCOUNT";
    public static final String PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD";

    /**
     * 是否记住密码
     */
    public static final String PREF_KEY_IS_REMEMBER_ACCOUNT = "PREF_KEY_IS_REMEMBER_ACCOUNT";


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
}

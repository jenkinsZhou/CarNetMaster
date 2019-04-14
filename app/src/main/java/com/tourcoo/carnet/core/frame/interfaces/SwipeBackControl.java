package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @author :zhoujian
 * @description :侧滑返回控制接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 21时39分
 * @Email: 971613168@qq.com
 */
public interface SwipeBackControl {

    /**
     * 设置当前Activity是否支持滑动返回(用于控制是否添加一层{@link cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout})
     * 返回为true {@link #setSwipeBack(Activity, BGASwipeBackHelper)}才有设置的意义
     *
     * @param activity 当前Activity
     * @return
     */
    boolean isSwipeBackEnable(Activity activity);

    /**
     * 设置滑动返回
     *
     * @param activity           当前Activity
     * @param bgaSwipeBackHelper
     */
    void setSwipeBack(Activity activity, BGASwipeBackHelper bgaSwipeBackHelper);

    /**
     * 正在滑动返回
     *
     * @param activity    滑动的Activity
     * @param slideOffset 滑动偏移量 0-1
     */
    void onSwipeBackLayoutSlide(Activity activity, float slideOffset);


    /**
     * 没达到滑动返回的阈值,取消滑动返回动作,回到默认状态
     *
     * @param activity 当前Activity
     */
    void onSwipeBackLayoutCancel(Activity activity);

    /**
     * 滑动返回执行完毕,销毁当前 Activity
     *
     * @param activity 当前activity
     */
    void onSwipeBackLayoutExecuted(Activity activity);
}

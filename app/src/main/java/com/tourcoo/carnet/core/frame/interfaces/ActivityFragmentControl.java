package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.tourcoo.carnet.core.widget.core.view.StatusViewHelper;
import com.tourcoo.carnet.core.widget.core.view.navigation.NavigationViewHelper;

import androidx.fragment.app.FragmentManager;

/**
 * @author :zhoujian
 * @description :Activity/Fragment 属性控制(生命周期/背景色/屏幕控制)
 * @company :翼迈科技
 * @date 2019年 03月 02日 23时00分
 * @Email: 971613168@qq.com
 */
public interface ActivityFragmentControl {


    /**
     * 设置背景色
     *
     * @param contentView
     * @param cls
     */
    void setContentViewBackground(View contentView, Class<?> cls);

    /**
     * 强制设置横竖屏
     *
     * @param activity
     */
    void setRequestedOrientation(Activity activity);

    /**
     * Activity 全局状态栏控制可设置部分页面属性
     *
     * @param activity
     * @param helper
     * @param topView
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setStatusBar(Activity activity, StatusViewHelper helper, View topView);

    /**
     * Activity 全局虚拟导航栏控制
     *
     * @param activity
     * @param helper
     * @param bottomView
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setNavigationBar(Activity activity, NavigationViewHelper helper, View bottomView);

    /**
     * Activity 全局生命周期回调
     *
     * @return
     */
    Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks();

    /**
     * Fragment全局生命周期回调
     *
     * @return
     */
    FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks();
}

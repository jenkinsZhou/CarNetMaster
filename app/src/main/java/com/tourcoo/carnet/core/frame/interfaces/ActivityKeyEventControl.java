package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;
import android.view.KeyEvent;

/**
 * @author :zhoujian
 * @description :Activity 按键监控(如音量键、返回键、菜单键)--必须继承自基类BaseActivity
 * * Activity 必须在前台
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时59分
 * @Email: 971613168@qq.com
 */
public interface ActivityKeyEventControl {


    /**
     * 按下事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyDown(Activity activity, int keyCode, KeyEvent event);

    /**
     * 抬起事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyUp(Activity activity, int keyCode, KeyEvent event);

    /**
     * 长按事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event);

    /**
     * 键盘快捷键事件
     *
     * @param activity
     * @param keyCode
     * @param event
     * @return
     */
    boolean onKeyShortcut(Activity activity, int keyCode, KeyEvent event);

    /**
     * 多个连续的重复事件
     *
     * @param activity
     * @param keyCode
     * @param repeatCount
     * @param event
     * @return
     */
    boolean onKeyMultiple(Activity activity, int keyCode, int repeatCount, KeyEvent event);
}

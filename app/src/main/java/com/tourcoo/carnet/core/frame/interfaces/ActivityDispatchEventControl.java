package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author :zhoujian
 * @description : Activity 事件派发--必须继承自BaseActivity
 * * Activity 必须在前台
 * @company :翼迈科技
 * @date 2019年 03月 02日 23时04分
 * @Email: 971613168@qq.com
 */
public interface ActivityDispatchEventControl {

    /**
     * 分发事件
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchTouchEvent(Activity activity, MotionEvent event);

    /**
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchGenericMotionEvent(Activity activity, MotionEvent event);

    /**
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchKeyEvent(Activity activity, KeyEvent event);

    /**
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchKeyShortcutEvent(Activity activity, KeyEvent event);

    /**
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchTrackballEvent(Activity activity, MotionEvent event);

    /**
     * @param activity
     * @param event
     * @return
     */
    boolean dispatchPopulateAccessibilityEvent(Activity activity, AccessibilityEvent event);
}

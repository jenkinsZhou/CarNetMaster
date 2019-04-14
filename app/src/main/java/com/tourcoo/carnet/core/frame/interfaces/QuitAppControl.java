package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;

/**
 * @author :zhoujian
 * @description :退出应用
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时11分
 * @Email: 971613168@qq.com
 */
public interface QuitAppControl {

    /**
     * 退出程序提示回调
     *
     * @param isFirst  是否首次提示
     * @param activity 操作的Activity
     * @return 延迟间隔--如不需要设置两次提示可设置0--最佳方式是直接在回调中执行想要的操作
     */
    long quitApp(boolean isFirst, Activity activity);
}

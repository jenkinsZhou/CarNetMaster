package com.tourcoo.carnet.core.frame.interfaces;

import android.widget.Toast;

import com.tourcoo.carnet.core.widget.core.view.radius.RadiusTextView;

/**
 * @author :zhoujian
 * @description :吐司控制接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 20时26分
 * @Email: 971613168@qq.com
 */
public interface ToastControl {
    /**
     * 获取吐司实例
     *
     * @return
     */
    Toast getToast();

    /**
     * 设置吐司
     *
     * @param toast:设置吐司对象
     * @param textView:设置显示的view
     */
    void setToast(Toast toast, RadiusTextView textView);

}

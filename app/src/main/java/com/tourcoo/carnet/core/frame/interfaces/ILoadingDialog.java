package com.tourcoo.carnet.core.frame.interfaces;

import android.app.Activity;

import com.tourcoo.carnet.core.frame.widget.LoadingDialog;

import androidx.annotation.Nullable;


/**
 * @author :zhoujian
 * @description :用于全局配置网络请求登录Loading提示框
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时53分
 * @Email: 971613168@qq.com
 */
public interface ILoadingDialog {

    /**
     * 设置快速Loading Dialog
     *
     * @param activity
     * @return
     */
    @Nullable
    LoadingDialog createLoadingDialog(@Nullable Activity activity);
}

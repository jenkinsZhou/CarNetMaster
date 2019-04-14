package com.tourcoo.carnet.core.frame.interfaces;

import android.os.Bundle;

import androidx.annotation.LayoutRes;

/**
 * @author :zhoujian
 * @description :基类 Activity及Fragment通用属性
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时50分
 * @Email: 971613168@qq.com
 */
public interface IBaseView {


    /**
     * 是否注册EventBus
     *
     * @return
     */
    boolean isEventBusEnable();

    /**
     * Activity或Fragment 布局xml
     *
     * @return
     */
    @LayoutRes
    int getContentLayout();

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 执行加载布局文件之前操作方法前调用
     */
    void beforeSetContentView();

    /**
     * 在初始化控件前进行一些操作
     *
     * @param savedInstanceState
     */
    void beforeInitView(Bundle savedInstanceState);

    /**
     * 需要加载数据时重写此方法
     */
    void loadData();
}

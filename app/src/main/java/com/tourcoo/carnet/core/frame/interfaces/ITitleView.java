package com.tourcoo.carnet.core.frame.interfaces;

import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :TitleBarView接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时30分
 * @Email: 971613168@qq.com
 */
public interface ITitleView {


    /**
     * 子类回调setTitleBar之前执行用于app设置全局Base控制统一TitleBarView
     *
     * @param titleBar
     */
    void beforeSetTitleBar(TitleBarView titleBar);

    /**
     * 一般用于最终实现子类设置TitleBarView 其它属性
     *
     * @param titleBar
     */
    void setTitleBar(TitleBarView titleBar);
}

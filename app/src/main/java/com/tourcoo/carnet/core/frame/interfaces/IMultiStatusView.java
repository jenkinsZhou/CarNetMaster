package com.tourcoo.carnet.core.frame.interfaces;

import android.view.View;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时26分
 * @Email: 971613168@qq.com
 */
public interface IMultiStatusView {

    /**
     * 设置StatusLayoutManager 的目标View
     *
     * @return
     */
    View getMultiStatusContentView();

    /**
     * 设置StatusLayoutManager属性
     *
     * @param statusView
     */
    void setMultiStatusView(StatusLayoutManager.Builder statusView);

    /**
     * 获取空布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getEmptyClickListener();

    /**
     * 获取错误布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getErrorClickListener();

    /**
     * 获取自定义布局里点击View回调
     *
     * @return
     */
    View.OnClickListener getCustomerClickListener();
}

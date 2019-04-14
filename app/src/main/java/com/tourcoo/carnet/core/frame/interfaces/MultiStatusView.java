package com.tourcoo.carnet.core.frame.interfaces;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时21分
 * @Email: 971613168@qq.com
 */
public interface MultiStatusView {

    /**
     * 设置多状态布局属性
     *
     * @param statusView
     * @param iFastRefreshLoadView
     */
    void setMultiStatusView(StatusLayoutManager.Builder statusView, IRefreshLoadView iFastRefreshLoadView);
}

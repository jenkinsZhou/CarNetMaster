package com.tourcoo.carnet.core.frame.interfaces;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :基于实现{@link IRefreshLoadView}下拉刷新、列表、多状态布局的全局回调接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时27分
 * @Email: 971613168@qq.com
 */
public interface IRequestControl {

    /**
     * 获取刷新布局
     *
     * @return
     */
    SmartRefreshLayout getRefreshLayout();

    /**
     * 获取RecyclerView Adapter
     *
     * @return
     */
    BaseQuickAdapter getRecyclerAdapter();

    /**
     * 获取多布局状态管理
     *
     * @return
     */
    StatusLayoutManager getStatusLayoutManager();

    /**
     * 获取当前页码
     * @return
     */
    int getCurrentPage();

    /**
     * 获取每页数量
     * @return
     */
    int getPageSize();

    /**
     * 获取调用类
     * @return
     */
    Class<?> getRequestClass();
}

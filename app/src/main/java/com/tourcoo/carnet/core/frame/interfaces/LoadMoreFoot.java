package com.tourcoo.carnet.core.frame.interfaces;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import androidx.annotation.Nullable;

/**
 * @author :zhoujian
 * @description :设置Adapter全局加载更多脚布局
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时53分
 * @Email: 971613168@qq.com
 */
public interface LoadMoreFoot {

    /**
     * 设置BaseQuickAdapter的加载更多视图
     *
     * @param adapter
     * @return
     */
    @Nullable
    LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter);
}

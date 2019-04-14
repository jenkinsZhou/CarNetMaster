package com.tourcoo.carnet.core.frame.interfaces;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :zhoujian
 * @description :列表布局全局控制RecyclerView
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时56分
 * @Email: 971613168@qq.com
 */
public interface RecyclerViewControl {

    /**
     * 全局设置
     *
     * @param recyclerView
     * @param cls
     */
    void setRecyclerView(RecyclerView recyclerView, Class<?> cls);
}

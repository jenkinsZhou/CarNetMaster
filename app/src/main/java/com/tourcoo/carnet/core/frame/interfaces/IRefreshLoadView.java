package com.tourcoo.carnet.core.frame.interfaces;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :zhoujian
 * @description :下拉刷新 上拉加载接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时23分
 * @Email: 971613168@qq.com
 */
public interface IRefreshLoadView<T> extends OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IMultiStatusView {

    /**
     * 使用BaseRecyclerViewAdapterHelper作为上拉加载的实现方式
     * 如果使用ListView或GridView等需要自己去实现上拉加载更多的逻辑
     *
     * @return BaseRecyclerViewAdapterHelper的实现类
     */
    BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    /**
     * 获取RecyclerView的布局管理器对象，根据自己业务实际情况返回
     *
     * @return RecyclerView的布局管理器对象
     */
    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 获取下拉刷新头布局
     *
     * @return 下拉刷新头
     */
    RefreshHeader getRefreshHeader();

    /**
     * 获取加载更多布局
     *
     * @return
     */
    LoadMoreView getLoadMoreView();

    /**
     * 触发下拉或上拉刷新操作
     *
     * @param page
     */
    void loadData(int page);

    /**
     * 是否支持加载更多功能
     *
     * @return
     */
    boolean isLoadMoreEnable();

    /**
     * 是否支持下拉刷新功能
     *
     * @return
     */
    boolean isRefreshEnable();

    /**
     * item是否有点击事件
     *
     * @return
     */
    boolean isItemClickEnable();

    /**
     * item点击回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position);

    /**
     * 设置全局监听接口
     *
     * @return
     */
    IRequestControl getIHttpRequestControl();
}

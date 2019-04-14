package com.tourcoo.carnet.core.frame.base.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tourcoo.carnet.core.frame.delegate.RefreshLoadDelegate;
import com.tourcoo.carnet.core.frame.interfaces.IRefreshLoadView;
import com.tourcoo.carnet.core.frame.interfaces.IRequestControl;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.core.frame.manager.WrapContentLinearLayoutManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :实现下刷新的基类Fragment
 * @company :翼迈科技
 * @date 2019年 03月 05日 20时40分
 * @Email: 971613168@qq.com
 */
public abstract class BaseRefreshFragment<T> extends BaseFragment implements IRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    protected int mDefaultPage = 1;
    protected int mDefaultPageSize = 10;
    private BaseQuickAdapter mQuickAdapter;
    private Class<?> mClass;

    protected RefreshLoadDelegate<T> mRefreshLoadDelegate;

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mClass = this.getClass();
        mRefreshLoadDelegate = new RefreshLoadDelegate<>(mContentView, this, mClass);
        mRecyclerView = mRefreshLoadDelegate.mRecyclerView;
        mRefreshLayout = mRefreshLoadDelegate.mRefreshLayout;
        mStatusManager = mRefreshLoadDelegate.mStatusManager;
        mQuickAdapter = mRefreshLoadDelegate.mAdapter;
    }

    @Override
    public RefreshHeader getRefreshHeader() {
        return null;
    }

    @Override
    public LoadMoreView getLoadMoreView() {
        return null;
    }

    @Override
    public IRequestControl getIHttpRequestControl() {
        return new IRequestControl() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mRefreshLayout;
            }

            @Override
            public BaseQuickAdapter getRecyclerAdapter() {
                return mQuickAdapter;
            }

            @Override
            public StatusLayoutManager getStatusLayoutManager() {
                return mStatusManager;
            }

            @Override
            public int getCurrentPage() {
                return mDefaultPage;
            }

            @Override
            public int getPageSize() {
                return mDefaultPageSize;
            }

            @Override
            public Class<?> getRequestClass() {
                return mClass;
            }
        };
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new WrapContentLinearLayoutManager(mContext);
    }

    @Override
    public View getMultiStatusContentView() {
        return null;
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {

    }

    @Override
    public View.OnClickListener getEmptyClickListener() {
        return null;
    }

    @Override
    public View.OnClickListener getErrorClickListener() {
        return null;
    }

    @Override
    public View.OnClickListener getCustomerClickListener() {
        return null;
    }

    @Override
    public void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position) {

    }

    @Override
    public boolean isItemClickEnable() {
        return true;
    }

    @Override
    public boolean isRefreshEnable() {
        return true;
    }

    @Override
    public boolean isLoadMoreEnable() {
        return true;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 0;
        mRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
        loadData(mDefaultPage);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(++mDefaultPage);
    }

    @Override
    public void loadData() {
        loadData(mDefaultPage);
    }

    @Override
    public void onDestroy() {
        if (mRefreshLoadDelegate != null) {
            mRefreshLoadDelegate.onDestroy();
        }
        super.onDestroy();
    }

}

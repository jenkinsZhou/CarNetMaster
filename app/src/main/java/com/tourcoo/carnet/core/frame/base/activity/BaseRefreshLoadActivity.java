package com.tourcoo.carnet.core.frame.base.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.luck.picture.lib.dialog.PictureDialog;
import com.tourcoo.carnet.core.frame.delegate.RefreshLoadDelegate;
import com.tourcoo.carnet.core.frame.interfaces.IRefreshLoadView;
import com.tourcoo.carnet.core.frame.interfaces.IRequestControl;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.core.frame.manager.WrapContentLinearLayoutManager;
import com.tourcoo.carnet.core.threadpool.ThreadPoolManager;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description : 带刷新功能的基类activity
 * @company :翼迈科技
 * @date 2019年03月05日下午 05:09
 * @Email: 971613168@qq.com
 */
public abstract class BaseRefreshLoadActivity<T> extends BaseTitleActivity implements IRefreshLoadView<T> {
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected StatusLayoutManager mStatusManager;
    private BaseQuickAdapter mQuickAdapter;
    protected int mDefaultPage = 1;
    protected int mDefaultPageSize = 6;
    protected RefreshLoadDelegate<T> mRefreshLoadDelegate;
    private Class<?> mClass;
    private MyHandler mMyHandler = new MyHandler();

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mClass = getClass();
        mRefreshLoadDelegate = new RefreshLoadDelegate<>(mContentView, this, getClass());
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
        mRefreshLoadDelegate.setLoadMore(isLoadMoreEnable());
        loadData(mDefaultPage);
    }

    @Override
    public void onLoadMoreRequested() {
        //延迟加载更多
        mMyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(++mDefaultPage);
            }
        }, 500);


    }

    @Override
    public void loadData() {
        loadData(mDefaultPage);
    }

    @Override
    protected void onDestroy() {
        if (mMyHandler != null) {
            mMyHandler.removeCallbacksAndMessages(null);
        }
        if (mRefreshLoadDelegate != null) {
            mRefreshLoadDelegate.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        TextView textView = titleBar.getTextView(Gravity.CENTER | Gravity.TOP);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private static class MyHandler extends Handler {

    }

}

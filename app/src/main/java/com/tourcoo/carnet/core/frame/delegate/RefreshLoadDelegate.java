package com.tourcoo.carnet.core.frame.delegate;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.interfaces.IRefreshLoadView;
import com.tourcoo.carnet.core.frame.widget.NaViLoadMoreView;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description : 快速实现下拉刷新及上拉加载更多代理类
 * @company :翼迈科技
 * @date 2019年03月04日上午 10:21
 * @Email: 971613168@qq.com
 */
public class RefreshLoadDelegate<T> {

    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    public StatusLayoutManager mStatusManager;
    private IRefreshLoadView<T> mIRefreshLoadView;
    private Context mContext;
    private UiConfigManager mManager;
    public View mRootView;
    private Class<?> mTargetClass;

    public RefreshLoadDelegate(View rootView, IRefreshLoadView<T> IRefreshLoadView, Class<?> cls) {
        this.mRootView = rootView;
        this.mIRefreshLoadView = IRefreshLoadView;
        this.mTargetClass = cls;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mManager = UiConfigManager.getInstance();
        if (mIRefreshLoadView == null) {
            return;
        }
        getRefreshLayout(rootView);
        getRecyclerView(rootView);
        initRefreshHeader();
        initRecyclerView();
        setStatusManager();
    }

    /**
     * 初始化刷新头配置
     */
    protected void initRefreshHeader() {
        if (mRefreshLayout == null) {
            return;
        }
        mRefreshLayout.setRefreshHeader(mIRefreshLoadView.getRefreshHeader() != null
                ? mIRefreshLoadView.getRefreshHeader() :
                mManager.getDefaultRefreshHeaderCreator() != null ?
                        mManager.getDefaultRefreshHeaderCreator().createRefreshHeader(mContext, mRefreshLayout) :
                        new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(mIRefreshLoadView);
        mRefreshLayout.setEnableRefresh(mIRefreshLoadView.isRefreshEnable());
    }

    /**
     * 初始化RecyclerView配置
     */
    protected void initRecyclerView() {
        if (mRecyclerView == null) {
            return;
        }
        if (UiConfigManager.getInstance().getRecyclerViewControl() != null) {
            UiConfigManager.getInstance().getRecyclerViewControl().setRecyclerView(mRecyclerView, mTargetClass);
        }
        mAdapter = mIRefreshLoadView.getAdapter();
        mRecyclerView.setLayoutManager(mIRefreshLoadView.getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            setLoadMore(mIRefreshLoadView.isLoadMoreEnable());
            //先判断是否Activity/Fragment设置过;再判断是否有全局设置;最后设置默认
            mAdapter.setLoadMoreView(mIRefreshLoadView.getLoadMoreView() != null
                    ? mIRefreshLoadView.getLoadMoreView() :
                    mManager.getLoadMoreFoot() != null ?
                            mManager.getLoadMoreFoot().createDefaultLoadMoreView(mAdapter) :
                            new NaViLoadMoreView(mContext).getBuilder().build());
            if (mIRefreshLoadView.isItemClickEnable()) {
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mIRefreshLoadView.onItemClicked(adapter, view, position);
                    }

                });
            }
        }
    }

    public void setLoadMore(boolean enable) {
        mAdapter.setOnLoadMoreListener(enable ? mIRefreshLoadView : null, mRecyclerView);
    }

    private void setStatusManager() {
        //优先使用当前配置
        View contentView = mIRefreshLoadView.getMultiStatusContentView();
        if (contentView == null) {
            contentView = mRefreshLayout;
        }
        if (contentView == null) {
            contentView = mRecyclerView;
        }
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.multi_empty)
                .setDefaultEmptyClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setDefaultLoadingText( R.string.multi_loading)
                .setDefaultErrorText(R.string.multi_error)
                .setDefaultErrorClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        if (mIRefreshLoadView.getEmptyClickListener() != null) {
                            mIRefreshLoadView.getEmptyClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        if (mIRefreshLoadView.getErrorClickListener() != null) {
                            mIRefreshLoadView.getErrorClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        if (mIRefreshLoadView.getCustomerClickListener() != null) {
                            mIRefreshLoadView.getCustomerClickListener().onClick(view);
                            return;
                        }
                        mStatusManager.showLoadingLayout();
                        mIRefreshLoadView.onRefresh(mRefreshLayout);
                    }
                });
        if (mManager != null && mManager.getMultiStatusView() != null) {
            mManager.getMultiStatusView().setMultiStatusView(builder, mIRefreshLoadView);
        }
        mIRefreshLoadView.setMultiStatusView(builder);
        mStatusManager = builder.build();
        mStatusManager.showLoadingLayout();
    }

    /**
     * 获取布局里的刷新Layout
     *
     * @param rootView
     * @return
     */
    private void getRefreshLayout(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.smartLayout_rootFastLib);
        if (mRefreshLayout == null) {
            mRefreshLayout = FindViewUtil.getTargetView(rootView, SmartRefreshLayout.class);
        }
    }

    /**
     * 获取布局RecyclerView
     *
     * @param rootView
     */
    private void getRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.rv_content);
        if (mRecyclerView == null) {
            mRecyclerView = FindViewUtil.getTargetView(rootView, RecyclerView.class);
        }
    }

    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        mRefreshLayout = null;
        mRecyclerView = null;
        mAdapter = null;
        mStatusManager = null;
        mIRefreshLoadView = null;
        mContext = null;
        mManager = null;
        mRootView = null;
        mTargetClass = null;
        TourcooLogUtil.i("RefreshLoadDelegate", "onDestroy");
    }

}

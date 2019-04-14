package com.tourcoo.carnet.core.frame.base.fragment;

import android.os.Bundle;

import com.tourcoo.carnet.core.frame.delegate.TitleBarDelegate;
import com.tourcoo.carnet.core.frame.interfaces.ITitleView;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :设置有TitleBar及下拉刷新基类Fragment
 * @company :翼迈科技
 * @date 2019年 03月 05日 20时50分
 * @Email: 971613168@qq.com
 */
public abstract class BaseTitleRefreshViewFragment<T> extends BaseRefreshFragment<T> implements ITitleView {

    protected TitleBarDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFastTitleDelegate = new TitleBarDelegate(mContentView, this, this.getClass());
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }
}

package com.tourcoo.carnet.core.frame.base.activity;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.delegate.MainTabDelegate;
import com.tourcoo.carnet.core.frame.interfaces.IHomeView;
import com.tourcoo.carnet.core.log.TourCooLogUtil;

import androidx.viewpager.widget.ViewPager;

/**
 * @author :zhoujian
 * @description : 创建主页Activity布局
 * @company :翼迈科技
 * @date 2019年03月05日下午 05:17
 * @Email: 971613168@qq.com
 */
public abstract class BaseMainActivity extends BaseActivity implements IHomeView {

    protected MainTabDelegate mMainTabDelegate;

    @Override
    public void setViewPager(ViewPager mViewPager) {

    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return isSwipeEnable() ? R.layout.base_activity_main_view_pager : R.layout.base_activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMainTabDelegate != null) {
            mMainTabDelegate.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mMainTabDelegate = new MainTabDelegate(mContentView, this, this);
    }

    @Override
    public void onTabReselect(int position) {
    }

    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public void onBackPressed() {
        quitApp();
    }

    @Override
    protected void onDestroy() {
        if (mMainTabDelegate != null) {
            mMainTabDelegate.onDestroy();
        }
        super.onDestroy();
    }


    protected void setBackgroundColor(int color){
        if(mMainTabDelegate != null && mMainTabDelegate.mTabLayout != null ){
            mMainTabDelegate.mTabLayout.setBackgroundColor(color);
        }
    }



}

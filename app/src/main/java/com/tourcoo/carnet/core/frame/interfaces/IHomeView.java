package com.tourcoo.carnet.core.frame.interfaces;

import android.os.Bundle;

import com.aries.ui.view.tab.CommonTabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.tourcoo.carnet.core.frame.entity.TabEntity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author :zhoujian
 * @description :主页控制接口
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时38分
 * @Email: 971613168@qq.com
 */
public interface IHomeView extends OnTabSelectListener {

    /**
     * 控制主界面Fragment是否可滑动切换
     *
     * @return true 可滑动切换(配合ViewPager)
     */
    boolean isSwipeEnable();

    /**
     * 用于添加Tab属性(文字-图标)
     *
     * @return 主页tab数组
     */
    @Nullable
    List<TabEntity> getTabList();

    /**
     * 获取onCreate 携带参数
     *
     * @return
     */
    Bundle getSavedInstanceState();

    /**
     * 返回 CommonTabLayout  对象用于自定义设置
     *
     * @param tabLayout CommonTabLayout 对象用于单独属性调节
     */
    void setTabLayout(CommonTabLayout tabLayout);

    /**
     * 设置ViewPager属性
     *
     * @param mViewPager ViewPager属性控制
     */
    void setViewPager(ViewPager mViewPager);
}

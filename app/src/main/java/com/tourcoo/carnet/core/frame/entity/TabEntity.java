package com.tourcoo.carnet.core.frame.entity;

import android.text.TextUtils;

import com.aries.ui.view.tab.listener.CustomTabEntity;
import com.tourcoo.carnet.core.frame.UiConfigManager;

import androidx.fragment.app.Fragment;

/**
 * @author :zhoujian
 * @description :主页Tab实体类
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时40分
 * @Email: 971613168@qq.com
 */
public class TabEntity implements CustomTabEntity {

    public String mTitle;
    public int mSelectedIcon;
    public int mUnSelectedIcon;
    public Fragment mFragment;

    public TabEntity(String title, int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this.mTitle = title;
        this.mSelectedIcon = selectedIcon;
        this.mUnSelectedIcon = unSelectedIcon;
        this.mFragment = fragment;
    }

    public TabEntity(int title, int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this(UiConfigManager.getInstance().getApplication().getString(title), unSelectedIcon, selectedIcon, fragment);
    }

    public TabEntity(int unSelectedIcon, int selectedIcon, Fragment fragment) {
        this("", unSelectedIcon, selectedIcon, fragment);
    }

    @Override
    public String getTabTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return "";
        }
        return mTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return mSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return mUnSelectedIcon;
    }
}

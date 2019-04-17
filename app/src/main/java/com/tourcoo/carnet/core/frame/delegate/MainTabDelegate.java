package com.tourcoo.carnet.core.frame.delegate;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.tab.CommonTabLayout;
import com.aries.ui.view.tab.listener.CustomTabEntity;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.entity.TabEntity;
import com.tourcoo.carnet.core.frame.interfaces.IHomeView;
import com.tourcoo.carnet.core.frame.manager.TabLayoutManager;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * @author :zhoujian
 * @description : 主Tab页代理
 * @company :翼迈科技
 * @date 2019年03月04日上午 09:56
 * @Email: 971613168@qq.com
 */
public class MainTabDelegate {
    private static final String TAG = "MainTabDelegate";
    private Context mContext;
    private Object mObject;
    private FragmentManager mFragmentManager;
    private IHomeView mIHomeView;
    public CommonTabLayout mTabLayout;
    public ViewPager mViewPager;
    public List<TabEntity> mListFastTab = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private Bundle mSavedInstanceState;

    public static final String SAVED_INSTANCE_STATE_CURRENT_TAB = "saved_instance_state_current_tab";
    /**
     * 存放历史主页Tab数量
     */
    public static final String SAVED_INSTANCE_STATE_FRAGMENT_NUMBER = "saved_instance_state_fragment_number";
    /**
     * 获取历史主页Tab数量Key --
     */
    public static final String SAVED_INSTANCE_STATE_KEY_FRAGMENT = "saved_instance_state_key_fragment";
    public static final String SAVED_INSTANCE_STATE_KEY_TITLE = "saved_instance_state_key_title";
    public static final String SAVED_INSTANCE_STATE_KEY_SELECTED_ICON = "saved_instance_state_key_selected_icon";
    public static final String SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON = "saved_instance_state_key_un_selected_icon";

    public MainTabDelegate(View rootView, FragmentActivity activity, IHomeView IHomeView) {
        if (IHomeView == null || rootView == null || activity == null) {
            return;
        }
        this.mContext = activity;
        this.mObject = activity;
        this.mIHomeView = IHomeView;
        mFragmentManager = activity.getSupportFragmentManager();
        mSavedInstanceState = IHomeView.getSavedInstanceState();
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    public MainTabDelegate(View rootView, Fragment activity, IHomeView IHomeView) {
        if (IHomeView == null || rootView == null || activity == null) {
            return;
        }
        this.mContext = activity.getContext();
        this.mObject = activity;
        this.mIHomeView = IHomeView;
        mFragmentManager = activity.getChildFragmentManager();
        mSavedInstanceState = IHomeView.getSavedInstanceState();
        getTabLayout(rootView);
        getViewPager(rootView);
        initTabLayout();
    }

    private void initTabLayout() {
        if (mTabLayout == null) {
            return;
        }
        mListFastTab = new ArrayList<>();
        getSaveState();
        //本地缓存及接口都没有获取到
        if (mListFastTab == null || mListFastTab.size() == 0) {
            return;
        }
        mTabLayout.setBackgroundResource(R.color.colorTabBackground);
        mTabLayout.getDelegate()
                .setTextSelectColor(ContextCompat.getColor(mContext, R.color.blueCommon))
                .setTextUnSelectColor(ContextCompat.getColor(mContext, R.color.colorTabTextUnSelect))
                .setUnderlineColor(ContextCompat.getColor(mContext, R.color.colorTabUnderline))
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_text_size))
                .setTextSelectSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_text_size))
                .setUnderlineGravity(Gravity.TOP)
                .setUnderlineHeight(mContext.getResources().getDimension(R.dimen.dp_tab_underline))
                .setIconMargin(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_margin))
                .setIconWidth(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_icon))
                .setIconHeight(mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_icon))
                //设置指示器高度为0
                .setIndicatorHeight(0);
        ViewGroup.LayoutParams params = mTabLayout.getLayoutParams();
        if (params != null) {
            params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dp_tab_height);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mListFastTab.size(); i++) {
            TabEntity entity = mListFastTab.get(i);
            fragments.add(entity.mFragment);
            mTabEntities.add(entity);
        }
        if (mIHomeView.isSwipeEnable()) {
            initViewPager(fragments);
        } else {
            if (mObject instanceof FragmentActivity) {
                mTabLayout.setTabData(mTabEntities, (FragmentActivity) mObject, R.id.fLayout_containerFastMain, fragments);
                mTabLayout.setOnTabSelectListener(mIHomeView);
            } else if (mObject instanceof Fragment) {
                FragmentActivity activity = ((Fragment) mObject).getActivity();
                if (activity != null) {
                    mTabLayout.setTabData(mTabEntities, activity, R.id.fLayout_containerFastMain, fragments);
                    mTabLayout.setOnTabSelectListener(mIHomeView);
                }
            }

        }
        mIHomeView.setTabLayout(mTabLayout);
        mIHomeView.setViewPager(mViewPager);
    }

    private void initViewPager(final List<Fragment> fragments) {
        if (mViewPager != null) {
            if (mObject instanceof FragmentActivity) {
                TabLayoutManager.getInstance().setCommonTabData((FragmentActivity) mObject, mTabLayout, mViewPager, mTabEntities, fragments, mIHomeView);
            } else if (mObject instanceof Fragment) {
                TabLayoutManager.getInstance().setCommonTabData((Fragment) mObject, mTabLayout, mViewPager, mTabEntities, fragments, mIHomeView);
            }
        }
    }

    /**
     * 获取布局里的CommonTabLayout
     *
     * @param rootView
     * @return
     */
    private void getTabLayout(View rootView) {
        mTabLayout = rootView.findViewById(R.id.tabLayout_commonFastLib);
        if (mTabLayout == null) {
            mTabLayout = FindViewUtil.getTargetView(rootView, CommonTabLayout.class);
        }
    }

    /**
     * 获取ViewPager
     *
     * @param rootView
     */
    private void getViewPager(View rootView) {
        mViewPager = rootView.findViewById(R.id.view_pager_content);
        if (mViewPager == null) {
            mViewPager = FindViewUtil.getTargetView(rootView, ViewPager.class);
        }
    }

    /**
     * 保存Fragment数据
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        if (mListFastTab != null && mTabLayout != null && mFragmentManager != null) {
            outState.putInt(SAVED_INSTANCE_STATE_FRAGMENT_NUMBER, mListFastTab.size());
            outState.putInt(SAVED_INSTANCE_STATE_CURRENT_TAB, mTabLayout.getCurrentTab());
            List<TabEntity> listFastTab = mListFastTab;
            for (int i = 0; i < listFastTab.size(); i++) {
                TabEntity item = listFastTab.get(i);
                if (item.mFragment != null) {
                    outState.putInt(SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON + i, item.mUnSelectedIcon);
                    outState.putInt(SAVED_INSTANCE_STATE_KEY_SELECTED_ICON + i, item.mSelectedIcon);
                    outState.putString(SAVED_INSTANCE_STATE_KEY_TITLE + i, item.mTitle);
                    mFragmentManager.putFragment(outState, SAVED_INSTANCE_STATE_KEY_FRAGMENT + i, item.mFragment);
                }
            }
        }
    }

    /**
     * 获取本地存储信息
     */
    private void getSaveState() {
        //从本地缓存获取
        if (mSavedInstanceState != null) {
            //先获取数量
            int size = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_FRAGMENT_NUMBER);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Fragment fragment = mFragmentManager.getFragment(mSavedInstanceState, SAVED_INSTANCE_STATE_KEY_FRAGMENT + i);
                    String title = mSavedInstanceState.getString(SAVED_INSTANCE_STATE_KEY_TITLE + i);
                    int selectedIcon = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY_SELECTED_ICON + i);
                    int unSelectedIcon = mSavedInstanceState.getInt(SAVED_INSTANCE_STATE_KEY_UN_SELECTED_ICON + i);
                    mListFastTab.add(new TabEntity(title, unSelectedIcon, selectedIcon, fragment));
                }
            }
        }
        //没有获取到
        if (mListFastTab == null || mListFastTab.size() == 0) {
            mListFastTab = mIHomeView.getTabList();
        }
    }

    /**
     * 与Activity 及Fragment onDestroy 及时解绑释放避免内存泄露
     */
    public void onDestroy() {
        mContext = null;
        mObject = null;
        mFragmentManager = null;
        mIHomeView = null;
        mTabLayout = null;
        mViewPager = null;
        if (mListFastTab != null) {
            mListFastTab.clear();
            mListFastTab = null;
        }
        if (mTabEntities != null) {
            mTabEntities.clear();
            mTabEntities = null;
        }
        if (mSavedInstanceState != null) {
            mSavedInstanceState.clear();
            mSavedInstanceState = null;
        }
        TourCooLogUtil.e(TAG, "onDestroy");
    }
}

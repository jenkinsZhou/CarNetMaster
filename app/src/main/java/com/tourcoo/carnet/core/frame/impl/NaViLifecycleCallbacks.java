package com.tourcoo.carnet.core.frame.impl;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.tab.CommonTabLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.CommonConfig;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.activity.BaseMainActivity;
import com.tourcoo.carnet.core.frame.interfaces.ActivityFragmentControl;
import com.tourcoo.carnet.core.frame.interfaces.SwipeBackControl;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.util.DrawableUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;
import com.tourcoo.carnet.core.widget.core.util.RomUtil;
import com.tourcoo.carnet.core.widget.core.util.SizeUtil;
import com.tourcoo.carnet.core.widget.core.view.StatusViewHelper;
import com.tourcoo.carnet.core.widget.core.view.navigation.NavigationViewHelper;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import cn.bingoogolapple.swipebacklayout.BGAKeyboardUtil;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * @author :zhoujian
 * @description :Activity/Fragment生命周期
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时27分
 * @Email: 971613168@qq.com
 */
public class NaViLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String TAG = getClass().getSimpleName();
    private ActivityFragmentControl mActivityFragmentControl;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;
    private SwipeBackControl mSwipeBackControl;
    private String IS_SET_STATUS_VIEW_HELPER = "IS_SET_STATUS_VIEW_HELPER";
    private String IS_SET_NAVIGATION_VIEW_HELPER = "IS_SET_NAVIGATION_VIEW_HELPER";
    private String IS_SET_CONTENT_VIEW_BACKGROUND = "IS_SET_CONTENT_VIEW_BACKGROUND";

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        TourcooLogUtil.i(TAG, "onActivityCreated:" + activity.getClass().getSimpleName() + ";contentView:" + TourcooUtil.getRootView(activity));
        getControl();

        //统一Activity堆栈管理
        StackUtil.getInstance().push(activity);
        //统一横竖屏操作
        if (mActivityFragmentControl != null) {
            mActivityFragmentControl.setRequestedOrientation(activity);
        }
        //统一Fragment生命周期处理
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentManager.registerFragmentLifecycleCallbacks(this, true);
            if (mFragmentLifecycleCallbacks != null) {
                fragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
            }
        }
        //设置滑动返回
        if (!(activity instanceof BGASwipeBackHelper.Delegate)) {
            setSwipeBack(activity);
        }
        //回调给开发者实现自己应用逻辑
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        getControl();
        TourcooLogUtil.i(TAG, "onActivityStarted:" + activity.getClass().getSimpleName() + ";contentView:" + TourcooUtil.getRootView(activity));
        boolean isSet = activity.getIntent().getBooleanExtra(IS_SET_CONTENT_VIEW_BACKGROUND, false);
        if (!isSet) {
            setContentViewBackground(TourcooUtil.getRootView(activity), activity.getClass());
        }
        //设置状态栏
        setStatusBar(activity);
        //设置虚拟导航栏功能
        setNavigationBar(activity);
        //回调开发者处理
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        getControl();
        TourcooLogUtil.i(TAG, "onActivityResumed:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        getControl();
        TourcooLogUtil.i(TAG, "onActivityPaused:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        //Activity销毁前的时机需要关闭软键盘-在onActivityStopped及onActivityDestroyed生命周期内已无法关闭
        if (activity.isFinishing()) {
            BGAKeyboardUtil.closeKeyboard(activity);
        }
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        getControl();
        TourcooLogUtil.i(TAG, "onActivityStopped:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        getControl();
        TourcooLogUtil.i(TAG, "onActivitySaveInstanceState:" + activity.getClass().getSimpleName());
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //横竖屏会重绘将状态重置
        if (activity.getIntent() != null) {
            activity.getIntent().putExtra(IS_SET_STATUS_VIEW_HELPER, false);
            activity.getIntent().putExtra(IS_SET_NAVIGATION_VIEW_HELPER, false);
            activity.getIntent().putExtra(IS_SET_CONTENT_VIEW_BACKGROUND, false);
        }
        getControl();
        TourcooLogUtil.i(TAG, "onActivityDestroyed:" + activity.getClass().getSimpleName() + ";isFinishing:" + activity.isFinishing());
        StackUtil.getInstance().pop(activity, false);
        //统一注销Fragment生命周期处理
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            if (mFragmentLifecycleCallbacks != null) {
                fragmentManager.unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);
            }
        }
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(activity);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        boolean isSet = f.getArguments() != null ? f.getArguments().getBoolean(IS_SET_CONTENT_VIEW_BACKGROUND, false) : false;
        if (!isSet) {
            setContentViewBackground(v, f.getClass());
        }
    }

    @Override
    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDestroyed(fm, f);
        if (f.getArguments() != null) {
            f.getArguments().putBoolean(IS_SET_CONTENT_VIEW_BACKGROUND, false);
        }
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (f.getArguments() != null) {
            f.getArguments().putBoolean(IS_SET_CONTENT_VIEW_BACKGROUND, false);
        }
    }

    /**
     * 实时获取回调
     */

    private void getControl() {
        mSwipeBackControl = UiConfigManager.getInstance().getSwipeBackControl();
        mActivityFragmentControl = UiConfigManager.getInstance().getActivityFragmentControl();
        if (mActivityFragmentControl == null) {
            return;
        }
        mActivityLifecycleCallbacks = mActivityFragmentControl.getActivityLifecycleCallbacks();
        mFragmentLifecycleCallbacks = mActivityFragmentControl.getFragmentLifecycleCallbacks();
    }

    /**
     * 回调设置Activity/Fragment背景
     *
     * @param v
     * @param cls
     */
    private void setContentViewBackground(View v, Class<?> cls) {
        if (mActivityFragmentControl != null && v != null) {
            mActivityFragmentControl.setContentViewBackground(v, cls);
        }
    }

    /**
     * 设置滑动返回相关
     *
     * @param activity
     */
    private void setSwipeBack(final Activity activity) {
        TourcooLogUtil.i(TAG, activity + getClass().getSimpleName() + ":设置Activity滑动返回");
        //需设置activity window背景为透明避免滑动过程中漏出背景也可减少背景层级降低过度绘制
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final BGASwipeBackHelper swipeBackHelper = new BGASwipeBackHelper(activity, new BGASwipeBackHelper.Delegate() {
            @Override
            public boolean isSupportSwipeBack() {
                return mSwipeBackControl != null ? mSwipeBackControl.isSwipeBackEnable(activity) : true;
            }

            @Override
            public void onSwipeBackLayoutSlide(float slideOffset) {
                TourcooLogUtil.i(TAG, "onSwipeBackLayoutCancel");
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutSlide(activity, slideOffset);
                }
            }

            @Override
            public void onSwipeBackLayoutCancel() {
                TourcooLogUtil.i(TAG, "onSwipeBackLayoutCancel");
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutCancel(activity);
                }
            }

            @Override
            public void onSwipeBackLayoutExecuted() {
                //设置退出动画-确保效果准确
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                BGAKeyboardUtil.closeKeyboard(activity);
                activity.finish();
                activity.overridePendingTransition(0, R.anim.bga_sbl_activity_swipeback_exit);
                if (mSwipeBackControl != null) {
                    mSwipeBackControl.onSwipeBackLayoutExecuted(activity);
                }
            }
        })
                //设置滑动背景
                .setShadowResId(R.drawable.bga_sbl_shadow)
                //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                .setIsNavigationBarOverlap(true)
                .setIsShadowAlphaGradient(true);
        //用于全局控制
        if (mSwipeBackControl != null) {
            mSwipeBackControl.setSwipeBack(activity, swipeBackHelper);
        }
    }

    private void setStatusBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(IS_SET_STATUS_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        TitleBarView titleBarView = FindViewUtil.getTargetView(activity.getWindow().getDecorView(), TitleBarView.class);
        //不包含TitleBarView处理
        if (titleBarView == null && !(activity instanceof BaseMainActivity)) {
            View topView = getTopView(TourcooUtil.getRootView(activity));
            TourcooLogUtil.i(TAG, "其它三方库设置状态栏沉浸");
            StatusViewHelper statusViewHelper = StatusViewHelper.with(activity)
                    .setControlEnable(true)
                    .setPlusStatusViewEnable(false)
                    .setTransEnable(true)
                    .setTopView(topView);
            if (topView != null && topView.getBackground() != null) {
                Drawable drawable = topView.getBackground().mutate();
                statusViewHelper.setStatusLayoutDrawable(drawable);
            }
            boolean isInit = mActivityFragmentControl != null ? mActivityFragmentControl.setStatusBar(activity, statusViewHelper, topView) : true;
            if (isInit) {
                statusViewHelper.init();
                activity.getIntent().putExtra(IS_SET_STATUS_VIEW_HELPER, true);
            }
        }
    }

    /**
     * 设置全局虚拟导航栏功能
     *
     * @param activity
     */
    private void setNavigationBar(Activity activity) {
        boolean isSet = activity.getIntent().getBooleanExtra(IS_SET_NAVIGATION_VIEW_HELPER, false);
        if (isSet) {
            return;
        }
        TourcooLogUtil.i(TAG, "setNavigationBars:设置虚拟导航栏");
        View bottomView = TourcooUtil.getRootView(activity);
        //继承FastMainActivity底部View处理
        if (BaseMainActivity.class.isAssignableFrom(activity.getClass())) {
            CommonTabLayout tabLayout = FindViewUtil.getTargetView(bottomView, CommonTabLayout.class);
            if (tabLayout != null) {
                bottomView = tabLayout;
            }
        }
        Drawable drawableTop = ContextCompat.getDrawable(activity, R.color.colorLineGray);
        DrawableUtil.setDrawableWidthHeight(drawableTop, SizeUtil.getScreenWidth(), SizeUtil.dp2px(0.5f));
        //设置虚拟导航栏控制
        NavigationViewHelper navigationViewHelper = NavigationViewHelper.with(activity)
                .setLogEnable(CommonConfig.DEBUG_MODE)
        //是否控制虚拟导航栏true 后续属性有效--第一优先级
                .setControlEnable(true)
                //是否全透明导航栏优先级第二--同步设置setNavigationViewColor故注意调用顺序
                //华为的半透明和全透明类似
                .setTransEnable(isTrans())
                //是否增加假的NavigationView用于沉浸至虚拟导航栏遮住
                .setPlusNavigationViewEnable(isTrans())
                //设置是否控制底部输入框--默认属性
                .setControlBottomEditTextEnable(true)
                //设置最下边View用于增加paddingBottom--建议activity 根布局
                .setBottomView(bottomView)
                //影响setPlusNavigationViewEnable(true)单个条件
                //或者(setPlusNavigationViewEnable(false)&&setControlEnable(true))--两个前置条件
                //半透明默认设置102
                .setNavigationViewColor(Color.argb(isTrans() ? 0 : 102, 0, 0, 0))
                //setPlusNavigationViewEnable(true)才有效注意与setNavigationViewColor调用顺序
//                .setNavigationViewDrawable(mContext.getResources().getDrawable(R.drawable.img_bg_login))
                //setPlusNavigationViewEnable(true)有效
                .setNavigationViewDrawableTop(drawableTop)
                .setNavigationLayoutColor(Color.WHITE);
        boolean isInit = mActivityFragmentControl != null ? mActivityFragmentControl.setNavigationBar(activity, navigationViewHelper, bottomView) : true;
        if (isInit) {
            activity.getIntent().putExtra(IS_SET_NAVIGATION_VIEW_HELPER, true);
            navigationViewHelper.init();
        }
    }

    /**
     * 是否全透明-华为4.1以上可根据导航栏位置颜色设置导航图标颜色
     *
     * @return
     */
    protected boolean isTrans() {
        return RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0);
    }

    /**
     * 获取Activity 顶部View(用于延伸至状态栏下边)
     *
     * @param target
     * @return
     */
    private View getTopView(View target) {
        if (target != null && target instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) target;
            if (group.getChildCount() > 0) {
                target = ((ViewGroup) target).getChildAt(0);
            }
        }
        return target;
    }
}

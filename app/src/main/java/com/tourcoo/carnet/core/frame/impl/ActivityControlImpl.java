package com.tourcoo.carnet.core.frame.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.MainActivity;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.CommonConfig;
import com.tourcoo.carnet.core.frame.interfaces.ActivityDispatchEventControl;
import com.tourcoo.carnet.core.frame.interfaces.ActivityFragmentControl;
import com.tourcoo.carnet.core.frame.interfaces.ActivityKeyEventControl;
import com.tourcoo.carnet.core.frame.util.SnackBarUtil;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.module.SplashActivity;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;
import com.tourcoo.carnet.core.widget.core.util.RomUtil;
import com.tourcoo.carnet.core.widget.core.util.SizeUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.StatusViewHelper;
import com.tourcoo.carnet.core.widget.core.view.navigation.KeyboardHelper;
import com.tourcoo.carnet.core.widget.core.view.navigation.NavigationBarUtil;
import com.tourcoo.carnet.core.widget.core.view.navigation.NavigationViewHelper;
import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.PicturePreviewActivity;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.tourcoo.carnet.CarNetApplication.isControlNavigation;


/**
 * @author :zhoujian
 * @description :Activity/Fragment 生命周期全局处理及BasisActivity 的按键处理
 * @company :翼迈科技
 * @date 2019年 03月 05日 22时30分
 * @Email: 971613168@qq.com
 */
public class ActivityControlImpl implements ActivityFragmentControl, ActivityKeyEventControl, ActivityDispatchEventControl {
    private static String TAG = "ActivityControlImpl";
    /**
     * Audio管理器，用了控制音量
     */
    private AudioManager mAudioManager = null;
    private int mMaxVolume = 0;
    private int mMinVolume = 0;
    private int mCurrentVolume = 0;

    private void volume(int value, boolean plus) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) CarNetApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
            // 获取最大音乐音量
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            // 获取最小音乐音量
            mMinVolume = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }
        if (plus) {
            if (mCurrentVolume >= mMaxVolume) {
                ToastUtil.show("当前音量已最大");
                return;
            }
            mCurrentVolume += value;
        } else {
            if (mCurrentVolume <= mMinVolume) {
                ToastUtil.show("当前音量已最小");
                return;
            }
            mCurrentVolume -= value;
        }
        if (mCurrentVolume < mMinVolume) {
            mCurrentVolume = mMinVolume;
        }
        if (mCurrentVolume > mMaxVolume) {
            mCurrentVolume = mMaxVolume;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, AudioManager.FLAG_PLAY_SOUND);
        // 获取当前音乐音量
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        TourcooLogUtil.i(TAG, "max:" + mMaxVolume + ";min:" + mMinVolume + ";current:" + mCurrentVolume);
        SnackBarUtil.with(StackUtil.getInstance().getCurrent().getWindow().getDecorView())
                .setBgColor(Color.LTGRAY)
                .setMessageColor(Color.MAGENTA)
                .setMessage("当前音量:" + mCurrentVolume)
                .setBottomMargin(NavigationBarUtil.getNavigationBarHeight(StackUtil.getInstance().getCurrent()))
                .show();

    }

    @Override
    public boolean onKeyDown(Activity activity, int keyCode, KeyEvent event) {
        //演示拦截系统音量键控制--类似抖音
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                volume(1, false);
                TourcooLogUtil.i(TAG, "volumeDown-activity:" + activity.getClass().getSimpleName());
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                volume(1, true);
                TourcooLogUtil.i(TAG, "volumeUp-activity:" + activity.getClass().getSimpleName());
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyLongPress(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyShortcut(Activity activity, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyMultiple(Activity activity, int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    /**
     * 设置Fragment/Activity根布局背景
     *
     * @param contentView
     * @param cls
     */
    @Override
    public void setContentViewBackground(View contentView, Class<?> cls) {
        //避免背景色重复
        if (!Fragment.class.isAssignableFrom(cls)
                && contentView.getBackground() == null) {
            contentView.setBackgroundResource(R.color.colorBackground);
        }
    }

    /**
     * 设置屏幕方向--注意targetSDK设置27以上不能设置windowIsTranslucent=true属性不然应用直接崩溃
     * 错误为 Only fullscreen activities can request orientation
     * 默认自动 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     * 竖屏 ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
     * 横屏 ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
     * {@link ActivityInfo#screenOrientation ActivityInfo.screenOrientation}
     *
     * @param activity
     */
    @Override
    public void setRequestedOrientation(Activity activity) {
        //全局控制屏幕横竖屏
        //先判断xml没有设置屏幕模式避免将开发者本身想设置的覆盖掉
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            try {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } catch (Exception e) {
                e.printStackTrace();
                TourcooLogUtil.e(TAG, "setRequestedOrientation:" + e.getMessage());
            }
        }
    }

    /**
     * 设置非FastLib且未实现Activity 状态栏功能的三方Activity 状态栏沉浸
     *
     * @param activity
     * @param helper
     * @return
     */
    @Override
    public boolean setStatusBar(Activity activity, StatusViewHelper helper, View topView) {
        boolean isSupportStatusBarFont = StatusBarUtil.isSupportStatusBarFontChange();
        StatusBarUtil.setStatusBarLightMode(activity);
        helper.setTransEnable(isSupportStatusBarFont)
                .setPlusStatusViewEnable(true)
                .setStatusLayoutColor(Color.WHITE);
        setStatusBarActivity(activity);
        return true;
    }

    /**
     * {@link FastLifecycleCallbacks#onActivityStarted(Activity)}
     *
     * @param activity
     * @param helper
     */
    @Override
    public boolean setNavigationBar(Activity activity, NavigationViewHelper helper, View bottomView) {
        //其它默认属性请参考FastLifecycleCallbacks
        helper.setLogEnable(CommonConfig.DEBUG_MODE)
                .setTransEnable(isTrans(activity))
                .setPlusNavigationViewEnable(isTrans(activity))
                .setOnKeyboardVisibilityChangedListener(mOnKeyboardVisibilityChangedListener)
                .setBottomView(PicturePreviewActivity.class.isAssignableFrom(activity.getClass()) ?
                        FindViewUtil.getTargetView(bottomView, R.id.select_bar_layout) : bottomView)
                .setNavigationViewColor(Color.argb(isTrans(activity) ? 0 : 102, 0, 0, 0))
                .setNavigationLayoutColor(Color.WHITE);
        if (!isControlNavigation() && !(activity instanceof MainActivity)) {
            KeyboardHelper.with(activity)
                    .setEnable()
                    .setOnKeyboardVisibilityChangedListener(mOnKeyboardVisibilityChangedListener);
        }
        return isControlNavigation();
    }

    @Override
    public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return null;
    }

    private KeyboardHelper.OnKeyboardVisibilityChangedListener mOnKeyboardVisibilityChangedListener = (activity, isOpen, heightDiff, navigationHeight) -> {
        View mContent = TourcooUtil.getRootView(activity);
        TourcooLogUtil.i("onKeyboardVisibilityChanged", "activity:" + activity + ";isOpen:" + isOpen + ";heightDiff:" + heightDiff + ";navigationHeight:" + navigationHeight);
        return false;
    };


    /**
     * Fragment 生命周期回调
     *
     * @return
     */
    @Override
    public FragmentManager.FragmentLifecycleCallbacks getFragmentLifecycleCallbacks() {
        return new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                super.onFragmentResumed(fm, f);
         /*       LoggerManager.i(TAG, "onFragmentResumed:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageStart(f.getClass().getSimpleName());*/
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                super.onFragmentPaused(fm, f);
       /*         LoggerManager.i(TAG, "onFragmentPaused:统计Fragment:" + f.getClass().getSimpleName());
                MobclickAgent.onPageEnd(f.getClass().getSimpleName());*/
            }

            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
                TourcooLogUtil.i(TAG, "onFragmentDestroyed:" + f.getClass().getSimpleName());
            }
        };
    }


    /**
     * 根据程序使用的三方库进行改造:本示例使用的三方库实现了自己的沉浸式状态栏及导航栏但和Demo的滑动返回不搭配故做相应调整
     *
     * @param activity
     */
    private void setStatusBarActivity(Activity activity) {
        if (PictureBaseActivity.class.isAssignableFrom(activity.getClass())) {
            View contentView = TourcooUtil.getRootView(activity);
            //该属性会影响适配滑动返回效果
            contentView.setFitsSystemWindows(false);
            ImageView imageView = contentView != null ? contentView.findViewById(R.id.picture_left_back) : null;
            if (imageView != null) {
                RelativeLayout layout = contentView.findViewById(R.id.rl_picture_title);
                if (layout != null) {
                    ViewCompat.setElevation(layout, activity.getResources().getDimension(R.dimen.dp_elevation));
                }
                //调整返回箭头大小
                imageView.setPadding(SizeUtil.dp2px(15), SizeUtil.dp2px(4), SizeUtil.dp2px(4), SizeUtil.dp2px(4));
            }
        }
    }

    /**
     * 是否全透明-华为4.1以上可根据导航栏位置颜色设置导航图标颜色
     *
     * @return
     */
    protected boolean isTrans(Activity activity) {
        return RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0) && activity.getClass() != SplashActivity.class;
    }

    /**
     * @param activity
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(Activity activity, MotionEvent event) {
        //根据事件派发全局控制点击非EditText 关闭软键盘
        if (activity != null) {
            KeyboardHelper.handleAutoCloseKeyboard(true, activity.getCurrentFocus(), event, activity);
        }
        return false;
    }

    @Override
    public boolean dispatchGenericMotionEvent(Activity activity, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(Activity activity, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyShortcutEvent(Activity activity, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTrackballEvent(Activity activity, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(Activity activity, AccessibilityEvent event) {
        return false;
    }
}

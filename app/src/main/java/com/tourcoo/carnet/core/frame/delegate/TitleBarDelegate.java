package com.tourcoo.carnet.core.frame.delegate;

import android.app.Activity;
import android.view.View;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.interfaces.ITitleView;
import com.tourcoo.carnet.core.frame.interfaces.TitleBarViewControl;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description : 带TitleBarView 的Activity及Fragment代理类
 * @company :翼迈科技
 * @date 2019年03月04日下午 04:39
 * @Email: 971613168@qq.com
 */
public class TitleBarDelegate {

    public TitleBarView mTitleBar;

    public TitleBarDelegate(View rootView, ITitleView iTitleBarView, final Class<?> cls) {
        mTitleBar = rootView.findViewById(R.id.titleBarView);
        if (mTitleBar == null) {
            mTitleBar = FindViewUtil.getTargetView(rootView, TitleBarView.class);
        }
        if (mTitleBar == null) {
            return;
        }
        TourcooLogUtil.i("class:" + cls.getSimpleName());
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        final Activity activity = StackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        mTitleBar.setLeftTextDrawable(activity != null ? R.mipmap.ic_return : 0)
                //.setLeftTextDrawableTintResource(R.color.colorTitleText)
                .setOnLeftTextClickListener(activity == null ? null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = StackUtil.getInstance().getActivity(cls);
                        //增加判断避免快速点击返回键造成崩溃
                        if (activity == null) {
                            return;
                        }
                        activity.onBackPressed();
                    }
                })
                .setTextColorResource(R.color.colorTitleText)
                //.setRightTextDrawableTintResource(R.color.colorTitleText)
                //.setActionTintResource(R.color.colorTitleText)
                .setTitleMainText(getTitle(activity));
        TitleBarViewControl titleBarViewControl = UiConfigManager.getInstance().getTitleBarViewControl();
        if (titleBarViewControl != null) {
            titleBarViewControl.createTitleBarViewControl(mTitleBar, cls);
        }
        iTitleBarView.beforeSetTitleBar(mTitleBar);
        iTitleBarView.setTitleBar(mTitleBar);
    }

    /**
     * 获取Activity 标题({@link Activity#getTitle()}获取不和应用名称一致才进行设置-因Manifest未设置Activity的label属性获取的是应用名称)
     *
     * @param activity
     * @return
     */
    private CharSequence getTitle(Activity activity) {
        if (activity != null) {
            CharSequence appName = TourcooUtil.getAppName(activity);
            CharSequence label = activity.getTitle();
            if (label != null && !label.equals(appName)) {
                return label;
            }
        }
        return "";
    }
}

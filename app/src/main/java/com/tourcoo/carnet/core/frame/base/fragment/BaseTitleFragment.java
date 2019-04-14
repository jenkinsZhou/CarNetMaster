package com.tourcoo.carnet.core.frame.base.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.luck.picture.lib.dialog.PictureDialog;
import com.tourcoo.carnet.core.frame.delegate.TitleBarDelegate;
import com.tourcoo.carnet.core.frame.interfaces.ITitleView;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :设置有TitleBar的BaseFragment
 * @company :翼迈科技
 * @date 2019年 03月 05日 20时34分
 * @Email: 971613168@qq.com
 */
public abstract class BaseTitleFragment extends BaseFragment implements ITitleView {

    protected TitleBarDelegate mFastTitleDelegate;
    protected TitleBarView mTitleBar;
    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
        mFastTitleDelegate = new TitleBarDelegate(mContentView, this,this.getClass());
        mTitleBar = mFastTitleDelegate.mTitleBar;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        TextView textView = titleBar.getTextView(Gravity.CENTER | Gravity.TOP);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


}

package com.tourcoo.carnet.core.widget.core.view.alpha.delegate;

import android.view.View;

import com.tourcoo.carnet.core.widget.core.helper.AlphaViewHelper;

/**
 * @author :zhoujian
 * @description : 控制View的alpha透明度代理类
 * @company :翼迈科技
 * @date 2019年02月28日下午 02:50
 * @Email: 971613168@qq.com
 */

public class AlphaDelegate {
    private View mView;
    private AlphaViewHelper mAlphaViewHelper;

    public AlphaDelegate(View view) {
        this.mView = view;
    }

    public AlphaViewHelper getAlphaViewHelper() {
        if (mAlphaViewHelper == null) {
            mAlphaViewHelper = new AlphaViewHelper(mView);
        }
        return mAlphaViewHelper;
    }
}

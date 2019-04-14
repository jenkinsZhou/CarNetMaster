package com.tourcoo.carnet.core.widget.core.view.alpha;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tourcoo.carnet.core.widget.core.view.alpha.delegate.AlphaDelegate;

/**
 * @author :zhoujian
 * @description : 控制Alpha 按下效果
 * @company :翼迈科技
 * @date 2019年02月28日下午 03:19
 * @email: 971613168@qq.com
 */
public class AlphaFrameLayout extends FrameLayout {

    private AlphaDelegate delegate;

    public AlphaFrameLayout(Context context) {
        this(context, null);
    }

    public AlphaFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new AlphaDelegate(this);
    }

    public AlphaDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        delegate.getAlphaViewHelper().onPressedChanged(this, pressed);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        delegate.getAlphaViewHelper().onEnabledChanged(this, enabled);
    }
}

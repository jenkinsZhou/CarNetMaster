package com.tourcoo.carnet.core.widget.core.view.alpha;

import android.content.Context;
import android.util.AttributeSet;

import com.tourcoo.carnet.core.widget.core.view.alpha.delegate.AlphaDelegate;


/**
 * @author :zhoujian
 * @description : 控制Alpha 按下效果
 * @company :翼迈科技
 * @date 2019年02月28日下午 03:23
 * @email: 971613168@qq.com
 */
public class AlphaTextView extends androidx.appcompat.widget.AppCompatTextView {

    private AlphaDelegate delegate;

    public AlphaTextView(Context context) {
        this(context, null);
    }

    public AlphaTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

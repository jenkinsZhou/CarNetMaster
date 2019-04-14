package com.tourcoo.carnet.core.widget.core.view.radius;

import android.content.Context;
import android.util.AttributeSet;

import com.tourcoo.carnet.core.widget.core.view.radius.delegate.RadiusCompoundButtonDelegate;


/**
 * @author :zhoujian
 * @description : 用于需要圆角矩形框背景的CheckBox的情况,减少直接使用CheckBox时引入的shape资源文件
 * @company :翼迈科技
 * @date 2019年02月28日下午 03:05
 * @Email: 971613168@qq.com
 */
public class RadiusCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {
    private RadiusCompoundButtonDelegate delegate;

    public RadiusCheckBox(Context context) {
        this(context, null);
    }

    public RadiusCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RadiusCompoundButtonDelegate(this, context, attrs);
    }

    /**
     * 获取代理类用于Java代码控制shape属性
     *
     * @return
     */
    public RadiusCompoundButtonDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate != null && delegate.getWidthHeightEqualEnable() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate != null) {
            if (delegate.getRadiusHalfHeightEnable()) {
                delegate.setRadius(getHeight() / 2);
            }
            delegate.init();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (delegate != null) {
            delegate.setSelected(selected);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (delegate != null) {
            delegate.init();
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (delegate != null) {
            delegate.init();
        }
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (delegate != null) {
            delegate.init();
        }
    }
}

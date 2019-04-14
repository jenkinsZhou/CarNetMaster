package com.tourcoo.carnet.core.frame.widget.iosloading;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.widget.dialog.progress.Indeterminate;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author :zhoujian
 * @description : 仿菊花转动view
 * @company :翼迈科技
 * @date 2019年03月13日下午 02:09
 * @Email: 971613168@qq.com
 */
public class SpinView extends AppCompatImageView implements Indeterminate {
    private float mRotateDegrees;
    private int mFrameTime;
    private boolean mNeedToUpdateView;
    private Runnable mUpdateViewRunnable;

    public SpinView(Context context) {
        super(context);
        init();
    }

    public SpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(R.drawable.kprogresshud_spinner);
        mFrameTime = 1000 / 12;
        mUpdateViewRunnable = new Runnable() {
            @Override
            public void run() {
                mRotateDegrees += 30;
                mRotateDegrees = mRotateDegrees < 360 ? mRotateDegrees : mRotateDegrees - 360;
                invalidate();
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime);
                }
            }
        };
    }

    @Override
    public void setAnimationSpeed(float scale) {
        mFrameTime = (int) (1000 / 12 / scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mRotateDegrees, (float) getWidth() / 2, (float) getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedToUpdateView = true;
        post(mUpdateViewRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        mNeedToUpdateView = false;
        super.onDetachedFromWindow();
    }
}

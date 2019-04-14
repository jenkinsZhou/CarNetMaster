package com.tourcoo.carnet.core.widget.dialog.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tourcoo.carnet.core.widget.custom.IProgress;

/**
 * @author :zhoujian
 * @description :圆形进度控件
 * @company :翼迈科技
 * @date 2019年 03月 23日 21时56分
 * @Email: 971613168@qq.com
 */
public class CircleProgressView extends View implements IProgress {
    private Paint mWhitePaint;
    private Paint mGreyPaint;
    private RectF mBound;
    private int maxProgress = 100;
    private int mProgress = 0;
    private float scale;

    public int dpToPixel(float dp, Context context) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * scale);
    }

    public CircleProgressView(Context context) {
        super(context);
        init();
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhitePaint.setStrokeWidth(dpToPixel(0.1f, getContext()));
        mWhitePaint.setColor(Color.WHITE);

        mGreyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGreyPaint.setStyle(Paint.Style.STROKE);
        mGreyPaint.setStrokeWidth(dpToPixel(2, getContext()));
        mGreyPaint.setColor(Color.WHITE);

        mBound = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = dpToPixel(4, getContext());
        mBound.set(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mAngle = mProgress * 360f / maxProgress;
        //画圆弧
        canvas.drawArc(mBound, 270, mAngle, true, mWhitePaint);
        int padding = dpToPixel(4, getContext());
        //画圆
        canvas.drawCircle((float) getWidth() / 2, (float) getHeight() / 2, (float) getWidth() / 2 - padding, mGreyPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dimension = dpToPixel(40, getContext());
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    public void setMax(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }
}

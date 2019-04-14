package com.tourcoo.carnet.core.frame.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 06日 22时42分
 * @Email: 971613168@qq.com
 */
public class IosLoadingView extends View {

    private static final String TAG = IosLoadingView.class.getSimpleName();
    /**
     * view宽度
     */
    private int width;
    /**
     * view高度
     */
    private int height;
    /**
     * 菊花的矩形的宽
     */
    private int rectWidth;
    /**
     * 菊花的矩形的宽
     */
    private int rectHeight;
    /**
     * 菊花绘制画笔
     */
    private Paint rectPaint;
    /**
     * 循环绘制位置
     */
    private int pos = 0;
    /**
     * 菊花矩形
     */
    private RectF mRectF;
    /**
     * 循环颜色
     *  private String[] color = {"#bbbbbb", "#aaaaaa", "#999999", "#888888", "#777777", "#666666",};
     */
    private String[] color = {"#bbbbbb", "#aaaaaa", "#999999", "#888888", "#777777", "#666666",};
//    private String[] color = {"#bbbbbb", "#666666", "#777777", "#bbbbbb", "#bbbbbb", "#bbbbbb",};

    public IosLoadingView(Context context) {
        this(context, null);
    }

    public IosLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IosLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //根据个人习惯设置  这里设置  如果是wrap_content  则设置为宽高200
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            width = 200;
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = Math.min(width, height);
        }
        //菊花矩形的宽
        rectWidth = width / 24;
        //菊花矩形的高
        rectHeight = 3 * rectWidth;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制部分是关键了，菊花花瓣矩形有12个，我们不可能去一个一个的算出所有的矩形坐标，我们可以考虑
        //旋转下面的画布canvas来实现绘制，每次旋转30度
        //首先定义一个矩形
        if (mRectF == null) {
            mRectF = new RectF((float) (width - rectWidth) / 2, 55, (float) (width + rectWidth) / 2, rectHeight + 55);
        }
        //  pos的值
        for (int i = 0; i < 12; i++) {
            if (i - pos >= 5) {
                rectPaint.setColor(Color.parseColor(color[5]));
            } else if (i - pos >= 0 && i - pos < 5) {
                rectPaint.setColor(Color.parseColor(color[i - pos]));
            } else if (i - pos >= -7 && i - pos < 0) {
                rectPaint.setColor(Color.parseColor(color[5]));
            } else if (i - pos >= -11 && i - pos < -7) {
                rectPaint.setColor(Color.parseColor(color[12 + i - pos]));
            }
            //绘制
//            canvas.drawRect(rect, rectPaint);
            canvas.drawRoundRect(mRectF, 10.5f, 10.5f, rectPaint);
            //旋转
            canvas.rotate(30, (float) width / 2, (float) width / 2);
        }

        pos++;
        if (pos > 11) {
            pos = 0;
        }
//一个周期用时1200
        postInvalidateDelayed(60);

    }

//    public void setStartAnimal() {
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 12);
//        valueAnimator.setDuration(1500);
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                pos = (int) animation.getAnimatedValue();
//                Log.d(TAG, "pos:" + pos);
//                invalidate();
//            }
//        });
//        valueAnimator.start();
//    }


}

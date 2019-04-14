package com.tourcoo.carnet.core.widget.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :带有滚动监听的ScrollView
 * @company :翼迈科技
 * @date 2019年03月15日14:26
 * @Email: 971613168@qq.com
 */
public class TourCooScrollView extends ScrollView {

    private List<OnScrollChangeListener> mListListener = new ArrayList<>();

    public interface OnScrollChangeListener {
        /**
         * 回调滚动事件
         *
         * @param v          当前滚动视图
         * @param scrollX    滚动 x 距离 -px
         * @param scrollY    滚动 y 距离 -px
         * @param oldScrollX 上次滚动 x 距离 -px
         * @param oldScrollY 上次滚动 y 距离 -px
         */
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    /**
     * 滚动监听
     *
     * @param onScrollChangeListener
     * @return
     */
    public TourCooScrollView addOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        synchronized (mListListener) {
            if (!mListListener.contains(onScrollChangeListener)) {
                mListListener.add(onScrollChangeListener);
            }
        }
        return this;
    }

    /**
     * 移除滚动监听
     *
     * @param onScrollChangeListener
     * @return
     */
    public TourCooScrollView removeOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        synchronized (mListListener) {
            if (mListListener.contains(onScrollChangeListener)) {
                mListListener.remove(onScrollChangeListener);
            }
        }
        return this;
    }

    public TourCooScrollView(Context context) {
        super(context);
    }

    public TourCooScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        if (mListListener != null && mListListener.size() > 0) {
            for (OnScrollChangeListener mOnScrollChangeListener : mListListener) {
                if (mOnScrollChangeListener != null) {
                    mOnScrollChangeListener.onScrollChange(this, scrollX, scrollY, oldScrollX, oldScrollY);
                }
            }
        }
    }
}

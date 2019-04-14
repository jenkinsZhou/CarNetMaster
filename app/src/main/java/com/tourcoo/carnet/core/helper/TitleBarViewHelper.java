package com.tourcoo.carnet.core.helper;

import android.app.Activity;
import android.view.View;

import com.tourcoo.carnet.core.frame.base.BaseHelper;
import com.tourcoo.carnet.core.frame.util.SizeUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.util.ViewColorUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.custom.TourCooScrollView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 17日 13时16分
 * @Email: 971613168@qq.com
 */
public class TitleBarViewHelper extends BaseHelper {


    private RecyclerView mRecyclerView;
    private TourCooScrollView mOverScrollView;
    private TitleBarView mTitleBarView;
    private boolean mShowTextEnable;
    /**
     * 滑动的最小距离
     */
    private int mMinHeight = SizeUtil.dp2px(24);
    /**
     * 滑动的最大距离
     */
    private int mMaxHeight = 20;
    /**
     * 转换透明度
     */
    private int mTransAlpha = 112;

    private OnScrollListener mOnScrollListener;
    private boolean mIsLightMode;

    public interface OnScrollListener {
        /**
         * 滚动回调
         *
         * @param alpha
         * @param isLightMode
         */
        void onScrollChange(int alpha, boolean isLightMode);
    }

    public TitleBarViewHelper(Activity context) {
        super(context);
    }

    public TitleBarViewHelper setTitleBarView(TitleBarView titleBarView) {
        this.mTitleBarView = titleBarView;
        return this;
    }

    public TitleBarViewHelper setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView == null) {
            return this;
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mAlpha;
            int mScrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY = recyclerView.computeVerticalScrollOffset();
                mAlpha = setChange(mScrollY);
            }
        });
        return this;
    }

    public TitleBarViewHelper setOverScrollView(TourCooScrollView overScrollView) {
        mOverScrollView = overScrollView;
        if (mOverScrollView == null) {
            return this;
        }
        overScrollView.addOnScrollChangeListener(new TourCooScrollView.OnScrollChangeListener() {
            int mAlpha;

            @Override
            public void onScrollChange(View v, int scrollX, int mScrollY, int oldScrollX, int oldScrollY) {
                mAlpha = setChange(mScrollY);
            }
        });
        return this;
    }

    public TitleBarViewHelper setShowTextEnable(boolean enable) {
        this.mShowTextEnable = enable;
        return this;
    }

    public TitleBarViewHelper setMinHeight(int minHeight) {
        mMinHeight = minHeight;
        return this;
    }

    public TitleBarViewHelper setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        return this;
    }

    public TitleBarViewHelper setTransAlpha(int transAlpha) {
        mTransAlpha = transAlpha;
        return this;
    }

    public TitleBarViewHelper setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
        return this;
    }

    private int setChange(int mScrollY) {
        int mAlpha;
        // 滑动距离小于定义得最小距离
        if (mScrollY <= mMinHeight) {
            mAlpha = 0;
        }
        // 滑动距离大于定义得最大距离
        else if (mScrollY > mMaxHeight) {
            mAlpha = 255;
        }
        // 滑动距离处于最小和最大距离之间
        else {
            // （滑动距离 - 开始变化距离）/ 最大限制距离 = mAlpha/255
            mAlpha = (mScrollY - mMinHeight) * 255 / (mMaxHeight - mMinHeight);
        }
        //白色背景
        if (mAlpha >= mTransAlpha) {
            if (!mIsLightMode) {
                StatusBarUtil.setStatusBarLightMode(mContext);
                mIsLightMode = true;
                if (mTitleBarView != null) {
                    mTitleBarView.setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 112);
                }
            }
        } else {
            if (mIsLightMode) {
                StatusBarUtil.setStatusBarDarkMode(mContext);
                mIsLightMode = false;
                if (mTitleBarView != null) {
                    mTitleBarView.setStatusAlpha(0);
                }
            }
        }
        if (mTitleBarView != null) {
            ViewColorUtil.getInstance().changeColor(mTitleBarView, mAlpha, mIsLightMode, mShowTextEnable);
            mTitleBarView.setDividerVisible(mAlpha >= 250).getBackground().setAlpha(mAlpha);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChange(mAlpha, mIsLightMode);
        }
        return mAlpha;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTitleBarView = null;
        mRecyclerView = null;
    }
}

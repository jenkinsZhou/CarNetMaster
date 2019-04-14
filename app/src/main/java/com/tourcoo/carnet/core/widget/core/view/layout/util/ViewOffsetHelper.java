package com.tourcoo.carnet.core.widget.core.view.layout.util;

import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 02月 28日 21时41分
 * @Email: 971613168@qq.com
 */
public class ViewOffsetHelper {

    private final View mView;

    private int mLayoutTop;
    private int mLayoutLeft;
    private int mOffsetTop;
    private int mOffsetLeft;

    public ViewOffsetHelper(View view) {
        mView = view;
    }

    public void onViewLayout() {
        mLayoutTop = mView.getTop();
        mLayoutLeft = mView.getLeft();
        updateOffsets();
    }

    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.getTop() - mLayoutTop));
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.getLeft() - mLayoutLeft));
    }

    /**
     * Set the top and bottom offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    public boolean setTopAndBottomOffset(int offset) {
        if (mOffsetTop != offset) {
            mOffsetTop = offset;
            updateOffsets();
            return true;
        }
        return false;
    }

    public int getLayoutTop() {
        return mLayoutTop;
    }
}

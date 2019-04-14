package com.tourcoo.carnet.core.widget.core.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourcoo.carnet.core.util.TourcooUtil;

/**
 * @author :zhoujian
 * @description :view颜色工具类
 * @company :翼迈科技
 * @date 2019年 03月 17日 13时19分
 * @Email: 971613168@qq.com
 */
public class ViewColorUtil {

    private static volatile ViewColorUtil sInstance;

    private ViewColorUtil() {
    }

    public static ViewColorUtil getInstance() {
        if (sInstance == null) {
            synchronized (ViewColorUtil.class) {
                if (sInstance == null) {
                    sInstance = new ViewColorUtil();
                }
            }
        }
        return sInstance;
    }

    public void changeColor(View rootView, int alpha, boolean mIsLight, boolean showText) {
        if (rootView != null) {
            //滚动视图
            if (rootView instanceof TextView) {
                TextView textView = (TextView) rootView;
                Drawable[] drawables = textView.getCompoundDrawables();
                for (Drawable item : drawables) {
                    if (item != null) {
                        //使用该方法避免同一Drawable被全局修改
                        item = item.mutate();
                        TourcooUtil.getTintDrawable(item, Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                    }
                }
                if (!showText) {
                    textView.setTextColor(Color.argb(alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                } else {
                    textView.setTextColor(Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
                }
            } else if (rootView instanceof ImageView) {
                //使用该方法避免同一Drawable被全局修改
                Drawable drawable = ((ImageView) rootView).getDrawable().mutate();
                TourcooUtil.getTintDrawable(drawable, Color.argb(mIsLight ? alpha : 255 - alpha, mIsLight ? 0 : 255, mIsLight ? 0 : 255, mIsLight ? 0 : 255));
            } else if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int size = contentView.getChildCount();
                //循环遍历所有子View
                for (int i = 0; i < size; i++) {
                    View childView = contentView.getChildAt(i);
                    //递归查找
                    changeColor(childView, alpha, mIsLight, showText);
                }
            }
        }
    }
}

package com.tourcoo.carnet.core.widget.core.view.layout.util;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * @author :zhoujian
 * @description :主题工具类
 * @company :翼迈科技
 * @date 2019年 02月 28日 21时30分
 * @Email: 971613168@qq.com
 */
public class ThemeUtils {
    private static final int[] APPCOMPAT_CHECK_ATTRS = {
            androidx.appcompat.R.attr.colorPrimary
    };

    public static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }
}

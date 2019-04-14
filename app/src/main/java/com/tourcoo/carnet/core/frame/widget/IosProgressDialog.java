package com.tourcoo.carnet.core.frame.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.tourcoo.carnet.R;

/**
 * @author :zhoujian
 * @description : 仿IOS等待对话框
 * @company :翼迈科技
 * @date 2019年03月13日下午 02:05
 * @Email: 971613168@qq.com
 */
public class IosProgressDialog extends Dialog {
    public Context context;

    public IosProgressDialog(Context context) {
        super(context, R.style.picture_alert_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.DialogWindowStyle);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ios_progress_dialog);
    }
}

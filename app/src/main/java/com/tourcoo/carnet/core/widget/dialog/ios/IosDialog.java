package com.tourcoo.carnet.core.widget.dialog.ios;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.tourcoo.carnet.R;

/**
 * @author :JenkinsZhou
 * @description :仿ios菊花对话框
 * @company :途酷科技
 * @date 2019年03月25日15:31
 * @Email: 971613168@qq.com
 */
public class IosDialog extends Dialog {

    public Context context;

    public IosDialog(Context context) {
        super(context, R.style.IosDialogStyle);
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
        setContentView(R.layout.dialog_ios_layout);
    }
}

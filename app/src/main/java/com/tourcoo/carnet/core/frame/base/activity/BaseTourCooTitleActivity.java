package com.tourcoo.carnet.core.frame.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.widget.TextView;

import com.tourcoo.carnet.core.frame.interfaces.IMultiStatusView;
import com.tourcoo.carnet.core.widget.confirm.AlertDialog;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.core.progress.EmiProgressDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.dialog.ios.IosDialog;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :带标题栏的基类activity
 * @company :翼迈科技
 * @date 2019年 03月 16日 12时04分
 * @Email: 971613168@qq.com
 */
public abstract class BaseTourCooTitleActivity extends BaseTitleActivity {
    protected StatusLayoutManager mStatusLayoutManager;
    protected IMultiStatusView multiStatusView;


    /**
     * 弹窗
     *
     * @param title
     * @param message
     * @param buttonText
     */
    protected void showAlertDialog(String title, String message, String buttonText) {
        if (!BaseTourCooTitleActivity.this.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title).setMessage(message);
            builder.setPositiveButton(buttonText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        TextView textView = titleBar.getTextView(Gravity.CENTER | Gravity.TOP);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    /**
     * 确认弹窗
     *
     * @param builder
     */
    protected void showConfirmDialog(ConfirmDialog.Builder builder) {
        if (!BaseTourCooTitleActivity.this.isFinishing() && builder != null) {
            builder.create().show();
        }
    }



    /**
     * 显示权限弹窗
     *
     * @param msg
     */
    protected void showLocatePermissionDialog(String msg) {
        showAlertDialog("需要定位权限", msg, "我知道了");
    }


    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    protected void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

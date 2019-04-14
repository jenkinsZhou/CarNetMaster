package com.tourcoo.carnet.core.frame.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.WindowManager;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.widget.core.util.FindViewUtil;

import java.lang.ref.WeakReference;


/**
 * @author :zhoujian
 * @description :创建loading对话框
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时53分
 * @Email: 971613168@qq.com
 */
public class LoadingDialog {

    private Dialog mDialog = null;

    private Activity mActivity;
    private final WeakReference<Activity> mReference;

    public LoadingDialog() {
        this(StackUtil.getInstance().getCurrent());
    }

    public LoadingDialog(Activity activity) {
        this(activity, new ProgressDialog.Builder(activity)
                .setMessage(R.string.load_more_loading)
                .create());
    }

    public LoadingDialog(Activity activity, Dialog dialog) {
        this.mReference = new WeakReference<>(activity);
        this.mDialog = dialog;
    }

    /**
     * 设置是否可点击返回键关闭dialog
     *
     * @param enable
     * @return
     */
    public LoadingDialog setCancelable(boolean enable) {
        if (mDialog != null) {
            mDialog.setCancelable(enable);
        }
        return this;
    }

    /**
     * 设置是否可点击dialog以外关闭
     *
     * @param enable
     * @return
     */
    public LoadingDialog setCanceledOnTouchOutside(boolean enable) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(enable);
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public LoadingDialog setMessage(CharSequence msg) {
        if (mDialog == null) {
            return this;
        }
        if (mDialog instanceof ProgressDialog) {
            ((ProgressDialog) mDialog).setMessage(msg);
        } else {
            TextView textView = FindViewUtil.getTargetView(mDialog.getWindow().getDecorView(), TextView.class);
            if (textView != null) {
                textView.setText(msg);
            }
        }
        return this;
    }

    /**
     * @param msg
     * @return
     */
    public LoadingDialog setMessage(int msg) {
        mActivity = mReference.get();
        if (mActivity != null) {
            return setMessage(mActivity.getText(msg));
        }
        return this;
    }

    /**
     * @param enable 设置全透明
     * @return
     */
    public LoadingDialog setFullTrans(boolean enable) {
        if (mDialog != null) {
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            // 黑暗度
            lp.dimAmount = enable ? 0f : 0.5f;
            mDialog.getWindow().setAttributes(lp);
        }
        return this;
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void show() {
        mActivity = mReference.get();
        if (mActivity != null && mDialog != null && !mActivity.isFinishing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        mActivity = mReference.get();
        if (mDialog != null && !mActivity.isFinishing()) {
            mDialog.dismiss();
        }
    }
}

package com.tourcoo.carnet.core.frame.retrofit;

import android.app.Activity;

import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.interfaces.IRequestControl;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.frame.widget.LoadingDialog;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * @author :zhoujian
 * @description : 快速创建支持Loading的Retrofit观察者
 * @company :翼迈科技
 * @date 2019年03月04日下午 03:31
 * @Email: 971613168@qq.com
 */
public abstract class BaseLoadingObserver<T> extends BaseObserver<T> {

    /**
     * Dialog
     */
    private LoadingDialog mDialog;

    /**
     * 用于全局配置
     *
     * @param activity
     */
    public BaseLoadingObserver(@Nullable Activity activity, IRequestControl httpRequestControl, @StringRes int resId) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId), httpRequestControl);
    }

    public BaseLoadingObserver(IRequestControl httpRequestControl, @StringRes int resId) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl, resId);
    }

    public BaseLoadingObserver(@Nullable Activity activity, IRequestControl httpRequestControl, CharSequence msg) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg), httpRequestControl);
    }

    public BaseLoadingObserver(IRequestControl httpRequestControl, CharSequence msg) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl, msg);
    }

    public BaseLoadingObserver(@Nullable Activity activity, @StringRes int resId) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(resId));
    }

    public BaseLoadingObserver(@StringRes int resId) {
        this(StackUtil.getInstance().getCurrent(), resId);
    }

    public BaseLoadingObserver(@Nullable Activity activity, CharSequence msg) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity).setMessage(msg));
    }

    public BaseLoadingObserver(CharSequence msg) {
        this(StackUtil.getInstance().getCurrent(), msg);
    }

    public BaseLoadingObserver(@Nullable Activity activity, IRequestControl httpRequestControl) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity), httpRequestControl);
    }

    public BaseLoadingObserver(IRequestControl httpRequestControl) {
        this(StackUtil.getInstance().getCurrent(), httpRequestControl);
    }

    public BaseLoadingObserver(@Nullable Activity activity) {
        this(UiConfigManager.getInstance().getLoadingDialog().createLoadingDialog(activity));
    }

    public BaseLoadingObserver() {
        this(StackUtil.getInstance().getCurrent());
    }

    public BaseLoadingObserver(boolean showDialog) {
        this(StackUtil.getInstance().getCurrent());

    }

    public BaseLoadingObserver(LoadingDialog dialog) {
        this(dialog, null);
    }

    public BaseLoadingObserver(LoadingDialog dialog, IRequestControl requestControl) {
        super(requestControl);
        this.mDialog = dialog;
    }


    @Override
    public void onNext(T entity) {
        dismissProgressDialog();
        super.onNext(entity);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        super.onError(e);
    }

    protected void showProgressDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }
}

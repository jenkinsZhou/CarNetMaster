package com.tourcoo.carnet.core.frame.retrofit;

import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.interfaces.IRequestControl;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * @author :zhoujian
 * @description : 基类观察者
 * @company :翼迈科技
 * @date 2019年03月04日下午 01:35
 * @Email: 971613168@qq.com
 */
public abstract class BaseObserver<T> extends DefaultObserver<T> {
    private IRequestControl mIRequestControl;


    public BaseObserver() {
        this(null);
    }

    public BaseObserver(IRequestControl requestControl) {
        this.mIRequestControl = requestControl;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (UiConfigManager.getInstance().getHttpRequestControl() != null) {
            UiConfigManager.getInstance().getHttpRequestControl().httpRequestError(mIRequestControl, e);
        }
        onRequestError(e);
    }

    @Override
    public void onNext(T entity) {
        onRequestNext(entity);
    }

    /**
     * 获取成功后数据展示
     *
     * @param entity
     */
    public abstract void onRequestNext(@NonNull T entity);

    public void onRequestError(@NonNull Throwable e) {
    }
}

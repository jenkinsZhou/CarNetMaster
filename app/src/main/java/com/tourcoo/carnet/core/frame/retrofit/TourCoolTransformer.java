package com.tourcoo.carnet.core.frame.retrofit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author :zhoujian
 * @description : 控制操作线程的辅助类
 * @company :翼迈科技
 * @date 2019年03月04日上午 11:38
 * @Email: 971613168@qq.com
 */
public class TourCoolTransformer {
    /**
     * 线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> switchSchedulersIo() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return switchSchedulersIo(upstream);
            }
        };
    }

    /**
     * 线程切换IO
     *
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> switchSchedulersIo(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}

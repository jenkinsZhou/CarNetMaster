package com.tourcoo.carnet.retrofit;

import android.accounts.NetworkErrorException;

import com.tourcoo.carnet.core.frame.retrofit.RetryWhen;
import com.tourcoo.carnet.core.frame.retrofit.TourCoolTransformer;
import com.tourcoo.carnet.entity.BaseEntity;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月02日18:52
 * @Email: 971613168@qq.com
 */
public class BaseRepository {



    /**
     * @param observable 用于解析 统一返回实体统一做相应的错误码--如登录失效
     * @param <T>
     * @return
     */
    protected <T> Observable<T> transform(Observable<BaseEntity<T>> observable) {
        return TourCoolTransformer.switchSchedulersIo(
                observable.retryWhen(new RetryWhen())
                        .flatMap((Function<BaseEntity<T>, ObservableSource<T>>) result -> {
                            if (result == null) {
                                return Observable.error(new NetworkErrorException());
                            } else {
                                return Observable.just(result.data);
                            }
                        }));
    }
}

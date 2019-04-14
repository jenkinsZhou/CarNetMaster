package com.tourcoo.carnet.core.frame.interfaces;

import java.util.List;

import io.reactivex.annotations.NonNull;


/**
 * @author :zhoujian
 * @description :网络请求相关(可全局控制网络成功及失败展示逻辑-需继承TourCooObserver或自己实现类似逻辑)
 * @company :翼迈科技
 * @date 2019年 03月 02日 22时27分
 * @Email: 971613168@qq.com
 */
public interface HttpRequestControl {

    /**
     * 网络成功回调
     *
     * @param requestControl 调用页面相关参数
     * @param list           数据列表
     * @param listener       设置完成回调--用于特殊需求页面做后续操作
     */
    void httpRequestSuccess(IRequestControl requestControl, List<?> list, HttpRequestListener listener);

    /**
     * 网络请求失败后执行
     *
     * @param requestControl 调用页面相关参数
     * @param e              错误
     */
    void httpRequestError(IRequestControl requestControl, @NonNull Throwable e);
}

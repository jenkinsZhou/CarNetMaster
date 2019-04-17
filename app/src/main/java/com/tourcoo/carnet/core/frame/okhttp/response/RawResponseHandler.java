package com.tourcoo.carnet.core.frame.okhttp.response;


/**
 * @author :JenkinsZhou
 * @description :raw 字符串结果回调
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public abstract class RawResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, String response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}

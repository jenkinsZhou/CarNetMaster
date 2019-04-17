package com.tourcoo.carnet.core.frame.okhttp.response;

import org.json.JSONObject;

/**
 * @author :JenkinsZhou
 * @description :json类型的回调接口
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public abstract class JsonResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, JSONObject response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}

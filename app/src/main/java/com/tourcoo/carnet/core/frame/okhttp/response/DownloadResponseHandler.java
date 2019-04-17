package com.tourcoo.carnet.core.frame.okhttp.response;

import java.io.File;


/**
 * @author :JenkinsZhou
 * @description :下载回调
 * @company :途酷科技
 * @date 2019年04月16日12:51
 * @Email: 971613168@qq.com
 */
public abstract class DownloadResponseHandler {

    public abstract void onFinish(File download_file);
    public abstract void onProgress(long currentBytes, long totalBytes);
    public abstract void onFailure(String error_msg);
}

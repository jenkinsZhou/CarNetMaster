package com.tourcoo.carnet.core.frame.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author :zhoujian
 * @description : 实现文件下载/上传
 * @company :翼迈科技
 * @date 2019年03月04日上午 11:44
 * @Email: 971613168@qq.com
 */
public interface NaViRetrofitService {

    /**
     * 大文件官方建议用 @Streaming 来进行注解,不然会出现IO异常,小文件可以忽略
     *
     * @param fileUrl 地址
     * @param header  可增加header信息
     * @return ResponseBody
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl, @HeaderMap Map<String, Object> header);

    /**
     * 文件及其它参数
     *
     * @param uploadUrl 接口全路径
     * @param body
     * @param header
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String uploadUrl, @Body RequestBody body, @HeaderMap Map<String, Object> header);
}

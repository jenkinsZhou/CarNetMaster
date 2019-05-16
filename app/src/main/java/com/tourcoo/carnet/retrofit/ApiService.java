package com.tourcoo.carnet.retrofit;

import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.InsuranceCompany;
import com.tourcoo.carnet.entity.garage.CommentDetail;
import com.tourcoo.carnet.entity.garage.ServiceInfo;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.garage.CommentEntity;
import com.tourcoo.carnet.entity.garage.GarageEntity;
import com.tourcoo.carnet.entity.ImgeEntity;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.car.CarFaultRemindType;
import com.tourcoo.carnet.entity.car.PayInfo;
import com.tourcoo.carnet.entity.order.OrderDetail;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月02日18:42
 * @Email: 971613168@qq.com
 */
public interface ApiService {


    @POST(ApiConstant.API_LOGIN_BY_PASSWORD)
    Observable<BaseEntity> loginByPassword(@QueryMap Map<String, Object> map);

    @POST("signLogin/loginByVCode")
    Observable<BaseEntity> loginByVCode(@QueryMap Map<String, Object> map);

    /**
     * 注册
     *
     * @param map
     * @return
     */
    @POST("signLogin/mobileRegister")
    Observable<BaseEntity> mobileRegister(@QueryMap Map<String, Object> map);


    @POST("meituApi")
    Observable<String> testApi(@QueryMap Map<String, Object> map);

    /**
     * 获取验证码
     *
     * @param map
     * @return
     */
    @POST("signLogin/getVCode")
    Observable<BaseEntity> getVCode(@QueryMap Map<String, Object> map);

    /**
     * 获取附近修理厂列表
     *
     * @param map
     * @return
     */
    @POST("garage/findNearbyGarages")
    Observable<BaseEntity> findNearbyGarages(@QueryMap Map<String, Object> map);


    /**
     * 根据条件查询修理厂列表
     *
     * @param map
     * @return
     */
    @POST("garage/searchGarages")
    Observable<BaseEntity<GarageEntity>> searchGarages(@QueryMap Map<String, Object> map);

    /**
     * 获取我的车辆列表
     *
     * @param
     * @return
     */
    @POST("car/findMyCars")
    Observable<BaseEntity> findMyCars();

    /**
     * 故障报修
     *
     * @param map
     * @return
     */
    @POST("order/reportFault")
    Observable<BaseEntity> reportFault(@Body Map<String, Object> map);

    /**
     * 获取我的订单列表
     *
     * @param map
     * @return
     */
    @POST("order/findMyList")
    Observable<FaultRepairEntity> findMyList(@QueryMap Map<String, Object> map);


    /**
     * 提交评价
     *
     * @param map
     * @return
     */
    @POST("order/commentOrder")
    Observable<BaseEntity> commentOrder(@QueryMap Map<String, Object> map);

    /**
     * 获取车辆品类
     *
     * @return
     */
    @POST("car/findCategoryList")
    Observable<BaseEntity> findCategoryList();

    /**
     * 根据车辆品类获取车辆型号
     *
     * @param map
     * @return
     */
    @POST("car/findCarModelList")
    Observable<BaseEntity> findCarModelList(@QueryMap Map<String, Object> map);

    /**
     * 添加车辆
     *
     * @param map
     * @return
     */
    @POST("car/addCar")
    Observable<BaseEntity> addCar(@QueryMap Map<String, Object> map);

    /**
     * 修改车辆
     *
     * @param map
     * @return
     */
    @POST("car/editCar")
    Observable<BaseEntity> editCar(@QueryMap Map<String, Object> map);

    /**
     * 删除车辆
     *
     * @param map
     * @return
     */
    @POST("car/deleteCar")
    Observable<BaseEntity> deleteCar(@Body Map<String, Object> map);


    /**
     * 获取保养规则列表
     *
     * @return
     */
    @POST("car/findMaintainRuleList")
    Observable<BaseEntity> findMaintainRuleList();

    /**
     * 故障提醒模式列表
     *
     * @return
     */
    @POST("car/findFaultRemindList")
    Observable<BaseEntity<List<CarFaultRemindType>>> findFaultRemindList();

    /**
     * 获取接收模式
     *
     * @return
     */
    @POST("car/findObdReceiveList")
    Observable<BaseEntity> findObdReceiveList();


    /**
     * 获取指定订单应付金额
     *
     * @param map
     * @return
     */
    @POST("order/findAmount")
    Observable<BaseEntity<Double>> findAmount(@QueryMap Map<String, Object> map);

    /**
     * 支付接口
     *
     * @param map
     * @return
     */
    @POST("pay/createPay")
    Observable<BaseEntity<PayInfo>> createPay(@Body Map<String, Object> map);


    /**
     * 单个文件上传
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("file/uploadFile")
    Call<BaseEntity> uploadFile(@Part MultipartBody.Part file);

    /**
     * 多个文件上传
     *
     * @param files
     * @return
     */
    @POST("file/uploadFiles")
    Call<BaseEntity<List<ImgeEntity>>> uploadFiles(@Body RequestBody files);


    /**
     * 取消服务
     *
     * @param map
     * @return
     */
    @POST("order/cancleOrder")
    Observable<BaseEntity> cancelOrder(@QueryMap Map<String, Object> map);


    /**
     * 系统消息列表
     *
     * @param map
     * @return
     */
    @POST("message/owner/msg")
    Observable<BaseEntity<MessageInfo>> getMsgList(@Body Map<String, Object> map);

    /**
     * 获取未读消息数量
     *
     * @param map
     * @return
     */
    @GET("message/owner/unreadMsg")
    Observable<BaseEntity<MessageInfo.MessageBean>> getNoReadCount(@QueryMap Map<String, Object> map);

    /**
     * 设置默认车辆
     *
     * @param map
     * @return
     */
    @POST("car/setDefaultCar")
    Observable<BaseEntity> setDefaultCar(@QueryMap Map<String, Object> map);

    /**
     * 上传设备id（用于极光推送）
     *
     * @param map
     * @return
     */
    @POST("device/clientId/upload")
    Observable<BaseEntity> uploadClientId(@Body Map<String, Object> map);


    /**
     * 获取修理厂评论
     *
     * @param map
     * @return
     */
    @POST("comment/findGarageCommentList")
    Observable<BaseEntity<CommentEntity>> findGarageCommentList(@QueryMap Map<String, Object> map);

    /**
     * 获取服务器中app当前版本
     *
     * @param map
     * @return
     */
    @POST("app/version/last/appVersionInfo")
    Observable<BaseEntity> appVersionInfo(@QueryMap Map<String, Object> map);

    /**
     * 修改个人资料
     *
     * @param map
     * @return
     */
    @POST("signLogin/editOwnerInfo")
    Observable<BaseEntity> editOwnerInfo(@QueryMap Map<String, Object> map);

    /**
     * 查看个人资料
     *
     * @param map
     * @return
     */
    @POST("signLogin/getUserInfo")
    Observable<BaseEntity<UserInfoEntity>> getUserInfo(@QueryMap Map<String, Object> map);

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @POST("signLogin/restPassword")
    Observable<BaseEntity> restPassword(@QueryMap Map<String, Object> map);


    /**
     * 更换手机号
     *
     * @param map
     * @return
     */
    @POST("signLogin/changeMobile")
    Observable<BaseEntity> changeMobile(@QueryMap Map<String, Object> map);

    /**
     * 反馈
     *
     * @param map
     * @return
     */
    @POST("owner/feedback/add")
    Observable<BaseEntity> feedBack(@Body Map<String, Object> map);


    /**
     * 获取指定订单详情
     *
     * @param map
     * @return
     */
    @POST("order/findDetail")
    Observable<BaseEntity<OrderDetail>> findDetail(@QueryMap Map<String, Object> map);


    /**
     * 获取修理厂详情
     *
     * @param map
     * @return
     */
    @POST("garage/findDetail")
    Observable<BaseEntity> findGarageDetail(@QueryMap Map<String, Object> map);


    /**
     * 获取评论详情
     *
     * @param map
     * @return
     */
    @POST("order/findComment")
    Observable<BaseEntity<List<CommentDetail>>> findComment(@QueryMap Map<String, Object> map);

    /**
     * 微信登录
     *
     * @param map
     * @return
     */
    @POST("signLogin/loginByWechat")
    Observable<BaseEntity> loginByWechat(@QueryMap Map<String, Object> map);

    /**
     * 绑定手机号
     *
     * @param map
     * @return
     */
    @POST("signLogin/bindMobile")
    Observable<BaseEntity> bindMobile(@QueryMap Map<String, Object> map);


    /**
     * 保险公司列表
     *
     * @param map
     * @return
     */
    @POST("insurance/queryByName")
    Observable<BaseEntity<InsuranceCompany>> queryAllInsurance(@Body Map<String, Object> map);

    /**
     * 保险公司详情
     *
     * @param map
     * @return
     */
    @GET("insurance/findOne")
    Observable<BaseEntity<InsuranceCompany.CompanyInfo>> queryInsuranceDetailById(@QueryMap Map<String, Object> map);

    /**
     * 查看修理厂服务内容
     *
     * @param map
     * @return
     */
    @GET("order/check-order-service")
    Observable<BaseEntity<ServiceInfo>> findMyService(@QueryMap Map<String, Object> map);

    /**
     * 客服热线
     * @return
     */
    @GET("custom-service/phone")
    Observable<String> getServicePhone();


    /**
     * 注册条例
     * @return
     */
    @POST("signLogin/findOrdinance")
    Observable<BaseEntity<String>> findOrdinance();
}

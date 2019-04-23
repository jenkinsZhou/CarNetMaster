package com.tourcoo.carnet.retrofit;

import android.text.TextUtils;

import com.tourcoo.carnet.core.frame.retrofit.RetryWhen;
import com.tourcoo.carnet.core.frame.retrofit.TourCoolRetrofit;
import com.tourcoo.carnet.core.frame.retrofit.TourCoolTransformer;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.log.widget.utils.DateUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.InsuranceCompany;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.garage.CommentDetail;
import com.tourcoo.carnet.entity.garage.CommentEntity;
import com.tourcoo.carnet.entity.garage.GarageEntity;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.car.CarFaultRemindType;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.garage.ServiceInfo;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;
import com.tourcoo.carnet.entity.car.PayInfo;
import com.tourcoo.carnet.entity.order.OrderDetail;
import com.tourcoo.carnet.utils.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.tourcoo.carnet.core.common.CommonConstant.TYPE_USER_CAR_OWER;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月02日18:54
 * @Email: 971613168@qq.com
 */
public class ApiRepository extends BaseRepository {
    private static final String TAG = "ApiRepository";

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    public ApiService getApiService() {
        mApiService = TourCoolRetrofit.getInstance().createService(ApiService.class);
        return mApiService;
    }

    public Observable<BaseEntity> loginByPassword(String mobile, String password) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("userType", TYPE_USER_CAR_OWER);
        return TourCoolTransformer.switchSchedulersIo(getApiService().loginByPassword(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseEntity> loginByVCode(String mobile, String vCode) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("mobile", mobile);
        params.put("vCode", vCode);
        params.put("userType", TYPE_USER_CAR_OWER);
        return TourCoolTransformer.switchSchedulersIo(getApiService().loginByVCode(params).retryWhen(new RetryWhen()));
    }


    public Observable<String> testApi(String page) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("page", page);
        return TourCoolTransformer.switchSchedulersIo(getApiService().testApi(params).retryWhen(new RetryWhen()));
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    public Observable<BaseEntity> getVcode(String mobile) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("mobile", mobile);
        return TourCoolTransformer.switchSchedulersIo(getApiService().getVCode(params).retryWhen(new RetryWhen()));
    }

    /**
     * 手机号注册
     *
     * @param mobile
     * @return
     */
    public Observable<BaseEntity> mobileRegister(String mobile, String password, String vCode) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("userType", TYPE_USER_CAR_OWER);
        params.put("vCode", vCode);
        return TourCoolTransformer.switchSchedulersIo(getApiService().mobileRegister(params).retryWhen(new RetryWhen()));
    }

    /**
     * 查询附近修理厂列表
     *
     * @param position
     * @param distance
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Observable<BaseEntity> findNearbyGarages(String orderId, String position, String distance, String pageIndex, String pageSize) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("position", position);
        params.put("distance", distance);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(orderId)) {
            params.put("orderId", orderId);
        }

        return TourCoolTransformer.switchSchedulersIo(getApiService().findNearbyGarages(params).retryWhen(new RetryWhen()));
    }

    /**
     * 根据条件查询修理厂列表
     *
     * @param position
     * @param condition：条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Observable<BaseEntity<GarageEntity>> searchGarages(String position, String condition, String pageIndex, String pageSize) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("position", position);
        params.put("condition", condition);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return TourCoolTransformer.switchSchedulersIo(getApiService().searchGarages(params).retryWhen(new RetryWhen()));
    }


    /**
     * 获取我的车辆列表
     *
     * @return
     */
    public Observable<BaseEntity> findMyCars() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().findMyCars().retryWhen(new RetryWhen()));
    }


    /**
     * 故障报修
     *
     * @return
     */
    public Observable<BaseEntity> reportFault(CarInfoEntity carInfoEntity, String images, String detail, String position, String address) {
        if (carInfoEntity == null) {
            carInfoEntity = new CarInfoEntity();
        }
        Map<String, Object> params = new HashMap<>(20);
        //车辆ID
        params.put("carId", carInfoEntity.getId());
        //故障描述
        params.put("detail", detail);
        params.put("updateTime", DateUtil.getCurrentTimeString());
        params.put("position", position);
        params.put("ownerId", carInfoEntity.getOwner_id());
        if (!TextUtils.isEmpty(images)) {
            params.put("images", images);
        }
        if (!TextUtils.isEmpty(address)) {
            params.put("address", address);
            TourCooLogUtil.i(TAG, TAG + ":" + "打印的结果:" + address);
        }
        return TourCoolTransformer.switchSchedulersIo(getApiService().reportFault(params).retryWhen(new RetryWhen()));
    }

    /**
     * 上门服务
     *
     * @param carInfoEntity
     * @param images
     * @param detail
     * @param position
     * @param type
     * @return
     */
    public Observable<BaseEntity> doorToDoorService(CarInfoEntity carInfoEntity, String images, String detail, String position, int type, String address) {
        if (carInfoEntity == null) {
            carInfoEntity = new CarInfoEntity();
        }
        Map<String, Object> params = new HashMap<>(20);
        //车辆ID
        params.put("carId", carInfoEntity.getId());
        //故障描述
        params.put("detail", detail);
        params.put("updateTime", DateUtil.getCurrentTimeString());
        params.put("position", position);
        params.put("ownerId", carInfoEntity.getOwner_id());
        params.put("type", type);
        if (!TextUtils.isEmpty(images)) {
            params.put("images", images);
        }
        if (!TextUtils.isEmpty(address)) {
            params.put("address", address);
        }
        return TourCoolTransformer.switchSchedulersIo(getApiService().reportFault(params).retryWhen(new RetryWhen()));
    }


    /**
     * 获取我的订单列表
     *
     * @return
     */
    public Observable<FaultRepairEntity> findMyList(String pageIndex, String pageSize, String types) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        params.put("types", types);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findMyList(params).retryWhen(new RetryWhen()));
    }

    /**
     * 提交评价
     *
     * @return
     */
    public Observable<BaseEntity> commentOrder(FaultRepairEntity.FaultRepairInfo faultRepairInfo, String content, int level, String images) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("comment", content);
        params.put("detail", content);
        params.put("level", level);
        params.put("updateTime", DateUtil.getCurrentTimeString());
        params.put("orderId", faultRepairInfo.getId());
        if (!TextUtils.isEmpty(images)) {
            params.put("images", images);
            TourCooLogUtil.i(TAG, TAG + ":" + "上传图片:" + images);
        }
        return TourCoolTransformer.switchSchedulersIo(getApiService().commentOrder(params).retryWhen(new RetryWhen()));
    }

    /**
     * 获取车辆品类
     *
     * @return
     */
    public Observable<BaseEntity> findCategoryList() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().findCategoryList().retryWhen(new RetryWhen()));
    }

    /**
     * 根据车辆品类获取车辆型号
     *
     * @return
     */
    public Observable<BaseEntity> findCarModelList(String categoryId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("categoryId", categoryId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findCarModelList(params).retryWhen(new RetryWhen()));
    }

    /**
     * 添加车辆
     *
     * @return
     */
    public Observable<BaseEntity> addCar(Map<String, Object> params) {
        return TourCoolTransformer.switchSchedulersIo(getApiService().addCar(params).retryWhen(new RetryWhen()));
    }

    /**
     * 编辑车辆
     *
     * @return
     */
    public Observable<BaseEntity> editCar(Map<String, Object> params) {
        return TourCoolTransformer.switchSchedulersIo(getApiService().editCar(params).retryWhen(new RetryWhen()));
    }


    /**
     * 删除车辆
     *
     * @param carId
     * @return
     */
    public Observable<BaseEntity> deleteCar(String ownerId, String carId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("ownerId", ownerId);
        params.put("carId", carId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().deleteCar(params).retryWhen(new RetryWhen()));
    }


    /**
     * 保险规则列表
     *
     * @return
     */
    public Observable<BaseEntity> findMaintainRuleList() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().findMaintainRuleList().retryWhen(new RetryWhen()));
    }

    /**
     * 故障提醒模式列表
     *
     * @return
     */
    public Observable<BaseEntity<List<CarFaultRemindType>>> findFaultRemindList() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().findFaultRemindList().retryWhen(new RetryWhen()));
    }


    /**
     * 接收模式列表
     *
     * @return
     */
    public Observable<BaseEntity> findObdReceiveList() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().findObdReceiveList().retryWhen(new RetryWhen()));
    }

    /**
     * 获取指定订单应付金额
     *
     * @return
     */
    public Observable<BaseEntity<Double>> findAmount(String orderId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", orderId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findAmount(params).retryWhen(new RetryWhen()));
    }

    /**
     * 支付接口
     *
     * @return
     */
    public Observable<BaseEntity<PayInfo>> createPay(Map<String, Object> map) {
        return TourCoolTransformer.switchSchedulersIo(getApiService().createPay(map).retryWhen(new RetryWhen()));
    }

    /**
     * 取消服务
     *
     * @return
     */
    public Observable<BaseEntity> cancelOrder(String orderId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", orderId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().cancelOrder(params).retryWhen(new RetryWhen()));
    }

    /**
     * 消息列表
     *
     * @param ownerId
     * @return
     */
    public Observable<BaseEntity<MessageInfo>> getMsgList(String ownerId, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("ownerId", ownerId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageSize", pageSize + "");
        return TourCoolTransformer.switchSchedulersIo(getApiService().getMsgList(params).retryWhen(new RetryWhen()));
    }

    /**
     * 获取未读消息数量
     *
     * @param ownerId
     * @return
     */
    public Observable<BaseEntity<MessageInfo.MessageBean>> getNoReadCount(String ownerId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("ownerId", ownerId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().getNoReadCount(params).retryWhen(new RetryWhen()));
    }


    /**
     * 设置当前车辆id
     *
     * @param carId
     * @return
     */
    public Observable<BaseEntity> setDefaultCar(String carId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", carId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().setDefaultCar(params).retryWhen(new RetryWhen()));
    }

    /**
     * 上传设备信息（用于推送）
     *
     * @param clientRegId
     * @param ownerId
     * @return
     */
    public Observable<BaseEntity> uploadClientId(String clientRegId, String ownerId) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("clientRegId", clientRegId);
        params.put("deviceType", "android");
        params.put("ownerId", ownerId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().uploadClientId(params).retryWhen(new RetryWhen()));
    }


    /**
     * 获取修理厂评价
     *
     * @param garageId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Observable<BaseEntity<CommentEntity>> findGarageCommentList(String garageId, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("garageId", garageId);
        params.put("pageIndex", pageIndex + "");
        params.put("pageSize", pageSize + "");
        return TourCoolTransformer.switchSchedulersIo(getApiService().findGarageCommentList(params).retryWhen(new RetryWhen()));
    }

    /**
     * 检测版本更新
     *
     * @return
     */
    public Observable<BaseEntity> appVersionInfo() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("deviceType", "android");
        return TourCoolTransformer.switchSchedulersIo(getApiService().findObdReceiveList().retryWhen(new RetryWhen()));
    }

    /**
     * 修改个人信息
     *
     * @return
     */
    public Observable<BaseEntity> editOwnerInfo(Map<String, Object> params) {
        return TourCoolTransformer.switchSchedulersIo(getApiService().editOwnerInfo(params).retryWhen(new RetryWhen()));
    }

    /**
     * 查看个人信息
     *
     * @return
     */
    public Observable<BaseEntity<UserInfoEntity>> getUserInfo() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("userType", "0");
        return TourCoolTransformer.switchSchedulersIo(getApiService().getUserInfo(params).retryWhen(new RetryWhen()));
    }


    /**
     * 充值密码
     *
     * @return
     */
    public Observable<BaseEntity> restPassword(String mobile, String password, String vCode) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("userType", "0");
        params.put("vCode", vCode);
        return TourCoolTransformer.switchSchedulersIo(getApiService().restPassword(params).retryWhen(new RetryWhen()));
    }


    /**
     * 查看个人信息
     *
     * @return
     */
    public Observable<BaseEntity> changeMobile(String mobile, String vCode) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("mobile", mobile);
        params.put("userType", "0");
        params.put("vCode", vCode);
        return TourCoolTransformer.switchSchedulersIo(getApiService().changeMobile(params).retryWhen(new RetryWhen()));
    }

    /**
     * 用户反馈
     *
     * @param map
     * @return
     */
    public Observable<BaseEntity> feedback(Map<String, Object> map) {
        return TourCoolTransformer.switchSchedulersIo(getApiService().feedBack(map).retryWhen(new RetryWhen()));
    }

    /**
     * 获取指定订单详情
     *
     * @param id
     * @return
     */
    public Observable<BaseEntity<OrderDetail>> findDetail(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findDetail(params).retryWhen(new RetryWhen()));
    }

    public Observable<BaseEntity> findGarageDetail(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findGarageDetail(params).retryWhen(new RetryWhen()));
    }


    /**
     * 获取指定评论详情
     *
     * @param id
     * @return
     */
    public Observable<BaseEntity<List<CommentDetail>>> findComment(String id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("orderId", id);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findComment(params).retryWhen(new RetryWhen()));
    }

    /**
     * 微信登录
     *
     * @param openid
     * @return
     */
    public Observable<BaseEntity> loginByWechat(String openid) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("openid", openid);
        params.put("userType", TYPE_USER_CAR_OWER);
        return TourCoolTransformer.switchSchedulersIo(getApiService().loginByWechat(params).retryWhen(new RetryWhen()));
    }

    /**
     * 绑定手机号
     *
     * @param openid
     * @param mobile
     * @param vCode
     * @return
     */
    public Observable<BaseEntity> bindMobile(String openid, String mobile, String vCode) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("code", openid);
        params.put("mobile", mobile);
        params.put("userType", TYPE_USER_CAR_OWER);
        params.put("vCode", vCode);
        return TourCoolTransformer.switchSchedulersIo(getApiService().bindMobile(params).retryWhen(new RetryWhen()));
    }

    /**
     * 查询保险公司
     *
     * @param location
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Observable<BaseEntity<InsuranceCompany>> queryAllInsurance(Location location, String keyword, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("lat", location.getLatitude());
        params.put("lng", location.getLongitude());
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        params.put("name", keyword);
        return TourCoolTransformer.switchSchedulersIo(getApiService().queryAllInsurance(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseEntity<InsuranceCompany.CompanyInfo>> queryInsuranceDetailById(String companyId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", companyId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().queryInsuranceDetailById(params).retryWhen(new RetryWhen()));
    }


    public Observable<BaseEntity<ServiceInfo>> findMyService(int orderId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("orderId", orderId);
        return TourCoolTransformer.switchSchedulersIo(getApiService().findMyService(params).retryWhen(new RetryWhen()));
    }


    /**
     * 客服热线
     * @return
     */
    public Observable<String> getServicePhone() {
        return TourCoolTransformer.switchSchedulersIo(getApiService().getServicePhone().retryWhen(new RetryWhen()));
    }
}

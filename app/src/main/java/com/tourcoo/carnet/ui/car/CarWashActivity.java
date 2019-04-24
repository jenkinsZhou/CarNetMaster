package com.tourcoo.carnet.ui.car;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.order.OrderHistoryActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.OrderConstant.EXTRA_ORDER_TYPE;
import static com.tourcoo.carnet.core.common.OrderConstant.TAB_KEY;
import static com.tourcoo.carnet.core.common.OrderConstant.TAB_SERVICE;
import static com.tourcoo.carnet.core.common.OrderConstant.TYPE_CAR_WASH;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.ui.order.OrderHistoryActivity.EXTRA_SKIP_TAG;

/**
 * @author :zhoujian
 * @description :上门洗车
 * @company :翼迈科技
 * @date 2019年 03月 17日 10时45分
 * @Email: 971613168@qq.com
 */
public class CarWashActivity extends BaseTourCooTitleActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private TextView tvConfirm;
    private TextView tvLocation;
    private TextView btnLocate;
    private String currentPosition;
    private EditText etRepairContent;
    private String mAddress;

    @Override
    public int getContentLayout() {
        return R.layout.activity_car_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvConfirm = findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(this);
        tvLocation = findViewById(R.id.tvLocation);
        btnLocate = findViewById(R.id.btnLocate);
        btnLocate.setOnClickListener(this);
        etRepairContent = findViewById(R.id.etRepairContent);
        getLocate();
    }


    private String getAddress(AMapLocation mapLocation) {
        String address = "";
        if (mapLocation == null) {
            return address;
        }
        return mapLocation.getAddress();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("上门洗车");
        titleBar.setRightText("历史洗车");
        titleBar.setRightTextColor(TourCooUtil.getColor(R.color.blueCommon));
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToOrderHistory();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvConfirm:
                doorToDoorService(AccountInfoHelper.getInstance().getCurrentCar());
                break;
            case R.id.btnLocate:
                getLocate();
                break;
            default:
                break;
        }
    }


    private String showResult(AMapLocation location) {
        StringBuffer sb = new StringBuffer();
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.getSatellites() + "\n");
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
//                sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
//            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间
//            sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
            //解析定位结果，
            return sb.toString();
        } else {
            return "定位失败，loc is null";
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //权限已被用户授予
        getLocate();
        closeLoadingDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
        showLocateFailed();
        closeLoadingDialog();
    }


    @Override
    protected void onDestroy() {
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }

    private void showLocating() {
        tvLocation.setText("定位中...");
    }

    private void showLocateFailed() {
        closeLoadingDialog();
        tvLocation.setText("定位失败,未授予定位权限");
        addLocateImage("定位失败,未授予定位权限");
    }

    private void showLocateSuccess(String address) {
        tvLocation.setText(address);
        closeLoadingDialog();
        addLocateImage(address);
    }


    private void getLocate() {
        showLoadingDialog();
        showLocating();
        if (checkLocatePermission()) {
            locate();
        } else {
            PermissionManager.requestPermission(mContext, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
        }
    }

    private void addLocateImage(String text) {
        SpannableString ss = new SpannableString(text + "  ");
        int len = ss.length();
        //图片
        Drawable d = ContextCompat.getDrawable(mContext, (R.mipmap.ic_positioning));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //构建ImageSpan
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, len - 1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvLocation.setText(ss);
    }

    /***
     * 定位
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String result = showResult(aMapLocation);
                TourCooLogUtil.d(TAG, "回调结果:" + result);
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    showLocateSuccess(aMapLocation.getAddress());
                    currentPosition = getPosition(aMapLocation);
                    mAddress = getAddress(aMapLocation);
                } else {
                    showLocateFailed();
                }
                LocateHelper.getInstance().stopLocation();
            }
        });
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(this, PermissionConstance.PERMS_LOCATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 上门洗车
     */
    private void doorToDoorService(CarInfoEntity carInfoEntity) {
        if (TextUtils.isEmpty(getDetail())) {
            ToastUtil.show("请输入内容");
            return;
        }
        if (carInfoEntity == null || carInfoEntity.getBrandName() == null) {
            ToastUtil.show("当前没有车辆,请先添加车辆");
            return;
        }
        if (TextUtils.isEmpty(currentPosition)) {
            ToastUtil.show("未获取位置信息");
            return;
        }
        ApiRepository.getInstance().doorToDoorService(carInfoEntity, "", getDetail(), currentPosition, TYPE_CAR_WASH, mAddress).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                clearUploadData();
                                parseOrderInfo(entity.data);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        closeLoadingDialog();
                        super.onRequestError(e);
                    }
                });
    }

    private void parseOrderInfo(Object object) {
        try {
            String orderInfo = JSONObject.toJSONString(object);
            JSONObject jsonObject = JSONObject.parseObject(orderInfo);
            jsonObject.getString("captcha");
            String vCode = jsonObject.getString("captcha");
            showVCodeDialog(vCode);
        } catch (Exception e) {
            ToastUtil.showFailed("订单信息获取失败");
        }
    }

    /**
     * 获取位置信息
     *
     * @return
     */
    private String getPosition(AMapLocation mapLocation) {
        String position = "";
        if (mapLocation == null) {
            return position;
        }
        //经度
        position += mapLocation.getLongitude();
        position += ",";
        //纬度
        position += mapLocation.getLatitude();
        return position;
    }


    private String getDetail() {
        return etRepairContent.getText().toString();
    }


    /**
     * 显示验证码
     *
     * @param vCode
     */
    private void showVCodeDialog(String vCode) {
        String msg = "接单验证码:";
        msg += vCode;
        showAlertDialog("提交成功", msg, "我知道了");
    }

    /**
     * 清空上一次上传的数据
     */
    private void clearUploadData() {
        currentPosition = "";
        addLocateImage("未获取位置信息,请点击图标获取");
        etRepairContent.setText("");
    }


    /**
     * 从上门洗车跳转至订单历史
     */
    private void skipToOrderHistory() {
        Intent intent = new Intent();
        //将订单类型置为上门保养
        intent.putExtra(EXTRA_ORDER_TYPE, TYPE_CAR_WASH);
        //当前要显示上门服务的tab
        intent.putExtra(TAB_KEY, TAB_SERVICE);
        //上门服务跳转的标记
        intent.putExtra(EXTRA_SKIP_TAG, TYPE_CAR_WASH);
        intent.setClass(mContext, OrderHistoryActivity.class);
        startActivity(intent);
    }
}

package com.tourcoo.carnet.ui.repair;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.interfaces.IMultiStatusView;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.util.SizeUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.core.action.ActionSheetDialog;
import com.tourcoo.carnet.core.widget.core.action.BaseDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.InsuranceCompany;
import com.tourcoo.carnet.entity.InsuranceCompanyDetail;
import com.tourcoo.carnet.entity.garage.GarageInfo;
import com.tourcoo.carnet.entity.order.OrderDetail;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.BaseTourCooTitleMultiViewActivity;
import com.tourcoo.carnet.utils.Location;
import com.trello.rxlifecycle3.android.ActivityEvent;


import java.math.BigDecimal;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.util.TourCooUtil.checkMapAppsIsExist;

/**
 * @author :zhoujian
 * @description :保险公司详情
 * @company :翼迈科技
 * @date 2019年 04月 19日 22时06分
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyDetailActivity extends BaseTourCooTitleMultiViewActivity implements View.OnClickListener {
    public static final String EXTRA_COMPANY_ID = "EXTRA_COMPANY_ID";
    private int companyId;
    private InsuranceCompanyDetail companyDetail;
    private TextView tvWarrantyCompanyName;
    private TextView tvWarrantyCompanyDepiction;
    private SuperTextView stvAlarmPhone;
    private SuperTextView stvBranchCompanyName;
    private SuperTextView stvBranchCompanyAddress;

    private SuperTextView stvBranchCompanyTel;


    private Location mStart, mEnd;

    //百度地图URL跳转到步行路线的参数

    //头部 添加相应地区
    private final static String BAIDU_HEAD = "baidumap://map/direction?region=0";
    //起点的经纬度
    private final static String BAIDU_ORIGIN = "&origin=";
    //终点的经纬度
    private final static String BAIDU_DESTINATION = "&destination=";
    //路线规划方式
    private final static String BAIDU_MODE = "&mode=walking";
    //百度地图的包名
    private final static String BAIDU_PKG = "com.baidu.BaiduMap";

    //高德地图URL跳转到步行路线的参数

    //头部 后面的sourceApplicaiton填自己APP的名字
    private final static String GAODE_HEAD = "androidamap://route?sourceApplication=BaiduNavi";
    //起点经度
    private final static String GAODE_SLON = "&slon=";
    //起点纬度
    private final static String GAODE_SLAT = "&slat=";
    //起点名字
    private final static String GAODE_SNAME = "&sname=";
    //终点经度
    private final static String GAODE_DLON = "&dlon=";
    //终点纬度
    private final static String GAODE_DLAT = "&dlat=";
    //终点名字
    private final static String GAODE_DNAME = "&dname=";
    // dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
    // t = 1(公交) =2（驾车） =4(步行)
    private final static String GAODE_MODE = "&dev=0&t=4";
    //高德地图包名
    private final static String GAODE_PKG = "com.autonavi.minimap";

    //腾讯地图URL跳转到路线的参数

    //头部 type出行方式
    private final static String TX_HEAD = "qqmap://map/routeplan?type=walk";
    //起点名称
    private final static String TX_FROM = "&from=";
    //起点的经纬度
    private final static String TX_FROMCOORD = "&fromcoord=";
    //终点名称
    private final static String TX_TO = "&to=";
    //终点的经纬度
    private final static String TX_TOCOORD = "&tocoord=";
    /**
     * 本参数取决于type参数的取值
     * 公交：type=bus，policy有以下取值
     * 0：较快捷
     * 1：少换乘
     * 2：少步行
     * 3：不坐地铁
     * 驾车：type=drive，policy有以下取值
     * 0：较快捷
     * 1：无高速
     * 2：距离
     * policy的取值缺省为0
     */
    private final static String TX_END = "&policy=1&referer=myapp";
    //腾讯地图包名
    private final static String TX_PKG = "com.tencent.map";

    private double myLat;
    private double myLong;
    private RelativeLayout rlContentView;


    @Override
    public int getContentLayout() {
        return R.layout.activity_insurance_company_detail;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("保险公司详情");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        rlContentView= findViewById(R.id.rlContentView);
        companyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, 0);
        tvWarrantyCompanyName = findViewById(R.id.tvWarrantyCompanyName);
        tvWarrantyCompanyDepiction = findViewById(R.id.tvWarrantyCompanyDepiction);
        stvAlarmPhone = findViewById(R.id.stvAlarmPhone);
        stvBranchCompanyName = findViewById(R.id.stvBranchCompanyName);
        stvBranchCompanyAddress = findViewById(R.id.stvBranchCompanyAddress);
        stvBranchCompanyTel = findViewById(R.id.stvBranchCompanyTel);
        stvBranchCompanyAddress.setOnClickListener(this);
        findViewById(R.id.tvCallPhone).setOnClickListener(this);
        getLocate();
        queryInsuranceDetailById(companyId);
    }


    private void queryInsuranceDetailById(int companyId) {
        TourCooLogUtil.d("companyId:", "companyId:" + companyId);
        ApiRepository.getInstance().queryInsuranceDetailById(companyId + "").compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<InsuranceCompany.CompanyInfo>>() {
                    @Override
                    public void onRequestNext(BaseEntity<InsuranceCompany.CompanyInfo> entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                //todo
                                showCompanyInfo(entity.data);
                                //加载位置信息
                                //todo
                                setEndPosition(entity.data);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stvBranchCompanyAddress:
                doNaVi();
                break;
            case R.id.tvCallPhone:
                showPhoneDialog(companyDetail);
                break;
            default:
                break;
        }
    }


    private void showCompanyInfo(InsuranceCompany.CompanyInfo companyDetail) {
        if (companyDetail == null) {
            ToastUtil.showFailed("信息获取失败");
            return;
        }
        tvWarrantyCompanyName.setText(getNotNullValue(companyDetail.getInsuranceName()));
        tvWarrantyCompanyDepiction.setText(getNotNullValue(companyDetail.getCompanyProfile()));
        stvAlarmPhone.setCenterString(getNotNullValue(companyDetail.getAlarmPhone()));
        stvBranchCompanyName.setCenterString(getNotNullValue(companyDetail.getCompanyBatchName()));
        stvBranchCompanyAddress.setCenterString(getNotNullValue(companyDetail.getAddress()));
        stvBranchCompanyTel.setCenterString(getNotNullValue(getPhone(companyDetail)));
    }

    private String getPhone(InsuranceCompany.CompanyInfo companyDetail) {
        String value = "电话:";
        value += companyDetail.getTeleAreaCode() + "-";
        value += companyDetail.getTelephone();
        return value;
    }

    private String getNotNullValue(String value) {
        return TourCooUtil.getNotNullValue(value);
    }

    private void showSheetDialog() {
        String[] array = new String[]{"百度地图", "高德地图"};
        new ActionSheetDialog.ListIOSBuilder(this)
                .addItems(array)
                .setItemsTextColorResource(R.color.colorActionSheetItemText)
                .setCancel(R.string.cancel)
                .setCancelMarginTop(SizeUtil.dp2px(8))
                .setCancelTextColorResource(R.color.colorActionSheetItemText)
                .setOnItemClickListener(mOnItemClickListener)
                .create()
                .setDimAmount(0.6f)
//                .setAlpha(0.6f)
                .show();
    }

    private ActionSheetDialog.OnItemClickListener mOnItemClickListener = new ActionSheetDialog.OnItemClickListener() {
        @Override
        public void onClick(BaseDialog dialog, View itemView, int position) {
            switch (position) {
                case 0:
                    skipBaidu();
                    break;
                case 1:
                    skipGaode();
                    break;
                default:
                    break;

            }
            dialog.dismiss();
        }
    };


    private void skipBaidu() {
        Intent intent = new Intent();
        if (checkMapAppsIsExist(mContext, BAIDU_PKG)) {
            intent.setData(Uri.parse(BAIDU_HEAD + BAIDU_ORIGIN + mStart.getLatitude()
                    + "," + mStart.getLongitude() + BAIDU_DESTINATION + mEnd.getLatitude() + "," + mEnd.getLongitude()
                    + BAIDU_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "百度地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipGaode() {
        Intent intent = new Intent();
        if (checkMapAppsIsExist(mContext, GAODE_PKG)) {
            BD09ToGCJ02(mStart, mEnd);
            intent.setAction("android.intent.action.VIEW");
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(GAODE_HEAD + GAODE_SLAT + mStart.getLatitude() + GAODE_SLON + mStart.getLongitude() +
                    GAODE_SNAME + mStart.getName() + GAODE_DLAT + mEnd.getLatitude() + GAODE_DLON + mEnd.getLongitude() +
                    GAODE_DNAME + mEnd.getName() + GAODE_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "高德地图未安装", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 坐标转换
     *
     * @param mStart
     * @param mEnd
     */
    public void BD09ToGCJ02(Location mStart, Location mEnd) {
        LatLng newStart = convertBaiduToGPS(new LatLng(mStart.getLatitude(), mStart.getLongitude()));
        LatLng newEnd = convertBaiduToGPS(new LatLng(mEnd.getLatitude(), mEnd.getLongitude()));
        mStart.setLatitude(newStart.latitude);
        mStart.setLongitude(newStart.longitude);

        mEnd.setLatitude(newEnd.latitude);
        mEnd.setLongitude(newEnd.longitude);

    }

    /**
     * 将百度地图坐标转换成GPS坐标
     *
     * @param sourceLatLng
     * @return
     */
    public LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter(mContext);
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }

    private void getLocate() {
        if (checkLocatePermission()) {
            locate();
        } else {
            showLocatePermissionDialog("请前往权限管理授予定位权限");
        }
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(this, PermissionConstance.PERMS_LOCATE);
    }


    @Override
    protected IMultiStatusView getMultiStatusView() {
        return new IMultiStatusView() {
            @Override
            public View getMultiStatusContentView() {
                return rlContentView;
            }

            @Override
            public void setMultiStatusView(StatusLayoutManager.Builder statusView) {

            }

            @Override
            public View.OnClickListener getEmptyClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }

            @Override
            public View.OnClickListener getErrorClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }

            @Override
            public View.OnClickListener getCustomerClickListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }
        } ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocateHelper.getInstance().destroyLocationInstance();
    }

    /***
     * 定位
     */

    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    myLat = aMapLocation.getLatitude();
                    myLong = aMapLocation.getLongitude();
                    setStartPosition(aMapLocation);
                } else {
                    ToastUtil.showFailed("定位失败");
                }
                closeLoadingDialog();
                LocateHelper.getInstance().stopLocation();
            }
        });
    }

    private void setStartPosition(AMapLocation mapLocation) {
        if (mapLocation == null) {
            return;
        }
        mStart = new Location(mapLocation.getLongitude(), mapLocation.getLatitude(), mapLocation.getAddress());
    }


    private void setEndPosition(InsuranceCompany.CompanyInfo companyDetail) {
        if (companyDetail == null) {
            ToastUtil.show("未获取到订单信息");
            return;
        }
        TourCooLogUtil.d("位置:myLong = " + myLong);
        TourCooLogUtil.d("位置:myLat = " + myLat);
        TourCooLogUtil.d("位置:getLng = " + companyDetail.getLng());
        TourCooLogUtil.d("位置:getLat = " + companyDetail.getLat());
        mEnd = new Location(companyDetail.getLng(), companyDetail.getLat(), companyDetail.getAddress());
    }


    private void doNaVi() {
        //定位并显示导航
        if (mStart == null || mStart.getLatitude() == 0 || mStart.getLongitude() == 0) {
            ToastUtil.show("未获取到您的位置 无法导航");
            return;
        }
        if (mEnd == null || mEnd.getLatitude() == 0 || mEnd.getLongitude() == 0) {
            ToastUtil.show("未获取到目的地位置 无法导航 ");
            return;
        }
        showSheetDialog();
    }


    private void showPhoneDialog(InsuranceCompanyDetail companyDetail) {
        //客服电话
        if (companyDetail == null || TextUtils.isEmpty(companyDetail.getTelephone())) {
            ToastUtil.show("未获取到联系方式");
            return;
        }
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessageGravity(Gravity.CENTER_HORIZONTAL);
        builder.setTitle("电话联系").setFirstMessage(companyDetail.getTelephone())
                .setFirstTextColor(TourCooUtil.getColor(R.color.blueCommon))
                .setFirstMsgSize(15).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callPhone(companyDetail.getTelephone());
                    }
                });
        showConfirmDialog(builder);
    }


}

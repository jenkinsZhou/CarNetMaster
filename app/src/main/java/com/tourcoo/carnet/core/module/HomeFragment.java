package com.tourcoo.carnet.core.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.fragment.BaseTitleFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.NetworkUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.helper.TitleBarViewHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.view.OptionsPickerView;
import com.tourcoo.carnet.core.widget.core.util.SizeUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.entity.event.CarRefreshEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.MsgSystemActivity;
import com.tourcoo.carnet.ui.car.AddCarActivity;
import com.tourcoo.carnet.ui.repair.FastSearchActivity;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.module.MineFragment.CODE_REQUEST_MSG;

/**
 * @author :zhoujian
 * @description : 首页
 * @company :翼迈科技
 * @date 2019年03月06日上午 10:12
 * @Email: 971613168@qq.com
 */
public class HomeFragment extends BaseTitleFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    /**
     * 添加车辆回调
     */
    private static final int CODE_REQUEST_ADD_CAR = 1002;
    private TextureMapView homeMapView;
    private AMap aMap;
    private RelativeLayout rlToolBar;
    private ImageView ivChange;
    private UserInfoEntity mUserInfo;
    private ImageView ivBindObd;
    /**
     * 是否拥有车辆
     */
    private boolean flagHasCar = false;
    private View viewStatusBar;
    private ImageView ivSpeedBoard;
    private RelativeLayout rlBindObd;
    private CarInfoEntity carInfoEntity;
    private ImageView ivRedDot;
    private OptionsPickerView opvCarCategory;

    /**
     * 当前车辆品牌
     */
    private TextView tvCurrentCarBrand;

    private TextView tvSwitchCarBrand;
    /**
     * 仪表盘控制区域
     */
    private RelativeLayout rlSpeedBoard;

    private LinearLayout llObd;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initCarCategoryOptionsPicker();
        rlToolBar = mContentView.findViewById(R.id.rlToolBar);
        ivChange = mContentView.findViewById(R.id.ivChange);
        viewStatusBar = mContentView.findViewById(R.id.viewStatusBar);
        ivSpeedBoard = mContentView.findViewById(R.id.ivSpeedBoard);
        rlSpeedBoard = mContentView.findViewById(R.id.rlSpeedBoard);
        tvCurrentCarBrand = mContentView.findViewById(R.id.tvCurrentCarBrand);
        tvSwitchCarBrand = mContentView.findViewById(R.id.tvSwitchCarBrand);
        rlBindObd = mContentView.findViewById(R.id.rlBindObd);
        mContentView.findViewById(R.id.ivSearch).setOnClickListener(this);
        tvSwitchCarBrand.setOnClickListener(this);
        llObd = mContentView.findViewById(R.id.llObd);
        ivChange.setOnClickListener(this);
        ivSpeedBoard.setOnClickListener(this);
        ivRedDot = mContentView.findViewById(R.id.ivRedDot);
        mContentView.findViewById(R.id.ivBindObd).setOnClickListener(this);
        ivRedDot.setOnClickListener(this);
        int alpha = 150;
        rlToolBar.getBackground().setAlpha(alpha);
        viewStatusBar.getBackground().setAlpha(alpha);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewStatusBar.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = StatusBarUtil.getStatusBarHeight();
        }
        showBindObd(true);
        mUserInfo = AccountInfoHelper.getInstance().getUserInfoEntity();
        EventBus.getDefault().register(this);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar
                .setDividerVisible(false)
                .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102)
                .setStatusAlpha(0)
                .setVisibility(View.GONE);
        StatusBarUtil.setStatusBarLightMode(mContext);

        titleBar.setLeftTextColor(Color.WHITE)
                .setBgColor(Color.WHITE);
        int mMaxHeight = CarNetApplication.getImageHeight() - StatusBarUtil.getStatusBarHeight() - getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        new TitleBarViewHelper(mContext)
                .setTitleBarView(mTitleBar)
                .setMinHeight(0)
                .setMaxHeight(mMaxHeight);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        homeMapView = mContentView.findViewById(R.id.homeMapView);
        homeMapView.onCreate(savedInstanceState);
        initMap();
        showCurrentCarInfo(AccountInfoHelper.getInstance().getCurrentCar());
        return mContentView;
    }

    @Override
    public void onPause() {
        homeMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        homeMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        homeMapView.onDestroy();
        EventBus.getDefault().unregister(this);
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }


    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = homeMapView.getMap();
        }
        // 矢量地图模式
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置定位蓝点的Style
        // 设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.public_location_my));
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //缩放级别 地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //去除+-图标
        aMap.getUiSettings().setZoomControlsEnabled(false);
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.public_location_my));
        aMap.setMyLocationStyle(myLocationStyle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivChange:
                showViewByStatus(true);
                break;
            case R.id.ivSpeedBoard:
                showViewByStatus(false);
                break;
            case R.id.tvSwitchCarBrand:
                //先判断是否有车辆
                if (flagHasCar) {
                    showCarSelectDialog();
                } else {
                    //跳转添加车辆页面
                    Intent intent = new Intent();
                    intent.setClass(mContext, AddCarActivity.class);
                    startActivityForResult(intent, CODE_REQUEST_ADD_CAR);
                }
                break;
            case R.id.ivSearch:
                TourcooUtil.startActivity(mContext, FastSearchActivity.class);
                break;
            case R.id.ivRedDot:
                //系统消息
                Intent intent = new Intent();
                intent.setClass(mContext, MsgSystemActivity.class);
                startActivityForResult(intent, CODE_REQUEST_MSG);
                break;
            case R.id.ivBindObd:
                ToastUtil.show("obd功能暂未开放");
                break;
            default:
                break;
        }
    }

    /**
     * 根据状态切换显示模式
     *
     * @param mapMode
     */
    private void showViewByStatus(boolean mapMode) {
        if (mapMode) {
            homeMapView.setVisibility(View.GONE);
            rlSpeedBoard.setVisibility(View.VISIBLE);
        } else {
            homeMapView.setVisibility(View.VISIBLE);
            rlSpeedBoard.setVisibility(View.GONE);
        }
    }


    private void showBindObd(boolean show) {
        if (show) {
            rlBindObd.setVisibility(View.VISIBLE);
            ivChange.setVisibility(View.GONE);
            llObd.setVisibility(View.GONE);
        } else {
            rlBindObd.setVisibility(View.GONE);
            ivChange.setVisibility(View.VISIBLE);
            llObd.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取账号对应的车辆信息
     */
    private void getMyCarInfoList() {
        ApiRepository.getInstance().findMyCars().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                List<CarInfoEntity> parseCarInfoList = parseCarInfoList(entity.data);
                                AccountInfoHelper.getInstance().setCarInfoEntityList(parseCarInfoList);
                                //设置车辆有无状态位
                                flagHasCar = hasCar(parseCarInfoList);
                                if (parseCarInfoList != null) {
                                    if (!parseCarInfoList.isEmpty()) {
                                        if (parseCarInfoList.size() == 1) {
                                            AccountInfoHelper.getInstance().setDefaultCar(parseCarInfoList.get(0));
                                            showCurrentCarInfo(parseCarInfoList.get(0));
                                            setDefaultCar(parseCarInfoList.get(0), false);
                                            return;
                                        }
                                        for (CarInfoEntity infoEntity : parseCarInfoList) {
                                            if (infoEntity != null && infoEntity.isDefault()) {
                                                //设置当前车辆
                                                AccountInfoHelper.getInstance().setDefaultCar(infoEntity);
                                                showCurrentCarInfo(infoEntity);
                                                TourcooLogUtil.i("车辆信息已保存");
                                                break;
                                            }
                                        }
                                        autoSettingCarAndShow(parseCarInfoList);
                                    } else {
                                        //当前车辆无车辆
                                        AccountInfoHelper.getInstance().setDefaultCar(null);
                                        showCurrentCarInfo(AccountInfoHelper.getInstance().getCurrentCar());
                                    }
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private List<CarInfoEntity> parseCarInfoList(Object jsonArrayStr) {
        try {
            return JSONObject.parseArray(JSON.toJSONString(jsonArrayStr), CarInfoEntity.class);
        } catch (Exception e) {
            TourcooLogUtil.e(TAG, "解析失败:" + e.toString());
            return null;
        }
    }


    @Override
    public void loadData() {
        super.loadData();
        getLocate();
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtil.show("网络未连接");
            return;
        }
        getRedDotCount();
        getMyCarInfoList();

    }

    /**
     * 判断该账号是否有车
     *
     * @param carInfoEntityList
     * @return
     */
    private boolean hasCar(List<CarInfoEntity> carInfoEntityList) {
        return carInfoEntityList != null && !carInfoEntityList.isEmpty();
    }


    private void getRedDotCount() {
        if (!NetworkUtil.isConnected(mContext) || mUserInfo == null || mUserInfo.getUserInfo() == null) {
            return;
        }
        ApiRepository.getInstance().getNoReadCount(mUserInfo.getUserInfo().getId() + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<MessageInfo.MessageBean>>() {
                    @Override
                    public void onRequestNext(BaseEntity<MessageInfo.MessageBean> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                    showRedDotByCondition(entity.data.getCountUnreadMsg());
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private void showRedDotByCondition(int count) {
        if (count <= 0) {
            ivRedDot.setImageResource(R.mipmap.ic_information_normal);
        } else {
            ivRedDot.setImageResource(R.mipmap.ic_information_red);
        }
    }


    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent != null) {
            showRedDotByCondition(baseEvent.id);
        }
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onRefreshCarInfoEvent(CarRefreshEvent carRefreshEvent) {
        if (carRefreshEvent != null) {
            getMyCarInfoList();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_MSG:
                getRedDotCount();
                break;
            case CODE_REQUEST_ADD_CAR:
                getMyCarInfoList();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化车辆品类选择器
     */
    @SuppressWarnings("uncheked")
    private void initCarCategoryOptionsPicker() {
        List<CarInfoEntity> carInfoEntityList = AccountInfoHelper.getInstance().getCarInfoEntityList();
        // 不联动
        opvCarCategory = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (carInfoEntityList != null) {
                    carInfoEntity = carInfoEntityList.get(options1);
                    if (carInfoEntity != null) {
                        setDefaultCar(carInfoEntity, true);
                    }
                }
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvCarCategory.setNPicker(carInfoEntityList, null, null);
        opvCarCategory.setSelectOptions(0, 1, 1);
    }

    /**
     * 显示当前的车辆信息
     *
     * @param carInfoEntity
     */
    private void showCurrentCarInfo(CarInfoEntity carInfoEntity) {
        if (carInfoEntity == null) {
            tvCurrentCarBrand.setText("当前无车辆");
            tvSwitchCarBrand.setText("添加车辆");
            flagHasCar = false;
            return;
        }
        tvCurrentCarBrand.setText(carInfoEntity.getBrandName());
        tvSwitchCarBrand.setText("切换车辆");
        Drawable drawable = TourcooUtil.getDrawable(R.mipmap.icon_drop_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvSwitchCarBrand.setCompoundDrawables(null, null, drawable, null);
        tvSwitchCarBrand.setCompoundDrawablePadding(SizeUtil.dp2px(5));
    }

    private void showCarSelectDialog() {
        if (AccountInfoHelper.getInstance().getCarInfoEntityList().isEmpty()) {
            return;
        }
        opvCarCategory.show();
    }

    /**
     * 设置默认车辆
     */
    private void setDefaultCar(CarInfoEntity carInfo, boolean showToast) {
        carInfoEntity = carInfo;
        if (TextUtils.isEmpty(carInfoEntity.getId() + "")) {
            ToastUtil.show("请输入车辆id");
            return;
        }
        ApiRepository.getInstance().setDefaultCar(carInfoEntity.getId() + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                showCurrentCarInfo(carInfoEntity);
                                AccountInfoHelper.getInstance().setDefaultCar(carInfoEntity);
                                if (showToast) {
                                    ToastUtil.showSuccess("成功设置默认车辆");
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    /**
     * 判断是否有车辆列表中是否有默认车辆
     *
     * @param carInfoEntityList
     * @return
     */
    private boolean hasDefaultCar(List<CarInfoEntity> carInfoEntityList) {
        if (carInfoEntityList != null && !carInfoEntityList.isEmpty()) {
            for (CarInfoEntity infoEntity : carInfoEntityList) {
                if (infoEntity.isDefault()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 如果用户把默认车辆删除了 系统则会自动其他车辆作为当前的默认车辆,如果有默认车辆则不设置
     *
     * @param carInfoEntityList
     */
    private void autoSettingCarAndShow(List<CarInfoEntity> carInfoEntityList) {
        if (carInfoEntityList != null && carInfoEntityList.size() > 1) {
            boolean hasDefaultCar = hasDefaultCar(carInfoEntityList);
            if (hasDefaultCar) {
                return;
            }
            AccountInfoHelper.getInstance().setDefaultCar(carInfoEntityList.get(0));
            showCurrentCarInfo(AccountInfoHelper.getInstance().getCurrentCar());
        }
    }


    private void getLocate() {
        if (checkLocatePermission()) {
            locate();
        } else {
            PermissionManager.requestPermission(HomeFragment.this, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
        }
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(mContext, PermissionConstance.PERMS_LOCATE);
    }

    /***
     * 定位
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LocateHelper.getInstance().stopLocation();
            }
        });
    }


    /**
     * 只负责定位
     */
    private void requestLocate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //定位回调
                closeLoadingDialog();
                LocateHelper.getInstance().stopLocation();
                if (aMapLocation != null) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        requestLocate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
    }


}

package com.tourcoo.carnet.ui.factory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.RepairDepotDescriptionAdapter;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.activity.BaseRefreshLoadActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.GarageEntity;
import com.tourcoo.carnet.entity.garage.GarageInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.repair.RepairFactoryDetailActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.ui.repair.RepairFactoryDetailActivity.EXTRA_GARAGE_DETAIL;

/**
 * @author :zhoujian
 * @description :附近修理厂
 * @company :翼迈科技
 * @date 2019年 03月 22日 21时38分
 * @Email: 971613168@qq.com
 */
public class NearbyRepairFactoryActivity extends BaseRefreshLoadActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private TextureMapView mapView;
    private AMap aMap;
    private RepairDepotDescriptionAdapter depotDescriptionAdapter;
    private String currentPosition;

    @Override
    public int getContentLayout() {
        return R.layout.activity_nearby_repair_factory;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mDefaultPage = 1;
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        initItemClick();
    }

    @Override
    public void loadData() {
        super.loadData();
        mStatusManager.showSuccessLayout();
        showLoadingDialog();
        getLocate();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("附近修理厂");
        titleBar.setRightText("重新定位");
        titleBar.setRightTextColor(TourcooUtil.getColor(R.color.blueCommon));
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocate();
            }
        });
    }

    /**
     * 只负责定位
     */
    private void requestLocate() {
        showLoadingDialog();
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //定位回调
                closeLoadingDialog();
                LocateHelper.getInstance().stopLocation();
                if (aMapLocation != null) {
                    currentPosition = getPosition(aMapLocation);
                    aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapView);
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        init();
        mapView.onSaveInstanceState(savedInstanceState);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        // 矢量地图模式
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(5000);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        // 设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.public_location_my));
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        depotDescriptionAdapter = new RepairDepotDescriptionAdapter();
        return depotDescriptionAdapter;
    }

    @Override
    public void loadData(int page) {
        TourcooLogUtil.i(TAG, "已执行：" + page);
        findNearbyGarages(String.valueOf(page), String.valueOf(mDefaultPageSize));
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 1;
        super.onRefresh(refreshlayout);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //权限已被用户授予
        getLocate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 获取当前位置
     */
    private void getLocate() {
        showLoadingDialog();
        locate();
    }

    /**
     * 定位并请求数据
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //定位回调
                closeLoadingDialog();
                currentPosition = getPosition(aMapLocation);
                LocateHelper.getInstance().stopLocation();
                if (TextUtils.isEmpty(currentPosition)) {
                    ToastUtil.showFailed("未获取到当前位置信息");
                    return;
                }
                //拿到位置信息后才请求接口
                TourcooLogUtil.i("当前经纬度:" + currentPosition);
                refreshRequest();
            }
        });
    }

    /**
     * 获取位置信息
     *
     * @param mapLocation
     * @return
     */
    private String getPosition(AMapLocation mapLocation) {
        if (mapLocation == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        //经度
        stringBuilder.append(mapLocation.getLongitude());
        stringBuilder.append(",");
        //纬度
        stringBuilder.append(mapLocation.getLatitude());
        return stringBuilder.toString();
    }


    /**
     * 获取附近修理厂列表
     */
    private void findNearbyGarages(String pageIndex, String pageSize) {
        TourcooLogUtil.i("当前请求的页码：", pageIndex + "默认每页的数目" + pageSize);
        ApiRepository.getInstance().findNearbyGarages(currentPosition, "100", pageIndex, pageSize).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity>(getIHttpRequestControl()) {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                GarageEntity garageEntity = parseGarageEntity(JSONObject.toJSONString(entity.data));
                                if (garageEntity != null) {
                                    TourcooLogUtil.i("本次数据页码：getPages:", garageEntity.getPages());
                                    TourcooLogUtil.i("本次数据页码：getCurrent:", garageEntity.getCurrent());
                                    TourcooLogUtil.i("本次数据长度：", garageEntity.getGarageList().size());
                                    UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), garageEntity.getGarageList() == null ? new ArrayList<>() : garageEntity.getGarageList(), null);
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        mStatusManager.showErrorLayout();
                    }
                });
    }

    private GarageEntity parseGarageEntity(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            return JSON.parseObject(jsonStr, GarageEntity.class);
        } catch (Exception e) {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    private void initItemClick() {
        depotDescriptionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<GarageInfo> factoryEntityList = (List<GarageInfo>) adapter.getData();
              /*  Bundle bundle = new Bundle();
                bundle.putSerializable("GarageInfo", factoryEntityList.get(position));
                if (factoryEntityList.get(position) == null) {
                    ToastUtil.show("本来就是空的");
                }*/
                Intent intent = new Intent();
                GarageInfo garageInfo = factoryEntityList.get(position);
                intent.putExtra(EXTRA_GARAGE_DETAIL, garageInfo);
                intent.setClass(mContext, RepairFactoryDetailActivity.class);
                startActivity(intent);
//                TourcooUtil.startActivity(mContext, RepairFactoryDetailActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void refreshRequest() {
        mDefaultPage = 1;
        findNearbyGarages("1", mDefaultPageSize + "");
    }
}

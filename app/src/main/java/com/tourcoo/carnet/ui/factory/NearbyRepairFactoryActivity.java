package com.tourcoo.carnet.ui.factory;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.RepairDepotDescriptionAdapter;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.NetworkUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.GarageEntity;
import com.tourcoo.carnet.entity.garage.GarageInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.repair.RepairFactoryDetailActivity;
import com.tourcoo.carnet.utils.Location;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.HttpException;

import static com.tourcoo.carnet.core.common.CommonConfig.DEBUG_MODE;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.ui.order.LookEvaluationActivity.EXTRA_ORDER_ID;
import static com.tourcoo.carnet.ui.repair.RepairFactoryDetailActivity.EXTRA_GARAGE_DETAIL;

/**
 * @author :JenkinsZhou
 * @description :附近修理厂列表
 * @company :途酷科技
 * @date 2019年04月19日9:04
 * @Email: 971613168@qq.com
 */
public class NearbyRepairFactoryActivity extends BaseTourCooTitleActivity implements EasyPermissions.PermissionCallbacks, OnRefreshListener, OnLoadMoreListener {
    private TextureMapView mapView;
    private static final int MAX_MARKER_SIZE = 10;
    /**
     * 没有更多数据足布局
     */
    private View footView;
    private AMap aMap;
    private RepairDepotDescriptionAdapter depotDescriptionAdapter;
    private String currentPosition;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int mDefaultPage = 1;
    private int mDefaultPageSize = 3;
    private Handler mHandler = new Handler();
    private StatusLayoutManager mStatusLayoutManager;
    private String orderId;
    private List<Marker> mMarkerList = new ArrayList<>();
    private List<String> positionList = new ArrayList<>();


    @Override
    public int getContentLayout() {
        return R.layout.activity_nearby_repair_factory;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("附近修理厂");
        titleBar.setRightText("重新定位");
        titleBar.setRightTextColor(TourCooUtil.getColor(R.color.blueCommon));
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

    private void initStatusManager() {
        //优先使用当前配置
        View contentView = mRecyclerView;
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.multi_empty)
                .setDefaultEmptyClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setDefaultErrorText(R.string.multi_error)
                .setDefaultErrorClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        mStatusLayoutManager.showLoadingLayout();
                        refreshRequest();
                        ToastUtil.showSuccess("点击了");
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                        mStatusLayoutManager.showLoadingLayout();
                        refreshRequest();
                        ToastUtil.showSuccess("点击了");
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        mStatusLayoutManager.showLoadingLayout();
                        refreshRequest();
                        ToastUtil.showSuccess("点击了");
                    }
                });
        mStatusLayoutManager = builder.build();
        mStatusLayoutManager.showLoadingLayout();
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        TourCooLogUtil.i(TAG, "订单id：" + orderId);
        mRefreshLayout = findViewById(R.id.smartLayout_rootFastLib);
        mRecyclerView = findViewById(R.id.rv_content);
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_no_more, null);
        initStatusManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        depotDescriptionAdapter = new RepairDepotDescriptionAdapter();
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        initItemClick();
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
        myLocationStyle.interval(10000);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //放大倍数
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        //连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
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
    public void loadData() {
        super.loadData();
        depotDescriptionAdapter.bindToRecyclerView(mRecyclerView);
        showLoadingDialog();
        getLocate();
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
                TourCooLogUtil.i("当前经纬度:" + currentPosition);
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


    private void refreshRequest() {
        mDefaultPage = 1;
        findNearbyGarages(mDefaultPage + "", mDefaultPageSize + "");
    }


    /**
     * 获取附近修理厂列表
     */
    private void findNearbyGarages(String pageIndex, String pageSize) {
        mStatusLayoutManager.showLoadingLayout();
        TourCooLogUtil.i(TAG, "当前请求的页码:" + pageIndex + "默认每页的数目:" + pageSize);
        ApiRepository.getInstance().findNearbyGarages(orderId, currentPosition, "100", pageIndex, pageSize).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                httpRequestSuccessCallback(parseGarageEntity(JSONObject.toJSONString(entity.data)));
                                addMarker();
                            } else {
                                ToastUtil.showFailed(entity.message);
                                mStatusLayoutManager.showErrorLayout();
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        handleError(e);
                        depotDescriptionAdapter.notifyDataSetChanged();
                    }
                });
    }


    private void handleError(Throwable e) {
        TourCooLogUtil.e(TAG, e.toString());
        int reason = R.string.exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtil.isConnected(CarNetApplication.getContext())) {
            reason = R.string.exception_network_not_connected;
        } else {
            //网络异常--继承于AccountsException
            if (e instanceof NetworkErrorException) {
                reason = R.string.exception_network_error;
                //账户异常
            } else if (e instanceof AccountsException) {
                reason = R.string.exception_accounts;
                //连接异常--继承于SocketException
            } else if (e instanceof ConnectException) {
                reason = R.string.exception_connect;
                //socket异常
            } else if (e instanceof SocketException) {
                reason = R.string.exception_socket;
                // http异常
            } else if (e instanceof HttpException) {
                reason = R.string.exception_http;
                //DNS错误
            } else if (e instanceof UnknownHostException) {
                reason = R.string.exception_unknown_host;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {
                //数据格式化错误
                reason = R.string.exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.exception_class_cast;
            }
        }
        if (mStatusLayoutManager == null) {
            if (DEBUG_MODE) {
                ToastUtil.show(reason);
            } else {
                ToastUtil.show("服务器异常");
            }
            return;
        }
        int page = mDefaultPage;
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(false);
        }
        if (depotDescriptionAdapter != null) {
            depotDescriptionAdapter.loadMoreComplete();
            if (mStatusLayoutManager == null) {
                return;
            }
            //初始页
            if (page == 1) {
                if (!NetworkUtil.isConnected(CarNetApplication.getContext())) {
                    //可自定义网络错误页面展示
                    mStatusLayoutManager.showErrorLayout();
//                    mStatusLayoutManager.showCustomLayout(R.layout.layout_status_layout_manager_error);
                } else {
                    mStatusLayoutManager.showErrorLayout();
                }
                return;
            }
            //可根据不同错误展示不同错误布局  showCustomLayout(R.layout.xxx);
            mStatusLayoutManager.showErrorLayout();
            mRefreshLayout.finishLoadMore(false);
            mRefreshLayout.setEnableLoadMore(false);
        }
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


    private void httpRequestSuccessCallback(GarageEntity garageEntity) {
        mRefreshLayout.finishRefresh();
        if (garageEntity == null || garageEntity.getGarageList() == null) {
            mStatusLayoutManager.showErrorLayout();
            mRefreshLayout.setEnableLoadMore(false);
            return;
        }
        mStatusLayoutManager.showSuccessLayout();
        List<GarageInfo> list = garageEntity.getGarageList();
        int page = mDefaultPage;
        int size = mDefaultPageSize;
        for (int i = 0; i < list.size(); i++) {
            positionList.add(list.get(i).getPosition());
            TourCooLogUtil.i(TAG, "修理厂位置信息:" + list.get(i).getPosition());
        }
        TourCooLogUtil.d(TAG, "位置长度:" + positionList.size());
        for (String s : positionList) {
            TourCooLogUtil.i(TAG, "修理厂位置信息position:" + s);
        }

        mRefreshLayout.finishRefresh();
        if (depotDescriptionAdapter == null) {
            return;
        }
        if (list.size() == 0) {
            if (mRefreshLayout != null) {
                mRefreshLayout.setEnableLoadMore(false);
            }
            //第一页没有
            if (page == 1) {
                depotDescriptionAdapter.setNewData(new ArrayList<>());
                mStatusLayoutManager.showEmptyLayout();
            } else {
                depotDescriptionAdapter.loadMoreEnd();
            }
            return;
        }
        mStatusLayoutManager.showSuccessLayout();
        boolean refresh = mRefreshLayout != null && (mRefreshLayout.isRefreshing() || page == 1);
        if (refresh) {
            depotDescriptionAdapter.setNewData(new ArrayList<>());
        }
        depotDescriptionAdapter.addData(list);
        depotDescriptionAdapter.loadMoreComplete();
        if (list.size() < size) {
            //没有更多了
            depotDescriptionAdapter.loadMoreEnd();
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setNoMoreData(true);
            depotDescriptionAdapter.addFooterView(footView);
        } else {
            mRefreshLayout.setEnableLoadMore(true);
        }
        mRefreshLayout.finishLoadMore(true);
    }


    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }


    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDefaultPage++;
                TourCooLogUtil.i("触发了onLoadMoreRequested:请求的页码:" + mDefaultPage);
                loadMoreRequest(mDefaultPage);
            }
        }, 1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearMarker();
                mDefaultPage = 1;
                mRefreshLayout.setEnableLoadMore(true);
                mRefreshLayout.setNoMoreData(false);
                depotDescriptionAdapter.removeFooterView(footView);
                positionList.clear();
                refreshRequest();
            }
        }, 1000);
    }


    private void loadMoreRequest(int page) {
        mStatusLayoutManager.showLoadingLayout();
        TourCooLogUtil.i(TAG, "当前请求的页码:" + page + "---当前每页请求数量:" + page);
        findNearbyGarages(page + "", mDefaultPageSize + "");
    }


    @SuppressWarnings("unchecked")
    private void initItemClick() {
        depotDescriptionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<GarageInfo> factoryEntityList = (List<GarageInfo>) adapter.getData();
                Intent intent = new Intent();
                GarageInfo garageInfo = factoryEntityList.get(position);
                intent.putExtra(EXTRA_GARAGE_DETAIL, garageInfo);
                intent.setClass(mContext, RepairFactoryDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 将POI点以不同颜色的标识展现在高德地图上
     */
    private void addMarker() {
        if (aMap == null) {
            return;
        }
        List<Marker> markerList = aMap.getMapScreenMarkers();
        if (markerList != null) {
            if (markerList.size() > MAX_MARKER_SIZE) {
                return;
            }
        }
        try {
            List<Location> locationList = new ArrayList<>();
            Location location;
            for (String position : positionList) {
                if (TextUtils.isEmpty(position)) {
                    continue;
                }
                location = parseLocation(position);
                if (location != null) {
                    locationList.add(location);
                }
            }
            for (Location locate : locationList) {
                Marker marker = aMap.addMarker(createMarkerOption(locate));
                mMarkerList.add(marker);
                marker.showInfoWindow();
            }

        } catch (Exception e) {
            TourCooLogUtil.i(TAG, "标记异常:" + e.toString());
        }


    }


    private Location parseLocation(String position) {
        if (TextUtils.isEmpty(position)) {
            return null;
        }
        String[] locationArray = position.split(",");
        if (locationArray.length != 2) {
            return null;
        }
        Location location = new Location();
        double lat = 0;
        double lang = 0;
        try {
            lang = Double.parseDouble(locationArray[1]);
            //纬度
            lat = Double.parseDouble(locationArray[0]);
        } catch (NumberFormatException e) {
            TourCooLogUtil.i(TAG, "经纬度转换异常");
        }
        location.setLatitude(lang);
        location.setLongitude(lat);
        return location;
    }


    private MarkerOptions createMarkerOption(Location location) {
        MarkerOptions markerOption = new MarkerOptions();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

//        markerOption.title("修理厂").snippet("xxx修理厂");

        markerOption.draggable(false);
        markerOption.position(latLng);
        markerOption.visible(true);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.ic_marker)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        //设置marker平贴地图效果
        markerOption.setFlat(true);
        return markerOption;
    }


    private void clearMarker() {
        Marker marker;
        for (int i = mMarkerList.size() - 1; i >= 0; i--) {
            marker = mMarkerList.get(i);
            if (marker != null) {
                marker.remove();
                mMarkerList.remove(i);
            }
        }
    }
}



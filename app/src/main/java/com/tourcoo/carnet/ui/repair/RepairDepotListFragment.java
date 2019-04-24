package com.tourcoo.carnet.ui.repair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.RepairDepotDescriptionAdapter;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.fragment.BaseRefreshFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.GarageEntity;
import com.tourcoo.carnet.entity.garage.GarageInfo;
import com.tourcoo.carnet.entity.event.SearchEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.entity.event.SearchEvent.EVENT_ACTION_SEARCH_GARAGE;
import static com.tourcoo.carnet.ui.repair.RepairFactoryDetailActivity.EXTRA_GARAGE_DETAIL;

/**
 * @author :zhoujian
 * @description :全部修理厂列表
 * @company :翼迈科技
 * @date 2019年 03月 18日 23时05分
 * @Email: 971613168@qq.com
 */
public class RepairDepotListFragment extends BaseRefreshFragment<GarageInfo> implements EasyPermissions.PermissionCallbacks {
    private RepairDepotDescriptionAdapter depotDescriptionAdapter;
    private String currentPosition = "";
    private String keyWord = "";

    private FastSearchActivity fastSearchActivity;

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fastSearchActivity = (FastSearchActivity) mContext;
        initItemClick();
        EventBus.getDefault().register(mContext);
        getLocateAndRequest();
    }

    @Override
    public BaseQuickAdapter<GarageInfo, BaseViewHolder> getAdapter() {
        depotDescriptionAdapter = new RepairDepotDescriptionAdapter();
        return depotDescriptionAdapter;
    }

    @Override
    public void loadData(int page) {
        if (TextUtils.isEmpty(currentPosition)) {
            mStatusManager.showLoadingLayout();
        } else {
            searchGarages(String.valueOf(page), String.valueOf(mDefaultPageSize));
        }

    }

    @Override
    public boolean isRefreshEnable() {
        return true;
    }


    public static RepairDepotListFragment newInstance() {
        Bundle args = new Bundle();
        RepairDepotListFragment fragment = new RepairDepotListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mDefaultPage = 1;
        super.onRefresh(refreshlayout);
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
    private void searchGarages(String pageIndex, String pageSize) {
        keyWord = fastSearchActivity.getInput();
        TourCooLogUtil.i("当前请求的页码：", pageIndex + "关键字:" + keyWord);
        ApiRepository.getInstance().searchGarages(currentPosition, keyWord, pageIndex, pageSize).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<GarageEntity>>(getIHttpRequestControl()) {
                    @Override
                    public void onRequestNext(BaseEntity<GarageEntity> entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                GarageEntity garageEntity = entity.data;
                                if (garageEntity != null) {
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


    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void onSearch(SearchEvent searchEvent) {
        if (searchEvent != null && searchEvent.type == EVENT_ACTION_SEARCH_GARAGE) {
            keyWord = searchEvent.getKeyWord();
            depotDescriptionAdapter.getData().clear();
            requestByPosition();
        }

    }

    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void clearInput(int clear) {
        if (clear == EVENT_ACTION_SEARCH_GARAGE) {
            keyWord = "";
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(mContext);
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
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


    private void getLocateAndRequest() {
        if (checkLocatePermission()) {
            locateAndRequest();
        } else {
            showPermissionDialog("请前往权限管理授予定位权限");
        }
    }

    /**
     * 显示权限弹窗
     *
     * @param msg
     */
    private void showPermissionDialog(String msg) {
        showAlertDialog("需要定位权限", msg, "我知道了");
    }


    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(mContext, PermissionConstance.PERMS_LOCATE);
    }

    /***
     * 获取位置信息，位置获取成功后请求修理厂接口(在有权限的情况下)
     */
    private void locateAndRequest() {
        showLoadingDialog();
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LocateHelper.getInstance().stopLocation();
                closeLoadingDialog();
                if (aMapLocation != null) {
                    currentPosition = getPosition(aMapLocation);
                    requestByPosition();
                } else {
                    ToastUtil.showFailed("定位失败");
                }
            }
        });
    }


    /**
     * 在已经获取位置信息的情况下请求修理厂数据
     */
    private void requestByPosition() {
        if (TextUtils.isEmpty(currentPosition)) {
            ToastUtil.showFailed("未获取到位置信息");
            mStatusManager.showEmptyLayout();
            return;
        }
        mStatusManager.showSuccessLayout();
        refreshRequest();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        getLocateAndRequest();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
    }

    private void refreshRequest() {
        mDefaultPage = 1;
        depotDescriptionAdapter.getData().clear();
        searchGarages(String.valueOf(1), String.valueOf(mDefaultPageSize));
    }



}

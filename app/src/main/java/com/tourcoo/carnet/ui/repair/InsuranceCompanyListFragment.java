package com.tourcoo.carnet.ui.repair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.InsuranceCompanyDescriptionAdapter;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.fragment.BaseRefreshFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.NetworkUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.InsuranceCompany;
import com.tourcoo.carnet.entity.event.SearchEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.utils.Location;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.entity.event.SearchEvent.EVENT_ACTION_SEARCH_INSURANCE_COMPANY;
import static com.tourcoo.carnet.ui.repair.InsuranceCompanyDetailActivity.EXTRA_COMPANY_ID;

/**
 * @author :JenkinsZhou
 * @description :保险公司列表
 * @company :途酷科技
 * @date 2019年03月18日17:04
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyListFragment extends BaseRefreshFragment<InsuranceCompany.CompanyInfo> implements EasyPermissions.PermissionCallbacks {
    private InsuranceCompanyDescriptionAdapter descriptionAdapter;
    private String keyWord;

    private Location mLocation;

    private FastSearchActivity fastSearchActivity;

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fastSearchActivity = (FastSearchActivity) mContext;
        getLocate();
        mDefaultPageSize = 5;
        if (NetworkUtil.isConnected(mContext)) {
            mRefreshLoadDelegate.mStatusManager.showSuccessLayout();
        } else {
            mRefreshLoadDelegate.mStatusManager.showErrorLayout();
        }
        initItemClick();
    }

    @Override
    public BaseQuickAdapter<InsuranceCompany.CompanyInfo, BaseViewHolder> getAdapter() {
        descriptionAdapter = new InsuranceCompanyDescriptionAdapter();
        return descriptionAdapter;
    }

    @Override
    public void loadData(int page) {
        findAllInsurance(page);
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        mDefaultPage = 1;
    }

    @Override
    public void onLoadMoreRequested() {
        super.onLoadMoreRequested();
        TourCooLogUtil.d("onLoadMoreRequested:");
    }


    public static InsuranceCompanyListFragment newInstance() {
        Bundle args = new Bundle();
        InsuranceCompanyListFragment fragment = new InsuranceCompanyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isRefreshEnable() {
        return true;
    }


    private void findAllInsurance(int pageIndex) {
        if (mLocation == null) {
            locate();
            return;
        }
        if (pageIndex == 1) {
            descriptionAdapter.getData().clear();
        }
        keyWord = fastSearchActivity.getInput();
        TourCooLogUtil.d("onLoadMoreRequested:", "当前关键字:" + keyWord);
        ApiRepository.getInstance().queryAllInsurance(mLocation, keyWord, pageIndex, mDefaultPageSize).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<InsuranceCompany>>() {
                    @Override
                    public void onRequestNext(BaseEntity<InsuranceCompany> entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                InsuranceCompany company = entity.data;
                                if (company != null) {
                                    UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), company.getElements() == null ? new ArrayList<>() : company.getElements(), null);
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        closeLoadingDialog();
                        mRefreshLayout.finishRefresh(false);
                        mRefreshLayout.finishLoadMore(false);
                    }
                });

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
        closeLoadingDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(getActivity(), PermissionConstance.PERMS_LOCATE);
    }

    /**
     * 获取当前位置
     */
    private void getLocate() {
        if (checkLocatePermission()) {
            locate();
        } else {
            showPermissionDialog("请前往权限管理授予定位权限");
//            PermissionManager.requestPermission(InsuranceCompanyFragment.this, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
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
     * 定位并请求数据
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //定位回调
                closeLoadingDialog();
                LocateHelper.getInstance().stopLocation();
                if (aMapLocation != null) {
                    mLocation = new Location();
                    mLocation.setLongitude(aMapLocation.getLongitude());
                    mLocation.setLatitude(aMapLocation.getLatitude());
                    //获取到位置信息后再请求接口
                    refreshRequest();
                } else {
                    mStatusManager.showErrorLayout();
                }
            }
        });
    }


    private void refreshRequest() {
        mDefaultPage = 1;
        findAllInsurance(mDefaultPage);
    }


    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void onSearch(SearchEvent searchEvent) {
        if (searchEvent != null && searchEvent.type == EVENT_ACTION_SEARCH_INSURANCE_COMPANY) {
            keyWord = searchEvent.getKeyWord();
            descriptionAdapter.getData().clear();
            refreshRequest();
        }

    }

    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void clearInput(int clear) {
        if (clear == EVENT_ACTION_SEARCH_INSURANCE_COMPANY) {
            keyWord = "";
        }
    }


    private void initItemClick() {
        descriptionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_COMPANY_ID, descriptionAdapter.getData().get(position).getId());
                TourCooLogUtil.i(TAG, "EXTRA_COMPANY_ID:" + descriptionAdapter.getData().get(position).getId());
                TourCooUtil.startActivity(mContext, InsuranceCompanyDetailActivity.class, bundle);
            }
        });
    }
}

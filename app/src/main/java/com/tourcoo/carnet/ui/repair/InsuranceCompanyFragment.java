package com.tourcoo.carnet.ui.repair;

import android.os.Bundle;
import android.os.Handler;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.InsuranceCompanyDescriptionAdapter;
import com.tourcoo.carnet.core.frame.base.fragment.BaseRefreshFragment;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.entity.InsuranceCompanyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :保险公司列表
 * @company :途酷科技
 * @date 2019年03月18日17:04
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyFragment extends BaseRefreshFragment<InsuranceCompanyEntity> {
    private InsuranceCompanyDescriptionAdapter descriptionAdapter;
    private List<InsuranceCompanyEntity> companyEntityList = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        loadCompany();
        mRefreshLoadDelegate.mRefreshLayout.finishRefresh();
        mRefreshLoadDelegate.mStatusManager.showSuccessLayout();
    }

    @Override
    public InsuranceCompanyDescriptionAdapter getAdapter() {
        descriptionAdapter = new InsuranceCompanyDescriptionAdapter(companyEntityList);
        return descriptionAdapter;
    }

    @Override
    public void loadData(int page) {
        TourCooLogUtil.d(TAG, "page = " + page);
    }

    @Override
    public void onLoadMoreRequested() {
        TourCooLogUtil.d(TAG, "已执行");
        new Handler() {
        }.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishLoadMore();
            }
        }, 2000);

    }

    private void loadCompany() {
        int i = 10;
        for (int i1 = 0; i1 < i; i1++) {
            InsuranceCompanyEntity companyEntity = new InsuranceCompanyEntity();
            companyEntity.companyAddress = "经开区繁华大道与九龙路交口 高速翡翠湖畔";
            companyEntity.companyDistance = "1.5km";
            companyEntity.companyName = "太平洋保险有限公司";
            companyEntity.companyPhone = "电话:0551-2568956";
            companyEntityList.add(companyEntity);
        }
    }

    public static InsuranceCompanyFragment newInstance() {
        Bundle args = new Bundle();
        InsuranceCompanyFragment fragment = new InsuranceCompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isRefreshEnable() {
        return true;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishRefresh();
            }
        }, 1000);
    }
}

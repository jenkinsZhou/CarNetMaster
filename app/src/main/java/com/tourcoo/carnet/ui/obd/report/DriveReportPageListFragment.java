package com.tourcoo.carnet.ui.obd.report;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.obd.DriveReportInfoAdapter;
import com.tourcoo.carnet.core.frame.base.fragment.BaseRefreshFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.obd.DriveReportInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.FragmentEvent;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :行驶报告分页
 * @company :途酷科技
 * @date 2019年06月13日16:29
 * @Email: 971613168@qq.com
 */
public class DriveReportPageListFragment extends BaseRefreshFragment<DriveReportInfo> {
    private DriveReportInfoAdapter mAdapter;

    private int carId;

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        carId = 88;
    }

    @Override
    public DriveReportInfoAdapter getAdapter() {
        mAdapter = new DriveReportInfoAdapter();
        return mAdapter;
    }

    @Override
    public void loadData(int page) {
        requestDriveReportDaily(page);
    }

    /**
     * 日常报告
     *
     * @param page
     */
    private void requestDriveReportDaily(int page) {
        if (page < 1) {
            page = 1;
        }
        ApiRepository.getInstance().requestDriveTripReportPage(carId, page).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    public static DriveReportPageListFragment newInstance() {
        Bundle args = new Bundle();
        DriveReportPageListFragment fragment = new DriveReportPageListFragment();
        fragment.setArguments(args);
        return fragment;
    }


}

package com.tourcoo.carnet.ui.obd.report;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.fragment.BaseTitleFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.FragmentEvent;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :日报告
 * @company :途酷科技
 * @date 2019年06月13日15:12
 * @Email: 971613168@qq.com
 */
public class DailyReportFragment extends BaseTitleFragment {
    private int carId;

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void loadData() {
        super.loadData();
        carId=  88;
        requestDriveReportDaily(carId);
    }

    private void requestDriveReportDaily(int carId) {
        ApiRepository.getInstance().requestDriveReportDaily(carId).compose(bindUntilEvent(FragmentEvent.DESTROY)).
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


    public static DailyReportFragment newInstance() {
        Bundle args = new Bundle();
        DailyReportFragment fragment = new DailyReportFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

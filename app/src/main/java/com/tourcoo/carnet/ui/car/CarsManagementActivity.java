package com.tourcoo.carnet.ui.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.CarInfoAdapter;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.activity.BaseRefreshLoadActivity;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.entity.event.CarRefreshEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :zhoujian
 * @description :车辆管理列表
 * @company :翼迈科技
 * @date 2019年 03月 16日 20时31分
 * @Email: 971613168@qq.com
 */
public class CarsManagementActivity extends BaseRefreshLoadActivity {
    private CarInfoAdapter mCarInfoAdapter;
    public static final int REQUEST_CODE_ADD_CAR = 100;
    public static final int REQUEST_CODE_DELETE_CAR = 101;

    @Override
    public int getContentLayout() {
        return R.layout.activity_car_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initItemClick();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setRightText("添加车辆");
        titleBar.setRightTextColor(ContextCompat.getColor(mContext, R.color.blueCommon));
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, AddCarActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_CAR);
            }
        });
        titleBar.setTitleMainText("车辆管理");
    }

    @Override
    public void loadData() {
        super.loadData();
        getMyCarInfoList();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getMyCarInfoList();
    }

    private void getMyCarInfoList() {
        mCarInfoAdapter.getData().clear();
        mStatusManager.showSuccessLayout();
        ApiRepository.getInstance().findMyCars().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        mRefreshLayout.finishRefresh();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                List<CarInfoEntity> parseCarInfoList = parseCarInfoList(entity.data);
                                AccountInfoHelper.getInstance().setCarInfoEntityList(parseCarInfoList);
                                if (parseCarInfoList.isEmpty()) {
                                    mStatusManager.showEmptyLayout();
                                }
                                UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), parseCarInfoList, null);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        TourcooLogUtil.e(TAG, "请求异常:" + e.toString());
                        mStatusManager.showErrorLayout();
                        mRefreshLayout.finishRefresh();

                    }
                });
    }


    private List<CarInfoEntity> parseCarInfoList(Object jsonArrayStr) {
        try {
            return JSONObject.parseArray(JSON.toJSONString(jsonArrayStr), CarInfoEntity.class);
        } catch (Exception e) {
            TourcooLogUtil.e(TAG, "解析失败:" + e.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        mCarInfoAdapter = new CarInfoAdapter();
        return mCarInfoAdapter;
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    public boolean isLoadMoreEnable() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD_CAR:
                if (resultCode == RESULT_OK) {
                    getMyCarInfoList();
                    EventBus.getDefault().post(new CarRefreshEvent());
                }
                break;
            case REQUEST_CODE_DELETE_CAR:
                if (resultCode == RESULT_OK) {
                    getMyCarInfoList();
                    EventBus.getDefault().post(new CarRefreshEvent());
                }
                break;
            default:
                break;
        }
    }


    private void initItemClick() {
        mCarInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CarInfoEntity carInfoEntity = mCarInfoAdapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("CarInfoEntity", carInfoEntity);
                intent.setClass(mContext, CarEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DELETE_CAR);
            }
        });
    }
}

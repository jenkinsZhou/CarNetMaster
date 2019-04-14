package com.tourcoo.carnet.ui.doortodoor;

import android.os.Bundle;
import android.view.View;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.fragment.BaseTitleFragment;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.ui.car.CarCuringActivity;
import com.tourcoo.carnet.ui.car.CarRepairActivity;
import com.tourcoo.carnet.ui.car.CarWashActivity;

/**
 * @author :JenkinsZhou
 * @description :上门服务
 * @company :翼迈科技
 * @date 2019年03月15日16:47
 * @Email: 971613168@qq.com
 */
public class DoorToDoorServiceFragment extends BaseTitleFragment implements View.OnClickListener {


    @Override

    public int getContentLayout() {
        return R.layout.fragment_door_to_door_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.rlCarWash).setOnClickListener(this);
        mContentView.findViewById(R.id.rlCarCuring).setOnClickListener(this);
        mContentView.findViewById(R.id.rlCarRepair).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("上门服务");
    }

    public static DoorToDoorServiceFragment newInstance() {
        Bundle args = new Bundle();
        DoorToDoorServiceFragment fragment = new DoorToDoorServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCarWash:
                TourcooUtil.startActivity(mContext, CarWashActivity.class);
                break;
            case R.id.rlCarCuring:
                TourcooUtil.startActivity(mContext, CarCuringActivity.class);
                break;
            case R.id.rlCarRepair:
                TourcooUtil.startActivity(mContext, CarRepairActivity.class);
                break;
            default:
                break;
        }
    }
}

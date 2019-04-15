package com.tourcoo.carnet.core.module;

import android.os.Bundle;
import android.text.TextUtils;

import com.aries.ui.view.tab.CommonTabLayout;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseMainActivity;
import com.tourcoo.carnet.core.frame.entity.TabEntity;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.helper.CheckVersionHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.doortodoor.DoorToDoorServiceFragment;
import com.tourcoo.carnet.ui.repair.RepairFaultFragment;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :zhoujian
 * @description : zj
 * @company :翼迈科技
 * @date 2019年03月06日上午 10:07
 * @Email: 971613168@qq.com
 */
public class MainTabActivity extends BaseMainActivity {
    String clientRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public List<TabEntity> getTabList() {
        ArrayList<TabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("首页", R.mipmap.ic_tab_home_normal, R.mipmap.ic_tab_home_selected, HomeFragment.newInstance()));
        tabEntities.add(new TabEntity("故障报修", R.mipmap.ic_tab_malfunction_service_normal, R.mipmap.ic_tab_malfunction_service_selected, RepairFaultFragment.newInstance()));
        tabEntities.add(new TabEntity("上门服务", R.mipmap.ic_tab_onsite_service_normal, R.mipmap.ic_tab_onsite_service_selected, DoorToDoorServiceFragment.newInstance()));
        tabEntities.add(new TabEntity("个人中心", R.mipmap.ic_tab_personal_center_normal, R.mipmap.ic_tab_personal_center_selected, MineFragment.newInstance()));
        return tabEntities;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        super.beforeInitView(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
      /*  CheckVersionHelper.with(this)
                .checkVersion(false);*/
        clientRegId = JPushInterface.getRegistrationID(this);
        TourcooLogUtil.i(TAG,"clientRegId："+clientRegId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TourcooLogUtil.i(TAG, "onDestroy");
    }


    private void uploadClientId(String clientId, String ownerId) {
        ApiRepository.getInstance().uploadClientId(clientId, ownerId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code != CODE_REQUEST_SUCCESS) {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    @Override
    public void loadData() {
        super.loadData();
        if (TextUtils.isEmpty(clientRegId)) {
            ToastUtil.show("未获取到设备信息");
            return;
        }
        UserInfoEntity userInfoEntity = AccountInfoHelper.getInstance().getUserInfoEntity();
        if (userInfoEntity == null || userInfoEntity.getUserInfo() == null) {
            ToastUtil.show("未获取到用户信息");
            return;
        }
        uploadClientId(clientRegId, userInfoEntity.getUserInfo().getId()+"");
    }
}

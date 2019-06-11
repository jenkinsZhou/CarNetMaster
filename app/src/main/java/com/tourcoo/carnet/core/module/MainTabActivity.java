package com.tourcoo.carnet.core.module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.aries.ui.view.tab.CommonTabLayout;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseMainActivity;
import com.tourcoo.carnet.core.frame.entity.TabEntity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.dialog.update.BaseUpdateDialog;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.update.VersionEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.account.LoginRegisterActivity;
import com.tourcoo.carnet.ui.doortodoor.DoorToDoorServiceFragment;
import com.tourcoo.carnet.ui.repair.RepairFaultFragment;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.tourcoo.carnet.core.common.CommonConstant.TOKEN_INVALID;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :zhoujian
 * @description :
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
        TourCooLogUtil.i(TAG, "clientRegId：" + clientRegId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TourCooLogUtil.i(TAG, "onDestroy");
    }


    private void uploadClientId(String clientId, String ownerId) {
        ApiRepository.getInstance().uploadClientId(clientId, ownerId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code != CODE_REQUEST_SUCCESS) {
                                if (TOKEN_INVALID.equals(entity.message)) {
                                    ToastUtil.showFailed(entity.message);
                                    TourCooUtil.startActivity(mContext, LoginRegisterActivity.class);
                                    finish();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void loadData() {
        super.loadData();
        if (NetworkUtils.isConnected()) {
            //如果网络可用 则检查更新
            checkUpdateDelay();
        }
        if (TextUtils.isEmpty(clientRegId)) {
            ToastUtil.show("未获取到设备信息");
            return;
        }
        UserInfoEntity userInfoEntity = AccountInfoHelper.getInstance().getUserInfoEntity();
        if (userInfoEntity == null || userInfoEntity.getUserInfo() == null) {
            TourCooLogUtil.e(TAG, TAG + ":未获取到用户信息");
            return;
        }

        uploadClientId(clientRegId, userInfoEntity.getUserInfo().getUserId() + "");
    }


    /**
     * 检测版本信息
     */
    private void requestAppVersionInfo() {
        ApiRepository.getInstance().requestAppVersionInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity == null || entity.data == null) {
                            ToastUtil.show("当前已经是最新版本");
                            return;
                        }
                        VersionEntity versionEntity = parseVersionInfo(entity.data);
                        //检测更新
                        checkUpdate(versionEntity);
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                    }
                });
    }


    /**
     * 判断是否需要更新
     *
     * @param versionEntity
     */
    /**
     * 判断是否需要更新
     *
     * @param versionEntity
     */
    private void checkUpdate(VersionEntity versionEntity) {
        if (versionEntity == null) {
            return;
        }
        if (TourCooUtil.getVersionCode() >= versionEntity.getLastCode()) {
            //表示已经是最新版本 无需更新 什么也不做
        } else {
            //需要版本更新 直接更新下载
            String downloadUrl = TourCooUtil.getUrl(versionEntity.getDownloadUrl());
            TourCooLogUtil.d(TAG, TAG + ":" + "下载的链接:" + downloadUrl);
            updateVersion(downloadUrl, versionEntity.getTitle(), versionEntity.getContent(), versionEntity.isForceUpdate());
        }
    }


    public void updateVersion(String downloadUrl,String title,String content, boolean isForce) {
        try {
            DownloadBuilder builder = AllenVersionChecker.getInstance()
                    .downloadOnly(
                            UIData.create().setDownloadUrl(downloadUrl).setTitle(title).setContent(content)
                    );

            if (isForce) {
//        强制更新 取消回调
                builder.setForceUpdateListener(new ForceUpdateListener() {
                    @Override
                    public void onShouldForceUpdate() {
                        ActivityUtils.finishAllActivities();
                    }
                });
            }
            //静默下载
            builder.setSilentDownload(false);
            //如果本地有安装包缓存也会重新下载apk
            builder.setForceRedownload(true);
            //更新界面选择
            builder.setCustomVersionDialogListener(createCustomDialogOne());
            //自定义下载路径
            builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/CarNetMaster/download/");
            builder.executeMission(CarNetApplication.sContext);
        } catch (Exception e) {
            ToastUtil.showFailed("下载地址有误");
            TourCooLogUtil.e(TAG, TAG + "更新异常:" + e.toString());
        }

    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
    private CustomVersionDialogListener createCustomDialogOne() {
        CustomVersionDialogListener listener = new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseUpdateDialog baseDialog = new BaseUpdateDialog(context, R.style.UpdateDialog, R.layout.custom_dialog_one_layout);
                TextView textView = baseDialog.findViewById(R.id.tv_msg);
                TextView tvTitle = baseDialog.findViewById(R.id.tv_title);
                LinearLayout llUpdateContent = baseDialog.findViewById(R.id.llUpdateContent);
                llUpdateContent.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
                textView.setText(versionBundle.getContent());
                tvTitle.setText(versionBundle.getTitle());
                return baseDialog;
            }
        };
        return listener;
    }


    private void checkUpdateDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestAppVersionInfo();
            }
        }, 300);
    }

    private VersionEntity parseVersionInfo(Object jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return JSON.parseObject(JSON.toJSONString(jsonStr), VersionEntity.class);
        } catch (Exception e) {
            TourCooLogUtil.e(TAG, "错误" + e.toString());
            return null;
        }
    }


}

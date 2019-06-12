package com.tourcoo.carnet.ui.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.ui.DownloadingActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.dialog.update.BaseUpdateDialog;
import com.tourcoo.carnet.core.widget.dialog.update.UpdateDownloadingDialog;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.update.VersionEntity;
import com.tourcoo.carnet.obd.report.DrivingReportActivity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcoo.carnet.AccountInfoHelper.PREF_TEL_PHONE_KEY;

/**
 * @author :zhoujian
 * @description :关于我们
 * @company :翼迈科技
 * @date 2019年 03月 17日 10时15分
 * @Email: 971613168@qq.com
 */
public class AboutUsActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private String phone;
    private SuperTextView stvPhoneNumber;
    private TextView tvAppVersion;

    public static final String TIPS_IS_THE_LATEST_VERSION = "当前已经是最新版本";

    @Override
    public int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.stvAppVersion).setOnClickListener(this);
        findViewById(R.id.stvPhoneNumber).setOnClickListener(this);
        findViewById(R.id.stvDrivingReport).setOnClickListener(this);
        findViewById(R.id.stvAppVersion).setOnClickListener(this);
        tvAppVersion = findViewById(R.id.tvAppVersion);

        tvAppVersion.setOnClickListener(this);
        stvPhoneNumber = findViewById(R.id.stvPhoneNumber);
        phone = (String) SharedPreferencesUtil.get(PREF_TEL_PHONE_KEY, "");
        showPhone(phone);
        showAppVersion();
    }

    @Override
    public void loadData() {
        super.loadData();
        getServicePhone();
    }

    private void showPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        stvPhoneNumber.setRightString(phone);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("关于我们");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stvAppVersion:
                requestAppVersionInfo();
                break;
            case R.id.stvPhoneNumber:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show("未获取到热线号码");
                    return;
                }
                call(phone);
                break;
            case R.id.stvDrivingReport:
                TourCooUtil.startActivity(mContext, DrivingReportActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getServicePhone() {
        ApiRepository.getInstance().getServicePhone().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<String>() {
                    @Override
                    public void onRequestNext(String entity) {
                        closeLoadingDialog();
                        if (entity != null && entity.length() != 0) {
                            phone = entity;
                            SharedPreferencesUtil.put(PREF_TEL_PHONE_KEY, phone);
                            showPhone(phone);
//                                ToastUtil.showFailed(entity.message);
                        }
                    }
                });
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
    private void checkUpdate(VersionEntity versionEntity) {
        if (versionEntity == null) {
            ToastUtil.show(TIPS_IS_THE_LATEST_VERSION);
            return;
        }
        if (TourCooUtil.getVersionCode() >= versionEntity.getLastCode()) {
            //表示已经是最新版本 无需更新
            ToastUtil.show(TIPS_IS_THE_LATEST_VERSION);
        } else {
            //需要版本更新 直接更新下载
            String downloadUrl = TourCooUtil.getUrl(versionEntity.getDownloadUrl());
            TourCooLogUtil.e(TAG, TAG + ":" + "下载的链接:" + downloadUrl);
            updateVersion(downloadUrl, versionEntity.getTitle(), versionEntity.getContent(), versionEntity.isForceUpdate());
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
                LinearLayout llUpdateContent = baseDialog.findViewById(R.id.llUpdateContent);
                llUpdateContent.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
                textView.setText(versionBundle.getContent());
                return baseDialog;
            }
        };
        return listener;
    }


    public void updateVersion(String downloadUrl, String title, String content, boolean isForce) {
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

            builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
            builder.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel() {
                    releaseDownloading();
                }
            });
            builder.setCustomVersionDialogListener(createCustomDialogOne());
            //自定义下载路径
            builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/CarNetMaster/download/");
            builder.executeMission(CarNetApplication.sContext);
        } catch (Exception e) {
            ToastUtil.showFailed("下载地址有误");
            TourCooLogUtil.e(TAG, TAG + "更新异常:" + e.toString());
        }
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


    private void showAppVersion() {
        String versionName = "V " + TourCooUtil.getVersionName(mContext);
        tvAppVersion.setText(versionName);
    }


    private CustomDownloadingDialogListener createCustomDownloadingDialog() {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                return new UpdateDownloadingDialog(context, R.style.UpdateDialog, R.layout.custom_dialog_downloading_layout);
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                ((UpdateDownloadingDialog) dialog).setProgressWithTip(progress);
            }
        };
    }


    private void releaseDownloading() {
        Activity activity = StackUtil.getInstance().getActivity(DownloadingActivity.class);
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    protected void onDestroy() {
        releaseDownloading();
        super.onDestroy();
    }
}

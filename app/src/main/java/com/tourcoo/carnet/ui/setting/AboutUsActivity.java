package com.tourcoo.carnet.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.allen.library.SuperTextView;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.helper.CheckVersionHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.ServiceInfo;
import com.tourcoo.carnet.obd.report.DrivingReportActivity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import static com.tourcoo.carnet.AccountInfoHelper.PREF_TEL_PHONE_KEY;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

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

    @Override
    public int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.stvAppVersion).setOnClickListener(this);
        findViewById(R.id.stvPhoneNumber).setOnClickListener(this);
        findViewById(R.id.stvDrivingReport).setOnClickListener(this);
        stvPhoneNumber = findViewById(R.id.stvPhoneNumber);
        phone = (String) SharedPreferencesUtil.get(PREF_TEL_PHONE_KEY, "");
        showPhone(phone);
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
                CheckVersionHelper.with(this).checkVersion(true);
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
}

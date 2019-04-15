package com.tourcoo.carnet.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :更换手机号
 * @company :途酷科技
 * @date 2019年04月15日16:47
 * @Email: 971613168@qq.com
 */
public class ChangePhoneNumberActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private EditText etNewPhone;
    private EditText etVCode;
    private TextView tvSendVerificationCode;
    private List<Disposable> disposableList = new ArrayList<>();
    private static final long SECOND = 1000;
    private static final int COUNT = 30;
    private int count = COUNT;

    @Override
    public int getContentLayout() {
        return R.layout.activity_change_new_phone;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etNewPhone = findViewById(R.id.etNewPhone);
        tvSendVerificationCode = findViewById(R.id.tvSendVerificationCode);
        tvSendVerificationCode.setOnClickListener(this);
        etVCode = findViewById(R.id.etVCode);
        findViewById(R.id.tvBindConfirm).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("修改手机号");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendVerificationCode:
                sendVCodeAndCountDownTime(getMobile());
                break;
            case R.id.tvBindConfirm:
                doChangeMobile(getMobile(), getVCode());
                break;
            default:
                break;
        }
    }

    private String getMobile() {

        return etNewPhone.getText().toString();
    }

    private void setClickEnable(boolean clickEnable) {
        tvSendVerificationCode.setEnabled(clickEnable);
    }

    private void setText(String text) {
        tvSendVerificationCode.setText(text);
    }

    private void reset() {
        setClickEnable(true);
        count = COUNT;
        setText("发送验证码");
    }


    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private void sendVCodeAndCountDownTime(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TourcooUtil.isMobileNumber(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        ApiRepository.getInstance().getVcode(phone).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.code == CODE_REQUEST_SUCCESS) {
                            ToastUtil.showSuccess(entity.message);
                            //验证码发送成功开始，倒计时
                            countDownTime();
                        } else {
                            ToastUtil.showFailed(entity.message);
                        }
                    }
                });
    }

    /**
     * 倒计时
     */
    private void countDownTime() {
        reset();
        setClickEnable(false);
        RxJavaManager.getInstance().doEventByInterval(SECOND, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }


            @Override
            public void onNext(Long aLong) {
                --count;
                setText("还有" + count + "秒");
                if (aLong >= COUNT - 1 || count < 0) {
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                cancelTime();
            }

            @Override
            public void onComplete() {
                reset();
                cancelTime();
            }
        });
    }

    private String getVCode() {
        return etVCode.getText().toString();
    }


    @Override
    protected void onDestroy() {
        cancelTime();
        super.onDestroy();
    }


    private void cancelTime() {
        if (disposableList != null && !disposableList.isEmpty()) {
            Disposable disposable;
            for (int i = 0; i < disposableList.size(); i++) {
                disposable = disposableList.get(i);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposableList.remove(disposable);
                }
            }

        }
    }


    /**
     * 更换手机号
     *
     * @param mobile
     * @param vCode
     */
    private void doChangeMobile(String mobile, String vCode) {
        if (!TourcooUtil.isMobileNumber(mobile)) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(vCode)) {
            ToastUtil.show("请输入验证码");
            return;
        }
        ApiRepository.getInstance().changeMobile(mobile, vCode).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (AccountInfoHelper.getInstance().isRemindPassword()) {
                                    AccountInfoHelper.getInstance().changeMobile(getMobile());
                                }
                                TourcooUtil.startActivity(mContext, EditSuccessActivity.class);
                                finish();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }
}

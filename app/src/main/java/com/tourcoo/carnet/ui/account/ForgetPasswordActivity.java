package com.tourcoo.carnet.ui.account;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.helper.TitleBarViewHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :忘记密码
 * @company :途酷科技
 * @date 2019年04月17日9:28
 * @Email: 971613168@qq.com
 */
public class ForgetPasswordActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private EditText etPhoneNumber;
    private EditText etVcode;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private TextView tvSendVerificationCode;
    private List<Disposable> disposableList = new ArrayList<>();
    private static final long SECOND = 1000;
    private static final int COUNT = 60;
    private int count = COUNT;
    private TitleBarViewHelper mTitleBarViewHelper;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setRightTextDrawable(R.drawable.ic_transition)
                .setDividerVisible(false)
                .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102)
                .setStatusAlpha(0)
                .setVisibility(View.GONE);
        StatusBarUtil.setStatusBarLightMode(this);
        titleBar.setLeftTextColor(Color.WHITE)
                .setBgColor(Color.WHITE);
        int mMaxHeight = CarNetApplication.getImageHeight() - StatusBarUtil.getStatusBarHeight() - getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        mTitleBarViewHelper = new TitleBarViewHelper(mContext)
                .setTitleBarView(mTitleBar)
                .setMinHeight(0)
                .setMaxHeight(mMaxHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendVerificationCode:
                if (TextUtils.isEmpty(getPhoneNumber())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
                    ToastUtil.show("请输入正确的手机号");
                    return;
                }
                sendVCodeAndCountDownTime(getPhoneNumber());
                break;
            case R.id.tvConfirmEdit:
                doEditPassword();
                break;
            default:
                break;
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etVcode = findViewById(R.id.etVcode);
        etPassword = findViewById(R.id.etPassword);
        findViewById(R.id.tvConfirmEdit).setOnClickListener(this);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        tvSendVerificationCode = findViewById(R.id.tvSendVerificationCode);
        tvSendVerificationCode.setOnClickListener(this);
    }

    private String getPhoneNumber() {
        return etPhoneNumber.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }

    private String getVCode() {
        return etVcode.getText().toString();
    }

    private String getConfirmPassword() {
        return etPasswordConfirm.getText().toString();
    }


    private void doEditPassword() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(getVCode())) {
            ToastUtil.show("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(getPassword())) {
            ToastUtil.show("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(getConfirmPassword())) {
            ToastUtil.show("请确认密码");
            return;
        }
        if (!getPassword().equals(getConfirmPassword())) {
            ToastUtil.show("两次密码输入不一致");
            return;
        }
        editPassword(getPhoneNumber(), getPassword(), getVCode());
    }


    /**
     * 修改密码
     *
     * @param mobile
     * @param pass
     * @param vCode
     */
    private void editPassword(String mobile, String pass, String vCode) {
          TourCooLogUtil.i(TAG, "输入的mobile："+mobile+"pass:"+pass +"vCode:"+vCode);
        ApiRepository.getInstance().restPassword(mobile, pass, vCode).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (AccountInfoHelper.getInstance().isRemindPassword()) {
                                    AccountInfoHelper.getInstance().changePassWord(getPassword());
                                }
                                ToastUtil.showSuccess("密码重置成功");
//                                TourCooUtil.startActivity(mContext, EditSuccessActivity.class);
                                finish();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private void sendVCodeAndCountDownTime(String phone) {
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

}

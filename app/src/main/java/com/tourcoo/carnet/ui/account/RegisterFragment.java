package com.tourcoo.carnet.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.WebViewActivity;
import com.tourcoo.carnet.core.frame.base.fragment.BaseFragment;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.module.MainTabActivity;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcoo.carnet.core.common.RequestConfig.BASE_URL_HEADER;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :注册
 * @company :途酷科技
 * @date 2019年03月20日14:12
 * @Email: 971613168@qq.com
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 注册标记
     */
    private static final String TAG_REGISTER = "userInfo";
    private String mUrl = "";

    private TextView tvSendVerificationCode;
    private List<Disposable> disposableList = new ArrayList<>();
    private static final long SECOND = 1000;
    private static final int COUNT = 60;
    private int count = COUNT;
    private EditText etPhoneNumber;
    private EditText etVcode;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private CheckBox cBoxAgree;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.tvLicensing).setOnClickListener(this);
        tvSendVerificationCode = mContentView.findViewById(R.id.tvSendVerificationCode);
        mContentView.findViewById(R.id.tvRegister).setOnClickListener(this);
        tvSendVerificationCode.setOnClickListener(this);
        etPhoneNumber = mContentView.findViewById(R.id.etPhoneNumber);
        etVcode = mContentView.findViewById(R.id.etVcode);
        etPassword = mContentView.findViewById(R.id.etPassword);
        etPasswordConfirm = mContentView.findViewById(R.id.etPasswordConfirm);
        cBoxAgree = mContentView.findViewById(R.id.cBoxAgree);
        findOrdinance();
    }


    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getPhoneNumber() {
        return etPhoneNumber.getText().toString();
    }

    private String getVcode() {
        return etVcode.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }

    private String getPasswordConfirm() {
        return etPasswordConfirm.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLicensing:
                WebViewActivity.start(mContext, mUrl);
                break;
            case R.id.tvSendVerificationCode:
                doSendVCode();
                break;
            case R.id.tvRegister:
                if (cBoxAgree.isChecked()) {
                    doRegister();
                } else {
                    ToastUtil.show("您未同意注册条例");
                }

                break;
            default:
                break;
        }
    }

    private void doRegister() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(getVcode())) {
            ToastUtil.show("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(getPassword())) {
            ToastUtil.show("请输入密码");
            return;
        }
        if (!getPassword().equals(getPasswordConfirm())) {
            ToastUtil.show("两次输入密码不一致");
            return;
        }
        register();
        //执行登录
    }

    /**
     * 验证码发送接口并倒计时
     *
     * @param phone
     */
    private void sendVCodeAndCountDownTime(String phone) {
        ApiRepository.getInstance().getVcode(phone).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                //验证码发送成功开始，倒计时
                                countDownTime();
                            }
                        }
                    }
                });
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


    @Override
    public void onDestroy() {
        cancelTime();
        super.onDestroy();
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
                cancelTime();
                reset();
            }
        });
    }

    /**
     * 发送验证码
     */
    private void doSendVCode() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        sendVCodeAndCountDownTime(getPhoneNumber());
    }

    /**
     * 注册请求
     */
    private void register() {
        ApiRepository.getInstance().mobileRegister(getPhoneNumber(), getPassword(), getVcode()).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data.toString().contains(TAG_REGISTER)) {
                                    UserInfoEntity userInfoEntity = parseUserInfo(JSONObject.toJSONString(entity.data));
                                    if (userInfoEntity != null) {
                                        //todo 保存用户信息
                                        AccountInfoHelper.getInstance().setUserInfoEntity(userInfoEntity);
                                        TourCooUtil.startActivity(mContext, MainTabActivity.class);
                                        mContext.finish();
                                    }
                                } else {
                                    ToastUtil.show("该号码已经注册");
                                    return;
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });

    }


    private UserInfoEntity parseUserInfo(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            TourCooLogUtil.e(TAG, "用户信息:" + jsonStr);
            UserInfoEntity userInfoEntity = JSON.parseObject(jsonStr, UserInfoEntity.class);
            JSONObject data = JSONObject.parseObject(jsonStr);
            JSONObject userInfo = data.getJSONObject("userInfo");
            int userId = userInfo.getIntValue("id");
            if (userInfoEntity.getUserInfo() != null) {
                userInfoEntity.getUserInfo().setUserId(userId);
                TourCooLogUtil.i(TAG, "设置成功:" + userId);
            }
            return userInfoEntity;
        } catch (Exception e) {
            TourCooLogUtil.e(TAG, "错误" + e.toString());
            return null;
        }
    }

  /*  userInfoEntity;
            TourcooLogUtil.i(TAG,userInfoEntity.getToken());
            TourcooLogUtil.i(TAG,userInfoEntity.getUserInfo().getCreateTime());
            AccountInfoHelper.getInstance().setUserInfoEntity(userInfoEntity);*/


    /**
     * 获取注册条例
     */
    private void findOrdinance() {
        ApiRepository.getInstance().findOrdinance().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<String>>() {
                    @Override
                    public void onRequestNext(BaseEntity<String> entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            TourCooLogUtil.e(TAG, entity);
                            mUrl = BASE_URL_HEADER + entity.data;
                            TourCooLogUtil.i(TAG, TAG + ":" + mUrl);
                        }
                    }
                });
    }
}

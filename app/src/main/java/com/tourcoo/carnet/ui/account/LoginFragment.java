package com.tourcoo.carnet.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.fragment.BaseFragment;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.module.MainTabActivity;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfo;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_ACCOUNT;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_IS_REMEMBER_ACCOUNT;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_IS_REMIND_PASSWORD;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_PASSWORD;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * /* TourCoolRetrofit.getInstance().setBaseUrl("https://www.apiopen.top/");
 * ApiRepository.getInstance().testApi("1").compose(bindUntilEvent(FragmentEvent.DESTROY)).
 * subscribe(new BaseLoadingObserver<String>() {
 *
 * @Override public void onRequestNext(String entity) {
 * TourcooLogUtil.d("测试："+entity);
 * }
 * });
 */

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年03月20日10:35
 * @Email: 971613168@qq.com
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 默认账号密码登录
     */
    private boolean passwordLoginFlag = true;
    private LinearLayout llVerifyCode;
    private LinearLayout llAccountLogin;
    private TextView tvVerificationCodeLogin;
    private EditText etPassword;
    private View lineVerifyCode;
    private TextView tvGetCode;
    private EditText etPhone;
    private EditText etCode;
    private List<Disposable> disposableList = new ArrayList<>();
    private static final long SECOND = 1000;
    private static final int COUNT = 30;
    private int count = COUNT;
    private CheckBox cBoxRemeberPassword;
    private LinearLayout llVerifyCodeAndPassword;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etPhone = mContentView.findViewById(R.id.etPhone);
        etCode = mContentView.findViewById(R.id.etCode);
        cBoxRemeberPassword = mContentView.findViewById(R.id.cBoxRemeberPassword);
        etPassword = mContentView.findViewById(R.id.etPassword);
        llVerifyCode = mContentView.findViewById(R.id.llVerifyCode);
        tvVerificationCodeLogin = mContentView.findViewById(R.id.tvVerificationCodeLogin);
        llAccountLogin = mContentView.findViewById(R.id.llAccountLogin);
        llVerifyCodeAndPassword = mContentView.findViewById(R.id.llVerifyCodeAndPassword);
        lineVerifyCode = mContentView.findViewById(R.id.lineVerifyCode);
        tvGetCode = mContentView.findViewById(R.id.tvGetCode);
        mContentView.findViewById(R.id.btnLogin).setOnClickListener(this);
        mContentView.findViewById(R.id.tvForgetPassword).setOnClickListener(this);
        llAccountLogin.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        tvVerificationCodeLogin.setOnClickListener(this);
        showLoginByAccount();
        cBoxRemeberPassword.setChecked(AccountInfoHelper.getInstance().isRemindPassword());
        //显保存的账号和密码
        if (isRemberPassword()) {
            etPhone.setText((CharSequence) SharedPreferencesUtil.get(PREF_KEY_ACCOUNT, ""));
            etPassword.setText((CharSequence) SharedPreferencesUtil.get(PREF_KEY_PASSWORD, ""));
        } else {
            etPhone.setText("");
            etPassword.setText("");
        }
    }

    private String getPhoneNumber() {
        return etPhone.getText().toString();
    }

    private String getPasword() {
        return etPassword.getText().toString();
    }

    private String getVcode() {
        return etCode.getText().toString();
    }


    public boolean isRemberPassword() {
        return cBoxRemeberPassword.isChecked();
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 验证码登录,隐藏密码登录模块
     */
    private void showLoginByVerify() {
        hideView(etPassword);
        showView(llVerifyCode);
        showView(llAccountLogin);
        hideView(llVerifyCodeAndPassword);
    }

    /**
     * 账号密码登录,隐藏验证码模块
     */
    private void showLoginByAccount() {
        showView(etPassword);
        hideView(llVerifyCode);
        hideView(llAccountLogin);
        showView(llVerifyCodeAndPassword);
        hideView(lineVerifyCode);
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
//                doLogin();
                if (passwordLoginFlag) {
                    doLoginByPhoneNumber();
                } else {
                    doLoginByVCode();
                }

                break;
            case R.id.tvVerificationCodeLogin:
                passwordLoginFlag = false;
                showLoginByVerify();

                break;
            case R.id.llAccountLogin:
                passwordLoginFlag = true;
                showLoginByAccount();
                break;
            case R.id.tvGetCode:
                doSendVCode();
                break;
            case R.id.tvForgetPassword:
                TourcooUtil.startActivity(mContext, EditPasswordActivity.class);
                break;
            default:
                break;
        }
    }


    private void setClickEnable(boolean clickEnable) {
        tvGetCode.setEnabled(clickEnable);
    }

    private void setText(String text) {
        tvGetCode.setText(text);
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
                if (aLong >= COUNT - 1) {
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
     * 账号密码登录
     *
     * @param phone
     * @param password
     */
    private void loginByPassword(String phone, String password) {
        ApiRepository.getInstance().loginByPassword(phone, password).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity == null) {
                            ToastUtil.showFailed("服务器异常");
                            return;
                        }
                        if (entity.code == CODE_REQUEST_SUCCESS) {
                            doSkipByCondition(entity);
                        } else {
                            ToastUtil.showFailed(entity.message);
                        }
                    }
                });
    }


    /**
     * 根据手机账号密码登录
     */
    private void doLoginByPhoneNumber() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourcooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(getPasword())) {
            ToastUtil.show("请输入密码");
            return;
        }
        SharedPreferencesUtil.put(PREF_KEY_IS_REMEMBER_ACCOUNT, isRemberPassword());
        if (isRemberPassword()) {
            //记住账号和密码
            SharedPreferencesUtil.put(PREF_KEY_PASSWORD, getPasword());
            SharedPreferencesUtil.put(PREF_KEY_ACCOUNT, getPhoneNumber());
        }
        //记录用户是否保存账号
        SharedPreferencesUtil.put(PREF_KEY_IS_REMIND_PASSWORD, isRemberPassword());
        loginByPassword(getPhoneNumber(), getPasword());
    }


    /**
     * 执行验证码登录
     */
    private void doLoginByVCode() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourcooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(getVcode())) {
            ToastUtil.show("请输入验证码");
            return;
        }
        loginByVCode();
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
     * 发送验证码
     */
    private void doSendVCode() {
        if (TextUtils.isEmpty(getPhoneNumber())) {
            ToastUtil.show("请先输入手机号");
            return;
        }
        if (!TourcooUtil.isMobileNumber(getPhoneNumber())) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        sendVCodeAndCountDownTime(getPhoneNumber());
    }


    private void loginByVCode() {
        ApiRepository.getInstance().loginByVCode(getPhoneNumber(), getVcode()).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                doSkipByCondition(entity);
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
            TourcooLogUtil.e(TAG, "用户信息:" + jsonStr);
            UserInfoEntity userInfoEntity = JSON.parseObject(jsonStr, UserInfoEntity.class);
            JSONObject data = JSONObject.parseObject(jsonStr);
            JSONObject userInfo = data.getJSONObject("userInfo");
            int userId = userInfo.getIntValue("id");
            if (userInfoEntity.getUserInfo() != null) {
                userInfoEntity.getUserInfo().setUserId(userId);
                TourcooLogUtil.i(TAG, "设置成功:" + userId);
            }
            return userInfoEntity;
        } catch (Exception e) {
            TourcooLogUtil.e(TAG, "错误" + e.toString());
            return null;
        }
    }


    private void doSkipByCondition(BaseEntity entity) {
        if (entity == null) {
            ToastUtil.show("用户信息获取失败");
            return;
        }
        UserInfoEntity userInfoEntity = parseUserInfo(JSONObject.toJSONString(entity.data));
        if (userInfoEntity != null) {
            TourcooLogUtil.i(TAG, "车主id：" + userInfoEntity.getUserInfo().getUserId());
            saveUserInfo(userInfoEntity);
            TourcooUtil.startActivity(mContext, MainTabActivity.class);
            if (mContext != null) {
                mContext.finish();
            }
        } else {
            ToastUtil.showFailed("用户信息获取失败");
        }
    }


    private void saveUserInfo(UserInfoEntity userInfoEntity) {
        TourcooLogUtil.i("解析的数据：", userInfoEntity.getToken());
        TourcooLogUtil.i("解析的数据", userInfoEntity.getUserInfo().getCreateTime());
        //todo 暂时存在内存中 后期存在本地数据库
        AccountInfoHelper.getInstance().deleteUserAccount();
        AccountInfoHelper.getInstance().setUserInfoEntity(userInfoEntity);
        AccountInfoHelper.getInstance().saveUserInfo(userInfoEntity);

    }
}

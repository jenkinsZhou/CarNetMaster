package com.tourcoo.carnet.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.WxConfig;
import com.tourcoo.carnet.core.frame.base.fragment.BaseFragment;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.module.MainTabActivity;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_ACCOUNT;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_IS_REMEMBER_ACCOUNT;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_IS_REMIND_PASSWORD;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_PASSWORD;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS_NOT_REGISTER;

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
    private IWXAPI api;
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
    public static final String EXTRA_LOGIN = "EXTRA_LOGIN";

    public static final int ACTION_LOGIN = 112;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
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
        mContentView.findViewById(R.id.ivLoginWeChat).setOnClickListener(this);
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
        registToWX();
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
        hideView(cBoxRemeberPassword);
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
        showView(cBoxRemeberPassword);
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
                TourCooUtil.startActivity(mContext, ForgetPasswordActivity.class);
                break;
            case R.id.ivLoginWeChat:
                wxLogin();
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
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
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
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
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
        if (!TourCooUtil.isMobileNumber(getPhoneNumber())) {
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


    private void doSkipByCondition(BaseEntity entity) {
        if (entity == null) {
            ToastUtil.show("用户信息获取失败");
            return;
        }
        UserInfoEntity userInfoEntity = parseUserInfo(JSONObject.toJSONString(entity.data));
        if (userInfoEntity != null) {
            TourCooLogUtil.i(TAG, "车主id：" + userInfoEntity.getUserInfo().getUserId());
            saveUserInfo(userInfoEntity);
            TourCooUtil.startActivity(mContext, MainTabActivity.class);
            if (mContext != null) {
                mContext.finish();
            }
        } else {
            ToastUtil.showFailed("用户信息获取失败");
        }
    }


    private void saveUserInfo(UserInfoEntity userInfoEntity) {
        TourCooLogUtil.i("解析的数据：", userInfoEntity.getToken());
        TourCooLogUtil.i("解析的数据", userInfoEntity.getUserInfo().getCreateTime());
        //todo 暂时存在内存中 后期存在本地数据库
        AccountInfoHelper.getInstance().deleteUserAccount();
        AccountInfoHelper.getInstance().setUserInfoEntity(userInfoEntity);
        AccountInfoHelper.getInstance().saveUserInfo(userInfoEntity);
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        api = WXAPIFactory.createWXAPI(mContext, WxConfig.APP_ID, false);
        // 将该app注册到微信
        api.registerApp(WxConfig.APP_ID);
    }


    public void wxLogin() {
        if (!api.isWXAppInstalled()) {
            ToastUtil.showFailed("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        api.sendReq(req);
    }


    /**
     * 微信登录
     *
     * @param openId
     */
    private void loginByWechat(String openId) {
        ApiRepository.getInstance().loginByWechat(openId).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS_NOT_REGISTER) {
                                weChatLoginCallback(entity);
                            } else if (entity.code == CODE_REQUEST_SUCCESS) {
                                doSkipByCondition(entity);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (api != null) {
            api.detach();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 微信登录回调
     *
     * @param event
     */
    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void loginSuccess(BaseEvent event) {
        if (event != null) {
            TourCooLogUtil.i(TAG, "接收到消息:" + event.tag);
            loginByWechat(event.tag);
        }
    }


    private void weChatLoginCallback(BaseEntity entity) {
        UserInfoEntity userInfoEntity = parseUserInfo(com.alibaba.fastjson.JSONObject.toJSONString(entity.data));
        if (userInfoEntity != null) {
            TourCooLogUtil.i(TAG, "车主id：" + userInfoEntity.getUserInfo().getUserId());
            saveUserInfo(userInfoEntity);
            TourCooUtil.startActivity(mContext, MainTabActivity.class);
            if (mContext != null) {
                mContext.finish();
            }
        } else {
//          去绑定手机号
            Intent intent = new Intent();
            intent.putExtra(EXTRA_LOGIN, ACTION_LOGIN);
            intent.setClass(mContext, BindPhoneNumberActivity.class);
            startActivity(intent);
        }
    }


}

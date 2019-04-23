package com.tourcoo.carnet.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.core.common.WxConfig;
import com.tourcoo.carnet.core.frame.okhttp.OkHttpUtils;
import com.tourcoo.carnet.core.frame.okhttp.response.JsonResponseHandler;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import static com.tourcoo.carnet.core.common.WxConfig.APP_ID;

/**
 * @author :JenkinsZhou
 * @description :微信第三方登录回调页面
 * @company :途酷科技
 * @date 2019年04月16日12:26
 * @Email: 971613168@qq.com
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String TAG = "WXEntryActivity";
    private static final int CODE_REQUEST_SUCCESS = -100;
    public static final int CODE_SUCCESS_LOGIN = 2;

    /**
     * IWXAPI 是第三方app和微信通信的openapi接口
     */
    private IWXAPI api;
    private WeiXinLoginInfo mWeiXinLoginInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api = WXAPIFactory.createWXAPI(this, APP_ID);
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        TourCooLogUtil.e("sssss", "errCode = " + baseResp.errCode);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                TourCooLogUtil.d(TAG, "onResp: 成功");
                //需要转换一下才可以
                final String code = ((SendAuth.Resp) baseResp).code;
                TourCooLogUtil.i(TAG, "code=" + code);
                //获取微信的token
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                TourCooLogUtil.d(TAG, "onResp: 用户取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                TourCooLogUtil.d(TAG, "onResp: 发送请求被拒绝");
                finish();
                break;
            default:
//                ToastUtil.show("未知");
                break;
        }

    }


    @Override
    protected void onDestroy() {
        if (api != null) {
            api.detach();
        }
        super.onDestroy();
    }


    /**
     * 获取access_token：
     * 用户或取access_token的code，仅在ErrCode为0时有效
     */
    private void getAccessToken(String code) {
        HashMap<String, String> params = new HashMap(3);
        params.put("grant_type", "authorization_code");
        params.put("appid", WxConfig.APP_ID);
        params.put("secret", WxConfig.APP_SECRET);
        params.put("code", code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
//        https://api.weixin.qq.com/cgi-bin/token
        OkHttpUtils.getInstance().get(WXEntryActivity.this, url, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                mWeiXinLoginInfo = parseInfo(response.toString());
                if (mWeiXinLoginInfo != null) {
                    AccountInfoHelper.getInstance().setWeChatOpenId(mWeiXinLoginInfo.getOpenid());
                    EventBus.getDefault().post(new BaseEvent(CODE_SUCCESS_LOGIN,mWeiXinLoginInfo.getOpenid()));
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                TourCooLogUtil.e(TAG, "error_msg:" + error_msg);
                ToastUtil.showFailed("登录失败");
                finish();
            }
        });
    }

    private WeiXinLoginInfo parseInfo(String jsonString) {
        try {
            return JSON.parseObject(jsonString, WeiXinLoginInfo.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取微信头像
     *
     * @param
     */
    /*private void getWeiXinUserInfo(WeiXinLoginInfo weiXinLoginInfo) {
        if(weiXinLoginInfo == null){
           return;
        }
        HashMap<String, String> params = new HashMap(3);
        params.put("access_token", weiXinLoginInfo.getAccess_token());
        params.put("openid", weiXinLoginInfo.getOpenid());
        String url = "https://api.weixin.qq.com/sns/userinfo";
        https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN
        OkHttpUtils.getInstance().get(WXEntryActivity.this, url, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                TourcooLogUtil.i(TAG, "response:" + response.toString());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                TourcooLogUtil.e(TAG, "error_msg:" + error_msg);
            }
        });
    }*/


    private UserInfoEntity parseUserInfo(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            TourCooLogUtil.e(TAG, "用户信息:" + jsonStr);
            UserInfoEntity userInfoEntity = JSON.parseObject(jsonStr, UserInfoEntity.class);
            com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
            com.alibaba.fastjson.JSONObject userInfo = data.getJSONObject("userInfo");
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






}

package com.tourcoo.carnet.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.just.agentweb.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;

import static com.tourcoo.carnet.core.common.WxPayConfig.APP_ID;


/**
 * @author :JenkinsZhou
 * @description :微信支付
 * @company :途酷科技
 * @date 2019年04月09日9:55
 * @Email: 971613168@qq.com
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    /**
     * IWXAPI 是第三方app和微信通信的openapi接口
     */
    private IWXAPI api;

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
        TourcooLogUtil.e("sssss", "onPayFinish, errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCord = baseResp.errCode;
            //这里接收到了返回的状态码可以进行相应的操作，如果不想在这个页面操作可以把状态码存在本地然后finish掉这个页面，这样就回到了你调起支付的那个页面
            //获取到你刚刚存到本地的状态码进行相应的操作就可以了
            switch (errCord) {
                //success
                case 0:
                    ToastUtil.showSuccess("支付成功");
                    break;
                case -1:
                    Toast.makeText(this, "支付失败，请重新尝试支付", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(this, "您已经取消支付，请重新尝试支付", Toast.LENGTH_SHORT).show();
                    break;
                default:

                    break;
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (api != null) {
            api.detach();
        }
        super.onDestroy();
    }
}

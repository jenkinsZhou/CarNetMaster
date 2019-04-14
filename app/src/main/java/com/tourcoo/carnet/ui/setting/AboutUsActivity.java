package com.tourcoo.carnet.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.helper.CheckVersionHelper;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :关于我们
 * @company :翼迈科技
 * @date 2019年 03月 17日 10时15分
 * @Email: 971613168@qq.com
 */
public class AboutUsActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    @Override
    public int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.stvAppVersion).setOnClickListener(this);
        findViewById(R.id.stvPhoneNumber).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stvAppVersion:
                CheckVersionHelper.with(this).checkVersion(true);
                break;
            case R.id.stvPhoneNumber:
            call("18256070563");
                    break;
            default:
                break;
        }
    }



    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

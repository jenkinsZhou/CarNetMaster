package com.tourcoo.carnet.ui.account;

import android.os.Bundle;
import android.view.View;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.event.UserInfoRefreshEvent;

import org.greenrobot.eventbus.EventBus;

import java.sql.Ref;

/**
 * @author :zhoujian
 * @description :修改密码成功
 * @company :翼迈科技
 * @date 2019年 03月 17日 09时37分
 * @Email: 971613168@qq.com
 */
public class EditSuccessActivity extends BaseTourCooTitleActivity {
    @Override
    public int getContentLayout() {
        return R.layout.activity_edit_password_success;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void finish() {
        EventBus.getDefault().post(new UserInfoRefreshEvent());
        super.finish();
    }
}

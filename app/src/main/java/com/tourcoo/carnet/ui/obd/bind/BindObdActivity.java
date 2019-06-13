package com.tourcoo.carnet.ui.obd.bind;

import android.os.Bundle;
import android.view.View;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :JenkinsZhou
 * @description :绑定obd
 * @company :途酷科技
 * @date 2019年06月13日9:30
 * @Email: 971613168@qq.com
 */
public class BindObdActivity extends BaseTourCooTitleActivity implements View.OnClickListener {

    @Override
    public int getContentLayout() {
        return R.layout.activity_bind_obd;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tvSkip).setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("绑定OBD");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvConfirmBind:
                //绑定obd
                break;
            case R.id.tvSkip:
                //跳过
                break;
            default:
                break;
        }
    }
}

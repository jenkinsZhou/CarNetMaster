package com.tourcoo.carnet.ui.account;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :切换账户
 * @company :翼迈科技
 * @date 2019年 03月 17日 09时38分
 * @Email: 971613168@qq.com
 */
public class ChangeAccountActivity extends BaseTourCooTitleActivity {
    @Override
    public int getContentLayout() {
        return R.layout.activity_change_account;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("切换账号");
    }
}

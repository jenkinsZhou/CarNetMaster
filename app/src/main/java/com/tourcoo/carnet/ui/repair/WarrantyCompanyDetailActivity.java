package com.tourcoo.carnet.ui.repair;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

/**
 * @author :zhoujian
 * @description :保修公司详情
 * @company :翼迈科技
 * @date 2019年 03月 17日 16时52分
 * @Email: 971613168@qq.com
 */
public class WarrantyCompanyDetailActivity extends BaseTourCooTitleActivity {
    @Override
    public int getContentLayout() {
        return R.layout.activity_warranty_company_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("保险公司详情");
    }
}

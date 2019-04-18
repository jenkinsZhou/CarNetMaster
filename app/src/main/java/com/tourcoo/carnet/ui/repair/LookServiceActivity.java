package com.tourcoo.carnet.ui.repair;

import android.os.Bundle;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :JenkinsZhou
 * @description :查看服务页面
 * @company :途酷科技
 * @date 2019年04月18日20:22
 * @Email: 971613168@qq.com
 */
public class LookServiceActivity extends BaseTourCooTitleActivity {

    private GridImageAdapter gridImageAdapter;

    private RecyclerView rvImageComment;
    @Override
    public int getContentLayout() {
        return R.layout.activity_look_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("查看服务详情");
    }
}

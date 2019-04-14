package com.tourcoo.carnet.ui.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :JenkinsZhou
 * @description :上门服务详情
 * @company :途酷科技
 * @date 2019年03月22日16:54
 * @Email: 971613168@qq.com
 */
public class DoorToDoorServiceDetailActivity extends BaseTourCooTitleActivity implements View.OnClickListener {

    private GridImageAdapter gridImageAdapter;
    private RecyclerView faultImageRecyclerView;

    private List<String> imageUrList = new ArrayList<>();
    private TextView tvCancelService;

    @Override
    public int getContentLayout() {
        return R.layout.activity_door_to_door_service_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        faultImageRecyclerView = findViewById(R.id.faultImageRecyclerView);
        findViewById(R.id.tvCancelService).setOnClickListener(this);
        gridImageAdapter = new GridImageAdapter(imageUrList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
        faultImageRecyclerView.setLayoutManager(manager);
        faultImageRecyclerView.setNestedScrollingEnabled(false);
        //由于尺寸固定所以设置为true,提高性能
        faultImageRecyclerView.setHasFixedSize(true);
        gridImageAdapter.bindToRecyclerView(faultImageRecyclerView);
    }

    @Override
    public void loadData() {
        super.loadData();
        int cout = 7;
        for (int i = 0; i < cout; i++) {
            if (i % 2 != 0) {
                imageUrList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1553271124996&di=a465b441898519ed8b25ad09c8ab2182&imgtype=0&src=http%3A%2F%2Fimg8.zol.com.cn%2Fbbs%2Fupload%2F23765%2F23764770.jpg");
            } else {
                imageUrList.add("http://img3.bitautoimg.com/bitauto/2013/01/171553274.JPG");
            }
        }
        gridImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("上门服务详情");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancelService:
                TourcooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class);
                break;
            default:
                break;
        }
    }
}

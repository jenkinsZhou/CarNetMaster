package com.tourcoo.carnet.ui.order;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.custom.EmiViewPager;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.entity.event.OrderEvent;
import com.tourcoo.carnet.ui.repair.HistoryFaultRepairFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.tourcoo.carnet.ui.repair.RepairFaultFragment.EXTRA_ORDER_TYPE;
import static com.tourcoo.carnet.ui.repair.RepairFaultFragment.TYPE_REPAIR;

/**
 * @author :zhoujian
 * @description :历史订单
 * @company :翼迈科技
 * @date 2019年 03月 16日 12时08分
 * @Email: 971613168@qq.com
 */
public class OrderHistoryActivity extends BaseTourCooTitleActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private EmiViewPager orderHistoryViewPager;
    private List<Fragment> fragmentList;
    private TextView tvTabRepair;
    private TextView tvTabService;
    private MyHandler mMyHandler = new MyHandler();
    private String type ;

    @Override
    public int getContentLayout() {
        return R.layout.activity_order_history;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        orderHistoryViewPager = findViewById(R.id.orderHistoryViewPager);
        tvTabRepair = findViewById(R.id.tvTabRepair);
        tvTabService = findViewById(R.id.tvTabService);
        tvTabRepair.setOnClickListener(this);
        tvTabService.setOnClickListener(this);
        EventBus.getDefault().register(this);
        type = getIntent().getStringExtra(EXTRA_ORDER_TYPE);
          TourCooLogUtil.i(TAG, "测试类型:"+type);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("历史订单");
    }

    @Override
    public void loadData() {
        super.loadData();
        fragmentList = new ArrayList<>();
        //故障报修
        fragmentList.add(HistoryFaultRepairFragment.newInstance());
        //上门服务
        fragmentList.add(HistoryServiceFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        orderHistoryViewPager.addOnPageChangeListener(this);
        orderHistoryViewPager.setAdapter(pagerAdapter);
        //必须在 viewPager.setAdapter()之后使用
        if (TYPE_REPAIR.equals(type)) {
            showHistoryFault();
              TourCooLogUtil.i(TAG, "接收到");
        } else {
            showHistoryService();
            TourCooLogUtil.e(TAG, "接收到");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTabRepair:
                showHistoryFault();
                break;
            case R.id.tvTabService:
                showHistoryService();
                break;
            default:
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    private void setCurrentTab(int index) {
        switch (index) {
            case 0:
                showHistoryFault();
                break;
            case 1:
                showHistoryService();
                break;
            default:
                break;
        }
    }

    private void setSelect(TextView textView) {
        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_rectangle_blue));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
    }

    private void setUnSelect(TextView textView) {
        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_rectangle_blue_hollow));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mMyHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBaseEvent(BaseEvent event) {
        EventBus.getDefault().postSticky(new OrderEvent(event.id));
        mMyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TYPE_REPAIR.equals(type)) {
                    showHistoryFault();
                    TourCooLogUtil.i(TAG, "接收到");
                } else {
                    showHistoryService();
                    TourCooLogUtil.e(TAG, "接收到");
                }
            }
        }, 100);
    }

    private void showHistoryService() {
        setSelect(tvTabService);
        setUnSelect(tvTabRepair);
        orderHistoryViewPager.setCurrentItem(1);
    }

    private void showHistoryFault() {
        setSelect(tvTabRepair);
        setUnSelect(tvTabService);
        orderHistoryViewPager.setCurrentItem(0);
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


}

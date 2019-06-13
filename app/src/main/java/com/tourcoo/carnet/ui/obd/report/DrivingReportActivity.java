package com.tourcoo.carnet.ui.obd.report;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseActivity;
import com.tourcoo.carnet.core.widget.custom.EmiViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :行驶报告
 * @company :途酷科技
 * @date 2019年06月04日17:18
 * @Email: 971613168@qq.com
 */
public class DrivingReportActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    /**
     * 行驶报告tab
     */
    public static final int TAB_DRIVING_REPORT = 0;
    /**
     * 日报tab
     */
    public static final int TAB_DAILY_REPORT = 1;
    /**
     * 时间段报告tab
     */
    public static final int TAB_TIME_INTERVAL_REPORT = 2;
    private EmiViewPager mViewPager;
    private TextView tvDrivingReport;
    private TextView tvDailyReport;
    private TextView tvTimeIntervalReport;
    /**
     * 当前需要显示的tab
     */
    public int currentTab = TAB_DRIVING_REPORT;

    @Override
    public int getContentLayout() {
        return R.layout.activity_driving_report;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mViewPager = findViewById(R.id.mViewPager);
        tvDrivingReport = findViewById(R.id.tvDrivingReport);
        tvDailyReport = findViewById(R.id.tvDailyReport);
        tvTimeIntervalReport = findViewById(R.id.tvTimeIntervalReport);
        mViewPager.addOnPageChangeListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(DriveReportPageListFragment.newInstance());
        fragmentList.add(DailyReportFragment.newInstance());
        fragmentList.add(DailyReportFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentTab = position;
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setCurrentTab(int position) {
        switch (position) {
            case TAB_DRIVING_REPORT:
                showTabDrivingReport();
                break;
            case TAB_DAILY_REPORT:
                showTabDailyReport();
                break;
            case TAB_TIME_INTERVAL_REPORT:
                showTabTimeIntervalReport();
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
        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_rectangle_blue_hollow_horizon));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }


    private void showTabDrivingReport() {
        setSelect(tvDrivingReport);
        setUnSelect(tvDailyReport);
        setUnSelect(tvTimeIntervalReport);
    }


    private void showTabDailyReport() {
        setSelect(tvDailyReport);
        setUnSelect(tvDrivingReport);
        setUnSelect(tvTimeIntervalReport);
    }

    private void showTabTimeIntervalReport() {
        setSelect(tvTimeIntervalReport);
        setUnSelect(tvDailyReport);
        setUnSelect(tvDrivingReport);
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


}

package com.tourcoo.carnet.ui.repair;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.android.material.tabs.TabLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.widget.core.view.navigation.KeyboardHelper;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.custom.WrapContentHeightViewPager;
import com.tourcoo.carnet.entity.event.SearchEvent;
import com.tourcoo.carnet.ui.factory.NearbyRepairFactoryActivity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author :JenkinsZhou
 * @description :快速检索
 * @company :途酷科技
 * @date 2019年03月25日14:09
 * @Email: 971613168@qq.com
 */
public class FastSearchActivity extends BaseTourCooTitleActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private WrapContentHeightViewPager searchViewpager;
    private TabLayout searchTabLayout;
    private EditText etSearch;
    public static final int EVENT_CLEAR_INPUT = 1;

    private String[] titles = new String[]{"修理厂", "保险公司"};

    @Override
    public int getContentLayout() {
        return R.layout.activity_fast_search;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        searchViewpager = findViewById(R.id.searchViewpager);
        searchTabLayout = findViewById(R.id.searchTabLayout);
        etSearch = findViewById(R.id.etSearch);
        findViewById(R.id.ivClear).setOnClickListener(this);
        findViewById(R.id.ivSearch).setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //按下搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    EventBus.getDefault().post(new SearchEvent(getInput()));
                    KeyboardHelper.closeKeyboard(mContext);
                    return true;
                }
                //返回true，保留软键盘。false，隐藏软键盘
                return false;
            }
        });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("快速检索");
        titleBar.setRightTextColor(TourcooUtil.getColor(R.color.blueCommon));
        titleBar.setRightText("附近修理厂");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourcooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class);
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        List<Fragment> fragmentList = new ArrayList<>();
        RepairDepotListFragment repairDepotListFragment = RepairDepotListFragment.newInstance();
        fragmentList.add(repairDepotListFragment);
        fragmentList.add(InsuranceCompanyFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        searchViewpager.addOnPageChangeListener(this);
        searchViewpager.setAdapter(pagerAdapter);
        searchTabLayout.setupWithViewPager(searchViewpager);
        initTabTitle();
    }


    private void initTabTitle() {
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = searchTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(titles[i]);
            }
        }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                break;
            case R.id.ivClear:
                EventBus.getDefault().post(EVENT_CLEAR_INPUT);
                clearInput();
                break;
            default:
                break;
        }
    }







    @Override
    protected void onDestroy() {
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }






    private void clearInput() {
        if (etSearch != null) {
            etSearch.setText("");
        }
    }

    public String getInput() {
        if (etSearch != null) {
            return etSearch.getText().toString();
        }
        return "";
    }

}

package com.tourcoo.carnet.ui.repair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.navigation.KeyboardHelper;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.custom.WrapContentHeightViewPager;
import com.tourcoo.carnet.entity.event.SearchEvent;
import com.tourcoo.carnet.ui.factory.NearbyRepairFactoryActivity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.tourcoo.carnet.entity.event.SearchEvent.EVENT_ACTION_SEARCH_GARAGE;
import static com.tourcoo.carnet.entity.event.SearchEvent.EVENT_ACTION_SEARCH_INSURANCE_COMPANY;

/**
 * @author :JenkinsZhou
 * @description :快速检索
 * @company :途酷科技
 * @date 2019年03月25日14:09
 * @Email: 971613168@qq.com
 */
public class FastSearchActivity extends BaseTourCooTitleActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private WrapContentHeightViewPager searchViewpager;
    private TabLayout searchTabLayout;
    private EditText etSearch;

    private int currentSelectPosition;
    private ImageView ivClear;


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
        ivClear = findViewById(R.id.ivClear);
        ivClear.setOnClickListener(this);
        findViewById(R.id.ivSearch).setOnClickListener(this);
        initInputListener();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //按下搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    switch (currentSelectPosition) {
                        case 0:
                            EventBus.getDefault().post(new SearchEvent(getInput(), EVENT_ACTION_SEARCH_GARAGE));
                            break;
                        case 1:
                            EventBus.getDefault().post(new SearchEvent(getInput(), EVENT_ACTION_SEARCH_INSURANCE_COMPANY));
                            break;
                        default:
                            break;
                    }
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
        titleBar.setRightTextColor(TourCooUtil.getColor(R.color.blueCommon));
      /*  titleBar.setRightText("附近修理厂");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class);
            }
        });*/
    }

    @Override
    public void loadData() {
        super.loadData();
        List<Fragment> fragmentList = new ArrayList<>();
        RepairDepotListFragment repairDepotListFragment = RepairDepotListFragment.newInstance();
        fragmentList.add(repairDepotListFragment);
        fragmentList.add(InsuranceCompanyListFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        searchViewpager.addOnPageChangeListener(this);
        searchViewpager.setAdapter(pagerAdapter);
        searchTabLayout.setupWithViewPager(searchViewpager);
        initTabTitle();
        searchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentSelectPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        currentSelectPosition = position;
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
                clearInput();
                switch (currentSelectPosition) {
                    case 0:
                        EventBus.getDefault().post(EVENT_ACTION_SEARCH_GARAGE);
                        break;
                    case 1:
                        EventBus.getDefault().post(EVENT_ACTION_SEARCH_INSURANCE_COMPANY);
                        break;
                    default:
                        break;
                }

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


    private void initInputListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.length() == 0){
                    ivClear.setVisibility(View.INVISIBLE);
                    }else {
                        ivClear.setVisibility(View.VISIBLE);
                    }
            }
        });
    }
}

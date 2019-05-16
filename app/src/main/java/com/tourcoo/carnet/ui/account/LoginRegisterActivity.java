package com.tourcoo.carnet.ui.account;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.tabs.TabLayout;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.helper.TitleBarViewHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.custom.WrapContentHeightViewPager;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.AccountInfoHelper.PREF_TEL_PHONE_KEY;

/**
 * @author :zhoujian
 * @description :注册登录
 * @company :翼迈科技
 * @date 2019年 03月 17日 12时55分
 * @Email: 971613168@qq.com
 */
public class LoginRegisterActivity extends BaseTourCooTitleActivity implements ViewPager.OnPageChangeListener, EasyPermissions.PermissionCallbacks {
    private TitleBarViewHelper mTitleBarViewHelper;
    private WrapContentHeightViewPager loginViewpager;
    private TabLayout loginTabLayout;
    private String[] titles = new String[]{"登录", "注册"};
    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    @Override
    public int getContentLayout() {
        return R.layout.activity_login_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        loginViewpager = findViewById(R.id.loginViewpager);
        loginTabLayout = findViewById(R.id.loginTabLayout);

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setRightTextDrawable(R.drawable.ic_transition)
                .setDividerVisible(false)
                .setStatusAlpha(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102)
                .setStatusAlpha(0)
                .setVisibility(View.GONE);
        StatusBarUtil.setStatusBarLightMode(this);
        titleBar.setLeftTextColor(Color.WHITE)
                .setBgColor(Color.WHITE);
        int mMaxHeight = CarNetApplication.getImageHeight() - StatusBarUtil.getStatusBarHeight() - getResources().getDimensionPixelSize(R.dimen.dp_title_height);
        mTitleBarViewHelper = new TitleBarViewHelper(mContext)
                .setTitleBarView(mTitleBar)
                .setMinHeight(0)
                .setMaxHeight(mMaxHeight);
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
    public void loadData() {
        super.loadData();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(LoginFragment.newInstance());
        fragmentList.add(RegisterFragment.newInstance());
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        loginViewpager.addOnPageChangeListener(this);
        loginViewpager.setAdapter(pagerAdapter);
        loginTabLayout.setupWithViewPager(loginViewpager);
        initTabTitle();
        requestPermission();

    }

    private void initTabTitle() {
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = loginTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(titles[i]);
            }
        }
    }

    private void getLocate() {
        if (checkLocatePermission()) {
            locate();
        } else {
            PermissionManager.requestPermission(mContext, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
        }
    }

    /***
     * 定位
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LocateHelper.getInstance().stopLocation();
            }
        });
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(this, PermissionConstance.PERMS_LOCATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //权限已被用户授予
//        getLocate();
        closeLoadingDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
        closeLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        LocateHelper.getInstance().stopLocation();
        super.onDestroy();
    }


    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mContext,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);
        }
    }



}

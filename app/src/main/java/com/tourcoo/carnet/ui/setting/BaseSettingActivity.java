package com.tourcoo.carnet.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.allen.library.SuperTextView;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.util.DataCleanManager;
import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.module.MainTabActivity;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.ui.account.LoginRegisterActivity;

import static com.tourcoo.carnet.core.frame.util.DataCleanManager.EMPTY_CACHE;

/**
 * @author :zhoujian
 * @description :基础设置
 * @company :翼迈科技
 * @date 2019年 03月 16日 23时25分
 * @Email: 971613168@qq.com
 */
public class BaseSettingActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private SuperTextView stvClearCache;

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting_base;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.btnExitLogin).setOnClickListener(this);
        findViewById(R.id.stvAboutUs).setOnClickListener(this);
        stvClearCache = findViewById(R.id.stvClearCache);
        stvClearCache.setOnClickListener(this);
        showCache();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("基础设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExitLogin:
                AccountInfoHelper.getInstance().deleteUserAccount();
                SharedPreferencesUtil.clearAll(mContext);
                Activity activity = StackUtil.getInstance().getActivity(MainTabActivity.class);
                if (activity != null) {
                    TourCooLogUtil.i("执行了:");
                    activity.finish();
                }
                TourCooUtil.startActivity(mContext, LoginRegisterActivity.class);
                finish();

                break;
            case R.id.stvAboutUs:
                TourCooUtil.startActivity(mContext, AboutUsActivity.class);
                break;
            case R.id.stvClearCache:
                cleanCache();
                showCache();
                ToastUtil.showSuccess("清除成功");
                break;
            default:
                break;
        }
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    private String getCacheSize() {
        String str = "";
        try {
            str = DataCleanManager.getTotalCacheSize(mContext);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
        return str;
    }

    /**
     * 清空缓存
     */
    private void cleanCache() {
        DataCleanManager.clearAllCache(mContext);
    }


    private void showCache() {
        if (EMPTY_CACHE.equalsIgnoreCase(getCacheSize())) {
            stvClearCache.setRightString("");
        } else {
            stvClearCache.setRightString(getCacheSize());
        }
    }

}

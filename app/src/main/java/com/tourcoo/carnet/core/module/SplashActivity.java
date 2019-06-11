package com.tourcoo.carnet.core.module;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTitleActivity;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.ui.account.LoginRegisterActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import androidx.core.content.ContextCompat;
import io.reactivex.disposables.Disposable;

/**
 * @author :zhoujian
 * @description : zj
 * @company :翼迈科技
 * @date 2019年03月06日上午 11:43
 * @Email: 971613168@qq.com
 */
public class SplashActivity extends BaseTitleActivity {
    private ImageView ivBg;
    private Disposable mDisposable;
    private String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551858906703&di=1ca8f8f6a8b88467bd4051128bf76ee2&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fa9716e409a267f1923ded98bb7642a42b80bd7961b5438-kdBlwv_fw658";
    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        TourCooLogUtil.i(TAG, "isTaskRoot:" + isTaskRoot() + ";getCurrent:" + StackUtil.getInstance().getCurrent());
        //防止应用后台后点击桌面图标造成重启的假象---MIUI及Flyme上发现过(原生未发现)
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.beforeSetContentView();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            return;
        }
        if (!StatusBarUtil.isSupportStatusBarFontChange()) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        ivBg = findViewById(R.id.sp_bg);
       RelativeLayout rlContentView = findViewById(R.id.rlContentView);
        rlContentView.setBackgroundColor( TourCooUtil.getColor(R.color.whiteCommon));
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_back);
        TourCooUtil.getTintDrawable(drawable, Color.WHITE);
        RxJavaManager.getInstance().setTimer(1000)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<Long>() {

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        //先判断有无记录账号信息
                        if (AccountInfoHelper.getInstance().isRemindPassword()) {
                            UserInfoEntity userInfoEntity = AccountInfoHelper.getInstance().findUserInfoByLocal();
                            if (userInfoEntity != null && userInfoEntity.getUserInfo() != null) {
                                AccountInfoHelper.getInstance().setUserInfoEntity(userInfoEntity);
                                TourCooUtil.startActivity(mContext, MainTabActivity.class);
                            } else {
                                TourCooUtil.startActivity(mContext, LoginRegisterActivity.class);
                            }
                        } else {
                            //用户没有记住密码
                            AccountInfoHelper.getInstance().deleteUserAccount();
                            TourCooUtil.startActivity(mContext, LoginRegisterActivity.class);
                        }
                        finish();
                    }

                    @Override
                    public void onNext(Long entity) {
                        TourCooLogUtil.d(TAG, "延时时间:" + entity);
                    }

                    @Override
                    public void onRequestNext(Long entity) {
                        TourCooLogUtil.i(TAG, "延时时间:" + entity);
                    }
                });
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setStatusBarLightMode(false)
                .setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }
}

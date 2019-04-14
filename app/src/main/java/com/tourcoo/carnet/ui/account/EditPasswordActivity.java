package com.tourcoo.carnet.ui.account;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.RxJavaManager;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author :zhoujian
 * @description :修改密码
 * @company :翼迈科技
 * @date 2019年 03月 17日 13时49分
 * @Email: 971613168@qq.com
 */
public class EditPasswordActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private TextView tvSendVerificationCode;
    private List<Disposable> disposableList = new ArrayList<>();
    private static final long SECOND = 1000;
    private static final int COUNT = 30;
    private int count = COUNT;

    @Override
    public int getContentLayout() {
        return R.layout.activity_edit_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvSendVerificationCode = findViewById(R.id.tvSendVerificationCode);
        tvSendVerificationCode.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("修改密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendVerificationCode:
                setClickEnable(false);
                countDownTime();
                break;
            default:
                break;
        }
    }

    private void cancelTime() {
        if (disposableList != null && !disposableList.isEmpty()) {
            Disposable disposable;
            for (int i = 0; i < disposableList.size(); i++) {
                disposable = disposableList.get(i);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposableList.remove(disposable);
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        cancelTime();
        super.onDestroy();
    }

    private void setClickEnable(boolean clickEnable) {
        tvSendVerificationCode.setEnabled(clickEnable);
    }

    private void setText(String text) {
        tvSendVerificationCode.setText(text);
    }

    private void reset() {
        setClickEnable(true);
        count = COUNT;
        setText("发送验证码");
    }

    /**
     * 倒计时
     */
    private void countDownTime() {
        reset();
        setClickEnable(false);
        RxJavaManager.getInstance().doEventByInterval(SECOND, new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }


            @Override
            public void onNext(Long aLong) {
                --count;
                setText("还有" + count + "秒");
                if (aLong >= COUNT - 1 || count < 0) {
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                cancelTime();
            }

            @Override
            public void onComplete() {
                reset();
                cancelTime();
            }
        });
    }


}

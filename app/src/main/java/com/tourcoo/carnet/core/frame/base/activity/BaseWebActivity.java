package com.tourcoo.carnet.core.frame.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.interfaces.TitleBarViewControl;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.action.ActionSheetDialog;
import com.tourcoo.carnet.core.widget.core.action.BaseDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.just.agentweb.AgentWeb;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

/**
 * @author :zhoujian
 * @description : App内快速实现WebView功能
 * @company :翼迈科技
 * @date 2019年03月04日下午 05:31
 * @Email: 971613168@qq.com
 */
public abstract class BaseWebActivity extends BaseTourCooTitleActivity {

    protected ViewGroup mContainer;
    protected String url = "";
    protected String mCurrentUrl;
    protected AlertDialog mAlertDialog;
    protected AgentWeb mAgentWeb;
    protected AgentWeb.CommonBuilder mAgentBuilder;
    protected ActionSheetDialog mActionSheetView;
    private TitleBarViewControl mTitleBarViewControl;

    protected static void start(Activity mActivity, Class<? extends BaseWebActivity> activity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        TourCooUtil.startActivity(mActivity, activity, bundle);
    }


    protected void setAgentWeb(AgentWeb mAgentWeb) {

    }

    protected void setAgentWeb(AgentWeb.CommonBuilder mAgentBuilder) {

    }

    /**
     * 设置进度条颜色
     *
     * @return
     */
    @ColorInt
    protected int getProgressColor() {
        return -1;
    }

    /**
     * 设置进度条高度 注意此处最终AgentWeb会将其作为float 转dp2px
     *
     * @return
     */
    protected int getProgressHeight() {
        return 2;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        mTitleBarViewControl = UiConfigManager.getInstance().getTitleBarViewControl();
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        mContainer = findViewById(R.id.lLayout_containerWeb);
        url = getIntent().getStringExtra("url");
        mCurrentUrl = url;
        initAgentWeb();
        super.beforeInitView(savedInstanceState);

    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        super.beforeSetTitleBar(titleBar);
        if (mTitleBarViewControl != null) {
            mTitleBarViewControl.createTitleBarViewControl(titleBar, this.getClass());
        }
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        })
                .setRightTextDrawable(TourCooUtil.getTintDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_more),
                        ContextCompat.getColor(mContext, R.color.colorTitleText)))
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showActionSheet();
                    }
                })
                .addLeftAction(titleBar.new ImageAction(
                        TourCooUtil.getTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_close),
                                ContextCompat.getColor(mContext, R.color.colorTitleText)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                }));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_web;
    }

    protected void initAgentWeb() {
        mAgentBuilder = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator(getProgressColor() != -1 ? getProgressColor() : ContextCompat.getColor(mContext, R.color.colorTitleText),
                        getProgressHeight())
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        mCurrentUrl = view.getUrl();
                        mTitleBar.setTitleMainText(title);
                    }
                })
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK);
        setAgentWeb(mAgentBuilder);
        mAgentWeb = mAgentBuilder
                .createAgentWeb()//
                .ready()
                .go(url);
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        //设置webView自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        setAgentWeb(mAgentWeb);
    }

    protected void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.web_alert_title)
                    .setMessage(R.string.web_alert_msg)
                    .setNegativeButton(R.string.web_alert_left, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })
                    .setPositiveButton(R.string.web_alert_right, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            mContext.finish();
                        }
                    }).create();
        }
        mAlertDialog.show();
        //show之后可获取对应Button对象设置文本颜色--show之前获取对象为null
        mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    protected void showActionSheet() {
        if (mActionSheetView == null) {
            mActionSheetView = new ActionSheetDialog.ListSheetBuilder(mContext)
                    .addItems(R.array.arrays_banner_all)
                    .setOnItemClickListener(new ActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onClick(BaseDialog dialog, View itemView, int i) {
                            switch (i) {
                                case 0:
                                    mAgentWeb.getUrlLoader().reload();
                                    break;
                                case 1:
                                    TourCooUtil.copyToClipboard(mContext, mCurrentUrl);
                                    ToastUtil.show("复制成功");
                                    break;
                                case 2:
                                    TourCooUtil.startShareText(mContext, mCurrentUrl);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .setCancel("取消")
                    .setTextSizeUnit(TypedValue.COMPLEX_UNIT_DIP)
                    .create();
        }
        mActionSheetView.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mAgentWeb != null && mAgentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
}

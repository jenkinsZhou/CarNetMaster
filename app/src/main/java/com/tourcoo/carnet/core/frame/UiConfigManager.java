package com.tourcoo.carnet.core.frame;


import android.app.Activity;
import android.app.Application;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.ExceptionConstant;
import com.tourcoo.carnet.core.frame.impl.NaViLifecycleCallbacks;
import com.tourcoo.carnet.core.frame.interfaces.ActivityDispatchEventControl;
import com.tourcoo.carnet.core.frame.interfaces.ActivityFragmentControl;
import com.tourcoo.carnet.core.frame.interfaces.ActivityKeyEventControl;
import com.tourcoo.carnet.core.frame.interfaces.HttpRequestControl;
import com.tourcoo.carnet.core.frame.interfaces.ILoadingDialog;
import com.tourcoo.carnet.core.frame.interfaces.LoadMoreFoot;
import com.tourcoo.carnet.core.frame.interfaces.MultiStatusView;
import com.tourcoo.carnet.core.frame.interfaces.QuitAppControl;
import com.tourcoo.carnet.core.frame.interfaces.RecyclerViewControl;
import com.tourcoo.carnet.core.frame.interfaces.SwipeBackControl;
import com.tourcoo.carnet.core.frame.interfaces.TitleBarViewControl;
import com.tourcoo.carnet.core.frame.interfaces.ToastControl;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.widget.LoadingDialog;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.progress.EmiProgressDialog;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @author :zhoujian
 * @description :UI配置管理类
 * @company :翼迈科技
 * @date 2019年 03月 02日 20时26分
 * @Email: 971613168@qq.com
 */
public class UiConfigManager {

    private static Application mApplication;

    private static String TAG = "FastManager";
    private static volatile UiConfigManager sInstance;

    private UiConfigManager() {
    }

    public static UiConfigManager getInstance() {
        if (sInstance == null) {
            throw new NullPointerException(ExceptionConstant.EXCEPTION_NOT_INIT_FAST_MANAGER);
        }
        return sInstance;
    }

    /**
     * 配置全局通用加载等待Loading提示框
     */
    private ILoadingDialog mILoadingDialog;

    public ILoadingDialog getLoadingDialog() {
        return mILoadingDialog;
    }

    public void setILoadingDialog(ILoadingDialog loadingDialog) {
        this.mILoadingDialog = loadingDialog;
    }


    public UiConfigManager setmToastControl(ToastControl mToastControl) {
        this.mToastControl = mToastControl;
        return this;
    }

    public Application getApplication() {
        return mApplication;
    }

    /**
     * ToastUtil相关配置
     */
    private ToastControl mToastControl;

    private TitleBarViewControl mTitleBarViewControl;

    private SwipeBackControl swipeBackControl;

    /**
     * 配置网络请求
     */
    private HttpRequestControl httpRequestControl;

    public HttpRequestControl getHttpRequestControl() {
        return httpRequestControl;
    }

    public UiConfigManager setHttpRequestControl(HttpRequestControl httpRequestControl) {
        this.httpRequestControl = httpRequestControl;
        return this;
    }

    public LoadMoreFoot getLoadMoreFoot() {
        return loadMoreFoot;
    }

    public MultiStatusView getMultiStatusView() {
        return multiStatusView;
    }

    public UiConfigManager setMultiStatusView(MultiStatusView multiStatusView) {
        this.multiStatusView = multiStatusView;
        return this;
    }

    /**
     * 多状态布局--加载中/空数据/错误/无网络
     */
    private MultiStatusView multiStatusView;

    public UiConfigManager setLoadMoreFoot(LoadMoreFoot loadMoreFoot) {
        this.loadMoreFoot = loadMoreFoot;
        return this;
    }

    /**
     * Adapter加载更多View
     */
    private LoadMoreFoot loadMoreFoot;

    public RecyclerViewControl getRecyclerViewControl() {
        return recyclerViewControl;
    }


    public DefaultRefreshHeaderCreator getDefaultRefreshHeaderCreator() {
        return defaultRefreshHeaderCreator;
    }

    public UiConfigManager setDefaultRefreshHeader(DefaultRefreshHeaderCreator defaultRefreshHeaderCreator) {
        this.defaultRefreshHeaderCreator = defaultRefreshHeaderCreator;
        return this;
    }

    /**
     * SmartRefreshLayout默认刷新头
     */
    private DefaultRefreshHeaderCreator defaultRefreshHeaderCreator;


    public UiConfigManager setRecyclerViewControl(RecyclerViewControl recyclerViewControl) {
        this.recyclerViewControl = recyclerViewControl;
        return this;
    }

    /**
     * 全局设置列表
     */
    private RecyclerViewControl recyclerViewControl;

    public SwipeBackControl getSwipeBackControl() {
        return swipeBackControl;
    }

    public UiConfigManager setSwipeBackControl(SwipeBackControl swipeBackControl) {
        this.swipeBackControl = swipeBackControl;
        return this;
    }

    public TitleBarViewControl getTitleBarViewControl() {
        return mTitleBarViewControl;
    }

    public UiConfigManager setTitleBarViewControl(TitleBarViewControl mTitleBarViewControl) {
        this.mTitleBarViewControl = mTitleBarViewControl;
        return this;
    }

    public ToastControl getToastControl() {
        return mToastControl;
    }


    public ActivityKeyEventControl getActivityKeyEventControl() {
        return activityKeyEventControl;
    }

    public UiConfigManager setActivityKeyEventControl(ActivityKeyEventControl activityKeyEventControl) {
        this.activityKeyEventControl = activityKeyEventControl;
        return this;
    }

    /**
     * 配置BasisActivity 子类前台时监听按键相关
     */
    private ActivityKeyEventControl activityKeyEventControl;

    /**
     * Activity 主页点击返回键控制
     */
    private QuitAppControl quitAppControl;

    public QuitAppControl getQuitAppControl() {
        return quitAppControl;
    }

    public UiConfigManager setQuitAppControl(QuitAppControl quitAppControl) {
        this.quitAppControl = quitAppControl;
        return this;
    }


    /**
     * 配置Activity/Fragment(背景+Activity强制横竖屏+Activity 生命周期回调+Fragment生命周期回调)
     */
    private ActivityFragmentControl activityFragmentControl;

    public ActivityFragmentControl getActivityFragmentControl() {
        return activityFragmentControl;
    }

    public UiConfigManager setActivityFragmentControl(ActivityFragmentControl activityFragmentControl) {
        this.activityFragmentControl = activityFragmentControl;
        return this;
    }

    public UiConfigManager setActivityDispatchEventControl(ActivityDispatchEventControl activityDispatchEventControl) {
        this.activityDispatchEventControl = activityDispatchEventControl;
        return this;
    }

    /**
     * 配置BasisActivity 子类事件派发相关
     */
    private ActivityDispatchEventControl activityDispatchEventControl;

    public ActivityDispatchEventControl getActivityDispatchEventControl() {
        return activityDispatchEventControl;
    }


    public static UiConfigManager init(Application application) {
        //保证只执行一次初始化属性
        if (mApplication == null && application != null) {
            mApplication = application;
            sInstance = new UiConfigManager();
            //预设置LoadDialog属性
            sInstance.setLoadingDialog(new ILoadingDialog() {
                @Nullable
                @Override
                public LoadingDialog createLoadingDialog(@Nullable Activity activity) {
                    return new LoadingDialog(activity,
                            new EmiProgressDialog.WeBoBuilder(activity)
                                    .setMessage(R.string.load_more_loading)
                                    .create());
                }
            });
            //设置滑动返回监听
            BGASwipeBackHelper.init(mApplication, null);
            //注册activity生命周期
            mApplication.registerActivityLifecycleCallbacks(new NaViLifecycleCallbacks());
            //初始化Toast工具
            ToastUtil.init(mApplication);
            //初始化Glide
            GlideManager.setPlaceholderColor(ContextCompat.getColor(mApplication, R.color.colorPlaceholder));
            GlideManager.setPlaceholderRoundRadius(mApplication.getResources().getDimension(R.dimen.dp_placeholder_radius));
        }
        return getInstance();
    }


    /**
     * 设置全局网络请求等待Loading提示框如登录等待loading
     * 最终调用{@link com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver#BaseLoadingObserver(Activity)}
     *
     * @param control
     * @return
     */
    public UiConfigManager setLoadingDialog(ILoadingDialog control) {
        if (control != null) {
            this.mILoadingDialog = control;
        }
        return this;
    }

    /**
     * 配置ToastUtil
     *
     * @param control
     * @return
     */
    public UiConfigManager setToastControl(ToastControl control) {
        mToastControl = control;
        return this;
    }
}
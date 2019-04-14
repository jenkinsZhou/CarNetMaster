package com.tourcoo.carnet;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.tourcoo.carnet.core.crash.CrashManager;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.impl.ActivityControlImpl;
import com.tourcoo.carnet.core.frame.impl.HttpRequestControlImpl;
import com.tourcoo.carnet.core.frame.impl.SwipeBackControlImpl;
import com.tourcoo.carnet.core.frame.impl.UiConfigImpl;
import com.tourcoo.carnet.core.frame.retrofit.TourCoolRetrofit;
import com.tourcoo.carnet.core.frame.util.SizeUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.log.widget.LogFileEngineFactory;
import com.tourcoo.carnet.core.log.widget.config.LogLevel;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;

import static com.tourcoo.carnet.core.common.CommonConfig.DEBUG_MODE;
import static com.tourcoo.carnet.core.common.CommonConstant.TAG_PRE_SUFFIX;
import static com.tourcoo.carnet.core.common.RequestConfig.BASE_URL;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 01日 21时54分
 * @Email: 971613168@qq.com
 */
public class CarNetApplication extends LitePalApplication {
    private static Application mContext;
    private static int imageHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initLog();
        initCrashHandle();
        ToastUtil.init(mContext);
        //最简单UI配置模式-必须进行初始化
        UiConfigManager.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //以下为更丰富自定义方法
        //全局UI配置参数-按需求设置
        UiConfigImpl impl = new UiConfigImpl(mContext);
        ActivityControlImpl activityControl = new ActivityControlImpl();
        UiConfigManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //全局设置RecyclerView
                .setRecyclerViewControl(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
                .setSwipeBackControl(new SwipeBackControlImpl())
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(activityControl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(activityControl)
                //配置BasisActivity 子类事件派发相关
                .setActivityDispatchEventControl(activityControl)
                //设置http请求结果全局控制
                .setHttpRequestControl(new HttpRequestControlImpl())
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl)
                //设置ToastUtil全局控制
                .setToastControl(impl);

        //初始化Retrofit配置
        TourCoolRetrofit.getInstance()
                //配置全局网络请求BaseUrl
                //todo
//                .setBaseUrl("https://api.douban.com/")
                .setBaseUrl(BASE_URL)
                //信任所有证书--也可设置setCertificates(单/双向验证)
                .setCertificates()
                //设置统一请求头
//                .addHeader(header)
//                .addHeader(key,value)
                //设置请求全局log-可设置tag及Level类型
                .setLogEnable(true)
//                .setLogEnable(BuildConfig.DEBUG, TAG, HttpLoggingInterceptor.Level.BODY)
                //设置统一超时--也可单独调用read/write/connect超时(可以设置时间单位TimeUnit)
                //默认10 s
                .setTimeout(10);

        //注意设置baseUrl要以/ 结尾 service 里的方法不要以/打头不然拦截到的url会有问题
        //以下为配置多BaseUrl--默认方式一优先级高 可通过FastRetrofit.getInstance().setHeaderPriorityEnable(true);设置方式二优先级
        //方式一 通过Service 里的method-(如:) 设置 推荐 使用该方式不需设置如方式二的额外Header


        //内存泄漏检测工具
        if (LeakCanary.isInAnalyzerProcess(mContext)) {
            return;
        }
        LeakCanary.install(mContext);
    }

    public static Application getInstance() {
        return mContext;
    }

    /**
     * 异常机制初始化
     */
    private void initCrashHandle() {
        //todo：暂时不将错误日志写入到文件
        CrashManager.init(mContext);
    }

    /**
     * 初始化日志配置
     */
    private void initLog() {
        TourcooLogUtil.getLogConfig()
                .configAllowLog(DEBUG_MODE)
                .configTagPrefix(TAG_PRE_SUFFIX)
                .configShowBorders(false).
                configLevel(LogLevel.TYPE_VERBOSE);
        // 支持输入日志到文件
        String filePath = Environment.getExternalStorageDirectory() + "/CarNetMaster/logs/";
        TourcooLogUtil.getLogFileConfig().configLogFileEnable(DEBUG_MODE)
                .configLogFilePath(filePath)
                .configLogFileLevel(LogLevel.TYPE_VERBOSE)
                .configLogFileEngine(new LogFileEngineFactory(this));
    }

    /**
     * 是否控制底部导航栏---目前发现小米8上检查是否有导航栏出现问题
     *
     * @return
     */
    public static boolean isControlNavigation() {
        TourcooLogUtil.i("CarNetApplication", "mode:" + Build.MODEL);
        return true;
    }


    /**
     * 获取banner及个人中心设置ImageView宽高
     *
     * @return
     */
    public static int getImageHeight() {
        imageHeight = (int) (SizeUtil.getScreenWidth() * 0.55);
        return imageHeight;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

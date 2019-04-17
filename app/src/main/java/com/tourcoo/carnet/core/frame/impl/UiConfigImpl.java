package com.tourcoo.carnet.core.frame.impl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.interfaces.ILoadingDialog;
import com.tourcoo.carnet.core.frame.interfaces.IRefreshLoadView;
import com.tourcoo.carnet.core.frame.interfaces.LoadMoreFoot;
import com.tourcoo.carnet.core.frame.interfaces.MultiStatusView;
import com.tourcoo.carnet.core.frame.interfaces.QuitAppControl;
import com.tourcoo.carnet.core.frame.interfaces.RecyclerViewControl;
import com.tourcoo.carnet.core.frame.interfaces.TitleBarViewControl;
import com.tourcoo.carnet.core.frame.interfaces.ToastControl;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.frame.widget.LoadingDialog;
import com.tourcoo.carnet.core.frame.widget.NaViLoadMoreView;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.util.SizeUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;
import com.tourcoo.carnet.core.widget.core.view.radius.RadiusTextView;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.luck.picture.lib.dialog.PictureDialog;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :zhoujian
 * @description :应用全局配置管理实现
 * @company :翼迈科技
 * @date 2019年 03月 05日 22时30分
 * @Email: 971613168@qq.com
 */
public class UiConfigImpl implements DefaultRefreshHeaderCreator, LoadMoreFoot,
        RecyclerViewControl, MultiStatusView, ILoadingDialog,
        TitleBarViewControl, QuitAppControl, ToastControl {
    private Context mContext;
    private String TAG = this.getClass().getSimpleName();

    public UiConfigImpl(@Nullable Context context) {
        this.mContext = context;
    }

    /**
     * 下拉刷新头配置
     *
     * @param context
     * @param layout
     * @return
     */
    @NonNull
    @Override
    public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
        layout.setEnableHeaderTranslationContent(true)
                .setEnableOverScrollDrag(true);
        return new ClassicsHeader(mContext);
    }

    /**
     * Adapter加载更多配置
     *
     * @param adapter
     * @return
     */
    @Nullable
    @Override
    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
        if (adapter != null) {
            //设置动画是否一直开启
            adapter.isFirstOnly(false);
            //设置动画
//            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        }

        //默认配置请参考LoadMoreView.Builder(mContext)里初始化
        return new NaViLoadMoreView.Builder(mContext)
                .setLoadingTextFakeBold(true)
                .setLoadingSize(SizeUtil.dp2px(20))
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置Loading 颜色-5.0以上有效
//                                .setLoadingProgressColor(Color.MAGENTA)
//                                //设置Loading drawable--会使Loading颜色失效
//                                .setLoadingProgressDrawable(R.drawable.dialog_loading_wei_bo)
//                                //设置全局TextView颜色
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置全局TextView文字字号
//                                .setLoadTextSize(SizeUtil.dp2px(14))
//                                .setLoadingText("努力加载中...")
//                                .setLoadingTextColor(Color.GREEN)
//                                .setLoadingTextSize(SizeUtil.dp2px(14))
//                                .setLoadEndText("我是有底线的")
//                                .setLoadEndTextColor(Color.GREEN)
//                                .setLoadEndTextSize(SizeUtil.dp2px(14))
//                                .setLoadFailText("哇哦!出错了")
//                                .setLoadFailTextColor(Color.RED)
//                                .setLoadFailTextSize(SizeUtil.dp2px(14))
                .build();
        //方式二:使用adapter自带--其实我默认设置的和这个基本一致只是提供了相应设置方法
//                        return new SimpleLoadMoreView();
        //方式三:参考SimpleLoadMoreView或FastLoadMoreView完全自定义自己的LoadMoreView
//                        return MyLoadMoreView();
    }

    /**
     * 全局设置
     *
     * @param recyclerView
     * @param cls
     */
    @Override
    public void setRecyclerView(RecyclerView recyclerView, Class<?> cls) {
        TourCooLogUtil.i(TAG, "setRecyclerView-" + cls.getSimpleName() + "context:" + recyclerView.getContext() + ";:" + (Activity.class.isAssignableFrom(recyclerView.getContext().getClass())) + ";:" + (recyclerView.getContext() instanceof Activity));
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView, IRefreshLoadView iFastRefreshLoadView) {
    }

    @Nullable
    @Override
    public LoadingDialog createLoadingDialog(@Nullable Activity activity) {
//        return new FastLoadDialog(activity,
//                new UIProgressDialog.WeBoBuilder(activity)
//                        .setMessage("加载中")
//                        .create())
//                .setCanceledOnTouchOutside(false)
//                .setMessage("请求数据中,请稍候...");
        //注意使用UIProgressDialog时最好在Builder里设置提示文字setMessage不然后续再设置文字信息也不会显示
//        return new FastLoadDialog(activity, new UIProgressDialog.WeChatBuilder(activity)
//                .setBackgroundColor(Color.parseColor("#FCFCFC"))
////                .setMinHeight(SizeUtil.dp2px(140))
////                .setMinWidth(SizeUtil.dp2px(270))
//                .setTextSizeUnit(TypedValue.COMPLEX_UNIT_PX)
//                .setMessage(R.string.fast_loading)
//                .setLoadingSize(SizeUtil.dp2px(30))
//                .setTextSize(SizeUtil.dp2px(16f))
//                .setTextPadding(SizeUtil.dp2px(10))
//                .setTextColorResource(R.color.colorTextGray)
//                .setIndeterminateDrawable(FastUtil.getTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.dialog_loading), ContextCompat.getColor(mContext, R.color.colorTitleText)))
//                .setBackgroundRadius(SizeUtil.dp2px(6f))
//                .create());
        Dialog dialog = new PictureDialog(activity);
        return new LoadingDialog(activity, dialog)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false);
    }

    /**
     * 控制全局TitleBarView
     *
     * @param titleBar
     * @return
     */
    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
      /*  Drawable mDrawable = TourcooUtil.getTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_back),
                ContextCompat.getColor(mContext, R.color.colorTitleText));*/
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();

//        boolean isActivity = Activity.class.isAssignableFrom(cls);

        Activity activity = StackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                .setLeftTextDrawableHeight(SizeUtil.dp2px(17)).setLeftTextDrawableWidth(SizeUtil.dp2px(10))
//                .setLeftTextDrawable(isActivity ? mDrawable : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0);
        if (activity != null) {
            titleBar.setTitleMainText(activity.getTitle());
        }
        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_elevation));
        return false;
    }

    /**
     * @param isFirst  是否首次提示
     * @param activity 操作的Activity
     * @return 延迟间隔--如不需要设置两次提示可设置0--最佳方式是直接在回调中执行你想要的操作
     */
    @Override
    public long quitApp(boolean isFirst, Activity activity) {
        //默认配置
        if (isFirst) {
            ToastUtil.show(R.string.quit_app);
        } else {
            StackUtil.getInstance().exit(false);
        }
        return 2000;
    }


    @Override
    public Toast getToast() {
        return null;
    }

    @Override
    public void setToast(Toast toast, RadiusTextView textView) {

    }
}

package com.tourcoo.carnet.ui.repair;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcoo.carnet.CarNetApplication;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.CommentAdapter;
import com.tourcoo.carnet.adapter.GoodFieldAdapter;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.base.activity.WebViewActivity;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.NetworkUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.CommentEntity;
import com.tourcoo.carnet.entity.garage.CommentInfo;
import com.tourcoo.carnet.entity.garage.GarageInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.HttpException;

import static com.tourcoo.carnet.core.common.CommonConfig.DEBUG_MODE;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :修理厂详情
 * @company :途酷科技
 * @date 2019年03月25日18:42
 * @Email: 971613168@qq.com
 */
public class RepairFactoryDetailActivity extends BaseTourCooTitleActivity implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener{
    protected BGABanner banner;
    private List<String> mImageList = new ArrayList<>();
    private GarageInfo garageInfo;
    private SmartRefreshLayout smartRefreshLayout;
    private TextView tvGarageName;
    private TextView tvGarageDescription;
    private int pageSize = 10;
    private RecyclerView rvGoodsField;
    private RecyclerView rvComment;
    private View emptyView;
    private LinearLayout llRefresh;
    private int refreshFlag = LOAD_MORE;
    private static final int LOAD_MORE = 1;
    private static final int REFRESH = 2;
    private StatusLayoutManager mStatusLayoutManager;
    private int currentPage = 1;

    /**
     * 擅长领域
     */
    private GoodFieldAdapter mGoodFieldAdapter;

    /**
     * 评论列表适配器
     */
    private CommentAdapter mCommentAdapter;
    private List<String> tagList = new ArrayList<>();
    public static final String  EXTRA_GARAGE_DETAIL = "EXTRA_GARAGE_DETAIL";

    @Override
    public int getContentLayout() {
        return R.layout.activity_repair_factory_detail_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        banner = findViewById(R.id.bgaBanner);
        findViewById(R.id.rlCallPhone).setOnClickListener(this);
        rvComment = findViewById(R.id.rvComment);
        rvGoodsField = findViewById(R.id.rvGoodsField);
        rvGoodsField.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvComment.setLayoutManager(new LinearLayoutManager(mContext));
        mGoodFieldAdapter = new GoodFieldAdapter(tagList);
        tvGarageDescription = findViewById(R.id.tvGarageDescription);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        tvGarageName = findViewById(R.id.tvGarageName);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext).setSpinnerStyle(SpinnerStyle.Translate));
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setEnableRefresh(false);
        banner.setAutoPlayAble(false);
        garageInfo = (GarageInfo) getIntent().getExtras().getSerializable(EXTRA_GARAGE_DETAIL);
        showDetail();
        if (garageInfo != null && !TextUtils.isEmpty(garageInfo.getImages())) {
            String[] images = garageInfo.getImages().split(",");
            for (String image : images) {
                mImageList.add(RequestConfig.BASE + image);
            }
        }

        initTagList(garageInfo);
        mGoodFieldAdapter.bindToRecyclerView(rvGoodsField);
        setBanner(mImageList);
        initCommentAdapter();
        initEmptyView();
//        setLoadMoreView();
        initStatusManager();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("修理厂详情");
    }


    @Override
    public void loadData() {
        super.loadData();
        findGarageCommentList(garageInfo.getId(), 1, pageSize);
    }




    private void refreshComment() {
        findGarageCommentList(garageInfo.getId(), 1, pageSize);
    }

    private void loadMoreComment(int page) {
          TourCooLogUtil.i(TAG, "当前请求的页码:"+page+"---当前每页请求数量:"+pageSize);
        findGarageCommentList(garageInfo.getId(), page, pageSize);
    }

    private void setBanner(List<String> images) {
        banner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                GlideManager.loadImg(model, (ImageView) itemView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                WebViewActivity.start(mContext, model.toString(), false);
            }
        });
        banner.setData(images, null);
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshFlag = REFRESH;
        currentPage = 1;
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setNoMoreData(false);
        refreshComment();
    }


    private void showDetail() {
        if (garageInfo != null) {
            tvGarageName.setText(TourCooUtil.getNotNullValue(garageInfo.getName()));
            tvGarageDescription.setText(TourCooUtil.getNotNullValue(garageInfo.getSynopsis()));
        } else {
            ToastUtil.showFailed("获取失败");
        }
    }

    /**
     * 获取修理厂评价
     *
     * @param garageId
     * @param index
     * @param pageSize
     */
    private void findGarageCommentList(String garageId, int index, int pageSize) {
        ApiRepository.getInstance().findGarageCommentList(garageId, index, pageSize).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<CommentEntity>>() {
                    @Override
                    public void onRequestNext(BaseEntity<CommentEntity> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                httpRequestSuccessCallback(entity.data.getElements());
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        handleError(e);
                    }
                });

    }


    private void initTagList(GarageInfo garageInfo) {
        if (garageInfo == null || garageInfo.getTagNames() == null) {
            return;
        }
        tagList.addAll(Arrays.asList(garageInfo.getTagNames().split(",")));
        for (int i = tagList.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(tagList.get(i))) {
                tagList.remove(i);
            }
        }
    }

    private void showPhoneDialog(GarageInfo garageInfo) {
        //客服电话
        if (garageInfo == null || TextUtils.isEmpty(garageInfo.getMobile())) {
            ToastUtil.show("该车主没有设置联系方式");
            return;
        }
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessageGravity(Gravity.CENTER_HORIZONTAL);
        builder.setTitle("联系修理厂").setFirstMessage(garageInfo.getMobile())
                .setFirstTextColor(TourCooUtil.getColor(R.color.blueCommon))
                .setFirstMsgSize(15).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        call(garageInfo.getMobile());
//                        ApiRepository.getInstance().updateApp()
                    }
                });
        showConfirmDialog(builder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCallPhone:
                showPhoneDialog(garageInfo);
                break;
            default:
                break;
        }
    }


    private void initEmptyView() {
        emptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_view_empty, null);
        llRefresh = emptyView.findViewById(R.id.llRefresh);
    }






    private void initCommentAdapter() {
        mCommentAdapter = new CommentAdapter();
        mCommentAdapter.removeAllFooterView();
        mCommentAdapter.bindToRecyclerView(rvComment);
    }


    private void handleError(Throwable e) {
        smartRefreshLayout.setEnableLoadMore(false);
        TourCooLogUtil.e(TAG, e.toString());
        int reason = R.string.exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtil.isConnected(CarNetApplication.getContext())) {
            reason = R.string.exception_network_not_connected;
        } else {
            //网络异常--继承于AccountsException
            if (e instanceof NetworkErrorException) {
                reason = R.string.exception_network_error;
                //账户异常
            } else if (e instanceof AccountsException) {
                reason = R.string.exception_accounts;
                //连接异常--继承于SocketException
            } else if (e instanceof ConnectException) {
                reason = R.string.exception_connect;
                //socket异常
            } else if (e instanceof SocketException) {
                reason = R.string.exception_socket;
                // http异常
            } else if (e instanceof HttpException) {
                reason = R.string.exception_http;
                //DNS错误
            } else if (e instanceof UnknownHostException) {
                reason = R.string.exception_unknown_host;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {
                //数据格式化错误
                reason = R.string.exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.exception_class_cast;
            }
        }
        if (mStatusLayoutManager == null) {
            if (DEBUG_MODE) {
                ToastUtil.show(reason);
            } else {
                ToastUtil.show("服务器异常");
            }
            return;
        }
        int page = currentPage;
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(false);
        }
        if (mCommentAdapter != null) {
            mCommentAdapter.loadMoreComplete();
            if (mStatusLayoutManager == null) {
                return;
            }
            //初始页
            if (page == 1) {
                if (!NetworkUtil.isConnected(CarNetApplication.getContext())) {
                    //可自定义网络错误页面展示
                    mStatusLayoutManager.showErrorLayout();
//                    mStatusLayoutManager.showCustomLayout(R.layout.layout_status_layout_manager_error);
                } else {
                    mStatusLayoutManager.showErrorLayout();
                }
                return;
            }
            //可根据不同错误展示不同错误布局  showCustomLayout(R.layout.xxx);
            mStatusLayoutManager.showErrorLayout();
        }
    }


    private void initStatusManager() {
        //优先使用当前配置
        View contentView = rvComment;
        if (contentView == null) {
            return;
        }
        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(contentView)
                .setDefaultLayoutsBackgroundColor(android.R.color.transparent)
                .setDefaultEmptyText(R.string.multi_empty)
                .setDefaultEmptyClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setDefaultErrorText(R.string.multi_error)
                .setDefaultErrorClickViewTextColor(contentView.getResources().getColor(R.color.colorTitleText))
                .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                    @Override
                    public void onEmptyChildClick(View view) {
                        mStatusLayoutManager.showLoadingLayout();
                        TourCooLogUtil.d(TAG, "点击了");
                        refreshComment();
                    }

                    @Override
                    public void onErrorChildClick(View view) {
                          TourCooLogUtil.e(TAG, "点击了");
                        mStatusLayoutManager.showLoadingLayout();
                        refreshComment();
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        TourCooLogUtil.i(TAG, "点击了");
                        mStatusLayoutManager.showLoadingLayout();
                        refreshComment();
                    }
                });
        mStatusLayoutManager = builder.build();
        mStatusLayoutManager.showLoadingLayout();
    }


    private void httpRequestSuccessCallback(List<CommentInfo> list) {
        int page = currentPage;
        int size = pageSize;
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
        if (mCommentAdapter == null) {
            return;
        }
        if (list == null || list.size() == 0) {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.setEnableLoadMore(false);
            }
            //第一页没有
            if (page == 1) {
                mCommentAdapter.setNewData(new ArrayList<>());
                mStatusLayoutManager.showEmptyLayout();
            } else {
                mCommentAdapter.loadMoreEnd();
            }
            return;
        }
        mStatusLayoutManager.showSuccessLayout();
        boolean refresh = smartRefreshLayout != null && (smartRefreshLayout.isRefreshing() || page == 1);
        if (refresh) {
            mCommentAdapter.setNewData(new ArrayList<>());
        }
        mCommentAdapter.addData(list);
        mCommentAdapter.loadMoreComplete();
        if (list.size() < size) {
            mCommentAdapter.loadMoreEnd();
            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.finishLoadMore();
        } else {
            smartRefreshLayout.setEnableLoadMore(true);
        }
        smartRefreshLayout.finishLoadMore();
    }





    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        currentPage++;
        TourCooLogUtil.i("触发了onLoadMoreRequested:请求的页码:" + currentPage);
        refreshFlag = LOAD_MORE;
        mStatusLayoutManager.showSuccessLayout();
        loadMoreComment(currentPage);
    }

    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

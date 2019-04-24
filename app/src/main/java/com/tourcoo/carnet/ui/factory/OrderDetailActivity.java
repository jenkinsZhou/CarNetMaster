package com.tourcoo.carnet.ui.factory;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.common.OrderConstant;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.interfaces.IMultiStatusView;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.util.SizeUtil;
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.threadpool.ThreadPoolManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.confirm.PayDialog;
import com.tourcoo.carnet.core.widget.core.action.ActionSheetDialog;
import com.tourcoo.carnet.core.widget.core.action.BaseDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.WeiXinPay;
import com.tourcoo.carnet.entity.car.PayInfo;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;
import com.tourcoo.carnet.entity.order.OrderDetail;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.BaseTourCooTitleMultiViewActivity;
import com.tourcoo.carnet.ui.order.LookEvaluationActivity;
import com.tourcoo.carnet.ui.repair.FillEvaluationActivity;
import com.tourcoo.carnet.ui.repair.LookServiceActivity;
import com.tourcoo.carnet.utils.Location;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tourcoo.carnet.core.common.OrderConstant.EXTRA_ORDER_TAG_REPAIR;
import static com.tourcoo.carnet.core.common.OrderConstant.EXTRA_ORDER_TAG_SERVICE;
import static com.tourcoo.carnet.core.common.OrderConstant.PAY_TYPE_ALI_PAY;
import static com.tourcoo.carnet.core.common.OrderConstant.PAY_TYPE_WEI_XIN_PAY;
import static com.tourcoo.carnet.core.common.OrderConstant.TAB_KEY;
import static com.tourcoo.carnet.core.common.OrderConstant.TAB_REPAIR;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.common.WxConfig.APP_ID;
import static com.tourcoo.carnet.core.util.TourCooUtil.checkMapAppsIsExist;
import static com.tourcoo.carnet.core.widget.confirm.PayDialog.PAY_TYPE_ALI;
import static com.tourcoo.carnet.core.widget.confirm.PayDialog.PAY_TYPE_WE_XIN;
import static com.tourcoo.carnet.entity.event.EventConstant.EVENT_ACTION_PAY_FRESH_FAILED;
import static com.tourcoo.carnet.entity.event.EventConstant.EVENT_ACTION_PAY_FRESH_SUCCESS;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CANCELED;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CLOSE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_FINISH;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_IN_SERVICE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_EVALUATE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_ORDER;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_PAY;
import static com.tourcoo.carnet.ui.order.HistoryServiceListFragment.EXTRA_SERVICE_DETAIL;
import static com.tourcoo.carnet.ui.order.LookEvaluationActivity.EXTRA_ORDER_ID;
import static com.tourcoo.carnet.ui.repair.HistoryFaultRepairListFragment.CODE_REQUEST_FILL;

/**
 * @author :JenkinsZhou
 * @description :订单详情(上门服务详情)
 * @company :途酷科技
 * @date 2019年03月22日16:54
 * @Email: 971613168@qq.com
 */
public class OrderDetailActivity extends BaseTourCooTitleMultiViewActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, IMultiStatusView {
    public static final String EXTRA_IS_CLICK = "EXTRA_IS_CLICK";
    public static final String EXTRA_ORDER_STATUS = "EXTRA_ORDER_STATUS";
    private int currentOrderStatus;
    /**
     * 记录用户是否操做过
     */
    private boolean isClick = false;

    private static final String PAY_STATUS = "resultStatus";
    private static final String PAY_STATUS_SUCCESS = "9000";
    private PaymentHandler mHandler = new PaymentHandler(this);
    private IWXAPI api;
    private int mPayType;
    private GridImageAdapter gridImageAdapter;
    private RecyclerView faultImageRecyclerView;
    private List<String> imageUrList = new ArrayList<>();
    private TextView tvFirstFunction;
    private TextView tvSecondFunction;
    private TextView tvOrderStatus;
    private TextView tvOrderNumber;
    private TextView tvAddress;
    private FaultRepairEntity.FaultRepairInfo mFaultRepairInfo;
    private TextView tvFaultContent;
    private TextView tvRepairFactory;
    /**
     * 付款金额
     */
    private double paymentAmount;

    private static final int SDK_PAY_FLAG = 1001;
    private String mOrderId;


    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;


    private Location mStart, mEnd;

    //百度地图URL跳转到步行路线的参数

    //头部 添加相应地区
    private final static String BAIDU_HEAD = "baidumap://map/direction?region=0";
    //起点的经纬度
    private final static String BAIDU_ORIGIN = "&origin=";
    //终点的经纬度
    private final static String BAIDU_DESTINATION = "&destination=";
    //路线规划方式
    private final static String BAIDU_MODE = "&mode=walking";
    //百度地图的包名
    private final static String BAIDU_PKG = "com.baidu.BaiduMap";

    //高德地图URL跳转到步行路线的参数

    //头部 后面的sourceApplicaiton填自己APP的名字
    private final static String GAODE_HEAD = "androidamap://route?sourceApplication=BaiduNavi";
    //起点经度
    private final static String GAODE_SLON = "&slon=";
    //起点纬度
    private final static String GAODE_SLAT = "&slat=";
    //起点名字
    private final static String GAODE_SNAME = "&sname=";
    //终点经度
    private final static String GAODE_DLON = "&dlon=";
    //终点纬度
    private final static String GAODE_DLAT = "&dlat=";
    //终点名字
    private final static String GAODE_DNAME = "&dname=";
    // dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
    // t = 1(公交) =2（驾车） =4(步行)
    private final static String GAODE_MODE = "&dev=0&t=4";
    //高德地图包名
    private final static String GAODE_PKG = "com.autonavi.minimap";

    //腾讯地图URL跳转到路线的参数

    //头部 type出行方式
    private final static String TX_HEAD = "qqmap://map/routeplan?type=walk";
    //起点名称
    private final static String TX_FROM = "&from=";
    //起点的经纬度
    private final static String TX_FROMCOORD = "&fromcoord=";
    //终点名称
    private final static String TX_TO = "&to=";
    //终点的经纬度
    private final static String TX_TOCOORD = "&tocoord=";
    /**
     * 本参数取决于type参数的取值
     * 公交：type=bus，policy有以下取值
     * 0：较快捷
     * 1：少换乘
     * 2：少步行
     * 3：不坐地铁
     * 驾车：type=drive，policy有以下取值
     * 0：较快捷
     * 1：无高速
     * 2：距离
     * policy的取值缺省为0
     */
    private final static String TX_END = "&policy=1&referer=myapp";
    //腾讯地图包名
    private final static String TX_PKG = "com.tencent.map";

    private double myLat;
    private double myLong;

    private String myAddress = "";

    private LinearLayout llPayInfo;

    private TextView tvPayType;
    private TextView tvPayForm;
    private TextView tvPayAmount;

    private LinearLayout llPayAmount;
    private LinearLayout llPayForm;
    private LinearLayout llPayType;

    private LinearLayout llContentView;
    private TextView repairFactory;
    private int currentTab;

    @Override
    public int getContentLayout() {
        return R.layout.activity_door_to_door_service_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //在这里可以不传AppId传null就可以
        currentTab = getIntent().getIntExtra(TAB_KEY, TAB_REPAIR);
        TourCooLogUtil.i(TAG, TAG + ":" + "currentTab=" + currentTab);
        api = WXAPIFactory.createWXAPI(mContext, null);
        llContentView = findViewById(R.id.llContentView);
        faultImageRecyclerView = findViewById(R.id.faultImageRecyclerView);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvAddress = findViewById(R.id.tvAddress);
        llPayInfo = findViewById(R.id.llPayInfo);
        llPayType = findViewById(R.id.llPayType);
        llPayForm = findViewById(R.id.llPayForm);
        llPayAmount = findViewById(R.id.llPayAmount);
        tvRepairFactory = findViewById(R.id.tvRepairFactory);
        tvRepairFactory.setOnClickListener(this);
        tvPayType = findViewById(R.id.tvPayType);
        tvPayForm = findViewById(R.id.tvPayForm);
        tvPayAmount = findViewById(R.id.tvPayAmount);
        mFaultRepairInfo = (FaultRepairEntity.FaultRepairInfo) getIntent().getSerializableExtra(EXTRA_SERVICE_DETAIL);
        tvFirstFunction = findViewById(R.id.tvFirstFunction);
        repairFactory = findViewById(R.id.repairFactory);
        tvSecondFunction = findViewById(R.id.tvSecondFunction);
        gridImageAdapter = new GridImageAdapter(imageUrList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
        tvFaultContent = findViewById(R.id.tvFaultContent);
        EventBus.getDefault().register(this);
        faultImageRecyclerView.setLayoutManager(manager);
        faultImageRecyclerView.setNestedScrollingEnabled(false);
        //由于尺寸固定所以设置为true,提高性能
        faultImageRecyclerView.setHasFixedSize(true);
        gridImageAdapter.bindToRecyclerView(faultImageRecyclerView);
    }

    @Override
    public void loadData() {
        super.loadData();
        getLocate();
        requestPermission();
        gridImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onThumbnailClick(view, gridImageAdapter.getData().get(position));
            }
        });
        if (mFaultRepairInfo != null) {
            mOrderId = mFaultRepairInfo.getId() + "";
            getOrderDetail(mFaultRepairInfo.getId() + "");
        }

    }


    private void doRefreshRequest() {
        mStatusLayoutManager.showLoadingLayout();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFaultRepairInfo != null) {
                    mOrderId = mFaultRepairInfo.getId() + "";
                    getOrderDetail(mFaultRepairInfo.getId() + "");
                }
            }
        }, delayTime);

    }

    @Override
    protected IMultiStatusView getMultiStatusView() {
        return this;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("上门服务详情");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFirstFunction:
                break;
            case R.id.tvSecondFunction:
                if (mFaultRepairInfo == null) {
                    ToastUtil.showFailed("未获取到订单信息");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_ORDER_ID, mFaultRepairInfo.getId() + "");
                TourCooLogUtil.i(TAG, "orderId:" + mFaultRepairInfo.getId());
                TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class, bundle);
                break;
            case R.id.tvRepairFactory:
                //定位并显示导航
                if (mStart == null || mStart.getLatitude() == 0 || mStart.getLongitude() == 0) {
                    ToastUtil.show("未获取到您的位置 无法导航");
                    return;
                }
                if (mEnd == null || mEnd.getLatitude() == 0 || mEnd.getLongitude() == 0) {
                    ToastUtil.show("未获取到目的地位置 无法导航 ");
                    return;
                }
                showSheetDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 显示详情
     */
    private void showDetailAndLoadFunction(OrderDetail.OrderInfo info) {
        if (info == null) {
            return;
        }
        String orderNum = info.getOut_trade_no();
        tvOrderNumber.setText(orderNum);
        showStatus(info);
        if (!TextUtils.isEmpty(info.getImages())) {
            List<String> images = Arrays.asList(info.getImages().split(","));
            String imageUrl;
            if (images != null && !images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    imageUrl = RequestConfig.BASE + images.get(i);
                    images.set(i, imageUrl);
                }
                imageUrList.addAll(images);
            }
        }
        gridImageAdapter.notifyDataSetChanged();
        tvAddress.setText(getNotNullValue(info.getAddress()));
        showLocate(tvRepairFactory, getNotNullValue(info.getGarageName()));
        tvFaultContent.setText(getNotNullValue(info.getDetail()));
        if (info.getStatus() == TYPE_STATUS_ORDER_FINISH || info.getStatus() == TYPE_STATUS_ORDER_CANCELED || info.getStatus() == TYPE_STATUS_ORDER_WAIT_ORDER) {
            hideFactory();
        }
        //将请求到的订单状态赋值
        currentOrderStatus = info.getStatus();
        initClickFunctionByStatus(info);
        showPayInfo(info);
    }

    private String getNotNullValue(String value) {
        return TourCooUtil.getNotNullValue(value);
    }

    private void showStatus(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            tvOrderStatus.setText("未知");
            return;
        }
        switch (orderInfo.getStatus()) {
            case TYPE_STATUS_ORDER_WAIT_ORDER:
                String captcha = "订单状态:待接单(验证码:" + orderInfo.getCaptcha() + ")";
                tvOrderStatus.setText(captcha);
                setVisibility(tvFirstFunction, true);
                setVisibility(tvSecondFunction, true);
                setVisibility(llPayInfo, false);
                break;
            case TYPE_STATUS_ORDER_IN_SERVICE:
                tvOrderStatus.setText("订单状态:服务中");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
                setVisibility(llPayInfo, false);
                break;
            case TYPE_STATUS_ORDER_WAIT_PAY:
                tvOrderStatus.setText("订单状态:待支付");
                setVisibility(tvFirstFunction, true);
                setVisibility(tvSecondFunction, true);
                setSolidText(tvFirstFunction, "支付订单");
                setHollowText(tvSecondFunction, "查看服务");
                setVisibility(llPayInfo, false);
                break;
            case TYPE_STATUS_ORDER_WAIT_EVALUATE:
                tvOrderStatus.setText("订单状态:待评价");
                setVisibility(tvFirstFunction, true);
                setVisibility(tvSecondFunction, true);
                setHollowText(tvFirstFunction, "查看服务");
                setSolidText(tvSecondFunction, "去评价");
                setVisibility(llPayInfo, true);
                break;
            case TYPE_STATUS_ORDER_FINISH:
                tvOrderStatus.setText("订单状态:服务完成");
                setVisibility(tvFirstFunction, true);
                setSolidText(tvFirstFunction, "查看服务");
                setVisibility(tvSecondFunction, false);
                setVisibility(llPayInfo, false);
                break;
            case TYPE_STATUS_ORDER_CANCELED:
                tvOrderStatus.setText("订单状态:已取消");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
                setVisibility(llPayInfo, false);
                break;
            case TYPE_STATUS_ORDER_CLOSE:
                tvOrderStatus.setText("订单状态:已关闭");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, true);
                setHollowText(tvSecondFunction, "查看评价");
                setVisibility(llPayInfo, true);
                break;
            default:
                tvOrderStatus.setText("订单状态:未知");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
                setVisibility(llPayInfo, false);
                break;
        }
    }

    /**
     * 获取指定订单详情
     *
     * @param orderId
     */
    private void getOrderDetail(String orderId) {
        imageUrList.clear();
        mStatusLayoutManager.showLoadingLayout();
        ApiRepository.getInstance().findDetail(orderId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<OrderDetail>>() {
                    @Override
                    public void onRequestNext(BaseEntity<OrderDetail> entity) {
                        mStatusLayoutManager.showSuccessLayout();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null && entity.data.getOrder() != null) {
                                    showDetailAndLoadFunction(entity.data.getOrder());
                                    //加载位置信息
                                    setEndPosition(entity.data.getOrder());
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        mStatusLayoutManager.showErrorLayout();
                    }
                });
    }


    private void setVisibility(TextView textView, boolean visible) {
        if (visible) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void setVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置实心文本
     *
     * @param textView
     * @param text
     */
    private void setSolidText(TextView textView, String text) {
        textView.setTextColor(TourCooUtil.getColor(R.color.colorWhite));
        textView.setBackground(TourCooUtil.getDrawable(R.drawable.selector_bg_radius_16_blue));
        textView.setText(text);
        setVisibility(textView, true);
    }


    /**
     * 设置空心文本
     *
     * @param textView
     * @param text
     */
    private void setHollowText(TextView textView, String text) {
        textView.setTextColor(TourCooUtil.getColor(R.color.blueCommon));
        textView.setBackground(TourCooUtil.getDrawable(R.drawable.selector_bg_radius_16_blue_hollow));
        textView.setText(text);
        setVisibility(textView, true);
    }

    private void initClickFunctionByStatus(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            return;
        }
        tvFirstFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                switch (orderInfo.getStatus()) {
                    //查看附近修理厂
                    case TYPE_STATUS_ORDER_WAIT_ORDER:
                        //待接单
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_ORDER_ID, orderInfo.getId() + "");
                        TourCooLogUtil.i(TAG, "orderId:" + orderInfo.getId());
                        TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class, bundle);
                        break;
                    //待支付
                    case TYPE_STATUS_ORDER_WAIT_PAY:
                        findAmount(orderInfo);
                        break;
                    case TYPE_STATUS_ORDER_FINISH:
                        //订单已完成 （查看服务）
                        skipLookServiceActivity(mFaultRepairInfo.getId());
                        break;
                    case TYPE_STATUS_ORDER_WAIT_EVALUATE:
                        //订单已完成 （查看服务）
                        skipLookServiceActivity(mFaultRepairInfo.getId());
                        break;
                    default:
                        break;
                }
            }
        });
        /**
         *
         * 第二个button
         */
        tvSecondFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                switch (orderInfo.getStatus()) {
                    case TYPE_STATUS_ORDER_WAIT_ORDER:
                        //取消订单
                        showCancelDialog(orderInfo);
                        break;
                    case TYPE_STATUS_ORDER_WAIT_EVALUATE:
                        //去评价
                        Intent intent1 = new Intent();
                        intent1.putExtra("FaultRepairInfo", mFaultRepairInfo);
                        intent1.setClass(mContext, FillEvaluationActivity.class);
                        startActivityForResult(intent1, CODE_REQUEST_FILL);
                        break;
                    case TYPE_STATUS_ORDER_CLOSE:
                        //订单已关闭 查看评价
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_ORDER_ID, mFaultRepairInfo.getId() + "");
                        TourCooUtil.startActivity(mContext, LookEvaluationActivity.class, bundle);
                        break;
                    case TYPE_STATUS_ORDER_WAIT_PAY:
                        //查看服务
                        skipLookServiceActivity(mFaultRepairInfo.getId());
                        break;

                    default:
                        break;
                }
            }
        });
    }


    /**
     * 确认取消服务
     */
    private void showCancelDialog(OrderDetail.OrderInfo orderInfo) {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(this);
        builder.setPositiveButtonPosition(ConfirmDialog.RIGHT);
        builder.setTitle("取消服务");
        builder.setFirstMessage("是否取消服务?").setNegativeButtonButtonBold(true);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelOrder(orderInfo);
            }
        });
        builder.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * 取消服务
     */
    private void cancelOrder(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            ToastUtil.show("未获取到订单信息");
        }
        TourCooLogUtil.i(TAG, "mCurrentFaultRepairInfo：id= " + orderInfo.getId());
        ApiRepository.getInstance().cancelOrder(orderInfo.getId() + "").compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                ToastUtil.showSuccess("订单已取消");
                                //刷新状态
                                getOrderDetail(orderInfo.getId());
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private void showPayDialog(OrderDetail.OrderInfo orderInfo) {
        PayDialog payDialog = new PayDialog(mContext, paymentAmount, new PayDialog.PayListener() {
            @Override
            public void pay(int payType, Dialog dialog) {
                mPayType = payType;
                createPay(orderInfo, payType);
                dialog.dismiss();
            }
        });
        payDialog.show();
    }


    /**
     * 支付接口
     */
    private void createPay(OrderDetail.OrderInfo orderInfo, int payType) {
        if (orderInfo == null) {
            ToastUtil.show("未获取到订单信息");
            return;
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("orderId", orderInfo.getId());
        map.put("ownerId", orderInfo.getOwner_id());
        map.put("payType", payType);
        ApiRepository.getInstance().createPay(map).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<PayInfo>>() {
                    @Override
                    public void onRequestNext(BaseEntity<PayInfo> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                PayInfo payInfo = entity.data;
                                if (payInfo != null) {
                                    ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (payType == PAY_TYPE_ALI) {
                                                aliPay(payInfo);
                                            } else if (mPayType == PAY_TYPE_WE_XIN) {
                                                weiChatPay(payInfo);
                                            }
                                        }
                                    });
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    /**
     * 微信支付
     *
     * @param payInfo
     */
    private void weiChatPay(PayInfo payInfo) {
        WeiXinPay weiXinPay = payInfo.getWxPayConf();
        if (weiXinPay != null) {
            PayReq req = new PayReq();
            req.appId = weiXinPay.getAppId();
            req.nonceStr = weiXinPay.getNonceStr();
            req.packageValue = weiXinPay.getExtra();
            req.partnerId = weiXinPay.getPartnerId();
            req.timeStamp = weiXinPay.getTimeStr();
            req.sign = weiXinPay.getSign();
            TourCooLogUtil.d("请求结果", weiXinPay);
            req.prepayId = weiXinPay.getPaymentStr();
            api.registerApp(APP_ID);
            api.sendReq(req);
            if (currentTab == TAB_REPAIR) {
                TourCooLogUtil.d("请求结果", "故障报修的tab");
                OrderConstant.currentOrderTabTag = EXTRA_ORDER_TAG_REPAIR;
            } else {
                TourCooLogUtil.i("请求结果", "上门服务的tab");
                OrderConstant.currentOrderTabTag = EXTRA_ORDER_TAG_SERVICE;
            }
        }
    }

    private void aliPay(PayInfo payInfo) {
        PayTask aliPay = new PayTask(mContext);
        Map<String, String> result = aliPay.payV2(payInfo.getAliPayConf().getPaymentStr(), true);
        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }


    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
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

    /**
     * 权限获取回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // 用户取消了权限弹窗
                if (grantResults.length == 0) {
                    ToastUtil.show("关闭了权限弹窗");
                    return;
                }

                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        ToastUtil.show(getString(R.string.permission_rejected));
                        return;
                    }
                }

                // 所需的权限均正常获取
                ToastUtil.show(getString(R.string.permission_granted));
            }
            default:
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //权限已被用户授予
        getLocate();
        closeLoadingDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
        closeLoadingDialog();
    }

    @Override
    public View getMultiStatusContentView() {
        return llContentView;
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {

    }

    @Override
    public View.OnClickListener getEmptyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefreshRequest();
            }
        };
    }

    @Override
    public View.OnClickListener getErrorClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefreshRequest();
            }
        };
    }

    @Override
    public View.OnClickListener getCustomerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefreshRequest();
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static class PaymentHandler extends Handler {
        private WeakReference<OrderDetailActivity> softReference;

        public PaymentHandler(OrderDetailActivity doorServiceDetailActivity) {
            softReference = new WeakReference<>(doorServiceDetailActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    Map<String, String> result = (Map<String, String>) msg.obj;
                    for (Map.Entry<String, String> stringStringEntry : result.entrySet()) {
                        if (PAY_STATUS.equalsIgnoreCase(stringStringEntry.getKey())) {
                            boolean success = PAY_STATUS_SUCCESS.equals(stringStringEntry.getValue());
                            if (success) {
                                ToastUtil.showSuccess("支付完成");
                                softReference.get().getOrderDetail(softReference.get().mOrderId);
                            } else {
                                ToastUtil.showFailed("支付失败");
                            }
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 获取订单金额
     */
    private void findAmount(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            ToastUtil.showFailed("未获取到订单信息");
            return;
        }
        ApiRepository.getInstance().findAmount(orderInfo.getId() + "").compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<Double>>() {
                    @Override
                    public void onRequestNext(BaseEntity<Double> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                paymentAmount = entity.data;
                                showPayDialog(orderInfo);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    public void onThumbnailClick(View v, String imageUrl) {
// 全屏显示的方法
        /* android.R.style.Theme_Black_NoTitleBar_Fullscreen*/
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imgView = getView();
        dialog.setContentView(imgView);
        dialog.show();
        GlideManager.loadImg(imageUrl, imgView);
// 点击图片消失
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private ImageView getView() {
        ImageView imgView = new ImageView(this);
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        return imgView;
    }

    private void showLocate(TextView tv, String text) {
        SpannableString ss = new SpannableString(text + "  ");
        int len = ss.length();
        //图片
        Drawable d = ContextCompat.getDrawable(mContext, (R.mipmap.ic_positioning));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //构建ImageSpan
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, len - 1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_FILL:
                if (resultCode == RESULT_OK) {
                    getOrderDetail(mOrderId);
                }
                break;
            default:
                break;
        }
    }


    private void showSheetDialog() {
        String[] array = new String[]{"百度地图", "高德地图"};
        new ActionSheetDialog.ListIOSBuilder(this)
                .addItems(array)
                .setItemsTextColorResource(R.color.colorActionSheetItemText)
                .setCancel(R.string.cancel)
                .setCancelMarginTop(SizeUtil.dp2px(8))
                .setCancelTextColorResource(R.color.colorActionSheetItemText)
                .setOnItemClickListener(mOnItemClickListener)
                .create()
//                .setDimAmount(0.6f)
                .show();
    }


    private ActionSheetDialog.OnItemClickListener mOnItemClickListener = new ActionSheetDialog.OnItemClickListener() {
        @Override
        public void onClick(BaseDialog dialog, View itemView, int position) {
            switch (position) {
                case 0:
                    skipBaidu();
                    break;
                case 1:
                    skipGaode();
                    break;
                default:
                    break;

            }
            dialog.dismiss();
        }
    };


    /**
     * 坐标转换
     *
     * @param mStart
     * @param mEnd
     */
    public void BD09ToGCJ02(Location mStart, Location mEnd) {
        LatLng newStart = convertBaiduToGPS(new LatLng(mStart.getLatitude(), mStart.getLongitude()));
        LatLng newEnd = convertBaiduToGPS(new LatLng(mEnd.getLatitude(), mEnd.getLongitude()));
        mStart.setLatitude(newStart.latitude);
        mStart.setLongitude(newStart.longitude);

        mEnd.setLatitude(newEnd.latitude);
        mEnd.setLongitude(newEnd.longitude);

    }

    /**
     * 将百度地图坐标转换成GPS坐标
     *
     * @param sourceLatLng
     * @return
     */
    public LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter(mContext);
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }

    private void getLocate() {
        if (checkLocatePermission()) {
            locate();
        } else {
            showLocatePermissionDialog("请前往权限管理授予定位权限");
        }
    }


    /***
     * 定位
     */

    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    myLat = aMapLocation.getLatitude();
                    myLong = aMapLocation.getLongitude();
                    myAddress = aMapLocation.getAddress();
                    setStartPosition(aMapLocation);
                } else {
                    ToastUtil.showFailed("定位失败");
                }
                closeLoadingDialog();
                LocateHelper.getInstance().stopLocation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }


    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(this, PermissionConstance.PERMS_LOCATE);
    }

    private void setStartPosition(AMapLocation mapLocation) {
        if (mapLocation == null) {
            return;
        }
        mStart = new Location(mapLocation.getLongitude(), mapLocation.getLatitude(), mapLocation.getAddress());
    }

    private void setEndPosition(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            ToastUtil.show("未获取到订单信息");
            return;
        }
        TourCooLogUtil.d("位置:myLong = " + myLong);
        TourCooLogUtil.d("位置:myLat = " + myLat);
        TourCooLogUtil.d("位置:getLng = " + orderInfo.getLng());
        TourCooLogUtil.d("位置:getLat = " + orderInfo.getLat());
        mEnd = new Location(orderInfo.getLng(), orderInfo.getLat(), orderInfo.getAddress());
    }


    private void skipBaidu() {
        Intent intent = new Intent();
        if (checkMapAppsIsExist(mContext, BAIDU_PKG)) {
            intent.setData(Uri.parse(BAIDU_HEAD + BAIDU_ORIGIN + mStart.getLatitude()
                    + "," + mStart.getLongitude() + BAIDU_DESTINATION + mEnd.getLatitude() + "," + mEnd.getLongitude()
                    + BAIDU_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "百度地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipGaode() {
        Intent intent = new Intent();
        if (checkMapAppsIsExist(mContext, GAODE_PKG)) {
            BD09ToGCJ02(mStart, mEnd);
            intent.setAction("android.intent.action.VIEW");
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(GAODE_HEAD + GAODE_SLAT + mStart.getLatitude() + GAODE_SLON + mStart.getLongitude() +
                    GAODE_SNAME + mStart.getName() + GAODE_DLAT + mEnd.getLatitude() + GAODE_DLON + mEnd.getLongitude() +
                    GAODE_DNAME + mEnd.getName() + GAODE_MODE));
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "高德地图未安装", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        //将订单状态回调给上一页面，上个页面根据状态刷新
        TourCooLogUtil.i(TAG, TAG + ":" + "回调出去的状态:" + currentOrderStatus);
        intent.putExtra(EXTRA_ORDER_STATUS, currentOrderStatus);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void skipLookServiceActivity(int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ORDER_ID, orderId);
        TourCooUtil.startActivity(mContext, LookServiceActivity.class, bundle);
    }


    private void showPayInfo(OrderDetail.OrderInfo orderInfo) {
        if (orderInfo == null) {
            setVisibility(llPayInfo, false);
            return;
        }
        switch (orderInfo.getPaytype()) {
            //线上支付
            case 0:
                setVisibility(llPayForm, true);
                setVisibility(llPayAmount, true);
                setVisibility(llPayType, true);
                tvPayType.setText("线上支付");
                switch (orderInfo.getPay_from()) {
                    case PAY_TYPE_WEI_XIN_PAY:
                        tvPayForm.setText("支付宝支付");
                        break;
                    case PAY_TYPE_ALI_PAY:
                        tvPayForm.setText("微信支付");
                        break;
                    default:
                        setVisibility(llPayForm, false);
                        break;
                }
                tvPayAmount.setText("￥ " + orderInfo.getAmount());
                break;
            //线下支付 隐藏支付平台和支付金额
            default:
                setVisibility(llPayForm, false);
                setVisibility(llPayType, true);
                setVisibility(llPayAmount, true);
                tvPayType.setText("线下支付");
                tvPayAmount.setText("￥ " + orderInfo.getAmount());
                break;
        }
    }


    private void hideFactory() {
        repairFactory.setVisibility(View.GONE);
        tvRepairFactory.setVisibility(View.GONE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBaseEvent(BaseEvent event) {
        if (event == null) {
            TourCooLogUtil.w(TAG, "直接拦截（故障报修）");
            return;
        }
        if (EXTRA_ORDER_TAG_SERVICE.equals(OrderConstant.currentOrderTabTag)) {
            return;
        }
        switch (event.id) {
            case EVENT_ACTION_PAY_FRESH_SUCCESS:
                TourCooLogUtil.i(TAG, "接收到回调");
                doRefreshRequest();
                break;
            case EVENT_ACTION_PAY_FRESH_FAILED:
                TourCooLogUtil.i(TAG, "接收到回调");
                doRefreshRequest();
                break;
            default:
                break;
        }
    }


}

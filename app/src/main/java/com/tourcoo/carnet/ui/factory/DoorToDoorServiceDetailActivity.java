package com.tourcoo.carnet.ui.factory;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.threadpool.ThreadPoolManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.confirm.PayDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.WeiXinPay;
import com.tourcoo.carnet.entity.car.PayInfo;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;
import com.tourcoo.carnet.entity.order.OrderDetail;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.order.LookEvaluationActivity;
import com.tourcoo.carnet.ui.repair.FillEvaluationActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.common.WxConfig.APP_ID;
import static com.tourcoo.carnet.core.widget.confirm.PayDialog.PAY_TYPE_ALI;
import static com.tourcoo.carnet.core.widget.confirm.PayDialog.PAY_TYPE_WE_XIN;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CANCELED;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CLOSE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_FINISH;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_IN_SERVICE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_EVALUATE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_ORDER;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_PAY;
import static com.tourcoo.carnet.ui.order.HistoryServiceFragment.EXTRA_SERVICE_DETAIL;
import static com.tourcoo.carnet.ui.order.LookEvaluationActivity.EXTRA_ORDER_ID;
import static com.tourcoo.carnet.ui.repair.HistoryFaultRepairFragment.CODE_REQUEST_FILL;

/**
 * @author :JenkinsZhou
 * @description :上门服务详情
 * @company :途酷科技
 * @date 2019年03月22日16:54
 * @Email: 971613168@qq.com
 */
public class DoorToDoorServiceDetailActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
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

    @Override
    public int getContentLayout() {
        return R.layout.activity_door_to_door_service_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //在这里可以不传AppId传null就可以
        api = WXAPIFactory.createWXAPI(mContext, null);
        faultImageRecyclerView = findViewById(R.id.faultImageRecyclerView);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvAddress = findViewById(R.id.tvAddress);
        tvRepairFactory = findViewById(R.id.tvRepairFactory);
        mFaultRepairInfo = (FaultRepairEntity.FaultRepairInfo) getIntent().getSerializableExtra(EXTRA_SERVICE_DETAIL);
        tvFirstFunction = findViewById(R.id.tvFirstFunction);
        tvSecondFunction = findViewById(R.id.tvSecondFunction);
        gridImageAdapter = new GridImageAdapter(imageUrList);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
        tvFaultContent = findViewById(R.id.tvFaultContent);
        faultImageRecyclerView.setLayoutManager(manager);
        faultImageRecyclerView.setNestedScrollingEnabled(false);
        //由于尺寸固定所以设置为true,提高性能
        faultImageRecyclerView.setHasFixedSize(true);
        gridImageAdapter.bindToRecyclerView(faultImageRecyclerView);
    }

    @Override
    public void loadData() {
        super.loadData();
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
                TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class);
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
        String orderNum = "订单编号:" + info.getOut_trade_no();
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
        showLocate(getNotNullValue(info.getAddress()));
        tvRepairFactory.setText(getNotNullValue(info.getGarageName()));
        tvFaultContent.setText(getNotNullValue(info.getDetail()));
        initClickFunctionByStatus(info);
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
                String captcha = "待接单(验证码:" + orderInfo.getCaptcha() + ")";
                tvOrderStatus.setText(captcha);
                setVisibility(tvFirstFunction, true);
                setVisibility(tvSecondFunction, true);
                break;
            case TYPE_STATUS_ORDER_IN_SERVICE:
                tvOrderStatus.setText("服务中");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
                break;
            case TYPE_STATUS_ORDER_WAIT_PAY:
                tvOrderStatus.setText("待支付");
                setVisibility(tvFirstFunction, true);
                setVisibility(tvSecondFunction, false);
                setSolidText(tvFirstFunction, "支付订单");
                break;
            case TYPE_STATUS_ORDER_WAIT_EVALUATE:
                tvOrderStatus.setText("待评价");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, true);
                setSolidText(tvSecondFunction, "去评价");
                break;
            case TYPE_STATUS_ORDER_FINISH:
                tvOrderStatus.setText("服务完成");
                setVisibility(tvFirstFunction, true);
                setSolidText(tvFirstFunction, "查看服务");
                setVisibility(tvSecondFunction, false);
                break;
            case TYPE_STATUS_ORDER_CANCELED:
                tvOrderStatus.setText("已取消");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
                break;
            case TYPE_STATUS_ORDER_CLOSE:
                tvOrderStatus.setText("已关闭");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, true);
                setHollowText(tvSecondFunction, "查看评价");
                break;
            default:
                tvOrderStatus.setText("未知");
                setVisibility(tvFirstFunction, false);
                setVisibility(tvSecondFunction, false);
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
        ApiRepository.getInstance().findDetail(orderId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<OrderDetail>>() {
                    @Override
                    public void onRequestNext(BaseEntity<OrderDetail> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null && entity.data.getOrder() != null) {
                                    showDetailAndLoadFunction(entity.data.getOrder());
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
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
                switch (orderInfo.getStatus()) {
                    //查看附近修理厂
                    case TYPE_STATUS_ORDER_WAIT_ORDER:
                        //待接单
                        TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class);
                        break;
                    //待支付
                    case TYPE_STATUS_ORDER_WAIT_PAY:
                        findAmount(orderInfo);
                        break;
                    default:
                        break;
                }
            }
        });
        tvSecondFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @SuppressWarnings("unchecked")
    private static class PaymentHandler extends Handler {
        private WeakReference<DoorToDoorServiceDetailActivity> softReference;

        public PaymentHandler(DoorToDoorServiceDetailActivity doorServiceDetailActivity) {
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
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        return imgView;
    }

    private void showLocate(String text) {
        text = TourCooUtil.getNotNullValue(text);
     /*   SpannableString ss = new SpannableString(text + "  ");
        int len = ss.length();
        //图片
        Drawable d = ContextCompat.getDrawable(mContext, (R.mipmap.ic_positioning));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //构建ImageSpan
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, len - 1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);*/
        tvAddress.setText(text);
    }


    @Override
    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
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

}

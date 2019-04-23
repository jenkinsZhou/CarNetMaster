package com.tourcoo.carnet.ui.order;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.RepairOrderAdapter;
import com.tourcoo.carnet.core.common.OrderConstant;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.fragment.BaseRefreshFragment;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.threadpool.ThreadPoolManager;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.confirm.PayDialog;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;
import com.tourcoo.carnet.entity.WeiXinPay;
import com.tourcoo.carnet.entity.car.PayInfo;
import com.tourcoo.carnet.entity.event.OrderEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.factory.OrderDetailActivity;
import com.tourcoo.carnet.ui.factory.NearbyRepairFactoryActivity;
import com.tourcoo.carnet.ui.repair.FillEvaluationActivity;
import com.tourcoo.carnet.ui.repair.LookServiceActivity;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

import static android.app.Activity.RESULT_OK;
import static com.tourcoo.carnet.core.common.OrderConstant.EXTRA_ORDER_TAG_REPAIR;
import static com.tourcoo.carnet.core.common.OrderConstant.EXTRA_ORDER_TAG_SERVICE;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.core.common.WxConfig.APP_ID;
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
import static com.tourcoo.carnet.ui.factory.OrderDetailActivity.EXTRA_IS_CLICK;
import static com.tourcoo.carnet.ui.factory.OrderDetailActivity.EXTRA_ORDER_STATUS;
import static com.tourcoo.carnet.ui.order.LookEvaluationActivity.EXTRA_ORDER_ID;

/**
 * @author :JenkinsZhou
 * @description :上门服务历史订单列表
 * @company :途酷科技
 * @date 2019年03月19日11:19
 * @Email: 971613168@qq.com
 */
public class HistoryServiceListFragment extends BaseRefreshFragment<FaultRepairEntity.FaultRepairInfo> {
    private RepairOrderAdapter repairOrderAdapter;
    private List<FaultRepairEntity.FaultRepairInfo> repairEntityList = new ArrayList<>();
    public static final int CODE_REQUEST_SERVICE_DETAIL = 1001;
    public static final String EXTRA_SERVICE_DETAIL = "EXTRA_SERVICE_DETAIL";
    private FaultRepairEntity.FaultRepairInfo mCurrentFaultRepairInfo;
    private PaymentHandler mHandler = new PaymentHandler(this);
    private IWXAPI api;
    private int mPayType;
    private static final String PAY_STATUS = "resultStatus";
    private static final String PAY_STATUS_SUCCESS = "9000";
    /**
     * 填写评价请求码
     */
    private static final int CODE_REQUEST_FILL = 10;
    private int refreshPosition;
    /**
     * 付款金额
     */
    private double paymentAmount;
    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    private static final int SDK_PAY_FLAG = 1001;
    private OrderHistoryActivity orderHistoryActivity;

    @Override
    public int getContentLayout() {
        return R.layout.layout_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        orderHistoryActivity = (OrderHistoryActivity) mContext;
        mStatusManager.showSuccessLayout();
        requestPermission();
        //在这里可以不传AppId传null就可以
        api = WXAPIFactory.createWXAPI(mContext, null);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    @Override
    public BaseQuickAdapter<FaultRepairEntity.FaultRepairInfo, BaseViewHolder> getAdapter() {
        repairOrderAdapter = new RepairOrderAdapter(repairEntityList);
        return repairOrderAdapter;
    }

    @Override
    public void loadData(int page) {
        findMyOrderList(page + "", mDefaultPageSize + "");
    }


    public static HistoryServiceListFragment newInstance() {
        Bundle args = new Bundle();
        HistoryServiceListFragment fragment = new HistoryServiceListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        mDefaultPage = 1;
    }


    /**
     * 单独设置状态
     *
     * @param statusView
     */
    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView) {
        super.setMultiStatusView(statusView);
    }


    /**
     * 获取我的历史订单列表
     */
    private void findMyOrderList(String pageIndex, String pageSize) {
        if (pageIndex.equals("0")) {
            pageIndex = "1";
        }
        String orderTypeString;
        if (orderHistoryActivity.isAllService()) {
            orderTypeString = "3,4,5";
        } else {
            orderTypeString = orderHistoryActivity.orderType + "";
        }
        TourCooLogUtil.i("当前请求类型：", orderHistoryActivity.orderType);
        ApiRepository.getInstance().findMyList(pageIndex, pageSize, orderHistoryActivity.orderType).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<FaultRepairEntity>(getIHttpRequestControl()) {
                              @Override
                              public void onRequestNext(FaultRepairEntity entity) {
                                  closeLoadingDialog();
                                  if (entity != null) {
                                      if (entity.code == CODE_REQUEST_SUCCESS) {
                                          FaultRepairEntity repairEntity = parseRepairInfo(entity.data);
                                          if (repairEntity != null) {
                                              UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), repairEntity.getOrderList() == null ? new ArrayList<>() : repairEntity.getOrderList(), null);
//                                              refreshOrderType(orderHistoryActivity.orderType);
                                          }
                                      } else {
                                          ToastUtil.showFailed(entity.message);
                                          mStatusManager.showErrorLayout();
                                      }
                                  }
                                  TourCooLogUtil.d("刷新位置:" + refreshPosition);
                                  repairOrderAdapter.refreshNotifyItemChanged(refreshPosition);
                              }

                              @Override
                              public void onError(Throwable e) {
                                  super.onError(e);
                                  closeLoadingDialog();
                                  mStatusManager.showErrorLayout();
                              }

                              @Override
                              public void onRequestError(Throwable e) {
                                  super.onRequestError(e);
                                  closeLoadingDialog();
                                  mStatusManager.showErrorLayout();
                              }
                          }

                );
    }


    @Override
    public void loadData() {
        super.loadData();
        initListener();
        showLoadingDialog();
    }


    private FaultRepairEntity parseRepairInfo(Object object) {
        try {
            return JSON.parseObject(JSON.toJSONString(object), FaultRepairEntity.class);
        } catch (Exception e) {
            TourCooLogUtil.e(TAG, e.toString());
            return null;
        }
    }



    /**
     * 设置item点击事件
     */
    @SuppressWarnings("unchecked")
    private void initListener() {
        repairOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FaultRepairEntity.FaultRepairInfo info = (FaultRepairEntity.FaultRepairInfo) adapter.getData().get(position);
                //跳转到服务详情页面
                Intent intent = new Intent();
                intent.setClass(mContext, OrderDetailActivity.class);
                intent.putExtra(EXTRA_SERVICE_DETAIL, info);
                refreshPosition = position;
                startActivityForResult(intent, CODE_REQUEST_SERVICE_DETAIL);
            }
        });
        repairOrderAdapter.setOnItemChildClickListener(new RepairOrderAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                refreshPosition = position;
                List<FaultRepairEntity.FaultRepairInfo> repairInfoList = adapter.getData();
                FaultRepairEntity.FaultRepairInfo faultRepairInfo = repairInfoList.get(position);
                mCurrentFaultRepairInfo = faultRepairInfo;
                if (faultRepairInfo == null) {
                    ToastUtil.showFailed("未获取到故障信息");
                    return;
                }
                switch (faultRepairInfo.getStatus()) {
                    /**
                     * 待接单
                     */
                    case TYPE_STATUS_ORDER_WAIT_ORDER:
                        if (view.getId() == R.id.tvRightButton) {
                            showCancelDialog();
                        } else if (view.getId() == R.id.tvLeftButton) {
                            //待接单
                            Bundle bundle = new Bundle();
                            bundle.putString(EXTRA_ORDER_ID, faultRepairInfo.getId() + "");
                            TourCooLogUtil.i(TAG, "orderId:" + faultRepairInfo.getId());
                            TourCooUtil.startActivity(mContext, NearbyRepairFactoryActivity.class, bundle);
                        }
                        break;
                    /**
                     * 服务中
                     */
                    case TYPE_STATUS_ORDER_IN_SERVICE:
                        //do nothing
                        break;
                    /**
                     * 完成
                     */
                    case TYPE_STATUS_ORDER_FINISH:
                        //查看服务
                        if (view.getId() == R.id.tvRightButton) {
                            skipLookServiceActivity(faultRepairInfo.getId());
                        }
                        break;
                    /**
                     * 待支付
                     */
                    case TYPE_STATUS_ORDER_WAIT_PAY:
                        if (view.getId() == R.id.tvRightButton) {
                            //先获取订单金额
                            findAmount();
                        } else if (view.getId() == R.id.tvLeftButton) {
                            skipLookServiceActivity(faultRepairInfo.getId());
                        }
                        break;
                    /**
                     * 待评价
                     */
                    case TYPE_STATUS_ORDER_WAIT_EVALUATE:
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("FaultRepairInfo", faultRepairInfo);
                        Intent intent = new Intent();
                        intent.putExtra("FaultRepairInfo", faultRepairInfo);
                        intent.setClass(mContext, FillEvaluationActivity.class);
                        startActivityForResult(intent, CODE_REQUEST_FILL);
//                        TourcooUtil.startActivity(mContext, FillEvaluationActivity.class, bundle);
                        break;
                    case TYPE_STATUS_ORDER_CANCELED:
                        //服务已经取消
                        break;
                    //服务关闭(完成)（查看评价）
                    case TYPE_STATUS_ORDER_CLOSE:
                        if (view.getId() == R.id.tvRightButton) {
                            //查看评价
                            Bundle bundle = new Bundle();
                            bundle.putString(EXTRA_ORDER_ID, faultRepairInfo.getId() + "");
                            TourCooLogUtil.i(TAG, "评价id:" + faultRepairInfo.getId());
                            TourCooUtil.startActivity(mContext, LookEvaluationActivity.class, bundle);
                        } else if (view.getId() == R.id.tvLeftButton) {
                            //查看服务
                            skipLookServiceActivity(faultRepairInfo.getId());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void showPayDialog() {
        PayDialog payDialog = new PayDialog(mContext, paymentAmount, new PayDialog.PayListener() {
            @Override
            public void pay(int payType, Dialog dialog) {
                mPayType = payType;
                createPay(payType);
                dialog.dismiss();
            }
        });
        payDialog.show();
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

    /**
     * 支付接口
     */
    private void createPay(int type) {
        if (mCurrentFaultRepairInfo == null) {
            ToastUtil.show("未获取到订单信息");
            return;
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("orderId", mCurrentFaultRepairInfo.getId());
        map.put("ownerId", mCurrentFaultRepairInfo.getOwner_id());
        map.put("payType", type);
        ApiRepository.getInstance().createPay(map).compose(bindUntilEvent(FragmentEvent.DESTROY)).
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
                                            if (mPayType == PAY_TYPE_ALI) {
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


    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        if (api != null) {
            api.detach();
        }
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @SuppressWarnings("unchecked")
    private static class PaymentHandler extends Handler {
        private WeakReference<HistoryServiceListFragment> softReference;

        public PaymentHandler(HistoryServiceListFragment mainTabActivity) {
            softReference = new WeakReference<>(mainTabActivity);
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
                                softReference.get().refreshStatus(TYPE_STATUS_ORDER_WAIT_EVALUATE);
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
    private void findAmount() {
        if (mCurrentFaultRepairInfo == null) {
            ToastUtil.showFailed("未获取到订单信息");
            return;
        }
        ApiRepository.getInstance().findAmount(mCurrentFaultRepairInfo.getId() + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<Double>>() {
                    @Override
                    public void onRequestNext(BaseEntity<Double> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                paymentAmount = entity.data;
                                showPayDialog();
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
            OrderConstant.currentOrderTabTag = EXTRA_ORDER_TAG_SERVICE;
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
     * 取消服务
     */
    private void cancelOrder() {
        if (mCurrentFaultRepairInfo == null) {
            ToastUtil.show("未获取到订单信息");
        }
        TourCooLogUtil.i(TAG, "mCurrentFaultRepairInfo：id= " + mCurrentFaultRepairInfo.getId());
        ApiRepository.getInstance().cancelOrder(mCurrentFaultRepairInfo.getId() + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                ToastUtil.showSuccess("订单已取消");
                                //刷新取消状态
                                refreshStatus(TYPE_STATUS_ORDER_CANCELED);
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    /**
     * 刷新请求(相当于下拉刷新)
     */
    private void refreshRequest() {
        TourCooLogUtil.i("执行了刷新");
        repairOrderAdapter.getData().clear();
        repairOrderAdapter.notifyDataSetChanged();
        findMyOrderList("1", mDefaultPageSize + "");
    }

    /**
     * 刷新列表状态
     */
    private void refreshStatus(int status) {
        TourCooLogUtil.d("准备执行刷新状态:" + status);
        TourCooLogUtil.d("数量:" + repairOrderAdapter.getData().size());
        if (repairOrderAdapter.getData().size() > refreshPosition) {
            mCurrentFaultRepairInfo = repairOrderAdapter.getData().get(refreshPosition);
            mCurrentFaultRepairInfo.setStatus(status);
            repairOrderAdapter.refreshNotifyItemChanged(refreshPosition);
            TourCooLogUtil.d("已经执行刷新：位置:" + refreshPosition);
        } else {
            TourCooLogUtil.e("未执行刷新：位置:" + refreshPosition);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_FILL:
                if (resultCode == RESULT_OK) {
                    refreshStatus(TYPE_STATUS_ORDER_CLOSE);
                }
                break;
            case CODE_REQUEST_SERVICE_DETAIL:
                if (data != null) {
                    int orderStatus = data.getIntExtra(EXTRA_ORDER_STATUS, -1);
                    TourCooLogUtil.i(TAG, TAG + ":" + "收到回调:orderStatus=" + orderStatus);
                    if (orderStatus == -1) {
                        mRefreshLayout.autoRefresh(100);
                    } else {
                        TourCooLogUtil.i(TAG, TAG + ":" + "刷新了状态:" + orderStatus);
                        //根据状态刷新UI
                        refreshStatus(orderStatus);
                    }
                }
                break;
            default:
                TourCooLogUtil.e(TAG, TAG + ":" + "收到回调");
                break;
        }
    }




    /**
     * 确认取消服务
     */
    private void showCancelDialog() {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(
                getActivity());
        builder.setPositiveButtonPosition(ConfirmDialog.RIGHT);
        builder.setTitle("取消服务");
        builder.setFirstMessage("是否取消服务?").setNegativeButtonButtonBold(true);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelOrder();
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


    private void skipLookServiceActivity(int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ORDER_ID, orderId);
        TourCooUtil.startActivity(mContext, LookServiceActivity.class, bundle);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBaseEvent(BaseEvent event) {
        if (event == null) {
            TourCooLogUtil.e(TAG, "直接拦截(上门服务)");
            return;
        }
        if (EXTRA_ORDER_TAG_REPAIR.equals(OrderConstant.currentOrderTabTag)) {
            TourCooLogUtil.w(TAG, "直接拦截(上门服务)");
            return;
        }
        switch (event.id) {
            case EVENT_ACTION_PAY_FRESH_SUCCESS:
                TourCooLogUtil.i(TAG, "接收到回调");
                refreshStatus(TYPE_STATUS_ORDER_WAIT_EVALUATE);
                break;
            case EVENT_ACTION_PAY_FRESH_FAILED:
                TourCooLogUtil.i(TAG, "接收到回调");
                refreshStatus(TYPE_STATUS_ORDER_WAIT_PAY);
                break;
            default:
                break;
        }
    }


}

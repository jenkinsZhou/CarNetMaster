package com.tourcoo.carnet.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.log.widget.utils.DateUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.entity.order.FaultRepairEntity;

import java.util.List;

import androidx.annotation.Nullable;

import static com.tourcoo.carnet.entity.order.FaultRepairEntity.FaultRepairInfo.TYPE_CAR_CURING;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.FaultRepairInfo.TYPE_CAR_REPAIR;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.FaultRepairInfo.TYPE_CAR_WASH;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CANCELED;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_CLOSE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_FINISH;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_IN_SERVICE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_EVALUATE;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_ORDER;
import static com.tourcoo.carnet.entity.order.FaultRepairEntity.TYPE_STATUS_ORDER_WAIT_PAY;

/**
 * @author :JenkinsZhou
 * @description :维修订单适配器
 * @company :途酷科技
 * @date 2019年03月19日11:23
 * @Email: 971613168@qq.com
 */
public class RepairOrderAdapter extends BaseQuickAdapter<FaultRepairEntity.FaultRepairInfo, BaseViewHolder> {
    public RepairOrderAdapter(@Nullable List<FaultRepairEntity.FaultRepairInfo> data) {
        super(R.layout.item_repair_order_description, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FaultRepairEntity.FaultRepairInfo item) {
        helper.setText(R.id.tvOrderNumber, TourCooUtil.getNotNullValue(item.getOut_trade_no()));
        helper.setText(R.id.tvFaultDescription, item.getDetail());
        helper.setText(R.id.tvMaintenanceShopLocate, TourCooUtil.getNotNullValue(item.getGarageName()));
        helper.setText(R.id.tvCreateTime, DateUtil.stampToDateChineseCharacter(item.getCreate_time()));
        TextView tvRepairStatus = helper.getView(R.id.tvRepairStatus);
        TextView tvLeftButton = helper.getView(R.id.tvLeftButton);
        TextView tvRightButton = helper.getView(R.id.tvRightButton);
        helper.addOnClickListener(R.id.tvLeftButton);
        helper.addOnClickListener(R.id.tvRightButton);
        TextView tvOrderType = helper.getView(R.id.tvOrderType);
        switch (item.getType()) {
            case TYPE_CAR_REPAIR:
                tvOrderType.setText("维修简述:");
                break;
            case TYPE_CAR_WASH:
                tvOrderType.setText("洗车简述:");
                break;
            case TYPE_CAR_CURING:
                tvOrderType.setText("保养简述:");
                break;
            default:
                break;
        }
        switch (item.getStatus()) {
            //待支付
            case TYPE_STATUS_ORDER_WAIT_ORDER:
                String captcha = "待接单(验证码:" + item.getCaptcha() + ")";
                tvRepairStatus.setText(captcha);
                setSolidText(tvLeftButton, "附近修理厂");
                setVisibility(tvLeftButton, true);
                setVisibility(tvRightButton, true);
                setHollowText(tvRightButton, "取消服务");
                break;
            case TYPE_STATUS_ORDER_IN_SERVICE:
                helper.setText(R.id.tvRepairStatus, "服务中");
                setHollowText(tvRightButton, "取消服务");
                setVisibility(tvLeftButton, false);
                setVisibility(tvRightButton, false);
                break;
            case TYPE_STATUS_ORDER_WAIT_PAY:
                helper.setText(R.id.tvRepairStatus, "待支付");
                setHollowText(tvLeftButton, "查看服务");
                setVisibility(tvRightButton, true);
                setVisibility(tvLeftButton, true);
                setSolidText(tvRightButton, "支付订单");

                break;
            case TYPE_STATUS_ORDER_WAIT_EVALUATE:
                setVisibility(tvLeftButton, true);
                helper.setText(R.id.tvRepairStatus, "待评价");
                setHollowText(tvLeftButton, "查看服务");
                setSolidText(tvRightButton, "填写评价");
                break;
            case TYPE_STATUS_ORDER_FINISH:
                helper.setText(R.id.tvRepairStatus, "服务完成");
                setVisibility(tvLeftButton, false);
                setVisibility(tvRightButton, true);
                setHollowText(tvRightButton, "查看服务");
                break;
            case TYPE_STATUS_ORDER_CANCELED:
                helper.setText(R.id.tvRepairStatus, "已取消");
                setVisibility(tvLeftButton, false);
                setHollowText(tvRightButton, "已取消");
                setVisibility(tvRightButton, false);
                break;
            case TYPE_STATUS_ORDER_CLOSE:
                helper.setText(R.id.tvRepairStatus, "已关闭");
                setHollowText(tvRightButton, "已关闭");
                setHollowText(tvRightButton, "查看评价");
                setSolidText(tvLeftButton, "查看服务");
                setVisibility(tvLeftButton, true);
                setVisibility(tvRightButton, true);
                break;
            default:
                break;
        }
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

    private void setVisibility(TextView textView, boolean visible) {
        if (visible) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }


}

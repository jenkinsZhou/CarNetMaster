package com.tourcoo.carnet.adapter;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.entity.car.CarInfoEntity;


import static com.tourcoo.carnet.entity.car.CarInfoEntity.REMIND_OPEN;

/**
 * @author :JenkinsZhou
 * @description :车辆信息适配器
 * @company :途酷科技
 * @date 2019年04月08日16:57
 * @Email: 971613168@qq.com
 */
public class CarInfoAdapter extends BaseQuickAdapter<CarInfoEntity, BaseViewHolder> {
    public CarInfoAdapter() {
        super(R.layout.item_car_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoEntity item) {
        SuperTextView stvPlatNumber = helper.getView(R.id.stvPlatNumber);
        stvPlatNumber.setCenterString(TourcooUtil.getNotNullValue(item.getPlate_num()));
        //车辆品类
        SuperTextView stvCarCategory = helper.getView(R.id.stvCarCategory);
        stvCarCategory.setCenterString(item.getBrandName());
        //车辆型号
        SuperTextView stvCarModel = helper.getView(R.id.stvCarModel);
        stvCarModel.setCenterString(item.getModeName());

        //汽车排量
        SuperTextView stvCarDisplacement = helper.getView(R.id.stvCarDisplacement);
        stvCarDisplacement.setCenterString(TourcooUtil.getNotNullValue(item.getDisplacement()));
        //发动机编号
        SuperTextView stvEngineNumber = helper.getView(R.id.stvEngineNumber);
        stvEngineNumber.setCenterString(TourcooUtil.getNotNullValue(item.getEngine()));


        //保险信息
        SuperTextView stvCarInsuranceInfo = helper.getView(R.id.stvCarInsuranceInfo);
        stvCarInsuranceInfo.setCenterString(TourcooUtil.getNotNullValue(item.getInsurer()));
        //保养信息
        SuperTextView stvMaintainInfo = helper.getView(R.id.stvMaintainInfo);
        stvMaintainInfo.setCenterString(TourcooUtil.getNotNullValue(item.getMaintain()));
        //年检信息
        SuperTextView stvYearly = helper.getView(R.id.stvYearly);
        stvYearly.setCenterString(TourcooUtil.getNotNullValue(item.getYearly()));
        //保养规则
        SuperTextView stvMaintainRule = helper.getView(R.id.stvMaintainRule);
        stvMaintainRule.setCenterString(TourcooUtil.getNotNullValue(item.getMaintain_rule()));
        //年检提醒信息开关
        SuperTextView stvSwitchRemindYear = helper.getView(R.id.stvSwitchRemindYear);
        if (item.isYearly_remind()) {
            stvSwitchRemindYear.setCenterString("开");
        } else {
            stvSwitchRemindYear.setCenterString("关");
        }
        //保险提醒开关
        SuperTextView stvSwitchRemindInsurance = helper.getView(R.id.stvSwitchRemindInsurance);
        if (item.isInsurer_remind()) {
            stvSwitchRemindInsurance.setCenterString("开");
        } else {
            stvSwitchRemindInsurance.setCenterString("关");
        }
        //故障提醒模式
        //TODO
        //obd序列号
        SuperTextView stvObdNumber = helper.getView(R.id.stvObdNumber);
        stvObdNumber.setCenterString(TourcooUtil.getNotNullValue(item.getObd_sn()));
        //obd接收模式
        SuperTextView stvObdReceiveMode = helper.getView(R.id.stvObdReceiveMode);
        stvObdReceiveMode.setCenterString(TourcooUtil.getNotNullValue(item.getObd_receive()));
        //保养提醒开关
        SuperTextView stvRemindMaintain = helper.getView(R.id.stvSwitchRemindMaintain);
        if (item.isMaintain_remind()) {
            stvRemindMaintain.setCenterString("开");
        } else {
            stvRemindMaintain.setCenterString("关");
        }
    }
}

package com.tourcoo.carnet.adapter.obd;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.entity.obd.DriveReportInfo;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年06月13日16:42
 * @Email: 971613168@qq.com
 */
public class DriveReportInfoAdapter extends BaseQuickAdapter<DriveReportInfo, BaseViewHolder> {
    public DriveReportInfoAdapter() {
        super(R.layout.item_driving_report);
    }

    @Override
    protected void convert(BaseViewHolder helper, DriveReportInfo item) {

    }
}

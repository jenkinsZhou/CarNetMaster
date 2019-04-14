package com.tourcoo.carnet.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.log.widget.utils.DateUtil;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.MsgSystemEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author :JenkinsZhou
 * @description :系统消息适配器
 * @company :途酷科技
 * @date 2019年03月18日13:59
 * @Email: 971613168@qq.com
 */
public class SystemMsgAdapter extends BaseQuickAdapter<MessageInfo.MessageBean, BaseViewHolder> {
    public SystemMsgAdapter() {
        super(R.layout.item_system_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageInfo.MessageBean item) {
        helper.setText(R.id.tvMsgTime, DateUtil.getTimeStringChina(item.getCreateTime()));
        helper.setText(R.id.tvMsgContent, item.getMessage());
        if (item.getReadStatus() == 1) {
            helper.setVisible(R.id.tvRedDot, false);
        } else {
            helper.setVisible(R.id.tvRedDot, true);
        }

    }
}

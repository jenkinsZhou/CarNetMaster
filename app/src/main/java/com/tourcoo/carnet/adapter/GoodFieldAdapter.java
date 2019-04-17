package com.tourcoo.carnet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.util.TourCooUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author :JenkinsZhou
 * @description :擅长领域适配器
 * @company :途酷科技
 * @date 2019年04月13日13:48
 * @Email: 971613168@qq.com
 */
public class GoodFieldAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GoodFieldAdapter(@Nullable List<String> data) {
        super(R.layout.item_field_good, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tvGoodField, TourCooUtil.getNotNullValue(item));
    }
}

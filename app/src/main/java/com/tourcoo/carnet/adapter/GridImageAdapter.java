package com.tourcoo.carnet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.manager.GlideManager;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author :JenkinsZhou
 * @description :附加图片适配器
 * @company :途酷科技
 * @date 2019年03月22日17:05
 * @Email: 971613168@qq.com
 */
public class GridImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GridImageAdapter(@Nullable List<String> data) {
        super(R.layout.item_grid_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideManager.loadImg(item, helper.getView(R.id.additionalRoundedImageView));
    }
}

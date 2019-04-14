package com.tourcoo.carnet.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.garage.CommentInfo;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月13日15:56
 * @Email: 971613168@qq.com
 */
public class CommentAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {
    public CommentAdapter() {
        super(R.layout.item_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentInfo item) {
      /*  helper.setText(R.id.tvRepairDepotName, item.getName());
        helper.setText(R.id.tvRepairDepotDistance, item.getDistance() + "km");
        helper.setText(R.id.tvVisitCount, item.getVisit() + "人");
        helper.setText(R.id.tvPoint, item.getLevel() + "分");
        RatingStarView ratingStarView = helper.getView(R.id.rsvRating);
        if (ratingStarView != null) {
            ratingStarView.setEnabled(false);
            ratingStarView.setRating(item.getLevel());
        }*/
    }
}






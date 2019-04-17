package com.tourcoo.carnet.adapter;

import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.garage.CommentInfo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月13日15:56
 * @Email: 971613168@qq.com
 */
public class CommentAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {
    private Drawable mDrawable;

    public CommentAdapter() {
        super(R.layout.item_comment);
        mDrawable = TourCooUtil.getDrawable(R.mipmap.img_default_minerva);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentInfo item) {
        helper.setText(R.id.tvNickName, TourCooUtil.getNotNullValue(item.getNickName()));
        helper.setText(R.id.tvTime, TourCooUtil.getNotNullValue(item.getCreateTime()));
        helper.setText(R.id.tvComment, TourCooUtil.getNotNullValue(item.getDetail()));
        CircleImageView circleImageView = helper.getView(R.id.civAvatar);
        RatingStarView ratingStarView = helper.getView(R.id.rsvRating);
        if (ratingStarView != null) {
            ratingStarView.setEnabled(false);
            ratingStarView.setRating(item.getLevel());
        }
        String imageUrl = RequestConfig.BASE + TourCooUtil.getNotNullValue(item.getOwnerIconUrl());
        GlideManager.loadImg(imageUrl, circleImageView, mDrawable);
    }
}






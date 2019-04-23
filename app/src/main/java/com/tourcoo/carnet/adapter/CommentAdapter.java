package com.tourcoo.carnet.adapter;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.garage.CommentInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author :JenkinsZhou
 * @description :评论适配器
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
        if (!TextUtils.isEmpty(item.getImages())) {
            List<String> imageUrlList = new ArrayList<>();
            String[] imageArray = item.getImages().split(",");
            for (String image : imageArray) {
                if (!TextUtils.isEmpty(image)) {
                    imageUrlList.add(RequestConfig.BASE + image);
                    TourCooLogUtil.i(TAG, "value:" + "添加的url:" + RequestConfig.BASE + image);
                }
            }
            RecyclerView commentImageRecyclerView = helper.getView(R.id.commentImageRecyclerView);
            commentImageRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            GridImageAdapter gridImageAdapter = new GridImageAdapter(imageUrlList);
            gridImageAdapter.bindToRecyclerView(commentImageRecyclerView);
            gridImageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    onThumbnailClick(imageUrlList.get(position));
                }
            });
        }

        RatingStarView ratingStarView = helper.getView(R.id.rsvRating);
        if (ratingStarView != null) {
            ratingStarView.setEnabled(false);
            ratingStarView.setRating(item.getLevel());
        }
        String imageUrl = RequestConfig.BASE + TourCooUtil.getNotNullValue(item.getOwnerIconUrl());
        GlideManager.loadImg(imageUrl, circleImageView, mDrawable);
    }


    public void onThumbnailClick(String imageUrl) {
// 全屏显示的方法
        /* android.R.style.Theme_Black_NoTitleBar_Fullscreen*/
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
        ImageView imgView = new ImageView(mContext);
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        return imgView;
    }
}






package com.tourcoo.carnet.ui.order;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.previewlibrary.GPreviewBuilder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.ImageEntity;
import com.tourcoo.carnet.entity.garage.CommentDetail;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :查看评价
 * @company :途酷科技
 * @date 2019年04月11日21:02
 * @Email: 971613168@qq.com
 */
public class LookEvaluationActivity extends BaseTourCooTitleActivity {
    private RecyclerView rvImageComment;
    private List<String> imageUrList = new ArrayList<>();
    private TextView tvCommentDetail;
    private RatingStarView rsvRating;
    private GridImageAdapter gridImageAdapter;
    private TextView tvCommentTime;
    private TextView tvLevel;

    private String orderId;
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    @Override
    public int getContentLayout() {
        return R.layout.activity_look_evalution;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        rvImageComment = findViewById(R.id.rvImageComment);
        rsvRating = findViewById(R.id.rsvRating);
        tvCommentDetail = findViewById(R.id.tvCommentDetail);
        tvCommentTime = findViewById(R.id.tvCommentTime);
        gridImageAdapter = new GridImageAdapter(imageUrList);
        gridImageAdapter.bindToRecyclerView(rvImageComment);
        rvImageComment.setLayoutManager(new GridLayoutManager(mContext, 4));
        tvLevel = findViewById(R.id.tvLevel);
        gridImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                onThumbnailClick(view, gridImageAdapter.getData().get(position));
                List<ImageEntity> imageEntityList = parseImageEntityList(gridImageAdapter.getData());
                    computeBoundsBackward(rvImageComment, imageEntityList);
                    GPreviewBuilder.from(mContext)
                            .setData(imageEntityList)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();
            }
        });

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("查看评价");
    }


    @Override
    public void loadData() {
        super.loadData();
        findComment(orderId);
    }

    /**
     * 获取指定评论详情
     *
     * @param orderId
     */
    private void findComment(String orderId) {
        if (TextUtils.isEmpty(orderId)) {
            TourCooLogUtil.e(TAG, "参数为null");
            return;
        }
        TourCooLogUtil.i(TAG, "订单编号:" + orderId);
        imageUrList.clear();
        ApiRepository.getInstance().findComment(orderId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<List<CommentDetail>>>() {
                    @Override
                    public void onRequestNext(BaseEntity<List<CommentDetail>> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                showDetail(getDetail(entity.data));
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private CommentDetail getDetail(List<CommentDetail> commentDetailList) {
        if (commentDetailList == null || commentDetailList.isEmpty()) {
            TourCooLogUtil.e(TAG, "参数为null");
            return null;
        }
        return commentDetailList.get(0);
    }


    private void showDetail(CommentDetail commentDetail) {
        if (commentDetail == null) {
            return;
        }
        tvCommentDetail.setText(getNotNullValue(commentDetail.getDetail()));
        rsvRating.setRating(commentDetail.getLevel());
        rsvRating.setEnabled(false);
        tvLevel.setText(commentDetail.getLevel() + "分");
        tvCommentTime.setText(getNotNullValue(commentDetail.getCreateTime()));
        String images = getNotNullValue(commentDetail.getImages());
        List<String> imageList = Arrays.asList(images.split(","));
        for (int i = 0; i < imageList.size(); i++) {
            if (!TextUtils.isEmpty(imageList.get(i))) {
                imageList.set(i, RequestConfig.BASE + imageList.get(i));
            }
        }
        imageUrList.addAll(imageList);
        for (int i = imageUrList.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(imageUrList.get(i))) {
                imageUrList.remove(i);
            }
        }
        for (String s : imageUrList) {
            TourCooLogUtil.i(TAG, TAG + ":" + "图片url:" + s);
        }
        gridImageAdapter.notifyDataSetChanged();
    }

    private String getNotNullValue(String value) {
        return TourCooUtil.getNotNullValue(value);
    }


    private ImageView getView() {
        ImageView imgView = new ImageView(this);
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
        return imgView;
    }


    public void onThumbnailClick(View v, String imageUrl) {
// 全屏显示的方法
        /* android.R.style.Theme_Black_NoTitleBar_Fullscreen*/
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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


    private List<ImageEntity> parseImageEntityList(List<String> imageUrlList) {
        List<ImageEntity> imageEntityList = new ArrayList<>();
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            return imageEntityList;
        }
        ImageEntity imageEntity;
        for (String url : imageUrlList) {
            imageEntity = new ImageEntity();
            imageEntity.setImageUrl(url);
            imageEntityList.add(imageEntity);
        }
        return imageEntityList;
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(RecyclerView imageRecyclerView, List<ImageEntity> imageEntityList) {
        if (imageRecyclerView == null || !(imageRecyclerView.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        GridLayoutManager gridLayoutManager = (GridLayoutManager) imageRecyclerView.getLayoutManager();
        int firstCompletelyVisiblePos = gridLayoutManager.findFirstVisibleItemPosition();
        for (int i = firstCompletelyVisiblePos; i < imageEntityList.size(); i++) {
            View itemView = gridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = itemView.findViewById(R.id.additionalRoundedImageView);
                thumbView.getGlobalVisibleRect(bounds);
            }
            imageEntityList.get(i).setBounds(bounds);
        }
    }

}

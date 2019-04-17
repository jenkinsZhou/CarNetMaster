package com.tourcoo.carnet.ui.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.GridImageAdapter;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.garage.CommentDetail;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        tvCommentTime.setText(getNotNullValue(commentDetail.getCreateTime()));
        String images = getNotNullValue(commentDetail.getImages());
        List<String> imageList = Arrays.asList(images.split(","));
        for (int i = 0; i < imageList.size(); i++) {
            imageList.set(i, RequestConfig.BASE + imageList.get(i));
        }
        imageUrList.addAll(imageList);
        gridImageAdapter.notifyDataSetChanged();
    }

    private String getNotNullValue(String value) {
        return TourCooUtil.getNotNullValue(value);
    }



}

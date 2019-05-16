package com.tourcoo.carnet.ui.repair;

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
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.ImageEntity;
import com.tourcoo.carnet.entity.garage.ServiceInfo;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcoo.carnet.ui.order.LookEvaluationActivity.EXTRA_ORDER_ID;

/**
 * @author :JenkinsZhou
 * @description :查看服务页面
 * @company :途酷科技
 * @date 2019年04月18日20:22
 * @Email: 971613168@qq.com
 */
public class LookServiceActivity extends BaseTourCooTitleActivity {
    private List<String> imageUrList = new ArrayList<>();
    private GridImageAdapter gridImageAdapter;
    private TextView tvServiceContent;
    private RecyclerView serviceImageRecyclerView;
    private int orderId;

    @Override
    public int getContentLayout() {
        return R.layout.activity_look_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, 0);
        serviceImageRecyclerView = findViewById(R.id.serviceImageRecyclerView);
        tvServiceContent = findViewById(R.id.tvServiceContent);
        serviceImageRecyclerView = findViewById(R.id.serviceImageRecyclerView);
        gridImageAdapter = new GridImageAdapter(imageUrList);
        gridImageAdapter.bindToRecyclerView(serviceImageRecyclerView);
        serviceImageRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        gridImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                onThumbnailClick(view, gridImageAdapter.getData().get(position));

                List<ImageEntity> imageEntityList = parseImageEntityList(gridImageAdapter.getData());
                computeBoundsBackward(serviceImageRecyclerView, imageEntityList);
                GPreviewBuilder.from(mContext)
                        .setData(imageEntityList)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });
        TourCooLogUtil.d(TAG, "订单id:" + orderId);
        findMyService(orderId);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("服务详情");
    }


    private void showServiceInfo(ServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            return;
        }
        tvServiceContent.setText(TourCooUtil.getNotNullValue(serviceInfo.getDescription()));
        imageUrList.clear();
        List<String> imageList = serviceInfo.getImageUrls();
        if (serviceInfo.getImageUrls() == null) {
            return;
        }
        for (int i = 0; i < imageList.size(); i++) {
            imageList.set(i, RequestConfig.BASE + imageList.get(i));
        }
        imageUrList.addAll(imageList);
        gridImageAdapter.notifyDataSetChanged();

    }


    private void findMyService(int orderId) {
        TourCooLogUtil.i(TAG, "订单编号:" + orderId);
        imageUrList.clear();
        ApiRepository.getInstance().findMyService(orderId).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<ServiceInfo>>() {
                    @Override
                    public void onRequestNext(BaseEntity<ServiceInfo> entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                    showServiceInfo(entity.data);
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
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

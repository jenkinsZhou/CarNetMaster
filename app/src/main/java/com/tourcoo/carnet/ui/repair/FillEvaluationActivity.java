package com.tourcoo.carnet.ui.repair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.UploadImageAdapter;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.UploadProgressBody;
import com.tourcoo.carnet.core.frame.retrofit.UploadRequestListener;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.FaultRepairEntity;
import com.tourcoo.carnet.entity.ImgeEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :填写评价
 * @company :途酷科技
 * @date 2019年03月25日9:43
 * @Email: 971613168@qq.com
 */
public class FillEvaluationActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private RatingBar ratingBar;
    private TextView tvRating;
    private KProgressHUD hud;
    private RelativeLayout rlAddImage;
    private RecyclerView mRecyclerView;
    private UploadImageAdapter uploadImageAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private EditText etComment;
    private List<String> imageList = new ArrayList<>();
    private FaultRepairEntity.FaultRepairInfo faultRepairInfo;
    private MyHandler mHandler = new MyHandler(this);
    private Message message;
    /**
     * 后台回调回来的图片ur集合
     */
    private String mImages = "";

    @Override
    public int getContentLayout() {
        return R.layout.activity_fill_evaluation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ratingBar = findViewById(R.id.ratingBar);
        tvRating = findViewById(R.id.tvRating);
        initProgressDialog();
        rlAddImage = mContentView.findViewById(R.id.rlAddImage);
        mRecyclerView = mContentView.findViewById(R.id.rvUploadImage);
        etComment = mContentView.findViewById(R.id.etComment);
        findViewById(R.id.ivCamera).setOnClickListener(this);
        findViewById(R.id.btnCommitEvaluate).setOnClickListener(this);
        ratingBar.setRating(5.0f);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                showRating((int) rating + "分");
            }
        });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("填写评价");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCamera:
                selectPic();
                break;
            case R.id.btnCommitEvaluate:
                doUploadComment();
                break;
            default:
                break;
        }
    }


    private void showRating(String rating) {
        tvRating.setText(rating);
    }

    private int getRating() {
        return (int) ratingBar.getRating();
    }

    @Override
    public void loadData() {
        super.loadData();
        faultRepairInfo = (FaultRepairEntity.FaultRepairInfo) getIntent().getExtras().getSerializable("FaultRepairInfo");
        GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        uploadImageAdapter = new UploadImageAdapter(mContext, onAddPicClickListener);
        uploadImageAdapter.setOnEmptyCallBack(new UploadImageAdapter.OnEmptyCallBack() {
            @Override
            public void empty() {
                showAddIcon();
            }
        });
        mRecyclerView.setAdapter(uploadImageAdapter);
        initItemClick();
    }

    private void initItemClick() {
        uploadImageAdapter.setOnItemClickListener(new UploadImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(mContext).themeStyle(R.style.PicturePickerStyle).openExternalPreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }


    private void showAddIcon() {
        rlAddImage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        rlAddImage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    uploadImageAdapter.setList(selectList);
                    imageList.clear();
                    for (LocalMedia localMedia : selectList) {
                        imageList.add(localMedia.getCompressPath());
                    }
                    uploadImageAdapter.notifyDataSetChanged();
                    showRecyclerView();
                    break;
                default:
                    showAddIcon();
                    break;



            }
        }
    }


    private UploadImageAdapter.OnAddPictureClickListener onAddPicClickListener = new UploadImageAdapter.OnAddPictureClickListener() {
        @Override
        public void onAddPicClick() {
            selectPic();
        }
    };

    private void selectPic() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PicturePickerStyle)
                // 最大图片选择数量
                .maxSelectNum(8)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(4)
                // 多选 or 单选
                .selectionMode(PictureConfig.MULTIPLE)
                // 是否可预览图片
                .previewImage(true)
                // 是否可播放音频
                .enablePreviewAudio(false)
                // 是否显示拍照按钮
                .isCamera(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                // 是否裁剪
                .enableCrop(false)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 是否显示uCrop工具栏，默认不显示
                .hideBottomControls(false)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(false)
                // 是否传入已选图片
                .selectionMedia(selectList)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 评价
     */
    private void commentOrder(FaultRepairEntity.FaultRepairInfo faultRepairInfo) {
        if (faultRepairInfo == null) {
            ToastUtil.show("未获取到订单信息");
            return;
        }
        if (TextUtils.isEmpty(getComment())) {
            ToastUtil.show("请先填写评价");
            return;
        }
        ApiRepository.getInstance().commentOrder(faultRepairInfo, getComment(), getRating(),mImages).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                ToastUtil.showSuccess("评价已提交");
                                clearInput();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    private void clearInput() {
        etComment.setText("");
        mImages = "";
        imageList.clear();
        selectList.clear();
        uploadImageAdapter.notifyDataSetChanged();
    }


    private String getComment() {
        return etComment.getText().toString();
    }


    /**
     * 评价上传
     */
    private void doUploadComment() {
        if (selectList.isEmpty()) {
            commentOrder(faultRepairInfo);
        } else {
            uploadImage(imageList);
        }
    }


    /**
     * 上传图片
     *
     * @param imageList
     */
    private void uploadImage(List<String> imageList) {
        if (getComment().isEmpty()) {
            ToastUtil.show("请先填写故障描述");
            return;
        }
        if (imageList == null || imageList.isEmpty()) {
            ToastUtil.show("您还没选择图片");
            return;
        }
        File file;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //注意，file是后台约定的参数，如果是多图，files，如果是单张图片，file就行
        for (String imagePath : imageList) {
            //这里上传的是多图
            file = new File(imagePath);
            builder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        RequestBody requestBody = builder.build();

        UploadProgressBody uploadProgressBody = new UploadProgressBody(requestBody, new UploadRequestListener() {
            @Override
            public void onProgress(float progress, long current, long total) {
                message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = (int) (progress * 100);
                mHandler.sendMessage(message);
            }

            @Override
            public void onFail(Throwable e) {
                TourcooLogUtil.e("异常：" + e.toString());
                closeHudProgressDialog();
            }
        });
        showHudProgressDialog();
        ApiRepository.getInstance().getApiService().uploadFiles(uploadProgressBody).enqueue(new Callback<BaseEntity<List<ImgeEntity>>>() {
            @Override
            public void onResponse(Call<BaseEntity<List<ImgeEntity>>> call, Response<BaseEntity<List<ImgeEntity>>> response) {
                closeHudProgressDialog();
                BaseEntity<List<ImgeEntity>> resp = response.body();
                if (resp != null) {
                    if (resp.code == CODE_REQUEST_SUCCESS && resp.data != null) {
                        List<String> imageUrl = new ArrayList<>();
                        for (ImgeEntity image : resp.data) {
                            imageUrl.add(image.getUrl());
                        }
                        mImages = StringUtils.join(imageUrl, ",");
                        commentOrder(faultRepairInfo);
                    } else {
                        ToastUtil.showFailed(resp.message);
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseEntity<List<ImgeEntity>>> call, Throwable t) {
                Toast.makeText(mContext, "图片上传失败，稍后重试", Toast.LENGTH_SHORT).show();
               closeHudProgressDialog();
               closeLoadingDialog();
            }
        });
    }

    private void initProgressDialog() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAutoDismiss(false)
                .setMaxProgress(100);
    }


    private void showHudProgressDialog() {
        if (hud != null) {
            hud.setProgress(0);
            hud.show();
        }else {
            initProgressDialog();
            hud.show();
        }
    }

    private void updateProgress(int progress) {
        TourcooLogUtil.i("进度：" + progress);
        hud.setProgress(progress);
    }

    private void closeHudProgressDialog() {
        if (hud != null) {
            hud.setProgress(0);
            hud.dismiss();
            hud =null;
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<FillEvaluationActivity> mEvaluationActivityWeakReference;

        MyHandler(FillEvaluationActivity fillEvaluationActivity) {
            mEvaluationActivityWeakReference = new WeakReference<>(fillEvaluationActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mEvaluationActivityWeakReference.get().updateProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }


}

package com.tourcoo.carnet.ui.car;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
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
import com.tourcoo.carnet.core.helper.LocateHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.permission.PermissionConstance;
import com.tourcoo.carnet.core.permission.PermissionManager;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.confirm.ConfirmDialog;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.ImgeEntity;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.factory.NearbyRepairFactoryActivity;
import com.tourcoo.carnet.ui.order.OrderHistoryActivity;
import com.tourcoo.carnet.ui.repair.RepairFaultFragment;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.apache.commons.lang.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tourcoo.carnet.core.common.CommonConstant.TYPE_CAR_REPAIR;
import static com.tourcoo.carnet.core.common.CommonConstant.TYPE_CAR_WASH;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;


/**
 * @author :zhoujian
 * @description :故障报修Fragment
 * @company :翼迈科技
 * @date 2019年 03月 15日 20时55分
 * @Email: 971613168@qq.com
 */
public class CarRepairActivity extends BaseTourCooTitleActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private RelativeLayout rlAddImage;
    private RecyclerView mRecyclerView;
    private UploadImageAdapter uploadImageAdapter;
    private TextView tvLocation;
    private TextView btnLocate;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private AMapLocation mapLocation;
    private EditText etRepairContent;
    private KProgressHUD hud;
    private MyHandler mHandler = new MyHandler(this);
    private Message message;
    private String currentPosition;
    /**
     * 回调回来的图片ur集合
     */
    private String mImages = "";

    @Override
    public int getContentLayout() {
        return R.layout.fragment_fault_repair;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.ivCamera).setOnClickListener(this);
        mContentView.findViewById(R.id.btnRepair).setOnClickListener(this);
        btnLocate = mContentView.findViewById(R.id.btnLocate);
        tvLocation = mContentView.findViewById(R.id.tvLocation);
        btnLocate.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        rlAddImage = mContentView.findViewById(R.id.rlAddImage);
        mRecyclerView = mContentView.findViewById(R.id.rvUploadImage);
        etRepairContent = mContentView.findViewById(R.id.etRepairContent);
        initProgressDialog();
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("上门维修");
        titleBar.setRightText("历史维修");
        titleBar.setRightTextColor(TourcooUtil.getColor(R.color.blueCommon));
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourcooUtil.startActivity(mContext,OrderHistoryActivity.class);
                EventBus.getDefault().postSticky(new BaseEvent(TYPE_CAR_REPAIR));
            }
        });
    }


    @Override
    public void loadData() {
        super.loadData();
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
        getLocate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCamera:
//                selectPhoto();
                selectPic();
                break;
            case R.id.btnRepair:
                doUpload();
                break;
            case R.id.btnLocate:
                getLocate();
                break;
            case R.id.tvLocation:
                getLocate();
                break;
            default:
                break;
        }
    }

    public static RepairFaultFragment newInstance() {
        Bundle args = new Bundle();
        RepairFaultFragment fragment = new RepairFaultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
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

    /**
     * 显示验证码
     *
     * @param vCode
     */
    private void showVCodeDialog(String vCode) {
        String msg = "接单验证码:";
        msg += vCode;
        showAlertDialog("提交成功", msg, "我知道了");

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


    private void getLocate() {
        showLoadingDialog();
        showLocating();
        if (checkLocatePermission()) {
            locate();
        } else {
            PermissionManager.requestPermission(CarRepairActivity.this, PermissionConstance.TIP_PERMISSION_LOCATE, PermissionConstance.PERMISSION_CODE_LOCATE, PermissionConstance.PERMS_LOCATE);
        }
    }

    private String showResult(AMapLocation location) {
        StringBuffer sb = new StringBuffer();
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.getSatellites() + "\n");
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
//                sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
//            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间
//            sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
            //解析定位结果，
            return sb.toString();
        } else {
            return "定位失败，loc is null";
        }
    }

    /***
     * 定位
     */
    private void locate() {
        LocateHelper.getInstance().startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                String result = showResult(aMapLocation);
                mapLocation = aMapLocation;
                TourcooLogUtil.d(TAG, "回调结果:" + result);
                closeLoadingDialog();
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    showLocateSuccess(aMapLocation.getAddress());
                    currentPosition = getPosition(aMapLocation);
                } else {
                    showLocateFailed();
                }
                LocateHelper.getInstance().stopLocation();
            }
        });
    }

    /**
     * 检查定位权限
     */
    private boolean checkLocatePermission() {
        return PermissionManager.checkPermission(mContext, PermissionConstance.PERMS_LOCATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        closeLoadingDialog();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //权限已被用户授予
        closeLoadingDialog();
        getLocate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被用户拒绝
        ToastUtil.showFailed("您未授予定位权限,请前往授权管理授予权限");
        closeLoadingDialog();
        showLocateFailed();
    }

    private void showLocating() {
        tvLocation.setText("定位中...");
        addLocateImage("定位中...");
    }

    private void showLocateFailed() {
        tvLocation.setText("定位失败,未授予定位权限");
        addLocateImage("定位失败,未授予定位权限");
    }

    private void showLocateSuccess(String address) {
        tvLocation.setText(address);
        addLocateImage(address);
    }

    @Override
    public void onDestroy() {
        LocateHelper.getInstance().destroyLocationInstance();
        super.onDestroy();
    }


    /**
     * 上传故障报修
     */
    private void uploadReportFault(CarInfoEntity carInfoEntity) {
        if (carInfoEntity == null || carInfoEntity.getBrandName() == null) {
            ToastUtil.show("当前没有车辆,请先添加车辆");
            return;
        }
        if (TextUtils.isEmpty(currentPosition)) {
            ToastUtil.show("未获取位置信息");
            return;
        }
        if (TextUtils.isEmpty(getDetail())) {
            ToastUtil.show("请输入故障描述");
            return;
        }
        ApiRepository.getInstance().doorToDoorService(carInfoEntity, mImages, getDetail(), currentPosition,TYPE_CAR_REPAIR).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        closeLoadingDialog();
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                clearUploadData();
                                showVCodeDialog(entity.data.toString());
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }

                    }

                    @Override
                    public void onRequestError(Throwable e) {
                        closeLoadingDialog();
                        super.onRequestError(e);
                    }
                });
    }


    private String getDetail() {
        return etRepairContent.getText().toString();
    }


    /**
     * 获取位置信息
     *
     * @return
     */
    private String getPosition(AMapLocation mapLocation) {
        String position = "";
        if (mapLocation == null) {
            return position;
        }
        //经度
        position += mapLocation.getLongitude();
        position += ",";
        //纬度
        position += mapLocation.getLatitude();
        return position;
    }


    private void clearInput() {
        etRepairContent.setText("");
    }


    private void addLocateImage(String text) {
        SpannableString ss = new SpannableString(text + "  ");
        int len = ss.length();
        //图片
        Drawable d = ContextCompat.getDrawable(mContext, (R.mipmap.ic_positioning));
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //构建ImageSpan
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, len - 1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvLocation.setText(ss);
    }


    /**
     * 故障报修上传
     */
    private void doUpload() {
        if (selectList.isEmpty()) {
            uploadReportFault(AccountInfoHelper.getInstance().getCurrentInfoCarInfo());
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
        if (getDetail().isEmpty()) {
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
                        uploadReportFault(AccountInfoHelper.getInstance().getCurrentCar());
                    } else {
                        ToastUtil.showFailed(resp.message);
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseEntity<List<ImgeEntity>>> call, Throwable t) {
                Toast.makeText(mContext, "图片上传失败，稍后重试", Toast.LENGTH_SHORT).show();
                closeHudProgressDialog();
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
        }
    }

    private void updateProgress(int progress) {
        TourcooLogUtil.i("进度：" + progress);
        hud.setProgress(progress);
    }

    private void closeHudProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.setProgress(0);
            hud.dismiss();
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<CarRepairActivity> mFaultFragmentWeakReference;

        MyHandler(CarRepairActivity repairFaultFragment) {
            mFaultFragmentWeakReference = new WeakReference<>(repairFaultFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mFaultFragmentWeakReference.get().updateProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 清空上一次上传的数据
     */
    private void clearUploadData() {
        clearInput();
        mImages = "";
        imageList.clear();
        currentPosition = "";
        selectList.clear();
        tvLocation.setText("未获取位置信息");
        uploadImageAdapter.notifyDataSetChanged();
    }
}

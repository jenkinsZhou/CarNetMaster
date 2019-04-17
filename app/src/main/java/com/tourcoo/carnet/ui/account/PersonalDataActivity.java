package com.tourcoo.carnet.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.retrofit.UploadProgressBody;
import com.tourcoo.carnet.core.frame.retrofit.UploadRequestListener;
import com.tourcoo.carnet.core.frame.util.SizeUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.view.OptionsPickerView;
import com.tourcoo.carnet.core.widget.core.view.radius.RadiusEditText;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.core.widget.dialog.alert.EmiAlertDialog;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.ImgeEntity;
import com.tourcoo.carnet.entity.account.UserInfo;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.event.UserInfoRefreshEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.apache.commons.lang.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :zhoujian
 * @description :个人资料
 * @company :翼迈科技
 * @date 2019年 03月 16日 18时45分
 * @Email: 971613168@qq.com
 */
public class PersonalDataActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private OptionsPickerView opvDriveAge;
    private UserInfoEntity mUserInfoEntity;
    private TextView tvNickName;
    private TextView tvMobile;
    private TextView tvDrivingAge;
    private EditText mEditText;
    private TextView tvDriverLicenseNumber;
    private CircleImageView cvAvatar;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private KProgressHUD hud;
    private MyHandler mHandler = new MyHandler(this);
    private Message message;
    /**
     * 回调回来的图片ur集合
     */
    private String mImages = "";
    private int editFlag;
    private static final int EDIT_NICK_NAME = 1;
    private static final int EDIT_DRIVER_AGE = 2;
    private static final int EDIT_LICENSE = 3;
    private static final int EDIT_AVATAR = 4;
    private List<String> driveAgeStringList = new ArrayList<>();

    private List<Integer> driveAgeList = new ArrayList<>();

    @Override
    public int getContentLayout() {
        return R.layout.activity_personnal_data;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mUserInfoEntity = AccountInfoHelper.getInstance().getUserInfoEntity();
        tvNickName = findViewById(R.id.tvNickName);
        tvNickName.setOnClickListener(this);
        tvMobile = findViewById(R.id.tvMobile);
        tvMobile.setOnClickListener(this);
        tvDrivingAge = findViewById(R.id.tvDrivingAge);
//        tvDrivingAge.setOnClickListener(this);
        tvDriverLicenseNumber = findViewById(R.id.tvDriverLicenseNumber);
        cvAvatar = findViewById(R.id.cvAvatar);
        tvDriverLicenseNumber.setOnClickListener(this);
        cvAvatar.setOnClickListener(this);
        findViewById(R.id.tvEditPassword).setOnClickListener(this);
        findViewById(R.id.relayDrivingAge).setOnClickListener(this);
        showCurrentInfo(mUserInfoEntity);
        initAgeList();
        initDriveAgePicker();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("个人资料");
    }

    /**
     * 显示当前信息
     */
    private void showCurrentInfo(UserInfoEntity userInfoEntity) {
        UserInfo userInfo;
        if (userInfoEntity == null || userInfoEntity.getUserInfo() == null) {
            TourCooLogUtil.e(TAG, "用户信息为null");
            userInfoEntity = new UserInfoEntity();
            userInfo = new UserInfo();
            userInfoEntity.setUserInfo(userInfo);
        }
        userInfo = userInfoEntity.getUserInfo();
        if (TextUtils.isEmpty(userInfo.getNickname())) {
            tvNickName.setText("未填写");
        } else {
            tvNickName.setText(userInfo.getNickname());
        }
        if (TextUtils.isEmpty(userInfo.getMobile())) {
            tvMobile.setText("未填写");
        } else {
            String maskNumber = userInfo.getMobile();
            if (TourCooUtil.isMobileNumber(userInfo.getMobile())) {
                maskNumber = userInfo.getMobile().substring(0, 3) + "****" + maskNumber.substring(7, userInfo.getMobile().length());
            }
            tvMobile.setText(maskNumber);
        }
        if (TextUtils.isEmpty(userInfo.getDriverAge())) {
            tvDrivingAge.setText("未填写");
        } else {
            tvDrivingAge.setText(userInfo.getDriverAge() + "年");
        }
        if (TextUtils.isEmpty(userInfo.getDriverLicense())) {
            tvDriverLicenseNumber.setText("未填写");
        } else {
            tvDriverLicenseNumber.setText(userInfo.getDriverLicense());
        }
        String url = RequestConfig.BASE + userInfo.getAvatar();
        showAvatar(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvAvatar:
                editFlag = EDIT_AVATAR;
                selectPic();
                break;
            case R.id.tvNickName:
                editFlag = EDIT_NICK_NAME;
                showEditText(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tvDrivingAge:
          /*      editFlag = EDIT_DRIVER_AGE;
                showEditText(InputType.TYPE_CLASS_NUMBER);*/
                opvDriveAge.show();
                break;
            case R.id.tvDriverLicenseNumber:
                editFlag = EDIT_LICENSE;
                showEditText(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tvEditPassword:
                TourCooUtil.startActivity(mContext, EditPasswordActivity.class);
                break;
            case R.id.tvMobile:
                TourCooUtil.startActivity(mContext, ChangePhoneNumberActivity.class);
                break;
            case R.id.relayDrivingAge:
                opvDriveAge.show();
                break;
            default:
                break;
        }
    }


    private void selectPic() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.PicturePickerStyle)
                // 最大图片选择数量
                .maxSelectNum(1)
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
                .enableCrop(true)
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
                    imageList.clear();
                    for (LocalMedia localMedia : selectList) {
                        imageList.add(localMedia.getCompressPath());
                    }
                    //todo
                    for (String s : imageList) {
                        TourCooLogUtil.i(TAG, "图片:" + s);
                    }
                    uploadImage(imageList);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传图片
     *
     * @param imageList
     */
    private void uploadImage(List<String> imageList) {
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
                TourCooLogUtil.e("异常：" + e.toString());
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
                        //todo
                        Map<String, Object> hashMap = new HashMap<>(1);
                        hashMap.put("icon", mImages);
                        hashMap.put("ownerId", mUserInfoEntity.getUserInfo().getUserId());
                        editOwnerInfo(hashMap);
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


    private static class MyHandler extends Handler {
        WeakReference<PersonalDataActivity> personalDataActivity;

        MyHandler(PersonalDataActivity dataActivity) {
            personalDataActivity = new WeakReference<>(dataActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    personalDataActivity.get().updateProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
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
        } else {
            initProgressDialog();
        }
        hud.show();
    }

    private void updateProgress(int progress) {
        TourCooLogUtil.i("进度：" + progress);
        if (hud != null) {
            hud.setProgress(progress);
        }
    }

    private void closeHudProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.setProgress(0);
            hud.dismiss();
        }
        hud = null;
    }

    /**
     * 修改个人信息
     */
    private void editOwnerInfo(Map<String, Object> map) {
        ApiRepository.getInstance().editOwnerInfo(map).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                switch (editFlag) {
                                    //修改成功回调
                                    case EDIT_NICK_NAME:
                                        tvNickName.setText(getInputString());
                                        break;
                                    case EDIT_DRIVER_AGE:
                                        tvDrivingAge.setText(getInputString() + "年");
                                        break;
                                    case EDIT_LICENSE:
                                        tvDriverLicenseNumber.setText(getInputString());
                                        break;
                                    case EDIT_AVATAR:
                                        String url = RequestConfig.BASE + mImages;
                                        showAvatar(url);
                                        break;
                                    default:
                                        break;
                                }
                                sycUserInfo();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    /**
     * 获取非必填项参数
     */
    private HashMap<String, Object> getNonEssentialParams() {
        HashMap<String, Object> hashMap = new HashMap<>(4);
        //昵称
        if (!TextUtils.isEmpty(getNickName())) {
            hashMap.put("nickName", getNickName());
        }

        //驾驶证号
        if (!TextUtils.isEmpty(getDriverLicenseNumber())) {
            hashMap.put("licenseNo", getDriverLicenseNumber());
        }
        //驾龄
        if (!TextUtils.isEmpty(getDriverAge())) {
            int age;
            try {
                age = Integer.parseInt(getDriverAge());
            } catch (NumberFormatException e) {
                age = 1;
            }
            hashMap.put("age", age);
        }
        return hashMap;
    }


    private String getNickName() {
        return tvNickName.getText().toString();
    }

    private String getDriverAge() {
        return tvDrivingAge.getText().toString();
    }

    private void setDriverAge(String value) {
        tvDrivingAge.setText(value);
    }

    private String getBindMobileNumber() {
        return tvMobile.getText().toString();
    }

    private String getDriverLicenseNumber() {
        return tvDriverLicenseNumber.getText().toString();
    }

    private void setMobileNumber(String value) {
        tvMobile.setText(value);
    }

    private void setNickName(String value) {
        tvNickName.setText(value);
    }

    private void setDriverLicenseNumber(String value) {
        tvDriverLicenseNumber.setText(value);
    }

    private void showEditText(int inputType) {
        final RadiusEditText editText = new RadiusEditText(mContext);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.getDelegate()
                .setTextColor(Color.GRAY)
                .setRadius(6f)
                .setBackgroundColor(TourCooUtil.getColor(R.color.colorWhite))
                .setStrokeColor(Color.GRAY)
                .setStrokeWidth(SizeUtil.dp2px(0.5f));
        editText.setMinHeight(SizeUtil.dp2px(40));
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setPadding(SizeUtil.dp2px(12), 0, SizeUtil.dp2px(12), 0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        editText.setHint("请输入内容");
        editText.setLayoutParams(params);
        editText.setInputType(inputType);
        new EmiAlertDialog.DividerIosBuilder(this)
                .setTitle("输入信息")
                .setTitleTextColorResource(R.color.colorAlertTitle)
                .setView(editText)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        switch (editFlag) {
                            case EDIT_NICK_NAME:
                                editNickName();
                                break;
                            case EDIT_DRIVER_AGE:
                                break;
                            case EDIT_LICENSE:
                                editLicenseNo();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create()
                .setDimAmount(0.6f)
                .show();
        mEditText = editText;
    }


    private String getInputString() {
        if (mEditText == null) {
            return "";
        }
        return mEditText.getText().toString().trim();
    }


    private void editNickName() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("ownerId", mUserInfoEntity.getUserInfo().getUserId());
        TourCooLogUtil.i(TAG, "车主id：" + mUserInfoEntity.getUserInfo().getUserId());
        if (TextUtils.isEmpty(getInputString())) {
            ToastUtil.show("请输入内容");
            return;
        }
        map.put("nickName", getInputString());
        editOwnerInfo(map);
    }


    private void editDriverAge(int age) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("ownerId", mUserInfoEntity.getUserInfo().getUserId());
        map.put("driverAge", age);
        editOwnerInfo(map);
    }


    private void editLicenseNo() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("ownerId", mUserInfoEntity.getUserInfo().getUserId());
        if (TextUtils.isEmpty(getInputString())) {
            ToastUtil.show("请输入内容");
            return;
        }
        map.put("licenseNo", getInputString());
        editOwnerInfo(map);
    }


    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    private void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            TourCooLogUtil.e(TAG, "userInfo== null 无法更新");
            return;
        }
        int userId = AccountInfoHelper.getInstance().getUserInfoEntity().getUserInfo().getUserId();
        userInfo.setUserId(userId);
        TourCooLogUtil.i("更新syc", userInfo);
        AccountInfoHelper.getInstance().updateAndSaveUserInfo(userInfo);
        showCurrentInfo(AccountInfoHelper.getInstance().getUserInfoEntity());
    }


    private void showAvatar(String url) {
        TourCooLogUtil.i(TAG, "图片URL：" + url);
        GlideManager.loadImg(url, cvAvatar, TourCooUtil.getDrawable(R.mipmap.img_default_minerva));
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    /**
     * 同步个人信息
     */
    private void sycUserInfo() {
        ApiRepository.getInstance().getUserInfo().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<UserInfoEntity>>() {
                    @Override
                    public void onRequestNext(BaseEntity<UserInfoEntity> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                    updateUserInfo(entity.data.getUserInfo());
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    /**
     * 刷新个人信息事件
     *
     * @param event
     */
    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void refreshUserInfo(UserInfoRefreshEvent event) {
        if (event != null) {
            TourCooLogUtil.i(TAG, "接收到消息");
            sycUserInfo();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 初始化驾龄选择器
     */
    @SuppressWarnings("uncheked")
    private void initDriveAgePicker() {
        // 不联动
        opvDriveAge = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
             editDriverAge(driveAgeList.get(options1));
            }
        }).build();
        opvDriveAge.setNPicker(driveAgeStringList, null, null);
        opvDriveAge.setSelectOptions(0, 1, 1);
    }

    private void initAgeList() {
        driveAgeList.clear();
        driveAgeStringList.clear();
        int maxSize = 60;
        for (int i = 1; i <= maxSize; i++) {
            driveAgeStringList.add(i + "年");
            driveAgeList.add(i);
        }
    }




}

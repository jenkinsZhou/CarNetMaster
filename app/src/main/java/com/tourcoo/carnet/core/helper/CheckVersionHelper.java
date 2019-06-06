package com.tourcoo.carnet.core.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseDownloadObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.retrofit.TourCoolRetrofit;
import com.tourcoo.carnet.core.frame.util.FileUtil;
import com.tourcoo.carnet.core.frame.util.FormatUtil;
import com.tourcoo.carnet.core.frame.util.StackUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.UpdateEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;

/**
 * @author :zhoujian
 * @description :版本更新帮助类
 * @company :翼迈科技
 * @date 2019年 04月 14日 14时01分
 * @Email: 971613168@qq.com
 */
public class CheckVersionHelper {


    private BaseDownloadObserver mDownloadObserver;
    private SoftReference<BaseActivity> mActivity;
    private boolean mIsLoading = false;

    private CheckVersionHelper(BaseActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public static CheckVersionHelper with(BaseActivity activity) {
        return new CheckVersionHelper(activity);
    }


    /**
     * Function:开放平台监测版本升级
     *
     * @param
     * @return
     */
    public void checkVersion(boolean loading) {
        BaseActivity activity = mActivity.get();
        mIsLoading = loading;
        if (activity == null) {
            return;
        }
        ApiRepository.getInstance().appVersionInfo()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(loading ?
                        new BaseLoadingObserver<BaseEntity>(R.string.checking) {
                            @Override
                            public void onError(Throwable e) {
                                if (mIsLoading) {
                                    super.onError(e);
                                }
                            }

                            @Override
                            public void onRequestNext(BaseEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("当前已是最新版本");
                                    return;
                                }
                                UpdateEntity updateEntity = new UpdateEntity();
                                updateEntity.force = false;
                                updateEntity.versionName = "1.0.2";
                                updateEntity.versionCode = 2;
                                updateEntity.url = "http://www.baidu.com/bg_rectangle_blue_hollow_horizon.apk";
                                checkVersion(updateEntity);
                            }
                        } :
                        new BaseObserver<BaseEntity>() {
                            @Override
                            public void onError(Throwable e) {
                                if (mIsLoading) {
                                    super.onError(e);
                                }
                            }

                            @Override
                            public void onRequestNext(BaseEntity entity) {
                                if (entity == null) {
                                    ToastUtil.show("当前已是最新版本");
                                    return;
                                }
                                UpdateEntity updateEntity = new UpdateEntity();
                                updateEntity.force = false;
                                updateEntity.versionName = "1.0.2";
                                updateEntity.versionCode = 2;
                                updateEntity.url = "http://www.baidu.com/bg_rectangle_blue_hollow_horizon.apk";
                                checkVersion(updateEntity);
                            }
                        });
    }

    private void checkVersion(UpdateEntity entity) {
        if (entity == null) {
            if (mIsLoading) {
                ToastUtil.show("版本信息有误");
            }
            return;
        }
        if (!entity.isSuccess()) {
            if (mIsLoading) {
                ToastUtil.show(entity.getMessage());
            }
            return;
        }
        if (TextUtils.isEmpty(entity.url) || !entity.url.contains("apk")) {
            if (mIsLoading) {
                ToastUtil.show("不是有效的下载链接:" + entity.url);
            }
            TourCooLogUtil.e("检测新版本:不是有效的apk下载链接");
            return;
        }
        showAlert(entity);
    }

    /**
     * 提示用户
     *
     * @param entity
     */
    private void showAlert(UpdateEntity entity) {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            activity = StackUtil.getInstance().getCurrent();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("发现新版本:V" + entity.versionName)
                .setMessage(entity.getMessage())
                .setPositiveButton(R.string.update_now, (dialog, which) ->
                        downloadApk(entity, "FastLib_" + entity.versionName + ".apk", true));
        if (!entity.force) {
            builder.setNegativeButton("暂不更新", null);
        }
        builder.create()
                .show();
    }

    /**
     * 下载apk--实际情况需自己调整逻辑避免因range不准造成下载解析不了问题--建议普通应用包下载(20M以内的不使用断点续传)
     *
     * @param entity
     * @param fileName      文件名
     * @param isRangeEnable 是否断点续传
     */
    public void downloadApk(UpdateEntity entity, String fileName, boolean isRangeEnable) {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            activity = StackUtil.getInstance().getCurrent();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(entity.getTitle());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage(entity.getMessage());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(!entity.force);
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setCanceledOnTouchOutside(!entity.force);

        //暂停下载-慎用;建议使用 Disposable.dispose();
//        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂停", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (mDownloadObserver != null) {
//                    mDownloadObserver.pause();
//                }
//            }
//        });

        File mLocal = new File(FileUtil.getCacheDir(), fileName);
        Map<String, Object> header = null;
        long length = mLocal.length();
        if (isRangeEnable) {
            header = new HashMap<>(1);
            header.put("range", "bytes=" + length + "-");
            TourCooLogUtil.i("downloadApk", "length:" + length);
        }
        //不同url不能使用相同的本地绝对路径不然很可能将B的后半部分下载追加到A的后面--最终也是错误的
        ProgressDialog finalMProgressDialog = mProgressDialog;
        mDownloadObserver = new BaseDownloadObserver(fileName, finalMProgressDialog, isRangeEnable) {
            @Override
            public void onSuccess(File file) {
                FileUtil.installApk(file);
            }

            @Override
            public void onFail(Throwable e) {
                TourCooLogUtil.e("downloadApk", e.getMessage());
                //HTTP 416 Range Not Satisfiable 出现该错误--很大可能性是文件已下载完成传递的
                boolean satisfiable = e != null && e.getMessage().contains("416") && e.getMessage().toLowerCase().contains("range");
                if (satisfiable) {
                    onSuccess(mLocal);
                    return;
                }
                boolean isPause = e != null && e.getMessage().equals(BaseDownloadObserver.DOWNLOAD_PAUSE);
                if (isPause) {
                    ToastUtil.show("暂停下载");
                    return;
                }
                ToastUtil.show("下载失败:" + e.getMessage());
            }

            @Override
            public void onProgress(float progress, long current, long total) {
                TourCooLogUtil.i("downloadApk", "current:" + current + ";total:" + total);
                if (!finalMProgressDialog.isShowing()) {
                    return;
                }
                finalMProgressDialog.setProgressNumberFormat(FormatUtil.formatDataSize(current) + "/" + FormatUtil.formatDataSize(total));
                finalMProgressDialog.setMax((int) total);
                finalMProgressDialog.setProgress((int) current);
            }
        };
        TourCoolRetrofit.getInstance().downloadFile(entity.url, header)
                .compose(((RxAppCompatActivity) activity).bindUntilEvent(ActivityEvent.DESTROY))
                //可自定义保存路径默认//storage/emulated/0/Android/data/<package-name>/cache/xxx/
                .subscribe(mDownloadObserver);
    }

}

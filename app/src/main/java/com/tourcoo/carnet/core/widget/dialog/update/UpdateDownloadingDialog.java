package com.tourcoo.carnet.core.widget.dialog.update;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.core.widget.custom.HorizontalProgressBar;

/**
 * @author :JenkinsZhou
 * @description :下载更新
 * @company :途酷科技
 * @date 2019年06月12日14:22
 * @Email: 971613168@qq.com
 */
public class UpdateDownloadingDialog extends BaseUpdateDialog {
    private HorizontalProgressBar hpbDownloading;

    public UpdateDownloadingDialog(Context context, int theme, int res) {
        super(context, theme, res);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout llUpdateContent = findViewById(R.id.llUpdateContent);
        llUpdateContent.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
        hpbDownloading = findViewById(R.id.hpbDownloading);
        hpbDownloading.setProgressColor(TourCooUtil.getColor(R.color.colorPrimary))
                .setTipColor(TourCooUtil.getColor(R.color.colorPrimary))
                .startProgressAnimation();
    }

    public void setProgress(float progress) {
        if (hpbDownloading != null) {
            hpbDownloading.setCurrentProgress(progress);
        }
    }

    public void setProgressWithTip(float progress) {
        if (hpbDownloading != null) {
            hpbDownloading.setCurrentProgress(progress);
            hpbDownloading.setProgressWithAnimation(progress);
        }
    }
}

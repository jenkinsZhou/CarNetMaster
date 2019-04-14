package com.tourcoo.carnet.core.helper;

import android.app.Activity;
import android.content.Intent;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.BaseHelper;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.widget.core.util.StatusBarUtil;

import java.util.List;

/**
 * @author :zhoujian
 * @description :图片选择帮助类
 * @company :翼迈科技
 * @date 2019年 03月 16日 00时37分
 * @Email: 971613168@qq.com
 */
public class ImagePickerHelper extends BaseHelper {

    public final static int IMG = 10000;
    private int mRequestCode;

    private OnImageSelect mOnImageSelect;

    public interface OnImageSelect {
        /**
         * 图片选择回调
         *
         * @param resultCode
         * @param requestCode
         * @param list
         */
        void onImageSelectCallback(int requestCode,int resultCode, List<LocalMedia> list);
    }

    public ImagePickerHelper(Activity activity) {
        super(activity);
    }

    public void selectPicture(int requestCode, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(StatusBarUtil.isSupportStatusBarFontChange() ? R.style.PicturePickerStyle : R.style.PicturePickerStyle_White)
                .selectionMode(PictureConfig.SINGLE)
                .forResult(mRequestCode);
    }

    public void selectPicture(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(StatusBarUtil.isSupportStatusBarFontChange() ? R.style.PicturePickerStyle : R.style.PicturePickerStyle_White)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_PICTURE)
                .forResult(mRequestCode);
    }

    public void selectFile(int requestCode, int count, OnImageSelect onImageSelect) {
        this.mOnImageSelect = onImageSelect;
        this.mRequestCode = requestCode;
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(StatusBarUtil.isSupportStatusBarFontChange() ? R.style.PicturePickerStyle : R.style.PicturePickerStyle_White)
                .maxSelectNum(count)
                .selectionMode(PictureConfig.TYPE_ALL)
                .forResult(mRequestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TourcooLogUtil.i("onActivityResult", "path:");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode != mRequestCode) {
                return;
            }
            // 图片、视频、音频选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            if (mOnImageSelect != null) {
                mOnImageSelect.onImageSelectCallback(mRequestCode, resultCode,selectList);
            }
        }
    }
}

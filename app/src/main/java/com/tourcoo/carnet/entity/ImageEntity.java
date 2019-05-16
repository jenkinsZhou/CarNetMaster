package com.tourcoo.carnet.entity;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;


/**
 * @author :JenkinsZhou
 * @description :图片浏览实体
 * @company :途酷科技
 * @date 2019年05月16日15:27
 * @Email: 971613168@qq.com
 */
public class ImageEntity implements IThumbViewInfo {
    private String imageUrl;
    private Rect mBounds;

    public ImageEntity() {
    }

    public ImageEntity(String imageUrl, Rect bounds) {
        this.imageUrl = imageUrl;
        mBounds = bounds;
    }

    @Override
    public String getUrl() {
        return imageUrl;
    }

    @Override
    public Rect getBounds() {
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.mBounds, flags);
    }

    protected ImageEntity(Parcel in) {
        this.imageUrl = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImageEntity> CREATOR = new Parcelable.Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel source) {
            return new ImageEntity(source);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }
}

package com.tourcoo.carnet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * @author :JenkinsZhou
 * @description :系统消息实体类
 * @company :途酷科技
 * @date 2019年03月18日13:54
 * @Email: 971613168@qq.com
 */
public class MsgSystemEntity extends LitePalSupport implements Parcelable {
   private String msgTime;

   private boolean hasRed;

    public boolean isHasRed() {
        return hasRed;
    }

    public void setHasRed(boolean hasRed) {
        this.hasRed = hasRed;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    private String msgContent;

    public MsgSystemEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msgTime);
        dest.writeByte(this.hasRed ? (byte) 1 : (byte) 0);
        dest.writeString(this.msgContent);
    }

    protected MsgSystemEntity(Parcel in) {
        this.msgTime = in.readString();
        this.hasRed = in.readByte() != 0;
        this.msgContent = in.readString();
    }

    public static final Creator<MsgSystemEntity> CREATOR = new Creator<MsgSystemEntity>() {
        @Override
        public MsgSystemEntity createFromParcel(Parcel source) {
            return new MsgSystemEntity(source);
        }

        @Override
        public MsgSystemEntity[] newArray(int size) {
            return new MsgSystemEntity[size];
        }
    };
}

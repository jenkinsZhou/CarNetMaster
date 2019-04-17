package com.tourcoo.carnet.entity.garage;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月13日15:11
 * @Email: 971613168@qq.com
 */
public class CommentInfo implements Serializable {


    /**
     * createTime : 2019-04-12 17:06:42
     * detail : 陈舟为评价3星无图片
     * images :
     * level : 3
     * nickName : 陈舟为
     * ownerIconUrl : img/2019-04-15-f460402c-5225-42e1-9af3-aa47c33fe641.png
     * reply :
     */

    private String createTime;
    private String detail;
    private String images;
    private int level;
    private String nickName;
    private String ownerIconUrl;
    private String reply;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOwnerIconUrl() {
        return ownerIconUrl;
    }

    public void setOwnerIconUrl(String ownerIconUrl) {
        this.ownerIconUrl = ownerIconUrl;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}

package com.tourcoo.carnet.entity.garage;

/**
 * @author :JenkinsZhou
 * @description :评价详情
 * @company :途酷科技
 * @date 2019年04月16日10:25
 * @Email: 971613168@qq.com
 */
public class CommentDetail {


    /**
     * createTime : 2019-04-11 18:53:17
     * detail : 哦哦哦哦
     * images :
     * level : 5
     * nickName :
     * ownerIconUrl :
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

package com.tourcoo.carnet.entity.garage;

import java.util.List;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 04月 20日 00时50分
 * @Email: 971613168@qq.com
 */
public class ServiceInfo {


    /**
     * createTime : 2019-04-18 18:35:05
     * description :
     * imageUrls : []
     */

    private String createTime;
    private String description;
    private List<String> imageUrls;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

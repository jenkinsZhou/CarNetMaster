package com.tourcoo.carnet.entity.car;

import com.tourcoo.carnet.core.widget.wheelview.contrarywind.interfaces.IPickerViewData;

/**
 * @author :JenkinsZhou
 * @description :汽车型号
 * @company :途酷科技
 * @date 2019年04月08日11:38
 * @Email: 971613168@qq.com
 */
public class CarModel implements IPickerViewData {

    /**
     * createTime :
     * createUser :
     * id : 2
     * image :
     * name : smart
     * pid : 1
     * status : normal
     * type : 2
     * updateTime :
     * updateUser :
     */

    private String createTime;
    private String createUser;
    private int id;
    private String image;
    private String name = "";
    private String pid;
    private String status;
    private int type;
    private String updateTime;
    private String updateUser;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String getPickerViewText() {
        return getName();
    }
}

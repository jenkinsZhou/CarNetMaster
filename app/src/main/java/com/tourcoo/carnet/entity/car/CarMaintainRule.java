package com.tourcoo.carnet.entity.car;

import com.tourcoo.carnet.core.widget.wheelview.contrarywind.interfaces.IPickerViewData;

/**
 * @author :JenkinsZhou
 * @description :车辆保养
 * @company :途酷科技
 * @date 2019年04月08日15:25
 * @Email: 971613168@qq.com
 */
public class CarMaintainRule implements IPickerViewData {


    /**
     * code : MILEAGE
     * createTime : 2019-03-18 11:08:34
     * createUser : 1
     * description : 按里程保养
     * dictId : 1107478874608439297
     * name : 里程
     * pid : 1107478550296465410
     * sort : 1
     * updateTime :
     * updateUser :
     */

    private String code;
    private String createTime;
    private String createUser;
    private String description = "";
    private String dictId;
    private String name;
    private String pid;
    private int sort;
    private String updateTime;
    private String updateUser;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
        return getDescription();
    }
}

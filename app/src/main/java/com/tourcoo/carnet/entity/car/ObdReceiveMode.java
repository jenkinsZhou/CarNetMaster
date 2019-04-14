package com.tourcoo.carnet.entity.car;

import com.tourcoo.carnet.core.widget.wheelview.contrarywind.interfaces.IPickerViewData;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月08日16:11
 * @Email: 971613168@qq.com
 */
public class ObdReceiveMode implements IPickerViewData {

    /**
     * code : REALTIME
     * createTime : 2019-03-18 11:34:16
     * createUser : 1
     * description :
     * dictId : 1107485342841769986
     * name : 实时
     * pid : 1107485265154871298
     * sort : 1
     * updateTime :
     * updateUser :
     */

    private String code;
    private String createTime;
    private String createUser;
    private String description;
    private String dictId;
    private String name = "";
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
        return name;
    }
}

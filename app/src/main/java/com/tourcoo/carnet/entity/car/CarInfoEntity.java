package com.tourcoo.carnet.entity.car;

import com.tourcoo.carnet.core.widget.wheelview.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :车辆信息实体
 * @company :途酷科技
 * @date 2019年04月04日11:41
 * @Email: 971613168@qq.com
 */
public class CarInfoEntity implements Serializable, IPickerViewData {
    public static final int REMIND_CLOSE = 0;
    public static final int REMIND_OPEN = 1;
    public String insurer_date;
    public String maintain_date;

    public String yearly_date;

    public String getInsurer_date() {
        return insurer_date;
    }

    public void setInsurer_date(String insurer_date) {
        this.insurer_date = insurer_date;
    }

    public String getMaintain_date() {
        return maintain_date;
    }

    public void setMaintain_date(String maintain_date) {
        this.maintain_date = maintain_date;
    }

    public String getYearly_date() {
        return yearly_date;
    }

    public void setYearly_date(String yearly_date) {
        this.yearly_date = yearly_date;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    private boolean is_default;

    public boolean isDefault() {
        return is_default;
    }

    public void setIsDefault(boolean isDefault) {
        this.is_default = isDefault;
    }

    public String getPlate_num() {
        return plate_num;
    }

    public void setPlate_num(String plate_num) {
        this.plate_num = plate_num;
    }

    /**
     * insurer_remind : true
     * fault_remind : 0
     * obd_receive : 0
     * brandName : 奔驰
     * modeName : smart
     * create_time : 2019-03-31 09:59:45
     * owner_id : 5
     * maintain :
     * model_id : 2
     * yearly_remind : true
     * update_time : 2019-03-31 09:59:45
     * maintain_remind : true
     * update_user : 2
     * engine : 2424F43qrGDDD34545
     * insurer :
     * displacement :
     * id : 3
     * create_user : 2
     * yearly :
     * status : 0
     */
    private String plate_num;
    private boolean insurer_remind;
    private String fault_remind;
    private String obd_receive;
    private String brandName;
    private String modeName;
    private String create_time;
    private int owner_id;
    private String maintain;
    private int model_id;
    private boolean yearly_remind;
    private String update_time;
    private boolean maintain_remind;
    private String update_user;
    private String engine;
    private String insurer;
    private String displacement;
    private int id;
    private String create_user;
    private String yearly;
    private int status;
    private String obd_sn;

    public String getObd_sn() {
        return obd_sn;
    }

    public void setObd_sn(String obd_sn) {
        this.obd_sn = obd_sn;
    }

    private String maintain_rule;

    public String getMaintain_rule() {
        return maintain_rule;
    }

    public void setMaintain_rule(String maintain_rule) {
        this.maintain_rule = maintain_rule;
    }

    public boolean isInsurer_remind() {
        return insurer_remind;
    }

    public void setInsurer_remind(boolean insurer_remind) {
        this.insurer_remind = insurer_remind;
    }

    public String getFault_remind() {
        return fault_remind;
    }

    public void setFault_remind(String fault_remind) {
        this.fault_remind = fault_remind;
    }

    public String getObd_receive() {
        return obd_receive;
    }

    public void setObd_receive(String obd_receive) {
        this.obd_receive = obd_receive;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getMaintain() {
        return maintain;
    }

    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public boolean isYearly_remind() {
        return yearly_remind;
    }

    public void setYearly_remind(boolean yearly_remind) {
        this.yearly_remind = yearly_remind;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean isMaintain_remind() {
        return maintain_remind;
    }

    public void setMaintain_remind(boolean maintain_remind) {
        this.maintain_remind = maintain_remind;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getInsurer() {
        return insurer;
    }

    public void setInsurer(String insurer) {
        this.insurer = insurer;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getYearly() {
        return yearly;
    }

    public void setYearly(String yearly) {
        this.yearly = yearly;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getPickerViewText() {
        return brandName;
    }
}

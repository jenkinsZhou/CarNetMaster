package com.tourcoo.carnet.entity.garage;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月13日15:11
 * @Email: 971613168@qq.com
 */
public class CommentInfo {


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    /**
     * garage_id : 26
     * update_time : 2019-04-09 14:16:20
     * update_user : 9
     * create_time : 2019-04-09 14:16:20
     * level : 5
     * owner_id : 9
     * id : 3
     * detail : 很好
     * create_user : 9
     * reply :
     * order_id : 42
     */

    private int garage_id;
    private String update_time;
    private String update_user;
    private String create_time;
    private int level;
    private String owner_id;
    private long id;
    private String detail;
    private String create_user;
    private String reply;
    private int order_id;

    public int getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(int garage_id) {
        this.garage_id = garage_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }



}

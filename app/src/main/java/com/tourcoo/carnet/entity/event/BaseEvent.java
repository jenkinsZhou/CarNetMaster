package com.tourcoo.carnet.entity.event;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月10日17:39
 * @Email: 971613168@qq.com
 */
public class BaseEvent {
    public int id;
    public String tag;

    public BaseEvent(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public BaseEvent() {

    }

    public BaseEvent(int id) {
        this.id = id;
    }
}

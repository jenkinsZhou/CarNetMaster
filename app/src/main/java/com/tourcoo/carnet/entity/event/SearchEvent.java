package com.tourcoo.carnet.entity.event;

import java.io.Serializable;

/**
 * @author :JenkinsZhou
 * @description :搜索事件
 * @company :途酷科技
 * @date 2019年04月09日15:55
 * @Email: 971613168@qq.com
 */
public class SearchEvent implements Serializable {
    public static final int EVENT_ACTION_SEARCH_GARAGE = 0;

    public static final int EVENT_ACTION_SEARCH_INSURANCE_COMPANY = 1;
    public static final int EVENT_ACTION_SEARCH_ALL = 2;

    public int type = EVENT_ACTION_SEARCH_GARAGE;



    public SearchEvent(String keyWord) {
        this.keyWord = keyWord;
    }

    public SearchEvent(String keyWord, int type) {
        this.type = type;
        this.keyWord = keyWord;
    }


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    private String keyWord;


}

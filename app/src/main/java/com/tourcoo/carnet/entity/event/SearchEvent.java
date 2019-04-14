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
    public SearchEvent(String keyWord) {
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

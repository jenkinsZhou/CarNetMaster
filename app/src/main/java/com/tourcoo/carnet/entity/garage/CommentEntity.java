package com.tourcoo.carnet.entity.garage;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年04月13日15:14
 * @Email: 971613168@qq.com
 */
public class CommentEntity {
    /**
     * total : 0
     * current : 1
     * pages : 0
     * size : 10
     * comment : []
     */

    private String total;
    private String current;
    private String pages;
    private String size;
    private List<CommentInfo> comment;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<CommentInfo> getComment() {
        return comment;
    }

    public void setComment(List<CommentInfo> comment) {
        this.comment = comment;
    }
}

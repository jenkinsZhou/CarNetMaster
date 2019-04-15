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
     * currentPage : 1
     * elements : [{"createTime":"2019-04-12 17:06:42","detail":"陈舟为评价3星无图片","images":"","level":3,"nickName":"陈舟为","ownerIconUrl":"img/2019-04-15-f460402c-5225-42e1-9af3-aa47c33fe641.png","reply":""},{"createTime":"2019-04-12 13:24:33","detail":"莫","images":"img/2019-04-12-cae76e57-b458-49f5-98b7-b01b9c468db0.png","level":2,"nickName":"","ownerIconUrl":"","reply":""},{"createTime":"2019-04-11 19:02:46","detail":"无语","images":"","level":3,"nickName":"","ownerIconUrl":"","reply":""},{"createTime":"2019-04-11 19:01:27","detail":"好","images":"","level":5,"nickName":"","ownerIconUrl":"","reply":""},{"createTime":"2019-04-11 18:53:17","detail":"哦哦哦哦","images":"","level":5,"nickName":"","ownerIconUrl":"","reply":""}]
     * pages : 2
     * totalElements : 10
     */

    private int currentPage;
    private int pages;
    private String totalElements;
    private List<CommentInfo> elements;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public List<CommentInfo> getElements() {
        return elements;
    }

    public void setElements(List<CommentInfo> elements) {
        this.elements = elements;
    }


}

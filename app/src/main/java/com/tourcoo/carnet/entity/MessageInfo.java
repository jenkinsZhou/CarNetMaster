package com.tourcoo.carnet.entity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :消息实体
 * @company :途酷科技
 * @date 2019年04月10日16:06
 * @Email: 971613168@qq.com
 */
public class MessageInfo {
    /**
     * currentPage : 1
     * elements : [{"createTime":"1554879358000","message":"订单：19041014555747创建！","messageId":"221","orderId":"62","readStatus":1},{"createTime":"1554878412000","message":"订单：19041014401156创建！","messageId":"220","orderId":"61","readStatus":1},{"createTime":"1554878099000","message":"订单：19041014345885创建！","messageId":"219","orderId":"60","readStatus":1},{"createTime":"1554876409000","message":"订单：19041014064976创建！","messageId":"218","orderId":"59","readStatus":1},{"createTime":"1554876192000","message":"订单：19041014031264创建！","messageId":"217","orderId":"58","readStatus":1},{"createTime":"1554875195000","message":"订单：19041013463485创建！","messageId":"216","orderId":"57","readStatus":1},{"createTime":"1554874642000","message":"订单：19041013372202创建！","messageId":"215","orderId":"56","readStatus":1},{"createTime":"1554866182000","message":"订单：19040919002882 支付完成！","messageId":"214","orderId":"55","readStatus":1},{"createTime":"1554865530000","message":"订单：19040919000662 支付完成！","messageId":"212","orderId":"52","readStatus":1},{"createTime":"1554865146000","message":"订单：19040918594484 支付完成！","messageId":"210","orderId":"49","readStatus":1}]
     * pages : 11
     * totalElements : 105
     */

    private int currentPage;
    private int pages;
    private String totalElements;
    private List<MessageBean> elements;


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

    public List<MessageBean> getElements() {
        return elements;
    }

    public void setElements(List<MessageBean> elements) {
        this.elements = elements;
    }

    public static class MessageBean {
        /**
         * createTime : 1554879358000
         * message : 订单：19041014555747创建！
         * messageId : 221
         * orderId : 62
         * readStatus : 1
         */

        private String createTime;
        private String message;
        private String messageId;
        private String orderId;
        private int readStatus;

        private int countUnreadMsg;

        public int getCountUnreadMsg() {
            return countUnreadMsg;
        }

        public void setCountUnreadMsg(int countUnreadMsg) {
            this.countUnreadMsg = countUnreadMsg;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }
    }
}

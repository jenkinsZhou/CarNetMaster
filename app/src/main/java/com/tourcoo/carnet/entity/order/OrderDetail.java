package com.tourcoo.carnet.entity.order;

import com.tourcoo.carnet.entity.garage.CommentInfo;

import java.util.List;

/**
 * @author :zhoujian
 * @description :订单详情
 * @company :翼迈科技
 * @date 2019年 04月 15日 22时27分
 * @Email: 971613168@qq.com
 */
public class OrderDetail {


    /**
     * commentList : []
     * order : {"owner_id":"27","garageMobile":"","type":3,"update_time":"2019-04-15 20:01:06","update_user":"27","obd_id":"0","ownerName":"一个板凳","captcha":"62kbw","id":"181","car_id":"59","lat":31.839991,"transaction_id":"","images":"img/2019-04-15-7b78e813-6f69-4819-b982-159fd60f84c9.jpg,img/2019-04-15-1aa7cbd6-e4c7-4186-8eac-2307eca46902.jpg","address":"","lng":117.210469,"create_time":"2019-04-15 20:01:06","garageName":"--","createrName":"--","garage_id":"0","out_trade_no":"19041520010588","ownerMobile":"15655196840","name":"","detail":"宝塔路附近抛锚","position":"117.210469,31.839991","create_user":"27","paytype":0,"status":1}
     */

    private OrderInfo order;
    private List<CommentInfo> commentList;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public List<?> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    public static class OrderInfo {
        /**
         * owner_id : 27
         * garageMobile :
         * type : 3
         * update_time : 2019-04-15 20:01:06
         * update_user : 27
         * obd_id : 0
         * ownerName : 一个板凳
         * captcha : 62kbw
         * id : 181
         * car_id : 59
         * lat : 31.839991
         * transaction_id :
         * images : img/2019-04-15-7b78e813-6f69-4819-b982-159fd60f84c9.jpg,img/2019-04-15-1aa7cbd6-e4c7-4186-8eac-2307eca46902.jpg
         * address :
         * lng : 117.210469
         * create_time : 2019-04-15 20:01:06
         * garageName : --
         * createrName : --
         * garage_id : 0
         * out_trade_no : 19041520010588
         * ownerMobile : 15655196840
         * name :
         * detail : 宝塔路附近抛锚
         * position : 117.210469,31.839991
         * create_user : 27
         * paytype : 0
         * status : 1
         */

        private String owner_id;
        private String garageMobile;
        private int type;
        private String update_time;
        private String update_user;
        private String obd_id;
        private String ownerName;
        private String captcha;
        private String id;
        private String car_id;
        private double lat;
        private String transaction_id;
        private String images;
        private String address;
        private double lng;
        private String create_time;
        private String garageName;
        private String createrName;
        private String garage_id;
        private String out_trade_no;
        private String ownerMobile;
        private String name;
        private String detail;
        private String position;
        private String create_user;
        private int paytype;
        private int status;

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }

        public String getGarageMobile() {
            return garageMobile;
        }

        public void setGarageMobile(String garageMobile) {
            this.garageMobile = garageMobile;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getObd_id() {
            return obd_id;
        }

        public void setObd_id(String obd_id) {
            this.obd_id = obd_id;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getGarageName() {
            return garageName;
        }

        public void setGarageName(String garageName) {
            this.garageName = garageName;
        }

        public String getCreaterName() {
            return createrName;
        }

        public void setCreaterName(String createrName) {
            this.createrName = createrName;
        }

        public String getGarage_id() {
            return garage_id;
        }

        public void setGarage_id(String garage_id) {
            this.garage_id = garage_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getOwnerMobile() {
            return ownerMobile;
        }

        public void setOwnerMobile(String ownerMobile) {
            this.ownerMobile = ownerMobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCreate_user() {
            return create_user;
        }

        public void setCreate_user(String create_user) {
            this.create_user = create_user;
        }

        public int getPaytype() {
            return paytype;
        }

        public void setPaytype(int paytype) {
            this.paytype = paytype;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

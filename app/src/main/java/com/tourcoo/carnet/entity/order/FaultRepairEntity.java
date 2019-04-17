package com.tourcoo.carnet.entity.order;

import com.tourcoo.carnet.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author :zhoujian
 * @description :故障报修实体
 * @company :翼迈科技
 * @date 2019年 04月 05日 22时03分
 * @Email: 971613168@qq.com
 */
public class FaultRepairEntity extends BaseEntity<FaultRepairEntity> implements Serializable {
    /**
     * 待接单
     */
    public static final int TYPE_STATUS_ORDER_WAIT_ORDER = 1;

    /**
     * 服务中
     */
    public static final int TYPE_STATUS_ORDER_IN_SERVICE = 2;
    /**
     * 完成
     */
    public static final int TYPE_STATUS_ORDER_FINISH = 3;

    /**
     * 待支付
     */
    public static final int TYPE_STATUS_ORDER_WAIT_PAY = 4;

    /**
     * 待评价
     */
    public static final int TYPE_STATUS_ORDER_WAIT_EVALUATE = 5;


    public static final int TYPE_STATUS_ORDER_CLOSE = 6;

    public static final int TYPE_STATUS_ORDER_CANCELED = 7;
    /**
     * 订单状态: 1,待接单 2,服务中 3,服务已完成 4,待支付 5,待评价 6,订单关闭 7,订单已取消
     * total : 7
     * current : 1
     * pages : 4
     * size : 2
     * orderList : [{"transaction_id":"","create_time":"2019-04-05 21:21:02","owner_id":"5","garageMobile":"","type":2,"createrName":"--","garage_id":"0","out_trade_no":"19040521210185","ownerMobile":"18256070563","captcha":"v1feq","name":"","id":"13","detail":"测试5","position":"117.192946,31.78477","car_id":"3","paytype":0,"status":1},{"transaction_id":"","create_time":"2019-04-05 21:20:53","owner_id":"5","garageMobile":"","type":2,"createrName":"--","garage_id":"0","out_trade_no":"19040521205282","ownerMobile":"18256070563","captcha":"nkeco","name":"","id":"12","detail":"测试5","position":"117.192946,31.78477","car_id":"3","paytype":0,"status":1}]
     */

    private String total;
    private String current;
    private String pages;
    private String size;
    private List<FaultRepairInfo> orderList;


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

    public List<FaultRepairInfo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<FaultRepairInfo> orderList) {
        this.orderList = orderList;
    }

    public static class FaultRepairInfo implements Serializable {
        /**
         * 上门维修
         */
        public static final int TYPE_CAR_REPAIR = 3;
        /**
         * 上门洗车
         */
        public static final int TYPE_CAR_WASH = 4;
        /**
         * 上门
         */
        public static final int TYPE_CAR_CURING = 5;
        /**
         * 订单状态: 1,待接单 2,服务中 3,服务已完成 4,待支付 5,待评价 6,订单关闭 7,订单已取消
         * transaction_id :
         * create_time : 2019-04-05 21:21:02
         * owner_id : 5
         * garageMobile :
         * type : 2
         * createrName : --
         * garage_id : 0
         * out_trade_no : 19040521210185
         * ownerMobile : 18256070563
         * captcha : v1feq
         * name :
         * id : 13
         * detail : 测试5
         * position : 117.192946,31.78477
         * car_id : 3
         * paytype : 0
         * status : 1
         */

        private String transaction_id;
        private String create_time;
        private int owner_id;
        private String garageName;
        private String garageMobile;
        private int type;
        private String createrName;
        private String garage_id;
        private String out_trade_no;
        private String ownerMobile;
        private String ownerName;
        private String captcha;
        private String name;
        private int id;
        private String detail;
        private String position;
        private String car_id;
        private int paytype;
        private int status;

        private String images = "";

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        /**
         * 订单类型
         */
        private int orderType;

        public String getGarageName() {
            return garageName;
        }

        public void setGarageName(String garageName) {
            this.garageName = garageName;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
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

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
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

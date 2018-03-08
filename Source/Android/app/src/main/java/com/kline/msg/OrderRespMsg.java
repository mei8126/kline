package com.kline.msg;

import com.kline.bean.Charge;

/**
 * Created by mei on 2016/3/28.
 */
public class OrderRespMsg {
    private String orderNum;

    private Charge charge;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}

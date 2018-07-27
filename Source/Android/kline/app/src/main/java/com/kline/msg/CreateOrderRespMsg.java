package com.kline.msg;

/**
 * Created by mei on 2016/3/28.
 */
public class CreateOrderRespMsg  extends BaseRespMsg{
    private OrderRespMsg data;

    public OrderRespMsg getData() {
        return data;
    }

    public void setData(OrderRespMsg data) {
        this.data = data;
    }


}

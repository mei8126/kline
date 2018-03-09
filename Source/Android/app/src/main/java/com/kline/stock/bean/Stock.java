package com.kline.stock.bean;

/**
 * Created by mei on 2018/3/9.
 */

public class Stock {
    // 名称
    protected String name;
    // 代码
    protected String code;
    // 市场， 深证， 沪证
    protected String market;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }


}

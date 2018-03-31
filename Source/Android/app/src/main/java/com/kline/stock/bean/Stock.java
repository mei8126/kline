package com.kline.stock.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by mei on 2018/3/9.
 */
@Table(name = "stock_info")
public class Stock {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    // 名称
    @Column(name = "name")
    protected String name;
    // 代码
    @Column(name = "code")
    protected String code;
    // 市场， 深证， 沪证
    @Column(name = "market")
    protected String market;

    // 简拼
    @Column(name = "simple_spelling")
    private String simpleSpelling;

    // 是否属于自选股
    @Column(name = "self")
    private Boolean self;


    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public Stock() {
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSimpleSpelling() {
        return simpleSpelling;
    }

    public void setSimpleSpelling(String simpleSpelling) {
        this.simpleSpelling = simpleSpelling;
    }

    public Boolean getSelf() {
        return self;
    }

    public void setSelf(Boolean self) {
        this.self = self;
    }
}

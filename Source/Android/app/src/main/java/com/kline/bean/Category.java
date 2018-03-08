package com.kline.bean;

/**
 * Created by mei on 2016/3/11.
 */
public class Category extends BaseBean {

    public Category() {

    }

    public Category(String name) {

    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

}

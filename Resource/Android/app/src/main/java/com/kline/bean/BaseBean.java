package com.kline.bean;

import java.io.Serializable;

/**
 * Created by mei on 2016/3/11.
 */
public class BaseBean implements Serializable {
    protected  long id;
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
}

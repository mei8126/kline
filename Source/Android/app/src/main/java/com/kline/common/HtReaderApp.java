package com.kline.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kline.bean.User;
import com.kline.utils.UserLocalData;

import org.xutils.x;

/**
 * Created by mei on 2016/3/15.
 */
public class HtReaderApp extends Application {

    private User user;
    private static HtReaderApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        x.Ext.init(this);
        x.Ext.setDebug(true);  // 输出日子， 开启会影响性能
        initUser();
        Fresco.initialize(getApplicationContext());
    }

    public static HtReaderApp getInstance() {
        return mInstance;
    }

    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){

        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken(){
        return  UserLocalData.getToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }


}

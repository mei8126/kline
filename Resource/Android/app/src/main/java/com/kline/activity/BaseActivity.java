package com.kline.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.kline.common.HtReaderApp;
import com.kline.bean.User;

/**
 * Created by mei on 2016/3/25.
 */
public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin) {
        if(isNeedLogin) {
            User user = HtReaderApp.getInstance().getUser();
            if(user != null) {
                super.startActivity(intent);
            } else {
                HtReaderApp.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }
}

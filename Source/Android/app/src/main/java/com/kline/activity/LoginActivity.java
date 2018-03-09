package com.kline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;

import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.R;
import com.kline.bean.User;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;
import com.kline.msg.LoginRespMsg;
import com.kline.utils.DESUtil;
import com.kline.utils.ToastUtils;
import com.kline.widget.ClearEditText;
import com.kline.widget.HtToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;


/**
 * Created by mei on 2016/3/23.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private HtToolBar mToolbar;

    @ViewInject(R.id.text_phone)
    private ClearEditText mTextPhone;
    @ViewInject(R.id.text_pwd)
    private ClearEditText mTextPwd;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Event(type = View.OnClickListener.class,value = R.id.btn_login)
    public void login(View view) {
        String phone = mTextPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mTextPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }

        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {


                HtReaderApp application = HtReaderApp.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if (application.getIntent() == null) {
                    setResult(RESULT_OK);
                    finish();
                } else {

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    @Event(type = View.OnClickListener.class, value = R.id.txt_toReg)
    public void gotoReg(View view) {
        Intent regIntent = new Intent(this, RegActivity.class);
        startActivity(regIntent);
    }
}

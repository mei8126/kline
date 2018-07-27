package com.kline.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kline.common.HtReaderApp;
import com.kline.activity.LoginActivity;
import com.kline.R;
import com.kline.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/24.
 */
public class SimpleCallback<T> extends BaseCallback<T> {

    private Context mContext;

    public SimpleCallback(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onRequestBefore(Request request) {

    }

    @Override
    public void onFailure(Request request, IOException e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onSuccess(Response response, T t) {

    }

    @Override
    public void onError(Response response, int code, Exception e) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        Log.i("CartFragment", "onTokenError response.code():" + response.code());
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        HtReaderApp.getInstance().clearUser();
    }
}

package com.kline.http;

import android.app.AlertDialog;
import android.content.Context;

import com.kline.utils.ToastUtils;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/13.
 */
public abstract class SpotsCallback<T> extends SimpleCallback<T> {

    private AlertDialog dialog;

    public SpotsCallback(Context context){
        super(context);
        dialog = new SpotsDialog(context, "拼命加载中......");
    }

    public void setDialogMessage(String message) {
        dialog.setMessage(message);
    }
    private void showDialog(){
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

}

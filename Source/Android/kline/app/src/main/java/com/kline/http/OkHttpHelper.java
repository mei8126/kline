package com.kline.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kline.common.HtReaderApp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/12.
 */
public class OkHttpHelper {

    // 用户登录使用的 token， 请求token的情况
    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期

    //okhttp client 是单例的。
    private static OkHttpClient okHttpClient;
    private Gson mGson;
    private Handler mHandler;

    private OkHttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        okHttpClient = builder.build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());

    }

    ;

    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, BaseCallback callback) {

        get(url, null, callback);
    }

    public void get(String url, Map<String, Object> param, BaseCallback callback) {
        Request request = buildGetRequest(url, param);
        doRequest(request, callback);
    }

    private Request buildGetRequest(String url, Map<String, Object> param) {
        return buildRequest(url, param, HttpMethodType.GET);
    }


    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callback);
    }

    private void doRequest(final Request request, final BaseCallback callback) {
        callback.onRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //callback.onFailure(request, e);
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                callback.onResponse(response);

                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.mType == String.class) {
                        //callback.onSuccess(response, resultStr);
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr, callback.mType);
                            //callback.onSuccess(response, object);
                            callbackSuccess(callback, response, object);
                        } catch (JsonParseException e) {
                            //callback.onError(response, response.code(), null);
                            callbackError(callback, response, response.code(), null);
                        }
                    }
                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING) {

                    callbackTokenError(callback, response);
                } else {
                    //callback.onError(response, response.code(), null);
                    callbackError(callback, response, response.code(), null);
                }

            }
        });
    }

    private Request buildRequest(String url, Map<String, Object> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();

        builder.url(url);
        if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildRequestBody(params);
            builder.post(body);
        }
        return builder.build();
    }

    private String buildUrlParams(String url, Map<String, Object> params) {

        if (params == null)
            params = new HashMap<>(1);

        String token = HtReaderApp.getInstance().getToken();
        if (!TextUtils.isEmpty(token))
            params.put("token", token);


        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }

    private RequestBody buildRequestBody(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            // 添加token
            String token = HtReaderApp.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                builder.add("token", token);
        }
        return builder.build();
    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }


    private void callbackSuccess(final BaseCallback callback, final Response response, final String resultStr) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, resultStr);
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final int errorCode, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, errorCode, e);
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    enum HttpMethodType {
        GET,
        POST
    }

}

package com.kline.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/12.
 */
public  abstract class BaseCallback<T> {
    public Type mType;

    static Type getSuperClassTypeParameter(Class<?> supclass) {
        Type superClass = supclass.getGenericSuperclass();
        if(superClass instanceof Class){
            throw new RuntimeException("Missing type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public BaseCallback(){
        mType = getSuperClassTypeParameter(getClass());
    }
    public abstract void onRequestBefore(Request request);
    // 请求失败，
    public abstract  void onFailure(Request request, IOException e);

    // 请求成功，返回不管是成功还是出错都调用 onResponse
    /**
     *请求成功时调用此方法
     * @param response
     */
    public abstract  void onResponse(Response response);

    // 请求成功后，回复会出现两种情况， 成功和出错
    /**
     *
     * 状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response,T t) ;
    /**
     * 状态码400，404，403，500等时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code,Exception e) ;

    /**
     * Token 验证失败。状态码401,402,403 等时调用此方法
     * @param response
     * @param code

     */
    public abstract void onTokenError(Response response, int code);
}

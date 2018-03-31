package com.kline.utils;

import android.content.Context;
import android.text.TextUtils;

import com.kline.common.Contants;
import com.kline.bean.User;

/**
 * Created by mei on 2016/3/24.
 */
public class UserLocalData {
    public static void putUser(Context context,User user){
        String user_json =  GsonUtils.toJsonStr(user);
        SharedPreferencesUtils.putString(context, Contants.USER_JSON,user_json);
    }

    public static void putToken(Context context,String token){
        SharedPreferencesUtils.putString(context, Contants.TOKEN,token);
    }

    public static User getUser(Context context){
        String user_json= SharedPreferencesUtils.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){
            return  GsonUtils.fromJson(user_json,User.class);
        }
        return  null;
    }

    public static  String getToken(Context context){
        return  SharedPreferencesUtils.getString( context,Contants.TOKEN);
    }


    public static void clearUser(Context context){
        SharedPreferencesUtils.putString(context, Contants.USER_JSON,"");
    }

    public static void clearToken(Context context){
        SharedPreferencesUtils.putString(context, Contants.TOKEN,"");
    }
}

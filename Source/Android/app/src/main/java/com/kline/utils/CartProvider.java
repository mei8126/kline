package com.kline.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.kline.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mei on 2016/3/17.
 */
public class CartProvider {

    public static final String CART_JSON = "cart_json";
    private SparseArray<ShoppingCart> datas = null;

    private Context mContext;

    public CartProvider(Context context) {
        mContext = context;
        datas = new SparseArray<>(10);

        List<ShoppingCart> carts = getDataFromLocal();
        listToSparseArray(carts);
    }

    public void put(ShoppingCart cart) {
        ShoppingCart tmp = datas.get((int) cart.getId());
        if (tmp != null) {
            tmp.setCount(tmp.getCount() + 1);
        } else {
            tmp = cart;
            tmp.setCount(1);
        }
        datas.put((int) cart.getId(), tmp);

        commit();

    }

    public void update(ShoppingCart cart) {
        datas.put((int)cart.getId(),cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete((int) cart.getId());
        commit();
    }

    public List<ShoppingCart> getAll() {
       return getDataFromLocal();
    }

    private void commit() {
        List<ShoppingCart> carts = sparseArrayToList();

        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    private List<ShoppingCart> getDataFromLocal(){
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if(json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return carts;
    }

    private void listToSparseArray(List<ShoppingCart> carts) {

        if(carts != null && carts.size() > 0) {
            for(ShoppingCart cart: carts) {
                datas.put((int) cart.getId(), cart);
            }
        }
    }


    private List<ShoppingCart> sparseArrayToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }
}

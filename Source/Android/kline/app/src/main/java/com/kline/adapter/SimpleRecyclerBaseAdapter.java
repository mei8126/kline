package com.kline.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by mei on 2016/3/16.
 */
public abstract class SimpleRecyclerBaseAdapter<T> extends RecyclerViewBaseAdapter<T, RecyclerViewBaseViewHolder> {

    public SimpleRecyclerBaseAdapter(Context context, int itemLayoutResId) {
        super(context, itemLayoutResId);
        mContext = context;
    }

    public SimpleRecyclerBaseAdapter(Context context, List<T> datas,  int layoutResId) {
        super(context, datas, layoutResId);
    }

}

package com.kline.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mei on 2016/3/16.
 */
public class RecyclerViewBaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected SparseArray<View> views;
    protected RecyclerViewBaseAdapter.OnItemClickListener mOnItemClickListener;

    public RecyclerViewBaseViewHolder(View itemView, RecyclerViewBaseAdapter.OnItemClickListener listener) {
        super(itemView);
        mOnItemClickListener = listener;
        views = new SparseArray<>();
        itemView.setOnClickListener(this);
    }

    public View getView(int id) {
        return findView(id);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public Button getButton(int viewId) {
        return findView(viewId);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    public CheckBox getCheckBox(int viewId) {
        return findView(viewId);
    }


    private <V extends View> V findView(int id) {
        View view = views.get(id);
        if(view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (V)view;
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(view, getLayoutPosition());
        }
    }
}

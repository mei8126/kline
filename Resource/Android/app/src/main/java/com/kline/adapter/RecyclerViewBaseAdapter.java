package com.kline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mei on 2016/3/16.
 */
public abstract class RecyclerViewBaseAdapter<T, Hodler extends RecyclerViewBaseViewHolder> extends
        RecyclerView.Adapter<RecyclerViewBaseViewHolder> {

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected int mItemLayoutResId;
    protected OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public RecyclerViewBaseAdapter(Context context, List<T> datas, int itemLayoutResId) {
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<T>();
        }
        mContext = context;
        mItemLayoutResId = itemLayoutResId;
        mInflater = LayoutInflater.from(mContext);
    }

    public RecyclerViewBaseAdapter(Context context, int layoutResId) {
        this(context, null, layoutResId);
    }

    @Override
    public RecyclerViewBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(mItemLayoutResId, null, false);
        return new RecyclerViewBaseViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewBaseViewHolder holder, int position) {
        T t = getItem(position);
        bindDatas(holder, t);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    public T getItem(int position) {
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    public abstract void bindDatas(RecyclerViewBaseViewHolder holder, T t);


    public List<T> getDatas() {

        return mDatas;
    }

    public void clearData() {
//        int itemCouunt = mDatas.size();
//        mDatas.clear();
//        notifyItemRangeRemoved(0, itemCouunt);
        for (Iterator it = mDatas.iterator(); it.hasNext(); ) {
            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }

    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int positon, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            for(T t:datas) {
                mDatas.add(positon, t);
                notifyItemInserted(positon++);
            }
        }
    }

    public void refreshData(List<T> list){

        if(list !=null && list.size()>0){

            clearData();
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }
        }

    }

}

package com.kline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kline.R;
import com.kline.stock.bean.Stock;

import java.util.List;

/**
 * Created by mei on 2016/3/10.
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHodler> {

    private List<Stock> mDatas;
    private LayoutInflater mInflater;
    private OnStockClickListener mListener;
    private Context mContext;

    public StockListAdapter(List<Stock> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    public void setOnStockClickListener(OnStockClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mInflater = LayoutInflater.from(parent.getContext());

        view = mInflater.inflate(R.layout.stock_list_item, parent, false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        Stock stock = mDatas.get(position);
        holder.tvName.setText(stock.getName());
        holder.tvCode.setText(stock.getCode());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private TextView tvCode;


        public ViewHodler(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvCode = (TextView) itemView.findViewById(R.id.code);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, mDatas.get(getLayoutPosition()));
        }

    }


    public interface OnStockClickListener {
        void onClick(View view, Stock stock);
    }

}

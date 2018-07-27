package com.kline.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kline.R;
import com.kline.bean.Wares;
import com.kline.log.HtLog;

import java.util.List;

/**
 * Created by mei on 2016/3/16.
 */
public class CategotyWaresAdapter extends SimpleRecyclerBaseAdapter<Wares> {
    public CategotyWaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.category_wares_item);
    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView)holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());

    }

}

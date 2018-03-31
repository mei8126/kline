package com.kline.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kline.R;
import com.kline.bean.Favorites;
import com.kline.bean.Wares;

import java.util.List;

/**
 * Created by mei on 2016/3/29.
 */
public class FavoriteAdatper extends SimpleRecyclerBaseAdapter<Favorites> {

    public FavoriteAdatper(Context context, List<Favorites> datas) {
        super(context, datas, R.layout.template_favorite);

    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, Favorites favorites) {
        Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button buttonRemove =holder.getButton(R.id.btn_remove);
        Button buttonLike =holder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}

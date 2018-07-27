package com.kline.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kline.R;
import com.kline.bean.ShoppingCart;
import com.kline.bean.Wares;
import com.kline.log.HtLog;
import com.kline.utils.CartProvider;
import com.kline.utils.ToastUtils;

import java.util.List;

/**
 * Created by mei on 2016/3/15.
 */
public class HotWaresAdapter extends SimpleRecyclerBaseAdapter<Wares>{

    private CartProvider cartProvider;

    public HotWaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.hot_wares_item);

        cartProvider = new CartProvider(context);
    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, final Wares hotWares) {

        SimpleDraweeView draweeView = (SimpleDraweeView)holder.getView(R.id.drawee_view);
        HtLog.i("hotWares url:" + hotWares.getImgUrl());
        HtLog.i("hotWares name:" + hotWares.getName());
        HtLog.i("draweeView:" + draweeView);

        draweeView.setImageURI(Uri.parse(hotWares.getImgUrl()));
        holder.getTextView(R.id.text_title).setText(hotWares.getName());
        holder.getTextView(R.id.text_price).setText("￥" + hotWares.getPrice());

        holder.getButton(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartProvider.put(convertData(hotWares));
                ToastUtils.show(mContext, "已添加到购物车");
            }
        });

    }

    private ShoppingCart convertData(Wares hotWares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(hotWares.getId());
        cart.setName(hotWares.getName());
        cart.setDescription(hotWares.getDescription());
        cart.setPrice(hotWares.getPrice());
        cart.setImgUrl(hotWares.getImgUrl());
        return cart;
    }

    public void  resetLayout(int layoutId){


        this.mItemLayoutResId = layoutId;

        notifyItemRangeChanged(0, getDatas().size());


    }

}

package com.kline.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kline.bean.ShoppingCart;

import java.util.List;

import com.kline.R;
import com.squareup.picasso.Picasso;


/**
 * Created by mei on 2016/3/28.
 */
public class WareOrderAdapter extends SimpleRecyclerBaseAdapter<ShoppingCart> {
    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, datas, R.layout.template_order_wares);
    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, final ShoppingCart shoppingCart) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(shoppingCart.getImgUrl()));

        //ImageView imageview = (ImageView) holder.getView(R.id.drawee_view);
        //Picasso.with(mContext).load(Uri.parse(shoppingCart.getImgUrl())).into(imageview);
    }

    public float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        for (ShoppingCart cart:
                mDatas) {

            sum += cart.getCount()*cart.getPrice();
        }
        return sum;
    }


    private boolean isNull(){
        return (mDatas !=null && mDatas.size()>0);
    }
}

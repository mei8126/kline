package com.kline.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kline.R;
import com.kline.bean.ShoppingCart;
import com.kline.utils.CartProvider;
import com.kline.widget.NumAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mei on 2016/3/17.
 */
public class CartAdapter extends SimpleRecyclerBaseAdapter<ShoppingCart> implements RecyclerViewBaseAdapter.OnItemClickListener {

    private CheckBox checkBox;
    private TextView totalPrice;
    private CartProvider cartProvider;

    public CartAdapter(Context context, List<ShoppingCart> datas, CheckBox checkBox, TextView textView) {
        super(context, datas, R.layout.car_wars_item);
        this.checkBox = checkBox;
        this.totalPrice = textView;
        cartProvider = new CartProvider(context);
        setOnItemClickListener(this);
        setCheckBoxListener();
        checkListener();
        showTotalPrice();
    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, final ShoppingCart shoppingCart) {
        holder.getTextView(R.id.text_title).setText(shoppingCart.getName());
        holder.getTextView(R.id.text_price).setText("￥" + shoppingCart.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(shoppingCart.getImgUrl()));
        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(shoppingCart.isChecked());

        NumAddSubView numAddSubView = (NumAddSubView) holder.getView(R.id.num_control);
        numAddSubView.setValue(shoppingCart.getCount());

        numAddSubView.setOnButtonClickListener(new NumAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                shoppingCart.setCount(value);
                cartProvider.update(shoppingCart);

                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                shoppingCart.setCount(value);
                cartProvider.update(shoppingCart);
                showTotalPrice();
            }
        });
    }

    private boolean isEmpty() {
        if (mDatas == null || mDatas.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private float getTotalPrice() {
        float sum = 0;
        if (!isEmpty()) {
            for (ShoppingCart cart : mDatas) {
                if (cart.isChecked()) {
                    sum += cart.getCount() * cart.getPrice();
                }
            }
        }
        return sum;
    }

    public void showTotalPrice() {
        totalPrice.setText("合计:￥" + String.valueOf(getTotalPrice()));
    }


    @Override
    public void OnItemClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListener();
        showTotalPrice();
    }

    private void checkListener() {
        int count = 0;
        int checkNum = 0;
        if (!isEmpty()) {
            count = mDatas.size();
            for (ShoppingCart cart : mDatas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum++;
                }
            }

            if (count == checkNum) {
                checkBox.setChecked(true);
            }
        }
    }

    public void checkAllOrNone(boolean isCheck) {
        if (isEmpty())
            return;
        int i = 0;
        for (ShoppingCart cart : mDatas) {
            cart.setIsChecked(isCheck);
            notifyItemChanged(i);
            i++;
        }
    }

    private void setCheckBoxListener() {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllOrNone(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    public void deleteSelectedCart() {
        if (isEmpty())
            return;
        // 在list 中使用这样的循环删除方法是不行的, 会报错
//        for (ShoppingCart cart : mDatas) {
//            if(cart.isChecked()) {
//                int position = mDatas.indexOf(cart);
//                cartProvider.delete(cart);
//                mDatas.remove(cart);
//                notifyItemChanged(position);
//            }
//        }
        // 使用迭代的方法来删除才是正确的
        for (Iterator iterator = mDatas.iterator(); iterator.hasNext(); ) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }

    }
}

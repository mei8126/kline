package com.kline.adapter;

import android.content.Context;

import com.kline.R;
import com.kline.bean.Category;

import java.util.List;

/**
 * Created by mei on 2016/3/16.
 */
public class CategoryAdapter extends SimpleRecyclerBaseAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, datas, R.layout.template_single_text);
    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, Category category) {
        holder.getTextView(R.id.textView).setText(category.getName());
    }
}

package com.kline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kline.R;

/**
 * Created by mei on 2018/3/8.
 */

public class TestFragment extends BaseFragment  {

    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=container.getContext();
        View root = inflater.inflate(R.layout.test, container, false);
        //文本textview
        TextView textview=(TextView)root.findViewById(R.id.textview);
        textview.setText("我是TestFragment");
        //为按钮组添加选中状态改变的事件监听
        return root;
    }
    @Override
    protected void lazyLoad() {
        //此方法用于延迟加载
    }

}

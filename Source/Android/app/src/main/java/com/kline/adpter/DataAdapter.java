package com.kline.adpter;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.kline.R;
import com.kline.Utils.AppConstants;
import com.kline.Utils.GetData;
import com.kline.bean.StockInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mei on 2018/3/8.
 */

//数据适配器，通过该适配器设置数据和动态更新界面的数据显示
private class DataAdapter extends BaseAdapter {
    //当前数据缓存
    List<StockInfo> stockInfo = new ArrayList<StockInfo>();
    //上一次的数据缓存
    List<StockInfo> lastStockInfo = new ArrayList<StockInfo>();
    //调用此方法进行界面的数据更新操作
    public void refreshDataAdapter() {
        //判断是否有最新数据
        if (GetData.stockInfos != null) {
            //如果是第一次进入页面，则stockInfo为null
            if (stockInfo != null) {
                //将stockInfo的数据保存到上一次数组缓存中
                lastStockInfo.addAll(stockInfo);
                //清空当前缓存
                stockInfo.clear();
            }
            //将最新数据添加到当前缓存
            stockInfo.addAll(GetData.stockInfos);
            //通知适配器数据更新
            notifyDataSetChanged();
        }
    }
    //根据数据多少返回listview需要显示的数据条数
    public int getCount() {
        if (stockInfo != null && stockInfo.size() > 0) {
            return stockInfo.size();
        }
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //使用ViewHolder优化listview的性能
        ViewHolder holder;
        if (convertView == null) {
            //首次进入引入当行布局文件
            convertView = inflater.inflate(R.layout.itemlistview, null);
            //声明对象
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.root = (LinearLayout) convertView
                    .findViewById(R.id.root);
            //设置夜间模式的单行布局视图
            if (AppConstants.theme.contains("night")) {
                holder.root.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.night_listviewselector));
            }
        } else {
            //直接从从convertView中getTag取出当行布局文件就可以
            holder = (ViewHolder) convertView.getTag();
        }
        setItemValue(position, convertView);
        // 校正（处理同时上下和左右滚动出现错位情况）
        View child = ((ViewGroup) convertView).getChildAt(1);
        int headx = hvListview.getHeadScrollX();
        //判断子布局的x轴位置是否和列名的一致，不一致则纠正为一致
        if (child.getScrollX() != headx) {
            child.scrollTo(hvListview.getHeadScrollX(), 0);
        }
        //获取当前位置该显示的数据
        final StockInfo item = stockInfo.get(position);
        //手势监听器
        FlingListener listener = new FlingListener();
        @SuppressWarnings("deprecation")
        //为当前行设置手势监听
        final GestureDetector detector = new GestureDetector(listener);
        listener.setItem(item);
        //分发手势
        holder.root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        //返回结果视图
        return convertView;
    }
    private void setItemValue(int position, View convertView) {
        StockInfo stock = stockInfo.get(position);
        //根据涨跌设置最新价、涨跌、涨幅数据的颜色
        if (Float.parseFloat(stock.getPriceChangeRatio()) > 0) {
            newPriceText.setTextColor(Color.RED);
            uprangeText.setTextColor(Color.RED);
            updownText.setTextColor(Color.RED);
        } else if (Float.parseFloat(stock.getPriceChangeRatio()) < 0) {
            if (AppConstants.theme.contains("night")) {
                newPriceText.setTextColor(Color.GREEN);
                uprangeText.setTextColor(Color.GREEN);
                updownText.setTextColor(Color.GREEN);
            } else {
                newPriceText.setTextColor(Color.BLUE);
                uprangeText.setTextColor(Color.BLUE);
                updownText.setTextColor(Color.BLUE);
            }
        }
        //如果有上一次缓存数据，那么与前一次比较根据涨跌设置其变化的颜色背景以示动态行情的效果
        if (lastStockInfo != null && lastStockInfo.size() > 0) {
            for (int index = 0; index < lastStockInfo.size(); index++) {
                if (stock.getName().equals(
                        lastStockInfo.get(index).getName())) {
                    float newP = Float.parseFloat(stock.getNew_price());
                    float oldP = Float.parseFloat(lastStockInfo.get(
                            index).getNew_price());
                    //如果最新价涨，则设置背景黄色
                    if (newP > oldP) {
                        newPriceText.setBackgroundColor(Color.YELLOW);
                    } else if (newP < oldP) {
                        //如果是跌，则根据是否为夜间模式调试出合适的颜色
                        if (AppConstants.theme.contains("day")) {
                            newPriceText
                                    .setBackgroundColor(getResources()
                                            .getColor(
                                                    R.color.whitesmoke));
                        } else {
                            newPriceText
                                    .setBackgroundColor(getResources()
                                            .getColor(
                                                    R.color.listview_item_click));
                        }
                    } else {
                        //如果没有变化则背景颜色为透明
                        newPriceText
                                .setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }
        //依次设置数据值
        nameText.setText(stock.getName());
        symbolText.setText(stock.getSymbol());
        newPriceText.setText(stock.getNew_price());
        uprangeText.setText(stock.getPriceChangeRatio());
        updownText.setText(stock.getUpdown());
        volumeText.setText(stock.getVolume());
        amountText.setText(stock.getAmount());
        openIntText.setText(stock.getOpen_int());
        vo2Text.setText(stock.getVo2());
        price2Text.setText(stock.getPrice2());
        price3Text.setText(stock.getPrice3());
        bp1Text.setText(stock.getBp1());
        sp1Text.setText(stock.getSp1());
        closeText.setText(stock.getClosing_price());
        openText.setText(stock.getOpening_price());
        highText.setText(stock.getHigh_price());
        lowText.setText(stock.getLow_price());
    }
}
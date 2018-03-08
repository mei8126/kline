package com.kline.stock.bean;

import com.kline.stock.bean.StockData;

/**
 * Created by mei on 2018/3/5.
 */

public class QQStockData extends StockData {
    // 未知
    private String unknow;
    // 外盘
    private String outerDisk;
    // 内盘
    private String innerDisk;
    // 最近逐笔成交
    private String recentBasis;
    // 涨跌
    private String upAndDowns;
    // 涨跌幅
    private String riseAndFall;
    // 振幅
    private String swing;
    // 流通市值
    private String circulationValue;
    // 换手率
    private String turnOverRate;
    // 市盈率
    private String priceEarningRatio;
    // 总市值
    private String generalValue;
    // 市净率
    private String priceToBookRatio;
    // 涨停价
    private String limitHight;
    // 跌停价
    private String limitLow;
}

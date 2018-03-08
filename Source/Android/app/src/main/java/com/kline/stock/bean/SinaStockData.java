package com.kline.stock.bean;

import com.kline.stock.bean.StockData;

/**
 * Created by mei on 2018/3/5.
 */

public class SinaStockData extends StockData {
    // String url = "http://hq.sinajs.cn/list=sh601006";

    // 日期
    private String date;


    public SinaStockData(String dataStr) {
//        String dataString = "hq_str_sh601006=\"大秦铁路,9.120,9.120,9.120,9.190,9.080,9.120,9.130," +
//                "14730895,134531009.000,39200,9.120,156946,9.110,377500,9.100,478200,9.090," +
//                "249900,"+"9.080,67900,9.130,179700,9.140,86800,9.150,111200,9.160,149800," +
//                "9.170,2018-03-05,13:52:29,00\"";

        // http://qt.gtimg.cn/q=sz000858
        // http://qt.gtimg.cn/q=sh601006


        String tmpStr = dataStr.substring(0, dataStr.lastIndexOf("="));
        code = tmpStr.substring(tmpStr.length()-6, tmpStr.length());
        market = tmpStr.substring(tmpStr.length()-8, tmpStr.length()-6);

        int startIndex = dataStr.indexOf("\"");
        int endIndex = dataStr.lastIndexOf("\"");
        String dataString = dataStr.substring(startIndex+1, endIndex);
        String datas[] = dataString.split(",");
        name = datas[0];
        open = datas[1];
        lastClose = datas[2];
        currentPrice = datas[3];
        hight = datas[4];
        low = datas[5];
        biddingPrice = datas[6];
        competitivePrice = datas[7];
        volume = datas[8];
        turnVolume = datas[9];
        buyVolume1 = datas[10];
        buy1 = datas[11];
        buyVolume2 = datas[12];
        buy2 = datas[13];
        buyVolume3 = datas[14];
        buy3 = datas[15];
        buyVolume4 = datas[16];
        buy4 = datas[17];
        buyVolume5 = datas[18];
        buy5 = datas[19];
        sellVolume1 = datas[20];
        sell1 = datas[21];
        sellVolume2 = datas[22];
        sell2 = datas[23];
        sellVolume3 = datas[24];
        sell3 = datas[25];
        sellVolume4 = datas[26];
        sell4 = datas[27];
        sellVolume5 = datas[28];
        sell5 = datas[29];
        date = datas[30];
        time = datas[31];
    }

    @Override
    public String toString() {
        return "SinaStockData{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", market='" + market + '\'' +
                ", open='" + open + '\'' +
                ", lastClose='" + lastClose + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", hight='" + hight + '\'' +
                ", low='" + low + '\'' +
                ", biddingPrice='" + biddingPrice + '\'' +
                ", competitivePrice='" + competitivePrice + '\'' +
                ", volume='" + volume + '\'' +
                ", turnVolume='" + turnVolume + '\'' +
                ", buyVolume1='" + buyVolume1 + '\'' +
                ", buy1='" + buy1 + '\'' +
                ", buyVolume2='" + buyVolume2 + '\'' +
                ", buy2='" + buy2 + '\'' +
                ", buyVolume3='" + buyVolume3 + '\'' +
                ", buy3='" + buy3 + '\'' +
                ", buyVolume4='" + buyVolume4 + '\'' +
                ", buy4='" + buy4 + '\'' +
                ", buyVolume5='" + buyVolume5 + '\'' +
                ", buy5='" + buy5 + '\'' +
                ", sellVolume1='" + sellVolume1 + '\'' +
                ", sell1='" + sell1 + '\'' +
                ", sellVolume2='" + sellVolume2 + '\'' +
                ", sell2='" + sell2 + '\'' +
                ", sellVolume3='" + sellVolume3 + '\'' +
                ", sell3='" + sell3 + '\'' +
                ", sellVolume4='" + sellVolume4 + '\'' +
                ", sell4='" + sell4 + '\'' +
                ", sellVolume5='" + sellVolume5 + '\'' +
                ", sell5='" + sell5 + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


}

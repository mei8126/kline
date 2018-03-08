package com.kline.Utils;

import android.content.Context;

import com.kline.bean.StockInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mei on 2018/3/8.
 */

public class GetData {
    public static ArrayList<StockInfo> stockInfos;
    private static int pageNumber;
    private static String lastMarket;
    private static String thisMarket;

    public static boolean getRanges(Context context, int sortIndex,
                                    String market, boolean appendFlag, boolean clickSort)
            throws Exception {
        //上一次取数据的市场保存到上一个市场中
        lastMarket = thisMarket;
        //本次取数据的市场设置为当前市场
        thisMarket = market;
        //判断当前是否进行了市场切换,切换了市场则默认取第一页的股票数据
        if (!thisMarket.equals(market)) {
            pageNumber = 1;
        }
        //缓存不为空则清空缓存，等待存放本次取得的数据
        if (stockInfos != null && stockInfos.size() > 0)
            stockInfos.clear();
        //构建url地址
        String path = AppConstants.API2 + "?market="
                + market + "&r_type=2&stime=" + Util.getLastWeekDay();
        //如果有追加标志，这按数量查找，否则只查询第一页50条数据
        if (appendFlag) {
            pageNumber++;
            path = path + "&page=" + pageNumber;
        } else {
            path = path + "&num=" + pageNumber * 50;
        }
        //构建排序方式
        path = getSortPath(path, sortIndex, clickSort);
        //从URL地址中获取返回的Json数组
        JSONArray jsonArray = getJsonArray(path);
        if (jsonArray != null) {
            int index = 0;
            //如果返回结果不为空，则开始遍历解析json
            for (; index < jsonArray.length(); index++) {
                JSONObject item = jsonArray.getJSONObject(index);
                //从json对象中按照键值的方式逐一解析所有的值
                StockInfo stockInfo = new StockInfo(); // 存放到stockInfo里面
                stockInfo.setDate(item.getString("Date")); // 获取对象对应的值
                stockInfo.setSymbol(item.getString("Symbol"));
                stockInfo.setName(item.getString("Name"));
                stockInfo.setPrice3(item.getString("Price3"));
                stockInfo.setVo2(item.getString("Vol2"));
                stockInfo.setOpen_int(item.getString("Open_Int"));
                stockInfo.setPrice2(item.getString("Price2"));
                stockInfo.setClosing_price(item.getString("LastClose"));
                stockInfo.setOpening_price(item.getString("Open"));
                stockInfo.setHigh_price(item.getString("High"));
                stockInfo.setLow_price(item.getString("Low"));
                stockInfo.setNew_price(item.getString("NewPrice"));
                stockInfo.setVolume(item.getString("Volume"));
                stockInfo.setAmount(item.getString("Amount"));
                //涨跌幅数据长度过长的截取掉
                if (item.getString("PriceChangeRatio").length() > 6)
                    stockInfo.setPriceChangeRatio(item.getString(
                            "PriceChangeRatio").substring(0, 6));
                else
                    stockInfo.setPriceChangeRatio(item
                            .getString("PriceChangeRatio"));
                //存入缓存数组
                stockInfos.add(stockInfo);
                if (index == 0) {
                    //默认其他页面展示的代码设置为排名页面第一个代码
                    AppConstants.showSymbol = stockInfo.getSymbol();
                }
            }
            //解析完成，返回真
            return true;
        }
        //结果为空，解析失败，返回假
        return false;
    }

    //从字符串表示的url地址中获取JSONArray
    private static JSONArray getJsonArray(String path) throws Exception {
        String json = null;
        //字符串url转换为URL类对象
        URL url = new URL(path);
        // 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 单位是毫秒，设置超时时间为5秒
        conn.setConnectTimeout(5 * 1000);
        // HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，默认为GET
        conn.setRequestMethod("GET");
        // 判断请求码是否是200码，否则失败
        if (conn.getResponseCode() == 200) {
            // 获取输入流
            InputStream is = conn.getInputStream();
            // 把输入流转换成字符数组
            byte[] data = readStream(is);
            // 把字符数组转换成字符串
            json = new String(data);
            // 数据形式eg：[{"id":1,"name":"小猪","age":22},{"id":2,"name":"小猫","age":23}]
            return new JSONArray(json); // 数据直接为一个数组形式，所以可以直接
            // 用android提供的框架JSONArray读取JSON数据，转换成Array
        }
        AppConstants.message = "请求出错：" + conn.getResponseCode();
        return null;
    }

    //把输入流转换成字节数组
    public static byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();
        return bout.toByteArray();
    }
}

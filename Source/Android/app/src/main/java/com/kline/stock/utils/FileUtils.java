package com.kline.stock.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kline.stock.bean.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mei on 2018/3/9.
 */

public class FileUtils {

    public static List<Stock> readAssetsStockFile(Context mContext, String fileAssetsName) throws IOException {

        List<Stock> stockList = new ArrayList<Stock>();
        String market;
        if(fileAssetsName.contains("sh")) {
            market = "sh";
        } else {
            market = "sz";
        }

        String encoding = "UTF-8";
        InputStream inputStream = mContext.getAssets().open(fileAssetsName);

        InputStreamReader read = new InputStreamReader(inputStream, encoding);//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            Stock stockCode = new Stock();
            lineTxt = lineTxt.trim();
            if(TextUtils.isEmpty(lineTxt))
                continue;
            Log.i("stock", "lineTxt:" + lineTxt + ", fileAssetsName:" + fileAssetsName);

            String name = lineTxt.substring(0, lineTxt.indexOf("("));
            String code = lineTxt.substring(lineTxt.indexOf("(")+1, lineTxt.lastIndexOf(")"));

            stockCode.setName(name);
            stockCode.setCode(code);
            stockCode.setMarket(market);
            stockList.add(stockCode);

        }
        read.close();

        return stockList;
    }

//    public static List<Stock> stockCodeTxtParse(File file) {
//        StringBuilder sb = new StringBuilder();
//        List<Stock> stockList = new ArrayList<Stock>();
//        String market;
//        if(file.getName().contains("sh")) {
//            market = "sh";
//        } else {
//            market = "sz";
//        }
//        try {
//            String encoding = "UTF-8";
//            if (file.isFile() && file.exists()) { //判断文件是否存在
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    Stock stockCode = new Stock();
//                    lineTxt = lineTxt.trim();
//                    String name = lineTxt.substring(0, lineTxt.indexOf("(")-1);
//                    String code = lineTxt.substring(lineTxt.indexOf("(")+1, lineTxt.lastIndexOf(")"));
//
//                    stockCode.setName(name);
//                    stockCode.setCode(code);
//                    stockCode.setMarket(market);
//                    stockList.add(stockCode);
//                }
//                read.close();
//            } else {
//                System.out.println("找不到指定的文件");
//                return stockList;
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        return stockList;
//    }




}

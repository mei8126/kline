package com.kline.stock.utils;

import android.content.Context;
import android.text.TextUtils;

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
        int lineIndex = 0;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            Stock stockCode = new Stock();
            lineTxt = lineTxt.trim();
            lineIndex++;
            if(TextUtils.isEmpty(lineTxt) || lineIndex == 1 ) {
                continue;
            }

            try {
                String name = lineTxt.substring(0, lineTxt.indexOf("("));
                String code = lineTxt.substring(lineTxt.indexOf("(") + 1, lineTxt.lastIndexOf(")"));
                stockCode.setName(name);
                stockCode.setCode(code);
                stockCode.setMarket(market);
                stockCode.setSimpleSpelling(getStockSimpleSpelling(name));
                stockCode.setSelf(true);
                stockList.add(stockCode);

            } catch (Exception e) {
                System.out.println("stock 文件在第 " + lineIndex + " 行编写有误");
                e.printStackTrace();
            }

        }
        read.close();

        return stockList;
    }

    /**
     *
     * @return
     */
    public static String getStockSimpleSpelling(String name) {
        StringBuilder simpleSpelling = new StringBuilder();

        CharacterParser characterParser = CharacterParser.getInstance();
        for (int i = 0; i < name.length(); i++) {
            String pinyin = characterParser.getSelling(name.substring(i,i+1));
            if (!TextUtils.isEmpty(pinyin)) {
                simpleSpelling.append(pinyin.charAt(0));
            } else {

            }
        }
        return simpleSpelling.toString();
    }





}

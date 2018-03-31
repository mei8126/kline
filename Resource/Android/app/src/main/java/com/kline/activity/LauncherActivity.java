package com.kline.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kline.R;
import com.kline.stock.bean.Stock;
import com.kline.stock.db.StockDBUtils;
import com.kline.stock.utils.FileUtils;

import org.xutils.DbManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {

    private static final int GOTO_MAIN_ACTIVITY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        String appFileDir = this.getFilesDir().getAbsolutePath();
        File dbFile = new File(appFileDir + File.separator + "stock.db");
        if (!dbFile.exists()) {
            DbManager dbManager = StockDBUtils.getStockDbManager(this);
            createStockDbFile(dbManager);
        }
        handler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 2000);

        //gotoMainActivity();
    }

    private void createStockDbFile(DbManager dbManager) {
        try {
            List<Stock> shStocks = FileUtils.readAssetsStockFile(this, "sh_code_20180309.txt");
            if (shStocks != null && shStocks.size() > 0) {
                dbManager.save(shStocks);
            }
            List<Stock> szStocks = FileUtils.readAssetsStockFile(this, "sz_code_20180309.txt");
            if (szStocks != null && szStocks.size() > 0) {
                dbManager.save(szStocks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    gotoMainActivity();
                    break;
                default:
                    break;
            }
        }
    };


}

package com.kline.stock.db;

import android.content.Context;
import android.util.Log;

import com.kline.stock.bean.Stock;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by mei on 2018/3/12.
 */

public class StockDBUtils {
    public static final String STOCK_DB_NAME = "stock.db";
    public static final String STOCK_TABLE_NAME = "stock_info";


    public static DbManager getStockDbManager(Context context) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig();
        //设置数据库名，默认xutils.db
        daoConfig.setDbName(STOCK_DB_NAME)
                //设置数据库路径，默认存储在app的私有目录
                .setDbDir(context.getFilesDir())
                //设置数据库的版本号
                .setDbVersion(1)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });
        //设置是否允许事务，默认true
        //.setAllowTransaction(true)

        return x.getDb(daoConfig);
    }

    public static List<Stock> dbFind(DbManager db, String key) throws DbException {

        List<Stock> stocks = db.selector(Stock.class)
                .where("code","like","%" + key + "%")
                .or("name", "like", "%" + key + "%")
                .limit(12) //只查询12记录
                .findAll();

        return stocks;
    }





}

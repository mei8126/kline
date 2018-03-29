package com.kline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kline.activity.StockSearchActivity;
import com.kline.common.Contants;
import com.kline.R;
import com.kline.adapter.StockListAdapter;
import com.kline.bean.HomeCampaign;
import com.kline.http.OkHttpHelper;
import com.kline.http.SimpleCallback;
import com.kline.stock.bean.Stock;
import com.kline.stock.db.StockDBUtils;
import com.kline.stock.utils.FileUtils;
import com.kline.widget.HtToolBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by mei on 2016/3/1.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    private final String TAG = HomeFragment.class.getSimpleName();

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.toolbar)
    private HtToolBar toolBar;
    private StockListAdapter mAdapter;
    private OkHttpHelper  okHttpHelper = OkHttpHelper.getInstance();



    private List<Stock> stocksList = new ArrayList<>();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_home, container, false);
        View view = x.view().inject(this, inflater, container);
        toolBar.setRightButtonIcon(R.mipmap.icon_search);
        toolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StockSearchActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void init() {
        //requestImages();
        //initRecyclerView();
        DbManager db = StockDBUtils.getStockDbManager(getContext());


        try {
            List<Stock> stocks = StockDBUtils.dbFind(db, "60");
            initDatas(stocks);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //initDatas(stocksList);
    }

    private void requestImages() {
//        try {
//            List<Stock> shStocks = FileUtils.readAssetsStockFile(getActivity(), "sh_code_20180309.txt");
//            if(shStocks != null && shStocks.size()>0) {
//                stocksList.addAll(shStocks);
//            }
//            List<Stock> szStocks = FileUtils.readAssetsStockFile(getActivity(), "sz_code_20180309.txt");
//            if(szStocks != null && szStocks.size()>0) {
//                stocksList.addAll(szStocks);
//            }
//
//            StockDBUtils.getStockDbManager(getContext()).save(stocksList);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void initRecyclerView() {


        okHttpHelper.get(Contants.API.CAMPAIGN_HOME, new SimpleCallback<List<HomeCampaign>>(getContext()) {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                if (homeCampaigns != null) {

                    //initDatas(homeCampaigns);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initDatas(List<Stock> stocksList) {

        mAdapter = new StockListAdapter(stocksList, getContext());
        mAdapter.setOnStockClickListener(new StockListAdapter.OnStockClickListener() {

            @Override
            public void onClick(View view, Stock stock) {
                //Intent intent = new Intent(getActivity(), WareListActivity.class);
                //intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());
                //startActivity(intent);
            }
        });
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.stock_divider_line));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }



}

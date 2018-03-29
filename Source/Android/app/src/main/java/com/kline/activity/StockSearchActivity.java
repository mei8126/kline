package com.kline.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextSwitcher;

import com.kline.R;
import com.kline.adapter.StockListAdapter;
import com.kline.stock.bean.Stock;
import com.kline.stock.db.StockDBUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_stock_search)
public class StockSearchActivity extends AppCompatActivity {
    @ViewInject(R.id.etSearch)
    private EditText edSearch;
    @ViewInject(R.id.recyclerview)
    private RecyclerView searchList;

    private List<Stock> stocks = new ArrayList<>();

    private StockListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        edSearch.addTextChangedListener(searchWatcher);
        initSearchList();
    }

    private void initSearchList() {

        mAdapter = new StockListAdapter(stocks, this);
        mAdapter.setOnStockClickListener(new StockListAdapter.OnStockClickListener() {

            @Override
            public void onClick(View view, Stock stock) {
                //Intent intent = new Intent(getActivity(), WareListActivity.class);
                //intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());
                //startActivity(intent);
            }
        });
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.stock_divider_line));
        searchList.setAdapter(mAdapter);
        searchList.addItemDecoration(decoration);
        searchList.setLayoutManager(new LinearLayoutManager(this));
    }

    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            searchStocks(editable.toString());
        }
    };

    private void searchStocks(String key) {
        DbManager db = StockDBUtils.getStockDbManager(this);
        try {
            stocks.clear();
            stocks.addAll(StockDBUtils.dbFind(db, key));
            mAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }
}

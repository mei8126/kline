package com.kline.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.kline.common.Contants;
import com.kline.R;
import com.kline.adapter.HotWaresAdapter;
import com.kline.adapter.decoration.DividerItemDecoration;
import com.kline.bean.Wares;
import com.kline.bean.WaresPage;
import com.kline.utils.Pager;
import com.kline.widget.HtToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by mei on 2016/3/20.
 */
@ContentView(R.layout.activity_ware_list_layout)
public class WareListActivity extends BaseActivity implements Pager.OnPageListener<Wares> {

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.toolbar)
    private HtToolBar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;
    private HotWaresAdapter mWaresAdapter;
    private Pager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ware_list_layout);
        x.view().inject(this);

        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);
        initToolbar();
        initTab();
        getWaresData();

    }

    private void initTab() {
        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTablayout.addTab(tab);
        tab = mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTablayout.addTab(tab);
        tab = mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTablayout.addTab(tab);
        mTablayout.setOnTabSelectedListener(onTabSelectedListener);
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeViewStyle(view);
            }
        });
    }

    private void changeViewStyle(View view) {
        if(mWaresAdapter == null)
            return;
        if ((int)view.getTag() == ACTION_LIST) {
            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            view.setTag(ACTION_GIRD);
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this, 2));
        } else if ((int)view.getTag() == ACTION_GIRD) {
            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            view.setTag(ACTION_LIST);
            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
        }
    }


    private void getWaresData() {
        mPager =Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<WaresPage<Wares>>(){}.getType());
        mPager.request();
    }

    @Override
    public void load(List<Wares> datas, int pageTotal, int totalCount) {
        mTxtSummary.setText("共有" + totalCount + "件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HotWaresAdapter(this, datas);
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }

    }

    @Override
    public void refresh(List<Wares> datas, int pageTotal, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int pageTotal, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            orderBy = (int) tab.getTag();
            mPager.putParam("orderBy",orderBy);
            mPager.request();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

}

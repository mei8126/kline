package com.kline.fragment;

import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.kline.common.Contants;
import com.kline.R;
import com.kline.adapter.HotWaresAdapter;
import com.kline.bean.Wares;
import com.kline.bean.WaresPage;
import com.kline.utils.Pager;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by mei on 2016/3/1.
 */
@ContentView(R.layout.fragment_hot)
public class HotFragment extends BaseFragment {

    private static final String TAG = HotFragment.class.getSimpleName();
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;
    private HotWaresAdapter mAdapter;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_hot, container, false);
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void init() {
        // http://112.124.22.238:8081/course_api/wares/hot?pageSize=20&curPage=1
        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(onPageListener)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(), new TypeToken<WaresPage<Wares>>() {
                }.getType());
        pager.request();
    }

    private Pager.OnPageListener<Wares> onPageListener = new Pager.OnPageListener<Wares>() {
        @Override
        public void load(List<Wares> datas, int pageTotal, int totalCount) {
            mAdapter = new HotWaresAdapter(getContext(), datas);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

        }

        @Override
        public void refresh(List<Wares> datas, int pageTotal, int totalCount) {
            mAdapter.refreshData(datas);
            mRecyclerView.scrollToPosition(0);
        }

        @Override
        public void loadMore(List<Wares> datas, int pageTotal, int totalCount) {
            mAdapter.loadMoreData(datas);
            mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
        }
    };
}

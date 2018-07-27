package com.kline.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.View;

import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.R;
import com.kline.adapter.FavoriteAdatper;
import com.kline.adapter.RecyclerViewBaseAdapter;
import com.kline.adapter.decoration.CardViewtemDecortion;
import com.kline.bean.Favorites;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;
import com.kline.widget.HtToolBar;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

@ContentView(R.layout.activity_my_favorite)
public class MyFavoriteActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private HtToolBar mToolbar;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    private FavoriteAdatper mAdapter;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_favorite);
        x.view().inject(this);
        initToolBar();
        getFavorites();
    }

    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFavorites(){

        Long userId = HtReaderApp.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id",userId);


        okHttpHelper.get(Contants.API.FAVORITE_LIST, params, new SpotsCallback<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showFavorites(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                //LogUtils.d("code:" + code);
            }
        });
    }

    private void showFavorites(List<Favorites> favorites) {
        mAdapter = new FavoriteAdatper(this,favorites);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new CardViewtemDecortion());

        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

            }
        });
    }

}

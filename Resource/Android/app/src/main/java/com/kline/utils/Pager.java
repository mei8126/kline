package com.kline.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import com.kline.bean.WaresPage;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/20.
 */
public class Pager {

    //构建器
    private static Builder builder;

    // 不会发生变化的都东西放在构建器的外边
    private static final int REFRESH_STATE_NORMAL = 0;
    private static final int REFRESH_STATE_REFRSH = 1;
    private static final int REFRESH_STATE_MORE = 2;
    private int mRefreshState = REFRESH_STATE_NORMAL;
    private OkHttpHelper mHttpHelper;

    // 私有化构造器, 不能直接生产一个Pager的实例
    private Pager() {
        mHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        // 创建一个构建器
        builder = new Builder();
        return builder;
    }

    public void request(){

        requestData();
    }

    public void  putParam(String key,Object value){
        builder.params.put(key,value);

    }

    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (builder.pageIndex < builder.pageTotal) {
                    loadMore();
                } else {
                    Toast.makeText(builder.mContext, "已经没有更多数据!", Toast.LENGTH_LONG).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refresh() {

        mRefreshState = REFRESH_STATE_REFRSH;
        builder.pageIndex = 1;
        requestData();
    }

    /**
     * 加载数据
     */
    private void loadMore() {

        mRefreshState = REFRESH_STATE_NORMAL;
        builder.pageIndex++;
        requestData();
    }


    private String buildUrl() {
        return builder.mUrl + "?" + buildUrlParams();
    }

    private String buildUrlParams() {
        HashMap<String, Object> map = builder.params;
        map.put("curPage", builder.pageIndex);
        map.put("pageSize", builder.pageSize);
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            strBuilder.append(entry.getKey() + "=" + entry.getValue());
            strBuilder.append("&");
        }
        String str = strBuilder.toString();
        if (str.endsWith("&")) ;
        {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String url = buildUrl();
        mHttpHelper.get(url, new RequestDataCallback(builder.mContext));
    }

    private <T> void showData(List<T> datas, int pageTotal, int pageSize) {

        if(datas == null || datas.size() <= 0) {
            Toast.makeText(builder.mContext,"加载不到数据",Toast.LENGTH_LONG).show();
            return;
        }
        switch (mRefreshState) {
            case REFRESH_STATE_NORMAL:
                if (builder.onPageListener != null) {
                    builder.onPageListener.load(datas, pageTotal, pageSize);
                }
             break;
            case REFRESH_STATE_REFRSH:
                if (builder.onPageListener != null) {
                    builder.onPageListener.refresh(datas, pageTotal, pageSize);
                }
                builder.mRefreshLayout.finishRefresh();
                break;
            case REFRESH_STATE_MORE:
                if (builder.onPageListener != null) {
                    builder.onPageListener.loadMore(datas, pageTotal, pageSize);
                }
                builder.mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    //  使用泛型, T 表示Pager中要显示的数据的类型
    public interface OnPageListener<T> {
        void load(List<T> datas, int pageTotal, int totalCount);

        void refresh(List<T> datas, int pageTotal, int totalCount);

        void loadMore(List<T> datas, int pageTotal, int totalCount);
    }


    // 会发生变化的东西放到构建器中
    public static class Builder {

        private String mUrl;
        private HashMap<String, Object> params = new HashMap<>(5);
        private MaterialRefreshLayout mRefreshLayout;
        private boolean canLoadMore;
        private int pageTotal = 1;  // 总共有多少页
        private int pageIndex = 1;  // 当前页码
        private int pageSize = 10;  // 一页有多少条数据

        private OnPageListener onPageListener;

        private Context mContext;
        // 解析泛型的Type, 调用者需要传入, 否则GSon 无法正确解析
        private Type mType;


        public Builder setUrl(String url) {
            builder.mUrl = url;
            return builder;
        }

        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setPageSize(int size) {
            pageSize = size;
            return builder;
        }

        public Builder setPageTotal(int total) {
            pageTotal = total;
            return builder;
        }

        public Builder setPageIndex(int index) {
            pageIndex = index;
            return builder;
        }

        public Pager build(Context context, Type type) {
            mContext = context;
            valide();
            this.mType = type;

            return new Pager();
        }

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        /**
         * 判断数据是否合法
         */
        private void valide() {
            if (mContext == null) {
                throw new RuntimeException("Context can't be null");
            }

            if (mUrl == null || mUrl.equals("")) {
                throw new RuntimeException("url can't be null");
            }
            if (mRefreshLayout == null) {
                throw new RuntimeException("MaterialRefreshLayout can't be null");
            }
        }
    }

    class RequestDataCallback<T> extends SpotsCallback<WaresPage<T>> {

        public RequestDataCallback(Context context) {
            super(context);
            super.mType = builder.mType;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            dismissDialog();
            Toast.makeText(builder.mContext,"请求出错："+e.getMessage(),Toast.LENGTH_LONG).show();

            if(REFRESH_STATE_REFRSH== mRefreshState)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(REFRESH_STATE_MORE == mRefreshState){

                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onSuccess(Response response, WaresPage<T> waresPage) {

            builder.pageIndex = waresPage.getCurrentPage();
            builder.pageTotal = waresPage.getTotalPage();
            builder.pageSize = waresPage.getPageSize();
            showData(waresPage.getList(), builder.pageTotal, waresPage.getTotalCount());
        }

        @Override
        public void onError(Response response, int errorCode, Exception e) {
            Toast.makeText(builder.mContext,"加载数据失败",Toast.LENGTH_LONG).show();

            if(REFRESH_STATE_REFRSH== mRefreshState)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(REFRESH_STATE_MORE == mRefreshState){

                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }


    }
}

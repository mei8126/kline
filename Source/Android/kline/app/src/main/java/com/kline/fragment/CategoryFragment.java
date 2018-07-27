package com.kline.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.kline.common.Contants;
import com.kline.R;
import com.kline.adapter.CategoryAdapter;
import com.kline.adapter.CategotyWaresAdapter;
import com.kline.adapter.RecyclerViewBaseAdapter;
import com.kline.adapter.decoration.DividerGridItemDecoration;
import com.kline.adapter.decoration.DividerItemDecoration;
import com.kline.bean.Banner;
import com.kline.bean.Category;
import com.kline.bean.Wares;
import com.kline.bean.WaresPage;
import com.kline.http.OkHttpHelper;
import com.kline.http.SimpleCallback;
import com.kline.http.SpotsCallback;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mei on 2016/3/1.
 */
@ContentView(R.layout.fragment_category)
public class CategoryFragment extends BaseFragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerviewCategory;
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    private CategoryAdapter mCategoryAdapter;

    private int mCurrPage = 1;
    private int mPageSize = 10;
    private int mTotalPage = 1;
    private long mCategoryId = 0;
    private static final int REFRESH_CATEGORY_WARES_STATE_NORMAL = 0;
    private static final int REFRESH_CATEGORY_WARES_STATE_REFRSH = 1;
    private static final int REFRESH_CATEGORY_WARES_STATE_MORE = 2;
    private int mRefreshCategoryWaresState = REFRESH_CATEGORY_WARES_STATE_NORMAL;
    private CategotyWaresAdapter mCategoryWaresAdapter;

    @Nullable
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.fragment_category, container, false);
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void init() {
        requestCategoryData();
        requestBannerData();

        initCategoryWaresRefreshLayout();
    }

    private void requestCategoryData() {
        mOkHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryDatas(categories);

                if (categories != null && categories.size() > 0) {
                    mCategoryId = categories.get(0).getId();
                    requestCategoryWares(mCategoryId);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });
    }

    private void showCategoryDatas(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        mCategoryAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                mCurrPage = 0;
                mCategoryId = category.getId();
                mRefreshCategoryWaresState = REFRESH_CATEGORY_WARES_STATE_NORMAL;
                requestCategoryWares(category.getId());
            }
        });
        mRecyclerviewCategory.setAdapter(mCategoryAdapter);
        mRecyclerviewCategory.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerviewCategory.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewCategory.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }


    private void requestBannerData() {

        String url = Contants.API.BANNER + "?type=1";
        mOkHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                if (banners != null) {
                    showSliderView(banners);
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });
    }

    private void showSliderView(List<Banner> banners) {
        if (banners != null) {
            for (Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);
            }
        }
        // 使用默认的indicator
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }





    private void requestCategoryWares(long categoryId) {


        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId
                + "&curPage=" + mCurrPage + "&pageSize=" + mPageSize;

        mOkHttpHelper.get(url, new SimpleCallback<WaresPage<Wares>>(getContext()) {
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
            public void onSuccess(Response response, WaresPage<Wares> waresPage) {

                mCurrPage = waresPage.getCurrentPage();
                mTotalPage = waresPage.getTotalPage();
                showCategoryWares(waresPage.getList());
            }


            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });
    }

    private void showCategoryWares(List<Wares> categoryWares) {
        switch (mRefreshCategoryWaresState) {
            case REFRESH_CATEGORY_WARES_STATE_NORMAL:
                if(mCategoryWaresAdapter == null) {
                    mCategoryWaresAdapter = new CategotyWaresAdapter(getContext(), categoryWares);
                    mRecyclerviewWares.setAdapter(mCategoryWaresAdapter);
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mCategoryWaresAdapter.clearData();
                    mCategoryWaresAdapter.addData(categoryWares);
                }
                break;
            case REFRESH_CATEGORY_WARES_STATE_REFRSH:
                mCategoryWaresAdapter.clearData();
                mCategoryWaresAdapter.addData(categoryWares);
                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLayout.finishRefresh();

                break;
            case REFRESH_CATEGORY_WARES_STATE_MORE:
                int itemCount = mCategoryWaresAdapter.getItemCount();

                mCategoryWaresAdapter.addData(itemCount, categoryWares);
                mRecyclerviewWares.scrollToPosition(itemCount);
                mRefreshLayout.finishRefreshLoadMore();
                break;
            default:
                break;
        }
    }

    private void initCategoryWaresRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //Log.i(TAG, "onRefresh currPage:");
                refreshCategoryWaresData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                //Log.i(TAG, "onRefreshLoadMore currPage:" + currPage + ", totalPage:" + totalPage);
                if (mCurrPage <= mTotalPage) {
                    loadMoreCategoryWaresData();
                } else {
                    Toast.makeText(getContext(), "已经没有更多数据!", Toast.LENGTH_LONG).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshCategoryWaresData() {
        mCurrPage = 1;
        mRefreshCategoryWaresState = REFRESH_CATEGORY_WARES_STATE_REFRSH;
        requestCategoryWares(mCategoryId);
    }

    private void loadMoreCategoryWaresData() {
        mCurrPage++;
        mRefreshCategoryWaresState = REFRESH_CATEGORY_WARES_STATE_MORE;
        requestCategoryWares(mCategoryId);
    }
}

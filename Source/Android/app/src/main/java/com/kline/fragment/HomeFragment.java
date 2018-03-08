package com.kline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.kline.common.Contants;
import com.kline.R;
import com.kline.activity.WareListActivity;
import com.kline.adapter.decoration.DividerItemDecoration;
import com.kline.adapter.HomeCatgoryAdapter;
import com.kline.bean.Banner;
import com.kline.bean.Campaign;
import com.kline.bean.HomeCampaign;
import com.kline.http.OkHttpHelper;
import com.kline.http.SimpleCallback;
import com.kline.http.SpotsCallback;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by mei on 2016/3/1.
 */
public class HomeFragment extends BaseFragment {

    private final String TAG = HomeFragment.class.getSimpleName();
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdapter;
    private Gson mGson = new Gson();
    private List<Banner> mBanners;
    private OkHttpHelper  okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void init() {
        requestImages();
        initRecyclerView();
    }

    private void requestImages() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
        okHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                if (banners != null) {
                    mBanners = banners;
                    initSlider();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i(TAG, "onError:");
            }
        });

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

                    initDatas(homeCampaigns);
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {

            }
        });


    }

    private void initSlider() {
        if(mBanners != null) {
            for(Banner banner:mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }


        // 使用默认的indicator
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        // 自定义指示器
        //mSliderLayout.setCustomIndicator(mPagerIndicator);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }

    private void initDatas(List<HomeCampaign> homeCampaigns) {

        mAdapter = new HomeCatgoryAdapter(homeCampaigns, getContext());
        mAdapter.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampainClickListener() {

            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }



}

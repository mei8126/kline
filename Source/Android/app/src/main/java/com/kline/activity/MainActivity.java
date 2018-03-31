package com.kline.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.kline.R;
import com.kline.bean.BottomTab;
import com.kline.fragment.CartFragment;
import com.kline.fragment.SelfStockFragment;
import com.kline.fragment.HotFragment;
import com.kline.fragment.MineFragment;
import com.kline.fragment.CategoryFragment;
import com.kline.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;

    private List<BottomTab> mTabs = new ArrayList<BottomTab>(5);

    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomTab();

    }


    private void initBottomTab() {
        BottomTab homeTab = new BottomTab(R.string.home, R.drawable.tab_selector_home, SelfStockFragment.class);
        BottomTab categoryTab = new BottomTab(R.string.hot, R.drawable.tab_selector_hot, HotFragment.class);
        BottomTab searchTab = new BottomTab(R.string.category, R.drawable.tab_selector_discover, CategoryFragment.class);
        BottomTab carTab = new BottomTab(R.string.cart, R.drawable.tab_selector_car, CartFragment.class);
        BottomTab minerTab = new BottomTab(R.string.mine, R.drawable.tab_selector_miner, MineFragment.class);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
        mTabs.add(homeTab);
        mTabs.add(categoryTab);
        mTabs.add(searchTab);
        mTabs.add(carTab);
        mTabs.add(minerTab);
        for (BottomTab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitleId()));
            View view = buildIndicatorView(tab);
            tabSpec.setIndicator(view);
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
        //显示分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        // 不显示分割线
        //mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)) {
                    if(cartFragment == null) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
                        if (fragment != null) {
                            cartFragment = (CartFragment) fragment;
                            cartFragment.refreshData();
                        }
                    } else {
                         cartFragment.refreshData();
                    }
                }

            }
        });
    }

    private View buildIndicatorView(BottomTab tab) {
        mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.txt_tab);
        imageView.setBackgroundResource(tab.getIconId());
        textView.setText(tab.getTitleId());
        return view;
    }


}

package com.kline.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.kline.fragment.TestFragment;

/**
 * Created by mei on 2018/3/8.
 */

public class TestPagerAdapter extends FragmentPagerAdapter {

    public TestPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            // 这里演示使用了TestFragment和Test2Fragment界面，实际实现的时候，各个界面应该都是不同的。
            case 0:
                fragment = new TestFragment();
                break;
            case 1:
                fragment = new TestFragment();
                break;
            case 2:
                fragment = new TestFragment();
                break;
            case 3:
                fragment = new TestFragment();
                break;
        }
        return fragment;
    }
}

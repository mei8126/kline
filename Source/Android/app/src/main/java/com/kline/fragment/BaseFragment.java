package com.kline.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by mei on 2018/3/8.
 */

public abstract class BaseFragment extends Fragment {
    // Fragment当前状态是否可见
    protected boolean isVisible ,isPrepared;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 判断对用户是否可见
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    // 可见
    protected void onVisible() {
        lazyLoad();
    }
    // 不可见
    protected void onInvisible() {
    }
    // 延迟加载子类必须重写此方法
    protected abstract void lazyLoad();
}

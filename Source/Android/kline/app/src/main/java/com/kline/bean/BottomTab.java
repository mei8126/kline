package com.kline.bean;

/**
 * Created by mei on 2016/3/1.
 */
public class BottomTab {
    private int titleId;
    private int iconId;
    private Class fragment;

    public BottomTab(int titleId, int iconId, Class fragment) {
        this.titleId = titleId;
        this.iconId = iconId;
        this.fragment = fragment;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}

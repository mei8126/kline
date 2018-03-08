package com.kline.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kline.R;

/**
 * Created by mei on 2016/3/2.
 */
public class HtToolBar extends Toolbar {

    private LayoutInflater mInflater;
    private View mView;
    private EditText mSearchView;
    private TextView mTitleView;
    private Button mRightButton;

    public HtToolBar(Context context) {
        this(context, null);
    }

    public HtToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);
        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.HtToolBar, defStyleAttr, 0);

            final Drawable rightButtonIcon = a.getDrawable(R.styleable.HtToolBar_rightButtonIcon);
            if (rightButtonIcon != null) {
                setRightButtonIcon(rightButtonIcon);
            }
            CharSequence rightButtonText = a.getText(R.styleable.HtToolBar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }

            final boolean isShowSearchView = a.getBoolean(R.styleable.HtToolBar_isShowSearchView, false);
            if (isShowSearchView == true) {
                setSearchViewVisibility(VISIBLE);
                setTitleViewVisibility(GONE);
            } else {
                setSearchViewVisibility(GONE);
                setTitleViewVisibility(VISIBLE);
            }
            a.recycle();
        }
    }


    private void initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.ht_toolbar, null);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mTitleView = (TextView) mView.findViewById(R.id.toolbar_title);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, layoutParams);
        }
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTitleView != null) {
            mTitleView.setText(title);
            setTitleViewVisibility(VISIBLE);
            setSearchViewVisibility(GONE);
        }
    }

    public void setSearchViewVisibility(int visibility) {
        if (mSearchView != null) {
            mSearchView.setVisibility(visibility);
        }
    }

    public void setTitleViewVisibility(int visibility) {
        if (mTitleView != null) {
            mTitleView.setVisibility(visibility);
        }
    }

    public void setRightButtonVisibility(int visibility) {
        if (mRightButton != null) {
            mRightButton.setVisibility(visibility);
        }
    }

    public void setRightButtonIcon(int resId) {
        setRightButtonIcon(getResources().getDrawable(resId));
    }
    public void setRightButtonIcon(Drawable icon) {
        if (mRightButton != null) {
            mRightButton.setBackground(icon);
        }
        setRightButtonVisibility(VISIBLE);
    }

    public void setRightButtonText(CharSequence text) {
        mRightButton.setText(text);
        setRightButtonVisibility(VISIBLE);
    }

    public void setRightButtonText(int id) {

        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton(){

        return mRightButton;
    }

    public void setRightButtonOnClickListener(OnClickListener listener) {
        mRightButton.setOnClickListener(listener);
    }

}


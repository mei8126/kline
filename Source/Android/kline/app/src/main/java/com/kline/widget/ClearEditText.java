package com.kline.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kline.R;

/**
 * Created by mei on 2016/3/23.
 */
public class ClearEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearTextIcon;
    private OnTouchListener mOnTouchListener;
    private OnFocusChangeListener mOnFocusChangeListener;


    public ClearEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_delete_32);
        final Drawable wrappenDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappenDrawable, getCurrentHintTextColor());
        mClearTextIcon = wrappenDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicWidth(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);

    }

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
       if(hasFocus) {
           setClearIconVisible(getText().length()>0);
       } else {
           setClearIconVisible(false);
       }
        if(mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if(isFocused()) {
            setClearIconVisible(text.length()>0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

package com.kline.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kline.R;

/**
 * Created by mei on 2016/3/17.
 */
public class NumAddSubView extends LinearLayout implements View.OnClickListener {

    public static final int DEFUALT_MAX=1000;

    private TextView mTextNum;
    private Button mBtnAdd;
    private Button mBtnSub;
    private int value = 1;
    private int maxValue = DEFUALT_MAX;
    private int minValue = 1;


    private LayoutInflater mInflater;

    public OnButtonClickListener onButtonClickListener;


    public interface OnButtonClickListener {
        public void onButtonAddClick(View view, int value);

        public void onButtonSubClick(View view, int value);
    }


    public NumAddSubView(Context context) {
        this(context, null);
    }

    public NumAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();
        if(attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumAddSubView, defStyleAttr, 0);

            int val = a.getInt(R.styleable.NumAddSubView_value, 0);
            setValue(val);
            int maxVal = a.getInt(R.styleable.NumAddSubView_maxValue, 0);
            if(maxVal != 0)
                setMaxValue(maxVal);
            int minVal = a.getInt(R.styleable.NumAddSubView_minValue, 0);
            setMinValue(minVal);

            Drawable textBg = a.getDrawable(R.styleable.NumAddSubView_editBackgroud);
            if(textBg != null) {
                setEditTextBackgroud(textBg);
            }

            Drawable buttonAddBackground = a.getDrawable(R.styleable.NumAddSubView_buttonAddBackgroud);
            if(buttonAddBackground!=null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = a.getDrawable(R.styleable.NumAddSubView_buttonSubBackgroud);
            if(buttonSubBackground!=null)
                setButtonSubBackgroud(buttonSubBackground);


            a.recycle();
        }
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.widget_num_add_sub, this,true);
        mTextNum = (TextView)view.findViewById(R.id.text_num);
        mBtnAdd = (Button)view.findViewById(R.id.btn_add);
        mBtnSub = (Button)view.findViewById(R.id.btn_sub);
        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                valueAdd();
                if(onButtonClickListener != null) {
                    onButtonClickListener.onButtonAddClick(view, value);
                }
                break;
            case R.id.btn_sub:
                valueSub();
                if(onButtonClickListener != null) {
                    onButtonClickListener.onButtonSubClick(view, value);
                }
                break;
            default:
                break;
        }
    }

    private void valueAdd() {
        getValue();
        if(value < maxValue) {
            value++;
        }

        mTextNum.setText(String.valueOf(value));
    }

    private void valueSub() {
        getValue();
        if(value > minValue) {
            value--;
        }
        mTextNum.setText(String.valueOf(value));
    }

    public void setValue(int value) {
        mTextNum.setText(String.valueOf(value));
        this.value = value;
    }

    public int getValue() {
        String textValue = mTextNum.getText().toString();
        if(textValue!= null && !textValue.equals("")) {
            this.value = Integer.parseInt(textValue);
        }

        return this.value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setEditTextBackgroud(Drawable drawable) {
        mTextNum.setBackgroundDrawable(drawable);
    }

    public void setTextNumBackgroud(int drawableResId) {
        setEditTextBackgroud(getResources().getDrawable(drawableResId));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }



}
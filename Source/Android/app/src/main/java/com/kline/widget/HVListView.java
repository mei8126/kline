package com.kline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 自定义支持横向滚动的ListView
 * Created by mei on 2018/3/8.
 */
public class HVListView extends ListView {
    //手势
    private GestureDetector mGesture;
    //列头
    public LinearLayout mListHead;
    //偏移坐标
    private int mOffset = 0;
    //屏幕宽度
    private int screenWidth;

    //构造函数
    public HVListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGesture = new GestureDetector(context, mOnGesture);
    }
    //分发触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGesture.onTouchEvent(ev);
    }

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //值是false这样才能把事件传给View里的onTouchEvent，否则事件传递在此处终止。
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    //手势
    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            //为真，事件才能够继续传递
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            //手势滑动事件，且手一直没有离开触发
            return true;
        }

        //滚动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //手势滚动事件，滑动之后手势离开屏幕才触发
            synchronized (HVListView.this) {
                int moveX = (int) distanceX;
                int curX = mListHead.getScrollX();
                int scrollWidth = getWidth();
                int dx = moveX;
                int moveY = (int) distanceY;
                //控制越界问题
                if (curX + moveX < 0)
                    dx = 0;
                if (curX + moveX + getScreenWidth() > scrollWidth)
                    dx = scrollWidth - getScreenWidth() - curX;

                if (Math.abs(moveY) >= Math.abs(dx)) {
                    dx = 0;
                }

                mOffset += dx;
                //根据手势滚动Item视图
                for (int i = 0, j = getChildCount(); i < j; i++) {
                    View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
                    if (child.getScrollX() != mOffset)
                        child.scrollTo(mOffset, 0);
                }
                mListHead.scrollBy(dx, 0);
            }
            //请求组件内部重新布局
            requestLayout();
            return true;
        }
    };

    //获取屏幕可见范围内最大屏幕
    public int getScreenWidth() {
        if (screenWidth == 0) {
            screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            if (getChildAt(0) != null) {
                screenWidth -= ((ViewGroup) getChildAt(0)).getChildAt(0)
                        .getMeasuredWidth();
            } else if (mListHead != null) {
                //减去固定第一列
                screenWidth -= mListHead.getChildAt(0).getMeasuredWidth();
            }
        }
        return screenWidth;
    }

    //获取列头偏移量
    public int getHeadScrollX() {
        return mListHead.getScrollX();
    }
}

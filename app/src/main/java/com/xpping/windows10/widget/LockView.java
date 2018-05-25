package com.xpping.windows10.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.xpping.windows10.utils.DensityUtils;

/*
*锁屏view
*/
public class LockView extends RelativeLayout {
    private Scroller mScroller;
    private GestureDetector mGestureDetector;

    private int windowsHeight;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new CustomGestureListener());
        windowsHeight = DensityUtils.getScreenH(getContext()) / 20;
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    //在绘制View时，会在draw()过程调用该方法。
    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            //Log.i("Y位置",mScroller.getCurrY()+"");
            if(mScroller.getCurrY()==DensityUtils.getScreenH(getContext()))
                ((Activity)getContext()).finish();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getScrollY() > windowsHeight) {
                    smoothScrollTo(0, DensityUtils.getScreenH(getContext()));

                    return true;
                }
                smoothScrollTo(0, 0);
                break;
            default:
                int distance = getScrollY();
                System.out.println("distance" + distance);
                if (distance >= 0) {
                    return mGestureDetector.onTouchEvent(event);
                }
        }
        return super.onTouchEvent(event);
    }

    class CustomGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            int dis = (int) ((distanceY - 0.5));
            smoothScrollBy(0, dis);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            e1.getY();
            e2.getY();
            System.out.println(e1.getY() + "------" + e2.getY());
            return false;
        }

    }

}

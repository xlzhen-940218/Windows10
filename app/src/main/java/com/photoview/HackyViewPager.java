package com.photoview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
    private VelocityTracker mVelocityTracker;
    private FastTouchEventListener listener;
    private long time;
    public HackyViewPager(Context context) {
        super(context);
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
    }

    public void setListener(FastTouchEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE://移动
                if(ev.getPointerCount()==1) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(25);
                    if(listener!=null&&System.currentTimeMillis()>time+1000){
                        if(mVelocityTracker.getXVelocity()>150)
                            listener.theLeft(getCurrentItem());
                        else if(mVelocityTracker.getXVelocity()<-150)
                            listener.theRight(getCurrentItem());
                        else if(mVelocityTracker.getYVelocity()<-150)
                            listener.theTop(getCurrentItem());
                        else if(mVelocityTracker.getYVelocity()>150)
                            listener.theBottom(getCurrentItem());

                        time=System.currentTimeMillis();
                    }
                }
                break;
        }
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
    }

    public interface FastTouchEventListener{
        void theLeft(int currentItem);
        void theRight(int currentItem);
        void theTop(int currentItem);
        void theBottom(int currentItem);
    }
}

package com.xpping.windows10.touchlistener;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.xpping.windows10.utils.AppUtis;

/**
 * Created by xlzhen on 9/12 0012.
 * Fragment Touch
 */

public class FragmentTouchListener implements View.OnTouchListener {
    private int _leftDelta;
    private int _topDelta;
    private int _rightDelta;
    private int _bottomDelta;
    private boolean fragmentIsTouch;
    private int resId;
    private Activity activity;
    public FragmentTouchListener(Activity activity,int resId) {
        this.resId=resId;
        this.activity=activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!fragmentIsTouch)
            return true;
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                AppUtis.changeFragmentFocus(activity,resId);//更改fragment焦点
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                _leftDelta = X - lParams.leftMargin;
                _topDelta = Y - lParams.topMargin;
                _rightDelta = X + lParams.rightMargin;
                _bottomDelta = Y + lParams.bottomMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _leftDelta;
                layoutParams.topMargin = Y - _topDelta;
                layoutParams.rightMargin = _rightDelta - X;
                layoutParams.bottomMargin = _bottomDelta - Y;
                view.setLayoutParams(layoutParams);
                break;
        }
        view.invalidate();
        return true;
    }

    public void setFragmentIsTouch(boolean fragmentIsTouch){
        this.fragmentIsTouch=fragmentIsTouch;
    }

}

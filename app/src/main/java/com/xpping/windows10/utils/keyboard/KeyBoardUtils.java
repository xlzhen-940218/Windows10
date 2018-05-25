package com.xpping.windows10.utils.keyboard;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 */
public class KeyBoardUtils {
    /**
     * 打开软键盘
     *
     * @param mEditText 输入框(好像关系不大)
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框(好像关系不大)
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }


    public static void closeKeyBoard(FragmentActivity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager im = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (im != null) {
            View v = activity.getWindow().getCurrentFocus();    //获取获得焦点的view，  关闭该view的 键盘
            if (v != null) {
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void observeSoftKeyboard(Activity activity, final int editHeight, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                boolean hide = (double) displayHeight / height > 0.8;
                /*if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "DecorView display hight = " + displayHeight);
                    Log.d(TAG, "DecorView hight = " + height);
                    Log.d(TAG, "softkeyboard visible = " + !hide);
                }*/

                listener.onSoftKeyBoardChange((height - displayHeight) - editHeight, !hide);

            }
        });
    }

    /**
     * Note: if you change layout in this method, maybe this method will repeat because the ViewTreeObserver.OnGlobalLayoutListener will repeat So maybe you need do some handle(ex: add some flag to avoid repeat) in this callback
     * <p/>
     * Example:
     * <p/>
     * private int previousHeight = -1; private void setupKeyboardLayoutWhenEdit() { SoftKeyboardUtil.observeSoftKeyBoard(this, new OnSoftKeyboardChangeListener() {
     *
     * @Override public void onSoftKeyBoardChange(int softkeybardHeight, boolean visible) { if (previousHeight == softkeybardHeight) { return; } previousHeight = softkeybardHeight; //code for change layout } }); }
     */
    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }
}

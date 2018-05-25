package com.xpping.windows10.listener;

import android.content.Context;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by xiong on 2017/12/9.
 * 监听 防止打开链接时不存在可以打开的应用崩溃
 */

public class MovementCheck extends LinkMovementMethod {
    private Context context;

    public MovementCheck(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event ) {
        try {
            return super.onTouchEvent( widget, buffer, event ) ;
        } catch( Exception ex ) {
            Toast.makeText(context, "没有可以打开此链接的应用哦！", Toast.LENGTH_SHORT ).show();
            return true;
        }
    }

}
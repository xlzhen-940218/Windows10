package com.xpping.windows10.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xpping.windows10.R;
import com.xpping.windows10.utils.DensityUtils;

/**
 * Toast显示
 * Created by YJHUI on 2016/9/23.
 */

public class ToastView {
    private View layout;
    private Toast toast;

    public static ToastView getInstaller(Context context) {
        return new ToastView(context);
    }

    public ToastView setText(CharSequence charSequence){
        ((TextView) layout.findViewById(R.id.tvToastContent)).setText(charSequence);
        return this;
    }

    public ToastView setText(int textId){
        ((TextView) layout.findViewById(R.id.tvToastContent)).setText(textId);
        return this;
    }

    public void show(){
        try {
            toast.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @SuppressLint("InflateParams")
    private ToastView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.view_toast_layout, null);
        toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL,0, DensityUtils.dp2px(200));
    }

    public ToastView setGravity(int gravity,int marginHor, int marginVer){
        toast.setGravity(gravity, marginHor, marginVer);
        return this;
    }

    public ToastView setDuration(int duration){
        toast.setDuration(duration);
        return this;
    }
}

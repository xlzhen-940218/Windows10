package com.xpping.windows10.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;

import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/**
 * Created by xlzhen on 9/11 0011.
 * metroView
 */

public class MetroView extends RelativeLayout {
    public MetroView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.metro_view_layout, this);

        @SuppressLint("Recycle") TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.MetroView);
        final String text = typedArray.getString(R.styleable.MetroView_metroText);
        final String packageName = typedArray.getString(R.styleable.MetroView_metroPackageName);
        final String action = typedArray.getString(R.styleable.MetroView_metroAction);
        final String uri = typedArray.getString(R.styleable.MetroView_metroUri);
        final String category = typedArray.getString(R.styleable.MetroView_metroCategory);
        int res = typedArray.getResourceId(R.styleable.MetroView_metroImage, R.mipmap.email);
        int color = typedArray.getColor(R.styleable.MetroView_metroBackground, ContextCompat.getColor(context, R.color.color_56));
        int textColor = typedArray.getColor(R.styleable.MetroView_metroTextColor, ContextCompat.getColor(context, android.R.color.white));
        int size = typedArray.getInteger(R.styleable.MetroView_metroSize, 70);
        final boolean packageArray = typedArray.getBoolean(R.styleable.MetroView_metroPackageArray, false);
        findViewById(R.id.relativeLayout).getLayoutParams().width = DensityUtils.dp2px(size);
        ((TextView) findViewById(R.id.textView)).setText(text != null ? text : "邮件");
        ((TextView) findViewById(R.id.textView)).setTextColor(textColor);
        BaseApplication.imageLoader.displayImage("drawable://"+res,(ImageView) findViewById(R.id.imageView)
            ,new ImageSize(DensityUtils.dp2px(30),DensityUtils.dp2px(30)));

        findViewById(R.id.imageView).setBackgroundColor(color);
        findViewById(R.id.metroView).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent closeMenuIntent = new Intent(MAIN_BROADCAST);
                closeMenuIntent.putExtra("message", "closeStartMenuDialog");
                context.sendBroadcast(closeMenuIntent);

                try {
                    if (packageName != null) {
                        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                        context.startActivity(intent);
                    } else if (action != null) {
                        Intent intent = new Intent(action);
                        context.startActivity(intent);
                    } else if (uri != null) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(uri));
                        context.startActivity(intent);
                    } else if (category != null) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.MAIN");
                        intent.addCategory(category);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(MAIN_BROADCAST);
                        intent.putExtra("message", text);
                        context.sendBroadcast(intent);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    Intent intent = new Intent(MAIN_BROADCAST);
                    intent.putExtra("message", text);
                    context.sendBroadcast(intent);
                }
            }

        });
    }
}

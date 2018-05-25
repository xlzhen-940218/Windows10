package com.xpping.windows10.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.SavePreference;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
*锁屏页面
*/
public class LockActivity extends Activity {
    private TextView hour_minute,month_day_week;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtis.setFullScreen(this);
        setContentView(R.layout.actiivty_lock);

        if (TextUtils.isEmpty(SavePreference.getStr(this, "lock_pro_path"))) {
            BaseApplication.imageLoader.displayImage("drawable://" +R.mipmap.lock_port_bg
                    , (ImageView) findViewById(R.id.wallpaperBackground)
                    , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));
        } else {
            BaseApplication.imageLoader.displayImage("file://" + SavePreference.getStr(this, "lock_pro_path")
                    , (ImageView) findViewById(R.id.wallpaperBackground)
                    , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));

        }

        hour_minute=findViewById(R.id.hour_minute);
        month_day_week=findViewById(R.id.month_day_week);

        hour_minute.setText(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));
        month_day_week.setText(new SimpleDateFormat("MM月dd日，EEEE").format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            //如果有设置过壁纸，则设置用户设置过的壁纸
            if (TextUtils.isEmpty(SavePreference.getStr(this, "lock_lan_path"))) {
                BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.lock_landscape
                        ,(ImageView) findViewById(R.id.wallpaperBackground)
                        ,new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));

            } else {
                BaseApplication.imageLoader.displayImage("file://" + SavePreference.getStr(this, "lock_lan_path")
                        , (ImageView) findViewById(R.id.wallpaperBackground)
                        , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));

            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (TextUtils.isEmpty(SavePreference.getStr(this, "lock_pro_path"))) {
                BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.lock_port_bg
                        ,(ImageView) findViewById(R.id.wallpaperBackground)
                        ,new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));
            } else {
                BaseApplication.imageLoader.displayImage("file://" + SavePreference.getStr(this, "lock_pro_path")
                        , (ImageView) findViewById(R.id.wallpaperBackground)
                        , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));

            }
        }
    }
}

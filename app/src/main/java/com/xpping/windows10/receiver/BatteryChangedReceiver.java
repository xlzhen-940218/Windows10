package com.xpping.windows10.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.DensityUtils;

/**
 * Created by xlzhen on 9/12 0012.
 * 电池消息
 */

public class BatteryChangedReceiver extends BroadcastReceiver {
    private ImageView battery;

    public BatteryChangedReceiver(ImageView battery) {
        this.battery = battery;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                //正在充电
                if (level >= 0 && level <= 10) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_0
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 10 && level <= 20) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_10
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 20 && level <= 30) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_20
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 30 && level <= 40) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_30
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 40 && level <= 50) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_40
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 50 && level <= 60) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_50
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 60 && level <= 70) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_60
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 70 && level <= 80) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_70
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 80 && level <= 90) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_80
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                } else if (level >= 90 && level <= 100) {
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.charge_battery_90
                            ,battery,new ImageSize(DensityUtils.dp2px(16),DensityUtils.dp2px(16)));
                }
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                // 没有充电
                if (level >= 0 && level <= 10) {
                    battery.setImageResource(R.mipmap.battery_0);
                } else if (level >= 10 && level <= 20) {
                    battery.setImageResource(R.mipmap.battery_10);
                } else if (level >= 20 && level <= 30) {
                    battery.setImageResource(R.mipmap.battery_20);
                } else if (level >= 30 && level <= 40) {
                    battery.setImageResource(R.mipmap.battery_30);
                } else if (level >= 40 && level <= 50) {
                    battery.setImageResource(R.mipmap.battery_40);
                } else if (level >= 50 && level <= 60) {
                    battery.setImageResource(R.mipmap.battery_50);
                } else if (level >= 60 && level <= 70) {
                    battery.setImageResource(R.mipmap.battery_60);
                } else if (level >= 70 && level <= 80) {
                    battery.setImageResource(R.mipmap.battery_70);
                } else if (level >= 80 && level <= 90) {
                    battery.setImageResource(R.mipmap.battery_80);
                } else if (level >= 90 && level <= 100) {
                    battery.setImageResource(R.mipmap.battery_90);
                }
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // 充满
                battery.setImageResource(R.mipmap.battery_100);

                break;
        }
    }
}

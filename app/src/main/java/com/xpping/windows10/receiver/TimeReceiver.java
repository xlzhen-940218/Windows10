package com.xpping.windows10.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xlzhen on 9/12 0012.
 * 主窗体右下角 每分钟更新时间消息监听
 */

public class TimeReceiver extends BroadcastReceiver {
    private TextView clockView;

    public TimeReceiver(TextView clockView) {
        this.clockView = clockView;
        //首次打开避免被用户看到初始值
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm\nMM/dd EEEE");
        clockView.setText(format.format(date));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        assert action != null;
        if (action.equals(Intent.ACTION_TIME_TICK)) {

            long time = System.currentTimeMillis();
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm\nMM/dd EEEE");
            clockView.setText(format.format(date));
        }
    }
}

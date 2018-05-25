package com.xpping.windows10.service;


import android.app.Notification;
import android.content.Intent;
import android.os.Binder;

import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.xpping.windows10.widget.OperationCenterDialog;
import com.xpping.windows10.widget.ToastView;

import java.lang.reflect.Array;
import java.util.Arrays;

/*
*通知栏监听
*/
public class ListenerNotificationService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("服务被创建", "ListenerNotificationService");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("服务被销毁", "ListenerNotificationService");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i("监听已连接", "ListenerNotificationService");
        try {
            ToastView.getInstaller(getApplicationContext()).setText("通知监听已开始，部分机型消息接收会有延迟...,请耐心等待。").show();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

        if (OperationCenterDialog.operationCenterDialog != null && getActiveNotifications() != null) {
                OperationCenterDialog.operationCenterDialog.addListNotificationView(Arrays.asList(getActiveNotifications()));
        }
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.i("监听断开连接", "ListenerNotificationService");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            Log.i("系统删除通知", "包名：" + statusBarNotification.getPackageName() + ",标题：" + statusBarNotification.getNotification().extras.getString(Notification.EXTRA_TITLE));
        else
            Log.i("系统删除通知", "包名：" + statusBarNotification.getPackageName() + ",标题：" + statusBarNotification.getNotification().tickerText.toString());

    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            Log.i("系统新增通知", "包名：" + statusBarNotification.getPackageName() + ",标题：" + statusBarNotification.getNotification().extras.getString(Notification.EXTRA_TITLE));
        else
            Log.i("系统新增通知", "包名：" + statusBarNotification.getPackageName() + ",标题：" + statusBarNotification.getNotification().tickerText.toString());
        if (OperationCenterDialog.operationCenterDialog != null)
            OperationCenterDialog.operationCenterDialog.addNotificationView(statusBarNotification);
    }
}

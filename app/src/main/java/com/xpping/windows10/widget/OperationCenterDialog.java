package com.xpping.windows10.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.adapter.NotificationListenerAdapter;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/*
*操作中心 通知 合并
*/
public class OperationCenterDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ListView listView;
    private TextView noticeTitle;

    private NotificationListenerAdapter notificationListenerAdapter;

    public static OperationCenterDialog operationCenterDialog;

    public OperationCenterDialog(@NonNull Context context) {
        super(context, R.style.dialogsss);
        this.context = context;
        if (getWindow() != null)
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_operation_center);
        changeOrientation();
        initData();
    }

    private void initData() {
        operationCenterDialog = this;
        listView = findViewById(R.id.listView);
        notificationListenerAdapter = new NotificationListenerAdapter(context, new ArrayList<StatusBarNotification>());
        listView.setAdapter(notificationListenerAdapter);

        findViewById(R.id.operation_center_button).setOnClickListener(this);
        findViewById(R.id.operation_clear_all).setOnClickListener(this);

        noticeTitle = findViewById(R.id.noticeTitle);
        noticeTitle.setOnClickListener(this);
        noticeTitle.setText(AppUtis.notificationListenerEnable(context) ? "没有新的通知" : "请点此去开启通知权限！");
    }

    public void changeOrientation() {
        final Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.END | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.OperationDialogAnimation);
        lp.y = DensityUtils.dp2px(40); // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        dialogWindow.setLayout(DensityUtils.dp2px(340), DensityUtils.getScreenH(context) - DensityUtils.dp2px(40));
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialogWindow.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialogWindow.getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operation_center_button:
                if (v.getTag() == null)
                    v.setTag(false);

                boolean showOrHide = (boolean) v.getTag();

                findViewById(R.id.bluetooth).setVisibility(showOrHide ? View.VISIBLE : View.GONE);
                findViewById(R.id.wlan).setVisibility(showOrHide ? View.VISIBLE : View.GONE);
                findViewById(R.id.hotspot).setVisibility(showOrHide ? View.VISIBLE : View.GONE);
                findViewById(R.id.flight_mode).setVisibility(showOrHide ? View.VISIBLE : View.GONE);
                ((TextView) findViewById(R.id.operation_center_button)).setText(!showOrHide ? "展开" : "折叠");
                v.setTag(!showOrHide);
                break;
            case R.id.noticeTitle:
                if (AppUtis.notificationListenerEnable(context)) {
                    ToastView.getInstaller(context).setText("已有通知权限，不需要重复开启").show();
                    return;
                }

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastView.getInstaller(context).setText("请找到Win10安卓桌面，然后开启通知权限！").show();
                    }
                }, 1000);
                Intent intent = new Intent(MAIN_BROADCAST);
                intent.putExtra("message", "openNotification");
                context.sendBroadcast(intent);
                break;
            case R.id.operation_clear_all:
                notificationListenerAdapter.setData(new ArrayList<StatusBarNotification>());
                v.setVisibility(View.GONE);
                noticeTitle.setText("没有新的通知");
                break;
        }
    }

    public void listenerNotificationIsOpen() {
        boolean isOpen = AppUtis.notificationListenerEnable(context);

        //如果权限已开启，但noticeTitle还是去开启通知权限，就要弹窗提醒用户通知权限已开启
        if (isOpen && noticeTitle.getText().toString().equals("请点此去开启通知权限！"))
            ToastView.getInstaller(context).setText("已开启通知权限").show();

        noticeTitle.setText(isOpen ? "没有新的通知" : "请点此去开启通知权限！");

    }

    public void addNotificationView(StatusBarNotification statusBarNotification) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.operation_clear_all).setVisibility(View.VISIBLE);
                noticeTitle.setText("");
                findViewById(R.id.noticeEgg).setVisibility(View.GONE);
            }
        });

        boolean isAdd=false;
        for (int i = 0; i < notificationListenerAdapter.getData().size(); i++)
            if (notificationListenerAdapter.getData().get(i).getId() == statusBarNotification.getId()) {
                notificationListenerAdapter.getData().set(i, statusBarNotification);
                isAdd = true;
            }

        if (!isAdd)
            notificationListenerAdapter.addData(statusBarNotification);
    }

    public void addListNotificationView(List<StatusBarNotification> statusBarNotifications) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.operation_clear_all).setVisibility(View.VISIBLE);
                noticeTitle.setText("");
                findViewById(R.id.noticeEgg).setVisibility(View.GONE);
            }
        });

        notificationListenerAdapter.addData(statusBarNotifications);
    }

    @Override
    public void show() {
        super.show();
        changeOrientation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppUtis.hideBottomUIMenu((Activity) context);
    }

    public List<StatusBarNotification> getListNotification() {
        return notificationListenerAdapter.getData();
    }
}

package com.xpping.windows10.receiver;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.xpping.windows10.fragment.AppsCurrencyFragment;
import com.xpping.windows10.fragment.EdgeFragment;
import com.xpping.windows10.fragment.base.BaseFragment;

/*
* 主窗体消息接收器
*/
public class MainMessageReceiver extends BroadcastReceiver {
    private BaseFragment[] baseFragments;
    private CallBack callback;

    public MainMessageReceiver(CallBack callback, BaseFragment... baseFragments) {
        this.callback = callback;
        this.baseFragments = baseFragments;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (BaseFragment baseFragment : baseFragments) {
            String fragmentName = intent.getStringExtra("message");

            if (fragmentName.equals(baseFragment.getName())) {
                if (callback != null)
                    callback.openReceiverFragment(baseFragment);
            }

            if(intent.getStringExtra("url")!=null&&baseFragment instanceof EdgeFragment)
                ((EdgeFragment)baseFragment).setUrl(intent.getStringExtra("url"));
        }
        switch (intent.getStringExtra("message")) {
            case "openNotification":
                try {
                    Intent notificationIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(notificationIntent);
                } catch (ActivityNotFoundException e) {
                    try {
                        Intent notificationIntent = new Intent();
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                        notificationIntent.setComponent(cn);
                        notificationIntent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                        context.startActivity(notificationIntent);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                break;
            case "closeStartMenuDialog":
                if (callback != null)
                    callback.closeStartMenuDialog();

                break;
            case "refreshWallpaper":
                if (callback != null)
                    callback.refreshWallpaper();
                break;
            case "nextPage"://执行看美女图片的下一页操作
                if (callback != null)
                    callback.nextPage();

                break;
            case "updateCatPhotoFragment":
                if (callback != null)
                    callback.updateCatPhotoFragment(intent);
                break;
            case "openCatPhotoFragment":
                if (callback != null)
                    callback.openCatPhotoFragment(intent);
                break;
            case "应用商店":
                if (callback != null)
                    callback.openAppStore();
                break;
            case "相机":
                if (callback != null)
                    callback.openCamera();
                break;
        }
    }

    public interface CallBack {
        void openReceiverFragment(BaseFragment baseFragment);

        void openCatPhotoFragment(Intent intent);

        void updateCatPhotoFragment(Intent intent);

        void nextPage();

        void refreshWallpaper();

        void closeStartMenuDialog();

        void openAppStore();
        void openCamera();
    }
}

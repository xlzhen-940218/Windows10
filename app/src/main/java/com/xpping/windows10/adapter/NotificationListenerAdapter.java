package com.xpping.windows10.adapter;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.widget.ToastView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
*通知栏 通知适配器
*/
public class NotificationListenerAdapter extends SimpleAdapter<StatusBarNotification> {
    public NotificationListenerAdapter(Context context, List<StatusBarNotification> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_layout, parent, false);
            convertView.setOnClickListener(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bundle extras = data.get(position).getNotification().extras;
            String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
            Bitmap notificationLargeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
            Bitmap notificationSmallIcon = extras.getParcelable(Notification.EXTRA_SMALL_ICON);

            CharSequence notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence notificationSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            if (notificationTitle == null)
                notificationTitle = data.get(position).getPackageName();

            ((TextView) convertView.findViewById(R.id.notificationTitle)).setText(notificationTitle);
            ((TextView) convertView.findViewById(R.id.notificationText)).setText(notificationText);
            Log.i("通知内容", "标题：" + notificationTitle + ",内容：" + notificationText + ",icon：" + notificationLargeIcon + ",package：" + data.get(position).getPackageName());
            if (notificationLargeIcon != null)
                ((ImageView) convertView.findViewById(R.id.icon)).setImageBitmap(notificationLargeIcon);
            else if (notificationSmallIcon != null) {
                ((ImageView) convertView.findViewById(R.id.icon)).setImageBitmap(notificationSmallIcon);
            } else {
                try {
                    ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(context.getPackageManager().getApplicationInfo(data.get(position).getPackageName()
                            , 0).loadIcon(context.getPackageManager()));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Icon notificationLargeBigIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG);
                if (notificationLargeBigIcon != null)
                    ((ImageView) convertView.findViewById(R.id.icon)).setImageIcon(notificationLargeBigIcon);
            }
        } else {
            List<String> textList = getText(data.get(position).getNotification());
            if (textList.size() > 0)
                ((TextView) convertView.findViewById(R.id.notificationTitle)).setText(textList.get(0));
            if (textList.size() > 1)
                ((TextView) convertView.findViewById(R.id.notificationText)).setText(textList.get(1));

            try {
                ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(context.getPackageManager()
                        .getApplicationInfo(data.get(position).getNotification().contentIntent.getCreatorPackage()
                        , 0).loadIcon(context.getPackageManager()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        convertView.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        try {
            PendingIntent pendingIntent = data.get(position).getNotification().contentIntent;

            if (pendingIntent != null)
                pendingIntent.send();

            data.remove(position);
            notifyDataSetChanged();
        } catch (Exception ex) {
            ToastView.getInstaller(context).setText("无法打开此通知").show();
        }
    }

    public List<String> getText(Notification notification) {
        if (null == notification) {
            return null;
        }
        RemoteViews views = notification.bigContentView;
        if (views == null) {
            views = notification.contentView;
        }
        if (views == null) {
            return null;
        }
        // Use reflection to examine the m_actions member of the given RemoteViews object.
        // It's not pretty, but it works.
        List<String> text = new ArrayList<>();
        try {
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);
            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions) {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
                if (tag != 2) continue;
                // View ID
                parcel.readInt();
                String methodName = parcel.readString();
                if (null == methodName) {
                    continue;
                } else if (methodName.equals("setText")) {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();
                    // Store the actual string
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    text.add(t);
                }
                parcel.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}

package com.xpping.windows10.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wanjian.cockroach.App;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.SavePreference;

/*
*操作中心底部 功能开关 view
*/
public class OperationItemView extends RelativeLayout {
    private boolean isWifiApOpen;

    public OperationItemView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_operation_item, this);

        @SuppressLint("Recycle") TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.OperationItemView);

        final String text = typedArray.getString(R.styleable.OperationItemView_operationText);
        final String packageName = typedArray.getString(R.styleable.OperationItemView_operationPackageName);
        final String action = typedArray.getString(R.styleable.OperationItemView_operationAction);
        final String uri = typedArray.getString(R.styleable.OperationItemView_operationUri);
        final String category = typedArray.getString(R.styleable.OperationItemView_operationCategory);
        int img = typedArray.getResourceId(R.styleable.OperationItemView_operationImage, R.mipmap.tablet_mode);

        BaseApplication.imageLoader.displayImage("drawable://" + img
                , (ImageView) findViewById(R.id.operation_image));
        ((TextView) findViewById(R.id.operation_text)).setText(text);

        findViewById(R.id.operation_item).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                assert text != null;
                if (text.equals("平板模式")) {
                    if (!AppUtis.isPad(context)&& !SavePreference.getBoolean(context,"enableTablet")) {
                        ToastView.getInstaller(context).setText("对不起，您的手机屏幕太小，无法开启平板模式！").show();
                        return;
                    }
                    if (v.getTag() == null)
                        v.setTag(true);

                    StartMenuDialog.isFullScreen=(boolean)v.getTag();
                    ToastView.getInstaller(context).setText(StartMenuDialog.isFullScreen?"平板模式已开启，开启开始菜单即可体验"
                            :"平板模式已关闭，开启开始菜单即可查看").show();
                    findViewById(R.id.operation_item).setBackgroundResource(StartMenuDialog.isFullScreen
                            ? R.drawable.operation_item_check_bg : R.drawable.operation_item_bg);

                    v.setTag(!(boolean)v.getTag());
                    return;
                }
                if (text.equals("移动热点")) {
                    AppUtis.setWifiApEnabled(context, !AppUtis.getWiFiAPConfig(context));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isWifiApOpen = AppUtis.getWiFiAPConfig(context);

                                    ToastView.getInstaller(context).setText(isWifiApOpen ? "热点创建成功，账号：" + Build.MODEL + ",密码：123456789！"
                                            : "热点已关闭").show();
                                    findViewById(R.id.operation_item).setBackgroundResource(isWifiApOpen ? R.drawable.operation_item_check_bg : R.drawable.operation_item_bg);
                                }
                            }, 2000);
                        }
                    }).start();
                    return;
                }
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
                    } else {
                        ToastView.getInstaller(context).setText("此应用“ " + text + " ”未遵循Google API标准" +
                                "，所以无法打开(package，action，uri，category都未遵循)").setDuration(Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    try {
                        if (action != null) {
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
                        } else {
                            ToastView.getInstaller(context).setText("此应用“ " + text + " ”未遵循Google API标准" +
                                    "，所以无法打开(package，action，uri，category都未遵循)").setDuration(Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex1) {
                        try {
                            if (uri != null) {
                                Intent intent = new Intent();
                                intent.setData(Uri.parse(uri));
                                context.startActivity(intent);
                            } else if (category != null) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.MAIN");
                                intent.addCategory(category);
                                context.startActivity(intent);
                            } else {
                                ToastView.getInstaller(context).setText("此应用“ " + text + " ”未遵循Google API标准" +
                                        "，所以无法打开(package，action，uri，category都未遵循)").setDuration(Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex2) {
                            try {
                                if (category != null) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.MAIN");
                                    intent.addCategory(category);
                                    context.startActivity(intent);
                                } else {
                                    ToastView.getInstaller(context).setText("此应用“ " + text + " ”未遵循Google API标准" +
                                            "，所以无法打开(package，action，uri，category都未遵循)").setDuration(Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex3) {
                                ToastView.getInstaller(context).setText("此应用“ " + text + " ”未遵循Google API标准" +
                                        "，所以无法打开(package，action，uri，category都未遵循)").setDuration(Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
            }
        });
    }
}

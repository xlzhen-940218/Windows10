package com.xpping.windows10.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wanjian.cockroach.App;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.entity.AppEntity;
import com.xpping.windows10.entity.AppListPosition;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.receiver.AdminReceiver;
import com.xpping.windows10.web.URIConfig;
import com.xpping.windows10.widget.MenuPopupWindow;
import com.xpping.windows10.widget.MenuSelectDialog;
import com.xpping.windows10.widget.ToastView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by xlzhen on 9/7 0007.
 * app 工具
 */

public class AppUtis {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean showRecentlyApp(Activity activity) {
        Class serviceManagerClass;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService",
                    String.class);
            IBinder retbinder = (IBinder) getService.invoke(
                    serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder
                    .getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                    "asInterface", IBinder.class).invoke(null,
                    new Object[]{retbinder});
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.android.systemui", "com.android.systemui.recents.RecentsActivity");
                activity.startActivity(intent);
                return true;
            } catch (Exception ex) {
                SavePreference.save(activity, "Recently", true);
                ToastView.getInstaller(activity).setText("你的手机把最近任务功能挪走了，打不开了...").show();
                return false;
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void openVoiceAI(Activity activity) {
        openPackageListApp(activity, new String[]{"com.microsoft.cortana", "com.meizu.voiceassistant"
                , "com.huawei.vassistant", "com.google.android.voicesearch"
                , "com.ou.xoxhsyz", "com.miui.voiceassist", "com.vlingo.midas"
        }, new CallBack() {
            @Override
            public void allFailed(Activity activity) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VOICE_COMMAND");
                    activity.startActivity(intent);
                } catch (Exception ex1) {
                    SavePreference.save(activity, "voiceAI", true);
                    ToastView.getInstaller(activity).setText("没有语音类应用").show();
                }
            }
        });

    }

    private static MenuSelectDialog menuSelectDialog;

    public static void openPackageListApp(final Activity activity, String[] packageNames, CallBack callBack) {
        List<ApplicationInfo> applicationInfos = new ArrayList<>();
        for (String packageName : packageNames) {
            if (isPkgInstalled(activity, packageName) != null)
                applicationInfos.add(isPkgInstalled(activity, packageName));
        }
        if (applicationInfos.size() == 1) {
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage(applicationInfos.get(0).packageName);
            activity.startActivity(intent);
        } else if (applicationInfos.size() > 1) {
            if (menuSelectDialog == null) {
                menuSelectDialog = new MenuSelectDialog(activity);
            }
            menuSelectDialog.setMenuDataApplication(applicationInfos, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(((ApplicationInfo) view.getTag()).packageName);
                    activity.startActivity(intent);
                    menuSelectDialog.dismiss();
                }
            });
            menuSelectDialog.show();
        } else
            callBack.allFailed(activity);

    }

    /**
     * 自定义wifi热点
     *
     * @param enabled 开启or关闭
     * @return
     */
    public static boolean setWifiApEnabled(Context context, boolean enabled) {
        if (!isHasPermissions(context))
            return false;

        boolean result = false;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null)
            return false;

        if (enabled) {
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        } else
            wifiManager.setWifiEnabled(true);

        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称
            apConfig.SSID = Build.MODEL;
            //配置热点的密码，至少八位
            apConfig.preSharedKey = "123456789";
            //配置热点安全性选项
            int indexOfWPA2_PSK = 4;
            //从WifiConfiguration.KeyMgmt数组中查找WPA2_PSK的值
            for (int i = 0; i < WifiConfiguration.KeyMgmt.strings.length; i++) {
                if (WifiConfiguration.KeyMgmt.strings[i].equals("WPA2_PSK")) {
                    indexOfWPA2_PSK = i;
                    break;
                }
            }
            apConfig.allowedKeyManagement.set(indexOfWPA2_PSK);
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            //返回热点打开状态
            result = (Boolean) method.invoke(wifiManager, apConfig, enabled);

            if (!result) {
                ToastView.getInstaller(context).setText("热点创建失败，请手动创建！").show();
                openAPUI(context);
            }
        } catch (Exception e) {
            ToastView.getInstaller(context).setText("热点创建失败，请手动创建！").show();
            openAPUI(context);
        }
        return result;
    }

    public static boolean isHasPermissions(Context context) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                ToastView.getInstaller(context).setText("打开热点需要启用“修改系统设置”权限，请手动开启").show();


                //清单文件中需要android.permission.WRITE_SETTINGS，否则打开的设置页面开关是灰色的
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                //判断系统能否处理，部分ROM无此action，如魅族Flyme
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    //打开应用详情页
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            } else {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 打开网络共享与热点设置页面
     */
    public static void openAPUI(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //打开网络共享与热点设置页面
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$TetherSettingsActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    /**
     * 读取热点配置信息
     */
    public static boolean getWiFiAPConfig(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(wifiManager);
            if (apConfig == null) {
                return false;
            }
            return isWifiApEnabled(context);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否已打开WiFi热点
     *
     * @return
     */
    public static boolean isWifiApEnabled(Context context) {
        boolean isOpen = false;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return false;
            }
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            isOpen = (boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    public static ApplicationInfo isPkgInstalled(Context context, String packageName) {

        if (packageName == null || "".equals(packageName))
            return null;

        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String getPhotoUri(Context context, Intent data) {
        String path = "";
        try {
            if (data.getData().getPath().contains(Environment.getExternalStorageDirectory().getPath())) {
                path = data.getData().getPath();
                return path;
            }
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex); //获取照片路径
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Check if the CPU architecture is x86
     */
    public static boolean checkIfCPUx86() {
        //1. Check CPU architecture: arm or x86
        if (getSystemProperty("ro.product.cpu.abi", "arm").contains("x86")) {
            //The CPU is x86
            return true;
        } else {
            return false;
        }
    }


    private static String getSystemProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(clazz, key, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return value;
    }

    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean notificationListenerEnable(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public static void openAppDetails(Context context, String appPackage) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", appPackage, null));
        context.startActivity(intent);
    }

    public static void openWeather(Context context) {

    }
    /*
     *打开微信扫一扫
     */
    public static void openWeChatCamera(Context context) {
        try {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setAction("com.tencent.mm.action.BIZSHORTCUT");
            intent1.addCategory("android.intent.category.DEFAULT");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent1.setPackage("com.tencent.mm");
            intent1.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            context.startActivity(intent1);
        } catch (Exception ex) {
            ToastView.getInstaller(context).setText("您未安装微信").show();
        }

    }

    public static void openWeChatWalletOfflineCoinPurseUI(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("am start -n com.tencent.mm/"+ URIConfig.wx_fukuan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openWeChatCollectMainUI(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("am start -n com.tencent.mm/"+ URIConfig.wx_shoukuan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openQQScanTorchActivity(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("am start -n com.tencent.mobileqq/"+ URIConfig.function_qq_scan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface CallBack {
        void allFailed(Activity activity);
    }

    public static void openAppStore(Activity activity) {
        openPackageListApp(activity, new String[]{"com.coolapk.market", "com.huawei.appmarket", "com.tencent.android.qqdownloader"
                , "com.qihoo.appstore", "com.baidu.appsearch", "com.xiaomi.market", "com.wandoujia.phoenix2"
                , "com.hiapk.marketpho", "com.yingyonghui.market", "com.oppo.market", "zte.com.market"
                , "com.lenovo.leos.appstore", "com.bbk.appstore", "com.sec.android.app.samsungapps"
                , "com.meizu.mstore", "com.oneplus.market", "com.smartisan.appstore", "com.android.vending"}, new CallBack() {
            @Override
            public void allFailed(Activity activity) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory("android.intent.category.APP_MARKET");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    activity.startActivity(intent);

                } catch (Exception ex) {
                    ToastView.getInstaller(activity).setText("您的手机没有任何应用商店可供打开").show();
                }

            }
        });
    }

    public static void openXXIONGAppStore(Activity activity) {
        //context.startActivity(new Intent(context, StoreActivity.class));
        AppUtis.openAppStore(activity);
    }

    //设置系统壁纸
    public static void setWallpaperBackground(final Activity activity) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有权限
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastView.getInstaller(activity).setText("没有内存卡读写权限，无法读取系统壁纸信息，正在为您设置默认壁纸...").show();

                        }
                    });
                    return;
                }
                final Drawable wallpaperManagerDrawable = wallpaperManager.getFastDrawable();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) activity.findViewById(R.id.wallpaperBackground)).setImageDrawable(wallpaperManagerDrawable);
                    }
                });
            }
        }).start();
    }

    // 隐藏标题栏 和 隐藏状态栏
    public static void setFullScreen(Activity activity) {
        if (SavePreference.getBoolean(activity, "statusBar_is_show"))
            return;

        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu(activity);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(Activity activity) {
        if (SavePreference.getBoolean(activity, "statusBar_is_show"))
            return;

        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 14 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void openClock(Activity activity) {
        //通过三种调用方式 打开系统闹钟界面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        try {

            intent.setComponent(new ComponentName("com.android.alarmclock", "com.android.alarmclock.AlarmClock"));
            activity.startActivity(intent);
        } catch (Exception ex) {
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction("android.intent.action.SHOW_ALARMS");
                activity.startActivity(intent);
            } catch (Exception ex1) {
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("deskclock://deskclock.android.com"));
                    activity.startActivity(intent);
                } catch (Exception ex2) {

                }

            }

        }

    }

    public static void listenerSignal(final Activity activity, final ImageView imageView) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new PhoneStateListener() {
            /*
             * Get the Signal strength from the provider, each tiome there is an
             * update 从得到的信号强度,每个tiome供应商有更新
             */
            //这个方法只有在信号强度改变时才调用，或者程序刚刚启动时调用，如果想看到Toast的信号强度提示，那就等信号改变或者重启程序
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                SignalStrengthsHandler.SimSignalInfo[] sims;
                try {
                    try {
                        sims = SignalStrengthsHandler.getInstance(activity).getSimSignalInfos();
                    } catch (Exception ex) {
                        sims = new SignalStrengthsHandler.SimSignalInfo[2];
                        sims[0] = new SignalStrengthsHandler.SimSignalInfo();
                    }
                } catch (Exception noSuchMethodError) {
                    sims = new SignalStrengthsHandler.SimSignalInfo[2];
                    sims[0] = new SignalStrengthsHandler.SimSignalInfo();
                }


                switch (sims[0].mLevel) {
                    case 0:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_0
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                    case 1:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_1
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                    case 2:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_2
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                    case 3:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_3
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                    case 4:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_4
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                    case 5:
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.signal_5
                                , imageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                        break;
                }

            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    public static void listenerWifi(final Activity activity, final ImageView wifiImageView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isWifiConnect(activity)) {
                    WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
                    int wifi = mWifiInfo.getRssi();//获取wifi信号强度
                    if (wifi > -30 && wifi < 0) {
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_4
                                , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                    } else if (wifi > -50 && wifi < -30) {//最强
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_3
                                , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                    } else if (wifi > -70 && wifi < -50) {//较强
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_2
                                , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                    } else if (wifi > -80 && wifi < -70) {//较弱
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_1
                                , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                    } else if (wifi > -100 && wifi < -80) {//微弱
                        BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_0
                                , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                    }
                } else {
                    //无连接
                    BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.wifi_error
                            , wifiImageView, new ImageSize(DensityUtils.dp2px(16), DensityUtils.dp2px(16)));
                }
                listenerWifi(activity, wifiImageView);
            }
        }, 5000);

    }

    /**
     * 检查wifi是否处开连接状态
     *
     * @return
     */
    private static boolean isWifiConnect(Activity activity) {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    private static DevicePolicyManager policyManager;
    private static ComponentName componentName;

    //关闭屏幕
    public static void closeScreen(Activity activity) {
        if (policyManager == null)
            policyManager = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (componentName == null)
            componentName = new ComponentName(activity, AdminReceiver.class);
        if (!policyManager.isAdminActive(componentName)) {
            goSetActivity(activity);
        } else {
            systemLock();
        }
    }

    private static void goSetActivity(Context context) {

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        context.startActivity(intent);

    }

    /**
     * 锁屏并关闭屏幕
     */
    private static void systemLock() {
        if (policyManager.isAdminActive(componentName))
            policyManager.lockNow();

    }

    /**
     * 关机
     */
    public static void shutDown(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("reboot -p");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开微信 我的名片
     */
    public static void openWeChatSelfQRCodeUI(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("am start -n com.tencent.mm/"+ URIConfig.wx_self_qr_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *重启
     */
    public static void reboot(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("reboot");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rebootRecovery(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("reboot recovery");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rebootBootloader(Context context) {
        try {
            ToastView.getInstaller(context).setText("此功能需要root权限").show();
            boolean isRoot = RootCommand("chmod 777 " + context.getPackageCodePath());
            ToastView.getInstaller(context).setText(isRoot ? "已获得root权限" : "未获得root权限").show();
            RootCommand("reboot bootloader");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }


    // 获取汉字的首字母大写
    public static String getFirstSpell(String string) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = string.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        if (arr[0] > 128) { //如果已经是字母就不用转换了
            try {
                //获取当前汉字的全拼
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[0], defaultFormat);
                if (temp != null) {
                    pybf.append(temp[0].charAt(0));// 取首字母
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            if (arr[0] >= 'a' && arr[0] <= 'z') {
                arr[0] -= 32;
            }
            pybf.append(arr[0]);
        }

        return pybf.toString();
    }

    public static List<AppEntity> initAppCreateList(final Context context) {
        List<AppEntity> appEntities = new ArrayList<>();
        BaseApplication.appListPositions = new ArrayList<>();
        if (!SavePreference.getBoolean(context, "init_first_add_show")) {
            SavePreference.save(context, "init_first_add_show", true);
            SavePreference.save(context, "first_add_show", true);
        }

        if (SavePreference.getBoolean(context, "first_add_show")) {
            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);

            try {
                Collections.sort(packageInfos, new Comparator<PackageInfo>() {
                    @Override
                    public int compare(PackageInfo packageInfo, PackageInfo t1) {
                        return packageInfo.firstInstallTime > t1.firstInstallTime ? -1 : 1;
                    }
                });
            } catch (Exception ex) {

            }
            for (int i = 0; i < (packageInfos.size() > 10 ? 10 : packageInfos.size()); i++) {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setPackage(packageInfos.get(i).packageName);
                List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
                if (resolveInfos.size() > 0) {
                    AppEntity appEntity = new AppEntity();

                    appEntity.setAppPackage(packageInfos.get(i).packageName);
                    appEntity.setAppTitle(packageInfos.get(i).applicationInfo.loadLabel(context.getPackageManager()).toString());
                    if (i == 0) {
                        appEntity.setHeadName("最近添加");

                        BaseApplication.appListPositions.add(new AppListPosition(appEntity.getHeadName(), appEntities.size()));
                    } else
                        appEntity.setHeadName("");

                    appEntities.add(appEntity);
                } else {
                    packageInfos.remove(i);
                    i--;
                }
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert mActivityManager != null;
            List<ActivityManager.RecentTaskInfo> appList4 = mActivityManager
                    .getRecentTasks(6, ActivityManager.RECENT_WITH_EXCLUDED);//参数，前一个是你要取的最大数，后一个是状态

            try {
                for (int i = 0; i < appList4.size(); i++) {
                    AppEntity appEntity = new AppEntity();
                    appEntity.setAppPackage(context.getPackageManager().resolveActivity(appList4.get(i).baseIntent, 0).activityInfo.packageName);
                    appEntity.setAppTitle(context.getPackageManager().resolveActivity(appList4.get(i).baseIntent, 0).loadLabel(context.getPackageManager()).toString());
                    if (i == 0) {
                        appEntity.setHeadName("最近使用");
                        BaseApplication.appListPositions.add(new AppListPosition(appEntity.getHeadName(), appEntities.size()));
                    } else
                        appEntity.setHeadName("");

                    appEntities.add(appEntity);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);

        Collections.sort(resolveInfos, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo resolveInfo0, ResolveInfo resolveInfo1) {
                return AppUtis.getFirstSpell(resolveInfo0.loadLabel(context.getPackageManager()).toString())
                        .compareTo(AppUtis.getFirstSpell(resolveInfo1.loadLabel(context.getPackageManager()).toString()));
            }
        });
        String firstLetterA = "";

        for (ResolveInfo resolveInfo : resolveInfos) {

            AppEntity appEntity = new AppEntity();

            appEntity.setAppPackage(resolveInfo.activityInfo.packageName);
            appEntity.setAppTitle(resolveInfo.loadLabel(context.getPackageManager()).toString());
            String firstLetterB = AppUtis.getFirstSpell(appEntity.getAppTitle());
            appEntity.setHeadName(firstLetterA.equals(firstLetterB) ? "" : firstLetterB);
            if (!firstLetterA.equals(firstLetterB)) {
                BaseApplication.appListPositions.add(new AppListPosition(appEntity.getHeadName(), appEntities.size()));
                firstLetterA = firstLetterB;
            }

            appEntities.add(appEntity);
        }

        return appEntities;
    }

    public static void openSignal(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        activity.startActivity(intent);

    }

    public static void openWifi(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            activity.startActivity(intent);

        } catch (Exception ex) {
            ToastView.getInstaller(activity).setText("无法打开WIFI页").show();
        }
    }

    public static void openBattery(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            activity.startActivity(intent);

        } catch (Exception ex) {
            ToastView.getInstaller(activity).setText("无法打开电源管理").show();
        }

    }

    public static int topZViewId;//哪个fragment在最顶层

    public static void changeFragmentFocus(Activity activity, int resId) {
        activity.findViewById(resId).bringToFront();
        topZViewId = resId;
        Log.i("最顶层fragmentLayoutId", topZViewId + "");
    }

    public static List<DesktopEntity> initDesktopAppList(Context context) {
        List<DesktopEntity> appEntities = RecordDesktopUtils.getDesktopData(context, DesktopEntity.class, "win_desktop_data");
        if (appEntities.size() == 0) {
            appEntities.add(getAppEntity("此电脑"/*, "" + R.mipmap.ic_launcher*/, ""
                    , DesktopEntity.DesktopType.system));
            appEntities.add(getAppEntity(Build.MODEL/*, "" + R.mipmap.user_folder*/, ""
                    , DesktopEntity.DesktopType.system));
            appEntities.add(getAppEntity("网络"/*, "" + R.mipmap.internet*/, ""
                    , DesktopEntity.DesktopType.system));
            appEntities.add(getAppEntity("回收站"/*, "" + R.mipmap.recyclestationnull*/, ""
                    , DesktopEntity.DesktopType.system));
            appEntities.add(getAppEntity("控制面板"/*, "" + R.mipmap.control_panel*/, ""
                    , DesktopEntity.DesktopType.system));
            appEntities.add(getAppEntity("Internet\nExplorer"/*, "" + R.mipmap.iexplore*/, "com.android.browser"
                    , DesktopEntity.DesktopType.system));

            appEntities.add(getAppEntity("是男人就坚持10秒"/*, "" + R.mipmap.help*/, ""
                    , DesktopEntity.DesktopType.system));
            RecordDesktopUtils.saveDesktopData(context, appEntities, DesktopEntity.class, "win_desktop_data");
        }else if(!containIsTitle("是男人就坚持10秒",appEntities)){
            appEntities.add(getAppEntity("是男人就坚持10秒"/*, "" + R.mipmap.help*/, ""
                    , DesktopEntity.DesktopType.system));

        }else if(!containIsTitle("鸣谢",appEntities)){
            appEntities.add(getAppEntity("鸣谢"/*, "" + R.mipmap.help*/, ""
                    , DesktopEntity.DesktopType.system));

        }
        return appEntities;
    }

    private static boolean containIsTitle(String title,List<DesktopEntity> appEntities) {
        for(int i=0;i<appEntities.size();i++)
            if(appEntities.get(i).getAppTitle().equals(title))
                return true;

        return false;
    }

    public static DesktopEntity getAppEntity(String title/*, String icon*/, String
            packageName, DesktopEntity.DesktopType desktopType) {
        DesktopEntity desktopEntity = new DesktopEntity();
        desktopEntity.setHeadName("");
        desktopEntity.setAppTitle(title);
        desktopEntity.setAppPackage(packageName);
        //desktopEntity.setAppIcon(icon);
        desktopEntity.setDesktopType(desktopType);
        return desktopEntity;
    }

    public static List<DesktopEntity> initRecycleBinList(Context context) {
        return RecordDesktopUtils.getDesktopData(context, DesktopEntity.class, "recycle_data");
    }

    public static void saveDesktopData(Context context) {
        RecordDesktopUtils.removeAllDesktopData(context, "win_desktop_data");
        RecordDesktopUtils.saveDesktopData(context, BaseApplication.desktopAppEntities, DesktopEntity.class, "win_desktop_data");
    }

    public static void saveRecycleData(Context context) {
        RecordDesktopUtils.removeAllDesktopData(context, "recycle_data");
        RecordDesktopUtils.saveDesktopData(context, BaseApplication.recycleBinEntities, DesktopEntity.class, "recycle_data");
    }

    //卸载应用程序
    public static void uninstallApp(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);


    }

    public static List<AppEntity> initAppList(Context context) {
        return RecordDesktopUtils.getDesktopData(context, AppEntity.class, "app_data");
    }

    public static void saveAppList(Context context) {
        RecordDesktopUtils.removeAllDesktopData(context, "app_data");
        RecordDesktopUtils.saveDesktopData(context, BaseApplication.appEntities, AppEntity.class, "app_data");
    }

    public static boolean openFlashLight(Context context, boolean open) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            //如果不支持手电筒则直接返回
            return false;
        }
        if (mCamera == null) {
            mCamera = Camera.open();
            open = !mCamera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF);
        }

        Camera.Parameters mParameters;
        if (!open) {
            if (mCamera == null)
                mCamera = Camera.open();


            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameters);
            mCamera.startPreview();
        } else {
            if (mCamera == null)
                mCamera = Camera.open();

            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameters);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        open = !open;

        return open;
    }

    private static Camera mCamera;

    public static long getTotalMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(mi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return mi.totalMem;
        }
        return 0;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}

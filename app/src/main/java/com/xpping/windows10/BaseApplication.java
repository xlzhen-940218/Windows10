package com.xpping.windows10;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.wanjian.cockroach.Cockroach;
import com.xpping.windows10.entity.AppEntity;
import com.xpping.windows10.entity.AppListPosition;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.widget.ToastView;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;

/**
 * Created by xlzhen on 9/11 0011.
 * APP入口
 */

public class BaseApplication extends Application {
    public static List<AppEntity> appEntities;
    public static List<AppListPosition> appListPositions;
    public static List<DesktopEntity> desktopAppEntities;
    public static List<DesktopEntity> recycleBinEntities;

    public static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        desktopAppEntities = AppUtis.initDesktopAppList(getApplicationContext());
        recycleBinEntities = AppUtis.initRecycleBinList(getApplicationContext());
        //appEntities=AppUtis.initAppList(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                appEntities = AppUtis.initAppCreateList(getApplicationContext());
                AppUtis.saveAppList(getApplicationContext());
            }
        }).start();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//普通场景统计

        initFileDownloader();
        initImageLoader(getApplicationContext());
        if (!BuildConfig.DEBUG)
            Cockroach.install(new Cockroach.ExceptionHandler() {
                @Override
                public void handlerException(final Thread thread, final Throwable throwable) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                                Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                                ToastView.getInstaller(BaseApplication.this)
                                        .setText("Exception Happend\n" + thread + "\n" + throwable.toString()).show();
                            } catch (Throwable e) {

                            }
                        }
                    });
                }
            });
    }

    private void initFileDownloader() {
        FileDownloadLog.NEED_LOG = false;
        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(30_000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });
    }

    private void initImageLoader(Context context) {
        // 创建DisplayImageOptions对象
        DisplayImageOptions defaulOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        // 创建ImageLoaderConfiguration对象
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaulOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new FileNameGenerator() {
                    @Override
                    public String generate(String s) {
                        return s;
                    }
                })
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // ImageLoader对象的配置
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();
    }

}

package com.xpping.windows10.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;

import com.xpping.windows10.adapter.AppGridAdapter;
import com.xpping.windows10.adapter.AppListAdapter;
import com.xpping.windows10.entity.AppEntity;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.utils.AppUtis;

import java.util.ConcurrentModificationException;

/**
 * Created by xlzhen on 9/14 0014.
 * 监听卸载完成
 */

public class UninstallReceiver extends BroadcastReceiver {
    private AppGridAdapter appGridAdapter;
    private AppListAdapter appListAdapter;

    public UninstallReceiver(AppGridAdapter appGridAdapter, AppListAdapter appListAdapter) {
        this.appGridAdapter = appGridAdapter;
        this.appListAdapter = appListAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName=intent.getDataString().replace("package:","");
        try {
            for(DesktopEntity desktopEntity:BaseApplication.desktopAppEntities)
                if(desktopEntity.getAppPackage().equals(packageName)) {
                    BaseApplication.desktopAppEntities.remove(desktopEntity);
                    appGridAdapter.notifyDataSetChanged();
                    AppUtis.saveDesktopData(context);
                }
        }catch (ConcurrentModificationException ex){

        }

        try {
            for(DesktopEntity desktopEntity:BaseApplication.recycleBinEntities)
                if(desktopEntity.getAppPackage().equals(packageName)) {
                    BaseApplication.recycleBinEntities.remove(desktopEntity);
                    if(BaseApplication.recycleBinEntities.size()==0){
                        appGridAdapter.getRecycleBin().setAppIcon("空");
                        appGridAdapter.notifyDataSetChanged();
                        AppUtis.saveDesktopData(context);
                    }
                    AppUtis.saveRecycleData(context);
                }
        }catch (ConcurrentModificationException ex){

        }

        try {
            for(AppEntity appEntity:BaseApplication.appEntities)
                if(appEntity.getAppPackage().equals(packageName)) {
                    BaseApplication.appEntities.remove(appEntity);
                    appListAdapter.notifyDataSetChanged();
                }
        }catch (ConcurrentModificationException ex){

        }

    }
}

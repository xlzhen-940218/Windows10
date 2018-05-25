package com.xpping.windows10.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.adapter.AppListAdapter;
import com.xpping.windows10.entity.AppEntity;
import com.xpping.windows10.utils.AppUtis;

/**
 * Created by xlzhen on 9/15 0015.
 * 监听APP安装完成
 */

public class InstallReceiver extends BroadcastReceiver {
    private AppListAdapter appListAdapter;

    public InstallReceiver(AppListAdapter appListAdapter) {
        this.appListAdapter = appListAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getDataString() == null)
            return;

        String packageName = intent.getDataString().replace("package:", "");
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            AppEntity appEntity = new AppEntity();
            appEntity.setAppPackage(applicationInfo.packageName);
            appEntity.setAppTitle(applicationInfo.loadLabel(context.getPackageManager()).toString());


            boolean isAddHeadName = true;
            int addLocation=0;
            boolean isAddToAppList = true;
            String firstName = AppUtis.getFirstSpell(appEntity.getAppTitle());

            for (int i = 0; i < BaseApplication.appEntities.size(); i++) {
                if (firstName.equals(BaseApplication.appEntities.get(i).getHeadName())) {//如果在列表中找得到它的首字母，则不必添加字母了

                    addLocation=i + 1;
                    isAddHeadName = false;
                }
                if (appEntity.getAppPackage().equals(BaseApplication.appEntities.get(i).getAppPackage()))
                    isAddToAppList = false;
            }

            if (isAddHeadName) {
                appEntity.setHeadName(firstName);
            }
            if (isAddToAppList)
                BaseApplication.appEntities.add(addLocation,appEntity);
            appListAdapter.notifyDataSetChanged();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}

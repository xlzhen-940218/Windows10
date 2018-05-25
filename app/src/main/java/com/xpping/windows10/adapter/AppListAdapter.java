package com.xpping.windows10.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.entity.AppEntity;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.utils.AppUtis;

import com.xpping.windows10.widget.MenuPopupWindow;

import com.xpping.windows10.widget.ToastView;

import java.util.ArrayList;
import java.util.List;

/*
*xlzhen 2018/4/27
* 开始菜单 应用列表 适配器
*/

public class AppListAdapter extends SimpleAdapter<AppEntity> implements View.OnLongClickListener {
    private MenuPopupWindow appMenuDialog;
    private OnShowGridViewListener onShowGridViewListener;
    private OnAddToDesktopListener onAddToDesktopListener;

    public AppListAdapter(Context context, List<AppEntity> data, OnShowGridViewListener onShowGridViewListener
            ,OnAddToDesktopListener onAddToDesktopListener) {
        super(context, data);
        this.onShowGridViewListener = onShowGridViewListener;
        this.onAddToDesktopListener = onAddToDesktopListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appItem://打开APP
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(data.get((int) view.getTag()).getAppPackage());
                    context.startActivity(intent);
                } catch (Exception e) {
                    ToastView.getInstaller(context).setText("此APP无法打开").show();
                }
                break;
            case R.id.headName:
                if (onShowGridViewListener != null)
                    onShowGridViewListener.onShowGridView();
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false);
            convertView.findViewById(R.id.appItem).setOnClickListener(this);
            convertView.findViewById(R.id.appItem).setOnLongClickListener(this);
            convertView.findViewById(R.id.headName).setOnClickListener(this);
        }

        convertView.findViewById(R.id.headName).setVisibility(TextUtils.isEmpty(data.get(position).getHeadName()) ? View.GONE
                : View.VISIBLE);

        ((TextView) convertView.findViewById(R.id.headName)).setText(TextUtils.isEmpty(data.get(position).getHeadName()) ? ""
                : data.get(position).getHeadName());

        ((TextView) convertView.findViewById(R.id.appName)).setText(data.get(position).getAppTitle());
        try {
            if(data.get(position).getAppDrawable()==null)
                data.get(position).setAppDrawable(context.getPackageManager().getApplicationInfo(data.get(position).getAppPackage()
                        , 0).loadIcon(context.getPackageManager()));

            ((ImageView) convertView.findViewById(R.id.appIcon))
                    .setImageDrawable(data.get(position).getAppDrawable());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        convertView.findViewById(R.id.appItem).setTag(position);
        return convertView;
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.appItem://打开APP
                final AppEntity appEntity = data.get((int) view.getTag());
                appMenuDialog = new MenuPopupWindow(context, MenuPopupWindow.Style.black);
                List<String> menu = new ArrayList<>();
                menu.add("发送到桌面快捷方式");
                menu.add("打开应用详情页");
                menu.add("卸载");
                appMenuDialog.setMenuData(menu, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0://发送到桌面快捷方式
                                onAddToDesktopListener.onAdd(AppUtis.getAppEntity(appEntity.getAppTitle()
                                        , appEntity.getAppPackage(), DesktopEntity.DesktopType.app));
                                break;
                            case 1://打开应用详情页
                                AppUtis.openAppDetails(context,appEntity.getAppPackage());
                                break;
                            case 2://卸载
                                AppUtis.uninstallApp(context,appEntity.getAppPackage());
                                break;
                        }
                        appMenuDialog.dismiss();
                    }
                });
                appMenuDialog.showAsDropDown(view);
                break;
        }
        return true;
    }

    public interface OnShowGridViewListener {
        void onShowGridView();
    }

    public interface OnAddToDesktopListener {
        void onAdd(DesktopEntity desktopEntity);
    }
}
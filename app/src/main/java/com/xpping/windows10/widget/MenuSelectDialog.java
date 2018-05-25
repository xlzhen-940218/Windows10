package com.xpping.windows10.widget;

import android.app.Activity;
import android.app.Dialog;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;

import java.util.List;

/**
 * Created by xlzhen on 9/10 0010.
 * 菜单选择dialog
 */

public class MenuSelectDialog extends Dialog {
    private ListView listView;
    private Activity activity;

    public MenuSelectDialog(Activity activity, int x_location, int y_location) {
        super(activity, R.style.dialogsss);
        this.activity = activity;

        setContentView(R.layout.dialog_menu_select);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        lp.x = x_location;
        lp.y = y_location; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        listView = (ListView) findViewById(R.id.listView);
    }

    public MenuSelectDialog(Activity activity) {
        super(activity, R.style.dialogsss);
        this.activity = activity;

        setContentView(R.layout.dialog_menu_select);

        listView = (ListView) findViewById(R.id.listView);
    }

    public void setMenuData(List<String> menuData, AdapterView.OnItemClickListener itemClickListener) {
        listView.setAdapter(new SimpleAdapter<String>(activity, menuData) {

            @Override
            public void onClick(View view) {

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(activity);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    convertView.setBackgroundResource(R.drawable.start_menu_item_bg);
                    convertView.setPadding(DensityUtils.dp2px(10), DensityUtils.dp2px(10)
                            , DensityUtils.dp2px(10), DensityUtils.dp2px(10));

                    ((TextView) convertView).setTextColor(Color.WHITE);
                }
                ((TextView) convertView).setText(data.get(position));
                return convertView;
            }
        });
        listView.setOnItemClickListener(itemClickListener);
    }

    public void setMenuDataApplication(List<ApplicationInfo> menuData, AdapterView.OnItemClickListener itemClickListener) {
        listView.setAdapter(new SimpleAdapter<ApplicationInfo>(activity, menuData) {

            @Override
            public void onClick(View view) {

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(activity);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    convertView.setBackgroundResource(R.drawable.start_menu_item_bg);
                    convertView.setPadding(DensityUtils.dp2px(10), DensityUtils.dp2px(10)
                            , DensityUtils.dp2px(10), DensityUtils.dp2px(10));

                    ((TextView) convertView).setTextColor(Color.WHITE);
                }
                ((TextView) convertView).setText(data.get(position).loadLabel(activity.getPackageManager()).toString());
                convertView.setTag(data.get(position));
                return convertView;
            }
        });
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void show() {
        if (activity != null&&!activity.isFinishing())
            super.show();

        if (getWindow() != null) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                    getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }
            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppUtis.hideBottomUIMenu(activity);
    }
}

package com.xpping.windows10.utils;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;

import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.adapter.AppGridAdapter;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.widget.MenuPopupWindow;
import com.xpping.windows10.widget.ToastView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/*
*主窗体 桌面工具
*/
public class DesktopUtils {
    private MainActivity mainActivity;
    private AppGridAdapter appGridAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;

    public DesktopUtils(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        recyclerView = mainActivity.recyclerView;
        initAppGridAdapter();
        initRecyclerView();

    }

    private void initAppGridAdapter() {
        if (SavePreference.getInt(mainActivity, "icon_size_pad") == -1)
            SavePreference.save(mainActivity, "icon_size_pad", 6);

        if (SavePreference.getInt(mainActivity, "icon_size") == -1)
            SavePreference.save(mainActivity, "icon_size", 5);

        if (SavePreference.getInt(mainActivity, "icon_size_ver_pad") == -1)
            SavePreference.save(mainActivity, "icon_size_ver_pad", 10);

        if (SavePreference.getInt(mainActivity, "icon_size_ver") == -1)
            SavePreference.save(mainActivity, "icon_size_ver", 6);

        if (mainActivity.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(mainActivity, AppUtis.isPad(mainActivity)
                    ? SavePreference.getInt(mainActivity, "icon_size_pad")
                    : SavePreference.getInt(mainActivity, "icon_size"));
        } else {
            gridLayoutManager = new GridLayoutManager(mainActivity, AppUtis.isPad(mainActivity)
                    ? SavePreference.getInt(mainActivity, "icon_size_ver_pad")
                    : SavePreference.getInt(mainActivity, "icon_size_ver"));
        }


        appGridAdapter = new AppGridAdapter(mainActivity, gridLayoutManager.getSpanCount(), BaseApplication.desktopAppEntities
                , Color.parseColor("#ffffff"), new AppGridAdapter.OnClickSystemListener() {
            @Override
            public void pc(View view) {
                mainActivity.openWindowFragment(mainActivity.fileViewFragment);
            }

            @Override
            public void me(View view) {
                mainActivity.openWindowFragment(mainActivity.fileCategoryFragment);
            }

            @Override
            public void network(View view) {
                mainActivity.openWindowFragment(mainActivity.netWorkDeviceFragment);
            }

            @Override
            public void recycle(View view) {
                mainActivity.openWindowFragment(mainActivity.recycleFragment);
            }

            @Override
            public void help(View view) {
                mainActivity.openWindowFragment(mainActivity.thanksAboutFragment);
            }

            @Override
            public void control(View view) {
                mainActivity.openWindowFragment(mainActivity.controlFragment);
            }
        }, new AppGridAdapter.OnLongClickListener() {
            @Override
            public void onClick(View view, final int position, final DesktopEntity desktopEntity) {
                final MenuPopupWindow menuPopupWindow = new MenuPopupWindow(mainActivity, MenuPopupWindow.Style.white);
                if (desktopEntity.getDesktopType() == DesktopEntity.DesktopType.system
                        && desktopEntity.getAppTitle().equals("此电脑")) {
                    List<String> menuData = new ArrayList<>();
                    menuData.add("打开");
                    menuData.add("系统信息");
                    menuPopupWindow.setMenuData(menuData, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    mainActivity.openWindowFragment(mainActivity.fileViewFragment);
                                    break;
                                case 1:
                                    mainActivity.openWindowFragment(mainActivity.pcDetailsMessageFragment);
                                    break;
                            }
                            menuPopupWindow.dismiss();
                        }
                    });
                    menuPopupWindow.showAsDropDown(view);
                }
                if (desktopEntity.getDesktopType() == DesktopEntity.DesktopType.system
                        && desktopEntity.getAppTitle().equals("回收站")) {

                    List<String> menuData = new ArrayList<>();
                    menuData.add("清空回收站");
                    menuData.add("还原回收站");
                    menuPopupWindow.setMenuData(menuData, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    BaseApplication.recycleBinEntities = new ArrayList<>();
                                    AppUtis.saveRecycleData(mainActivity);
                                    appGridAdapter.getRecycleBin().setAppIcon("空");
                                    appGridAdapter.notifyDataSetChanged();
                                    AppUtis.saveDesktopData(mainActivity);
                                    break;
                                case 1:
                                    BaseApplication.desktopAppEntities.addAll(BaseApplication.recycleBinEntities);
                                    BaseApplication.recycleBinEntities = new ArrayList<>();
                                    AppUtis.saveRecycleData(mainActivity);
                                    appGridAdapter.getRecycleBin().setAppIcon("空");
                                    appGridAdapter.notifyDataSetChanged();
                                    AppUtis.saveDesktopData(mainActivity);
                                    break;
                            }
                            menuPopupWindow.dismiss();
                        }
                    });
                    menuPopupWindow.showAsDropDown(view);
                }
                if (desktopEntity.getDesktopType() == DesktopEntity.DesktopType.app) {

                    List<String> menuData = new ArrayList<>();
                    menuData.add("打开");
                    menuData.add("打开应用详情页");
                    menuData.add("卸载");
                    menuData.add("移入回收站");
                    menuPopupWindow.setMenuData(menuData, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    try {
                                        Intent intent = mainActivity.getPackageManager().getLaunchIntentForPackage(desktopEntity.getAppPackage());
                                        mainActivity.startActivity(intent);

                                    } catch (Exception e) {
                                        ToastView.getInstaller(mainActivity).setText("此APP无法打开").show();
                                    }
                                    break;
                                case 1:
                                    AppUtis.openAppDetails(mainActivity,desktopEntity.getAppPackage());
                                    break;
                                case 2:
                                    AppUtis.uninstallApp(mainActivity, desktopEntity.getAppPackage());
                                    break;
                                case 3:
                                    BaseApplication.recycleBinEntities.add(appGridAdapter.getData(position));
                                    AppUtis.saveRecycleData(mainActivity);
                                    appGridAdapter.getRecycleBin().setAppIcon("满");
                                    BaseApplication.desktopAppEntities.remove(position);
                                    appGridAdapter.notifyDataSetChanged();
                                    AppUtis.saveDesktopData(mainActivity);
                                    break;
                            }
                            menuPopupWindow.dismiss();
                        }
                    });
                    menuPopupWindow.showAsDropDown(view);

                }
                recyclerView.setTag(menuPopupWindow);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(appGridAdapter);
        if (!SavePreference.getBoolean(mainActivity, "icon_is_show_init")) {
            SavePreference.save(mainActivity, "icon_is_show_init", true);
            SavePreference.save(mainActivity, "icon_is_show", true);
        }
        appGridAdapter.clearOrAddOtherSystemIcon(SavePreference.getBoolean(mainActivity, "icon_is_show"));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.DOWN) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        if (recyclerView.getTag() != null) {
                            MenuPopupWindow menuPopupWindow = (MenuPopupWindow) recyclerView.getTag();
                            menuPopupWindow.dismiss();
                        }
                        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                        if (appGridAdapter.getData(toPosition).getAppTitle().equals("回收站")
                                && appGridAdapter.getData(fromPosition).getDesktopType() != DesktopEntity.DesktopType.system) {
                            BaseApplication.recycleBinEntities.add(appGridAdapter.getData(fromPosition));
                            AppUtis.saveRecycleData(mainActivity);
                            appGridAdapter.getRecycleBin().setAppIcon("满");
                            BaseApplication.desktopAppEntities.remove(fromPosition);
                            appGridAdapter.notifyDataSetChanged();
                            AppUtis.saveDesktopData(mainActivity);
                            return false;
                        } else {
                            if (fromPosition < toPosition) {
                                //分别把中间所有的item的位置重新交换
                                for (int i = fromPosition; i < toPosition; i++) {
                                    Collections.swap(BaseApplication.desktopAppEntities, i, i + 1);
                                }
                            } else {
                                for (int i = fromPosition; i > toPosition; i--) {
                                    Collections.swap(BaseApplication.desktopAppEntities, i, i - 1);
                                }
                            }
                            appGridAdapter.notifyItemMoved(fromPosition, toPosition);
                            appGridAdapter.notifyItemRangeChanged(0, appGridAdapter.getItemCount());
                            AppUtis.saveDesktopData(mainActivity);
                            //返回true表示执行拖动
                            return true;
                        }
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    }
                });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public AppGridAdapter getAppGridAdapter(){
        return appGridAdapter;
    }

    public GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }
}

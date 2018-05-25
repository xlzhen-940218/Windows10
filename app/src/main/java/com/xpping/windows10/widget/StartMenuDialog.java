package com.xpping.windows10.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.adapter.AppListAdapter;
import com.xpping.windows10.adapter.AppListPositionAdapter;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.SavePreference;

import java.util.ArrayList;
import java.util.List;

import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/**
 * Created by xlzhen on 9/10 0010.
 * 开始菜单
 */

public class StartMenuDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private AppListAdapter appListAdapter;
    public AppListAdapter.OnAddToDesktopListener onAddToDesktopListener;
    public static boolean isFullScreen;

    public StartMenuDialog(Context context, AppListAdapter.OnAddToDesktopListener onAddToDesktopListener) {
        super(context, R.style.dialogsss);
        if (getWindow() != null)
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_start_menu);
        this.context = context;
        this.onAddToDesktopListener = onAddToDesktopListener;
        changeOrientation(context.getResources().getConfiguration().orientation);
        findViewById(R.id.menu_switch).setOnClickListener(this);
        findViewById(R.id.user_switch).setOnClickListener(this);
        findViewById(R.id.setting_switch).setOnClickListener(this);
        findViewById(R.id.power_switch).setOnClickListener(this);
        findViewById(R.id.explorer_switch).setOnClickListener(this);
        ((TextView) findViewById(R.id.phoneName)).setText(Build.MODEL);

        appListAdapter = new AppListAdapter(context, BaseApplication.appEntities, new AppListAdapter.OnShowGridViewListener() {
            @Override
            public void onShowGridView() {
                findViewById(R.id.appGridView).setVisibility(View.VISIBLE);
                findViewById(R.id.appListView).setVisibility(View.GONE);
            }
        }, onAddToDesktopListener);
    }

    public void changeOrientation(int orientation) {
        final Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.START | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.StartMenuDialogAnimation);
        lp.y = DensityUtils.dp2px(40); // 新位置Y坐标
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setAttributes(lp);

        dialogWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialogWindow.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                dialogWindow.getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (SavePreference.getInt(context, "start_menu_landscape_width") == -1)
                SavePreference.save(context, "start_menu_landscape_width",AppUtis.isPad(getContext()) ? 200 : 150);

            if (SavePreference.getInt(context, "start_menu_landscape_height") == -1)
                SavePreference.save(context, "start_menu_landscape_height",AppUtis.isPad(getContext()) ? 180 : 80);

            dialogWindow.setLayout(DensityUtils.getScreenW(context) - DensityUtils.dp2px(SavePreference.getInt(context, "start_menu_landscape_width"))
                    , DensityUtils.getScreenH(context) - DensityUtils.dp2px(SavePreference.getInt(context, "start_menu_landscape_height")));

            findViewById(R.id.wechat).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));

            ((RelativeLayout.LayoutParams) findViewById(R.id.wechat).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.message);
            if (AppUtis.isPad(getContext())) {
                findViewById(R.id.qq_music).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                ((RelativeLayout.LayoutParams) findViewById(R.id.qq_music).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.wechat);

                findViewById(R.id.meitu).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                ((RelativeLayout.LayoutParams) findViewById(R.id.meitu).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.qq_music);
            }

        } else {
            if (SavePreference.getInt(context, "start_menu_portrait_width") == -1)
                SavePreference.save(context, "start_menu_portrait_width", AppUtis.isPad(getContext()) ? 0 : 5);

            if (SavePreference.getInt(context, "start_menu_portrait_height") == -1)
                SavePreference.save(context, "start_menu_portrait_height",AppUtis.isPad(getContext()) ? 440 : 300);

            dialogWindow.setLayout(DensityUtils.getScreenW(context) - DensityUtils.dp2px(SavePreference.getInt(context, "start_menu_portrait_width"))
                    , DensityUtils.getScreenH(context) - DensityUtils.dp2px(SavePreference.getInt(context, "start_menu_portrait_height")));

            findViewById(R.id.wechat).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            ((RelativeLayout.LayoutParams) findViewById(R.id.wechat).getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.qqmail);

            if (AppUtis.isPad(getContext())) {
                findViewById(R.id.wechat).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));

                ((RelativeLayout.LayoutParams) findViewById(R.id.wechat).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.message);

                findViewById(R.id.qq_music).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                ((RelativeLayout.LayoutParams) findViewById(R.id.qq_music).getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.taobao);
                ((RelativeLayout.LayoutParams) findViewById(R.id.qq_music).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.meitu);
                findViewById(R.id.meitu).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                ((RelativeLayout.LayoutParams) findViewById(R.id.meitu).getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.photo);
            }
        }
        if (isFullScreen) {
            dialogWindow.setLayout(DensityUtils.getScreenW(context), DensityUtils.getScreenH(context) - DensityUtils.dp2px(40));
        }
    }

    public AppListAdapter getAppListAdapter() {
        return appListAdapter;
    }

    public void show(Context context) {
        super.show();
        if (BaseApplication.appEntities != null && ((ListView) findViewById(R.id.appListView)).getAdapter() == null) {

            appListAdapter.setData(BaseApplication.appEntities);
            ((GridView) findViewById(R.id.appGridView)).setAdapter(new AppListPositionAdapter(context
                    , BaseApplication.appListPositions, DensityUtils.dp2px(54), new AppListPositionAdapter.OnItemOnClickListener() {
                @Override
                public void onClick(int position) {
                    ((ListView) findViewById(R.id.appListView)).setSelection(position);
                    findViewById(R.id.appGridView).setVisibility(View.GONE);
                    findViewById(R.id.appListView).setVisibility(View.VISIBLE);
                }
            }));
            ((ListView) findViewById(R.id.appListView)).setAdapter(appListAdapter);
        }

        changeOrientation(context.getResources().getConfiguration().orientation);
    }

    private MenuSelectDialog userMenuDialog, powerMenuDialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_switch://展开所有按钮

                Animation animation = new TranslateAnimation(0, 30, 0, 0);
                animation.setDuration(100);
                animation.setRepeatCount(1);//动画的重复次数
                animation.setRepeatMode(Animation.REVERSE);
                animation.setInterpolator(new DecelerateInterpolator());
                view.startAnimation(animation);//开始动画

                if (view.getTag() == null)
                    view.setTag(false);
                boolean isOpenStartMenu = (boolean) view.getTag();

                findViewById(R.id.menu_layout).setBackgroundColor(Color.parseColor(isOpenStartMenu ? "#00000000" : "#232323"));

                findViewById(R.id.menu_layout).setLayoutParams(new RelativeLayout.LayoutParams(DensityUtils.dp2px(isOpenStartMenu ? 50 : 200)
                        , ViewGroup.LayoutParams.MATCH_PARENT));
                view.setTag(!isOpenStartMenu);
                break;
            case R.id.user_switch://用户账户
                if (userMenuDialog == null) {
                    userMenuDialog = new MenuSelectDialog((Activity) context, (int) view.getX(), DensityUtils.dp2px(210));
                    List<String> menuData = new ArrayList<>();
                    menuData.add("更改账户设置");
                    menuData.add("锁定");
                    userMenuDialog.setMenuData(menuData, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0://更改账户设置
                                    try {
                                        getContext().startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));

                                    } catch (Exception ex) {
                                        ToastView.getInstaller(getContext()).setText("无法打开账户设置，错误信息：" + ex.getMessage()).show();
                                    }

                                    break;
                                case 1://锁定 也就是关闭屏幕
                                    AppUtis.closeScreen((Activity) context);
                                    break;
                            }
                            userMenuDialog.dismiss();
                        }
                    });
                }
                userMenuDialog.show();
                break;
            case R.id.setting_switch://打开设置页
                getContext().startActivity(new Intent(Settings.ACTION_SETTINGS));

                break;
            case R.id.explorer_switch://打开文件管理器
                //ToastView.getInstaller(context).setText("请等待2.0版本哦！").show();
                Intent intent = new Intent(MAIN_BROADCAST);
                intent.putExtra("message", "openFileCategoryFragment");
                context.sendBroadcast(intent);
                dismiss();
                break;
            case R.id.power_switch://关闭电源
                if (powerMenuDialog == null) {
                    powerMenuDialog = new MenuSelectDialog((Activity) context, (int) view.getX(), DensityUtils.dp2px(90));
                    List<String> menuData = new ArrayList<>();
                    menuData.add("睡眠");
                    menuData.add("关机");
                    menuData.add("重启");
                    menuData.add("重启到recovery");
                    menuData.add("重启到fastboot");
                    powerMenuDialog.setMenuData(menuData, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0://睡眠 和锁定一样
                                    AppUtis.closeScreen((Activity) context);
                                    break;
                                case 1://关机
                                    AppUtis.shutDown(context);
                                    break;
                                case 2://重启
                                    AppUtis.reboot(context);
                                    break;
                                case 3://重启到recovery
                                    AppUtis.rebootRecovery(context);
                                    break;
                                case 4://重启到bootloader
                                    AppUtis.rebootBootloader(context);
                                    break;
                            }
                            powerMenuDialog.dismiss();
                        }
                    });
                }
                powerMenuDialog.show();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppUtis.hideBottomUIMenu((Activity) context);
    }
}

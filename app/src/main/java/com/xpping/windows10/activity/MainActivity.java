package com.xpping.windows10.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;

import android.view.View;


import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.TextView;

import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.base.BaseActivity;
import com.xpping.windows10.adapter.AppGridAdapter;
import com.xpping.windows10.adapter.AppListAdapter;
import com.xpping.windows10.adapter.TaskBarAdapter;
import com.xpping.windows10.adapter.base.SimpleViewPagerAdapter;
import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.entity.TaskBarEntity;
import com.xpping.windows10.fragment.AppsCurrencyFragment;
import com.xpping.windows10.fragment.CatHouseFragment;
import com.xpping.windows10.fragment.CatPhotoFragment;
import com.xpping.windows10.fragment.ControlFragment;
import com.xpping.windows10.fragment.EdgeFragment;

import com.xpping.windows10.fragment.FileCategoryFragment;
import com.xpping.windows10.fragment.FileViewFragment;
import com.xpping.windows10.fragment.FlashlightFragment;
import com.xpping.windows10.fragment.NetWorkDeviceFragment;
import com.xpping.windows10.fragment.PCDetailsMessageFragment;
import com.xpping.windows10.fragment.RecycleFragment;

import com.xpping.windows10.fragment.ThanksAboutFragment;
import com.xpping.windows10.fragment.TranslateFragment;
import com.xpping.windows10.fragment.base.BaseFragment;

import com.xpping.windows10.receiver.BatteryChangedReceiver;
import com.xpping.windows10.receiver.InstallReceiver;
import com.xpping.windows10.receiver.MainMessageReceiver;
import com.xpping.windows10.receiver.ScreenListener;
import com.xpping.windows10.receiver.TimeReceiver;
import com.xpping.windows10.receiver.UninstallReceiver;

import com.xpping.windows10.service.ListenerNotificationService;


//import com.xpping.windows10.utils.AdUtils;
import com.xpping.windows10.utils.AppUtis;

import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.DesktopUtils;
import com.xpping.windows10.utils.FragmentUtils;

import com.xpping.windows10.utils.SavePreference;

import com.xpping.windows10.widget.HorizontalListView;
import com.xpping.windows10.widget.OperationCenterDialog;
import com.xpping.windows10.widget.QuickWindowsView;
import com.xpping.windows10.widget.StartMenuDialog;
import com.xpping.windows10.widget.ToastView;
import com.nostra13.universalimageloader.core.assist.ImageSize;


import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*
*win10安卓桌面 主窗体
*/
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public EdgeFragment edgeFragment;
    public RecycleFragment recycleFragment;
    public FileViewFragment fileViewFragment;
    public FileCategoryFragment fileCategoryFragment;
    public NetWorkDeviceFragment netWorkDeviceFragment;
    public TranslateFragment translateFragment;
    public FlashlightFragment flashlightFragment;
    public PCDetailsMessageFragment pcDetailsMessageFragment;
    public CatHouseFragment catHouseFragment;
    public CatPhotoFragment catPhotoFragment;
    public ThanksAboutFragment thanksAboutFragment;
    public ControlFragment controlFragment;

    public AppsCurrencyFragment netEaseCloudMusicFragment, bilibiliFragment, iQiYiFragment, taobaoFragment, qqMusicFragment, qqNewsFragment, qqZoneFragment, tencentVideoFragment, jdFragment, kuGouFragment, qqBookFragment, youkuFragment, weiboFragment, mailFragment, xiuxiuFragment, baiduFragment, mapBaiduFragment, toutiaoFragment, meituanFragment, coolApkFragment, zuoyebangFragment, qqACFragment, tiebaFragment, douyinFragment, alipayFragment, pinduoduoFragment;

    private ViewPager viewPager;
    private SimpleViewPagerAdapter homeDesktopAdapter;
    public RecyclerView recyclerView;
    private QuickWindowsView quickWindowsView;

    private StartMenuDialog startMenuDialog;
    private OperationCenterDialog operationCenterDialog;

    private BatteryChangedReceiver batteryChangedReceiver;
    private TimeReceiver timeReceiver;
    private UninstallReceiver uninstallReceiver;
    private InstallReceiver installReceiver;

    public TaskBarAdapter taskBarAdapter;
    private AppListAdapter.OnAddToDesktopListener onAddToDesktopListener;

   /* //编写 javaScript方法
    String javascript = "javascript:function getClass(parent,sClass){" +
            "var aEle=parent.getElementsByTagName('div');" +
            "var aResult=[];" +
            "var i=0;" +
            "for(i<0;i<aEle.length;i++){" +
            "if(aEle[i].className==sClass){" +
            "aResult.push(aEle[i]);" +
            "}" +
            "};" +
            "return aResult;}" +
            "function getClassId(parent,sClassId){" +
            "var aEle=document.getElementById(sClassId);" +
            "return aEle;}" +
            "function hideOther() {" +
            "getClass(document,'sm-dl')[0].style.display='none';" +
            "getClass(document,'ad-head publicHeadAd')[0].style.display='none';" +
            "getClassId(document,'saveDesk2').style.display='none';" +
            "getClassId(document,'localCarousel').style.display='none';" +
            "getClassId(document,'downloadApp').style.display='none';" +
            "for(var i=0;i<document.getElementsByClassName('days15-weather')[0].children.length;i++){" +
            "document.getElementsByClassName('days15-weather')[0].children[i].style.display='none'}" +
            "document.getElementsByClassName('days15-weather')[0].children[1].style.display='inline';" +
            "for(var i=0;i<document.getElementsByTagName('iframe').length;i++){" +
            "if(document.getElementsByTagName('iframe')[i].src.indexOf('baidu') != -1){" +
            "document.getElementsByTagName('iframe')[i].style.display='none';" +
            "}" +
            "}}";*/

    public static final String MAIN_BROADCAST = "android.intent.action.MAIN_BROADCAST";// 广播跳转意图

    private ScreenListener mScreenListener;

    private MainMessageReceiver messageReceiver;

    private DesktopUtils desktopUtils;

    @Override
    protected void initUI() {
        methodRequiresTwoPermission();
    }


    @Override
    public void setContentView() {
        //必须在setContentView之前调用
        AppUtis.setFullScreen(this);
        setContentView(R.layout.activity_main);
    }

    public static final int RC_CAMERA_AND_LOCATION = 10085;

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WAKE_LOCK
                , Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

            //AdUtils.initAd(this,"1106823939","6070333357494545");
            initWidget();

            //CheckUpdateAsyncTask.implementCheckUpdateAsyncTask(this);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "桌面APP需要读取手机信息，SD权限，" +
                            "定位权限，设置桌面，打开相机权限才能正常运行，请允许哦！",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    private boolean isInit = false;

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        isInit = true;

        //AdUtils.initAd(this,"1106823939","6070333357494545");
        initWidget();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            /*new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用详情页以修改应用权限")
                    .setTitle("必需权限").build().show();*/
            if (isInit)
                return;

            //AdUtils.initAd(this,"1106823939","6070333357494545");


            initWidget();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    private void initInstall() {
        installReceiver = new InstallReceiver(startMenuDialog.getAppListAdapter());
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        this.registerReceiver(installReceiver, filter);
    }

    private void initUninstall() {
        if (onAddToDesktopListener == null)
            onAddToDesktopListener = new AppListAdapter.OnAddToDesktopListener() {
                @Override
                public void onAdd(DesktopEntity desktopEntity) {
                    boolean isOk = true;
                    for (DesktopEntity desktopEntity1 : BaseApplication.desktopAppEntities)
                        if (desktopEntity.getAppPackage().equals(desktopEntity1.getAppPackage())) {
                            isOk = false;
                            ToastView.getInstaller(MainActivity.this).setText("已存在该APP快捷方式在桌面").show();
                        }
                    if (isOk) {
                        BaseApplication.desktopAppEntities.add(desktopEntity);
                        desktopUtils.getAppGridAdapter().notifyDataSetChanged();
                        AppUtis.saveDesktopData(MainActivity.this);
                    }
                }
            };

        if (startMenuDialog == null)
            startMenuDialog = new StartMenuDialog(this, onAddToDesktopListener);

        uninstallReceiver = new UninstallReceiver(desktopUtils.getAppGridAdapter(), startMenuDialog.getAppListAdapter());
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        this.registerReceiver(uninstallReceiver, filter);
    }

    public GridLayoutManager getGridLayoutManager() {
        return desktopUtils.getGridLayoutManager();
    }

    public AppGridAdapter getAppGridAdapter() {
        return desktopUtils.getAppGridAdapter();
    }


    private void initBattery() {
        batteryChangedReceiver = new BatteryChangedReceiver((ImageView) findViewById(R.id.battery));
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryChangedReceiver, filter);
    }

    private void initSignal() {
        //AppUtis.listenerSignal(this, signal);
        AppUtis.listenerWifi(this, (ImageView) findViewById(R.id.wifi));
    }

    private void initClockView() {
        timeReceiver = new TimeReceiver((TextView) findViewById(R.id.clock));
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filter);
    }

    private void initWidget() {
        initViewPager();

        desktopUtils = new DesktopUtils(this);
        initClockView();
        initSignal();
        initBattery();

        initUninstall();
        initInstall();

        mScreenListener = new ScreenListener(this);
        mScreenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onUserPresent() {
                if (SavePreference.getBoolean(MainActivity.this, "lock_is_show"))
                    startActivity(new Intent(MainActivity.this, LockActivity.class));
            }

            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {

            }
        });

        if (!SavePreference.getBoolean(this, "lock_is_show_init")) {
            SavePreference.save(this, "lock_is_show_init", true);
            SavePreference.save(this, "lock_is_show", true);
        }

        openNotificationService();
        HorizontalListView horizontalListView = findViewById(R.id.horizontalListView);

        List<TaskBarEntity> taskBarEntities = new ArrayList<>();
        taskBarEntities.add(new TaskBarEntity("" + R.mipmap.cortana, "微软小娜", !SavePreference.getBoolean(this, "voiceAI")));
        taskBarEntities.add(new TaskBarEntity("" + R.drawable.task_view, "任务视图", !SavePreference.getBoolean(this, "Recently")));
        taskBarEntities.add(new TaskBarEntity("" + R.mipmap.app_store, "应用商店", true));
        FragmentUtils.initFragmentList(this);
        taskBarEntities = initTaskBarEntitesList(taskBarEntities, getFragmentList());

        taskBarAdapter = new TaskBarAdapter(this, taskBarEntities);

        horizontalListView.setAdapter(taskBarAdapter);
        horizontalListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TaskBarEntity bean = (TaskBarEntity) view.getTag();
                ToastView.getInstaller(MainActivity.this).setText(bean.getName()).show();
                return true;
            }
        });
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskBarEntity bean = (TaskBarEntity) view.getTag();
                if (bean.getBaseFragment() != null) {
                    openWindowFragment(bean.getBaseFragment());
                    return;
                }
                switch (bean.getName()) {
                    case "微软小娜":
                        openVoiceAI(view);
                        break;
                    case "任务视图":
                        bean.setOpen(openAllTask());
                        taskBarAdapter.notifyDataSetChanged();
                        break;
                    case "应用商店":
                        openAppStore();
                        break;
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MAIN_BROADCAST);
        messageReceiver = new MainMessageReceiver(new MainMessageReceiver.CallBack() {
            @Override
            public void openReceiverFragment(BaseFragment baseFragment) {
                openWindowFragment(baseFragment);
            }

            @Override
            public void openCatPhotoFragment(Intent intent) {
                if (intent.getStringExtra("picList") != null) {
                    catPhotoFragment.setPicList(intent.getStringExtra("picList"));
                    catPhotoFragment.setPicPosition(intent.getIntExtra("picPosition", 0));
                }
                openWindowFragment(catPhotoFragment);
            }

            @Override
            public void updateCatPhotoFragment(Intent intent) {
                catPhotoFragment.refreshPictureList(intent.getStringExtra("data"));
            }

            @Override
            public void nextPage() {
                catHouseFragment.nextPage(true);
            }

            @Override
            public void refreshWallpaper() {
                if (findViewById(R.id.wallpaperBackground) != null)
                    BaseApplication.imageLoader.displayImage("file://"
                                    + SavePreference.getStr(MainActivity.this, "wallpaper")
                            , (ImageView) findViewById(R.id.wallpaperBackground)
                            , new ImageSize(DensityUtils.getScreenW(MainActivity.this)
                                    , DensityUtils.getScreenH(MainActivity.this)));
            }

            @Override
            public void closeStartMenuDialog() {
                startMenuDialog.dismiss();
            }

            @Override
            public void openAppStore() {
                AppUtis.openAppStore(MainActivity.this);
            }

            @Override
            public void openCamera() {
                AppUtis.openPackageListApp(MainActivity.this, new String[]{"com.android.camera"
                        , "com.sec.android.app.camera", "com.huawei.camera", "com.meizu.media.camera"
                        ,}, new AppUtis.CallBack() {
                    @Override
                    public void allFailed(Activity activity) {
                        ToastView.getInstaller(activity).setText("无法打开相机").show();
                    }
                });
            }
        }, getFragmentList());
        registerReceiver(messageReceiver, intentFilter);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);
        List<String> viewPagerStrings = new ArrayList<>();
        viewPagerStrings.add("quick");
        viewPagerStrings.add("desktop");
        recyclerView = new RecyclerView(this);
        recyclerView.setPadding(DensityUtils.dp2px(10), DensityUtils.dp2px(10)
                , DensityUtils.dp2px(10), DensityUtils.dp2px(10));
        quickWindowsView = new QuickWindowsView(this);
        homeDesktopAdapter=new SimpleViewPagerAdapter<String>(this,viewPagerStrings) {
            @Override
            protected View getView(View convertView, int position, String bean) {
                switch (bean){
                    case "quick":
                        convertView=quickWindowsView;
                        break;
                    case "desktop":
                        convertView=recyclerView;
                        break;
                }
                return convertView;
            }
        };
        viewPager.setAdapter(homeDesktopAdapter);
        viewPager.setCurrentItem(1);
    }

    private BaseFragment[] getFragmentList() {
        return new BaseFragment[]{edgeFragment, recycleFragment, fileViewFragment
                , fileCategoryFragment, netWorkDeviceFragment, translateFragment, flashlightFragment
                , pcDetailsMessageFragment, catHouseFragment, catPhotoFragment, thanksAboutFragment
                , controlFragment, netEaseCloudMusicFragment, bilibiliFragment, iQiYiFragment, taobaoFragment
                , qqMusicFragment, qqNewsFragment, qqZoneFragment
                , tencentVideoFragment, jdFragment, kuGouFragment, qqBookFragment, youkuFragment, weiboFragment
                , mailFragment, xiuxiuFragment, baiduFragment, mapBaiduFragment, toutiaoFragment
                , meituanFragment, coolApkFragment, zuoyebangFragment, qqACFragment, tiebaFragment
                , douyinFragment, alipayFragment, pinduoduoFragment};
    }

    private List<TaskBarEntity> initTaskBarEntitesList(List<TaskBarEntity> taskBarEntities, BaseFragment... baseFragments) {

        for (BaseFragment baseFragment : baseFragments)
            taskBarEntities.add(new TaskBarEntity(baseFragment));

        return taskBarEntities;
    }

    private void openNotificationService() {
        if (operationCenterDialog == null)
            operationCenterDialog = new OperationCenterDialog(this);

        //toggleNotificationListenerService();

        Intent intent = new Intent(this, ListenerNotificationService.class);
        startService(intent);
        toggleNotificationListenerService();
    }

    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, ListenerNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, ListenerNotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }


    private void openAppStore() {
        AppUtis.openXXIONGAppStore(this);
    }


    //打开语音助手
    public void openVoiceAI(View view) {
        AppUtis.openVoiceAI(this);
    }

    //打开多任务栏
    public boolean openAllTask() {
        return AppUtis.showRecentlyApp(this);
    }

    //打开Window窗口
    public void openWindowFragment(BaseFragment fragment) {
        if (fragment == null) {
            methodRequiresTwoPermission();//如果fragment为空，则Activity初始化
            ToastView.getInstaller(this).setText("页面数据异常，重置当前页面...").show();
            return;
        }
        TaskBarEntity bean = taskBarAdapter.getFragmentId(fragment.getLayoutId());
        if (!bean.isOpen()) {
            FragmentUtils.openFragment(MainActivity.this, fragment);
            bean.setOpen(true);
            bean.setShow(true);
            taskBarAdapter.notifyDataSetChanged();
            return;
        }

        if (!bean.isShow()) { //如果不在显示状态，则显示fragment
            AppUtis.changeFragmentFocus(this, fragment.getLayoutId());
            FragmentUtils.showFragment(fragment);
            bean.setShow(!bean.isShow());
        } else { //如果在显示状态，则关闭fragment
            if (fragment.getLayoutId() == AppUtis.topZViewId) {
                FragmentUtils.dismissFragment(fragment);
                bean.setShow(false);
            } else {
                AppUtis.changeFragmentFocus(this, fragment.getLayoutId());
                bean.setShow(true);
            }
        }
        taskBarAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (quickWindowsView != null && quickWindowsView.getDoubanWebView() != null) {
            quickWindowsView.getDoubanWebView().destroy();
            quickWindowsView.setDoubanWebView(null);
        }

        try {
            unregisterReceiver(uninstallReceiver);
            unregisterReceiver(installReceiver);
            unregisterReceiver(timeReceiver);
            unregisterReceiver(batteryChangedReceiver);
            unregisterReceiver(messageReceiver);
            FragmentUtils.closeFragment(getFragmentList());
            mScreenListener.unregisterListener();
        } catch (Exception ex) {
            //防止注销广播失败
        }

    }

    public void openClock(View view) {
        AppUtis.openClock(this);
    }

    public void openStartMenu(View view) {
        if (startMenuDialog == null)
            startMenuDialog = new StartMenuDialog(this, onAddToDesktopListener);
        try {
            startMenuDialog.show(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void openOperationCenter(View view) {

        operationCenterDialog.show();
    }

    public void openSignal(View view) {
        AppUtis.openSignal(this);
    }

    public void openWifi(View view) {
        AppUtis.openWifi(this);
    }

    public void openBattery(View view) {
        AppUtis.openBattery(this);
    }

    //重写旋转时方法，不销毁activity
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (startMenuDialog == null)
            return;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            desktopUtils.getGridLayoutManager()
                    .setSpanCount(SavePreference.getInt(this, AppUtis.isPad(this) ? "icon_size_ver_pad" : "icon_size_ver"));
            startMenuDialog.changeOrientation(getResources().getConfiguration().orientation);

            //如果有设置过壁纸，则设置用户设置过的壁纸
            if (TextUtils.isEmpty(SavePreference.getStr(this, "wallpaper"))) {
                BaseApplication.imageLoader.displayImage("drawable://" + R.mipmap.background_landscape
                        , (ImageView) findViewById(R.id.wallpaperBackground)
                        , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));
            } else {
                BaseApplication.imageLoader.displayImage("file://" + SavePreference.getStr(this, "wallpaper")
                        , (ImageView) findViewById(R.id.wallpaperBackground)
                        , new ImageSize(DensityUtils.getScreenW(this), DensityUtils.getScreenH(this)));

            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            desktopUtils.getGridLayoutManager()
                    .setSpanCount(SavePreference.getInt(this, AppUtis.isPad(this) ? "icon_size_pad" : "icon_size"));

            startMenuDialog.changeOrientation(getResources().getConfiguration().orientation);
            AppUtis.setWallpaperBackground(this);
        }

        if (operationCenterDialog != null)
            operationCenterDialog.changeOrientation();

        if(quickWindowsView!=null)
            quickWindowsView.changeOrientation();


        setWindowsMax(getFragmentList());
        findViewById(R.id.wallpaperBackground).invalidate();
    }

    private void setWindowsMax(BaseFragment... fragments) {
        for (BaseFragment baseFragment : fragments)
            baseFragment.setWindowMax(baseFragment.getWindowMenuClickListener().onMax(true, baseFragment));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (quickWindowsView.getDoubanWebView() != null && quickWindowsView.getDoubanWebView().canGoBack()) {
                quickWindowsView.getDoubanWebView().goBack();
            }

            keyBack(getFragmentList());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void keyBack(BaseFragment... baseFragments) {
        for (BaseFragment baseFragment : baseFragments) {
            if (baseFragment == null)
                return;

            if (baseFragment.onBackKey() && baseFragment.isVisible()) {
                FragmentUtils.closeFragment(baseFragment);
                taskBarAdapter.remove(taskBarAdapter.getFragmentId(baseFragment.getLayoutId()));
            }
        }
    }


    @Override
    public void onClick(View v) {

    }

    private ActionMode mActionMode;

    public void setActionMode(ActionMode actionMode) {
        mActionMode = actionMode;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public Fragment getFileViewFragment() {
        return fileViewFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ControlFragment.IMAGE_1_REQUEST_CODE
                || requestCode == ControlFragment.IMAGE_2_REQUEST_CODE
                || requestCode == ControlFragment.IMAGE_3_REQUEST_CODE) {
            controlFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (quickWindowsView != null && quickWindowsView.getDoubanWebView() != null)
            quickWindowsView.getDoubanWebView().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (quickWindowsView != null && quickWindowsView.getDoubanWebView() != null)
            quickWindowsView.getDoubanWebView().onResume();

        if (operationCenterDialog != null)
            operationCenterDialog.listenerNotificationIsOpen();

        if (findViewById(R.id.wallpaperBackground) != null)
            AppUtis.setWallpaperBackground(this);
    }


}

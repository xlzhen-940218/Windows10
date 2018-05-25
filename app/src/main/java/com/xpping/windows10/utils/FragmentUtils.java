package com.xpping.windows10.utils;


import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
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
import com.xpping.windows10.touchlistener.FragmentTouchListener;
import com.xpping.windows10.widget.ToastView;

/**
 * Created by xlzhen on 9/7 0007.
 * fragment 工具
 */

public class FragmentUtils {
    private static FragmentManager fm;
    //fragment的窗口化在竖屏状态时，顶部的间距为160DP
    private static int windowTopPortraitMargin = DensityUtils.dp2px(160);
    //fragment的窗口化在横屏状态时，右边的间距为200DP
    private static int windowRightLandscapeMargin = DensityUtils.dp2px(200);
    //fragment的窗口化在竖屏状态时，右边的间距为80DP
    private static int windowRightPortraitMargin = DensityUtils.dp2px(80);
    
    public static void initFragmentList(MainActivity mainActivity){
        windowTopPortraitMargin = DensityUtils.dp2px(AppUtis.isPad(mainActivity) ? 500 : 200);
        windowRightLandscapeMargin = DensityUtils.dp2px(AppUtis.isPad(mainActivity) ? 600 : 200);
        windowRightPortraitMargin = DensityUtils.dp2px(AppUtis.isPad(mainActivity) ? 260 : 40);
        
        mainActivity.edgeFragment = (EdgeFragment) initFragment(mainActivity,new EdgeFragment(), "Microsoft Edge"
                , R.mipmap.edge_white, R.id.fragment1, Color.parseColor("#0078d7")
                , BaseFragment.Theme.white, "");

        mainActivity.recycleFragment = (RecycleFragment) initFragment(mainActivity,new RecycleFragment(), "回收站"
                , R.mipmap.recyclestationnull, R.id.fragment2, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.fileViewFragment = (FileViewFragment) initFragment(mainActivity,new FileViewFragment(), "我的电脑"
                , R.mipmap.ic_launcher, R.id.fragment3, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.fileCategoryFragment = (FileCategoryFragment) initFragment(mainActivity,new FileCategoryFragment()
                , "文件资源管理器", R.mipmap.user_folder, R.id.fragment4, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.netWorkDeviceFragment = (NetWorkDeviceFragment) initFragment(mainActivity,new NetWorkDeviceFragment(), "网络"
                , R.mipmap.internet, R.id.fragment5, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.translateFragment = (TranslateFragment) initFragment(mainActivity,new TranslateFragment(), "翻译"
                , R.mipmap.translate, R.id.fragment6, Color.parseColor("#7463ff")
                , BaseFragment.Theme.white, "");

        mainActivity.flashlightFragment = (FlashlightFragment) initFragment(mainActivity,new FlashlightFragment(), "手电筒"
                , R.mipmap.flashlight, R.id.fragment7, Color.parseColor("#0078d7")
                , BaseFragment.Theme.white, "");

        mainActivity.pcDetailsMessageFragment = (PCDetailsMessageFragment) initFragment(mainActivity,new PCDetailsMessageFragment(), "系统信息"
                , R.mipmap.pc_details_icon, R.id.fragment8, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.catHouseFragment = (CatHouseFragment) initFragment(mainActivity,new CatHouseFragment(), "美猫"
                , R.mipmap.cat_logo, R.id.fragment9, Color.parseColor("#111111")
                , BaseFragment.Theme.white, "");

        mainActivity.catPhotoFragment = (CatPhotoFragment) initFragment(mainActivity,new CatPhotoFragment(), "美猫-看图"
                , R.mipmap.cat_logo, R.id.fragment10, Color.parseColor("#111111")
                , BaseFragment.Theme.white, "");

        mainActivity.thanksAboutFragment = (ThanksAboutFragment) initFragment(mainActivity,new ThanksAboutFragment(), "鸣谢"
                , R.mipmap.help, R.id.fragment11, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");
        mainActivity.controlFragment = (ControlFragment) initFragment(mainActivity,new ControlFragment(), "控制面板"
                , R.mipmap.control_panel, R.id.fragment12, Color.parseColor("#cccccc")
                , BaseFragment.Theme.black, "");

        mainActivity.netEaseCloudMusicFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "网易云音乐"
                , R.mipmap.netease_cloud_music, R.id.fragment13, Color.parseColor("#e20000")
                , BaseFragment.Theme.white, "http://music.163.com/");
        mainActivity.bilibiliFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "哔哩哔哩动画"
                , R.mipmap.bilibili, R.id.fragment14, Color.parseColor("#d76789")
                , BaseFragment.Theme.white, "http://m.bilibili.com/");
        mainActivity.iQiYiFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "爱奇艺"
                , R.mipmap.iqiyi, R.id.fragment15, Color.parseColor("#0bbe06")
                , BaseFragment.Theme.white, "http://m.iqiyi.com/");
        mainActivity.taobaoFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "手机淘宝"
                , R.mipmap.taobao, R.id.fragment16, Color.parseColor("#ff5000")
                , BaseFragment.Theme.white, "http://m.taobao.com/");
        mainActivity.qqMusicFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "QQ音乐"
                , R.mipmap.qqmusic, R.id.fragment17, Color.parseColor("#0eae52")
                , BaseFragment.Theme.white, "https://m.y.qq.com/");
        mainActivity.qqNewsFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "腾讯新闻"
                , R.mipmap.qqnews, R.id.fragment18, Color.parseColor("#35b2e2")
                , BaseFragment.Theme.white, "http://news.qq.com/");
        mainActivity.qqZoneFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "QQ空间"
                , R.mipmap.qqzone, R.id.fragment19, Color.parseColor("#ff9e02")
                , BaseFragment.Theme.white, "https://qzone.qq.com/");
        mainActivity.tencentVideoFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "腾讯视频"
                , R.mipmap.tencent_video, R.id.fragment20, Color.parseColor("#ffffff")
                , BaseFragment.Theme.black, "https://v.qq.com/");
        mainActivity.jdFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "京东"
                , R.mipmap.jd, R.id.fragment21, Color.parseColor("#ee2a17")
                , BaseFragment.Theme.white, "https://www.jd.com/");
        mainActivity.kuGouFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "酷狗音乐"
                , R.mipmap.kugou, R.id.fragment22, Color.parseColor("#1d82fe")
                , BaseFragment.Theme.white, "http://www.kugou.com/");
        mainActivity.qqBookFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "QQ阅读"
                , R.mipmap.qqbook, R.id.fragment23, Color.parseColor("#ffffff")
                , BaseFragment.Theme.black, "http://ubook.qq.com");
        mainActivity.youkuFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "优酷视频"
                , R.mipmap.youku, R.id.fragment24, Color.parseColor("#2495ff")
                , BaseFragment.Theme.white, "http://www.youku.com/");
        mainActivity.weiboFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "微博"
                , R.mipmap.weibo, R.id.fragment25, Color.parseColor("#ffd647")
                , BaseFragment.Theme.white, "https://weibo.com/");
        mainActivity.mailFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "QQ邮箱"
                , R.mipmap.qqmail, R.id.fragment26, Color.parseColor("#f59733")
                , BaseFragment.Theme.white, "https://mail.qq.com/");
        mainActivity.xiuxiuFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "美图秀秀"
                , R.mipmap.meitu, R.id.fragment27, Color.parseColor("#ff0c65")
                , BaseFragment.Theme.white, "http://xiuxiu.web.meitu.com/");
        mainActivity.baiduFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "手机百度"
                , R.mipmap.baidu, R.id.fragment28, Color.parseColor("#3073ff")
                , BaseFragment.Theme.white, "https://www.baidu.com/");
        mainActivity.mapBaiduFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "百度地图"
                , R.mipmap.map_baidu, R.id.fragment29, Color.parseColor("#f74239")
                , BaseFragment.Theme.white, "https://map.baidu.com/");
        mainActivity.toutiaoFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "今日头条"
                , R.mipmap.toutiao, R.id.fragment30, Color.parseColor("#ff0000")
                , BaseFragment.Theme.white, "https://m.toutiao.com/?W2atIF=1");
        mainActivity.meituanFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "美团"
                , R.mipmap.meituan, R.id.fragment31, Color.parseColor("#16dcc5")
                , BaseFragment.Theme.white, "http://i.meituan.com/");
        mainActivity.coolApkFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "酷安"
                , R.mipmap.coolapk, R.id.fragment32, Color.parseColor("#4caf50")
                , BaseFragment.Theme.white, "https://www.coolapk.com");
        mainActivity.zuoyebangFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "作业帮"
                , R.mipmap.zuoyebang, R.id.fragment33, Color.parseColor("#1fa0fe")
                , BaseFragment.Theme.white, "http://zhibo.zuoyebang.com");
        mainActivity.qqACFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "腾讯动漫"
                , R.mipmap.tencent_dongman, R.id.fragment34, Color.parseColor("#ff7c48")
                , BaseFragment.Theme.white, "http://ac.qq.com/");
        mainActivity.tiebaFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "贴吧"
                , R.mipmap.tieba, R.id.fragment35, Color.parseColor("#3986fb")
                , BaseFragment.Theme.white, "https://tieba.baidu.com");
        mainActivity.douyinFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "抖音短视频"
                , R.mipmap.douyin, R.id.fragment36, Color.parseColor("#120c18")
                , BaseFragment.Theme.white, "https://www.iesdouyin.com/share/video/6543136194392231176/?region=CN&mid=6528210152431684356&titleType=title&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=32545528699&timestamp=1526470845");
        mainActivity.alipayFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "支付宝"
                , R.mipmap.alipay, R.id.fragment37, Color.parseColor("#00aaef")
                , BaseFragment.Theme.white, "https://www.alipay.com/");
        mainActivity.pinduoduoFragment = (AppsCurrencyFragment) initFragment(mainActivity,new AppsCurrencyFragment(), "拼多多"
                , R.mipmap.pinduoduo, R.id.fragment38, Color.parseColor("#f40008")
                , BaseFragment.Theme.white, "https://m.pinduoduo.com/");
    }

    private static BaseFragment initFragment(final MainActivity activity, BaseFragment baseFragment, String name, int icon, int layoutRes, int themeColor, BaseFragment.Theme theme, String url) {

        if (baseFragment instanceof AppsCurrencyFragment)
            ((AppsCurrencyFragment) baseFragment).setUrl(url);

        //设置fragment的name，方便后续从fragment取出，每个fragment指定唯一的name
        baseFragment.setName(name);
        //设置fragment的icon，方便后续从fragment取出，每个fragment指定唯一的icon
        baseFragment.setIcon(icon);
        //设置layoutId，方便后续从fragment取出，每个fragment指定唯一的layoutId
        baseFragment.setLayoutId(layoutRes);
        //设置fragment的窗口色
        baseFragment.setThemeColor(themeColor);
        //设置窗口的文字，图标颜色
        baseFragment.setTheme(theme);
        //设置触摸事件，让每个fragment都能来回拖动
        final FragmentTouchListener fragmentTouchListener = new FragmentTouchListener(activity, baseFragment.getLayoutId());
        activity.findViewById(baseFragment.getLayoutId()).setOnTouchListener(fragmentTouchListener);
        //设置最小化，最大化/窗口化，关闭 按钮事件
        baseFragment.setWindowMenuClickListener(new BaseFragment.WindowMenuClickListener() {
            @Override
            public void onMin(View view, BaseFragment baseFragment) {
                TaskBarEntity bean = activity.taskBarAdapter.getFragmentId(baseFragment.getLayoutId());
                if (!bean.isShow()) //如果不在显示状态，则显示fragment
                    FragmentUtils.showFragment(baseFragment);
                else //如果在显示状态，则关闭fragment
                    FragmentUtils.dismissFragment(baseFragment);


                bean.setShow(!bean.isShow());

                activity.taskBarAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onMax(boolean isWindowsMax, BaseFragment baseFragment) {
                if (!baseFragment.isVisible()) {
                    return !isWindowsMax;
                }
                Configuration configuration = baseFragment.getResources().getConfiguration();//判断当前的横竖屏状态

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) baseFragment.getActivity().findViewById(baseFragment.getLayoutId())
                        .getLayoutParams();
                layoutParams.leftMargin = isWindowsMax ? 0 : 10;
                layoutParams.topMargin = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? (isWindowsMax ? 0 : AppUtis.isPad(activity) ? DensityUtils.dp2px(220) : 10) : (isWindowsMax ? 0 : windowTopPortraitMargin);

                layoutParams.rightMargin = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? (isWindowsMax ? 0 : windowRightLandscapeMargin) : (isWindowsMax ? 0 : windowRightPortraitMargin);
                layoutParams.bottomMargin = isWindowsMax ? 0 : 10;

                baseFragment.getActivity().findViewById(baseFragment.getLayoutId()).setLayoutParams(layoutParams);


                fragmentTouchListener.setFragmentIsTouch(!isWindowsMax);
                isWindowsMax = !isWindowsMax;
                return isWindowsMax;
            }

            @Override
            public void onClose(View view, BaseFragment baseFragment) {
                FragmentUtils.closeFragment(baseFragment);
                activity.taskBarAdapter.remove(activity.taskBarAdapter.getFragmentId(baseFragment.getLayoutId()));
            }
        });

        return baseFragment;
    }

    public static void openFragment(MainActivity activity, Fragment fragment) {
        AppUtis.changeFragmentFocus(activity,((BaseFragment)fragment).getLayoutId());//更改fragment焦点
        fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(((BaseFragment)fragment).getLayoutId(), fragment);
        try {
            ft.commit();
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public static void dismissFragment(Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(fragment);
        try {
            transaction.commit();
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public static void showFragment(Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(fragment);
        try {
            transaction.commit();
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public static void closeFragment(Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        try {
            transaction.commit();
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public static void closeFragment(Fragment... fragments) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : fragments)
            transaction.remove(fragment);
        try {
            transaction.commit();
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }
}

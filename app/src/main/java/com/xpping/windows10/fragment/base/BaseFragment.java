package com.xpping.windows10.fragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.Windows10Loger;
import com.xpping.windows10.widget.ToastView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by xlzhen on 12/28 0028.
 * fragment 基类
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private View fragmentView;
    private boolean isWindowMax;//窗口是否是全屏模式

    private int layoutId,icon;
    private WindowMenuClickListener windowMenuClickListener;
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setContentView(inflater, container, savedInstanceState);



        if(fragmentView!=null&&fragmentView instanceof RelativeLayout) {

            View menuView=View.inflate(getActivity(), R.layout.windows_menu_layout,null);

            menuView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));

            ((RelativeLayout) fragmentView).addView(menuView);

            menuView.findViewById(R.id.menu_min).setOnClickListener(this);
            menuView.findViewById(R.id.menu_max).setOnClickListener(this);
            menuView.findViewById(R.id.menu_close).setOnClickListener(this);
            setActionBarTitle(name);
            setActionBarIcon(icon);
            setActionBarTheme(theme,themeColor);
        }

        initData();
        initWidget();

        return fragmentView;
    }

    private void setActionBarTheme(Theme theme, int themeColor) {
        ((TextView) findViewById(R.id.window_title)).setTextColor(theme==Theme.white? Color.WHITE:Color.BLACK);
        BaseApplication.imageLoader.displayImage("drawable://"+(theme==Theme.white?R.mipmap.min_window_white:R.mipmap.min_window)
                ,(ImageView)findViewById(R.id.menu_min),new ImageSize(DensityUtils.dp2px(12),DensityUtils.dp2px(12)));

        BaseApplication.imageLoader.displayImage("drawable://"+(theme==Theme.white?R.mipmap.max_window_white:R.mipmap.max_window)
                ,(ImageView)findViewById(R.id.menu_max),new ImageSize(DensityUtils.dp2px(12),DensityUtils.dp2px(12)));

        BaseApplication.imageLoader.displayImage("drawable://"+(theme==Theme.white?R.mipmap.close_window_white:R.mipmap.close_window)
                ,(ImageView)findViewById(R.id.menu_close),new ImageSize(DensityUtils.dp2px(12),DensityUtils.dp2px(12)));

        findViewById(R.id.windows_action_bar).setBackgroundColor(themeColor);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onCreate");
    }

    protected abstract void initData();

    protected abstract void initWidget();

    protected abstract View setContentView(LayoutInflater inflater,
                                         ViewGroup container, Bundle bundle);

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id, boolean clickAble) {
        if(fragmentView==null)
            return null;

        T views = (T) fragmentView.findViewById(id);
        if (clickAble)
            views.setOnClickListener(this);
        return views;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        return findViewById(id,false);
    }

    protected void showToast(String text) {
        if (TextUtils.isEmpty(text))
            return;

        ToastView.getInstaller(getActivity()).setText(text).show();
    }

    protected void setActionBarTitle(String title) {
        ((TextView) findViewById(R.id.window_title)).setText(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Windows10Loger.debug(this.getClass().getSimpleName()+"........onStop");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_min://最小化
                if(windowMenuClickListener!=null)
                    windowMenuClickListener.onMin(view,this);
                break;
            case R.id.menu_max://最大化
                if(windowMenuClickListener!=null)
                    isWindowMax=windowMenuClickListener.onMax(isWindowMax,this);

                switch (theme){
                    case white:
                        BaseApplication.imageLoader.displayImage("drawable://"+(isWindowMax ? R.mipmap.max_window_2_white : R.mipmap.max_window_white)
                                ,(ImageView)view,new ImageSize(DensityUtils.dp2px(12),DensityUtils.dp2px(12)));

                        break;
                    case black:
                        BaseApplication.imageLoader.displayImage("drawable://"+(isWindowMax ? R.mipmap.max_window_2 : R.mipmap.max_window)
                                ,(ImageView)view,new ImageSize(DensityUtils.dp2px(12),DensityUtils.dp2px(12)));

                        break;
                }
                break;
            case R.id.menu_close://关闭
                if(windowMenuClickListener!=null)
                    windowMenuClickListener.onClose(view,this);
                break;
            default:
                onClick(view,view.getId());
                break;
        }
    }

    protected abstract void onClick(View view,int viewId);

    public static <T extends BaseFragment> T newInstance(T fragment,String key,String[] args){
        Bundle bundle = new Bundle();
        bundle.putStringArray(key, args);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void setWindowMenuClickListener(WindowMenuClickListener windowMenuClickListener) {
        this.windowMenuClickListener = windowMenuClickListener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public void setActionBarIcon(int actionBarIcon) {
        BaseApplication.imageLoader.displayImage("drawable://"+actionBarIcon
                ,(ImageView)findViewById(R.id.window_icon),new ImageSize(DensityUtils.dp2px(19),DensityUtils.dp2px(19)));
    }

    public interface WindowMenuClickListener{
        void onMin(View view,BaseFragment baseFragment);
        boolean onMax(boolean isWindowsMax,BaseFragment baseFragment);
        void onClose(View view,BaseFragment baseFragment);
    }

    public abstract boolean onBackKey();

    public int getLayoutId(){
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;

    }

    public View getFragmentView(){
        return fragmentView;
    }

    public View setFragmentView(View fragmentView){
        this.fragmentView=fragmentView;
        return fragmentView;
    }

    public boolean isWindowMax() {
        return isWindowMax;
    }

    public void setWindowMax(boolean windowMax) {
        isWindowMax = windowMax;
    }

    public WindowMenuClickListener getWindowMenuClickListener() {
        return windowMenuClickListener;
    }

    private Theme theme;
    private int themeColor;

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public enum Theme{
        white,black
    }
}

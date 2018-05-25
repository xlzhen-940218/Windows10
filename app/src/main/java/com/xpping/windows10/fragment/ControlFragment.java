package com.xpping.windows10.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.albums.AlbumActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.SavePreference;
import com.xpping.windows10.widget.ToastView;

import static android.app.Activity.RESULT_OK;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/*
*控制面板
*/
public class ControlFragment extends BaseFragment {
    private Switch desktop_icon_switch, lock_show_switch, statusBarSwitch,showFirstAdd,orientationChange
            ,enableTablet;
    private SeekBar desktop_icon_size_hor_seekbar,desktop_icon_size_ver_seekbar,start_menu_width_seekBar,start_menu_height_seekBar;

    private ImageView wallpaperBackground, lock_lan_Background, lock_pro_Background;

    private ImageSize imageSizeLan, imageSizePro;

    private TextView startMenuDemo;

    @Override
    protected void initData() {
        if (getActivity() != null) {
            imageSizeLan = new ImageSize(DensityUtils.getScreenW(getActivity()), DensityUtils.dp2px(300));
            imageSizePro = new ImageSize(DensityUtils.getScreenW(getActivity()), DensityUtils.dp2px(180));
        }

        wallpaperBackground = findViewById(R.id.wallpaperBackground);
        lock_lan_Background = findViewById(R.id.lock_lan_Background);
        lock_pro_Background = findViewById(R.id.lock_pro_Background);

        wallpaperBackground.setOnClickListener(this);
        lock_lan_Background.setOnClickListener(this);
        lock_pro_Background.setOnClickListener(this);
    }

    @Override
    protected void initWidget() {
        enableTablet=findViewById(R.id.enableTablet);
        enableTablet.setChecked(SavePreference.getBoolean(getContext(),"enableTablet"));
        orientationChange=findViewById(R.id.orientationChange);
        start_menu_width_seekBar=findViewById(R.id.start_menu_width_seekBar);
        start_menu_height_seekBar=findViewById(R.id.start_menu_height_seekBar);

        startMenuDemo=findViewById(R.id.startMenuDemo);
        startMenuDemo.setLayoutParams(new RelativeLayout.LayoutParams(
                (DensityUtils.getScreenW(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_portrait_width"))
                , (DensityUtils.getScreenH(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_portrait_height"))));
        start_menu_width_seekBar.setMax(50);
        start_menu_width_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_portrait_width"));

        start_menu_height_seekBar.setMax(500);
        start_menu_height_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_portrait_height"));

        statusBarSwitch = findViewById(R.id.statusBarSwitch);
        statusBarSwitch.setChecked(SavePreference.getBoolean(getContext(), "statusBar_is_show"));

        desktop_icon_size_hor_seekbar = findViewById(R.id.desktop_icon_size_hor_seekbar);
        desktop_icon_size_hor_seekbar.setMax(AppUtis.isPad(getContext()) ? 15 : 10);

        desktop_icon_size_hor_seekbar.setProgress(AppUtis.isPad(getContext())
                ?SavePreference.getInt(getContext(), "icon_size_pad"):SavePreference.getInt(getContext(), "icon_size"));

        desktop_icon_size_ver_seekbar=findViewById(R.id.desktop_icon_size_ver_seekbar);

        desktop_icon_size_ver_seekbar.setMax(AppUtis.isPad(getContext()) ? 15 : 10);
        desktop_icon_size_ver_seekbar.setProgress(AppUtis.isPad(getContext())
                ?SavePreference.getInt(getContext(), "icon_size_ver_pad"):SavePreference.getInt(getContext(), "icon_size_ver"));

        desktop_icon_switch = findViewById(R.id.desktop_icon_switch);

        desktop_icon_switch.setChecked(SavePreference.getBoolean(getContext(), "icon_is_show"));

        lock_show_switch = findViewById(R.id.lock_show_switch);
        lock_show_switch.setChecked(SavePreference.getBoolean(getContext(), "lock_is_show"));

        showFirstAdd=findViewById(R.id.showFirstAdd);
        showFirstAdd.setChecked(SavePreference.getBoolean(getContext(),"first_add_show"));

        String path = SavePreference.getStr(getContext(), "wallpaper");
        BaseApplication.imageLoader.displayImage(TextUtils.isEmpty(path)
                        ? "drawable://" + R.mipmap.background_landscape : "file://" + path, wallpaperBackground
                , getContext().getResources().getConfiguration()
                        .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);


        String lock_pro_path = SavePreference.getStr(getContext(), "lock_pro_path");
        BaseApplication.imageLoader.displayImage(TextUtils.isEmpty(lock_pro_path)
                        ? "drawable://" + R.mipmap.lock_port_bg : "file://" + lock_pro_path, lock_pro_Background
                , getContext().getResources().getConfiguration()
                        .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);

        String lock_lan_path = SavePreference.getStr(getContext(), "lock_lan_path");
        BaseApplication.imageLoader.displayImage(TextUtils.isEmpty(lock_lan_path)
                        ? "drawable://" + R.mipmap.lock_landscape : "file://" + lock_lan_path, lock_lan_Background
                , getContext().getResources().getConfiguration()
                        .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);

        desktop_icon_size_hor_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    desktop_icon_size_hor_seekbar.setProgress(1);
                    return;
                }

                if (getActivity() != null) {
                    SavePreference.save(getActivity(), AppUtis.isPad(getActivity()) ? "icon_size_pad" : "icon_size", progress);
                    if(getActivity().getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT) {
                        ((MainActivity) getActivity()).getGridLayoutManager().setSpanCount(progress);
                        ((MainActivity) getActivity()).getAppGridAdapter().setSpaCount(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        desktop_icon_size_ver_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    desktop_icon_size_ver_seekbar.setProgress(1);
                    return;
                }

                if (getActivity() != null) {
                    SavePreference.save(getActivity(), AppUtis.isPad(getActivity()) ? "icon_size_ver_pad" : "icon_size_ver", progress);
                    if(getActivity().getResources().getConfiguration().orientation==ORIENTATION_LANDSCAPE) {
                        ((MainActivity) getActivity()).getGridLayoutManager().setSpanCount(progress);
                        ((MainActivity) getActivity()).getAppGridAdapter().setSpaCount(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        desktop_icon_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                desktop_icon_switch.setText(isChecked ? "图标(显示)" : "图标(隐藏)");

                if (getActivity() != null) {
                    SavePreference.save(getActivity(), "icon_is_show", isChecked);
                    ((MainActivity) getActivity()).getAppGridAdapter().clearOrAddOtherSystemIcon(isChecked);
                }
            }
        });
        lock_show_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lock_show_switch.setText(isChecked ? "进入桌面前看到锁屏(开启)" : "进入桌面前看到锁屏(关闭)");
                if (getActivity() != null) {
                    SavePreference.save(getActivity(), "lock_is_show", isChecked);
                }

            }
        });
        statusBarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                statusBarSwitch.setText(isChecked ? "(显示)状态栏" : "(隐藏)状态栏");
                if (getActivity() != null) {
                    SavePreference.save(getActivity(), "statusBar_is_show", isChecked);
                    ToastView.getInstaller(getActivity()).setText("重启桌面APP生效").show();
                }

            }
        });

        showFirstAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showFirstAdd.setText(isChecked ? "(显示)最近添加APP" : "(隐藏)最近添加APP");
                if (getActivity() != null) {
                    ToastView.getInstaller(getContext()).setText("重启桌面最近添加即"+(isChecked?"显示":"隐藏")).show();
                    SavePreference.save(getActivity(), "first_add_show", isChecked);
                }
            }
        });
        start_menu_width_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SavePreference.save(getContext(),orientationChange.isChecked()?"start_menu_portrait_width"
                        :"start_menu_landscape_width",progress);

                startMenuDemo.setLayoutParams(new RelativeLayout.LayoutParams(
                        (DensityUtils.getScreenW(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext()
                                ,orientationChange.isChecked()?"start_menu_portrait_width"
                                :"start_menu_landscape_width"))
                        , (DensityUtils.getScreenH(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext()
                        ,orientationChange.isChecked()?"start_menu_portrait_height"
                        :"start_menu_landscape_height"))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        start_menu_height_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SavePreference.save(getContext(),orientationChange.isChecked()?"start_menu_portrait_height"
                        :"start_menu_landscape_height",progress);

                startMenuDemo.setLayoutParams(new RelativeLayout.LayoutParams(
                        (DensityUtils.getScreenW(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext()
                                ,orientationChange.isChecked()?"start_menu_portrait_width"
                                        :"start_menu_landscape_width"))
                        , (DensityUtils.getScreenH(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext()
                        ,orientationChange.isChecked()?"start_menu_portrait_height"
                                :"start_menu_landscape_height"))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        orientationChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                orientationChange.setText(isChecked?"开始菜单(竖)屏设置":"开始菜单(横)屏设置");
                if(isChecked) {
                    startMenuDemo.setLayoutParams(new RelativeLayout.LayoutParams(
                            (DensityUtils.getScreenW(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_portrait_width"))
                            , (DensityUtils.getScreenH(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_portrait_height"))));
                    start_menu_width_seekBar.setMax(50);
                    start_menu_width_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_portrait_width"));

                    start_menu_height_seekBar.setMax(500);
                    start_menu_height_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_portrait_height"));
                }else {
                    startMenuDemo.setLayoutParams(new RelativeLayout.LayoutParams(
                            (DensityUtils.getScreenW(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_landscape_width"))
                            , (DensityUtils.getScreenH(getContext())/2) - DensityUtils.dp2px(SavePreference.getInt(getContext(),"start_menu_landscape_height"))));

                    start_menu_width_seekBar.setMax(300);
                    start_menu_width_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_landscape_width"));

                    start_menu_height_seekBar.setMax(500);
                    start_menu_height_seekBar.setProgress(SavePreference.getInt(getContext(),"start_menu_landscape_height"));
                }
            }
        });
        enableTablet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SavePreference.save(getContext(),"enableTablet",isChecked);
            }
        });
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_control, container, false));
    }

    public static final int IMAGE_1_REQUEST_CODE = 10889;
    public static final int IMAGE_2_REQUEST_CODE = 10890;
    public static final int IMAGE_3_REQUEST_CODE = 10891;

    @Override
    protected void onClick(View view, int viewId) {
        switch (viewId) {
            case R.id.wallpaperBackground:
                openPictureSelect(getActivity(), IMAGE_1_REQUEST_CODE);
                break;
            case R.id.lock_lan_Background:
                openPictureSelect(getActivity(), IMAGE_2_REQUEST_CODE);
                break;
            case R.id.lock_pro_Background:
                openPictureSelect(getActivity(), IMAGE_3_REQUEST_CODE);
                break;
        }
    }

    @Override
    public boolean onBackKey() {
        return true;
    }

    public void setPhotoToPath(Context context, int requestCode, String path) {
        switch (requestCode) {
            case IMAGE_1_REQUEST_CODE:
                SavePreference.save(context, "wallpaper", path);
                BaseApplication.imageLoader.displayImage("file://" + path, wallpaperBackground
                        , getContext().getResources().getConfiguration()
                                .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);
                if (context.getResources().getConfiguration()
                        .orientation == ORIENTATION_LANDSCAPE) {
                    Intent intent = new Intent(MAIN_BROADCAST);
                    intent.putExtra("message", "refreshWallpaper");
                    context.sendBroadcast(intent);
                }
                ToastView.getInstaller(context).setText("横屏壁纸已更新").show();
                break;
            case IMAGE_2_REQUEST_CODE:
                SavePreference.save(context, "lock_lan_path", path);
                BaseApplication.imageLoader.displayImage("file://" + path, lock_lan_Background
                        , getContext().getResources().getConfiguration()
                                .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);

                ToastView.getInstaller(context).setText("锁屏横屏壁纸已更新").show();
                break;
            case IMAGE_3_REQUEST_CODE:
                SavePreference.save(context, "lock_pro_path", path);
                BaseApplication.imageLoader.displayImage("file://" + path, lock_pro_Background
                        , getContext().getResources().getConfiguration()
                                .orientation == ORIENTATION_LANDSCAPE ? imageSizeLan : imageSizePro);

                ToastView.getInstaller(context).setText("锁屏默认壁纸已更新").show();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        if (requestCode == IMAGE_1_REQUEST_CODE || requestCode == IMAGE_2_REQUEST_CODE || requestCode == IMAGE_3_REQUEST_CODE)
            if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                String photoPath = AppUtis.getPhotoUri(getContext(), data);
                setPhotoToPath(getContext(), requestCode, photoPath);
            }

    }


    private void openPictureSelect(Context context, int requestCode) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("MAX_Length", 1);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}

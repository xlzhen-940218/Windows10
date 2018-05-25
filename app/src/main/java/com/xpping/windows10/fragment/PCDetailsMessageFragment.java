package com.xpping.windows10.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.albums.AlbumActivity;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.SavePreference;
import com.xpping.windows10.widget.ToastView;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.windows.explorer.utils.Util;

import static android.app.Activity.RESULT_OK;
import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/*
 *xlzhen 2018/4/27
 * 系统信息
 */
public class PCDetailsMessageFragment extends BaseFragment {


    @Override
    protected void initData() {



        ((TextView) findViewById(R.id.windows_version)).setText("Windows 10 Desktop By Android " + Build.VERSION.RELEASE + " XPP");
        ((TextView) findViewById(R.id.windows_cpu)).setText("处理器：" + AppUtis.getCpuInfo());
        ((TextView) findViewById(R.id.windows_memory)).setText("已安装的内存(RAM)：" + Util.convertStorage(AppUtis.getTotalMemory(getContext())));
        ((TextView) findViewById(R.id.windows_system_type)).setText("系统类型：" + (!AppUtis.checkIfCPUx86()
                ? "64位操作系统，基于x64的处理器" : "32位操作系统，基于x86的处理器"));

        ((TextView) findViewById(R.id.windows_name)).setText("计算机名：" + Build.MODEL);


    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_pc_details_message, container, false));
    }

    @Override
    protected void onClick(View view, int viewId) {

    }


    @Override
    public boolean onBackKey() {
        return true;
    }


}

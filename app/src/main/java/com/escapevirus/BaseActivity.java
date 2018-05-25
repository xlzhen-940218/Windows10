package com.escapevirus;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xpping.windows10.widget.ToastView;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xlzhen on 4/14 0014.
 * 是男人就坚持10秒 activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("",this.getClass().getSimpleName()+"........onCreate");
        setContentView();
        initStatus(statusBarDarkMode());
        initStatusBarDarkMode(statusBarDarkMode());
        initData();
        initWidget();
    }

    protected void setStatusBarDarkMode(boolean isDark){
        initStatus(isDark);
        initStatusBarDarkMode(isDark);
    }

    private void initStatus(boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            if(isDark) {
                //因为标题栏是白色，所以给系统状态栏设置深色主题
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    /*
    * isDark 是否使用深色主题 默认为true
    */
    public void initStatusBarDarkMode(boolean isDark) {
        //MIUI 深色主题
        AppUtils.flyMeSetStatusBarLightMode(getWindow(),isDark);
        AppUtils.MIUISetStatusBarLightMode(getWindow(),isDark);
    }

    public abstract void setContentView();

    /*
    *是否使用深色主题 请填充返回值
    */
    public abstract boolean statusBarDarkMode();

    public abstract void initData();

    public abstract void initWidget();

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id, boolean clickAble) {
        T views = (T) findViewById(id);
        if (clickAble)
            views.setOnClickListener(this);
        return views;
    }

    protected void showToast(String text) {
        if (TextUtils.isEmpty(text))
            return;
        /*if (mToast == null) {
            mToast = Toast.makeText(FrameActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();*/
        ToastView.getInstaller(this).setText(text).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("",this.getClass().getSimpleName()+"........onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("",this.getClass().getSimpleName()+"........onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("",this.getClass().getSimpleName()+"........onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("",this.getClass().getSimpleName()+"........onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("",this.getClass().getSimpleName()+"........onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("",this.getClass().getSimpleName()+"........onStop");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}

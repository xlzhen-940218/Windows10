package com.xpping.windows10.activity.base;


import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.xpping.windows10.utils.AppUtis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xiong on 2018/1/28.
 * 基类activity
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getName(),"onCreate");
        setContentView();
        initUI();
    }

    protected abstract void initUI();
    public abstract void setContentView();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.i(this.getClass().getName(),"onResume");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppUtis.hideBottomUIMenu(BaseActivity.this);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Log.i(this.getClass().getName(),"onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getName(),"onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(this.getClass().getName(),"onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(this.getClass().getName(),"onStop");
    }
}

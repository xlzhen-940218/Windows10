package com.xpping.windows10.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.AppUtis;
/*
 *xlzhen 2018/4/27
 * 手电筒
 */
public class FlashlightFragment extends BaseFragment {
    private TextView flashLight,flashLightButton;
    private boolean isOpen;
    @Override
    protected void initData() {
        flashLight=findViewById(R.id.flashLight);
        flashLightButton=findViewById(R.id.flashLightButton);
        flashLightButton.setOnClickListener(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            flashLight.setText("我是手电筒\n我现在\n是关\n着的\n请点\n击打\n开手\n电筒\n打开\n我就\n亮了哦");
            flashLightButton.setText("暂不支持您的系统版本");
            flashLightButton.setEnabled(false);
            return;
        }
        checkAndOpenFlashLight();
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_flash_light,container,false));
    }

    @Override
    protected void onClick(View view, int viewId) {
        switch (viewId){
            case R.id.flashLightButton:
                checkAndOpenFlashLight();
                break;
        }
    }

    private void checkAndOpenFlashLight() {
        if(getContext()==null)
            return;
        isOpen = AppUtis.openFlashLight(getContext(),isOpen);
        flashLightButton.setText(isOpen?"关闭手电筒":"打开手电筒");
        flashLightButton.setTextColor(isOpen? Color.RED:Color.BLACK);
        flashLight.setTextColor(isOpen?Color.WHITE: ContextCompat.getColor(getContext(),R.color.color_33));
        flashLight.setText(isOpen?"我是手电筒\n我现在\n是开\n着的\n请点\n击关\n闭手\n电筒\n关闭\n我就\n灭了哦":"我是手电筒\n我现在\n是关\n着的\n请点\n击打\n开手\n电筒\n打开\n我就\n亮了哦");
    }

    @Override
    public boolean onBackKey() {
        return true;
    }
}

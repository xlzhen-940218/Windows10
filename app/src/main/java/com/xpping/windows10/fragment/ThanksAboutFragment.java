package com.xpping.windows10.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.listener.MovementCheck;

/*
*鸣谢
*/
public class ThanksAboutFragment extends BaseFragment {
    //private TextView github1,github2,github3,github4,github5,github6,github7,github8,github9,github10;
    @Override
    protected void initData() {
        setTextAndHtml(new int[]{R.id.github1,R.id.github2,R.id.github3,R.id.github4,R.id.github5
                                 ,R.id.github6,R.id.github7,R.id.github8,R.id.github9,R.id.github10
                                 ,R.id.github11,R.id.github12,R.id.github13,R.id.github14,R.id.github15}
                                 ,new String[]{null,null,null,null,null,null,null,null,null,null
                                 ,"酷安@zd523","酷安@脸比名字长","酷安@如风如风","酷安@剑姬","酷安@求求你换个名字好不好"}
                                 ,new String[]{"https://github.com/square/okhttp"
                                 ,"https://github.com/lingochamp/FileDownloader"
                                 ,"https://github.com/googlesamples/easypermissions"
                                 ,"https://github.com/bingoogolapple/BGARefreshLayout-Android"
                                 ,"https://github.com/alibaba/fastjson"
                                 ,"https://github.com/nostra13/Android-Universal-Image-Loader"
                                 ,"https://github.com/j256/ormlite-android"
                                 ,"https://github.com/chrisbanes/PhotoView"
                                 ,"https://github.com/MiCode/FileExplorer"
                                 ,"https://github.com/belerweb/pinyin4j"
                                 ,"https://www.coolapk.com/u/1354403"
                                 ,"http://www.coolapk.com/u/485182"
                                ,"http://www.coolapk.com/u/771599 "
                                ,"http://www.coolapk.com/u/1116007 "
                                ,"http://www.coolapk.com/u/1071418"},0);

    }

    private void setTextAndHtml(int[] id,String[] name,String[] url,int index){
        TextView textView=findViewById(id[index]);
        textView.setText(Html.fromHtml("<a href='"+url[index]+"'>"+(name[index]==null?url[index]:name[index])+"</a>"));
        textView.setMovementMethod(MovementCheck.getInstance());

        if(index<id.length-1) {
            index++;

            setTextAndHtml(id,name, url, index);
        }
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_thanks_about, container, false));
    }

    @Override
    protected void onClick(View view, int viewId) {

    }

    @Override
    public boolean onBackKey() {
        return true;
    }
}

package com.photoview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlzhen on 3/21 0021.
 * viewPager 适配器
 */

public abstract class SimpleViewPagerAdapter<T> extends PagerAdapter {
    protected Context context;
    protected List<T> data;
    protected ViewGroup[] views;
    public SimpleViewPagerAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data == null ? new ArrayList<T>() : data;

        views = new ViewGroup[this.data.size()];
        try {
            for (int i = 0; i < views.length; i++) {
                views[i]=new FrameLayout(context);
                views[i].addView(getView(views[i], i, this.data.get(i)));
            }
        }catch (Exception ex){}
    }

    public SimpleViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setNewData(List<T> data){
        this.data = data == null ? new ArrayList<T>() : data;

        views = new ViewGroup[this.data.size()];

        for (int i = 0; i < views.length; i++) {
            views[i]=new FrameLayout(context);
            views[i].addView(getView(views[i], i, this.data.get(i)));
        }
    }

    protected void addData(List<T> data){
        this.data .addAll(data == null ? new ArrayList<T>() : data);

        views = new ViewGroup[this.data.size()];

        for (int i = 0; i < views.length; i++) {
            views[i]=new FrameLayout(context);
            views[i].addView(getView(views[i], i, this.data.get(i)));
        }
    }

    protected abstract View getView(View convertView,int position,T bean);

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views[position % views.length]);
    }
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views[position % views.length], 0);
        return views[position % views.length];
    }
    public List<T> getData(){
        return data;
    }


}

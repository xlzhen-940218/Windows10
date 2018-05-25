package com.xpping.windows10.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlzhen on 12/4 0004.
 * 继承自BaseAdapter的简易模型适配器
 */
public abstract class SimpleAdapter<T> extends BaseAdapter implements View.OnClickListener{

    protected Context context;
    protected List<T> data;

    private updateData updateData;

    public SimpleAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data == null ? new ArrayList<T>() : data;
        initData();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size())
            return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void addData(List<T> data){
        this.data.addAll(data);

        if(updateData!=null)
            updateData.updateData();

        notifyDataSetChanged();
    }

    public void addData(T data){
        this.data.add(data);

        if(updateData!=null)
            updateData.updateData();

        notifyDataSetChanged();
    }

    public void setData(List<T> data){
        this.data=data;

        if(updateData!=null)
            updateData.updateData();

        notifyDataSetChanged();
    }

    public void initData(){

    }

    public List<T> getData(){
        return data==null?new ArrayList<T>():data;
    }

    public void setUpdateData(SimpleAdapter.updateData updateData) {
        this.updateData = updateData;
    }

    public interface updateData{
        void updateData();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(View view,int id, boolean clickAble) {
        T views = (T)view.findViewById(id);
        if (clickAble)
            views.setOnClickListener(this);
        return views;
    }
}

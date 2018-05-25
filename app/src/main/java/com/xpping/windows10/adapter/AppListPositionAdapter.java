package com.xpping.windows10.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.entity.AppListPosition;

import java.util.List;

/**
 * Created by xlzhen on 9/12 0012.
 * 开始菜单app列表 快速定位
 */

public class AppListPositionAdapter extends SimpleAdapter<AppListPosition> {
    private OnItemOnClickListener onItemOnClickListener;
    private int size;

    public AppListPositionAdapter(Context context, List<AppListPosition> data,int size, OnItemOnClickListener onItemOnClickListener) {
        super(context, data);
        this.onItemOnClickListener = onItemOnClickListener;
        this.size=size;
    }

    @Override
    public void onClick(View view) {
        int appListPosition = (int) view.getTag();
        if (onItemOnClickListener != null)
            onItemOnClickListener.onClick(appListPosition);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(context);
            convertView.setBackgroundResource(R.drawable.app_list_position_item_bg);
            convertView.setLayoutParams(new AbsListView.LayoutParams(size
                    , size));
            ((TextView) convertView).setGravity(Gravity.CENTER);
            ((TextView) convertView).getPaint().setFakeBoldText(true);
            ((TextView) convertView).setTextColor(Color.WHITE);
            ((TextView) convertView).setTextSize(22);
            convertView.setOnClickListener(this);
        }
        ((TextView) convertView).setText(data.get(position).getText());
        convertView.setTag(data.get(position).getPosition());
        return convertView;
    }

    public interface OnItemOnClickListener {
        void onClick(int position);
    }
}

package com.xpping.windows10.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.utils.DensityUtils;

import java.util.List;

/**
 * Created by xlzhen on 9/14 0014.
 * 弹窗 菜单
 */

public class MenuPopupWindow extends PopupWindow {
    private Context context;
    private ListView listView;
    private Style style;
    public MenuPopupWindow(Context context,Style style){
        setContentView(View.inflate(context,R.layout.dialog_menu_select,null));
        this.style=style;
        setWidth(DensityUtils.dp2px(200));
        setOutsideTouchable(true);
        setTouchable(true);
        setBackgroundDrawable(new PaintDrawable());
        this.context=context;
        listView=(ListView)getContentView().findViewById(R.id.listView);
        listView.setBackgroundResource(style==Style.black?R.drawable.menu_select_bg:R.drawable.menu_select_white_bg);
    }
    public void setMenuData(List<String> menuData, AdapterView.OnItemClickListener itemClickListener){
        setHeight(DensityUtils.dp2px(44*menuData.size()));
        listView.setAdapter(new SimpleAdapter<String>(context,menuData) {

            @Override
            public void onClick(View view) {

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView=new TextView(context);
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    convertView.setBackgroundResource(style==Style.black?R.drawable.start_menu_item_bg:R.drawable.start_menu_white_item_bg);
                    convertView.setPadding(DensityUtils.dp2px(10),DensityUtils.dp2px(10)
                            ,DensityUtils.dp2px(10),DensityUtils.dp2px(10));

                    ((TextView)convertView).setTextColor(style==Style.black?Color.WHITE:Color.BLACK);
                }
                ((TextView)convertView).setText(data.get(position));
                return convertView;
            }
        });
        listView.setOnItemClickListener(itemClickListener);
    }

    public enum Style{
        white,black
    }
}

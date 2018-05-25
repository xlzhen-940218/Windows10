package com.xpping.windows10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.xpping.windows10.entity.TaskBarEntity;
import com.xpping.windows10.utils.DensityUtils;

import java.util.List;

/*
*任务栏 适配器
*/
public class TaskBarAdapter extends SimpleAdapter<TaskBarEntity> {
    private int size;

    public TaskBarAdapter(Context context, List<TaskBarEntity> data) {
        super(context, data);
        size = DensityUtils.dp2px(40);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_task_bar, parent, false);
        }
        if (data.get(position).getName().equals("任务视图")) {
            ((ImageView) convertView.findViewById(R.id.imageView)).setImageResource(Integer.parseInt(data.get(position).getIcon()));
        } else
            BaseApplication.imageLoader.displayImage("drawable://" + data.get(position).getIcon()
                    , (ImageView) convertView.findViewById(R.id.imageView), new ImageSize(size, size));

        convertView.findViewById(R.id.imageView).setBackgroundResource(data.get(position).isOpen()
                && data.get(position).getBaseFragment() != null ? R.drawable.task_view_opened_bg : R.drawable.taskbar_item_bg);
        convertView.findViewById(R.id.imageView).setVisibility(!data.get(position).isOpen()
                /*&&data.get(position).getFragmentId()!=0*/ ? View.GONE : View.VISIBLE);

        convertView.setTag(data.get(position));
        return convertView;
    }

    public TaskBarEntity getFragmentId(int fragmentId) {
        //取出和fragmentId对应的taskBarEntity
        TaskBarEntity bean = null;
        for (TaskBarEntity taskBarEntity : data)
            if (taskBarEntity.getBaseFragment()!=null&&fragmentId == taskBarEntity.getBaseFragment().getLayoutId())
                bean = taskBarEntity;

        return bean;
    }

    @Override
    public void onClick(View v) {

    }

    public void remove(TaskBarEntity taskBarEntity) {
        for (TaskBarEntity bean : data)
            if (taskBarEntity.getBaseFragment().getLayoutId()== taskBarEntity.getBaseFragment().getLayoutId()) {
                taskBarEntity.setOpen(false);
                break;
            }
        notifyDataSetChanged();
    }
}

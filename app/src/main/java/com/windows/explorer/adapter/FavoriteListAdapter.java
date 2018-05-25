
package com.windows.explorer.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.windows.explorer.entity.FavoriteItem;
import com.windows.explorer.helper.FileIconHelper;
import com.windows.explorer.utils.Util;
import com.xpping.windows10.utils.DensityUtils;

import java.util.ArrayList;

/*
*xlzhen 2018/4/27
*/
public class FavoriteListAdapter extends SimpleAdapter<FavoriteItem> {
    private Context context;

    private FileIconHelper fileIconHelper;
    private int imageSize;
    public FavoriteListAdapter(Context context, ArrayList<FavoriteItem> objects, FileIconHelper fileIconHelper) {
        super(context, objects);
        imageSize=DensityUtils.dp2px(50);
        this.context = context;
        this.fileIconHelper = fileIconHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_file_list, parent, false);
            convertView.findViewById(R.id.file_checkbox).setVisibility(View.GONE);
        }

        //每一栏目的颜色不同
        convertView.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0?R.color.file_list_bg:android.R.color.white));

        //对文件的图标进行定制
        if (data.get(position).getFileInfo().isDir())
            BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.folder_fav
                    ,(ImageView) convertView.findViewById(R.id.file_image),new ImageSize(imageSize,imageSize));
        else
            fileIconHelper.setIcon(data.get(position).getFileInfo()
                    , (ImageView) convertView.findViewById(R.id.file_image),imageSize);

        //对文件名进行定制
        ((TextView) convertView.findViewById(R.id.file_name)).setText(data.get(position).getFileInfo().getFileName());

        //对文件数量进行定制
        ((TextView) convertView.findViewById(R.id.file_count)).setText(data.get(position).getFileInfo().isDir()
                ? data.get(position).getFileInfo().getCount() + "项\t\t|" : "");

        //对修改时间进行定制
        ((TextView) convertView.findViewById(R.id.modified_time)).setText((data.get(position).getFileInfo().isDir()
                ?"\t\t":"")+ Util.formatDateString(context
                , data.get(position).getFileInfo().getModifiedDate())+(data.get(position).getFileInfo().isDir()
                ?"":"\t\t"));

        //对文件大小进行定制
        ((TextView) convertView.findViewById(R.id.file_size)).setText(data.get(position).getFileInfo().isDir() ? ""
                : "|\t\t" + Util.convertStorage(data.get(position).getFileInfo().getFileSize()));

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}

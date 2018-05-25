package com.windows.explorer.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.adapter.base.SimpleAdapter;
import com.windows.explorer.entity.FileInfo;
import com.windows.explorer.entity.FileListItem;
import com.windows.explorer.helper.FileIconHelper;
import com.windows.explorer.utils.FileViewInteractionHub;
import com.windows.explorer.utils.Util;
import com.xpping.windows10.utils.DensityUtils;

import java.util.ArrayList;

/*
 *熊龙镇 windows10 风格列表
 */
public class FileListAdapter extends SimpleAdapter<FileInfo> {
    private FileIconHelper fileIconHelper;
    private FileViewInteractionHub fileViewInteractionHub;
    private int imageSize;
    public FileListAdapter(Context context, FileIconHelper fileIconHelper, FileViewInteractionHub fileViewInteractionHub, ArrayList<FileInfo> data) {
        super(context, data);
        imageSize= DensityUtils.dp2px(50);
        this.fileIconHelper = fileIconHelper;
        this.fileViewInteractionHub = fileViewInteractionHub;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_file_list, parent, false);
            convertView.findViewById(R.id.file_checkbox).setOnClickListener(this);
        }

        //每一栏目的颜色不同
        convertView.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0?R.color.file_list_bg:android.R.color.white));

        //对文件的图标进行定制
        if (data.get(position).isDir())
            BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.folder
                    ,(ImageView) convertView.findViewById(R.id.file_image),new ImageSize(imageSize,imageSize));
        else
            fileIconHelper.setIcon(data.get(position), (ImageView) convertView.findViewById(R.id.file_image),imageSize);


        if (fileViewInteractionHub.isMoveState()) {
            data.get(position).setSelected(fileViewInteractionHub.isFileSelected(data.get(position).getFilePath()));
        }

        //对文件的是否选择框进行定制
        if (fileViewInteractionHub.getMode() == FileViewInteractionHub.Mode.Pick) {
            convertView.findViewById(R.id.file_checkbox).setVisibility(View.GONE);
        } else {
            convertView.findViewById(R.id.file_checkbox).setVisibility(fileViewInteractionHub.canShowCheckBox() ? View.VISIBLE : View.GONE);

            BaseApplication.imageLoader.displayImage("drawable://"+(data.get(position).isSelected()
                            ? R.mipmap.checkbox_on : R.mipmap.checkbox_off)
                    ,(ImageView) convertView.findViewById(R.id.file_checkbox),new ImageSize(imageSize,imageSize));

            convertView.findViewById(R.id.file_checkbox).setTag(data.get(position));
            convertView.setSelected(data.get(position).isSelected());
        }

        //对文件名进行定制
        ((TextView) convertView.findViewById(R.id.file_name)).setText(data.get(position).getFileName());

        //对文件数量进行定制
        ((TextView) convertView.findViewById(R.id.file_count)).setText(data.get(position).isDir()
                ? data.get(position).getCount() + "项\t\t|" : "");

        //对修改时间进行定制
        ((TextView) convertView.findViewById(R.id.modified_time)).setText((data.get(position).isDir()
                ?"\t\t":"")+ Util.formatDateString(context
                , data.get(position).getModifiedDate())+(data.get(position).isDir()
                ?"":"\t\t"));

        //对文件大小进行定制
        ((TextView) convertView.findViewById(R.id.file_size)).setText(data.get(position).isDir() ? ""
                : "|\t\t" + Util.convertStorage(data.get(position).getFileSize()));

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_checkbox://点击了选择框
                ImageView img = (ImageView) v;
                assert img.getTag() != null;

                FileInfo tag = (FileInfo) img.getTag();
                tag.setSelected(!tag.isSelected());
                ActionMode actionMode = ((MainActivity) context).getActionMode();
                if (actionMode == null) {
                    actionMode = ((MainActivity) context)
                            .startActionMode(new FileListItem.ModeCallback(context,
                                    fileViewInteractionHub));
                    ((MainActivity) context).setActionMode(actionMode);
                } else {
                    actionMode.invalidate();
                }
                if (fileViewInteractionHub.onCheckItem(tag, v)) {
                    BaseApplication.imageLoader.displayImage("drawable://"+(tag.isSelected() ? R.mipmap.checkbox_on : R.mipmap.checkbox_off)
                            ,img,new ImageSize(imageSize,imageSize));
                } else {
                    tag.setSelected(!tag.isSelected());
                }
                Util.updateActionModeTitle(actionMode, context,
                        fileViewInteractionHub.getSelectedFileList().size());
                break;
        }
    }
}

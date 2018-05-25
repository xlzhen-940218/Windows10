package com.windows.explorer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.windows.explorer.entity.FileInfo;
import com.windows.explorer.entity.FileListItem;
import com.windows.explorer.helper.FileCategoryHelper;
import com.windows.explorer.helper.FileIconHelper;
import com.windows.explorer.utils.FileViewInteractionHub;
import com.windows.explorer.utils.Util;
import com.xpping.windows10.utils.DensityUtils;

import java.util.Collection;
import java.util.HashMap;

/*
*xlzhen 2018/4/27
*/
public class FileListCursorAdapter extends CursorAdapter implements View.OnClickListener {

    private final LayoutInflater mFactory;

    private FileViewInteractionHub fileViewInteractionHub;

    private FileIconHelper fileIconHelper;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, FileInfo> mFileNameList = new HashMap<>();

    private Context context;
    private int imageSize;
    public FileListCursorAdapter(Context context, Cursor cursor,
                                 FileViewInteractionHub fileViewInteractionHub, FileIconHelper fileIconHelper) {
        super(context, cursor, false /* auto-requery */);
        imageSize= DensityUtils.dp2px(50);
        mFactory = LayoutInflater.from(context);
        this.fileViewInteractionHub = fileViewInteractionHub;
        this.fileIconHelper = fileIconHelper;
        this.context = context;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        FileInfo fileInfo = getFileItem(cursor.getPosition());
        if (fileInfo == null) {
            // file is not existing, create a fake info
            fileInfo = new FileInfo();
            fileInfo.setDbId( cursor.getLong(FileCategoryHelper.COLUMN_ID));
            fileInfo.setFilePath(cursor.getString(FileCategoryHelper.COLUMN_PATH));
            fileInfo.setFileName( Util.getNameFromFilepath(fileInfo.getFilePath()));
            fileInfo.setFileSize( cursor.getLong(FileCategoryHelper.COLUMN_SIZE));
            fileInfo.setModifiedDate( cursor.getLong(FileCategoryHelper.COLUMN_DATE));
        }

        //每一栏目的颜色不同
        convertView.setBackgroundColor(ContextCompat.getColor(context, cursor.getPosition() % 2 == 0? R.color.file_list_bg:android.R.color.white));

        //对文件的图标进行定制
        if (fileInfo.isDir())
            BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.folder
                    ,(ImageView) convertView.findViewById(R.id.file_image),new ImageSize(imageSize,imageSize));
        else
            fileIconHelper.setIcon(fileInfo, (ImageView)convertView.findViewById(R.id.file_image),imageSize);


        if (fileViewInteractionHub.isMoveState()) {
            fileInfo.setSelected(fileViewInteractionHub.isFileSelected(fileInfo.getFilePath()));
        }

        //对文件的是否选择框进行定制
        if (fileViewInteractionHub.getMode() == FileViewInteractionHub.Mode.Pick) {
            convertView.findViewById(R.id.file_checkbox).setVisibility(View.GONE);
        } else {
            convertView.findViewById(R.id.file_checkbox).setVisibility(fileViewInteractionHub.canShowCheckBox() ? View.VISIBLE : View.GONE);

            BaseApplication.imageLoader.displayImage("drawable://"+(fileInfo.isSelected()
                            ? R.mipmap.checkbox_on : R.mipmap.checkbox_off)
                    ,(ImageView) convertView.findViewById(R.id.file_checkbox),new ImageSize(imageSize,imageSize));

            convertView.findViewById(R.id.file_checkbox).setTag(fileInfo);
            convertView.setSelected(fileInfo.isSelected() );
        }

        //对文件名进行定制
        ((TextView)convertView.findViewById(R.id.file_name)).setText(fileInfo.getFileName());

        //对文件数量进行定制
        ((TextView)convertView.findViewById(R.id.file_count)).setText(fileInfo.isDir()
                ?fileInfo.getCount()+"项\t\t|":"");

        //对修改时间进行定制
        ((TextView) convertView.findViewById(R.id.modified_time)).setText((fileInfo.isDir()
                ?"\t\t":"")+Util.formatDateString(context
                , fileInfo.getModifiedDate())+(fileInfo.isDir()
                ?"":"\t\t"));

        //对文件大小进行定制
        ((TextView)convertView.findViewById(R.id.file_size)).setText(fileInfo.isDir()?""
                :"|\t\t"+Util.convertStorage(fileInfo.getFileSize()));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view=mFactory.inflate(R.layout.adapter_file_list, parent, false);

        view.findViewById(R.id.file_checkbox).setOnClickListener(this);
        return view;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        mFileNameList.clear();
        super.changeCursor(cursor);
    }

    public Collection<FileInfo> getAllFiles() {
        if (mFileNameList.size() == getCount())
            return mFileNameList.values();

        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            do {
                Integer position = Integer.valueOf(cursor.getPosition());
                if (mFileNameList.containsKey(position))
                    continue;
                FileInfo fileInfo = getFileInfo(cursor);
                if (fileInfo != null) {
                    mFileNameList.put(position, fileInfo);
                }
            } while (cursor.moveToNext());
        }

        return mFileNameList.values();
    }

    public FileInfo getFileItem(int pos) {
        Integer position = Integer.valueOf(pos);
        if (mFileNameList.containsKey(position))
            return mFileNameList.get(position);

        Cursor cursor = (Cursor) getItem(pos);
        FileInfo fileInfo = getFileInfo(cursor);
        if (fileInfo == null)
            return null;

        fileInfo.setDbId(cursor.getLong(FileCategoryHelper.COLUMN_ID));
        mFileNameList.put(position, fileInfo);
        return fileInfo;
    }

    private FileInfo getFileInfo(Cursor cursor) {
        return (cursor == null || cursor.getCount() == 0) ? null : Util
                .GetFileInfo(cursor.getString(FileCategoryHelper.COLUMN_PATH));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.file_checkbox://点击了选择框
                ImageView img = (ImageView) v;
                assert img.getTag() != null;

                FileInfo tag = (FileInfo) img.getTag();
                tag.setSelected( !tag.isSelected());
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
                    BaseApplication.imageLoader.displayImage("drawable://"+(tag.isSelected() ? R.mipmap.checkbox_on
                                    : R.mipmap.checkbox_off)
                            ,img,new ImageSize(imageSize,imageSize));
                } else {
                    tag.setSelected( !tag.isSelected());
                }
                Util.updateActionModeTitle(actionMode, context,
                        fileViewInteractionHub.getSelectedFileList().size());
                break;
        }
    }
}

/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.windows.explorer.helper;


import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.windows.explorer.entity.FileInfo;
import com.windows.explorer.loader.FileIconLoader;
import com.windows.explorer.utils.Util;

import java.util.HashMap;

public class FileIconHelper {
    private static HashMap<String, Integer> fileExtToIcons = new HashMap<>();
    private FileIconLoader mIconLoader;

    static {
        addItem(new String[] {
            "mp3"
        }, R.mipmap.mp3);
        addItem(new String[] {
            "wma"
        }, R.mipmap.wma);
        addItem(new String[] {
            "wav"
        }, R.mipmap.wav);
        addItem(new String[] {
            "mid","mp4", "3gp", "3gpp", "3g2", "3gpp2", "asf"
        }, R.mipmap.mp4);
        addItem(new String[] {
                 "wmv"
        }, R.mipmap.wmv);
        addItem(new String[]{"mpeg"},R.mipmap.mpeg);
        addItem(new String[]{"m4v"},R.mipmap.m4v);
        addItem(new String[] {
                "jpg", "jpeg", "gif", "png", "bmp", "wbmp"
        }, R.mipmap.jpg);
        addItem(new String[] {
                "txt", "log", "xml", "ini", "lrc"
        }, R.mipmap.text);
        addItem(new String[] {
                "doc", "docx"
        }, R.mipmap.doc);
        addItem(new String[]{"ppt", "pptx"},R.mipmap.ppt);
        addItem(new String[]{"xsl", "xslx"},R.mipmap.xsl);
        addItem(new String[] {
            "pdf"
        }, R.mipmap.pdf);
        addItem(new String[] {
            "zip"
        }, R.mipmap.zip);
        addItem(new String[] {
            "rar"
        }, R.mipmap.zip);
    }

    public FileIconHelper(Context context) {
        mIconLoader = new FileIconLoader(context);
    }

    private static void addItem(String[] exts, int resId) {
        if (exts != null) {
            for (String ext : exts) {
                fileExtToIcons.put(ext.toLowerCase(), resId);
            }
        }
    }

    public static int getFileIcon(String ext) {
        Integer i = fileExtToIcons.get(ext.toLowerCase());
        if (i != null) {
            return i.intValue();
        } else {
            return R.mipmap.blank;
        }

    }

    public void setIcon(FileInfo fileInfo, ImageView fileImage,int imageSize) {
        String filePath = fileInfo.getFilePath();
        long fileId = fileInfo.getDbId();
        String extFromFilename = Util.getExtFromFilename(filePath);
        FileCategoryHelper.FileCategory fc = FileCategoryHelper.getCategoryFromPath(filePath);

        boolean set = false;
        int id = getFileIcon(extFromFilename);
        BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.folder_fav
                ,fileImage,new ImageSize(imageSize,imageSize));

        mIconLoader.cancelRequest(fileImage);
        switch (fc) {
            case Apk:
                set = mIconLoader.loadIcon(fileImage, filePath, fileId, fc);
                break;
            case Picture:
            case Video:
                set = mIconLoader.loadIcon(fileImage, filePath, fileId, fc);
                if (!set){
                    BaseApplication.imageLoader.displayImage("drawable://"+(fc == FileCategoryHelper.FileCategory.Picture ? R.mipmap.jpg
                                    : R.mipmap.mp4)
                            ,fileImage,new ImageSize(imageSize,imageSize));

                    set = true;
                }
                break;
            default:
                set = true;
                break;
        }

        if (!set)
            BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.blank
                    ,fileImage,new ImageSize(imageSize,imageSize));
    }

}

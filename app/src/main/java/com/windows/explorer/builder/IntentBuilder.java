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

package com.windows.explorer.builder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.xpping.windows10.BuildConfig;
import com.xpping.windows10.R;
import com.windows.explorer.entity.FileInfo;
import com.windows.explorer.utils.MimeUtils;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.widget.ToastView;


import java.io.File;
import java.util.ArrayList;

public class IntentBuilder {

    public static void viewFile(final MainActivity activity, final String filePath) {
        String type = getMimeType(filePath);

        if (!TextUtils.isEmpty(type) && !TextUtils.equals(type, "*/*")) {
            /* 设置intent的file与MimeType */
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            Uri apk_path;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                apk_path = FileProvider.getUriForFile(activity, "com.xpping.windows10.fileProvider", new File(filePath));

            } else {
                apk_path = Uri.fromFile(new File(filePath));
            }
            intent.setDataAndType(apk_path, type);
            activity.startActivity(intent);

        } else {
            // unknown MimeType
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder.setTitle(R.string.dialog_select_type);

            CharSequence[] menuItemArray = new CharSequence[] {
                    activity.getString(R.string.dialog_type_text),
                    activity.getString(R.string.dialog_type_audio),
                    activity.getString(R.string.dialog_type_video),
                    activity.getString(R.string.dialog_type_image) };
            dialogBuilder.setItems(menuItemArray,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectType = "*/*";
                            switch (which) {
                            case 0:
                                selectType = "text/plain";
                                break;
                            case 1:
                                selectType = "audio/*";
                                break;
                            case 2:
                                selectType = "video/*";
                                break;
                            case 3:
                                selectType = "image/*";
                                break;
                            }
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            try {
                                Uri apk_path;
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    apk_path = FileProvider.getUriForFile(activity, "com.xpping.windows10.fileProvider", new File(filePath));

                                } else {
                                    apk_path = Uri.fromFile(new File(filePath));
                                }
                                intent.setDataAndType(apk_path, selectType);
                                activity.startActivity(intent);

                            }catch (IllegalArgumentException ex){
                                ToastView.getInstaller(activity).setText("无法打开系统文件，权限不足！").show();
                            }

                        }
                    });
            dialogBuilder.show();
        }
    }

    public static Intent buildSendFile(MainActivity activity,ArrayList<FileInfo> files) {
        ArrayList<Uri> uris = new ArrayList<Uri>();

        String mimeType = "*/*";
        for (FileInfo file : files) {
            if (file.isDir())
                continue;

            File fileIn = new File(file.getFilePath());
            mimeType = getMimeType(file.getFileName());
            Uri u = Uri.fromFile(fileIn);
            Uri apk_path;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                apk_path = FileProvider.getUriForFile(activity, "com.xpping.windows10.fileProvider", fileIn);

            } else {
                apk_path = Uri.fromFile(fileIn);
            }
            uris.add(apk_path);
        }

        if (uris.size() == 0)
            return null;

        boolean multiple = uris.size() > 1;
        Intent intent = new Intent(multiple ? Intent.ACTION_SEND_MULTIPLE
                : Intent.ACTION_SEND);

        if (multiple) {
            intent.setType("*/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }

        return intent;
    }

    private static String getMimeType(String filePath) {
        int dotPosition = filePath.lastIndexOf('.');
        if (dotPosition == -1)
            return "*/*";

        String ext = filePath.substring(dotPosition + 1, filePath.length()).toLowerCase();
        String mimeType = MimeUtils.guessMimeTypeFromExtension(ext);
        if (ext.equals("mtz")) {
            mimeType = "application/miui-mtz";
        }

        return mimeType != null ? mimeType : "*/*";
    }
}

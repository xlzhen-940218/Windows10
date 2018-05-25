package com.baidu.translate.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;


import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by xlzhen on 12/1 0001.
 * 安装mp3 工具
 */

public class PlayMp3Utils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static ProgressDialog progressDialog;
    private static MediaPlayer mediaPlayer;

    public static void installMp3(Context context, String url) {
        PlayMp3Utils.context = context;
        progressDialog = new ProgressDialog(context);
        FileDownloader.getImpl().create(url).setPath(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/translate/" + url.substring(url.lastIndexOf("/"), url.length())).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putInt("soFar", soFarBytes);
                bundle.putInt("total", totalBytes);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Log.v("文件存储在", task.getPath());
                Message msg = new Message();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("mp3", task.getPath());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Message msg = new Message();
                msg.what = 404;
                Bundle bundle = new Bundle();
                bundle.putString("error", e.getMessage());
                bundle.putString("mp3Url", task.getUrl());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                progressDialog.dismiss();
                String mp3SdCardPath = msg.getData().getString("mp3");
                if (!TextUtils.isEmpty(mp3SdCardPath)) {
                    //播放mp3文件
                    if(mediaPlayer==null) {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        });
                    }
                    //从sd卡中加载音乐
                    try {
                        mediaPlayer.reset();
                        FileInputStream fis = new FileInputStream(new File(mp3SdCardPath));
                        mediaPlayer.setDataSource(fis.getFD());
                        //需使用异步缓冲
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (msg.what == 1) {
                progressDialog.setMessage("正在下载发音文件..."  + "(" + msg.getData().getInt("soFar") + "/" + msg.getData().getInt("total") + ")");
                progressDialog.setProgress(msg.getData().getInt("soFar"));

                if (!progressDialog.isShowing()) {
                    progressDialog.setMax(msg.getData().getInt("total"));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

            } else if (msg.what == 404) {
                progressDialog.dismiss();
                installMp3(context, msg.getData().getString("mp3Url"));

            }
        }
    };
}

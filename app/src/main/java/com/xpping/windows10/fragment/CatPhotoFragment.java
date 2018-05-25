package com.xpping.windows10.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.xpping.windows10.BuildConfig;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.FastJSONParser;
import com.xpping.windows10.widget.ToastView;
import com.photoview.HackyViewPager;
import com.photoview.PhotoPagerAdapter;
import com.xlzhen.cathouse.entity.PictureEntity;
import com.xlzhen.cathouse.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 *xlzhen 2018/4/27
 * 美猫 大图
 */
public class CatPhotoFragment extends BaseFragment {

    public String picList = "";
    public int picPosition;
    private ProgressDialog progressDialog;
    private PhotoPagerAdapter photoPagerAdapter;
    private List<PictureEntity> pictureEntities;

    @Override
    protected void initData() {
        HackyViewPager mViewPager = findViewById(R.id.view_pager);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        String picJson = picList;
        if (!TextUtils.isEmpty(picJson)) {
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == pictureEntities.size() - 1) {
                        //加载下一页
                        Intent intent = new Intent(MainActivity.MAIN_BROADCAST);
                        intent.putExtra("message", "nextPage");
                        getActivity().sendBroadcast(intent);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            pictureEntities = FastJSONParser.getBeanList(picJson, PictureEntity.class);
        } else {
            pictureEntities = new ArrayList<>();
            File[] listPicture = new File(StringUtils.SDCardPath).listFiles();
            for (File file : listPicture) {
                if (file.getName().contains(".config"))
                    pictureEntities.add(FastJSONParser.getBean(StringUtils.getTxtToString(file.getName()), PictureEntity.class));
            }
            if (pictureEntities.size() == 0)
                ToastView.getInstaller(getActivity()).setText("没有任何图片传输进来呢...").show();
        }


        photoPagerAdapter = new PhotoPagerAdapter(getActivity(), pictureEntities);
        mViewPager.setAdapter(photoPagerAdapter);

        ToastView.getInstaller(getActivity()).setText("快速上滑分享，快速下滑下载图片。").show();


        if (picPosition < pictureEntities.size())
            mViewPager.setCurrentItem(picPosition);

        mViewPager.setListener(new HackyViewPager.FastTouchEventListener() {
            @Override
            public void theLeft(int currentItem) {

            }

            @Override
            public void theRight(int currentItem) {

            }

            @Override
            public void theTop(int currentItem) {
                //分享
                downLoadPicture(pictureEntities.get(currentItem), true);
            }

            @Override
            public void theBottom(int currentItem) {
                //下载
                downLoadPicture(pictureEntities.get(currentItem), false);

            }
        });
    }

    public void refreshPictureList(String jsonString) {
        pictureEntities = FastJSONParser.getBeanList(jsonString, PictureEntity.class);
        if (photoPagerAdapter != null)
            photoPagerAdapter.setData(pictureEntities);
    }

    private void downLoadPicture(final PictureEntity pictureEntity, final boolean isShare) {
        FileDownloader.getImpl().create("http://hbimg.b0.upaiyun.com/" + pictureEntity.getFile().getKey())
                .setPath(StringUtils.SDCardPath + pictureEntity.getPin_id() + ".jpg").setListener(new FileDownloadListener() {
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
                pictureEntity.getFile().setPath(task.getPath());
                StringUtils.saveStringToTxt(FastJSONParser.getJsonString(pictureEntity), pictureEntity.getPin_id() + ".config");
                Message msg = new Message();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("path", task.getPath());
                bundle.putBoolean("isShare", isShare);
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
                bundle.putString("url", task.getUrl());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (!progressDialog.isShowing())
                    progressDialog.show();

                progressDialog.setMessage("正在下载图片(" + msg.getData().getInt("soFar") + "/" + msg.getData().getInt("total") + ")");
            } else if (msg.what == 0) {
                path = msg.getData().getString("path");
                Uri localUri = Uri.fromFile(new File(path));
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                getActivity().sendBroadcast(localIntent);

                progressDialog.dismiss();
                if (msg.getData().getBoolean("isShare")) {
                    //走分享
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    File file = new File(path);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(getActivity()
                                , BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));

                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    } else {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    }

                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, "分享图片到..."));
                } else {
                    //走下载
                    new AlertDialog.Builder(getActivity())
                            .setTitle("提醒")
                            .setMessage("图片已下载完毕，是否查看？")
                            .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setAction(Intent.ACTION_VIEW);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        Uri contentUri = FileProvider.getUriForFile(getActivity()
                                                , BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));
                                        intent.setDataAndType(contentUri, "image/*");
                                    } else {
                                        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                                    }
                                    try {
                                        startActivity(intent);
                                    } catch (Exception ex) {
                                        ToastView.getInstaller(getContext()).setText("没有相关应用...").show();
                                    }

                                }
                            }).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();

                    path = msg.getData().getString("path");
                }

            } else if (msg.what == 404) {
                progressDialog.dismiss();
            }
        }
    };

    private String path;

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_cathouse_photo, container, false));
    }

    @Override
    protected void onClick(View view, int viewId) {

    }

    @Override
    public boolean onBackKey() {
        return true;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        if (!TextUtils.isEmpty(this.picList)) {
            this.picList = picList;
            //延时两秒再去请求，不然很有可能会崩溃
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        });
                }
            }).start();

            return;
        }
        this.picList = picList;

    }

    public int getPicPosition() {
        return picPosition;
    }

    public void setPicPosition(int picPosition) {
        this.picPosition = picPosition;
    }
}

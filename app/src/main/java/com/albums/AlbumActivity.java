package com.albums;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.albums.ListImageDirPopupWindow.OnImageDirSelected;
import com.xpping.windows10.R;
import com.xpping.windows10.utils.DensityUtils;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/*
*图片选择器 兼容国产系统选择系统图片导致的一堆问题
*/
public class AlbumActivity extends Activity implements OnImageDirSelected ,View.OnClickListener{

    public static final String CAMERA_TAG = "security_camera";
    public static final String ALBUM_RESULT="picture_list";
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 各个相册的所有图片
     */
    private List<String> mImgs;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<>();
    //所有图片张数
    int totalCount = 0;

    //所有图片
    private List<String> allImgs = new ArrayList<>();

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    //最大图片可选长度
    public static int MaxSelectNum = Integer.MAX_VALUE;

    private ProgressDialog mProgressDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://刷新数据
                    mProgressDialog.dismiss();

                    // 为View绑定数据
                    data2View();
                    // 初始化展示文件夹的popupWindw
                    initListDirPopupWindw();
                    break;
                case 2://没有图片 退出

                    Toast.makeText(AlbumActivity.this,"您的图库没有图片",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }

        }
    };

    private TitleView mTitle;
    private GridView mGirdView;
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    private View viewsContainer;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picmain);
        initData();
    }



    public void initData() {

        mTitle=(TitleView)findViewById(R.id.title_view);
        mGirdView=(GridView)findViewById(R.id.id_gridView);
        mBottomLy=(RelativeLayout)findViewById(R.id.id_bottom_ly);
        mBottomLy.setOnClickListener(this);
        viewsContainer=findViewById(R.id.viewsContainer);
        viewsContainer.setOnClickListener(this);
        mChooseDir=(TextView) findViewById(R.id.id_choose_dir);
        mImageCount=(TextView) findViewById(R.id.id_total_count);

        if (mDirPaths == null)
            mDirPaths = new HashSet<>();

        MaxSelectNum = getIntent().getIntExtra("MAX_Length", Integer.MAX_VALUE);

        mScreenHeight = DensityUtils.getScreenH(getApplication());
        allImgs.add(CAMERA_TAG);
        getImages();
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(this,"一张图片都没扫描到",Toast.LENGTH_SHORT).show();

            return;
        }
        //  if () {
        mImgs = allImgs;
//        } else {
//            mImgs = Arrays.asList(mImgDir.list());
//        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(this, mImgs,
                R.layout.grid_item, mImgDir.getAbsolutePath());
        mAdapter.setmDirPath("所有图片");
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount + "张");
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.65),
                mImageFloders, getLayoutInflater()
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                viewsContainer.setVisibility(View.GONE);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bottom_ly:
                /**
                 * 为底部的布局设置点击事件，弹出popupWindow
                 */
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                // 设置背景颜色变暗
                /*WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);*/
                viewsContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.viewsContainer://点击关闭
                viewsContainer.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = new ProgressDialog(this);

        ImageFloder imgFloder = new ImageFloder();
        imgFloder.setCustomName("所有图片");
        imgFloder.setTag(false);
        mImageFloders.add(imgFloder);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Cursor mCursor = null;
                Uri mImageUri = Media.EXTERNAL_CONTENT_URI;
                try {
                    ContentResolver mContentResolver = AlbumActivity.this
                            .getContentResolver();

                    // 只查询jpeg和png的图片
                    mCursor = mContentResolver.query(mImageUri, null,
                            Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=? or "
                                    + Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png", "image/jpg"},
                            //       Media.DATE_MODIFIED + " DESC");
                            Media.DATE_ADDED + " DESC");
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(Media.DATA));
                        allImgs.add(path);
                        //    Log.e("TAG", path); 打印图片 path
                        // 拿到第一张图片的路径
                        if (firstImage == null)
                            firstImage = path;
                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;
                        //绝对路径
                        String dirPath = parentFile.getAbsolutePath();
                        ImageFloder imageFloder;
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                        }

                        try {
                            int picSize = parentFile.list(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String filename) {
                                    return filename.toLowerCase().endsWith(".jpg")
                                            || filename.toLowerCase().endsWith(".png")
                                            || filename.toLowerCase().endsWith(".jpeg");
                                }
                            }).length;
                            totalCount += picSize;
                            imageFloder.setCount(picSize);
                            mImageFloders.add(imageFloder);

                            if (picSize > mPicsSize) {
                                mPicsSize = picSize;
                                mImgDir = parentFile;
                            }
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception ex) {
                    Log.i("扫描图库错误，错误原因" , ex.getMessage());
                    mHandler.sendEmptyMessage(2);
                }

                //     Log.e("TAG", "---------------------------------            " + mCursor.getCount() + "");

                try {
                    //全部图片 ImageFloder
                    mImageFloders.get(0).setFirstImagePath(mImageFloders.get(1).getFirstImagePath());
                    mImageFloders.get(0).setCount(totalCount);
                    mCursor.close();

                    // 扫描完成，辅助的HashSet也就可以释放内存了
                    mDirPaths = null;
                    // 通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(1);
                } catch (Exception ex) {
                    Log.i("获取全部图片失败，原因:" , ex.getMessage());
                    mHandler.sendEmptyMessage(2);
                }
            }
        }).start();

    }

    @Override
    public void selected(ImageFloder floder) {
        if (floder.getTag()) {
            mImgDir = new File(floder.getDir());
            mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png")
                            || filename.toLowerCase().endsWith(".jpeg");
                }
            }));
            mAdapter.setmDirPath(mImgDir.getAbsolutePath());
            mChooseDir.setText(floder.getName());
        } else {
            mImgs = allImgs;
            mAdapter.setmDirPath("所有图片");
            mChooseDir.setText("所有图片");
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter.setDatas(mImgs);
        mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        viewsContainer.setVisibility(View.GONE);
        mListImageDirPopupWindow.dismiss();
    }


    public void refreshTitleBtn() {
        mTitle.setRightBtn_Album(MyAdapter.mSelectedImage.size(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selectImage= MyAdapter.mSelectedImage.toArray(new String[MyAdapter.mSelectedImage.size()]);
                Intent intent = new Intent();
                intent.putExtra( ALBUM_RESULT, selectImage);
                setResult( RESULT_OK, intent );

                finish();
                MyAdapter.mSelectedImage.clear();//如果需要让相册选择框架具有记忆功能，请删除此项
                finish();

            }
        }, MaxSelectNum);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
        //  MyAdapter.mSelectedImage.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"正在为您处理刚刚拍好的图片，请稍等...",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlbumActivity.this, AlbumActivity.class);
        intent.putExtra("MAX_Length", requestCode);
        startActivity(intent);
        finish();
    }
}

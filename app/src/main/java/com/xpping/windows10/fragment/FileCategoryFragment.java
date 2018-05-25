
package com.xpping.windows10.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.windows.explorer.adapter.FileListCursorAdapter;
import com.windows.explorer.entity.FavoriteList;
import com.windows.explorer.entity.FileInfo;
import com.windows.explorer.entity.GlobalConsts;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.windows.explorer.helper.FavoriteDatabaseHelper;
import com.windows.explorer.helper.FileCategoryHelper;
import com.windows.explorer.helper.FileIconHelper;
import com.windows.explorer.helper.FileSortHelper;
import com.windows.explorer.listener.IFileInteractionListener;
import com.windows.explorer.utils.FileViewInteractionHub;
import com.windows.explorer.utils.Util;
import com.xpping.windows10.utils.FragmentUtils;
import com.xpping.windows10.widget.ToastView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/*
 *xlzhen 2018/4/27
 * 文件管理器 仿win10我的电脑打开
 */
public class FileCategoryFragment extends BaseFragment implements IFileInteractionListener,
        FavoriteDatabaseHelper.FavoriteDatabaseListener {

    public static final String EXT_FILETER_KEY = "ext_filter";

    private static final String LOG_TAG = "FileCategoryActivity";

    private static HashMap<Integer, FileCategoryHelper.FileCategory> button2Category = new HashMap<>();

    private HashMap<FileCategoryHelper.FileCategory, Integer> categoryIndex = new HashMap<>();

    private FileListCursorAdapter mAdapter;

    private FileViewInteractionHub mFileViewInteractionHub;

    private FileCategoryHelper mFileCagetoryHelper;

    private FileIconHelper mFileIconHelper;

    private ProgressBar mCategoryBar;

    private ScannerReceiver mScannerReceiver;

    private FavoriteList mFavoriteList;

    private ViewPage curViewPage = ViewPage.Invalid;

    private ViewPage preViewPage = ViewPage.Invalid;

    private MainActivity mActivity;

    private FileViewFragment fileViewFragment;

    private boolean mConfigurationChanged = false;

    public void setConfigurationChanged(boolean changed) {
        mConfigurationChanged = changed;
    }

    static {
        button2Category.put(R.id.category_music, FileCategoryHelper.FileCategory.Music);
        button2Category.put(R.id.category_video, FileCategoryHelper.FileCategory.Video);
        button2Category.put(R.id.category_picture, FileCategoryHelper.FileCategory.Picture);
        button2Category.put(R.id.category_document, FileCategoryHelper.FileCategory.Doc);
        button2Category.put(R.id.category_apk, FileCategoryHelper.FileCategory.Apk);
        button2Category.put(R.id.category_favorite, FileCategoryHelper.FileCategory.Favorite);
    }

    @Override
    protected void initData() {
        curViewPage = ViewPage.Invalid;
        mFileViewInteractionHub = new FileViewInteractionHub(this);
        mFileViewInteractionHub.setMode(FileViewInteractionHub.Mode.View);
        mFileViewInteractionHub.setRootPath("/");
        mFileIconHelper = new FileIconHelper(mActivity);
        mFavoriteList = new FavoriteList(mActivity, (ListView) getFragmentView().findViewById(R.id.favorite_list), this, mFileIconHelper);
        mFavoriteList.initList();
        mAdapter = new FileListCursorAdapter(mActivity, null, mFileViewInteractionHub, mFileIconHelper);

        findViewById(R.id.disk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由直接调起转换成发消息，防止崩溃
                Intent intent = new Intent(MAIN_BROADCAST);
                intent.putExtra("message", "openFileViewFragment");
                v.getContext().sendBroadcast(intent);
            }
        });

        ListView fileListView = getFragmentView().findViewById(R.id.file_path_list);
        fileListView.setAdapter(mAdapter);

        setupClick();
        setupCategoryInfo();
        updateUI();
        registerScannerReceiver();
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        fileViewFragment = (FileViewFragment) mActivity.getFileViewFragment();
        return setFragmentView(inflater.inflate(R.layout.fragment_file_explorer_category, container, false));
    }

    private void registerScannerReceiver() {
        mScannerReceiver = new ScannerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        mActivity.registerReceiver(mScannerReceiver, intentFilter);
    }

    private void setupCategoryInfo() {
        mFileCagetoryHelper = new FileCategoryHelper(mActivity);

        mCategoryBar = getFragmentView().findViewById(R.id.category_bar);
        /*int[] imgs = new int[] {
                R.drawable.category_bar_music, R.drawable.category_bar_video,
                R.drawable.category_bar_picture, R.drawable.category_bar_document,
                R.drawable.category_bar_apk, R.drawable.category_bar_other
        };

        for (int i = 0; i < imgs.length; i++) {
            mCategoryBar.addCategory(imgs[i]);
        }*/
        for (int i = 0; i < FileCategoryHelper.sCategories.length; i++) {
            categoryIndex.put(FileCategoryHelper.sCategories[i], i);
        }
    }

    public void refreshCategoryInfo() {
        Util.SDCardInfo sdCardInfo = Util.getSDCardInfo();
        if (sdCardInfo != null) {
            int total = Util.convertStorage(sdCardInfo.total, 0);
            int progress = Util.convertStorage(sdCardInfo.total - sdCardInfo.free, 0);
            mCategoryBar.setMax(total);
            mCategoryBar.setProgress(progress);
            setTextView(R.id.sd_card_type, Util.convertStorage(sdCardInfo.free) + "可用，共" + Util.convertStorage(sdCardInfo.total));
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限不足
            ToastView.getInstaller(mActivity).setText("没有内存卡读写权限,无法打开个人文件夹！").show();
            FragmentUtils.closeFragment(this);
            return;
        }
        mFileCagetoryHelper.refreshCategoryInfo();

        // the other category size should include those files didn't get scanned.
        long size = 0;
        for (FileCategoryHelper.FileCategory fc : FileCategoryHelper.sCategories) {
            FileCategoryHelper.CategoryInfo categoryInfo = mFileCagetoryHelper.getCategoryInfos().get(fc);
            setCategoryCount(fc, categoryInfo.count);

            // other category size should be set separately with calibration
            if (fc == FileCategoryHelper.FileCategory.Other)
                continue;

            setCategorySize(fc, categoryInfo.size);
            setCategoryBarValue(fc, categoryInfo.size);
            size += categoryInfo.size;
        }

        if (sdCardInfo != null) {
            long otherSize = sdCardInfo.total - sdCardInfo.free - size;
            setCategorySize(FileCategoryHelper.FileCategory.Other, otherSize);
            setCategoryBarValue(FileCategoryHelper.FileCategory.Other, otherSize);
        }

        setCategoryCount(FileCategoryHelper.FileCategory.Favorite, mFavoriteList.getCount());

        /*if (mCategoryBar.getVisibility() == View.VISIBLE) {
            mCategoryBar.startAnimation();
        }*/
    }

    public enum ViewPage {
        Home, Favorite, Category, NoSD, Invalid
    }

    private void showPage(ViewPage p) {
        if (curViewPage == p) return;

        curViewPage = p;

        showView(R.id.file_path_list, false);
        showView(R.id.navigation_bar, false);
        showView(R.id.category_page, false);
        //showView(R.id.operation_bar, false);
        showView(R.id.sd_not_available_page, false);
        mFavoriteList.show(false);
        showEmptyView(false);

        switch (p) {
            case Home:
                showView(R.id.category_page, true);
                if (mConfigurationChanged) {
                    //((MainActivity) mActivity).reInstantiateCategoryTab();
                    mConfigurationChanged = false;
                }
                break;
            case Favorite:
                showView(R.id.navigation_bar, true);
                mFavoriteList.show(true);
                showEmptyView(mFavoriteList.getCount() == 0);
                break;
            case Category:
                showView(R.id.navigation_bar, true);
                showView(R.id.file_path_list, true);
                showEmptyView(mAdapter.getCount() == 0);
                break;
            case NoSD:
                showView(R.id.sd_not_available_page, true);
                break;
        }
    }

    private void showEmptyView(boolean show) {
        View emptyView = mActivity.findViewById(R.id.empty_view);
        if (emptyView != null)
            emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showView(int id, boolean show) {
        try {
            View view = getFragmentView().findViewById(id);
            if (view != null) {
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        } catch (Exception ex) {
            //有几率存在getFragmentView为空
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileCategoryHelper.FileCategory f = button2Category.get(v.getId());
            if (f != null) {
                onCategorySelected(f);
                if (f != FileCategoryHelper.FileCategory.Favorite) {
                    setHasOptionsMenu(true);
                }
            }
        }

    };

    private void setCategoryCount(FileCategoryHelper.FileCategory fc, long count) {
        int id = getCategoryCountId(fc);
        if (id == 0)
            return;
        switch (id) {
            case R.id.music_text:
                setTextView(id, "音乐 " + count + " 份");
                break;
            case R.id.video_text:
                setTextView(id, "视频 " + count + " 份");
                break;
            case R.id.pic_text:
                setTextView(id, "图片 " + count + " 份");
                break;
            case R.id.apk_text:
                setTextView(id, "安装包 " + count + " 份");
                break;
            case R.id.doc_text:
                setTextView(id, "文档 " + count + " 份");
                break;
            case R.id.fav_text:
                setTextView(id, "收藏夹 " + count + " 份");
                break;
        }

    }

    private void setTextView(int id, String t) {
        TextView text = getFragmentView().findViewById(id);
        text.setText(t);
    }

    private void onCategorySelected(FileCategoryHelper.FileCategory f) {
        if (mFileCagetoryHelper.getCurCategory() != f) {
            mFileCagetoryHelper.setCurCategory(f);
            mFileViewInteractionHub.setCurrentPath(mFileViewInteractionHub.getRootPath()
                    + getString(mFileCagetoryHelper.getCurCategoryNameResId()));
            mFileViewInteractionHub.refreshFileList();
        }

        if (f == FileCategoryHelper.FileCategory.Favorite) {
            showPage(ViewPage.Favorite);
        } else {
            showPage(ViewPage.Category);
        }
    }

    private void setupClick(int id) {
        View button = getFragmentView().findViewById(id);
        button.setOnClickListener(onClickListener);
    }

    private void setupClick() {
        setupClick(R.id.category_music);
        setupClick(R.id.category_video);
        setupClick(R.id.category_picture);
        setupClick(R.id.category_document);
        setupClick(R.id.category_apk);
        setupClick(R.id.category_favorite);

    }

    public boolean isHomePage() {
        return curViewPage == ViewPage.Home;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (curViewPage != ViewPage.Category && curViewPage != ViewPage.Favorite) {
            return;
        }
        mFileViewInteractionHub.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!isHomePage() && mFileCagetoryHelper.getCurCategory() != FileCategoryHelper.FileCategory.Favorite) {
            mFileViewInteractionHub.onPrepareOptionsMenu(menu);
        }
    }

    public boolean onRefreshFileList(String path, FileSortHelper sort) {
        FileCategoryHelper.FileCategory curCategory = mFileCagetoryHelper.getCurCategory();
        if (curCategory == FileCategoryHelper.FileCategory.Favorite || curCategory == FileCategoryHelper.FileCategory.All)
            return false;

        Cursor c = mFileCagetoryHelper.query(curCategory, sort.getSortMethod());
        showEmptyView(c == null || c.getCount() == 0);
        mAdapter.changeCursor(c);

        return true;
    }

    @Override
    public View getViewById(int id) {
        return getFragmentView().findViewById(id);
    }

    @Override
    public MainActivity getContext() {
        return mActivity;
    }

    @Override
    public void onDataChanged() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mFavoriteList.getArrayAdapter().notifyDataSetChanged();
                showEmptyView(mAdapter.getCount() == 0);
            }

        });
    }

    @Override
    public void onPick(FileInfo f) {
        // do nothing
    }

    @Override
    public boolean shouldShowOperationPane() {
        return true;
    }

    @Override
    public void setActionBarPath(String actionBarTitle) {
        setActionBarTitle(actionBarTitle);
    }

    @Override
    public boolean onOperation(int id) {
        mFileViewInteractionHub.addContextMenuSelectedItem();
        switch (id) {
            //case R.id.button_operation_copy:
            case GlobalConsts.MENU_COPY:
                copyFileInFileView(mFileViewInteractionHub.getSelectedFileList());
                mFileViewInteractionHub.clearSelection();
                break;
            //case R.id.button_operation_move:
            case GlobalConsts.MENU_MOVE:
                startMoveToFileView(mFileViewInteractionHub.getSelectedFileList());
                mFileViewInteractionHub.clearSelection();
                break;
            case GlobalConsts.OPERATION_UP_LEVEL:
                setHasOptionsMenu(false);
                showPage(ViewPage.Home);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public String getDisplayPath(String path) {
        return "分类浏览" + path;
    }

    @Override
    public String getRealPath(String displayPath) {
        return "";
    }

    @Override
    public boolean onNavigation(String path) {
        showPage(ViewPage.Home);
        return true;
    }

    @Override
    public boolean shouldHideMenu(int menu) {
        return (menu == GlobalConsts.MENU_NEW_FOLDER || menu == GlobalConsts.MENU_FAVORITE
                || menu == GlobalConsts.MENU_PASTE || menu == GlobalConsts.MENU_SHOWHIDE);
    }

    @Override
    public void addSingleFile(FileInfo file) {
        refreshList();
    }

    @Override
    public Collection<FileInfo> getAllFiles() {
        return mAdapter.getAllFiles();
    }

    @Override
    public FileInfo getItem(int pos) {
        return mAdapter.getFileItem(pos);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getCount();
    }

    @Override
    public void sortCurrentList(FileSortHelper sort) {
        refreshList();
    }

    private void refreshList() {
        mFileViewInteractionHub.refreshFileList();
    }

    private void copyFileInFileView(ArrayList<FileInfo> files) {
        if (files.size() == 0) return;
        fileViewFragment.copyFile((MainActivity) mActivity, files);
        //mActivity.getActionBar().setSelectedNavigationItem(Util.SDCARD_TAB_INDEX);
    }

    private void startMoveToFileView(ArrayList<FileInfo> files) {
        if (files.size() == 0) return;
        fileViewFragment.moveToFile(files);
        //mActivity.getActionBar().setSelectedNavigationItem(Util.SDCARD_TAB_INDEX);
    }

    @Override
    public FileIconHelper getFileIconHelper() {
        return mFileIconHelper;
    }

    private static int getCategoryCountId(FileCategoryHelper.FileCategory fc) {
        switch (fc) {
            case Music:
                return R.id.music_text;
            case Video:
                return R.id.video_text;
            case Picture:
                return R.id.pic_text;
            case Doc:
                return R.id.doc_text;
            case Apk:
                return R.id.apk_text;
            case Favorite:
                return R.id.fav_text;
        }

        return 0;
    }

    private void setCategorySize(FileCategoryHelper.FileCategory fc, long size) {
        int txtId = 0;
        int resId = 0;
        switch (fc) {
            case Music:
                txtId = R.id.category_legend_music;
                resId = R.string.category_music;
                break;
            case Video:
                txtId = R.id.category_legend_video;
                resId = R.string.category_video;
                break;
            case Picture:
                txtId = R.id.category_legend_picture;
                resId = R.string.category_picture;
                break;

            case Doc:
                txtId = R.id.category_legend_document;
                resId = R.string.category_document;
                break;

            case Apk:
                txtId = R.id.category_legend_apk;
                resId = R.string.category_apk;
                break;
            case Other:
                txtId = R.id.category_legend_other;
                resId = R.string.category_other;
                break;
        }

        if (txtId == 0 || resId == 0)
            return;

        setTextView(txtId, getString(resId) + ":" + Util.convertStorage(size));
    }

    private void setCategoryBarValue(FileCategoryHelper.FileCategory f, long size) {
        /*if (mCategoryBar == null) {
            mCategoryBar = (CategoryBar) getFragmentView().findViewById(R.id.category_bar);
        }
        mCategoryBar.setCategoryValue(categoryIndex.get(f), size);*/
    }

    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.unregisterReceiver(mScannerReceiver);
        }
    }

    @Override
    protected void onClick(View view, int viewId) {

    }

    @Override
    public boolean onBackKey() {
        if (isHomePage() || curViewPage == ViewPage.NoSD || mFileViewInteractionHub == null) {
            return false;
        }

        return !mFileViewInteractionHub.onBackPressed();
    }

    private class ScannerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(LOG_TAG, "received broadcast: " + action.toString());
            // handle intents related to external storage
            if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED) || action.equals(Intent.ACTION_MEDIA_MOUNTED)
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                notifyFileChanged();
            }
        }
    }

    private void updateUI() {
        boolean sdCardReady = Util.isSDCardReady();
        if (sdCardReady) {
            if (preViewPage != ViewPage.Invalid) {
                showPage(preViewPage);
                preViewPage = ViewPage.Invalid;
            } else if (curViewPage == ViewPage.Invalid || curViewPage == ViewPage.NoSD) {
                showPage(ViewPage.Home);
            }
            refreshCategoryInfo();
            // refresh file list
            mFileViewInteractionHub.refreshFileList();
            // refresh file list view in another tab
            if (fileViewFragment != null)
                fileViewFragment.refresh();
        } else {
            preViewPage = curViewPage;
            showPage(ViewPage.NoSD);
        }
    }

    // process file changed notification, using a timer to avoid frequent
    // refreshing due to batch changing on file system
    synchronized public void notifyFileChanged() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                timer = null;
                Message message = new Message();
                message.what = MSG_FILE_CHANGED_TIMER;
                handler.sendMessage(message);
            }

        }, 1000);
    }

    private static final int MSG_FILE_CHANGED_TIMER = 100;

    private Timer timer;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FILE_CHANGED_TIMER:
                    updateUI();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    // update the count of favorite
    @Override
    public void onFavoriteDatabaseChanged() {
        setCategoryCount(FileCategoryHelper.FileCategory.Favorite, mFavoriteList.getCount());
    }

    @Override
    public void runOnUiThread(Runnable r) {
        mActivity.runOnUiThread(r);
    }
}

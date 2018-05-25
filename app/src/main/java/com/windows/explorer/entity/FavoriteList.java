
package com.windows.explorer.entity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.windows.explorer.adapter.FavoriteListAdapter;
import com.windows.explorer.builder.IntentBuilder;
import com.xpping.windows10.fragment.FileViewFragment;
import com.windows.explorer.helper.FavoriteDatabaseHelper;
import com.windows.explorer.helper.FileIconHelper;
import com.windows.explorer.utils.Util;

import java.io.File;
import java.util.ArrayList;

public class FavoriteList implements FavoriteDatabaseHelper.FavoriteDatabaseListener {
    private static final String LOG_TAG = "FavoriteList";

    private ArrayList<FavoriteItem> mFavoriteList = new ArrayList<FavoriteItem>();

    private FavoriteListAdapter mFavoriteListAdapter;

    private FavoriteDatabaseHelper mFavoriteDatabase;

    private ListView mListView;

    private FavoriteDatabaseHelper.FavoriteDatabaseListener mListener;

    private MainActivity mContext;

    public FavoriteList(MainActivity context, ListView list, FavoriteDatabaseHelper.FavoriteDatabaseListener listener,
                        FileIconHelper iconHelper) {
        mContext = context;

        mFavoriteDatabase = new FavoriteDatabaseHelper(context, this);
        mFavoriteListAdapter = new FavoriteListAdapter(context,
                mFavoriteList, iconHelper);
        setupFavoriteListView(list);
        mListener = listener;
    }

    public FavoriteListAdapter getArrayAdapter() {
        return mFavoriteListAdapter;
    }

    public void update() {
        mFavoriteList.clear();

        Cursor c = mFavoriteDatabase.query();
        if (c != null) {
            while (c.moveToNext()) {
                FavoriteItem item = new FavoriteItem(c.getLong(0), c.getString(1), c.getString(2));
                item.setFileInfo( Util.GetFileInfo(item.getLocation()));
                mFavoriteList.add(item);
            }
            c.close();
        }

        // remove not existing items
        if (Util.isSDCardReady()) {
            for (int i = mFavoriteList.size() - 1; i >= 0; i--) {
                File file = new File(mFavoriteList.get(i).getLocation());
                if (file.exists())
                    continue;

                FavoriteItem favorite = mFavoriteList.get(i);
                mFavoriteDatabase.delete(favorite.getId(), false);
                mFavoriteList.remove(i);
            }
        }

        mFavoriteListAdapter.notifyDataSetChanged();
    }

    public void initList() {
        mFavoriteList.clear();
        Cursor c = mFavoriteDatabase.query();
        if (c != null)
            c.close();

        if (mFavoriteDatabase.isFirstCreate()) {
            for (FavoriteItem fi : Util.getDefaultFavorites(mContext)) {
                mFavoriteDatabase.insert(fi.getTitle(), fi.getLocation());
            }
        }

        update();
    }

    public long getCount() {
        return mFavoriteList.size();
    }

    public void show(boolean show) {
        mListView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setupFavoriteListView(ListView list) {
        mListView = list;
        mListView.setAdapter(mFavoriteListAdapter);
        mListView.setLongClickable(true);
        mListView.setOnCreateContextMenuListener(mListViewContextMenuListener);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFavoriteListItemClick(parent, view, position, id);
            }
        });
    }

    public void onFavoriteListItemClick(AdapterView<?> parent, View view, int position, long id) {
        FavoriteItem favorite = mFavoriteList.get(position);

        if (favorite.getFileInfo().isDir()) {
            MainActivity activity = mContext;
            ((FileViewFragment) activity.getFileViewFragment())
                    .setPath(favorite.getLocation());
            //activity.getActionBar().setSelectedNavigationItem(Util.SDCARD_TAB_INDEX);
        } else {
            try {
                IntentBuilder.viewFile(mContext, favorite.getFileInfo().getFilePath());
            } catch (ActivityNotFoundException e) {
                Log.e(LOG_TAG, "fail to view file: " + e.toString());
            }
        }
    }

    private static final int MENU_UNFAVORITE = 100;

    // context menu
    private OnCreateContextMenuListener mListViewContextMenuListener = new OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            menu.add(0, MENU_UNFAVORITE, 0, R.string.operation_unfavorite)
                    .setOnMenuItemClickListener(menuItemClick);
        }
    };

    private OnMenuItemClickListener menuItemClick = new OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            int position = info != null ? info.position : -1;

            switch (itemId) {
                case MENU_UNFAVORITE:
                    if (position != -1) {
                        deleteFavorite(position);
                    }
                    break;

                default:
                    return false;
            }

            return true;
        }
    };

    private void deleteFavorite(int position) {
        FavoriteItem favorite = mFavoriteList.get(position);
        mFavoriteDatabase.delete(favorite.getId(), false);
        mFavoriteList.remove(position);
        mFavoriteListAdapter.notifyDataSetChanged();
        mListener.onFavoriteDatabaseChanged();
    }

    @Override
    public void onFavoriteDatabaseChanged() {
        update();
        mListener.onFavoriteDatabaseChanged();
    }
}

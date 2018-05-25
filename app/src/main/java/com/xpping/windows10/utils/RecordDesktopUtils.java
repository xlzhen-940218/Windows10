package com.xpping.windows10.utils;

import android.content.Context;
import android.util.Log;

import com.xpping.windows10.db.LocalCache;
import com.xpping.windows10.db.MeCacheBusiness;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlzhen on 10/31 0031.
 * 搜索记录存储 实体类
 */

public class RecordDesktopUtils {
    @SuppressWarnings("unchecked")
    public  static <T> void saveDesktopData(Context context, T btResult,Class<T> tClass,String key) {
        List<T> btHistories = getDesktopData(context,tClass,key);
        btHistories.add(btResult);
        LocalCache cache = new LocalCache();
        cache.setKey(key);
        cache.setResult(FastJSONParser.getJsonString(btHistories));
        try {
            MeCacheBusiness.getInstance(context).createOrUpdate(cache);
        } catch (IllegalStateException ignored) {

        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void saveDesktopData(Context context, List<T> btResult, Class<T> tClass, String key) {
        List<T> btHistories = getDesktopData(context,tClass,key);
        btHistories.addAll(btResult);
        LocalCache cache = new LocalCache();
        cache.setKey(key);
        cache.setResult(FastJSONParser.getJsonString(btHistories));
        try {
            MeCacheBusiness.getInstance(context).createOrUpdate(cache);
        } catch (IllegalStateException ignored) {

        }
    }

    public static <T> List<T> getDesktopData(Context context, Class<T> tClass, String key) {
        try {
            LocalCache nc = MeCacheBusiness.getInstance(context).queryBykey(key);
            List<T> btHistories = new ArrayList<>();
            try {
                btHistories = FastJSONParser.getBeanList(nc.getResult(), tClass);
            } catch (Exception ex) {
                Log.v("取出错误，错误原因", "空数据");
            }
            return btHistories;
        } catch (IllegalStateException ex) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void removeAllDesktopData(Context context,String key) {
        LocalCache cache = new LocalCache();
        cache.setKey(key);
        cache.setResult(FastJSONParser.getJsonString(new ArrayList<T>()));
        MeCacheBusiness.getInstance(context).createOrUpdate(cache);
    }
}

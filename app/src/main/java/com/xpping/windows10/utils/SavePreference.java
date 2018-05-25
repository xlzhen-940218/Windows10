package com.xpping.windows10.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/*
*数据存储
*/
public class SavePreference {
    private static final String NAME = "common_config";

    @SuppressLint("UseValueOf")
    public static void save(Context ctx, String key, Object value) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof String) {
            sp.edit().putString(key, (String) value).apply();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Double) {
            sp.edit().putFloat(key, new Float((Double) value)).apply();
        } else if (value instanceof Long)
            sp.edit().putLong(key, (Long) value).apply();
    }

    // 一般配置
    public static String getStr(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getString(key, "");
    }

    public static String getStrDefault(Context ctx, String key, String str) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getString(key, str);
    }

    public static boolean getBoolean(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getBoolean(key, false);
    }

    public static boolean getBooleanDefaultTrue(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getBoolean(key, true);
    }

    public static int getInt(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getInt(key, -1);
    }

    public static float getFloat(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getFloat(key, 0);
    }

    public static Long getLong(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getLong(key, 0);
    }

    public static String getStrOhter(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        return sp.getString(key, null);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(NAME, 0);
        sp.edit().remove(key).apply();
    }


    /**
     * 清除所有数据
     *
     * @param context
     * @return 是否成功
     */
    public static boolean clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return 是否存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(
                NAME, 0);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return Map<String, ?>
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                NAME, 0);
        return sp.getAll();
    }


}

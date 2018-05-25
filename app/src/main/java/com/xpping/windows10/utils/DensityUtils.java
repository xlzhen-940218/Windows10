package com.xpping.windows10.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 * 单位转换 工具类<br>
 */
public class DensityUtils {

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        try {
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem()
                            .getDisplayMetrics());
        } catch (Exception ex) {
            return 0;
        }

    }

    /**
     * sp转px
     */
    public static int sp2px( float spVal) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, spVal, Resources.getSystem()
                        .getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (int) (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        return h;
    }

    public static String getStringInsert(String nums, char news_char, int location) {
        return nums.substring(0, location) + news_char + nums.substring(location, nums.length());
    }


}

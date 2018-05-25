package com.albums;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class SelectorUtils {

    public static StateListDrawable getDrawable(Context ctx, int defaultid, int pressid) {
        StateListDrawable drawable = new StateListDrawable();
        //Non focused states
        drawable.addState(new int[]{-android.R.attr.state_focused,
                -android.R.attr.state_selected, -android.R.attr.state_pressed}, ctx.getResources()
                .getDrawable(defaultid));
        //Pressed
        drawable.addState(new int[]{android.R.attr.state_pressed}, ctx.getResources()
                .getDrawable(pressid));
        //Pressed
        drawable.addState(new int[]{android.R.attr.state_selected}, ctx.getResources()
                .getDrawable(pressid));
        return drawable;
    }

    public static StateListDrawable getDrawableChecked(Context ctx, int defaultid, int pressid) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_selected, -android.R.attr.state_pressed}, ctx.getResources()
                .getDrawable(defaultid));
        drawable.addState(new int[]{android.R.attr.state_selected,}, ctx.getResources()
                .getDrawable(pressid));
        //checked
        drawable.addState(new int[]{android.R.attr.state_checked}, ctx.getResources()
                .getDrawable(pressid));
        return drawable;
    }

    public static StateListDrawable getDrawableWithSelected(Context ctx, int defaultid, int pressid) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_selected, -android.R.attr.state_pressed}, ctx.getResources()
                .getDrawable(defaultid));
        //selected
        drawable.addState(new int[]{android.R.attr.state_selected,}, ctx.getResources()
                .getDrawable(pressid));
        return drawable;
    }


    public static ColorStateList getColorStateList(Context ctx, int defaultid, int pressid) {

        int[] colors = new int[]{ctx.getResources().getColor(pressid),
                ctx.getResources().getColor(defaultid)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        //        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        //        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        //        states[2] = new int[] { android.R.attr.state_enabled };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList getColorListState(Context ctx, String color, int pressid) {

        int[] colors = new int[]{Color.parseColor(color), ctx.getResources().getColor(pressid),
                Color.parseColor(color)};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled};
        states[2] = new int[]{};
        //        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        //        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        //        states[2] = new int[] { android.R.attr.state_enabled };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList getColorStateBtn(Context ctx, int defaultid, int pressid) {

        int[] colors = new int[]{ctx.getResources().getColor(pressid), ctx.getResources().getColor(pressid), ctx.getResources().getColor(pressid),
                ctx.getResources().getColor(defaultid)};
        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{};
        return new ColorStateList(states, colors);
    }


    public GradientDrawable getShape(int fillColor, int roundRadius, int strokeWidth,
                                     int strokeColor) {

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    public static StateListDrawable getDrawableWithDrawa(Drawable pressid, Drawable defaultid) {
        StateListDrawable drawable = new StateListDrawable();
        //Non focused states
        drawable.addState(new int[]{-android.R.attr.state_focused,
                -android.R.attr.state_selected, -android.R.attr.state_pressed}, defaultid);
        //clickable
        drawable.addState(new int[]{android.R.attr.clickable}, pressid);
        //clickable
        drawable.addState(new int[]{android.R.attr.state_focused}, pressid);
        //Pressed
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressid);
        //Selected
        drawable.addState(new int[]{android.R.attr.state_selected}, pressid);
        return drawable;
    }


}
package com.albums;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.utils.DensityUtils;


public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private int width,height;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
        width=height= DensityUtils.getScreenW(context)/3;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context     ctx
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return VIewHolder
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, parent, layoutId, position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
        }
        return holder;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId Id
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId id
     * @param text   内容
     * @return ViewHolder
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId     Id
     * @param drawableId 资源id
     * @return ViewHolder
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        BaseApplication.imageLoader.displayImage("drawable://"+drawableId
                ,view);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId Id
     * @param bm     资源bitmap
     * @return ViewHolder
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId       Id
     * @param url（drwable）
     * @return ViewHolder
     */
    public ViewHolder setImageByUrl(int viewId, String url) {
        BaseApplication.imageLoader.displayImage("file://"+url,(ImageView) getView(viewId),new ImageSize(width,width));
        return this;
    }

    public ViewHolder setImageRes(ImageView img, String url) {
        BaseApplication.imageLoader.displayImage("file://"+url,(ImageView) img,new ImageSize(width,width));
        return this;
    }

    public void setVisible(int viewId, int visible) {
        getView(viewId).setVisibility(visible);
    }


    public int getPosition() {
        return mPosition;
    }

}

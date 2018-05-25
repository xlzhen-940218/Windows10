package com.xlzhen.cathouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.xlzhen.cathouse.entity.PictureEntity;
import com.xpping.windows10.utils.DensityUtils;

import java.util.List;

/**
 * Created by xlzhen on 2018/2/24.
 * 美猫 列表 适配器
 */
public class HeaderBottomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //item类型
    public static final int ITEM_TYPE_CONTENT = 1;

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mBottomCount = 1;//底部View个数
    private List<PictureEntity> mPictureEntities;
    private int screenWidth;

    public HeaderBottomAdapter(Context context,int width, List<PictureEntity> pictureEntities) {
        mPictureEntities = pictureEntities;
        mContext = context;
        screenWidth = width - DensityUtils.dp2px(2);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<PictureEntity> pictureEntities) {
        mPictureEntities = pictureEntities;
        notifyDataSetChanged();
    }

    public void addData(List<PictureEntity> pictureEntities) {
        mPictureEntities.addAll(pictureEntities);
        notifyDataSetChanged();
    }

    public void addData(List<PictureEntity> pictureEntities, boolean isBackground) {
        mPictureEntities.addAll(pictureEntities);
        notifyDataSetChanged();
        if (isBackground) {
            Intent intent = new Intent(MainActivity.MAIN_BROADCAST);
            intent.putExtra("message", "updateCatPhotoFragment");
            intent.putExtra("data", JSON.toJSONString(mPictureEntities));
            mContext.sendBroadcast(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_CONTENT;
    }

    //内容长度
    public int getContentItemCount() {
        return mPictureEntities.size();
    }

    public void setWidth(int width) {
        screenWidth = width - DensityUtils.dp2px(2);
        notifyDataSetChanged();
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            //textView=(TextView)itemView.findViewById(R.id.tv_item_text);
            pictureView = itemView.findViewById(R.id.pictureView);

        }
    }

    //底部 ViewHolder
    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            View view = mLayoutInflater.inflate(R.layout.adapter_picture_list_layout, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //进入图片预览页

                    Intent intent = new Intent(MainActivity.MAIN_BROADCAST);
                    intent.putExtra("message","openCatPhotoFragment");
                    intent.putExtra("picList",JSON.toJSONString(mPictureEntities));
                    intent.putExtra("picPosition",(int) view.getTag());
                    mContext.sendBroadcast(intent);
                }
            });
            return new ContentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            int width = screenWidth / 2;
            float po = (float) width / mPictureEntities.get(position).getFile().getWidth();
            int height = (int) (mPictureEntities.get(position).getFile().getHeight() * po);
            ((ContentViewHolder) holder).pictureView.setLayoutParams(new RecyclerView.LayoutParams(width, height));

            DisplayImageOptions.Builder options=new  DisplayImageOptions.Builder()
                    .showImageForEmptyUri(new ColorDrawable(Color.parseColor("#"
                            + (TextUtils.isEmpty(mPictureEntities.get(position).getFile().getTheme()) ? "000000"
                    : mPictureEntities.get(position).getFile().getTheme()))));

            BaseApplication.imageLoader.displayImage("http://hbimg.b0.upaiyun.com/"
                    + mPictureEntities.get(position).getFile().getKey() + "_fw320"
                    ,((ContentViewHolder) holder).pictureView,options.build());


            ((ContentViewHolder) holder).pictureView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount();
    }
}

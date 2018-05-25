package com.photoview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;


import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import android.widget.TextView;


import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xlzhen.cathouse.entity.PictureEntity;
import com.xpping.windows10.utils.DensityUtils;


import java.util.List;
import java.util.Random;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by xlzhen on 1/9 0009.
 * 图片浏览器 适配器
 */

public class PhotoPagerAdapter extends PagerAdapter {
    private List<PictureEntity> mPictureEntities;
    private Context context;
    private int width,height;
    public PhotoPagerAdapter(Context context,List<PictureEntity> pictureEntities) {
        this.context=context;
        width= DensityUtils.getScreenW(context)/2;
        height=DensityUtils.getScreenH(context)/2;
        mPictureEntities=pictureEntities;
    }

    public void addData(List<PictureEntity> pictureEntities){
        mPictureEntities.addAll(pictureEntities);
        notifyDataSetChanged();
    }

    public void setData(List<PictureEntity> pictureEntities){
        mPictureEntities=pictureEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPictureEntities.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        FrameLayout frameLayout=new FrameLayout(context);
        TextView textView=new TextView(context);
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(DensityUtils.dp2px(10)
                ,DensityUtils.dp2px(2),DensityUtils.dp2px(10),
                DensityUtils.dp2px(2));
        textView.setBackgroundResource(R.drawable.btn_bottom);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(mPictureEntities.get(position).getBoard().getTitle().length()>10
                ?mPictureEntities.get(position).getBoard().getTitle().substring(0,10)+"..."
                :mPictureEntities.get(position).getBoard().getTitle());
        textView.setTag(mPictureEntities.get(position).getLink());
        int heightScale=500;
        if(mPictureEntities.get(position).getFile().getWidth()
                >mPictureEntities.get(position).getFile().getHeight())
            heightScale=100;
        int tempW=200-new Random().nextInt(550)+width;
        int tempH=heightScale-new Random().nextInt(heightScale*2)+height;

        textView.setX(tempW);
        textView.setY(tempH);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse((String) view.getTag()));
                    context.startActivity(intent);

                }catch (Exception ex){}
            }
        });
        PhotoView photoView=new PhotoView(context);
        BaseApplication.imageLoader.displayImage(TextUtils.isEmpty(mPictureEntities.get(position).getFile().getPath())
                ?"http://hbimg.b0.upaiyun.com/" + mPictureEntities.get(position).getFile().getKey()
                :"file://"+mPictureEntities.get(position).getFile().getPath(),photoView
                ,new ImageSize(mPictureEntities.get(position).getFile().getWidth()
                , mPictureEntities.get(position).getFile().getHeight()));


        frameLayout.addView(photoView);
        frameLayout.addView(textView);
        container.addView(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return frameLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

package com.albums;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpping.windows10.R;


/**
 * Created by Administrator on 2015/6/8.
 * 这个是titleview，支持安卓4.4的沉浸栏效果
 */
public class TitleView extends FrameLayout {
    private Context context;
    public TitleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);
        this.context = context;

        ImageView backButton=(ImageView)findViewById(R.id.backButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }

    private TextView rightAlbum_tv;

    //相册选中多图
    public void setRightBtn_Album(int length, OnClickListener onClickListener, int maxLength) {
        if (rightAlbum_tv == null) {
            rightAlbum_tv=(TextView) findViewById(R.id.rightAlbum_tv);
            rightAlbum_tv.setVisibility(VISIBLE);
            rightAlbum_tv.setOnClickListener(onClickListener);
            rightAlbum_tv.setTextColor(SelectorUtils.getColorListState(context,"#fafafa", android.R.color.black));
        }

        if (length > 0) {
            rightAlbum_tv.setEnabled(true);
            rightAlbum_tv.setText("完成(" + length + "/" + maxLength + ")");
        } else {
            rightAlbum_tv.setEnabled(false);
            rightAlbum_tv.setText("完成");
        }
    }

}
package com.albums;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xpping.windows10.R;
import com.xpping.windows10.utils.DensityUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 图片gridview的adapter
 */
public class MyAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<>();

    public List<String> oldSelectedImage = new LinkedList<>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private int itemSize;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        itemSize = (DensityUtils.getScreenW(context) - DensityUtils.dp2px(12)) / 3;
        if (AlbumActivity.MaxSelectNum != 1) {
            for (String str : mSelectedImage)
                oldSelectedImage.add(str);
        }
    }

    public void setmDirPath(String dirPath) {
        mDirPath = dirPath;
    }

    public void setDatas(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
        RelativeLayout rlRoot = helper.getView(R.id.rl_dir_photo_root);
        rlRoot.getLayoutParams().width = itemSize;
        rlRoot.getLayoutParams().height = itemSize;
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        if (AlbumActivity.MaxSelectNum != 1) {
            //设置no_selected
            helper.setImageResource(R.id.id_item_select,
                    R.mipmap.picture_unselected);
            mSelect.setVisibility(View.VISIBLE);
            if ("所有图片".equals(mDirPath) && helper.getPosition() == 0) {
                mSelect.setVisibility(View.GONE);
            }

            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (mSelectedImage.contains("所有图片".equals(mDirPath) ? item : mDirPath + "/" + item)) {
                mSelect.setImageResource(R.mipmap.pictures_selected);
                mImageView.setColorFilter(Color.parseColor("#57000000"));
            }
            ((AlbumActivity) mContext).refreshTitleBtn();
        } else {
            mSelect.setVisibility(View.GONE);
        }

        //设置图片
        if ("所有图片".equals(mDirPath)) {
            if (helper.getPosition() == 0) {
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImageView.setImageResource(R.mipmap.photocam);
            } else {
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                helper.setImageRes(mImageView, item);
            }
        } else {
            helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
        }

        mImageView.setColorFilter(null);

        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
                                          //选择，则将图片变暗，反之则反之
                                          @SuppressWarnings("unchecked")
                                          @Override
                                          public void onClick(View v) {
                                              if (AlbumActivity.CAMERA_TAG.equals(item)) {
                                                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                  if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                                                      ContentValues contentValues = new ContentValues(2);
                                                      contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg");
                                                      contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                                      Uri mPhotoUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                                                      intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                                                      ((AlbumActivity) mContext).startActivityForResult(intent, AlbumActivity.MaxSelectNum);
                                                  }
                                                  return;
                                              }

                                              if (AlbumActivity.MaxSelectNum == 1) {
                                                  Intent intent = new Intent();
                                                  intent.setData(Uri.parse("所有图片".equals(mDirPath) ? item : mDirPath + "/" + item));
                                                  ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                                                  ((Activity) mContext).finish();
                                                  return;
                                              }
                                              // 已经选择过该图片
                                              if (mSelectedImage.contains("所有图片".equals(mDirPath) ? item : mDirPath + "/" + item)) {
                                                  mSelectedImage.remove("所有图片".equals(mDirPath) ? item : mDirPath + "/" + item);
                                                  mSelect.setImageResource(R.mipmap.picture_unselected);
                                                  mImageView.setColorFilter(null);
                                              } else
                                              // 未选择该图片
                                              {
                                                  if (AlbumActivity.MaxSelectNum - mSelectedImage.size() == 0)
                                                      return;
                                                  mSelectedImage.add("所有图片".equals(mDirPath) ? item : mDirPath + "/" + item);
                                                  mSelect.setImageResource(R.mipmap.pictures_selected);
                                                  mImageView.setColorFilter(Color.parseColor("#57000000"));

                                              }
                                              ((AlbumActivity) mContext).refreshTitleBtn();
                                          }
                                      }
        );
    }

    //还原数据
    public void refreshSelectedPic() {
        mSelectedImage.clear();
        for (String str : oldSelectedImage)
            mSelectedImage.add(str);
    }
}
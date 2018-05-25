package com.xpping.windows10.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.escapevirus.GameActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;

import com.xpping.windows10.entity.DesktopEntity;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.utils.DensityUtils;

import com.xpping.windows10.widget.ToastView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlzhen on 9/12 0012.
 * 桌面 适配器
 */

public class AppGridAdapter extends RecyclerView.Adapter<AppGridAdapter.BaseViewHolder> {
    private Context context;
    private List<DesktopEntity> appEntities;
    private int size;
    private int textColor;
    private OnClickSystemListener systemListener;
    private OnLongClickListener onLongClickListener;

    private BaseViewHolder holder;

    public AppGridAdapter(Context context, int gridNumber, List<DesktopEntity> appEntities, int textColor
            , OnClickSystemListener systemListener, OnLongClickListener onLongClickListener) {
        this.context = context;
        this.textColor = textColor;
        this.appEntities = appEntities;
        this.systemListener = systemListener;
        this.onLongClickListener = onLongClickListener;
        size = (DensityUtils.getScreenW(context) / gridNumber) - DensityUtils.dp2px(10);
    }

    public void setSpaCount(int gridNumber){
        size = (DensityUtils.getScreenW(context) / gridNumber) - DensityUtils.dp2px(10);
        holder.itemImage.getLayoutParams().width=size;
        holder.itemImage.getLayoutParams().height=size;
        notifyDataSetChanged();
    }

    public DesktopEntity getData(int position) {
        return appEntities.get(position);
    }

    public DesktopEntity getRecycleBin() {
        for (DesktopEntity desktopEntity : appEntities)
            if (desktopEntity.getDesktopType() == DesktopEntity.DesktopType.system
                    && desktopEntity.getAppTitle().equals("回收站")) {
                return desktopEntity;
            }

        return null;
    }

    @Override
    public AppGridAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder= new BaseViewHolder(LayoutInflater.from(
                context).inflate(R.layout.adapter_app_grid_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.itemText.setText(appEntities.get(position).getAppTitle());
        if (appEntities.get(position).getDesktopType() == DesktopEntity.DesktopType.system) {
            switch (appEntities.get(position).getAppTitle()) {
                case "此电脑":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.ic_launcher
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "网络":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.internet
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "回收站":

                    BaseApplication.imageLoader.displayImage("drawable://"+(appEntities.get(position).getAppIcon() == null
                                    || appEntities.get(position).getAppIcon().equals("空")
                                    ? R.mipmap.recyclestationnull : R.mipmap.recyclestation)
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "控制面板":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.control_panel
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "Internet\nExplorer":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.iexplore
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "鸣谢":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.help
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                case "是男人就坚持10秒":
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.man_game
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
                default:
                    BaseApplication.imageLoader.displayImage("drawable://"+R.mipmap.user_folder
                            ,holder.itemImage,new ImageSize(size,size));
                    break;
            }
        } else
            try {
                holder.itemImage.setImageDrawable(context.getPackageManager()
                        .getApplicationInfo(appEntities.get(position).getAppPackage(), 0)
                        .loadIcon(context.getPackageManager()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appEntities.get(position).getDesktopType() == DesktopEntity.DesktopType.system) {
                    switch (appEntities.get(position).getAppTitle()) {
                        case "回收站":
                            if (systemListener != null)
                                systemListener.recycle(view);
                            break;
                        case "此电脑":
                            //ToastView.getInstaller(context).setText("请等待2.0版本哦！").show();
                            if (systemListener != null)
                                systemListener.pc(view);
                            break;
                        case "网络":
                            //context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            if (systemListener != null)
                                systemListener.network(view);
                            break;
                        case "控制面板":
                            //context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                            if (systemListener != null)
                                systemListener.control(view);
                            break;
                        case "鸣谢":
                            if (systemListener != null)
                                systemListener.help(view);
                            break;
                        case "是男人就坚持10秒":
                            context.startActivity(new Intent(context, GameActivity.class));
                            break;
                        case "Internet\nExplorer":
                            AppUtis.openPackageListApp((Activity) context, new String[]{"com.android.browser"
                                    , "com.sec.android.app.sbrowser", "com.vivo.browser", "com.UCMobile"
                                    , "com.tencent.mtt", "com.android.chrome", "com.yandex.browser", "com.quark.browser"
                                    , "sogou.mobile.explorer", "com.ijinshan.browser_fast", "com.baidu.browser.apps"
                                    , "com.oupeng.mini.android", "org.mozilla.firefox", "mark.via"}, new AppUtis.CallBack() {
                                @Override
                                public void allFailed(Activity activity) {
                                    ToastView.getInstaller(activity).setText("无法打开浏览器").show();
                                }
                            });

                            break;
                        default://我的文件
                            //ToastView.getInstaller(context).setText("请等待2.0版本哦！").show();
                            if (systemListener != null)
                                systemListener.me(view);
                            break;
                    }
                    return;
                }
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(appEntities.get(position).getAppPackage());
                    context.startActivity(intent);

                } catch (Exception e) {
                    ToastView.getInstaller(context).setText("此APP无法打开").show();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongClickListener.onClick(view, position, appEntities.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return appEntities.size();
    }

    public void clearOrAddOtherSystemIcon(boolean isCheck) {
        if (!isCheck)
            for (int i = 0; i < appEntities.size(); i++) {
                switch (appEntities.get(i).getAppTitle()) {
                    case "鸣谢":
                    case "Internet\nExplorer":
                    case "是男人就坚持10秒":
                        tempAppEntities.add(appEntities.get(i));
                        appEntities.remove(i);
                        i--;
                        break;
                }

            }
            else {
            appEntities.addAll(tempAppEntities);
            tempAppEntities.clear();
        }
        notifyDataSetChanged();
    }

    private List<DesktopEntity> tempAppEntities = new ArrayList<>();

    class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemText;

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_text);
            itemText.setTextColor(textColor);
            itemImage = itemView.findViewById(R.id.item_image);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size, size);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            itemImage.setLayoutParams(lp);
        }
    }

    public interface OnClickSystemListener {
        void pc(View view);

        void me(View view);

        void network(View view);

        void recycle(View view);

        void help(View view);

        void control(View view);
    }

    public interface OnLongClickListener {
        void onClick(View view, int position, DesktopEntity desktopEntity);
    }
}

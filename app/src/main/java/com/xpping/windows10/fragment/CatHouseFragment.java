package com.xpping.windows10.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpping.windows10.R;
import com.xpping.windows10.activity.MainActivity;
import com.xpping.windows10.fragment.base.BaseFragment;

import com.xlzhen.cathouse.adapter.HeaderBottomAdapter;
import com.xlzhen.cathouse.asnyctask.GetPictureListAsyncTask;
import com.xlzhen.cathouse.entity.PictureEntity;
import com.xlzhen.cathouse.listener.Y_NetWorkSimpleResponse;
import com.xlzhen.cathouse.utils.StringUtils;
import com.xpping.windows10.utils.FragmentUtils;
import com.xpping.windows10.widget.ToastView;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/*
*xlzhen 2018/4/27
* 美猫 首页 列表
*/
public class CatHouseFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private RecyclerView pictureRecyclerView;
    private int pageIndex;
    private HeaderBottomAdapter pictureListAdapter;
    private StaggeredGridLayoutManager gridLayoutManager;
    private boolean isBackground;

    private BGARefreshLayout mRefreshLayout;
    private boolean isLoadMore;

    @Override
    protected void initData() {
        initRefreshLayout();

        pictureRecyclerView = findViewById(R.id.pictureRecyclerView);
        getCachePictureList();

        getData("http://api.huaban.com/favorite/beauty?limit=10");
    }

    private void initRefreshLayout() {
        mRefreshLayout = findViewById(R.id.rl_modulename_refresh);
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载更多...");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(android.R.color.white);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(android.R.color.white);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    private void getCachePictureList() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限不足
            ToastView.getInstaller(getActivity()).setText("没有内存卡读写权限,无法使用美猫！").show();
            FragmentUtils.closeFragment(this);
            return;
        }
        if (new File(StringUtils.SDCardPath).exists()&&new File(StringUtils.SDCardPath).list().length > 0){
            //查看已下载的图片
            findViewById(R.id.openSDCardPhoto).setVisibility(View.VISIBLE);
            findViewById(R.id.openSDCardPhoto).setOnClickListener(this);

        }
    }

    private void getData(String url) {
        new GetPictureListAsyncTask(url, new Y_NetWorkSimpleResponse<PictureEntity>() {
            @Override
            public void successResponse(List<PictureEntity> bean) {

                if (pictureListAdapter == null) {
                    mRefreshLayout.endRefreshing();

                    //Grid布局
                    gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    pictureRecyclerView.setLayoutManager(gridLayoutManager);//这里用线性宫格显示 类似于grid view
                    pictureRecyclerView.setAdapter(pictureListAdapter = new HeaderBottomAdapter(getContext(),pictureRecyclerView.getMeasuredWidth(), bean));
                    isLoadMore=true;
                }else if(pageIndex==0){
                    mRefreshLayout.endRefreshing();
                    pictureListAdapter.setData(bean);
                    isLoadMore=true;
                } else {
                    mRefreshLayout.endLoadingMore();
                    pictureListAdapter.addData(bean, isBackground);
                    isLoadMore=true;
                }
                pageIndex = bean.get(bean.size() - 1).getPin_id();
            }

            @Override
            public void failResponse(JSONObject response) {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                isLoadMore=false;
            }

            @Override
            public void endResponse() {
                mRefreshLayout.endRefreshing();
                mRefreshLayout.endLoadingMore();
                isLoadMore=false;
            }
        }, PictureEntity.class);
    }

    public void nextPage(boolean isBackground) {
        this.isBackground = isBackground;
        getData("http://api.huaban.com/favorite/beauty?max=" + pageIndex + "&limit=10");
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pageIndex=0;
        getData("http://api.huaban.com/favorite/beauty?limit=10");
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        nextPage(false);
        return isLoadMore;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pictureRecyclerView=null;
        pictureListAdapter=null;
    }

    @Override
    protected void initWidget() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_max://最大化最小化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pictureListAdapter.setWidth(pictureRecyclerView.getMeasuredWidth());
                            }
                        });
                    }
                }).start();

                break;
        }
        super.onClick(view);
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_cathouse_home,container,false));
    }

    @Override
    protected void onClick(View view, int viewId) {
        switch (viewId){
            case R.id.openSDCardPhoto:
            Intent intent = new Intent(MainActivity.MAIN_BROADCAST);
            intent.putExtra("message","openCatPhotoFragment");
            getContext().sendBroadcast(intent);
            break;
        }
    }

    @Override
    public boolean onBackKey() {
        return true;
    }
}

package com.xpping.windows10.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.widget.AppWebView;

/*
*第三方内置应用 Fragment窗口
*/
public class AppsCurrencyFragment extends BaseFragment {
    private ImageView goBack;
    private ImageView goForward;
    private EditText edgeLink;
    private AppWebView webView;
    private int imageSize;
    @Override
    protected void initData() {
        imageSize= DensityUtils.dp2px(22);
        edgeLink = findViewById(R.id.edgeLink);
        edgeLink.setEnabled(false);
        edgeLink.setText(url);
        goBack = findViewById(R.id.goBack, true);
        goForward = findViewById(R.id.goForward, true);
        findViewById(R.id.goRefresh, true);
        webView = findViewById(R.id.webView);
        initWebView();
    }
    private void initWebView() {
        webView.setWebClient(new AppWebView.WebClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                setActionBarTitle(webView.getTitle());
                edgeLink.setText(url);
                BaseApplication.imageLoader.displayImage("drawable://"+(webView.canGoBack() ? R.mipmap.arrow_back : R.mipmap.arrow_back_no_press)
                        ,goBack,new ImageSize(imageSize,imageSize));
                BaseApplication.imageLoader.displayImage("drawable://"+(webView.canGoForward() ? R.mipmap.arrow_forward : R.mipmap.arrow_forward_no_press)
                        ,goForward,new ImageSize(imageSize,imageSize));

                goBack.setEnabled(webView.canGoBack());
                goForward.setEnabled(webView.canGoForward());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http://") || url.contains("https://"))
                    view.loadUrl(url);
                else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getContext().startActivity(intent);
                    } catch (Exception ex) {
                        //ToastView.getInstaller(getActivity()).setText("未找到“"+url+"”这个链接").show();
                    }

                }
                return true;
            }

        });

        webView.loadUrl(edgeLink.getText().toString());
    }
    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_apps_currency, container, false));
    }

    @Override
    protected void onClick(View view, int viewId) {
        switch (viewId) {
            case R.id.goRefresh://刷新
                webView.reload();
                break;
            case R.id.goForward://前进
                webView.goForward();
                break;
            case R.id.goBack://后退
                webView.goBack();
                break;
        }
    }

    @Override
    public boolean onBackKey() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
    private String url;
    public void setUrl(String url) {
        this.url=url;
    }
}

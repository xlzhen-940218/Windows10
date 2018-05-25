package com.xpping.windows10.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.utils.keyboard.KeyBoardUtils;
import com.xpping.windows10.widget.AppWebView;

/**
 * Created by xlzhen on 9/7 0007.
 * edge浏览器
 */

public class EdgeFragment extends BaseFragment {
    private ImageView goBack;
    private ImageView goForward;
    private EditText edgeLink;
    private AppWebView webView;
    private int imageSize;
    @Override
    protected void initData() {
        imageSize= DensityUtils.dp2px(22);
        edgeLink = findViewById(R.id.edgeLink);
        goBack = findViewById(R.id.goBack, true);
        goForward = findViewById(R.id.goForward, true);
        findViewById(R.id.goRefresh, true);
        webView = findViewById(R.id.webView);
        initWebView();

    }

    private void initWebView() {
        webView.setWebClient(new AppWebView.WebClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                setActionBarTitle(webView.getTitle());
                edgeLink.setText(url);
                BaseApplication.imageLoader.displayImage("drawable://"+(view.canGoBack() ? R.mipmap.arrow_back : R.mipmap.arrow_back_no_press)
                        ,goBack,new ImageSize(imageSize,imageSize));
                BaseApplication.imageLoader.displayImage("drawable://"+(view.canGoForward() ? R.mipmap.arrow_forward : R.mipmap.arrow_forward_no_press)
                        ,goForward,new ImageSize(imageSize,imageSize));

                goBack.setEnabled(view.canGoBack());
                goForward.setEnabled(view.canGoForward());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http://") || url.contains("https://"))
                    view.loadUrl(url);
                else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getActivity().startActivity(intent);
                    } catch (Exception ex) {
                        //ToastView.getInstaller(getActivity()).setText("未找到“"+url+"”这个链接").show();
                    }

                }
                return true;
            }
        });
        webView.loadUrl(getUrl()!=null?getUrl():edgeLink.getText().toString());
    }



    @Override
    protected void initWidget() {
        /*edgeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edgeLink.setText(edgeLink.getText());// 添加这句后实现效果
                edgeLink.selectAll();
            }
        });*/
        edgeLink.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (!edgeLink.getText().toString().contains("http://") && !edgeLink.getText().toString().contains("https://"))
                        edgeLink.setText("http://" + edgeLink.getText().toString());

                    webView.loadUrl(edgeLink.getText().toString());
                    KeyBoardUtils.closeKeybord(edgeLink, getActivity());
                    return true;
                }
                return false;

            }

        });
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_edge, container, false));
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

    public String getUrl() {
        return url;
    }
}

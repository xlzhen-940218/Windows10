package com.xpping.windows10.widget;

import android.content.Context;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.translate.web.NetWorkResponseListener;

import com.wanjian.cockroach.App;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.entity.WeatherEntity;
import com.xpping.windows10.utils.AppUtis;
import com.xpping.windows10.web.RequestHttpUtils;
import com.xpping.windows10.web.URIConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import static com.xpping.windows10.activity.MainActivity.MAIN_BROADCAST;

/*
* 负一屏 view
*/
public class QuickWindowsView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout view1;
    private RelativeLayout view2;
    private TextView weather_degree, weather_county, weather_text;
    private ImageView weather_icon;
    private AppWebView doubanWebView;

    public QuickWindowsView(Context context) {
        super(context);

        view1=(LinearLayout) View.inflate(context,R.layout.view_quick_windows,null);
        view2=(RelativeLayout) View.inflate(context,R.layout.view_quick_windows_land,null);
        this.context = context;

        setContentView();
    }

    public QuickWindowsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        view1=(LinearLayout) View.inflate(context,R.layout.view_quick_windows,null);
        view2=(RelativeLayout) View.inflate(context,R.layout.view_quick_windows_land,null);
        this.context = context;

        setContentView();

    }

    protected void setContentView() {
        removeAllViews();
        addView(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE?view2:view1);
        initData();
    }

    private void initData() {

        //初始化定位
        AMapLocationClient locationClient = new AMapLocationClient(context);
        //设置定位回调监听
        locationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setInterval(300 * 1000);//5分钟定位一次
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != locationClient) {
            locationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation();
            locationClient.startLocation();
        }


        //以下是天气
        weather_degree = findViewById(R.id.weather_degree);
        weather_degree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtis.openWeather(context);
            }
        });
        weather_county = findViewById(R.id.weather_county);
        weather_text = findViewById(R.id.weather_text);
        weather_icon = findViewById(R.id.weather_icon);

        //以下是快捷方式跳转
        ImageView wx_self_qr_code = findViewById(R.id.wx_self_qr_code);
        wx_self_qr_code.setOnClickListener(this);
        ImageView alipay_fukuan = findViewById(R.id.alipay_fukuan);
        alipay_fukuan.setOnClickListener(this);
        ImageView wx_fukuan = findViewById(R.id.wx_fukuan);
        wx_fukuan.setOnClickListener(this);
        ImageView alipay_scan = findViewById(R.id.alipay_scan);
        alipay_scan.setOnClickListener(this);
        ImageView wx_shoukuan = findViewById(R.id.wx_shoukuan);
        wx_shoukuan.setOnClickListener(this);
        ImageView wx_scan = findViewById(R.id.wx_scan);
        wx_scan.setOnClickListener(this);
        ImageView function_qq_scan = findViewById(R.id.function_qq_scan);
        function_qq_scan.setOnClickListener(this);
        //以下是豆瓣电影热映
        doubanWebView = findViewById(R.id.doubanWebView);
        doubanWebView.setBackgroundColor(0);
        doubanWebView.loadUrl("https://m.douban.com/movie/nowintheater?loc_id=108288");
        doubanWebView.setWebClient(new AppWebView.WebClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                webView.loadUrl("javascript:function hiddenClass(){document.body.style.background='transparent';" +
                        "document.getElementsByClassName('TalionNav')[0].style.display='none';" +
                        "document.getElementById('app').style.paddingTop='0';"+
                        "document.getElementsByTagName('h1')[1].style.display='none';" +
                        "document.getElementById('list').style.padding='10px';}hiddenClass();");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent openEdgeFragment = new Intent(MAIN_BROADCAST);
                openEdgeFragment.putExtra("message", "Microsoft Edge");
                openEdgeFragment.putExtra("url", url);
                context.sendBroadcast(openEdgeFragment);
                return true;
            }
        });
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                getWeatherData(aMapLocation.getProvince(), aMapLocation.getCity(), aMapLocation.getDistrict());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private void getWeatherData(String province, final String city, final String county) throws UnsupportedEncodingException {
        String url = "https://wis.qq.com/weather/common?source=xw&weather_type" +
                "=observe|rise&province=" + URLEncoder.encode(province, "utf-8") + "&city=" + URLEncoder.encode(city, "utf-8") +
                "&county=" + URLEncoder.encode(county, "utf-8");

        RequestHttpUtils.postData(context, url, WeatherEntity.class, new NetWorkResponseListener<WeatherEntity>() {
            @Override
            public void onSuccess(WeatherEntity bean) {
                weather_degree.setText(bean.getDegree() + "°");
                weather_county.setText(city + "  " + county);
                weather_text.setText(bean.getWeather());
                BaseApplication.imageLoader.displayImage("drawable://" + getResources()
                        .getIdentifier("weather_" + bean.getWeather_code(), "mipmap", context.getPackageName()), weather_icon);

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.wx_self_qr_code://微信 我的名片 root 可用
                AppUtis.openWeChatSelfQRCodeUI(context);
                break;
            case R.id.alipay_fukuan://支付宝 付款 可用
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(URIConfig.alipay_fukuan));
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ToastView.getInstaller(context).setText("未安装支付宝").show();
                }

                break;
            case R.id.wx_fukuan://微信 付款 root可用
                AppUtis.openWeChatWalletOfflineCoinPurseUI(context);
                break;
            case R.id.alipay_scan://支付宝 扫一扫 可用
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(URIConfig.alipay_scan));
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ToastView.getInstaller(context).setText("未安装支付宝").show();
                }
                break;
            case R.id.wx_shoukuan://微信 收款 root可用
                AppUtis.openWeChatCollectMainUI(context);
                break;
            case R.id.wx_scan://微信 扫一扫 可用
                AppUtis.openWeChatCamera(context);
                break;
            case R.id.function_qq_scan://QQ 扫一扫
                AppUtis.openQQScanTorchActivity(context);
                break;
        }
    }

    public AppWebView getDoubanWebView() {
        return doubanWebView;
    }

    public void setDoubanWebView(AppWebView doubanWebView) {
        this.doubanWebView = doubanWebView;
    }

    public void changeOrientation() {
        setContentView();
    }
}

package com.xlzhen.cathouse.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HttpUtils {
    public static String getHtml(String path) {
        String html = "";
        final Request request;
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //创建一个Request
        if (true)
            request = new Request.Builder()
                    .url(path)
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                    .build();
        else
            request = new Request.Builder()
                    .url(path)
                    .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        try {
            html = call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}

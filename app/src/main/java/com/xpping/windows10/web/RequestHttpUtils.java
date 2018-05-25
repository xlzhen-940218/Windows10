package com.xpping.windows10.web;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.baidu.translate.web.NetWorkResponseListener;
import com.xpping.windows10.utils.FastJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
*网络访问 天气用得到
*/
public class RequestHttpUtils {
    @SuppressWarnings("unchecked")
    public static <T> void postData(final Context context, String url, final Class<?> cls, final NetWorkResponseListener listener) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder builder;
        builder = new FormBody.Builder();
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 " +
                        "Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36")
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onError("无网络");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                JSONObject jsonObject = null;
                try {
                    try {
                        jsonObject = new JSONObject(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null)
                                listener.onError("无网络");
                        }
                    });
                }
                final JSONObject finalJsonObject = jsonObject;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //网络访问成功
                        try {

                            if (finalJsonObject != null) {
                                Log.v("返回数据", finalJsonObject.toString());
                                if (finalJsonObject.getString("status").equals("200")) {
                                    if (listener != null) {

                                        if (cls != String.class) {
                                            T bean = (T) FastJSONParser.getBean(finalJsonObject.getJSONObject("data").getString("observe"), cls);
                                            listener.onSuccess(bean);
                                        } else
                                            listener.onSuccess(finalJsonObject);
                                    }
                                } else {
                                    if (listener != null)
                                        listener.onError(finalJsonObject.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        });
    }
}

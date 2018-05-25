package com.baidu.translate.web;

/**
 * Created by xiong on 3/18 0018.
 * 百度翻译 访问网络 接口
 */

public interface NetWorkResponseListener<T> {
    void onSuccess(T bean);
    void onError(String errorMessage);
}

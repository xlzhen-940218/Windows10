package com.xlzhen.cathouse.listener;


import org.json.JSONObject;

import java.util.List;

/**
 * netWork 请求结果回调<br> (简单json结构)
 */

public interface Y_NetWorkSimpleResponse<T> {
    /**
     * 请求成功
     */
    void successResponse(List<T> bean);

    /**
     * 请求失败
     */
    void failResponse(JSONObject response);

    /**
     * 请求结束
     */
    void endResponse();

}

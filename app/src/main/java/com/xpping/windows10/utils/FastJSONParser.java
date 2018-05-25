package com.xpping.windows10.utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
*阿里巴巴 解析json数据
*/
public class FastJSONParser {
    /**
     * @描述:获得对象
     */
    public static <T> T getBean(String jsonString, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @描述:获得数组
     */
    public static <T> List<T> getBeanList(String jsonString, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, String>> listKeymaps(String jsonString) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> String getJsonString(T... clazz) {
        String jsonStr = null;
        try {
            for (T element : clazz) {
                jsonStr = JSON.toJSONString(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}

package com.rose.common;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 自定义响应结构
 */
public class JsonUtil {

    // 定义jackson对象
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 功能：对象 转 json str
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：map 转 json str
     * @param params
     * @return
     */
    public static String simpleMap2JsonStr(Map<String, Object> params) {
        try {
            Iterator<String> iterator = params.keySet().iterator();
            JSONObject jsonObject = new JSONObject();
            while (iterator.hasNext()) {
                String key = iterator.next();
                jsonObject.put(key, params.get(key));
            }
            return jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：json 转 object
     * @param jsonData json数据
     * @param clazz    对象中的object类型
     * @return
     */
    public static <T> T jsonToObject(String jsonData, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonData, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：json 转 list
     * @param jsonData json str
     * @param beanType list 中 元素类型
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return OBJECT_MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：json 转 map
     * @param jsonData
     * @return
     */
    public static Map jsonToMap(String jsonData) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);
        try {
            return OBJECT_MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
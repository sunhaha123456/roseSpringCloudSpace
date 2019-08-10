package com.rose.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能：http 工具类
 * @date 2017
 */
@Slf4j
public class HttpClientUtil {

    private static int socketTimeout = 30000;

    // 传输超时时间，默认30秒
    private static int connectTimeout = 30000;

    private static Executor executor = new ThreadPoolExecutor(3, 100, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public static void async(Runnable runnable){
        executor.execute(runnable);
    }

    /**
     * 功能：post 发送 json
     * @param url
     * @param bodyStr
     * @param returnLogFlag true：打印请求返回结果的日志，false：不打印请求返回结果的日志
     * @return
     */
    public static String postJson(String url, String bodyStr, boolean returnLogFlag) {
        return post(url, bodyStr, "json", returnLogFlag);
    }

    /**
     * 功能：post 发送 json 并将返回 string 包装成返回对象
     * @param url
     * @param param
     * @param responseClazz
     * @param <T>
     * @param returnLogFlag true：打印请求返回结果的日志，false：不打印请求返回结果的日志
     * @return
     */
    public static <T> T postJson(String url, Object param, Class<? extends T> responseClazz, boolean returnLogFlag){
        String requestJsonStr = JSONObject.toJSONString(param);
        String responseJsonStr = post(url, requestJsonStr, "json", returnLogFlag);
        return JSONObject.parseObject(responseJsonStr, responseClazz);
    }

    /**
     * 功能：post 发送 xml str
     * @param url
     * @param xmlStr
     * @param returnLogFlag true：打印请求返回结果的日志，false：不打印请求返回结果的日志
     * @return
     */
    public static String postXml(String url, String xmlStr, boolean returnLogFlag) {
        return post(url, xmlStr, "xml", returnLogFlag);
    }

    /**
     * 功能：post 发送 报文 公共方法
     * 备注：不建议直接使用
     * @param url
     * @param bodyStr
     * @param bodyType xml:请求报文类型是xml类型 json：请求报文类型是json类型
     * @param returnLogFlag true：打印请求返回结果的日志，false：不打印请求返回结果的日志
     * @return 返回结果 string
     */
    public static String post(String url, String bodyStr, String bodyType, boolean returnLogFlag) {
        log.info("HttpClient工具类自带打印，请求地址：{}，参数为：{}", url, bodyStr);
        String result = null;
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(url);

        //解决XStream对出现双下划线的bug
        /*XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);*/

        /*Util.log("API，POST过去的数据是：");
        Util.log(postDataXML);*/

        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(bodyStr, "UTF-8");
        switch (bodyType) {
            case "xml":
                httpPost.addHeader("Content-Type", "application/xml;charset=utf-8");
                break;
            case "json":
                httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
                break;
            default:
                httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        }
        httpPost.setEntity(postEntity);
        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            if (returnLogFlag) {
                log.info("HttpClient工具类自带打印，请求地址：{}，返回值：{}", url, result);
            }
        } catch (Exception e) {
            log.error("HttpClient工具类自带打印，请求地址：{}，http请求错误！错误信息：{}", url, e);
        } finally {
            httpPost.abort();
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void postAsync(String url, String json, String bodyType, boolean returnLogFlag){
        executor.execute(() -> post(url,  json, bodyType, returnLogFlag));
    }

    public static String get(String url, boolean returnLogFlag) {
        log.info("HttpClient工具类自带打印，请求地址：{}", url);
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        CloseableHttpClient httpClient = HttpClients.custom().build();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            if (returnLogFlag) {
                log.info("HttpClient工具类自带打印，请求地址：{}，返回值：{}", url, result);
            }
        } catch (Exception e) {
            log.error("HttpClient工具类自带打印，请求地址：{}，http请求错误！错误信息：{}", url, e);
        } finally {
            httpGet.abort();
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
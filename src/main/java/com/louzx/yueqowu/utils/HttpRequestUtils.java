package com.louzx.yueqowu.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.UUID;

/**
 * @author 1Zx.
 * @date 2020/10/28 20:41
 */

public class HttpRequestUtils {

    public static JSONObject doHttp(String url, Map<String, String> header, JSONObject jo, Method method) {
        return doHttp(url, header, jo, method, false);
    }

    public static JSONObject doHttp (String url, Map<String, String> header, JSONObject jo, Method method, Boolean reqId) {
        if (reqId) {
            url = url + "?reqId=" + UUID.randomUUID().toString();
        }
        HttpRequest post = HttpUtil.createRequest(method, url);
        post.setHttpProxy("192.168.3.203", 8888);
        post.header("Content-Type", "application/json");
        post.header("Accept", "application/json");
        post.header("distribute-channel", "{\"channelType\":1,\"inviteeId\":null}");
        post.header("Origin", "https://m.yuegowu.com");
        post.header("Referer", "https://m.yuegowu.com/");
        post.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        post.header("Sec-Fetch-Dest", "empty");
        post.header("Sec-Fetch-Mode", "cors");
        post.header("Sec-Fetch-Site", "same-site");
        post.setConnectionTimeout(5000);
        post.setReadTimeout(2000);
        post.setSSLProtocol(SSLSocketFactoryBuilder.TLSv12);
        post.cookie("acw_tc=2760826016039400277627105e72c11c71d53753add034de92110e834f5303");
        try {
            if (null != jo && jo.size() > 0) {
                post.body(jo.toJSONString().getBytes("UTF-8"));
            }
            if (null != header && header.size() > 0) {
                header.forEach((k, v) -> post.header(k, v));
            }
            HttpResponse httpResponse = post.execute();
            String res = httpResponse.body();
            return JSON.parseObject(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
    }

}

package com.louzx.yueqowu.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;
import java.util.UUID;

/**
 * @author 1Zx.
 * @date 2020/10/28 20:41
 */

public class HttpRequestUtils {

    public static JSONObject doHttp(String url, Map<String, String> header, JSONObject body, Method method) {
        url += "?reqId=" + UUID.randomUUID().toString();
        try {
            URL realUrl = new URL(url);
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 8888);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
            URLConnection connection = realUrl.openConnection(proxy);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) connection;
//            httpURLConnection.setHostnameVerifier(()->{return true;});
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("distribute-channel", "{\"channelType\":1,\"inviteeId\":null}");
            httpURLConnection.setRequestProperty("Origin", "https://m.yuegowu.com");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.15(0x17000f31) NetType/WIFI Language/zh_CN miniProgram");
            if (null != header && header.size() > 0) {
                header.forEach(httpURLConnection::setRequestProperty);
            }
            httpURLConnection.setRequestMethod(String.valueOf(method));
            httpURLConnection.setDoInput(true);
            if (null != body) {
                byte[] b = JSON.toJSONString(body, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames).getBytes("UTF-8");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(b.length));
                httpURLConnection.setDoOutput(true);
                OutputStream out = httpURLConnection.getOutputStream();
                out.write(b);
                out.close();
            }
            InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            isr.close();
            return JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject doHttp (String url, Map<String, String> header, JSONObject jo, Method method, Boolean reqId) {
        if (reqId) {
            url = url + "?reqId=" + UUID.randomUUID().toString();
        }
        HttpRequest post = HttpUtil.createRequest(method, url);
        post.setHttpProxy("192.168.3.203", 8888);
        post.header("Content-Type", "application/json");
        post.header("Accept", "*/*");
        post.header("distribute-channel", "{\"channelType\":1,\"inviteeId\":null}");
        post.header("Origin", "https://m.yuegowu.com");
        post.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.15(0x17000f31) NetType/WIFI Language/zh_CN miniProgram");
        post.header("Pragma", "no-cache");
        post.header("Cache-Control", "no-cache");
        post.header("Accept-Language", "zh-cn");
        post.header("Connection", "keep-alive");
        post.setConnectionTimeout(5000);
        post.setReadTimeout(5000);
        post.setSSLProtocol(SSLSocketFactoryBuilder.TLSv12);
        post.cookie("acw_tc=2760826016039400277627105e72c11c71d53753add034de92110e834f5303");
        try {
            if (null != jo && jo.size() > 0) {
                post.body(JSON.toJSONString(jo, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames).getBytes("UTF-8"));
//                System.out.println(JSON.toJSONString(jo, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));
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

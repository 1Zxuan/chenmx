package com.louzx.yueqowu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itactic.jdbc.constants.CommonConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
//
///**
// * @author 1Zx.
// * @date 2020/10/14 14:48
// */
public class HttpClients {
//
//    private static final int TIMEOUT = 50000;
//    private static final String ENCODING = "UTF-8";
//    private static SSLConnectionSocketFactory sslsf = null;
//    private static PoolingHttpClientConnectionManager cm = null;
//    private static SSLContextBuilder builder = null;
//    private static final String HTTP = "http";
//    private static final String HTTPS = "https";
//    static {
//        try {
//            builder = new SSLContextBuilder();
//            // 全部信任 不做身份鉴定
//            builder.loadTrustMaterial(null, new TrustStrategy() {
//                @Override
//                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//                    return true;
//                }
//            });
//            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
//            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register(HTTP, new PlainConnectionSocketFactory())
//                    .register(HTTPS, sslsf)
//                    .build();
//            cm = new PoolingHttpClientConnectionManager(registry);
//            cm.setMaxTotal(200);//max connection
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static CloseableHttpClient getHttpClient() throws Exception {
//        CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.custom()
//                .setSSLSocketFactory(sslsf)
//                .setConnectionManager(cm)
//                .setConnectionManagerShared(true)
//                .build();
//        return httpClient;
//    }
//
//    private static HttpClient sslClient() {
//        try {
//            // 在调用SSL之前需要重写验证方法，取消检测SSL
//            X509TrustManager trustManager = new X509TrustManager() {
//                @Override public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
//                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
//            };
//            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
//            ctx.init(null, new TrustManager[] { trustManager }, null);
//            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
//            // 创建Registry
//            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
//                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
//                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", PlainConnectionSocketFactory.INSTANCE)
//                    .register("https",socketFactory).build();
//            // 创建ConnectionManager，添加Connection配置信息
//            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//            CloseableHttpClient closeableHttpClient = org.apache.http.impl.client.HttpClients.custom().setConnectionManager(connectionManager)
//                    .setDefaultRequestConfig(requestConfig).build();
//            return closeableHttpClient;
//        } catch (KeyManagementException ex) {
//            throw new RuntimeException(ex);
//        } catch (NoSuchAlgorithmException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//
//    private static HttpClient wrapClient(String host,String path) {
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        if (host != null && host.startsWith("https://")) {
//            return sslClient();
//        }else if (StringUtils.isBlank(host) && path != null && path.startsWith("https://")) {
//            return sslClient();
//        }
//        return httpClient;
//    }
//
//    public static JSONObject doPost(String url, Map<String, String> heads, Map<String, Object> params){
//        HttpClient httpClient = null;
//        HttpPost httpPost = null;
//        String result = null;
//        try {
//            httpClient = getHttpClient();
//            httpPost = new HttpPost(url);
//            if (null != heads && heads.size() > 0) {
//                Iterator<Map.Entry<String, String>> iterator = heads.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, String> entry = iterator.next();
//                    httpPost.addHeader(entry.getKey(), entry.getValue());
//                }
//            }
//            httpPost.setHeader("Connection", "close");
//            if (null != params && params.size() > 0) {
//                StringEntity stringEntity = new StringEntity(JSON.toJSONString(params, SerializerFeature.WriteMapNullValue), "UTF-8");
//                stringEntity.setContentType("application/json");
//                httpPost.setEntity(stringEntity);
//            }
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            if (null != httpResponse) {
//                HttpEntity resEntity = httpResponse.getEntity();
//                if (null != resEntity) {
//                    result = EntityUtils.toString(resEntity, "UTF-8");
//                    try {
//                        return JSON.parseObject(result);
//                    } catch (Exception e) {
//                        return null;
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            return null;
//        } finally {
////            ((SSLClient) httpClient).close();
//            if (null != httpPost) {
//                httpPost.releaseConnection();
//            }
//        }
//
//        return null;
//    }
//
//    public static JSONObject doPut(String url, Map<String, String> heads, Map<String, Object> params){
//        HttpClient httpClient = null;
//        HttpPut httpPut = null;
//        String result = null;
//        try {
//            httpClient = new SSLClient();
//            httpPut = new HttpPut(url);
//            httpPut.addHeader("Accept", "application/json");
//            httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
//            if (null != heads && heads.size() > 0) {
//                Iterator<Map.Entry<String, String>> iterator = heads.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, String> entry = iterator.next();
//                    httpPut.addHeader(entry.getKey(), entry.getValue());
//                }
//            }
//            if (null != params && params.size() > 0) {
//                StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), "UTF-8");
////                stringEntity.setContentEncoding("UTF-8");
//                stringEntity.setContentType("application/json");
//                httpPut.setEntity(stringEntity);
//            }
//            HttpResponse httpResponse = httpClient.execute(httpPut);
//            if (null != httpResponse) {
//                HttpEntity resEntity = httpResponse.getEntity();
//                if (null != resEntity) {
//                    result = EntityUtils.toString(resEntity, "UTF-8");
//                    try {
//                        return JSON.parseObject(result);
//                    } catch (Exception e) {
//                        return null;
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            return null;
//        } finally {
//            ((SSLClient) httpClient).close();
//        }
//
//        return null;
//    }
}

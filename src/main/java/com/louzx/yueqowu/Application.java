package com.louzx.yueqowu;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itactic.core.utils.DateFormatUtils;
import com.louzx.yueqowu.thread.SwipeThread;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 1Zx.
 * @date 2020/10/14 14:14
 */
@SpringBootApplication
public class Application {
    // change : 2c9194587219d0ae017219dc98f309fb ----  2c9194597219d0ad017219dc90b703f9 ---- 2c9194587219d0ae017219dc909c03e0
//    private static String goodsInfoId = "2c91c7f47407d82f01740b96633c03e3";

    private static String goodsInfoId = "2c9194587219d0ae017219dc909c03e0";
    static ExecutorService executorService = Executors.newCachedThreadPool();
    public static Map<Integer, String> areaMap = new HashMap<>();
    static {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Application.class.getClassLoader().getResourceAsStream("area.txt"), "GBK"));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.replace(" ", "");
                areaMap.put(Integer.valueOf(line.substring(0, 6)), line.substring(7, line.length() - 1 ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//
//    private static HttpRequest request;
//
//    private static Map<String, String> head = new HashMap<>();
//
//    private static JSONObject jo = null;

    public static void main(String[] args) throws InterruptedException, IOException {
//        Integer times = 0;
//        Map<String, Object> params = new HashMap<>();
//        params.put("goodsInfoId", goodsInfoId);
//        params.put("goodsNum", 3);
//        while (true) {
//            request = HttpUtil.createPost("https://mbff.yuegowu.com/site/purchase?reqId=" + UUID.randomUUID().toString());
//            request.header("Content-Type", "application/json");
//            request.setHttpProxy("127.0.0.1", 8888);
//            request.headerMap(head, false);
//            request.body(new JSONObject(params).toJSONString().getBytes("UTF-8"));
//            HttpResponse execute = request.execute();
//            if (200 == execute.getStatus()) {
//                String body = execute.body();
//                try {
//                    jo = JSON.parseObject(body);
//                } catch (Exception e) {
//                    jo = null;
//                }
//            }
//            System.out.println(++times + "次数返回结果：" + jo);
//            if (null != jo && "K-000015".equals(jo.getString("code"))) {
//                Map<String, Object> loginParams = new HashMap<>();
//                loginParams.put("customerAccount", "MTM5NTg0MDMxNjg=");
//                loginParams.put("customerPassword", "MTIzNDU2");
//                request = HttpUtil.createPost("https://mbff.yuegowu.com/login?reqId=" + UUID.randomUUID().toString());
//                request.header("Content-Type", "application/json");
//                request.setHttpProxy("127.0.0.1", 8888);
//                request.headerMap(head, false);
//                request.body(new JSONObject(loginParams).toJSONString().getBytes("UTF-8"));
//                String body = request.execute().body();
//                try {
//                    jo = JSON.parseObject(body);
//                } catch (Exception e) {
//                    jo = null;
//                }
//                if (null != jo && "K-000000".equals(jo.getString("code"))) {
//                    JSONObject ct = jo.getJSONObject("context");
//                    head.put("Authorization", "Bearer " + ct.getString("token"));
//                    continue;
//                }
//            } else if (null != jo && "K-000000".equals(jo.getString("code"))) {
//                break;
//            }
//            Thread.sleep(500L);
//        }
//        params.clear();
//        JSONObject tradeItemJO = new JSONObject();
//        tradeItemJO.put("skuId", goodsInfoId);
//        tradeItemJO.put("num", 3);
//        List<JSONObject> tr = new ArrayList<>();
//        tr.add(tradeItemJO);
//        params.put("tradeItems", tr);
//        params.put("tradeMarketingList", new ArrayList<>());
//        params.put("forceConfirm", false);
//        params.put("tradePrice", 0);
//        params.put("returnGoodsFlag", 0);
//        request = HttpUtil.createRequest(Method.PUT, "https://mbff.yuegowu.com/trade/confirm?reqId=" + UUID.randomUUID().toString());
//        request.header("Content-Type", "application/json");
//        request.setHttpProxy("127.0.0.1", 8888);
//        request.headerMap(head, false);
//        request.body(new JSONObject(params).toJSONString().getBytes("UTF-8"));
//        String put = request.execute().body();
//        try {
//            jo = JSON.parseObject(put);
//        } catch (Exception e) {
//            jo = null;
//        }
//        System.out.println(put);
//        params.clear();
//        params.put("consigneeId", "2c91c7f2742ba3200174311f3cb80d45");
//        params.put("provinceName", "浙江省");
//        params.put("cityName", "杭州市");
//        params.put("areaName", "余杭区");
//        params.put("consigneeAddress", "浙江省杭州市余杭区合景天峻5栋1402");
//        params.put("consigneeUpdateTime", null);
//        JSONArray ja = new JSONArray();
//        JSONObject jo = new JSONObject();
//        jo.put("storeId", "123456861");
//        jo.put("payType", 0);
//        jo.put("invoiceType", -1);
//        jo.put("generalInvoice", new JSONObject());
//        jo.put("specialInvoice", new JSONObject());
//        jo.put("specialInvoiceAddress", false);
//        jo.put("invoiceAddressId", "2c91c7f2742ba3200174311f3cb80d45");
//        jo.put("invoiceAddressDetail", "浙江省杭州市余杭区合景天峻5栋1402");
//        jo.put("invoiceAddressUpdateTime", null);
//        jo.put("invoiceProjectId", "");
//        jo.put("invoiceProjectName", "");
//        jo.put("invoiceProjectUpdateTime", null);
//        jo.put("buyerRemark", "");
//        jo.put("encloses", "");
//        jo.put("deliverWay", "1");
//        ja.add(jo);
//        params.put("storeCommitInfoList", ja);
//        params.put("commonCodeId", null);
//        params.put("orderSource", "LITTLEPROGRAM");
//        params.put("forceCommit", false);
//        params.put("shareUserId", null);
//        params.put("flightNumber", "CA216");
//        params.put("arriveTime", "2020-10-20 13:46:00");
//        params.put("seatNumber", "33A");
//        params.put("certificateType", 0);
//        params.put("passport", "E66204255");
//        params.put("hongkongAndMacaoPass", "");
//        params.put("taiwanPass", "");
//        params.put("taiwanPassName", "");
//        request = HttpUtil.createPost("https://mbff.yuegowu.com/trade/commit?reqId=" + UUID.randomUUID().toString());
//        request.header("Content-Type", "application/json");
//        request.setHttpProxy("127.0.0.1", 8888);
//        request.headerMap(head, false);
//        request.body(new JSONObject(params).toJSONString().getBytes("UTF-8"));
//        String commit = request.execute().body();
////        JSONObject cmmit = HttpClients.doPost("https://mbff.yuegowu.com/trade/commit?reqId=" + UUID.randomUUID().toString(), head, params);
//        System.out.println(commit);

        SwipeThread swipeThread = new SwipeThread();
        swipeThread.setGoodInfoId(goodsInfoId);
        swipeThread.setUsername("13958403168");
        swipeThread.setPassport("E66204255");
        swipeThread.setPassword("123456");
        swipeThread.setGoodCount(1);
        executorService.execute(swipeThread);
        executorService.shutdown();
    }
}

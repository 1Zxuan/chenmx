package com.louzx.yueqowu.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itactic.core.utils.EnAndDe;
import com.louzx.yueqowu.constants.Constant;
import com.louzx.yueqowu.utils.HttpRequestUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 1Zx.
 * @date 2020/10/28 20:52
 */
@Setter
public class SwipeThread implements Runnable{

    private String goodInfoId;

    private String username;
    private String password;

    public void setGoodInfoId(String goodInfoId) {
        this.goodInfoId = goodInfoId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private Map<String, String> header = new HashMap<>();

    private Integer goodCount;

    private Integer times = 0;

    @Override
    public void run() {
        if (StringUtils.isBlank(goodInfoId) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return;
        }
        getToken();
        while (true) {
            try { Thread.sleep(500L); } catch (InterruptedException e) { }
            JSONObject spuJO = HttpRequestUtils.doHttp(String.format(Constant.URLS.SPU.URL, goodInfoId), header, null, Constant.URLS.SPU.method, true);
            System.out.println(spuJO);
            if (null == spuJO) {
                continue;
            } else if (Constant.RequestStatus.NOLOGIN.equals(spuJO.getString("code"))) {
                getToken();
                continue;
            } else if (Constant.RequestStatus.NOEXIT.equals(spuJO.getString("code"))) {
                System.out.println("商品不存在...");
                continue;
            } else if (Constant.RequestStatus.SUCCESS.equals(spuJO.getString("code"))) {
//                JSONArray ja = spuJO.getJSONObject("context").getJSONArray("goodsInfos");
//                if (null != ja && ja.size() > 0) {
//                    JSONObject goodInfoJO = ja.getJSONObject(0);
//                    JSONArray marketingLabelsJA = goodInfoJO.getJSONArray("marketingLabels");
//                    if (null != marketingLabelsJA && marketingLabelsJA.size() > 0) {
//                        JSONObject jo = marketingLabelsJA.getJSONObject(0);
//                        String tmp = jo.getString("marketingDesc");
//                        String[] tmps = tmp.split("，");
//                        tmp = tmps[tmps.length - 1];
//                        boolean get = false;
//                        if (tmp.indexOf("满") == -1 || tmp.indexOf("件") == -1) {
//                            goodCount = 3;
//                        } else {
//                            get = true;
//                            goodCount = Integer.valueOf(tmp.substring(tmp.indexOf("满") + 1, tmp.indexOf("件")));
//                        }
//                        System.out.println("获取商品打折数量状态：" + get + ", 数量：" + goodCount );
//                        break;
//                    }
//                }
                break;
            }
        }

        while (true) {
            Constant.URLS url = Constant.URLS.PRUCHASE;
            JSONObject body = new JSONObject();
            body.put("goodsInfoId", goodInfoId);
            body.put("goodsNum", goodCount);
            JSONObject pruchaseJO = HttpRequestUtils.doHttp(url.URL, header, body, url.method, true);
            System.out.println(times++ + "次结果" + pruchaseJO);
            if (null != pruchaseJO && Constant.RequestStatus.SUCCESS.equals(pruchaseJO.getString("code"))) {
                break;
            } else {
                try { Thread.sleep(500L); } catch (InterruptedException e) { }
                continue;
            }
        }

        /** 获取优惠信息 */
        Integer marketingLevelId = -1;
        Integer marketingId = -1 ;
        JSONObject PURCHASESJO = new JSONObject();
        PURCHASESJO.put("goodsInfoIds", new ArrayList<String>(){{this.add(goodInfoId);}});
        JSONObject purchasesJO = HttpRequestUtils.doHttp(Constant.URLS.PURCHASES.URL, header, PURCHASESJO, Constant.URLS.PURCHASES.method, true);
        if (null != purchasesJO && Constant.RequestStatus.SUCCESS.equals(purchasesJO.getString("code"))) {
            JSONArray goodsInfos = purchasesJO.getJSONObject("context").getJSONObject("goodsMarketingMap").getJSONArray(this.goodInfoId);
            if (null != goodsInfos && goodsInfos.size() > 0) {
                JSONArray tmp = goodsInfos.getJSONObject(0).getJSONArray("fullDiscountLevelList");
                for ( int i = 0; i < tmp.size(); i++) {
                    Integer fullCount = tmp.getJSONObject(i).getInteger("fullCount");
                    if (goodCount.equals(fullCount)) {
                        marketingLevelId = tmp.getJSONObject(i).getInteger("discountLevelId");
                        marketingId = tmp.getJSONObject(i).getInteger("marketingId");
                        break;
                    }
                }
            }
        }

        /** 确认订单并提交订单 */
        Map<String, Object> params = new HashMap<>();
        JSONObject confirmJO = new JSONObject();
        confirmJO.put("skuId", goodInfoId);
        confirmJO.put("num", goodCount);
        List<JSONObject> tr = new ArrayList<>();
        tr.add(confirmJO);
        params.put("tradeItems", tr);
        if (marketingLevelId != -1 && marketingId != -1) {
            List<JSONObject> tmp = new ArrayList<>();
            JSONObject tmpJO = new JSONObject();
            tmpJO.put("marketingId", marketingId);
            tmpJO.put("skuIds", new ArrayList<String>(){{this.add(goodInfoId);}});
            tmpJO.put("marketingLevelId", marketingLevelId);
            tmpJO.put("giftSkuIds", new ArrayList<>());
            tmp.add(tmpJO);
            params.put("tradeMarketingList", tmp);
        } else {
            params.put("tradeMarketingList", new ArrayList<>());
        }
        params.put("forceConfirm", false);
        params.put("tradePrice", 0);
        params.put("returnGoodsFlag", 0);

        System.out.println(HttpRequestUtils.doHttp(Constant.URLS.CONFIRM.URL, header, new JSONObject(params), Constant.URLS.CONFIRM.method, true));

        params.clear();

//        params.put("consigneeId", "2c91c7f2742ba3200174311f3cb80d45");
//        params.put("provinceName", "浙江省");
//        params.put("cityName", "杭州市");
//        params.put("areaName", "余杭区");
//        params.put("consigneeAddress", "浙江省杭州市余杭区合景天峻5幢1402");
//
//        params.put("consigneeId", "2c91945774b172c101753c7a72590b89");
//        params.put("provinceName", "江苏省");
//        params.put("cityName", "南通市");
//        params.put("areaName", "如皋市");
//        params.put("consigneeAddress", "江苏省南通市如皋市如城街道丰乐苑");


        params.put("consigneeId", "2c9194d17575878001757971924505d2");
        params.put("provinceName", "浙江省");
        params.put("cityName", "杭州市");
        params.put("areaName", "余杭区");
        params.put("consigneeAddress", "浙江省杭州市余杭区五月花城5幢2单元1002");


        params.put("consigneeUpdateTime", null);
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        jo.put("storeId", "123456861");
        jo.put("payType", 0);
        jo.put("invoiceType", -1);
        jo.put("generalInvoice", new JSONObject());
        jo.put("specialInvoice", new JSONObject());
        jo.put("specialInvoiceAddress", false);
//        jo.put("invoiceAddressId", "2c91c7f2742ba3200174311f3cb80d45");
//        jo.put("invoiceAddressDetail", "浙江省杭州市余杭区合景天峻5幢1402");

//        jo.put("invoiceAddressId", "2c91945774b172c101753c7a72590b89");
//        jo.put("invoiceAddressDetail", "江苏省南通市如皋市如城街道丰乐苑");


        jo.put("invoiceAddressId", "2c9194d17575878001757971924505d2");
        jo.put("invoiceAddressDetail", "浙江省杭州市余杭区五月花城5幢2单元1002");

        jo.put("invoiceAddressUpdateTime", null);
        jo.put("invoiceProjectId", "");
        jo.put("invoiceProjectName", "");
        jo.put("invoiceProjectUpdateTime", null);
        jo.put("buyerRemark", "");
        jo.put("encloses", "");
        jo.put("deliverWay", "1");
        ja.add(jo);
        params.put("storeCommitInfoList", ja);
        params.put("commonCodeId", null);
        params.put("orderSource", "LITTLEPROGRAM");
        params.put("forceCommit", false);
        params.put("shareUserId", null);
        params.put("flightNumber", "CA216");
        params.put("arriveTime", "2020-10-31 13:46:00");
        params.put("seatNumber", "33A");
        params.put("certificateType", 0);
        params.put("passport", "EE0698783");
        params.put("hongkongAndMacaoPass", "");
        params.put("taiwanPass", "");
        params.put("taiwanPassName", "");
        System.out.println(HttpRequestUtils.doHttp(Constant.URLS.COMMIT.URL, header, new JSONObject(params), Constant.URLS.COMMIT.method, true));
    }

    private void getToken(){
        String username = EnAndDe.baseEn(this.username);
        String password = EnAndDe.baseEn(this.password);
        JSONObject bodyJO = new JSONObject();
        bodyJO.put("customerAccount", username);
        bodyJO.put("customerPassword", password);
        JSONObject resJO = HttpRequestUtils.doHttp(Constant.URLS.LOGIN.URL, null, bodyJO, Constant.URLS.LOGIN.method, true);
        if (null == resJO) {
            System.out.println("登录失败...");
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) { }
            getToken();
        } else if (Constant.RequestStatus.SUCCESS.equals(resJO.getString("code"))) {
            header.put("Authorization", "Bearer " + resJO.getJSONObject("context").getString("token"));
        }
    }
}

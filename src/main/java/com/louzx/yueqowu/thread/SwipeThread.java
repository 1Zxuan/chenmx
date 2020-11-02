package com.louzx.yueqowu.thread;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itactic.core.utils.EnAndDe;
import com.louzx.yueqowu.Application;
import com.louzx.yueqowu.constants.Constant;
import com.louzx.yueqowu.utils.HttpRequestUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private String customerId;

    private String passport;

    private Integer provinceId;
    private Integer cityId;
    private Integer areaId;

    private String provinceName;
    private String cityName;
    private String areaName;


    private String customerAddress;
    private Integer marketingLevelId = -1;
    private Integer marketingId = -1;

    /** 间隔时间 默认2s*/
    private Long intervals = 2000L;

    private Map<String, Object> params = new LinkedHashMap<>();

    private Double tradePrice = 0D;

    @Override
    public void run() {
        if (StringUtils.isBlank(goodInfoId) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return;
        }
        if (StringUtils.isBlank(passport)) {
            passport = "EE0698783";
        }
        getToken();
        if (header.size() == 0) {
            System.out.println("登录失败");
            return;
        }
        getDefaultAddress();
        if (StringUtils.isBlank(customerAddress)) {
            return;
        }
        spu();
        while (true) {
            pruchase();
            confirm();
            commit();
        }
    }

    private void commit() {
        params = new LinkedHashMap<>();
        params.put("consigneeId", customerId);
        params.put("provinceName", this.provinceName);
        params.put("cityName", cityName);
        params.put("areaName", areaName);
        params.put("consigneeAddress", customerAddress);
        params.put("consigneeUpdateTime", null);
        JSONArray ja = new JSONArray();
        Map<String, Object> joParams = new LinkedHashMap<>();
        joParams.put("storeId", 123456861);
        joParams.put("payType", 0);
        joParams.put("invoiceType", -1);
        joParams.put("generalInvoice", new JSONObject());
        joParams.put("specialInvoice", new JSONObject());
        joParams.put("specialInvoiceAddress", false);
        joParams.put("invoiceAddressId", customerId);
        joParams.put("invoiceAddressDetail", customerAddress);
        joParams.put("invoiceAddressUpdateTime", null);
        joParams.put("invoiceProjectId", "");
        joParams.put("invoiceProjectName", "");
        joParams.put("invoiceProjectUpdateTime", null);
        joParams.put("buyerRemark", "");
        joParams.put("encloses", "");
        joParams.put("deliverWay", "1");
        ja.add(joParams);
        params.put("storeCommitInfoList", ja);
        params.put("commonCodeId", null);
        params.put("orderSource", "LITTLEPROGRAM");
        params.put("forceCommit", false);
        params.put("shareUserId", null);
        params.put("flightNumber", "CA216");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        LocalDateTime localDateTime = calendar.getTime().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        params.put("arriveTime", "2020-11-02 12:30:00");
        params.put("seatNumber", "33A");
        params.put("certificateType", 0);
        params.put("passport", passport);
        params.put("hongkongAndMacaoPass", "");
        params.put("taiwanPass", "");
        params.put("taiwanPassName", "");
        header.put("Referer", "https://m.yuegowu.com/order-confirm");
        JSONObject res = HttpRequestUtils.doHttp(Constant.URLS.COMMIT.URL, header, new JSONObject(params), Constant.URLS.COMMIT.method, true);
        System.out.println(res);
    }

    /** confirm */
    private void confirm(){
        /** 确认订单并提交订单 */
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
            tmpJO.put("skuIds", new ArrayList<String>() {{
                this.add(goodInfoId);
            }});
            tmpJO.put("marketingLevelId", marketingLevelId);
            tmpJO.put("giftSkuIds", new ArrayList<>());
            tmp.add(tmpJO);
            params.put("tradeMarketingList", tmp);
        } else {
            params.put("tradeMarketingList", new ArrayList<>());
        }
        params.put("forceConfirm", false);
        params.put("tradePrice", tradePrice);
        params.put("returnGoodsFlag", 0);
        System.out.println(HttpRequestUtils.doHttp(Constant.URLS.CONFIRM.URL, header, new JSONObject(params), Constant.URLS.CONFIRM.method, true));
    }

    /** 加入购物车 */
    private void pruchase(){
        while (true) {
            Constant.URLS url = Constant.URLS.PRUCHASE;
            JSONObject body = new JSONObject();
            body.put("goodsInfoId", goodInfoId);
            body.put("goodsNum", goodCount);
            JSONObject pruchaseJO = HttpRequestUtils.doHttp(url.URL, header, body, url.method, true);
            System.out.println(++times + "次结果" + pruchaseJO);
            if (null != pruchaseJO && Constant.RequestStatus.SUCCESS.equals(pruchaseJO.getString("code"))) {
                break;
            } else if (null != pruchaseJO && Constant.RequestStatus.OVER_SHOPPINGCAR.equals(pruchaseJO.getString("code"))){
                /** 清空购物车代码 */
                JSONObject jo = new JSONObject();
                jo.put("goodsInfoIds", new ArrayList<>());
                JSONObject res = HttpRequestUtils.doHttp(Constant.URLS.PURCHASES.URL, header, jo, Constant.URLS.PURCHASES.method, true);
                List<String> delGoodsInfos = new ArrayList<>();
                if (null != res && Constant.RequestStatus.SUCCESS.equals(res.getString("code"))) {
                    JSONArray ja = res.getJSONObject("context").getJSONArray("goodsInfos");
                    for (int i = 0; i < ja.size(); i++) {
                        delGoodsInfos.add(ja.getJSONObject(i).getString("goodsInfoId"));
                    }
                }
                jo.put("goodsInfoIds", delGoodsInfos);
                HttpRequestUtils.doHttp(Constant.URLS.PRUCHASE.URL, header, jo, Method.DELETE, true);
                continue;
            } else {
                try { Thread.sleep(intervals); } catch (InterruptedException e) { }
                continue;
            }
        }
        /** 获取优惠信息 */
        if (marketingId == -1 || marketingLevelId == -1) {
            JSONObject PURCHASESJO = new JSONObject();
            PURCHASESJO.put("goodsInfoIds", new ArrayList<String>() {{
                this.add(goodInfoId);
            }});
            JSONObject purchasesJO = HttpRequestUtils.doHttp(Constant.URLS.PURCHASES.URL, header, PURCHASESJO, Constant.URLS.PURCHASES.method, true);
            if (null != purchasesJO && Constant.RequestStatus.SUCCESS.equals(purchasesJO.getString("code"))) {
                tradePrice = purchasesJO.getJSONObject("context").getDouble("tradePrice");
                JSONArray goodsInfos = purchasesJO.getJSONObject("context").getJSONObject("goodsMarketingMap").getJSONArray(this.goodInfoId);
                if (null != goodsInfos && goodsInfos.size() > 0) {
                    JSONArray tmp = goodsInfos.getJSONObject(0).getJSONArray("fullDiscountLevelList");
                    for (int i = tmp.size() - 1; i >= 0; i--) {
                        Integer fullCount = tmp.getJSONObject(i).getInteger("fullCount");
                        if (goodCount >= fullCount) {
                            marketingLevelId = tmp.getJSONObject(i).getInteger("discountLevelId");
                            marketingId = tmp.getJSONObject(i).getInteger("marketingId");
                            break;
                        }
                    }
                }
            }
        }
    }

    /** 判断商品状态 */
    private void spu() {
        while (true) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
            }
            JSONObject spuJO = HttpRequestUtils.doHttp(String.format(Constant.URLS.SPU.URL, goodInfoId), header, null, Constant.URLS.SPU.method, true);
            System.out.println(spuJO);
            if (null == spuJO) {
                continue;
            } else if (Constant.RequestStatus.NOLOGIN.equals(spuJO.getString("code"))) {
                getToken();
                continue;
            } else if (Constant.RequestStatus.NOEXIT.equals(spuJO.getString("code"))) {
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
    }

    /** 获取地址 */
    private void getDefaultAddress() {
        JSONObject jo = HttpRequestUtils.doHttp(Constant.URLS.ADDRESS.URL, header, null, Constant.URLS.ADDRESS.method, true);
        if (null != jo && Constant.RequestStatus.SUCCESS.equals(jo.getString("code"))) {
            JSONArray ja = jo.getJSONArray("context");
            if (null != ja && ja.size() > 0) {
                boolean getAddress = false;
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject tmpJO = ja.getJSONObject(i);
                    Integer isDefaltAddress = tmpJO.getInteger("isDefaltAddress");
                    if (1 == isDefaltAddress) {
                        this.provinceId = tmpJO.getInteger("provinceId");
                        this.cityId = tmpJO.getInteger("cityId");
                        this.areaId = tmpJO.getInteger("areaId");
                        this.customerId = tmpJO.getString("deliveryAddressId");
                        provinceName = Application.areaMap.get(provinceId);
                        cityName = Application.areaMap.get(cityId);
                        areaName = Application.areaMap.get(areaId);
                        StringBuffer sb = new StringBuffer(provinceName);
                        if (!cityId.equals(provinceId)) {
                            sb.append(cityName);
                        }
                        sb.append(areaName);
                        this.customerAddress = sb.toString() + tmpJO.getString("deliveryAddress");
                        getAddress = true;
                        break;
                    }
                }
                if (!getAddress) {
                    JSONObject tmpJO = ja.getJSONObject(0);
                    this.provinceId = tmpJO.getInteger("provinceId");
                    this.cityId = tmpJO.getInteger("cityId");
                    this.areaId = tmpJO.getInteger("areaId");
                    this.customerId = tmpJO.getString("customerId");
                    this.provinceName = Application.areaMap.get(provinceId);
                    this.cityName = Application.areaMap.get(cityId);
                    this.areaName = Application.areaMap.get(areaId);
                    StringBuffer sb = new StringBuffer(provinceName);
                    if (!cityId.equals(provinceId)) {
                        sb.append(cityName);
                    }
                    sb.append(cityName);
                    sb.append(Application.areaMap.get(areaId));
                    this.customerAddress = sb.toString() + tmpJO.getString("customerAddress");
                }
            } else {
                this.customerAddress = null;
                System.out.println("获取收货地址失败");
            }
        } else {
            this.customerAddress = null;
            System.out.println("获取收货地址失败");
        }
    }

    /** 获取登录信息 */
    private void getToken(){
        String username = EnAndDe.baseEn(this.username);
        String password = EnAndDe.baseEn(this.password);
        JSONObject bodyJO = new JSONObject();
        bodyJO.put("customerAccount", username);
        bodyJO.put("customerPassword", password);
        header.put("Authorization", "Bearer");
        header.put("Referer", "https://m.yuegowu.com/user-center?code=081sD0100KPZyK169S300E0emv4sD01b&state=b2bOpenId");
        JSONObject resJO = HttpRequestUtils.doHttp(Constant.URLS.LOGIN.URL, header, bodyJO, Constant.URLS.LOGIN.method, true);
        if (null == resJO) {
            System.out.println("登录失败...");
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) { }
            getToken();
        } else if (Constant.RequestStatus.SUCCESS.equals(resJO.getString("code"))) {
            header.put("Authorization", "Bearer " + resJO.getJSONObject("context").getString("token"));
            header.put("Referer", "https://m.yuegowu.com/");
            JSONObject confirmLogin = HttpRequestUtils.doHttp(Constant.URLS.LOGINCONFIRM.URL, header, null, Constant.URLS.LOGINCONFIRM.method, true);
            if (null != confirmLogin && Constant.RequestStatus.SUCCESS.equals(confirmLogin.getString("code"))) {
                header.put("Authorization", "Bearer " + confirmLogin.getString("context"));
            } else {
                header.clear();
            }
        }
    }
}

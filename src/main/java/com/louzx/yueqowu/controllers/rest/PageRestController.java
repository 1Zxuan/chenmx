package com.louzx.yueqowu.controllers.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itactic.core.dao.ICommonDao;
import com.itactic.core.utils.ArrayToolkitUtils;
import com.itactic.jdbc.jdbc.SqlBuilder;
import com.louzx.yueqowu.po.GoodInfo;
import com.louzx.yueqowu.po.SearchData;
import com.louzx.yueqowu.po.UserOrder;
import com.louzx.yueqowu.utils.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 1Zx.
 * @date 2020/10/14 14:16
 */
@RestController
public class PageRestController {

    @Resource
    private ICommonDao commonDao;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public AjaxResult search(String key, Integer page, Integer limit){
        Map<String, Object> params = new HashMap<>();
        params.put("likeBrandName", key);
//        JSONObject jsonObject = HttpClients.doPost("https://mbff.yuegowu.com/goods/goodsBrand/queryBrandList?reqId=" + UUID.randomUUID().toString(), null, params);
        List<SearchData> searchData = new ArrayList<>();
//        if (null != jsonObject) {
//            JSONArray jsonArray = jsonObject.getJSONArray("context");
//            if (null != jsonArray && jsonArray.size() > 0) {
//                for ( int i = 0; i < jsonArray.size(); i++) {
//                    JSONObject tmp = (JSONObject) jsonArray.get(i);
//                    JSONArray tmpJA = tmp.getJSONArray("goodsBrandVOList");
//                    for (int j = 0; j < tmpJA.size(); j++) {
//                        JSONObject jo = tmpJA.getJSONObject(j);
//                        SearchData sd = new SearchData();
//                        sd.setBrandId(jo.getInteger("brandId"));
//                        sd.setBrandName(jo.getString("brandName"));
//                        sd.setLogo(jo.getString("logo"));
//                        searchData.add(sd);
//                    }
//
//                }
//            }
//        }
        return AjaxResult.success("操作成功", searchData.size(), ArrayToolkitUtils.pageList(searchData, page, limit));
    }

    @RequestMapping(value = "brandInfo", method = RequestMethod.GET)
    public AjaxResult brandInfo(String key, Integer page, Integer limit, String sortFlag){
        Map<String, Object> params = new HashMap<>();
        params.put("brandIds", new ArrayList<String>(){{this.add(key);}});
        params.put("sortFlag", 0);
        params.put("pageNum", page);
        params.put("pageSize", limit);
        if (StringUtils.isNotBlank(sortFlag)) {
            params.put("sortFlag", sortFlag);
        }
        params.put("propDetails", new ArrayList<>());
        params.put("esGoodsInfoDTOList", new ArrayList<>());
//        JSONObject jsonObject = HttpClients.doPost("https://mbff.yuegowu.com/goods/spuListFront?reqId=" + UUID.randomUUID().toString(),null, params);
        Integer count = 0;
        List<GoodInfo> goodInfos = new ArrayList<>();
//        if (null != jsonObject) {
//            JSONObject dataJO = jsonObject.getJSONObject("context") ;
//            count = dataJO.getInteger("totalGoodsNum");
//            JSONObject esGoodsJO = dataJO.getJSONObject("esGoods");
//            if (null == count) {
//                count = esGoodsJO.getInteger("totalElements");
//            }
//            JSONArray contentJA = esGoodsJO.getJSONArray("content");
//            for (int i = 0; i < contentJA.size(); i++) {
//                JSONObject tmpJO = contentJA.getJSONObject(i);
//                JSONArray goodsInfo = tmpJO.getJSONArray("goodsInfos");
//                if (null != goodsInfo && goodsInfo.size() > 0) {
//                    JSONObject goodsInfoJO = goodsInfo.getJSONObject(0);
//
//                    JSONObject marketJO = goodsInfoJO.getJSONArray("marketingLabels").size() > 0 ? goodsInfoJO.getJSONArray("marketingLabels").getJSONObject(0): new JSONObject();
//                    GoodInfo gi = new GoodInfo();
//                    gi.setCostPrice(goodsInfoJO.getDouble("costPrice"));
//                    gi.setBrandId(goodsInfoJO.getInteger("brandId"));
//                    gi.setGoodsId(goodsInfoJO.getString("goodsId"));
//                    gi.setAddedTime(goodsInfoJO.getString("addedTime"));
//                    gi.setCateId(goodsInfoJO.getInteger("cateId"));
//                    gi.setCreateTime(goodsInfoJO.getString("createTime"));
//                    gi.setGoodsImg(goodsInfoJO.getString("goodsInfoImg"));
//                    gi.setGoodsName(goodsInfoJO.getString("goodsInfoName"));
//                    gi.setGoodsNo(goodsInfoJO.getString("goodsInfoNo"));
//                    gi.setGoodsStatus(goodsInfoJO.getInteger("goodsStatus"));
//                    gi.setGrouponPrice(goodsInfoJO.getDouble("grouponPrice"));
//                    gi.setMarketingDesc(marketJO.getString("marketingDesc"));
//                    gi.setMarketPrice(goodsInfoJO.getDouble("marketPrice"));
//                    gi.setSalePrice(goodsInfoJO.getDouble("salePrice"));
//                    gi.setUpdateTime(goodsInfoJO.getString("updateTime"));
//                    goodInfos.add(gi);
//                }
//            }
//        }
        return AjaxResult.success("操作成功", count - 1, goodInfos);

    }

    @PostMapping("start")
    public AjaxResult start(@RequestBody UserOrder userOrder){
        if (StringUtils.isBlank(userOrder.getUsername()) || StringUtils.isBlank(userOrder.getPassword()) || StringUtils.isBlank(userOrder.getGoodsId()) || null == userOrder.getGoodsNum()) {
            return AjaxResult.error("缺少必填参数");
        }
        Integer count = commonDao.count(SqlBuilder.build(UserOrder.class).eq("username", userOrder.getUsername()).eq("goodsId", userOrder.getGoodsId()).eq("status", 1));
        if (count > 0) {
            return AjaxResult.error("该商品正在刷单中...");
        }
        commonDao.save(SqlBuilder.build(UserOrder.class), userOrder);
        return AjaxResult.success("操作成功");
    }
}

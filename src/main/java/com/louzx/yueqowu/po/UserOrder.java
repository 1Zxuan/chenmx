package com.louzx.yueqowu.po;

import com.itactic.jdbc.jdbc.Id;
import com.itactic.jdbc.jdbc.Table;

/**
 * @author 1Zx.
 * @date 2020/10/15 13:19
 */
@Table("user_order")
public class UserOrder {

    @Id
    private String id;
    private String username;
    private String password;
    private String brandId;
    private String goodsId;
    private Integer goodsNum;
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}

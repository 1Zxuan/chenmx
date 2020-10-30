package com.louzx.yueqowu.constants;

import cn.hutool.http.Method;

/**
 * @auther LinYiHao
 * @date 2020/7/27 15:46
 */
public interface Constant {

    public interface retMsg {
        public static final String success = "操作成功";
        public static final String error = "操作失败";
    }

    public interface Status {
        public static final Integer TRUE = 1;
        public static final Integer FALSE = 0;
    }


    public interface RequestStatus {
        public static final String NOPURCHASE = "K-030302";
        public static final String SUCCESS = "K-000000";
        public static final String NOLOGIN = "K-000015";
        public static final String NOEXIT = "K-030001";

    }

    public enum  URLS {
        LOGIN(Method.POST, "https://mbff.yuegowu.com/login"),
        PRUCHASE(Method.POST, "https://mbff.yuegowu.com/site/purchase"),
        CONFIRM(Method.PUT, "https://mbff.yuegowu.com/trade/confirm"),
        COMMIT(Method.POST, "https://mbff.yuegowu.com/trade/commit"),
        SPU(Method.GET, "https://mbff.yuegowu.com/goods/spu/%s"),
        PURCHASES(Method.POST, "https://mbff.yuegowu.com/site/purchases");

        public Method method;
        public String URL;

        URLS(Method method, String URL) {
            this.method = method;
            this.URL = URL;
        }
    }

}

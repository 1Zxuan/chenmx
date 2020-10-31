package com.louzx.yueqowu;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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

    private static String goodsInfoId = "2c91c7f47407d82f01740b96633c03e3";
    static ExecutorService executorService = Executors.newCachedThreadPool();
    public static Map<Integer, String> areaMap = new HashMap<>();
    static {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Application.class.getClassLoader().getResourceAsStream("area.txt"), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.replace(" ", "").replace("\t", "");
                areaMap.put(Integer.valueOf(line.substring(0, 6)), line.substring(6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        SwipeThread louzx = new SwipeThread();
//        louzx.setGoodInfoId("2c91c7f47407d82f01740b96633c03e3");
        louzx.setGoodInfoId("2c9194597219d0ad017219dc891f0018");
        louzx.setUsername("13958403168");
        louzx.setPassport("E66204255");
        louzx.setPassword("lzx1998");
        louzx.setGoodCount(2);
        executorService.execute(louzx);
        executorService.shutdown();
    }
}

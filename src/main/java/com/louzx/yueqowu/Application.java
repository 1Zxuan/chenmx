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


    public static void main(String[] args) throws InterruptedException, IOException {

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

//package com.louzx.yueqowu.service;
//
//import com.itactic.core.dao.ICommonDao;
//import com.itactic.core.utils.ArrayToolkitUtils;
//import com.itactic.core.utils.SpringBeanFactory;
//import com.itactic.jdbc.jdbc.SqlBuilder;
//import com.louzx.yueqowu.po.UserOrder;
//import com.louzx.yueqowu.thread.StartThread;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @author 1Zx.
// * @date 2020/10/14 14:17
// */
//@Service
//public class SearchService {
//
//    @Resource
//    private ICommonDao commonDao;
//
//    private static ExecutorService es = Executors.newFixedThreadPool(400);
//
//    public void start() {
//        List<UserOrder> userOrders = commonDao.query(SqlBuilder.build(UserOrder.class).neq("status", 0));
//        if (userOrders.size() > 0) {
//            Map<String, List<UserOrder>> map = ArrayToolkitUtils.group(userOrders, "brandId");
//            for (String key: map.keySet()) {
//                StartThread st = SpringBeanFactory.getBean(StartThread.class);
//                st.setBrandId(key);
//                st.setUserList(map.get(key));
//                es.execute(st);
//            }
//        }
//    }
//
//
//}

//package com.ldg.baoli.runner;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.ldg.baoli.service.SingletonContext;
//import com.ldg.baoli.service.TicketService;
//import com.ldg.baoli.util.FileUtil;
//import com.ldg.baoli.util.TicketUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.net.InetAddress;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//@Order(1)
//public class TicketCheckLoginRunner implements CommandLineRunner {
//
//    @Autowired
//    private TicketService ticketService;
//
//    @Override
//    public void run(String... strings) throws Exception {
//
////        Map<String,String> ipNoIps = TicketUtil.getIpsMaps();
////        StringBuilder filePathBuilder = new StringBuilder("./config/config");
////
////        try{
////            String ip = InetAddress.getLocalHost().getHostAddress();
////            log.error("ip : " + ip);
////            filePathBuilder.append(ipNoIps.get(ip)).append(".json");
////        }catch (Exception e){
////        }
//        String jsonStr = FileUtil.readJsonFile("./config/config.json");
//
//        //所有需要抢购人信息
//        //所有需要抢购的观影信息
//        JSONObject sourceJsonObject = JSON.parseObject(jsonStr);
//        JSONArray jsonArray = sourceJsonObject.getJSONArray("userOrderList");
//        for(int i = 0 ; i < jsonArray.size() ; i++){
//            try {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String phone = jsonObject.getString("loginPhone");
//                String name = jsonObject.getString("loginName");
//                String cookies = jsonObject.getString("cookies");
//                JSONObject jo = ticketService.checkLogin(cookies);
//                Thread.sleep(500);
//                boolean isLogin = jo.getJSONObject("data").getBoolean("hasLogin");
//                log.error((i+1) + ": " + phone + "(" + name + ") isLogin : " + isLogin);
//                if(isLogin){
//                    SingletonContext.getInstance().getUserPhoneCookieMap().put(phone,cookies);
//                }
//            }catch (Exception e){
//                log.error(""+e);
//            }
//        }
//    }
//}

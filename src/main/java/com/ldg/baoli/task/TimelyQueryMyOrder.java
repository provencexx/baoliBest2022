//package com.ldg.baoli.task;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.ldg.baoli.service.TicketService;
//import com.ldg.baoli.util.FileUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class TimelyQueryMyOrder {
//    @Autowired
//    private TicketService ticketService;
//
//    @Value("${filePath}")
//    private String filePath;
//
//    //3.添加定时任务
//    @Scheduled(fixedRate = 4000)
//    public void configureTasks() {
//        try {
//            String jsonStr = FileUtil.readJsonFile(filePath);
//            //所有需要抢购的观影信息
//            JSONObject jsonObject = JSON.parseObject(jsonStr);
//            String tmpcookies = jsonObject.getString("cookies");
//
//            JSONObject jo = ticketService.getOrderList(tmpcookies,"02");
//
//            log.error("check https status : {} ",jo.toJSONString());
//        }catch (Exception e){
//            log.error("check https status : " + e);
//        }
//    }
//}

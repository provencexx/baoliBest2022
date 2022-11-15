//package com.ldg.baoli.task;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.ldg.baoli.exception.ServiceException;
//import com.ldg.baoli.service.SingletonContext;
//import com.ldg.baoli.service.TicketService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//@Slf4j
//@Component      //1.主要用于标记配置类，兼备Component的效果。
//@Order(3)
//public class TimedQuerySeatsTask implements CommandLineRunner{
//    @Autowired
//    private TicketService ticketService;
//
//    @Value("${filePath}")
//    private String filePath;
//
//
//    @Override
//    public void run(String... strings) throws Exception {
//        try {
//            ConcurrentHashMap<Integer, JSONObject> concurrentHashMap = SingletonContext.getInstance().getTicketJOMap();
//            for(Map.Entry<Integer,JSONObject> entrySet : concurrentHashMap.entrySet()){
//                int showId = entrySet.getKey().intValue();
//                JSONObject tmpjo = entrySet.getValue();
//                String projectId = tmpjo.getString("projectId");
//                int sectionId =tmpjo.getInteger("sectionId").intValue();
//                String cookies = tmpjo.getString("cookies");
//                JSONArray jsonArray = new JSONArray();
//                while (true) {
//                    try {
//                        jsonArray = ticketService.getSeatInfo(projectId, sectionId, showId, cookies);
//                        if (jsonArray.size() == 0) {
//                            //没有座位了
//                            log.error(showId + " : 没有可售座位了");
//                            //                SingletonContext.getInstance().getSeatFlagMap().put(showId,true);
//                        }
//                        break;
//                    } catch (ServiceException se) {
//                        log.error(se + "");
//                    }
//                }
//                ConcurrentLinkedQueue<JSONObject> postQueue = new ConcurrentLinkedQueue<>();
//                for(int k = 0;k<jsonArray.size();k++){
//                    postQueue.add(jsonArray.getJSONObject(k));
//                }
//                if(!postQueue.isEmpty())
//                    SingletonContext.getInstance().getSeatArrayMap().put(showId,postQueue);
//            }
//            log.error("TimedQuerySeatsTask end...");
//        }catch (Exception e){
//            log.error("TimedQuerySeatsTask error : " + e);
//        }
//    }
//}

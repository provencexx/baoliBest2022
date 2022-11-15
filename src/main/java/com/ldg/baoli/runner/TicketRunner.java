package com.ldg.baoli.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ldg.baoli.config.TicketTimerTask;
import com.ldg.baoli.service.TicketService;
import com.ldg.baoli.util.FileUtil;
import com.ldg.baoli.util.TicketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@Order(2)
public class TicketRunner implements CommandLineRunner {

    @Autowired
    private TicketService ticketService;
    @Value("${productId}")
    private String productId;
    @Value("${cookies}")
    private String tmpcookies;

    @Value("${miaoshaTime}")
    private String miaoshaTime;
    @Value("${totalMod}")
    private Integer totalMod;

    @Value("${priceList}")
    private String priceListStr;


    @Override
    public void run(String... strings) {
        String jsonStr = FileUtil.readJsonFile("./config/config.json");
        //所有需要抢购的观影信息
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        JSONArray orderInfoArray = jsonObject.getJSONArray("userOrderList");
        JSONArray theatorNos = jsonObject.getJSONArray("theatorNos");

        JSONObject projectDetailJSON = ticketService.getProjectDetail(productId, TicketUtil.generateHeaders(tmpcookies));

        //获取演出详情
        JSONArray platShowInfoDetailVOList = projectDetailJSON.getJSONArray("showInfoDetailList");

        List<Integer> priceList = JSONArray.parseArray(priceListStr,Integer.class);
        Map<Integer,JSONObject> showIdJSONMap = new HashMap<>();

        //构造所有需要抢购的座位信息
        for(int i = 0;i<platShowInfoDetailVOList.size();i++) {
            if(!theatorNos.contains(i+1)){
                continue;
            }
            JSONObject showJSON = platShowInfoDetailVOList.getJSONObject(i);
            //获取该场次放映信息
//            String showTime = showJSON.getString("showTime");
            Integer showId = showJSON.getIntValue("showId");
            String sectionId = showJSON.getString("sectionId");
            JSONObject tmpjo = new JSONObject();
            tmpjo.put("productId", productId);
            tmpjo.put("sectionId", sectionId);
            tmpjo.put("showId", showId);
            tmpjo.put("totalMod",totalMod);

//            JSONArray seatArray = null;
//            try {
//                while (seatArray == null) {
//                    try {
//                        seatArray = ticketService.getAllSeatInfo(showId,sectionId,priceList);
//                    } catch (Exception e) {
//                        log.error("ticketService.getAllSeatInfo error : " + e);
//                    }
//                    Thread.sleep(1500);
//                }
//            }catch (Exception e1){
//
//            }
//            JSONObject allSeatJSONOB = new JSONObject();
//            for(int k=0;k<seatArray.size();k++){
//                //{\"b\":\"\",\"k\":862887,\"p\":284676,\"s\":\"莲花池第8排15座\",\"sc\":53239,\"sd\":190413822,\"sf\":\"1楼\",\"t\":1,\"x\":23,\"y\":9}
//                allSeatJSONOB.put(seatArray.getJSONObject(k).getString("sid"),seatArray.getJSONObject(k));
//            }
//            tmpjo.put("allSeatJSONOB",allSeatJSONOB);
//            log.error("总座位数: {}",allSeatJSONOB.size());
            showIdJSONMap.put(i+1,tmpjo);
        }

        //抢购时间
        String[] miaoshaTimeArray =  miaoshaTime.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(miaoshaTimeArray[0])); // 控制时
        calendar.set(Calendar.MINUTE, Integer.valueOf(miaoshaTimeArray[1]));    // 控制分
        calendar.set(Calendar.SECOND, Integer.valueOf(miaoshaTimeArray[2]));    // 控制秒
        calendar.set(Calendar.MILLISECOND, Integer.valueOf(miaoshaTimeArray[3]));
        Date time = calendar.getTime();     // 得出执行任务的时间,此处为今天的12：00：00


        for(int m=0;m<orderInfoArray.size();m++){
            JSONObject userOrderInfo = orderInfoArray.getJSONObject(m);
            //构造单人单场次抢购实例
            JSONObject ticketObject = new JSONObject();
            ticketObject.put("loginName", userOrderInfo.getString("loginName"));
            ticketObject.put("loginPhone", userOrderInfo.getString("loginPhone"));
            ticketObject.put("cookies", userOrderInfo.getString("cookies"));
            ticketObject.put("authorization", userOrderInfo.getString("authorization"));
            ticketObject.put("viewerId",userOrderInfo.getString("viewerId"));
            ticketObject.put("totalUser",orderInfoArray.size());
            ticketObject.put("mod",userOrderInfo.getIntValue("mod"));
            ticketObject.put("movieIdListStr",String.join(",",userOrderInfo.getString("viewerId")));
            //准点抢购
            for(int i = 0;i<theatorNos.size();i++) {
                //提前获取可选座位信息
                JSONObject threatorJSON = showIdJSONMap.get(theatorNos.getInteger(i));
                Timer timer = new Timer();
                log.error("ticketObject : {}",ticketObject);
                timer.schedule(new TicketTimerTask(ticketService, ticketObject, threatorJSON,theatorNos.getInteger(i)-1), time);
            }
        }
    }
}

package com.ldg.baoli.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ldg.baoli.service.TicketService;
import com.ldg.baoli.util.FileUtil;
import com.ldg.baoli.util.TicketUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件上传下载
 *
 * @author K0181203
 */
@Slf4j
@Controller
@RequestMapping("/api/baoli/monitor")
public class MonitorController {

    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private TicketService ticketService;


//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/testPostSeat", produces = "application/json;charset=utf-8")
//    public String testPostSeat() {
//        String url = "https://platformpcgateway.polyt.cn/api/1.0/platformOrder/commitOrderOnSeat";
//        String jsonStr = "{\"channelId\":\"\",\"checkModel\":{\"sessionId\":\"011j8h0pMydQXo1Oevsk7Uty6XoNfYixhQ4q8ZlO-WU54FMmkQxJwPmZc4KGJafkwcjAr1CM1c9K2nqe5MRNWMgCGmUJ4G0Tqc7CTgA67lXwpdmSKZwZsD5eWXAjaj23zeT1Z416EVhc4Cul1r5cM2IQb6CKeg-AKJ7o5qUHEUrpAOG9GpNfns9N0ehQG_N0ZV-lBipCIfbJaMg_chHq6kZZKToFIMEXj1yOM5W6UcQx6k\",\"sig\":\"05XqrtZ0EaFgmmqIQes-s-CHVDFzDLtfaCZLZRQLtAYyWWIwbN8ZZNvsQvNGwIbjXwIfZeSjxn8uQyIgkseA1vbcnwmvswSDPxklKt28LxW_VKUbJ_e1p3qoxBhtahFcVG_NFsXrmsPl5Gj8zIPv0xxnSnTKKNuAnFb2Ru7tL2dUF2NFjWPyYO4m_Fm-0ty5IcHK2Vnzso11w6g0vO6p1bPPZ_wnyvPIpLZt3XXIF9Gq5-0poFVi5gojEJZovLbvge6A3mjqjs6uX5hMzmvNYFzOL8V1N8Teg5JzxXTpStK1MDObuFxu28qROgwSTmsweKxQbQqQseiHP-SU5VGNQTQ5gwZho-39nQ1_dVC3Q6rrRfIq7UNMMGU9CSxydqVcMuJpcdRnWtWPvfLbpqQlDqa1R9pCBUn6iZTWkwYkUrG2w-0-BIUZTT2al5P38wgoBAHuWPk8wQLtowh3CU9F4ZqS-PPZ6uG9PI8KXsXF261c9bChpAaQHS1GC28MJDvOenV92pnmMU-l8C0BT31GUP8g\",\"token\":\"FFFF0N00000000009D3A:nc_login_h5:1634128335366:0.3497909793481979\"},\"priceList\":[{\"count\":1,\"freeTicketCount\":1,\"priceId\":\"284162\",\"seat\":\"190283685\"}],\"projectId\":\"34474\",\"requestModel\":{\"applicationCode\":\"plat_pc\",\"applicationSource\":\"plat_pc\",\"atgc\":\"f5cf346883c38f426e85f6190dba2d59\",\"current\":1,\"size\":10,\"timestamp\":1634128920046,\"utgc\":\"utoken\"},\"seriesId\":\"\",\"showId\":53339,\"showTime\":\"2021-11-20 星期六 20:00\"}";
//        HttpEntity<String> entity = new HttpEntity<String>(jsonStr, TicketUtil.generateHeaders(cookies));
//        try{
//            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
//            log.error("postSeatInfo out ： " + responseEntity.getBody());
//            Thread.sleep(100);
//        }catch (Exception e){
//
//        }
//        return "ok";
//    }
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/testGetSellSeat", produces = "application/json;charset=utf-8")
//    public String testGetSellSeat() {
//        String url = "https://platformpcgateway.polyt.cn/api/1.0/seat/getSellSeatList";
//        String jsonStr = "{\"requestModel\":{\"applicationCode\":\"plat_pc\",\"applicationSource\":\"plat_pc\",\"atgc\":\"d65e79adc91e1efe68042db942b19a89\",\"current\":1,\"size\":10,\"timestamp\":1634132711991,\"utgc\":\"utoken\"},\"sectionId\":53159,\"showId\":53339}";
//        String cookies = "Hm_lvt_0cb4627679a11906d6bf0ced685dc014=1634134885; acw_tc=781bad2a16341348869918144e2bf1b287278956debbdce5fd9ddcb0a2e6e5; loginSessionNew=cc41a9d65d1d56d8e7267fc77bf8e514&&06cc9faf4bcc8804297e7e9828c627fe; Hm_lpvt_0cb4627679a11906d6bf0ced685dc014=1634134907";
//        HttpEntity<String> entity = new HttpEntity<String>(jsonStr, TicketUtil.generateHeaders(cookies));
//        for(int i=0;i<100;i++){
//            try{
//                log.error("testGetSellSeat in ： " + i);
//                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
//                log.error("testGetSellSeat out ： " + responseEntity.getBody());
//                Thread.sleep(1500);
//            }catch (Exception e){
//                log.error("e : "+e);
//            }
//
//        }
//        return "ok";
//    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/getPayUrl", produces = "application/json;charset=utf-8")
    public String getPayUrl(@RequestParam("orderId") String orderId,@RequestParam("index") Integer index) {
        String jsonStr = FileUtil.readJsonFile("./config/config.json");
        //所有需要抢购的观影信息
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        JSONArray orderInfoArray = jsonObject.getJSONArray("userOrderList");
        JSONObject ticketObject = orderInfoArray.getJSONObject(index);
        String cookies = ticketObject.getString("cookies");
        String authorization = ticketObject.getString("authorization");
        HttpHeaders httpHeaders = TicketUtil.generateHeaders(cookies,authorization);
        JSONObject payUrl = ticketService.getPayQRCode(orderId,httpHeaders);
        return JSON.toJSONString(payUrl);
    }

//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value = "/getAllPayOrder", produces = "application/json;charset=utf-8")
//    public JSONArray getAllPayOrder() {
//        JSONArray returnArray = new JSONArray();
//        String jsonStr = FileUtil.readJsonFile(allPersonPath);
//        //所有需要抢购的观影信息
//        JSONArray personCookiesArray = JSON.parseArray(jsonStr);
//        if(null == personCookiesArray || personCookiesArray.size()<=0){
//            return null;
//        }
//        for(int i=0;i<personCookiesArray.size();i++){
//            JSONObject jo = personCookiesArray.getJSONObject(i);
//            try {
//                JSONObject payOrder = getNeedPayOrder(jo.getString("loginPhone"));
//                returnArray.add(payOrder);
//            }catch (Exception e){
//                logger.error("getAllPayOrder error : " + e);
//            }
//        }
//        return returnArray;
//    }
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value = "/getNeedPayOrder", produces = "application/json;charset=utf-8")
//    public JSONObject getNeedPayOrder(@RequestParam("phone") String phone) {
//        String jsonStr = FileUtil.readJsonFile(allPersonPath);
//        //所有需要抢购的观影信息
//        JSONArray personCookiesArray = JSON.parseArray(jsonStr);
//        if(null == personCookiesArray || personCookiesArray.size()<=0){
//            return null;
//        }
//        Map<String,String> phoneCookieMap = new HashMap<>();
//        for(int i=0;i<personCookiesArray.size();i++){
//            JSONObject jo = personCookiesArray.getJSONObject(i);
//            phoneCookieMap.put(jo.getString("loginPhone"),jo.getString("cookies"));
//        }
//        String cookieStr = phoneCookieMap.get(phone);
//        JSONObject returnObj = new JSONObject();
//        returnObj.put("phone",phone);
//        if(StringUtils.isNoneBlank(cookieStr)){
//            JSONObject orderList = ticketService.getOrderList(cookieStr,"02");
//            logger.error("orderList : " + orderList);
//            JSONArray orderArray = orderList.getJSONObject("data").getJSONArray("records");
//            if(orderArray.size()>0){
//               String orderId = orderArray.getJSONObject(0).getString("orderId");
//               String productName = orderArray.getJSONObject(0).getString("productName");
//               String showSessions = orderArray.getJSONObject(0).getString("showSessions");
//               returnObj.put("orderId",orderId);
//               returnObj.put("productName",productName);
//                returnObj.put("showSessions",showSessions);
//               String payUrl = null;
//                try {
//                    payUrl = ticketService.getPayQRCode(orderId,cookieStr);
//                    returnObj.put("payUrl",payUrl);
//                }catch (Exception e4){
//                    logger.error("getPayQRCode e4 : " + e4);
//                }
//            }
//            return returnObj;
//        }
//
//        return null;
//    }


}

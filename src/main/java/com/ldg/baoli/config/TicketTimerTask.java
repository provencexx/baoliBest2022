package com.ldg.baoli.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ldg.baoli.service.SingletonContext;
import com.ldg.baoli.service.TicketService;
import com.ldg.baoli.util.TicketUtil;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class TicketTimerTask extends TimerTask {

    public TicketService ticketService;
    public JSONObject ticketObject;
    public JSONObject threatorJSON;
    public Integer index;

    public TicketTimerTask(TicketService ticketService, JSONObject ticketObject,JSONObject threatorJSON,Integer index) {
        this.ticketService = ticketService;
        this.ticketObject = ticketObject;
        this.threatorJSON = threatorJSON;
        this.index = index;
    }

    @Override
    public void run() {
        //获取用户信息
        String cookies = ticketObject.getString("cookies");
        String authorization = ticketObject.getString("authorization");
        HttpHeaders httpHeaders = TicketUtil.generateHeaders(cookies,authorization);
        String loginName = ticketObject.getString("loginName");
        String loginPhone = ticketObject.getString("loginPhone");
        String viewerId = ticketObject.getString("viewerId");
        int mod = ticketObject.getIntValue("mod");
        log.error(loginName + " start miaosha.............");
        if(null == threatorJSON) {
            return;
        }
        //获取场次演出信息
        String productId = threatorJSON.getString("productId");
        String sectionId = threatorJSON.getString("sectionId");
        Integer sectionIdInt = null;
        Integer priceId = null;
        sectionId = null;
        if(StringUtils.isBlank(sectionId)){
            JSONObject jo  = ticketService.getProjectSectionId(productId,httpHeaders,index);
            sectionId = jo.getString("sectionId");
            JSONArray priceArray = jo.getJSONArray("ticketPriceList");
            priceId = priceArray.getJSONObject(0).getInteger("priceId");
        }
        sectionIdInt = Integer.parseInt(sectionId);
        int showId = threatorJSON.getInteger("showId");
        JSONArray allSeats = null;
        try {
            boolean hasOrderd = false;
            while (true){
                JSONArray finalAllSeats = allSeats;
                Integer finalPriceId = priceId;
                CompletableFuture<JSONArray> future1
                        = CompletableFuture.supplyAsync(() -> {
                    if(null == finalAllSeats){
                        return ticketService.getAllSeatInfo(showId,httpHeaders, finalPriceId);
                    }
                    return finalAllSeats;
                });
                Integer finalSectionIdInt = sectionIdInt;
                CompletableFuture<Set<Integer>> future2
                        = CompletableFuture.supplyAsync(() -> {
                    return ticketService.getAvaliableSeatInfo(productId,showId, finalSectionIdInt,httpHeaders);
                });
                CompletableFuture<Void> combinedFuture
                        = CompletableFuture.allOf(future1, future2);
                //获取可售座位
//                if(null == allSeats){
//                    allSeats = ticketService.getAllSeatInfo(showId,httpHeaders,priceId);
//                }
//                Set<Integer> avaliableSet = ticketService.getAvaliableSeatInfo(productId,showId,sectionIdInt,httpHeaders);

                allSeats = future1.get();
                Set<Integer> avaliableSet = future2.get();

                JSONObject currSeats = null;
                for(int i = allSeats.size()-5-mod;i>0;i = i-3){
                    JSONObject tmpSeat = allSeats.getJSONObject(i);
                    if(!avaliableSet.contains(tmpSeat.getInteger("i"))){
                        continue;
                    }
                    currSeats = tmpSeat;
                    String uuid = null;
                    //提交订单获取uuid
                    try {
                        //提交座位信息
                        //获取滑动验证token
                        JSONObject checkJSONObject = SingletonContext.getInstance().getAWSCJsonObject();
                        JSONObject postSeatJO = ticketService.postSeatInfo(productId, showId, sectionIdInt, Lists.newArrayList(tmpSeat.getString("sid")), checkJSONObject,httpHeaders);
                        if(StringUtils.equalsIgnoreCase("1059",postSeatJO.getString("code"))){
                            //有未支付订单
                            log.error(loginName + " : 存在未支付订单...");
                            hasOrderd = true;
                            break;
                        }
                        if(StringUtils.isNotBlank(postSeatJO.getString("errors"))){
                            continue;
                        }
                        uuid = postSeatJO.getString("data");
                        if(StringUtils.isBlank(uuid)){
                            continue;
                        }
                        log.error("uuid : " + uuid);
                    }catch (Exception e2){
                        log.error("postSeatInfo e2 : " + e2);
                        continue;
                    }

                    //提交订单
                    String orderId = null;
                    try {
                        //必须要有这一步
                        ticketService.discountAmt(uuid,httpHeaders);
                    }catch (Exception discontEx){
                        log.error("discountAmt error : {}",discontEx.getMessage(),discontEx);
                        continue;
                    }
                    try{
                        JSONObject orderInfo = ticketService.createRealOrder(loginName, loginPhone, uuid,Lists.newArrayList(viewerId), httpHeaders);
                        if (StringUtils.equalsIgnoreCase("200",orderInfo.getString("code"))) {
                            //抢到结束
                            orderId = orderInfo.getJSONObject("data").getString("orderId");
                            String tipInfo = loginName + "(" + loginPhone + ") : order success , orderId: " + orderId;
                            log.error(tipInfo);
                            hasOrderd = true;
//                            //获取支付码
//                            String payUrl = null;
//                            while (null == payUrl){
//                                try {
//                                    payUrl = ticketService.getPayQRCode(orderId,cookies);
//                                }catch (Exception e4){
//                                    log.error("getPayQRCode e4 : " + e4);
//                                    Thread.sleep(500);
//                                }
//                            }
//                            String pushStr = "【"+loginName + "】 : " + productName + "(" + showTime + "," + seatjo.getString("site") +") : payUrl : " + payUrl;
//                            log.error(pushStr);
                            //微信推送
                            try{
                                String payUrlCopy = "payUrl";
                                //分页查询全部用户
                                Result<Page<WxUser>> wxUsers = WxPusher.queryWxUser("AT_xAqbADhByMeSdabrp7ilWKNIEmBVHhVt", 1, 50);
                                String finalOrderId = orderId;
                                wxUsers.getData().getRecords().forEach(d-> {
                                    Message message = new Message();
                                    message.setAppToken("AT_xAqbADhByMeSdabrp7ilWKNIEmBVHhVt");
                                    message.setContentType(Message.CONTENT_TYPE_TEXT);
                                    message.setContent(tipInfo);
                                    message.setUrl(payUrlCopy);
                                    message.setUid(d.getUid());
                                    Result<List<MessageResult>> result = WxPusher.send(message);
                                });
                                //根据查询指定UID用户
                            }catch (Exception e6){
                                log.error("send e6 : {}", e6.getMessage(),e6);
                            }
                            break;
                        }
                        break;
                    }catch (Exception e2){
                        log.error("createRealOrder e2 : {}",e2.getMessage(),e2);
                        Thread.sleep(200);
                        continue;
                    }

                }
                //存在未支付订单 已有订单号情况 已经抢到结束抢购
                if(hasOrderd){
                    log.error(loginName + " 已抢到，结束抢购！");
                    break;
                }
            }

        } catch (Exception ex) {
            log.error("TicketTimerTask : " + ex);
        }
    }

}

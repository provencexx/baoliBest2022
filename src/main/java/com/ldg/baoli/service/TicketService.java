package com.ldg.baoli.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ldg.baoli.exception.ServiceException;
import com.ldg.baoli.util.TicketUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class TicketService {

    private static final String BAOLI_BASE_URL = "https://www.polyt.cn/platform-backend/";
    @Autowired
    private RestTemplate restTemplate;

    public JSONObject getProjectDetail(String productId, HttpHeaders httpHeaders){
        JSONObject projectDetailJSON = null;
        while (projectDetailJSON == null) {
            String url = BAOLI_BASE_URL + "good/shows/"+productId+"?distributionSeriesId=&distributionChannelId=";
            HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            JSONObject jo = JSON.parseObject(responseEntity.getBody());
            projectDetailJSON = jo.getJSONObject("data");
        }
        return projectDetailJSON;
    }

    public JSONObject getProjectSectionId(String productId, HttpHeaders httpHeaders,Integer index){
        JSONObject projectDetailJSON = null;
        String url = BAOLI_BASE_URL + "good/shows/"+productId+"?distributionSeriesId=&distributionChannelId=";
        while (projectDetailJSON == null) {
            try {
                HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                JSONObject jo = JSON.parseObject(responseEntity.getBody());
                projectDetailJSON = jo.getJSONObject("data");
            }catch (Exception se){
                log.error("getProjectSectionId error : {}",se.getMessage(),se);
            }
        }
        JSONObject jo = projectDetailJSON.getJSONArray("showInfoDetailList").getJSONObject(index);
        return jo;
    }

    /**
     *
     * @param showId
     * @param cookies
     * @return
     * @throws Exception
     */
    public JSONArray getAllSeatInfo(int showId, HttpHeaders httpHeaders, Integer priceId){
        String url = BAOLI_BASE_URL + "good/show/section/"+showId+"?distributionSeriesId=&distributionChannelId=";
        JSONArray seatsArray = null;
        while (seatsArray == null) {
            try {
                HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                JSONObject jo = JSON.parseObject(responseEntity.getBody());
                String cdnUrl = jo.getJSONObject("data").getJSONArray("showSectionDtos").getJSONObject(0).getString("appCdnPath");

                ResponseEntity<String> seatEntity = restTemplate.getForEntity(cdnUrl, String.class);
                JSONObject seatjo = JSON.parseObject(seatEntity.getBody());
                seatsArray = seatjo.getJSONArray("data");
            }catch (Exception se){
                log.error("getAllSeatInfo error : {}",se.getMessage(),se);
            }
        }
        JSONArray realSeatsArray = new JSONArray();
        for(int i =0;i<seatsArray.size();i++){
            if(priceId.equals(seatsArray.getJSONObject(i).getIntValue("p"))){
                realSeatsArray.add(seatsArray.getJSONObject(i));
            }
        }
        return realSeatsArray;
    }

    /**
     *
     * @param showId
     * @param cookies
     * @return
     * @throws Exception
     */
    public Set<Integer> getAvaliableSeatInfo(String productId,int showId,Integer sectionId,HttpHeaders httpHeaders){
        String url = BAOLI_BASE_URL + "good/seats/"+productId+"/"+showId+"/"+sectionId+"/available?distributionSeriesId=&distributionChannelId=";
        Set<Integer> avaliableSets = new HashSet<>();
        List<Boolean> avaliableSeats = null;
        while (avaliableSeats == null) {
            try {
                HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                        String.class);
                JSONObject jo = JSON.parseObject(responseEntity.getBody());
                avaliableSeats = JSON.parseArray(jo.getString("data"),Boolean.class);
            }catch (Exception se){
                log.error("getAllSeatInfo error : {}",se.getMessage(),se);
            }
        }
        for(int i =0;i< avaliableSeats.size();i++){
            if(BooleanUtils.isTrue(avaliableSeats.get(i))){
                avaliableSets.add(i);
            }
        }
        return avaliableSets;
    }


    /**
     *
     * @param productId
     * @param showId
     * @param sectionId
     * @param seatList
     * @param cookies
     * @return
     */
    public JSONObject postSeatInfo(String productId,int showId,int sectionId, List<String> seatList,JSONObject checkJSONObject,HttpHeaders httpHeaders){
        String url = BAOLI_BASE_URL + "order/lock-seat-choose";
        JSONObject jo = new JSONObject();
        if(null != checkJSONObject){
            JSONObject checkJSON = new JSONObject();
            checkJSON.put("token",checkJSONObject.getString("token"));
            checkJSON.put("sessionId",checkJSONObject.getString("sessionId"));
            checkJSON.put("sig",checkJSONObject.getString("sig"));
            jo.put("aliSliderCaptchaDto",checkJSON);
        }
        jo.put("productId",productId);
        jo.put("seatList",seatList);
        jo.put("sectionId",sectionId);
        jo.put("showId",showId);
        log.error("postSeatInfo in : " + jo.toJSONString());
        HttpEntity<String> entity = new HttpEntity<String>(jo.toJSONString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        log.error("postSeatInfo out ： " + responseEntity.getBody());
        JSONObject resultjo = JSON.parseObject(responseEntity.getBody());
        return resultjo;
    }

    public JSONObject discountAmt(String uuid,HttpHeaders httpHeaders){
        String url = BAOLI_BASE_URL + "order/discount-amt";
        TreeMap<String,Object> params = new TreeMap();
        params.put("uuid",uuid);
        HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(params), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        JSONObject jo = JSON.parseObject(responseEntity.getBody());
        return jo;
    }

    public JSONObject createRealOrder(String consignee,String consigneePhone,String uuid,List<String> viewerIdList,HttpHeaders httpHeaders){
        String url = BAOLI_BASE_URL + "order/order";
        TreeMap<String,Object> params = new TreeMap();
        params.put("consignee",consignee);
        params.put("consigneePhone",consigneePhone);
        params.put("deliveryWay","01");
        params.put("uuid",uuid);
        params.put("viewerIdList",viewerIdList);
        params.put("giftCardIdList",Lists.newArrayList());
        log.error("createRealOrder in : " + JSON.toJSONString(params, SerializerFeature.WriteMapNullValue));
        HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(params), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        log.error("createRealOrder out ： " + responseEntity.getBody());
        JSONObject jo = JSON.parseObject(responseEntity.getBody());
        return jo;
    }

    /**
     *
     * @param orderId
     * @param httpHeaders
     * @return
     */
    public JSONObject getPayQRCode(String orderId,HttpHeaders httpHeaders){
        String url = BAOLI_BASE_URL + "payment/order";
        TreeMap<String,Object> params = new TreeMap();
        params.put("channelEnum","PLAT_PC");
        params.put("orderId",orderId);
        params.put("orderType","SHOW");
        params.put("payMethodEnum","WXPAY");
        params.put("sceneTypeEnum","IOS_SDK");
        HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(params), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        JSONObject jo = JSON.parseObject(responseEntity.getBody());
        return jo;
    }



}

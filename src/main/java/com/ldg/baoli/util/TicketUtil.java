package com.ldg.baoli.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;

import java.util.*;

@Slf4j
public class TicketUtil {

    private static final String ApiSource = "plat_pc";

    public static void main(String[] args) {
        String site = "as池座硕大的";
        System.err.println(StringUtils.contains(site, "池座"));
        TreeMap<String, Object> params = new TreeMap();
        params.put("channelId", "");
        List<TreeMap> seatArray = new ArrayList<>();
        TreeMap<String, Object> priceList = new TreeMap();
        seatArray.add(priceList);
        priceList.put("count", 1);
        priceList.put("priceId", 247166);
        priceList.put("seat", 178558771);
        priceList.put("freeTicketCount", 1);
        params.put("priceList", seatArray);

        params.put("projectId", 29678);
        params.put("seriesId", "");
        params.put("showId", "45618");
        params.put("showTime", "2021-04-25 星期日 14:00");
        params.put("channelId", "");

        TreeMap<String, Object> checkModel = new TreeMap();
        checkModel.put("token", "FFFF0N00000000009D3A:nc_login_h5:1617686337620:0.45727891649556773");
        checkModel.put("sessionId", "01wks8yoZBYT5e1noB57MJxqXoNfYixhQ4q8ZlO-WU54FM5FJICr4DKsyKDtIs8OyQiBE0wZPX1UaPOUb6YlvBYvQdFiyR5vjIUeK8TvQptndTvQyWMop3wzPzpncbNU9XgUogcxGXazfr80Vhmi_Kt1OVgyVN0S6VBJBh50AD0GR75vRxHM5y4YebdTZ_MyrbbK6eWorVK14UI93g2-dKOqiTpln6usAMd5kgJHVo1Lw");
        checkModel.put("sig", "05XqrtZ0EaFgmmqIQes-s-CGtcEeTbIePj1PUFm5rQB7qWIwbN8ZZNvsQvNGwIbjXwavHiz1FCxKcKkA_Q5Xcb_cbzE8YGUI23q7LGILdYzKdYo48P8mDUFTeUWrZjkFSLsCMzvAppQgrEsG4Qf5wuH5n6r6Vz2sqC-pOIuzsWl1IlXUGNpwIgefe8tdxATr6qDcJFrzylDpZAT0rCGGDi9wTzEf5gDIbVXObOlgZpEwasY0_RcMSvJwmdEUmXOhuPacB30qvtfnniM70iLk0EUeaV56tQqSbPFpYdZZlCdm1YK-Y7jryQEJEP2zNGlDWq3UsR1NXC1h-XpmKe3WFbCJreoDHOL7ie2aVUM_KIyHftSGULlYkEwBfac7LU3M8gtVCfl3NeU9fG1Iy0NSeS6cOMH9rhAvx1UfPATIds5vrokru87AyaxpAsm6-64w1rcV1Ro3JiA8G7S5nVyFAC1n4CbSa3J4knPsKJmYhAWg2ijdzXWjG59Hlo2QYizNRRNjKNxxK48wRGL2HvSYKI1A");
        params.put("checkModel", checkModel);

        System.err.println(TicketUtil.generateRealRequestModel(params));
    }

    /**
     * 构造基本的RequestModel
     * <p>
     * {"applicationCode":"plat_pc","applicationSource":"plat_pc","atgc":"atoken","current":1,"size":10,"timestamp":1616850152997,"utgc":"utoken"}
     * </p>
     *
     * @return
     */
    public static TreeMap generateBaseRequestModel() throws JSONException {
        TreeMap treeMap = new TreeMap();
        treeMap.put("applicationCode", "plat_pc");
        treeMap.put("applicationSource", "plat_pc");
        treeMap.put("atgc", "atoken");
        treeMap.put("current", 1);
        treeMap.put("size", 10);
        treeMap.put("timestamp", new Date().getTime());
//        treeMap.put("timestamp",1617686344482L);
        treeMap.put("utgc", "utoken");
        return treeMap;
    }


    public static HttpHeaders generateHeaders(String cookies) {
        return generateHeaders(cookies,null);
    }

    public static HttpHeaders generateHeaders(String cookies,String authorization) {
        HttpHeaders headers = new HttpHeaders();//header参数
        List<String> cookieList = new ArrayList<String>();
        for (String cookieVal : cookies.split(";")) {
            cookieList.add(cookieVal);
        }
//        cookieList.add("Hm_lvt_0cb4627679a11906d6bf0ced685dc014=1617006005");
//        cookieList.add("loginSession=cc41a9d65d1d56d8e7267fc77bf8e514&&06cc9faf4bcc8804297e7e9828c627fe");
//        cookieList.add("acw_tc=781bad2416171529367868446e40f7d8e9b993bc9940a0f9aaaa17fcf6e1f6");
//        cookieList.add("Hm_lpvt_0cb4627679a11906d6bf0ced685dc014=1617152959");
        // header设置
        headers.set(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set("Content-Type", "application/json");
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headers.add("Accept", "*/*");
        if(StringUtils.isNotBlank(authorization)){
            headers.add("Authorization",authorization);
        }
        headers.set("Origin", "https://www.polyt.cn");
        headers.set("Referer", "https://www.polyt.cn/");
        headers.set("Channel", "plat_pc");
//        headers.set("sec-ch-ua","\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        // cookie设置
        headers.put(HttpHeaders.COOKIE, cookieList);
        return headers;
    }


    public static TreeMap generateRealRequestModel(TreeMap<String, Object> tmap) {
        try {
            tmap.put("requestModel", generateBaseRequestModel());
            String sourceToMd5 = JSON.toJSONString(tmap, SerializerFeature.WriteMapNullValue) + ApiSource;
            String md5Str = getMD5(sourceToMd5);
            ((TreeMap) tmap.get("requestModel")).put("atgc", md5Str);
            return tmap;
        } catch (JSONException jsException) {
            log.error("" + jsException);
        }
        return null;
    }

    /**
     * 生成md5
     *
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        String base = source;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}

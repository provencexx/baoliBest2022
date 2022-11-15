package com.ldg.baoli.controller;

import com.alibaba.fastjson.JSONObject;
import com.ldg.baoli.service.SingletonContext;
import com.ldg.baoli.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 文件上传下载
 *
 * @author K0181203
 */
@Controller
@RequestMapping("/api/baoli")
public class TicketsController {

    private static Logger logger = LoggerFactory.getLogger(TicketsController.class);

    @Autowired
    private TicketService ticketService;

//    @Autowired
//    private TicketRunner ticketRunner;


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/addAWSCToken", produces = "application/json;charset=utf-8")
    public void addAWSCToken(@RequestParam("sessionId") String sessionId,
                             @RequestParam("sig") String sig,
                             @RequestParam("token") String token) {
        JSONObject jo = new JSONObject();
        jo.put("sessionId",sessionId);
        jo.put("sig",sig);
        jo.put("token",token);
        SingletonContext.getInstance().addAWSCJsonObject(jo);
    }

    @ResponseBody
    @RequestMapping(value = "/getAWSCToken", produces = "application/json;charset=utf-8")
    public JSONObject getAWSCToken() {
        return SingletonContext.getInstance().getAWSCJsonObject();
    }

    @ResponseBody
    @RequestMapping(value = "/getAWSCTokenCount", produces = "application/json;charset=utf-8")
    public int getAWSCTokenCount() {
        return SingletonContext.getInstance().getConcurrentLinkedQueue().size();
    }

    @ResponseBody
    @RequestMapping(value = "/getSeatArrayMap", produces = "application/json;charset=utf-8")
    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<JSONObject>> getSeatArrayMap() {
        return SingletonContext.getInstance().getSeatArrayMap();
    }

//    @ResponseBody
//    @RequestMapping(value = "/triggerRun", produces = "application/json;charset=utf-8")
//    public void triggerRun() {
//        ticketRunner.run();
//    }


}

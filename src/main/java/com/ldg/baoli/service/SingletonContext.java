package com.ldg.baoli.service;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class SingletonContext {

    private static SingletonContext instance = new SingletonContext();

    private ConcurrentLinkedQueue<JSONObject> concurrentLinkedQueue = new ConcurrentLinkedQueue();

    //暂存演出信息用于定时任务查询座位
    private ConcurrentHashMap<Integer,JSONObject> ticketJOMap = new ConcurrentHashMap();

    //可用的座位数组Map
    private ConcurrentHashMap<Integer,ConcurrentLinkedQueue<JSONObject>> seatArrayMap = new ConcurrentHashMap<>();


    private ConcurrentHashMap<Integer,Boolean> seatFlagMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,String> userPhoneCookieMap = new ConcurrentHashMap<>();


    // 私有化构造方法
	private SingletonContext() {
    }

    public static SingletonContext getInstance() {
        return instance;
    }

    public JSONObject getAWSCJsonObject(){
	    return getInstance().concurrentLinkedQueue.poll();
    }
    public void addAWSCJsonObject(JSONObject jo){
	    getInstance().concurrentLinkedQueue.add(jo);
    }

    public ConcurrentHashMap getTicketJOMap() {
        return ticketJOMap;
    }

    public void setTicketJOMap(ConcurrentHashMap ticketJOMap) {
        this.ticketJOMap = ticketJOMap;
    }

    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<JSONObject>> getSeatArrayMap() {
        return seatArrayMap;
    }

    public void setSeatArrayMap(ConcurrentHashMap<Integer,ConcurrentLinkedQueue<JSONObject>> seatArrayMap) {
        this.seatArrayMap = seatArrayMap;
    }

    public ConcurrentHashMap<Integer, Boolean> getSeatFlagMap() {
        return seatFlagMap;
    }

    public void setSeatFlagMap(ConcurrentHashMap<Integer, Boolean> seatFlagMap) {
        this.seatFlagMap = seatFlagMap;
    }

    public ConcurrentLinkedQueue<JSONObject> getConcurrentLinkedQueue() {
        return concurrentLinkedQueue;
    }

    public void setConcurrentLinkedQueue(ConcurrentLinkedQueue<JSONObject> concurrentLinkedQueue) {
        this.concurrentLinkedQueue = concurrentLinkedQueue;
    }

    public ConcurrentHashMap<String, String> getUserPhoneCookieMap() {
        return userPhoneCookieMap;
    }

    public void setUserPhoneCookieMap(ConcurrentHashMap<String, String> userPhoneCookieMap) {
        this.userPhoneCookieMap = userPhoneCookieMap;
    }
}

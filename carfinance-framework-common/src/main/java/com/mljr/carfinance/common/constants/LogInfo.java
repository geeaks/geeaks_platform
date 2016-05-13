package com.mljr.carfinance.common.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 日志信息
 * @Description:
 * @Author:Sine Chen
 * @Date:May 28, 2015 2:43:09 PM
 * @Copyright: All Rights Reserved. Copyright(c) 2015
 */
public class LogInfo {
    private String act;
    private int code; // 0: success  1:fail  -1:error
    private Long uid;
    private Object msg;
    private Object param;
    private long time;

    public String getAct() {
        return act;
    }

    public int getCode() {
        return code;
    }

    public Long getUid() {
        return uid;
    }

    public Object getMsg() {
        return msg;
    }

    public Object getParam() {
        return param;
    }

    public long getTime() {
        return time;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

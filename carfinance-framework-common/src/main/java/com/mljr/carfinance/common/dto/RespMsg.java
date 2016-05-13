package com.mljr.carfinance.common.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespMsg<E extends Serializable> {
    private int status; // 0： 成功  1：失败   -1：服务器失败   
    private String msg;
    private Object data;

    @SuppressWarnings("rawtypes")
    public static RespMsg getInstance() {
        return new RespMsg<Serializable>();
    }

    public RespMsg() {
    }

    public RespMsg(int status, String msg, Object data) {
        super();
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

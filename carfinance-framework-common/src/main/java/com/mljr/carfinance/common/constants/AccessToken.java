package com.mljr.carfinance.common.constants;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class AccessToken {
    private Long ui;
    private String di;
    private String agent;
    private String token;

    public Long getUi() {
        return ui;
    }

    public String getDi() {
        return di;
    }

    public String getAgent() {
        return agent;
    }

    public String getToken() {
        return token;
    }

    public void setUi(Long ui) {
        this.ui = ui;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccessToken(HttpServletRequest request) {
        if (request != null) {
            this.ui = NumberUtils.toLong(request.getHeader("X-ui"), 0L);
            this.di = request.getHeader("X-di");
            this.agent = request.getHeader("X-agent");
            this.token = request.getHeader("X-token");
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

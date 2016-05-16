package com.mljr.carfinance.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 统一定义id的entity基类.
 */
@SuppressWarnings("serial")
public abstract class IdEntity extends Entity<Long> {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " = " + JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

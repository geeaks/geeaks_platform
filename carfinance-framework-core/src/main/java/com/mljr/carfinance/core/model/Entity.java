package com.mljr.carfinance.core.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 统一定义id的entity基类.
 * 
 * 
 */
public abstract class Entity<PK extends Serializable> implements Serializable {
    public abstract PK getId();

    public abstract void setId(PK id);

    public String toString() {
        try {
            return this.getClass().getName() + " = " + JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            return super.toString();
        }
    }
}

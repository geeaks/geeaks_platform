package com.mljr.carfinance.core.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @Description: 基础model
 * @ClassName: baseModel
 * @author gaoxiang
 * @date 2015年11月17日 下午10:50:24
 */ 
public abstract class BaseModel implements Serializable {
	
	private static final long serialVersionUID = 8102332839758942806L;
	
	public String toString() {
		try {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		} catch (Exception e) {
			// 大部分情况下，toString()用在日志输出等调试场景
			return super.toString();
		}
	}
}

package com.mljr.carfinance.log.enums;

/**
 * @Description: 日志级别
 * @ClassName: LoggerLevel
 * @author gaoxiang
 * @date 2015年11月17日 下午10:35:57
 */ 
public enum LoggerLevel {
	
	DEBUG("DEBUG", "DEBUG级别日志"), INFO("INFO", "INFO级别日志"), WARN("WARN", "WARN级别日志"), ERROR("ERROR", "ERROR级别日志");
	
	private LoggerLevel(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	/** 编码 */
	private String code;
	
	/** 描述的KEY */
	private String description;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}

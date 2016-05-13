package com.mljr.framework.log.model;

import org.apache.commons.lang.time.StopWatch;
import com.mljr.framework.core.model.BaseModel;
import com.mljr.framework.log.enums.LoggerLevel;

/**
 * @Description: 日志数据
 * @ClassName: LogInfo
 * @author gaoxiang
 * @date 2015年11月17日 下午10:32:57
 */ 
public abstract class LogInfo extends BaseModel {
	
	private static final long serialVersionUID = 7419129125801133567L;
	
	/**
	 * 被代理拦截的方法名
	 */
	private String interceptorMethod;
	
	/**
	 * 被代理拦截的类
	 */
	private String interceptorClass;
	
	/**
	 * 日志级别(默认INFO级别)
	 */
	private LoggerLevel loggerLevel = LoggerLevel.INFO;
	
	/**
	 * 日志文件的名字
	 * 
	 */
	private String logFileName;
	
	/**
	 * 计时器
	 */
	private StopWatch stopWatch;
	
	public String getInterceptorMethod() {
		return interceptorMethod;
	}
	
	public void setInterceptorMethod(String interceptorMethod) {
		this.interceptorMethod = interceptorMethod;
	}
	
	public String getInterceptorClass() {
		return interceptorClass;
	}
	
	public void setInterceptorClass(String interceptorClass) {
		this.interceptorClass = interceptorClass;
	}
	
	public LoggerLevel getLoggerLevel() {
		return loggerLevel;
	}
	
	public void setLoggerLevel(LoggerLevel loggerLevel) {
		this.loggerLevel = loggerLevel;
	}
	
	public String getLogFileName() {
		return logFileName;
	}
	
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	public StopWatch getStopWatch() {
		return stopWatch;
	}
	
	public void setStopWatch(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}
	
	/**
	 * 生成日志信息 各业务自己负责自己的日志信息
	 */
	public abstract String toLogString();
	
}

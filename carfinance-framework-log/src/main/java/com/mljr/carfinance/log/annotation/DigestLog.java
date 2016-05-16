package com.mljr.carfinance.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.mljr.carfinance.log.enums.LoggerLevel;
import com.mljr.carfinance.log.enums.LoggerPrintType;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DigestLog {
	
	/**
	 * @Description: 日志文件名
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午10:33:52
	 */
	public String logFileName();
	
	/**
	 * @Description: 日志记录级别
	 * @return LoggerLevel 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午10:34:04
	 */
	public LoggerLevel loggerLevel();
	
	/**
	 * @Description: 日志输出类型
	 * @return
	 * @return LoggerPrintType 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午10:34:28
	 */
	public LoggerPrintType printType() default LoggerPrintType.ALL;
	
}

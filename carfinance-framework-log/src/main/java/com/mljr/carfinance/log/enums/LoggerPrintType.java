package com.mljr.carfinance.log.enums;

/**
 * @Description: 日志输出类型
 * @ClassName: LoggerPrintType
 * @author gaoxiang
 * @date 2015年11月17日 下午10:36:45
 */ 
public enum LoggerPrintType {
	/**
	 * 日志输出[入参、返回结果、异常信息]
	 */
	ALL,
	/**
	 * 日志输出[返回结果、异常信息]
	 */
	IGNORE_INPUT,
	/**
	 * 日志输出[入参、异常信息]
	 */
	IGNORE_OUTPUT,
	/**
	 * 日志输出[入参、返回结果]
	 */
	IGNORE_EXCEPTION;
}

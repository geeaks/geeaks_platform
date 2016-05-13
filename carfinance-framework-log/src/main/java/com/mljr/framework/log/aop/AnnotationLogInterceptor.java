package com.mljr.framework.log.aop;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mljr.framework.core.aop.BaseInterceptor;
import com.mljr.framework.core.util.AnnotatedUtils;
import com.mljr.framework.log.annotation.DigestLog;
import com.mljr.framework.log.enums.LoggerLevel;
import com.mljr.framework.log.enums.LoggerPrintType;
import com.mljr.framework.log.model.LogInfo;
import com.mljr.framework.log.util.DefaultDigestLogInfo;

/**
 * @Description: 摘要日志拦截器
 * @ClassName: AnnotatedLogInterceptor
 * @author gaoxiang
 * @date 2015年11月17日 下午10:31:48
 */
public class AnnotationLogInterceptor extends BaseInterceptor {
	
	@Override
	public Object bizInvoke(MethodInvocation invocation) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		//开始记录时间
		stopWatch.start();
		
		Method method = invocation.getMethod();
		String className = method.getDeclaringClass().getSimpleName();
		String methodName = method.getName();
		
		String argumentString = null;
		try {
			DigestLog digestAnnotated = AnnotatedUtils.getAnnotated(DigestLog.class, invocation);
			if (digestAnnotated != null) {
				if (LoggerPrintType.IGNORE_INPUT != digestAnnotated.printType()) {
					argumentString = convert2argumentList(invocation.getArguments());
				}
			}
			// 执行业务方法
			Object result = invocation.proceed();
			if (digestAnnotated != null) {
				if (LoggerPrintType.IGNORE_OUTPUT != digestAnnotated.printType()) {
					digestInvokeLog(className, methodName, digestAnnotated.logFileName(), digestAnnotated.loggerLevel(),
					        stopWatch, result, argumentString, null, true);
				} else {
					digestInvokeLog(className, methodName, digestAnnotated.logFileName(), digestAnnotated.loggerLevel(),
					        stopWatch, null, argumentString, null, true);
				}
			}
			return result;
		} catch (Throwable e) {
			DigestLog digestAnnotated = AnnotatedUtils.getAnnotated(DigestLog.class, invocation);
			if (digestAnnotated != null) {
				if (LoggerPrintType.IGNORE_INPUT != digestAnnotated.printType()) {
					argumentString = (StringUtils.isBlank(argumentString) ? convert2argumentList(invocation.getArguments()) : argumentString);
				}
				if (LoggerPrintType.IGNORE_EXCEPTION != digestAnnotated.printType()) {
					digestInvokeLog(className, methodName, digestAnnotated.logFileName(), digestAnnotated.loggerLevel(),
					        stopWatch, null, argumentString, e, false);
				} else {
					digestInvokeLog(className, methodName, digestAnnotated.logFileName(), digestAnnotated.loggerLevel(),
					        stopWatch, null, argumentString, null, true);
				}
			}
			throw e;
		}
	}
	
	/**
	 * @Description: 转换请求参数数组为请求参数列表
	 * @param arguments 请求参数数组
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:30:58
	 */
	private String convert2argumentList(Object[] arguments) {
		String returnStr = "";
		if (null != arguments) {
			returnStr = JSON.toJSONString(arguments, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
		}
		return returnStr;
	}
	
	/**
	 * @Description: 执行摘要日志记录
	 * @param className 类名
	 * @param methodName 方法名
	 * @param loggerName 日志名称
	 * @param logLevel 日志级别
	 * @param stopWatch 
	 * @param result 
	 * @param arguments 
	 * @param e 异常信息
	 * @param isInvokeSuccess 调用是否成功
	 * @param logId 日志ID
	 * @param digestIdentificationCode 
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:31:24
	 */
	protected void digestInvokeLog(String className, String methodName, String loggerName, LoggerLevel logLevel,
	        StopWatch stopWatch, Object result, String arguments, Throwable e, boolean isInvokeSuccess) {
		this.digestInvokeLog(className, methodName, loggerName, logLevel, stopWatch, result, arguments, e,
		        isInvokeSuccess, false);
	}
	
	protected void digestInvokeLog(String className, String methodName, String loggerName, LoggerLevel logLevel,
	        StopWatch stopWatch, Object result, String arguments, Throwable e, boolean isInvokeSuccess, boolean isSensitiveInfo) {
		
		stopWatch.split();
		DefaultDigestLogInfo digestLogInfo = new DefaultDigestLogInfo();
		digestLogInfo.setInterceptorClass(className);
		digestLogInfo.setInterceptorMethod(methodName);
		digestLogInfo.setStopWatch(stopWatch);
		digestLogInfo.setLogFileName(loggerName);
		digestLogInfo.setLoggerLevel(logLevel);
		digestLogInfo.setRequestParams(arguments);
		digestLogInfo.setInvokeResult(result);
		digestLogInfo.setException(e);
		digestLogInfo.setInvokeSuccess(isInvokeSuccess);
		printLog(digestLogInfo);
		
	}
	
	/**
	 * @Description: 输出日志
	 * @param logInfo
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:32:15
	 */
	protected void printLog(LogInfo logInfo) {
		Logger logger = LoggerFactory.getLogger(logInfo.getLogFileName());
		switch (logInfo.getLoggerLevel()) {
		case DEBUG: {
			logger.debug(logInfo.toLogString());
			break;
		}
		case INFO: {
			logger.info(logInfo.toLogString());
			break;
		}
		case WARN: {
			logger.warn(logInfo.toLogString());
			break;
		}
		case ERROR: {
			logger.error(logInfo.toLogString());
			break;
		}
		default:
			break;
		}
	}
}

package com.mljr.framework.log.util;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mljr.framework.log.model.LogInfo;

/**
 * @Description: 摘要日志数据
 * @ClassName: DefaultDigestLogInfo
 * @author gaoxiang
 * @date 2015年11月17日 下午10:32:44
 */ 
public class DefaultDigestLogInfo extends LogInfo {
	
	private static final long serialVersionUID = -6366610594393235582L;
	
	/**
	 * 请求参数列表
	 */
	private String requestParams = "";
	
	/**
	 * 方法执行结果
	 */
	private Object invokeResult;
	
	/**
	 * 是否执行成功
	 */
	private boolean isInvokeSuccess;
	
	/**
	 * 异常信息
	 */
	private Throwable exception;
	
	@Override
	public String toLogString() {
		StringBuffer sb = new StringBuffer();
		/**
		 * 1.业务信息 [摘要CODE(类名,方法名,业务执行结果,消耗时间)(入参数据)(返回结果数据)(异常信息)]
		 */
		sb.append("[(").append(getInterceptorClass()).append(",")
		        .append(getInterceptorMethod()).append(",").append(isInvokeSuccess ? "Y" : "N").append(",")
		        .append(getStopWatch().getSplitTime()).append("ms)").append("(")
		        .append(StringUtils.isBlank(requestParams) ? "-" : requestParams).append(")").append("(")
		        .append(invokeResult != null ? (JSON.toJSONString(invokeResult, SerializerFeature.WriteMapNullValue,
		                SerializerFeature.WriteNullListAsEmpty)) : "-")
		        .append(")").append("(").append(exception != null ? exception.getMessage() : "-").append(")")
		        .append("]");
		return sb.toString();
	}
	
	public String getRequestParams() {
		return requestParams;
	}
	
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	public Object getInvokeResult() {
		return invokeResult;
	}
	
	public void setInvokeResult(Object invokeResult) {
		this.invokeResult = invokeResult;
	}
	
	public boolean isInvokeSuccess() {
		return isInvokeSuccess;
	}
	
	public void setInvokeSuccess(boolean isInvokeSuccess) {
		this.isInvokeSuccess = isInvokeSuccess;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
}

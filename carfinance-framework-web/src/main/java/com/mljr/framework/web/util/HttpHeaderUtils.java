package com.mljr.framework.web.util;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 该类提供对Http Header的一些操作和访问
 * @ClassName: HttpHeaderUtils
 * @author gaoxiang
 * @date 2015年11月28日 上午3:15:42
 */ 
public final class HttpHeaderUtils {
	
	/**
	 * @Description: 获得真实IP地址
	 * @param request
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月24日 下午10:47:00
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * @Description: 获得referer
	 * @param request
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月24日 下午10:47:11
	 */
	public static String getReferer(HttpServletRequest request) {
		return request.getHeader("Referer");
	}
	
	
	/**
	 * @Description: 获得URL，同时附加所有参数
	 * @param request
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月24日 下午10:47:43
	 */
	@SuppressWarnings("rawtypes")
	public static String getRequestURLWithParameter(HttpServletRequest request) {
		StringBuffer buffer = request.getRequestURL();
		Map parameter = request.getParameterMap();
		if (null != parameter && !parameter.isEmpty()) {
			buffer.append("?");
			Iterator keys = parameter.keySet().iterator();
			String key = null;
			String[] value = null;
			while (keys.hasNext()) {
				key = (String) keys.next();
				value = request.getParameterValues(key);
				if ((null == value) || (value.length == 0)) {
					buffer.append(key).append("=").append("");
				} else if (value.length > 0) {
					buffer.append(key).append("=").append(value[0]);
				}
				if (keys.hasNext()) {
					buffer.append("&");
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * @Description: 获得URL，不附加任何输入参数
	 * @param request
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月24日 下午10:47:52
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuffer buffer = request.getRequestURL();
		return buffer.toString();
	}
	
	/**
	 * 获得 Http://xxxx:yyy/
	 * @param request
	 * @return
	 */
	public static String getHttpRootAddress(HttpServletRequest request) {
		String protocol = request.getProtocol();
		if (StringUtils.isNotBlank(protocol)) {
			int p = protocol.indexOf("/");
			if (p > -1) {
				protocol = protocol.substring(0, p);
			}
		}
		StringBuffer buffer = new StringBuffer(protocol);
		buffer.append("://");
		buffer.append(request.getServerName());
		buffer.append(":");
		buffer.append(request.getServerPort());
		String contextPath = request.getContextPath();
		if (StringUtils.isNotBlank(contextPath)) {
			buffer.append("/").append(contextPath);
		}
		return buffer.toString();
	}
}

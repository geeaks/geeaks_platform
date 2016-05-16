package com.mljr.carfinance.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.mljr.carfinance.web.util.HttpHeaderUtils;
import com.mljr.carfinance.web.util.HttpSpringUtils;
import com.mljr.framework.core.utils.StringMatchUtils;

/**
 * @Description: 在进行过滤的时候，将优先进行后缀过滤，然后再说pattern过滤
 * @ClassName: AbstractFilter
 * @author gaoxiang
 * @date 2015年11月24日 下午10:46:41
 */ 
public abstract class AbstractFilter implements Filter {
	
	// 用来标记当前环境是否已经建立
	protected final static String FILTER_TAG = "_filter_tag_";
	
	/**
	 * Filter进行过滤时，那些资源将会被忽略的配置，以Pattern形式提供，用在Filter类中
	 */
	public static final String URL_IGNORE_LIST_PATTERN = "ignoreListPattern";
	
	/**
	 * Filter进行过滤时，那些资源将会被忽略的配置，以后缀形式提供，用在Filter类中
	 */
	public static final String URL_IGNORE_LIST_SUFFIX = "ignoreListSuffix";
	
	/**
	 * 将要忽略的资源的列表
	 */
	private String ignoreList = "gif,css,ico,js,swf,jpg,jpeg,png,tiff,pcx";
	
	private List<String> ignorePattern = new ArrayList<String>();
	
	private FilterConfig filterConfig;
	
	@Override
	public void destroy() {
	}
	
	@Override
	public final void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		// 1.Init Spring Context
		HttpSpringUtils.getCurrentApplicationContext(filterConfig.getServletContext());
		// 1.1 Get ignore List
		// 1.2 Get pattern
		String temp = filterConfig.getInitParameter(URL_IGNORE_LIST_PATTERN);
		if (StringUtils.isNotBlank(temp)) {
			String[] tempIgnorePattern = StringUtils.split(temp, ",");
			for (String tip : tempIgnorePattern) {
				ignorePattern.add(tip);
			}
		}
		// 1.3 Get suffix
		temp = filterConfig.getInitParameter(URL_IGNORE_LIST_SUFFIX);
		if (StringUtils.isNotBlank(temp)) {
			if (!temp.startsWith(",")) {
				ignoreList += ",";
			}
			ignoreList += temp;
		}
		doInit(filterConfig);
	}
	
	/**
	 * 子类可以继承该方法
	 * 
	 * @param filterConfig
	 * @throws ServletException
	 */
	protected void doInit(FilterConfig filterConfig) throws ServletException {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		if (checkIgnoreFilter((HttpServletRequest) request)) {
			chain.doFilter(request, response);
		} else {
			doFilterLogic(request, response, chain);
		}
	}
	
	/**
	 * 实际的过滤逻辑
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 */
	protected abstract void doFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException;
			
	// --------------- 业务辅助方法 ------------------------------------------
	
	// 检查是否忽略当前请求
	protected boolean checkIgnoreFilter(HttpServletRequest request) {
		String uri = HttpHeaderUtils.getRequestURL(request);
		int p = uri.lastIndexOf(".");
		// 1. 按照后缀进行检查，可以处理大部分内容
		if (p > -1) {
			try {
				String type = uri.substring(p + 1);
				if (ignoreList.indexOf(type) > -1 && !uri.contains("Captcha")) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 2. 按照Pattern进行检查
		for (String pattern : ignorePattern) {
			if (StringMatchUtils.stringMatch(uri, pattern)) {
				return true;
			}
		}
		return false;
	}
	
	// 获取在web.xml中配置的参数
	protected String findInitParameter(String paramName, String defaultValue) {
		// 取filter参数
		String value = trimToNull(getFilterConfig().getInitParameter(paramName));
		
		// 如果未取到，则取全局参数
		if (value == null) {
			value = trimToNull(getServletContext().getInitParameter(paramName));
		}
		
		// 如果未取到，则取默认值
		if (value == null) {
			value = defaultValue;
		}
		
		return value;
	}
	
	protected FilterConfig getFilterConfig() {
		return filterConfig;
	}
	
	protected ServletContext getServletContext() {
		return getFilterConfig().getServletContext();
	}
	
	protected String trimToNull(String str) {
		if (str != null) {
			str = str.trim();
			
			if (str.length() == 0) {
				str = null;
			}
		}
		
		return str;
	}
	
	protected void debug(Logger log, String msg) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Thread.currentThread().getId()).append(" | ");
		buffer.append(Thread.currentThread().getName()).append(" | ");
		buffer.append(msg);
		buffer.append(" ] ");
		log.debug(buffer.toString());
	}
}

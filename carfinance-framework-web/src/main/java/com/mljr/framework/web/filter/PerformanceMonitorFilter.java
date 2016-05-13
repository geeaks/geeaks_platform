package com.mljr.framework.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mljr.framework.core.diagnostic.Profiler;
import com.mljr.framework.web.util.HttpHeaderUtils;

/**
 * @Description: http请求性能拦截
 * @ClassName: PerformanceMonitorFilter
 * @author gaoxiang
 * @date 2015年11月24日 下午10:46:19
 */ 
public class PerformanceMonitorFilter extends AbstractFilter implements Filter {
	
	private final static Logger logger = LoggerFactory.getLogger(PerformanceMonitorFilter.class);
	
	/** 以毫秒表示的阈值 */
	private int threshold = 250;
	
	/**
	 * 缺省的构造方法.
	 */
	public PerformanceMonitorFilter() {
		super();
	}
	
	@Override
	protected void doFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
			
		String url = ((HttpServletRequest) request).getRequestURI();
		Profiler.start("Invoking URL: " + url);
		
		try {
			chain.doFilter(request, response);
		} finally {
			Profiler.release();
			long elapseTime = Profiler.getDuration();
			
			if (elapseTime > threshold) {
				StringBuilder builder = new StringBuilder();
				builder.append("URL:");
				builder.append(HttpHeaderUtils.getRequestURLWithParameter(((HttpServletRequest) request)));
				// 执行时间超过阈值时间
				builder.append(" over PMX = ").append(threshold).append("ms,");
				// 实际执行时间为
				builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
				builder.append(Profiler.dump());
				logger.info(builder.toString());
			}
			// 清楚线程相关的资源
			Profiler.reset();
		}
	}
	
	@Override
	protected void doInit(FilterConfig filterConfig) throws ServletException {
		String sthreshold = filterConfig.getInitParameter("threshold");
		if (StringUtils.isNotBlank(sthreshold)) {
			threshold = NumberUtils.toInt(sthreshold);
		}
		logger.warn("init threshold = " + threshold);
	}
}

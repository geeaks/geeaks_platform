package com.mljr.carfinance.log.aop;

import org.aopalliance.intercept.MethodInvocation;
import com.mljr.carfinance.core.aop.BaseInterceptor;
import com.mljr.carfinance.core.diagnostic.Profiler;

/**
 * @Description: 性能日志拦截
 * @ClassName: PerformanceInstrumentInterceptor
 * @author gaoxiang
 * @date 2015年11月17日 下午10:32:07
 */ 
public class PerformanceInstrumentInterceptor extends BaseInterceptor {
	
	@Override
	public Object bizInvoke(MethodInvocation invocation) throws Throwable {
		StringBuilder builder = new StringBuilder(64);
		builder.append("Invoking method: ");
		builder.append(invocation.getMethod().getDeclaringClass().getName());
		builder.append(".");
		builder.append(invocation.getMethod().getName());
		Profiler.enter(builder.toString());
		try {
			return invocation.proceed();
		} finally {
			Profiler.release();
		}
	}
}

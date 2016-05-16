package com.mljr.carfinance.core.aop;

import java.lang.reflect.Method;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.google.common.collect.Maps;

/**
 * @Description: 基础拦截类
 * @ClassName: BaseInterceptor
 * @author gaoxiang
 * @date 2015年11月17日 下午10:27:42
 */ 
public abstract class BaseInterceptor implements MethodInterceptor {
	
	private static Map<Method, Boolean> methods = null;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 过滤原生方法
		if (null == methods) {
			methods = Maps.newHashMap();
			for (Method m : Object.class.getMethods()) {
				methods.put(m, true);
			}
		}
		
		if (null != methods.get(invocation.getMethod()))
			return invocation.proceed();
		return bizInvoke(invocation);
	}
	
	public abstract Object bizInvoke(MethodInvocation invocation) throws Throwable;
	
}

package com.mljr.framework.core.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: 简单的拦截器链路的实现
 * @ClassName: InterceptorChainSupport
 * @author gaoxiang
 * @date 2015年11月17日 下午10:38:08
 */ 
public class InterceptorChainSupport implements MethodInvocation {
	
	private MethodInvocation proxy;
	
	private List<BaseInterceptor> chains;
	
	public InterceptorChainSupport(MethodInvocation proxy, List<BaseInterceptor> chains) {
		this.proxy = proxy;
		this.chains = chains;
	}
	
	public Method getMethod() {
		return proxy.getMethod();
	}
	
	public Object[] getArguments() {
		return proxy.getArguments();
	}
	
	public AccessibleObject getStaticPart() {
		return proxy.getStaticPart();
	}
	
	public Object getThis() {
		return proxy.getThis();
	}
	
	public Object proceed() throws Throwable {
		if (null != chains) {
			if (chains.size() > 0) {
				return chains.remove(0).invoke(this);
			}
		}
		return proxy.proceed();
	}
	
}

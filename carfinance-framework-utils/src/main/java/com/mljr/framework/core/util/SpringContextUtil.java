package com.mljr.framework.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description: spring上下文工具类
 * @ClassName: SpringContextUtil
 * @author gaoxiang
 * @date 2015年11月18日 下午9:43:30
 */ 
public class SpringContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext; // Spring应用上下文环境
	
	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * @param applicationContext
	 * @throws BeansException
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}
	
	/**
	 * @Description: 获取spring上下文
	 * @return ApplicationContext 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:54:19
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * @Description: 获取bean
	 * @param name bean注册名
	 * @throws BeansException
	 * @return Object 一个以所给名字注册的bean的实例
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:53:14
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}
	
	/**
	 * @Description: 获取类型为requiredType的对象 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	 * @param name bean注册名
	 * @param requiredType 类型
	 * @throws BeansException
	 * @return Object 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:52:27
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(String name, Class requiredType) throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}
	
	/**
	 * @Description: 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * @param name bean注册名
	 * @return boolean 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:53:55
	 */
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}
	
	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * @param name bean名称
	 * @return boolean 是否是单例
	 * @throws NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(name);
	}
	
	/**
	 * @Description: 获取bean的类型
	 * @param name bean名称
	 * @throws NoSuchBeanDefinitionException
	 * @return Class 注册对象的类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:50:44
	 */
	@SuppressWarnings("rawtypes")
	public static Class getType(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getType(name);
	}
	
	/**
	 * @Description: 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * @param name bean名称
	 * @throws NoSuchBeanDefinitionException
	 * @return String[] 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午11:51:28
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}
	
}

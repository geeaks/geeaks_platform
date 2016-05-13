package com.mljr.carfinance.common.util;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

public class URLUtil {
	/**
	 * 获取URL
	 * @param handlerMethod
	 * @return
	 */
	public static String getUrl(HandlerMethod handlerMethod) {
		String mappingUrl = "";
		String classUrl = "";
		String methodUrl = "";
		RequestMapping classMapping = handlerMethod.getBean().getClass().getAnnotation(RequestMapping.class);
		RequestMapping methodMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);

		if (classMapping != null) {
			classUrl = classMapping.value()[0] == null ? "" : classMapping.value()[0];
		}
		if (methodMapping != null) {
			if (methodMapping.value() != null && (methodMapping.value().length != 0)) {
				methodUrl = methodMapping.value()[0] == null ? "" : methodMapping.value()[0];
			}
		}
		mappingUrl = classUrl + methodUrl;
		return mappingUrl;
	}

}

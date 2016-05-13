package com.mljr.framework.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @Title: StringMatchUtils.java
 * @Copyright: Copyright (c) 2014
 * @Description: 一个方便的工具，提供字符串匹配的能力，兼容：antPath 与 正则表达式 <br>
 * @Created on 2014-4-24 下午4:13:59
 * @author 刘文涛 [liuwentao@ucfgroup.com]
 */
public class StringMatchUtils {
	
	private static PathMatcher pathMatcher = new AntPathMatcher();
	
	// COMMENT:这样可以提供性能吗？
	private static Map<String, Pattern> patternCache = Collections.synchronizedMap(new HashMap<String, Pattern>());
	
	/**
	 * 方便的方法，用来判断字符串是否匹配
	 * 
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static boolean stringMatch(String source, String[] pattern) {
		if (pattern.length == 1) {
			return stringMatch(source, pattern[0]);
		} else {
			boolean b = false;
			for (String s : pattern) {
				b = stringMatch(source, s);
				if (b) {
					return true;
				}
			}
		}
		return false;
	}
	
	// 采用AntPath匹配方法
	// 然后采用正则表达式匹配方法
	public static boolean stringMatch(String source, String pattern) {
		if (pathMatcher.match(pattern, source)) {
			return true;
		}
		try {
			Pattern sp = null;
			if (patternCache.containsKey(pattern)) {
				sp = patternCache.get(pattern);
			} else {
				sp = Pattern.compile(pattern);
				patternCache.put(pattern, sp);
			}
			Matcher matcher = sp.matcher(source);
			if (matcher.find()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

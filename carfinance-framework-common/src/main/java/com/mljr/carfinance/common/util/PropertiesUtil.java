package com.mljr.carfinance.common.util;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesUtil {
	private static Map<String, ResourceBundle> cache = new HashMap<String, ResourceBundle>();

	public static String getValue(String resources, String key) {
		String value = "";
		try {
			ResourceBundle rb = cache.get(resources);
			if (rb == null) {
				rb = ResourceBundle.getBundle(resources);
				cache.put(resources, rb);
			}
			value = rb.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return value;

	}

	public static String getValue(String resources, String key, boolean notCached) {
		if (notCached) {
			clear(key);
		}
		return getValue(resources, key);
	}

	public static String getValue(Locale local, String resources, String key) {
		String value = "";
		try {
			ResourceBundle rb = cache.get(resources + local.getDisplayName());
			if (rb == null) {
				rb = ResourceBundle.getBundle(resources, local);
				cache.put(resources + local.getDisplayName(), rb);
			}
			value = rb.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static boolean getBoolean(String resources, String key) {
		String value = "";
		try {
			ResourceBundle rb = cache.get(resources);
			if (rb == null) {
				rb = ResourceBundle.getBundle(resources);
				cache.put(resources, rb);
			}
			value = rb.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return isTrue(value);
	}

	public static boolean getBoolean(Locale local, String resources, String key) {
		String value = "";
		try {
			ResourceBundle rb = cache.get(resources + local.getDisplayName());
			if (rb == null) {
				rb = ResourceBundle.getBundle(resources, local);
				cache.put(resources + local.getDisplayName(), rb);
			}
			value = rb.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return isTrue(value);
	}

	private static void clearAll() {
		cache.clear();
	}

	private static void clear(String key) {
		cache.remove(key);
	}

	private static boolean isTrue(String value) {
		return (("true".equalsIgnoreCase(value)) || ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value))
				|| ("y".equalsIgnoreCase(value)) || "是".equalsIgnoreCase(value));
	}

	public static void main(String[] args) {
		String value = PropertiesUtil.getValue("app", "mail.smtp.host");
		System.out.println("value:" + value);
	}
}

package com.mljr.framework.log.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * @Title: LocalIPUtils.java
 * @Copyright: Copyright (c) 2014
 * @Description: IP工具<br>
 * @Created on 2014-4-24 下午4:12:10
 * @author 刘文涛 [liuwentao@ucfgroup.com]
 */
@SuppressWarnings("rawtypes")
public class LocalIPUtils {
	
	private static Map<String, String> ip4s = new HashMap<String, String>();
	
	private static Map<String, String> ip6s = new HashMap<String, String>();
	
	private static String ipAddress = null;
	static {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			// Get hostname
			String hostname = addr.getHostName();
			if ("127.0.0.1".equals(addr.getHostAddress())) {
				Enumeration e = NetworkInterface.getNetworkInterfaces();
				while (e.hasMoreElements()) {
					NetworkInterface netface = (NetworkInterface) e.nextElement();
					String name = netface.getName();
					if (!name.startsWith("lo")) {
						Enumeration e2 = netface.getInetAddresses();
						while (e2.hasMoreElements()) {
							InetAddress ip2 = (InetAddress) e2.nextElement();
							if (ip2 instanceof java.net.Inet4Address) {
								ip4s.put(name, ip2.getHostAddress());
							} else {
								ip6s.put(name, ip2.getHostAddress());
							}
						}
					}
				}
			} else {
				ip4s.put(hostname, addr.getHostAddress());
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 得到IPV4模式的唯一IP地址，如果有多个，那么随机获得一个
	 * 
	 * @return
	 */
	public static String getIp4Single() {
		if (ip4s.isEmpty()) {
			return "127.0.0.1";
		}
		if (null == ipAddress) {
			Iterator iter = ip4s.values().iterator();
			while (iter.hasNext()) {
				ipAddress = (String) iter.next();
				if (!StringUtils.equals("127.0.0.1", ipAddress)) {
					break;
				}
			}
		}
		if (null == ipAddress) {
			ipAddress = "127.0.0.1";
		}
		return ipAddress;
	}
}

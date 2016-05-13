package com.mljr.framework.core.util;

import java.security.MessageDigest;

/**
 * @Description: 一个方便的加密类
 * @ClassName: GtsDigestUtils
 * @author gaoxiang
 * @date 2015年11月17日 下午10:56:43
 */ 
public class GtsDigestUtils {
	
	/**
	 * @Description: 获取输入的字符串摘要
	 * @param input
	 * @return
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月17日 下午10:56:56
	 */
	public static String digest(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bDigests = md.digest(input.getBytes("UTF-8"));
			return byte2hex(bDigests);
		} catch (Exception e) {
			return "";
		}
	}
	
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	/**
	 * To check if it is equal between two digest sting.
	 * 
	 * @param digesta
	 * @param digestb
	 * @return compare result
	 * @throws GenericException
	 */
	public static boolean isEqual(String digesta, String digestb) throws Exception {
		try {
			return MessageDigest.isEqual(digesta.toUpperCase().getBytes(), digestb.toUpperCase().getBytes());
		} catch (Exception e) {
			throw e;
		}
	}
}

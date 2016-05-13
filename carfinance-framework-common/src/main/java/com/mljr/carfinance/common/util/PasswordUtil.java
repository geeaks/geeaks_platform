package com.mljr.carfinance.common.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PBKDF2 单向强加密算法
 * 基本原理：安全随机Salt后迭代加密N次
 * Created by Yuxiao He on 14-7-18.
 */
public class PasswordUtil {
	private static Logger LOG = LoggerFactory.getLogger(PasswordUtil.class);
    private final static int saltByteSize = 24;
    private final static int hashByteSize = 24;
    private final static int pbkdf2Iterations = 20000;

    /**
     * 单向高强度加密密码
     * @param password 原始密码
     * @return 密文
     */
    public static String encrypt(String password) {
        char[] chars = password.toCharArray();

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[saltByteSize];
            sr.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(chars, salt, pbkdf2Iterations, hashByteSize * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return pbkdf2Iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (Exception e) {
        	LOG.error("Encrypt password error: " + password);
        }
        return null;
    }

    /**
     * 计算原始密码和密文密码是否相等
     * @param originalPassword 原始密码
     * @param storedPassword 待比较密文
     * @return true / false
     */
    public static boolean isEqual(String originalPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        if (parts.length != 3) {
            return false;
        }
        int iterations = Integer.parseInt(parts[0]);
        byte[] hash = null, testHash = null;
        try {
            byte[] salt = fromHex(parts[1]);
            hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
        	LOG.error("Password compare error: " + originalPassword + " " + storedPassword);
        }
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    
    
    public static void main(String[] args) {
		String pwd = PasswordUtil.encrypt("youjie.com");
		System.out.println(pwd);
	}

}

package com.mljr.framework.core.util;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: AES加密工具类
 * @ClassName: AESCoder
 * @author gaoxiang
 * @date 2015年11月18日 下午9:45:38
 */ 
public class AESCoder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AESCoder.class);
	
	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	private static final String GTSKEY = "GTS-MAIL-PWD-KEY";
	
	/**
	 * 生成密钥
	 * @return
	 */
	public static String createKey() {
		byte[] key = initSecretKey();
		return Hex.encodeHexString(key);
	}
	
	/**
	 * 初始化密钥
	 * @return byte[] 密钥
	 * @throws Exception
	 */
	private static byte[] initSecretKey() {
		// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		// 初始化此密钥生成器，使其具有确定的密钥大小
		// AES 要求密钥长度为 128
		kg.init(128);
		// 生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return 密钥
	 */
	public static Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * 加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, Key key) throws GeneralSecurityException {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 加密
	 * @param data 待加密数据
	 * @param key 二进制密钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws GeneralSecurityException {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 加密
	 * @param data 待加密数据
	 * @param key 二进制密钥
	 * @param cipherAlgorithm 加密算法/工作模式/填充方式
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws GeneralSecurityException {
		// 还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @param cipherAlgorithm 加密算法/工作模式/填充方式
	 * @return byte[] 加密数据
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws GeneralSecurityException {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * @param data 待解密数据
	 * @param key 二进制密钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws GeneralSecurityException {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * @Description: 解密
	 * @param data 要解密的字符串
	 * @throws GeneralSecurityException
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月27日 上午11:57:32
	 */
	public static String gtsDecrypt(String data) {
		try {
			return new String(decrypt(data.getBytes(), GTSKEY.getBytes(), DEFAULT_CIPHER_ALGORITHM));
		} catch (Exception e) {
			LOGGER.error("AES加密异常", e);
			return data;
		}
	}
	
	/**
	 * @Description: 加密
	 * @param data 要加密的字符串
	 * @throws GeneralSecurityException
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月27日 上午11:59:54
	 */
	public static String gtsEncrypt(String data) {
		try {
			return new String(encrypt(data.getBytes(), GTSKEY.getBytes(), DEFAULT_CIPHER_ALGORITHM));
		} catch (Exception e) {
			LOGGER.error("AES解密异常", e);
			return data;
		}
	}
	
	/**
	 * 解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, Key key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 解密
	 * @param data 待解密数据
	 * @param key 二进制密钥
	 * @param cipherAlgorithm 加密算法/工作模式/填充方式
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws GeneralSecurityException {
		// 还原密钥
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @param cipherAlgorithm 加密算法/工作模式/填充方式
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws GeneralSecurityException {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 执行操作
		return cipher.doFinal(data);
	}
	
	public static void main(String[] args) throws Exception {
		Key k = toKey(GTSKEY.getBytes());
		String data = "AES数据";
		System.out.println("加密前数据: string:" + data);
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("加密后数据: hexStr:" + Hex.encodeHexString(encryptData));
		byte[] decryptData = decrypt(encryptData, k);
		System.out.println("解密后数据: string:" + new String(decryptData));
		
	}
}

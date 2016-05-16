package com.mljr.framework.core.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @Description: DES加解密
 * @ClassName: DesCoder
 * @author gaoxiang
 * @date 2015年11月18日 下午9:42:29
 */ 
public class DesCoder {
	
	/**
	 * 密钥算法
	 * @version 1.0
	 * @author
	 */
	public static final String KEY_ALGORITHM = "DESede";
	
	/**
	 * 加密/解密算法/工作模式/填充方式
	 * @version 1.0
	 * @author
	 */
	public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
	
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return key 密钥
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		// 实例化DES密钥材料
		DESedeKeySpec dks = new DESedeKeySpec(key);
		// 实例化秘密密钥工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		// 生成秘密密钥
		return keyFactory.generateSecret(dks);
	}
	
	/**
	 * 解密
	 * 
	 * @param data 待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
	        InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 还原密钥
		Key k = toKey(key);
		/**
		 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		 */
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}
	
	public static byte[] encrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
	        InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 还原密钥
		Key k = toKey(key);
		/**
		 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
		 */
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, k);
		
		// 执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 生成密钥
	 * 
	 * @return byte[] 二进制密钥
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] initKey() throws NoSuchAlgorithmException {
		/**
		 * 实例化 使用128位或192位长度密钥 KeyGenerator.getInstance(KEY_ALGORITHM,"BC");
		 */
		KeyGenerator kg;
		kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		/**
		 * 初始化 使用128位或192位长度密钥，按如下代码实现 kg.init(128); kg.init(192);
		 */
		kg.init(168);
		// 生成秘密密钥
		SecretKey secretKey = kg.generateKey();
		// 获得密钥的二进制编码形式
		return secretKey.getEncoded();
	}
	
	/**
	 * Description: 10进制数组转换16进制数组
	 */
	public static String printbytes(String tip, byte[] b) {
		String ret = "";
		String str;
		for (int i = 0; i < b.length; i++) {
			str = Integer.toHexString((int) (b[i] & 0xff));
			if (str.length() == 1) {
				str = "0" + str;
			}
			ret = ret + str + " ";
		}
		return ret;
	}
}

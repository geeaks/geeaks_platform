package com.mljr.carfinance.core.property;

import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import com.mljr.framework.core.utils.AESCoder;

/**
 * @Description: 基于AES加解密定制的配置文件属性替换
 * @ClassName: CustomerPlaceholderConfigurer
 * @author gaoxiang
 * @date 2016年5月13日 下午4:14:42
 */ 
public class CustomerPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	private String propertyEncryptKey;
	
	private final static String prefix = "$[";
	
	private final static String suffix = "]";
	
	/**
	 * @Fields key : 临时key
	 */ 
	private static String key = "4824a32081415369c2770a6950e10c1f";
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		propertyEncryptKey = key;
		if (StringUtils.isNotBlank(propertyEncryptKey)) {
			reSetProperty(props, propertyEncryptKey);
		}
		super.processProperties(beanFactory, props);
	}
	
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props) {
		return props.getProperty(placeholder);
	}
	
	private Properties reSetProperty(Properties props, String key) {
		Set<Object> keySet = props.keySet();
		for (Object o : keySet) {
			String pKey = (String) o;
			String pValue = props.getProperty(pKey);
			props.setProperty(pKey, this.findValue(pValue, key));
		}
		return props;
	}
	
	private String findValue(String encodeValue, String deCodeKey) {
		String value = encodeValue;
		try {
			if (StringUtils.isNotBlank(encodeValue) && StringUtils.startsWith(encodeValue, prefix) && StringUtils.endsWith(encodeValue, suffix)) {
				value = AESCoder.decodeByAes(deCodeKey, StringUtils.removeEnd(StringUtils.removeStart(encodeValue, prefix), suffix));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public String getPropertyEncryptKey() {
		return propertyEncryptKey;
	}
	
	public void setPropertyEncryptKey(String propertyEncryptKey) {
		this.propertyEncryptKey = propertyEncryptKey;
	}
	
	public static String doEncode(String value) {
		try {
			return "$[" + AESCoder.encodeByAes(key, value) + "]";
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void main(String[] args) {
		String m = "newsit2014";
		try {
			String encodeByAes = AESCoder.encodeByAes(key, m);
			System.out.println("密文 :" + prefix + encodeByAes + suffix);
			System.out.println("明文 ： " + AESCoder.decodeByAes(key, encodeByAes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

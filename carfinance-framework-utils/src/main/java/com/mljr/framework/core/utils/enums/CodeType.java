package com.mljr.framework.core.utils.enums;

import com.mljr.framework.core.utils.JavaEnumUtils;

/**
 * @Description: 加密类型
 * @ClassName: CodeType
 * @author gaoxiang
 * @date 2015年11月23日 下午10:57:41
 */
public enum CodeType {
	
	AES("AES", "AES加密类型"), MD5("MD5", "MD5加密类型");
	
	/**
	 * 构造函数
	 * @param code 编码
	 * @param description 说明
	 */
	private CodeType(String code, String description) {
		this.code = code;
		this.description = description;
		JavaEnumUtils.put(this.getClass().getName() + code, this);
	}
	
	/**
	 * 一个便利的方法，方便使用者通过code获得枚举对象，
	 * 对于非法状态，以个人处理&lt;/b&gt;
	 * @param code
	 * @return
	 */
	public static CodeType valueByCode(String code) {
		Object obj = JavaEnumUtils.get(CodeType.class.getName() + code);
		if (null != obj) {
			return (CodeType) obj;
		}
		return AES;
	}
	
	/** 编码 */
	private String code;
	
	/** 描述的KEY */
	private String description;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}

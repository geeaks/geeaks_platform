package com.mljr.framework.core.utils.enums;

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

package com.mljr.framework.core.utils.mail;

import org.apache.commons.lang.StringUtils;
import com.mljr.framework.core.utils.AESCoder;

public class MailSenderUtil {
	
	private String emailAddress;
	
	private String emailPassword;
	
	private String mailServerHost;
	
	private String mailServerPort;
	
	private String toAddress;
	
	/**
	 * @Description: 网信金融发送运营异常信息
	 * @param subject
	 * @param content
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2015-7-22 下午3:39:02
	 */
	public void sendWXJRMail(String subject, String content) {
		MailSenderInfo mailInfo = setMailInfo(subject, content);
		if (StringUtils.isNotEmpty(toAddress)) {
			String[] toAdressArray = toAddress.split(",");
			for (String to : toAdressArray) {
				mailInfo.setToAddress(to);
				SimpleMailSender.sendHtmlMail(mailInfo);
			}
		}
	}
	
	/**
	 * @Description: 向指定邮箱地址发送邮件
	 * @param subject
	 * @param content
	 * @param address
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2015-7-22 上午11:54:08
	 */
	public void sendHTMLMail(String subject, String content, String address) {
		MailSenderInfo mailInfo = setMailInfo(subject, content);
		if (StringUtils.isNotEmpty(address)) {
			mailInfo.setToAddress(address);
			SimpleMailSender.sendHtmlMail(mailInfo);
		}
	}
	
	private MailSenderInfo setMailInfo(String subject, String content) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mailServerHost);
		mailInfo.setMailServerPort(mailServerPort);
		mailInfo.setValidate(true);
		mailInfo.setUserName(emailAddress);
		if (StringUtils.isNotEmpty(emailPassword)) {
			mailInfo.setPassword(AESCoder.gtsDecrypt(emailPassword));// 邮箱密码
		}
		mailInfo.setFromAddress(emailAddress);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);
		return mailInfo;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailPassword() {
		return emailPassword;
	}
	
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getMailServerHost() {
		return mailServerHost;
	}
	
	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}
	
	public String getMailServerPort() {
		return mailServerPort;
	}
	
	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}
	
	public static void main(String args[]) {
		MailSenderUtil mailSenderUtil = new MailSenderUtil();
		mailSenderUtil.setToAddress("vsflyhigh@qq.com");
		mailSenderUtil.sendWXJRMail("test", "test");
	}
}

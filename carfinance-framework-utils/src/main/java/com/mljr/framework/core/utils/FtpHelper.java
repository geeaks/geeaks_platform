package com.mljr.framework.core.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FtpHelper.class);
	
	private FTPClient ftp = null;
	
	@Value("#{settings['gts.ftp.server']}")
	private String server;
	
	@Value("#{settings['gts.ftp.port']}")
	private int port = 21;
	
	@Value("#{settings['gts.ftp.userName']}")
	private String userName;
	
	@Value("#{settings['gts.ftp.password']}")
	private String password;
	
	private Document document;
	
	public FtpHelper() {
		super();
		// 初始化
		ftp = new FTPClient();
	}
	
	public FtpHelper(String server, int port, String userName, String password) {
		this.server = server;
		if (this.port > 0) {
			this.port = port;
		}
		this.userName = userName;
		this.password = password;
		// 初始化
		ftp = new FTPClient();
	}
	
	/**
	 * 连接FTP服务器
	 * 
	 * @param server
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public FTPClient connectFTPServer() throws Exception {
		try {
			LOGGER.debug(
					"ftp客户端信息：" + "IP:【" + this.server + "】port:【" + this.port + "】userName:【" + this.userName + "】password:【" + this.password + "】");
			if (ftp == null) {
				ftp = new FTPClient();
			}
			ftp.configure(getFTPClientConfig());
			ftp.connect(this.server, this.port);
			if (!ftp.login(this.userName, this.password)) {
				ftp.logout();
				ftp = null;
				return ftp;
			}
			// 文件类型,默认是ASCII
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.setControlEncoding("UTF-8");
			// 设置被动模式
			// ftp.enterLocalPassiveMode();
			ftp.setConnectTimeout(8000);
			ftp.setBufferSize(1024);
			// 响应信息
			int replyCode = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				// 关闭Ftp连接
				closeFTPClient();
				// 释放空间
				ftp = null;
				throw new Exception("登录FTP服务器失败,请检查![Server:" + server + "、" + "User:" + userName + "、" + "Password:" + password);
			} else {
				return ftp;
			}
		} catch (Exception e) {
			LOGGER.error("创建FTP连接异常:", e);
			ftp.disconnect();
			ftp = null;
			throw e;
		}
	}
	
	/**
	 * 配置FTP连接参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public FTPClientConfig getFTPClientConfig() throws Exception {
		String systemKey = FTPClientConfig.SYST_NT;
		String serverLanguageCode = "zh";
		FTPClientConfig conf = new FTPClientConfig(systemKey);
		conf.setServerLanguageCode(serverLanguageCode);
		conf.setDefaultDateFormatStr("yyyy-MM-dd");
		return conf;
	}
	
	/**
	 * 向FTP根目录上传文件
	 * 
	 * @param localFile
	 * @param newName
	 *            新文件名
	 * @throws Exception
	 */
	public Boolean uploadFile(String localFile, String newName) throws Exception {
		InputStream input = null;
		boolean success = false;
		try {
			File file = null;
			if (checkFileExist(localFile)) {
				file = new File(localFile);
			}
			input = new FileInputStream(file);
			success = ftp.storeFile(newName, input);
			if (!success) {
				throw new Exception("文件上传失败!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return success;
	}
	
	/**
	 * 向FTP根目录上传文件
	 * 
	 * @param input
	 * @param newName
	 *            新文件名
	 * @throws Exception
	 */
	public Boolean uploadFile(InputStream input, String newName) throws Exception {
		boolean success = false;
		try {
			success = ftp.storeFile(newName, input);
			if (!success) {
				throw new Exception("文件上传失败!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return success;
	}
	
	/**
	 * 向FTP指定路径上传文件
	 * 
	 * @param localFile
	 * @param newName
	 *            新文件名
	 * @param remoteFoldPath
	 * @throws Exception
	 */
	public Boolean uploadFile(String localFile, String newName, String remoteFoldPath) throws Exception {
		InputStream input = null;
		boolean success = false;
		try {
			File file = null;
			if (checkFileExist(localFile)) {
				file = new File(localFile);
			}
			input = new FileInputStream(file);
			// 改变当前路径到指定路径
			if (!this.changeDirectory(remoteFoldPath)) {
				LOGGER.info("服务器路径不存!");
				return false;
			}
			success = ftp.storeFile(newName, input);
			if (!success) {
				throw new Exception("文件上传失败!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return success;
	}
	
	/**
	 * 向FTP指定路径上传文件
	 * 
	 * @param input
	 * @param newName
	 *            新文件名
	 * @param remoteFoldPath
	 * @throws Exception
	 */
	public Boolean uploadFile(InputStream input, String newName, String remoteFoldPath) throws Exception {
		boolean success = false;
		try {
			// 改变当前路径到指定路径
			if (!this.changeDirectory(remoteFoldPath)) {
				LOGGER.info("服务器路径不存!");
				return false;
			}
			success = ftp.storeFile(newName, input);
			if (!success) {
				throw new Exception("文件上传失败!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return success;
	}
	
	/**
	 * @Description: 从FTP服务器下载文件
	 * @param remotePath
	 *            FTP路径(不包含文件名)
	 * @param fileName
	 *            下载文件名
	 * @param localPath
	 *            本地路径
	 * @throws Exception
	 * @return Boolean 返回类型
	 * @author gaoxiang
	 * @date 2015-5-29 下午2:01:13
	 */
	public Boolean downloadFile(String remotePath, String fileName, String localPath) throws Exception {
		BufferedOutputStream output = null;
		boolean success = false;
		try {
			// 检查本地路径
			this.checkFileExist(localPath);
			// 改变工作路径
			if (!this.changeDirectory(remotePath)) {
				LOGGER.info("服务器路径不存在");
				return false;
			}
			// 列出当前工作路径下的文件列表
			List<FTPFile> fileList = this.getFileList();
			if (fileList == null || fileList.size() == 0) {
				LOGGER.info("服务器当前路径下不存在文件！");
				return success;
			}
			for (FTPFile ftpfile : fileList) {
				if (ftpfile.getName().equals(fileName)) {
					File localFilePath = new File(localPath + File.separator + ftpfile.getName());
					output = new BufferedOutputStream(new FileOutputStream(localFilePath));
					success = ftp.retrieveFile(ftpfile.getName(), output);
				}
			}
			if (!success) {
				throw new Exception("文件下载失败!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (output != null) {
				output.close();
			}
		}
		return success;
	}
	
	/**
	 * @Description: 从FTP服务器获取文件流
	 * @param remoteFilePath
	 *            FTP路径
	 * @throws Exception
	 * @return InputStream 返回类型
	 * @author gaoxiang
	 * @date 2015-5-29 下午2:01:53
	 */
	public InputStream downloadFile(String remoteFilePath) throws Exception {
		InputStream retrieveFileStream = ftp.retrieveFileStream(remoteFilePath);
		return retrieveFileStream;
	}
	
	/**
	 * 获取FTP服务器上指定路径下的文件列表
	 * 
	 * @param filePath
	 * @return
	 */
	public List<FTPFile> getFtpServerFileList(String remotePath) throws Exception {
		FTPListParseEngine engine = ftp.initiateListParsing(remotePath);
		List<FTPFile> ftpfiles = Arrays.asList(engine.getNext(25));
		return ftpfiles;
	}
	
	/**
	 * 获取FTP服务器上[指定路径]下的文件列表
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<FTPFile> getFileList(String remotePath) throws Exception {
		List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles(remotePath));
		return ftpfiles;
	}
	
	/**
	 * 获取FTP服务器[当前工作路径]下的文件列表
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public List<FTPFile> getFileList() throws Exception {
		List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles());
		return ftpfiles;
	}
	
	/**
	 * 改变FTP服务器工作路径
	 * 
	 * @param remoteFoldPath
	 */
	public Boolean changeDirectory(String remoteFoldPath) throws Exception {
		return ftp.changeWorkingDirectory(remoteFoldPath);
	}
	
	/**
	 * 删除文件
	 * 
	 * @param remoteFilePath
	 * @return
	 * @throws Exception
	 */
	public Boolean deleteFtpServerFile(String remoteFilePath) throws Exception {
		return ftp.deleteFile(remoteFilePath);
	}
	
	/**
	 * 创建目录
	 * 
	 * @param remoteFoldPath
	 * @return
	 */
	public boolean createFold(String remoteFoldPath) throws Exception {
		boolean flag = ftp.makeDirectory(remoteFoldPath);
		if (!flag) {
			throw new Exception("创建目录失败");
		}
		return false;
	}
	
	/**
	 * 删除目录
	 * 
	 * @param remoteFoldPath
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFold(String remoteFoldPath) throws Exception {
		return ftp.removeDirectory(remoteFoldPath);
	}
	
	/**
	 * 删除目录以及文件
	 * 
	 * @param remoteFoldPath
	 * @return
	 */
	public boolean deleteFoldAndsubFiles(String remoteFoldPath) throws Exception {
		boolean success = false;
		List<FTPFile> list = this.getFileList(remoteFoldPath);
		if (list == null || list.size() == 0) {
			return deleteFold(remoteFoldPath);
		}
		for (FTPFile ftpFile : list) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				success = deleteFoldAndsubFiles(remoteFoldPath + "/" + name);
				if (!success)
					break;
			} else {
				success = deleteFtpServerFile(remoteFoldPath + "/" + name);
				if (!success)
					break;
			}
		}
		if (!success)
			return false;
		success = deleteFold(remoteFoldPath);
		return success;
	}
	
	/**
	 * 检查本地路径是否存在
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean checkFileExist(String filePath) throws Exception {
		boolean flag = false;
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("本地路径不存在,请检查!");
		} else {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 创建XML文件
	 * 
	 * @return
	 */
	public Element getCurrentElement() {
		document = DocumentHelper.createDocument();
		return document.addElement("root");
	}
	
	/**
	 * 生成目录XML文件
	 */
	@SuppressWarnings("unused")
	public void createDirectoryXML(String remotePath, Element fatherElement) throws Exception {
		List<FTPFile> list = this.getFileList();
		for (FTPFile ftpfile : list) {
			Element currentElement = fatherElement; // 当前的目录节点
			String newRemotePath = remotePath + ftpfile.getName();
			if (ftpfile.isDirectory()) {
				Element dirElement = fatherElement.addElement("dir");
				dirElement.addAttribute("name", ftpfile.getName());
				currentElement = dirElement;
				this.changeDirectory(newRemotePath); // 从根目录开始
				createDirectoryXML(newRemotePath, dirElement);
			} else {
				Element fileElement = fatherElement.addElement("file");// 文件节点
				fileElement.setText(ftpfile.getName());
			}
		}
	}
	
	/**
	 * 保存xml
	 */
	public void saveXML() {
		XMLWriter output = new XMLWriter();
		// 输出格式化
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			output = new XMLWriter(new FileWriter("E:/dir.xml"), format);
			output.write(this.document);
			output.close();
		} catch (IOException e) {
			LOGGER.error("保存xml系统异常", e);
		}
	}
	
	/**
	 * 关闭FTP连接
	 * 
	 * @param ftp
	 * @throws Exception
	 */
	public void closeFTPClient(FTPClient ftp) throws Exception {
		try {
			if (ftp.isConnected())
				ftp.logout();
			ftp.disconnect();
		} catch (Exception e) {
			throw new Exception("关闭FTP服务出错!");
		}
	}
	
	/**
	 * 关闭FTP连接
	 * 
	 * @throws Exception
	 */
	public void closeFTPClient() throws Exception {
		this.closeFTPClient(this.ftp);
	}
	
	/**
	 * Get Attribute Method
	 * 
	 */
	public FTPClient getFtp() {
		return ftp;
	}
	
	public String getServer() {
		return server;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getPort() {
		return port;
	}
	
	/**
	 * Set Attribute Method
	 * 
	 */
	public void setFtp(FTPClient ftp) {
		this.ftp = ftp;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public static boolean downFile(String url, int port, String username, String password, String remotePath, String fileName, String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream outputStream = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), outputStream);
					outputStream.close();
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			LOGGER.error("下载文件系统异常", e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	public static void main(String[] args) {
		try {
			FtpHelper ftpHelper = new FtpHelper("127.0.0.1", 21, "linwei", "123456");
			ftpHelper.connectFTPServer();
			InputStream inputStream = ftpHelper.downloadFile("Java_Concurrent_Example.pptx");
			File file = new File("E:/test/Java_Concurrent_Example.pptx");
			int a = 0;
			while ((a = inputStream.read()) != -1) {
				new FileOutputStream(file).write(a);
			}
			ftpHelper.closeFTPClient();
		} catch (Exception e) {
			LOGGER.error("异常信息：", e);
		}
	}
}

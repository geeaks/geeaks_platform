package com.mljr.framework.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 地域信息
 * @ClassName: AddressUtils
 * @author gaoxiang
 * @date 2015年11月28日 上午3:28:16
 */
public class AddressUtils {
	
	/**
	 * @Description: 根据IP地址获取详细的地域信息
	 * @param ip
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月30日 下午6:29:40
	 */
	public static String getIPAddresses(String ip){
		try {
			return getAddresses("ip="+ip, "utf-8");
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * @Description: 根据IP获取地址
	 * @param content 请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encodingString 服务器端请求编码。如GBK,UTF-8等
	 * @throws UnsupportedEncodingException
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月30日 下午5:43:45
	 */
	@SuppressWarnings("unchecked")
	public static String getAddresses(String content, String encodingString) throws Exception {
		// 这里调用taobao的接口
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		String address = "";
		// 取得IP所在的省市区信息
		String returnStr = getResult(urlStr, content, encodingString);
		if (returnStr == null) {
			return address;
		}
		JSONObject obj = JSONObject.parseObject(returnStr);
		Integer code = (Integer) obj.get("code");
		if(0 == code){
			Map<String,String> data = (Map<String,String>) obj.get("data");
			address += data.get("country");//国家
			address += data.get("area");//区域
			address += data.get("region");//省份
			address += data.get("city");//城市
			address += data.get("county");//县
		}
		return address;
	}
	
	/**
	 * @Description: 获取
	 * @param urlStr 请求的地址
	 * @param content 请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding 服务器端请求编码。如GBK,UTF-8等
	 * @return String 返回类型
	 * @author gaoxiang
	 * @date 2015年11月30日 下午5:42:19
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
			// 以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String ip = "106.20.70.55";
		String address = AddressUtils.getIPAddresses(ip);
		System.out.println(address);
	}
}

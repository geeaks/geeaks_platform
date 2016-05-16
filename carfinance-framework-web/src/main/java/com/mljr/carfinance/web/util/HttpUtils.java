package com.mljr.carfinance.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Description: HTTP工具类
 * @ClassName: HttpUtils
 * @author gaoxiang
 * @date 2015年11月24日 下午10:48:52
 */ 
public final class HttpUtils {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * 链接超时
	 */
	private static int connectTimeout = 15000;
	
	/**
	 * 读取超时
	 */
	private static int readTimeout = 50000;
	
	/**
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @return
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters, String charset) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendPost(reqUrl, parameters, charset);
			if (200 == urlConn.getResponseCode()) {
				String responseContent = getContent(urlConn, charset);
				return responseContent.trim();
			} else {
				throw new RuntimeException("http responseCode is " + urlConn.getResponseCode());
			}
		} catch (IOException e) {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
			logger.error(
			        new StringBuilder(e.getMessage()).append("|").append(JSON.toJSONString(parameters,
			                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty)).toString(),
			        e);
			throw new RuntimeException("http responseCode is not [200]");
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}
	
	public static String doPost(String reqUrl, String body, String charset) {
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(connectTimeout);
			urlConn.setReadTimeout(readTimeout);
			urlConn.setDoOutput(true);
			
			byte[] b = body.getBytes(charset);
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
			String content = getContent(urlConn, charset);
			if (200 == urlConn.getResponseCode()) {
				return content.trim();
			} else {
				throw new RuntimeException("http responseCode is " + urlConn.getResponseCode());
			}
		} catch (Exception e) {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
			logger.error(
			        new StringBuilder(e.getMessage()).append("|").append(JSON.toJSONString(body,
			                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty)).toString(),
			        e);
			return "";
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}
	
	/**
	 * 
	 * @param urlConn
	 * @return
	 */
	private static String getContent(HttpURLConnection urlConn, String charset) {
		try {
			String responseContent = null;
			InputStream in = urlConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, charset));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @return
	 */
	private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters, String charset) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters, charset);
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(connectTimeout);// （单位：毫秒）jdk
			urlConn.setReadTimeout(readTimeout);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}
	
	/**
	 * 将parameters中数据转换成用"&"链接的http请求参数形式
	 * 
	 * @param parameters
	 * @return
	 */
	public static String generatorParamString(Map<String, String> parameters, String charset) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				value = StringUtils.isBlank(value) ? "" : value;
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, charset));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				
				if (iter.hasNext()) {
					params.append("&");
				}
			}
		}
		return params.toString();
	}
	
	/**
	 * URL转map
	 * 
	 * @param param
	 * @return
	 */
	public static Map<String, String> getUrlParams(String url) {
		Map<String, String> map = new HashMap<String, String>(16);
		if (StringUtils.isBlank(url)) {
			return map;
		}
		String[] params = StringUtils.substringAfter(url, "?").split("&");
		for (int i = 0; i < params.length; i++) {
			String[] p = params[i].split("=");
			if (p.length == 1 && StringUtils.isNotBlank(p[0])) {
				map.put(p[0], null);
			} else if (p.length == 2 && StringUtils.isNotBlank(p[0])) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param link
	 * @param charset
	 * @return
	 */
	public static String doGet(String link, String charset, int timeout) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setReadTimeout(timeout);
			conn.setConnectTimeout(timeout);
			if (200 == conn.getResponseCode()) {
				BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				for (int i = 0; (i = in.read(buf)) > 0;) {
					out.write(buf, 0, i);
				}
				out.flush();
				String s = new String(out.toByteArray(), charset);
				return s;
			} else {
				throw new RuntimeException("http responseCode is " + conn.getResponseCode());
			}
		} catch (Exception e) {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	
	/**
	 * 
	 * @param link
	 * @return
	 */
	public static String doGet(String link, String charset) {
		return doGet(link, charset, readTimeout);
	}
	
	public static String doGet(String link, int timeout, String charset) {
		return doGet(link, charset, timeout);
	}
	
	/**
	 * url 编码
	 * 
	 * @param url
	 * @param enc
	 * @return
	 */
	public static String urlEncoding(String url, String enc) {
		try {
			return URLEncoder.encode(url, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 向HTTPS地址发送POST请求
	 * 
	 * @param reqURL
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应内容
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation", "resource" })
	public static String sendSSLPostRequest(String reqURL, Map<String, String> params) throws Exception {
		String responseContent = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
			
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
			
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
			// 请求超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			HttpPost httpPost = new HttpPost(reqURL); // 创建HttpPost
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			
			HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
			HttpEntity entity = response.getEntity();// 获取响应实体
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, "UTF-8");//
				EntityUtils.consume(entity); // Consume response content
			}
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
		}
		return responseContent;
	}
	
	// ============域访问器=================
	public static int getConnectTimeout() {
		return connectTimeout;
	}
	
	public static void setConnectTimeout(int connectTimeout) {
		HttpUtils.connectTimeout = connectTimeout;
	}
	
	public static int getReadTimeout() {
		return readTimeout;
	}
	
	public static void setReadTimeout(int readTimeout) {
		HttpUtils.readTimeout = readTimeout;
	}
	
	public static void main(String[] args) {
		System.out.println(HttpUtils.doGet(
		        "http://s.mljrpay.com/salary/salary/memberInfo?token=5NIVphJARzhM3TMzLnV39qsoubWcoYf4&reqId=375B1BA8090ACA47991352F3C43AAAF9&bizId=46cd8a4a-c27a-4c69-b0c8-baaff59106ca&merchantId=M200000081",
		        "UTF-8"));
	}
}

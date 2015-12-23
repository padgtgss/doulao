package common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import common.entity.BaseBusinessException;
import common.var.exception.GroupError;
import common.var.constants.SystemConstant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;

public class HttpUtil {

	private static Log logger = LogFactory.getLog(HttpUtil.class);
	private static final int SC_PEMASS_ERROR = 508;
	private static final String DEFAULT_CHARSET = "UTF-8"; // 默认字符集
	private static CloseableHttpClient client = null;

	static {
		try {
			/*-- Configuration SSL --*/
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", new PlainConnectionSocketFactory()).register("https", sslsf).build();

			/*-- Init Pool Manager --*/
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(2000);
			cm.setDefaultMaxPerRoute(20);

			/*-- Build Client --*/
			client = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm).build();

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 发送GET请求
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 *            请求参数
	 * @return 字符串结果
	 */
	public static String get(String url, Map<String, Object> param) {
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpGet = new HttpGet(doGetURL(url, param));
			response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 == statusCode || SC_PEMASS_ERROR == statusCode) {
				result = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			release(httpGet, response);
		}
		return result;
	}

	/**
	 * 发送POST请求
	 * <p/>
	 * 默认字符集：UTF-8
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 * @return
	 */
	public static String post(String url, Map<String, Object> param) {
		return post(url, param, DEFAULT_CHARSET);
	}

	/**
	 * 发送POST请求
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 *            请求参数
	 * @param charset
	 *            parm参数字符编码
	 * @return
	 */
	public static String post(String url, Map<String, Object> param, String charset) {
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setEntity(doGetEntity(param, charset));
			response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 == statusCode || SC_PEMASS_ERROR == statusCode) {
				result = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			release(httpPost, response);
		}
		return result;
	}

	/**
	 * 发送PUT请求
	 * <p/>
	 * 默认字符集：UTF-8
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static String put(String url, Map<String, Object> param) {
		return put(url, param, DEFAULT_CHARSET);
	}

	/**
	 * 发送PUT请求
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 *            请求参数
	 * @param charset
	 *            param参数字符编码
	 * @return
	 */
	public static String put(String url, Map<String, Object> param, String charset) {
		HttpPut httpPut = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpPut = new HttpPut(url);
			httpPut.setEntity(doGetEntity(param, charset));
			response = client.execute(httpPut);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 == statusCode || SC_PEMASS_ERROR == statusCode) {
				result = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			release(httpPut, response);
		}
		return result;
	}

	/**
	 * 发送DELETE请求
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 *            请求参数
	 * @return
	 */
	public static String delete(String url, Map<String, Object> param) {
		HttpDelete httpDelete = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpDelete = new HttpDelete(doGetURL(url, param));
			response = client.execute(httpDelete);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 == statusCode || SC_PEMASS_ERROR == statusCode) {
				result = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			release(httpDelete, response);
		}
		return result;
	}

	/**
	 * 释放资源
	 *
	 * @param requestBase
	 * @param response
	 */
	private static void release(HttpRequestBase requestBase, CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (requestBase != null) {
			requestBase.releaseConnection();
		}
	}

	/**
	 * 将map集合封装成Entity对象
	 *
	 * @param param
	 * @return
	 */
	private static HttpEntity doGetEntity(Map<String, Object> param, String charset) {
		if (param == null || charset == null) {
			return null;
		}

		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> me : param.entrySet()) {
			formParams.add(new BasicNameValuePair(me.getKey(), me.getValue().toString()));
		}

		try {
			return new UrlEncodedFormEntity(formParams, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 拼接请求字符串，将请求参数拼接到URL地址后面
	 *
	 * @param url
	 *            请求URL地址
	 * @param param
	 *            参数
	 * @return
	 */
	private static String doGetURL(String url, Map<String, Object> param) {
		if (param == null || param.size() == 0) {
			return url;
		}

		if (url == null || StringUtils.isBlank(url)) {
			throw new RuntimeException("url is not allow null");
		}

		StringBuilder sb = new StringBuilder(10);
		sb.append(url);

		if (!url.endsWith("?")) {
			sb.append("?");
		}

		for (Map.Entry<String, Object> me : param.entrySet()) {
			sb.append(me.getKey()).append("=").append(me.getValue()).append("&");
		}

		String urlString = sb.toString();
		urlString = urlString.substring(0, urlString.length() - 1);

		return urlString;
	}

	/**
	 * 创建子账户
	 * @param friendlyName
	 * @return
	 */
	public static String pushAboutSubAccounts(String friendlyName,String RequestMethodAboutSubAccount){
		//设置包体
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("appId","8a48b5514fd49643014fda46ebdf11e6");//应用Id
		map.put("friendlyName",friendlyName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5("8a48b5514fd49643014fda42a90311cc"+ "92092172061b41e9b231800c45e9c95e" + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/Accounts/8a48b5514fd49643014fda42a90311cc"+RequestMethodAboutSubAccount+"?sig="+hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json;");
		httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
		String code = "8a48b5514fd49643014fda42a90311cc" + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
	/*		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr,"utf-8");

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);
				closeableHttpClient.close();
				return result;	//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}

	/**
	 * 群相关的推送
	 * @param map 请求包体的map
	 * @param userId	子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 * @param requestMethod 业务功能，拼在请求路径里面
	 * @return
	 */
	public static String pushGroupUrlByMap(Map<String, Object> map,String userId,String requestMethod){
		String subAccountSid = "8a48b5514fd49643014fda46ebdf11e6" + userId;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5("8a48b5514fd49643014fda46ebdf11e6"+ "e21cf13ea27a2f48e0f7f1dce1ec2a8c" + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/"
				+subAccountSid + requestMethod +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/xml;");
		httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
		String code = "8a48b5514fd49643014fda46ebdf11e6" + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
/*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
/*			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);*/
			String xmlStr = FormatStructure.MapToXmlStr(map);
			StringEntity stringEntity = new StringEntity(xmlStr,"utf-8");

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);//xml格式的字符串
				logger.info("容联群方法："+requestMethod);
				logger.info("容联返回："+result);
				result = FormatStructure.XmlToJsonStr(result);
				closeableHttpClient.close();
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}

	/**
	 * 群相关的推送
	 * @param xmlStr 请求包体
	 * @param userId	子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 * @param requestMethod 业务功能，拼在请求路径里面
	 * @return
	 */
	public static String pushGroupUrlByObject(String xmlStr,String userId,String requestMethod){
		String subAccountSid = "8a48b5514fd49643014fda46ebdf11e6"+userId;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5("8a48b5514fd49643014fda46ebdf11e6"+ "e21cf13ea27a2f48e0f7f1dce1ec2a8c" + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/"
				+subAccountSid + requestMethod +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/xml;");
		httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
		String code = "8a48b5514fd49643014fda46ebdf11e6" + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
/*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
/*			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);*/
			StringEntity stringEntity = new StringEntity(xmlStr,"utf-8");

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);//xml格式的字符串
				logger.info("容联群方法："+requestMethod);
				logger.info("容联返回："+result);
				result = FormatStructure.XmlToJsonStr(result);
				closeableHttpClient.close();
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
			}
		}
	}

	/**
	 * 群相关的推送
	 * @param map 请求包体的map
	 * @param userId	子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 * @param requestMethod 业务功能，拼在请求路径里面
	 * @return
	 */
	public static String pushGroupUrlByMapReturnXml(Map<String, Object> map,String userId,String requestMethod){
		String subAccountSid = "8a48b5514fd49643014fda46ebdf11e6" + userId;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5("8a48b5514fd49643014fda46ebdf11e6"+ "e21cf13ea27a2f48e0f7f1dce1ec2a8c" + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/"
				+subAccountSid + requestMethod +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/xml;");
		httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
		String code = "8a48b5514fd49643014fda46ebdf11e6" + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
/*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
/*			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);*/
			String xmlStr = FormatStructure.MapToXmlStr(map);
			StringEntity stringEntity = new StringEntity(xmlStr,"utf-8");

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);//xml格式的字符串
				logger.info("容联群方法："+requestMethod);
				logger.info("容联返回："+result);
				closeableHttpClient.close();
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}

	/**
	 * 群消息
	 * @param map 请求包体的map
	 * @return
	 */
	public static String pushGroupMsg(Map<String, Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5(SystemConstant.ACCOUNTS_ID+ SystemConstant.ACCOUNTS_TOKEN + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/Accounts/"
				+SystemConstant.ACCOUNTS_ID + "/IM/PushMsg" +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json;");
		httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
		String code = SystemConstant.ACCOUNTS_ID + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
/*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr,"utf-8");
/*			String xmlStr = FormatStructure.MapToXmlStr(map);
			StringEntity stringEntity = new StringEntity(xmlStr);*/

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);
				logger.info("容联消息推送:"+"/IM/PushMsg");
				logger.info("容联返回："+result);

				closeableHttpClient.close();
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}

	/**
	 * 群消息
	 * @param map 请求包体的map
	 * @return
	 */
	public static String pushGroupMsg2(Map<String, Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5(SystemConstant.ACCOUNTS_ID+ SystemConstant.ACCOUNTS_TOKEN + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/Accounts/"
				+SystemConstant.ACCOUNTS_ID + "/IM/PushMsg" +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json;");
		httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
		String code = SystemConstant.ACCOUNTS_ID + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
/*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*/
			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);
/*			String xmlStr = FormatStructure.MapToXmlStr(map);
			StringEntity stringEntity = new StringEntity(xmlStr);*/

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);
				logger.info("容联消息推送:"+"/IM/PushMsg");
				logger.info("容联返回："+result);
				closeableHttpClient.close();
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}
	/**
	 * 群相关的推送
	 * @param map 请求包体的map
	 * @param subAccountSid	子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 * @param requestMethod 业务功能，拼在请求路径里面
	 * @return
	 *//*
	public static String pushGroupUrlByMap(Map<String, Object> map,String subAccountSid,String subToken,String requestMethod){
		Map<String, Object> m = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5(subAccountSid+ subToken + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/"
				+subAccountSid + requestMethod +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/xml;");
		httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
		String code = subAccountSid + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
*//*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*//*
*//*			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);*//*
			String xmlStr = FormatStructure.MapToXmlStr(map);
			StringEntity stringEntity = new StringEntity(xmlStr);

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);//xml格式的字符串
				result = FormatStructure.XmlToJsonStr(result);
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.REQUEST_FAILED);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		}
	}

	*//**
	 * 群相关的推送
	 * @param xmlStr 请求包体
	 * @param subAccountSid	子账户Id。由32个英文字母和阿拉伯数字组成的子账户唯一标识符
	 * @param requestMethod 业务功能，拼在请求路径里面
	 * @return
	 *//*
	public static String pushGroupUrlByObject(String xmlStr,String subAccountSid,String subToken,String requestMethod){
		Map<String, Object> m = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String stamp = sdf.format(new Date());
		MD5Util md5 = new MD5Util();
		String hash = md5.string2MD5(subAccountSid+ subToken + stamp);
		String url = "https://app.cloopen.com:8883/2013-12-26/SubAccounts/"
				+subAccountSid + requestMethod +"?sig=" + hash;
		//创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		//HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//设置包头
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/xml;");
		httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
		String code = subAccountSid + ":" + stamp;
		String authorization = (new BASE64Encoder()).encode(code.getBytes());
		httpPost.setHeader("Authorization", authorization);
		try {
			//设置包体
*//*			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				formparams.add(new BasicNameValuePair(key, value));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);*//*
*//*			String jsonStr = JSON.toJSONString(map);
			StringEntity stringEntity = new StringEntity(jsonStr);*//*
			StringEntity stringEntity = new StringEntity(xmlStr);

			httpPost.setEntity(stringEntity);
			// post请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			//getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			//请求成功状态
			if(httpResponse.getStatusLine().getStatusCode()==200){
				//响应内容
				String result = "";
				result = EntityUtils.toString(httpEntity);//xml格式的字符串
				result = FormatStructure.XmlToJsonStr(result);
				return result;//返回json格式的字符串
			}else{
				throw new BaseBusinessException(GroupError.REQUEST_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BaseBusinessException(GroupError.UNKNOWN_ERROR);
			}
		}
	}*/
	
	/**
	 * （测试）调用流量币平台数据
	 * @param params 请求参数
	 * @param key  应答中需要的数据
	 * @return
	 */
	public String getResponse2(List<BasicNameValuePair> params,String key){
		String result = "";
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(SystemConstant.PHP_API_URL);
		httpPost.setHeader("ContentType","application/x-www-form-urlencoded;charset=utf-8");
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(entity);
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity!=null){
				result = EntityUtils.toString(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}

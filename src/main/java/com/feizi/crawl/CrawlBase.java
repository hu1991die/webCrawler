/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.feizi.util.CharsetUtil;

/**  
 * 爬虫基类（抽象类）
 * 主要用于定义一些公共的属性和方法
 * @Author: feizi
 * @Date: 2015年9月29日 下午4:31:39 
 * @ModifyUser: feizi
 * @ModifyDate: 2015年9月29日 下午4:31:39 
 * @Version:V6.0
 */
public abstract class CrawlBase {

	private static Logger log = Logger.getLogger(CrawlBase.class);
	
	//链接源代码
	private String pageSourceCode = "";
	//响应头（返回头）信息
	private Header[] responseHeaders = null;
	
	//连接超时时间
	private static int connectTimeout = 3500;
	//连接读取时间
	private static int readTimeout = 3500;
	//默认最大访问次数
	private static int maxConnectTimes = 3;
	
	//网页默认编码方式
	private static String charsetName = "iso-8859-1";
	private static HttpClient httpClient = new HttpClient();
	
	static{
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
	}
	
	/**
	 * 请求页面（判断是get方式还是post方式）
	  * @Discription:扩展说明
	  * @param urlStr
	  * @param charsetName
	  * @param method
	  * @param params
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年9月29日 下午4:44:01
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年9月29日 下午4:44:01
	 */
	public boolean readPage(String urlStr, String charsetName, String method, HashMap<String, String> params){
		if("post".equals(method) || "POST".equals(method)){
			return readPageByPost(urlStr, charsetName, params);
		}else{
			return readPageByGet(urlStr, charsetName, params);
		}
	}
	
	/**
	 * 判断GET方式访问是否成功
	  * @Discription:GET方式访问页面
	  * @param urlStr
	  * @param charsetName
	  * @param params
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年9月29日 下午4:47:43
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年9月29日 下午4:47:43
	 */
	public boolean readPageByGet(String urlStr, String charsetName, HashMap<String, String> params){
		GetMethod getMethod = createGetMethod(urlStr, params);
		return readPage(getMethod, charsetName, urlStr);
	}
	
	/**
	 * 判断POST方式访问是否成功
	  * @Discription:POST方式访问页面
	  * @param urlStr
	  * @param charsetName
	  * @param params
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年9月29日 下午4:56:54
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年9月29日 下午4:56:54
	 */
	public boolean readPageByPost(String urlStr, String charsetName, HashMap<String, String> params){
		PostMethod postMethod = createPostMethod(urlStr, params);
		return readPage(postMethod, charsetName, urlStr);
	}
	
	/**
	 * 读取页面信息和响应（返回）头信息
	  * @Discription:扩展说明
	  * @param method
	  * @param defaultCharset
	  * @param urlStr
	  * @return 访问是否成功
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年9月29日 下午4:58:15
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年9月29日 下午4:58:15
	 */
	private boolean readPage(HttpMethod method, String defaultCharset, String urlStr){
		int n = maxConnectTimes;
		while(n > 0){
			try {
				if(httpClient.executeMethod(method) != HttpStatus.SC_OK){
					log.error("can not connect " + urlStr + "\t" + (maxConnectTimes - n +1)
							+ "\t" + httpClient.executeMethod(method));
					n--;
				}else{
					//获取返回头信息
					responseHeaders = method.getResponseHeaders();
					//获取页面源代码
					InputStream inputStream = method.getResponseBodyAsStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
					
					StringBuffer stringBuffer = new StringBuffer();
					String lineString = null;
					while((lineString = bufferedReader.readLine()) != null){
						stringBuffer.append(lineString);
						stringBuffer.append("\n");
					}
					
					pageSourceCode = stringBuffer.toString();
					InputStream in = new ByteArrayInputStream(pageSourceCode.getBytes(charsetName));
					String charset = CharsetUtil.getStreamCharset(in, defaultCharset);
					//下面这个判断是为了IP归属地查询特意加上去的
					if("Big5".equals(charset)){
						charset = "gbk";
					}
					
					if(!charsetName.toLowerCase().equals(charset.toLowerCase())){
						pageSourceCode = new String(pageSourceCode.getBytes(charsetName),charsetName);
					}
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(urlStr + " -- can't connect  " + (maxConnectTimes - n + 1));  
                n--; 
			}
		}
		return false;
	}
	
	/**
	 * 设置get请求参数
	  * @Discription:扩展说明
	  * @param urlStr
	  * @param params
	  * @return
	  * @return GetMethod
	  * @Author: feizi
	  * @Date: 2015年10月11日 上午11:20:04
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 上午11:20:04
	 */
	@SuppressWarnings("rawtypes")
	private GetMethod createGetMethod(String urlStr, HashMap<String, String> params){
		GetMethod getMethod = new GetMethod(urlStr);
		if(null == params){
			return getMethod;
		}
		
		Iterator iter = params.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<?, ?> entry = (Entry<?, ?>) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			getMethod.setRequestHeader(key, val);
		}
		return getMethod;
	}
	
	/**
	 * 设置post请求参数
	  * @Discription:扩展说明
	  * @param urlStr
	  * @param params
	  * @return
	  * @return PostMethod
	  * @Author: feizi
	  * @Date: 2015年10月11日 上午11:37:37
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 上午11:37:37
	 */
	private PostMethod createPostMethod(String urlStr, HashMap<String, String> params){
		PostMethod postMethod = new PostMethod(urlStr);
		if(null == params){
			return postMethod;
		}
		
		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			postMethod.setParameter(key, val);
		}
		
		return postMethod;
	}
	
	/**
	 * 不设置任何头信息直接访问网页
	  * @Discription:扩展说明
	  * @param urlStr
	  * @param charsetName
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年10月11日 上午11:42:23
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 上午11:42:23
	 */
	public boolean readPageByGet(String urlStr, String charsetName){
		return this.readPageByGet(urlStr, charsetName, null);
	}
	
	/**
	 * 获取网页源代码
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年10月11日 上午11:44:01
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 上午11:44:01
	 */
	public String getPageSourceCode(){
		return pageSourceCode;
	}
	
	/**
	 * 获取网页返回头信息
	  * @Discription:扩展说明
	  * @return
	  * @return Header[]
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:03:46
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:03:46
	 */
	public Header[] getHeaders(){
		return responseHeaders;
	}
	
	/**
	 *  设置连接超时时间
	  * @Discription:扩展说明
	  * @param timeout
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:04:57
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:04:57
	 */
	public void setConnectTimeout(int timeout){
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
	}
	
	/**
	 * 设置读取超时时间
	  * @Discription:扩展说明
	  * @param timeout
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:06:03
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:06:03
	 */
	public void setReadTimeout(int timeout){
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
	}
	
	/**
	 * 设置最大访问次数，链接失败的情况下使用
	  * @Discription:扩展说明
	  * @param maxConnectTimes
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:08:05
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:08:05
	 */
	public static void setMaxConnectTimes(int maxConnectTimes){
		CrawlBase.maxConnectTimes = maxConnectTimes;
	}
	
	/**
	 * 设置连接超时时间和读取超时时间
	  * @Discription:扩展说明
	  * @param connectTimeout
	  * @param readTimeout
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:10:11
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:10:11
	 */
	public void setTimeout(int connectTimeout, int readTimeout){
		setConnectTimeout(connectTimeout);
		setReadTimeout(readTimeout);
	}
	
	/**
	 * 设置默认编码方式
	  * @Discription:扩展说明
	  * @param charsetName
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午1:11:35
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午1:11:35
	 */
	public static void setCharsetName(String charsetName){
		CrawlBase.charsetName = charsetName;
	}
}

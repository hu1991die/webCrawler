package com.feizi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式工具类
 * @author ljj
 * @time 2015年10月22日 下午8:59:35
 * TODO
 */
public final class RegexUtil {

	private static String ROOT_URL_REGEX = "(http://.*?/)";
	private static String CURRENT_URL_REGEX = "(http://.*/)";
	private static String CHINESE_REGEX = "([\u4e00-\u9fa5]+)";
	
	/**
	 * 构造方法私有，防止实例化
	 */
	private RegexUtil(){
		
	}
	
	/**
	 * 正则匹配结果，每条记录用splitStr进行分割
	 * @param dealStr
	 * @param regexStr
	 * @param splitStr
	 * @param n
	 * @return
	 */
	public static String getString(String dealStr, String regexStr, String splitStr, int n){
		String reStr = "";
		if(null == dealStr || null == regexStr || n < 1 || dealStr.isEmpty()){
			return reStr;
		}
		
		splitStr = (null == splitStr) ? "" : splitStr;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		
		StringBuffer stringBuffer = new StringBuffer();
		while(matcher.find()){
			stringBuffer.append(matcher.group(n).trim());
			stringBuffer.append(splitStr);
		}
		
		reStr = stringBuffer.toString();
		if("" != splitStr && reStr.endsWith(splitStr)){
			reStr = reStr.substring(0, reStr.length() - splitStr.length());
		}
		
		return reStr;
	}
	
	/**
	 * 正则匹配结果，将所有匹配记录组装成字符串
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static String getString(String dealStr, String regexStr, int n){
		return getString(dealStr, regexStr, null, n);
	}
	
	/**
	 * 正则匹配第一条结果
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static String getFirstString(String dealStr, String regexStr, int n){
		if(null == dealStr || null == regexStr || n < 1 || dealStr.isEmpty()){
			return "";
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			return matcher.group(n).trim();
		}
		return "";
	}
	
	/**
	 * 正则匹配结果，将匹配结果组装成数组
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static List<String> getList(String dealStr, String regexStr, int n){
		List<String> reArrayList = new ArrayList<String>();
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()){
			return reArrayList;
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			reArrayList.add(matcher.group(n).trim());
		}
		return reArrayList;
	}
	
	/**
	 * 组装网址，网页的URL
	 * @param url
	 * @param currentUrl
	 * @return
	 */
	private static String getHttpUrl(String url, String currentUrl){
		try {
			url = encodeUrlCh(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(url.indexOf("http") == 0){
			return url;
		}
		if(url.indexOf("/") == 0){
			return getFirstString(currentUrl, ROOT_URL_REGEX, 1) + url.substring(1);
		}
		return getFirstString(currentUrl, CURRENT_URL_REGEX, 1) + url;
	}
	
	/**
	 * 获取和正则匹配的绝对链接地址
	 * @param dealStr
	 * @param regexStr
	 * @param currentUrl
	 * @param n
	 * @return
	 */
	public static List<String> getArrayList(String dealStr, String regexStr, String currentUrl, int n){
		List<String> reArrayList = new ArrayList<String>();
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()){
			return reArrayList;
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			reArrayList.add(getHttpUrl(matcher.group(n).trim(), currentUrl));
		}
		return reArrayList;
	}
	
	/**
	 * 将连接地址中的中文进行编码处理
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeUrlCh(String url) throws UnsupportedEncodingException{
		while(true){
			String s = getFirstString(url, CHINESE_REGEX, 1);
			if("".equals(s)){
				return url;
			}
			url = url.replaceAll(s, URLEncoder.encode(s, "utf-8"));
		}
	}
	
	/**
	 * 获取全部
	 * @param dealStr
	 * @param regexStr
	 * @param array 正则位置数组
	 * @return
	 */
	public static List<String[]> getListArray(String dealStr, String regexStr, int[] array){
		List<String[]> reArrayList = new ArrayList<String[]>();
		if(dealStr == null || regexStr == null || array == null){
			return reArrayList;
		}
		for (int i = 0; i < array.length; i++) {
			if(array[i] < 1){
				return reArrayList;
			}
		}
		
		Pattern pattern =  Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			String[] str = new String[array.length];
			for (int i = 0; i < str.length; i++) {
				str[i] = matcher.group(array[i]).trim();
			}
			reArrayList.add(str);
		}
		return reArrayList;
	}
	
	/**
	 * 获取全部
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return
	 */
	public static List<String> getStringArray(String dealStr, String regexStr, int[] array){
		List<String> reStringList = new ArrayList<String>();
		if(dealStr == null || regexStr == null || array == null){
			return reStringList;
		}
		
		for (int i = 0; i < array.length; i++) {
			if(array[i] < 1){
				return reStringList;
			}
		}
		
		Pattern pattern =  Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				sb.append(matcher.group(array[i]).trim());
			}
			reStringList.add(sb.toString());
		}
		return reStringList;
	}
	
	/**
	 * 获取第一个
	 * @param dealStr
	 * @param regexStr
	 * @param array 正则位置数组
	 * @return
	 */
	public static String[] getFirstArray(String dealStr, String regexStr, int[] array){
		if(dealStr == null || regexStr == null || array == null){
			return null;
		}
		for (int i = 0; i < array.length; i++) {
			if(array[i] < 1){
				return null;
			}
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()){
			String[] str = new String[array.length];
			for (int i = 0; i < str.length; i++) {
				str[i] = matcher.group(i).trim();
			}
			return str;
		}
		return null;
	}
}

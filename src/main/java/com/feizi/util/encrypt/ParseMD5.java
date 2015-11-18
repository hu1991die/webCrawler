package com.feizi.util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.feizi.lucene.util.StringUtils;


/**
 * 将字符串加密转换成MD5
 * @author ljj
 * @time 2015年11月18日 下午10:10:48
 * TODO
 */
public class ParseMD5{

	/**
	 * 32位小写MD5
	 * @param str
	 * @return
	 */
	public static String parseStr2MD5L32(String str){
		String reStr = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(str.getBytes());
			
			StringBuffer sb = new StringBuffer();
			for (byte b : bytes) {
				int bt = b & 0xff;
				if(bt < 16){
					sb.append(0);
				}
				sb.append(Integer.toHexString(bt));
			}
			reStr = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return reStr;
	}
	
	/**
	 * 32位大写MD5
	 * @param str
	 * @return
	 */
	public static String parseStr2MD5U32(String str){
		String reStr = parseStr2MD5L32(str);
		if(StringUtils.isNotBlank(reStr)){
			reStr = reStr.toUpperCase();
		}
		return reStr;
	}
	
	/**
	 * 16位大写MD5
	 * @param str
	 * @return
	 */
	public static String parseStr2MD5U16(String str){
		String reStr = parseStr2MD5U32(str);
		if(StringUtils.isNotBlank(reStr)){
			reStr = reStr.substring(8, 24);
		}
		return reStr;
	}
	
	/**
	 * 16位小写MD5
	 * @param str
	 * @return
	 */
	public static String parseStr2MD5L16(String str){
		String reStr = parseStr2MD5L32(str);
		if(StringUtils.isNotBlank(reStr)){
			reStr = reStr.substring(8, 24);
		}
		return reStr;
	}
}

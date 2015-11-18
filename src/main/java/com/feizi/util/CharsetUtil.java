package com.feizi.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

/**
 * 编码工具类
 * 检查编码方式（编码探测器）
 * @author ljj
 * @time 2015年10月22日 下午10:14:09
 * TODO
 */
public final class CharsetUtil {

	private static final CodepageDetectorProxy detector;
	
	static{
		//初始化探测器
		detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		detector.add(JChardetFacade.getInstance());
	}
	
	/**
	 * 防止实例化
	 */
	private CharsetUtil(){
		
	}
	
	/**
	 * 获取文件的编码方式
	 * @param url
	 * @param defaultCharset
	 * @return
	 */
	public static String getStreamCharset(URL url, String defaultCharset){
		if(null == url){
			return defaultCharset;
		}
		
		try {
			//使用第三方jar包检查文件的编码
			Charset charset = detector.detectCodepage(url);
			if(charset != null){
				return charset.name();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defaultCharset;
	}
	
	/**
	 * 获取文件流的编码方式
	 * @param inputStream
	 * @param defaultCharset
	 * @return
	 */
	public static String getStreamCharset(InputStream inputStream, String defaultCharset){
		if(null == inputStream){
			return defaultCharset;
		}
		
		try {
			int count = 200;
			count = inputStream.available();
			
			//使用第三方jar包检测文件（流）的编码
			Charset charset = detector.detectCodepage(inputStream, count);
			if(null != charset){
				return charset.name();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return defaultCharset;
	}
}

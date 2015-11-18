package com.feizi.main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.news.BaiduNewsList;
import com.feizi.crawl.news.News;
import com.feizi.pojo.NewsBean;

/**
 * 抓取新闻信息
 * @author ljj
 * @time 2015年10月26日 下午10:48:31
 * TODO
 */
public class CrawNews {

	private static List<Info> infos;
	private static HashMap<String, Integer> result;
	private static final char[] CH_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
	static{
		infos = new ArrayList<Info>();
		infos.add(new Info("http://news.baidu.com/n?cmd=4&class=sportnews&pn=1&from=tab", "体育类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=sportnews&pn=2&from=tab", "体育类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=sportnews&pn=3&from=tab", "体育类"));  
          
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=mil&pn=1&sub=0", "军事类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=mil&pn=2&sub=0", "军事类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=mil&pn=3&sub=0", "军事类"));  
          
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=finannews&pn=1&sub=0", "财经类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=finannews&pn=2&sub=0", "财经类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=finannews&pn=3&sub=0", "财经类"));  
          
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=internet&pn=1&from=tab", "互联网"));  
          
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=housenews&pn=1&sub=0", "房产类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=housenews&pn=2&sub=0", "房产类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=housenews&pn=3&sub=0", "房产类"));  
          
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=gamenews&pn=1&sub=0", "游戏类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=gamenews&pn=2&sub=0", "游戏类"));  
        infos.add(new Info("http://news.baidu.com/n?cmd=4&class=gamenews&pn=3&sub=0", "游戏类"));
	}
	
	/**
	 * 抓取网址信息
	 * @author ljj
	 * @time 2015年10月26日 下午10:51:09
	 * TODO
	 */
	static class Info{
		String url;
		String type;
		Info(String url, String type) {
			this.url = url;
			this.type = type;
		}
	}
	
	private void crawl(Info info) throws NoSuchAlgorithmException{
		if(null == info){
			return;
		}
		
		BaiduNewsList baiduNewsList = new BaiduNewsList(info.url);
		List<String> urlList = baiduNewsList.getPageUrls();
		for (String url : urlList) {
			News news = new News(url);
			
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(url.getBytes());
			byte[] b = md5.digest();
			
			NewsBean newsBean = new NewsBean();
			newsBean.setId(byteArrayToHex(b));
			newsBean.setType(info.type);
			newsBean.setUrl(url);
			newsBean.setTitle(news.getTitle());
			newsBean.setContent(newsBean.getContent());
			
			//保存到索引文件中
			
			//knn验证
		}
	}
	
	/**
	 * 将字节数组转为十六进制字符串
	 * @param bytes
	 * @return 返回16进制字符串
	 */
	private static String byteArrayToHex(byte[] bytes){
		// 一个字节占8位，一个十六进制字符占4位；十六进制字符数组的长度为字节数组长度的两倍
        char[] chars = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            // 取字节的高4位
            chars[index++] = CH_HEX[b >>> 4 & 0xf];
            // 取字节的低4位
            chars[index++] = CH_HEX[b & 0xf];
        }
        return new String(chars);
	}
}

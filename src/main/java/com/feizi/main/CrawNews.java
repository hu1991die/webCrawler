package com.feizi.main;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.news.BaiduNewsList;
import com.feizi.crawl.news.News;
import com.feizi.lucene.index.CrawIndex;
import com.feizi.lucene.index.CrawSearch;
import com.feizi.pojo.NewsBean;
import com.feizi.util.encrypt.ParseMD5;

/**
 * 抓取新闻信息
 * @author ljj
 * @time 2015年10月26日 下午10:48:31
 * TODO
 */
public class CrawNews {

	private static List<Info> infos;
	private static CrawIndex crawIndex = new CrawIndex();
	private static CrawSearch crawSearch = new CrawSearch();
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
	
	/**
	 * 抓取一个列表页面下的新闻信息
	  * @Discription:扩展说明
	  * @param info
	  * @throws NoSuchAlgorithmException
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月18日 下午7:47:57
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年11月18日 下午7:47:57
	 */
	private void crawl(Info info){
		if(null == info){
			return;
		}
		
		try {
			BaiduNewsList baiduNewsList = new BaiduNewsList(info.url);
			List<String> urlList = baiduNewsList.getPageUrls();
			for (String url : urlList) {
				News news = new News(url);
				
				/*MessageDigest md5 = MessageDigest.getInstance("md5");
				md5.update(url.getBytes());
				byte[] b = md5.digest();*/
				
				NewsBean newsBean = new NewsBean();
//				newsBean.setId(byteArrayToHex(b));
				newsBean.setId(ParseMD5.parseStr2MD5L32(url));
				newsBean.setType(info.type);
				newsBean.setUrl(url);
				newsBean.setTitle(news.getTitle());
				newsBean.setContent(newsBean.getContent());
				
				//保存到索引文件中
				crawIndex.add(newsBean);
				
				//craw验证
				if(news.getContent() == null || "".equals(news.getContent())){
					result.put("E", 1 + result.get("E"));
					continue;
				}
				
				if(info.type.equals(crawSearch.getType(news.getContent()))){
					result.put("R", 1 + result.get("R"));
				}else{
					result.put("W", 1 + result.get("W"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public void run(){
		result = new HashMap<String, Integer>();
		result.put("R", 0);
		result.put("W", 0);
		result.put("E", 0);
		
		for (Info info : infos) {
			System.out.println(info.url + "==================start============");
			crawl(info);
			System.out.println(info.url + "==================end============");
		}
		
		try {
			crawIndex.commit();
			
			System.out.println("R = " + result.get("R"));
			System.out.println("W = " + result.get("W"));
			System.out.println("E = " + result.get("E"));
			
			System.out.println("精确度：" + (result.get("R") * 1.0 / (result.get("R") + result.get("W"))));
			System.out.println("===================finished=====================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new CrawNews().run();
	}
}

package com.feizi.crawl.news;

import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.CrawlListPageBase;
/**
 * 百度新闻滚动列表页，可以获取当前页面上得链接
 * @author ljj
 * @time 2015年10月26日 下午9:42:42
 * TODO
 */
public class BaiduNewsList extends CrawlListPageBase {

	private static HashMap<String, String> params;
	
	/**
	 * 添加相关头信息，对请求进行伪装
	 */
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://www.baidu.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
	}
	
	/**
	 * 定义构造器
	 * @param urlStr
	 */
	public BaiduNewsList(String urlStr){
		super(urlStr, "utf-8", "get", params);
	}

	@Override
	public String getUrlRegexString() {
		//新闻列表页中文章链接地址的正则表达式
		return "• <a href=\"(.*?)\"";
	}

	@Override
	public int getUrlRegexStringNum() {
		//链接地址在正则表达式中的位置
		return 1;
	}

	/**
	 * 测试用例
	 * @param args
	 */
	public static void main(String[] args) {
		BaiduNewsList baiduNews = new BaiduNewsList("http://news.baidu.com/n?cmd=4&class=sportnews&pn=1&from=tab");
		
		List<String> baiduNewsList = baiduNews.getPageUrls();
		for (String str : baiduNewsList) {
			System.out.println(str);
		}
	}
}

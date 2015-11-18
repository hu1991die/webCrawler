package com.feizi.crawl.news;

import java.util.HashMap;

import com.feizi.crawl.CrawlBase;
import com.feizi.util.RegexUtil;

/**
 * 新闻
 * @author ljj
 * @time 2015年10月26日 下午10:22:23
 * TODO
 */
public class News extends CrawlBase {

	private String url;
	private String content;
	private String title;
	private String type;
	
	private static String contentRegex = "<p.*?>(.*?)</p>";
	private static String titleRegex = "<title>(.*?)</title>";
	private static int maxLength = 300;
	
	private static HashMap<String, String> params;
	
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://www.baidu.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
	}
	
	public News(String url){
		this.url = url;
		readPageByGet(url, "utf-8", params);
		setContent();
		setTitle();
	}
	
	/**
	 * 默认p标签内的内容为正文，如果正文超过设置的最大长度，则截取前半部分
	 */
	private void setContent(){
		String content = RegexUtil.getString(getPageSourceCode(), contentRegex, 1);
		content = content.replaceAll("\n", "")
						 .replaceAll("<script.*?/script>", "")
						 .replaceAll("<style.*?/style>", "")
						 .replaceAll("<.*?>", "");
		
		this.content = content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
	
	/**
	 * 默认title标签内的内容为标题
	 */
	private void setTitle(){
		this.title = RegexUtil.getString(getPageSourceCode(), titleRegex, 1);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static void setMaxLength(int maxLength){
		News.maxLength = maxLength;
	}
	
	/**
	 * 测试用例
	 * @param args
	 */
	public static void main(String[] args) {
		News news = new News("http://we.sportscn.com/viewnews-1634777.html");
		System.out.println("===content:" + news.getContent());
		System.out.println("===title:" + news.getTitle());
		System.out.println("===type:" + news.getType());
	}
}

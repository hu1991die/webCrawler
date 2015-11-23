package com.feizi.crawl.novel.zhongheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.CrawlListPageBase;
/**
 * 更新小说列表页
 * @author ljj
 * @time 2015年11月23日 下午10:17:46
 * TODO
 */
public class UpdateNovelList extends CrawlListPageBase {
	
	private static HashMap<String, String> params;
	
	/**
	 * 添加相关头信息，对请求进行伪装
	 */
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11) AppleWebKit/601.1.56 (KHTML, like Gecko) Version/9.0 Safari/601.1.56");
	}

	/**
	 * 构造方法
	 * @param urlStr
	 */
	public UpdateNovelList(String urlStr) {
		super(urlStr, "utf-8", params);
	}

	/**
	 * 返回页面上需求的网址链接的正则表达式
	 */
	@Override
	public String getUrlRegexString() {
		return "<a class=\"fs14\" href=\"(.*?)\"";
	}

	/**
	 * 正则表达式中要去的字段位置
	 */
	@Override
	public int getUrlRegexStringNum() {
		return 1;
	}

	/**
	 * 是否排除非纵横网的书籍
	 * @param exceptOther
	 * @return
	 */
	public List<String> getPageUrls(boolean exceptOther){
		List<String> urlList = getPageUrls();
		if(exceptOther){
			List<String> exceptUrlList = new ArrayList<String>();
			for (String url : urlList) {
				if(url.indexOf("zongheng") > 0){
					exceptUrlList.add(url);
				}
			}
			return exceptUrlList;
		}
		return urlList;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateNovelList novelList = new UpdateNovelList("http://book.zongheng.com/store/c0/c0/b9/u0/p1/v0/s9/t0/ALL.html");
		List<String> urlList = novelList.getPageUrls(true);
		if(null != urlList && urlList.size() > 0){
			for (String url : urlList) {
				System.out.println("小说的URL地址：" + url);
			}
		}
	}
}

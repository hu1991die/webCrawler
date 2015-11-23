package com.feizi.crawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feizi.util.RegexUtil;

/**
 * 获取页面链接地址信息基类
 * @Author: feizi
 * @Date: 2015年10月11日 下午2:40:53 
 * @ModifyUser: feizi
 * @ModifyDate: 2015年10月11日 下午2:40:53 
 * @Version:V6.0
 */
public abstract class CrawlListPageBase extends CrawlBase{
	
	private String pageUrl;
	
	/**
	 * 构造函数带参
	 * @Title:
	 * @Description:
	 * @param urlStr
	 * @param charsetName
	 */
	public CrawlListPageBase(String urlStr, String charsetName){
		readPageByGet(urlStr, charsetName);
		this.pageUrl = urlStr;
	}
	
	public CrawlListPageBase(String urlStr, String charsetName, HashMap<String, String> params){
		readPageByGet(urlStr, charsetName, params);
		this.pageUrl = urlStr;
	}
	
	public CrawlListPageBase(String urlStr, String charsetName, String method, HashMap<String, String> params){
		readPage(urlStr, charsetName, method, params);
		this.pageUrl = urlStr;
	}
	
	/**
	 * 返回页面上需求的链接地址
	  * @Discription:扩展说明
	  * @return
	  * @return List<String>
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午2:50:08
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午2:50:08
	 */
	public List<String> getPageUrls(){
		List<String> pageUrls = new ArrayList<String>();
		pageUrls = RegexUtil.getArrayList(getPageSourceCode(), getUrlRegexString(), pageUrl, getUrlRegexStringNum());
		return pageUrls;
	}
	
	/**
	 * 返回页面上需求的网址链接的正则表达式
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午2:50:50
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午2:50:50
	 */
	public abstract String getUrlRegexString();
	
	/**
	 * 正则表达式中要去的字段位置
	  * @Discription:扩展说明
	  * @return
	  * @return int
	  * @Author: feizi
	  * @Date: 2015年10月11日 下午2:51:44
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年10月11日 下午2:51:44
	 */
	public abstract int getUrlRegexStringNum();
}

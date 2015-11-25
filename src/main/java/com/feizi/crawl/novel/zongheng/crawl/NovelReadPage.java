/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.crawl;

import java.util.HashMap;

import com.feizi.crawl.CrawlBase;
import com.feizi.crawl.novel.zongheng.model.NovelReadModel;
import com.feizi.lucene.util.ParseUtils;
import com.feizi.util.RegexUtil;

/**  
 * 纵横小说阅读页信息采集
 * @Author: feizi
 * @Date: 2015年11月24日 上午11:40:01 
 * @Version:V6.0
 */
public class NovelReadPage extends CrawlBase {

	//小说内容
	private static final String NOVEL_CONTENT = "<div id=\"chapterContent\" class=\"content\" itemprop=\"acticleBody\">(.*?)</div>";
	//小说标题
	private static final String NOVEL_TITLE = "chapterName=\"(.*?)\"";
	//小说字数
	private static final String NOVEL_WORD_COUNT = "<span itemprop=\"wordCount\">(\\d*)</span>";
	
	private String pageUrl;
	private static HashMap<String, String> params = null;
	
	/**
	 * 添加相关请求头信息，对请求进行伪装
	 */
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
	}
	
	public NovelReadPage(String urlStr){
		readPageByGet(urlStr, "utf-8", params);
		this.pageUrl = urlStr;
	}
	
	/**
	 * 章节标题
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午1:14:08
	 */
	private String getNovelTitle(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_TITLE, 1);
	}
	
	/**
	 * 统计字数
	  * @Discription:扩展说明
	  * @return
	  * @return int
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午1:16:43
	 */
	private int getNovelWordCount(){
		String wordCount = RegexUtil.getFirstString(getPageSourceCode(), NOVEL_WORD_COUNT, 1);
		return ParseUtils.parseStringToInt(wordCount, 0);
	}
	
	/**
	 * 小说正文
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午1:17:58
	 */
	private String getNovelContent(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_CONTENT, 1);
	}
	
	/**
	 * 分析阅读页，抓取阅读页数据
	  * @Discription:扩展说明
	  * @return
	  * @return NovelReadModel
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午11:33:53
	 */
	public NovelReadModel getNovelRead(){
		NovelReadModel novelRead = new NovelReadModel();
		novelRead.setTitle(getNovelTitle());
		novelRead.setWordCount(getNovelWordCount());
		novelRead.setContent(getNovelContent());
		return novelRead;
	}
	
	/**
	 * 测试用例
	  * @Discription:扩展说明
	  * @param args
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午1:21:01
	 */
	public static void main(String[] args) {
		NovelReadPage readNovelPage = new NovelReadPage("http://book.zongheng.com/chapter/362857/6001264.html");
		
		System.out.println("小说的URL网址：" + readNovelPage.pageUrl);
		System.out.println("小说的标题：" + readNovelPage.getNovelTitle());
		System.out.println("小说的字数：" + readNovelPage.getNovelWordCount());
		System.out.println("小说的正文：" + readNovelPage.getNovelContent());
	}
}

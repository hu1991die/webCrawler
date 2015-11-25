/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.crawl;

import java.util.HashMap;

import com.feizi.crawl.CrawlBase;
import com.feizi.crawl.novel.zongheng.model.NovelIntroModel;
import com.feizi.lucene.util.ParseUtils;
import com.feizi.util.RegexUtil;
import com.feizi.util.encrypt.ParseMD5;

/**  
 * 小说简介页的信息采集
 * @Author: feizi
 * @Date: 2015年11月25日 上午9:49:55 
 * @Version:V6.0
 */
public class NovelIntroPage extends CrawlBase {

	private static final String NOVEL_NAME = "<meta name=\"og:novel:book_name\" content=\"(.*?)\"/>";
	private static final String NOVEL_AUTHOR = "<meta name=\"og:novel:author\" content=\"(.*?)\"/>";
	private static final String NOVEL_DESC = "<meta property=\"og:description\" content=\"(.*?)\"/>";
	private static final String NOVEL_TYPE = "<meta name=\"og:novel:category\" content=\"(.*?)\"/>";
	private static final String NOVEL_LAST_CHAPTER = "<meta name=\"og:novel:latest_chapter_name\" content=\"(.*?)\"/>";
	private static final String NOVEL_CHAPTER_URL = "<meta name=\"og:novel:read_url\" content=\"(.*?)\"/>";
	private static final String NOVEL_WORD_COUNT = "<span title=\"(\\d*?)([\u4e00-\u9fa5]?)\">(\\d*?)</span>";
	private static final String NOVEL_KEYWORDS = "<div class=\"keyword\">(.*?)</div>";
	private static final String NOVEL_KEYWORD = "<a.*?>(.*?)</a>";
	
	private String pageUrl;
	private static HashMap<String, String> params;
	
	/**
	 * 添加相关请求头信息，对请求进行伪装
	 */
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
	}
	
	/**
	 * 构造器
	 * @Title:
	 * @Description:
	 * @param urlStr
	 */
	public NovelIntroPage(String urlStr){
		readPageByGet(urlStr, "utf-8", params);
		this.pageUrl = urlStr;
	}
	
	/**
	 * 获取小说书名
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:18:28
	 */
	private String getName(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_NAME, 1);
	}
	
	/**
	 * 获取小说作者名
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:19:22
	 */
	private String getAuthor(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_AUTHOR, 1);
	}
	
	/**
	 * 获取小说简介信息
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:20:09
	 */
	private String getDesc(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_DESC, 1);
	}
	
	/**
	 * 获取小说分类（书籍分类）
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:21:05
	 */
	private String getType(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_TYPE, 1);
	}
	
	/**
	 * 获取小说最新章节
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:22:39
	 */
	private String getLatestChapter(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_LAST_CHAPTER, 1);
	}
	
	/**
	 * 获取章节列表页URL
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:23:48
	 */
	private String getChapterListUrl(){
		return RegexUtil.getFirstString(getPageSourceCode(), NOVEL_CHAPTER_URL, 1);
	}
	
	/**
	 * 获取小说字数
	  * @Discription:扩展说明
	  * @return
	  * @return int
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:25:42
	 */
	private int getWordCount(){
		String wordCount = RegexUtil.getFirstString(getPageSourceCode(), NOVEL_WORD_COUNT, 3);
		return ParseUtils.parseStringToInt(wordCount, 0);
	}
	
	/**
	 * 获取标签
	  * @Discription:扩展说明
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:27:56
	 */
	private String keyWords(){
		String keyHtml = RegexUtil.getFirstString(getPageSourceCode(), NOVEL_KEYWORDS, 1);
		return RegexUtil.getString(keyHtml, NOVEL_KEYWORD, " ",1);
	}
	
	/**
	 * 分析简介页，获取简介页数据信息
	  * @Discription:扩展说明
	  * @return
	  * @return NovelIntroModel
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午11:31:58
	 */
	public NovelIntroModel getNovelIntro(){
		NovelIntroModel novelIntro = new NovelIntroModel();
		novelIntro.setMd5Id(ParseMD5.parseStr2MD5L32(this.pageUrl));
		novelIntro.setName(getName());  
		novelIntro.setAuthor(getAuthor());  
		novelIntro.setDescription(getDesc());  
		novelIntro.setType(getType());  
		novelIntro.setLastChapter(getLatestChapter());  
		novelIntro.setChapterListUrl(getChapterListUrl());;  
		novelIntro.setWordCount(getWordCount());  
		novelIntro.setKeyWords(keyWords()); 
		return novelIntro;
	}
	
	/**
	 * 测试用例
	  * @Discription:扩展说明
	  * @param args
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午10:28:20
	 */
	public static void main(String[] args) {
		NovelIntroPage novelIntro = new NovelIntroPage("http://book.zongheng.com/book/362857.html");
		System.out.println(novelIntro.pageUrl);  
        System.out.println(novelIntro.getName());  
        System.out.println(novelIntro.getAuthor());  
        System.out.println(novelIntro.getDesc());  
        System.out.println(novelIntro.getType());  
        System.out.println(novelIntro.getLatestChapter());  
        System.out.println(novelIntro.getChapterListUrl());  
        System.out.println(novelIntro.getWordCount());  
        System.out.println(novelIntro.keyWords());
	}
}

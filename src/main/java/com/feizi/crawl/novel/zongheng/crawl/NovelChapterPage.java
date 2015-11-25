/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.crawl;

import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.CrawlBase;
import com.feizi.util.RegexUtil;

/**  
 * 小说章节列表页数据采集
 * @Author: feizi
 * @Date: 2015年11月25日 上午10:58:39 
 * @Version:V6.0
 */
public class NovelChapterPage extends CrawlBase {

	private static final String NOVEL_CHAPTER = "<td class=\"chapterBean\" chapterId=\"\\d*\" chapterName=\"(.*?)\" "
			+ "chapterLevel=\"\\d*\" wordNum=\"(\\d*?)\" updateTime=\"(.*?)\"><a href=\"(.*?)\" title=\".*?\">";
	
	private static final int[] CHAPTER_ARRAY = {1, 2, 3, 4};
	private static HashMap<String, String> params;
	
	/**
	 *添加相关请求头信息
	 */
	static{
		params = new HashMap<String, String>();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
	}
	
	public NovelChapterPage(String urlStr){
		readPageByGet(urlStr, "utf-8", params);
	}
	
	/**
	 * 采集章节列表信息
	  * @Discription:扩展说明
	  * @return
	  * @return List<String[]>
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午11:11:32
	 */
	public List<String[]> getChaptersInfo(){
		return RegexUtil.getListArray(getPageSourceCode(), NOVEL_CHAPTER, CHAPTER_ARRAY);
	}
	
	public static void main(String[] args) {
		NovelChapterPage novelChapter = new NovelChapterPage("http://book.zongheng.com/showchapter/362857.html");
		if(null != novelChapter){
			for (String[] chapterInfos : novelChapter.getChaptersInfo()) {
				for (String chapter : chapterInfos) {
					System.out.println(chapter);
				}
				System.out.println("================================");
			}
		}
	}
}

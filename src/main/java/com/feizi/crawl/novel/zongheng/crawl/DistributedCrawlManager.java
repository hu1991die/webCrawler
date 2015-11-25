/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.crawl;

import java.util.List;

import com.feizi.crawl.novel.zongheng.dao.ZongHengDao;
import com.feizi.crawl.novel.zongheng.model.CrawlNovelListModel;
import com.feizi.crawl.novel.zongheng.thread.NovelIntroPageThread;
import com.feizi.crawl.novel.zongheng.thread.NovelReadPageThread;
import com.feizi.crawl.novel.zongheng.thread.UpdateNovelListThread;

/** 
 * 分布式采集管理类
 * 这个类的主要作用用于管理采集线程（先前编写的四个采集线程类） 
 * @Author: feizi
 * @Date: 2015年11月25日 下午2:55:20 
 * @Version:V6.0
 */
public class DistributedCrawlManager {

	private static boolean crawlListFlag = false;
	private static boolean crawlIntroFlag = false;
	private static boolean crawlReadFlag = false;
	//简介页信息采集线程数目
	private static int crawlIntroThreadNum = 2;
	//阅读页信息采集线程数目
	private static int crawlReadThreadNum = 10;
	
	/**
	 * 开启更新列表页信息采集线程
	  * @Discription:扩展说明
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午3:14:02
	 */
	public void startCrawListThread(){
		if(crawlListFlag){
			return;
		}
		
		crawlListFlag = true;
		ZongHengDao dao = new ZongHengDao();
		List<CrawlNovelListModel> crawlNovelList = dao.getCrawlListInfos();
		if(null == crawlNovelList){
			return;
		}
		for (CrawlNovelListModel novelModel : crawlNovelList) {
			if(null == novelModel.getUrl() || "".equals(novelModel.getUrl())){
				continue;
			}
			//启动采集线程
			UpdateNovelListThread thread = new UpdateNovelListThread(novelModel.getInfo(), 
					novelModel.getUrl(), novelModel.getFrequency());
			thread.start();
		}
	}
	
	/**
	 * 开启小说简介页和章节列表页信息采集线程
	  * @Discription:扩展说明
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午3:30:28
	 */
	public void startCrawlIntroThread(){
		if (crawlIntroFlag) {
			return;
		}
		crawlIntroFlag = true;
		for (int i = 0; i < crawlIntroThreadNum; i++) {
			NovelIntroPageThread thread = new NovelIntroPageThread("novelIntroPageThread" + i);
			thread.start();
		}
	}
	
	/**
	 * 开启小说阅读页信息采集线程
	  * @Discription:扩展说明
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午3:33:49
	 */
	public void startCrawReadThread(){
		if(crawlReadFlag){
			return;
		}
		crawlReadFlag = true;
		for (int i = 0; i < crawlReadThreadNum; i++) {
			NovelReadPageThread thread = new NovelReadPageThread("novelReadPageThread" + i);
			thread.start();
		}
	}
	
	/**
	 * 测试用例
	  * @Discription:扩展说明
	  * @param args
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午3:35:27
	 */
	public static void main(String[] args) {
		DistributedCrawlManager manager = new DistributedCrawlManager();
		manager.startCrawListThread();
		manager.startCrawlIntroThread();
		manager.startCrawReadThread();
	}
}

/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.feizi.crawl.novel.zongheng.crawl.NovelChapterPage;
import com.feizi.crawl.novel.zongheng.crawl.NovelIntroPage;
import com.feizi.crawl.novel.zongheng.dao.ZongHengDao;
import com.feizi.crawl.novel.zongheng.model.NovelIntroModel;

/**  
 * 小说简介页&章节列表页线程类
 * 一个简介页对应一个章节列表页，实现小说简介页的信息采集
 * 以及小说章节列表页信息的采集
 * @Author: feizi
 * @Date: 2015年11月25日 下午1:07:07 
 * @Version:V6.0
 */
public class NovelIntroPageThread extends Thread{

	private boolean flag = false;
	
	public NovelIntroPageThread(String name){
		super(name);
	}
	
	/**
	 * 重写run方法
	  * @Discription:扩展说明
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午1:09:35
	  * @see java.lang.Thread#run()
	  */
	@Override
	public void run() {
		flag = true;
		try {
			ZongHengDao dao = new ZongHengDao();
			while (flag) {
				//随机获取一个待采集的简介页URL
				String urlStr = dao.getRandIntroPageUrl(1);
				if(null != urlStr){
					NovelIntroPage novelIntroPage = new NovelIntroPage(urlStr);
					NovelIntroModel novelIntroModel = novelIntroPage.getNovelIntro();
					
					//采集小说章节列表页信息
					NovelChapterPage novelChapterPage = new NovelChapterPage(novelIntroModel.getChapterListUrl());
					List<String[]> chapterInfos = novelChapterPage.getChaptersInfo();
					novelIntroModel.setChapterCount(chapterInfos == null ? 0 : chapterInfos.size());
					
					//更新小说简介信息
					dao.updateInfo(novelIntroModel);
					
					//插入带采集的章节列表
					dao.saveChapters(chapterInfos);
					
					//如果本次有待采集的资源，休眠一个时间，没有则休眠另一个时间
					TimeUnit.MILLISECONDS.sleep(500);
				}else{
					TimeUnit.MILLISECONDS.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试用例
	  * @Discription:扩展说明
	  * @param args
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午1:20:10
	 */
	public static void main(String[] args) {
		NovelIntroPageThread thread = new NovelIntroPageThread("novelIntroThread");
		thread.start();
	}
}

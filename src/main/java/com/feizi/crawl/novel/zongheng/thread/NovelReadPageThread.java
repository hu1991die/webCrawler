/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.thread;

import java.util.concurrent.TimeUnit;

import com.feizi.crawl.novel.zongheng.crawl.NovelReadPage;
import com.feizi.crawl.novel.zongheng.dao.ZongHengDao;
import com.feizi.crawl.novel.zongheng.model.NovelChapterModel;
import com.feizi.crawl.novel.zongheng.model.NovelReadModel;
import com.feizi.util.encrypt.ParseMD5;

/**  
 * 小说阅读页线程采集类
 * 这个线程的主要功能就是将小说阅读页的信息采集并持久化到数据库中
 * @Author: feizi
 * @Date: 2015年11月25日 下午1:21:08 
 * @Version:V6.0
 */
public class NovelReadPageThread extends Thread {

	private boolean flag = false;
	
	public NovelReadPageThread(String name){
		super(name);
	}
	
	/**
	 *  重写run()方法
	  * @Discription:扩展说明
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午1:22:14
	  * @see java.lang.Thread#run()
	  */
	@Override
	public void run() {
		flag = true;
		try {
			ZongHengDao dao = new ZongHengDao();
			while(flag){
				//随机获取待采集的阅读页
				NovelChapterModel chapter = dao.getRandReadPageUrl(1);
				if(null != chapter){
					NovelReadPage readPage = new NovelReadPage(chapter.getUrl());
					NovelReadModel novelReadModel = readPage.getNovelRead();
					if(null == novelReadModel){
						continue;
					}
					
					novelReadModel.setChapterId(chapter.getChapterId());
					novelReadModel.setTime(chapter.getTime());
					novelReadModel.setUrl(chapter.getUrl());
					
					//保存阅读页信息
					dao.saveNovelRead(novelReadModel);
					
					//将阅读页信息表的采集状态修改为不需要采集
					dao.updateChapterState(ParseMD5.parseStr2MD5L32(novelReadModel.getUrl()), 0);
					
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
	  * @Date: 2015年11月25日 下午2:45:56
	 */
	public static void main(String[] args) {
		NovelReadPageThread thread = new NovelReadPageThread("novelReadPageThread");
		thread.start();
	}
}

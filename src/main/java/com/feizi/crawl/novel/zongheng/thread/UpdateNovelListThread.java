/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.feizi.crawl.novel.zongheng.crawl.NovelUpdateListPage;
import com.feizi.crawl.novel.zongheng.dao.ZongHengDao;

/**  
 * 更新列表页线程
 * 这个线程的主要功能就是监控更新列表页的数据，提取页面上的简介页URL，如果有更新，
 * 就将对应的信息持久化到数据库中
 * @Author: feizi
 * @Date: 2015年11月25日 上午11:37:16 
 * @Version:V6.0
 */
public class UpdateNovelListThread extends Thread {

	private boolean flag = false;
	private String urlStr = null;//抓取的更新列表页URL
	private int frequency;//采集频率
	
	public UpdateNovelListThread(String name, String urlStr, int frequency){
		super(name);
		this.urlStr = urlStr;
		this.frequency = frequency;
	}

	/**
	 * 重写run方法
	  * @Discription:扩展说明
	  * @Author: feizi
	  * @Date: 2015年11月25日 上午11:42:08
	  * @see java.lang.Thread#run()
	  */
	@Override
	public void run() {
		flag = true;
		ZongHengDao dao = new ZongHengDao();
		while(flag){
			try {
				NovelUpdateListPage updateNovelList = new NovelUpdateListPage(urlStr);
				List<String> urlList = updateNovelList.getPageUrls(flag);
				dao.saveInfoUrls(urlList);
				TimeUnit.SECONDS.sleep(frequency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 测试用例
	  * @Discription:扩展说明
	  * @param args
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月25日 下午2:50:23
	 */
	public static void main(String[] args) {
		UpdateNovelListThread thread = new UpdateNovelListThread("novelListThread", "http://book.zongheng.com/store/c0/c0/b9/u0/p1/v0/s9/t0/ALL.html", 60);
		thread.start();
	}
}

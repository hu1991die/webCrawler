/**
 * @Description:扩展说明
 * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
 * @Version: V6.0
 */
package com.feizi.crawl.novel.zongheng.model;

import java.io.Serializable;

/**
 * 小说章节列表model类
 * 
 * @Author: feizi
 * @Date: 2015年11月24日 下午3:43:17
 * @Version:V6.0
 */
public class NovelChapterModel implements Serializable {

	private static final long serialVersionUID = 2287331870915104590L;

	private String url;
	private int chapterId;
	private long time;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

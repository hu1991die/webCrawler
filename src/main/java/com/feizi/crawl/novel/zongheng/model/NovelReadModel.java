/**
 * @Description:扩展说明
 * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
 * @Version: V6.0
 */
package com.feizi.crawl.novel.zongheng.model;

import java.io.Serializable;

/**
 * 小说阅读页model类
 * 
 * @Author: feizi
 * @Date: 2015年11月24日 下午3:44:38
 * @Version:V6.0
 */
public class NovelReadModel extends NovelChapterModel implements Serializable {

	private static final long serialVersionUID = -6540670263015372447L;

	//小说标题
	private String title;
	//字数统计
	private int wordCount;
	//小说正文
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

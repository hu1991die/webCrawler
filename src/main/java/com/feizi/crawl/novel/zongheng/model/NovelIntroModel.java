/**
 * @Description:扩展说明
 * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
 * @Version: V6.0
 */
package com.feizi.crawl.novel.zongheng.model;

import java.io.Serializable;

/**
 * 小说简介model类
 * 
 * @Author: feizi
 * @Date: 2015年11月24日 下午3:39:53
 * @Version:V6.0
 */
public class NovelIntroModel implements Serializable {

	private static final long serialVersionUID = -9083619531234102387L;

	private String md5Id;
	private String name;
	private String author;
	private String description;
	private String type;
	private String lastChapter;
	private String chapterListUrl;
	private int wordCount;
	private String keyWords;
	private int chapterCount;

	public String getMd5Id() {
		return md5Id;
	}

	public void setMd5Id(String md5Id) {
		this.md5Id = md5Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLastChapter() {
		return lastChapter;
	}

	public void setLastChapter(String lastChapter) {
		this.lastChapter = lastChapter;
	}

	public String getChapterListUrl() {
		return chapterListUrl;
	}

	public void setChapterListUrl(String chapterListUrl) {
		this.chapterListUrl = chapterListUrl;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public int getChapterCount() {
		return chapterCount;
	}

	public void setChapterCount(int chapterCount) {
		this.chapterCount = chapterCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

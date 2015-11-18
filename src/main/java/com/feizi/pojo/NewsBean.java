package com.feizi.pojo;

import java.io.Serializable;

/**
 * News新闻的pojo类
 * 
 * @author ljj
 * @time 2015年10月26日 下午11:03:37 TODO
 */
public class NewsBean implements Serializable {

	private static final long serialVersionUID = -2771785626775707816L;
	
	private String id;
	private String type;
	private String url;
	private String title;
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

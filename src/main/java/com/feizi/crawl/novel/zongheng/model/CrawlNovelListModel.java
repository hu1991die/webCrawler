/**
 * @Description:扩展说明
 * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
 * @Version: V6.0
 */
package com.feizi.crawl.novel.zongheng.model;

import java.io.Serializable;

/**
 * 小说采集入口列表model类
 * 
 * @Author: feizi
 * @Date: 2015年11月24日 下午3:35:51
 * @Version:V6.0
 */
public class CrawlNovelListModel implements Serializable {

	private static final long serialVersionUID = -8357133683874313649L;

	private String url;
	private String info;
	private int frequency;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}

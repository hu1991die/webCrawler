/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.lucene.index;

import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;

import com.feizi.lucene.analyzer.AnalyzerFactory;
import com.feizi.lucene.analyzer.AnalyzerType;
import com.feizi.lucene.index.manager.IndexManager;
import com.feizi.lucene.index.model.ConfigBean;
import com.feizi.lucene.index.model.IndexConfig;
import com.feizi.lucene.index.operation.NRTIndex;
import com.feizi.pojo.NewsBean;

/**  
 * 定义数据索引类
 * @Author: feizi
 * @Date: 2015年11月18日 下午5:12:10 
 * @ModifyUser: feizi
 * @ModifyDate: 2015年11月18日 下午5:12:10 
 * @Version:V6.0
 */
public class CrawIndex extends NRTIndex {

	//定义索引名称
	public final static String indexName = "crawIndex";
	
	/**
	 * 当系统加载的时候，请将下面的static配置放到系统初始化中
	 */
	static{
		//索引初始化
		HashSet<ConfigBean> condigBeanHS = new HashSet<ConfigBean>();
		Analyzer analyzer = new AnalyzerFactory().createAnalyzer(AnalyzerType.IK_ANALYZER, null);
		
		ConfigBean configBean = new ConfigBean();
		configBean.setIndexPath("E:/index");
		configBean.setIndexName(indexName);
		configBean.setAnalyzer(analyzer);
		condigBeanHS.add(configBean);
		IndexConfig.setConfigBean(condigBeanHS);
		IndexManager.getIndexManager(indexName);
	}
	
	/**
	 * @Title:
	 * @Description:
	 * @param indexName
	 */
	public CrawIndex() {
		super(indexName);
	}

	/**
	 * 将NewsBean添加至索引
	  * @Discription:扩展说明
	  * @param news
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月18日 下午6:12:21
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年11月18日 下午6:12:21
	 */
	public void add(NewsBean news){
		Document doc = parse(news);
		Term term = new Term("id", news.getId());
		updateDocument(term, doc);
	}
	
	/**
	 * 将NewsBean转换为document
	  * @Discription:扩展说明
	  * @param news
	  * @return
	  * @return Document
	  * @Author: feizi
	  * @Date: 2015年11月18日 下午6:59:16
	  * @ModifyUser：feizi
	  * @ModifyDate: 2015年11月18日 下午6:59:16
	 */
	private Document parse(NewsBean news){
		if(news == null){
			return null;
		}
		Document doc = new Document();
		doc.add(new StringField("id", news.getId(), Store.YES));
		doc.add(new StringField("url", news.getUrl(), Store.YES));
		doc.add(new StringField("type", news.getType(), Store.YES));
		
		TextField title = new TextField("title", news.getTitle(), Store.YES);
		title.setBoost(2.0f);
		doc.add(title);
		
		TextField content = new TextField("content", news.getContent(), Store.YES);
		content.setBoost(1.0f);
		doc.add(content);
		return doc;
	}
}

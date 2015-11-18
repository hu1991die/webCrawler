/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.lucene.index;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

import com.feizi.lucene.index.model.SearchResultBean;
import com.feizi.lucene.index.operation.NRTSearch;
import com.feizi.lucene.query.PackQuery;
import com.feizi.lucene.util.LuceneUtils;

/**  
 * 检索索引CrawSearch类
 * @Author: feizi
 * @Date: 2015年11月18日 下午7:01:37 
 * @ModifyUser: feizi
 * @ModifyDate: 2015年11月18日 下午7:01:37 
 * @Version:V6.0
 */
public class CrawSearch extends NRTSearch {

	//实例化索引检索PackQuery类
	private static PackQuery packQuery = new PackQuery(CrawIndex.indexName);
	
	/**
	 * @Title:
	 * @Description:
	 * @param indexName
	 */
	public CrawSearch() {
		super(CrawIndex.indexName);
	}
	
	public String getType(String content){
		content = LuceneUtils.escapeLuceneKey(content);
		
		try {
			Query query = packQuery.getOneFieldQuery(content, "content");
			SearchResultBean result = search(query, 0, 3);
			if(null == result || result.getCount() == 0){
				return "未知结果";
			}
			
			if(result.getCount() < 3){
				return result.getDatas().get(0).get("type");
			}
			
			if(result.getDatas().get(1).get("type")
					.equals(result.getDatas().get(2).get("type"))){
				return result.getDatas().get(1).get("type");
			}
			
			return result.getDatas().get(0).get("type");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "未知结果";
	}

	public static void main(String[] args){
		System.out.println(new CrawSearch().getType("科比"));
	}
}

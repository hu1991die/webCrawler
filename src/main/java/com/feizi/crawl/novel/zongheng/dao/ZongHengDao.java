/**
  * @Description:扩展说明
  * @Copyright: 2015 dreamtech.com.cn Inc. All right reserved
  * @Version: V6.0
  */
package com.feizi.crawl.novel.zongheng.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feizi.crawl.novel.zongheng.model.CrawlNovelListModel;
import com.feizi.crawl.novel.zongheng.model.NovelChapterModel;
import com.feizi.crawl.novel.zongheng.model.NovelIntroModel;
import com.feizi.crawl.novel.zongheng.model.NovelReadModel;
import com.feizi.lucene.dao.manager.DBServer;
import com.feizi.util.encrypt.ParseMD5;

/**  
 * 纵横小说Dao数据库操作类
 * @Author: feizi
 * @Date: 2015年11月24日 下午3:57:28 
 * @Version:V6.0
 */
public class ZongHengDao {

	private static final String DB_POOL_NAME = "proxool.xml";
	
	/**
	 * 保存更新列表接口采集到的URL列表
	  * @Discription:扩展说明
	  * @param urlList
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午3:59:44
	 */
	public void saveInfoUrls(List<String> urlList){
		if(null == urlList || urlList.size() == 0){
			return;
		}
		
		for (String url : urlList) {
			String md5Id = ParseMD5.parseStr2MD5L32(url);
			if(haveInfoUrl(md5Id)){
				updateInfoState(md5Id, 1);
			}else{
				insertInfoUrl(md5Id, url);
			}
		}
	}
	
	/**
	 * 随机获取一个简介URL
	  * @Discription:扩展说明
	  * @param state
	  * @return
	  * @return String
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:09:10
	 */
	public String getRandIntroPageUrl(int state){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "SELECT * FROM novelinfo WHERE state = '" + state + "' ORDER BY RAND() LIMIT 1";
			ResultSet rs = dbServer.select(sql);
			while(rs.next()){
				return rs.getString("url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
		return null;
	}
	
	/**
	 * 随机获取一个章节信息
	  * @Discription:扩展说明
	  * @param state
	  * @return
	  * @return NovelChapterModel
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:10:37
	 */
	public NovelChapterModel getRandReadPageUrl(int state){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "SELECT * FROM novelchapter WHERE state = '" + state + "' ORDER BY RAND() LIMIT 1";
			ResultSet rs = dbServer.select(sql);
			while(rs.next()){
				NovelChapterModel novelChapter = new NovelChapterModel();
				novelChapter.setChapterId(rs.getInt("chapterid"));
				novelChapter.setTime(rs.getLong("chaptertime"));
				novelChapter.setUrl(rs.getString("url"));
				return novelChapter;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
		return null;
	}
	
	/**
	 * 保存小说阅读页信息
	  * @Discription:扩展说明
	  * @param novel
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:31:23
	 */
	public void saveNovelRead(NovelReadModel novel){
		if(null == novel){
			return;
		}
		
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			String md5Id = ParseMD5.parseStr2MD5L32(novel.getUrl());
			
			//如果已经存在，则直接返回
			if(haveReadUrl(md5Id)){
				return;
			}
			long now = System.currentTimeMillis();
			params.put(i++, md5Id);
			params.put(i++, novel.getUrl());
			params.put(i++, novel.getTitle());
			params.put(i++, novel.getWordCount());
			params.put(i++, novel.getChapterId());
			params.put(i++, novel.getContent());
			params.put(i++, novel.getTime());
			params.put(i++, now);
			params.put(i++, now);
			
			dbServer.insert("novelchapterdetail", "ID,URL,TITLE,WORDCOUNT,CHAPTERID,CONTENT,CHAPTERTIME,CREATETIME,UPDATETIME", params);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 获取监控的更新列表页
	  * @Discription:扩展说明
	  * @return
	  * @return List<CrawlNovelListModel>
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:36:13
	 */
	public List<CrawlNovelListModel> getCrawlListInfos(){
		List<CrawlNovelListModel> novelList = new ArrayList<CrawlNovelListModel>();
		
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "SELECT * FROM crawllist WHERE state = '1'";
			ResultSet rs = dbServer.select(sql);
			
			CrawlNovelListModel novel = null;
			while(rs.next()){
				novel = new CrawlNovelListModel();
				novelList.add(novel);
				novel.setUrl(rs.getString("url"));
				novel.setInfo(rs.getString("info"));
				novel.setFrequency(rs.getInt("frequency"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
		return novelList;
	}
	
	/**
	 * 更新简介页记录
	  * @Discription:扩展说明
	  * @param introInfo
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:47:42
	 */
	public void updateInfo(NovelIntroModel introInfo){
		if(null == introInfo){
			return;
		}
		
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, introInfo.getName());
			params.put(i++, introInfo.getAuthor());
			params.put(i++, introInfo.getDescription());
			params.put(i++, introInfo.getType());
			params.put(i++, introInfo.getLastChapter());
			params.put(i++, introInfo.getChapterCount());
			params.put(i++, introInfo.getChapterListUrl());
			params.put(i++, introInfo.getWordCount());
			params.put(i++, introInfo.getKeyWords());
			
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, "0");
			String columns = "NAME, AUTHOR, DESCRIPTION, TYPE, LASTCHAPTER, CHAPTERCOUNT, CHAPTERLISTURL, WORDCOUNT, KEYWORDS, UPDATETIME, STATE";
			String condition = "WHERE id = '" + introInfo.getMd5Id() + "'";
			dbServer.update("novelinfo", columns, condition, params);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 保存章节列表信息
	  * @Discription:扩展说明
	  * @param chapters
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:48:28
	 */
	public void saveChapters(List<String[]> chapters){
		if(null == chapters){
			return;
		}
		
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			for (int i = 0; i < chapters.size(); i++) {
				String[] chapter = chapters.get(i);
				if(chapter.length != 4){
					continue;
				}
				//name,wordcount,time,url
				 String md5Id = ParseMD5.parseStr2MD5L32(chapter[3]);
				 if(!haveChapterUrl(md5Id)){
					 insertChapterUrl(chapter, i);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 修改简介页的抓取状态
	  * @Discription:扩展说明
	  * @param md5Id
	  * @param state
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:55:05
	 */
	public void updateInfoState(String md5Id, int state){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "UPDATE novelinfo SET state = '" + state + "' WHERE id = '" + md5Id + "'";
			dbServer.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 更新章节列表采集状态
	  * @Discription:扩展说明
	  * @param md5Id
	  * @param state
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午4:57:06
	 */
	public void updateChapterState(String md5Id, int state){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "UPDATE novelchapter SET state = '" + state + "' WHERE id = '" + md5Id + "'";
			dbServer.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 新增一个抓取简介页
	  * @Discription:扩展说明
	  * @param md5Id
	  * @param url
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午5:01:11
	 */
	public void insertInfoUrl(String md5Id, String url){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, md5Id);
			params.put(i++, url);
			
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, now);
			params.put(i++, "1");
			dbServer.insert("novelinfo", "ID, URL, CREATETIME, UPDATETIME, STATE", params);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 判断简介页是否存在
	  * @Discription:扩展说明
	  * @param md5Id
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午5:04:34
	 */
	private boolean haveInfoUrl(String md5Id){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		
		try {
			String sql = "SELECT SUM(1) AS count FROM novelinfo WHERE id = '" + md5Id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()){
				int count  = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 判断阅读页信息是否存在
	  * @Discription:扩展说明
	  * @param md5Id
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午5:08:55
	 */
	private boolean haveReadUrl(String md5Id){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "SELECT SUM(1) AS count FROM novelchapterdetail WHERE id = '" + md5Id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()){
				int count = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 插入章节列表页信息
	  * @Discription:扩展说明
	  * @param chapter
	  * @param chapterId
	  * @return void
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午5:15:00
	 */
	private void insertChapterUrl(String[] chapter, int chapterId){
		//name, wordcount, time, url
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, ParseMD5.parseStr2MD5L32(chapter[3]));
			params.put(i++, chapter[3]);
			params.put(i++, chapter[0]);
			params.put(i++, chapter[1]);
			params.put(i++, chapterId);
			params.put(i++, chapter[2]);
			
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, "1");
			dbServer.insert("novelchapter", "ID, URL, TITLE, WORDCOUNT, CHAPTERID, CHAPTERTIME, CREATETIME, STATE", params);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			dbServer.close();
		}
	}
	
	/**
	 * 是否存在章节信息
	  * @Discription:扩展说明
	  * @param md5Id
	  * @return
	  * @return boolean
	  * @Author: feizi
	  * @Date: 2015年11月24日 下午5:18:33
	 */
	private boolean haveChapterUrl(String md5Id){
		DBServer dbServer = new DBServer(DB_POOL_NAME);
		try {
			String sql = "SELECT SUM(1) AS count FROM novelchapter WHERE id = '" + md5Id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()){
				int count = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}finally{
			dbServer.close();
		}
	}
	
	public static void main(String[] args) {
		
	}
}

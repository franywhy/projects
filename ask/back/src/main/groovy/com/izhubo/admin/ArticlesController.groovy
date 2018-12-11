package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils

import com.hqonline.model.ArticleType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 文章管理
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ArticlesController extends BaseController {
	
	@Value("#{application['h5.domain']}")
	private String h5domain;
	private static final String DFMT = "yyyy-MM-dd HH:mm";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DFMT);
	
	DBCollection table(){
		return mainMongo.getCollection('hq_articles');
	}
	
	//0.企业动态 1.企业文化 2.总裁语录 3.2017年会
	
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		stringQuery(query, req, "article_title");
		stringQuery(query, req, "article_author");
		intQuery(query, req, "article_type");
		query.and("dr").is(0);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				//类型
				Integer type = obj["article_type"] as Integer;
				obj["article_type_name"] = ArticleType.n(type);
			}
		}
	}
	
	def add(HttpServletRequest req){
		
		Map user = (Map) req.getSession().getAttribute("user");
		def article = $$("_id" : UUID.randomUUID().toString());
		Integer article_type = ServletRequestUtils.getIntParameter(req, "article_type" , 0);
		article.put("article_title", req["article_title"]);//标题
		article.put("article_type", article_type);//类型
		article.put("article_info", req["article_info"]);//简介
		article.put("article_pic", req["article_pic"]);//图片
		article.put("article_url", buildArticleUrl(article["_id"]));//地址
//		article.put("article_url", req["article_url"]);//地址
		article.put("article_author", req["article_author"]);//作者
		article.put("article_html", req["article_html"]);//html
		article.put("timestamp", System.currentTimeMillis());//创建时间
		article.put("timestamp_text", getD());//创建时间
		article.put("create_time", System.currentTimeMillis());//创建时间
		article.put("create_user_id", user.get("_id") as Integer);//创建用户
		article.put("read_count", 0);//阅读数量
		article.put("dr", 0);//标记
//		article.put("article_", req["article_"]);
		
		table().save(article);
		Crud.opLog("hq_articles",[save:article["_id"]]);
		return OK();
	}
	
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		if(StringUtils.isEmpty(id))
			return [code:0];
		Integer article_type = ServletRequestUtils.getIntParameter(req, "article_type" , 0);
		table().update(
			new BasicDBObject("_id":id),
			new BasicDBObject('$set':
				new BasicDBObject(
					"article_title" : req["article_title"],
					"article_type" : article_type,
					"article_info" : req["article_info"],
					"article_pic" : req["article_pic"],
//					"article_url" : buildArticleUrl,
//					"article_url" : req["article_url"],
					"article_author" : req["article_author"],
					"article_html" : req["article_html"],
					"update_user_id" : user.get("_id") as Integer,
					"update_time" : System.currentTimeMillis(),
					"timestamp" : System.currentTimeMillis(),
					"timestamp_text" : getD()
				)
			));
		
		Crud.opLog("hq_articles",[edit:id]);
		return OK();
	}
	
	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		String id = req["_id"];
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
			new BasicDBObject("_id":id),
			new BasicDBObject('$set':
				new BasicDBObject(
					"dr" : 1
				)
			));
		
		Crud.opLog("hq_articles",[del:id]);
		return OK();
	}
	
	def buildArticleUrl(String id){
		return h5domain+"article_teacher.html?article_id="+id;
	}
	
	private String getD(){
		return sdf.format(new Date());
	}
}

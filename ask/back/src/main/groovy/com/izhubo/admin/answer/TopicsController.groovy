package com.izhubo.admin.answer

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value

import com.izhubo.admin.BaseController
import com.izhubo.admin.Web
import com.izhubo.admin.msg.MsgController
import com.izhubo.model.ReplyShowType
import com.izhubo.model.ReplyType
import com.izhubo.model.TopicBunsStates
import com.izhubo.model.TopicBunsType
import com.izhubo.model.TopicChannelType
import com.izhubo.model.TopicEndType
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicReplyShowType
import com.izhubo.model.TopicsReplyType
import com.izhubo.model.TopicsType
import com.izhubo.model.node.Chat
import com.izhubo.model.node.Nodes
import com.izhubo.model.node.TopicChatMsg
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.Bunus
import com.mysqldb.model.UserFinance

/**
 * 问题列表和详情
 * @author shihongjie
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class TopicsController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private MsgController msgController;
	
	public DBCollection _topics(){
		return mainMongo.getCollection("topics");
	}
	public DBCollection _topics_reply(){
		return mainMongo.getCollection("topics_reply");
	}
	private DBCollection topics_reply(){
		return mainMongo.getCollection("topics_reply");
	}
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	//红包图片地址
	String red_url ;
	@Value("#{application['red.url']}")
	void setRedUrl(String red_url){
		this.red_url = red_url;
	}
	
	//初始化文件服务器地址
	String file_url ;
	@Value("#{application['pic.domain']}")
	void setFileUrl(String file_url){
		this.file_url = file_url;
	}
	
	/**
	 * 问题列表
	 * @param req
	 * @return
	 */
	def list(HttpServletRequest req){

		QueryBuilder query = Web.fillTimeBetween(req);

		//学生id
		intQuery(query,req,'author_id');
		//老师id
		intQuery(query,req,'teach_id');
		//问题状态
		intQuery(query,req,'type');
		//评价状态
		intQuery(query,req,'evaluation_type');
		//红包状态
		intQuery(query,req,'buns_states');
		//结束触发
		intQuery(query,req,'end_type');
		//打赏状态
		String tip_states=req["tip_kd"];
		if(tip_states.equals("0")){
			intQuery(query,req,'tip_kd');
		}else if((!tip_states.equals(null)) && (!tip_states.equals("0"))){
			query.and('tip_kd').greaterThan(0);
		}


		//问题内容
		def content = req.getParameter("content");
		if(StringUtils.isNotBlank(content)){
			Pattern pattern = Pattern.compile("^.*" + content + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("content").regex(pattern);
		}

		Crud.list(req,_topics(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){

				//学生id
				Integer author_id = obj['author_id'] as Integer;

				//问题状态
				Integer type = obj['type'] as Integer;
				obj["type_name"] = TopicsType.vname(type);

				//评价
				Integer evaluation_type = obj['evaluation_type'] as Integer;
				if(evaluation_type){
					obj["evaluation_name"] = TopicEvaluationType.vname(evaluation_type);
					
					if(evaluation_type == TopicEvaluationType.满意.ordinal() || evaluation_type == TopicEvaluationType.非常满意.ordinal()){
						
						/*Bunus bunus = (Bunus) sessionFactory.getCurrentSession().createCriteria(Bunus.class)
						.add(Restrictions.eq(Bunus.PROP_TOPICID, obj["_id"]))
						.uniqueResult();
						
						if(bunus){
							obj["bunus_money"] = bunus.getMmoney();
						}*/
						
						def bunus = topic_bunus().findOne($$("topic_id" : obj["_id"]) , $$("mmoney" : 1));
						if(bunus){
							obj["bunus_money"] = bunus["mmoney"];
						}
					}
				}
				//提问渠道
				Integer channel_type = obj['channel_type'] as Integer;
				if(channel_type){
					obj["channel_name"] = TopicChannelType.vname(channel_type);
				}
				//结束类型
				Integer end_type = obj['end_type'] as Integer;
				if(end_type){
					obj["end_name"] = TopicEndType.vname(end_type);
				}
				//抽奖状态
				Integer buns_states = obj['buns_states'] as Integer;
				if(buns_states){
					obj["buns_name"] = TopicBunsType.vname(buns_states);
				}

				//学生名字
				obj["author_name"] = users().findOne($$("_id" : author_id) , $$("nick_name" : 1))?.get("nick_name");

				//已有老师抢答
				if(TopicsType.抢答成功.ordinal() == type || TopicsType.问题已结束.ordinal() == type ){

					//老师id
					Integer teach_id = obj['teach_id'] as Integer;

					if(teach_id){
						//老师名字
						obj["teach_name"] = users().findOne($$("_id" : teach_id ), $$("nick_name" : 1))?.get("nick_name");
					}
				}
				//打赏金额
				Double topic_tip = 0d;
				topic_tip=obj['tip_kd'] as Double;
				obj["topic_tip"] = topic_tip;
				
				//标签
				obj["tips_name"] = _topics().findOne($$("_id" : obj["_id"]),$$("tips":1))?.get("tips")[0]?.get("tip_name");
				

			}

		}
	}
	
	/**
	 * 聊天内容
	 * @param req
	 * @return
	 */
	def topics_reply_list(HttpServletRequest req){
		def data = new HashMap();
		String topic_id = req["topic_id"];
		if(StringUtils.isNotBlank(topic_id)){
			def topics = _topics().findOne($$("_id" : topic_id) , $$("teach_id" : 1 , "author_id" : 1 , "type" : 1));
			def trData = _topics_reply().find($$("topic_id" : topic_id)).sort($$("reply_time" : 1))?.toArray();
			if(trData && topics){
				Integer type = topics["type"] as Integer;
				Integer author_id = topics["author_id"] as Integer;
				Integer teach_id = topics["teach_id"] as Integer;
				
				String author_name = null;
				String teach_name = null;
				
				if(author_id) author_name = users().findOne($$("_id" : author_id) , $$("nick_name" : 1))?.get("nick_name");
				if(teach_id) teach_name = users().findOne($$("_id" : teach_id) , $$("nick_name" : 1))?.get("nick_name");
				
				trData.each {BasicDBObject obj ->
					//角色  系统 老师 学生
					String role_name = "系统";
					Integer role_type = 0;
					if(obj["user_id"] == author_id){
						role_name = "学生";
						role_type = 1;
						obj["user_name"] = author_name;
					}else if(obj["user_id"] == teach_id){
						role_name = "教师";
						role_type = 2;
						obj["user_name"] = teach_name;
					}
					obj["role_name"] = role_name;
					obj["role_type"] = role_type;
					
					//回复类型
					obj["reply_type_name"] = TopicsReplyType.vname(obj["reply_type"] as Integer);
					//显示类型
					obj["show_type_name"] = TopicReplyShowType.vname(obj["show_type"] as Integer);
					
					Integer reply_type = obj["reply_type"] as Integer;
					
					if(obj["reply_content"] && (reply_type==TopicsReplyType.图片.ordinal() || reply_type==TopicsReplyType.语音.ordinal()) ){
						obj["reply_content"] = file_url + obj["reply_content"];
					}
				}
			}
			data["topics"] = topics;
			data["trData"] = trData;
			
		}
		return ["code" : 1 , "data" : data];
	}
	
	

}

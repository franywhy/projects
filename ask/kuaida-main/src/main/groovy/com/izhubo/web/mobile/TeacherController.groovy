package com.izhubo.web.mobile

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.hqonline.model.Privs
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UnEvaluation
import com.izhubo.model.UserType
import com.izhubo.model.VlevelType
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.utils.RegExUtils
import com.izhubo.web.BaseController
import com.izhubo.web.vo.TeacherListByScoreV200
import com.izhubo.web.vo.TeacherTopicEndTeacherInfoV200
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation


@Rest
@TypeChecked(TypeCheckingMode.SKIP)
@RequestMapping("/teacher")
class TeacherController extends BaseController {
	
	private DBCollection topics(){
//		mainMongo.group(HEADER_CACHE_CONTROL, null, null)
		return mainMongo.getCollection("topics");
	}
	private DBCollection _attention(){
		return mainMongo.getCollection("attention");
	}
	
	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}
	
	public DBCollection reportdata_teacher_groupbytopicscount(){
		return logMongo.getCollection("reportdata_teacher_groupbytopicscount");
	}
	
	
	//问题结束后-评价-老师信息(老师id 昵称 图片 --抢答数量 关注数量 好评率 是否收藏)
	@ResponseBody
	@RequestMapping(value = "topic_end_teacher_info_v200", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "问题结束后-评价-老师信息(老师id 昵称 图片 --抢答数量 关注数量 好评率 是否收藏);不满意的tips", httpMethod = "GET", response = TeacherTopicEndTeacherInfoV200.class, notes = "问题结束后-评价-老师信息(老师id 昵称 图片 --抢答数量 关注数量 好评率 是否收藏);不满意的tips")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "topic_id", value = "问题id", required = true, dataType = "String", paramType = "query")
	])
	def topic_end_teacher_info_v200(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//老师id
		def topic = topics().findOne($$("_id" : topic_id) , $$("teach_id" : 1 , "author_id" : 1 , "_id" : 1));
		Integer teacher_id = topic["teach_id"] as Integer;
		//老师信息
		def teacher = users().findOne($$("_id" : teacher_id) , $$("_id" : 1 , "pic" : 1 , "nick_name" : 1));
		//关注数量
		teacher["target_num"] = attention_num(teacher_id);
		//
		teacher["teach_id"] = teacher["_id"];
		//好评比率
		teacher["satisfaction"] = teacher_replay_end_percent(teacher_id);
		//抢答数
		teacher["qiangda_num"] = replay_num(teacher_id);
		//是否已收藏
		teacher["isAttention"] = _attention().count($$("target_tuid" : teacher_id , "source_tuid" : topic["author_id"])) > 0 ;
		
		//结果集
		Map data = new HashMap();
		//教师信息
		data["teacher"] = teacher;
		//
		data["evaluation"] = UnEvaluation.getUnEvaluationList();
		return getResultOK(data);
	}
	//问题结束后-评价-老师信息(老师id 昵称 图片 --抢答数量 关注数量 好评率 是否收藏)
	def topic_end_teacher_info(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//老师id
		def topic = topics().findOne($$("_id" : topic_id) , $$("teach_id" : 1 , "author_id" : 1 , "_id" : 1));
		Integer teacher_id = topic["teach_id"] as Integer;
		//老师信息
		def teacher = users().findOne($$("_id" : teacher_id) , $$("_id" : 1 , "pic" : 1 , "nick_name" : 1));
		//关注数量
		teacher["target_num"] = attention_num(teacher_id);
		//
		teacher["teach_id"] = teacher["_id"];
		//好评比率
		teacher["satisfaction"] = teacher_replay_end_percent(teacher_id);
		//抢答数
		teacher["qiangda_num"] = replay_num(teacher_id);
		//是否已收藏
		teacher["isAttention"] = _attention().count($$("target_tuid" : teacher_id , "source_tuid" : topic["author_id"])) > 0 ;
		return getResultOK(teacher);
	}
	
	//好评百分比
	def teacher_replay_end_percent(Integer teacher_id){
		long y = teacher_replay_end_with_evaluation(teacher_id);
		long z = 0;
		if(y != 0){
			z = teacher_replay_end_well(teacher_id) / y * 100;
		}
		return NumberUtil.formatDouble3(z , 0)  + "%";
	}
	
	//老师回答的问题-已结束-满意的数量
	def teacher_replay_end_well(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id ,"type" : TopicsType.问题已结束.ordinal()  , "deleted" : false,"evaluation_type" : $$('$in' :[TopicEvaluationType.满意.ordinal() , TopicEvaluationType.很满意.ordinal()]) ));
	}
	
	//老师回答的问题-已结束
	def teacher_replay_end(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id , "type" : TopicsType.问题已结束.ordinal() , "deleted" : false  ));
	}
	
	//老师回答的问题-已结束-并且已经评价的（未评价的不计入满意计算中）
	def teacher_replay_end_with_evaluation(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id , "type" : TopicsType.问题已结束.ordinal() , "deleted" : false,"evaluation_type": $$($ne : TopicEvaluationType.未评价.ordinal()) ));
	}
	
	//抢答数量
	def replay_num(Integer teach_id){
		return topics().count($$("teach_id" : teach_id , "deleted" : false ));
	}
	
	/**
	 * 粉丝列表
	 */
	def attentionList(HttpServletRequest request){
		Integer teacher_id = ServletRequestUtils.getIntParameter(request, "teacher_id", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		if(teacher_id > 0){
			def attention = _attention().
				find($$("target_tuid" : teacher_id)).
				sort($$("timestamp" : -1)).
				skip((page - 1) * size).
				limit(size)?.toArray();
			if(attention){
				attention.each {def dbo->
					//用户id
					Integer source_tuid = dbo["source_tuid"];
					
					//用户详细信息
					def user = users().findOne($$("_id" : source_tuid) , $$("nick_name" : 1 , "pic" : 1));
					//用户昵称
//					dbo["nick_name"] = user["nick_name"];
					String nick_name = user["nick_name"];
					dbo["nick_name"] = RegExUtils.mobileReplace(nick_name);
					
					//用户图片
					dbo["pic"] = user["pic"];
					
					//用户提问数量
					double topic_num = topics().count($$("author_id" : source_tuid , "deleted" : false));
					//用户提问完成
					double topic_fin_num = topics().count($$("author_id" : source_tuid , "deleted" : false ,"type" : TopicsType.问题已结束.ordinal()));
//					dbo["topic_num"] = topic_num;
					
					//用户提问后有评价的数量
					double topic_evaluation_num = topics().count($$("author_id" : source_tuid , "deleted" : false ,  "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne : TopicEvaluationType.未评价.ordinal())));
					
					//好评率
					String evaluation_percent = "0";
					if(topic_fin_num > 0){
						evaluation_percent = NumberUtil.formatDouble3((topic_evaluation_num / topic_fin_num ) *100 , 0);
					}
//					dbo["evaluation_percent"] = evaluation_percent;
					
					//用户关注数量
					def attention_num = _attention().count($$("source_tuid" : source_tuid ));
					
					dbo["uinfo"] = "提问：" + topic_num + " ● 关注：" + attention_num + " ● 点评率：" + evaluation_percent + "%";
				}
			}
			return getResultOKS(attention);
				
		}
		return getResultParamsError();
	}
	/**
	 * 粉丝列表
	 */
	def attentionList_v300(HttpServletRequest request){
		Integer teacher_id = ServletRequestUtils.getIntParameter(request, "teacher_id", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		if(teacher_id > 0){
			def attention = _attention().
					find($$("target_tuid" : teacher_id)).
					sort($$("timestamp" : -1)).
					skip((page - 1) * size).
					limit(size)?.toArray();
			if(attention){
				attention.each {def dbo->
				//用户id
				Integer source_tuid = dbo["source_tuid"];
				
				//用户详细信息
				def user = users().findOne($$("_id" : source_tuid) , $$("nick_name" : 1 , "pic" : 1));
				//用户昵称
//					dbo["nick_name"] = user["nick_name"];
				String nick_name = user["nick_name"];
				dbo["nick_name"] = RegExUtils.mobileReplace(nick_name);
				
				//用户图片
				dbo["pic"] = user["pic"];
				
				//用户提问数量
				double topic_num = topics().count($$("author_id" : source_tuid , "deleted" : false));
				//用户提问完成
				double topic_fin_num = topics().count($$("author_id" : source_tuid , "deleted" : false ,"type" : TopicsType.问题已结束.ordinal()));
//					dbo["topic_num"] = topic_num;
				
				//用户提问后有评价的数量
				double topic_evaluation_num = topics().count($$("author_id" : source_tuid , "deleted" : false ,  "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne : TopicEvaluationType.未评价.ordinal())));
				
				//好评率
				String evaluation_percent = "0";
				if(topic_fin_num > 0){
					evaluation_percent = NumberUtil.formatDouble3((topic_evaluation_num / topic_fin_num ) *100 , 0);
				}
//					dbo["evaluation_percent"] = evaluation_percent;
				
				//用户关注数量
				def attention_num = _attention().count($$("source_tuid" : source_tuid ));
				
				//用户提问数量
				dbo["topic_num"] = topic_num;
				//用户关注数量
				dbo["attention_num"] = attention_num;
				//点评率
				dbo["evaluation_percent"] = evaluation_percent+"%";
//				dbo["uinfo"] = "提问：" + topic_num + " ● 关注：" + attention_num + " ● 点评率：" + evaluation_percent + "%";
				}
			}
			return getResultOKS(attention);
			
		}
		return getResultParamsError();
	}
	
	
	/**
	 * 每日精彩 1.问题已经结束的 2.评论是满意的
	 * @Description: 每日精彩
	 * @date 2015年6月3日 上午10:24:33 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def topics_list_max_score(HttpServletRequest request){
		//
		def topocs = topics().find(
			$$("type" : TopicsType.问题已结束.ordinal() , "evaluation_type" :  $$('$in' :[TopicEvaluationType.满意.ordinal() , TopicEvaluationType.很满意.ordinal()]) , "deleted" : false ), 
			$$("_id" : 1 , "author_id" : 1 , "industry_id" : 1 , "content" : 1 , "tips" : 1)
			).sort($$("update_at" : -1)).limit(5).toArray();
		if(topocs != null && topocs.size() > 0){
			topocs.each { DBObject dbo ->
				//用户图片
				dbo["pic"] = mainMongo.getCollection("users").findOne( $$("_id":dbo["author_id"]), $$("pic" : 1))?.get("pic");
				//行业名称
				dbo["industry_name"] = mainMongo.getCollection("industry").findOne($$("_id":dbo["industry_id"]), $$("industry_name" : 1))?.get("industry_name");
			}
		}
		return getResultOK(topocs);
	}
	
	/**
	 * 每日精彩 1.问题已经结束的 2.评论是满意的
	 * @Description: 每日精彩
	 * @date 2015年6月3日 上午10:24:33
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	def topics_list_max_score_android_v150(HttpServletRequest request){
		//
		def topocs = topics().find(
			$$("type" : TopicsType.问题已结束.ordinal() , "evaluation_type" :   $$('$in' :[TopicEvaluationType.满意.ordinal() , TopicEvaluationType.很满意.ordinal()]) , "deleted" : false ),
			$$("_id" : 1 , "author_id" : 1 , "industry_id" : 1 , "content" : 1 , "tips" : 1 )
			).sort($$("update_at" : -1)).limit(5).toArray();
		if(topocs != null && topocs.size() > 0){
			topocs.each { DBObject dbo ->
				//用户图片
//				dbo["pic"] = mainMongo.getCollection("users").findOne( $$("_id":dbo["author_id"]), $$("pic" : 1 , "vlevel" : 1))?.get("pic");
				//add by shihongjie 2015-12-21 VIP_ICON
				//vlevle
				def authorMap = mainMongo.getCollection("users").findOne($$("_id":dbo["author_id"]), $$("pic" : 1 , "vlevel" : 1));
				if(authorMap){
					//add by shihongjie 2015-12-21 VIP_ICON
					dbo["s_vip_icon"] = VlevelType.vipIcon(authorMap["vlevel"]);
					dbo["pic"] = authorMap["pic"];
				}else{//如果用户被删除 返回null数据 测试线存在用户删除情况
					dbo["s_vip_icon"] = false;
					dbo["pic"] = null;
				}
				//行业名称
				dbo["industry_name"] = mainMongo.getCollection("industry").findOne($$("_id":dbo["industry_id"]), $$("industry_name" : 1))?.get("industry_name");
				
				
			}
		}
		return getResultOK(topocs);
	}
	
	/**
	 * 首页三条老师
	 * @Description: 首页三条老师
	 * @date 2015年6月3日 上午10:11:56 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def teacher_list_by_score(HttpServletRequest request){
		return getResultOK(home_teacher_list_by_score_v151( ServletRequestUtils.getIntParameter(request, "user_id", 0)) );
	}
	
	def home_teacher_list_by_score_v151( int user_id){
		def teacherList = reportdata_teacher_groupbytopicscount().find($$("dr" : 0))
				.sort( $$("timesatmp" : -1 , "topic_day_count" : -1 , "topic_total" : -1))
				.limit(5).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each {def row ->
				Map map = new HashMap();
				map["teach_id"] = row["user_id"];
				map["pic"] = row["user_id"];
				
				map["qiangda_num"] = (row["topic_total"] == null?0:row["topic_total"]);
				//被关注数量
				map["target_num"] = attention_num(row["_id"]);
				
				
	//			//满意度
				map["satisfaction"] = row["attention_count"];
				
				//是否有关注
				if(user_id > 0){
					map["isAttention"] = _attention().count($$("target_tuid" : row["user_id"] , "source_tuid" : user_id)) > 0 ;
				}
				list.add(map);
			}
		}
		
		return list;
	}
	def home_teacher_list_by_score(int size , int page , int user_id){
		def teacherList = users().find(
				$$("priv" : UserType.教学老师.ordinal()), 
				$$("_id" : 1 , "pic" : 1 , "topic_count" : 1 , "topic_evaluation_count" : 1  )//抢答完成的数量,抢答完成后好评的数量
				)
				.skip( (page - 1) * size)
				.sort( $$("topic_evaluation_count" : -1 , "topic_count" : -1 , "_id" : -1))
				.limit(size).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each {def row ->
			Map map = new HashMap();
			map["teach_id"] = row["_id"];
			map["pic"] = row["pic"];
			
			map["qiangda_num"] = (row["topic_count"] == null?0:row["topic_count"]);
			//被关注数量
			map["target_num"] = attention_num(row["_id"]);
			
			long qiangda_num = row["topic_count"]? row["topic_count"] : 0;
			long topic_evaluation_count = row["topic_evaluation_count"] ? row["topic_evaluation_count"] : 0 ;
			
//			//满意度
			map["satisfaction"] = teacher_replay_end_percent(row["_id"] as Integer);
			
			//是否有关注
			if(user_id > 0){
				map["isAttention"] = _attention().count($$("target_tuid" : row["_id"] , "source_tuid" : user_id)) > 0 ;
			}
			list.add(map);
			
			}
		}
		
		return list;
	}
	
	
	/**
	 * 首页老师
	 * @Description: 首页老师
	 */
	@ResponseBody
	@RequestMapping(value = "teacher_list_by_score_v200", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页老师2.00", httpMethod = "GET", response = TeacherListByScoreV200.class, notes = "首页老师2.00")
	def teacher_list_by_score_v200(HttpServletRequest request){
//		Integer user_id = ServletRequestUtils.getIntParameter(request, "user_id", 0);
		
		//add by shihongjie 2015-12-21 VIP_ICON
		def teacherList = reportdata_teacher_groupbytopicscount().find($$("dr" : 0))
				.sort( $$("timesatmp" : -1 , "topic_day_count" : -1 , "topic_total" : -1))
				.limit(5).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each {def row ->
				Map map = new HashMap();
				map["teach_id"] = row["user_id"];
				map["pic"] = row["user_pic"];
				//TODO 2.0
				map["nick_name"] = row["user_name"];
				
//				map["qiangda_num"] = (row["topic_total"] == null?0:row["topic_total"]);
//				//被关注数量
//				map["target_num"] = row["attention_count"];
				
				//满意度
				map["satisfaction"] = row["satisfaction"];
				
//				//是否有关注
//				if(user_id > 0){
//					map["isAttention"] = _attention().count($$("target_tuid" : row["user_id"] , "source_tuid" : user_id)) > 0 ;
//				}
				
				map["vip_icon"] = VlevelType.vipIcon(row["vlevel"]);
				list.add(map);
			}
		}
		
		return getResultOK(list);
	}
	/**
	 * 首页三条老师
	 * @Description: 首页三条老师
	 * @date 2015年6月3日 上午10:11:56
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	def teacher_list_by_score_android_v150(HttpServletRequest request){
		return getResultOK(home_teacher_list_by_score_android_v151(ServletRequestUtils.getIntParameter(request, "user_id", 0)) );
	}
	
	/**
	 * 老师排序改为前天的数据
	 * @param user_id
	 * add by shihongjie 2016-03-28
	 */
	def home_teacher_list_by_score_android_v151(int user_id){
		
		//add by shihongjie 2015-12-21 VIP_ICON
		def teacherList = reportdata_teacher_groupbytopicscount().find($$("dr" : 0))
				.sort( $$("timesatmp" : -1 , "topic_day_count" : -1 , "topic_total" : -1))
				.limit(5).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each {def row ->
				Map map = new HashMap();
				map["teach_id"] = row["user_id"];
				map["pic"] = row["user_pic"];
				
				map["qiangda_num"] = (row["topic_total"] == null?0:row["topic_total"]);
				//被关注数量
				map["target_num"] = attention_num(row["user_id"]);
//				map["target_num"] = row["attention_count"];
				
				//满意度
				map["satisfaction"] = row["satisfaction"];
				
				//是否有关注
				if(user_id > 0){
					map["isAttention"] = _attention().count($$("target_tuid" : row["user_id"] , "source_tuid" : user_id)) > 0 ;
				}
				
				map["vip_icon"] = VlevelType.vipIcon(row["vlevel"]);
				list.add(map);
			}
		}
		
		return list;
	}
	def home_teacher_list_by_score_android_v150(int size , int page , int user_id){
		
		//add by shihongjie 2015-12-21 VIP_ICON
		def teacherList = users().find(
				$$("priv" : UserType.教学老师.ordinal()),
				$$("_id" : 1 , "pic" : 1 , "topic_count" : 1 , "topic_evaluation_count" : 1 ,"vlevel" : 1  )//抢答完成的数量,抢答完成后好评的数量
				)
				.skip( (page - 1) * size)
				.sort( $$("topic_evaluation_count" : -1 , "topic_count" : -1 , "_id" : -1))
				.limit(size).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each {def row ->
			Map map = new HashMap();
			map["teach_id"] = row["_id"];
			map["pic"] = row["pic"];
			
			map["qiangda_num"] = (row["topic_count"] == null?0:row["topic_count"]);
			//被关注数量
			map["target_num"] = attention_num(row["_id"]);
			
			long qiangda_num = row["topic_count"]? row["topic_count"] : 0;
			long topic_evaluation_count = row["topic_evaluation_count"] ? row["topic_evaluation_count"] : 0 ;
			//满意度
			map["satisfaction"] = teacher_replay_end_percent(row["_id"] as Integer);
			
			//是否有关注
			if(user_id > 0){
				map["isAttention"] = _attention().count($$("target_tuid" : row["_id"] , "source_tuid" : user_id)) > 0 ;
			}
			
			//add by shihongjie 2015-12-21 VIP_ICON
			//vlevle
			map["vip_icon"] = VlevelType.vipIcon(row["vlevel"]);
			list.add(map);
			
			}
		}
		
		return list;
	}
	
	
	
	/**
	 * 老师列表-分页
	 * @Description: 老师列表-分页 
	 * @date 2015年6月5日 下午5:08:51 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def teacher_list(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int user_id = ServletRequestUtils.getIntParameter(request, "user_id", 0);
		return getResultOK(_teacher_list_by_score1(size , page ,user_id))
	}
	
	
	
	def _teacher_list_by_score1(int size , int page , int user_id){
		def teacherList = users().find(
				$$("priv1" : 1 , "priv" : UserType.教学老师.ordinal()) , 
				$$("_id" : 1 , "pic" : 1 , "nick_name" : 1 , 
						"topic_count" : 1 , "topic_evaluation_count" : 1 , //抢答完成的数量,抢答完成后好评的数量
						"user_industry" : 1		) 
				)
				.skip( (page - 1) * size)
				.sort( $$("topic_evaluation_count" : -1 , "topic_count" : -1 , "_id" : -1))
				.limit(size).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each { def row->
			
//				BasicDBObject row = (BasicDBObject) records.next();
			Map map = new HashMap();
			map["teach_id"] = row["_id"];
			map["nick_name"] = row["nick_name"];
			map["pic"] = row["pic"];
			
			map["qiangda_num"] = row["topic_count"];
			//被关注数量
			map["target_num"] = attention_num(row["_id"]);
			//教师标签
			def user_industryList = row["user_industry"];
			map["tips"] = tipToString(user_industryList);
			
			long qiangda_num = row["topic_count"]? row["topic_count"] : 0;
			long topic_evaluation_count = row["topic_evaluation_count"] ? row["topic_evaluation_count"] : 0 ;
			//满意度
//			if(qiangda_num > 0){
//				map["satisfaction"] = NumberUtil.formatDouble3((topic_evaluation_count/qiangda_num  ) * 100 , 0) + "%";
//			}else{
//				map["satisfaction"] = "0%";
//			}
			
			map["satisfaction"] = teacher_replay_end_percent(row["_id"] as Integer);
			
			//是否有关注
			if(user_id > 0){
				map["isAttention"] = _attention().count($$("target_tuid" : row["_id"] , "source_tuid" : user_id)) > 0 ;
			}
				list.add(map);
			}
		}
		
		return list;
			
	}
	
	/**
	 * 老师列表-分页
	 * @Description: 老师列表-分页 
	 * @date 2015年6月5日 下午5:08:51 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def teacher_list_android_v150(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		int user_id = ServletRequestUtils.getIntParameter(request, "user_id", 0);
		return getResultOK(_teacher_list_by_score1_android_v150(size , page ,user_id))
	}
	
	def _teacher_list_by_score1_android_v150(int size , int page , int user_id){
		
		def teacherList = users().find(
				$$("priv" : UserType.教学老师.ordinal() , "priv1" : 1) , 
				$$("_id" : 1 , "pic" : 1 , "nick_name" : 1 , 
						"topic_count" : 1 , "topic_evaluation_count" : 1 , //抢答完成的数量,抢答完成后好评的数量
						"user_industry" : 1	, "vlevel" : 1	
						) 
				)
				.skip( (page - 1) * size)
				.sort( $$("topic_evaluation_count" : -1 , "topic_count" : -1 , "_id" : -1))
				.limit(size).toArray();
		
		List list = new ArrayList();
		if(teacherList){
			teacherList.each { def row->
			
//				BasicDBObject row = (BasicDBObject) records.next();
			Map map = new HashMap();
			map["teach_id"] = row["_id"];
			map["nick_name"] = row["nick_name"];
			map["pic"] = row["pic"];
			
			map["qiangda_num"] = row["topic_count"];
			//被关注数量
			map["target_num"] = attention_num(row["_id"]);
			//教师标签
			def user_industryList = row["user_industry"];
			map["tips"] = tipToString(user_industryList);
			
			long qiangda_num = row["topic_count"]? row["topic_count"] : 0;
			long topic_evaluation_count = row["topic_evaluation_count"] ? row["topic_evaluation_count"] : 0 ;
			//满意度
//			if(qiangda_num > 0){
//				map["satisfaction"] = NumberUtil.formatDouble3((topic_evaluation_count/qiangda_num  ) * 100 , 0) + "%";
//			}else{
//				map["satisfaction"] = "0%";
//			}
			
			map["satisfaction"] = teacher_replay_end_percent(row["_id"] as Integer);
			
			//是否有关注
			if(user_id > 0){
				map["isAttention"] = _attention().count($$("target_tuid" : row["_id"] , "source_tuid" : user_id)) > 0 ;
			}
			list.add(map);
			//vlevle
			map["vip_icon"] = VlevelType.vipIcon(row["vlevel"]);
			}
			
		}
		
		return list;
		
	}
	
	/**
	 * 老师详情
	 * @Description: 老师详情
	 * @date 2015年6月5日 下午5:09:28 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def teacher_info(HttpServletRequest request){
		int teacher_id = ServletRequestUtils.getIntParameter(request, "teacher_id", 0);
		int user_id = ServletRequestUtils.getIntParameter(request, "user_id", 0);
		if(teacher_id != 0){
			def user = mainMongo.getCollection("users").findOne($$("_id" : teacher_id), $$("_id" : 1 , "pic" : 1 , "nick_name" : 1 , "user_industry" : 1 , "topic_count" : 1 , "topic_evaluation_count" : 1 , "vlevel" : 1));
			
			//抢答数量
			long qiangda_num = user["topic_count"];
//			long qiangda_num = topics().count($$("deleted": false , "type" : TopicsType.问题已结束.ordinal() , "teach_id" : teacher_id));
			//好评数量
			long evaluation_num = user["topic_evaluation_count"];
//			long evaluation_num = topics().count($$("deleted": false , "type" : TopicsType.问题已结束.ordinal() , "teach_id" : teacher_id , "evaluation_type" : TopicEvaluationType.好评.ordinal()));
			//抢答数量
			user["qiangda_num"] = qiangda_num;
			//满意度
//			if(qiangda_num > 0){
//				user["evaluation_num"] = NumberUtil.formatDouble3(evaluation_num / qiangda_num * 100 , 0) + "%";
//			}else{
//				user["evaluation_num"] = "0%";
//			}
			
			user["evaluation_num"] = teacher_replay_end_percent(teacher_id);
			//被关注数量
			user["target_num"] = attention_num(teacher_id);
			
			
			//关注学员列表
			def attention_list = mainMongo.getCollection("attention").find($$("target_tuid" : teacher_id) , $$("source_tuid" : 1)).sort($$("timestamp" : -1)).limit(5).toArray();
			if(attention_list){
				user["attention_list"] = attention_list;
				attention_list.each {BasicDBObject row ->
					row["pic"] = mainMongo.getCollection("users").findOne($$("_id" : row["source_tuid"]) , $$("pic" : 1))?.get("pic");
				}
			}
			
			def user_industry = user["user_industry"];
			user["tips"] = tipToString("",user_industry);
			
			def topics = topics().find(
					$$("teach_id" : teacher_id , "deleted" : false , "type" : TopicsType.问题已结束.ordinal() ,"evaluation_type" :   $$('$in' :[TopicEvaluationType.满意.ordinal() , TopicEvaluationType.很满意.ordinal()])) , 
					$$("_id" : 1 , "content" : 1 , "author_id" : 1 , "industry_id" : 1 , "tips" : 1 )
				).sort($$("update_at" : -1)).limit(5).toArray();
			
			if(topics){
				topics.each {BasicDBObject row ->
					row["pic"] = mainMongo.getCollection("users").findOne($$("_id" : row["author_id"] as Integer) , $$("pic" : 1))?.get("pic");
				}
			}
			
			if(user_id > 0){
				user["isAttention"] = _attention().count($$("target_tuid" : teacher_id , "source_tuid" : user_id)) > 0 ;
			}
			
			user["topics"] = topics;
			
			//vlevel
			user["vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
//			user["vip_icon"] = vipIcon(Integer.valueOf(user["vlevel"]?.toString()));
			user.removeField("vlevel");
			return getResultOK(user);
		}
		return getResultParamsError();
	}
	
	
	/**
	 * 会答中获取老师详情
	 * @Description: 老师详情
	 * @date 2017年8月4日 下午3:09:28
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	def teacher_info_v210(HttpServletRequest request){
		int teacher_id = ServletRequestUtils.getIntParameter(request, "teacher_id", 0);
		int user_id = ServletRequestUtils.getIntParameter(request, "user_id", 0);
		int size = ServletRequestUtils.getIntParameter(request , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);
		
		Map data = new HashMap();
		if(teacher_id != 0){
			if(page==1){
				data["teacherInfo"] = teacher_info_v210(teacher_id,user_id);
			}else{
				data["teacherInfo"] = null;
			}
			data["topicList"] = teacherTopicList(teacher_id,size,page);
			return getResultOK(data);
		}
		return getResultParamsError();
	}
	
	/**
	 * 获取会答老师基础信息v210
	 * @param teacher_id
	 * @param user_id
	 * @return
	 */
	def teacher_info_v210(int teacher_id , int user_id){
		def user = mainMongo.getCollection("users").findOne($$("_id" : teacher_id), $$("_id" : 1 , "pic" : 1 , "nick_name" : 1 , "user_industry" : 1 , "topic_count" : 1 , "topic_evaluation_count" : 1 , "vlevel" : 1));
		//抢答数量
		long qiangda_num = user["topic_count"];
		//好评数量
		long evaluation_num = user["topic_evaluation_count"];
		//抢答数量
		user["qiangda_num"] = qiangda_num;

		user["evaluation_num"] = teacher_replay_end_percent(teacher_id);
		//被关注数量
		user["target_num"] = attention_num(teacher_id);
		
		//关注学员列表
		def attention_list = mainMongo.getCollection("attention").find($$("target_tuid" : teacher_id) , $$("source_tuid" : 1)).sort($$("timestamp" : -1)).limit(5).toArray();
		if(attention_list){
			user["attention_list"] = attention_list;
			attention_list.each {BasicDBObject row ->
				row["pic"] = mainMongo.getCollection("users").findOne($$("_id" : row["source_tuid"]) , $$("pic" : 1))?.get("pic");
			}
		}
		
		def user_industry = user["user_industry"];
		user["tips"] = tipToString("",user_industry);
		
		if(user_id > 0){
			user["isAttention"] = _attention().count($$("target_tuid" : teacher_id , "source_tuid" : user_id)) > 0 ;
		}
		user["vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
		user.removeField("vlevel");
		
		return user;
	}
	/**
	 * 获取老师回答列表v210
	 * @param teacher_id
	 * @param user_id
	 * @param size
	 * @param page
	 * @return
	 */
	def teacherTopicList(int teacher_id , int size , int page){
		def topics = topics().find(
				$$("teach_id" : teacher_id , "deleted" : false , "type" : TopicsType.问题已结束.ordinal() ,"evaluation_type" :   $$('$in' :[
					TopicEvaluationType.满意.ordinal() ,
					TopicEvaluationType.很满意.ordinal()
				])) ,
				$$("_id" : 1 , "content" : 1 , "author_id" : 1 , "industry_id" : 1 , "tips" : 1 )
				).sort($$("update_at" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topics){
			topics.each {BasicDBObject row ->
				row["pic"] = mainMongo.getCollection("users").findOne($$("_id" : row["author_id"] as Integer) , $$("pic" : 1))?.get("pic");
			}
		}
		return topics
	}
	
	
//	/**
//	 * 是否显示VIP的ICON
//	 * @param vlevel
//	 * @return
//	 */
//	private boolean vipIcon(Integer vlevel){
//		boolean show = false;
//		if(VlevelType.V1.ordinal() == vlevel){
//			show = true;
//		}
//		return show;
//	}
	
	/**
	 * 查询被关注的数量
	 * @Description: 查询被关注的数量
	 * @date 2015年6月2日 下午4:22:24 
	 * @param @param target_tuid
	 * @param @return 
	 */
	def attention_num(int target_tuid){
		Long c = mainMongo.getCollection("attention").count($$("target_tuid" : target_tuid));
		return c;
	}
	
	/**
	 * 查询教师详细信息-pic
	 * @Description: 查询教师详细信息-pic 
	 * @date 2015年6月2日 下午4:28:53 
	 * @param @param _id
	 * @param @return 
	 */
	def teach_msg(int _id){
		return mainMongo.getCollection("users").findOne($$("_id" : _id), $$("pic" : 1 , "user_industry" : 1 , "nick_name" : 1));
	}
	
	
	public String tipToString(def user_industryList){
		String head = "擅长领域：";
		return tipToString(head, user_industryList);
	}
	public String tipToString(String head, def user_industryList){
		StringBuffer tipStr = new StringBuffer(head);

		if(user_industryList){
			user_industryList.each { def industry ->
				def tipList = industry["users_industry_tip"];
				if(tipList){
					tipList.each {def tip ->
						tipStr.append(tip_content().findOne($$("_id" : tip["industry_tip_id"]) , $$("tip_name" : 1))?.get("tip_name") + "、");
					}
				}
			}
		}
		return tipStr.length() > head.length() ? tipStr.substring(0, tipStr.length() - 1) : head;
	}
}

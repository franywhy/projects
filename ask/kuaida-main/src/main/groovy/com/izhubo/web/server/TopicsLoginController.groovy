package com.izhubo.web.server

import com.izhubo.common.constant.Constant
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.*
import com.izhubo.model.node.*
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.topics.utils.TopicsDataUtils
import com.izhubo.utils.DataUtils
import com.izhubo.utils.RegExUtils
import com.izhubo.web.BaseController
import com.izhubo.web.SynTipThread
import com.izhubo.web.api.Web
import com.izhubo.web.currency.CurrencyController
import com.izhubo.web.mobile.TeacherController
import com.izhubo.web.msg.MsgController
import com.izhubo.web.vo.TopicReplyVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.regex.Matcher
import java.util.regex.Pattern

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
class TopicsLoginController extends BaseController {

	private Lock lock = new ReentrantLock();

	private static Logger logger = LoggerFactory.getLogger(TopicsLoginController.class);

	private DBCollection topics(){
		return mainMongo.getCollection("topics");
	}
	private DBCollection topic_tip(){
		return mainMongo.getCollection("topic_tip");
	}
	private DBCollection topics_reply(){
		return mainMongo.getCollection("topics_reply");
	}
	private DBCollection attention(){
		return mainMongo.getCollection("attention");
	}
	private DBCollection industry(){
		return mainMongo.getCollection("industry");
	}
	private DBCollection collection(){
		return mainMongo.getCollection("collection");
	}
	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}

	private DBCollection tip_type(){
		return mainMongo.getCollection("tip_type");
	}
	private DBCollection topic_resubmit_log(){
		return logMongo.getCollection("topic_resubmit_log");
	}

	private DBCollection fuzzy_query_record(){
		return mainMongo.getCollection("fuzzy_query_record");
	}

	private static final String TEST_USER_ID_ARRAY = "10053435,10010277";

	private static final String FILE_HEADER = "A_";
	private static final int USER_ID_TO_DIR_NUM = 5;
	private static final Long ONE_DAY_SECONDS = 1 * 24 * 3600L;
	private static final Long ONE_HOUR_SECONDS = 1 * 3600L;

	private static final String LOCK_KEY = "topicLockKey"

	@Resource
	private MsgController msgController;
	@Resource
	private EarningsController earningsController;

	@Resource
	private TeacherController teacherController;
	@Resource
	private CurrencyController currencyController;


	private Lock evaluation_lock = new ReentrantLock();

	//初始化文件保存位置
	File topic_file_folder;
	@Value("#{application['topic.file.folder']}")
	void setVideoFolder(String folder){
		topic_file_folder = new File(folder);
		topic_file_folder.mkdirs();
		//		println "初始化-提问-图片存放路径: ${folder}";
	}
	//初始化文件服务器地址
	String file_url ;
	@Value("#{application['pic.domain']}")
	void setFileUrl(String file_url){
		this.file_url = file_url;
	}

	//红包图片地址
	String red_url ;
	@Value("#{application['red.url']}")
	void setRedUrl(String red_url){
		this.red_url = red_url;
	}

	/** 根据学员获取课程 */
	@Value("#{application['learning.api.domain']}")
	private String LEARNING_API_DOMAIN = null;

	def con(){
		return getResultOK(red_url);
	}

	/**
	 * 待抢答问题的剩余抢答时间
	 * @return  code : 1 , data : {"topic_id" : topic_id , "type" : 0.待抢答 1.抢答失败 2.抢答成功 3.问题已结束 , "surplus_time" : 剩余毫秒数  Long类型 , "full_time" : 提问待抢答的最大毫秒数 Long类型}
	 * type = 1的时候  surplus_time>0  否则 surplus_time = 0
	 */
	def topic_times(HttpServletRequest request){
		//问题ID
		String topic_id = request["topic_id"];
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if(StringUtils.isNotBlank(topic_id)){
			def topic = topics().findOne($$("_id" : topic_id , "author_id" : user_id));
			if(topic){
				Integer type = topic["type"] as Integer;
				Integer topicEndType = topic["type"] as Integer;
                int product = (topic["product"] == null? 0:topic["product"]) as int
				Long surplus_time = 0L;
				//全部时间
                Long full_time
                if(0 == product) { //会计
                    full_time = KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME
                } else if(1 == product) { //自考
                    full_time = KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME_ZIKAO
                }
				//Long full_time = KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME * 3600000;
				if(type == TopicsType.待抢答.ordinal()){
					Long update_at = topic["update_at"] as Long;
					surplus_time = full_time + update_at - System.currentTimeMillis();
					Map map = new HashMap();
					map["topic_id"] = topic_id;
					map["type"] = type;
					map["surplus_time"] = surplus_time;
					map["full_time"] = full_time;
					return ["code" : 1 , "data" : map];
				}
				else
				{
					return ["code" : 3000+ type , "data" : ""];
				}
			}

		}
		return getResultParamsError();
		//		还剩多长时间超时    超时时常
	}

	/**
	 * 用户关注标签的问题列表v310
	 */
	//	@ResponseBody
	//	@RequestMapping(value = "tips_topic_listv310/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "用户关注标签的问题列表v310" ,  notes = "用户关注标签的问题列表v310")
	def tips_topic_list_v310(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		//用户id
		Integer user_id = Web.getCurrentUserId();
		String tip = request["tip"];
		if(StringUtils.isNotBlank(tip)){
			//推荐问题列表
			def topicList;
			/* 只获取最新的
			 def refresh_time;
			 for(int i = 0;i<tip_contents.size();i++){
			 String t = tip_contents.get(i)["_id"];
			 if(t.equals(tip)){
			 refresh_time = (long)tip_contents.get(i)["refresh_time"];
			 break;
			 }
			 }*/
			/*if(refresh_time==null){
			 refresh_time = 0;
			 }*/
			// 更新刷新时间
			def tipContent = tip_content().findOne($$("_id" : Integer.valueOf(tip)) , $$("_id" : 1 , "tip_name" : 1));
			def userTipList = (List)(users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents"));
			if(userTipList){
				for(int i = 0;i<userTipList.size();i++){
					if(userTipList.get(i)["_id"] == tipContent["_id"]){
						userTipList.get(i)["refresh_time"] = System.currentTimeMillis();
						users().update($$("_id" : user_id) , $$('$set' : $$("tip_contents" : userTipList)));
						break;
					}
				}
			}
			// 更新刷新时间

			topicList = topics().find(
					$$(
					"type" : TopicsType.问题已结束.ordinal() , "evaluation_type":  $$('$in' :[
						TopicEvaluationType.满意.ordinal() ,
						TopicEvaluationType.很满意.ordinal()
					]) ,
					"deleted" : false ,"tips._id" :Integer.valueOf(tip),
					"author_id" :$$('$nin' : [user_id]),
					/*"update_at" :$$('$gt': refresh_time, '$lt': System.currentTimeMillis())*/
					),
					$$("_id" : 1 , "author_id" : 1 , "content" : 1 , "timestamp" : 1 , "da_shang" : 1,"read_num":1,"teach_id":1)
					).sort($$("race_time" : -1)).skip((page - 1) * size).limit(size)?.toArray();
			topicList.each { def dbo ->
				dbo["collection_size"] = collection().find(
						$$("topics_id" : dbo["_id"])
						).size();
				if(dbo["da_shang"] == null){
					dbo["da_shang"] = 0;
				}
				if(dbo["read_num"] == null){
					dbo["read_num"] = 0;
				}
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);
				def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1));
				if(teacherMap){
					dbo["pic"] = teacherMap["pic"];
				}
			}
			return getResultOK(topicList);
		}else{
			return getResult(Code.学员未提交标签, Code.学员未提交标签_S);
		}

	}


	/**
	 * 用户关注标签的问题列表
	 */
	//	@ResponseBody
	//	@RequestMapping(value = "tips_topic_list/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "用户关注标签的问题列表" ,  notes = "用户关注标签的问题列表")
	def tips_topic_list(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户关注的标签
		def user_tip_contents = users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents");
		if(user_tip_contents){
			//查询条件拼接 in
			List tids = new ArrayList();
			user_tip_contents.each {def row->
				tids.add(row["_id"]);
			}
			//推荐问题列表
			def topicList = topics().find(
					$$(
					"type" : TopicsType.问题已结束.ordinal() , "evaluation_type":  $$('$in' :[
						TopicEvaluationType.满意.ordinal() ,
						TopicEvaluationType.很满意.ordinal()
					]) ,
					"deleted" : false ,"tips._id" : $$('$in' : tids),
					"author_id" :$$('$nin' : [user_id])
					),
					$$("_id" : 1 , "author_id" : 1 , "content" : 1 , "timestamp" : 1)
					).limit(10).sort($$("race_time" : -1)).toArray();
			//教师图片
			if(topicList){
				topicList.each {def row->
					row["pic"] = users().findOne($$("_id" : row["author_id"]) , $$("pic" : 1))?.get("pic");

					row["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)row["timestamp"]);
				}
			}

			def data = ["user_tip_contents" : user_tip_contents , "topicList" : topicList];
			return getResultOK(data);
		}
		return getResult(Code.学员未设置关注标签, Code.学员未设置关注标签_S);
	}

	/**
	 * 打开红包
	 * @Description: 打开红包
	 * @date 2015年7月31日 上午10:00:15 
	 */
	//	@ResponseBody
	//	@RequestMapping(value = "openRed/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "打开红包" ,  notes = "打开红包")
	def openRed(HttpServletRequest request){
		String topic_id = request["topic_id"];

		return _openRed(topic_id);
	}

	//批量打开红包
	//	def bath_open_bunus(HttpServletRequest request){
	//		String a_key_b = request["a_key_b"];
	//		if("d1aef200-b353-401d-959d-41837d7f9120asdfasf".equals(a_key_b) && System.currentTimeMillis() < 1467129600000L){
	//			def topicList = topic_bunus().find($$("open_type":TopicBunuOpenType.未打开.ordinal())).sort($$("create_time" : 1)).toArray();
	//			if(topicList){
	//				topicList.each {def bun->
	//					String topic_id = bun["topic_id"];
	//					if(StringUtils.isNotBlank(topic_id)){
	//						openRedTimeOut("a:b:" + topic_id);
	//					}
	//				}
	//			}
	//		}
	//		return getResultOK();
	//	}





	/**
	 * 红包超时打开
	 * @param key
	 * @return
	 */
	def openRedTimeOut(String key){

		String topic_id = key.split(':')[2];

		if(StringUtils.isNotBlank(topic_id)){
			Integer teach_id = topics().findOne($$("_id" : topic_id ))?.get("teach_id") as Integer;
			if(teach_id != null){
				def bunu = earningsController.openRed(topic_id, teach_id, TopicBunuOpenType.超时开启.ordinal());
				if(bunu){
					//修改提问中红包开启状态
					topics().update(
							$$("_id" : topic_id),
							$$($set:$$("buns_states" : TopicBunsStates.已打开.ordinal() , "teacher_show_type" : TopicTeacherShowType.金额.ordinal()))
							);
					//发送聊天消息
					Double mmoney = bunu["mmoney"] as Double;
					addRedMoneyReply(teach_id , topic_id , mmoney);
				}
			}
		}
		return true;
	}


	def _openRed(String topic_id){
		if(StringUtils.isNotBlank(topic_id)){
			int user_id = Web.currentUserId;
			def bunu = earningsController.openRed(topic_id, user_id , TopicBunuOpenType.教师打开.ordinal());
			Map map = new HashMap();
			if(bunu){
				//修改提问中红包开启状态
				topics().update(
						$$("_id" : topic_id),
						$$($set:$$("buns_states" : TopicBunsStates.已打开.ordinal() , "teacher_show_type" : TopicTeacherShowType.无.ordinal()))
						);
				//发送聊天消息
				Double mmoney = bunu["mmoney"] as Double;
				addRedMoneyReply(user_id , topic_id , mmoney);

				map["money"] = mmoney;
				map["template"] = bunu["mtemplate"];

			}else {
				//					return getResult(Code.红包已打开, Code.红包已打开_S , null);
				def bunsInfo = earningsController.get_topic_bunus_info(topic_id, user_id );
				if(bunsInfo){
					map["money"] = bunsInfo["mmoney"];
					map["template"] = bunsInfo["mtemplate"];

					//修改提问中红包开启状态
					topics().update(
							$$("_id" : topic_id),
							$$($set:$$( "teacher_show_type" : TopicTeacherShowType.无.ordinal()))
							);
				}
			}
			return getResultOKS(map);
		}

		return getResultParamsError();
	}

	/**
	 * 打开红包后的消息提醒
	 * @Description: 打开红包后的消息提醒
	 * @date 2015年8月18日 上午11:20:17 
	 * @param @param teach_id
	 * @param @param topic_id
	 * @param @param money
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def addRedMoneyReply(int teach_id ,String topic_id , float money){
		long now = System.currentTimeMillis();
		//消息
		def rep = $$(
				"_id" : UUID.randomUUID().toString() , "reply_content" : "恭喜您获得了"+money+"元会答红包，继续抢答吧！" , "reply_time" : now ,
				"reply_type" : ReplyType.系统文字.ordinal() , "topic_id" : topic_id , "user_id" : 1 , "timestamp" : now , "user_pic" : null,
				"show_type" : ReplyShowType.老师.ordinal()
				);
		//保存消息
		topics_reply().save(rep);

		//发送红包消息
		TopicChatMsg msg = new TopicChatMsg();
		Chat chat = new Chat();
		chat.set_id(rep["_id"]);
		chat.setReply_content(rep["reply_content"]);
		chat.setReply_time(now);
		chat.setTimestamp(now);
		chat.setTopic_id(topic_id);
		chat.setUser_id(rep["user_id"]);
		chat.setReply_type(rep["reply_type"]);

		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);

		msg.setChat(chat);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(teach_id) );
		msgController.SendMsg(tousers, msg);
	}


	/**
	 * 判断学员问题是否被抢答成功
	 * @Description: 判断学员问题是否被抢答成功 
	 * @date 2015年7月9日 下午3:10:55 
	 * @param @param request[topic_id] 问题id
	 * @return 0 未被抢答 1 抢答成功 2 问题已过期失效
	 */
	//	@ResponseBody
	//	@RequestMapping(value = "topicState/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "判断学员问题是否被抢答成功" ,  notes = "判断学员问题是否被抢答成功 [返回值  0 未被抢答 1 抢答成功 2 问题已过期失效]")
	//	@ApiImplicitParams([
	//		@ApiImplicitParam(name = "topic_id", value = "问题id", required = true, dataType = "String", paramType = "query")
	//	])
	def topicState(HttpServletRequest request){
		String topic_id = request["topic_id"];
		if(StringUtils.isNotBlank(topic_id)){
			//			def valOp = mainRedis.opsForValue();
			String key = KeyUtils.TOPICES.tpicesIndustrys(topic_id);
			if(mainRedis.hasKey(key)){//问题还在redis中 未被抢答
				return getResultOKS(0);
			}else{
				long lc = topics().count($$("_id" : topic_id));
				if(lc > 0){//问题已经在mongodb中 已被抢答成功
					return getResultOKS(1);
				}else{//问题不在mongodb中 问题已过期失效
					return getResultOKS(2);
				}
			}
		}
		return getResultParamsError();
	}

	//收藏课程
	//	@ResponseBody
	//	@RequestMapping(value = "collection_topic/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "收藏课程" ,  notes = "收藏课程")
	//	@ApiImplicitParams([
	//		@ApiImplicitParam(name = "topic_id", value = "问题id", required = true, dataType = "String", paramType = "query")
	//	])
	def collection_topic(HttpServletRequest request){
		String topic_id = request["topic_id"];
		Integer tuid = Web.getCurrentUserId();
		if(StringUtils.isNotBlank(topic_id)){
			if(isCollection(topic_id , tuid)){
				collection().save(
						$$(
						"_id":UUID.randomUUID().toString() , "timestamp" : System.currentTimeMillis() ,
						"topics_id" : topic_id , "tuid" : tuid
						)
						);
			}else{
				return getResult(Code.问题已收藏, Code.问题已收藏_S, Code.问题已收藏_S)
			}
			return getResultOKS();
		}
		return getResultParamsError();
	}

	/**
	 * 判断用户是否收藏过课程
	 * @Description: 判断用户是否收藏过课程
	 * @date 2015年10月12日 下午3:01:22 
	 * @param @param topic_id
	 * @param @param user_id
	 * @param @return 
	 * @throws
	 */	
	private boolean isCollection(String topic_id , int user_id){
		return collection().count($$("topics_id" : topic_id , "tuid" : user_id)) == 0;
	}

	//取消收藏课程
	def uncollection_topic(HttpServletRequest request){
		String topic_id = request["topic_id"];
		if(StringUtils.isNotBlank(topic_id)){
			collection().remove($$("topics_id" : topic_id , "tuid" : Web.getCurrentUserId()));
			return getResultOKS();
		}
		return getResultParamsError();
	}

	//关注用户
	//	@ResponseBody
	//	@RequestMapping(value = "attention_user/*-*" , produces = "application/json; charset=utf-8")
	//	@ApiOperation(value = "关注用户" ,  notes = "关注用户")
	//	@ApiImplicitParams([
	//		@ApiImplicitParam(name = "target_tuid", value = "要关注用户的id", required = true, dataType = "String", paramType = "query")
	//	])
	def attention_user(HttpServletRequest request){
		Integer target_tuid = ServletRequestUtils.getIntParameter(request , "target_tuid");
		Integer source_tuid = Web.getCurrentUserId();
		if(target_tuid != null && !isAttention(source_tuid , target_tuid)){
			attention().save(
					$$(
					"_id" : target_tuid+"_"+source_tuid , "source_tuid" : source_tuid ,
					"target_tuid" : target_tuid , "timestamp" : System.currentTimeMillis()
					)
					);
			return getResultOKS();
		}
		return getResultParamsError();
	}

	//取消关注
	def unattention_user(HttpServletRequest request){
		//关注id
		Integer target_tuid = ServletRequestUtils.getIntParameter(request , "target_tuid");
		if(target_tuid != null){
			attention().remove($$("target_tuid" : target_tuid , "source_tuid" : Web.getCurrentUserId()));
			return getResultOKS();
		}
		return getResultParamsError();
	}

	/**
	 * 是否已经关注过
	 * @Description: 是否已经关注过
	 * @date 2015年10月12日 下午2:57:17 
	 * @param @param source_tuid 用户id
	 * @param @param target_tuid 被关注用户id
	 * @param @return 
	 */
	private boolean isAttention(int source_tuid , int target_tuid){
		return attention().count($$("target_tuid" : target_tuid , "source_tuid" :source_tuid))>0;
	}

	//我的抢答列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicReplyList(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("teach_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"author_id" : 1 , "content" : 1 , "tips" : 1 ,
				"evaluation" : 1 ,"evaluation_type" : 1, "type" : 1 , "timestamp" : 1 , "buns_states" : 1 , "update_at" : 1
				)
				).sort($$("type" : 1  ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				def user = users().findOne($$("_id" : dbo["author_id"]) , $$("_id" : 1 , "pic" : 1 ));
				//头像
				dbo["pic"] = user["pic"]?.toString();
				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);
				dbo["tips"] = "关键词：" + dbo["tips"]?.get(0)["tip_name"];

				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");
				//V200开始 评价类型加入[非常满意] 对应到旧版本 [满意]
				dbo["evaluation_type"] = TopicEvaluationType.oldTypeInt(dbo["evaluation_type"]);
			}
		}
		return getResultOKS(topicsList);
	}



	//我的抢答列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicReplyList_android_v150(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("teach_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"author_id" : 1 , "content" : 1 , "tips" : 1 ,
				"evaluation" : 1 ,"evaluation_type" : 1, "type" : 1 , "timestamp" : 1 , "buns_states" : 1 , "update_at" : 1
				)
				).sort($$("type" : 1  ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				def user = users().findOne($$("_id" : dbo["author_id"]) , $$("_id" : 1 , "pic" : 1 , "vlevel" : 1 ));
				//头像
				dbo["pic"] = user["pic"]?.toString();
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);
				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["tips"] = "关键词：" + dbo["tips"]?.get(0)["tip_name"];

				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//vlevle
				dbo["s_vip_icon"] = VlevelType.vipIcon(user["vlevel"]);

				//V200开始 评价类型加入[非常满意] 对应到旧版本 [满意]
				dbo["evaluation_type"] = TopicEvaluationType.oldTypeInt(dbo["evaluation_type"]);
			}
		}
		return getResultOKS(topicsList);
	}

	//我的抢答列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicReplyList_v200(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("teach_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"author_id" : 1 , "teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 ,"evaluation_type" : 1, "type" : 1 , "timestamp" : 1 , "buns_states" : 1 , "update_at" : 1,
				"tip_kd" : 1, "da_shang" : 1
				)
				).sort($$("type" : 1  ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				def user = users().findOne($$("_id" : dbo["author_id"]) , $$("_id" : 1 , "pic" : 1 , "vlevel" : 1 ));
				dbo["teacher_name"] = user["nick_name"];
				//头像
				dbo["pic"] = user["pic"]?.toString();
				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);
				dbo["tips"] = dbo["tips"]?.get(0)["tip_name"];

				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//vlevle
				dbo["s_vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
				dbo["vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
				//是否有打赏  0.没有 1.有打赏
				Integer tip_kd_type = 0;
				if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
					tip_kd_type = 1;
				}
				dbo["tip_kd_type"] = tip_kd_type;
				Integer can_del = 0;
				if(dbo["type"]){
					if(dbo["type"]==1){
						can_del = 1
					}else{
						can_del = 0
					}
				}
				dbo["can_del"] = can_del;
			}
		}
		return getResultOKS(topicsList);
	}


	//我的提问列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicList(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("author_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1
				)
				).sort($$("type" : 1, "update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
		//	).sort($$("type" : 1, "evaluation" : 1 ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 ))?.get("pic");

				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

				def tips = dbo["tips"];
				if(tips){
					dbo["tips"] = "关键词：" + tips.get(0)["tip_name"];
				}else{
					dbo["tips"] = "";
				}

				//过滤掉换行符
				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//V200开始 评价类型加入[非常满意] 对应到旧版本 [满意]
				dbo["evaluation_type"] = TopicEvaluationType.oldTypeInt(dbo["evaluation_type"]);
			}
		}
		return getResultOKS(topicsList);
	}

	//我的提问列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicList_android_v150(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("author_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1
				)
				).sort($$("type" : 1, "evaluation" : 1 ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				//				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1))?.get("pic");

				//add by shihongjie 2015-12-22 VIP_ICON
				def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1));
				if(teacherMap){
					dbo["pic"] = teacherMap["pic"];
					//vlevle
					dbo["vip_icon"] = VlevelType.vipIcon(teacherMap["vlevel"]);
				}else{
					dbo["vip_icon"] = false;
				}

				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

				def tips = dbo["tips"];
				if(tips){
					dbo["tips"] = "关键词：" + tips.get(0)["tip_name"];
				}else{
					dbo["tips"] = "";
				}

				//过滤掉换行符
				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");
				//V200开始 评价类型加入[非常满意] 对应到旧版本 [满意]
				dbo["evaluation_type"] = TopicEvaluationType.oldTypeInt(dbo["evaluation_type"]);
			}
		}
		return getResultOKS(topicsList);
	}

	//我的提问列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicList_v200(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		●
		def topicsList = topics().find(
				$$("author_id" : user_id , "deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
				"tip_kd" : 1,"da_shang" : 1
				)
				).sort($$("type" : 1, "evaluation" : 1 ,"update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				//				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1))?.get("pic");

				//add by shihongjie 2015-12-22 VIP_ICON
				def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1));
				if(teacherMap){
					dbo["pic"] = teacherMap["pic"];
					//vlevle
					dbo["vip_icon"] = VlevelType.vipIcon(teacherMap["vlevel"]);
				}else{
					dbo["vip_icon"] = false;
				}

				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

				def tips = dbo["tips"];
				if(tips){
					dbo["tips"] = "关键词：" + tips.get(0)["tip_name"];
				}else{
					dbo["tips"] = "";
				}

				//过滤掉换行符
				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//是否有打赏  0.没有 1.有打赏
				Integer tip_kd_type = 0;
				if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
					tip_kd_type = 1;
				}
				dbo["tip_kd_type"] = tip_kd_type;

				//是否是直接打赏提交的问题  0.不是 1.是
				Integer da_shang = 0;
				if(null != dbo["da_shang"] && dbo["da_shang"] == 1){
					da_shang = 1;
				}
				dbo["da_shang"] = da_shang;
			}
		}
		return getResultOKS(topicsList);
	}

	private static final SimpleDateFormat sdf= new SimpleDateFormat("yy/MM/dd HH:mm");
	private String dateFormate(long timestamp){
		return sdf.format(new Date(timestamp));
	}
	//我的提问列表  草稿
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicList_v310(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		//用户id
		Integer user_id = Web.getCurrentUserId();
		def topicsList = topics().find(
				$$("author_id" : user_id ,"product":$$('$ne' : 1),"deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
				"tip_kd" : 1,"da_shang" : 1
				)
				).sort($$("update_at" : -1 , "timestamp" : -1))?.toArray();

		if(topicsList){
			int px = 0;
			for(int i =0;i<topicsList.size;i++){
				if(topicsList.get(i)["type"] == 2){
					def dbo = topicsList.get(i);
					topicsList.remove(i);
					topicsList.add(px, dbo)
					px++;
				}
				if(topicsList.get(i)["type"] == 3){
					break;
				}
			}
			int allSize = topicsList.size();
			if(allSize>(size*page)){
				topicsList = topicsList.subList((page-1)*size, size*page);
			}else if(allSize>(size*(page-1))){
				topicsList = topicsList.subList((page-1)*size, topicsList.size());
			}else{
				topicsList.clear();
			}
		}
		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				//				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1))?.get("pic");

				//add by shihongjie 2015-12-22 VIP_ICON
				def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1 , "nick_name" : 1));
				if(teacherMap){
					dbo["pic"] = teacherMap["pic"];
					//vlevle
					dbo["vip_icon"] = VlevelType.vipIcon(teacherMap["vlevel"]);
					dbo["teacher_name"] = teacherMap["nick_name"];
				}else{
					dbo["vip_icon"] = false;
					dbo["teacher_name"] = "等待老师抢答...";
					if(dbo["type"]){
						if(dbo["type"]==1){
							dbo["teacher_name"] = "暂无老师抢答";
						}
					}
				}

				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

				def tips = dbo["tips"];
				if(tips){
					dbo["tips"] = tips.get(0)["tip_name"];
				}else{
					dbo["tips"] = "";
				}

				//过滤掉换行符
				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//是否有打赏  0.没有 1.有打赏
				Integer tip_kd_type = 0;
				if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
					tip_kd_type = 1;
				}
				dbo["tip_kd_type"] = tip_kd_type;

				//是否是直接打赏提交的问题  0.不是 1.是
				Integer da_shang = 0;
				if(null != dbo["da_shang"] && dbo["da_shang"] == 1){
					da_shang = 1;
				}
				dbo["da_shang"] = da_shang;
				Integer can_del = 0;
				if(dbo["type"]){
					if(dbo["type"]==1){
						can_del = 1
					}else{
						can_del = 0
					}
				}
				dbo["can_del"] = can_del;
			}
		}
		return getResultOKS(topicsList);
	}

	/*//我的提问列表  草稿
	 @TypeChecked(TypeCheckingMode.SKIP)
	 def myTopicList_v310(HttpServletRequest request){
	 int size = ServletRequestUtils.getIntParameter(request, "size", 20);
	 int page = ServletRequestUtils.getIntParameter(request, "page", 1);
	 //用户id
	 Integer user_id = Web.getCurrentUserId();
	 def topicsList = topics().find(
	 $$("author_id" : user_id , "deleted" : false,"type":-1),
	 $$(
	 "_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
	 "evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
	 "tip_kd" : 1,"da_shang" : 1
	 )
	 ).toArray();
	 //通话中的问题
	 def topicsCallList = topics().find(
	 $$("author_id" : user_id , "deleted" : false,"type":2),
	 $$(
	 "_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
	 "evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
	 "tip_kd" : 1,"da_shang" : 1
	 )
	 ).sort($$("update_at" : -1 , "timestamp" : -1)).toArray();
	 //待抢答,超时,没评价的问题
	 def topicsOtherList = topics().find(
	 $$("author_id" : user_id , "deleted" : false,"type":$$('$ne' : 2),"evaluation_type":0),
	 $$(
	 "_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
	 "evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
	 "tip_kd" : 1,"da_shang" : 1
	 )
	 ).sort($$("type" : 1, "evaluation" : 1,"update_at" : -1 , "timestamp" : -1)).toArray();
	 //完成的问题，除了未评价的
	 def topicsOtherOKList = topics().find(
	 $$("author_id" : user_id , "deleted" : false ,"evaluation_type":$$('$ne' : 0)),
	 $$(
	 "_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
	 "evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
	 "tip_kd" : 1,"da_shang" : 1
	 )
	 ).sort($$("type" : 1, "evaluation" : 1,"update_at" : -1 , "timestamp" : -1)).toArray();
	 topicsList.addAll(topicsCallList);
	 topicsList.addAll(topicsOtherList);
	 int fromIndex = (page - 1) * size;
	 if(fromIndex >= topicsList.size){
	 topicsList = topicsList.clear();
	 }else{
	 int toIndex;
	 if(page * size<topicsList.size){
	 toIndex = page * size;
	 }else{
	 toIndex = topicsList.size;
	 }
	 topicsList = topicsList.subList(fromIndex, toIndex);
	 }
	 if(topicsList){
	 topicsList.each { def dbo ->
	 //头像
	 //				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1))?.get("pic");
	 //add by shihongjie 2015-12-22 VIP_ICON
	 def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1));
	 if(teacherMap){
	 dbo["pic"] = teacherMap["pic"];
	 }else{
	 }
	 dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);
	 def tips = dbo["tips"];
	 if(tips){
	 dbo["tips"] = "关键词：" + tips.get(0)["tip_name"];
	 }else{
	 dbo["tips"] = "";
	 }
	 //过滤掉换行符
	 dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");
	 //是否有打赏  0.没有 1.有打赏
	 Integer tip_kd_type = 0;
	 if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
	 tip_kd_type = 1;
	 }
	 dbo["tip_kd_type"] = tip_kd_type;
	 //是否是直接打赏提交的问题  0.不是 1.是
	 Integer da_shang = 0;
	 if(null != dbo["da_shang"] && dbo["da_shang"] == 1){
	 da_shang = 1;
	 }
	 dbo["da_shang"] = da_shang;
	 }
	 }
	 return getResultOKS(topicsList);
	 }
	 */


	/**
	 * 会答1.5.1 学员发起结束接口
	 * @param topic_id 问题id
	 * @param user_id 教师id
	 *
	 * add by shihongjie
	 * 2016-1-12
	 */
	def student_end_topic(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//不满意-标签
		String eval_contents = request["eval_contents"];
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//评分 0 不满意 1满意
		Integer score = ServletRequestUtils.getIntParameter(request, "score" , 1);

		//根据提问id和教师id查询问题
		def topic = topics().findOne(
				$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() ,  "author_id" : user_id),
				$$("author_id" : 1 , "teach_id" : 1)
				)
		Long now =  System.currentTimeMillis();
		Integer to_user = null;
		if(topic){
			to_user = (Integer) topic["teach_id"];
			//更新
			topics().update(
					$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() , "author_id" : user_id),
					$$( $set : $$("type" : TopicsType.问题已结束.ordinal() , "end_user" : user_id , "update_at" : now , "end_type" : TopicEndType.学生结束.ordinal()))
					);
		}

		//通知教师端
		student_end_topic_node_teacher(topic_id, now, to_user);

		//评价
		evaluate_topic_v200(topic_id , score , now , eval_contents);
		//		evaluate_topic_v151(topic_id , score , now);

		Map result = new HashMap();
		result["topic_id"] = topic_id;
		result["timestamp"] = now;


		return getResultOKS(result);
	}

	/**
	 * 会答1.5.1 学生发起结束后 node通知教师
	 * @param topic_id 问题id
	 * @param now 时间
	 * @param to_user 接受人id
	 *
	 * add by shihongjie
	 * 2016-1-12
	 */
	def student_end_topic_node_teacher(String topic_id , Long now , Integer to_user){
		ChatBaseMsg msg = new ChatBaseMsg();
		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_CHAT_END);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(to_user) );
		msgController.SendMsg(tousers, msg);
	}

	/**
	 * 学员评价成功 通知教师端
	 * @param topic_id 问题id
	 * @param now 时间
	 * @param to_user 接受人id
	 *
	 * add by shihongjie
	 * 2016-06-08
	 */
	def student_evaluation_end(String topic_id , Long now , Integer to_user){
		ChatBaseMsg msg = new ChatBaseMsg();
		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_EVALUATION);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(to_user) );
		msgController.SendMsg(tousers, msg);
	}

	/**
	 * 会答2.00  学生发起结束后评价课程
	 * @param topic_id 问题id
	 * @param now 时间
	 * @param score 评分 0.不满意 1.满意 2.非常满意
	 *
	 * add by shihongjie
	 * 2016-1-12
	 */
	def evaluate_topic_v200(String topic_id , Integer score , Long now , String eval_contents){
		if(StringUtils.isNotBlank(topic_id)){
			//用户id
			Integer user_id = Web.getCurrentUserId();

			//更新
			evaluation_lock.lock();
			//红包显示界面
			Integer teacher_show_type = TopicTeacherShowType.undefined.ordinal();
			//兼容老版本:非常满意->满意
			Integer evaluation_type = TopicEvaluationType.不满意.ordinal();
			if(score == ScoreType.满意.ordinal()){
				evaluation_type = TopicEvaluationType.满意.ordinal();

				teacher_show_type = TopicTeacherShowType.红包.ordinal();
			}else if(score == ScoreType.很满意.ordinal()){
				evaluation_type = TopicEvaluationType.很满意.ordinal();

				teacher_show_type = TopicTeacherShowType.红包.ordinal();
			}

			//兼容老版本:非常满意->满意
			//			Integer score_old = score;
			//			if(score == ScoreType.非常满意.ordinal()){
			//				score_old = ScoreType.满意.ordinal();
			//			}

			try {
				//				def evaluation = $$("evaluation_id" : UUID.randomUUID().toString() , "score": score_old , "timestamp" : now);
				def unevaluation_topic = topics().findOne($$("_id" : topic_id , "author_id" : user_id , "evaluation_type" : TopicEvaluationType.未评价.ordinal()));
				if(unevaluation_topic){
					//更新问题状态
					if(
					topics().update(
					$$("_id" : topic_id , "author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : TopicEvaluationType.未评价.ordinal()),
					$$($set :$$("evaluation_type" : evaluation_type,"teacher_show_type" : teacher_show_type)
					)
					).getN()>0
					){

						//教师id
						Integer teach_id = topics().findOne($$("_id" : topic_id) , $$("teach_id" : 1))?.get("teach_id") as Integer;

						//发送评价内容
						topicReplyEvaluation(topic_id, score, teach_id , eval_contents);
						//发送消息通知教师端评价结束
						student_evaluation_end(topic_id , now ,teach_id);

						if(ScoreType.满意.ordinal() == score || ScoreType.很满意.ordinal() == score){
							//校验是否作弊
							if(!isSameDeviceId(topic_id)){
								/**  *******************************抽取红包 *********************************/
								def bunu = earningsController.lottery_v200(topic_id , teach_id , score);
								//抽取红包成功后发送红包消息
								if(bunu != null){
									//红包消息回复
									addRedReply(topic_id , teach_id );

									//保存redis消息
									saveRedisForBunus(topic_id , teach_id);
								}

								/**  ******************************* 维护 users老师满意数量 *********************************/
								//维护老师抢答数量
								users().update(
										$$("_id" : teach_id),
										$$('$inc':$$("topic_evaluation_count" : 1))
										);


								//教师打赏金额到账
								Double tip_kd = (Double)unevaluation_topic.get("tip_kd");
								//学员打赏金额
								Double tip_kd_full = (Double)unevaluation_topic.get("tip_kd_full");
								if(null != tip_kd && tip_kd > 0 ){
									if(tip_kd_full == null){
										tip_kd_full = tip_kd;
									}
									//打赏到账
									boolean tipsSuc = earningsController.tipsAdd(teach_id, tip_kd, topic_id, now);
									//打赏成功后发送消息提醒教师
									if(tipsSuc){
										topicReplyTipsSuccess(topic_id, tip_kd, teach_id);

										//更新打赏记录-记录接收打赏人ID
										topic_tip().update(
												$$("dr" : 0 , "topic_id" : topic_id , "from_user_id" : user_id),
												$$('$set' : $$("to_user_id" : teach_id))
												)
									}
								}

							}else{
								//发送作弊消息
								topicReplyCheat(topic_id , teach_id);
							}
						}
					}
					return getResultOKS();
					//					return getResultOKS(evaluation);
				}
			} catch (Exception e) {

				logger.error(e.toString());
				e.printStackTrace();
			}finally{
				//显示释放锁
				evaluation_lock.unlock();


				//评价课程后，删除评价超时键值

				String key = KeyUtils.TOPICES.topicsPJRemindKey(topic_id);
				chatRedis.delete(key);
				mainRedis.delete(key);
				//mainRedis.opsForValue().(key, "", KeyUtils.TOPICES.TOPIC_PJ_REMIND_LIMIT_TIME , TimeUnit.HOURS);
			}

		}
		return getResultParamsError();
	}

	/**
	 * 保存在redis
	 * @Description: 保存在redis
	 * @date 2015年6月15日 下午5:13:55
	 * @param @param topic
	 * @param @return
	 * @throws
	 */
	def saveRedisForBunus(String topic_id , Integer teach_id){
		if(topic_id){
			String key = KeyUtils.BUNUS.bunusTimeOutKey(topic_id);
			String val = KeyUtils.BUNUS.bunusTimeOutVal(topic_id , teach_id);
			//红包打开
			mainRedis.opsForValue().set(key,val, KeyUtils.BUNUS.BUNUS_TIME , TimeUnit.HOURS);
			chatRedis.opsForValue().set(key,val, KeyUtils.BUNUS.BUNUS_TIME , TimeUnit.HOURS);
		}
	}


	/**
	 * 会答1.5.1 学生发起结束后评价课程
	 * @param topic_id 问题id
	 * @param now 时间
	 * @param score 评分 0 不满意 1满意
	 *
	 * add by shihongjie
	 * 2016-1-12
	 */
	/*	def evaluate_topic_v151(String topic_id , Integer score , Long now){
	 if(StringUtils.isNotBlank(topic_id)){
	 //用户id
	 Integer user_id = Web.getCurrentUserId();
	 //更新
	 evaluation_lock.lock();
	 try {
	 def evaluation = $$("evaluation_id" : UUID.randomUUID().toString() , "score": score , "timestamp" : now);
	 def unevaluation_topic = topics().find($$("_id" : topic_id , "author_id" : user_id , "evaluation_type" : TopicEvaluationType.未评价.ordinal()));
	 if(unevaluation_topic){
	 //更新问题状态
	 topics().update(
	 $$("_id" : topic_id , "author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : TopicEvaluationType.未评价.ordinal()),
	 $$($set :
	 $$(
	 "evaluation" : evaluation ,
	 "evaluation_type" : (score == ScoreType.不满意.ordinal() ? TopicEvaluationType.不满意.ordinal() : TopicEvaluationType.满意.ordinal()))
	 )
	 );
	 int teach_id = topics().findOne($$("_id" : topic_id) , $$("teach_id" : 1))?.get("teach_id") as Integer;
	 // 评论成功的广播
	 try{
	 nodeChatEvaluation(score, teach_id, now, topic_id);
	 }catch (Exception e){
	 e.printStackTrace();
	 }
	 //发送评价内容
	 topicReplyEvaluation(topic_id, score, teach_id);
	 if(ScoreType.满意.ordinal() == score){
	 //校验是否作弊
	 if(!isSameDeviceId(topic_id)){
	 *//**  *******************************抽取红包 *********************************//*
	 Bunus bunu = (Bunus)earningsController.lottery(topic_id , teach_id);
	 //抽取红包成功后发送红包消息
	 if(bunu != null){
	 addRedReply(topic_id , teach_id );
	 }
	 *//**  ******************************* 维护 users老师满意数量 *********************************//*
	 users().update(
	 $$("_id" : teach_id),
	 $$('$inc':$$("topic_evaluation_count" : 1))
	 );
	 }else{
	 //发送作弊消息
	 topicReplyCheat(topic_id , teach_id );
	 }
	 }
	 return getResultOKS(evaluation);
	 }
	 } catch (Exception e) {
	 logger.error(e.toString());
	 e.printStackTrace();
	 }finally{
	 //显示释放锁
	 evaluation_lock.unlock();
	 }
	 }
	 return getResultParamsError();
	 }*/


	//评价课程
	def evaluate_topic(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//评分 0 不满意 1满意
		Integer score = ServletRequestUtils.getIntParameter(request, "score");
		String eval_contents = request["eval_contents"];

		Long now = System.currentTimeMillis();

		evaluate_topic_v200(topic_id, score, now, eval_contents)

		Map result = new HashMap();
		result["topic_id"] = topic_id;
		result["timestamp"] = now;



		return getResultOK(result);
	}



	// 评价成功后通知
	private void nodeChatEvaluation(int score, Integer teach_id , Long now , String topic_id) {
		// 消息token发送到服务端
		TopicChatEvalutaionMsg mb = new TopicChatEvalutaionMsg();
		Map userMap = Web.currentUser();
		if(userMap != null ){
			String user_id = userMap["_id"].toString();

			mb.setCreate_time(now);
			mb.setFrom_user(user_id);
			mb.setIs_confirm(true);
			List<String> userlist = new ArrayList<String>();
			userlist.add(teach_id+"");

			mb.setTo_user(userlist);
			mb.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_EVALUATION);


			mb.setTopic_id(topic_id);
			mb.setUser_id(Integer.valueOf(user_id));
			mb.setUser_name(userMap["nick_name"].toString());
			mb.setUser_pic(userMap["pic"].toString());
			mb.setScore(score);

			msgController.SendMsg(userlist, mb);

		}

	}


	/**
	 * 校验提问和抢答是否在同一台机器上
	 * @date 2015年9月2日 上午11:19:15 
	 * @param @param topic_id 课程ID
	 * @param @return 
	 * @throws
	 */
	private boolean isSameDeviceId(String topic_id){
		boolean isSame = false;
		def topic = topics().findOne($$("_id" : topic_id), $$("teach_device_id" : 1 , "author_device_id" : 1));
		if(topic){
			String teach_device_id = topic["teach_device_id"];
			String author_device_id = topic["author_device_id"];
			if(teach_device_id != null && author_device_id != null && teach_device_id.equals(author_device_id)){
				isSame = true;
			}
		}
		return isSame;
	}

	/**
	 * 增加红包记录
	 * @Description: 增加红包记录
	 * @date 2015年7月28日 上午9:18:33 
	 * @param @param topic_id
	 * @param @return 
	 * @throws
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def addRedReply(String topic_id , int teach_id){
		long now = System.currentTimeMillis();
		//红包结构
		def rep = $$(
				"_id" : UUID.randomUUID().toString() , "reply_content" : red_url , "reply_time" : now ,
				"reply_type" : ReplyType.红包.ordinal() , "topic_id" : topic_id , "user_id" : 1 , "timestamp" : now , "user_pic" : null,
				"show_type" : ReplyShowType.老师.ordinal()
				);
		/*		//红包结构
		 def rep = $$(
		 "_id" : UUID.randomUUID().toString() , "reply_content" : "恭喜您获得一个会答红包，拆开来看看吧！" , "reply_time" : now ,
		 "reply_type" : ReplyType.红包.ordinal() , "topic_id" : topic_id , "user_id" : 1 , "timestamp" : now , "user_pic" : null,
		 "show_type" : ReplyShowType.老师.ordinal()
		 );
		 */		//保存红包记录
		topics_reply().save(rep);

		//发送红包消息
		TopicChatMsg msg = new TopicChatMsg();
		Chat chat = new Chat();
		chat.set_id(rep["_id"]);
		chat.setReply_content(rep["reply_content"]);
		chat.setReply_time(now);
		chat.setTimestamp(now);
		chat.setTopic_id(topic_id);
		chat.setUser_id(rep["user_id"]);
		chat.setReply_type(TopicsReplyType.红包.ordinal());
		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);
		msg.setChat(chat);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_RED);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(teach_id));
		msgController.SendMsg(tousers, msg);
	}

	//问题发起结束
	def endTopic(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//用户id
		Integer user_id = Web.getCurrentUserId();

		def topic = topics().findOne(
				$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() , $or : [
					$$("author_id" : user_id) ,
					$$("teach_id" : user_id)
				]),
				$$("author_id" : 1 , "teach_id" : 1)
				)
		int end_type = -1;
		Long now =  System.currentTimeMillis();
		Integer to_user = null;
		if(topic){
			if(topic["author_id"] == user_id){
				to_user = (Integer) topic["teach_id"];
				end_type = TopicEndType.学生结束.ordinal();
			}else if(topic["teach_id"] == user_id){
				to_user = (Integer) topic["author_id"];
				end_type = TopicEndType.老师结束.ordinal();
			}
			//更新
			topics().update(
					$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() , $or : [
						$$("author_id" : user_id) ,
						$$("teach_id" : user_id)
					]), //_id = topic_id and (author_id = user_id or teach_id = user_id)
					$$( $set : $$("type" : TopicsType.问题已结束.ordinal() , "end_user" : user_id , "update_at" : now , "end_type" : end_type))
					);
		}
		nodeEndTopic(topic_id, now, to_user);
		Map result = new HashMap();
		result["topic_id"] = topic_id;
		result["timestamp"] = now;

		return getResultOKS(result);
	}

	//发起结束通知
	def nodeEndTopic(String topic_id , Long now , Integer user_id){
		ChatBaseMsg msg = new ChatBaseMsg();
		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_CHAT_END);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(user_id) );
		msgController.SendMsg(tousers, msg);
	}

	/**
	 * 会答1.5.1 教师发起结束接口
	 * @param topic_id 问题id
	 * @param user_id 教师id
	 * 
	 * add by shihongjie
	 * 2016-1-12
	 */
	def teacher_end_topic(HttpServletRequest request){
		//问题id
		String topic_id = request["topic_id"];
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//根据提问id和教师id查询问题
		def topic = topics().findOne(
				$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() ,  "teach_id" : user_id),
				$$("author_id" : 1 , "teach_id" : 1)
				)
		Long now =  System.currentTimeMillis();
		Integer to_user = null;
		if(topic){
			to_user = (Integer) topic["author_id"];
			//更新
			topics().update(
					$$("_id" : topic_id,"type" : TopicsType.抢答成功.ordinal() , "teach_id" : user_id),
					$$( $set : $$("type" : TopicsType.问题已结束.ordinal() , "end_user" : user_id , "update_at" : now , "end_type" : TopicEndType.老师结束.ordinal()))
					);
		}
		teacher_end_topic_node_student(topic_id, now, to_user);
		Map result = new HashMap();
		result["topic_id"] = topic_id;
		result["timestamp"] = now;


		//结束之后，存入超时键值，如果1个小时后未评价，则发送提醒消息。
		String key = KeyUtils.TOPICES.topicsPJRemindKey(topic_id);
		mainRedis.opsForValue().set(key, "", KeyUtils.TOPICES.TOPIC_PJ_REMIND_LIMIT_TIME , TimeUnit.HOURS);
		//chat也要存一份，因为要用到键值过期
		chatRedis.opsForValue().set(key, "", KeyUtils.TOPICES.TOPIC_PJ_REMIND_LIMIT_TIME , TimeUnit.HOURS);

		return getResultOKS(result);
	}

	/**
	 * 会答1.5.1 教师发起结束后 node通知学生
	 * @param topic_id 问题id
	 * @param now 时间
	 * @param to_user 接受人id
	 * 
	 * add by shihongjie
	 * 2016-1-12
	 */
	def teacher_end_topic_node_student(String topic_id , Long now , Integer to_user){
		ChatBaseMsg msg = new ChatBaseMsg();
		msg.setTopic_id(topic_id);
		msg.setTimestamp(now);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_CHAT_END);
		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(to_user) );
		msgController.SendMsg(tousers, msg);
	}


	/**
	 * 发送作弊消息
	 * @date 2015年9月2日 上午11:25:06 
	 * @param @param topic_id
	 * @param @param teach_id
	 * @param @return 
	 * @throws
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topicReplyCheat(String topic_id  , int teach_id){
		//用户评价保存为聊天消息
		if(StringUtils.isNotBlank(topic_id)){
			String reply_content = "会答检测到本次抢答无效，您可能有违规操作，有疑问致电客服电话：4008-717-707";
			long now = System.currentTimeMillis();
			def tr = $$(
					"_id" : UUID.randomUUID().toString() ,"reply_content" : reply_content ,
					"reply_time" : now , "reply_type" : TopicsReplyType.系统文字.ordinal(),
					"topic_id" : topic_id , null : Web.getCurrentUserId()  , "user_pic" :null,
					"show_type" : ReplyShowType.老师.ordinal(),"timestamp" : now
					);

			topics_reply().save(tr);

			//聊天消息已NODE发送

			TopicChatMsg msg = new TopicChatMsg();
			Chat chat = new Chat();
			chat.set_id(tr["_id"]);
			chat.setReply_content(tr["reply_content"]);
			chat.setReply_time(now);
			chat.setTimestamp(now);
			chat.setTopic_id(topic_id);
			chat.setUser_id(null);
			chat.setUser_pic(null);
			chat.setReply_type(tr["reply_type"]);
			msg.setTopic_id(topic_id);
			msg.setTimestamp(now);

			msg.setChat(chat);
			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
			//			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_RED);
			//评价消息 两条消息
			List<String> tousers1 = new ArrayList();

			tousers1.add(String.valueOf(teach_id) );

			msgController.SendMsg(tousers1, msg);
		}
	}


	/**
	 * 用户评价后保存消息记录
	 * @Description: 用户评价后保存消息记录
	 * @date 2015年7月28日 下午4:05:15 
	 * @param @param topic_id 问题id
	 * @param @param score 评分
	 * @param @param teach_id 老师id
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topicReplyEvaluation(String topic_id , int score , int teach_id , String eval_contents){
		//用户评价保存为聊天消息
		if(StringUtils.isNotBlank(topic_id)){
			//评论内容
			String reply_content = UnEvaluation.getMsg(score , eval_contents);
			long now = System.currentTimeMillis();
			String pic = users().findOne($$("_id" : Web.getCurrentUserId() ) , $$("pic" : 1))?.get("pic");
			def tr = $$(
					"_id" : UUID.randomUUID().toString() ,"reply_content" : reply_content ,
					"reply_time" : now , "reply_type" : TopicsReplyType.文字.ordinal(),
					"topic_id" : topic_id , "user_id" : Web.getCurrentUserId()  , "user_pic" :pic,
					"show_type" : ReplyShowType.所有人.ordinal(),"timestamp" : now
					);

			topics_reply().save(tr);

			//聊天消息已NODE发送

			TopicChatMsg msg = new TopicChatMsg();
			Chat chat = new Chat();
			chat.set_id(tr["_id"]);
			chat.setReply_content(tr["reply_content"]);
			chat.setReply_time(now);
			chat.setTimestamp(now);
			chat.setTopic_id(topic_id);
			chat.setUser_id(tr["user_id"]);
			chat.setUser_pic(tr["user_pic"]);
			chat.setReply_type(tr["reply_type"]);

			msg.setTopic_id(topic_id);
			msg.setTimestamp(now);

			msg.setChat(chat);
			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
			//评价消息 两条消息
			List<String> tousers1 = new ArrayList();
			List<String> tousers2 = new ArrayList();

			tousers1.add(String.valueOf(teach_id) );
			tousers2.add(String.valueOf(Web.getCurrentUserId()) );

			msgController.SendMsg(tousers1, msg);
			msgController.SendMsg(tousers2, msg);
		}
	}
	/**
	 * 打赏消息通知
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topicReplyTipsSuccess(String topic_id , Double money , int teach_id){
		//用户评价保存为聊天消息
		if(StringUtils.isNotBlank(topic_id)){
			//评论内容
			String reply_content = "恭喜您，您获得"+ money +"元打赏金额";
			long now = System.currentTimeMillis();
			//			String pic = users().findOne($$("_id" : teach_id ) , $$("pic" : 1))?.get("pic");
			String pic = null;
			def tr = $$(
					"_id" : UUID.randomUUID().toString() ,"reply_content" : reply_content ,
					"reply_time" : now , "reply_type" : TopicsReplyType.系统文字.ordinal(),
					"topic_id" : topic_id , "user_id" : null  , "user_pic" :null,
					"show_type" : ReplyShowType.老师.ordinal(),"timestamp" : now
					);

			topics_reply().save(tr);

			//聊天消息已NODE发送

			TopicChatMsg msg = new TopicChatMsg();
			Chat chat = new Chat();
			chat.set_id(tr["_id"]);
			chat.setReply_content(tr["reply_content"]);
			chat.setReply_time(now);
			chat.setTimestamp(now);
			chat.setTopic_id(topic_id);
			chat.setUser_id(tr["user_id"]);
			chat.setUser_pic(tr["user_pic"]);
			chat.setReply_type(tr["reply_type"]);

			msg.setTopic_id(topic_id);
			msg.setTimestamp(now);

			msg.setChat(chat);
			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
			//打赏消息
			List<String> tousers1 = new ArrayList();

			tousers1.add(String.valueOf(teach_id) );

			msgController.SendMsg(tousers1, msg);
		}
	}

	//	/**
	//	 * 用户评价后保存消息记录
	//	 * @Description: 用户评价后保存消息记录
	//	 * @date 2015年7月28日 下午4:05:15
	//	 * @param @param topic_id 问题id
	//	 * @param @param score 评分
	//	 * @param @param teach_id 老师id
	//	 */
	//	@TypeChecked(TypeCheckingMode.SKIP)
	//	def topicReplyEvaluation(String topic_id , int score , int teach_id){
	//		//用户评价保存为聊天消息
	//		if(StringUtils.isNotBlank(topic_id)){
	//			String reply_content = score == ScoreType.不满意.ordinal() ? "[ 不满意 ]" : "[ 满意 ]";
	//			long now = System.currentTimeMillis();
	//			String pic = users().findOne($$("_id" : Web.getCurrentUserId() ) , $$("pic" : 1))?.get("pic");
	//			def tr = $$(
	//					"_id" : UUID.randomUUID().toString() ,"reply_content" : reply_content ,
	//					"reply_time" : now , "reply_type" : TopicsReplyType.文字.ordinal(),
	//					"topic_id" : topic_id , "user_id" : Web.getCurrentUserId()  , "user_pic" :pic,
	//					"show_type" : ReplyShowType.所有人.ordinal(),"timestamp" : now
	//					);
	//
	//			topics_reply().save(tr);
	//
	//			//聊天消息已NODE发送
	//
	//			TopicChatMsg msg = new TopicChatMsg();
	//			Chat chat = new Chat();
	//			chat.set_id(tr["_id"]);
	//			chat.setReply_content(tr["reply_content"]);
	//			chat.setReply_time(now);
	//			chat.setTimestamp(now);
	//			chat.setTopic_id(topic_id);
	//			chat.setUser_id(tr["user_id"]);
	//			chat.setUser_pic(tr["user_pic"]);
	//			chat.setReply_type(tr["reply_type"]);
	//
	//			msg.setTopic_id(topic_id);
	//			msg.setTimestamp(now);
	//
	//			msg.setChat(chat);
	//			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
	////			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_RED);
	//			//评价消息 两条消息
	//			List<String> tousers1 = new ArrayList();
	//			List<String> tousers2 = new ArrayList();
	//
	//			tousers1.add(String.valueOf(teach_id) );
	//			tousers2.add(String.valueOf(Web.getCurrentUserId()) );
	//
	//			msgController.SendMsg(tousers1, msg);
	//			msgController.SendMsg(tousers2, msg);
	//		}
	//	}

/**
 * 对话接口
 * @param req
 * @return
 */
	def topicReply(HttpServletRequest req){

		try{
			Integer id = Web.getCurrentUserId();
			java.net.URLDecoder urlDecoder = new java.net.URLDecoder();

			//for ios not decode 2016-01-15
			//			String rJson = req["json"];
			//			String json = null;
			//			json = rJson;
			//			if(StringUtils.isNotBlank(rJson) && rJson.indexOf("+") == -1 && rJson.indexOf("%") == -1){
			//				json = urlDecoder.decode(rJson, "UTF-8");
			//			}
			String json = urlDecoder.decode(req["json"], "UTF-8");
			//校验非空
			if(StringUtils.isBlank(json)){
				return getResultParamsError();
			}
			//参数
			TopicReplyVO vo = JSONUtil.jsonToBean(json, TopicReplyVO.class);
			//当前时间
			Long timestamp = System.currentTimeMillis();
			//问题id
			String topic_id = vo.getTopic_id();
			//问题最后一次回复的内容
			String reply_content = vo.getReply_content();
			Integer reply_type = Integer.valueOf(vo.getReply_type());
			if(reply_type == ReplyType.图片.ordinal()){
				reply_content = "[ 图片 ]";
			}else if(reply_type == ReplyType.语音.ordinal()){
				reply_content = "[ 语音 ]";
			}

			//修改最后一次回复
			topics().update(
					$$("_id" : topic_id),
					$$($set : $$("last_reply_content" : reply_content, "last_reply_at" : timestamp ,"update_at" : timestamp ), '$inc': $$("reply_count":1))
					);

			//新增回复内容
			def topic_reply = $$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : vo.getReply_content() ,"reply_time" : timestamp,
					"reply_type": reply_type , "topic_id" : topic_id , "user_id" : Web.getCurrentUserId(),
					"user_pic" : vo.getUser_pic() , "timestamp" : timestamp , "show_type" : ReplyShowType.所有人.ordinal(),
					"pic_height":vo.getPic_height(),"pic_width":vo.getPic_width(),"topics_reply_first":0
					);

			//缓存位置
			String fileCache = null;
			//如果是文件
			if(reply_type == 1 || reply_type == 2){
				/////////////////////////////////////////////////////////////////////////////////////文件保存//////////////////////////////////////
				//				FtpInfo ftpInfo = vo.getFile_json();
				//兼容新的文件服务加入的判断
				String strUrl = vo.getFile_json();
				if(strUrl.substring(0,4)=="http"){
					String url1=strUrl.substring(strUrl.indexOf("/", 8)+1)
					/*正则方式，效率会低一点
					String patternStr="([^/]+(?!.*//*))";
					Pattern pattern=Pattern.compile(patternStr);
					Matcher matcher=pattern.matcher(strUrl);
					//文件原名
					String original_file_name1;
					while(matcher.find())
					{
						original_file_name1=matcher.group();
					}*/
					//文件原名
					String original_file_name1=StringUtils.substringAfterLast(strUrl,"/");;
					String _id1 = original_file_name1.substring(0,original_file_name1.indexOf("."))
					Map fileMap1 = new HashMap();
					fileMap1["original_file_name"] = original_file_name1;
//					//文件大小
					fileMap1["file_size"] = 0L;
//					//文件地址
					fileMap1["url"] = url1;
//					//文件id
					fileMap1["_id"] =_id1 ;

					//文件地址
					topic_reply["reply_content"] =  strUrl;
					//文件详细信息
					topic_reply["file_info"] = fileMap1;

					if(reply_type == ReplyType.语音.ordinal()){
						topic_reply["duration"] = vo.getDuration();
					}

				}else{
					FtpInfo ftpInfo = JSONUtil.jsonToBean(vo.getFile_json(), FtpInfo.class)
					if(ftpInfo){
					Map fileMap = new HashMap();
					//文件原名称
					fileMap["original_file_name"] = ftpInfo.getOriginal_file_name();
					//文件大小
					fileMap["file_size"] = ftpInfo.getFile_size();
					//文件地址
					fileMap["url"] = ftpInfo.getUrl();
					//文件id
					fileMap["_id"] = ftpInfo.get_id();

					//文件地址
					topic_reply["reply_content"] =  ftpInfo.getUrl();
					//文件详细信息
					topic_reply["file_info"] = fileMap;

					if(reply_type == ReplyType.语音.ordinal()){
						topic_reply["duration"] = vo.getDuration();
					}
				}
				}
			}
			topics_reply().save(topic_reply);

			if(reply_type == ReplyType.图片.ordinal() || reply_type == ReplyType.语音.ordinal()){
				String strUrl = topic_reply["reply_content"]
				if(!(strUrl.substring(0,4)=="http")){
					topic_reply["reply_content"] = file_url + topic_reply["reply_content"];
				}
			}
			return getResultOKS(topic_reply);
		}finally {
		}

	}



	/**
	 * 查询问答详细内容 包含聊天记录
	 * @Description: 查询问答详细内容 包含聊天记录
	 * @date 2015年6月17日 下午5:53:14 
	 */
	def getTopicsInfo(HttpServletRequest request){
		String topic_id = request["topic_id"];

		if(StringUtils.isNotBlank(topic_id)){

			def topic = topics().findOne(
					$$("_id" : topic_id) ,
					$$(
					"_id" : 1      , "type" : 1      , "author_id" : 1      , "teach_id" : 1    , "evaluation" : 1 ,
					"timestamp" : 1, "update_at" : 1 , "evaluation_type": 1 , "buns_states" : 1 , "end_type" : 1   ,
					//v200
					"teacher_show_type" : 1
					)
					);

			if(topic){
				def show = $$("_id" : 1 , "nick_name" : 1 , "pic" : 1);
				def author = users().findOne($$("_id" : topic["author_id"]) , show);
				//状态判断
				def teach = null;
				if(topic["type"] != TopicsType.待抢答.ordinal()){
					teach = users().findOne($$("_id" : topic["teach_id"]) , show);
				}
				def query = $$("topic_id" : topic_id );

				if(teach && Web.getCurrentUserId() == teach["_id"]){//教师
					query.append("show_type", $$($in : [
						ReplyShowType.所有人.ordinal() ,
						ReplyShowType.老师学员.ordinal() ,
						ReplyShowType.老师.ordinal()
					]));

					//					//v200教师视图-如果教师应该呈现金额视图-修改标志-结束显示
					//					topics().update(
					//						$$("_id" : topic_id ,"evaluation_type" : $$('$in' : [TopicEvaluationType.满意.ordinal() , TopicEvaluationType.非常满意.ordinal() ]) , "teacher_show_type" : TopicTeacherShowType.金额.ordinal() ),
					//						$$('$set' : $$("teacher_show_type" : TopicTeacherShowType.无.ordinal()))
					//						);

				}else if(Web.getCurrentUserId() == author["_id"]){//学员
					query.append("show_type", $$($in : [
						ReplyShowType.所有人.ordinal() ,
						ReplyShowType.老师学员.ordinal() ,
						ReplyShowType.学员.ordinal()
					]));
				}else{//其他用户
					query.append("show_type", $$($in : [ReplyShowType.所有人.ordinal()]));
				}

				//查询 聊天
				List<DBObject> list = topics_reply().find(query).sort($$("timestamp" : 1)).limit(1000)?.toArray();
				if(list){
					//					Collections.reverse(list);
					list.each {def dbo->
						Integer reply_type = dbo["reply_type"] as Integer;
						if(dbo["reply_content"] && (reply_type==TopicsReplyType.图片.ordinal() || reply_type==TopicsReplyType.语音.ordinal()) ){
							dbo["reply_content"] = file_url + dbo["reply_content"];
						}
						if(dbo["user_id"] == author["_id"]){
							dbo["user_pic"] = author["pic"];
							dbo["user_name"] = author["nick_name"];
						}else if(teach && dbo["user_id"] == teach["_id"]){
							dbo["user_pic"] = teach["pic"];
							dbo["user_name"] = teach["nick_name"];
						}

					}
				}

				topic["topic_replay"] = list;

				topic["collection_id"] = collection().findOne($$("topics_id" :topic_id , "tuid" : Web.getCurrentUserId()), $$("_id" : 1))?.get("_id");

				//V200开始 评价类型加入[非常满意] 对应到旧版本 [满意]
				topic["evaluation_type"] = TopicEvaluationType.oldTypeInt(topic["evaluation_type"] as Integer);
			}

			return getResultOKS(topic);
		}
		return getResultParamsError();
	}

	/**
	 * 查询问答详细内容 包含聊天记录
	 * @Description: 查询问答详细内容 包含聊天记录
	 * v200新增
	 * teacher_show_type 教师显示类型
	 * 0.undefined 1.红包 2.拆开界面 3.无
	 * @date 2015年6月17日 下午5:53:14 
	 */
	def getTopicsInfo_v200(HttpServletRequest request){
		String topic_id = request["topic_id"];
		long startTime = ServletRequestUtils.getLongParameter(request, "startTime", -1)
		long endTime = ServletRequestUtils.getLongParameter(request, "endTime", -1)

		if(StringUtils.isNotBlank(topic_id)){

			def topic = topics().findOne(
					$$("_id" : topic_id) ,
					$$(
					"_id" : 1 , "type" : 1 , "author_id" : 1 , "teach_id" : 1 , "evaluation" : 1 ,
					"timestamp" : 1, "update_at" : 1 , "evaluation_type": 1 , "buns_states" : 1 , "end_type" : 1,
					"teacher_show_type" : 1
					)
					);

			if(topic){
				def show = $$("_id" : 1 , "nick_name" : 1 , "pic" : 1);
				def author = users().findOne($$("_id" : topic["author_id"]) , show);
				topic["author_name"] = author["nick_name"];
				//状态判断
				def teach = null;
				if(topic["type"] != TopicsType.待抢答.ordinal()){
					teach = users().findOne($$("_id" : topic["teach_id"]) , show);
					topic["teach_pic"] = teach["pic"];
					topic["teach_name"] = teach["nick_name"];
				}else{
					topic["teach_pic"] = null;
					topic["teach_name"] = null;
				}
				def query = $$("topic_id" : topic_id , "reply_status": $$('$ne' : 1));

				if(teach && Web.getCurrentUserId() == teach["_id"]){//教师
					query.append("show_type", $$($in : [
						ReplyShowType.所有人.ordinal() ,
						ReplyShowType.老师学员.ordinal() ,
						ReplyShowType.老师.ordinal()
					]))

					//					//教师视图-如果教师应该呈现金额视图-修改标志-结束显示
					//					topics().update(
					//						$$("_id" : topic_id ,"evaluation_type" : $$('$in' : [TopicEvaluationType.满意.ordinal() , TopicEvaluationType.非常满意.ordinal() ]) , "teacher_show_type" : TopicTeacherShowType.金额.ordinal() ),
					//						$$('$set' : $$("teacher_show_type" : TopicTeacherShowType.无.ordinal()))
					//						);

				}else if(Web.getCurrentUserId() == author["_id"]){//学员
					query.append("show_type", $$($in : [
						ReplyShowType.所有人.ordinal() ,
						ReplyShowType.老师学员.ordinal() ,
						ReplyShowType.学员.ordinal()
					]))
				}else{//其他用户
					query.append("show_type", $$($in : [ReplyShowType.所有人.ordinal()]))
					def def_read_num = topics().findOne(	$$("_id" : topic_id))?.get("read_num");//获取浏览次数
					long read_num;
					if(def_read_num==null){
						read_num = 1;
					}else{
						read_num = (long)def_read_num;
						read_num++;
					}
					topics().update(
							$$("_id" : topic_id),
							$$($set : $$(
							"read_num" : read_num)));
				}
				if(-1 != startTime && -1 != endTime) {
					query.append("timestamp",$$('$gte':startTime,'$lte':endTime))
				}

				//查询 聊天
				List<DBObject> list = topics_reply().find(query).sort($$("timestamp" : 1)).limit(1000)?.toArray();
				if (list) {
					//Collections.reverse(list);
					list.each { def dbo ->
						Integer reply_type = dbo["reply_type"] as Integer;
						if (dbo["reply_content"] && (reply_type == TopicsReplyType.图片.ordinal() || reply_type == TopicsReplyType.语音.ordinal())) {
							String subStr=dbo["reply_content"];
							if (subStr.substring(0,4)=="http") {
								dbo["reply_content"] = subStr;
							}else {
								dbo["reply_content"] = file_url + dbo["reply_content"];
							}
						}
						if (dbo["user_id"] == author["_id"]) {
							dbo["user_pic"] = author["pic"];
							dbo["user_name"] = author["nick_name"];
						} else if (teach && dbo["user_id"] == teach["_id"]) {
							dbo["user_pic"] = teach["pic"];
							dbo["user_name"] = teach["nick_name"];
						} else {
							dbo["user_name"] = Constant.TOPIC_REPLAY_SYSTEM_NAME;
							dbo["user_pic"] = Constant.TOPIC_REPLAY_SYSTEM_PIC;
						}

					}
				}

				topic["topic_replay"] = list;

				topic["collection_id"] = collection().findOne($$("topics_id" :topic_id , "tuid" : Web.getCurrentUserId()), $$("_id" : 1))?.get("_id");

				topic["evaluation_bumanyi"] = UnEvaluation.getUnEvaluationList();


			}

			return getResultOKS(topic);
		}
		return getResultParamsError();
	}

	//TODO 重新发起提问的会豆奖励
	@TypeChecked(TypeCheckingMode.SKIP)
	def reSubmit_v200(HttpServletRequest request){
		int userid = Web.getCurrentUserId();

		if(isStudentLimitTime(userid)){
			return Code.TOPICS.学生问题时限();
		}

		//提问id  250版本
		String topic_id = request["topic_id"];
		Double tip_kd_full = ServletRequestUtils.getDoubleParameter(request, "tip_kd", 0);

		//判断该问题是否允许免费提交
		def dashang = topics().findOne($$("_id" : topic_id))?.get("da_shang");
		if(null != dashang && dashang == 1){
			if (tip_kd_full == 0) {
				return ["code" : 40091 , "data" : "该问题只能打赏提交"];
			}
		}

		if(tip_kd_full > 0 && !useKD(userid, tip_kd_full)){
			return ["code" : 40090 , "data" : "余额不足"];
		}

		if(StringUtils.isNotBlank(topic_id) && userid > 0){
			Long now = System.currentTimeMillis();
			//打赏给用户的金额
			Double tip_to_user_money = 0d;
			//比例
			Double tip_fee_prop = 0d;
			//id 用户id 未删除 问题状态为抢答失败
			//			topics().update(
			//					$$("_id" : topic_id , "author_id" : userid , "deleted" : false , "type" : TopicsType.抢答失败.ordinal()),
			//					$$($set : $$(
			//						"type" : TopicsType.待抢答.ordinal() ,
			//						"update_at" : now ,
			//						"submit_time" : now ,  //问题提交时间
			//						"tip_kd" : tip_kd
			//						))
			//					);

			//V250打赏会豆记录  250版本
			if(tip_kd_full > 0 ){
				def tt = saveTopicTip(userid , tip_kd_full , topic_id);
				//打赏给用户的金额
				tip_to_user_money = tt["to_user_money"];
				//比例
				tip_fee_prop = tt["fee_prop"];
			}

			topics().update(
					$$("_id" : topic_id , "author_id" : userid , "deleted" : false , "type" : TopicsType.抢答失败.ordinal()),
					$$($set : $$(
					"type" : TopicsType.待抢答.ordinal() ,
					"update_at" : now ,
					"submit_time" : now ,  //问题提交时间
					"tip_kd" : tip_to_user_money,//打赏给教师的金额
					"tip_fee_prop" : tip_fee_prop,//平台收取百分比
					"tip_kd_full" : tip_kd_full,//学员真实打赏金额
					"is_reSubmit" : TopicsIsReSubmit.重新提交的问题.ordinal()//是否是重新提交的问题
					))
					);


			def topic = topics().findOne($$("_id" : topic_id));

			//记录重新提交问题次数
			if(topic){
				topics().update(
						$$("_id" : topic_id),
						$$('$inc':$$("resubmit_num" : 1))
						);

				topic_resubmit_log().save($$("topic_id" : topic["_id"] , "timestamp" : now , "resubmit_num" : topic["resubmit_num"] , "remark" : topic));
			}


			//redis中学生图片问题
			def topics_pic = topic["topics_pic"];
			if(topics_pic != null && topics_pic.size() > 0){
				topics_pic.each {def m->
					m["pic_url"] = file_url+ m["pic_url"];
				}
				topic["topics_pic"] = topics_pic;
			}
			//保存到redis
			topic["product"] = (topic["product"] == null? 0:topic["product"])
			if(0 == topic["product"] as int) { //会计
				saveRedis(topic)
			} else if(1 == topic["product"] as int) { //自考
				saveRedisZikao(topic)
			}
			return getResultOKS(now);
		}
		return getResultParamsError();
	}
	/**
	 * 扣除会豆
	 */
	def useKD(Integer user_id , Double kd){
		return currencyController.deductCurrency(user_id , kd,"打赏");
	}

	/**
	 * 打赏记录
	 * 会豆数量
	 */
	def saveTopicTip(Integer user_id , Double kd , String topic_id){
		//会豆和金额比例
		double kdp = kd_prop();
		//手续费比例
		double fdp = fee_prop();
		//打赏金额
		double kd_money = mul(kdp , kd);
		//手续费
		double fee = mul(kd_money , fdp);
		//分配给用户的金额
		double to_user_money = kd_money - fee;

		Long now = System.currentTimeMillis();

		def tt = $$(
				"_id" : UUID.randomUUID().toString() ,"from_user_id" : user_id , "to_user_id" : null ,
				"timestamp" : now , "kd" : kd , "topic_id" : topic_id,
				"create_time" : now , "kd_prop" :kdp , "kd_money" : kd_money,
				"fee" : fee , "fee_prop" : fdp, "to_user_money" : to_user_money,
				"dr" : com.izhubo.model.DR.正常.ordinal() , "update_time" : now
				);

		topic_tip().save(tt);
		return tt;
	}



	def getTipType(HttpServletRequest request){

		getResultOK(tip_type().find().toArray());
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 重新提交
	 * @Description: 重新提交
	 * @date 2015年7月27日 上午9:52:05 
	 */
	//	@TypeChecked(TypeCheckingMode.SKIP)
	//	def reSubmit_v200(HttpServletRequest request){
	//
	//		int userid = Web.getCurrentUserId();
	//
	//		if(isStudentLimitTime(userid)){
	//			return Code.TOPICS.学生问题时限();
	//		}
	//
	//		//提问id
	//		String topic_id = request["topic_id"];
	//		if(StringUtils.isNotBlank(topic_id) && userid > 0){
	//			Long now = System.currentTimeMillis();
	//			//id 用户id 未删除 问题状态为抢答失败
	//			topics().update(
	//					$$("_id" : topic_id , "author_id" : userid , "deleted" : false , "type" : TopicsType.抢答失败.ordinal()),
	//					$$($set : $$("type" : TopicsType.待抢答.ordinal() , "update_at" : System.currentTimeMillis() ))
	//					);
	//			def topic = topics().findOne($$("_id" : topic_id));
	//			//redis中学生图片问题
	//			def topics_pic = topic["topics_pic"];
	//			if(topics_pic != null && topics_pic.size() > 0){
	//				topics_pic.each {def m->
	//					m["pic_url"] = file_url+ m["pic_url"];
	//				}
	//				topic["topics_pic"] = topics_pic;
	//			}
	//			//保存在redis
	//			saveRedis(topic);
	//			return getResultOKS(now);
	//		}
	//		return getResultParamsError();
	//	}
	/**
	 * 重新提交
	 * @Description: 重新提交
	 * @date 2015年7月27日 上午9:52:05 
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def reSubmit(HttpServletRequest request){

		int userid = Web.getCurrentUserId();

		//		if(isStudentLimitTime(userid)){
		//			return Code.TOPICS.学生问题时限();
		//		}

		//提问id
		String topic_id = request["topic_id"];
		//判断该问题是否允许免费提交
		def dashang = topics().findOne($$("_id" : topic_id))?.get("da_shang");
		if(null != dashang && dashang == 1){
			return ["code" : 40091 , "data" : "该问题只能打赏提交"];
		}
		if(StringUtils.isNotBlank(topic_id) && userid > 0){
			Long now = System.currentTimeMillis();
			//id 用户id 未删除 问题状态为抢答失败
			topics().update(
					$$("_id" : topic_id , "author_id" : userid , "deleted" : false , "type" : TopicsType.抢答失败.ordinal()),
					$$($set : $$(
					"type" : TopicsType.待抢答.ordinal() ,
					"submit_time" : now ,//问题提交时间
					"update_at" : now,
					"tip_kd" : 0,//打赏给教师的金额
					"tip_fee_prop" : 0,//平台收取百分比
					"tip_kd_full" : 0,//学员真实打赏金额
					"is_reSubmit" : TopicsIsReSubmit.重新提交的问题.ordinal()//是否是重新提交的问题
					))
					);
			def topic = topics().findOne($$("_id" : topic_id));
			//redis中学生图片问题
			def topics_pic = topic["topics_pic"];
			if(topics_pic != null && topics_pic.size() > 0){
				topics_pic.each {def m->
					m["pic_url"] = file_url+ m["pic_url"];
				}
				topic["topics_pic"] = topics_pic;
			}
			//保存在redis
			saveRedis(topic);
			return getResultOKS(now);
		}
		return getResultParamsError();
	}

	//抢答
	//v150 抢答完后由服务端通知学生端抢答成功(原本是getTopic_v150为了兼容修改为v151)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getTopic_v151(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//v2.0 教师抢答时限校验
		if(isTeachertLimitTime(user_id)){
			return Code.TOPICS.教师抢答时限();
		}


		//问题id
		String tid = request["tid"];
		//v2.0加入抢答时限
		//		teacherLimitTimeSaveRedis(user_id , tid);
		//抢答者的设备id
		String device_id = request["device_id"];


		def valOp = mainRedis.opsForValue();
		def valOp1 = chatRedis.opsForValue();



		if(StringUtils.isNotBlank(tid)){
			String key = KeyUtils.TOPICES.tpicesIndustrys(tid);//加入自考的
			String sjson = null;
			lock.lock();
			try {
				//获取问题json
				sjson = valOp.get(key);

				if(sjson != null){
					//删除redis中问题json
					mainRedis.delete(key);

					int len = mainRedis.opsForList().size(KeyUtils.TOPICES.TOPICESLIST);
					for(int i=0;i<len;i++)
					{

						String insideKey = mainRedis.opsForList().index(KeyUtils.TOPICES.TOPICESLIST, i);

						if(insideKey == tid)
						{
							mainRedis.opsForList().remove(KeyUtils.TOPICES.TOPICESLIST, i, tid);//删除队列的内容
							break;
						}
					}


				}
				if(valOp1.get(key) != null){
					chatRedis.delete(key);
				}
			}finally{
				//显示释放锁
				lock.unlock();
			}

			if(sjson != null){
				Long now = System.currentTimeMillis();
				//更新问题状态
				Integer number = topics().update(
						$$("_id" : tid , "type" : TopicsType.待抢答.ordinal()),
						$$($set : $$("type": TopicsType.抢答成功.ordinal(),"race_time" : now , "teach_id" : user_id , "update_at" : now , "teach_device_id" : device_id))
						).getN();

				if(number == 0){
					return getResult(Code.已被抢答S, Code.已被抢答_S, Code.已被抢答_S);
				}

				/**  ******************************* 维护 users老师抢答数量 *********************************/ 
				users().update(
						$$("_id" : user_id),
						$$('$inc':$$("topic_count" : 1))
						);
				/**  ******************************* 维护 users老师抢答数量 end*********************************/ 

				//学员id
				String student_id = topics().findOne($$("_id" : tid), $$("author_id" : 1))?.get("author_id");

				//聊天超时时间
				chatSaveRedis(tid);
				//向学员端产生系统消息 Xxx已经抢答了您的问题，为您解答。
				getChatSystemMsg(tid , now , student_id);

				//抢答老师信息
				Map user = Web.currentUser();

				getTopicSendStudent(
						tid ,
						String.valueOf(user_id), String.valueOf(user["nick_name"]) , String.valueOf(user["pic"]),
						student_id , now
						);

				return getResultOKS();
			}else{
				return getResult(Code.已被抢答S, Code.已被抢答_S, Code.已被抢答_S);
			}
		}
		return getResultParamsError();
	}

	/**
	 * 问题抢答成功后 通知学生端
	 * @Description 问题抢答成功后 通知学生端
	 * @date 2015年10月27日 上午10:01:27 
	 * @param  topic_id 问题id
	 * @param  teach_id 教师id
	 * @param  teach_nick_name 教师昵称
	 * @param  teach_pic 教师头像
	 * @param  student_id 学生id
	 * @param  now 时间
	 */
	private void getTopicSendStudent(String topic_id , String teach_id , String teach_nick_name , String teach_pic , String student_id , Long now){
		//聊天消息已NODE发送

		TeacherFindTopicMsg mb = new TeacherFindTopicMsg();


		mb.setCreate_time(System.currentTimeMillis());
		mb.setFrom_user(teach_id);
		mb.setIs_confirm(true);
		List userlist = new ArrayList();

		mb.initMA(Nodes.NODE_MODULE_TOPIC_CHAT , Nodes.NODE_ACTION_TOPIC_CHAT_WAITING_SUCCESS);
		mb.setTopic_id(topic_id);
		mb.setTeacher_pic(teach_pic);
		mb.setTeacher_id(teach_id);
		mb.setTeacher_name(teach_nick_name);

		mb.setCreate_time(now);

		List<String> tousers = new ArrayList();

		tousers.add(String.valueOf(student_id) );

		msgController.SendMsg(tousers, mb);
	}



	@TypeChecked(TypeCheckingMode.SKIP)
	def getChatSystemMsg(String topic_id , Long now , String user_id){
		Map user = Web.currentUser();
		//		long now = System.currentTimeMillis();
		def tr = $$(
				"_id" : UUID.randomUUID().toString() ,"reply_content" : user["nick_name"] + "已经抢答了您的问题，为您解答。" ,
				"reply_time" : now , "reply_type" : TopicsReplyType.系统文字.ordinal(),
				"topic_id" : topic_id , "user_id" : null  , "user_pic" :null,
				"show_type" : ReplyShowType.学员.ordinal(),"timestamp" : now
				);

		topics_reply().save(tr);


		//聊天消息已NODE发送
		//抢答成功后通知 Xxx已经抢答了您的问题，为您解答。
		TopicChatMsg msg = new TopicChatMsg();
		Chat chat = new Chat();
		chat.set_id(topic_id);
		chat.setReply_content(tr["reply_content"]);
		chat.setReply_time(now);
		chat.setTimestamp(now);
		chat.setTopic_id(topic_id);
		chat.setUser_id(null);
		chat.setUser_pic(null);
		chat.setReply_type(tr["reply_type"]);
		msg.setChat(chat);
		msg.setTimestamp(now);
		msg.setTopic_id(topic_id);
		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_TEXT);
		//		msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_SYS_RED);
		List<String> tousers = new ArrayList();

		//		String user_id = topics().findOne($$("_id" : topic_id), $$("author_id" : 1))?.get("author_id");

		tousers.add(String.valueOf(user_id) );

		msgController.SendMsg(tousers, msg);
	}



	/**
	 * 保存在redis
	 * @Description: 保存在redis
	 * @date 2015年6月15日 下午5:13:55
	 * @param @param topic
	 * @param @return
	 * @throws
	 */
	def chatSaveRedis(String topic_id){
		if(StringUtils.isNotBlank(topic_id)){
			String key = KeyUtils.TOPICES.tpicesChatTimeOut(topic_id);
			mainRedis.opsForValue().set(key, key, KeyUtils.TOPICES.TPICES_CHAT_TIMEOUT , TimeUnit.HOURS);
			//需要存入chat一份，因为指向的redis是发布模式的redis，才能获取过期的键值
			chatRedis.opsForValue().set(key, key, KeyUtils.TOPICES.TPICES_CHAT_TIMEOUT , TimeUnit.HOURS);

		}
	}

	/**
	 * 删除超时问题
	 * 根据问题id和学生id
	 * 
	 * 修改成功 [code : 1 , data : true]
	 * 修改失败 [code : 1 , data : false]
	 * 参数错误 [code : 30406 , data : "参数错误"]
	 */
	def remove_topic_v200(HttpServletRequest req){
		//问题id
		String topic_id = req["topic_id"];

		if(StringUtils.isNotBlank(topic_id)){
			//用户id
			Integer user_id = Web.getCurrentUserId();

			Boolean data = topics().update(
					$$("_id" : topic_id , "author_id" : user_id , "type" : TopicsType.抢答失败.ordinal() , "deleted" : false),
					$$('$set' : $$("deleted" : true))
					).getN() == 1;

			return ["code" : 1 , "data" : data];
		}

		return getResultParamsError();
	}

	private static String not_evaluation_text = "为提高讲师的抢答速度\n请对08月10日后提问的问题进行评价"

	private boolean hasTopicNotEvaluation(int userId, int product) {
		boolean not_evaluation = false

		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss")
		def constant = constants().find(null,$$("topic_not_evaluated_limit_start_time":1,"topic_not_evaluated_limit_text":1))?.toArray()
		String limitStartTime = "2018-08-10 00:00:00"
		if(constant) {
			limitStartTime = constant.get(0).get("topic_not_evaluated_limit_start_time")
			not_evaluation_text = constant.get(0).get("topic_not_evaluated_limit_text")
		}
		Long startTime = sdf.parse(limitStartTime).getTime()
		//校验是否有未评价问题
		topics().find($$("timestamp":$$('$gte':startTime),"author_id":userId,"product":product,"type":TopicsType.问题已结束.ordinal(),
				"evaluation_type":TopicEvaluationType.未评价.ordinal())).sort($$("timestamp":-1)).skip(0).limit(1).each {
			not_evaluation = true
		}
		return not_evaluation
	}

	/**
	 * 新增未评价提问限制
	 * @param req
	 */
	def topicsPower(HttpServletRequest req) {
		Integer userId = Web.getCurrentUserId()
		Integer ssoUserId = Web.getCurrentSSOUserId()
		if(0 == userId || 0 == ssoUserId) {
			return getResultToken()
		}
		int product = ServletRequestUtils.getIntParameter(req, "product", 0)
		def map = null
		boolean not_evaluation = hasTopicNotEvaluation(userId, product)

		//0：会计，1：自考
		if(0 == product) {
			map = topics_free_num(req).get("data")
			boolean is_free = map["is_free"] as boolean
			if(is_free) {
				if(not_evaluation) {
					is_free = false
					map["is_show"]  = !is_free
					map["free_text"] = not_evaluation_text
					map["is_show_text"]  = not_evaluation_text
					map["is_show_text_tip"]  = not_evaluation_text
				} else {
					String tip_key = KeyUtils.kuaijiTip(ssoUserId)
					String jsonList = mainRedis.opsForValue().get(tip_key)
					if (StringUtils.isBlank(jsonList)) {
						SynTipThread kuaiji = new SynTipThread()
						kuaiji.setMainRedis(mainRedis)
						kuaiji.setBusinessId(SynTipThread.KUAIJI)
						kuaiji.setSsoUserId(ssoUserId)
						kuaiji.hasCourseNo()
					}
				}
			}
		} else if(1 == product) {
			map = topics_permissions(req)
			if(1 == map.get("code")) {
				map = map.get("data")
				boolean has_permissions = map["has_permissions"] as boolean
				if(has_permissions && not_evaluation) {
					map["has_permissions"] = false
					map["permissions_type"] = 3 // 0：可以提问，1：未购买自考课程，2：没有免费次数, 3：最近提问未评价
					map["permissions_text"] = not_evaluation_text
					map["free_text"] = not_evaluation_text
				}
			} else {
				return map
			}
		}
		return getResultOK(map)
	}

	/**
	 * 提问权限判断
	 * @param req
	 */
	def topics_permissions(HttpServletRequest req) {
		Integer userid = Web.getCurrentSSOUserId()
		if(0 == userid) {
			return getResultToken()
		}
        def resultMap = new HashMap()
		//是否还有免费次数
		def map = topics_free_num(req).get("data")
		boolean is_free = map["is_free"] as boolean

        resultMap["has_permissions"] = false
        resultMap["permissions_type"] = 1 // 0：可以提问，1：未购买自考课程，2：没有免费次数
        resultMap["permissions_text"] = "该账号未购买自考课程"
        resultMap["free_text"] = map["free_text"]

		if(is_free) {
			boolean hasCourseNo = false
			String tip_key = KeyUtils.zikaoTip(userid)
			String jsonList = mainRedis.opsForValue().get(tip_key)
			if (StringUtils.isBlank(jsonList)) {
				SynTipThread zikao = new SynTipThread()
				zikao.setMainRedis(mainRedis)
				zikao.setBusinessId(SynTipThread.ZIKAO)
				zikao.setSsoUserId(userid)
				hasCourseNo = zikao.hasCourseNo()
			} else {
				hasCourseNo =true
			}
			if(hasCourseNo) {
				resultMap["has_permissions"] = true
				resultMap["permissions_type"] = 0 // 0：可以提问，1：未购买自考课程，2：没有免费次数
				resultMap["permissions_text"] = "该账号已购买自考课程"
			}
		} else {
			resultMap["permissions_type"] = 2 // 0：可以提问，1：未购买自考课程，2：没有免费次数
			resultMap["permissions_text"] = map["is_show_text"]
		}
		return getResultOK(resultMap)
	}

	/**
	 * 3.0 当月提问数量
	 * 
	 * @param req
	 * @return	free_num : 当月剩余的免费数量  is_free: 是否还有免费次数  free_text:提问文案提醒
	 */
	def topics_free_num(HttpServletRequest req){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//产品类型
		int product = ServletRequestUtils.getIntParameter(req, "product", 0)
		//当月剩余的免费数量
		int free_num
		//提问文案提醒
		String free_text
		String is_show_text
		String is_show_text_tip
		//是否还有免费次数
		Boolean is_free

		if(0 == product) { //会计
			Boolean isStudent =  getIsStudent(user_id);
			if(getIsZJStudent(user_id)){
				free_num = 30
			}else if(getIsStudent(user_id)){
				//当天提问数量
				Integer dnum = topicNumDay(user_id);
				//改为每日
				Integer dtopic_free_num = topic_free_num_by_day(user_id,isStudent);
				free_num = dtopic_free_num - dnum;
			}else{
				//本月提问数量
	//			Integer mnum = topicNumMonth(user_id);
	//			Integer mtopic_free_num = topic_free_num(user_id,isStudent);
				Integer mnum = topicNumDay(user_id);
				Integer mtopic_free_num = topic_free_num_by_day(user_id,isStudent);
				free_num = mtopic_free_num - mnum;
			}
			if(getIsZJStudent(user_id)){
				is_free = true;
			}else if(getIsStudent(user_id)){
				is_free = isTopicFreeByDay(user_id,product)
			}else{
				is_free = isTopicFreeByDay(user_id,product)
			}
			free_text = topic_free_num_text(is_free,isStudent).replace("{FREE_NUM}", free_num+"");
			is_show_text = topic_is_show_text(is_free,isStudent);
			is_show_text_tip = topic_is_show_text_tip(is_free,isStudent);

		} else if(1 == product) { //自考
			Integer mnum = topicNumDayZikao(user_id)
			Integer mtopic_free_num = topic_free_num_by_day_zikao()
			free_num = mtopic_free_num - mnum
			is_free = isTopicFreeByDay(user_id,product)
			free_text = topic_free_num_text_zikao(is_free).replace("{FREE_NUM}", free_num+"")
			is_show_text = topic_is_show_text_zikao(is_free)
			is_show_text_tip = topic_is_show_text_tip_zikao(is_free)
		}

		//提问文案颜色变化
		def map = new HashMap();
		map["free_num"] = free_num;
		map["free_text"] = free_text;
		map["is_free"] = is_free;
		map["is_show"]  = !is_free;
		map["is_show_text"]  = is_show_text;
		map["is_show_text_tip"]  = is_show_text_tip;
		
		return getResultOK(map);
	}





	//生成提问
	//分钟限制
	@TypeChecked(TypeCheckingMode.SKIP)
	def topics_add_v200(HttpServletRequest req){

		return ["code":404, "msg":"请更新到最新版使用", "data":"请更新到最新版使用"]

		try{
			Integer id = Web.getCurrentUserId();

			if(isStudentLimitTime(id)){
				return Code.TOPICS.学生问题时限();
			}
			//提问数量校验
			if(!isTopicFree(id)){
				return Code.TOPICS.免费提问限制();
			}
			java.net.URLDecoder urlDecoder = new java.net.URLDecoder();
			String json = urlDecoder.decode(req["json"], "UTF-8");
			//校验非空
			if(StringUtils.isBlank(json)){
				return getResultParamsError();
			}
			Map jMap = JSONUtil.jsonToMap(json);
			if(StringUtils.isBlank(jMap["content"].toString()) || jMap["content"].toString().length()<10){
				return ["code" : 40092 , "data" : "问题不能少于10个字符"];
			}
			//标签
			List tips = new ArrayList();
			List tresult = new ArrayList();
			if(StringUtils.isNotBlank(jMap["tips_id"]?.toString()) && StringUtils.isNotBlank(jMap["tips_name"]?.toString())){
				//标签
				String[] tips_id = jMap["tips_id"].toString().split(",");
				String[] tips_name = jMap["tips_name"].toString().split(",");
				for(int i = 0 ; i < tips_id.length ; i++){
					//2017-2-9 加入标签权限校验
					//判断是不是中级的学员
					if(getIsZJStudent(id)){//是中级学员

					}else{//普通用户
						Integer tid = Integer.valueOf(Integer.valueOf(tips_id[i]));
						if(tid>=10000 && tid<=10010){//中级学员的标签范围
							return Code.TOPICS.提问标签限制();
						}
					}
					Map tm = new HashMap();
					tm["_id"] = Integer.valueOf(tips_id[i]);
					tm["tip_name"] = tips_name[i];
					tips.add(tm);
					tresult.add(tips_id[i]);
				}
			}

			//2015-10-28 加入校验-标签
			if(tips.size() == 0){
				return getResultParamsError();
			}

			//版本1.5 问题等级 add by shihongjie 2015-12-15
			int vlevel = users().findOne($$("_id" : id) , $$( "vlevel" : 1 ))?.get("vlevel");
			//问题的图片
			List topics_pic = new ArrayList();
			//课程id
			String topic_id = UUID.randomUUID().toString();

			if(VlevelType.V1.ordinal() == vlevel){
				topic_id += KeyUtils.TOPICES.TOPICESLIST_V1;
			}

			long now = System.currentTimeMillis();

			def topic = new HashMap();
			topic["_id"] = topic_id;
			topic["author_id"] = id;
			topic["content"] = jMap["content"];
			topic["author_device_id"] = jMap["device_id"];//学员的设备ID
			//			topic["industry_id"] = Integer.valueOf(jMap["industry_id"].toString());
			topic["_id"] = topic_id;
			topic["timestamp"] = now;
			//问题提交时间
			topic["submit_time"] = now;
			topic["user_info"] = getUserInfo(id);

			topic["tips"] = tips;

			//重新提交次数记录
			topic["resubmit_num"] = 0;
			//打赏给教师的金额
			topic["tip_kd"] = 0d;
			//比例
			topic["tip_fee_prop"] = 0d;
			//学员打赏金额
			topic["tip_kd_full"] = 0d;


			topic["vlevel"] = vlevel;
			topic["teacher_show_type"] = TopicTeacherShowType.undefined.ordinal();
			//			topic["vlevel"] = Web.currentUserVLevel();

			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


			//提问管道类型 0.会答  1.题库
			//			int channel_type = ServletRequestUtils.getIntParameter(req, "channel_type", 0);
			Integer channel_type = jMap["channel_type"] as Integer;

			def tkMap = jMap["tkMap"];

			if(channel_type == null){
				channel_type = 0;
			}

			topic["channel_type"] = channel_type;

			if(channel_type > 0){
				//问题id
				String tk_id = tkMap["tk_id"];
				//商品id
				String tk_com_id = tkMap["tk_com_id"];
				//产品id
				String tk_kind_id = tkMap["tk_kind_id"];
				//章节id
				String tk_chapter_id = tkMap["tk_chapter_id"];
				//备注
				String tk_remark = tkMap["tk_remark"];
				//分享时间
				//				Long tk_timestamp = ServletRequestUtils.getLongParameter(req, "tk_timestamp", now);
				Long tk_timestamp = tkMap["tk_timestamp"];
				if(tk_timestamp == null){
					tk_timestamp = now;
				}

				if(
				StringUtils.isBlank(tk_id) || StringUtils.isBlank(tk_com_id) ||
				StringUtils.isBlank(tk_kind_id) || StringUtils.isBlank(tk_chapter_id)
				){
					return getResultParamsError();
				}

				//题库提问信息
				def tiku_map = new HashMap();
				tiku_map["_id"] = tk_id;
				//问题id
				tiku_map["tk_id"] = tk_id;
				//商品id
				tiku_map["tk_com_id"] = tk_com_id;
				//产品id
				tiku_map["tk_kind_id"] = tk_kind_id;
				//章节id
				tiku_map["tk_chapter_id"] = tk_chapter_id;
				//备注
				tiku_map["tk_remark"] = tk_remark;
				//分享时间
				tiku_map["tk_timestamp"] = tk_timestamp;

				topic["tiku_map"] = tiku_map;
			}

			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


			if(StringUtils.isNotBlank(jMap["files"]?.toString())){

				String files_json = jMap["files"];
				List filesList = JSONUtil.jsonToBean(jMap["files"]?.toString(), ArrayList.class);
				if(filesList){
					filesList.each {Map fm->
						Map map = new HashMap();
						//文件原名称
						map["original_file_name"] = fm["original_file_name"];
						//文件大小
						map["file_size"] = fm["file_size"];
						//文件地址
						map["pic_url"] = fm["url"];
						//文件id
						map["_id"] = fm["_id"];

						topics_pic.add(map);
					}
				}
			}

			////////////////////////////////////////////////////////////////////问题保存//////////////////////////////////////
			//问题图片
			if(topics_pic.size() > 0){
				topic["topics_pic"] = topics_pic;
			}

			//固定数据
			//收藏数量
			topic["collect_count"] = 0;
			//是否删除
			topic["deleted"] = false;
			//回复数量
			topic["reply_count"] = 0;
			//访问数量
			topic["visit_count"] = 0;
			//是否置顶
			topic["top"] = false;

			//类型
			topic["type"] = TopicsType.待抢答.ordinal();
			//更新时间
			topic["update_at"] = now;
			//评论内容
			topic["evaluation"] = null;
			//评论状态
			topic["evaluation_type"] = TopicEvaluationType.未评价.ordinal();
			//抽奖状态
			topic["buns_states"] = TopicBunsStates.未打开.ordinal();
			//保存到数据库
			topics().save($$(topic));

			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////START
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题系统提示
			topics_reply().save(
					$$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : "你的问题已成功提交，会答正在为你匹配抢答教师，平均响应时长3分钟内，本次提问1小时内有效，敬请等待……" ,"reply_time" :  System.currentTimeMillis(),
					"reply_type": ReplyType.系统文字.ordinal() , "topic_id" : topic_id , "user_id" : null,
					"user_pic" : null , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.学员.ordinal()
					)
					);
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题文字内容
			//提问人id
			int author_id = (int) topic["author_id"];
			//			查询提问者的头像
			String author_pic = users().findOne($$("_id" : author_id ) , $$("pic" : 1))?.get("pic");
			//将问题内容保存到聊天表中
			def topic_reply = $$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : topic["content"] ,"reply_time" :  System.currentTimeMillis(),
					"reply_type": ReplyType.文字.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
					"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal()
					)
			topics_reply().save(topic_reply);
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题图片内容
			//			//将问题的图片保存到聊天内容中
			if(topics_pic){
				topics_pic.each { def tdbo ->
					topics_reply().save(
							$$(
							"_id" : UUID.randomUUID().toString() , "reply_content" : tdbo["pic_url"] ,"reply_time" : now,
							"reply_type": ReplyType.图片.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
							"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal()
							)
							);
				}
			}
			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////END


			//redis中学生图片问题
			if(topics_pic.size() > 0){
				topics_pic.each {def m->
					m["pic_url"] = file_url+ m["pic_url"];
				}
				topic["topics_pic"] = topics_pic;
			}

			//保存到redis
			saveRedis(topic);
			//v2.0 提问5分钟限制

			//返回问题id 消息id 行业id 标签信息
			def rmap = new HashMap();
			rmap["topic_id"] = topic_id;
			//			rmap["msg_id"] = msg_id;
			//v200 delete
			//			rmap["industry_id"] = topic["industry_id"].toString();

			rmap["tips"] = tresult;

			//scoreBase.PushScoreMsg(AccScoreGainType.会答提问,author_id,topic["user_info"].get("nick_name").toString());

			return getResultOK(JSONUtil.beanToJson(rmap));
		}finally {
			//			parse.cleanupMultipart(req)
		}
		return getResult(Code.提问保存错误, Code.提问保存错误_S, Code.提问保存错误_S);
	}

	/**
	 * 提问-加入每月提问数量限制
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topics_add_v300(HttpServletRequest req){

		try{
			Integer id = Web.getCurrentUserId();
			if(0 == id) {
				return getResultToken()
			}
			//获取打赏金额
			Double tip_kd_full = ServletRequestUtils.getDoubleParameter(req, "tip_kd", 0);
			//产品类型	0：会计，1：自考
			int product = ServletRequestUtils.getIntParameter(req, "product", 0)


			//提问时间限制校验
			if(isStudentLimitTime(id)){
				return Code.TOPICS.学生问题时限();
			}

			//提问数量校验
			if(tip_kd_full == 0 && !isTopicFreeByDay(id, product)){
				//return ["code" : 40103 ,"msg":"啦啦啦啦啦啦啦啦","data" : "您当月已经没有免费权限了!"];
				return Code.TOPICS.免费提问限制();
			}

			//会计问题未评价限制
			if(0 == product && hasTopicNotEvaluation(id, product)) {
				return ["code":40103, "msg":not_evaluation_text, "data":not_evaluation_text]
			}

			java.net.URLDecoder urlDecoder = new java.net.URLDecoder();
			String json = urlDecoder.decode(req["json"], "UTF-8");
			//校验非空
			if(StringUtils.isBlank(json)){
				return getResultParamsError();
			}
			Map jMap = JSONUtil.jsonToMap(json);
			//println("解析之后的JSON:=============="+jMap)
			if(StringUtils.isBlank(jMap["content"].toString()) || jMap["content"].toString().length()<10){
				return ["code" : 40092 , "data" : "问题不能少于10个字符"];
			}

			//判断余额是否充足
			if(tip_kd_full > 0 && !useKD(id, tip_kd_full)){
				return ["code" : 40090 , "data" : "余额不足"];
			}



			//标签
			List tips = new ArrayList();
			List tresult = new ArrayList();
			if(StringUtils.isNotBlank(jMap["tips_id"]?.toString()) && StringUtils.isNotBlank(jMap["tips_name"]?.toString())){
				//标签
				String[] tips_id = jMap["tips_id"].toString().split(",");
				String[] tips_name = jMap["tips_name"].toString().split(",");
				for(int i = 0 ; i < tips_id.length ; i++){
					/*if(0 == product) { //0：会计，1：自考
						//2017-2-9 加入标签权限校验
						//判断是不是中级的学员
						if(getIsZJStudent(id)){//是中级学员

						}else{//普通用户
							Integer tid = Integer.valueOf(Integer.valueOf(tips_id[i]));
							if(tid>=10000 && tid<=10010){//中级学员的标签范围
								return Code.TOPICS.提问标签限制();
							}
						}
					}*/
					Map tm = new HashMap();
					tm["_id"] = Integer.valueOf(tips_id[i]);
					tm["tip_name"] = tips_name[i];
					tips.add(tm);

					tresult.add(tips_id[i]);
				}
			}

			//2015-10-28 加入校验-标签
			if(tips.size() == 0){
				return getResultParamsError();
			}
			//版本1.5 问题等级 add by shihongjie 2015-12-15
			int vlevel = users().findOne($$("_id" : id) , $$( "vlevel" : 1 ))?.get("vlevel");
			//问题的图片
			List topics_pic = new ArrayList();
			//课程id
			String topic_id = UUID.randomUUID().toString();

			if(VlevelType.V1.ordinal() == vlevel){
				topic_id += KeyUtils.TOPICES.TOPICESLIST_V1;
			}

			long now = System.currentTimeMillis();

			def topic = new HashMap();
			topic["_id"] = topic_id;
			topic["author_id"] = id;
			topic["content"] = jMap["content"];
			topic["author_device_id"] = jMap["device_id"];//学员的设备ID
			//			topic["industry_id"] = Integer.valueOf(jMap["industry_id"].toString());
			topic["_id"] = topic_id;
			topic["timestamp"] = now;
			//问题提交时间
			topic["submit_time"] = now;
			topic["user_info"] = getUserInfo(id);

			topic["tips"] = tips;
			topic["product"] = product


			//打赏给用户的金额
			Double tip_to_user_money = 0d;
			//比例
			Double tip_fee_prop = 0d;
			//是否为打赏提交的问题
			Integer da_shang = TopicsDaShangType.不是的打赏提交的问题.ordinal();
			//V250打赏会豆记录  250版本
			if(tip_kd_full > 0 ){
				def tt = saveTopicTip(id , tip_kd_full , topic_id);
				//打赏给用户的金额
				tip_to_user_money = tt["to_user_money"];
				//比例
				tip_fee_prop = tt["fee_prop"];
				da_shang = TopicsDaShangType.打赏提交的问题.ordinal();
			}
			//是否为打赏提交的问题
			topic["da_shang"] = da_shang;
			//重新提交次数记录
			topic["resubmit_num"] = 0;
			//打赏给教师的金额
			topic["tip_kd"] = tip_to_user_money;
			//比例
			topic["tip_fee_prop"] = tip_fee_prop;
			//学员打赏金额
			topic["tip_kd_full"] = tip_kd_full;
			topic["vlevel"] = vlevel;
			topic["teacher_show_type"] = TopicTeacherShowType.undefined.ordinal();
			//			topic["vlevel"] = Web.currentUserVLevel();

			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


			//提问管道类型 0.会答  1.题库
			//			int channel_type = ServletRequestUtils.getIntParameter(req, "channel_type", 0);
			Integer channel_type = jMap["channel_type"] as Integer;

			def tkMap = jMap["tkMap"];

			if(channel_type == null){
				channel_type = 0;
			}

			topic["channel_type"] = channel_type;

			if(channel_type > 0){
				//问题id
				String tk_id = tkMap["tk_id"];
				//商品id
				String tk_com_id = tkMap["tk_com_id"];
				//产品id
				String tk_kind_id = tkMap["tk_kind_id"];
				//章节id
				String tk_chapter_id = tkMap["tk_chapter_id"];
				//备注
				String tk_remark = tkMap["tk_remark"];
				//分享时间
				//				Long tk_timestamp = ServletRequestUtils.getLongParameter(req, "tk_timestamp", now);
				Long tk_timestamp = tkMap["tk_timestamp"];
				if(tk_timestamp == null){
					tk_timestamp = now;
				}

				if(
				StringUtils.isBlank(tk_id) || StringUtils.isBlank(tk_com_id) ||
				StringUtils.isBlank(tk_kind_id) || StringUtils.isBlank(tk_chapter_id)
				){
					return getResultParamsError();
				}

				//题库提问信息
				def tiku_map = new HashMap();
				tiku_map["_id"] = tk_id;
				//问题id
				tiku_map["tk_id"] = tk_id;
				//商品id
				tiku_map["tk_com_id"] = tk_com_id;
				//产品id
				tiku_map["tk_kind_id"] = tk_kind_id;
				//章节id
				tiku_map["tk_chapter_id"] = tk_chapter_id;
				//备注
				tiku_map["tk_remark"] = tk_remark;
				//分享时间
				tiku_map["tk_timestamp"] = tk_timestamp;

				topic["tiku_map"] = tiku_map;
			}

			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

			Boolean newPicUrl=false;
			if(StringUtils.isNotBlank(jMap["files"]?.toString())){

				String files_json = jMap["files"];

				String strUrl = files_json.toString();
					if(strUrl.substring(0,4)=="http"){
						newPicUrl=true;
						List<String> filesList1 = new ArrayList<>();
						String[] str = strUrl.split(",");
						filesList1 = Arrays.asList(str);
						filesList1.each { String url1 ->
							String url2=url1;
							url1 = url1.substring(url1.indexOf("/", 8) + 1);
							//文件原名
							String original_file_name1 = StringUtils.substringAfterLast(url1, "/");
							String _id1 = original_file_name1.substring(0, original_file_name1.indexOf("."))
							Map fileMap1 = new HashMap();
							fileMap1["original_file_name"] = original_file_name1;
							//文件大小
							fileMap1["file_size"] = 0L;
							//文件地址
							fileMap1["pic_url"] = url2;
							//文件id
							fileMap1["_id"] = _id1;

							topics_pic.add(fileMap1);
						}
					}else{
						List filesList = JSONUtil.jsonToBean(jMap["files"]?.toString(), ArrayList.class);
						if(filesList) {
							filesList.each { Map fm ->
								Map map = new HashMap();
								//文件原名称
								map["original_file_name"] = fm["original_file_name"];
								//文件大小
								map["file_size"] = fm["file_size"];
								//文件地址
								map["pic_url"] = fm["url"];
								//文件id
								map["_id"] = fm["_id"];

								topics_pic.add(map);
							}
						}
					}

			}

			////////////////////////////////////////////////////////////////////问题保存//////////////////////////////////////
			//问题图片
			if(topics_pic.size() > 0){
				topic["topics_pic"] = topics_pic;
			}


			//固定数据
			//收藏数量
			topic["collect_count"] = 0;
			//是否删除
			topic["deleted"] = false;
			//回复数量
			topic["reply_count"] = 0;
			//访问数量
			topic["visit_count"] = 0;
			//是否置顶
			topic["top"] = false;

			//类型
			topic["type"] = TopicsType.待抢答.ordinal();
			//更新时间
			topic["update_at"] = now;
			//评论内容
			topic["evaluation"] = null;
			//评论状态
			topic["evaluation_type"] = TopicEvaluationType.未评价.ordinal();
			//抽奖状态
			topic["buns_states"] = TopicBunsStates.未打开.ordinal();
			//保存到数据库
			topics().save($$(topic));
			
			//println "tips :"+ tresult.get(0)
			
			/*注释推送功能
			//中级学员的问题进行推送
			if(getIsZJStudent(id)){//是中级学员
//			if(tresult.get(0) >= 10000 && tresult.get(0) <= 10010){//中级学员的标签范围
//				//获取所有中级老师
//				def teachers = users().find($$("priv3" : 1),$$("_id" : 1)).toArray();
//				if(teachers){
//					//创建消息体，然后进行推送
//					PushMsgBase pushmsg = new PushMsgBase();
//					pushmsg.msg_title = "您有中级职称新问题未抢答，快快抢答赚收益";
//					pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
//					//print "获取了===========消息推送实体:"
//					int env = Integer.parseInt(AppProperties.get("push.env"));
//					XinggePushService xingge = new XinggePushService();
//					xingge.PushServiceInit(env);
//					int size = teachers.size();
//					int userId 
//					for(int i = 0 ; i<size ; i++){
//						userId = teachers.get(i)["_id"];
//						//println "用户ID："+userId+"进行消息推送";
//						//xingge.PushMsgToSingleTeacher(userId,pushmsg);
//					}
//				}
//			}else{//普通问题
//				//println "普通问题";
//			}
		}else if(getIsStudent(id)){
		//判断中职学员
			 //println "普通学员";
		 }else{
			// println "非学员";
		 }*/
		
			
			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////START
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题系统提示
			String reply_content = ""
			if(0 == product) { //会计
				//reply_content = "你的问题已成功提交，会答正在为你匹配抢答教师，平均响应时长3分钟内，本次提问1小时内有效，敬请等待……"
			} else if(1 == product) { //自考
				reply_content = "同学你好，课程学习上有什么疑问吗？请用文字描述 或 发送图片。"
                topics_reply().save(
                        $$(
                                "_id" : UUID.randomUUID().toString() , "reply_content" : reply_content ,"reply_time" :  System.currentTimeMillis(),
                                "reply_type": ReplyType.系统文字.ordinal() , "topic_id" : topic_id , "user_id" : null,
                                "user_pic" : null , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.学员.ordinal(), "topics_reply_first" : 1
                        )
                );
			}
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题文字内容
			//提问人id
			int author_id = (int) topic["author_id"];
			//			查询提问者的头像
			String author_pic = users().findOne($$("_id" : author_id ) , $$("pic" : 1))?.get("pic");
			//将问题内容保存到聊天表中
			def topic_reply = $$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : topic["content"] ,"reply_time" :  System.currentTimeMillis(),
					"reply_type": ReplyType.文字.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
					"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal(),"topics_reply_first" : 1
					)
			topics_reply().save(topic_reply);
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题图片内容
			//			//将问题的图片保存到聊天内容中
			if(topics_pic){
				topics_pic.each { def tdbo ->
					topics_reply().save(
							$$(
							"_id" : UUID.randomUUID().toString() , "reply_content" : tdbo["pic_url"] ,"reply_time" : now,
							"reply_type": ReplyType.图片.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
							"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal(),"topics_reply_first" : 1
							)
							);
				}
			}
			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////END


			//redis中学生图片问题
			if(topics_pic.size() > 0){
				if(newPicUrl){
					topics_pic.each {def m->
						m["pic_url"] = m["pic_url"];//完整地址
					}
				}else {
					topics_pic.each {def m->
						m["pic_url"] = file_url+ m["pic_url"];//完整地址
						}
					}
				topic["topics_pic"] = topics_pic;
			}

			//保存到redis
			topic["product"] = (topic["product"] == null? 0:topic["product"])
			if(0 == topic["product"] as int) {
				saveRedis(topic)
			} else if(1 == topic["product"] as int) {
				saveRedisZikao(topic)
			}
			//v2.0 提问5分钟限制

			//返回问题id 消息id 行业id 标签信息
			def rmap = new HashMap();
			rmap["topic_id"] = topic_id;
			//			rmap["msg_id"] = msg_id;
			//v200 delete
			//			rmap["industry_id"] = topic["industry_id"].toString();

			rmap["tips"] = tresult;

			//scoreBase.PushScoreMsg(AccScoreGainType.会答提问,author_id,topic["user_info"].get("nick_name").toString());

			return getResultOK(JSONUtil.beanToJson(rmap));
		}finally {
			//			parse.cleanupMultipart(req)
		}
		return getResult(Code.提问保存错误, Code.提问保存错误_S, Code.提问保存错误_S);
	}

	/**
	 * 获取题库提问相似问题列表
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def getTopicListByTikuId(HttpServletRequest request) {
		int size = ServletRequestUtils.getIntParameter(request, "size", 20)
		int page = ServletRequestUtils.getIntParameter(request, "page", 1)
		int product = ServletRequestUtils.getIntParameter(request, "product", 0)
		String tkId = ServletRequestUtils.getStringParameter(request, "tkId", "")

		def topicsList = topics().find(
				$$(
						"tiku_map._id" : tkId, "product" : product, "deleted" : false,
						"type" : TopicsType.问题已结束.ordinal(),
						"evaluation_type" : $$('$in':[TopicEvaluationType.满意.ordinal(), TopicEvaluationType.很满意.ordinal()])
				),
				$$(
						"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 ,
						"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
						"tip_kd" : 1,"da_shang" : 1, "read_num":1, "author_id":1
				)
		).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray()
		if(topicsList) {
			topicsList.each { def dbo ->
				def studentMap = users().findOne($$("_id" : dbo["author_id"]) , $$("pic" : 1))
				if(studentMap) {
					dbo["pic"] = studentMap["pic"]
				}
				dbo["read_num"] = dbo["read_num"] == null ? 0 : dbo["read_num"]
			}
		}
		return getResultOKS(topicsList)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	def topics_add(HttpServletRequest req){

		return ["code":404, "msg":"请更新到最新版使用", "data":"请更新到最新版使用"]

		try{
			Integer id = Web.getCurrentUserId();
			//提问数量校验
			if(!isTopicFree(id)){
				return Code.TOPICS.免费提问限制();
			}
			//			if(isStudentLimitTime(id)){
			//				return Code.TOPICS.学生问题时限();
			//			}

			java.net.URLDecoder urlDecoder = new java.net.URLDecoder();
			String json = urlDecoder.decode(req["json"], "UTF-8");
			//校验非空
			if(StringUtils.isBlank(json)){
				return getResultParamsError();
			}
			Map jMap = JSONUtil.jsonToMap(json);
			if(StringUtils.isBlank(jMap["content"].toString()) || jMap["content"].toString().length()<10){
				return ["code" : 40092 , "data" : "问题不能少于10个字符"];
			}
			//标签
			List tips = new ArrayList();
			List tresult = new ArrayList();
			if(StringUtils.isNotBlank(jMap["tips_id"]?.toString()) && StringUtils.isNotBlank(jMap["tips_name"]?.toString())){
				//标签
				String[] tips_id = jMap["tips_id"].toString().split(",");
				String[] tips_name = jMap["tips_name"].toString().split(",");
				for(int i = 0 ; i < tips_id.length ; i++){
					//2017-2-9 加入标签权限校验
					//判断是不是中级的学员
					if(getIsZJStudent(id)){//是中级学员

					}else{//普通用户
						Integer tid = Integer.valueOf(Integer.valueOf(tips_id[i]));
						if(tid>=10000 && tid<=10010){//中级学员的标签范围
							return Code.TOPICS.提问标签限制();
						}
					}
					Map tm = new HashMap();
					tm["_id"] = Integer.valueOf(tips_id[i]);
					tm["tip_name"] = tips_name[i];
					tips.add(tm);

					tresult.add(tips_id[i]);
				}
			}

			//2015-10-28 加入校验-标签
			if(tips.size() == 0){
				return getResultParamsError();
			}
			//版本1.5 问题等级 add by shihongjie 2015-12-15
			int vlevel = users().findOne($$("_id" : id) , $$( "vlevel" : 1 ))?.get("vlevel");
			//问题的图片
			List topics_pic = new ArrayList();
			//课程id
			String topic_id = UUID.randomUUID().toString();

			if(VlevelType.V1.ordinal() == vlevel){
				topic_id += KeyUtils.TOPICES.TOPICESLIST_V1;
			}

			long now = System.currentTimeMillis();

			def topic = new HashMap();
			topic["_id"] = topic_id;
			topic["author_id"] = id;
			topic["content"] = jMap["content"];
			topic["author_device_id"] = jMap["device_id"];//学员的设备ID
			//			topic["industry_id"] = Integer.valueOf(jMap["industry_id"].toString());
			topic["_id"] = topic_id;
			topic["timestamp"] = now;
			//问题提交时间
			topic["submit_time"] = now;
			topic["user_info"] = getUserInfo(id);

			topic["tips"] = tips;

			//打赏给教师的金额
			topic["tip_kd"] = 0d;
			//比例
			topic["tip_fee_prop"] = 0d;
			//学员打赏金额
			topic["tip_kd_full"] = 0d;
			//重新提交次数记录
			topic["resubmit_num"] = 0;


			topic["vlevel"] = vlevel;
			topic["teacher_show_type"] = TopicTeacherShowType.undefined.ordinal();
			//			topic["vlevel"] = Web.currentUserVLevel();


			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


			//提问管道类型 0.会答  1.题库
			//			int channel_type = ServletRequestUtils.getIntParameter(req, "channel_type", 0);
			Integer channel_type = jMap["channel_type"] as Integer;

			def tkMap = jMap["tkMap"];

			if(channel_type == null){
				channel_type = 0;
			}

			topic["channel_type"] = channel_type;

			if(channel_type > 0){
				//问题id
				String tk_id = tkMap["tk_id"];
				//商品id
				String tk_com_id = tkMap["tk_com_id"];
				//产品id
				String tk_kind_id = tkMap["tk_kind_id"];
				//章节id
				String tk_chapter_id = tkMap["tk_chapter_id"];
				//备注
				String tk_remark = tkMap["tk_remark"];
				//分享时间
				//				Long tk_timestamp = ServletRequestUtils.getLongParameter(req, "tk_timestamp", now);
				Long tk_timestamp = tkMap["tk_timestamp"];
				if(tk_timestamp == null){
					tk_timestamp = now;
				}

				if(
				StringUtils.isBlank(tk_id) || StringUtils.isBlank(tk_com_id) ||
				StringUtils.isBlank(tk_kind_id) || StringUtils.isBlank(tk_chapter_id)
				){
					return getResultParamsError();
				}

				//题库提问信息
				def tiku_map = new HashMap();
				tiku_map["_id"] = tk_id;
				//问题id
				tiku_map["tk_id"] = tk_id;
				//商品id
				tiku_map["tk_com_id"] = tk_com_id;
				//产品id
				tiku_map["tk_kind_id"] = tk_kind_id;
				//章节id
				tiku_map["tk_chapter_id"] = tk_chapter_id;
				//备注
				tiku_map["tk_remark"] = tk_remark;
				//分享时间
				tiku_map["tk_timestamp"] = tk_timestamp;

				topic["tiku_map"] = tiku_map;
			}

			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 1.5版本 题库 提问 add by 史宏杰 2015-12-14  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


			if(StringUtils.isNotBlank(jMap["files"]?.toString())){

				String files_json = jMap["files"];
				List filesList = JSONUtil.jsonToBean(jMap["files"]?.toString(), ArrayList.class);
				if(filesList){
					filesList.each {Map fm->
						Map map = new HashMap();
						//文件原名称
						map["original_file_name"] = fm["original_file_name"];
						//文件大小
						map["file_size"] = fm["file_size"];
						//文件地址
						map["pic_url"] = fm["url"];
						//文件id
						map["_id"] = fm["_id"];

						topics_pic.add(map);
					}
				}
			}

			////////////////////////////////////////////////////////////////////问题保存//////////////////////////////////////
			//问题图片
			if(topics_pic.size() > 0){
				topic["topics_pic"] = topics_pic;
			}

			//固定数据
			//收藏数量
			topic["collect_count"] = 0;
			//是否删除
			topic["deleted"] = false;
			//回复数量
			topic["reply_count"] = 0;
			//访问数量
			topic["visit_count"] = 0;
			//是否置顶
			topic["top"] = false;

			//类型
			topic["type"] = TopicsType.待抢答.ordinal();
			//更新时间
			topic["update_at"] = now;
			//评论内容
			topic["evaluation"] = null;
			//评论状态
			topic["evaluation_type"] = TopicEvaluationType.未评价.ordinal();
			//抽奖状态
			topic["buns_states"] = TopicBunsStates.未打开.ordinal();
			//保存到数据库
			topics().save($$(topic));

			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////START
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题系统提示
			topics_reply().save(
					$$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : "你的问题已成功提交，会答正在为你匹配抢答教师，平均响应时长3分钟内，本次提问1小时内有效，敬请等待……" ,"reply_time" :  System.currentTimeMillis(),
					"reply_type": ReplyType.系统文字.ordinal() , "topic_id" : topic_id , "user_id" : null,
					"user_pic" : null , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.学员.ordinal()
					)
					);
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题文字内容
			//提问人id
			int author_id = (int) topic["author_id"];
			//			查询提问者的头像
			String author_pic = users().findOne($$("_id" : author_id ) , $$("pic" : 1))?.get("pic");
			//将问题内容保存到聊天表中
			def topic_reply = $$(
					"_id" : UUID.randomUUID().toString() , "reply_content" : topic["content"] ,"reply_time" :  System.currentTimeMillis(),
					"reply_type": ReplyType.文字.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
					"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal()
					)
			topics_reply().save(topic_reply);
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  问题图片内容
			//			//将问题的图片保存到聊天内容中
			if(topics_pic){
				topics_pic.each { def tdbo ->
					topics_reply().save(
							$$(
							"_id" : UUID.randomUUID().toString() , "reply_content" : tdbo["pic_url"] ,"reply_time" : now,
							"reply_type": ReplyType.图片.ordinal() , "topic_id" : topic_id , "user_id" : author_id,
							"user_pic" : author_pic , "timestamp" : System.currentTimeMillis() , "show_type" : ReplyShowType.所有人.ordinal()
							)
							);
				}
			}
			///////////////////////////////////////////问题内容保存到聊天内容中//////////////////////////////////////END


			//redis中学生图片问题
			if(topics_pic.size() > 0){
				topics_pic.each {def m->
					m["pic_url"] = file_url+ m["pic_url"];
				}
				topic["topics_pic"] = topics_pic;
			}

			//保存到redis
			saveRedis(topic);
			//v2.0 提问5分钟限制

			//返回问题id 消息id 行业id 标签信息
			def rmap = new HashMap();
			rmap["topic_id"] = topic_id;
			//			rmap["msg_id"] = msg_id;
			//v200 delete
			//			rmap["industry_id"] = topic["industry_id"].toString();

			rmap["tips"] = tresult;

			//生成提问后加积分
			//scoreBase.PushScoreMsg(AccScoreGainType.会答提问,author_id,topic["user_info"].get("nick_name").toString());

			return getResultOK(JSONUtil.beanToJson(rmap));
		}finally {
			//			parse.cleanupMultipart(req)
		}
		return getResult(Code.提问保存错误, Code.提问保存错误_S, Code.提问保存错误_S);
	}


	//学生-用户基本信息
	def getUserInfo(Integer user_id){

		//用户基本信息
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1));
		if(user){
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));
			//已完成提问的数量
			Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			//点评率
			user["evaluation_num"] = all > 0 ? NumberUtil.formatDouble3((double)(evaluation_num / all) , 2) : 0;
			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));


			//判断如果用户是手机号作为昵称，则改掉
			String nick_name = user["nick_name"];
			user["nick_name"] = RegExUtils.mobileReplace(nick_name);


		}
		return user;

	}

	//根据问题id获取问题内容
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_topic_content(HttpServletRequest request)
	{
		//问题id
		String tid = request["tid"];

		def result = topics().findOne($$("_id" : tid));


		def tips = result["tips"];
		if(tips){
			result["tips"] = tips.get(0)["tip_name"];
		}else{
			result["tips"] = "";
		}

		result["timestamp_formate"] = dateFormate((Long)result["timestamp"]);


		def teacherMap = users().findOne($$("_id" : result["teach_id"]) , $$("pic" : 1 , "vlevel" : 1));
		if(teacherMap){
			result["pic"] = teacherMap["pic"];

		}else{
			result["pic"] = "";
		}



		//是否有打赏  0.没有 1.有打赏
		Integer tip_kd_type = 0;
		if(null != result["tip_kd"] && result["tip_kd"] > 0){
			tip_kd_type = 1;
		}
		result["tip_kd_type"] = tip_kd_type;

		return getResultOKS(result);
	}

	/**
	 * 每月提问数量
	 * @param user_id 用户id
	 * @return	该月提问数量
	 */
	private Integer topicNumMonth(Integer user_id){
		long shangxianTime =(long)constants().findOne(new BasicDBObject("dr", 0)).get("topic_free_num_start");
		//判断是否已经上线
		if(System.currentTimeMillis() > shangxianTime){
			long startTime = DataUtils.getTimesMonthmorning().getTime();
			if(startTime < shangxianTime){
				startTime = shangxianTime;
			}
			//本月提问数量
			Integer mnum = topics().count($$(
					"author_id" : user_id , //用户ID
					"timestamp" : $$( '$gt': startTime, '$lt': DataUtils.getTimesMonthnight().getTime() ),//本月时间
					"is_reSubmit" : $$('$in' : [
						null,
						TopicsIsReSubmit.普通的问题.ordinal()
					]),//是否为重新提交的问题
					"da_shang" : $$('$in' : [
						null,
						TopicsDaShangType.不是的打赏提交的问题.ordinal()
					]),//是否为初始打赏提交的问题
					"type" : $$('$in' : [
						TopicsType.待抢答.ordinal(),
						TopicsType.抢答成功.ordinal() ,
						TopicsType.问题已结束.ordinal()
					])//问题状态
					)).intValue();
			return mnum;
		}else{
			return 0 ;
		}
	}

	/**
	 * 用户本月是否有免费提问权限
	 * @param user_id
	 * @return true 有权限
	 */
	private Boolean isTopicFree(Integer user_id){
		Boolean isStudent =  getIsStudent(user_id);
		////本月提问数量
		Integer mnum = topicNumMonth(user_id);
		//本月免费提问数量
		Integer mtopic_free_num = topic_free_num(user_id,isStudent);
		//当月剩余的免费数量
		return mtopic_free_num - mnum > 0;
	}

	/**
	 * 保存在redis_zikao
	 * @Description: 保存在redis_zikao
	 * @date 2015年6月15日 下午5:13:55
	 * @param @param topic
	 * @param @return
	 * @throws
	 */
	def saveRedisZikao(def topic){
		if(topic){
			println "==============================1=============================="
			String key = KeyUtils.TOPICES.tpicesIndustrysZikao(topic["_id"] as String);
			String json = JSONUtil.beanToJson(topic);
			mainRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME_ZIKAO , TimeUnit.MILLISECONDS);
			//			mainRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.HOURS);
			//chat也要存一份，因为要用到键值过期
			chatRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME_ZIKAO , TimeUnit.MILLISECONDS);
			//			chatRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.HOURS);
			//学生提问-5分钟限制
			Integer user_id = topic["author_id"] as Integer;
			if(TEST_USER_ID_ARRAY.indexOf(user_id+"") == -1){
				String limit_key = KeyUtils.TOPICES.topicsStudentKey(user_id);
				mainRedis.opsForValue().set(limit_key,topic["_id"].toString() , KeyUtils.TOPICES.TOPIC_STUDENT_LIMIT_TIME , TimeUnit.MINUTES);
			}

			//存储队列
			String listkey = KeyUtils.TOPICES.TOPICESLIST_ZIKAO;
			String topiceQueueValue = topic["_id"].toString();
			mainRedis.opsForList().leftPush(KeyUtils.TOPICES.TOPICESLIST_ZIKAO, topiceQueueValue);
			println "==============================2=============================="
		}
	}

	/**
	 * 保存在redis
	 * @Description: 保存在redis
	 * @date 2015年6月15日 下午5:13:55 
	 * @param @param topic
	 * @param @return 
	 * @throws
	 */
	def saveRedis(def topic){
		if(topic){
			println "==============================1=============================="
			String key = KeyUtils.TOPICES.tpicesIndustrys(topic["_id"] as String);
			String json = JSONUtil.beanToJson(topic);
			mainRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.MILLISECONDS);
			//			mainRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.HOURS);
			//chat也要存一份，因为要用到键值过期
			chatRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.MILLISECONDS);
			//			chatRedis.opsForValue().set(key, json, KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , TimeUnit.HOURS);
			//学生提问-5分钟限制
			Integer user_id = topic["author_id"] as Integer;
			if(TEST_USER_ID_ARRAY.indexOf(user_id+"") == -1){
				String limit_key = KeyUtils.TOPICES.topicsStudentKey(user_id);
				mainRedis.opsForValue().set(limit_key,topic["_id"].toString() , KeyUtils.TOPICES.TOPIC_STUDENT_LIMIT_TIME , TimeUnit.MINUTES);
			}

			//存储队列
			String listkey = KeyUtils.TOPICES.TOPICESLIST;
			String topiceQueueValue = topic["_id"].toString();
			mainRedis.opsForList().leftPush(KeyUtils.TOPICES.TOPICESLIST, topiceQueueValue);
			println "==============================2=============================="
		}
	}
	/**
	 * 教师提问的时限记录
	 * @param teach_id
	 */
	def teacherLimitTimeSaveRedis(Integer teach_id , String topic_id){
		if(TEST_USER_ID_ARRAY.indexOf(teach_id+"") == -1){
			String key = KeyUtils.TOPICES.topicsTeacherKey(teach_id);
			mainRedis.opsForValue().set(key,topic_id, KeyUtils.TOPICES.TOPIC_TEACHER_LIMIT_TIME , TimeUnit.MINUTES);
		}
	}


	/**
	 * 教师抢答是否还在时限范围
	 * @param user_id
	 * @return true:有限制 false:无限制
	 */
	private boolean isTeachertLimitTime(Integer user_id){
		return false;
		//		String limit_key = KeyUtils.TOPICES.topicsTeacherKey(user_id);
		//		if(mainRedis.hasKey(limit_key)){
		//			return true;
		//		}
		//		return false;
	}

	/**
	 * 学员提问是否还在时限范围
	 * @param user_id
	 * @return true:有限制 false:无限制
	 */
	private boolean isStudentLimitTime(Integer user_id){
		String limit_key = KeyUtils.TOPICES.topicsStudentKey(user_id);
		if(mainRedis.hasKey(limit_key)){
			return true;
		}
		return false;
	}

	//用户id变成string 截取用户前5位id
	def userIdToString(int _id){
		String id = _id + "";
		if(id.length() >= USER_ID_TO_DIR_NUM){
			id = id.substring(USER_ID_TO_DIR_NUM);
		}
		return FILE_HEADER + id;
	}

	// 对扩展名进行小写转换  aa.txt
	public String getEXT(String _file_name) {
		if(StringUtils.isNotBlank(_file_name)){
			return _file_name.substring(_file_name.lastIndexOf(".") + 1, _file_name.length()).toLowerCase();
		}
		return ".png";
	}
	
	
	/**
	 * 每日提问数量
	 * @param user_id 用户id
	 * @return	当日提问数量
	 */
	private Integer topicNumDay(Integer user_id){
		long shangxianTime =(long)constants().findOne(new BasicDBObject("dr", 0)).get("topic_free_num_start");
		//判断是否已经上线
		if(System.currentTimeMillis() > shangxianTime){
			long startTime = DataUtils.getTimesmorning().getTime();
			if(startTime < shangxianTime){
				startTime = shangxianTime;
			}
			//当天提问数量
			Integer mnum = topics().count($$(
					"author_id" : user_id , //用户ID
					"timestamp" : $$( '$gt': startTime, '$lt': DataUtils.getTimesnight().getTime() ),//今天时间
					"product" : $$('$in' : [
							null,0
					]),//会计
					"is_reSubmit" : $$('$in' : [
						null,
						TopicsIsReSubmit.普通的问题.ordinal()
					]),//是否为重新提交的问题
					"da_shang" : $$('$in' : [
						null,
						TopicsDaShangType.不是的打赏提交的问题.ordinal()
					]),//是否为初始打赏提交的问题
					"type" : $$('$in' : [
						TopicsType.待抢答.ordinal(),
						TopicsType.抢答成功.ordinal() ,
						TopicsType.问题已结束.ordinal()
					])//问题状态
					)).intValue();
			return mnum;
		}else{
			return 0 ;
		}
	}

	/**
	 * 每日提问数量-自考
	 * @param user_id 用户id
	 * @return	当日提问数量-自考
	 */
	private Integer topicNumDayZikao(Integer user_id){
		long shangxianTimeZikao = constants().findOne($$("dr",0),$$("topic_free_num_start_zikao",1)).get("topic_free_num_start_zikao") as long
		//判断是否已经上线
		if(System.currentTimeMillis() > shangxianTimeZikao){
			long startTime = DataUtils.getTimesmorning().getTime();
			if(startTime < shangxianTimeZikao){
				startTime = shangxianTimeZikao;
			}
			//当天提问数量
			Integer mnum = topics().count($$(
					"author_id" : user_id , //用户ID
					"timestamp" : $$( '$gt': startTime, '$lt': DataUtils.getTimesnight().getTime() ),//今天时间
					"product" : 1,//自考
					"is_reSubmit" : $$('$in' : [
							null,
							TopicsIsReSubmit.普通的问题.ordinal()
					]),//是否为重新提交的问题
					"type" : $$('$in' : [
							TopicsType.待抢答.ordinal(),
							TopicsType.抢答成功.ordinal() ,
							TopicsType.问题已结束.ordinal()
					])//问题状态
			)).intValue();
			return mnum;
		}else{
			return 0 ;
		}
	}
	
	/**
	 * 用户当天是否有免费提问权限
	 * @param user_id
	 * @return true 有权限
	 */
	private Boolean isTopicFreeByDay(Integer user_id, int product){
		//当天提问数量
		Integer dnum;
		//当天免费提问数量
		Integer dtopic_free_num;
		if(0 == product) { //会计
			dnum = topicNumDay(user_id);
			Boolean isStudent =  getIsStudent(user_id);
			dtopic_free_num = topic_free_num_by_day(user_id,isStudent);
		} else if(1 == product) { //自考
			dnum = topicNumDayZikao(user_id);
			dtopic_free_num = topic_free_num_by_day_zikao()
		}
		//当天剩余的免费数量
		return dtopic_free_num - dnum > 0;
	}
	
	
	/**
	 * 抢答问题v_150可以抢答自考和会计的问题
	 * @author Vince
	 * @param user_id
	 * @return true 有权限
	 */
	//v150 抢答完后由服务端通知学生端抢答成功
	@TypeChecked(TypeCheckingMode.SKIP)
	def getTopic_v150(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//v2.0 教师抢答时限校验
		if(isTeachertLimitTime(user_id)){
			return Code.TOPICS.教师抢答时限();
		}
		//
		Integer topicsKey = 1;
		String topicsList = KeyUtils.TOPICES.TOPICESLIST;
		if(user_id == 0){
			return getResult(30405 , "token过期" , null);
		}else{
//			//用户标签信息
//			def user_industry_zikao = null;
//			user_industry_zikao = users().findOne($$("_id" : user_id) , $$("user_industry_zikao" : 1))?.get("user_industry_zikao");//标签
			def business_id = null;
			business_id = users().findOne($$("_id" : user_id) , $$("business_id" : 1))?.get("business_id");//业务线
			if (business_id!=null && "zikao".equals(business_id.get(0))){//自考
				//println("获取自考问题");
				topicsKey =2
				topicsList=KeyUtils.TOPICES.TOPICESLIST_ZIKAO;
			}else if(business_id!=null && "kuaiji".equals(business_id.get(0))){//会计
				//println("无标签获取会计问题");
				topicsKey =1
				topicsList=KeyUtils.TOPICES.TOPICESLIST;
			}
		}
//
		//问题id
		String tid = request["tid"];
		//v2.0加入抢答时限
		//		teacherLimitTimeSaveRedis(user_id , tid);
		//抢答者的设备id
		String device_id = request["device_id"];


		def valOp = mainRedis.opsForValue();
		def valOp1 = chatRedis.opsForValue();



		if(StringUtils.isNotBlank(tid)){
			String key=null;
			if(topicsKey==2){
				key = KeyUtils.TOPICES.tpicesIndustrysZikao(tid);
			}else{
				key = KeyUtils.TOPICES.tpicesIndustrys(tid);
			}
			String sjson = null;
			String requestId = user_id+""
			int lockSum = 0
			while (true) {
				++lockSum
				if(tryGetDistributedLock(getJedis(),LOCK_KEY,requestId,2000)) {
					logger.info("用户："+user_id+" 成功获得分布式锁")
					lock.lock();
					try {
						//获取问题json
						sjson = valOp.get(key);

						if(sjson != null){
							//删除redis中问题json
							mainRedis.delete(key);

							int len = mainRedis.opsForList().size(topicsList);
							for(int i=0;i<len;i++)
							{

								String insideKey = mainRedis.opsForList().index(topicsList, i);

								if(insideKey == tid)
								{
									mainRedis.opsForList().remove(topicsList, i, tid);
									break;
								}
							}


						}
						if(valOp1.get(key) != null){
							chatRedis.delete(key);
						}
					}finally{
						//显示释放锁
						lock.unlock();
						if(releaseDistributedLock(getJedis(),LOCK_KEY,requestId)) {
							logger.info("用户："+user_id+" 成功释放分布式锁")
						}
					}
					break
				} else {
					if(lockSum > 25) {
						logger.info("用户："+user_id+" 第"+lockSum+"次无法获得分布式锁，放弃抢锁！！！")
						break
					}
					logger.info("用户："+user_id+" 第"+lockSum+"次无法获得分布式锁，休眠100毫秒，继续抢锁！！！")
					Thread.sleep(100)
				}
			}

			if(sjson != null){
				Long now = System.currentTimeMillis();
				//更新问题状态
				Integer number = topics().update(
						$$("_id" : tid , "type" : TopicsType.待抢答.ordinal()),
						$$($set : $$("type": TopicsType.抢答成功.ordinal(),"race_time" : now , "teach_id" : user_id , "update_at" : now , "teach_device_id" : device_id))
						).getN();

				if(number == 0){
					return getResult(Code.已被抢答S, Code.已被抢答_S, Code.已被抢答_S);
				}

				/**  ******************************* 维护 users老师抢答数量 *********************************/
				users().update(
						$$("_id" : user_id),
						$$('$inc':$$("topic_count" : 1))
						);
				/**  ******************************* 维护 users老师抢答数量 end*********************************/

				//学员id
				String student_id = topics().findOne($$("_id" : tid), $$("author_id" : 1))?.get("author_id");

				//聊天超时时间
				chatSaveRedis(tid);
				//向学员端产生系统消息 Xxx已经抢答了您的问题，为您解答。
				getChatSystemMsg(tid , now , student_id);

				//抢答老师信息
				Map user = Web.currentUser();

				getTopicSendStudent(
						tid ,
						String.valueOf(user_id), String.valueOf(user["nick_name"]) , String.valueOf(user["pic"]),
						student_id , now
						);

				return getResultOKS("抢答成功");
			}else{
				return getResult(Code.已被抢答S, Code.已被抢答_S, Code.已被抢答_S);
			}
		}
		return getResultParamsError();
	}
	
	
	
	/**
	 * 我的提问列表(会计和自考)
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def myTopicList_v320(HttpServletRequest request){
		int product = ServletRequestUtils.getIntParameter(request, "product", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if(0 == user_id) {
			return getResultToken()
		}
		def topicsList = null;
		if(product==0){
			topicsList = topics().find(
				$$("author_id" : user_id ,"product":$$('$ne' : 1), "deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
				"tip_kd" : 1,"da_shang" : 1
				)
				).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
		}else if(product==1){
			topicsList = topics().find(
				$$("author_id" : user_id ,"product" : product , "deleted" : false),
				$$(
				"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
				"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
				"tip_kd" : 1,"da_shang" : 1
				)
				).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
		}
		
		/*if(topicsList){
			int px = 0;
			for(int i =0;i<topicsList.size;i++){
				if(topicsList.get(i)["type"] == 2){
					def dbo = topicsList.get(i);
					topicsList.remove(i);
					topicsList.add(px, dbo)
					px++;
				}
				if(topicsList.get(i)["type"] == 3){
					break;
				}
			}
			int allSize = topicsList.size();
			if(allSize>(size*page)){
				topicsList = topicsList.subList((page-1)*size, size*page);
			}else if(allSize>(size*(page-1))){
				topicsList = topicsList.subList((page-1)*size, topicsList.size());
			}else{
				topicsList.clear();
			}
		}*/

		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				//				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1))?.get("pic");

				//add by shihongjie 2015-12-22 VIP_ICON
				def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1 , "nick_name" : 1));
				if(teacherMap){
					dbo["pic"] = teacherMap["pic"];
					//vlevle
					dbo["vip_icon"] = VlevelType.vipIcon(teacherMap["vlevel"]);
					dbo["teacher_name"] = teacherMap["nick_name"];
				}else{
					dbo["vip_icon"] = false;
					dbo["teacher_name"] = "等待老师抢答...";
					if(dbo["type"]){
						if(dbo["type"]==1){
							dbo["teacher_name"] = "暂无老师抢答";
						}
					}
				}

				//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
				dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

				def tips = dbo["tips"];
				if(tips){
					dbo["tips"] = tips.get(0)["tip_name"];
				}else{
					dbo["tips"] = "";
				}

				//过滤掉换行符
				dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

				//是否有打赏  0.没有 1.有打赏
				Integer tip_kd_type = 0;
				if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
					tip_kd_type = 1;
				}
				dbo["tip_kd_type"] = tip_kd_type;

				//是否是直接打赏提交的问题  0.不是 1.是
				Integer da_shang = 0;
				if(null != dbo["da_shang"] && dbo["da_shang"] == 1){
					da_shang = 1;
				}
				dbo["da_shang"] = da_shang;
				Integer can_del = 0;
				if(dbo["type"]){
					if(dbo["type"]==1){
						can_del = 1
					}else{
						can_del = 0
					}
				}
				dbo["can_del"] = can_del;
			}
		}
		return getResultOKS(topicsList);
	}



	//获取评价弹窗提示文案
	@TypeChecked(TypeCheckingMode.SKIP)
	def topicEndWin(HttpServletRequest request) {
//		String accessToken = ServletRequestUtils.getStringParameter(request, "access_token");
		String topicId = ServletRequestUtils.getStringParameter(request, "topic_id");

		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (StringUtils.isBlank(topicId)) {
			return getResultParamsError();
		}
		def topice = topics().findOne($$("_id": topicId), $$("_id": 1, "end_type": 1));
        if (null==topice) {
            return getResultParamsError();
        }
		int endType = topice["end_type"];
		def relsult;
		if (endType==TopicEndType.学生结束.ordinal()) {
			relsult = constants().aggregate($$($match:$$("dr":0)), $$($project:$$("content":"\$topic_end_win_content_by_student","contentTip":"\$topic_end_win_tip"))).results().iterator();
		} else if (endType==TopicEndType.老师结束.ordinal()) {
            relsult = constants().aggregate($$($match:$$("dr":0)), $$($project:$$("content":"\$topic_end_win_content_by_teacher","contentTip":"\$topic_end_win_tip"))).results().iterator();
		} else if (endType==TopicEndType.问题超时.ordinal()) {
            relsult = constants().aggregate($$($match:$$("dr":0)), $$($project:$$("content":"\$topic_end_win_content_by_sys","contentTip":"\$topic_end_win_tip"))).results().iterator();
		} else {
            relsult = constants().aggregate($$($match:$$("dr":0)), $$($project:$$("content":"\$topic_end_win_content_by_sys","contentTip":"\$topic_end_win_tip"))).results().iterator();
		}
        def content = 0;
        def contentTip = 0;
        def res=[:];
        while(relsult.hasNext()){
            def finaObj = relsult.next();
            content  = finaObj.get("content");
            contentTip =finaObj.get("contentTip");
            break;
        }
        res.put("content",content);
        res.put("contentTip",contentTip);
        res.put("endType",endType);

		return getResultOKS(res);
	}


	/**
	 * 模糊查询(会计和自考)
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def searchTopicByKeyword(HttpServletRequest request) {
		int product = ServletRequestUtils.getIntParameter(request, "product", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 10);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		String keyword = ServletRequestUtils.getStringParameter(request, "keyword");
		def result = [:];
		def topicsMyMap = [:];
		def topicsOtherMap = [:];
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (0 == user_id) {
			return getResultToken()
		}
		if (StringUtils.isBlank(keyword) || page == 0) {
			return getResultParamsError();
		}
		insertKeywordQueryRecord(product,keyword);
		if (page<2) {
			int topicsListSize = 0;
			if (product == 0) {
				topicsListSize = topics().count(
						$$("author_id": user_id, "product": $$('$ne': 1), "deleted": false, "content": $$('$regex': keyword, '$options': 'i')));
			} else if (product == 1) {
				topicsListSize = topics().count(
						$$("author_id": user_id, "product": product, "deleted": false, "content": $$('$regex': keyword, '$options': 'i')));
			}
			//more标识是否有更多，0没有，1有
			Integer more = 0;

			if (topicsListSize > 0) {
				def topicsList = formatMyTopicList(product, keyword, user_id, 2, 1);
				if (topicsListSize >= 3) {
					more = 1
				}
				topicsMyMap.put("more", more);
				topicsMyMap.put("title", "我的问题");
				topicsMyMap.put("keyword", keyword);
				topicsMyMap.put("dataList", topicsList);

				result.put("myTopics", topicsMyMap);
			}
		}
		long nowTime=System.currentTimeMillis();
		long counTime=nowTime-15638400000;

		int otherTopicsListSize=0;
		if(product==0){
			otherTopicsListSize = topics().count(
					$$("update_at":$$('$gt':counTime, '$lt':nowTime),"evaluation_type":$$('$in':[2,3]),"product":0, "deleted" : false,"content": $$('$regex': keyword, '$options':'i'),"author_id":$$('$ne':user_id)));
		}else if(product==1){
			otherTopicsListSize = topics().count(
					$$("update_at":$$('$gt':counTime, '$lt':nowTime),"evaluation_type":$$('$in':[2,3]),"product":1, "deleted" : false,"content": $$('$regex': keyword, '$options':'i'),"author_id":$$('$ne':user_id)));
		}
		//more1总页数
		Integer more1 = 0;
		if(otherTopicsListSize>0){
			def otherTopicsList = formatOtherTopicList(product,keyword,user_id,size,page);
			more1 = (otherTopicsListSize/10)+1;
			topicsOtherMap.put("totalPage",more1);
			topicsOtherMap.put("title", "其他问题");
			topicsOtherMap.put("keyword", keyword);
			topicsOtherMap.put("dataList", otherTopicsList);

			result.put("otherTopics", topicsOtherMap);
		}

		return getResultOKS(result);
	}



	/**
	 * 获取格式化我的问题列表数据
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private def formatMyTopicList(int product,String keyword ,int user_id,int size,int page){
		def topicsList = null;
		if(product==0){
			topicsList = topics().find(
					$$("author_id" : user_id ,"product":0, "deleted" : false,"content": $$('$regex': keyword, '$options':'i')),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size).toArray();
		}else if(product==1){
			topicsList = topics().find(
					$$("author_id" : user_id ,"product" : 1 , "deleted" : false,"content": $$('$regex': keyword, '$options':'i')),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size).toArray();
		}

		if(topicsList){
			formatTopicList(topicsList);
		}
		return topicsList;
	}

	/**
	 * 获取格式化其他问题列表数据
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private def formatOtherTopicList(int product,String keyword ,int user_id,int size,int page){
//		println(product+"GG"+keyword+"GG"+user_id+"GG"+size+page);
		long nowTime=System.currentTimeMillis();
		long counTime=nowTime-15638400000;
		def topicsList = null;
		if(product==0){
			topicsList = topics().find(
					$$("update_at":$$('$gt':counTime, '$lt':nowTime),"evaluation_type":$$('$in':[2,3]),"product":0, "deleted" : false,"content": $$('$regex': keyword),"author_id":$$('$ne':user_id)),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 )).skip((page - 1) * size).limit(size).toArray();
		}else if(product==1){
			topicsList = topics().find(
					$$("update_at":$$('$gt':counTime, '$lt':nowTime),"evaluation_type":$$('$in':[2,3]),"product":1, "deleted" : false,"content": $$('$regex': keyword),"author_id":$$('$ne':user_id)),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 )).skip((page - 1) * size).limit(size).toArray();
		}

		if(topicsList){
			formatTopicList(topicsList);
		}
		return topicsList;
	}


	/**
	 * 我的问题列表模糊查询(会计和自考)
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def searchMyTopicByKeyword(HttpServletRequest request){
		int product = ServletRequestUtils.getIntParameter(request, "product", 0);
		int size = ServletRequestUtils.getIntParameter(request, "size", 10);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		String keyword = ServletRequestUtils.getStringParameter(request, "keyword");

		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (0 == user_id) {
			return getResultToken()
		}
		if (StringUtils.isBlank(keyword) || page == 0) {
			return getResultParamsError();
		}

		def topicsList = null;
		if(product==0){
			topicsList = topics().find(
					$$("author_id" : user_id ,"product":0, "deleted" : false,"content": $$('$regex': keyword, '$options':'i')),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size).toArray();
		}else if(product==1){
			topicsList = topics().find(
					$$("author_id" : user_id ,"product" : 1 , "deleted" : false,"content": $$('$regex': keyword, '$options':'i')),
					$$(
							"_id" : 1 ,"teach_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 ,
							"evaluation" : 1 , "type" : 1 , "timestamp" : 1 , "evaluation_type" : 1 , "update_at" : 1,
							"tip_kd" : 1,"da_shang" : 1
					)
			).sort($$("update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size).toArray();
		}

		if(topicsList){
			formatTopicList(topicsList);
		}
		return getResultOKS(topicsList);
	}

	/**
	 * 格式化数据
	 * @param topicsList
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private def formatTopicList(def topicsList){
		topicsList.each { def dbo ->
			def teacherMap = users().findOne($$("_id" : dbo["teach_id"]) , $$("pic" : 1 , "vlevel" : 1 , "nick_name" : 1));
			if(teacherMap){
				dbo["pic"] = teacherMap["pic"];
				//vlevle
				//dbo["vip_icon"] = VlevelType.vipIcon(teacherMap["vlevel"]);
				//优化vlevle
				dbo["vip_icon"] = false;
				dbo["teacher_name"] = teacherMap["nick_name"];
			}else{
				dbo["vip_icon"] = false;
				dbo["teacher_name"] = "等待老师抢答...";
				if(dbo["type"]){
					if(dbo["type"]==1){
						dbo["teacher_name"] = "暂无老师抢答";
					}
				}
			}

			//				dbo["timestamp_formate"] = dateFormate((Long)dbo["timestamp"]);
			dbo["timestamp_formate"] = TopicsDataUtils.timeFormatter((Long)dbo["timestamp"]);

			def tips = dbo["tips"];
			if(tips){
				dbo["tips"] = tips.get(0)["tip_name"];
			}else{
				dbo["tips"] = "";
			}

			//过滤掉换行符
			dbo["content"] = dbo["content"].toString().replace("\r", "").replace("\n", "");

			//是否有打赏  0.没有 1.有打赏
			/*Integer tip_kd_type = 0;
			if(null != dbo["tip_kd"] && dbo["tip_kd"] > 0){
				tip_kd_type = 1;
			}
			dbo["tip_kd_type"] = tip_kd_type;*/
			//优化是否有打赏
			dbo["tip_kd_type"] = 0;
			//是否是直接打赏提交的问题  0.不是 1.是
			/*Integer da_shang = 0;
			if(null != dbo["da_shang"] && dbo["da_shang"] == 1){
				da_shang = 1;
			}
			dbo["da_shang"] = da_shang;*/
			//优化是否是直接打赏提交的问题
			dbo["da_shang"]=0;
			Integer can_del = 0;
			if(dbo["type"]){
				if(dbo["type"]==1){
					can_del = 1
				}else{
					can_del = 0
				}
			}
			dbo["can_del"] = can_del;
		}
		return topicsList;
	}
	/**
	 * 新增模糊查询记录
	 * @author Vince
	 * @param user_id product(0会计，1自考),keyword关键字
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def insertKeywordQueryRecord(int product,String keyword){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if(0 == user_id) {
			return getResultToken()
		}
		if (StringUtils.isBlank(keyword)) {
			return getResultParamsError();
		}
		BasicDBObject keywordQueryRecord = new BasicDBObject();

		keywordQueryRecord.append("_id", UUID.randomUUID().toString());
		keywordQueryRecord.append("user_id", user_id);
		keywordQueryRecord.append("keyword", keyword);
		keywordQueryRecord.append("create_time", System.currentTimeMillis());
		keywordQueryRecord.append("dr", 0);
		keywordQueryRecord.append("product", product);
		fuzzy_query_record().save(keywordQueryRecord);
		return getResultOK("新增记录成功");
	}

	/**
	 * 查询模糊记录接口
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def getKeywordQueryRecord(HttpServletRequest request) {
		int product = ServletRequestUtils.getIntParameter(request, "product", 0);
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (0 == user_id) {
			return getResultToken()
		}

		def keywordQueryRecord = fuzzy_query_record().find(
				$$("user_id": user_id, "product": product, "dr": 0),
				$$("_id": 1, "keyword": 1)
		).sort($$("create_time": -1))?.toArray();
		List resule = new ArrayList();
		List keywordList = new ArrayList();
		for(it in keywordQueryRecord){
			def obj = it["keyword"]
			if (keywordList.contains(obj)) {
			}else {
				keywordList.add(obj)
				resule.add(it)
				if(keywordList.size()>9){
					return getResultOKS(resule);
				}
			}

		}
		return getResultOKS(resule);
	}

	/**
	 * 删除模糊记录接口
	 * @author Vince
	 * @param user_id product(0会计，1自考)
	 * @return true 有权限
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def delKeywordQueryRecord(HttpServletRequest request){
		int product = ServletRequestUtils.getIntParameter(request, "product", 0);
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if(0 == user_id) {
			return getResultToken()
		}

		fuzzy_query_record().update(
				$$("user_id" : user_id,"product" : product),
				$$('$set':$$("dr" : 1)),false,true
		);

		return getResultOK("删除记录成功");
	}

	/**
	 *消息撤回接口
	 * @param req
	 * @return
	 */
	def messageRecall(HttpServletRequest request) {
		String msgId = ServletRequestUtils.getStringParameter(request, "msgId");
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (0 == user_id) {
			return getResultToken()
		}
		if (StringUtils.isBlank(msgId)) {
			return getResultParamsError();
		}
		def topicsReply = topics_reply().findOne(
				$$("_id": msgId),
				$$("_id": 1, "topic_id": 1));

		println(topicsReply);
		def topics = topics().findOne(
				$$("_id": topicsReply["topic_id"]),
				$$("_id": 1, "type": 1));
		println(topics)
		if (2 == topics.get("type")) {
			topics_reply().update(
					$$("_id": msgId),
					$$($set: $$("reply_status": 1))
			);
		} else {
			return getResultParamsError();
		}


		return getResultOKS();

	}
}

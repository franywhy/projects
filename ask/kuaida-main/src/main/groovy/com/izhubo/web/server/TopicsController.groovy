package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import com.hqonline.model.Privs
import com.izhubo.common.doc.Param
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.Code
import com.izhubo.model.CurrencyGainType
import com.izhubo.model.DR
import com.izhubo.model.Nodes
import com.izhubo.model.TopicBunuOpenType
import com.izhubo.model.TopicContentType
import com.izhubo.model.TopicEndType
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicTeacherShowType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType
import com.izhubo.model.VlevelType
import com.izhubo.model.node.ChatBaseMsg
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.BMatch;
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.topics.utils.TopicsDataUtils
import com.izhubo.utils.RegExUtils
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.currency.CurrencyController
import com.izhubo.web.msg.MsgController
import com.izhubo.web.vo.TopicReplyVO
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject



/**
 * 
 * @ClassName: MainController 
 * @Description: api接口
 * @author shihongjie
 * @date 2015年5月21日 下午2:26:25 
 *
 */
@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class TopicsController extends BaseController{

	@Resource
	private MsgController msgController;
	@Resource
	private EarningsController earningsController;
	@Resource
	private CurrencyController currencyController;

	private static Logger logger = LoggerFactory.getLogger(TopicsController.class);



	@Value("#{application['push.env']}")
	private int PUSH_ENV;

	@Resource
	KGS   msgKGS

	//初始化文件服务器地址
	String file_url ;
	@Value("#{application['pic.domain']}")
	void setFileUrl(String file_url){
		this.file_url = file_url;
	}
	Lock lock = new ReentrantLock();

	public DBCollection topics() {
		return mainMongo.getCollection("topics");
	}
	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	private DBCollection industry(){
		return mainMongo.getCollection("industry");
	}
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	private DBCollection topic_tip(){
		return mainMongo.getCollection("topic_tip");
	}
	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}

	public void topicBunusTimeoutProcess(String key)
	{
		println "==============================topicBunusTimeoutProcess id:" + key;
		String topic_id = key.split(':')[2];
		if(StringUtils.isNotBlank(topic_id)){

			def topic = topics().findOne(
					$$("_id" : topic_id, "buns_states" : 0 ,
					"type" : TopicsType.问题已结束.ordinal() ,
					"teacher_show_type" : TopicTeacherShowType.红包.ordinal() ,
					"evaluation_type" : $$('$in' :
					[
						TopicEvaluationType.满意.ordinal() ,
						TopicEvaluationType.很满意.ordinal()]
					)
					)
					);
			if(topic){
				//更新红包开启状态
				topics().update(
						$$("_id" : topic_id, "buns_states" : 0 ,
						"type" : TopicsType.问题已结束.ordinal() ,
						"teacher_show_type" : TopicTeacherShowType.红包.ordinal() ,
						"evaluation_type" : $$('$in' :
						[
							TopicEvaluationType.满意.ordinal() ,
							TopicEvaluationType.很满意.ordinal()]
						)
						),
						$$($set :
						$$(
						"teacher_show_type" : TopicTeacherShowType.金额.ordinal()  ,
						"buns_states" :1
						//							,
						//							"end_type" : TopicEndType.问题超时.ordinal()
						)
						)
						);

				Long now = System.currentTimeMillis();
				Integer teach_id = topics["teach_id"];
				topic_bunus().update(
						$$("_id" : KeyUtils.BUNUS.bunusTimeOutVal(topic_id, teach_id)),
						$$('$set' :
						$$("open_time" : now , "open_type" : TopicBunuOpenType.超时开启.ordinal())
						)
						);

				earningsController.bunus_logs_save(topic_id, teach_id, TopicBunuOpenType.超时开启.ordinal(), "topicBunusTimeoutProcess");
			}
		}
	}

	/**
	 * @Description:  问题聊天中超时
	 * @date 2015年8月26日 下午7:34:41
	 * @param @param topickey
	 * @throws
	 */
	public void chatTimeoutProcess(String topickey)
	{
		println "==============================chatTimeoutProcess topickey:" + topickey;
		String topic_id = topickey.split(':')[1];
		if(StringUtils.isNotBlank(topic_id)){
			//问题是否未结束
			def topic = topics().findOne($$("_id" : topic_id , "type" : TopicsType.抢答成功.ordinal()), $$("_id" : 1 ));
			if(topic){
				//问题状态修改为结束
				topics().update(
						$$("_id" : topic_id , "type" : TopicsType.抢答成功.ordinal()),
						$$($set : $$("type" : TopicsType.问题已结束.ordinal() , "update_at" : System.currentTimeMillis() , "end_type" : TopicEndType.问题超时.ordinal()))
						);
				//结束之后，存入超时键值，如果1个小时后未评价，则发送提醒消息。
				String key = KeyUtils.TOPICES.topicsPJRemindKey(topic_id);
				mainRedis.opsForValue().set(key, "", KeyUtils.TOPICES.TOPIC_PJ_REMIND_LIMIT_TIME , TimeUnit.HOURS);
				//chat也要存一份，因为要用到键值过期
				chatRedis.opsForValue().set(key, "", KeyUtils.TOPICES.TOPIC_PJ_REMIND_LIMIT_TIME , TimeUnit.HOURS);

			}
		}


	}




	//	/**
	//	 * 测试 问题提交超时
	//	 * @Description: 问题提交超时
	//	 * @date 2015年7月27日 上午10:46:22
	//	 */
	//	def topicTimeout(HttpServletRequest request){
	//
	////		提问id
	//		String topic_id = request["topic_id"];
	//		topicTimeoutProcess(topic_id);
	//		return getResultOK();
	//	}

	/**
	 *  问题提交超时
	 * @Description:  问题提交超时
	 * @date 2015年8月26日 下午7:34:41 
	 * @param @param topickey 
	 * @throws
	 */
	public void topicTimeoutProcess(String topickey)
	{
		println "==============================topicTimeoutProcess topickey:" + topickey
		String topic_id = topickey.split(':')[1];
		if(StringUtils.isNotBlank(topic_id)){
			Long now = System.currentTimeMillis();
			topics().update(
					$$("_id" : topic_id , "type" : TopicsType.待抢答.ordinal()),
					$$($set : $$("type" : TopicsType.抢答失败.ordinal() , "update_at" : now))
					);

			Integer user_id = (Integer) topics().findOne($$("_id" : topic_id ) , $$("author_id" : 1))?.get("author_id");
			//问题超时后，清除问题队列内容
			int len = mainRedis.opsForList().size(KeyUtils.TOPICES.TOPICESLIST);
			for(int i=0;i<len;i++)
			{
				String insideKey = mainRedis.opsForList().index(KeyUtils.TOPICES.TOPICESLIST, i);
				if(insideKey == topickey)
				{
					mainRedis.opsForList().remove(KeyUtils.TOPICES.TOPICESLIST, i, topickey);//删除队列的内容
					break;
				}
			}

			//TODO 会豆退回
			returnKD(topic_id ,user_id );
			//发消息
			ChatBaseMsg msg = new ChatBaseMsg();
			msg.initMA(Nodes.NODE_MODULE_TOPIC_CHAT, Nodes.NODE_ACTION_TOPIC_CHAT_WAITING_TIMEOUT);
			//问题id
			msg.setTopic_id(topic_id);
			//时间
			msg.setTimestamp(now);
			List<String> tousers = new ArrayList();
			tousers.add(String.valueOf(user_id));
			msgController.SendMsg(tousers, msg);

		}
	}
	/**
	 * 退回会豆
	 * @param topic_id 问题id
	 * @param user_id  用户id
	 * @return
	 */
	def returnKD(String topic_id , Integer user_id){
		def tlist = topic_tip().find($$("topic_id" : topic_id , "from_user_id" : user_id , "dr" : DR.正常.ordinal())).limit(1).toArray();
		if(tlist != null && tlist.size() > 0){
			def titem = tlist[0];
			//会豆使用记录标示作废
			topic_tip().update($$("_id" :titem["_id"]), $$('$set' : $$("dr" : DR.删除.ordinal())));
			//			topic_tip().update($$("topic_id" : topic_id , "from_user_id" : user_id , "dr" : DR.正常.ordinal()), $$('$set' : $$("dr" : DR.删除.ordinal())));
			//通知会豆返账
			currencyController.increaseCurrency(user_id, titem["kd"], CurrencyGainType.打赏退回);
		}

	}

	/**
	 *  评价超时
	 * @Description:  评价超时：问题结束1个小时后，尚未评价，会触发超时
	 * @date 2015年8月26日 下午7:34:41
	 * @param @param topickey
	 * @throws
	 */
	public void topicPJTimeoutProcess(String topickey)
	{
		println "==============================topicPJTimeoutProcess topickey:" + topickey
		String topic_id = topickey.split(':')[1];
		if(StringUtils.isNotBlank(topic_id)){

			Integer user_id = (Integer) topics().findOne($$("_id" : topic_id ) , $$("author_id" : 1))?.get("author_id");

			msgController.pushRemindMsg(user_id,topic_id);
		}
	}

	/**
	 * 学生-粉丝详情
	 * @Description: 粉丝详情
	 * @date 2015年7月16日 下午3:14:37 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def sfansInfo(HttpServletRequest request){
		int user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);
		int size = ServletRequestUtils.getIntParameter(request , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);

		Map data = new HashMap();
		if(user_id > 0){
			if(page == 1){
				data["userInfo"] = userInfo(user_id);
			}else{
				data["userInfo"] = null;
			}
			data["topicList"] = userTopicList(user_id, size, page , false);
			return getResultOKS(data);
		}
		return getResultParamsError();

	}

	/**
	 * 老师-粉丝详情
	 * @Description: 粉丝详情
	 * @date 2015年7月16日 下午3:14:37 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def tfansInfo(HttpServletRequest request){
		int user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);
		int size = ServletRequestUtils.getIntParameter(request , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);

		Map data = new HashMap();
		if(user_id > 0){
			if(page == 1){
				data["userInfo"] = userInfo(user_id);
			}else{
				data["userInfo"] = null;
			}
			data["topicList"] = userTopicList(user_id, size, page , true);
			return getResultOKS(data);
		}
		return getResultParamsError();

	}

	/**
	 * 学员详情
	 * @Description: 学员详情 
	 * @date 2015年6月25日 下午7:47:20 
	 */
	def userInfo(int user_id){
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1));
		if(user){
			//学员详情中昵称如果是手机号码则修改
			user["nick_name"] = RegExUtils.mobileReplace(user["nick_name"]);
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));
			//已完成提问的数量
			Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			//点评率
			//				user["evaluation_num"] = NumberUtil.formatDouble3(evaluation_num / all , 2);
			user["evaluation_num"] = all > 0 ? NumberUtil.formatDouble3((double)(evaluation_num / all) , 2) : 0;

			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));

		}
		return user;
	}

	/**
	 * 学生-粉丝详情
	 * @Description: 粉丝详情
	 * @date 2015年7月16日 下午3:14:37
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	def sfansInfo_android_v150(HttpServletRequest request){
		int user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);
		int size = ServletRequestUtils.getIntParameter(request , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);

		Map data = new HashMap();
		if(user_id > 0){
			if(page == 1){
				data["userInfo"] = userInfo_android_v150(user_id);
			}else{
				data["userInfo"] = null;
			}
			data["topicList"] = userTopicList(user_id, size, page , false);
			return getResultOKS(data);
		}
		return getResultParamsError();

	}

	/**
	 * 老师-粉丝详情
	 * @Description: 粉丝详情
	 * @date 2015年7月16日 下午3:14:37
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	def tfansInfo_android_v150(HttpServletRequest request){
		int user_id = ServletRequestUtils.getIntParameter(request , "user_id" , 0);
		int size = ServletRequestUtils.getIntParameter(request , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);

		Map data = new HashMap();
		if(user_id > 0){
			if(page == 1){
				data["userInfo"] = userInfo_android_v150(user_id);
			}else{
				data["userInfo"] = null;
			}
			data["topicList"] = userTopicList(user_id, size, page , true);
			return getResultOKS(data);
		}
		return getResultParamsError();

	}

	/**
	 * 学员详情
	 * @Description: 学员详情 
	 * @date 2015年6月25日 下午7:47:20 
	 */
	def userInfo_android_v150(int user_id){
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1 , "vlevel" : 1));
		if(user){
			//学员详情中昵称如果是手机号码则修改
			user["nick_name"] = RegExUtils.mobileReplace(user["nick_name"]);
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));
			//已完成提问的数量
			Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			//点评率
			//				user["evaluation_num"] = NumberUtil.formatDouble3(evaluation_num / all , 2);
			user["evaluation_num"] = all > 0 ? NumberUtil.formatDouble3((double)(evaluation_num / all) , 2) : 0;

			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));

			//add by shihongjie 2015-12-24 VIP_ICON
			//vlevle
			user["s_vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
			user.removeField("vlevel")
		}
		return user;
	}

	//学员提问列表
	def userTopicList(int user_id , int size , int page , boolean isTeacher){
		def query = $$("author_id" : user_id , "deleted" : false , "type" : TopicsType.问题已结束.ordinal());
		if(!isTeacher){
			query.append("evaluation_type",   $$('$in' :[
				TopicEvaluationType.满意.ordinal() ,
				TopicEvaluationType.很满意.ordinal()
			]));
		}
		def topicsList = topics().find(
				query,
				$$("_id" : 1 ,"teach_id" : 1 , "content" : 1 , "tips" : 1  , "timestamp" : 1)
				).sort($$("timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(topicsList){
			topicsList.each { def dbo ->
				//头像
				dbo["pic"] = users().findOne($$("_id" : dbo["teach_id"]) , $$( "pic" : 1 ))?.get("pic");
				//				dbo["pic"] = user["pic"];

				dbo["industry_name"] = industry().findOne($$("_id" : dbo["industry_id"]), $$("industry_name" : 1))?.get("industry_name");

				dbo["tips"] = "关键词：" + dbo["tips"]?.get(0)["tip_name"];
			}
		}
		return topicsList;
	}


	//s	获取老师信息
	def find_teach_list(HttpServletRequest request){
		return getResultOK(users().find($$("priv" : UserType.教学老师.ordinal()), $$("_id" : 1 , "online" : 1 , "industry_tip_id" : 1)).toArray());
	}


	//判断当前id是否存在于用户的tag列表中
	def isContainTag(String tagId,BasicDBList tagslist)
	{
		Boolean result = false;

		if(tagslist == null)
			return result;

		for(int i=0;i<tagslist.size();i++)
		{
			BasicDBList itemlist = tagslist.get(i).get("users_industry_tip");
			for(int j=0;j<itemlist.size();j++)
			{
				BasicDBObject db = itemlist.get(j);

				if(db.get("industry_tip_id").toString() == tagId)
				{
					result = true;
				}
			}
		}

		return result;
	}

	/**
	 * @Description: 获取最近一条未抢答消息/获取最新的未抢答问题
	 * @date 2015年5月27日 上午9:57:14 
	 * 2017年2月28日  放弃该接口  停止对教师端1.5版本的维护
	 */
	def find_topic(HttpServletRequest request){
		/*		String jdata = request["jdata"];
		 String token = request["access_token"].toString();
		 if(StringUtils.isNotBlank(jdata)){
		 Map jmap = JSONUtil.jsonToMap(jdata);
		 //问题列表
		 List keylist = jmap["keylist"];
		 String listKey = KeyUtils.TOPICES.TOPICESLIST;
		 int len = mainRedis.opsForList().size(listKey);
		 String jsonResult = null;
		 int user_id = 0;
		 user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
		 if(user_id>0)
		 {
		 //             java.util.List userprivs = getCurrentUserPrivs(user_id);
		 //
		 //			 if(userprivs.get(Priv.教学老师.ordinal()) == 0)
		 //			 {
		 //				return getResultOK(jsonResult);
		 //			 }
		 //判断教师是否有抢答权限
		 if(!isPrivs(user_id, Privs.抢答))
		 {
		 return getResultOK(jsonResult);
		 }
		 logger.info(token + " start xunhuan");
		 int logid = 0;
		 for(int i=0;i<len;i++)
		 {
		 String insideKey = mainRedis.opsForList().index(listKey, i);
		 logid = i;
		 if(!keylist.contains(insideKey))
		 {
		 String topkey = KeyUtils.TOPICES.tpicesIndustrys(insideKey);
		 jsonResult =  mainRedis.opsForValue().get(topkey);
		 if(jsonResult != null)
		 {
		 break;
		 }
		 }
		 }
		 logger.info(token + " end xunhuan"+logid);
		 return getResultOK(jsonResult);
		 }
		 }
		 else*/
		//{
		return 	getResult(30405 , "token过期" , null);
		//}


		//return getResultParamsError();
	}
	/**
	 * @Description: 获取最近一条未抢答消息/获取最新的未抢答问题
	 * @date 2015年5月27日 上午9:57:14 
	 */
	def find_topic_v200(HttpServletRequest request){
		String jdata = request["jdata"];
		String token = request["access_token"].toString();

		if(StringUtils.isNotBlank(jdata)){
			Map jmap = JSONUtil.jsonToMap(jdata);
			//问题列表
			List keylist = jmap["keylist"];

			String listKey = KeyUtils.TOPICES.TOPICESLIST;
			int len = mainRedis.opsForList().size(listKey);
			String jsonResult = null;



			logger.info(token + " start finduserid");
			int user_id = 0;
			user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
			logger.info(token + " end find userid");
			if(user_id>0)
			{
				Map jMap = null;
				//				java.util.List userprivs = getCurrentUserPrivs(user_id);
				//				//抢答老师权限判断
				//				if(userprivs.get(Priv.教学老师.ordinal()) == 0)
				//				{
				//					return getResultOK(jsonResult);
				//				}
				if(!isPrivs(user_id, Privs.抢答))
				{
					return getResultOK(jsonResult);
				}

				logger.info(token + " start xunhuan");


				int logid = 0;
				for(int i=0;i<len;i++){
					String insideKey = mainRedis.opsForList().index(listKey, i);
					logid = i;
					//问题里面的标签。包含了用的的标签
					if(!keylist.contains(insideKey))
					{
						String topkey = KeyUtils.TOPICES.tpicesIndustrys(insideKey);
						jsonResult =  mainRedis.opsForValue().get(topkey);
						if(jsonResult != null)
						{
							//TODO 时间处理
							jMap = JSONUtil.jsonToMap(jsonResult);
							List listTip = jMap["tips"];
							Map MapTip = listTip[0];
							Integer tid = MapTip["_id"]
							//判断是不是中级的老师
							if (isPrivs(user_id, Privs.特殊标签老师)){//是中级老师
								List list = new ArrayList();
								//用户基本信息
								def user_industry = null;
								user_industry = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");		//行业
								if(user_industry){
									//行业名称
									user_industry.each { def tdbo ->
										//标签名称
										def users_industry_tip = tdbo["users_industry_tip"];
										if(users_industry_tip){
											users_industry_tip.each { def tdbosub ->
												list.add(tdbosub["industry_tip_id"]);
											}
										}
									}
								}

								if(list.contains(tid)){//中级老师的标签范围


								}else{
									jMap = null;
									continue;
								}
							}else{//普通用户
								if((tid>=0 && tid<7000)|| (tid>=8000 && tid<10000)){//普通老师的标签范围,7000是中央财大，10000是中级

								}else{
									jMap = null;
									continue;
								}
							}

							Long submit_time = jMap["submit_time"];
							if(submit_time){
								//问题要结束,显示突出颜色
								Double mi = BMatch.sub(System.currentTimeMillis() , submit_time);
								jMap["time_text"] = TopicsDataUtils.msTomD(mi);
								if(mi < KeyUtils.TOPICES.TPICES_TIME_LIMIT_TEXT){
									//									jMap["time_text"] = TopicsDataUtils.msTom(mi);
									jMap["time_colour"] = "999999";
								}else{
									//									jMap["time_text"] = TopicsDataUtils.timeFormatter(update_at);
									jMap["time_colour"] = "F16D60";
								}

								/*								int mi = System.currentTimeMillis() - submit_time;
								 jMap["time_text"] = TopicsDataUtils.msTom(mi);
								 if(mi < KeyUtils.TOPICES.TPICES_TIME_LIMIT_TEXT){
								 //									jMap["time_text"] = TopicsDataUtils.msTom(mi);
								 jMap["time_colour"] = "999999";
								 }else{
								 //									jMap["time_text"] = TopicsDataUtils.timeFormatter(update_at);
								 jMap["time_colour"] = "F16D60";
								 }
								 */									
							}else{//没有submit_time这个字段
								jMap["time_text"] ="~";
								jMap["time_colour"] = "999999";
							}

							if(null == jMap["tip_kd"]){
								jMap["tip_kd"] = 0;
							}
							break;
						}
					}
				}
				logger.info(token + " end xunhuan"+logid);

				return getResultOK(jMap);
			}
		}
		else
		{
			return 	getResult(30405 , "token过期" , null);
		}


		return getResultParamsError();
	}
	
	
	
	
	/**
	 * @Description: 获取最近一条未抢答消息/获取最新的未抢答问题
	 * @author Vince
	 * @date 2017年10月25日 下午5:57:14
	 * @remark (原find_topic_v300由于兼容自考问题，修改为find_topic_v310)
	 */
	def find_topic_v310(HttpServletRequest request){
		String jdata = request["jdata"];
		String token = request["access_token"].toString();
		String labelId = request["labelId"];
		//labelId
		if(StringUtils.isNotBlank(jdata)){
			Map jmap = JSONUtil.jsonToMap(jdata);
			//问题列表
			List keylist = jmap["keylist"];

			String listKey = KeyUtils.TOPICES.TOPICESLIST;
			int len = mainRedis.opsForList().size(listKey);
			String jsonResult = null;

			logger.info(token + " start finduserid");
			int user_id = 0;
			user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
			logger.info(token + " end find userid");
			if(user_id>0)
			{
				Map jMap = null;
				//				java.util.List userprivs = getCurrentUserPrivs(user_id);
				//				//抢答老师权限判断
				//				if(userprivs.get(Priv.教学老师.ordinal()) == 0)
				//				{
				//					return getResultOK(jsonResult);
				//				}
				if(!isPrivs(user_id, Privs.抢答))
				{
					return getResultOK(jsonResult);
				}

				logger.info(token + " start xunhuan");


				int logid = 0;
				for(int i=len-1;i>=0;i--){
//				for(int i=0;i<len;i++){//修改为倒序
					String insideKey = mainRedis.opsForList().index(listKey, i);
					logid = i;
					if(!keylist.contains(insideKey))
					{
						String topkey = KeyUtils.TOPICES.tpicesIndustrys(insideKey);
						jsonResult =  mainRedis.opsForValue().get(topkey);
						if(jsonResult != null)
						{
							//TODO 时间处理
							jMap = JSONUtil.jsonToMap(jsonResult);
							List listTip = jMap["tips"];
							Map MapTip = listTip[0];
							Integer tid = MapTip["_id"]
							//如果传入标签筛选为空(原流程)，不为空必须相等，否者重新获取题目
							if(null==labelId || "".equals(labelId) || labelId.equals("0")){
							}else if(labelId.equals(tid.toString())){
							}else {
								jMap = null;
								continue;
							}

							//判断是不是有标签
							//用户基本信息
							def user_industry = null;
							user_industry = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");//行业
							if (user_industry){//有标签
								List list = new ArrayList();
									//行业名称
									user_industry.each { def tdbo ->
										//标签名称
										def users_industry_tip = tdbo["users_industry_tip"];
										if(users_industry_tip){
											users_industry_tip.each { def tdbosub ->
												list.add(tdbosub["industry_tip_id"]);
											}
										}
									}


								if(list.contains(tid)){//老师的标签范围之内
									
								}else{
									jMap = null;
									continue;
								}
							}else{//普通用户
								jMap = null;
								return getResultOK(null);
							}
							Long submit_time = jMap["submit_time"];
							if(submit_time){
								final Double timeFormat = 23 * 3600 * 1000L;//时间格式处理1小时以上转换为小时
								//问题要结束,显示突出颜色
								Double mi = BMatch.sub(System.currentTimeMillis() , submit_time);

								if(mi>timeFormat){
									jMap["time_text"] = TopicsDataUtils.msTomD(mi);
								}else {
									jMap["time_text"] = TopicsDataUtils.hsTomD(mi);
								}
								if(mi < KeyUtils.TOPICES.TPICES_TIME_LIMIT_TEXT){
									//jMap["time_text"] = TopicsDataUtils.msTom(mi);
									//jMap["time_colour"] = "999999";
									jMap["time_colour"] = "F16D60";
								}else{
									//jMap["time_text"] = TopicsDataUtils.timeFormatter(update_at);
									jMap["time_colour"] = "F16D60";
								}

								/*								int mi = System.currentTimeMillis() - submit_time;
								 jMap["time_text"] = TopicsDataUtils.msTom(mi);
								 if(mi < KeyUtils.TOPICES.TPICES_TIME_LIMIT_TEXT){
								 //									jMap["time_text"] = TopicsDataUtils.msTom(mi);
								 jMap["time_colour"] = "999999";
								 }else{
								 //									jMap["time_text"] = TopicsDataUtils.timeFormatter(update_at);
								 jMap["time_colour"] = "F16D60";
								 }
								 */
							}else{//没有submit_time这个字段
								jMap["time_text"] ="~";
								jMap["time_colour"] = "999999";
							}

							if(null == jMap["tip_kd"]){
								jMap["tip_kd"] = 0;
							}
							break;
						}
					}
				}
				
				logger.info(token + " end xunhuan"+logid);

				return getResultOK(jMap);
			}
		}
		else
		{
			return 	getResult(30406 , "jdata不能为空" , null);
		}


		return getResultParamsError();
	}
	

	
	/**
	 * @Description: 获取最近一条未抢答消息/获取最新的未抢答问题(自考)
	 * @author Vince
	 * @date 2017年11月13日 上午9:57:14
	 */
	def find_zk_topic_v100(HttpServletRequest request){
		String jdata = request["jdata"];
		String token = request["access_token"].toString();
		String labelId = request["labelId"];

		if(StringUtils.isNotBlank(jdata)){
			Map jmap = JSONUtil.jsonToMap(jdata);
			//问题列表
			List keylist = jmap["keylist"];
			String listKey = KeyUtils.TOPICES.TOPICESLIST_ZIKAO;//自考的
			int len = mainRedis.opsForList().size(listKey);
			String jsonResult = null;

			logger.info(token + " start finduserid");
			int user_id = 0;
			user_id =Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
			logger.info(token + " end find userid");
			if(user_id>0)
			{
//				println("55:用户ID不为空")
				Map jMap = null;
				if(!isPrivs(user_id, Privs.抢答))
				{
					return getResultOK(jsonResult);
				}

				logger.info(token + " start xunhuan");


				int logid = 0;
				for(int i=len-1;i>=0;i--){
//				for(int i=0;i<len;i++){//修改为倒序
					String insideKey = mainRedis.opsForList().index(listKey, i);
					logid = i;
					if(!keylist.contains(insideKey))
					{
						String topkey = KeyUtils.TOPICES.tpicesIndustrysZikao(insideKey);//自考
						jsonResult =  mainRedis.opsForValue().get(topkey);
						if(jsonResult != null)
						{
							//TODO 时间处理
							jMap = JSONUtil.jsonToMap(jsonResult);
							List listTip = jMap["tips"];
							Map MapTip = listTip[0];
							Integer tid = MapTip["_id"]

							//如果传入标签筛选为空(原流程)，不为空必须相等，否者重新获取题目
							if(null==labelId || "".equals(labelId)){
							}else if(labelId.equals(tid.toString())){
							}else {
								jMap = null;
								continue;
							}

							//判断是不是有标签
							//用户基本信息
							def user_industry_zikao = null;
							user_industry_zikao = users().findOne($$("_id" : user_id) , $$("user_industry_zikao" : 1))?.get("user_industry_zikao");//行业
							if (user_industry_zikao){//有标签
								List list = new ArrayList();
									//行业名称
									user_industry_zikao.each { def tdbo ->
										//标签名称
										def users_industry_tip = tdbo["users_industry_tip"];
										if(users_industry_tip){
											users_industry_tip.each { def tdbosub ->
												list.add(tdbosub["industry_tip_id"]);
											}
										}
									}
								if(list.contains(tid)){//老师的标签范围
									
								}else{
									jMap = null;
									continue;
								}
							}else{//普通用户
								jMap = null;
								continue;
							}

							Long submit_time = jMap["submit_time"];
							if(submit_time){
								//问题要结束,显示突出颜色
								Double mi = BMatch.sub(System.currentTimeMillis() , submit_time);
								jMap["time_text"] = TopicsDataUtils.hsTomD(mi);
								if(mi < KeyUtils.TOPICES.TPICES_TIME_LIMIT_TEXT){
									//									jMap["time_text"] = TopicsDataUtils.msTom(mi);
									jMap["time_colour"] = "999999";
								}else{
									//									jMap["time_text"] = TopicsDataUtils.timeFormatter(update_at);
									jMap["time_colour"] = "F16D60";
								}
							}else{//没有submit_time这个字段
								jMap["time_text"] ="~";
								jMap["time_colour"] = "999999";
							}

							if(null == jMap["tip_kd"]){
								jMap["tip_kd"] = 0;
							}
							break;
						}
					}
				}
				
				
				logger.info(token + " end xunhuan"+logid);

				return getResultOK(jMap);
			}
		}
		else
		{
			return 	getResult(30405 , "token过期" , null);
		}

		return getResultParamsError();
	}

	
	
	//s 查询该问题是否被其他老师抢答
	def isLock_topic(HttpServletRequest request){
		return getResultOK(isLockTopic(request["_id"]));
	}



	/**
	 * 
	 * @Description: 更新-记录该问题的抢答老师
	 * @date 2015年5月25日 下午2:25:31 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def updata_topic_teach(HttpServletRequest request){
		//id
		String _id = request["_id"];
		//老师编号
		String teach_id = request["teach_id"];

		if(StringUtils.isBlank(_id) || StringUtils.isBlank(teach_id)){
			return [ code : Code.参数无效 , msg : Code.参数无效_S ];
		}
		lock.lock();//
		try{
			if(isLockTopic(_id)){
				return [code : Code.已被抢答 , msg : Code.已被抢答_S ];
			}else{
				BasicDBObject topic = new BasicDBObject();
				topic.append("teach_id" , teach_id);
				topic.append("lock" , true);
				topic.append("race_time" , System.currentTimeMillis());
				topics().update(new BasicDBObject("_id" : _id , "lock" : false),new BasicDBObject('$set' : topic));

				return getResultOK();
			}
		}finally{
			//显示释放锁
			lock.unlock();
		}
	}

	/**
	 * 问题是否被抢答
	 * @param _id 问题被抢答 
	 */
	private boolean isLockTopic(String _id){
		def topic = topics().findOne(
				new BasicDBObject("_id" : _id),
				new BasicDBObject("lock":1)
				)
		if(topic){
			return topic.get("lock");
		}
		return true;
	}

	//c 获取问题
	//	问题编号
	def find_topic_byId(HttpServletRequest request){
		return find_topic_reply(request);
	}

	//c 获取问题及回复的内容
	//	问题编号
	def find_topic_reply(HttpServletRequest request){
		//TODO 数据格式jdata & _id
		String _id = request["_id"];
		if(StringUtils.isNotBlank(_id)){
			def topic = topics.findOne(
					$$("_id" : _id , "deleted" : false),
					$$("deleted" : 0 , "top" : 0 , "evaluation" : 0)
					);
			return getResultOK(topic);
		}
		return getResultParamsError();
	}



	/**
	 * 
	 * @Description: 保存答案 
	 * @see com.izhubo.web.vo.TopicReplyVO
	 * @date 2015年5月25日 下午2:10:46 
	 * @param @param request jdata:问题编号，老师编号，回复编号，回答内容，回答时间
	 * @param @return 
	 * @throws
	 */
	def add_topic_reply(HttpServletRequest request){
		String jdata = request["jdata"];
		if(StringUtils.isNotBlank(jdata)){
			TopicReplyVO vo = JSONUtil.jsonToBean(jdata, TopicReplyVO.class);
			//查询
			def topic = topics.findOne($$("_id" : vo.getTopic_id() , "teach_id" : vo.getUser_id() , "deleted" : false));
			if(topic){
				long now = System.currentTimeMillis();
				//最后回复时间
				topic["last_reply_at"] = now;
				if(TopicContentType.文字.ordinal().equals(vo.getReply_type())){
					//最后回复内容
					topic["last_reply_content"] = vo.getReply_content();
				}else if(TopicContentType.图片.ordinal().equals(vo.getReply_type())){
					//最后回复内容
					topic["last_reply_content"] = "[图片]";
				}else if(TopicContentType.语音.ordinal().equals(vo.getReply_type())){
					//最后回复内容
					topic["last_reply_content"] = "[语音]";
				}
				//回复内容列表
				BasicDBList topics_reply = topic["topics_reply"];
				if(topics_reply == null){
					topics_reply = new BasicDBList();
				}
				Map reply = new HashMap();
				//id
				reply["_id"] = topics_reply.size();
				//回复内容
				reply["reply_content"] = vo.getReply_content();
				//回复时间
				reply["reply_time"] = now;
				//回复类型
				reply["reply_type"] = vo.getReply_type();
				//回复类型
				reply["topic_id"] = vo.getReply_type();

				topics_reply.add(reply);
				//回复数量
				topic["reply_count"] =topics_reply.size();
				//更新
				topics.update($$("_id" : vo.getTopic_id()), $$("$set" : topic));
				return getResultOK();
			}
		}

		return getResultParamsError();
	}

	File file_folder

	@Value("#{application['file.folder']}")
	void setVideoFolder(String folder){
		file_folder = new File(folder)
		file_folder.mkdirs()
		println "初始化文件上传: ${folder}"
	}

	def con(){
		return getResultOK();
	}

	def upload_pic(HttpServletRequest request){
		def parse = new CommonsMultipartResolver()
		def req = parse.resolveMultipart(request)

		try{
			Integer id = Web.getCurrentUserId()
			String type = req.getParameter(Param.first)

			String filePath = "${id&63}/${id&7}/${id}_${type}.jpg"
			for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
				MultipartFile file = entry.getValue()
				def target = new File(pic_folder ,filePath)
				target.getParentFile().mkdirs()
				file.transferTo(target)
				break
			}
			[code: 1,data: [pic_url:"${pic_domain}${filePath}?v=${System.currentTimeMillis()}".toString()]]
		}finally {
			parse.cleanupMultipart(req)
		}
	}


	/**
	 * @Description: 获取最近一条未抢答消息/获取最新的未抢答问题(总接口适配自考和会计)
	 * @author Vince
	 * @date 2017年11月13日 上午10:57:14
	 * @remark 覆盖原来的find_topic_v300
	 */
	def find_topic_v300(HttpServletRequest request){
		String token = request["access_token"].toString();

		logger.info(token + " start finduserid");
		int user_id = 0;
		user_id = Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
		logger.info(token + " end find userid");
		if(user_id == 0){
			return getResult(30405 , "token过期" , null);
		}else{
//			//用户标签信息
//			def user_industry_zikao = null;
//			user_industry_zikao = users().findOne($$("_id" : user_id) , $$("user_industry_zikao" : 1))?.get("user_industry_zikao");//自考标签
			def business_id = null;
			business_id = users().findOne($$("_id" : user_id) , $$("business_id" : 1))?.get("business_id");//业务线
			if (business_id!=null && "zikao".equals(business_id.get(0))){
				//println("获取自考问题");
				return find_zk_topic_v100(request);
			}else if(business_id!=null && "kuaiji".equals(business_id.get(0))){
				//println("获取会计问题");
				return find_topic_v310(request);
			}else{
				//println("无标签获取会计问题");
				return find_topic_v310(request);//普通用户
			}
		}
	}

	/**
	 * @Description: 获取老师标签列表信息
	 * @author Vince
	 */
	def getTeacherLabelInit(HttpServletRequest request){
		String token = request["access_token"].toString();
		Integer user_id = 0;
		user_id = Integer.valueOf(mainRedis.opsForHash().get("token:"+token,"_id") == null?0:mainRedis.opsForHash().get("token:"+token,"_id"));
		def user = users().findOne(
				$$("_id": user_id, "priv": 2)
		)
		List reult = new ArrayList();
		/*先添加默认标签默认标签*/
		List defList = new ArrayList();
		Map<String, Object> defclassMap = new HashMap<String, Object>();
		defclassMap.put("_id", 0);
		defclassMap.put("tip_name", "全部问题");
		defList.add(defclassMap)
		Map<String, Object> defMap = new HashMap<String, Object>();
		defMap.put("id", 0);
		defMap.put("tip_name", "默认标签");
		defMap.put("second_tip", defList);
		reult.add(defMap);

		if (user) {
			def businessName = user.get("business_id");
			if (businessName) {
				if (("kuaiji").equals(businessName.get(0))) {
					def user_industry = user["user_industry"].get(0).get("users_industry_tip")
					user_industry.each { item ->
						Integer user_label_id = item.get("industry_tip_id");
						//子类标签
						Map<String, Object> subMap = new HashMap<String, Object>();
						List subList = new ArrayList();
						def subclassLabel = tip_content().findOne(
								$$("_id": user_label_id, "dr": 0)
						);
						if (subclassLabel) {
							subMap.put("_id", subclassLabel.get("_id"));
							subMap.put("tip_name", subclassLabel.get("tip_name"));
							subList.add(subMap)
							//夫类标签
							Map<String, Object> parMap = new HashMap<String, Object>();
							Integer parent_tip_id = subclassLabel.get("parent_tip_id")

							def paralabel = tip_content().findOne(
									$$("_id": parent_tip_id, "dr": 0)
							);
							parMap.put("id", paralabel.get("_id"));
							parMap.put("tip_name", paralabel.get("tip_name"));
							parMap.put("second_tip", subList);
							//判断该夫类是否已经在结果类中存在
							Boolean haveP = false;
							Integer num = 0;
							for (int i = 0; i < reult.size(); i++) {
								def resLab = reult.get(i);
								Integer lId = resLab["id"]
								if (parent_tip_id == lId) {
									haveP = true;
									num = i;
									break;
								}
							}
							//存在add，不存在新增
							if (haveP) {
								reult.get(num).get("second_tip").add(subMap);
							} else {
								reult.add(parMap);
							}
						}
					}
				} else if (("zikao").equals(businessName.get(0))) {
					println(user["user_industry_zikao"])
					def user_industry = user["user_industry_zikao"].get(0).get("users_industry_tip")
					user_industry.each { item ->
						Integer user_label_id = item.get("industry_tip_id");
						//子类标签
						Map<String, Object> subMap = new HashMap<String, Object>();
						List subList = new ArrayList();
						def subclassLabel = tip_content().findOne(
								$$("_id": user_label_id, "dr": 0)
						);
						if (subclassLabel) {
							subMap.put("_id", subclassLabel.get("_id"));
							subMap.put("tip_name", subclassLabel.get("tip_name"));
							subList.add(subMap)
							//夫类标签
							Map<String, Object> parMap = new HashMap<String, Object>();
							Integer parent_tip_id = subclassLabel.get("parent_tip_id")

							def paralabel = tip_content().findOne(
									$$("_id": parent_tip_id, "dr": 0)
							);
							parMap.put("id", paralabel.get("_id"));
							parMap.put("tip_name", paralabel.get("tip_name"));
							parMap.put("second_tip", subList);
							//判断该夫类是否已经在结果类中存在
							Boolean haveP = false;
							Integer num = 0;
							for (int i = 0; i < reult.size(); i++) {
								def resLab = reult.get(i);
								Integer lId = resLab["id"]
								if (parent_tip_id == lId) {
									haveP = true;
									num = i;
									break;
								}
							}
							//存在add，不存在新增
							if (haveP) {
								reult.get(num).get("second_tip").add(subMap);
							} else {
								reult.add(parMap);
							}
						}
					}
				} else {
					return getResultOK(reult);
				}
			}else {
				return getResultOK(reult);
			}
		}
		return getResultOK(reult);
	}

}

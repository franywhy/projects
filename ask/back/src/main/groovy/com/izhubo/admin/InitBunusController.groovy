package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.transaction.Transactional

import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value

import com.izhubo.admin.msg.MsgController
import com.izhubo.model.ReplyShowType
import com.izhubo.model.ReplyType
import com.izhubo.model.TopicBunsStates
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.node.Chat
import com.izhubo.model.node.Nodes
import com.izhubo.model.node.TopicChatMsg
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.StaticSpring
import com.mongodb.DBCollection
import com.mysqldb.model.Bunus
import com.mysqldb.model.UserFinance

/**
 * http://www.itnose.net/detail/6254910.html
 * 初始化红包 mysql->mongodb
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class InitBunusController extends BaseController {

	@Resource
	private SessionFactory sessionFactory ;

	DBCollection topic_bunus(){
		return mainMongo.getCollection('topic_bunus');
	}

	@Resource
	private MsgController msgController;

	public DBCollection _topics(){
		return mainMongo.getCollection("topics");
	}
//	public DBCollection _topics_reply(){
//		return mainMongo.getCollection("topics_reply");
//	}
	private DBCollection topics_reply(){
		return mainMongo.getCollection("topics_reply");
	}

	//红包图片地址
	String red_url ;
	@Value("#{application['red.url']}")
	void setRedUrl(String red_url){
		this.red_url = red_url;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");


	/** ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  补发红包;打开未打开的红包   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */

	@TypeChecked(TypeCheckingMode.SKIP)
	def initTopicRed(){
		openRedList();
		return OK();
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncTopicRed(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						openRedList();
					}
				}
				);
	}

	def openRedList(){
		def topicList = _topics().find(
				$$("type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : TopicEvaluationType.满意.ordinal() , "buns_states" : TopicBunsStates.未打开.ordinal())
				).sort($$("timestamp" : 1)).toArray();

		if(topicList){
			topicList.each {def item->
				//问题id
				String topic_id = item["_id"];
				//抢到教师id
				Integer teach_id = item["teach_id"];

				//补发红包
				_retroactive_bunus(topic_id);

				//打开红包
				if(teach_id != null){
					openRed(topic_id, teach_id);
				}
			}
		}
		print "initTopicRed ==========end============"

	}

	/**
	 * 打开红包
	 * @Description: 打开红包
	 * @date 2015年7月31日 上午10:00:15
	 */
	@Transactional
	public Bunus openRed(String topic_id , Integer user_id){
		Bunus bunu = null;
		if(StringUtils.isNotBlank(topic_id)){
			Session session = sessionFactory.getCurrentSession();
			//用户红包
			List bunusList = session.createCriteria(Bunus.class)
					.add(Restrictions.eq(Bunus.PROP_USERID, user_id))	//用户id
					.add(Restrictions.eq(Bunus.PROP_ISOPEN, 0))			//红包开启状态
					.add(Restrictions.eq(Bunus.PROP_TOPICID, topic_id))	//提问id
					.addOrder(Order.asc(Bunus.PROP_MMONEY))
					.setFirstResult(1)
					.setMaxResults(1)
					.list();
			
			if(bunusList != null && bunusList.size() > 0){
				bunu = bunusList.get(0);
			}
					
			if(bunu){
				//用户剩余金额
				UserFinance ufe = (UserFinance)session.createCriteria(UserFinance.class)
						.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))	//用户id
						.uniqueResult();

				if(ufe == null){
					ufe = new UserFinance();
					ufe.setUserId(user_id);
					ufe.setUserMoney(new java.math.BigDecimal(0));
				}
				ufe.setUserMoney(ufe.getUserMoney() + bunu.getMmoney());

				if(bunu != null){
					//开启红包
					bunu.setIsOpen(1);
					//开启时间
					bunu.setOpenTime(new java.sql.Timestamp(new java.util.Date().getTime()));
					//开启事务
					Transaction tran =session.beginTransaction();
					//修改红包
					session.update(bunu);
					//修改累计金额
					session.saveOrUpdate(ufe);
					//提交事务
					tran.commit();
					session.flush();
				}
			}
		}
		return bunu;
	}

	/**
	 * 补发红包
	 */
	@Transactional
	def _retroactive_bunus(String topic_id){
		//问题id
		if(StringUtils.isNotBlank(topic_id)){

			boolean isRet = isRetroactive(topic_id);
			//非作弊
			if(isRet){
				Integer teach_id = _topics().findOne(
						$$("_id" : topic_id , "type":TopicsType.问题已结束.ordinal() , "evaluation_type" : TopicEvaluationType.满意.ordinal()) ,
						$$( "teach_id" : 1 )
						)?.get("teach_id") as Integer;

				Session session = sessionFactory.getCurrentSession();

				String ym = sdf.format(new Date());

				//获取没打开过的红包-按金额排序
				List bunusList =  session.createCriteria(Bunus.class)
						.add(Restrictions.eq(Bunus.PROP_YEARMON , ym))
						.add(Restrictions.isNull(Bunus.PROP_TOPICID))
						.add(Restrictions.isNull(Bunus.PROP_USERID))
						.addOrder(Order.asc(Bunus.PROP_MMONEY))
						.setFirstResult(1)
						.setMaxResults(1)
						.list();


				if(bunusList != null && bunusList.size() > 0){
					Bunus bunu = (Bunus)bunusList.get(0);
					if(bunu){
						//系统当前时间
						java.sql.Timestamp time = new java.sql.Timestamp( System.currentTimeMillis())

						//抽取红包信息
						//教师id
						bunu.setUserId(teach_id);
						//问题id
						bunu.setTopicId(topic_id);
						//时间
						bunu.setLotteryTime(time);

						//打开红包信息
						//开启红包
						bunu.setIsOpen(1);
						//开启时间
						bunu.setOpenTime(time);

						//修改提问中红包开启状态
						_topics().update($$("_id" : topic_id), $$($set:$$("buns_states" : TopicBunsStates.已打开.ordinal())))

						//用户剩余金额
						UserFinance ufe = (UserFinance)session.createCriteria(UserFinance.class)
								.add(Restrictions.eq(UserFinance.PROP_USERID, teach_id))	//用户id
								.uniqueResult();

						if(ufe == null){
							ufe = new UserFinance();
							ufe.setUserId(teach_id);
							ufe.setUserMoney(new java.math.BigDecimal(0));
						}
						//累加金额
						ufe.setUserMoney(ufe.getUserMoney() + bunu.getMmoney());

						//开启事务
						Transaction tran =session.beginTransaction();
						//修改红包
						session.update(bunu);
						//修改累计金额
						session.saveOrUpdate(ufe);
						//提交事务
						tran.commit();
						//刷新session缓存
						session.flush();
						println "retroactive bunu topic_id:" + topic_id + "====teach_id:" + teach_id + "===time:" + new Date() ;
						//发送推送消息

						long now = System.currentTimeMillis();
						//红包结构
						def rep = $$(
								"_id" : UUID.randomUUID().toString() , "reply_content" : red_url , "reply_time" : now ,
								"reply_type" : ReplyType.红包.ordinal() , "topic_id" : topic_id , "user_id" : 1 , "timestamp" : now , "user_pic" : null,
								"show_type" : ReplyShowType.老师.ordinal()
								);
						topics_reply().save(rep);

						addRedMoneyReply(teach_id , topic_id , bunu.getMmoney());
						return ["code" : 1 , "data" : bunu.getMmoney()];
					}
				}

			}
		}
		return getParamsErr();
	}

	/**
	 * 是否可以补发
	 * @param topic_id 课程id
	 */
	private boolean isRetroactive(String topic_id){
		boolean res = false;
		if(StringUtils.isNotBlank(topic_id)){
			def topic = _topics().findOne(
					$$(
					"_id" : topic_id , "type" : TopicsType.问题已结束.ordinal() ,
					"evaluation_type" : TopicEvaluationType.满意.ordinal() , "buns_states" :TopicBunsStates.未打开.ordinal()
					)
					,
					$$("teach_id" : 1 , "teach_device_id": 1 , "author_device_id" : 1)
					);
			if(topic){
				String teach_device_id = topic["teach_device_id"];
				String author_device_id = topic["author_device_id"];
				if(
				(StringUtils.isBlank(author_device_id) && StringUtils.isBlank(teach_device_id)) ||
				(StringUtils.isNotBlank(teach_device_id) && !teach_device_id.equals(author_device_id)) ||
				(StringUtils.isNotBlank(author_device_id) && !author_device_id.equals(teach_device_id))
				){

					Session session = sessionFactory.getCurrentSession();

					int count = (int)sessionFactory
							.getCurrentSession().createCriteria(Bunus.class)
							.setProjection(Projections.count(Bunus.PROP_ID))
							.add(Restrictions.eq(Bunus.PROP_TOPICID, topic_id))
							.uniqueResult();
					if(count == 0 && topic_bunus().count($$("_id" : topic_id + "-" + topic["teach_id"]))==0){
						res = true;
					}
				}
			}
		}
		return res;
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

	/** ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  补发红包;打开未打开的红包  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */


	/** ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   红包迁移 MYSQL->MONGODB  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */

	@TypeChecked(TypeCheckingMode.SKIP)
	def init(){
		asyncGetbunus();
		return OK();
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncGetbunus(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {

						bunusInit();

					}
				}
				);
	}

	def bunusInit(){
		int count = (int)sessionFactory.openSession().createCriteria(Bunus.class)
				.setProjection(Projections.count(Bunus.PROP_ID))
				.add(Restrictions.isNotNull(Bunus.PROP_TOPICID ))
				.uniqueResult();
		int page = 1;
		int size = 10000;
		
		int maxPage = (count / size) + (count%size == 0 ? 0 : 1);
		
		for(;page <= maxPage ; page++){
			bunusInitItem(page , size);
		}
		
		
		logMongo.getCollection("mysql_mongodb_bunus").save($$("_id" : "1"));
		print "init ==========end============"
	}

	def bunusInitItem(int page , int size){

		List bunusList = sessionFactory.openSession().createCriteria(Bunus.class)
				.add(Restrictions.isNotNull(Bunus.PROP_TOPICID ))
				.addOrder(Order.asc(Bunus.PROP_OPENTIME))
				.setFirstResult((page-1)*size)
				.setMaxResults(size)
				.list();

		if(bunusList){
			bunusList.each {Bunus bunu->
				def topic_bunu = $$(
						"_id" : bunu.getTopicId()+"-" + bunu.getUserId() ,
						"topic_id" : bunu.getTopicId() ,
						"create_time" : bunu.getLotteryTime()?.getTime() ,
						"mmoney" : bunu.getMmoney().doubleValue() ,
						"open_time" : bunu.getOpenTime()?.getTime() ,
						"open_type" : bunu.getIsOpen() ,
						"mtemplate" : bunu.getMtemplate() ,
						"user_id" : bunu.getUserId()
						);
				topic_bunus().save(topic_bunu);
			}
		}
	}
	def bunusInitAll(){
		
		List bunusList = sessionFactory.openSession().createCriteria(Bunus.class)
				.add(Restrictions.isNotNull(Bunus.PROP_TOPICID ))
				.addOrder(Order.asc(Bunus.PROP_OPENTIME))
//				.setFirstResult((page-1)*size)
//				.setMaxResults(size)
				.list();
		
		if(bunusList){
			bunusList.each {Bunus bunu->
			def topic_bunu = $$(
					"_id" : bunu.getTopicId()+"-" + bunu.getUserId() ,
					"topic_id" : bunu.getTopicId() ,
					"create_time" : bunu.getLotteryTime().getTime() ,
					"mmoney" : bunu.getMmoney().doubleValue() ,
					"open_time" : bunu.getOpenTime()?.getTime() ,
							"open_type" : bunu.getIsOpen() ,
							"mtemplate" : bunu.getMtemplate() ,
							"user_id" : bunu.getUserId()
					);
			topic_bunus().save(topic_bunu);
			}
		}
	}

	/** ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑    红包迁移 MYSQL->MONGODB   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */

}

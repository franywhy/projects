package com.izhubo.web.server

import com.hqonline.model.UserIncomType
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.ApplyState
import com.izhubo.model.BunusLimitOpenType
import com.izhubo.model.ScoreType
import com.izhubo.model.TopicBunuOpenType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.web.BaseController
import com.izhubo.web.api.TipBunusRelationController
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.mysqldb.dao.BunusDao
import com.mysqldb.model.Apply
import com.mysqldb.model.Bunus
import com.mysqldb.model.UserFinance
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import org.apache.commons.lang.StringUtils
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.ProjectionList
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.transform.Transformers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
@RequestMapping("/earnings")
class EarningsController extends BaseController {

	private BunusDao bunusDao = new BunusDao();
	@Resource
	private SessionFactory sessionFactory;

	private Lock lock = new ReentrantLock();

	private DBCollection topics(){
		return mainMongo.getCollection("topics");
	}
	private DBCollection bunus_limits(){
		return mainMongo.getCollection("bunus_limits");
	}
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	private DBCollection bunus_logs(){
		return adminMongo.getCollection("bunus_logs");
	}
	private DBCollection user_incom_log(){
		return logMongo.getCollection("user_incom_log");
	}
	private DBCollection tip_bunus_relation() {
		return mainMongo.getCollection("tip_bunus_relation");
	}

	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}

	private static Logger logger = LoggerFactory.getLogger(EarningsController.class);

	@Resource
	private UserIncomController userincomController;

	/**
	 * 打赏到账
	 * @param user_id	用户id
	 * @param money		打赏金额
	 * @param topic_id	问题id
	 * @param timestamp	打赏到账时间
	 * @return
	 */
	def tipsAdd(Integer user_id , Double money , String topic_id , Long timestamp){
		if(StringUtils.isNotBlank(topic_id) && null != money && money > 0){
			//新增记录
			if(userincomController.save(user_id, money, UserIncomType.打赏.ordinal(), topic_id, timestamp , 0)){
				Session session = sessionFactory.getCurrentSession();
				//账户余额新增
				//用户剩余金额
				UserFinance ufe = (UserFinance)session.createCriteria(UserFinance.class)
						.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))	//用户id
						.uniqueResult();
				if(ufe == null){
					ufe = new UserFinance();
					ufe.setUserId(user_id);
					ufe.setUserMoney(new java.math.BigDecimal(0));
				}
				ufe.setUserMoney(ufe.getUserMoney() + money);
				//修改累计金额
				session.saveOrUpdate(ufe);
				return true;
			}
		}
		return false;
	}

	/**
	 * 打开红包
	 * @Description: 打开红包
	 * @date 2015年7月31日 上午10:00:15 
	 */
	def openRed(String topic_id , int user_id , int open_type){
		def bunu = null;
		if(StringUtils.isNotBlank(topic_id)){
			Session session = sessionFactory.getCurrentSession();
			//用户红包
			bunu = topic_bunus().findOne(
					$$(
					"_id" : KeyUtils.BUNUS.bunusTimeOutVal(topic_id, user_id) ,
					"open_type":TopicBunuOpenType.未打开.ordinal(),
					"user_id" : user_id
					)
					);
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
				ufe.setUserMoney(ufe.getUserMoney() + (bunu["mmoney"] as Double));


				Long now = System.currentTimeMillis();

				if(bunu != null){
					//红包打开时间
					//					红包打开类型
					//					0.未打开 1.教师打开 2.超时开启
					if(
					topic_bunus().update(
					$$(
					"_id" : KeyUtils.BUNUS.bunusTimeOutVal(topic_id, user_id) ,
					"open_type":TopicBunuOpenType.未打开.ordinal(),
					"user_id" : user_id
					),
					$$('$set' : $$("open_time" : now , "open_type" : open_type))
					).getN() > 0
					){


						//修改累计金额
						session.saveOrUpdate(ufe);
						//提交事务
						session.flush();
						//							TODO 红包收入明细  红包到账时存入收入明细表
						//							def user_incom_log = $$("type" : UserIncomType.红包.ordinal() , "timestamp" : now , "user_id" : user_id , "topic_id" : topic_id , "remark" : "红包到账" , "money" : bunu["mmoney"]);
						//							logMongo.getCollection("user_incom_log").save(user_incom_log);
						//红包到账时存入收入明细表
						userincomController.save(user_id, bunu["mmoney"] as Double, UserIncomType.红包.ordinal(), topic_id , now , 0);
					}
				}
				//日志
				bunus_logs_save(topic_id, user_id, open_type, "openRed");
			}
		}
		return bunu;
	}

	def get_topic_bunus_info(String topic_id , int user_id){
		return topic_bunus().findOne(
		$$("_id" : KeyUtils.BUNUS.bunusTimeOutVal(topic_id, user_id)),
		$$("mmoney" : 1 , "mtemplate" : 1)
		);
	}

	def bunus_logs_save(String topic_id , Integer user_id , Integer open_type , String method){
		bunus_logs().save($$("_id" : UUID.randomUUID().toString() , "topic_id" : topic_id , "user_id" : user_id , "open_type" : open_type , "timestamp" : System.currentTimeMillis() , "remark" : method))
	}
	//	/**
	//	 * 打开红包
	//	 * @Description: 打开红包
	//	 * @date 2015年7月31日 上午10:00:15
	//	 */
	//	public Bunus openRed(String topic_id , int user_id){
	//		Bunus bunu = null;
	//		if(StringUtils.isNotBlank(topic_id)){
	//			Session session = sessionFactory.getCurrentSession();
	//			//用户红包
	//			bunu = (Bunus)session.createCriteria(Bunus.class)
	//					.add(Restrictions.eq(Bunus.PROP_USERID, user_id))	//用户id
	//					.add(Restrictions.eq(Bunus.PROP_ISOPEN, 0))			//红包开启状态
	//					.add(Restrictions.eq(Bunus.PROP_TOPICID, topic_id))	//提问id
	//					.uniqueResult();
	//			if(bunu){
	//				//用户剩余金额
	//				UserFinance ufe = (UserFinance)session.createCriteria(UserFinance.class)
	//						.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))	//用户id
	//						.uniqueResult();
	//
	//				if(ufe == null){
	//					ufe = new UserFinance();
	//					ufe.setUserId(user_id);
	//					ufe.setUserMoney(new java.math.BigDecimal(0));
	//				}
	//				ufe.setUserMoney(ufe.getUserMoney() + bunu.getMmoney());
	//
	//				if(bunu != null){
	//					//开启红包
	//					bunu.setIsOpen(1);
	//					//开启时间
	//					bunu.setOpenTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	//					//开启事务
	//					Transaction tran =session.beginTransaction();
	//					//修改红包
	//					session.update(bunu);
	//					//修改累计金额
	//					session.saveOrUpdate(ufe);
	//					//提交事务
	//					tran.commit();
	//					session.flush();
	//				}
	//			}
	//		}
	//		return bunu;
	//	}

	/**
	 * 抽奖
	 * @param topic_id			问题id
	 * @param user_id			用户id
	 * @param bunus_limit_id	限制类型id
	 * @return
	 */
	def lottery_v200(String topic_id , int user_id , Integer score){
		Long now =  System.currentTimeMillis();

		Integer bunus_limit_id = 0;

		//为确保问题只有一个红包
		def result = $$("_id" : KeyUtils.BUNUS.bunusTimeOutVal(topic_id , user_id));
		//		def result = $$("_id" : topic_id + "-" + user_id);
		//		def result = $$("_id" : UUID.randomUUID().toString());
		//随机金额
		Integer tid = (Integer) topics().findOne($$("_id" : topic_id ),$$("tips" : 1))?.get("tips")["0"]["_id"];
		println "tid======================="+tid;

		Double mmoney;
		/*旧的红包金额获取代码2018-08-24
		if(tid==null || tid<10000){
			if(score == ScoreType.满意.ordinal()){
				bunus_limit_id = 1000;
			}else if(score == ScoreType.很满意.ordinal()){
				bunus_limit_id = 2000;
			}else {
				return null;
			}
			mmoney = random_money(bunus_limit_id);
		}else if(tid>=10000 && tid<10010){//中级学员的标签范围
			if(score == ScoreType.满意.ordinal()){
				bunus_limit_id = 3000;
			}else if(score == ScoreType.很满意.ordinal()){
				bunus_limit_id = 4000;
			}else {
				return null;
			}
			mmoney = random_money(bunus_limit_id);

		}else{
			if(score == ScoreType.满意.ordinal()){
				bunus_limit_id = 1000;
			}else if(score == ScoreType.很满意.ordinal()){
				bunus_limit_id = 2000;
			}else {
				return null;
			}
			mmoney = random_money(bunus_limit_id);
		}*/

		if(null!=tid && !("".equals(tid))){
			if(score == ScoreType.满意.ordinal()){
                bunus_limit_id = get_bunus_limits_id(tid,ScoreType.满意.toString());
				logger.info("lottery_v200 info. topic_id:"+topic_id+" tid:"+tid+" bunus_limit_id:"+bunus_limit_id+" score:"+score);
			}else if(score == ScoreType.很满意.ordinal()){
                bunus_limit_id = get_bunus_limits_id(tid,ScoreType.很满意.toString());
				logger.info("lottery_v200 info. topic_id:"+topic_id+" tid:"+tid+" bunus_limit_id:"+bunus_limit_id+" score:"+score);
			}else {
				return null;
			}
			mmoney = random_money(bunus_limit_id);
			logger.info("lottery_v200 info. topic_id:"+topic_id+" tid:"+tid+" mmoney:"+mmoney);
		}else{
			if(score == ScoreType.满意.ordinal()){
				bunus_limit_id = 1000;
			}else if(score == ScoreType.很满意.ordinal()){
				bunus_limit_id = 2000;
			}else {
				return null;
			}
			mmoney = random_money(bunus_limit_id);
			logger.info("lottery_v200 info. topic_id:"+topic_id+" tid:"+tid+" mmoney:"+mmoney);
		}

		println "mmoney======================="+mmoney;
		//红包金额
		result["mmoney"] = mmoney;
		//问题id
		result["topic_id"] = topic_id;
		//创建时间
		result["create_time"] = now;
		//红包打开时间
		result["open_time"] = null;
		//红包打开类型
		result["open_type"] = BunusLimitOpenType.未打开.ordinal();
		//模板 0-红色模板 1-蓝色模板
		result["mtemplate"] = 0;
		//用户id
		result["user_id"] = user_id;
		//保存
		topic_bunus().save(result);

		return result;
	}


	/**
	 * 获取对应红包限制ID
	 * @param tid
	 * @param scoreTypeName
	 * @return
	 */
	private Integer get_bunus_limits_id(Integer tid, String scoreTypeName) {
		Integer _id = 0;

		//根据标签ID获取标签
		def tipContent = tip_content().findOne($$("_id": tid));
		if (null == tipContent) {
			if (scoreTypeName.equals(ScoreType.满意.toString())) {
				_id = 1000;
			} else if (scoreTypeName.equals(ScoreType.很满意.toString())) {
				_id = 2000;
			} else {
				return 0;
			}
		}
		Integer parent_tip_id = tipContent["parent_tip_id"] as Integer;
		def tip_bunus_info = tip_bunus_relation().find($$("tip_id" : parent_tip_id));

		if (tip_bunus_info) {
//			Integer parent_tip_id = tipContent["parent_tip_id"] as Integer;
			//先更具获取标签和红包关系
			def iter = tip_bunus_relation().aggregate(
					$$(
							$lookup:
									$$(
											from: "bunus_limits",
											localField: "bunus_limits_id",
											foreignField: "_id",
											as: "inv_docs"
									)
					),
					$$($match: ["dr": 0, "tip_id": parent_tip_id]),
					$$($project: ["inv_docs": 1, "tip_id": 1, "_id": 0])
			).results().toList();
			//循环拿到对应的红包限制ID

			iter.each { it ->
				def obj = it["inv_docs"]
				obj.each { it1 ->
					if (scoreTypeName.equals(it1["name"])) {
						_id = (Integer) it1["_id"];
					}
				}
			}

		}else {
			if (scoreTypeName.equals(ScoreType.满意.toString())) {
				_id = 1000;
			} else if (scoreTypeName.equals(ScoreType.很满意.toString())) {
				_id = 2000;
			} else {
				return 0;
			}
		}

		return _id;
	}

	/**
	 * 获取随机金额
	 * @param bunus_limit_id 范围id
	 */
	def random_money(Integer bunus_limit_id){
		//查询范围
		def bunusLimit = bunus_limits().findOne($$("_id" : bunus_limit_id));
		if(bunusLimit){
			//最大金额范围
			Double max_money = bunusLimit["max_money"] as Double;
			//最小金额范围
			Double min_money = bunusLimit["min_money"] as Double;
			//随机数
			Random r = new Random();
			Double d = r.nextDouble() * (max_money - min_money) + min_money;
			//转换
			java.math.BigDecimal bd = new java.math.BigDecimal(d);
			//返回含有两位小数的
			return bd.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
		}
		return 0;
	}

	/**
	 * 抽奖
	 * @Description: 抽奖
	 * @date 2015年7月31日 上午9:46:12 
	 * @param @param topic_id 问题id
	 * @param @param user_id  用户id
	 * @param @return Bunus 抽奖对象
	 */
	def lottery(String topic_id , int user_id){
		List bunusList = sessionFactory.getCurrentSession()
				.createCriteria(Bunus.class)
				.add(Restrictions.isNull(Bunus.PROP_USERID))
				.add(Restrictions.eq(Bunus.PROP_YEARMON, formate()))
				.list();
		Bunus lb = null;
		if(bunusList){
			//权重和
			int weight_total = 0;
			bunusList.each {Bunus dbo ->
				weight_total += dbo.getMweight();
			}

			int randomNumber = (int) (Math.random() * weight_total);//搜索

			boolean success = false;

			int priority = 0;

			for(int i = 0 ; i < bunusList.size() ; i ++){
				Bunus dbo = (Bunus) bunusList.get(i);
				priority += dbo.getMweight();
				if (priority >= randomNumber) {

					lock.lock();
					try {
						//查询单条记录
						Session session = sessionFactory.getCurrentSession();
						Bunus bunu = (Bunus)session.createCriteria(Bunus.class)
								.add(Restrictions.isNull(Bunus.PROP_USERID))
								.add(Restrictions.eq(Bunus.PROP_ID, dbo.getId()))
								.uniqueResult();
						if(bunu){//如果未被抽取-更新
							bunu.setUserId(user_id);
							bunu.setTopicId(topic_id);
							bunu.setLotteryTime(new java.sql.Timestamp(new java.util.Date().getTime()));
							//开启事务
							Transaction tran =session.beginTransaction();
							session.update(bunu);
							tran.commit();
							session.flush();
							lb = bunu;
							success = true;
							break;
						}else{//抽取后-释放锁-递归
							lock.unlock();
							lb = (Bunus)lottery(topic_id , user_id);
						}

					} finally{
						//显示释放锁
						lock.unlock();
					}
				}
				else{

					//do nothing
				}

				//end if (priority >= randomNumber) {
			}//end for(int i = 0 ; i < bunusList.size() ; i ++){
			//循环结束，如果仍然没发红包，则记录日志
			if(!success)
			{
				logger.info("lottery erro. topic_id:"+topic_id+" userid:"+Integer.valueOf(user_id));
			}
		}
		return lb;

	}
	//	def lottery(String topic_id , int user_id){
	//		Bunus lb = null;
	//		def vlevel = topics().findOne($$("_id" : topic_id) , $$("vlevel" : 1))?.get("vlevel");
	//		if(vlevel == null){
	//			vlevel = 0;
	//		}
	//		if(VlevelType.V1.ordinal() == vlevel){
	//			lb = lotteryV1(topic_id, user_id);
	//		}else{
	//			lb = lotteryV0(topic_id, user_id);
	//		}
	//		return lb;
	//	}

	/**
	 * 普通用户抽奖算法
	 * @param topic_id
	 * @param user_id
	 * @return
	 */
	def lotteryV0(String topic_id , int user_id){

		List bunusList = sessionFactory.getCurrentSession()
				.createCriteria(Bunus.class)
				.add(Restrictions.isNull(Bunus.PROP_USERID))
				.add(Restrictions.eq(Bunus.PROP_YEARMON, formate()))
				.list();
		Bunus lb = null;
		if(bunusList){
			//权重和
			int weight_total = 0;
			bunusList.each {Bunus dbo ->
				weight_total += dbo.getMweight();
			}

			int randomNumber = (int) (Math.random() * weight_total);//搜索

			int priority = 0;

			for(int i = 0 ; i < bunusList.size() ; i ++){
				Bunus dbo = (Bunus) bunusList.get(i);
				priority += dbo.getMweight();
				if (priority >= randomNumber) {

					lock.lock();
					try {
						//查询单条记录
						Session session = sessionFactory.getCurrentSession();
						Bunus bunu = (Bunus)session.createCriteria(Bunus.class)
								.add(Restrictions.isNull(Bunus.PROP_USERID))
								.add(Restrictions.eq(Bunus.PROP_ID, dbo.getId()))
								.uniqueResult();
						if(bunu){//如果未被抽取-更新
							bunu.setUserId(user_id);
							bunu.setTopicId(topic_id);
							bunu.setLotteryTime(new java.sql.Timestamp(new java.util.Date().getTime()));
							//开启事务
							Transaction tran =session.beginTransaction();
							session.update(bunu);
							tran.commit();
							session.flush();
							lb = bunu;
							break;
						}else{//抽取后-释放锁-递归
							lock.unlock();
							lb = (Bunus)lottery(topic_id , user_id);
						}

					} finally{
						//显示释放锁
						lock.unlock();
					}
				}//end if (priority >= randomNumber) {
			}//end for(int i = 0 ; i < bunusList.size() ; i ++){
		}
		return lb;


	}

	/**
	 * VIP用户抽奖算法
	 * @param topic_id
	 * @param user_id
	 * @return
	 */
	def lotteryV1(String topic_id , int user_id){

		List bunusList = sessionFactory.getCurrentSession()
				.createCriteria(Bunus.class)
				.add(Restrictions.isNull(Bunus.PROP_USERID))
				.add(Restrictions.eq(Bunus.PROP_YEARMON, formate()))
				.list();
		Bunus lb = null;
		if(bunusList){
			//权重和
			int weight_total = 0;
			bunusList.each {Bunus dbo ->
				weight_total += dbo.getVweight();
			}

			int randomNumber = (int) (Math.random() * weight_total);//搜索

			int priority = 0;

			for(int i = 0 ; i < bunusList.size() ; i ++){
				Bunus dbo = (Bunus) bunusList.get(i);
				priority += dbo.getVweight();
				if (priority >= randomNumber) {

					lock.lock();
					try {
						//查询单条记录
						Session session = sessionFactory.getCurrentSession();
						Bunus bunu = (Bunus)session.createCriteria(Bunus.class)
								.add(Restrictions.isNull(Bunus.PROP_USERID))
								.add(Restrictions.eq(Bunus.PROP_ID, dbo.getId()))
								.uniqueResult();
						if(bunu){//如果未被抽取-更新
							bunu.setUserId(user_id);
							bunu.setTopicId(topic_id);
							bunu.setLotteryTime(new java.sql.Timestamp(new java.util.Date().getTime()));
							//开启事务
							Transaction tran =session.beginTransaction();
							session.update(bunu);
							tran.commit();
							session.flush();
							lb = bunu;
							break;
						}else{//抽取后-释放锁-递归
							lock.unlock();
							lb = (Bunus)lottery(topic_id , user_id);
						}

					} finally{
						//显示释放锁
						lock.unlock();
					}
				}//end if (priority >= randomNumber) {
			}//end for(int i = 0 ; i < bunusList.size() ; i ++){
		}
		return lb;
	}

	/**
	 * 历史转出
	 * @Description: 历史转出 
	 * @date 2015年7月30日 下午4:37:39 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def applyList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		def list = sessionFactory.getCurrentSession()
				.createCriteria(Apply.class)
				.setProjection(
				Projections.projectionList()
				.add(Projections.property(Apply.PROP_ID).as(Apply.PROP_ID))
				.add(Projections.property(Apply.PROP_APPLYMONEY).as(Apply.PROP_APPLYMONEY))
				.add(Projections.property(Apply.PROP_CREATETIME).as(Apply.PROP_CREATETIME))
				.add(Projections.property(Apply.PROP_APPLYSTATE).as(Apply.PROP_APPLYSTATE))
				)
				.setResultTransformer(Transformers.aliasToBean(Apply.class))
				.add(Restrictions.eq(Apply.PROP_USERID, user_id))
				.addOrder(Order.desc(Apply.PROP_CREATETIME))
				.setFirstResult((page - 1) * size)
				.setMaxResults(size)
				.list();
		return getResultOK(list);
	}

	/**
	 * 账户可提现余额
	 * @Description: 账户可提现余额
	 * @date 2015年9月15日 下午2:59:33 
	 */
	def userFinanceMoney(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		double userMoney = userFinanceMoneyByUserId(user_id) as double

		Map<String,Object> map = new HashMap()
		Calendar c = Calendar.getInstance()
		int date = c.get(Calendar.DATE)
		//提现时间为：1-4号，金额不能小于：100
		//提现时间为：每周一、周二，金额不能小于：100（都要动态配置到数据库）
		if(date <= 4 && userMoney >= 100) {
			map.put("status",1)
		} else {
			map.put("status",0)
		}
		map.put("applyMoney",userMoney)
		//获取提现手续费
		def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025))
		if(ahurl) {
			float ratio = ahurl.get("url") as float
			double fees = Math.round(userMoney * ratio * 100) / 100
			map.put("fees",fees)
			map.put("arrivalMoney",Math.round((userMoney - fees) * 100) / 100)
			map.put("copywriting",ahurl.get("copywriting"))
		}
		return getResultOK(map)
	}

	def userFinanceMoneyByUserId(Integer user_id){
		//账户余额
		def uFinance = sessionFactory.getCurrentSession()
				.createCriteria(UserFinance.class)
				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
				.uniqueResult();

		UserFinance userFinance = uFinance ? uFinance : null;

		//获取当前月第一天
		Calendar c = Calendar.getInstance()
		c.add(Calendar.MONTH, 0)
		c.set(Calendar.DAY_OF_MONTH,1)
		c.set(Calendar.HOUR_OF_DAY, 0)
		c.set(Calendar.MINUTE, 0)
		c.set(Calendar.SECOND, 0)
		c.set(Calendar.MILLISECOND, 0)

		double month = 0d
		//当前月收益
		def itear0 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
				]) , "timestamp" : $$('$gte' : c.getTime().getTime())]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
		).results().iterator()
		if(itear0 != null){
			while (itear0.hasNext()){
				def obj = itear0.next()
				if(obj && obj["money"] != null){
					month = obj["money"] as double
				}
			}
		}

		Double userMoney = userFinance!=null ? userFinance.getUserMoney().toDouble() - month : 0
		if(userMoney < 100) {
			//不满100，向下取，保留2位小数
			userMoney = Math.floor(userMoney * 100) / 100
		} else {
			//取100的整数倍
			userMoney = Math.floor(userMoney / 100)  * 100;
		}
		return userMoney;
	}

	/**
	 * 收支列表
	 */
	@ResponseBody
	@RequestMapping(value = "earningList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "收支列表-分页", httpMethod = "GET" , notes = "金额(money),时间(timestamp),标题(title)")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", defaultValue="20", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", defaultValue="1", paramType = "query")
	])
	def earningList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		def list = user_incom_log().find(
				$$("user_id" : user_id),
				//				$$("user_id" : user_id , "type" : $$('$in' : [UserIncomType.红包.ordinal() , UserIncomType.打赏.ordinal()])),
				$$("money" : 1 , "timestamp" : 1 , "type" : 1 , "_id" : 0)
				).sort($$("timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
		if(list){
			list.each {def item->
				item["title"] = UserIncomType.getName((Integer)item["type"]);
			}
		}
		return getResultOK(list);
	}

	/**
	 *  我的收益
	 * @Description: 我的收益
	 * @date 2015年7月30日 上午11:20:15 
	 */
	def myearnings(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//今天的收益
		def today = 0d;
		def itear0 = topic_bunus().aggregate(
				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [
						TopicBunuOpenType.教师打开.ordinal() ,
						TopicBunuOpenType.超时开启.ordinal()
					]) , "open_time" : $$('$gte' : dateBefore(0).getTime() )]),
				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
				).results().iterator();
		if(itear0 != null){
			while (itear0.hasNext()){
				def obj = itear0.next();
				if(obj && obj["mmoney"] != null){
					today = obj["mmoney"]
				}
			}
		}

		//总计收益
		def history = 0d;
		def itear1 = topic_bunus().aggregate(
				$$('$match', ["user_id" : user_id , "open_type" :$$('$in' : [
						TopicBunuOpenType.教师打开.ordinal() ,
						TopicBunuOpenType.超时开启.ordinal()
					])  ]),
				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
				).results().iterator();
		if(itear1 != null){
			while (itear1.hasNext()){
				def obj = itear1.next();
				if(obj && obj["mmoney"] != null){
					history = obj["mmoney"]
				}
			}
		}

		//账户余额
		def uFinance = sessionFactory.getCurrentSession()
				.createCriteria(UserFinance.class)
				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
				.uniqueResult();

		UserFinance userFinance = uFinance ? uFinance : null;
		//近一周收益
		def week = 0d;
		def itear2 = topic_bunus().aggregate(
				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [
						TopicBunuOpenType.教师打开.ordinal() ,
						TopicBunuOpenType.超时开启.ordinal()
					])  , "open_time" : $$('$gte' : dateBefore(7).getTime() )]),
				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
				).results().iterator();
		if(itear2 != null){
			while (itear2.hasNext()){
				def obj = itear2.next();
				if(obj && obj["mmoney"] != null){
					week = obj["mmoney"]
				}
			}
		}

		//近一个月收益
		def month = 0d;
		def itear3 = topic_bunus().aggregate(
				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [
						TopicBunuOpenType.教师打开.ordinal() ,
						TopicBunuOpenType.超时开启.ordinal()
					])  , "open_time" : $$('$gte' : dateBefore(30).getTime() )]),
				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
				).results().iterator();
		if(itear3 != null){
			while (itear3.hasNext()){
				def obj = itear3.next();
				if(obj && obj["mmoney"] != null){
					month = obj["mmoney"]
				}
			}
		}

		//累计抽奖次数

		def redcount = topic_bunus().count($$("user_id" : user_id , "open_type" : 1));

		//累计抢答次数
		int qiangdacount =(Integer) topics().count($$("teach_id" : user_id));

		Map map = new HashMap();
		map["today"] = today;
		map["history"] = history;
		map["week"] = week;
		map["month"] = month;
		map["redcount"] = redcount;
		map["qiangdacount"] = qiangdacount;
		map["usermoney"] = userFinance ? userFinance.getUserMoney() : 0;
		return getResultOKS(map);
	}


	/**
	 *  我的收益
	 * @Description: 我的收益
	 * @date 2015年7月30日 上午11:20:15 
	 */
	@ResponseBody
	@RequestMapping(value = "myearnings_v300/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的收益", httpMethod = "GET" , notes = "我的收益-2.00")
	def myearnings_v300(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		//今天的收益
		def today = 0d;
		def itear0 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
					]) , "timestamp" : $$('$gte' : dateBefore(0).getTime() )]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
				).results().iterator();
		if(itear0 != null){
			while (itear0.hasNext()){
				def obj = itear0.next();
				if(obj && obj["money"] != null){
					today = obj["money"]
				}
			}
		}

		//总计收益
		def history = 0d;
		def itear1 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
					])  ]),
				$$('$project', ["user_id" : '$user_id' , money : '$money']),
				$$('$group', [_id: null , money:[$sum: '$money']])
				).results().iterator();
		if(itear1 != null){
			while (itear1.hasNext()){
				def obj = itear1.next();
				if(obj && obj["money"] != null){
					history = obj["money"]
				}
			}
		}

		//账户余额
		def uFinance = sessionFactory.getCurrentSession()
				.createCriteria(UserFinance.class)
				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
				.uniqueResult();

		UserFinance userFinance = uFinance ? uFinance : null;
		//近一周收益
		def week = 0d;
		def itear2 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
					])   , "timestamp" : $$('$gte' : dateBefore(7).getTime() )]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
				).results().iterator();
		if(itear2 != null){
			while (itear2.hasNext()){
				def obj = itear2.next();
				if(obj && obj["money"] != null){
					week = obj["money"]
				}
			}
		}

		//近一个月收益
		def month = 0d;
		def itear3 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
					])  , "timestamp" : $$('$gte' : dateBefore(30).getTime() )]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
				).results().iterator();
		if(itear3 != null){
			while (itear3.hasNext()){
				def obj = itear3.next();
				if(obj && obj["money"] != null){
					month = obj["money"]
				}
			}
		}

		//打赏收益
		def tip_money = 0d;
		def itear4 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : UserIncomType.打赏.ordinal()  ]),
				//			$$('$match', ["user_id" : user_id , "type" : $$('$in' : [UserIncomType.红包.ordinal() , UserIncomType.打赏.ordinal()])  , "open_time" : $$('$gte' : dateBefore(30).getTime() )]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
				).results().iterator();
		if(itear4 != null){
			while (itear4.hasNext()){
				def obj = itear4.next();
				if(obj && obj["money"] != null){
					tip_money = obj["money"]
				}
			}
		}

		//红包收益
		def red_money = 0d;
		def itear5 = user_incom_log().aggregate(
				$$('$match', ["user_id" : user_id , "type" : UserIncomType.红包.ordinal()  ]),
				//			$$('$match', ["user_id" : user_id , "type" : $$('$in' : [UserIncomType.红包.ordinal() , UserIncomType.打赏.ordinal()])  , "open_time" : $$('$gte' : dateBefore(30).getTime() )]),
				$$('$project', ["user_id": '$user_id',money: '$money']),
				$$('$group', [_id: null,money:[$sum: '$money']])
				).results().iterator();
		if(itear5 != null){
			while (itear5.hasNext()){
				def obj = itear5.next();
				if(obj && obj["money"] != null){
					red_money = obj["money"]
				}
			}
		}


		//		//累计抽奖次数
		//		def redcount = topic_bunus().count($$("user_id" : user_id , "open_type" : 1));

		//累计抢答次数
		int qiangdacount =(Integer) topics().count($$("teach_id" : user_id));

		Map map = new HashMap();
        def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025),$$("incomeRuleUrl":1))
        if(ahurl) {
            map.put("incomeRuleUrl",ahurl.get("incomeRuleUrl"))
        }
		map["today"] = NumberUtil.formatDouble4((Double)today);
		map["history"] = NumberUtil.formatDouble4((Double)history);
		map["week"] = NumberUtil.formatDouble4((Double)week);
		map["month"] = NumberUtil.formatDouble4((Double)month);
		//		map["redcount"] = redcount;
		map["tip_money"] = NumberUtil.formatDouble4((Double)tip_money);
		map["red_money"] = NumberUtil.formatDouble4((Double)red_money);
		map["qiangdacount"] = qiangdacount;
		map["usermoney"] = userFinance ? userFinance.getUserMoney() : 0.00;
		return getResultOKS(map);
	}

	/**
	 * 提现记录
	 * @param request
	 * @return
	 */
	def withdrawRecord(HttpServletRequest request){
		Integer userId = Web.getCurrentUserId()

		int size = ServletRequestUtils.getIntParameter(request, "size", 20)
		int page = ServletRequestUtils.getIntParameter(request, "page", 1)

		BigDecimal applySum = (BigDecimal) sessionFactory.getCurrentSession().createCriteria(Apply.class)
				.setProjection(Projections.sum(Apply.PROP_APPLYMONEY))
				.add(Restrictions.eq(Apply.PROP_USERID,userId))
				.add(Restrictions.in(Apply.PROP_APPLYSTATE,Arrays.asList(ApplyState.已申请.ordinal(),ApplyState.已支付.ordinal())))
				.uniqueResult()

		ProjectionList pList = Projections.projectionList()
		pList.add(Projections.property("applyMoney"),"applyMoney")
		pList.add(Projections.property("applyState"),"applyState")
		pList.add(Projections.property("createTime"),"applyTime")
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Apply.class)
		criterion.setProjection(pList)
		criterion.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
		criterion.add(Restrictions.eq(Apply.PROP_USERID,userId))

		List applyList = criterion.addOrder(Order.desc(Apply.PROP_CREATETIME)).addOrder(Order.desc(Apply.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list()

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
		List<Map<String,Object>> list = new ArrayList<>()
		for (int i=0; i<applyList.size(); i++) {
			Map map = (Map)applyList.get(i)
			map.put("applyTime",sdf.format(map.get("applyTime")))
			list.add(map)
		}

		Map map = new HashMap()
		//获取提现失败原因提示语
		def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025),$$("reason4failure":1))
		if(ahurl) {
			map.put("reason4failure",ahurl.get("reason4failure"))
		}
		map.put("applySum",applySum == null? 0 : applySum)
		map.put("list",list)

		return getResultOKS(map)
	}

	//	/**
	//	 *  我的收益
	//	 * @Description: 我的收益
	//	 * @date 2015年7月30日 上午11:20:15
	//	 */
	//	def myearnings(HttpServletRequest request){
	//		//用户id
	//		Integer user_id = Web.getCurrentUserId();
	//
	//		//今天的收益
	//		def today = 0d;
	//		def itear0 = topic_bunus().aggregate(
	//				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [TopicBunuOpenType.教师打开.ordinal() , TopicBunuOpenType.超时开启.ordinal()]) , "open_time" : $$('$gte' : dateBefore(0).getTime() )]),
	//				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
	//				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
	//				).results().iterator();
	//		if(itear0 != null){
	//			while (itear0.hasNext()){
	//				def obj = itear0.next();
	//				if(obj && obj["mmoney"] != null){
	//					today = obj["mmoney"]
	//				}
	//			}
	//		}
	//
	//		//总计收益
	//		def history = 0d;
	//		def itear1 = topic_bunus().aggregate(
	//				$$('$match', ["user_id" : user_id , "open_type" :$$('$in' : [TopicBunuOpenType.教师打开.ordinal() , TopicBunuOpenType.超时开启.ordinal()])  ]),
	//				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
	//				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
	//				).results().iterator();
	//		if(itear1 != null){
	//			while (itear1.hasNext()){
	//				def obj = itear1.next();
	//				if(obj && obj["mmoney"] != null){
	//					history = obj["mmoney"]
	//				}
	//			}
	//		}
	//
	//		//账户余额
	//		def uFinance = sessionFactory.getCurrentSession()
	//				.createCriteria(UserFinance.class)
	//				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
	//				.uniqueResult();
	//
	//		UserFinance userFinance = uFinance ? uFinance : null;
	//		//近一周收益
	//		def week = 0d;
	//		def itear2 = topic_bunus().aggregate(
	//				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [TopicBunuOpenType.教师打开.ordinal() , TopicBunuOpenType.超时开启.ordinal()])  , "open_time" : $$('$gte' : dateBefore(7).getTime() )]),
	//				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
	//				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
	//				).results().iterator();
	//		if(itear2 != null){
	//			while (itear2.hasNext()){
	//				def obj = itear2.next();
	//				if(obj && obj["mmoney"] != null){
	//					week = obj["mmoney"]
	//				}
	//			}
	//		}
	//
	//		//近一个月收益
	//		def month = 0d;
	//		def itear3 = topic_bunus().aggregate(
	//				$$('$match', ["user_id" : user_id , "open_type" : $$('$in' : [TopicBunuOpenType.教师打开.ordinal() , TopicBunuOpenType.超时开启.ordinal()])  , "open_time" : $$('$gte' : dateBefore(30).getTime() )]),
	//				$$('$project', ["user_id": '$user_id',mmoney: '$mmoney']),
	//				$$('$group', [_id: '$user_id',mmoney:[$sum: '$mmoney']])
	//				).results().iterator();
	//		if(itear3 != null){
	//			while (itear3.hasNext()){
	//				def obj = itear3.next();
	//				if(obj && obj["mmoney"] != null){
	//					month = obj["mmoney"]
	//				}
	//			}
	//		}
	//
	//		//累计抽奖次数
	//
	//		def redcount = topic_bunus().count($$("user_id" : user_id , "open_type" : 1));
	//
	//		//累计抢答次数
	//		int qiangdacount =(Integer) topics().count($$("teach_id" : user_id));
	//
	//		Map map = new HashMap();
	//		map["today"] = today;
	//		map["history"] = history;
	//		map["week"] = week;
	//		map["month"] = month;
	//		map["redcount"] = redcount;
	//		map["qiangdacount"] = qiangdacount;
	//		map["usermoney"] = userFinance ? userFinance.getUserMoney() : 0;
	//		return getResultOKS(map);
	//	}
	//	/**
	//	 *  我的收益
	//	 * @Description: 我的收益
	//	 * @date 2015年7月30日 上午11:20:15
	//	 */
	//	def myearnings(HttpServletRequest request){
	//		//用户id
	//		Integer user_id = Web.getCurrentUserId();
	//		//今天的收益
	//		def today =  sessionFactory.getCurrentSession()
	//				.createCriteria(Bunus.class)
	//				.setProjection(Projections.sum(Bunus.PROP_MMONEY))
	//				.add(Restrictions.eq(Bunus.PROP_USERID, user_id))
	//				.add(Restrictions.eq(Bunus.PROP_ISOPEN, 1))
	//				.add(Restrictions.ge(Bunus.PROP_OPENTIME, dateBefore(0)))
	//				.uniqueResult()?: 0;
	//
	//
	//		//总计收益
	//		def history = sessionFactory.getCurrentSession()
	//				.createCriteria(Bunus.class)
	//				.setProjection(Projections.sum(Bunus.PROP_MMONEY))
	//				.add(Restrictions.eq(Bunus.PROP_USERID, user_id))
	//				.add(Restrictions.eq(Bunus.PROP_ISOPEN, 1))
	//				.uniqueResult();
	//		history =  (history==null)?0:(Double)history;
	//		//账户余额
	//		def uFinance = sessionFactory.getCurrentSession()
	//				.createCriteria(UserFinance.class)
	//				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
	//				.uniqueResult();
	//
	//		UserFinance userFinance = uFinance ? uFinance : null;
	//		//近一周收益
	//		def week =  sessionFactory.getCurrentSession()
	//				.createCriteria(Bunus.class)
	//				.setProjection(Projections.sum(Bunus.PROP_MMONEY))
	//				.add(Restrictions.eq(Bunus.PROP_USERID, user_id))
	//				.add(Restrictions.eq(Bunus.PROP_ISOPEN, 1))
	//				.add(Restrictions.ge(Bunus.PROP_OPENTIME, dateBefore(7)))
	//				.uniqueResult()?: 0;
	//
	//		//近一个月收益
	//		def month = sessionFactory.getCurrentSession()
	//				.createCriteria(Bunus.class)
	//				.setProjection(Projections.sum(Bunus.PROP_MMONEY))
	//				.add(Restrictions.eq(Bunus.PROP_USERID, user_id))
	//				.add(Restrictions.eq(Bunus.PROP_ISOPEN, 1))
	//				.add(Restrictions.ge(Bunus.PROP_OPENTIME, dateBefore(30)))
	//				.uniqueResult()?: 0;
	//
	//		//累计抽奖次数
	//		def redcount =  sessionFactory.getCurrentSession()
	//				.createCriteria(Bunus.class)
	//				.setProjection(Projections.count(Bunus.PROP_ID))
	//				.add(Restrictions.eq(Bunus.PROP_USERID, user_id))
	//				.add(Restrictions.eq(Bunus.PROP_ISOPEN, 1))
	//				.uniqueResult()?: 0;
	//
	//		//累计抢答次数
	//		int qiangdacount =(Integer) topics().count($$("teach_id" : user_id));
	//
	//		Map map = new HashMap();
	//		map["today"] = today;
	//		map["history"] = history;
	//		map["week"] = week;
	//		map["month"] = month;
	//		map["redcount"] = redcount;
	//		map["qiangdacount"] = qiangdacount;
	//		map["usermoney"] = userFinance ? userFinance.getUserMoney() : 0;
	//		return getResultOKS(map);
	//	}

	private Date dateBefore(int d){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.set(Calendar.HOUR_OF_DAY, 0);       //把当前时间小时变成０
		c.set(Calendar.MINUTE, 0);            //把当前时间分钟变成０
		c.set(Calendar.SECOND, 0);            //把当前时间秒数变成０
		c.set(Calendar.MILLISECOND, 0);       //把当前时间毫秒变成０
		c.add(Calendar.DATE, -d);			  //d天前
		return c.getTime();
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	private String formate(){
		return sdf.format(new Date());
	}

}

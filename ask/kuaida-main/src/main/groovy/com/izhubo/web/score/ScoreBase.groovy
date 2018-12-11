package com.izhubo.web.score;

import java.text.SimpleDateFormat
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock

import javax.annotation.Resource

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.orm.hibernate4.HibernateTransactionManager

import com.izhubo.model.AccScoreGainType
import com.izhubo.model.AccScoreType
import com.izhubo.rest.anno.Rest
import com.izhubo.web.mq.MessageProductor
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mysqldb.model.ScoreDetail
import com.mysqldb.model.UserFinance
import com.mysqldb.model.UserScore

@Rest
public class ScoreBase  {

	private static Logger logger = LoggerFactory
	.getLogger(ScoreBase.class);


	private Lock lock = new ReentrantLock();

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	private SessionFactory sessionFactory;

	@Resource
	public MongoTemplate mainMongo;


	@Resource
	private  HibernateTransactionManager txManager;

	@Resource
	private MessageProductor messageProductorService;

	public ScoreBase() {
	}


	public ScoreBase(SessionFactory sessionFactoryc,MongoTemplate mainMongoc) {

		sessionFactory = sessionFactoryc;
		mainMongo = mainMongoc;
	}


	public static String dateToString(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyyMM HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}

	public static String dateToStringNormal(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}

	public static java.sql.Timestamp dateStart(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyy-MM-dd 00:00:00");
		String ctime = formatter.format(time);
		Date result = formatter.parse(ctime);

		java.sql.Timestamp dateTime = new java.sql.Timestamp(result.getTime());
		return dateTime;
	}
	public static java.sql.Timestamp dataEnd(Date time){

		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyy-MM-dd 00:00:00");
		String ctime = formatter.format(time);

		int dayMis = 1000 * 60 * 60 * 24;//
		long endLong = formatter.parse(ctime).getTime() + dayMis - 1;

		java.sql.Timestamp dateTime = new java.sql.Timestamp(new Date(endLong).getTime());
		return dateTime;
	}

	//基础类库默认不判断，这个留给业务层面去判断
	public boolean IsAllow(double score,int scoreGainType,Integer userid,Date datetime)
	{
		return true;
	}



	//新增积分
	public void AddScore(double score,int scoreGainType,Integer userid,String nickname,Date datetime )
	{
		if(IsAllow(score,scoreGainType, userid,datetime))
		{
			BasicDBObject scoreType = GetScoreType(scoreGainType);
			int scoreTypevalue = scoreType.getInt("score_type");
			double gainScore = scoreType.getDouble("score");

			//如果积分是不定的，则入账积分由传入的分值决定
			if(gainScore == 0.0)
			{

			}
			else
			{
				score = gainScore;
			}


			try {
				lock.lock();
				Boolean ischeck =CheckTypeAllow(score,scoreGainType, userid,datetime );
				if(ischeck)
				{
					addScoreIntoDataBase(score,scoreGainType, userid,nickname,datetime );
				}
			}catch (Exception e) {
				e.printStackTrace()
			}finally{
		     	lock.unlock();
			}

		}
	}

	//是否允许新增积分：新增积分的时候，判断是否允许，基础类库，只能判断每天限制的积分，和只赠送首次的积分，对于其他附加条件则无法判断。
	private boolean CheckTypeAllow(double score,int scoreGainType,Integer userid,Date datetime )
	{
		Session asyncSession =  sessionFactory.openSession();
		Boolean Result = false;
		try {
			BasicDBObject scoreType = GetScoreType(scoreGainType);
			int scoreTypevalue = scoreType.getInt("score_type");
			int limit_perday = scoreType.getInt("limit_perday");
			
			
			
			if( scoreTypevalue == AccScoreType.首次赠送.ordinal())
			{
				//判断之前是否赠送过，如果赠送过则取消
				
				int count = (int)asyncSession.createCriteria(ScoreDetail.class)
						.setProjection(Projections.count(ScoreDetail.PROP_ID))
						.add(Restrictions.eq(ScoreDetail.PROP_USERID, userid))
						.add(Restrictions.eq(ScoreDetail.PROP_SCORETYPE, scoreGainType))
						.uniqueResult();
				
				asyncSession.flush();
				asyncSession.close();
				
				if(count>0)
				{
					Result = false;
				}
				else
				{
					Result = true;
				}
			}
			else if (scoreTypevalue ==AccScoreType.每天有限赠送.ordinal() )
			{
				
				
				int count = (int)asyncSession.createCriteria(ScoreDetail.class)
						.setProjection(Projections.count(ScoreDetail.PROP_ID))
						.add(Restrictions.eq(ScoreDetail.PROP_USERID, userid))
						.add(Restrictions.eq(ScoreDetail.PROP_SCOREGAINTYPE, scoreGainType))
						.add(Restrictions.between(ScoreDetail.PROP_CREATETIME,dateStart(datetime),dataEnd(datetime)))
						.uniqueResult();
				asyncSession.flush();
				asyncSession.close();
				
				if(count>limit_perday)
				{
					Result = false;
				}
				else
				{
					Result = true;
				}
			}
			else
			{
				//无需判断，直接入库
				Result = true;
			}
		} catch (Exception e) {
			e.printStackTrace()
		}finally{
			if(asyncSession != null && asyncSession.isConnected()){
				asyncSession.close();
			}
		}

		return Result;

	}

	//积分入库
	private void addScoreIntoDataBase(double score,int scoreGainType,Integer userid,String nickname,Date datetime )
	{
		Session asyncSession =  sessionFactory.openSession();
		asyncSession.beginTransaction();
		try {

			BasicDBObject scoreType = GetScoreType(scoreGainType);
			int scoreTypevalue = scoreType.getInt("score_type");
			String score_detail = scoreType.getString("score_detail");

			ScoreDetail scoredetail = new ScoreDetail();

			java.sql.Timestamp dateTimesql = new java.sql.Timestamp(datetime.getTime());


			scoredetail.setScoreDetail(score_detail);
			scoredetail.setScoreType(scoreTypevalue);
			scoredetail.setScoreGainType(scoreGainType);
			scoredetail.setCreateTime(dateTimesql);
			scoredetail.setUserId(userid);
			scoredetail.setUserNickname(nickname);
			scoredetail.setScore(new java.math.BigDecimal(score));

			asyncSession.save(scoredetail);



			UserScore ufe = (UserScore)asyncSession.createCriteria(UserScore.class)
					.add(Restrictions.eq(UserScore.PROP_USERID, userid))	//用户id
					.uniqueResult();

			if(ufe == null){
				ufe = new UserScore();
				ufe.setUserId(userid);
				ufe.setUserScoreRemain(new java.math.BigDecimal(0));
			}
			ufe.setUserScoreRemain(ufe.getUserScoreRemain()+score);



			asyncSession.saveOrUpdate(ufe);
			asyncSession.getTransaction().commit();
			asyncSession.flush();
			asyncSession.close();

		} catch (Exception e) {

			asyncSession.getTransaction().rollback();
//			asyncSession.flush();
//			asyncSession.close();
			throw new RuntimeException(e);
		}
		finally{

//			asyncSession.flush();
			if(asyncSession != null && asyncSession.isConnected()){
				asyncSession.close();
			}
		}

	}

	//积分没规定好，必须由程序算出来的，则直接发送
	public void PushScoreMsg(double score,AccScoreGainType scoreGainType,Integer userid,String nickname  )
	{
		//

		JSONObject jo = new JSONObject();
		jo.put("score", score);
		jo.put("score_gain_type", scoreGainType.ordinal());
		jo.put("user_id", userid);
		jo.put("nickname", nickname);
		jo.put("create_time", dateToStringNormal(new Date()));

		//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_score",  jo.toString())
	}

	//积分预先就规定好的，直接调用即可
	public void PushScoreMsg(AccScoreGainType scoreGainType,Integer userid,String nickname  )
	{
		JSONObject jo = new JSONObject();
		jo.put("score_gain_type", scoreGainType.ordinal());
		jo.put("user_id", userid);
		jo.put("score", 0);
		jo.put("nickname", nickname);
		jo.put("create_time", dateToStringNormal(new Date()));

		//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_score", jo.toString());
	}

	private BasicDBObject GetScoreType(int type)
	{

		return	(BasicDBObject)mainMongo.getCollection("score_type").findOne(new BasicDBObject("score_gain_type",type));

	}

	/**
	 * 用户可使用的最大积分
	 * @param user_id 	用户id
	 * @return			积分
	 */
	public Double userUseMaxScore(Integer user_id){
		Session session =  sessionFactory.openSession();
		UserScore ufe = (UserScore)session.createCriteria(UserScore.class)
				.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))	//用户id
				.uniqueResult();

		if(ufe == null){
			return 0l;
		}else {
			return ufe.getUserScoreRemain().toDouble();
		}
	}

	/**
	 * 获取当前积分比例 sacle可兑换1元
	 * @return scale
	 */
	public Double getScale1(){
		Double scale = 0d;
		DBObject score_money = mainMongo.getCollection("score_money").findOne(new BasicDBObject("is_delete",0) , new BasicDBObject("scale",1));
		if(score_money != null){
			scale = Double.valueOf(score_money.get("scale").toString());
		}
		return scale;
	}

	/**
	 * 获取当前积分比例 1积分可抵多少元
	 * @return scale
	 */
	public Double getScale2(){
		Double scale = getScale1();
		if(scale > 0){
			scale = div(1d , scale) ;
			//			scale = 1d / scale ;
		}
		return scale;
	}

	/**
	 * money需要多少积分
	 * @param money
	 * @return
	 */
	public Double moneyEqScore(double money){
		return mul(getScale1() , money);
	}

	public Double userScoreToMoneyMax(Integer user_id){
		return userUseMaxScore(user_id) * getScale2();
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
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, 2);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}


}



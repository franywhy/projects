package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder;
import com.mysqldb.model.UserFinance

/**
 * 教师详情
 * @author Administrator
 *
 */
@RestWithSession
class TeacherController extends BaseController{

	static final  Logger logger = LoggerFactory.getLogger(TeacherController.class)
	
	@Resource
	private SessionFactory sessionFactory;
	
	DBCollection _users(){
		mainMongo.getCollection('users')
	}
	DBCollection _qquser(){
		qquserMongo.getCollection('qQUser')
	}
	private DBCollection topics(){
		return mainMongo.getCollection("topics");
	}
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	
	private DBCollection day_login(){
		return logMongo.getCollection("day_login");
	}
	
	private DBCollection topic_tip(){
		return mainMongo.getCollection("topic_tip");
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){

		def query = QueryBuilder.start();
		stringQuery(query,req,'nick_name');
		
		if (req["user_id"]){
			query.and(_id).is(req.getInt("user_id"));
		}
		
	
		
		query.and("priv").is(UserType.主播.ordinal());
		
		Session session = sessionFactory.getCurrentSession();
		
		
//		def query_bunus_num = $$();
//		//红包表
//		StringBuffer sql_bunus = new StringBuffer();
//		sql_bunus.append(" SELECT ");
//		sql_bunus.append("         COUNT(1) as 'bunus_num' , ");  //红包数量
//		sql_bunus.append("         sum(mmoney) as 'bonus_money' , "); //总金额
//		sql_bunus.append("         SUM(CASE  WHEN open_time IS NULL THEN 0 ELSE 1 END ) as open_bunus_num ");//打开红包的数量
//		sql_bunus.append("   FROM bunus ");
//		sql_bunus.append("  WHERE user_id = ? ");
//		sql_bunus.append("  GROUP BY user_id ");
//		
//		//提现申请
		StringBuffer sql_apply = new StringBuffer();
//		sql_apply.append(" SELECT COUNT(1), ");
		sql_apply.append(" SELECT ");
		//提现完成
		sql_apply.append("        SUM(CASE WHEN apply_state = 2 THEN 1 ELSE 0 END ) as apply_success_num ,  SUM(CASE WHEN apply_state = 2 THEN apply_money ELSE 0 END ) as apply_success_money ,");
		//提现中
		sql_apply.append("        SUM(CASE WHEN apply_state = 0 THEN 1 ELSE 0 END ) as apply_ing_num ,  SUM(CASE WHEN apply_state = 0 THEN apply_money ELSE 0 END ) as apply_ing_money , ");
		//提现失败
		sql_apply.append("        SUM(CASE WHEN apply_state = 1 THEN 1 ELSE 0 END ) as apply_fail_num ");
		sql_apply.append("   FROM apply ");
		sql_apply.append("  WHERE user_id = ? ");
		sql_apply.append("  GROUP BY user_id ");
		
		def show_file = $$("_id" : 1 , "nick_name" : 1 , "tuid" : 1 , "topic_evaluation_count" : 1 , "topic_count" : 1 , "vlevel" : 1 , "province" : 1 , "city" : 1);
		
		def desc = $$("topic_count" : -1);
		
		Crud.list(req,users(),query.get(),show_file,desc){List<BasicDBObject> data->
			
			
			for(BasicDBObject obj : data){
				
				//联系方式
				String tuid = obj["tuid"];
				obj["mobile"] = _qquser().findOne($$("tuid" : tuid) , $$("username" : 1))?.get("username");
				//删除tuid
				obj.remove("tuid");
				
				def user_id = obj["_id"];
				
				//满意度
				obj["satisfaction"] = teacher_replay_end_percent(user_id);
				
//				//红包查询结果 红包数量  总金额  红包打开数量
//				def bunus_res = session.createSQLQuery(sql_bunus.toString()).setParameter(0, user_id).uniqueResult();
//				
				//提现申请
				def apply_res = session.createSQLQuery(sql_apply.toString()).setParameter(0, user_id).uniqueResult();
				
				int islogin = day_login().count($$("user_id",user_id));
				
				if(islogin>0)
				{
					obj["islogin"] = 1;
				}
				else
				{
					obj["islogin"] = 0;
				}
				
				//红包数量
//				obj["bunus_num"] = topic_bunus().count($$("user_id" : user_id));
				//红包数量 总金额
				Iterator records = topic_bunus().aggregate(
					$$('$match' , ["user_id":user_id]),
					$$('$group' , [_id : null , count:[$sum : 1] , money : [$sum : '$mmoney']])
					).results().iterator();
				Integer bunus_num = 0;
				Double bunus_money = 0d;
				if(records.hasNext()){
					def robj = records.next();
					if(robj){
						bunus_num = (robj.get("count") as Double).intValue();
						bunus_money = robj.get("money") as Double;
					}
				}
				//红包数量
				obj["bunus_num"] = bunus_num;
				//总金额
				obj["bonus_money"] = bunus_money;
				
				
				
				//打开红包的数量
				obj["bunus_open_num"] = topic_bunus().count($$("user_id" : user_id , "open_type" : 1));
						
//				//红包表
//				if(bunus_res){
////					println "bunus_res.getClass() :" + bunus_res.getClass();
//					//红包数量
//					obj["bunus_num"] = bunus_res[0];
//					//总金额
//					obj["bonus_money"] = bunus_res[1];
//					//打开红包的数量
//					obj["bunus_open_num"] = bunus_res[2];
//				}else{
//					//红包数量
//					obj["bunus_num"] = 0;
//					//总金额
//					obj["bonus_money"] = 0;
//					//打开红包的数量
//					obj["bunus_open_num"] = 0;
//				}
				
				
				//打赏数量 总金额
				Iterator tip_records = topic_tip().aggregate(
					$$('$match' , ["to_user_id":user_id]),
					$$('$group' , [_id : null , count:[$sum : 1] , money : [$sum : '$kd_money']])
					).results().iterator();
				Integer tip_num = 0;
				Double tip_money = 0d;
				if(tip_records.hasNext()){
					def tobj = tip_records.next();
					if(tobj){
						tip_num = (tobj.get("count") as Double).intValue();
						tip_money = tobj.get("money") as Double;
					}
				}
				//打赏数量
				obj["tip_num"] = tip_num;
				//打赏额
				obj["tip_money"] = tip_money;
				
				
				//提现申请
				if(apply_res){
					//成功提现笔数
					obj["apply_success_num"] = apply_res[0];
					//成功提现总金额
					obj["apply_success_money"] = apply_res[1];
					
					//提现中笔数
					obj["apply_ing_num"] = apply_res[2];
					//提现中总金额
					obj["apply_ing_money"] = apply_res[3];
					
					//提现失败总笔数
					obj["apply_fail_num"] = apply_res[4];
				}else{
					//成功提现笔数
					obj["apply_success_num"] = 0;
					//成功提现总金额
					obj["apply_success_money"] = 0;
					
					//提现中笔数
					obj["apply_ing_num"] = 0;
					//提现中总金额
					obj["apply_ing_money"] = 0;
					
					//提现失败总笔数
					obj["apply_fail_num"] = 0;
				}
				
				//3.账户余额表
				UserFinance userFinance = (UserFinance) session.createCriteria(UserFinance.class).add(Restrictions.eq(UserFinance.PROP_USERID, user_id)).uniqueResult();
				if(userFinance){
					obj["account_money"] = userFinance.getUserMoney();
				}else{
					obj["account_money"] = 0;
				}
			}
		}
	}
//	@TypeChecked(TypeCheckingMode.SKIP)
//	def list(HttpServletRequest req){
//		
//		def query = QueryBuilder.start();
//		stringQuery(query,req,'nick_name');
//		
//		if (req["user_id"]){
//			query.and(_id).is(req.getInt("user_id"));
//		}
//		
//		
//		
//		query.and("priv").is(UserType.主播.ordinal());
//		
//		Session session = sessionFactory.getCurrentSession();
//		
//		//红包表
//		StringBuffer sql_bunus = new StringBuffer();
//		sql_bunus.append(" SELECT ");
//		sql_bunus.append("         COUNT(1) as 'bunus_num' , ");  //红包数量
//		sql_bunus.append("         sum(mmoney) as 'bonus_money' , "); //总金额
//		sql_bunus.append("         SUM(CASE  WHEN open_time IS NULL THEN 0 ELSE 1 END ) as open_bunus_num ");//打开红包的数量
//		sql_bunus.append("   FROM bunus ");
//		sql_bunus.append("  WHERE user_id = ? ");
//		sql_bunus.append("  GROUP BY user_id ");
//		
//		//提现申请
//		StringBuffer sql_apply = new StringBuffer();
////		sql_apply.append(" SELECT COUNT(1), ");
//		sql_apply.append(" SELECT ");
//		//提现完成
//		sql_apply.append("        SUM(CASE WHEN apply_state = 2 THEN 1 ELSE 0 END ) as apply_success_num ,  SUM(CASE WHEN apply_state = 2 THEN apply_money ELSE 0 END ) as apply_success_money ,");
//		//提现中
//		sql_apply.append("        SUM(CASE WHEN apply_state = 0 THEN 1 ELSE 0 END ) as apply_ing_num ,  SUM(CASE WHEN apply_state = 0 THEN apply_money ELSE 0 END ) as apply_ing_money , ");
//		//提现失败
//		sql_apply.append("        SUM(CASE WHEN apply_state = 1 THEN 1 ELSE 0 END ) as apply_fail_num ");
//		sql_apply.append("   FROM apply ");
//		sql_apply.append("  WHERE user_id = ? ");
//		sql_apply.append("  GROUP BY user_id ");
//		
//		def show_file = $$("_id" : 1 , "nick_name" : 1 , "tuid" : 1 , "topic_evaluation_count" : 1 , "topic_count" : 1 , "vlevel" : 1 , "province" : 1 , "city" : 1);
//		
//		def desc = $$("topic_count" : -1);
//		
//		Crud.list(req,users(),query.get(),show_file,desc){List<BasicDBObject> data->
//		
//		
//		for(BasicDBObject obj : data){
//			
//			//联系方式
//			String tuid = obj["tuid"];
//			obj["mobile"] = _qquser().findOne($$("tuid" : tuid) , $$("username" : 1))?.get("username");
//			//删除tuid
//			obj.remove("tuid");
//			
//			def user_id = obj["_id"];
//			
//			//满意度
//			obj["satisfaction"] = teacher_replay_end_percent(user_id);
//			
//			//红包查询结果 红包数量  总金额  红包打开数量
//			def bunus_res = session.createSQLQuery(sql_bunus.toString()).setParameter(0, user_id).uniqueResult();
//			
//			//提现申请
//			def apply_res = session.createSQLQuery(sql_apply.toString()).setParameter(0, user_id).uniqueResult();
//			
//			int islogin = day_login().count($$("user_id",user_id));
//			
//			if(islogin>0)
//			{
//				obj["islogin"] = 1;
//			}
//			else
//			{
//				obj["islogin"] = 0;
//			}
//			
//			//红包表
//			if(bunus_res){
////					println "bunus_res.getClass() :" + bunus_res.getClass();
//				//红包数量
//				obj["bunus_num"] = bunus_res[0];
//				//总金额
//				obj["bonus_money"] = bunus_res[1];
//				//打开红包的数量
//				obj["bunus_open_num"] = bunus_res[2];
//			}else{
//				//红包数量
//				obj["bunus_num"] = 0;
//				//总金额
//				obj["bonus_money"] = 0;
//				//打开红包的数量
//				obj["bunus_open_num"] = 0;
//			}
//			
//			//提现申请
//			if(apply_res){
//				//成功提现笔数
//				obj["apply_success_num"] = apply_res[0];
//				//成功提现总金额
//				obj["apply_success_money"] = apply_res[1];
//				
//				//提现中笔数
//				obj["apply_ing_num"] = apply_res[2];
//				//提现中总金额
//				obj["apply_ing_money"] = apply_res[3];
//				
//				//提现失败总笔数
//				obj["apply_fail_num"] = apply_res[4];
//			}else{
//				//成功提现笔数
//				obj["apply_success_num"] = 0;
//				//成功提现总金额
//				obj["apply_success_money"] = 0;
//				
//				//提现中笔数
//				obj["apply_ing_num"] = 0;
//				//提现中总金额
//				obj["apply_ing_money"] = 0;
//				
//				//提现失败总笔数
//				obj["apply_fail_num"] = 0;
//			}
//			
//			//3.账户余额表
//			UserFinance userFinance = (UserFinance) session.createCriteria(UserFinance.class).add(Restrictions.eq(UserFinance.PROP_USERID, user_id)).uniqueResult();
//			if(userFinance){
//				obj["account_money"] = userFinance.getUserMoney();
//			}else{
//				obj["account_money"] = 0;
//			}
//		}
//		}
//	}
	
	//好评百分比
	@TypeChecked(TypeCheckingMode.SKIP)
	def teacher_replay_end_percent(Integer teacher_id){
		long y = teacher_replay_end_with_evaluation(teacher_id);
		long z = 0;
		if(y != 0){
			z = teacher_replay_end_well(teacher_id) / y * 100;
		}
		return NumberUtil.formatDouble3(z , 0)  + "%";
	}
	
	//老师回答的问题-已结束-并且已经评价的（未评价的不计入满意计算中）
	def teacher_replay_end_with_evaluation(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id , "type" : TopicsType.问题已结束.ordinal() , "deleted" : false,"evaluation_type": $$($ne : TopicEvaluationType.未评价.ordinal()) ));
	}
	//老师回答的问题-已结束-满意的数量
	def teacher_replay_end_well(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id ,"type" : TopicsType.问题已结束.ordinal()  , "evaluation_type" : $$('$in' : [TopicEvaluationType.满意.ordinal() , TopicEvaluationType.非常满意.ordinal()]) ));
	}
}

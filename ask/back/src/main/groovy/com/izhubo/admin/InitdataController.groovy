package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Criteria
import org.hibernate.SessionFactory
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions

import com.hqonline.model.UserIncomType
import com.izhubo.model.ApplyState
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.model.Apply
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class InitdataController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	
	DBCollection table(){
		return logMongo.getCollection('user_incom_log');
	}
	
	def list(HttpServletRequest req){
		List list = new ArrayList();
		Map map0 = new HashMap();
		map0.put("text" , "V3.0同步提现申请-提现申请和提现失败的数据同步到教师收入明细表中");
		map0.put("method" , "/initdata/initApply");
		list.add(map0);
		return ["code" : 1 , "data" : list];
	}
	
	
	
	/**
	 * 初始化数据
	 * @return
	 */
	def initApply(HttpServletRequest req){
		initApplying();
		initApplyfail();
		return ["code" : 1];
	}
	/**
	 * 申请中数据
	 */
	def initApplying(){
		
		int size = 2000;
		int page = 1;
		
		//List数据
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Apply.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(Apply.class).setProjection(Projections.count(Apply.PROP_ID));
		//已申请
		criterion.add(Restrictions.or(Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已申请.ordinal()) , Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已作废.ordinal())));
		criterion_count.add(Restrictions.or(Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已申请.ordinal()) , Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已作废.ordinal())));
		//总数量
		int apply_count = (Integer) criterion_count.uniqueResult();
		//总共页数
		int allpage = apply_count / size + apply_count% size >0 ? 1 : 0;
		
		int applyType = 2;
		for(;page <= allpage; page++){
			def applyList = criterion.addOrder(Order.desc(Apply.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();
			applyList.each {Apply apply_item->
				java.sql.Timestamp t = apply_item.getCreateTime();
				if(t == null){
					t = apply_item.getUpdateTime();
				}
				Long l = 0;
				if(t == null){
					l = System.currentTimeMillis();
				}else{
					l = t.getTime();
				}
				
				//Integer user_id , Double money  , Integer type , String topic_id , Long timestamp , Integer apply_id
				def m = getSaveMap(apply_item.getUserId() ,(Double)apply_item.getApplyMoney(),applyType,null,l, apply_item.getId());
				
				table().save(m);
//				table().save($$(
//						//"_id" : apply_item.getCreateTime().getTime() + "_"+apply_item.getUserId(),
//						"_id" : 'apply_' + apply_item.getId(),
//						"type" : applyType,//申请体现类型
//						"timestamp" : l,//时间
//						"user_id" : apply_item.getUserId(),//用户id
//						"topic_id" : null,//问题id
//						"remark" : "提现申请",//备注
//						"money" : apply_item.getApplyMoney(),//金额
//						"apply_id" : apply_item.getId(),
//						"sync" : 1
//						));
			}
		}
	
	}
	/**
	 * 申请失败数据
	 */
	def initApplyfail(){
		
		int size = 2000;
		int page = 1;
		
		//List数据
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Apply.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(Apply.class).setProjection(Projections.count(Apply.PROP_ID));
		//已申请
		criterion.add(Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已作废.ordinal()));
		criterion_count.add(Restrictions.eq(Apply.PROP_APPLYSTATE , ApplyState.已作废.ordinal()));
		//总数量
		int apply_count = (Integer) criterion_count.uniqueResult();
		//总共页数
		int allpage = apply_count / size + apply_count% size >0 ? 1 : 0;
		
		int applyType = 3;
		for(;page <= allpage; page++){
			def applyList = criterion.addOrder(Order.desc(Apply.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();
			applyList.each {Apply apply_item->
				java.sql.Timestamp t = apply_item.getUpdateTime();
				if(t == null){
					t = apply_item.getCreateTime();
				}
				
				Long l = 0;
				if(t == null){
					l = System.currentTimeMillis();
				}else{
					l = t.getTime();
				}
				def m = getSaveMap(apply_item.getUserId() ,(Double)(-apply_item.getApplyMoney()),applyType,null,l, apply_item.getId());
				table().save(m);
//				table().save($$(
//						//"_id" : apply_item.getUpdateTime().getTime() + "_"+apply_item.getUserId(),
//						"_id" : 'apply_' + apply_item.getId(),
//						"type" : applyType,//申请体现类型
//						"timestamp" : l,//时间
//						"user_id" : apply_item.getUserId(),//用户id
//						"topic_id" : null,//问题id
//						"remark" : "提现失败,返还金额",//备注
//						"money" : -apply_item.getApplyMoney(),//金额
//						"apply_id" : apply_item.getId(),
//						"sync" : 1
//						));
			}
		}
		
	}
	
	/**
	 * 获取保存收支明细的MAP
	 * @param user_id	用户id
	 * @param money		金额
	 * @param type		类型 @see com.hqonline.model.UserIncomType
	 * @param topic_id	问题ID
	 * @param timestamp 时间戳
	 * @param apply_id	申请ID
	 * @return			Map<String , Object>
	 */
	public BasicDBObject getSaveMap(Integer user_id , Double money  , Integer type , String topic_id , Long timestamp , Integer apply_id){
//		new BasicDBObject
		BasicDBObject map = new BasicDBObject();
		String id = null;
		if(type == UserIncomType.红包.ordinal()){
			id = UserIncomType.红包.ordinal() + "_" + topic_id + "_" + user_id;
		}else if(type == UserIncomType.打赏.ordinal()){
			id = UserIncomType.打赏.ordinal() + "_" + topic_id + "_" + user_id;
		}else if(type == UserIncomType.申请提现.ordinal()){
			id = UserIncomType.申请提现.ordinal() + "_" + apply_id + "_" + user_id;
		}else if(type == UserIncomType.提现失败.ordinal()){
			id = UserIncomType.提现失败.ordinal() + "_" + apply_id + "_" + user_id;
		}else{
			id = System.currentTimeMillis() + "";
		}
		
		map.put("_id", id);
		map.put("user_id", user_id);
		map.put("money", money);
		map.put("timestamp", timestamp);
		map.put("type", type);
		map.put("topic_id", topic_id);
		map.put("apply_id", apply_id);
		map.put("ts", System.currentTimeMillis());
		
		return map;
	}
	
}

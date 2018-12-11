package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder


/**
 * 订单管理
 * @Description: 
 * @author zbm  
 * @date 2015年2月6日 上午11:42:39
 */
@RestWithSession
class PayOrderController extends BaseController{
	
	@Resource
	KGS baseKGS;
	DBCollection table(){mainMongo.getCollection('pay_order')}
	DBCollection _course(){mainMongo.getCollection('course')}
	DBCollection _rooms(){mainMongo.getCollection('rooms')}
	DBCollection _admins(){adminMongo.getCollection('admins')}
	DBCollection _users(){ mainMongo.getCollection('users')}
	
	DBCollection _employment(){mainMongo.getCollection('employment_registration')}
	
     
	//订单列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
//		Integer item_type =req.getParameter("type") as Integer;
//      query.and("item_type").is(item_type);
		intQuery(query,req,'item_type')
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer pay_user_id = obj['user_id'] as Integer;
				Integer user_id = obj['manage_info']['create_user_id'] as Integer;
				Integer update_id = obj['manage_info']['update_user_id'] as Integer;
				
				if(pay_user_id){
					obj.put("pay_user_name", _users().findOne(new BasicDBObject("_id" : pay_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
	
				if(user_id){
					obj.put("user_name", _admins().findOne(new BasicDBObject("_id" : user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(update_id){
					obj.put("update_name", _admins().findOne(new BasicDBObject("_id" : update_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
			}
		}
	}
	
	//学员下拉框列表
	//@TypeChecked(TypeCheckingMode.SKIP)
	def user_list(HttpServletRequest req){ //TODO
		Map user = req.getSession().getAttribute("user") as Map;
		String search=req.getParameter("search");
		def query = QueryBuilder.start()
			query.and("priv").is(UserType.普通用户.ordinal());
		if (StringUtils.isNotBlank(search)) {
			//启用不区分大小写的匹配。
			Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nick_name").regex(pattern)
		}
		Crud.list(req,_users(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	//商品列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def item_list(HttpServletRequest req){
		Integer item_type =req.getParameter("item_type") as Integer;
		Map user = (Map) req.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		def query = QueryBuilder.start();
		//获取公司id进行数据权限处理
	//	query.and("company_id").is(company_id);  //暂时不用公司权限
		String search=req.getParameter("search");
		if (item_type==0){ 
		//商品类型为不包就业课程的列表
			if (StringUtils.isNotBlank(search)) {
				//启用不区分大小写的匹配。
				Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
				query.and("course_name").regex(pattern)
			}
		  query.and("course_price.item_type").is(0);
		  Crud.list(req,_course(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer teacher_id = obj['teacher_id']as Integer;
				if(teacher_id){
					obj.put("teacher_name", _users().findOne(new BasicDBObject("_id" : teacher_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				for (BasicDBObject obj1: obj['course_price']){	
					Integer price_type = obj1['item_type']as Integer;
					if (price_type==0){
						Double item_price=obj1['price'] as Double;
						obj.put("item_price",item_price);
					}
			    }
			 }	
		  }
		}else if (item_type==1)	{
		//商品类型为包就业的列表
		if (StringUtils.isNotBlank(search)) {
			//启用不区分大小写的匹配。
			Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("course_name").regex(pattern)
		}
		query.and("course_price.item_type").is(1);
		Crud.list(req,_course(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer teacher_id = obj['teacher_id']as Integer;
				if(teacher_id){
					obj.put("teacher_name", _users().findOne(new BasicDBObject("_id" : teacher_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				for (BasicDBObject obj1: obj['course_price']){
					Integer price_type = obj1['item_type']as Integer;
					if (price_type==1){
						Double item_price=obj1['price'] as Double;
						obj.put("item_price",item_price);
					}
				}
			 }
		  }
            
		}else if (item_type==3){
		   //商品类型为直播间的列表
			if (StringUtils.isNotBlank(search)) {
				//启用不区分大小写的匹配。
				Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
				query.and("room_name").regex(pattern)
			}
		   query.and("room_type").is(4);
		   Crud.list(req,_rooms(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer room_type = obj['room_type']as Integer;
				switch (room_type){
					case 0 :obj.put("room_type_name", "在线咨询");
					break;
					case 1 : obj.put("room_type_name", "课程直播间");
					break;
					case 2 : obj.put("room_type_name", "答疑直播间");
					break;
					case 3 : obj.put("room_type_name", "上岗指导直播间");
					break;
					case 4 : obj.put("room_type_name", "大讲堂");
					break;
					case 5 : obj.put("room_type_name", "员工直播间");
					break;
					}
			 }	
		  }
		}
	}
	
	def add(HttpServletRequest request)
	{   
		Map user = (Map) request.getSession().getAttribute("user");
		def pay_order=$$("_id" : UUID.randomUUID().toString());
		Long now = System.currentTimeMillis();
		// 推广渠道来源 
		pay_order.append("comefrom", null);
		// 是否到账
		pay_order.append("comfirm_state", null);
		//提交时间
		pay_order.append("create_time", now);
		//是否为内部充值
		pay_order.append("is_backend", true);
		//商品描述
		pay_order.append("item_detail", request.getParameter("item_detail"));
		//商品id
		pay_order.append("item_id", request.getParameter("item_id"));
		//商品名称 
		pay_order.append("item_name", request.getParameter("item_name"));
		//商品数量 
		Integer item_type =request.getParameter("item_type") as Integer;
		Integer item_num;
		Double item_price;
		Double money;
		if(item_type==2){
			item_num =request.getParameter("item_num") as Integer;
			pay_order.append("item_num", item_num);
			item_price=2000; //  //TODO  先设设默认值 	
		}else{
			item_num=1;
			pay_order.append("item_num", item_num);
			item_price=request.getParameter("item_price") as Double;
			}
		//订单金额
		money =item_price*item_num;
		pay_order.append("money",money);
		//商品图片url
		pay_order.append("item_pic_url", request.getParameter("item_pic_url"));
		//商品类型
		pay_order.append("item_type", request.getParameter("item_type") as Integer);
		if(item_type==3){    // TODO 直播间默认半年一次
			//商品有效期开始时间
			pay_order.append("item_begin_time", now);
			//商品有效期结束时间
			pay_order.append("item_end_time",  getHalfYearTime());
		} else {	
			//商品有效期开始时间
			pay_order.append("item_begin_time", null);
			//商品有效期结束时间
			pay_order.append("item_end_time", null);
		}
		//订单号
		Integer user_id=request.getParameter("user_id") as Integer;
		String orderNo  =  "xuelxuew_${user_id}_${System.currentTimeMillis()}";
		pay_order.append("order_no", orderNo);
		//订单状态
		pay_order.append("order_state", 0);
		//支付类型  
		pay_order.append("pay_type", null);
		//订单类型  0订单 1退单
		pay_order.append("pay_order_type", 0);
		//是否可以退款
		pay_order.append("refund_state", true);
		//订单结算状态
		pay_order.append("statement_state1", false);
		//就业结算状态
		pay_order.append("statement_state2", false);
		//支付渠道订单号
		pay_order.append("thirdpart_orderno", orderNo);
		//学员id 
		pay_order.append("user_id",user_id);	
		//管理信息
		Map manage_info = new HashMap();
		//创建人id
		manage_info.put("create_user_id",user.get("_id") as Integer);
		//创建日期
		manage_info.put("timestamp",now);
		//修改人Id
		manage_info.put("update_user_id",user.get("_id") as Integer);
		//修改日期
		manage_info.put("update_date",now);
		//提交标记
		manage_info.put("upload_flag" , false);
		//审核标记
		manage_info.put("audit_flag" , false);
		pay_order.append("manage_info", manage_info);
		table().save(pay_order);
		Crud.opLog("pay_order",[save:request["_id"]]);
		return OK();
	}
	//新增退单方法
	def add_refund(HttpServletRequest request){
		String old_order_no = request.getParameter("order_no");
		Map user = (Map) request.getSession().getAttribute("user");
		def pay_order=$$("_id" : UUID.randomUUID().toString());
		Long now = System.currentTimeMillis();
		// 推广渠道来源
		pay_order.append("comefrom", null);
		// 是否到账
		pay_order.append("comfirm_state", null);
		//提交时间
		pay_order.append("create_time", now);
		//是否为内部充值
		pay_order.append("is_backend", true);
		//商品描述
		pay_order.append("item_detail", request.getParameter("item_detail"));
		//商品id
		pay_order.append("item_id", request.getParameter("item_id"));
		//商品名称
		pay_order.append("item_name", request.getParameter("item_name"));
		//商品类型
		Integer item_type =request.getParameter("item_type") as Integer;
		//商品数量
		Integer item_num =request.getParameter("item_num") as Integer;
	    pay_order.append("item_num", item_num);
		double money =request.getParameter("money") as double;
		//订单金额
		pay_order.append("money",money);
		//商品图片url
		pay_order.append("item_pic_url", request.getParameter("item_pic_url"));
		//商品类型
		pay_order.append("item_type", request.getParameter("item_type") as Integer);
		if(item_type==3){    // TODO 直播间默认半年一次
			//商品有效期开始时间
			pay_order.append("item_begin_time", now);
			//商品有效期结束时间
			pay_order.append("item_end_time", getHalfYearTime());
		} else {
			//商品有效期开始时间
			pay_order.append("item_begin_time", null);
			//商品有效期结束时间
			pay_order.append("item_end_time", null);
		}
		//订单号
		Integer user_id=request.getParameter("user_id") as Integer;
		String orderNo  =  "xuelxuew_${user_id}_${System.currentTimeMillis()}";
		pay_order.append("order_no", orderNo);
		//订单状态
		pay_order.append("order_state", 0);  //待定
		//支付类型
		pay_order.append("pay_type", null);
		//订单类型  0订单 1退单
		pay_order.append("pay_order_type", 1);
		//是否可以退款   当退款额等于对应订单总金额时该值为true
		double caculate=caculate_amount(old_order_no);
		double value =caculate-money;
		if(value==0){
			table().update(new BasicDBObject("order_no":old_order_no),new BasicDBObject('$set':  new BasicDBObject("refund_state":false)));
		}
		pay_order.append("refund_state", false);
		//对应的退款单号
		pay_order.append("refund_order_no", old_order_no);
		//退款说明
		pay_order.append("refund_instructions",  request.getParameter("refund_instructions"));
		//订单结算状态
		pay_order.append("statement_state1", false);
		//就业结算状态
		pay_order.append("statement_state2", false);
		//支付渠道订单号
		pay_order.append("thirdpart_orderno", orderNo);
		//学员id
		pay_order.append("user_id",user_id);
		//管理信息
		Map manage_info = new HashMap();
		//创建人id
		manage_info.put("create_user_id",user.get("_id") as Integer);
		//创建日期
		manage_info.put("timestamp",now);
		//修改人Id
		manage_info.put("update_user_id",user.get("_id") as Integer);
		//修改日期
		manage_info.put("update_date",now);
		//提交标记
		manage_info.put("upload_flag" , false);
		//审核标记
		manage_info.put("audit_flag" , false);
		pay_order.append("manage_info", manage_info);
		table().save(pay_order);
		Crud.opLog("pay_order",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		Map user = (Map)request.getSession().getAttribute("user");
		BasicDBObject pay_order=new BasicDBObject();
		Long now = System.currentTimeMillis();
		//提交时间
		//pay_order.append("create_time", now);
		//商品描述
		pay_order.append("item_detail", request.getParameter("item_detail"));
		//商品id
		pay_order.append("item_id", request.getParameter("item_id"));
		//商品名称 
		pay_order.append("item_name", request.getParameter("item_name"));
		//商品图片url
		pay_order.append("item_pic_url", request.getParameter("item_pic_url"));
		//商品类型
		pay_order.append("item_type", request.getParameter("item_type") as Integer);
		Integer user_id=request.getParameter("user_id") as Integer;		
		//订单金额
		if(StringUtils.isNotBlank(request.getParameter("item_price"))){
		//商品类型
		Integer item_type =request.getParameter("item_type") as Integer;
		Integer item_num;
		Double item_price;
		Double money;
		if(item_type==2){
				item_num =request.getParameter("item_num") as Integer; //商品
				pay_order.append("item_num", item_num);
				item_price=2000; //  //TODO  先设设默认值 
				money =item_price*item_num;
				
			}else{
				item_num=1;
				pay_order.append("item_num", item_num);
				item_price=request.getParameter("item_price") as Double;
				money =item_price*item_num;
			}
			//总价
			pay_order.append("money",money);
		}
		//学员id 
		pay_order.append("user_id",user_id);
			
		//管理信息 修改人Id
		pay_order.put("manage_info.update_user_id",user.get("_id") as Integer);
		//修改日期
		pay_order.put("manage_info.update_date",now);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':pay_order));
		Crud.opLog("pay_order",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
	    DBObject pay_order = table().findOne(new BasicDBObject("_id" : id));
		table().remove(new BasicDBObject(_id,id))
		Integer state =pay_order['pay_order_type'] as Integer;
		if (state){
		String order_no=pay_order.get("refund_order_no");
		//删除退单后，更改对应的订单的退款状态
		table().update(new BasicDBObject("order_no":order_no),
			new BasicDBObject('$set': new BasicDBObject("refund_state":true)));
		}
		Crud.opLog("pay_order",[del:id]);
		return OK();
	}

	/**
	 * 提交
	 */	
	@TypeChecked(TypeCheckingMode.SKIP)
	def submit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
	   table().update(
		   new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : false),
		   new BasicDBObject('$set':
			   new BasicDBObject(
				   "manage_info.upload_flag" : true,
				   //提交人
				   "manage_info.upload_user_id": user.get("_id") as Integer,
				   "manage_info.upload_date" : System.currentTimeMillis()
				   )
			   ));
	   return OK();
	}
	/**
	 * 收回
	 */
	def recovery(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		table().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : true),
			new BasicDBObject('$set':new BasicDBObject("manage_info.upload_flag" : false,)
				));
		return OK();
	}
	/**
	 * 审核
	 */
	def audit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		table().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.audit_flag" : false),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.audit_flag" : true,
					//提交人
					"manage_info.audit_user_id": user.get("_id") as Integer,
					"manage_info.audit_date" : System.currentTimeMillis()
					)
				));
		return OK();
	}
	
	/**
	 * 反审核   
	 */
	def reaudit(HttpServletRequest request){  //需要加验证条件
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		def _id =request.getParameter("_id");
		if(isUse(_id)){
			return [code : 0 , msg : '订单已被引用，无法反审核！取消引用才可操作！'];
		}
		//TODO
		table().update(
			new BasicDBObject("_id":_id, "manage_info.audit_flag" : true),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.audit_flag" : false,
					)
				));
		return OK();
	}
	
	
	//订单是否被引用
	private Boolean isUse(String payorder_id){
		Boolean isUse = false;
		DBObject employment = _employment().findOne(new BasicDBObject("order_no" : payorder_id));
		if(employment){
			isUse = true;
		}
		return isUse;
	}
	
	
	//计算剩余退款金额
	private double caculate_amount(String order_no){
		double caculate;
		DBObject pay_order = table().findOne(new BasicDBObject("order_no" : order_no));
		double pay_money =pay_order.get("money") as double;
		
		List<DBObject> pay_list=table().find(new BasicDBObject("refund_order_no" : order_no)).toArray();
		double total_money=0;
		if(pay_list){
		for(DBObject dbo :pay_list){
			double refund_money=dbo['money'] as double;
			      total_money=+refund_money;
			}
		  }
		caculate=pay_money-total_money;
		return caculate;

	}
	
	//前段计算剩余退款金额
	def caculate_refund(HttpServletRequest request){
		double caculate;
		String order_no=request.getParameter("order_no");
		DBObject pay_order = table().findOne(new BasicDBObject("order_no" : order_no));
		double pay_money =pay_order.get("money") as double;
		
		List<DBObject> pay_list=table().find(new BasicDBObject("refund_order_no" : order_no)).toArray();
		double total_money=0;
		if(pay_list){
		for(DBObject dbo :pay_list){
			double refund_money=dbo['money'] as double;
				  total_money=+refund_money;
			}
		  }
		caculate=pay_money-total_money;
		//return caculate;
		return ["data" : caculate];

	}
	/**
	 * 获半年后的结束时间
	 * @return
	 */
	public long getHalfYearTime(){
		Calendar curr = Calendar.getInstance();
		// 推迟一周示例：
		//curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+7);
		
		//推迟一个月示例：
		//curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+1);
		
		//推迟一年示例：
		//curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
		curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+6);
		Date date=curr.getTime();
		long new_time =date.getTime();
	    return new_time;
	}
	
	//TODO 测试批量付款申请
	
	@Resource
	private AlipayBatchController alipaybatch;
	
	def applytest(HttpServletRequest request){
		String payment_id = request["payment_id"];
		if(StringUtils.isBlank(payment_id)){
			return OK();
		}
		int id = Integer.valueOf(payment_id);
		String s = alipaybatch.apply(id);
		return ['code' : 1 , 'data' : s];
	}
}




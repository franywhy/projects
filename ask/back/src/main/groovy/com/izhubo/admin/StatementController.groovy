package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.TwoTableCommit
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

import java.util.Comparator


@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class StatementController extends BaseController{
	
	@Resource
	KGS answerTypeKGS;

	DBCollection statement(){mainMongo.getCollection('statement')}
	
	DBCollection pay_order(){mainMongo.getCollection('pay_order')}

	DBCollection course(){mainMongo.getCollection('course')}
	
	DBCollection rooms(){mainMongo.getCollection('rooms')}
																 
	DBCollection company_settle_ratio(){mainMongo.getCollection('company_settle_ratio')}
	
	DBCollection registration(){mainMongo.getCollection('employment_registration')}
														 
	DBCollection company(){mainMongo.getCollection('company')};
	
	//供应商结算单
	def list(HttpServletRequest req){	
		def query = QueryBuilder.start();
		def querymonth = req.getParameter("etime");	
		def company_name =  req.getParameter("company_name");
		if(StringUtils.isNotBlank(company_name)){
			Pattern pattern = Pattern.compile("^.*" + company_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("detail.company_name").regex(pattern)
		}
		if(StringUtils.isNotBlank(querymonth)){
			query.and("year_month").is(querymonth);
		}
		Crud.list(req,statement(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	

	//
	def selectYM(HttpServletRequest req){
	  def type = req.getParameter("type");
	  if("0".equals(type)){
		  countMonth(req);
	  }else if("1".equals(type)){
	  	  countYear(req);
	  }
	}
	
	//年结算
	def countYear(HttpServletRequest req){
		def cy = req.getParameter("conutYear");
		def company_id = req.getParameter("company_id");
		def query = QueryBuilder.start();
		if(StringUtils.isNotBlank(cy)){
			Pattern pattern = Pattern.compile("^.*" + cy + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("year_month").regex(pattern)
		}	
		if(StringUtils.isNotBlank(company_id)){
			query.and("company_id").is(company_id);
		}
		query.and("detail.year_bill_code").is(null);
			
	     List<DBObject> lsts = statement().find(query.get()).toArray();   //公司的结算明细
		 if(lsts){
			 Double sumMoney = 0;
			 for(DBObject lst:lsts){					//根据公司计算年度订单总额
				DBObject detail = (DBObject)lst.get("detail");
			    def order_money = detail.get("order_money") as Double;
				  sumMoney = sumMoney + order_money;
		     }
		 	 //获得当前公司所有年提成比率
			 List<DBObject> year_ratio = (List<DBObject>)company_settle_ratio().findOne(new BasicDBObject("company_id",company_id),new BasicDBObject("year_ratio",1))?.get("year_ratio");  //找年结算比率
			 //按销售金额从小到大排序年提成比率
			 yrSort(year_ratio); 
			 //找出匹配的金额得出年提成比率
			 DBObject select_yr = null;  
			 for(DBObject yr : year_ratio){
				 def sale_money =  yr.get("sale_money") as Double;				 
				 if(sumMoney>=sale_money){					 
					  select_yr = yr;				
				 }
			 }
			 //没有找到匹配的金额，默认是最低档年度提成比率
			 if(select_yr==null){
				select_yr = year_ratio.get(0);
			 }
			 //年度结算比率=年度提成比率-月度提成比率
			for(DBObject lst:lsts){					
				DBObject detail =  (DBObject)lst.get("detail");
				def month_ratio = detail.get("month_ratio")as Double;
				
				def ratio = select_yr.get("ratio") as Double;
				Double y_ratio = ratio-month_ratio; //
				Double msm = detail.get("month_settlement_money") as Double;//订单月结算金额
				Double year_settlement_money = msm*y_ratio;             //订单结算金额*(年度结算比率-月度结算比率)
				
				detail.put("year_bill_code", answerTypeKGS.nextId())
				detail.put("year_ratio", (Double)Math.round(y_ratio*100)/100.0)
				detail.put("year_settlement_money", (Double)Math.round(year_settlement_money*100)/100.0)
				lst.put("detail",detail);
				//更新
				statement().update(new BasicDBObject("_id":lst.get("_id")),new BasicDBObject('$set':lst));
		     }
			return [code : 1 , data :["flg":4]];
		 }else{
		 	return [code : 1 , data :["flg":1]];
		 }
	}
	
	//月结算
	def countMonth(HttpServletRequest req){
		//学员登记表
		def  ct =  req.getParameter("conutMonth");
		Long lon=0;
		if(StringUtils.isNotBlank(ct)){
			String fillDate = ct+"-30 23:59:59";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lon = sdf.parse(fillDate).getTime();					
		}
		def query = QueryBuilder.start();
			//单据月份
			query.and("bill_date");
			query.lessThanEquals(lon);
			//单据状态
			query.and("statement_state").is(false);   
		 List<DBObject> xydjs = registration().find(query.get()).toArray();
		
		 if(xydjs){
			 for(DBObject xydj:xydjs){
				def orderNO = xydj.get("order_no");//订单号
				def userID = xydj.get("user_id")
			    DBObject order = pay_order().findOne(new BasicDBObject("order_no",orderNO));//找到订单
								def item_name = order.get("item_name"); 	          //商品名称
								Double money = order.get("money") as Double;          //订单金额
								def item_type = order.get("item_type") as Integer;	  //商品类别 int
								def create_time = order.get("create_time");           //订单单创建时间
								def user_id = order.get("user_id") as Integer;		  //学员id int
								def item_num = order.get("item_num") as Integer;	  //商品数量 int
								def order_id = order.get("_id");			          //商品主键 uuid	
								Map m = query_ratio(order);    						  //提成比率
				Map hm = new HashMap();
				if(m.get("mr")){
					Double d3 = computeMoney(m.get("mr") as Double,money);					
					hm.put("item_name",item_name);
					hm.put("item_num",item_num);
					hm.put("item_type",item_type);
					hm.put("month_bill_code",answerTypeKGS.nextId());
					hm.put("month_ratio",m.get("mr"));    
					hm.put("month_settlement_money",d3);   
					hm.put("order_date",create_time);
					hm.put("order_money",money);
					hm.put("order_no", orderNO);
					hm.put("company_name", m.get("company_name"));
					hm.put("month_bill_code",answerTypeKGS.nextId());//月度结算编号
					hm.put("user_id",userID);   					   //学员ID
					hm.put("user_name",users().findOne($$("_id":userID),$$("nick_name":1))?.get("nick_name"));
					hm.put("year_bill_code",null);
					hm.put("year_ratio", null);
					hm.put("year_settlement_money",null);
				}else{
					return [code : 1 , data :["flg":3]];
				}
				def newstatement = $$("_id",UUID.randomUUID().toString());
				newstatement.append("bill_code",answerTypeKGS.nextId());   		      //单据编号
				newstatement.append("year_month",ct);  					              //结算年月
				newstatement.append("settlement_date",new Date().toLocaleString());   //结算时间
				newstatement.append("company_id", m.get("company_id"));				  //所属公司	
				newstatement.append("detail",hm);   								  //详细
				newstatement.append("manage_info",add_manage_info(req));   			  //数据元素
			//两步提交
				Crud.doTwoTableCommit(newstatement,
					[main:{registration()},//A表更新
					 logColl:{statement()},	//B表保存
					 queryWithId:{new BasicDBObject("_id":xydj.get("_id"))},	
					 update:{new BasicDBObject('$set',['statement_state': true]);},
					 successCallBack:{ 
						true == registration().findOne(new BasicDBObject("_id":xydj.get("_id")),new BasicDBObject("statement_state",1))?.get("statement_state")
					},								
					 rollBack:{
						 registration().update(new BasicDBObject("_id":xydj.get("_id")),new BasicDBObject('$set':['statement_state': false]));
					 }
				] as TwoTableCommit);
				
				/*//保存
				statement().save(newstatement);
				//更新
				BasicDBObject updateStatement =new BasicDBObject();
				updateStatement.append("statement_state", true);
				registration().update(new BasicDBObject("_id":xydj.get("_id")),new BasicDBObject('$set':updateStatement));*/
			 }
			 return [code : 1 , data :["flg":4]];
		 }else{
		 	return [code : 1 , data :["flg":2]];
		 }
	}
		
	//根据类别查找商品公司提成比率
	def query_ratio(DBObject order){
		if(order){
			def itemTYPE = order.get("item_type"); //商品id
			def mr=0;
			Map m = new HashMap();
			//课程
			if(itemTYPE == 1){														
			  def company_id = course().findOne($$("_id": order.get("item_id")),$$("company_id":1))?.get("company_id"); //根据商品公司得到所有公司制定的加盟表格
			  def company_name = company().findOne(new BasicDBObject("_id",company_id),new BasicDBObject("company_name",1))?.get("company_name");
			  List<DBObject> csrs = company_settle_ratio().find(new BasicDBObject("company_id",company_id)).toArray();  //找结算比率
			  Long now = System.currentTimeMillis(); 		//根据当前时间找到最近的比率开始时间						 
			  for(DBObject csr:csrs){
				   Long begin =  strbylon(csr.get("begin_date") as String);
				   Long end = strbylon(csr.get("end_date") as String) ;
				 if(now>=begin&&now<=end){
					mr = csr.get("month_ratio");          //根据时间段公司有效的提成比率
					break;
				 }
			  }
			  m.put("mr", mr as Double);
			  m.put("company_name", company_name);
			  m.put("company_id", company_id);
			}
			//直播间
			if(itemTYPE == 3){
				Map map = new HashMap();
				def  company_id = rooms().findOne($$("_id": order.get("item_id")),$$("company_id":1))?.get("company_id"); //根据商品公司得到
				def company_name = company().findOne(new BasicDBObject("_id",company_id),new BasicDBObject("company_name",1))?.get("company_name");
				List<DBObject> csrs = company_settle_ratio().find(new BasicDBObject("company_id",company_id)).toArray();
				Long now = System.currentTimeMillis(); 		//根据当前时间找到最近的比率开始时间
				for(DBObject csr:csrs){
				   Long begin =  strbylon(csr.get("begin_date") as String);
				   Long end = strbylon(csr.get("end_date") as String) ;
				    if(now>=begin&&now<=end){
					  mr = csr.get("month_ratio");          //根据时间段公司有效的提成比率
					  break;
				   }
				}
				m.put("mr", mr as Double);
				m.put("company_name", company_name);
				m.put("company_id", company_id);
			  }
			return m;
	   }else{
	   		return;
	   }
	}
	
	def computeMoney(Double d1,Double d2){
		Double d3  = d1*d2;
		return (Double)Math.round(d3*100)/100.0; //计算
	}
	
	//按年度提成比率的金额
	def yrSort(List<DBObject> list){
		for(int i=0;i<list.size()-1;i++){
			for(int j=1;j<list.size()-i;j++){
			 Double o1 = ((DBObject)list.get(j-1)).get("sale_money") as Double;
			 Double o2 = ((DBObject)list.get(j)).get("sale_money") as Double;
			 if(o1>o2){
				DBObject jj = list.get(j-1);//o1
				list.set(j-1,list.get(j));//o2
				list.set(j,jj);
			 }
			}
		}
		System.out.println(list);
		return list;
	}

	def add_manage_info(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");		
		Map manage_info = new HashMap();
		Long now = System.currentTimeMillis();
		manage_info.put("audit_flag" , false);
		manage_info.put("audit_user_id","NO");		
		manage_info.put("create_user_id",user.get("_id") as Integer);
		manage_info.put("timestamp",now);
		manage_info.put("update_user_id",user.get("_id") as Integer);
		manage_info.put("update_date",now);
		manage_info.put("upload_flag" , false);
		manage_info.put("upload_user_id" , "NO");	
		return 	manage_info;
	}
	
	//为结算选择公司服务
	def query_company(){
		List<DBObject> list = company().find().toArray();
		return ["code":1,"data":list];
	}
	
	def property = "statement"; //定义表名
	
	def submit(HttpServletRequest request){
		submit1(request);
	}
	
	def rollbackSubmit(HttpServletRequest request){
		rollbackSubmit1(request)
	}
	
	def audit(HttpServletRequest request){
		audit1(request);
	}
	
	def rollbackAudit(HttpServletRequest request){
		rollbackAudit1(request);
	}
}

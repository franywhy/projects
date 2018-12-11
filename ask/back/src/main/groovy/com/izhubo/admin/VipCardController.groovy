package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import java.util.regex.Pattern
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.izhubo.model.ApplyType
import com.izhubo.model.FamilyType
import com.izhubo.model.MsgType
import com.izhubo.model.OpType

/**
 *  vip 卡片档案
 * date: 15-04-09 下午2:31
 * @author:hzj
 */
@RestWithSession
class VipCardController extends BaseController {
 
	
    DBCollection vipcard() {
		 mainMongo.getCollection('vipcard');
	}
	
    // 
    def list(HttpServletRequest req) {
		QueryBuilder query = QueryBuilder.start();
		def memo = req.getParameter("memo");
		if(StringUtils.isNotBlank(memo)){
			Pattern pattern = Pattern.compile("^.*" + memo + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("memo").regex(pattern);
		}
        Crud.list(req, vipcard(), query.get(), ALL_FIELD, SJ_DESC)
    }
	
	def add(HttpServletRequest req){
		def main_type_id = req.getParameter("main_type_id");
		def card_url = req.getParameter("card_url");
		
		def price = req.getParameter("price");
		if(StringUtils.isNotBlank(price)){
			price = price as Double;
		}
		
		def month_num = req.getParameter("month_num");
		if(StringUtils.isNotBlank(month_num)){
			month_num = month_num as Integer;
		}
		def begin_date = req.getParameter("begin_date");
		def end_date = req.getParameter("end_date");
		def memo = req.getParameter("memo");
		DBObject addM = new BasicDBObject();
		
			addM.put("_id", UUID.randomUUID().toString());
				addM.put("memo", memo);
				addM.put("main_type_id", main_type_id);
				addM.put("card_url", card_url);
				addM.put("month_num", month_num);
				addM.put("price", price);
				addM.put("begin_date", begin_date);
				addM.put("end_date", end_date);
				
				addM.put("manage_info",getCurrentUserInfo(req));
		vipcard().save(addM);
		return OK();
	}

	def edit(HttpServletRequest req){
		def _id = req.getParameter("_id");
		def main_type_id = req.getParameter("main_type_id");
		def card_url = req.getParameter("card_url");
			def price = req.getParameter("price");
		if(StringUtils.isNotBlank(price)){
			price = price as Double;
		}
		
		def month_num = req.getParameter("month_num");
		if(StringUtils.isNotBlank(month_num)){
			month_num = month_num as Integer;
		}
		def begin_date = req.getParameter("begin_date");
		def end_date = req.getParameter("end_date");
		def memo = req.getParameter("memo");
	
			DBObject editM = new BasicDBObject();
				editM.put("memo", memo);
				editM.put("main_type_id", main_type_id);
				editM.put("card_url", card_url);
				editM.put("month_num", month_num);
				editM.put("price", price);
				editM.put("begin_date", begin_date);
				editM.put("end_date", end_date);
				editM.put("manage_info",getCurrentUserInfo(req));
		vipcard().update(new BasicDBObject("_id":_id),new BasicDBObject('$set',editM));			
		return OK();
	}
	
	def del(HttpServletRequest req){
		def _id = req.getParameter("_id");
		vipcard().remove(new BasicDBObject("_id",_id));
		return OK();
	}
	
	def getCurrentUserInfo(HttpServletRequest req){
			Map user = (Map) req.getSession().getAttribute("user");
			Map manage_info = new HashMap();
			Long now = System.currentTimeMillis();
			//创建人id
			manage_info.put("create_user_id",user.get("_id") as Integer);
			//创建日期
			manage_info.put("timestamp",now);
			//修改人Id
			manage_info.put("update_user_id",user.get("_id") as Integer);
			//修改日期
			manage_info.put("update_date",now);
			return manage_info;
	}
	
	def property = "vipcard"; //定义表名
		
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

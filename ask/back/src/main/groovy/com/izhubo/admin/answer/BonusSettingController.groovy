package com.izhubo.admin.answer

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONArray
import net.sf.json.JSONObject
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.izhubo.admin.BaseController
import com.izhubo.admin.Web;
import com.izhubo.common.util.JSONHelper
import com.izhubo.model.ApplyType
import com.izhubo.model.FamilyType
import com.izhubo.model.MsgType
import com.izhubo.model.OpType

import org.springframework.web.bind.ServletRequestUtils
import org.hibernate.Criteria;
import org.hibernate.Session
import org.hibernate.SessionFactory;
import org.hibernate.Transaction
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mysqldb.model.BonusPools
import com.mysqldb.model.BonusType
import com.mysqldb.model.BonusTypeMain
import com.mysqldb.model.Bunus

/**
 *  vip 卡片档案
 * date: 15-04-09 下午2:31
 * @author:hzj
 */
@RestWithSession
class BonusSettingController extends BaseController {


	@Resource
	private SessionFactory sessionFactory;

	DBCollection vipcard() {
		mainMongo.getCollection('vipcard');
	}

	//
	def list(HttpServletRequest req) {

		Map<String, Object> map = new HashMap<String, Object>();
		int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(req , "page" , 1);

		List incomelist = sessionFactory
				.getCurrentSession().createCriteria(BonusTypeMain.class)
				.setFirstResult((page-1)*size)
				.setMaxResults(size)
				.list();


		return getResultOKS(incomelist);

	}
	def auditlist(HttpServletRequest req) {

		Map<String, Object> map = new HashMap<String, Object>();
		int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(req , "page" , 1);

		List incomelist = sessionFactory
				.getCurrentSession().createCriteria(BonusTypeMain.class)
				.add(Restrictions.eq(BonusTypeMain.PROP_AUDITFLAG, 1))
				.setFirstResult((page-1)*size)
				.setMaxResults(size)
				.list();


		return getResultOKS(incomelist);

	}
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest req){


		int userid = Web.currentUserId;
		def main_template_name = req.getParameter("mainTemplateName");
		def all_money = req.getParameter("allMoney");
		if(StringUtils.isNotBlank(all_money)){
			all_money = all_money as Double;
		}
		def year_mon = req.getParameter("yearMonth");
		def remark = req.getParameter("remark");
		def detailjson =  req.getParameter("detailjson");
		






		BonusTypeMain bt = new BonusTypeMain();
		bt.setMainTemplateName(main_template_name.toString());
		bt.setAllMoney(java.math.BigDecimal.valueOf(all_money));
		bt.setYearMonth(year_mon.toString());
		bt.setRemark(remark.toString());

		bt.BonusTypeList = new ArrayList();
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);

		double subSumMoney = 0;
		for(int i=0;i<ja.size();i++)
		{

			JSONObject json = ja.get(i);
			double mmoney =json.get("mmoney") as double;
			Integer mtemplate =json.get("mtemplate") as Integer;
			Integer mweight =json.get("mweight") as Integer;
			//add by shihongjie 2015-12-16 vip权重
			Integer vweight =json.get("vweight") as Integer;
			Integer quantity =json.get("quantity") as Integer;
			subSumMoney +=mmoney*quantity;

			BonusType btype = new BonusType();
			btype.setMlevel(json.get("mlevel").toString());
			btype.setMmoney(java.math.BigDecimal.valueOf(mmoney));
			btype.setMtemplate(mtemplate);
			btype.setMweight(mweight);
			//add by shihongjie 2015-12-16 vip权重
			btype.setVweight(vweight);
			btype.setQuantity(quantity);
			btype.bonusTypeMain = bt;

			bt.getBonusTypeList().add(btype);
		}

		if(subSumMoney>all_money)
		{

			return ["code" : 99, "msg" : "明细金额已经超出总金额，无法添加"];
		}
		else
		{

			bt.setTimestamp(new Timestamp(System.currentTimeMillis()));
			bt.setCreateUserId(userid);

			sessionFactory.getCurrentSession().save(bt);
			return OK();
		}
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest req){
		int id =Integer.valueOf( req.getParameter("_id").toString());
		int userid = Web.currentUserId;
		def main_template_name = req.getParameter("mainTemplateName");
		def all_money = req.getParameter("allMoney");
		if(StringUtils.isNotBlank(all_money)){
			all_money = all_money as Double;
		}
		def year_mon = req.getParameter("yearMonth");
		def remark = req.getParameter("remark");
		def detailjson =  req.getParameter("detailjson");


		BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);

		bt.setMainTemplateName(main_template_name.toString());
		bt.setAllMoney(java.math.BigDecimal.valueOf(all_money));
		bt.setYearMonth(year_mon.toString());
		bt.setRemark(remark.toString());


		for(int j=0;j<bt.BonusTypeList.size();j++)
		{
			BonusType btsub = bt.BonusTypeList.getAt(j);
			sessionFactory.getCurrentSession().delete(btsub);

		}
		bt.BonusTypeList = new ArrayList();
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);

		for(int i=0;i<ja.size();i++)
		{

			JSONObject json = ja.get(i);
			double mmoney =json.get("mmoney") as double;
			Integer mtemplate =json.get("mtemplate") as Integer;
			Integer mweight =json.get("mweight") as Integer;
			Integer quantity =json.get("quantity") as Integer;

			BonusType btype = new BonusType();
			btype.setMlevel(json.get("mlevel").toString());
			btype.setMmoney(java.math.BigDecimal.valueOf(mmoney));
			btype.setMtemplate(mtemplate);
			btype.setMweight(mweight);
			btype.setQuantity(quantity);
			btype.bonusTypeMain = bt;

			bt.getBonusTypeList().add(btype);
		}



		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);

		sessionFactory.getCurrentSession().update(bt);

		sessionFactory.getCurrentSession().flush();


		return OK();
	}

	public static String dateToString(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyyMM");
		String ctime = formatter.format(time);

		return ctime;
	}

	private void Audit(int id)
	{
		int userid = Web.currentUserId;

		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();

		BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);
		//		//批量的保存奖金池数据
		//		for(int i=0;i<bt.quantity;i++)
		//		{
		//			Bunus bonus = new Bunus();
		//			bonus.setUniqueMark( UUID.randomUUID().toString());
		//			bonus.setMlevel(bt.getMlevel());
		//			bonus.setMmoney(bt.getMmoney())
		//			bonus.setMtemplate(bt.getMtemplate());
		//			bonus.setIsOpen(0);
		//			bonus.setMweight(bt.getMweight());
		//			bonus.setMtimestamp(new Timestamp(System.currentTimeMillis()) );
		//			bonus.setYearMon(dateToString(new Date()));
		//			sessionFactory.getCurrentSession().save(bonus);
		//
		//		}


		//修改状态
		bt.setAuditFlag(1);
		bt.setAuditUserId(userid);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);


		//在奖金池总额上增加

		sessionFactory.getCurrentSession().update(bt);


		transaction.commit();
		sessionFactory.getCurrentSession().flush();
	}

	def del(HttpServletRequest req){
		Integer id = req.getParameter("_id") as Integer;

		BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);

		sessionFactory.getCurrentSession().delete(bt);

		sessionFactory.getCurrentSession().flush();
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

		int userid = Web.currentUserId;
		int id =Integer.valueOf( request.getParameter("_id").toString());
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);
		bt.setUploadFlag(1);
		bt.setUploadUserId(userid);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);
		sessionFactory.getCurrentSession().update(bt);
		transaction.commit();
		sessionFactory.getCurrentSession().flush();

		return OK();
	}

	def rollbackSubmit(HttpServletRequest request){

		int userid = Web.currentUserId;
		int id =Integer.valueOf( request.getParameter("_id").toString());
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);
		bt.setUploadFlag(0);
		bt.setUploadUserId(0);
		bt.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		bt.setUpdateUserId(userid);
		sessionFactory.getCurrentSession().update(bt);
		transaction.commit();
		sessionFactory.getCurrentSession().flush();
		return OK();

	}

	def audit(HttpServletRequest request){
		int id =Integer.valueOf( request.getParameter("_id").toString());
		Audit(id);
		return OK();
	}

	def rollbackAudit(HttpServletRequest request){
		//rollbackAudit1(request);
	}
}


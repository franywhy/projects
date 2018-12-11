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
import com.mysqldb.model.BonusPoolsSample

@RestWithSession
class BonuspoolsController extends BaseController {
	
	
		@Resource
		private SessionFactory sessionFactory;
	
	
		//
		def list(HttpServletRequest req) {
	
			Map<String, Object> map = new HashMap<String, Object>();
			int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
			int page = ServletRequestUtils.getIntParameter(req , "page" , 1);
	
			List incomelist = sessionFactory
					.getCurrentSession().createCriteria(BonusPoolsSample.class)
					.setFirstResult((page-1)*size)
					.setMaxResults(size)
					.list();
	
	
			return getResultOKS(incomelist);
	
		}
		
		def getdetaillist(HttpServletRequest req) {
	
			Map<String, Object> map = new HashMap<String, Object>();
			int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
			int page = ServletRequestUtils.getIntParameter(req , "page" , 1);
	
			int id =Integer.valueOf( req.getParameter("main_id").toString());
			
			int count = (int)sessionFactory
					.getCurrentSession().createCriteria(Bunus.class)
					.setProjection(Projections.count(Bunus.PROP_ID))
					.add(Restrictions.eq(Bunus.PROP_MAINID, id))
					.uniqueResult();
					
					
					
					//				float sum = (Long) sessionFactory.getCurrentSession()
					//						.createCriteria(Apply.class)
					//						.setProjection(Projections.sum(Apply.PROP_APPLYMONEY))
					//						.add(Restrictions.eq(Apply.PROP_USERID, user_id))
					//						.uniqueResult();
			List incomelist = sessionFactory
					.getCurrentSession().createCriteria(Bunus.class)
					.add(Restrictions.eq(Bunus.PROP_MAINID, id))
					.setFirstResult((page-1)*size)
					.setMaxResults(size)
					.list();
	
					return GetResultOKForList(incomelist,count);
			
	
		}
		
		
		@TypeChecked(TypeCheckingMode.SKIP)
		def add(HttpServletRequest req){
	
	
			int userid = Web.currentUserId;
			int id =Integer.valueOf( req.getParameter("referid").toString());
			String yearmon  = req.getParameter("select_months").toString();
			
			BonusTypeMain bt = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, id);
			BonusPools bp = new BonusPools();
			bp.setTemplateId(id);
			bp.setYearMon(yearmon);
			
			bp.setTimestamp(new Timestamp(System.currentTimeMillis()));
			bp.setCreateUserId(userid);
			
			sessionFactory.getCurrentSession().save(bp);
			return OK();
		}
	
		@TypeChecked(TypeCheckingMode.SKIP)
		def edit(HttpServletRequest req){
	
	
	
			return OK();
		}
	
		public static String dateToString(Date time){
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat ("yyyyMM");
			String ctime = formatter.format(time);
	
			return ctime;
		}
	
		@TypeChecked(TypeCheckingMode.SKIP)
		private void Audit(int id)
		{
			int userid = Web.currentUserId;
	
			Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
	
			BonusPools bt = (BonusPools) sessionFactory.getCurrentSession().get(BonusPools.class, id);
			BonusTypeMain bonusType = (BonusTypeMain) sessionFactory.getCurrentSession().get(BonusTypeMain.class, bt.getTemplateId());
		

			bt.BonusList = new ArrayList();
			
			double summoney = 0;
			if(bonusType.getBonusTypeList().size()>0)
			{
				
				for(int i=0;i<bonusType.getBonusTypeList().size();i++)
				{
					BonusType btype = bonusType.getBonusTypeList().getAt(i);
					
					for(int j=0;j<btype.getQuantity();j++)
					{
						summoney = summoney +btype.getMmoney();
						Bunus bonus = new Bunus();
						bonus.setUniqueMark( UUID.randomUUID().toString());
						bonus.setMlevel(btype.getMlevel());
						bonus.setMmoney(btype.getMmoney())
						bonus.setMtemplate(btype.getMtemplate());
						bonus.setIsOpen(0);
						bonus.setMweight(btype.getMweight());
						bonus.setMtimestamp(new Timestamp(System.currentTimeMillis()) );
						bonus.setYearMon(bt.getYearMon());
						bonus.setBonusPools(bt);
						bt.BonusList.add(bonus);
						
					}
					
				}
				
			}
			bt.setBonus(BigDecimal.valueOf(summoney))
			
			
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
	
			BonusPools bt = (BonusPools) sessionFactory.getCurrentSession().get(BonusPools.class, id);
	
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
			BonusPools bt = (BonusPools) sessionFactory.getCurrentSession().get(BonusPools.class, id);
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
			BonusPools bt = (BonusPools) sessionFactory.getCurrentSession().get(BonusPools.class, id);
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

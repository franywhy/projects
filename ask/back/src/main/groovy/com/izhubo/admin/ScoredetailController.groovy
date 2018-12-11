package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils
import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.SessionFactory;

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.ScoreDetail

/**
 * 积分明细
 * @author zhengxin
 * 2016-05-13
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ScoredetailController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	private static final String TAG = "score_detail";
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		
		String user_id = req["user_id"];
		String user_nickname = req["user_nickname"];
		String score_gain_type = req["score_gain_type"];
		String score_type = req["score_type"];
		Date screate_time = Web.getTime(req, "screate_time");
		Date ecreate_time = Web.getTime(req, "ecreate_time");
		
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(ScoreDetail.class)
		if (StringUtils.isNotBlank(user_id)) {
			criterion.add(Restrictions.eq(ScoreDetail.PROP_USERID, Integer.valueOf(user_id)));
		}
		if (StringUtils.isNotBlank(user_nickname)) {
			criterion.add(Restrictions.ilike(ScoreDetail.PROP_USERNICKNAME, "%"+user_nickname+"%"));
		}
		if (StringUtils.isNotBlank(score_gain_type)) {
			criterion.add(Restrictions.eq(ScoreDetail.PROP_SCOREGAINTYPE, Integer.valueOf(score_gain_type)));
		}
		if (StringUtils.isNotBlank(score_type)) {
			criterion.add(Restrictions.eq(ScoreDetail.PROP_SCORETYPE, Integer.valueOf(score_type)));
		}
		if(screate_time){
			criterion.add(Restrictions.ge(ScoreDetail.PROP_CREATETIME,new Timestamp(screate_time.getTime())));
		}
		if(ecreate_time){
			criterion.add(Restrictions.le(ScoreDetail.PROP_CREATETIME,new Timestamp(ecreate_time.getTime())));
		}
		
		
		def ScoreDetailList = criterion.addOrder(Order.desc(ScoreDetail.PROP_CREATETIME)).addOrder(Order.desc(ScoreDetail.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();
		
		return getResultOKS(ScoreDetailList);
	}
	
}

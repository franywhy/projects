package com.izhubo.admin.answer



import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.Map;
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value

import com.izhubo.admin.BaseController
import com.izhubo.admin.Web
import com.izhubo.model.TopicBunsType
import com.izhubo.model.TopicChannelType
import com.izhubo.model.TopicEndType
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicReplyShowType
import com.izhubo.model.TopicsReplyType
import com.izhubo.model.TopicsType
import com.izhubo.model.NCUserState
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder


/**
 * 校区报表
 * @author
 *
 */
@RestWithSession
class ProvinceUserReportController extends BaseController {

	public DBCollection users(){
		return mainMongo.getCollection("users");
	}

	public DBCollection logs(){
		return logMongo.getCollection("day_login");
	}
	
	public DBCollection provreportlogs(){
		return logMongo.getCollection("reportdata_user_reg_groupbyprov");
	}
	/**
	 * 问题列表
	 * @param req
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){



		List maxtime =provreportlogs().find().sort(new BasicDBObject("update_time" , -1)).limit(1).toArray();

		BasicDBObject maxobj = maxtime.get(0);

		String updatetime = maxobj.get("update_time");

		def result = provreportlogs().find(new BasicDBObject("update_time" ,updatetime)).toArray();

		return getResultOKS(result);
	}



	Integer Intersection(List<com.mongodb.DBObject> ncuserlist,List<com.mongodb.DBObject> activelist){

		def usercount = 0;


		int pointerA = 0;
		int pointerB = 0;

		for(int i=0;i<ncuserlist.size();i++) {
			DBObject ncuser = new BasicDBObject();

			ncuser.put("user_id", ncuserlist.get(i).get("_id"));

			if(activelist.contains(ncuser)) {
				usercount++;
			}
		}

		return usercount;
	}
}


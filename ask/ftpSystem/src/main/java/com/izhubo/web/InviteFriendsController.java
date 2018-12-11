package com.izhubo.web;
import static com.izhubo.rest.common.doc.MongoKey.$inc;
import static com.izhubo.rest.common.doc.MongoKey._id;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.web.spring.Interceptors;
import com.izhubo.web.BaseController;
import com.izhubo.web.api.Web;


@Controller
@RestWithSession
@Interceptors
public class InviteFriendsController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(InviteFriendsController.class);
	
	
	public Object inviteFriends(HttpServletRequest req,
			HttpServletResponse res){
		logger.info("into inviteFriends()");
		Map<String, Object> result = new HashMap<>();
		try {
			long userid = Long.parseLong(Web.getCurrentUserId().toString());
			logger.info("userid:{}",userid);
			//检查用户是否通过任务集市进入APP
			DBObject temp = new BasicDBObject("_id",userid);
			DBObject obj = adminMongo.getCollection("taskmarket").findOne(temp);
			if(obj != null ){
				if(adminMongo.getCollection("taskmarket").update(
						new BasicDBObject(_id, userid),
						new BasicDBObject($inc, new BasicDBObject("invitefriends_num",
								1))).getN() == 1);{
									result.put("ret", 1);
								}
			}else {
				result.put("ret", 2);
			}	
		} catch (Exception e) {
			logger.error("e",e);
		}		
		return result;		
	}

}

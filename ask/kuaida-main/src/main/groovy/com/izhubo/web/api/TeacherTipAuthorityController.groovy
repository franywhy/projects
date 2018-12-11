package com.izhubo.web.api

import com.alibaba.fastjson.JSONObject
import com.izhubo.web.BaseController
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$

@Controller
@RequestMapping("api/teacherTipAuthority")
class TeacherTipAuthorityController extends BaseController {

	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content")
	}

	@ResponseBody
	@RequestMapping(value = "getTeacherList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def getTeacherList(HttpServletRequest request){
		String mobile = ServletRequestUtils.getStringParameter(request,"mobile","")
		int answerPermission = ServletRequestUtils.getIntParameter(request,"answerPermission",-1)
		int currPage = ServletRequestUtils.getIntParameter(request,"currPage",1)
		int pageSize = ServletRequestUtils.getIntParameter(request,"pageSize",10)

		boolean hasFilter = false
		QueryBuilder filterQuery = QueryBuilder.start()
		if(StringUtils.isNotBlank(mobile)) {
			hasFilter = true
			filterQuery.and("username").regex(getLikeStrPattern(mobile))
		}
		def tuidList = null
		if(hasFilter) {
			tuidList = new ArrayList<String>()
			qQUser().find(filterQuery.get(),$$("tuid":1)).each { item ->
				tuidList.add(item.get("tuid"))
			}
		}

		QueryBuilder teacherQuery = QueryBuilder.start()
		if(null != tuidList) {
			teacherQuery.and("tuid").in(tuidList)
		} else {
			teacherQuery.and("priv").is(2)
		}
		if(-1 != answerPermission) {
			teacherQuery.and("priv1").is(answerPermission)
		}

		List<Map<String,Object>> result = new ArrayList<>()
		long total = users().count(teacherQuery.get())
		users().find(teacherQuery.get()).skip((currPage - 1) * pageSize).limit(pageSize).each { item ->
			def user_industry = null
			def business_id = item.get("business_id") == null ? null : item.get("business_id")[0]
			if (business_id != null && "zikao".equals(business_id)){
				user_industry = item.get("user_industry_zikao")
			} else if(business_id != null && "kuaiji".equals(business_id)) {
				user_industry = item.get("user_industry")
			}
			String parentTipName = ""
			def parentTipIdSet = new HashSet<>()
			def tipIdList = new ArrayList<>()
			if (null != user_industry) {
				user_industry.each { def tdbo ->
					def users_industry_tip = tdbo["users_industry_tip"]
					if(users_industry_tip){
						for(tdbosub in users_industry_tip) {
							tipIdList.add(tdbosub["industry_tip_id"])
						}
					}
				}
			}
			tip_content().find($$("_id":$$('$in':tipIdList)),$$("parent_tip_id":1)).each { tip_item ->
				parentTipIdSet.add(tip_item.get("parent_tip_id"))
			}
			tip_content().find($$("_id":$$('$in':parentTipIdSet)),$$("tip_name":1)).each { tip_item ->
				parentTipName += tip_item.get("tip_name") + "\n"
			}
			String username = qQUser().findOne($$("tuid":item.get("tuid"))).get("username")
			String schoolName = ""
			if(item.get("school_code")) {
				DBObject area = area().findOne($$("code":item.get("school_code")),$$("name":1))
				if(null != area) {
					schoolName = area.get("name")
				}
			}
			Map<String,Object> map = new HashMap<>()
			map.put("tuid",item.get("tuid"))
			map.put("realName",item.get("real_name_from_nc"))
			map.put("mobile",username)
			map.put("schoolName",schoolName)
			map.put("parentTipName",parentTipName)
			map.put("isHq",item.get("is_hq"))
			map.put("priv",item.get("priv"))
			map.put("answerPermission",item.get("priv1"))
			map.put("parentTipIdList",parentTipIdSet)
			map.put("tipIdList",tipIdList)
			map.put("businessId",business_id)
			result.add(map)
		}
		Map<String,Object> resultMap = new HashMap<>()
		resultMap.put("total",total)
		resultMap.put("list",result)
		return getResultOK(resultMap)
	}

	@ResponseBody
	@RequestMapping(value = "updateUserTip", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def updateUserTip(@RequestBody String body, long userId) {
		JSONObject jsonObject = JSONObject.parseObject(body)
		String tuid = jsonObject.getString("tuid")
		int isHq = jsonObject.getIntValue("isHq")
		int priv = jsonObject.getIntValue("priv")
		int answerPermission = jsonObject.getIntValue("answerPermission")
		String businessId = jsonObject.getString("businessId")
		List<Long> tipIdList = jsonObject.getJSONArray("tipIdList").toList()

		List<DBObject> user_industry = new ArrayList<>()
		DBObject mapTip = new BasicDBObject()
		List<DBObject> listTip = new ArrayList<>()
		if(null != tipIdList) {
			for(int i=0; i<tipIdList.size(); i++) {
				DBObject idMap = new BasicDBObject()
				idMap.put("industry_tip_id",tipIdList.get(i))
				listTip.add(idMap)
			}
		}
		mapTip.put("industry_id",1111)
		mapTip.put("users_industry_tip",listTip)
		user_industry.add(mapTip)

		String user_industry_text = "user_industry"
		if ("zikao".equals(businessId)) {
			user_industry_text = "user_industry_zikao"
		}
		users().update(new BasicDBObject("tuid", tuid),
				new BasicDBObject('$set',new BasicDBObject(user_industry_text,user_industry).append("business_id", Arrays.asList(businessId))
						.append("is_hq",isHq)
						.append("priv1",answerPermission).append("priv",priv).append("modify_person_id",userId)))

		return getResultOK()
	}
}

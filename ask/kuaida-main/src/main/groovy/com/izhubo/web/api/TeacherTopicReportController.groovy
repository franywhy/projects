package com.izhubo.web.api

import com.izhubo.model.TopicsType
import com.izhubo.web.BaseController
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$

@Controller
@RequestMapping("api/teacherTopicReport")
class TeacherTopicReportController extends BaseController {
	
	private DBCollection topics(){
		return mainMongo.getCollection("topics")
	}

	@ResponseBody
	@RequestMapping(value = "teacherTopic", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def teacherTopic(HttpServletRequest request){
		Long startTime = ServletRequestUtils.getLongParameter(request,"startTime",0)
		Long endTime = ServletRequestUtils.getLongParameter(request,"endTime",0)
		int product = ServletRequestUtils.getIntParameter(request,"product",-1)
		String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","")
		String teacherName = ServletRequestUtils.getStringParameter(request,"teacherName","")
		String mobile = ServletRequestUtils.getStringParameter(request,"mobile","")
		int currPage = ServletRequestUtils.getIntParameter(request,"currPage",1)
		int pageSize = ServletRequestUtils.getIntParameter(request,"pageSize",200)

		int skip = (currPage - 1) * pageSize
		int limit = pageSize

		QueryBuilder filterQuery = QueryBuilder.start("race_time").is($$('$gte':startTime,'$lte':endTime))
				.and("type").is(TopicsType.问题已结束.ordinal())
		if(-1 != product) {
			filterQuery.and("product").is(product)
		}
		if(StringUtils.isNotBlank(childTipIds)) {
			filterQuery.and("tips.0._id").in(string2List(childTipIds))
		}
		if(StringUtils.isNotBlank(teacherName) || StringUtils.isNotBlank(mobile)) {
			QueryBuilder teachQuery = QueryBuilder.start("real_name_from_nc").regex(getLikeStrPattern(teacherName))
					.and("mobile").regex(getLikeStrPattern(mobile))
			List<ObjectId> teachIdList = new ArrayList<>()
			users().find(teachQuery.get(),$$("_id":1)).each { item ->
				teachIdList.add(item.get("_id"))
			}
			filterQuery.and("teach_id").in(teachIdList)
		}

		Map<String,Object> resultMap = findTeacherTopic(filterQuery, skip, limit)

		return getResultOK(resultMap)
	}

	private Map<String,Object> findTeacherTopic(QueryBuilder filterQuery, int skip, int limit){

		def list = topics().aggregate(
				$$('$match',filterQuery.get()),
				$$('$group',[
						_id:["teacherId":'$teach_id',"tipId":'$tips._id'],
						evaluationTypes:['$push':'$evaluation_type'],
						sum:['$sum':1]
				]),
				$$('$group',[
						_id:null,
						list:['$push':'$$ROOT'],
						total:['$sum':1]
				]),
				$$('$unwind','$list'),
				$$('$sort',["list.sum":-1]),
				$$('$skip',skip),
				$$('$limit',limit),
				$$('$lookup',[
						from:"users",
						localField:"list._id.teacherId",
						foreignField:"_id",
						as:"users"
				]),
				$$('$lookup',[
						from:"tip_content",
						localField:"list._id.tipId",
						foreignField:"_id",
						as:"childTip"
				]),
				$$('$lookup',[
						from:"tip_content",
						localField:"childTip.parent_tip_id",
						foreignField:"_id",
						as:"parentTip"
				]),
				$$('$unwind','$users'),
				$$('$unwind','$childTip'),
				$$('$unwind','$parentTip'),
				$$('$project',[
						"teacherId":'$list._id.teacherId',
						"teacherName":'$users.real_name_from_nc',
						"tuid":'$users.tuid',
						"mobile":'$users.mobile',
						"product":'$childTip.product',
						"tipId":'$childTip._id',
						"tipName":'$childTip.tip_name',
						"parentTipId":'$parentTip._id',
						"parentTipName":'$parentTip.tip_name',
						"sum":'$list.sum',
						"total":'$total',
						"evaluationTypes":'$list.evaluationTypes',
						"_id":0
				])
		).results().iterator().toList()

		Map<String,Object> resultMap = new HashMap<>()
		resultMap.put("list",list)
		if(null == list || list.size() == 0) {
			resultMap.put("total",0)
			return resultMap
		}
		resultMap.put("total",list.get(0).get("total"))

		for (DBObject item : list) {
			int weipingjia = 0
			int bumangyi = 0
			int mangyi = 0
			int henmangyi = 0
			List<Integer> evaluationTypes = item.get("evaluationTypes")
			for (Integer type : evaluationTypes) {
				//未评价 , 不满意 , 满意 , 很满意;
				switch (type) {
					case 0: weipingjia++
						break
					case 1: bumangyi++
						break
					case 2: mangyi++
						break
					case 3: henmangyi++
						break
				}
			}
			item.put("weipingjia",weipingjia)
			item.put("bumangyi",bumangyi)
			item.put("mangyi",mangyi)
			item.put("henmangyi",henmangyi)
			item.removeField("evaluationTypes")
			item.removeField("total")
		}
		return resultMap
	}

	private List<Long> string2List(String strs) {
		List<Long> list = new ArrayList<Long>()
		String[] strArray = strs.split(",")
		for(String str : strArray) {
			if(StringUtils.isNotBlank(str)) {
				list.add(Long.parseLong(str))
			}
		}
		return list
	}

}

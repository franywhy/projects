package com.izhubo.web.api

import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.web.BaseController
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$

@Controller
@RequestMapping("api/accountQueAns")
class AccountQueAnsController extends BaseController {
	
	private DBCollection topics(){
		return mainMongo.getCollection("topics")
	}

	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content")
	}

	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus")
	}

	@ResponseBody
	@RequestMapping(value = "bunusTopic", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def bunusTopic(HttpServletRequest request) {
		Long startTime = ServletRequestUtils.getLongParameter(request,"startTime",-1)
		Long endTime = ServletRequestUtils.getLongParameter(request,"endTime",-1)
		String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","")
		String hasParentTipIds = ServletRequestUtils.getStringParameter(request,"hasParentTipIds","")

		def hasTipList = getHasTipList(childTipIds, hasParentTipIds)

		List<Map<String,Object>> result = new ArrayList<>()
		def allManyiBunus = 0d
		def allHenmanyiBunus = 0d
		def allHenmanyiCount = 0
		def allManyiCount = 0
		for(tip in hasTipList) {
			def tip_id = tip["_id"]
			def tip_name = tip["tip_name"]

			Map<String,Object> map = new HashMap<>()
			def manyiTopicIdList = new ArrayList<>()
			def henmanyiTopicIdList = new ArrayList<>()
			topics().find($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.满意.ordinal()),$$("_id":1)).each { item ->
				manyiTopicIdList.add(item.get("_id"))
			}
			topics().find($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.很满意.ordinal()),$$("_id":1)).each { item ->
				henmanyiTopicIdList.add(item.get("_id"))
			}
			def manyiBunus = 0d
			def henmanyiBunus = 0d
			def manyiCount = 0
			def henmanyiCount = 0

			def manyiMmoney = topic_bunus().aggregate(
					$$('$match', ["topic_id" : $$('$in' : manyiTopicIdList)]),
					$$('$project', [mmoney: [$sum: '$mmoney']])
			).results().iterator()
			def henmanyiMmoney = topic_bunus().aggregate(
					$$('$match', ["topic_id" : $$('$in' : henmanyiTopicIdList)]),
					$$('$project', [mmoney: [$sum: '$mmoney']])
			).results().iterator()

			if(manyiMmoney != null){
				while (manyiMmoney.hasNext()){
					def obj = manyiMmoney.next()
					if(obj && obj["mmoney"] != null){
						manyiBunus += obj["mmoney"]
					}
				}
			}
			if(henmanyiMmoney != null){
				while (henmanyiMmoney.hasNext()){
					def obj = henmanyiMmoney.next()
					if(obj && obj["mmoney"] != null){
						henmanyiBunus += obj["mmoney"]
					}
				}
			}
			henmanyiCount = henmanyiTopicIdList.size()
			manyiCount = manyiTopicIdList.size()

			allHenmanyiCount += henmanyiCount
			allHenmanyiBunus += henmanyiBunus
			allManyiCount += manyiCount
			allManyiBunus += manyiBunus

			map.put("tipId",tip_id)
			map.put("tipName",tip_name)
			map.put("henmanyiCount",henmanyiCount)
			map.put("henmanyiBunus",henmanyiBunus)
			map.put("manyiCount",manyiCount)
			map.put("manyiBunus",manyiBunus)
			result.add(map)
		}
		Map<String,Object> sumMap = new HashMap<>()
		sumMap.put("tipId",0)
		sumMap.put("tipName","合计")
		sumMap.put("henmanyiCount",allHenmanyiCount)
		sumMap.put("henmanyiBunus",allHenmanyiBunus)
		sumMap.put("manyiCount",allManyiCount)
		sumMap.put("manyiBunus",allManyiBunus)
		result.add(sumMap)
		return getResultOK(result)
	}

	@ResponseBody
	@RequestMapping(value = "tipTopic", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def tipTopic(HttpServletRequest request) {
		Long startTime = ServletRequestUtils.getLongParameter(request,"startTime",-1)
		Long endTime = ServletRequestUtils.getLongParameter(request,"endTime",-1)
		String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","")
		String hasParentTipIds = ServletRequestUtils.getStringParameter(request,"hasParentTipIds","")

		def hasTipList = getHasTipList(childTipIds, hasParentTipIds)

		List<Map<String,Object>> result = new ArrayList<>()
		int sumCount = 0
		int sumWeipingjia = 0
		int sumBumangyi = 0
		int sumMangyi = 0
		int sumHenmangyi = 0
		int sumChaoshi = 0
		int sumWeiqiangda = 0
		int sumDayizhon = 0
		for(tip in hasTipList) {
			def tip_id = tip["_id"]
			def tip_name = tip["tip_name"]

			Map<String,Object> map = new HashMap<>()
			int count = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id))

			int weipingjia = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.未评价.ordinal()))

			int bumangyi = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.不满意.ordinal()))

			int mangyi = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.满意.ordinal()))

			int henmangyi = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.问题已结束.ordinal(),
					"evaluation_type":TopicEvaluationType.很满意.ordinal()))

			int chaoshi = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.抢答失败.ordinal()))

			int weiqiangda = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.待抢答.ordinal()))

			int dayizhon = topics().count($$("timestamp":$$('$gte':startTime,'$lte':endTime),"tips.0._id":tip_id,
					"type":TopicsType.抢答成功.ordinal()))

			sumCount += count
			sumWeipingjia += weipingjia
			sumBumangyi += bumangyi
			sumMangyi += mangyi
			sumHenmangyi += henmangyi
			sumChaoshi += chaoshi
			sumWeiqiangda += weiqiangda
			sumDayizhon += dayizhon

			map.put("tipId",tip_id)
			map.put("tipName",tip_name)
			map.put("count",count)
			map.put("henmangyi",henmangyi)
			map.put("mangyi",mangyi)
			map.put("bumangyi",bumangyi)
			map.put("weipingjia",weipingjia)
			map.put("chaoshi",chaoshi)
			map.put("weiqiangda",weiqiangda)
			map.put("dayizhon",dayizhon)
			result.add(map)
		}
		Map<String,Object> sumMap = new HashMap<>()
		sumMap.put("tipId",0)
		sumMap.put("tipName","合计")
		sumMap.put("count",sumCount)
		sumMap.put("henmangyi",sumHenmangyi)
		sumMap.put("mangyi",sumMangyi)
		sumMap.put("bumangyi",sumBumangyi)
		sumMap.put("weipingjia",sumWeipingjia)
		sumMap.put("chaoshi",sumChaoshi)
		sumMap.put("weiqiangda",sumWeiqiangda)
		sumMap.put("dayizhon",sumDayizhon)
		result.add(sumMap)
		return getResultOK(result)
	}

	@ResponseBody
	@RequestMapping(value = "teacherTopic", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	def teacherTopic(HttpServletRequest request){
		Long startTime = ServletRequestUtils.getLongParameter(request,"startTime",-1)
		Long endTime = ServletRequestUtils.getLongParameter(request,"endTime",-1)
		String mobile = ServletRequestUtils.getStringParameter(request,"mobile","")
		String realName = ServletRequestUtils.getStringParameter(request,"realName","")
		Long teachId = ServletRequestUtils.getLongParameter(request,"teachId",-1)
		String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","")
		String hasParentTipIds = ServletRequestUtils.getStringParameter(request,"hasParentTipIds","")
		int currPage = ServletRequestUtils.getIntParameter(request,"currPage",1)
		int pageSize = ServletRequestUtils.getIntParameter(request,"pageSize",200)

		def hasTipList = getHasTipList(childTipIds, hasParentTipIds)
		boolean isMobileOrTeachId = false
		boolean hasFilter = false
		QueryBuilder filterQuery = QueryBuilder.start()
		if(StringUtils.isNotBlank(mobile)) {
			isMobileOrTeachId = true
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
		if(-1 != teachId) {
			isMobileOrTeachId = true
			teacherQuery.and("_id").is(teachId)
		} else {
			if(null != tuidList) {
				teacherQuery.and("tuid").in(tuidList)
			} else {
				teacherQuery.and("priv1").is(1)
				teacherQuery.and("priv").is(2)
				teacherQuery.and("business_id").exists(true)
			}
			if(StringUtils.isNotBlank(realName)) {
				teacherQuery.and("real_name_from_nc").regex(getLikeStrPattern(realName))
			}
		}

		List<Map<String,Object>> result = new ArrayList<>()
		int allCount = 0
		int allWeipingjia = 0
		int allBumangyi = 0
		int allMangyi = 0
		int allHenmangyi = 0
		long total = users().count(teacherQuery.get()) * (hasTipList.size() + 1) + 1
		if(hasTipList.size() > 0) {
			pageSize = pageSize / hasTipList.size()
		} else {
			pageSize = 0
		}
		users().find(teacherQuery.get()).skip((currPage - 1) * pageSize).limit(pageSize).each { item ->
			String username = qQUser().findOne($$("tuid":item.get("tuid")),$$("username":1)).get("username")
			def tipIdList = new ArrayList<>()

			if(-1 != teachId) {
				def tip_iterator = topics().aggregate(
						$$('$match',["race_time":['$gte':startTime,'$lte':endTime], "teach_id":teachId]),
						$$('$unwind','$tips'),
						$$('$group',[_id:["tipId":'$tips._id']]),
						$$('$project',["tipId":'$_id.tipId'])
				).results().iterator()
				while (tip_iterator.hasNext()) {
					def topicsItem = tip_iterator.next()
					tipIdList.add(topicsItem.get("tipId"))
				}
			} else {
				def user_industry = null
				def business_id = item.get("business_id") == null ? null : item.get("business_id")[0]
				if (business_id != null && "zikao".equals(business_id)){
					user_industry = item.get("user_industry_zikao")
				} else if(business_id != null && "kuaiji".equals(business_id)) {
					user_industry = item.get("user_industry")
				}
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
			}

			if(hasTipList.size() > 0 && tipIdList.size() > 0) {
				boolean isShow = false
				int sumCount = 0
				int sumWeipingjia = 0
				int sumBumangyi = 0
				int sumMangyi = 0
				int sumHenmangyi = 0

				hasTipList.each { def tip ->
					def tip_id = tip["_id"]
					def tip_name = tip["tip_name"]
					if(tipIdList.contains(tip_id)) {
						isShow = true
						Map<String,Object> map = new HashMap<>()

						int count = topics().count($$("race_time":$$('$gte':startTime,'$lte':endTime),"teach_id":item.get("_id"),"tips.0._id":tip_id))

						int weipingjia = topics().count($$("race_time":$$('$gte':startTime,'$lte':endTime),"teach_id":item.get("_id"),"tips.0._id":tip_id,
								"type":TopicsType.问题已结束.ordinal(),
								"evaluation_type":TopicEvaluationType.未评价.ordinal()))

						int bumangyi = topics().count($$("race_time":$$('$gte':startTime,'$lte':endTime),"teach_id":item.get("_id"),"tips.0._id":tip_id,
								"type":TopicsType.问题已结束.ordinal(),
								"evaluation_type":TopicEvaluationType.不满意.ordinal()))

						int mangyi = topics().count($$("race_time":$$('$gte':startTime,'$lte':endTime),"teach_id":item.get("_id"),"tips.0._id":tip_id,
								"type":TopicsType.问题已结束.ordinal(),
								"evaluation_type":TopicEvaluationType.满意.ordinal()))

						int henmangyi = topics().count($$("race_time":$$('$gte':startTime,'$lte':endTime),"teach_id":item.get("_id"),"tips.0._id":tip_id,
								"type":TopicsType.问题已结束.ordinal(),
								"evaluation_type":TopicEvaluationType.很满意.ordinal()))

						sumCount += count
						sumWeipingjia += weipingjia
						sumBumangyi += bumangyi
						sumMangyi += mangyi
						sumHenmangyi += henmangyi

						map.put("teachId",item.get("_id"))
						map.put("mobile",username)
						map.put("realName",item.get("real_name_from_nc"))
						map.put("tipId",tip_id)
						map.put("tipName",tip_name)
						map.put("count",count)
						map.put("henmangyi",henmangyi)
						map.put("mangyi",mangyi)
						map.put("bumangyi",bumangyi)
						map.put("weipingjia",weipingjia)
						result.add(map)
					}
				}
				if(isMobileOrTeachId || isShow) {
					Map<String,Object> sumMap = new HashMap<>()
					sumMap.put("teachId",item.get("_id"))
					sumMap.put("mobile","小计")
					sumMap.put("realName",item.get("real_name_from_nc"))
					sumMap.put("tipId",0)
					sumMap.put("count",sumCount)
					sumMap.put("henmangyi",sumHenmangyi)
					sumMap.put("mangyi",sumMangyi)
					sumMap.put("bumangyi",sumBumangyi)
					sumMap.put("weipingjia",sumWeipingjia)
					result.add(sumMap)

					allCount += sumCount
					allHenmangyi += sumHenmangyi
					allMangyi += sumMangyi
					allBumangyi += sumBumangyi
					allWeipingjia += sumWeipingjia
				}
			}
		}
		Map<String,Object> allMap = new HashMap<>()
		allMap.put("mobile","合计")
		allMap.put("realName","")
		allMap.put("tipId",0)
		allMap.put("count",allCount)
		allMap.put("henmangyi",allHenmangyi)
		allMap.put("mangyi",allMangyi)
		allMap.put("bumangyi",allBumangyi)
		allMap.put("weipingjia",allWeipingjia)
		result.add(allMap)
		Map<String,Object> resultMap = new HashMap<>()
		resultMap.put("total",total)
		resultMap.put("list",result)
		return getResultOK(resultMap)
	}

	def getHasTipList(String childTipIds, String hasParentTipIds) {
		List<Long> childTipIdList = string2List(childTipIds)
		List<Long> hasParentTipIdList = string2List(hasParentTipIds)
		def hasTipList = new ArrayList<>()
		if(childTipIdList.size() > 0) {
			hasTipList.addAll(tip_content().find($$("parent_tip_id":$$('$in':hasParentTipIdList),"_id":$$('$in':childTipIdList)),$$("_id":1,"tip_name":1)).toArray())
		} else {
			hasTipList.addAll(tip_content().find($$("parent_tip_id":$$('$in':hasParentTipIdList)),$$("_id":1,"tip_name":1)).toArray())
		}
		return hasTipList
	}

	List<Long> string2List(String strs) {
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

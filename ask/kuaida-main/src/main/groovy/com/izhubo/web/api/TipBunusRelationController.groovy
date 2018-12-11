package com.izhubo.web.api

import com.izhubo.model.ScoreType;
import com.izhubo.web.BaseController
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat;

import static com.izhubo.rest.common.util.WebUtils.$$;

/**
 * @author hq
 */
@Controller
@RequestMapping("api/tipBunusRelation")
class TipBunusRelationController extends BaseController {


	private DBCollection tip_bunus_relation() {
		return mainMongo.getCollection("tip_bunus_relation");
	}

	private DBCollection bunus_limits() {return mainMongo.getCollection("bunus_limits");}

    private DBCollection tip_content() {return mainMongo.getCollection("tip_content");}

	private static Logger logger = LoggerFactory.getLogger(TipBunusRelationController.class);

    /**
     * 获取标签对应红包关系和对应红包限制列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTipBunusRelationList", method = RequestMethod.POST ,produces = "application/json; charset=UTF-8")
    def getTipBunusRelationList(HttpServletRequest request){
        String tipName = ServletRequestUtils.getStringParameter(request,"labelName","")
		String page = ServletRequestUtils.getStringParameter(request,"page",1+"")
		Integer size = ServletRequestUtils.getIntParameter(request,"size",10)
		Integer pages=Integer.parseInt(page);
		if (pages<=0){
			pages=1
		}else {
			pages=(pages+10)/10
		}
        if("null".equals(tipName) || StringUtils.isEmpty(tipName)){
            tipName="";
        }
        List resultList = new ArrayList();
		List resultList2 = new ArrayList();
        def tipContent = tip_content().find($$("dr" : 0,"parent_tip_id" : 0, "tip_name": $$('$regex': tipName, '$options': 'i'))).toArray();
        tipContent.each { it ->
            Map data = new HashMap();
            Integer tid = it.get("_id");
            Integer satisfy = get_bunus_limits_id(tid,ScoreType.满意.toString());
            if(satisfy!=0){
                def bunusLimit = bunus_limits().findOne($$("_id" : satisfy));
				Double min=bunusLimit.get("min_money");
				Double max=bunusLimit.get("max_money");
                data.put("min",min);
                data.put("max",max);
            }
            Integer verySatisfy = get_bunus_limits_id(tid,ScoreType.很满意.toString());
            if(verySatisfy!=0) {
                def veryBunusLimit = bunus_limits().findOne($$("_id": verySatisfy));
				Double veryMin = veryBunusLimit.get("min_money")
				Double veryMax = veryBunusLimit.get("max_money")
                Long createTime = veryBunusLimit.get("create_time")
                data.put("veryMin", veryMin);
                data.put("veryMax", veryMax);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(createTime);
				data.put("createTime", dateString);
                String name = it.get("tip_name");
                data.put("tid",tid);
                data.put("tidName",name);

                resultList.add(data);

				if(resultList.size()>((pages-1)*size) && resultList.size()<=(((pages-1)*size)+10)){
					resultList2.add(data);
				}

            }
        }
        return getResult(1,"获取标签对应红包关系和对应红包限制列表",resultList2);
    }

	/**
	 * 查询总数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryTotal", method = RequestMethod.POST ,produces = "application/json; charset=UTF-8")
	def queryTotal(HttpServletRequest request){
		String tipName = ServletRequestUtils.getStringParameter(request,"labelName","")
//		String page = ServletRequestUtils.getStringParameter(request,"page",1+"")
//		Integer size = ServletRequestUtils.getIntParameter(request,"size",10)
//		Integer pages=Integer.parseInt(page);
//		if (pages<=0){
//			pages=1
//		}
		if("null".equals(tipName) || StringUtils.isEmpty(tipName)){
			tipName="";
		}
		List resultList = new ArrayList();
		def tipContent = tip_content().find($$("dr" : 0,"parent_tip_id" : 0, "tip_name": $$('$regex': tipName, '$options': 'i'))).toArray();
		tipContent.each { it ->
			Map data = new HashMap();
			Integer tid = it.get("_id");
			Integer satisfy = get_bunus_limits_id(tid,ScoreType.满意.toString());
			if(satisfy!=0){
				def bunusLimit = bunus_limits().findOne($$("_id" : satisfy));
				Double min=bunusLimit.get("min_money");
				Double max=bunusLimit.get("max_money");
				data.put("min",min);
				data.put("max",max);
			}
			Integer verySatisfy = get_bunus_limits_id(tid,ScoreType.很满意.toString());
			if(verySatisfy!=0) {
				def veryBunusLimit = bunus_limits().findOne($$("_id": verySatisfy));
				Double veryMin = veryBunusLimit.get("min_money")
				Double veryMax = veryBunusLimit.get("max_money")
				Long createTime = veryBunusLimit.get("create_time")
				data.put("veryMin", veryMin);
				data.put("veryMax", veryMax);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(createTime);
				data.put("createTime", dateString);
				String name = it.get("tip_name");
				data.put("tid",tid);
				data.put("tidName",name);

				resultList.add(data);
			}
		}
		return getResult(1,"获取标签对应红包关系和对应红包限制列表",resultList);
	}

    /**
     * 根据标签ID获取对应红包限额
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTipBunusRelationByTipId", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    def getTipBunusRelationByTipId(HttpServletRequest request) {
        Integer tid = ServletRequestUtils.getIntParameter(request, "tid", 0)

        def tipContent = tip_content().findOne($$("dr": 0, "_id": tid))
        Map data = new HashMap();
        data = new HashMap();
        Integer satisfy = get_bunus_limits_id(tid, ScoreType.满意.toString());
        if (satisfy != 0) {
            def bunusLimit = bunus_limits().findOne($$("_id": satisfy));
            Double min = bunusLimit.get("min_money");
			Double max = bunusLimit.get("max_money");
            data.put("min", min);
            data.put("max", max);
        }
        Integer verySatisfy = get_bunus_limits_id(tid, ScoreType.很满意.toString());
        if (verySatisfy != 0) {
            def veryBunusLimit = bunus_limits().findOne($$("_id": verySatisfy));
			Double veryMin = veryBunusLimit.get("min_money")
			Double veryMax = veryBunusLimit.get("max_money")
            Long createTime = veryBunusLimit.get("create_time")
            data.put("veryMin", veryMin);
            data.put("veryMax", veryMax);
            data.put("createTime", createTime);

            String name = tipContent.get("tip_name");
            data.put("tid", tid);
            data.put("tidName", name);
        }
        return getResult(1, "根据标签ID获取对应红包限额", data);
    }

	/**
	 * 保存标签对应红包关系和对应红包限制
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveTipBunusRelation", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def saveTipBunusRelation(HttpServletRequest request){
		Integer userId = ServletRequestUtils.getIntParameter(request,"userId",0)
		Integer product = ServletRequestUtils.getIntParameter(request,"product",0)

		Integer tip_id = ServletRequestUtils.getIntParameter(request,"tip_id",0)

		Double max_money = ServletRequestUtils.getDoubleParameter(request,"maxMoney",2)
		Double min_money = ServletRequestUtils.getDoubleParameter(request,"minMoney",1)
		Double very_max_money = ServletRequestUtils.getDoubleParameter(request,"veryMaxMoney",2)
		Double very_min_money = ServletRequestUtils.getDoubleParameter(request,"veryMinMoney",1)
        //product没有传值的遗留问题
		/*if(product==3){
            def tipContent = tip_content().findOne($$("dr" : 0,"_id" : tip_id))
            product=tipContent.get("product");
        }*/
		//保存红包限制表数据
		Map<String,Map> bunusLimits = saveBunusLimits(userId,product,max_money,min_money,1);
		//获取红包限制的最大ID
		Integer bunus_limits_id = bunusLimits.get("data").get("bunus_limits_id");
		//保存关系
		saveTipBunusRelationTable(userId,tip_id,bunus_limits_id,product);

		//保存红包限制表数据
		Map<String,Map> bunusLimits2 = saveBunusLimits(userId,product,very_max_money,very_min_money,2);
		//获取红包限制的最大ID
		Integer bunus_limits_id2=bunusLimits2.get("data").get("bunus_limits_id");
		//保存关系
		saveTipBunusRelationTable(userId,tip_id,bunus_limits_id2,product);

		return getResultOK("新增(标签对应红包关系和对应红包限制)成功");
	}

    /**
     * 更新标签对应红包关系和对应红包限制
     * @param request
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "updateTipBunusRelation", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def updateTipBunusRelation(HttpServletRequest request){
		Integer userId = ServletRequestUtils.getIntParameter(request,"userId",0)
		Integer product = ServletRequestUtils.getIntParameter(request,"product",0)

		Integer tip_id = ServletRequestUtils.getIntParameter(request,"tip_id",0)

		Double max_money = ServletRequestUtils.getDoubleParameter(request,"maxMoney",2)
		Double min_money = ServletRequestUtils.getDoubleParameter(request,"minMoney",1)
		Double very_max_money = ServletRequestUtils.getDoubleParameter(request,"veryMaxMoney",2)
		Double very_min_money = ServletRequestUtils.getDoubleParameter(request,"veryMinMoney",1)

        /*if(product==3){
            def tipContent = tip_content().findOne($$("dr" : 0,"_id" : tip_id))
            product=tipContent.get("product");
        }*/
		//根据标签ID查出列表红包ID
		def tipBunusRelation = tip_bunus_relation().find(
				$$("tip_id" : tip_id ,"product":product, "dr" : 0),
				$$("_id" : 1 ,"bunus_limits_id" : 1 ))?.toArray();

		Integer bunus_limits_id1 = tipBunusRelation.get(0).get("bunus_limits_id");
		updateBunusLimits(userId,product,bunus_limits_id1,max_money,min_money);

		Integer bunus_limits_id2 = tipBunusRelation.get(1).get("bunus_limits_id");
		updateBunusLimits(userId,product,bunus_limits_id2,very_max_money,very_min_money);

		return getResultOK("新增(标签对应红包关系和对应红包限制)成功");
	}

	/**
	 * 新增标签和红包限制之间的关系
	 * @param userId
	 * @param tip_id
	 * @param bunus_limits_id
	 * @param product
	 * @return
	 */
	private def saveTipBunusRelationTable(Integer userId,Integer tip_id,Integer bunus_limits_id,Integer product){
		BasicDBObject tipBunusRelation = new BasicDBObject();

		tipBunusRelation.append("_id", UUID.randomUUID().toString());
		tipBunusRelation.append("tip_id", tip_id);
		tipBunusRelation.append("bunus_limits_id", bunus_limits_id);
		tipBunusRelation.append("creater", userId);
		tipBunusRelation.append("create_time", System.currentTimeMillis());
		tipBunusRelation.append("updater", userId);
		tipBunusRelation.append("update_time", System.currentTimeMillis());
		tipBunusRelation.append("product", product);
		tipBunusRelation.append("dr", 0);
		tip_bunus_relation().save(tipBunusRelation);

		return getResultOK("新增（标签和红包额度限制关系）成功");
	}

	/**
	 * 保存红包限额
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveBunusLimits", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	private Map<String,Object> saveBunusLimits(Integer userId,Integer product,Double max_money,Double min_money,Integer scoreTypeOrder){
		String name = ScoreType.不满意.toString()
		if (scoreTypeOrder == ScoreType.满意.ordinal()) {
			name = ScoreType.满意.toString()
		} else if (scoreTypeOrder == ScoreType.很满意.ordinal()) {
			name = ScoreType.很满意.toString()
		}
		Integer bunusLimitsId = bunus_limits().find().sort($$("_id": -1)).limit(1).toArray().get(0).get("_id");
		BasicDBObject bunusLimits = new BasicDBObject();

		bunusLimits.append("_id", bunusLimitsId+1);
		bunusLimits.append("name", name);
		bunusLimits.append("max_money", max_money);
		bunusLimits.append("min_money", min_money);
		bunusLimits.append("creater", userId);
		bunusLimits.append("create_time", System.currentTimeMillis());
		bunusLimits.append("updater", userId);
		bunusLimits.append("update_time", System.currentTimeMillis());
		bunusLimits.append("product", product);
		bunusLimits.append("dr", 0);
		bunus_limits().save(bunusLimits);

		Map<String,Object> resultMap = new HashMap<>()
		resultMap.put("bunus_limits_id",bunusLimitsId+1)
		resultMap.put("message","新增红包额度限制成功")
		return getResultOK(resultMap)
	}

	/**
	 * 更新红包限额
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateBunusLimits", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	private def updateBunusLimits(Integer userId,Integer product,Integer bunus_limits_id,Double max_money,Double min_money){
		bunus_limits().update(
				$$("_id" : bunus_limits_id,"product" : product,"dr" : 0),
				$$($set : $$("max_money": max_money,"min_money" : min_money, "update_time" : System.currentTimeMillis(),"updater": userId)),false,false
		);
		return getResultOK("更新红包限制成功");
	}

	/**
	 * 获取红包限额ID
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "get_bunus_limits_id", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	Integer get_bunus_limits_id(Integer tid , String name) {
        def iter = tip_bunus_relation().aggregate(
                $$(
                        $lookup:
                                $$(
                                        from: "bunus_limits",
                                        localField: "bunus_limits_id",
                                        foreignField: "_id",
                                        as: "inv_docs"
                                )
                ),
                $$($match: ["dr": 0, "tip_id": tid]),
                $$($project: ["inv_docs": 1, "tip_id": 1, "_id": 0])
        ).results().toList()
        //循环拿到对应的红包限制ID
        Integer _id = 0;
        iter.each { it ->
            List obj = it.get("inv_docs")
            obj.each { it1 ->
                if(name.equals(it1["name"])){
                    _id = it1["_id"];
                }
            }
        }
        if(_id==0){
            return 0;
        }else {
            return _id;
        }
    }
}

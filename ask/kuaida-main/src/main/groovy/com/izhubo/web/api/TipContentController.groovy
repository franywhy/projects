package com.izhubo.web.api

import com.alibaba.fastjson.JSON
import com.izhubo.web.BaseController
import com.mongodb.DBCollection
import com.mongodb.DBObject
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$

@Controller
@RequestMapping("api/tip")
class TipContentController extends BaseController {

	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content")
	}

	@ResponseBody
	@RequestMapping(value = "/addlist", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	String addlist(HttpServletRequest request) throws Exception {
		//List<DBObject> tips = tip_content().find($$("parent_tip_id":0),$$("_id":1,"order":1)).sort($$("_id":-1)).skip(0).limit(1).toArray();
		//int parent_id = (tips.get(0).get("_id") as int) + 10;
		//int order = (tips.get(0).get("order") as int) + 1;
		//int child_id = (parent_id % 10000) * 100;
		
		String professionName = null;//专业名称
		String labelName = null;//标签名称
		String smallPicUrl = null;//小图地址
		String bigPicUrl = null;//大图地址
		String courseCode = null;//课程代码
		int productId = 0;//产品线  0：会计  1：自考
		int dr = 0;//状态  0：有效  1：失效
		int isCommon = 0
		List<String> courseCodeList = new ArrayList<>()

		StringBuffer errorMsg = new StringBuffer();

		try{
			String labels = ServletRequestUtils.getStringParameter(request, "labels");
			List<Map<String, Object>> labelList = JSON.parseObject(labels,ArrayList.class);//将消息体转成List
			
			if(null != labelList && labelList.size() > 0){
				for(Map<String, Object> labelMap : labelList){
					professionName = labelMap.get("professionName");
					labelName = labelMap.get("labelName");
					smallPicUrl = labelMap.get("smallPicUrl");
					bigPicUrl = labelMap.get("bigPicUrl");
					courseCode = labelMap.get("courseCode");
					if(StringUtils.isNotBlank(courseCode)) {
						courseCodeList = courseCode.split(",").toList()
					}
					productId = labelMap.get("productId") as int
					isCommon = labelMap.get("isCommon") as int
					
					DBObject parentTip = tip_content().findOne($$("tip_name":professionName,"parent_tip_id":0,"product":productId));//根据专业名称和产品线找父级
					
					if(null != parentTip){
						Long countLabel = tip_content().count($$("tip_name":labelName,"parent_tip_id":parentTip.get("_id"),"product":productId));//根据标签名和父级id和产品线找子级
						
						if(countLabel == 0){
							int child_id = 0
							List<DBObject> childTips = tip_content().find($$("parent_tip_id":parentTip.get("_id")),$$("_id":1)).sort($$("_id":-1)).skip(0).limit(1).toArray();
							if(null != childTips && childTips.size() > 0) {
								child_id = childTips.get(0).get("_id") + 1;
							} else {
								int parent_id = parentTip.get("_id")
								child_id = (parent_id % 10000) * 100
							}
							
							def child = new HashMap();
							if(0 == productId){//会计标签
								child["_id"] = child_id;
								child["course_no"] = courseCodeList
								child["tip_name"] = labelName;
								child["parent_tip_id"] = parentTip.get("_id");
								child["pic"] = smallPicUrl;
								child["bigpic"] = bigPicUrl;
								child["product"] = 0;
								child["dr"] = 0;
								child["is_common"] = isCommon
								try{
									tip_content().save($$(child));   
								}catch(Exception e){
									logger.error("TipContentController addlist Exception:"+e.toString());
									errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
								}
							}else{//自考标签
								child["_id"] = child_id;
								child["course_no"] = courseCodeList
								child["tip_name"] = labelName;
								child["parent_tip_id"] = parentTip.get("_id");
								child["product"] = 1;
								child["dr"] = 0;
								child["is_common"] = isCommon
								try{
									tip_content().save($$(child));   
								}catch(Exception e){
									logger.error("TipContentController addlist Exception:"+e.toString());
									errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
								}
							}
						}
					}else{
						List<DBObject> tips = tip_content().find($$("parent_tip_id":0),$$("_id":1,"order":1)).sort($$("_id":-1)).skip(0).limit(1).toArray();
						int parent_id = (tips.get(0).get("_id") as int) + 10;//父级id
						int order = (tips.get(0).get("order") as int) + 1;//序号
						int child_id = (parent_id % 10000) * 100;//子级id
						
						def parent = new HashMap();
						def child = new HashMap();
						if(0 == productId){//会计标签
							parent["_id"] = parent_id;
							parent["tip_name"] = professionName;
							parent["parent_tip_id"] = 0;
							parent["product"] = 0;
							parent["dr"] = 0;
							parent["order"] = order;
							try{
								tip_content().save($$(parent));   
							}catch(Exception e){
								logger.error("TipContentController addlist Exception:"+e.toString());
								errorMsg.append("[专业:"+professionName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
							}
							
							child["_id"] = child_id;
							child["course_no"] = courseCodeList
							child["tip_name"] = labelName;
							child["parent_tip_id"] = parent_id;
							child["pic"] = smallPicUrl;
							child["bigpic"] = bigPicUrl;
							child["product"] = 0;
							child["dr"] = 0;
							child["is_common"] = isCommon
							try{
								tip_content().save($$(child));   
							}catch(Exception e){
								logger.error("TipContentController addlist Exception:"+e.toString());
								errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
							}
						}else{//自考标签
							parent["_id"] = parent_id;
							parent["tip_name"] = professionName;
							parent["parent_tip_id"] = 0;
							parent["product"] = 1;
							parent["dr"] = 0;
							parent["order"] = order;
							try{
								tip_content().save($$(parent));   
							}catch(Exception e){
								logger.error("TipContentController addlist Exception:"+e.toString());
								errorMsg.append("[专业:"+professionName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
							}
							
							child["_id"] = child_id;
							child["course_no"] = courseCodeList
							child["tip_name"] = labelName;
							child["parent_tip_id"] = parent_id;
							child["product"] = 1;
							child["dr"] = 0;
							child["is_common"] = isCommon
							try{
								tip_content().save($$(child));   
							}catch(Exception e){
								logger.error("TipContentController addlist Exception:"+e.toString());
								errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
							}
						}
					}
					
				}
				
			}
			if(errorMsg.length() != 0){
            	errorMsg.append("其余导入成功！<br/>");
            	return errorMsg.toString();
            }else{
            	errorMsg.append("导入成功！！！");
            	return errorMsg.toString();
            }
		}catch(Exception e){
			logger.error("TipContentController addlist Exception:"+e.toString());
			errorMsg.append("系统异常，请联系管理员！！！"+"["+e.toString()+"]");
			return errorMsg.toString();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	String add(HttpServletRequest request) throws Exception {
		String type = null;//类型  "0":专业  "1":标签
		String professionName = null;//专业名称
		String labelName = null;//标签名称
		String smallPicUrl = null;//小图地址
		String bigPicUrl = null;//大图地址
		String courseCode = null;//课程代码
		int productId = 0;//产品线  0：会计  1：自考
		int dr = 0;//状态  0：有效  1：失效
		int isCommon = 0
		List<String> courseCodeList = new ArrayList<>()

		StringBuffer errorMsg = new StringBuffer();
		
		try{
			String label = ServletRequestUtils.getStringParameter(request, "label");
			Map<String, Object> labelMap = JSON.parseObject(label,HashMap.class);//将消息体转成Map
		
			if(null != labelMap){
				type = labelMap.get("type");
				professionName = labelMap.get("professionName");
				labelName = labelMap.get("labelName");
				smallPicUrl = labelMap.get("smallPicUrl");
				bigPicUrl = labelMap.get("bigPicUrl");
				courseCode = labelMap.get("courseCode");
				if(StringUtils.isNotBlank(courseCode)) {
					courseCodeList = courseCode.split(",").toList()
				}
				productId = labelMap.get("productId") as int
				isCommon = labelMap.get("isCommon") as int

				if("0".equals(type)){//新增专业
					DBObject professionTip = tip_content().findOne($$("tip_name":professionName,"parent_tip_id":0,"product":productId));//根据专业名称和产品线查该专业是否存在
					if(null == professionTip){
						List<DBObject> tips = tip_content().find($$("parent_tip_id":0),$$("_id":1,"order":1)).sort($$("_id":-1)).skip(0).limit(1).toArray();
						int parent_id = (tips.get(0).get("_id") as int) + 10;//父级id
						int order = (tips.get(0).get("order") as int) + 1;//序号
						def parent = new HashMap();
						parent["_id"] = parent_id;
						parent["tip_name"] = professionName;
						parent["parent_tip_id"] = 0;
						parent["product"] = productId;
						parent["dr"] = 0;
						parent["order"] = order;
						try{
							tip_content().save($$(parent));   
						}catch(Exception e){
							logger.error("TipContentController add Exception:"+e.toString());
							errorMsg.append("[专业:"+professionName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
						}
					}
				}else{//新增标签
				    DBObject parentTip = tip_content().findOne($$("tip_name":professionName,"parent_tip_id":0,"product":productId));//根据专业名称和产品线找父级
				    
				    if(null != parentTip){
						Long countLabel = tip_content().count($$("tip_name":labelName,"parent_tip_id":parentTip.get("_id"),"product":productId));//根据标签名和父级id和产品线找子级
						
						if(countLabel == 0){
							int child_id = 0
							List<DBObject> childTips = tip_content().find($$("parent_tip_id":parentTip.get("_id")),$$("_id":1)).sort($$("_id":-1)).skip(0).limit(1).toArray();
							if(null != childTips && childTips.size() > 0) {
								child_id = childTips.get(0).get("_id") + 1;
							} else {
								int parent_id = parentTip.get("_id")
								child_id = (parent_id % 10000) * 100
							}
							
							def child = new HashMap();
							if(0 == productId){//会计标签
								child["_id"] = child_id;
								child["course_no"] = courseCodeList
								child["tip_name"] = labelName;
								child["parent_tip_id"] = parentTip.get("_id");
								child["pic"] = smallPicUrl;
								child["bigpic"] = bigPicUrl;
								child["product"] = productId;
								child["dr"] = 0;
								child["is_common"] = isCommon
								try{
									tip_content().save($$(child));   
								}catch(Exception e){
									logger.error("TipContentController add Exception:"+e.toString());
									errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
								}
							}else{//自考标签
								child["_id"] = child_id;
								child["course_no"] = courseCodeList
								child["tip_name"] = labelName;
								child["parent_tip_id"] = parentTip.get("_id");
								child["product"] = productId;
								child["dr"] = 0;
								child["is_common"] = isCommon
								try{
									tip_content().save($$(child));   
								}catch(Exception e){
									logger.error("TipContentController add Exception:"+e.toString());
									errorMsg.append("[标签:"+labelName+"]未保存成功!!!"+"["+e.toString()+"]<br/>");
								}
							}
						}
					}
				}

			}
			
			if(errorMsg.length() != 0){
   
            	return errorMsg.toString();
            }else{
            	errorMsg.append("新增成功！！！");
            	return errorMsg.toString();
            }
		}catch(Exception e){
			logger.error("TipContentController add Exception:"+e.toString());
			errorMsg.append("系统异常，请联系管理员！！！"+"["+e.toString()+"]");
			return errorMsg.toString();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	String update(HttpServletRequest request) throws Exception {
		String type = null;//类型  "0":专业  "1":标签
		int productId = 0;//产品线  0:会计  1:自考
		int dr = 0;//状态  0：有效  1：失效
		int isCommon = 0
		List<String> courseCodeList = new ArrayList<>()

		StringBuffer errorMsg = new StringBuffer();

		try{
			String label = ServletRequestUtils.getStringParameter(request, "label");
			Map<String, Object> labelMap = JSON.parseObject(label,HashMap.class);//将消息体转成Map
			
			if(null != labelMap){
				
				type = labelMap.get("type");
				productId = labelMap.get("productId") as int
				
				if("0".equals(type)){//修改专业
					String oldProfessionName = labelMap.get("oldProfessionName");    
				    String newProfessionName = labelMap.get("newProfessionName");
				    dr = labelMap.get("dr") as int
				    
				    DBObject parentTip = tip_content().findOne($$("tip_name":oldProfessionName,"parent_tip_id":0,"product":productId));//根据原来专业找父级
				    
				    if(null != parentTip){
				    	int id = parentTip["_id"];
						
						try{
							tip_content().update($$("_id":id),$$('$set':$$("tip_name":newProfessionName,"dr":dr)));   
						}catch(Exception e){
							logger.error("TipContentController update Exception:"+e.toString());
							errorMsg.append("[专业:"+newProfessionName+"]未修改成功!!!"+"["+e.toString()+"]<br/>");
						}
				    }
				}else{//修改标签
					String oldProfessionName = labelMap.get("oldProfessionName");
				 	String professionName = labelMap.get("professionName");
				 	String oldLabelName = labelMap.get("oldLabelName");
					String newLabelName = labelMap.get("newLabelName");
					String courseCode = labelMap.get("courseCode");
					if(StringUtils.isNotBlank(courseCode)) {
						courseCodeList = courseCode.split(",").toList()
					}
					dr = labelMap.get("dr") as int
					isCommon = labelMap.get("isCommon") as int

					DBObject parentTip = tip_content().findOne($$("tip_name":professionName,"parent_tip_id":0,"product":productId));//根据专业找父级
				
					if(null != parentTip){
						DBObject oldParentTip = tip_content().findOne($$("tip_name":oldProfessionName,"parent_tip_id":0,"product":productId))
						DBObject childTip = tip_content().findOne($$("parent_tip_id":oldParentTip.get("_id"),"tip_name":oldLabelName,"product":productId));//根据父级id和原来标签名找子级
						if(null != childTip){
							int id = childTip["_id"];
							
							try{
								tip_content().update($$("_id":id),$$('$set':$$("parent_tip_id":parentTip.get("_id"),"tip_name":newLabelName,"dr":dr,"course_no":courseCodeList,"is_common":isCommon)));
							}catch(Exception e){
								logger.error("TipContentController update Exception:"+e.toString());
								errorMsg.append("[标签:"+newLabelName+"]未修改成功!!!"+"["+e.toString()+"]<br/>");
							}
						}
					}   
				}
			}
			if(errorMsg.length() != 0){
   
            	return errorMsg.toString();
            }else{
            	errorMsg.append("修改成功！！！");
            	return errorMsg.toString();
            }
		}catch(Exception e){
			logger.error("TipContentController update Exception:"+e.toString());
			errorMsg.append("系统异常，请联系管理员！！！"+"["+e.toString()+"]");
			return errorMsg.toString();
		}
		return null;
	}
}

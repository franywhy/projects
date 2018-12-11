package com.hq.learningapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hq.common.util.StringUtil;
import com.hq.learningapi.entity.CourseClassplanLivesEntity;
import com.hq.learningapi.entity.CoursesEntity;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.CourseClassplanLivesService;
import com.hq.learningapi.service.CoursesService;
import com.hq.learningapi.service.TikuApiService;
import com.hq.learningapi.util.DateUtils;
import com.hq.learningapi.util.JSONUtil;
import com.hq.learningapi.util.SSOTokenUtils;
import com.hq.learningapi.util.http.HttpClientUtil;



@Service("tikuApiService")
public class TikuApiServiceImpl implements TikuApiService {
    protected static final Logger logger = LoggerFactory.getLogger(TikuApiServiceImpl.class);
	
	@Value("${local-info.self-host}")
	private String selfUrl;

	@Value("${local-info.self-alg-host}")
	private String selAlgfUrl;
	
	@Autowired
	private CoursesService coursesService;
	
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;

	@Override
	public List<Map<String, Object>> getPhaseList(HttpServletRequest request,String token,String courseFk) {
		List<Map<String, Object>> errorResult = new ArrayList<Map<String, Object>>();
		UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
		Long courseFkL = Long.valueOf(courseFk).longValue();
		if(courseFkL==0 || courseFkL.equals(0)){
			Map<String, Object> mesMap=new HashMap<String, Object>();
			//自适应平台不存在课程
			mesMap.put("code", 11101);
			mesMap.put("message", "旧课程没有对应自适应平台");
			errorResult.add(mesMap);
			return errorResult;
		}
		CoursesEntity courses = coursesService.queryObjectByCourseFk(courseFkL);
		if(null==courses){
			Map<String, Object> mesMap=new HashMap<String, Object>();
			//自适应平台不存在课程
			mesMap.put("code", 11101);
			mesMap.put("message", "自适应平台不存在课程");
			errorResult.add(mesMap);
			return errorResult;
		}
		List<CourseClassplanLivesEntity> classPlanLiveList = courseClassplanLivesService.queryClassPlanLiveListByCourseId(courses.getCourseId());
		if(null==classPlanLiveList || classPlanLiveList.size()==0){
			Map<String, Object> mesMap=new HashMap<String, Object>();
			//自适应平台不存在课程
			mesMap.put("code", 11103);
			mesMap.put("message", "不存在排课计划");
			errorResult.add(mesMap);
			return errorResult;
		}
		//建一个知识点实体
		List<Long> phaseIdList = new ArrayList<Long>();
		
		List<Map<String,Object>> resuleList = new ArrayList<Map<String,Object>>();

		for (CourseClassplanLivesEntity classPlanLive : classPlanLiveList) {
			if(null==classPlanLive.getExamStageId() || "".equals(classPlanLive.getExamStageId())){
				continue;
			}
			//根据阶段ID和课程ID判断这个阶段是否有知识点
			/*if(!checkPhase(courseFkL,classPlanLive.getExamStageId())){
				continue;
			}*/
			Long pList = Long.valueOf(classPlanLive.getExamStageId()).longValue();
			if(!phaseIdList.contains(pList)){
				phaseIdList.add(pList);
			}
			Map<String,Object> rs=new HashMap<String,Object>();
			if(DateUtils.getNow().getTime() > classPlanLive.getEndTime().getTime()){
					rs.put("classLiveType", 1);//已上课
					rs.put("classplanLiveId", classPlanLive.getClassplanLiveId());
					resuleList.add(rs);
				}else{
					rs.put("classLiveType", 2);//未上课
					rs.put("classplanLiveId", classPlanLive.getClassplanLiveId());
					resuleList.add(rs);
				}
		}
		//学员知识点掌握情况
		Long userId = userInfo.getUserId();
		String phaseStr = StringUtils.strip(phaseIdList.toString(),"[]").replaceAll(" ", "");
//		
		if(null==phaseStr || phaseStr.equals("")){
			phaseStr = "0";
		}
		String param = "userId="+userId+"&courseId="+courseFk+"&phaseIds="+phaseStr;
		String domain = selAlgfUrl+"/inner/knowledgeCountByPhases";
		String httpAdaptiveUrl = domain+"?"+param;
        logger.info("learning-api:TikuApiServiceImpl-getPhaseList - info - httpAdaptiveUrl: {}",httpAdaptiveUrl);
		String AdaptiveString = HttpClientUtil.getInstance().sendHttpGet(httpAdaptiveUrl);
//			String AdaptiveString = "{'code':200,'message':'操作成功','data':[{'totalCount':9,'passingCount':0,'type':-1,'phaseName':'有序知识点阶段测试'},{'totalCount':9,'passingCount':0,'type':-1,'phaseName':'交叉知识点'}]}";
		//{"timestamp":1520585394575,"status":500,"error":"Internal Server Error","exception":"com.netflix.zuul.exception.ZuulException","message":"GENERAL"}
        logger.info("learning-api:TikuApiServiceImpl-getPhaseList - info - AdaptiveString: {}",AdaptiveString);
        Map<String, Object> json = JSONUtil.jsonToMap(AdaptiveString);
		if (json.get("code").equals(200)) {
			List<Map<String, Object>> result = (List<Map<String, Object>>) json.get("data");
			for (int i = 0; i < result.size(); i++) {
				result.get(i).put("courseId", courseFkL);// 阶段ID
				result.get(i).put("phaseId", phaseIdList.get(i));// 阶段ID
				result.get(i).put("classLiveStatic", resuleList.get(i).get("classLiveType"));// 状态
				result.get(i).put("businessId", "kuaiji");// 业务线ID
				result.get(i).put("classplanLiveId", resuleList.get(i).get("classplanLiveId"));// 课次ID
			}
			return result;
		}else if(json.get("code").equals(400)){
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Map<String, Object> mesMap=new HashMap<String, Object>();
			//自适平台应参数错误
			mesMap.put("code", 11102);
//			mesMap.put("message", "自适平台应参数错误");
			result.add(mesMap);
			return result;
		}
		return null;
	}
	
	public Boolean checkPhase(Long courseId,String phaseId){
		Long phaseIdL = Long.valueOf(phaseId).longValue();
		String param = "courseId="+courseId+"&phaseId="+phaseIdL;
		String domain = selfUrl+"/inner/checkPhase";
		String httpAdaptiveUrl = domain+"?"+param;
		String AdaptiveString = HttpClientUtil.getInstance().sendHttpPost(httpAdaptiveUrl);
		
		if(!StringUtil.isNullOrEmpty(AdaptiveString)){
			Map<String, Object> map = JSONUtil.jsonToMap(AdaptiveString);
			if(null!=map.get("code")){
				if(map.get("code").equals(200)){
					if(map.get("data").equals(true)){
						return true;
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	
	
}

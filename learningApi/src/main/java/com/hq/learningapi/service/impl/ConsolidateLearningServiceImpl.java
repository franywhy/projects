package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.CourseClassplanLivesDao;

import com.hq.learningapi.dao.KnowledgeFileDao;
import com.hq.learningapi.entity.CourseClassplanLivesEntity;
import com.hq.learningapi.entity.KnowledgeFileEntity;
import com.hq.learningapi.entity.MessageCard;
import com.hq.learningapi.pojo.KnowledgeFilePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.ConsolidateLearningService;
import com.hq.learningapi.util.DateUtils;
import com.hq.learningapi.util.JSONUtil;
import com.hq.learningapi.util.RandomUtils;
import com.hq.learningapi.util.SSOTokenUtils;
import com.hq.learningapi.util.http.HttpClientUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liangdongbin on 2018/1/17 0017.
 */
@Service
public class ConsolidateLearningServiceImpl implements ConsolidateLearningService {
	protected static final Logger logger = LoggerFactory.getLogger(ConsolidateLearningServiceImpl.class);
	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;

	@Autowired
	private KnowledgeFileDao knowledgeFileDao;

	@Value("${local-info.sso-host}")
	private String ssoUrl;

	@Value("${local-info.self-host}")
	private String selfUrl;

	@Value("${local-info.self-alg-host}")
	private String selAlgfUrl;

	@Value("${local-info.msg-host}")
	private String msgUrl;

	@Value("${tiku.parse-url}")
	private String parseUrl;
	
	/**
	 * 根据Token获取用户ID
	 * 
	 * @param token
	 * @return
	 */
	public Long getUserIdByToken(String token, HttpServletRequest request) {

		/*
		 * String result = HttpClientUtil.getInstance().sendHttpGet(ssoUrl +
		 * "/inner/userTokenDetail?token="+token);
		 * 
		 * if (StringUtil.isNullOrEmpty(result)) { return new Long(-1); } Map
		 * map = JSONUtil.jsonToBean(result, Map.class); Integer code =
		 * (Integer) map.get("code"); if (code != 200) { return new Long(0); }
		 * map = (Map) map.get("data"); Integer userId = (Integer)
		 * map.get("userId"); return Long.valueOf(userId);
		 */
		UserInfoPOJO user = SSOTokenUtils.getUserInfo(request, token);
		return user.getUserId();
	}

	@Override
	public List getKnowledgeFilesByMultiClassesId(String token,String multiClassesId,String examUUID,HttpServletRequest request) {
		Long userId = getUserIdByToken(token, request);
		if (null==userId || userId <= 0) {
			return null;
		}
		Map map = new HashMap();
		map.put("multiClassesId", multiClassesId);
		map.put("userId", userId);
		map.put("examUUID", examUUID);
		//获取知识点List
		List<KnowledgeFilePOJO> knowledgeList= this.knowledgeFileDao.queryForList(map);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

		for (KnowledgeFilePOJO knowledgeFileEntity:knowledgeList) {
			//格式化拆分数据
			List<Map<String,Object>> infoList = new ArrayList<Map<String,Object>>();
			//视频数据
			if(null==knowledgeFileEntity.getPolyvVid() || "".equals(knowledgeFileEntity.getPolyvVid())) {
			}else{
				Map<String,Object> infoMap1 = new HashMap<String,Object>();
				infoMap1.put("knowledgeName",knowledgeFileEntity.getKnowledgeName());
				infoMap1.put("knowledgePic",knowledgeFileEntity.getEmployvPicail());
				infoMap1.put("polyvVid",knowledgeFileEntity.getPolyvVid());
				infoMap1.put("polyvDuration",knowledgeFileEntity.getPolyvDuration());
				infoMap1.put("vtype",knowledgeFileEntity.getVtype());
				infoMap1.put("ctype",0);
				infoMap1.put("ctypeName","视频");
				infoList.add(infoMap1);
			}
            //ppt等资料数据
			if(null==knowledgeFileEntity.getFileUrl() || "".equals(knowledgeFileEntity.getFileUrl())){
			}else {
				Map<String,Object> infoMap2 = new HashMap<String,Object>();
				infoMap2.put("fileName",knowledgeFileEntity.getFileName());
				infoMap2.put("fileUrl",knowledgeFileEntity.getFileUrl());
//				infoMap2.put("filePic","http://download.hqjy.com/staic/lessontimes/2-min.jpg");
                infoMap2.put("filePic", RandomUtils.getFileUrl());
				infoMap2.put("ctype",1);
				infoMap2.put("ctypeName","PPT");
				infoList.add(infoMap2);
			}
			//ppt等资料数据
			if(null==knowledgeFileEntity.getExamUUID() || "".equals(knowledgeFileEntity.getExamUUID())){
			}else {
				Map<String,Object> infoMap3 = new HashMap<String,Object>();
				infoMap3.put("examUUIDName","解析");
				infoMap3.put("examUUIDUrl",parseUrl+"?examUUID="+knowledgeFileEntity.getExamUUID()+"&knowledgeId="+knowledgeFileEntity.getKnowledgeId());
				infoMap3.put("examUUIDPic",RandomUtils.getFileUrl());
				infoMap3.put("ctype",2);
				infoMap3.put("ctypeName","解析");
				infoList.add(infoMap3);
			}

			//内层结构
			Map<String,Object> knowledgeMap = new HashMap<String,Object>();
			knowledgeMap.put("title",knowledgeFileEntity.getKnowledgeName());
//			knowledgeMap.put("multiClassesId",knowledgeFileEntity.getMultiClassesId());
			knowledgeMap.put("list",infoList);
			result.add(knowledgeMap);
		}

		return result;
	}

	/**
	 * 根据课次ID获取阶段名字
	 *
	 * @param multiClassesId
	 * @return
	 */
	public String getPhaseName(String multiClassesId){
		CourseClassplanLivesEntity entity=courseClassplanLivesDao.queryObject(multiClassesId);
		String phaseName=null;
		if(null==entity || "".equals(entity) || StringUtils.isBlank(entity.getExamStageId())){
			return phaseName;
		}else{
			String phaseId = entity.getExamStageId();

			String httpResult = HttpClientUtil.getInstance().sendHttpGet(selfUrl + "/adlPhase/list?phaseId=" + phaseId);
			logger.info("learning-api:ConsolidateLearningServiceImpl-ConsolidateLearningServiceImpl - error - message: {}",httpResult);
			Map<String,Object> map = JSONUtil.jsonToBean(httpResult, Map.class);
			Integer code = (Integer) map.get("code");
			if (code != 0 ) {
				return null;
			}
			Map data = (Map) map.get("data");
			if(data.get("totalCount").equals(0)){
				return null;
			}
			List<Object> list = (List) data.get("list");
			Map phase = (Map)list.get(0);
			phaseName = phase.get("phaseName").toString();
		}

		return phaseName;
	}

	public String process(Long userId, Long phaseId, String multiClassesId, Long courseId, Long trainType) {

		// 请求自适应系统
		// Long courseId =
		List list = getKnowledgeFiles(userId, phaseId, courseId);
		if (null == list) {
			return "请求自适应系统fail";
		}else if(list.get(0).equals(201)){
			return "未掌握知识点为空";
		}
		//获取课次
        CourseClassplanLivesEntity courseClassplanLivesEntity = courseClassplanLivesDao.queryObject(multiClassesId);
		String msgs = setMsg(list,phaseId,multiClassesId,courseId); // 发送消息的list
		
		// 发送消息
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("msg", msgs);
		map.put("group_channels", userId.toString());
		map.put("title", String.valueOf("巩固学习"));// http://dev.api.hqjy.cn/service-msg-api/msg/sendUserMessage  #消息系统的请求路径
		map.put("content", String.valueOf(courseClassplanLivesEntity.getClassplanLiveName()));
		String msgId = HttpClientUtil.getInstance().sendHttpPost(msgUrl + "/msg/sendUserMessage", map);
        logger.info("learning-api:ConsolidateLearningServiceImpl-setMsg - msgId - message: {}",msgId);
		//清理上一次做错的记录(2018-09-05改为不清理)
//        cleanOldRecord(userId, multiClassesId,examUUID);
		// 保存数据
		saveKnowledgeFiles(list, userId, phaseId, multiClassesId);

		return "";
	}

	@Override
	public int saveBath(List<KnowledgeFileEntity> knowledgeFiles) {
		this.knowledgeFileDao.batchSave(knowledgeFiles);
		return 1;

	}

	/**
	 * 拼装消息体
	 * 
	 * @param list
	 * @return
	 */
	public String setMsg(List list,Long phaseId, String multiClassesId, Long courseId) {
        //获取课次
        CourseClassplanLivesEntity courseClassplanLivesEntity = courseClassplanLivesDao.queryObject(multiClassesId);
		String str = "";
		MessageCard card = new MessageCard();
		card.setMsgType(2);
		card.setTitle("巩固学习");
		card.setDescribe(String.valueOf(courseClassplanLivesEntity.getClassplanLiveName()));
		card.setPushText("这节"+String.valueOf((courseClassplanLivesEntity.getClassplanLiveName()))+"课学的怎么样？巩固是掌握学习必不可少的方法。");
		card.setPushTime(DateUtils.getNow().getTime());
		
		Map map = (Map) list.get(0);
		map.put("classplanLiveId", multiClassesId);
		map.put("phaseId", phaseId);
		map.put("courseFk", courseId);
		map.put("count", list.size());
//		map.put("filePic", "http://file.hqjy.com/file/singleDirectDownload/tNWCTXkTvHwBTXETueUfQSIAAAAAAAAAAQ.jpg");
		map.put("filePic", RandomUtils.getFileUrl());
//		map.put("examUUID", );
		map.put("examUUIDName", "解析");
		map.put("examUUIDUrl",parseUrl+"?examUUID="+((Map) list.get(0)).get("examUUID")+"&knowledgeId="+((Map) list.get(0)).get("knowledgeId"));
		map.put("examUUIDPic", RandomUtils.getFileUrl());


		card.setMsgData(map);
		return JSONUtil.beanToJson(card);

	}

	/**
	 * 请求自适应系统
	 * 
	 * @param userId
	 * @param phaseId
	 * @return
	 */
	public List getKnowledgeFiles(Long userId, Long phaseId, Long courseId) {
		Map map = new HashMap();
		/*
		 * map.put("phaseId", String.valueOf(phaseId)); map.put("userId",
		 * String.valueOf(userId)); map.put("page", "1"); map.put("size",
		 * "1000");
		 */
		String result = null;

		result = HttpClientUtil.getInstance().sendHttpGet(selAlgfUrl + "/inner/notMasteredKnowledge?userId=" + userId
				+ "&phaseId=" + phaseId + "&courseId=" + courseId + "&page=1&size=1000");
		logger.info("learning-api:ConsolidateLearningServiceImpl-getNotMasteredKnowledge - error - message: {}",result);
		map = JSONUtil.jsonToBean(result, Map.class);
		Integer code = (Integer) map.get("code");
		if (code != 200) {
			return null;
		}
		Map data = (Map) map.get("data");

		List<Map<String,Object>> list = (List) data.get("list");
		if (null == list || list.size()==0) {
//			list.add(201);
			return null;
		}else {
			for (Map<String,Object> obj:list) {
				obj.put("examUUID",data.get("examUUID"));
			}
		}
		return list;
	}

	public void cleanOldRecord(Long userId, String multiClassesId,String examUUID){
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("multiClassesId",multiClassesId);
		map.put("examUUID",examUUID);
        this.knowledgeFileDao.cleanOldRecord(map);
    }

	/**
	 * 保存数据
	 * 
	 * @param list
	 * @param userId
	 * @param phaseId
	 */
	public void saveKnowledgeFiles(List list, Long userId, Long phaseId, String multiClassesId) {
		List knowledgeFiles = new ArrayList(); // 保存数据库的list

		KnowledgeFileEntity knowledgeFileEntity = null;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			knowledgeFileEntity = new KnowledgeFileEntity();
			knowledgeFileEntity.setDr(0);

			knowledgeFileEntity.setPhaseId(phaseId);
			knowledgeFileEntity.setUserId(userId);
			knowledgeFileEntity.setMultiClassesId(multiClassesId);
			//

			knowledgeFileEntity.setFileUrl((String) map.get("fileUrl"));
			knowledgeFileEntity.setFileName((String) map.get("fileName"));
			knowledgeFileEntity.setKnowledgeId(Long.valueOf(((Integer) map.get("knowledgeId"))));
			knowledgeFileEntity.setKnowledgeName((String) map.get("knowledgeName"));
			knowledgeFileEntity.setPloyvPic((String) map.get("ployvPic"));
			Integer polyvDurationL =  (Integer) map.get("polyvDuration");
			Long polyvDuration = (null==polyvDurationL?0:polyvDurationL.longValue());
			
			knowledgeFileEntity.setPolyvDuration(polyvDuration);
			knowledgeFileEntity.setPolyvVid((String) map.get("polyvVid"));
			knowledgeFileEntity.setPplyvName((String) map.get("pplyvName"));

			knowledgeFileEntity.setVtype(0L);
			knowledgeFileEntity.setExamUUID((String) map.get("examUUID"));
			// knowledgeFileEntity.setVtype(Long.valueOf(Long.valueOf((((String)
			// map.get("vtype"))=="null"?"0":(String) map.get("vtype")))));
			knowledgeFiles.add(knowledgeFileEntity);

		}
		this.knowledgeFileDao.batchSave(knowledgeFiles);
	}

	@Override
	public String process(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String token = ServletRequestUtils.getStringParameter(request, "token", "");
		Long phaseId = ServletRequestUtils.getLongParameter(request, "phaseId", 0);
		String multiClassesId = ServletRequestUtils.getStringParameter(request, "classplanLiveId", "");
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId", 0);
		Long trainType = ServletRequestUtils.getLongParameter(request, "trainType", 0);
        logger.info("learning-api:ConsolidateLearningController-processPara - info - message: {}",token,phaseId,multiClassesId,courseId,trainType);
		// 根据Token获取用户ID
		Long userId = getUserIdByToken(token, request);
		if (null==userId || userId <= 0) {
			return "token无效";
		}
		logger.info("learning-api:ConsolidateLearningServiceImpl-process - info - message: {}",userId);
		return process(userId, phaseId, multiClassesId, courseId, trainType);

	}

	/**
	 * 保存巩固学习视频播放记录数据
	 * @param request
	 */
	@Override
	public String saveKnowledgePlayLog(HttpServletRequest request) {
		String token = ServletRequestUtils.getStringParameter(request, "token", "");
        String knowledgeId = ServletRequestUtils.getStringParameter(request, "knowledgeId", "");
        String polyvVid = ServletRequestUtils.getStringParameter(request, "polyvVid", "");
        
        // 根据Token获取用户ID
     	Long userId = getUserIdByToken(token, request);
        
        String result = null;
        result = HttpClientUtil.getInstance().sendHttpPost(selfUrl + "/inner/videoLog?userId=" + userId + "&knowledgeId=" + knowledgeId + "&polyvVid=" + polyvVid);
        return result;
	}
}

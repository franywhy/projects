package com.hq.learningcenter.school.service.impl;

import com.hq.common.util.CommonUtil;
import com.hq.learningcenter.config.LocalConfig;
import com.hq.learningcenter.school.dao.*;
import com.hq.learningcenter.school.entity.*;
import com.hq.learningcenter.school.pojo.*;
import com.hq.learningcenter.school.service.MyCourseService;
import com.hq.learningcenter.utils.ClientEnum;
import com.hq.learningcenter.utils.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

@Service("myCourseService")
public class MyCourseServiceImpl implements MyCourseService {

	@Autowired
	private MallOrderDao mallOrderDao;
	
	@Autowired
	private CourseUserplanDao courseUserPlanDao;
	
	@Autowired
	private CourseUserplanDetailDao courseUserPlanDetailDao;
	
	@Autowired
	private CourseUserplanClassDetailDao courseUserPlanClassDetailDao;
	
	@Autowired
	private CourseClassplanLivesDao courseClassPlanLivesDao;
	
	@Autowired
	private CourseRecordDetailDao courseRecordDetailDao;
	
	@Autowired
	private LogWatchDao logWatchDao;
	
	@Autowired
	private LogWatchRecordDao logWatchRecordDao;
	
	@Autowired
	private MallClassDao mallClassDao;
	
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private MallProfessionDao mallProfessionDao;
	
	@Autowired
	private MallLevelDao mallLevelDao;
	
	@Autowired
	private LcTopMsgDao lcTopMsgDao;
	
	@Autowired
	private SysBusinessDao sysBusinessDao;
	
	@Autowired
	private MallGoodsDetailsDao mallGoodsDetailsDao;
	
	@Autowired
	private SysConfigDao sysConfigDao;

	@Autowired
    private CoursesDao coursesDao;

	@Autowired
	private LocalConfig localConfig;

	private static String WX_CODE = "";
	@Value("")
	private void setLiveUrl(){
		WX_CODE = localConfig.getCustomerCareUrl();
	}
	
	private static String OLD_CENTER = "";
	@Value("")
	private void setOldCenter(){
		OLD_CENTER = localConfig.getOldCenter();
	}
	
	private static String CC_COMMODITY = "";
	@Value("")
	private void setCCCommodity(){
		CC_COMMODITY = localConfig.getCcCommodity();
	}
	
	@Override
	public Map<String,Object> getCourse(UserInfoPOJO user, String businessId, ClientEnum client) {
		try{
			Map<String,Object> resultMap = new HashMap<>();
			
			LcTopMsgPOJO topMsgPOJO = null;
			LcTopMsgEntity topMsg = lcTopMsgDao.queryByBusinessId(businessId);
			if(null != topMsg){
				topMsgPOJO = new LcTopMsgPOJO();
				topMsgPOJO.setStatus(topMsg.getStatus());
				topMsgPOJO.setMsgContent(topMsg.getMsgContent());
				if(topMsg.getStatus() != 2 || ClientEnum.APP == client){
					topMsgPOJO.setUrl(topMsg.getUrl());
				}else{
					String encode = URLEncoder.encode(EncryptionUtils.AESencrypt(user.getMobileNo(), "06k9bCEpUAKeL9I3"),"UTF-8");
					
					topMsgPOJO.setUrl(OLD_CENTER+encode);
				}
			}
			resultMap.put("topMsg", topMsgPOJO);
			
			//获取订单
			List<MallOrderEntity> orderList = mallOrderDao.queryOrderByUserId(user.getUserId(), businessId);
			DecimalFormat df = new DecimalFormat("#");
			if(null != orderList && orderList.size() > 0){
				List<LcCoursePOJO> livePOJOList = new ArrayList<>();
				List<LcCoursePOJO> recordPOJOList = new ArrayList<>();
				for(MallOrderEntity order : orderList){
					
					String courseTitle = getCourseName(order,businessId);
			        Long validityDayCount = (order.getDateValidity().getTime() - new Date().getTime()) / CommonUtil.day;
					
					Map<String, Object> userPlanParameter = new HashMap<String, Object>();
			        userPlanParameter.put("userId", user.getUserId());
			        userPlanParameter.put("orderId", order.getOrderId());
			        userPlanParameter.put("productId", order.getProductId());
			        userPlanParameter.put("status", 1);
			        userPlanParameter.put("dr", 0);
			        CourseUserplanEntity userPlanEntity = courseUserPlanDao.queryUserPlanEntity(userPlanParameter);
			        if (null != userPlanEntity) {
				        
				        Map<String, Object> userPlanDetailParameter = new HashMap<String, Object>();
				        userPlanDetailParameter.put("userPlanId", userPlanEntity.getUserPlanId());
				        userPlanDetailParameter.put("status", 1);
				        userPlanDetailParameter.put("dr", 0);
				        Object tmp1 = userPlanEntity.getIsRep() == 1 ? userPlanDetailParameter.put("isSubstituted", 0) : null;
				        Object tmp2 = userPlanEntity.getIsRep() == 0 ? userPlanDetailParameter.put("isSubstitute", 0) : null;
				        Object tmp3 = userPlanEntity.getIsMatch() == 0 ? userPlanDetailParameter.put("isSuitable", 0) : null;
				        List<CourseUserplanDetailEntity> fullUserPlanDetailList = courseUserPlanDetailDao.queryList(userPlanDetailParameter);
				        List<CourseUserplanDetailEntity> filteredUserPlanDetailList = new ArrayList<CourseUserplanDetailEntity>();
				        List<String> classPlanIdList = new ArrayList<>();
				        
				        for (CourseUserplanDetailEntity entity : fullUserPlanDetailList) {
				            String classPlanId = courseUserPlanClassDetailDao.queryClassPlanId(entity.getUserplanDetailId(), 1, 0);
				            if (classPlanId != null) {
				                classPlanIdList.add(classPlanId);
				                filteredUserPlanDetailList.add(entity);
				            }
				        }
				        
				        LcCoursePOJO livePOJO = new LcCoursePOJO();
				        
				        livePOJO.setUserPlanId(userPlanEntity.getUserPlanId());
			        	livePOJO.setCommodityId(userPlanEntity.getCommodityId());
				        livePOJO.setCourseTitle(courseTitle);
				        livePOJO.setOrderId(order.getOrderId());
                        //设置课程图片为商品图片,没有取默认图片
                        livePOJO.setPic(StringUtils.isBlank(order.getPic()) ? "http://download.hqjy.com/staic/course/1-min.jpg": order.getSpic());

				        if(validityDayCount > 0){
				        	livePOJO.setIsEffective(1);
				        	livePOJO.setEffectiveDuration(String.format("剩余有效期  %d天",validityDayCount));
				        }else{
				        	livePOJO.setIsEffective(0);
				        	livePOJO.setEffectiveDuration("有效期已结束");
				        }
				        
				        livePOJO.setCourseType(0);
				        
				        if (fullUserPlanDetailList.size() > 0 && filteredUserPlanDetailList.size() > 0) {
					        //计算课程进度
//					        Integer totalCourse = 0;
//					        List<String> classPlanLiveIdList = new LinkedList<>();
//					        for(int i = 0; i < filteredUserPlanDetailList.size(); i++){
//					        	CourseUserplanDetailEntity entity = filteredUserPlanDetailList.get(i);
//					        	Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(classPlanIdList.get(i), userPlanEntity.getClassTypeId());
//					        	
//					        	classPlanLiveIdList.addAll(courseClassPlanLivesDao.queryClassPlanLiveIdList(userPlanClassLivesParameter));
//					        	
//					        	Integer totalScheduledCourse = courseClassPlanLivesDao.queryTotalScheduledCourse(userPlanClassLivesParameter);
//					        	totalCourse += (totalScheduledCourse == null ? 0 : totalScheduledCourse);
//					        }
//					        Map<String, Object> latestScheduledCourseParameter = generateUserPlanClassLivesParam(null, userPlanEntity.getClassTypeId());
//					        latestScheduledCourseParameter.put("list", classPlanIdList);
//					        if(totalCourse > 0){
//						        Integer latestScheduledCourse = courseClassPlanLivesDao.queryLatestScheduledCourse(latestScheduledCourseParameter);
//						        Float progressRate = (latestScheduledCourse == 0 ? 0 : latestScheduledCourse.floatValue()) * 100 / totalCourse.floatValue();
//						        livePOJO.setProgressRate(Integer.valueOf(df.format(progressRate)));
//						        livePOJO.setProgressRateStr(livePOJO.getProgressRate() + "%");
//					        }else{
//					        	livePOJO.setProgressRate(0);
//					        	livePOJO.setProgressRateStr("0%");
//					        }
//					        //计算出勤率
//					        if (classPlanLiveIdList.size() != 0) {
//					            List<String> businessIdList = logWatchDao.getBusinessIdList(user.getUserId(), classPlanLiveIdList, 1);
//					            if (businessIdList == null || businessIdList.size() == 0) {
//					            	livePOJO.setParticipationRate(0);
//					            	livePOJO.setParticipationRateStr("0%");
//					            } else {
//					                float participationRate = (float) businessIdList.size() * 100 / (float) classPlanLiveIdList.size();
//					                livePOJO.setParticipationRate(Integer.valueOf(df.format(participationRate)));
//					                livePOJO.setParticipationRateStr(livePOJO.getParticipationRate()+"%");
//					            }
//					        } else {
//					        	livePOJO.setParticipationRate(0);
//				            	livePOJO.setParticipationRateStr("0%");
//					        } 
				        	livePOJO.setProgressRateStr("");
					        livePOJO.setIsNoClass(0);
					        //联系班主任
					        String wxCode = sysUserDao.queryWxCode(mallClassDao.queryUserId(order.getClassId()));
					        if(null != wxCode && !wxCode.equals("")){
					        	livePOJO.setWxCode(WX_CODE.replaceFirst("wx_code", wxCode));
					        }else{
					        	livePOJO.setWxCode("");
					        }
					        
					        
				        }else{
				        	//没有学习规划或者没有排课的课程,各项数据统一显示为0,给到特殊字段,标记
				        	livePOJO.setProgressRate(0);
				        	livePOJO.setProgressRateStr("");
//				        	livePOJO.setProgressRateStr("0%");
				        	livePOJO.setParticipationRate(0);
			            	livePOJO.setParticipationRateStr("0%");
			            	livePOJO.setIsNoClass(1);
			            	//联系班主任
			            	livePOJO.setWxCode("");
			            	Long teacherId = mallClassDao.queryUserId(order.getClassId());
			            	if(null != teacherId){
			            		String wxCode = sysUserDao.queryWxCode(teacherId);
						        if(null != wxCode && !wxCode.equals("")){
						        	livePOJO.setWxCode(WX_CODE.replaceFirst("wx_code", wxCode));
						        }
			            	}
				        }
				        
				        UdeskPOJO udesk = getUdesk(user,userPlanEntity.getUserPlanId(), businessId);
				        livePOJO.setUdesk(udesk);
				        livePOJOList.add(livePOJO);
			        }
			        //录播课:通过订单获取商品id和省份id,再从商品商品明细获取课程id列表
			        List<MallGoodsDetailsEntity> goodsDeatails = mallGoodsDetailsDao.queryCourseByCommodityId(order.getCommodityId(), order.getAreaId(), 0, client==ClientEnum.APP?CC_COMMODITY:null);
			        if(null != goodsDeatails && goodsDeatails.size() > 0){
			        	List<Long> totalRecordList = new ArrayList<>();
			        	for(MallGoodsDetailsEntity goodsDeatail : goodsDeatails){
				        	List<Long> recordList = courseRecordDetailDao.queryIdListByCourseId(goodsDeatail.getCourseId());
				        	totalRecordList.addAll(recordList);
			        	}
			        	
			        	if(totalRecordList.size() > 0){
			        		LcCoursePOJO recordPOJO = new LcCoursePOJO();
				        	recordPOJO.setCourseType(1);
				        	recordPOJO.setCommodityId(order.getCommodityId());
				        	recordPOJO.setCourseTitle(courseTitle);
				        	recordPOJO.setOrderId(order.getOrderId());
                            //设置课程图片为商品图片,没有取默认图片
                            recordPOJO.setPic(StringUtils.isBlank(order.getPic()) ? "http://download.hqjy.com/staic/course/1-min.jpg": order.getSpic());
				        	if(validityDayCount > 0){
				        		recordPOJO.setIsEffective(1);
				        		recordPOJO.setEffectiveDuration(String.format("剩余有效期  %d天",validityDayCount));
					        }else{
					        	recordPOJO.setIsEffective(0);
					        	recordPOJO.setEffectiveDuration("有效期已结束");
					        }
//			        		Integer attendCount = logWatchRecordDao.queryAttendCount(user.getUserId(), totalRecordList, 1);
//			        		if(null == attendCount || attendCount ==0){
			        			recordPOJO.setProgressRate(0);
//				        		recordPOJO.setProgressRateStr("0%");
//			        		}else{
//			        			Float progressRate = attendCount.floatValue() * 100 / totalRecordList.size();
//			        			recordPOJO.setProgressRate(Integer.valueOf(df.format(progressRate)));
//			        			recordPOJO.setProgressRateStr(recordPOJO.getProgressRate()+"%");
//			        		}
				        	recordPOJO.setProgressRateStr("");
			        		recordPOJOList.add(recordPOJO);
				        }
			        }
				}
				resultMap.put("oldVersion", 0);
				//根据不同端,返回不一样的数据结构
				if(client == ClientEnum.WEB){
					if(null != livePOJOList && livePOJOList.size() > 0) resultMap.put("live", livePOJOList);
					if(null != recordPOJOList && recordPOJOList.size() > 0) resultMap.put("record", recordPOJOList);
				}else if(client == ClientEnum.APP){
					List<LcCoursePOJO> course = new ArrayList();
					if(null != livePOJOList && livePOJOList.size() > 0) course.addAll(livePOJOList);
					if(null != recordPOJOList && recordPOJOList.size() > 0) course.addAll(recordPOJOList);
					
					resultMap.put("course", course);
				}
				
			}
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Map<String, Object> generateUserPlanClassLivesParam(String classPlanId, Long classTypeId) {
        Map<String, Object> userPlanClassLivesParameter = new HashMap<String, Object>();
        userPlanClassLivesParameter.put("classPlanId", classPlanId);
        userPlanClassLivesParameter.put("isAudited", 1);
        userPlanClassLivesParameter.put("isOpen", 1);
        userPlanClassLivesParameter.put("dr", 0);
        userPlanClassLivesParameter.put("status", 1);
        StringBuilder sb = new StringBuilder();
        sb.append("%,").append(classTypeId).append(",%");
        userPlanClassLivesParameter.put("typeIds", sb.toString());
        userPlanClassLivesParameter.put("startTime", new Date(System.currentTimeMillis()));
        userPlanClassLivesParameter.put("endTime", new Date(System.currentTimeMillis()));
        return userPlanClassLivesParameter;
    }
	
	/**
	 * 获得课程名称	商品名 + (专业 + 层次)
	 * @param param
	 * @return
	 */
	private String getCourseName(MallOrderEntity order, String businessId){
		try{
			Map<String, Object> param = new HashMap<>();
			param.put("orderId", order.getOrderId());
			param.put("userId", order.getUserId());
			param.put("dr", 0);
			param.put("status", 1);
			if(businessId.indexOf("kuaiji") != -1){
				return order.getCommodityName();
			}
			String professionName = mallProfessionDao.queryName(param);
			String levelName = mallLevelDao.queryName(param);
			if(null != professionName && null != levelName){
				//专业,层次均有
				return order.getCommodityName() + "(" + professionName + levelName + ")";
			}else if(null == professionName && null == levelName){
				//专业,层次均无
				return order.getCommodityName();
			}else if(null != professionName){
				//仅有专业
				return order.getCommodityName() + "(" + professionName + ")";
			}else if(null != levelName){
				//仅有层次
				return order.getCommodityName() + "(" + levelName + ")";
			}else{
				return "";
			}
		}catch(Exception e){
			return "";
		}
	}

	@Override
	public SysBusinessEntity getSysBusiness(String businessId) {
		try{
			return sysBusinessDao.queryByBusinessId(businessId);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getWxCode(Long userplanId, String businessId) {
		CourseUserplanEntity userplan = courseUserPlanDao.query(userplanId, businessId);
		if(null != userplan){
			MallOrderEntity order = mallOrderDao.queryOrder(userplan.getOrderId(), businessId);
			if(null != order){
				String wxCode = sysUserDao.queryWxCode(mallClassDao.queryUserId(order.getClassId()));
		        if(null != wxCode && !wxCode.equals("")){
		        	return WX_CODE.replaceFirst("wx_code", wxCode);
		        }
			}
		}
		return "";
	}
	
	

	@Override
	public UdeskPOJO getUdesk(UserInfoPOJO userInfo, Long userplanId, String businessId) {
		CourseUserplanEntity userplan = courseUserPlanDao.query(userplanId, businessId);
		if(null != userplan){
			MallOrderEntity order = mallOrderDao.queryOrder(userplan.getOrderId(), businessId);
			if(null != order){
				UdeskPOJO udesk = new UdeskPOJO();
				udesk.setNick_name(userInfo.getNickName());
				udesk.setC_phone(userInfo.getMobileNo()+"-"+order.getOrderId());
				udesk.setC_email(userInfo.getEmail());
				udesk.setSession_key(userInfo.getUserId().toString());
				udesk.setWeb_token(order.getOrderId().toString());
				udesk.setSdk_token(order.getOrderId().toString());
				udesk.setApp_id(sysConfigDao.queryByKey(businessId.split("_")[0]+"_udesk_app_id"));
				udesk.setApp_key(sysConfigDao.queryByKey(businessId.split("_")[0]+"_udesk_app_key"));
				udesk.setDomain(sysConfigDao.queryByKey("udesk_domain"));
				udesk.setNonce(order.getOrderNo());
				Long timestamp = new Date().getTime();
				udesk.setTimestamp(timestamp);
				String sing_str = "nonce="+udesk.getNonce()+"&timestamp="+udesk.getTimestamp()+"&web_token="+udesk.getWeb_token()+"&545075c194a075d12a71ed519914ff57";
				sing_str = EncryptionUtils.shaHex(sing_str);
				sing_str = sing_str.toUpperCase();
				udesk.setSign(sing_str);
				
				String agent_id = sysUserDao.queryOwnerId(mallClassDao.queryUserId(order.getClassId()));
		        if(null != agent_id && !agent_id.equals("")){
		        	udesk.setAgent_id(agent_id);
		        }
		        return udesk;
			}
		}
		return null;
	}

	@Override
	public RatePOJO getLiveRate(Long userplanId, String businessId, Long userId) {
		RatePOJO rate = new RatePOJO();
		DecimalFormat df = new DecimalFormat("#");
		try{
			CourseUserplanEntity userPlanEntity = courseUserPlanDao.query(userplanId, businessId);
			
			if (null != userPlanEntity) {
		        Map<String, Object> userPlanDetailParameter = new HashMap<String, Object>();
		        userPlanDetailParameter.put("userPlanId", userPlanEntity.getUserPlanId());
		        userPlanDetailParameter.put("status", 1);
		        userPlanDetailParameter.put("dr", 0);
		        Object tmp1 = userPlanEntity.getIsRep() == 1 ? userPlanDetailParameter.put("isSubstituted", 0) : null;
		        Object tmp2 = userPlanEntity.getIsRep() == 0 ? userPlanDetailParameter.put("isSubstitute", 0) : null;
		        Object tmp3 = userPlanEntity.getIsMatch() == 0 ? userPlanDetailParameter.put("isSuitable", 0) : null;
		        List<CourseUserplanDetailEntity> fullUserPlanDetailList = courseUserPlanDetailDao.queryList(userPlanDetailParameter);
		        //List<CourseUserplanDetailEntity> filteredUserPlanDetailList = new ArrayList<CourseUserplanDetailEntity>();
		        List<String> classPlanIdList = new ArrayList<>();
		        
		        for (CourseUserplanDetailEntity entity : fullUserPlanDetailList) {
		            String classPlanId = courseUserPlanClassDetailDao.queryClassPlanId(entity.getUserplanDetailId(), 1, 0);
		            if (classPlanId != null) {
		                classPlanIdList.add(classPlanId);
		                //filteredUserPlanDetailList.add(entity);
		            }
		        }
		        
		        if (fullUserPlanDetailList.size() > 0 && classPlanIdList.size() > 0) {
			        //计算课程进度
			        Integer totalCourse = 0;
			        List<String> classPlanLiveIdList = new LinkedList<>();
			        for(int i = 0; i < classPlanIdList.size(); i++){
			        	Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(classPlanIdList.get(i), userPlanEntity.getClassTypeId());
			        	
			        	classPlanLiveIdList.addAll(courseClassPlanLivesDao.queryClassPlanLiveIdList(userPlanClassLivesParameter));
			        	
			        	Integer totalScheduledCourse = courseClassPlanLivesDao.queryTotalScheduledCourse(userPlanClassLivesParameter);
			        	totalCourse += (totalScheduledCourse == null ? 0 : totalScheduledCourse);
			        }
			        Map<String, Object> latestScheduledCourseParameter = generateUserPlanClassLivesParam(null, userPlanEntity.getClassTypeId());
			        latestScheduledCourseParameter.put("list", classPlanIdList);
			        if(totalCourse > 0){
				        Integer latestScheduledCourse = courseClassPlanLivesDao.queryLatestScheduledCourse(latestScheduledCourseParameter);
				        Float progressRate = (latestScheduledCourse == 0 ? 0 : latestScheduledCourse.floatValue()) * 100 / totalCourse.floatValue();
				        //rate.setProgressRate(progressRate.intValue());
				        rate.setProgressRate(Integer.valueOf(df.format(progressRate)));
			        }else{
			        	rate.setProgressRate(0);
			        }
			        //计算出勤率
			        if (classPlanLiveIdList.size() != 0) {
			            List<String> businessIdList = logWatchDao.getBusinessIdList(userId, classPlanLiveIdList, 1);
			            if (businessIdList == null || businessIdList.size() == 0) {
			            	rate.setParticipationRate(0);
			            } else {
			            	Float participationRate = (float) businessIdList.size() * 100 / (float) classPlanLiveIdList.size();
			                rate.setParticipationRate(Integer.valueOf(df.format(participationRate)));
			            }
			        } else {
			        	rate.setParticipationRate(0);
			        }
		        }else{
		        	//没有学习规划或者没有排课的课程,各项数据统一显示为0,给到特殊字段,标记
		        	rate.setProgressRate(0);
		        	rate.setParticipationRate(0);
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
			rate.setProgressRate(0);
        	rate.setParticipationRate(0);
		}
		return rate;
	}

	@Override
	public RatePOJO getRecordRate(Long orderId, String businessId, Long userId) {
		RatePOJO rate = new RatePOJO();
		rate.setProgressRate(0);
		DecimalFormat df = new DecimalFormat("#");
		try{
			MallOrderEntity order = mallOrderDao.queryOrder(orderId, businessId);
			if(null != order){
				//录播课:通过订单获取商品id和省份id,再从商品商品明细获取课程id列表
				List<MallGoodsDetailsEntity> goodsDeatails = mallGoodsDetailsDao.queryCourseByCommodityId(order.getCommodityId(), order.getAreaId(), 0, null);
	        	if(null != goodsDeatails && goodsDeatails.size() > 0){
		        	List<Long> totalRecordList = new ArrayList<>();
		        	for(MallGoodsDetailsEntity goodsDeatail : goodsDeatails){
			        	List<Long> recordList = courseRecordDetailDao.queryIdListByCourseId(goodsDeatail.getCourseId());
			        	totalRecordList.addAll(recordList);
		        	}
		        	if(totalRecordList.size() > 0){
		        		Integer attendCount = logWatchRecordDao.queryAttendCount(userId, totalRecordList, 1);
		        		if(null == attendCount || attendCount ==0){
		        			rate.setProgressRate(0);
		        		}else{
		        			Float progressRate = attendCount.floatValue() * 100 / totalRecordList.size();
		        			rate.setProgressRate(Integer.valueOf(df.format(progressRate)));
		        		}
			        }
		        }
			}
		}catch(Exception e){
			e.printStackTrace();
			rate.setProgressRate(0);
		}
		return rate;
	}

    @Override
    public CoursesPOJO queryObject(Long courseId) {
        return coursesDao.queryObject(courseId);
    }
}

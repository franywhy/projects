package com.school.service.impl;

import com.school.dao.*;
import com.school.entity.*;
import com.school.pojo.*;
import com.school.service.LiveCourseService;
import com.school.service.MyCourseService;
import com.school.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("liveCourseService")
public class LiveCourseServiceImpl implements LiveCourseService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("#{application['timeout.centerPlanLivesAttendPer']}")
    private Long planLivesAttendPerTimeout;

    @Resource
    private StringRedisTemplate mainRedis;

    private static final int TYPE_WEB = 1;
    private static final int TYPE_APP = 2;

    @Autowired
    private CourseUserplanDao courseUserPlanDao;

    @Autowired
    private CourseClassplanLivesDao courseClassPlanLivesDao;

    @Autowired
    private LogGenseeWatchDao logGenseeWatchDao;

    @Autowired
    private CourseUserplanDetailDao courseUserPlanDetailDao;

    @Autowired
    private CourseUserplanClassDetailDao courseUserPlanClassDetailDao;

    @Autowired
    private CourseClassplanDao courseClassplanDao;

    @Autowired
    private CourseMaterialDao courseMaterialDao;

    @Autowired
    private CourseMaterialTypeDao courseMaterialTypeDao;

    @Autowired
    private CourseMaterialDetailDao courseMaterialDetailDao;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    private MyCourseService myCourseService;

    private static String NOT_BUSINESS_DOMAIN = "";

    @Value("#{application['pom.not.business.domain']}")
    private void setCCReplay(String domain) {
        NOT_BUSINESS_DOMAIN = domain;
    }

    private DecimalFormat df = new DecimalFormat("#");

    /**
     * 根据userplanId获得排课
     */
    private List<CourseClassplanEntity> getClassplan(Long userplanId, String businessId) {
        CourseUserplanEntity userPlanEntity = courseUserPlanDao.query(userplanId, businessId);
        if (null == userPlanEntity) {
            return null;
        }

        Map<String, Object> userPlanDetailParameter = new HashMap<String, Object>();
        userPlanDetailParameter.put("userPlanId", userPlanEntity.getUserPlanId());
        userPlanDetailParameter.put("status", 1);
        userPlanDetailParameter.put("dr", 0);
        Object tmp1 = userPlanEntity.getIsRep() == 1 ? userPlanDetailParameter.put("isSubstituted", 0) : null;
        Object tmp2 = userPlanEntity.getIsRep() == 0 ? userPlanDetailParameter.put("isSubstitute", 0) : null;
        Object tmp3 = userPlanEntity.getIsMatch() == 0 ? userPlanDetailParameter.put("isSuitable", 0) : null;
        List<CourseUserplanDetailEntity> fullUserPlanDetailList = courseUserPlanDetailDao.queryList(userPlanDetailParameter);
        List<CourseClassplanEntity> classplanList = new ArrayList<>();
        for (CourseUserplanDetailEntity entity : fullUserPlanDetailList) {
            String classplanId = courseUserPlanClassDetailDao.queryClassPlanId(entity.getUserplanDetailId(), 1, 0);
            if (null != classplanId) {
                CourseClassplanEntity classplanEntity = courseClassplanDao.queryClassplanByClassplanId(classplanId, 1, 1, 0, 1);
                if (null != classplanEntity)
                    classplanList.add(classplanEntity);
            }
        }
        return classplanList;
    }

    /**
     * 获取某天直播课程详细
     */
    @Override
    public List<ClassplanPOJO> getLiveDetail(Long userId, Long userplanId, String businessId, Date date, ClientEnum client) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            //List<CourseClassplanEntity> classplanList = courseClassPlanDao.queryClassplanByUserplanId(userplanId, 0, 1);
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<ClassplanPOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
                    ClassplanPOJO pojo = new ClassplanPOJO();
                    pojo.setClassplanId(entity.getClassplanId());
                    pojo.setClassplanName(entity.getClassplanName());
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    userPlanClassLivesParameter.put("dateStart", DateUtils.getDayStart(date));
                    userPlanClassLivesParameter.put("dateEnd", DateUtils.getDayEnd(date));
                    List<Map<String, Object>> liveList = courseClassPlanLivesDao.queryByClassplanId(userPlanClassLivesParameter);

                    if (null != liveList && liveList.size() > 0) {
                        //根据不同的请求端,数据内容稍有不同
                        if (client == ClientEnum.WEB) {
                            pojo.setList(getLives4WEB(liveList, userId));
                        } else if (client == ClientEnum.APP) {
                            pojo.setList(getLives4APP(liveList, userId));
                        }
                        pojoList.add(pojo);
                    }
                }
            } else {

            }
            return pojoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加工web端直播课程详细数据
     *
     * @param liveList
     * @return
     */
    private List<ClassplanLivesPOJO> getLives4WEB(List<Map<String, Object>> liveList, Long userId) {
        List<ClassplanLivesPOJO> livesPOJO = new ArrayList();
        for (int i = 0; i < liveList.size(); i++) {
            Map<String, Object> live = liveList.get(i);
            ClassplanLivesPOJO livePOJO = new ClassplanLivesPOJO();

            String classplanLiveId = (String) live.get("classplanLiveId");
            livePOJO.setClassplanLiveId(classplanLiveId);
            livePOJO.setClassplanLiveName((String) live.get("classplanLiveName"));
            livePOJO.setTeacher((String) live.get("teacherName"));
            Date startTime = (Date) live.get("startTime");
            Date endTime = (Date) live.get("endTime");
            livePOJO.setTime(DateUtils.format(startTime, DateUtils.HOUR_MINUTE_PATTERN) +
                    "~" +
                    DateUtils.format(endTime, DateUtils.HOUR_MINUTE_PATTERN));

            livePOJO.setClassStatus(getClassStatus(live));
            this.setPlanLiveAttendper(userId,livePOJO,classplanLiveId,TYPE_WEB);
            livesPOJO.add(livePOJO);
        }
        return livesPOJO;
    }

    /**
     * 加工移动端直播课程详细数据
     *
     * @param liveList
     * @return
     */
    private List<ClassplanLivesPOJO> getLives4APP(List<Map<String, Object>> liveList, Long userId) {
        List<ClassplanLivesPOJO> livesPOJO = new ArrayList();

        for (int i = 0; i < liveList.size(); i++) {
            Map<String, Object> live = liveList.get(i);
            ClassplanLivesPOJO livePOJO = new ClassplanLivesPOJO();
            String classplanLiveId = (String) live.get("classplanLiveId");
            livePOJO.setClassplanLiveId(classplanLiveId);
            livePOJO.setClassplanLiveName((String) live.get("classplanLiveName"));
            livePOJO.setTeacher("讲师 " + live.get("teacherName"));
            Date startTime = (Date) live.get("startTime");
            Date endTime = (Date) live.get("endTime");
            Date readyTime = (Date) live.get("readyTime");
            Date closeTime = (Date) live.get("closeTime");
            livePOJO.setTime("开始时间  " +
                    DateUtils.format(startTime, DateUtils.HOUR_MINUTE_PATTERN) +
                    "-" +
                    DateUtils.format(endTime, DateUtils.HOUR_MINUTE_PATTERN));
            livePOJO.setClassStatus(getClassStatus(live));
            this.setPlanLiveAttendper(userId,livePOJO,classplanLiveId,TYPE_APP);
            //app4.0增加的字段
            //上期复习字段
            String reviewUrl = (String) live.get("reviewUrl");
            if (StringUtils.isNotBlank(reviewUrl)) {
                String[] reviewUrlArr = reviewUrl.split(",");
                livePOJO.setReviewUrl(reviewUrlArr[0]);
            } else {
                livePOJO.setReviewUrl(reviewUrl);
            }
            //本期预习字段
            String prepareUrl = (String) live.get("prepareUrl");
            if (StringUtils.isNotBlank(prepareUrl)) {
                String[] prepareUrlArr = prepareUrl.split(",");
                livePOJO.setPrepareUrl(prepareUrlArr[0]);
            } else {
                livePOJO.setPrepareUrl(prepareUrl);
            }
            //课堂资料
            String coursewareUrl = (String) live.get("coursewareUrl");
            if (StringUtils.isNotBlank(coursewareUrl)) {
                String[] coursewareUrlArr = coursewareUrl.split(",");
                livePOJO.setCoursewareUrl(coursewareUrlArr[0]);
            } else {
                livePOJO.setCoursewareUrl(coursewareUrl);
            }

            //livePOJO.setHomeworkUrl((String) live.get("homeworkUrl"));
            //livePOJO.setPracticeStageId((String) live.get("practiceStageId"));
            livePOJO.setPhaseId(StringUtils.isBlank((String) live.get("examStageId")) ? 0L : Long.valueOf(live.get("examStageId").toString()));
            livePOJO.setFileUrl((String) live.get("fileUrl"));
            livePOJO.setTimestamp(((Date) live.get("dayTime")).getTime());
            Long courseId = Long.valueOf(live.get("courseId").toString());
            CoursesPOJO course = myCourseService.queryObject(courseId);
            livePOJO.setCourseFk(course.getCourseFk() == null ? 0L : course.getCourseFk());
            //从服务器上获取随机图片
            livePOJO.setPic(RandomUtils.getFileUrl());
            //livePOJO.setPic("http://file.hqjy.com/file/singleDirectDownload/qhs4NDXy3xEDAm-1p37pQiMAAAAAAAAAAQ.png");
            livePOJO.setCourseTime(DateUtils.format(startTime, DateUtils.HOUR_MINUTE_PATTERN) +
                    "-" +
                    DateUtils.format(endTime, DateUtils.HOUR_MINUTE_PATTERN));
           /* if (livePOJO.getClassStatus() == 1 ){
                livePOJO.setReadyTime(startTime.getTime() - new Date().getTime());
            }*/
            livePOJO.setStartTimeStamp(startTime.getTime());
            livePOJO.setEndTimeStamp(endTime.getTime());
            livePOJO.setReadyTime(readyTime.getTime());
            livePOJO.setCloseTime(closeTime.getTime());
            livesPOJO.add(livePOJO);
        }
        return livesPOJO;
    }

    @Override
    public List<CourseCalendarPOJO> getLiveCalendar(Long userId, Long userplanId, String businessId, String startDate, String endDate) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            //List<CourseClassplanEntity> classplanList = courseClassPlanDao.queryClassplanByUserplanId(userplanId, 0, 1);
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<CourseCalendarPOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    userPlanClassLivesParameter.put("dateStart", startDate);
                    userPlanClassLivesParameter.put("dateEnd", endDate);
                    List<CourseClassplanLivesEntity> liveList = courseClassPlanLivesDao.queryEntityByClassplanId(userPlanClassLivesParameter);
                    if (null != liveList && liveList.size() > 0) {
                        CourseCalendarPOJO pojo = null;
                        //遍历新获得的排课明细,与已获得的排课明细,若同一天有多节课,则update,否则为新增项
                        for (CourseClassplanLivesEntity live : liveList) {
                            //标记是否新增项
                            boolean isNew = true;
                            //获取是否出勤
                            List<String> classplanLiveIdList = new ArrayList();
                            classplanLiveIdList.add(live.getClassplanLiveId());
                            //暂时去掉不算
                            /*List<String> attend = logWatchDao.getBusinessIdList(userId, classplanLiveIdList, 1);*/
                            Integer isAttend = 0;//attend.size() > 0 ? 1 : 0;//计算出勤,attend有数据代表已出勤
                            Date liveDate = DateUtils.parse(DateUtils.format(live.getStartTime()));
                            if (null != liveDate) {

                                for (CourseCalendarPOJO mainPOJO : pojoList) {

                                    if (DateUtils.format(liveDate).equals(mainPOJO.getDate())) {
                                        //若已有项为已出勤,则根据新新查得数据update,原则为:有一个未出勤,即为未出勤
                                        if (mainPOJO.getIsAttend() == 1) mainPOJO.setIsAttend(isAttend);
                                        isNew = false;
                                        break;
                                    }
                                }
                                if (isNew) {
                                    pojo = new CourseCalendarPOJO();
                                    pojo.setDate(DateUtils.format(liveDate));
                                    pojo.setIsAttend(isAttend);
                                    pojo.setUserplanId(userplanId);
                                    pojo.setStatus(1);
                                    pojoList.add(pojo);
                                }
                            }
                        }
                    }
                }
            }
            return pojoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ClassplanPOJO> getLiveSchedule(Long userId, Long userplanId, String businessId, ClientEnum client) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            //List<CourseClassplanEntity> classplanList = courseClassPlanDao.queryClassplanByUserplanId(userplanId, 0, 1);
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<ClassplanPOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
                    ClassplanPOJO pojo = new ClassplanPOJO();
                    pojo.setClassplanId(entity.getClassplanId());
                    pojo.setClassplanName(entity.getClassplanName());
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    List<Map<String, Object>> liveList = courseClassPlanLivesDao.queryByClassplanId(userPlanClassLivesParameter);

                    if (null != liveList && liveList.size() > 0) {
                        //根据不同的请求端,数据内容稍有不同
                        if (client == ClientEnum.WEB) {
                            pojo.setList(getSchedule4WEB(liveList, userId));
                        } else if (client == ClientEnum.APP) {
                            pojo.setList(getSchedule4APP(liveList, userId));
                        }
                        pojoList.add(pojo);
                    }
                }
            }
            return pojoList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加工web端直播课程表数据
     *
     * @param liveList
     * @return
     */
    private List<ClassplanLivesPOJO> getSchedule4WEB(List<Map<String, Object>> liveList, Long userId) {
        List<ClassplanLivesPOJO> livesPOJO = new ArrayList();
        for (int i = 0; i < liveList.size(); i++) {
            Map<String, Object> live = liveList.get(i);
            ClassplanLivesPOJO livePOJO = new ClassplanLivesPOJO();

            livePOJO.setClassplanLiveId((String) live.get("classplanLiveId"));
            livePOJO.setClassplanLiveName((String) live.get("classplanLiveName"));
            livePOJO.setTeacher((String) live.get("teacherName"));
            livePOJO.setTimestamp(((Date) live.get("dayTime")).getTime());
            Date startTime = (Date) live.get("startTime");
            Date endTime = (Date) live.get("endTime");
            livePOJO.setTime(DateUtils.format(startTime, DateUtils.DATE_HOUR_MINUTE_PATTERN) + "-" +
                    DateUtils.format(endTime, DateUtils.HOUR_MINUTE_PATTERN));

            livePOJO.setClassStatus(getClassStatus(live));
            //获取出勤状态
            List<String> classplanLiveIdList = new ArrayList();
            String classplanLiveId = (String) live.get("classplanLiveId");
            classplanLiveIdList.add(classplanLiveId);
            this.setPlanLiveAttendper(userId, livePOJO, classplanLiveId, TYPE_WEB);
            livesPOJO.add(livePOJO);
        }
        return livesPOJO;
    }

    //type 1:web 2:app
    private void setPlanLiveAttendper(Long userId, ClassplanLivesPOJO livePOJO, String classplanLiveId,int type) {
        String redisKey = Constants.CENTER_PLANLIVES_ATTENDPER+userId+":"+classplanLiveId;
        String liveAttendPerStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(liveAttendPerStr)){
            if (type == TYPE_WEB){
                livePOJO.setAttendPer("null".equals(liveAttendPerStr) ? "0" : liveAttendPerStr);
            }else {
                livePOJO.setAttendPer("null".equals(liveAttendPerStr) ? null : liveAttendPerStr+"%");
            }
            logger.info("直播课详情,从缓存中获取直播课次考勤attendPer,userId={},classplanLiveId={},attendPer={}",userId,classplanLiveId,liveAttendPerStr);
        }else {
            Float attendPer = logGenseeWatchDao.queryAttendPer(userId, classplanLiveId);
            String attendPerStr = attendPer == null ? "null" :df.format(attendPer * 100);
            if (type == TYPE_WEB){
                livePOJO.setAttendPer(attendPer == null ? "0" :df.format(attendPer * 100));
            }else {
                livePOJO.setAttendPer(attendPer == null ? null :df.format(attendPer * 100)+ "%");
            }
            mainRedis.opsForValue().set(redisKey,attendPerStr);
            mainRedis.expire(redisKey,planLivesAttendPerTimeout, TimeUnit.MINUTES);
            logger.info("直播课详情,从数据库获取直播课次考勤attendPer,userId={},classplanLiveId={},attendPer={}",userId,classplanLiveId,attendPer);
        }
    }

    /**
     * 加工移动端直播课程表数据
     *
     * @param liveList
     * @return
     */
    private List<ClassplanLivesPOJO> getSchedule4APP(List<Map<String, Object>> liveList, Long userId) {
        List<ClassplanLivesPOJO> livesPOJO = new ArrayList();
        for (int i = 0; i < liveList.size(); i++) {
            Map<String, Object> live = liveList.get(i);
            ClassplanLivesPOJO livePOJO = new ClassplanLivesPOJO();

            livePOJO.setClassplanLiveId((String) live.get("classplanLiveId"));
            livePOJO.setClassplanLiveName((String) live.get("classplanLiveName"));
            livePOJO.setTeacher("讲师 " + live.get("teacherName"));
            livePOJO.setTimestamp(((Date) live.get("dayTime")).getTime());
            livePOJO.setTime("时间:" + live.get("classLiveTimeDetail"));

            livePOJO.setClassStatus(getClassStatus(live));
            //获取出勤状态
            List<String> classplanLiveIdList = new ArrayList();
            String classplanLiveId = (String) live.get("classplanLiveId");
            classplanLiveIdList.add(classplanLiveId);
            this.setPlanLiveAttendper(userId, livePOJO, classplanLiveId, TYPE_APP);
            livesPOJO.add(livePOJO);
        }
        return livesPOJO;
    }

    private Integer getClassStatus(Map<String, Object> live) {
        Date startTime = (Date) live.get("startTime");
        Date endTime = (Date) live.get("endTime");
        Date readyTime = (Date) live.get("readyTime");
        Date closeTime = (Date) live.get("closeTime");
        String backUrl = (String) live.get("backUrl");
        Date now = new Date();

        Long compaReadyTime = readyTime != null ? readyTime.getTime() : startTime.getTime() - 1800000;
        Long compaCloseTime = closeTime != null ? closeTime.getTime() : endTime.getTime();
        if (now.getTime() < compaReadyTime) {
            return 0;
        } else if (now.getTime() < startTime.getTime()) {
            return 1;
        } else if (now.getTime() > startTime.getTime() && now.getTime() < compaCloseTime) {
            return 2;
        } else if (now.getTime() > compaCloseTime) {
            if (null == backUrl || backUrl.equals("")) {
                return 3;
            } else {
                return 4;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Map<String, Object> getLiveHear(Long userplanId, String businessId) {
        try {
            Date now = new Date();
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            if (null != classplanList && classplanList.size() > 0) {
                CourseClassplanLivesEntity tempLive = null;
                for (CourseClassplanEntity classplanEntity : classplanList) {
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(classplanEntity.getClassplanId(), userplanEntity.getClassTypeId());
                    List<CourseClassplanLivesEntity> liveList = courseClassPlanLivesDao.queryEntityByClassplanId(userPlanClassLivesParameter);

                    if (null != liveList && liveList.size() > 0) {
                        for (CourseClassplanLivesEntity liveEntity : liveList) {
                            if (null != liveEntity) {
                                Long compaCloseTime = liveEntity.getCloseTime() != null ? liveEntity.getCloseTime().getTime() : liveEntity.getEndTime().getTime();
                                if (compaCloseTime < now.getTime()) continue;

                                if (null == tempLive)
                                    tempLive = liveEntity;

                                if (tempLive.getStartTime().getTime() > liveEntity.getStartTime().getTime())
                                    tempLive = liveEntity;
                            }
                        }
                    }
                }

                Map<String, Object> result = new HashMap<>();
                if (null != tempLive) {
                    CourseClassplanEntity tempClassplan = null;
                    for (CourseClassplanEntity classplan : classplanList) {
                        if (classplan.getClassplanId().equals(tempLive.getClassplanId())) tempClassplan = classplan;
                    }
                    if (tempLive.getReadyTime().getTime() <= now.getTime()) {

                        result.put("courseStatus", "正在直播:" + tempClassplan.getClassplanName() + "-" + tempLive.getClassplanLiveName());
                        result.put("courseDuration", "上课时间:" + tempLive.getClassplanLiveTimeDetail());
                    } else {
                        result.put("courseStatus", "即将开始:" + tempClassplan.getClassplanName() + "-" + tempLive.getClassplanLiveName());
                        result.put("courseDuration", "上课时间:" + tempLive.getClassplanLiveTimeDetail());
                    }
                } else {
                    result.put("courseStatus", "恭喜你，所有课程学完了，生活不会辜负你的每一份努力！");
                    result.put("courseDuration", "");
                }
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("courseStatus", "暂无");
                result.put("courseDuration", "暂无");
                return result;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> generateUserPlanClassLivesParam(String classPlanId, Long classTypeId) {
        Map<String, Object> userPlanClassLivesParameter = new HashMap<String, Object>();
        userPlanClassLivesParameter.put("classplanId", classPlanId);
        userPlanClassLivesParameter.put("isAudited", 1);
        userPlanClassLivesParameter.put("isOpen", 1);
        userPlanClassLivesParameter.put("dr", 0);
        userPlanClassLivesParameter.put("status", 1);
        StringBuilder sb = new StringBuilder();
        sb.append("%,").append(classTypeId).append(",%");
        userPlanClassLivesParameter.put("typeIds", sb.toString());
        userPlanClassLivesParameter.put("startTime", new Date(System.currentTimeMillis()));
        return userPlanClassLivesParameter;
    }

    /**
     * 获取某天 你希望  WEB端
     */
    @Override
    public List<HopePOJO> getLiveHopeForWeb(Long userplanId, String businessId, Date date) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<HopePOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
					/*ClassplanPOJO pojo = new ClassplanPOJO();
					pojo.setClassplanId(entity.getClassplanId());
					pojo.setClassplanName(entity.getClassplanName());*/
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    userPlanClassLivesParameter.put("dateStart", DateUtils.getDayStart(date));
                    userPlanClassLivesParameter.put("dateEnd", DateUtils.getDayEnd(date));
                    List<CourseClassplanLivesEntity> liveList = courseClassPlanLivesDao.queryEntityByClassplanId(userPlanClassLivesParameter);

                    //查询资料下载
                    if (null != liveList && liveList.size() > 0) {
                        SysBusinessEntity business = sysBusinessDao.queryByBusinessId(businessId);
                        String jsonSet = business.getJsonSetting();
                        Map<String, Object> iconMap = null;
                        if (null != jsonSet && !jsonSet.equals("")) {
                            Map<String, Object> businessJsonMap = JSONUtil.jsonToMap(jsonSet);
                            Map<String, Object> hopeMap = (Map<String, Object>) businessJsonMap.get("hope");
                            iconMap = (Map<String, Object>) hopeMap.get("icon");
                        }

                        for (CourseClassplanLivesEntity live : liveList) {
                            String fileUrl = live.getFileUrl();
                            String fileName = live.getFileName();
                            String reviewName = live.getReviewName();
                            String reviewUrl = live.getReviewUrl();
                            String prepareUrl = live.getPrepareUrl();
                            String prepareName = live.getPrepareName();
                            String coursewareUrl = live.getCoursewareUrl();
                            String coursewareName = live.getCoursewareName();
                            String homeworkUrl = live.getHomeworkUrl();
                            String homeworkName = live.getHomeworkName();
                            if (StringUtils.isNotBlank(fileUrl) || StringUtils.isNotBlank(homeworkUrl) ||
                                    StringUtils.isNotBlank(reviewUrl) || StringUtils.isNotBlank(prepareUrl) || StringUtils.isNotBlank(coursewareUrl)) {
                                HopePOJO pojo = new HopePOJO();
                                pojo.setType(0);
                                pojo.setTypeName("下载资料");
                                pojo.setName(live.getClassplanLiveName());
                                List<Map<String, Object>> filelist = new ArrayList<>();
                                //非自适应课程的课堂文件资料
                                if (StringUtils.isNotBlank(fileUrl)) {
                                    List<Map<String, Object>> fileDatalist = new ArrayList<>();
                                    HashMap<String, Object> fileUrlMap = new HashMap<String, Object>();
                                    fileUrlMap.put("name", "课堂资料");
                                    fileUrlMap.put("num", fileUrl.split(",").length);
                                    fileUrlMap.put("fileType", 4);
                                    String[] firlUrlArr = fileUrl.split(",");
                                    //判断文件名是否为空
                                    if (StringUtils.isNotBlank(fileName)) {
                                        String[] fileNameArr = fileName.split(",");
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", fileNameArr[i]);
                                            fileDatalist.add(fileData);
                                        }
                                    } else {
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", live.getClassplanLiveName());
                                            fileDatalist.add(fileData);
                                        }
                                    }
                                    fileUrlMap.put("fileData", fileDatalist);
                                    filelist.add(fileUrlMap);
                                }
                                //上期复习的资料
                                if (StringUtils.isNotBlank(reviewName) && StringUtils.isNotBlank(reviewUrl)) {
                                    List<Map<String, Object>> fileDatalist = new ArrayList<>();
                                    HashMap<String, Object> fileUrlMap = new HashMap<String, Object>();
                                    fileUrlMap.put("name", "上期复习");
                                    fileUrlMap.put("num", reviewUrl.split(",").length);
                                    fileUrlMap.put("fileType", 1);
                                    String[] firlUrlArr = reviewUrl.split(",");
                                    if (StringUtils.isNotBlank(reviewName)) {
                                        String[] fileNameArr = reviewName.split(",");
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", fileNameArr[i]);
                                            fileDatalist.add(fileData);
                                        }
                                    } else {
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", live.getClassplanLiveName());
                                            fileDatalist.add(fileData);
                                        }
                                    }
                                    fileUrlMap.put("fileData", fileDatalist);
                                    filelist.add(fileUrlMap);
                                }

                                //本期预习的资料
                                if (StringUtils.isNotBlank(prepareUrl) && StringUtils.isNotBlank(prepareName)) {
                                    List<Map<String, Object>> fileDatalist = new ArrayList<>();
                                    HashMap<String, Object> fileUrlMap = new HashMap<String, Object>();
                                    fileUrlMap.put("name", "本次预习");
                                    fileUrlMap.put("num", prepareUrl.split(",").length);
                                    fileUrlMap.put("fileType", 2);
                                    String[] firlUrlArr = prepareUrl.split(",");
                                    if (StringUtils.isNotBlank(prepareName)) {
                                        String[] fileNameArr = prepareName.split(",");
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", fileNameArr[i]);
                                            fileDatalist.add(fileData);
                                        }
                                    } else {
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", live.getClassplanLiveName());
                                            fileDatalist.add(fileData);
                                        }
                                    }
                                    fileUrlMap.put("fileData", fileDatalist);
                                    filelist.add(fileUrlMap);
                                }

                                //课堂作业的资料
                                if (StringUtils.isNotBlank(coursewareUrl) && StringUtils.isNotBlank(coursewareName)) {
                                    List<Map<String, Object>> fileDatalist = new ArrayList<>();
                                    HashMap<String, Object> fileUrlMap = new HashMap<String, Object>();
                                    fileUrlMap.put("name", "课堂资料");
                                    fileUrlMap.put("num", coursewareUrl.split(",").length);
                                    fileUrlMap.put("fileType", 3);
                                    String[] firlUrlArr = coursewareUrl.split(",");
                                    if (StringUtils.isNotBlank(coursewareName)) {
                                        String[] fileNameArr = coursewareName.split(",");
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", fileNameArr[i]);
                                            fileDatalist.add(fileData);
                                        }
                                    } else {
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", live.getClassplanLiveName());
                                            fileDatalist.add(fileData);
                                        }
                                    }
                                    fileUrlMap.put("fileData", fileDatalist);
                                    filelist.add(fileUrlMap);
                                }

                                //获取课后作业
                                if (StringUtils.isNotBlank(homeworkUrl) && StringUtils.isNotBlank(homeworkName)) {
                                    List<Map<String, Object>> fileDatalist = new ArrayList<>();
                                    HashMap<String, Object> fileUrlMap = new HashMap<String, Object>();
                                    fileUrlMap.put("name", "课后作业");
                                    fileUrlMap.put("num", homeworkUrl.split(",").length);
                                    fileUrlMap.put("fileType", 5);
                                    String[] firlUrlArr = homeworkUrl.split(",");
                                    if (StringUtils.isNotBlank(homeworkName)){
                                        String[] fileNameArr = homeworkName.split(",");
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", fileNameArr[i]);
                                            fileDatalist.add(fileData);
                                        }
                                    }else{
                                        for (int i = 0; i < firlUrlArr.length; i++) {
                                            HashMap<String, Object> fileData = new HashMap<String, Object>(1);
                                            fileData.put("fileUrl", firlUrlArr[i]);
                                            fileData.put("fileName", live.getClassplanLiveName());
                                            fileDatalist.add(fileData);
                                        }
                                    }
                                    fileUrlMap.put("fileData", fileDatalist);
                                    filelist.add(fileUrlMap);
                                }
                                pojo.setWebData(filelist);
                                if (null != iconMap) {
                                    pojo.setIcon((String) iconMap.get("materialDownload"));
                                }
                                pojoList.add(pojo);
                            }
                        }
                        //查询资料预览 (押题宝)
                        if (null != entity.getMaterialId()) {
                            CourseMaterialEntity materialEntity = courseMaterialDao.queryByMaterialId(entity.getMaterialId(), businessId);
                            if (null != materialEntity) {
                                HashMap<String, Object> data = new HashMap<String, Object>();
                                List<Map<String, Object>> list = new ArrayList<>();
                                HopePOJO pojo = new HopePOJO();
                                pojo.setType(1);
                                pojo.setTypeName("资料预览");
                                pojo.setName(materialEntity.getMaterialName() + "  押题宝");
                                data.put(materialEntity.getMaterialName(), (materialEntity.getMaterialId()).toString());
                                list.add(data);
                                pojo.setWebData(list);
                                if (null != iconMap)
                                    pojo.setIcon((String) iconMap.get("materialPreview"));
                                pojoList.add(pojo);
                            }
                        }
                    }
                }
            }
            return pojoList;
        } catch (
                Exception e)

        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取某天 你希望 APP端
     */
    @Override
    public List<HopePOJO> getLiveHope(Long userplanId, String businessId, Date date) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<HopePOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
					/*ClassplanPOJO pojo = new ClassplanPOJO();
					pojo.setClassplanId(entity.getClassplanId());
					pojo.setClassplanName(entity.getClassplanName());*/
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    userPlanClassLivesParameter.put("dateStart", DateUtils.getDayStart(date));
                    userPlanClassLivesParameter.put("dateEnd", DateUtils.getDayEnd(date));
                    List<CourseClassplanLivesEntity> liveList = courseClassPlanLivesDao.queryEntityByClassplanId(userPlanClassLivesParameter);

                    //查询资料下载
                    if (null != liveList && liveList.size() > 0) {
                        SysBusinessEntity business = sysBusinessDao.queryByBusinessId(businessId);
                        String jsonSet = business.getJsonSetting();
                        Map<String, Object> iconMap = null;
                        if (null != jsonSet && !jsonSet.equals("")) {
                            Map<String, Object> businessJsonMap = JSONUtil.jsonToMap(jsonSet);
                            Map<String, Object> hopeMap = (Map<String, Object>) businessJsonMap.get("hope");
                            iconMap = (Map<String, Object>) hopeMap.get("icon");
                        }

                        for (CourseClassplanLivesEntity live : liveList) {
                            String fileUrl = (String) live.getFileUrl();
                            if (null != fileUrl && !fileUrl.equals("")) {
                                HopePOJO pojo = new HopePOJO();
                                pojo.setType(0);
                                pojo.setTypeName("下载资料");
                                pojo.setName((String) live.getClassplanLiveName());
                                String firstfileUrl = fileUrl.split(",")[0];
                                pojo.setData(firstfileUrl);
                                if (null != iconMap)
                                    pojo.setIcon((String) iconMap.get("materialDownload"));
                                pojoList.add(pojo);
                            }
                        }

                        //查询资料预览 (押题宝)
                        if (null != entity.getMaterialId()) {
                            CourseMaterialEntity materialEntity = courseMaterialDao.queryByMaterialId(entity.getMaterialId(), businessId);
                            if (null != materialEntity) {
                                HopePOJO pojo = new HopePOJO();
                                pojo.setType(1);
                                pojo.setTypeName("资料预览");
                                pojo.setName(materialEntity.getMaterialName() + "  押题宝");
                                pojo.setData((materialEntity.getMaterialId()).toString());
                                if (null != iconMap)
                                    pojo.setIcon((String) iconMap.get("materialPreview"));
                                pojoList.add(pojo);
                            }
                        }
                    }
                }
            }
            return pojoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public MaterialPOJO getMaterial(Long materialId, String businessId) {
        try {
            MaterialPOJO materialPOJO = new MaterialPOJO();

            CourseMaterialEntity material = courseMaterialDao.queryByMaterialId(materialId, businessId);
            if (null != material) {
                materialPOJO.setName(material.getMaterialName());
                List<CourseMaterialTypeEntity> typeList = courseMaterialTypeDao.queryByMaterialId(materialId, businessId);
                List<MaterialTypePOJO> typePOJOList = new ArrayList();
                if (null != typeList && typeList.size() > 0) {
                    for (CourseMaterialTypeEntity type : typeList) {
                        if (null != type) {
                            MaterialTypePOJO typePOJO = new MaterialTypePOJO();
                            typePOJO.setName(type.getMaterialTypeName());

                            List<CourseMaterialDetailEntity> detailList = courseMaterialDetailDao.queryByMaterialTypeId(type.getMaterialTypeId(), businessId);
                            List<MaterialDetailPOJO> detailPOJOList = new ArrayList();
                            if (null != detailList && detailList.size() > 0) {

                                //循环第三层结构
                                for (CourseMaterialDetailEntity detail : detailList) {
                                    if (null != detail) {
                                        MaterialDetailPOJO detailPOJO = new MaterialDetailPOJO();
                                        detailPOJO.setDetailId(detail.getDetailId());
                                        detailPOJO.setName(detail.getDetailName());

                                        detailPOJO.setUrl(NOT_BUSINESS_DOMAIN + "/material/preview?detailId=" + detail.getDetailId());
                                        detailPOJOList.add(detailPOJO);
                                    }
                                }
                            }
                            typePOJO.setDetail(detailPOJOList);
                            typePOJOList.add(typePOJO);
                        }
                    }
                }
                materialPOJO.setType(typePOJOList);
            }
            return materialPOJO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getMaterialContent(Long detailId) {
        try {
            String content = courseMaterialDetailDao.queryMaterialContent(detailId);
            if (null != content) return content;

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<ClassplanPOJO> getClassPlanSchedule(Long userId, Long userplanId, String businessId, ClientEnum
            client) {
        try {
            CourseUserplanEntity userplanEntity = courseUserPlanDao.query(userplanId, businessId);
            //根据学员规划Id查询排课列表
            //List<CourseClassplanEntity> classplanList = courseClassPlanDao.queryClassplanByUserplanId(userplanId, 0, 1);
            List<CourseClassplanEntity> classplanList = getClassplan(userplanId, businessId);

            List<ClassplanPOJO> pojoList = new ArrayList();
            if (null != classplanList && classplanList.size() > 0) {

                for (CourseClassplanEntity entity : classplanList) {
                    //根据排课id获得排课明细
                    Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(entity.getClassplanId(), userplanEntity.getClassTypeId());
                    List<Map<String, Object>> liveList = courseClassPlanLivesDao.queryByClassplanId(userPlanClassLivesParameter);
                    if (null != liveList && liveList.size() > 0) {
                        ClassplanPOJO pojo = new ClassplanPOJO();
                        pojo.setClassplanId(entity.getClassplanId());
                        pojo.setClassplanName(entity.getClassplanName());
                        pojo.setClassTypeId(userplanEntity.getClassTypeId());
                        pojoList.add(pojo);
                    }
                }
            }
            return pojoList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ClassplanLivesPOJO> getClassPlanDetailSchedule(Long userId, String classplanId, Long
            classtypeId, String businessId, ClientEnum client) {
        try {
            Map<String, Object> userPlanClassLivesParameter = generateUserPlanClassLivesParam(classplanId, classtypeId);
            List<Map<String, Object>> liveList = courseClassPlanLivesDao.queryByClassplanId(userPlanClassLivesParameter);
            if (null != liveList && liveList.size() > 0) {
                //根据不同的请求端,数据内容稍有不同
                if (client == ClientEnum.WEB) {
                    return getSchedule4WEB(liveList, userId);
                } else if (client == ClientEnum.APP) {
                    return getSchedule4APP(liveList, userId);
                }
            }
            return null;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
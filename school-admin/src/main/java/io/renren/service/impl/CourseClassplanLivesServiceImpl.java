package io.renren.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.renren.entity.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import io.renren.dao.CourseClassplanLivesDao;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.CourseClassplanService;
import io.renren.service.MallLiveRoomService;
import io.renren.service.SysProductService;
import io.renren.utils.ClassTypeUtils;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.R;
import io.renren.utils.UUIDUtils;
import io.renren.utils.http.HttpClientUtil4_3;



@Service("courseClassplanLivesService")
public class CourseClassplanLivesServiceImpl implements CourseClassplanLivesService {
    @Autowired
    private CourseClassplanLivesDao courseClassplanLivesDao;

    @Autowired
    private MallLiveRoomService mallLiveRoomService;
    @Autowired
    private CourseClassplanService courseClassplanService;
    @Autowired
    private SysProductService sysProductService;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    private static String replayUrl = "";
    @Value("#{application['pom.cc.vod.url']}")
    private void setReplayUrl(String replayUrl){
        this.replayUrl = replayUrl;
    }

    @Override
    public CourseClassplanLivesEntity queryObject(Map<String, Object> map){
        return courseClassplanLivesDao.queryObject(map);
    }

    @Override
    public List<CourseClassplanLivesEntity> queryList(Map<String, Object> map){
        return courseClassplanLivesDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map){
        return courseClassplanLivesDao.queryTotal(map);
    }

    @Override
    public void save(CourseClassplanLivesEntity courseClassplanLives){
        //dr
        courseClassplanLives.setDr(0);
        //生成ID
        courseClassplanLives.setClassplanLiveId(UUIDUtils.formatter());
//		courseClassplanLives.setClassplanLiveId(UUID.randomUUID().toString());
        //子表创建时间
        courseClassplanLives.setCreationTime(new Date());
        //子表修改时间
        courseClassplanLives.setModifiedTime(courseClassplanLives.getCreationTime());
        if(null != courseClassplanLives){
            courseClassplanLives.setLiveClassTypeIds(ClassTypeUtils.in(courseClassplanLives.getLiveClassTypeIds()));
        }
        courseClassplanLives.setClassplanLiveTimeDetail(classplanLiveTimeDetailFormate(courseClassplanLives.getStartTime(), courseClassplanLives.getEndTime()));
        //设置回放id和回放类型
        setBackIdAndBackType(courseClassplanLives);
        courseClassplanLivesDao.save(courseClassplanLives);
    }

    public static String classplanLiveTimeDetailFormate(Date sdate , Date eDate){
        String formate = "[开始时间]-[结束时间]";
        String startFormate = "yyyy-MM-dd HH:mm" ;
        String endFormate = "HH:mm" ;

        return formate.replace("[开始时间]", DateUtils.format(sdate , startFormate)).replace("[结束时间]", DateUtils.format(eDate , endFormate));
    }

    @Override
    public void update(CourseClassplanLivesEntity courseClassplanLives){
        //子表更新时间
        courseClassplanLives.setModifiedTime(new Date());
        if(null != courseClassplanLives){
            courseClassplanLives.setLiveClassTypeIds(ClassTypeUtils.in(courseClassplanLives.getLiveClassTypeIds()));
        }
        courseClassplanLives.setClassplanLiveTimeDetail(classplanLiveTimeDetailFormate(courseClassplanLives.getStartTime(), courseClassplanLives.getEndTime()));
        //设置回放id和回放类型
        setBackIdAndBackType(courseClassplanLives);
        courseClassplanLivesDao.update(courseClassplanLives);
    }

    @Override
    public void delete(Long classplanLiveId){
        courseClassplanLivesDao.delete(classplanLiveId);
    }

    @Override
    public void deleteBatch(Map<String, Object> map){
        courseClassplanLivesDao.deleteBatch(map);
    }
    @Override
    public void pause(Long[] classplanLiveIds){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", classplanLiveIds);
        map.put("status", Constant.Status.PAUSE.getValue());
        courseClassplanLivesDao.updateBatch(map);
    }

    @Override
    public void resume(Long[] classplanLiveIds){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", classplanLiveIds);
        map.put("status", Constant.Status.RESUME.getValue());
        courseClassplanLivesDao.updateBatch(map);
    }

    /**
     * 查询子表
     * @param classplanId	排课计划ID
     * @param school_id	校区ID
     */
    @Override
    public List<ClassplanLivePOJO> queryPojoList(Map<String, Object> map) {
        List<ClassplanLivePOJO> classplanLivePOJOs = courseClassplanLivesDao.queryPojoList(map);
        if(null != classplanLivePOJOs && classplanLivePOJOs.size() >0){
            for (ClassplanLivePOJO classplanLivePOJO : classplanLivePOJOs) {
                classplanLivePOJO.setLiveClassTypeIds(ClassTypeUtils.out(classplanLivePOJO.getLiveClassTypeIds()));
            }
        }
        return classplanLivePOJOs;
    }

    /**
     * 删除ID不等于classplanLiveIds的数据
     * @param classplanLiveIds = id数组
     * @param classplanId = 排课计划PK
     */
    @Override
    public void deleteBatchNotIn(Map<String, Object> map) {
        courseClassplanLivesDao.deleteBatchNotIn(map);
    }
    /**
     * 授课老师校验
     */
    @Override
    public Map<String, Object> checkTeacher(CourseClassplanLivesEntity courseClassplanLives) {
        return this.courseClassplanLivesDao.checkTeacher(courseClassplanLives);
    }
    /**
     * 直播室校验
     */
    @Override
    public Map<String, Object> checkStudio(CourseClassplanLivesEntity courseClassplanLives) {
        return this.courseClassplanLivesDao.checkStudio(courseClassplanLives);
    }
    /**
     * 直播间校验
     */
    @Override
    public Map<String, Object> checkLiveRoom(CourseClassplanLivesEntity courseClassplanLives) {
        return this.courseClassplanLivesDao.checkLiveRoom(courseClassplanLives);
    }


    @Override
    public Map<String, Object> checkCourseLive(CourseClassplanLivesEntity courseClassplanLives) {
        // TODO Auto-generated method stub
        return this.courseClassplanLivesDao.checkCourseLive(courseClassplanLives);
    }


    @Override
    public int checkClassType(long id) {
        return this.courseClassplanLivesDao.checkClassType(id);
    }

    @Override
    public List<String> getCourseLives(Long courseId, String schoolId) {
        return this.courseClassplanLivesDao.getCourseLives(courseId, schoolId);
    }

    @Override
    public Map<String, Object> checkOneCommodityCourseLive(CourseClassplanLivesEntity courseClassplanLives) {
        return this.courseClassplanLivesDao.checkOneCommodityCourseLive(courseClassplanLives);
    }


    @Override
    public List<Map<String, Object>> queryByDate(Map<String, Object> map) {

        return this.courseClassplanLivesDao.queryByDate(map);
    }


    /**
     * 设置回放id和回放类型
     * @param courseClassplanLives
     */
    private void setBackIdAndBackType(CourseClassplanLivesEntity courseClassplanLives){
        String backUrl = courseClassplanLives.getBackUrl().trim();
        if(StringUtils.isNotBlank(backUrl)){
            if(backUrl.startsWith("http") && backUrl.indexOf("play-") > 0){
                String[] backUrls = backUrl.split("play-");
                courseClassplanLives.setBackId(backUrls[1]);
                courseClassplanLives.setBackType(1);//展示互动平台
            }
            if(!backUrl.startsWith("http")){
                courseClassplanLives.setBackId(null);
                courseClassplanLives.setBackType(0);//cc平台
            }
        }else{
            courseClassplanLives.setBackId(null);
            courseClassplanLives.setBackType(null);
        }
    }

    @Override
    public List<ClassplanLivePOJO> queryPojoList1(Map<String, Object> map) {
        List<ClassplanLivePOJO> classplanLivePOJOs = courseClassplanLivesDao.queryPojoList1(map);
        if(null != classplanLivePOJOs && classplanLivePOJOs.size() >0){
            for (ClassplanLivePOJO classplanLivePOJO : classplanLivePOJOs) {
                classplanLivePOJO.setLiveClassTypeIds(ClassTypeUtils.out(classplanLivePOJO.getLiveClassTypeIds()));
            }
        }
        return classplanLivePOJOs;
    }

    @Override
    public ClassplanLivePOJO queryObject1(Map<String, Object> map) {
        ClassplanLivePOJO classplanLivePOJO = courseClassplanLivesDao.queryObject1(map);
        classplanLivePOJO.setLiveClassTypeIds(ClassTypeUtils.out(classplanLivePOJO.getLiveClassTypeIds()));
        return classplanLivePOJO;
    }

    @Override
    public int findMid(String Mid) {
        int count= courseClassplanLivesDao.findMid(Mid);
        return count;
    }

    @Override
    public void updateBackUrl(Map<String, Object> map) {
        courseClassplanLivesDao.updateBackUrl(map);
    }

    @Override
    public void updateBackUrlByClassplanliveId(Map<String, Object> map) {
        courseClassplanLivesDao.updateBackUrlByClassplanliveId(map);
    }

    @Override
    public Map<String, Object> queryByBackId(Map<String, Object> map) {
        return this.courseClassplanLivesDao.queryByBackId(map);
    }

    @Override
    public String findClassplanliveId(String mId) {
        return this.courseClassplanLivesDao.findClassplanliveId(mId);
    }

    @Override
    public CourseClassplanLivesEntity queryByClassPlanLiveId(Map<String, Object> map) {
        return courseClassplanLivesDao.queryByClassPlanLiveId(map);
    }

    @Override
    public String queryReplayUrl(Map<String, Object> map, Map<String, Object> parameters) {

        CourseClassplanLivesEntity courseClassplanLives = courseClassplanLivesDao.queryByClassPlanLiveId(map);
        if(null != courseClassplanLives){
            String backUrl = courseClassplanLives.getBackUrl();
            if(null == backUrl || backUrl.equals("")){
                return "";
            }else{
                if(backUrl.indexOf("http:") != -1){
                    return backUrl + spliceParameter(parameters);
                }else{
                    return replayUrl + backUrl;
                }
            }
        }else{
            return "";
        }
    }

    private static String spliceParameter(Map<String, Object> parameters){
        StringBuilder parameterStr = new StringBuilder();
        parameterStr.append("?");
        for (Map.Entry<String, Object> entry:parameters.entrySet()) {
            parameterStr.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return  parameterStr.deleteCharAt(parameterStr.length()-1).toString();
    }
    @Override
    public List<CourseClassplanLivesEntity> queryCourseClassplanLivesByDate(Map<String,Object> map) {
        return this.courseClassplanLivesDao.queryCourseClassplanLivesByDate(map);
    }

    @Override
    public Map<String, Object> updataPlaybackData(Integer datePicker,String startCountTime,String endCountTime) {
        logger.debug(String.format("\nupdataPlaybackData param is %s\n%s\n%s",datePicker,startCountTime,endCountTime));
        Map<String, Object> resultData = new HashMap<String, Object>();

        try {
//			String startCountTime;
//			String endCountTime;
//			if(null==request){
//				startCountTime = "";
//				endCountTime = "";
//			}else{
//				startCountTime = ServletRequestUtils.getStringParameter(request, "startCountTime");
//				endCountTime = ServletRequestUtils.getStringParameter(request, "endCountTime");
//			}
            // 如果传入时间为空，则为定时任务，时间减少一天(日期选择 不填默认0为当天，1为前一天)
            if(null==datePicker){
                resultData.put("code", 3);
                resultData.put("message", "datePicker不能为空");
                logger.error(String.format("\nupdataPlaybackData datePicker error is %s",datePicker));
                return resultData;
            }
            if (datePicker == 1 && StringUtils.isBlank(startCountTime)) {
                startCountTime = DateUtils.subtractOneday(DateUtils.getStringDate());
                endCountTime = startCountTime;
            }else if(datePicker == 0 && StringUtils.isBlank(startCountTime)){
                startCountTime = DateUtils.getStringDate();
                endCountTime = startCountTime;
            }

            Long startCountTimeLong = DateUtils.getStartTimeLong(startCountTime);
            Long endCountTimeLong = DateUtils.getEndTimeLong(endCountTime);
            if(startCountTimeLong > endCountTimeLong){
                resultData.put("code", 1);
                resultData.put("message", "参数错误");
                logger.error(String.format("\nupdataPlaybackData time param is error %s\n%s",startCountTime,endCountTime));
                return resultData;
            }
            Long reduceTime = endCountTimeLong-startCountTimeLong;
            Integer except = (int) (reduceTime/86399999);
            for (int day = 0; day < except; day++) {
                //使用时间来对比排课详情表(course_classplan_lives),筛选出一天的直播课;
                Map<String,Object> dayMap = new HashMap<String,Object>();
                startCountTime = DateUtils.longFormatString(DateUtils.getStartTimeLong(startCountTime));//统计日期的开始时间
                endCountTime = DateUtils.longFormatString(DateUtils.getEndTimeLong(startCountTime));//统计日期的结束时间
                dayMap.put("startTime", startCountTime);
                dayMap.put("endTime", endCountTime);
                List<CourseClassplanLivesEntity> entityList = queryCourseClassplanLivesByDate(dayMap);
                if(entityList.size() != 0){
                    for (CourseClassplanLivesEntity courseClassplanLivesEntity : entityList) {
                        if(courseClassplanLivesEntity.getStartTime().getTime() > courseClassplanLivesEntity.getEndTime().getTime()){
                            resultData.put("code", 2);
                            resultData.put("message", "直播时间异常");
                            return resultData;
                        }
                        //获取录制件前的常量准备
                        String classPlanId = courseClassplanLivesEntity.getClassplanId();
                        CourseClassplanEntity courseClassplanEntity = courseClassplanService.queryObjectByClassplanId(classPlanId);
//							Long ProductId = courseClassplanEntity.getProductId();
                        Map<String,Object> produceMap = new HashMap<String,Object>();
                        produceMap.put("productId", courseClassplanEntity.getProductId());
                        SysProductEntity sysProductEntity = sysProductService.queryObject(produceMap);
                        Map<String,Object> LiceroomMap = new HashMap<String,Object>();
                        LiceroomMap.put("liveRoomId", courseClassplanLivesEntity.getLiveroomId());
                        MallLiveRoomEntity mallLiveRoomEntity = mallLiveRoomService.queryObject(LiceroomMap);

                        Long liveStartLong = courseClassplanLivesEntity.getReadyTime().getTime();
                        Long liveEndLong = courseClassplanLivesEntity.getEndTime().getTime();
                        Long vagueStartLong = liveStartLong - 1200000;
                        Long vagueEndLong = liveEndLong + 3600000;
                        //获取录制件前的常量准备
                        String URL = "http://"+sysProductEntity.getGenseeDomain()+"/integration/site/webcast/record/info";
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("loginName", sysProductEntity.getGenseeLoginname());
                        params.put("password", sysProductEntity.getGenseePassword());
                        params.put("webcastId", mallLiveRoomEntity.getGenseeLiveId());

                        String dataList = HttpClientUtil4_3.post(URL,params,null);
                        logger.warn(String.format("\nupdataPlaybackData integration data is warn %s",dataList));
                        System.out.println("拿到的数据为："+dataList);
                        JSONObject jsonObject = new JSONObject(dataList);
                        JSONArray recordList = jsonObject.getJSONArray("recordList");

                        Integer size = recordList.length();
                        Map<Object, Object> differTimeMap = new HashMap<Object,Object>();
                        List<Long> differList = new ArrayList<Long>();

                        for (int i = 0; i < size; i++) {
                            Map<String, Object> recordMap = JSONUtil.jsonToMap(recordList.get(i).toString());
                            Long recordStartTime = (Long) recordMap.get("recordStartTime");
                            Long recordEndTime = (Long) recordMap.get("recordEndTime");
                            // 录制件时间 比直播时间大(一般情况都是)匹配
                            if (liveStartLong < recordStartTime && vagueEndLong > recordEndTime) {
                                Long differ = recordStartTime - liveStartLong;
                                differTimeMap.put(differ, i);
                                differList.add(differ);
                            } else if (liveStartLong > recordStartTime && recordStartTime > vagueStartLong) {
                                Long differ = liveStartLong - recordStartTime;
                                differTimeMap.put(differ, i);
                                differList.add(differ);
                            }
                        }
                        if(differList.size()>0){
                            Map<String, Object> recordInfo = JSONUtil.jsonToMap(recordList.get((int)differTimeMap.get(Collections.min(differList))).toString());
                            Map<String, Object> param = new HashMap<String,Object>();
                            String backId = null;
                            if(recordInfo!=null && recordInfo.get("url")!=null && StringUtils.isNotBlank(recordInfo.get("url")+"")){
                                //url = https://hqyzx.gensee.com/webcast/site/vod/play-e92032a1053144b7bc3d7d2eeb0924ba
                                String url = recordInfo.get("url").toString();
                                backId = url.substring(url.lastIndexOf("play-")+5);
                            }
                            //放进去直播详情表
                            param.put("backUrl", recordInfo.get("url"));
                            param.put("backId", backId);
                            param.put("classplanLiveId", courseClassplanLivesEntity.getClassplanLiveId());
                            //录播视频进行填入;
                            if(StringUtils.isBlank(courseClassplanLivesEntity.getBackUrl())){
                                logger.error("auto updateBackUrl task: classplanLiveId={},backUrl={},backId={}",courseClassplanLivesEntity.getClassplanLiveId(),recordInfo.get("url"),backId);
                                this.updateBackUrlByClassplanliveId(param);
                                logger.info(String.format("\nupdataPlaybackData updateBackUrlByClassplanliveId data is update succeed"));
                                //courseClassplanLivesService.updateBackUrl(param);
                                //System.out.println("录播视频保存");
                            }else{
                                //录播视频已经存在;
                                //System.out.println("录播视频已经存在");
                            }
                        }else{//无正确数据
                        }
                    }
                }
                startCountTime = DateUtils.addOneday(startCountTime);//统计多日
            }
//				System.out.println("统计多少天:"+except);
            resultData.put("code", 0);
            return resultData;

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return resultData;
    }

    @Override
    public List<CourseClassplanLivesChangeRecordEntity> queryEntityListByClassplanId(String classplanId) {
        return courseClassplanLivesDao.queryEntityListByClassplanId(classplanId);
    }

    @Override
    public List<CourseClassplanLivesEntity> queryListByClassplanId(String courseClassplanId) {

        return courseClassplanLivesDao.queryListByClassplanId(courseClassplanId);
    }


}

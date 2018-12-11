package io.renren.task;

import io.renren.entity.AttendReportEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.SchoolReportDetailEntity;
import io.renren.entity.SchoolReportEntity;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.ClassPlanLivesDetailPOJO;
import io.renren.pojo.SchoolReportUserMessagePOJO;
import io.renren.pojo.log.LogAttendWatchTimePOJO;
import io.renren.service.*;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计班主任出勤率报表
 * Created by DL on 2018/3/17.
 */
@Component("io.renren.task.SchoolReportJob")
public class SchoolReportJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SchoolReportDetailService schoolReportDetailService;
    @Autowired
    private SchoolReportService schoolReportService;
    @Autowired
    private LogGenseeWatchService logGenseeWatchService;
    @Autowired
    private CourseAbnormalCheckService courseAbnormalCheckService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void month(Map<String,Object> params) {
        String date = (String) params.get("date");
        logger.info("GenerateMonthReportJob start==> date:{},time:{}" ,
                date,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

        Map<String,Object> paramsMap = new HashMap<>();
        Date startDate = null;
        Date endDate = null;
        Date generDate = date==null?new Date():DateUtils.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(generDate);
        int month_index = cal.get(Calendar.DAY_OF_MONTH);
        if(month_index != 3){
            return;
        }
        //获取生成月报的时间范围
        cal.set(Calendar.DAY_OF_MONTH, 0);//设定日期为上个月的最后一日
        endDate = cal.getTime();//获取上个月最后一天
        cal.set(Calendar.DAY_OF_MONTH, 1);//此时日历为当月的上个月,再设定为第一天
        startDate = cal.getTime();//获取上个月第一天

//        paramsMap.put("startDate", DateUtils.format(startDate)+" 00:00:00");
//        paramsMap.put("endDate", DateUtils.format(endDate)+" 23:59:59");
        String startDateStr = DateUtils.format(startDate)+" 00:00:00";
        String endDateStr = DateUtils.format(endDate)+" 23:59:59";
        //记录明细
        generateReportDetail(startDateStr,endDateStr,0);
        //生成汇总报表
        generateReport(startDateStr,endDateStr,0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void week(Map<String,Object> params) {
        String date = (String) params.get("date");
        logger.info("GenerateWeekReportJob start==> date:{},time:{}" ,
                date,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

        Map<String,Object> paramsMap = new HashMap<>();
        Date startDate = null;
        Date endDate = null;
        Date generDate = date==null?new Date():DateUtils.parse(date);
        int week = DateUtils.getWeek(generDate);
        //获取生成周报的时间范围
        if(week == 2){
            /*startDate = DateUtils.getDateBefore(generDate,3);
            endDate = DateUtils.getDateBefore(generDate,1);*/

            Calendar cal = Calendar.getInstance();
            cal.setTime(generDate);
            endDate = cal.getTime();//当前日期
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();//获取当月第一天

        }else if(week == 5){
            /*startDate = DateUtils.getDateBefore(generDate,4);
            endDate = DateUtils.getDateBefore(generDate,1);*/

            Calendar cal = Calendar.getInstance();
            cal.setTime(generDate);
            endDate = cal.getTime();//当前日期
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();//获取当月第一天
        }else{
            return;
        }
//        paramsMap.put("startDate", DateUtils.format(startDate)+" 00:00:00");
//        paramsMap.put("endDate", DateUtils.format(endDate)+" 23:59:59");

        String startDateStr = DateUtils.format(startDate)+" 00:00:00";
        String endDateStr = DateUtils.format(endDate)+" 23:59:59";
        generateReportDetail(startDateStr,endDateStr,1);
        generateReport(startDateStr,endDateStr,1);
    }

    private void generateReportDetail(String startDateStr,String endDateStr,int type) {
        try {
            Date startDate = DateUtils.parse(startDateStr,"yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtils.parse(endDateStr,"yyyy-MM-dd HH:mm:ss");
            //记录明细
            //获取直播商品所有班级的学员(id)
            List<SchoolReportUserMessagePOJO> userMessagePOJOList = schoolReportDetailService.queryUserMessage();
            if (userMessagePOJOList != null){
                //获取学员排课列表
                for (SchoolReportUserMessagePOJO userMessagePOJO : userMessagePOJOList) {
                    SchoolReportDetailEntity entity = new SchoolReportDetailEntity();
                    Long userId = userMessagePOJO.getUserId();
                    entity.setClassTeacherId(userMessagePOJO.getClassTeacherId());
                    entity.setClassTeacherName(userMessagePOJO.getClassTeacherName());
                    entity.setOrderId(userMessagePOJO.getOrderId());
                    entity.setUserId(userId);
                    entity.setStartDate(startDate);
                    entity.setEndDate(endDate);
                    entity.setDr(0);
                    List<String> classPlanIdList = schoolReportDetailService.classPlanIdByOrder(userMessagePOJO.getOrderId());
                    //调用方法判断学员状态(学员,排课列表,订单id,开始时间,结束时间)
                    CourseAbormalTypeEnum statusEum = courseAbnormalCheckService.getUserCourseAbormalTypeEnum(userId,classPlanIdList,userMessagePOJO.getOrderId(),startDate,endDate);
                    //查询课次是否正常
                    List<ClassPlanLivesDetailPOJO> detailPOJOList = schoolReportDetailService.getClassPlanLivesDetail(userMessagePOJO.getOrderId(),userId,startDateStr,endDateStr);
                    if (detailPOJOList != null && detailPOJOList.size() > 0) {
                        Map<String,Object> map = new HashMap<>();
                        List<String> classPlacLiveIds = new ArrayList<>();
                        for (ClassPlanLivesDetailPOJO classPlanLivesDetailPOJO : detailPOJOList) {
                            if (classPlanLivesDetailPOJO.getClassPlanLiveIsError() == 0){
                               classPlacLiveIds.add(classPlanLivesDetailPOJO.getClassPlanLiveId());
                            }
                        }
                        if (classPlacLiveIds.size() > 0){
                        map.put("classPlacLiveIds",classPlacLiveIds);
                        map.put("userId",userId);
                        map.put("classTypeId",userMessagePOJO.getClassTypeId()+"");
                        //获取正常课次出勤时间
                        LogAttendWatchTimePOJO logAttend =  logGenseeWatchService.sumWatchTimeByClassPlanLives(map);
                        entity.setWatchDuration(logAttend.getWatchTime());
                        entity.setFullDuration(logAttend.getFullTime());
                        }
                    }
                    entity.setLearningStatus(statusEum.getValue());
                    entity.setCreateTime(new Date());
                    entity.setType(type);
                    schoolReportDetailService.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("execute:{}", e.toString());
        }
    }


    private void generateReportDetail3(String startDateStr,String endDateStr,int type) {
        try {
            Date startDate = DateUtils.parse(startDateStr,"yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtils.parse(endDateStr,"yyyy-MM-dd HH:mm:ss");
            //记录明细
            //获取直播商品所有班级的学员(id)
            Long begin = System.currentTimeMillis();
            List<SchoolReportUserMessagePOJO> userMessagePOJOList = schoolReportDetailService.queryUserMessage();
//            System.err.println("查找学员时间===========");
//            System.out.println(System.currentTimeMillis()-begin);
            if (userMessagePOJOList != null){
                //获取学员排课列表
                for (SchoolReportUserMessagePOJO userMessagePOJO : userMessagePOJOList) {
                    SchoolReportDetailEntity entity = new SchoolReportDetailEntity();
                    entity.setClassTeacherId(userMessagePOJO.getClassTeacherId());
                    entity.setClassTeacherName(userMessagePOJO.getClassTeacherName());
                    entity.setOrderId(userMessagePOJO.getOrderId());
                    entity.setUserId(userMessagePOJO.getUserId());
                    entity.setStartDate(startDate);
                    entity.setEndDate(endDate);
                    entity.setDr(0);
                    Long begin1 = System.currentTimeMillis();
                    List<String> classPlanIdList = schoolReportDetailService.classPlanIdByOrder(userMessagePOJO.getOrderId());
//                    System.err.println("查找排课时间===========");
//                    System.out.println(System.currentTimeMillis()-begin1);
                    //调用方法判断学员状态(学员,排课列表,订单id,开始时间,结束时间)
                    Long begin2 = System.currentTimeMillis();
                    CourseAbormalTypeEnum statusEum = courseAbnormalCheckService.getUserCourseAbormalTypeEnum(userMessagePOJO.getUserId(),classPlanIdList,userMessagePOJO.getOrderId(),startDate,endDate);
//                    System.err.println("调用查找状态时间===========");
//                    System.out.println(System.currentTimeMillis()-begin2);
                    //根据状态记录不同的信息内容
//                    CourseAbormalTypeEnum.normal ==
                    if (CourseAbormalTypeEnum.normal == statusEum) {
                        //在读获取出勤时间
                        Long begin3 = System.currentTimeMillis();

                        //语文 数学 英语  1月-4月
                        //语文 免考1月  3月   (2月,4月)
                        //数学 免考2月
                        //英语 免考3月



                        LogAttendWatchTimePOJO logAttend = null;
//                        LogAttendWatchTimePOJO logAttend =  logGenseeWatchService.=(userMessagePOJO.getUserId(),classPlanIdList,userMessagePOJO.getClassTypeId()+"",startDateStr,endDateStr);
//                        System.err.println("调用获取出勤时间===========");
//                        System.out.println(System.currentTimeMillis()-begin3);
                        entity.setWatchDuration(logAttend.getWatchTime());
                        entity.setFullDuration(logAttend.getFullTime());
                    }
                    entity.setLearningStatus(statusEum.getValue());
                    entity.setCreateTime(new Date());
                    entity.setType(type);
                    schoolReportDetailService.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("execute:{}", e.toString());
        }
    }


    private void generateReport(String startDate,String endDate,int type) {
        try {
            //生成汇总报表
            //找出当次定时任务生成的明细数据
            Long begin = System.currentTimeMillis();
            List<SchoolReportEntity> reportList = schoolReportService.getDetailsByDate(startDate,endDate,type);
//            System.err.println("统计报表时间===========");
//            System.out.println(System.currentTimeMillis()-begin);
            if (reportList != null && reportList.size()>0){
                for (SchoolReportEntity entity : reportList) {
                    entity.setDr(0);
                    entity.setCreateTime(new Date());
                    entity.setStartDate(DateUtils.parse(startDate,"yyyy-MM-dd HH:mm:ss"));
                    entity.setEndDate(DateUtils.parse(endDate,"yyyy-MM-dd HH:mm:ss"));
                    entity.setType(type);
                    schoolReportService.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("execute:{}", e.toString());
        }
    }
}

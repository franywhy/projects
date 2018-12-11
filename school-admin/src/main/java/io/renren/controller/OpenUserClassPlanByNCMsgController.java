package io.renren.controller;

import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.NcCourseClassplanEntity;
import io.renren.pojo.AppBannerPOJO;
import io.renren.pojo.CourseUserplanClassDetailPOJO;
import io.renren.pojo.CourseUserplanClassPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.*;
import io.renren.utils.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by DL on 2018/8/21.
 */
@Controller
@RequestMapping("task")
public class OpenUserClassPlanByNCMsgController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static  final String TASK_KEY = "task_key";

    private static final String HEAD = "XXJH_";

    private static final int LIMIT = 500;

    @Resource
    KGS studyplanKGS;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysCheckQuoteService sysCheckQuoteService;
    @Autowired
    private NcCourseClassplanService ncCourseClassplanService;
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private CourseUserplanService courseUserplanService;
    @Autowired
    private CourseUserplanClassService courseUserplanClassService;
    @Autowired
    private CourseUserplanClassDetailService courseUserplanClassDetailService;
    @Autowired
    private CourseClassplanService courseClassplanService;

    @ResponseBody
    @RequestMapping("/openUserClassPlanByNCMsg")
    public R open(String key, HttpServletRequest request) {
        String keyValue = sysConfigService.getValue(TASK_KEY, "task");
        if (keyValue.equals(key)) {
            try {
                Integer total = ncCourseClassplanService.queryTotalNotSuccess();
                NcCourseClassplanEntity cacheEntity = null;
                for (int i = 0; i <=  total/LIMIT; i++) {
                   Map<String,Object> map = new HashedMap();
                    map.put("offset",i*LIMIT);
                    map.put("limit",LIMIT);
                   List<NcCourseClassplanEntity> ncCourseClassplanEntityList = ncCourseClassplanService.queryDataByTs(map);
                    //查询订单(班级id)
                    if (ncCourseClassplanEntityList != null && ncCourseClassplanEntityList.size() > 0) {
                        Map<String,NcCourseClassplanEntity> ncCourseClassplanMap = new HashMap<>();
                        for (NcCourseClassplanEntity ncCourseClassplanEntity : ncCourseClassplanEntityList) {
                            //排课id
                            String classplanId = ncCourseClassplanEntity.getClassplanId();
                            //学员手机
                            String mobile = ncCourseClassplanEntity.getMobile();

                            ncCourseClassplanMap.put(getMapKey(mobile,classplanId),ncCourseClassplanEntity);
                        }
                        for (Map.Entry<String, NcCourseClassplanEntity> entry : ncCourseClassplanMap.entrySet()) {
                            NcCourseClassplanEntity entity = entry.getValue();
                            //订单ncId
                            String ncId = entity.getNcId();
                            //排课id
                            String classplanId = entity.getClassplanId();
                            //学员手机
                            String mobile = entity.getMobile();
                            //商品的ncId
                            String ncCommodityId = entity.getNcCommodityId();
                            MallOrderEntity orderEntity = mallOrderService.queryObjectByNcIdAndCommodityId(ncId, ncCommodityId);
                            if (orderEntity == null) {
                                orderEntity = mallOrderService.queryObjectByMobileAndComodityId(mobile, ncCommodityId);
                            }
                            //订单不为空
                            if (null != orderEntity && orderEntity.getClassId() != null) {
                                if (entity.getOpenFlag() == Constant.openFlag.OPEN.getValue()) {
                                    //生成学员规划(商品id,班级id)
                                    courseUserplanService.saveByOrderId(orderEntity.getOrderId());
                                    //生成学习计划(订单--->学员规划---->学习计划)
                                    CourseClassplanEntity classplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
                                    if (null != classplanEntity) {
                                        List<Map<String, Object>> userplanDetailIdList = courseUserplanService.queryUserplanDetailIdByOrderId(orderEntity.getOrderId(), classplanEntity.getCourseId());
                                        if (userplanDetailIdList != null && userplanDetailIdList.size() > 0) {
                                            for (Map<String, Object> userplanMap : userplanDetailIdList) {
                                                this.saveUserplanClass(request, entity, userplanMap);
                                            }
                                            //设置排课数据已经处理
                                            ncCourseClassplanService.updateIsSuccessByTime(entity.getMobile(),entity.getClassplanId(),entity.getNcModifiedTime());
                                        }else {
                                            logger.error("openUserClassPlanByNCMsg:该排课的课程没有匹配的学员规划详情,mobile={},classplanId={}",mobile,classplanId);
                                        }
                                    }else {
                                        logger.error("openUserClassPlanByNCMsg:排课计划为空,mobile={},classplanId={}",mobile,classplanId);
                                    }
                                } else if (entity.getOpenFlag() == Constant.openFlag.CLOSE.getValue()) {
                                    try {
                                        //设置排课数据已经处理
                                        ncCourseClassplanService.updateIsSuccessByTime(entity.getMobile(),entity.getClassplanId(),entity.getNcModifiedTime());
                                        this.closeUserplanClass(classplanId, orderEntity.getOrderId());
                                    } catch (Exception e) {
                                        logger.error("nc排课关闭学员规划出错,cause{},mobile={},classplanId={}",
                                                e.toString(),entity.getMobile(),entity.getClassplanId());
                                    }
                                }
                            } else {
                                logger.error("OpenUserClassPlanByNCMsgController open error,cause:{}, ncId={},ncCommodityId={},moblie={}",
                                        orderEntity == null?"订单为空!":"订单班级为空", entity.getNcId(),
                                        entity.getNcCommodityId(), entity.getMobile());
                            }
                        }
                    }
                }
                return R.ok();
            } catch (Exception e) {
                logger.error(e.toString());
                return R.error(e.getMessage());
            }
        }
        return R.error("OpenUserClassPlanByNCMsgController open error,keyValue is not match!");
    }

    private String getMapKey(String mobile, String classplanId) {
        return mobile+":"+classplanId;
    }

    private void closeUserplanClass(String ncClassplanId, Long orderId) {
        Map<String,Object> map = new HashedMap();
        //设置学员规划,学员规划详情,学习计划,学习计划详情dr=1;
        CourseUserplanEntity courseUserplanEntity = courseUserplanService.queryUserplanObjectByOrderId(orderId);
        if (courseUserplanEntity != null) {
            //根据学员规划查询学习计划
            List<Long> userplanClassList = courseUserplanClassService.queryCourseUserplanClass(courseUserplanEntity.getUserPlanId());
            if (userplanClassList != null && userplanClassList.size() > 0) {
                for (Long userplanClassId : userplanClassList) {
                    //根据学习计划查询学习计划详情
                    map.put("userplanClassId", userplanClassId);
                    List<CourseUserplanClassDetailPOJO> courseUserplanClassDetailList = courseUserplanClassDetailService.queryListByUserplanClassId(map);
                    if (courseUserplanClassDetailList != null || courseUserplanClassDetailList.size() > 0) {
                        for (CourseUserplanClassDetailPOJO courseUserplanClassDetailPOJO : courseUserplanClassDetailList) {
                            if (courseUserplanClassDetailPOJO.getClassplanId().equals(ncClassplanId)) {
                                //删除学习计划详情
                                map.put("id", courseUserplanClassDetailPOJO.getUserplanClassDetailId());
                                courseUserplanClassDetailService.delete(map);
                                if (courseUserplanClassDetailList.size() == 1){
                                    //删除学习计划
                                    map.put("id",userplanClassId);
                                    courseUserplanClassService.delete(map);

                                }
                               if (userplanClassList.size() == 1){
                                   //删除学员规划
                                   map.put("id",courseUserplanEntity.getUserPlanId());
                                   courseUserplanService.delete(map);
                               }
                            }
                        }
                    }
                }
            }
        }
    }

    private void saveUserplanClass(HttpServletRequest request, NcCourseClassplanEntity ncCourseClassplanEntity, Map<String, Object> userplanMap) {
        //学员规划子表ID
        Long userplanDetailId = (Long) userplanMap.get("userplanDetailId");
        //学员规划ID
        Long userplanId = (Long) userplanMap.get("userplanId");
        CourseUserplanClassPOJO courseUserplanClass = new CourseUserplanClassPOJO();
        courseUserplanClass.setUserplanId(userplanId);
        courseUserplanClass.setUserplanClassNo(getCode());
        courseUserplanClass.setExamScheduleId(null);
        courseUserplanClass.setRemark("");
        courseUserplanClass.setSchoolId(SchoolIdUtils.getSchoolId(request));
        courseUserplanClass.setCreateTime(new Date());
        courseUserplanClass.setModifyTime(new Date());
        courseUserplanClass.setCreatePerson(null);
        courseUserplanClass.setModifyPerson(null);
        courseUserplanClass.setDr(0);
        courseUserplanClass.setStatus(1);
        courseUserplanClass.setDeptId((Long)userplanMap.get("deptId"));
        List<CourseUserplanClassDetailPOJO> detailList = new ArrayList<>();
        CourseUserplanClassDetailPOJO classDetailPOJO = new CourseUserplanClassDetailPOJO();
        classDetailPOJO.setUserplanDetailId(userplanDetailId);
        classDetailPOJO.setClassplanId(ncCourseClassplanEntity.getClassplanId());
        classDetailPOJO.setTimestamp(new Date());
        classDetailPOJO.setRemark("");
        classDetailPOJO.setDr(0);
        classDetailPOJO.setSchoolId(SchoolIdUtils.getSchoolId(request));
        classDetailPOJO.setOrderNum(1);
        courseUserplanClass.setDetailList(detailList);
        detailList.add(classDetailPOJO);
        try {
            courseUserplanClassService.save(courseUserplanClass);
        } catch (UserPlanClassDetailException e) {
            logger.error("~学员规划ID:"+userplanId + "学员规划子表ID:"+userplanDetailId+"生成学习计划时异常!");
        }
    }

    public String getCode(){
        String userplanClassNo = HEAD + studyplanKGS.nextId();
        return userplanClassNo;
    }
}
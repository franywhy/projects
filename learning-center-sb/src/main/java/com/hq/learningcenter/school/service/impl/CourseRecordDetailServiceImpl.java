package com.hq.learningcenter.school.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.learningcenter.school.dao.LogPolyvWatchDao;
import com.hq.learningcenter.school.dao.MallGoodsDetailsDao;
import com.hq.learningcenter.school.dao.MallOrderDao;
import com.hq.learningcenter.school.entity.MallGoodsDetailsEntity;
import com.hq.learningcenter.school.service.CourseRecordDetailService;
import com.hq.learningcenter.utils.DateUtils;
import com.hq.learningcenter.school.dao.CourseRecordDetailDao;
import com.hq.learningcenter.school.entity.MallOrderEntity;
import com.hq.learningcenter.school.pojo.CourseRecordDetailPOJO;
import com.hq.learningcenter.school.pojo.CoursesPOJO;
import com.hq.learningcenter.school.pojo.LogPolyvWatchPOJO;
import com.hq.learningcenter.school.pojo.LogWatchRecordPOJO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("recordCourseService")
public class CourseRecordDetailServiceImpl implements CourseRecordDetailService {

    private static Logger logger = LoggerFactory.getLogger(CourseRecordDetailServiceImpl.class);
    @Autowired
    private CourseRecordDetailDao courseRecordDetailDao;
    @Autowired
    private MallOrderDao mallOrderDao;
    @Autowired
    private LogPolyvWatchDao logPolyvWatchDao;

    @Autowired
    private MallGoodsDetailsDao mallGoodsDetailsDao;

    @Resource
    protected StringRedisTemplate mainRedis;

    //Redis数据库Key 商品录播课程
    static String GOODS_RECORD_COURSE = "GOODS_RECORD_COURSE:";

    //Redis数据库Key 商品录播课程
    static String GOODS_RECORD_COURSE_APP = "GOODS_RECORD_COURSE_APP:";
    /**
     * 缓存过期时间（day）
     */
    final static Integer EXPIRE = 300;

    @Override
    public List<CoursesPOJO> getRecordCourseList(Long orderId, List<Long> productIdList, String businessId) {
        try {

            List<CoursesPOJO> courseList = new ArrayList<CoursesPOJO>();

            MallOrderEntity mallOrderEntity = this.mallOrderDao.queryOrder(orderId, businessId);
            boolean flag = belongCalendar();
            //时间在 19:00 - 21:00，就设置缓存
            if (true == flag) {
                String recordListStr = mainRedis.opsForValue().get(GOODS_RECORD_COURSE + mallOrderEntity.getCommodityId());
                if (StringUtils.isNotBlank(recordListStr)) {
                    courseList = JSONObject.parseArray(recordListStr, CoursesPOJO.class);
                    logger.info(" get cache successful   " + mallOrderEntity.getCommodityId());
                    return courseList;
                }
                List<MallGoodsDetailsEntity> goodsDetails = mallGoodsDetailsDao.queryCourseByCommodityId(mallOrderEntity.getCommodityId(), mallOrderEntity.getAreaId(), 0, null);//
                if (null != goodsDetails && goodsDetails.size() > 0) {
                    for (MallGoodsDetailsEntity goodsDetail : goodsDetails) {
                        //根据课程id查询是否有课程录播
                        int recordCourseNum = 0;
                        recordCourseNum = this.courseRecordDetailDao.queryRecordCourseNum(goodsDetail.getCourseId());
                        if (recordCourseNum > 0) {
                            //获取有录播课的课程
                            CoursesPOJO coursesPOJO = this.courseRecordDetailDao.queryRecordCourse(goodsDetail.getCourseId(), productIdList);
                            if (coursesPOJO != null) {
                                //根据课程id获取录播课详情（即课程的录播课子表）
                                List<CourseRecordDetailPOJO> detailList = this.courseRecordDetailDao.queryRecordDetailList(goodsDetail.getCourseId());

                                for (CourseRecordDetailPOJO courseRecordDetailPOJO : detailList) {
                                    if (null != courseRecordDetailPOJO.getParentId()) {
                                        List<CourseRecordDetailPOJO> detailList2 = this.courseRecordDetailDao.queryRecordDetailList2(goodsDetail.getCourseId(), courseRecordDetailPOJO.getRecordId());
                                        if (null != detailList2 && detailList2.size() > 0) {
                                            for (CourseRecordDetailPOJO detail : detailList2) {
                                                setParam(mallOrderEntity.getUserId(), detail);
                                            }
                                            courseRecordDetailPOJO.setList(detailList2);
                                        }
                                    }
                                    setParam(mallOrderEntity.getUserId(), courseRecordDetailPOJO);
                                    coursesPOJO.setCourseRecordDetailList(detailList);
                                }
                                courseList.add(coursesPOJO);
                            }
                        }
                    }
                }
                mainRedis.opsForValue().set(GOODS_RECORD_COURSE + mallOrderEntity.getCommodityId(), JSONObject.toJSONString(courseList));
                mainRedis.expire(GOODS_RECORD_COURSE, EXPIRE, TimeUnit.MINUTES);
                logger.info(" set cache successful   " + mallOrderEntity.getCommodityId());
                return courseList;
            } else {
                //其他时间段就不设置缓存
                //根据订单id查询商品详情的课程ID集合
                List<MallGoodsDetailsEntity> goodsDetails = mallGoodsDetailsDao.queryCourseByCommodityId(mallOrderEntity.getCommodityId(), mallOrderEntity.getAreaId(), 0, null);//
                if (null != goodsDetails && goodsDetails.size() > 0) {
                    for (MallGoodsDetailsEntity goodsDetail : goodsDetails) {
                        //根据课程id查询是否有课程录播
                        int recordCourseNum = 0;
                        recordCourseNum = this.courseRecordDetailDao.queryRecordCourseNum(goodsDetail.getCourseId());
                        if (recordCourseNum > 0) {
                            //获取有录播课的课程
                            CoursesPOJO coursesPOJO = this.courseRecordDetailDao.queryRecordCourse(goodsDetail.getCourseId(), productIdList);
                            if (coursesPOJO != null) {
                                //根据课程id获取录播课详情（即课程的录播课子表）
                                List<CourseRecordDetailPOJO> detailList = this.courseRecordDetailDao.queryRecordDetailList(goodsDetail.getCourseId());

                                for (CourseRecordDetailPOJO courseRecordDetailPOJO : detailList) {
                                    if (null != courseRecordDetailPOJO.getParentId()) {
                                        List<CourseRecordDetailPOJO> detailList2 = this.courseRecordDetailDao.queryRecordDetailList2(goodsDetail.getCourseId(), courseRecordDetailPOJO.getRecordId());
                                        if (null != detailList2 && detailList2.size() > 0) {
                                            for (CourseRecordDetailPOJO detail : detailList2) {
                                                setParam(mallOrderEntity.getUserId(), detail);
                                            }
                                            courseRecordDetailPOJO.setList(detailList2);
                                        }
                                    }
                                    setParam(mallOrderEntity.getUserId(), courseRecordDetailPOJO);
                                    coursesPOJO.setCourseRecordDetailList(detailList);
                                }
                                courseList.add(coursesPOJO);
                            }
                        }
                    }
                }
            }
            return courseList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map<String, Object> getRecordHear(Long userId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            //根据学员id查询录播课观看记录表
            List<LogWatchRecordPOJO> logWatchRecordList = this.courseRecordDetailDao.queryLogWatchRecordList(userId);
            if (null != logWatchRecordList && logWatchRecordList.size() > 0) {
                LogWatchRecordPOJO logWatchRecordPOJO = logWatchRecordList.get(0);
                //录播课名称
                map.put("recordName", "上次学习：" + logWatchRecordPOJO.getRecordName());
                if (null != logWatchRecordPOJO.getTs()) {
                    //学习时间
                    map.put("learnTime", "学习时间：" + DateUtils.format(logWatchRecordPOJO.getTs(), DateUtils.DATE_HOUR_MIN_PATTERN));
                } else {
                    //学习时间
                    map.put("learnTime", "学习时间：无");
                }
            } else {
                map.put("recordName", "上次学习：无");
                map.put("learnTime", "学习时间：无");
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Long> queryProductId(String businessId) {
        return this.courseRecordDetailDao.queryProductId(businessId);
    }

    @Override
    public List<CoursesPOJO> getCourseRecord(Long orderId, List<Long> productIdList, String businessId) {
        try {
            List<CoursesPOJO> courseList = new ArrayList<CoursesPOJO>();
            MallOrderEntity mallOrderEntity = this.mallOrderDao.queryOrder(orderId, businessId);
            //根据订单id查询商品详情的课程ID集合
            List<MallGoodsDetailsEntity> goodsDetails = mallGoodsDetailsDao.queryCourseByCommodityId(mallOrderEntity.getCommodityId(), mallOrderEntity.getAreaId(), 0, null);//
            if (null != goodsDetails && goodsDetails.size() > 0) {
                for (MallGoodsDetailsEntity goodsDetail : goodsDetails) {
                    //根据课程id查询是否有课程录播
                    int recordCourseNum = 0;
                    recordCourseNum = this.courseRecordDetailDao.queryRecordCourseNum(goodsDetail.getCourseId());
                    if (recordCourseNum > 0) {
                        //获取有录播课的课程
                        CoursesPOJO coursesPOJO = this.courseRecordDetailDao.queryRecordCourse(goodsDetail.getCourseId(), productIdList);
                        courseList.add(coursesPOJO);
                    }
                }
            }
            return courseList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CourseRecordDetailPOJO> getCourseRecordDetailByCourseId(Long userId, Long courseId) {
        try {
            List<CourseRecordDetailPOJO> detailList = new ArrayList<CourseRecordDetailPOJO>();
            boolean flag = belongCalendar();
            //时间在 19:00 - 21:00，就设置缓存
            if (true == flag) {
                String recordListStr = mainRedis.opsForValue().get(GOODS_RECORD_COURSE_APP + courseId);
                if (StringUtils.isNotBlank(recordListStr)) {
                    detailList = JSONObject.parseArray(recordListStr, CourseRecordDetailPOJO.class);
                    logger.info(" get cache successful   " + courseId);
                    return detailList;
                }
                detailList = this.courseRecordDetailDao.queryRecordDetailList(courseId);
                for (CourseRecordDetailPOJO courseRecordDetailPOJO : detailList) {
                    if (null != courseRecordDetailPOJO.getParentId()) {
                        List<CourseRecordDetailPOJO> detailList2 = this.courseRecordDetailDao.queryRecordDetailList2(courseId, courseRecordDetailPOJO.getRecordId());
                        if (null != detailList2 && detailList2.size() > 0) {
                            for (CourseRecordDetailPOJO deltail2 : detailList2) {
                                setParam(userId, deltail2);
                            }
                            courseRecordDetailPOJO.setList(detailList2);
                        }
                    }
                    setParam(userId, courseRecordDetailPOJO);
                }
                mainRedis.opsForValue().set(GOODS_RECORD_COURSE_APP + courseId, JSONObject.toJSONString(detailList));
                mainRedis.expire(GOODS_RECORD_COURSE_APP, EXPIRE, TimeUnit.MINUTES);
                logger.info(" set cache successful   " + courseId);
                return detailList;
            }
            //其他时间段就不设置缓存
            //根据课程id获取录播课详情（即课程的录播课子表）
            detailList = this.courseRecordDetailDao.queryRecordDetailList(courseId);
            for (CourseRecordDetailPOJO courseRecordDetailPOJO : detailList) {
                if (null != courseRecordDetailPOJO.getParentId()) {
                    List<CourseRecordDetailPOJO> detailList2 = this.courseRecordDetailDao.queryRecordDetailList2(courseId, courseRecordDetailPOJO.getRecordId());
                    if (null != detailList2 && detailList2.size() > 0) {
                        for (CourseRecordDetailPOJO deltail2 : detailList2) {
                            setParam(userId, deltail2);
                        }
                        courseRecordDetailPOJO.setList(detailList2);
                    }
                }
                setParam(userId, courseRecordDetailPOJO);
            }
            return detailList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 参数设置
     *
     * @param userId
     * @param detail
     */
    private void setParam(Long userId, CourseRecordDetailPOJO detail) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("recordId", detail.getRecordId());
        map.put("userId", userId);
        List<LogPolyvWatchPOJO> watchs = logPolyvWatchDao.queryObject(map);
        if (watchs.size() != 0 && watchs.get(0).getAttentPer() != null) {
            detail.setAttentPer(watchs.get(0).getAttentPer().setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            detail.setAttentPer(BigDecimal.ZERO);
        }

        detail.setParam1(userId);
        detail.setParam2(String.valueOf(detail.getRecordId()));
    }

    /**
     * 判断当前的时间是否在 19:00 - 21:00 之间
     *
     * @return
     */
    public static boolean belongCalendar() {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;

        try {
            nowTime = df.parse(df.format(new Date()));
            beginTime = df.parse("17:00");
            endTime = df.parse("22:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        }
        return false;
    }

}

package io.renren.modules.job.task;

import com.alibaba.fastjson.JSON;
import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.entity.LogPolyvDetailEntity;
import io.renren.modules.job.entity.LogPolyvWatchEntity;
import io.renren.modules.job.pojo.CourseRecordDetailPOJO;
import io.renren.modules.job.pojo.PolyvResultPOJO;
import io.renren.modules.job.pojo.log.LogPolyvDetailPOJO;
import io.renren.modules.job.service.CourseRecordDetailService;
import io.renren.modules.job.service.LogPolyvDetailService;
import io.renren.modules.job.service.LogPolyvWatchService;
import io.renren.modules.job.service.SysProductService;
import io.renren.modules.job.utils.DateUtils;
import io.renren.modules.job.utils.http.HttpClientUtil4_3;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从保利威视平台拉取录播考勤日志
 * Created by DL on 2018/10/16.
 */
@Component("io.renren.modules.job.task.SynchronizePolyvRecordLogJob")
public class SynchronizePolyvRecordLogJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${polyv.userid}")
    private String userid;
    @Value("${polyv.secretkey}")
    private String secretkey;
    @Value("${polyv.logurl}")
    private String logurl;

    private LogPolyvDetailService logPolyvDetailService;
    private LogPolyvWatchService logPolyvWatchService;
    private CourseRecordDetailService courseRecordDetailService;
    private SysProductService sysProductService;

    private LogPolyvDetailService getLogPolyvDetailService() {
        if (null == logPolyvDetailService)
            logPolyvDetailService = (LogPolyvDetailService) SpringContextUtils.getBean("logPolyvDetailService");
        return logPolyvDetailService;
    }

    private LogPolyvWatchService getLogPolyvWatchService() {
        if (null == logPolyvWatchService)
            logPolyvWatchService = (LogPolyvWatchService) SpringContextUtils.getBean("logPolyvWatchService");
        return logPolyvWatchService;
    }

    private CourseRecordDetailService getCourseRecordDetailService() {
        if (null == courseRecordDetailService)
            courseRecordDetailService = (CourseRecordDetailService) SpringContextUtils.getBean("courseRecordDetailService");
        return courseRecordDetailService;
    }
    private SysProductService getSysProductService(){
        if(null == sysProductService)sysProductService = (SysProductService)SpringContextUtils.getBean("sysProductService");
        return sysProductService;
    }

    public void execute(Map<String, Object> params) {
        courseRecordDetailService = this.getCourseRecordDetailService();
        logPolyvWatchService = this.getLogPolyvWatchService();
        logPolyvDetailService = this.getLogPolyvDetailService();
        sysProductService = this.getSysProductService();

        String day = (String) params.get("day");//同步的某一天
        String vid = (String) params.get("vid");
        logger.info("SynchronizePolyvRecordLogJob.execut.params.day={},vid={}", day, vid);

        if (StringUtils.isBlank(day)) {
            day = DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), 1), "yyyyMMdd");
        } else {
            day = DateFormatUtils.format(DateUtils.parse(day), "yyyyMMdd");
        }
        logger.info("SynchronizePolyvRecordLogJob.execut.params.formatDay={}", day);

        //拼接参数:http://api.polyv.net/v2/data/{userid}/viewlog?day=&ptime=&sign=
        logger.info("SynchronizePolyvRecordLogJob.execut.day={},vid={},userid={},secretkey={}", day, vid, userid, secretkey);
        Long ptime = new Date().getTime();
        String signStr = "day=" + day + "&ptime=" + ptime + "&userid=" + userid + secretkey;
        String sign = DigestUtils.sha1Hex(signStr).toUpperCase();
        String polyvUrl = logurl + userid + "/viewlog?day=" + day + "&ptime=" + ptime + "&sign=" + sign;
        if (StringUtils.isNotBlank(vid)) {
            polyvUrl = logurl + userid + "/viewlog?day=" + day + "&ptime=" + ptime + "&sign=" + sign + "&vid=" + vid;
        }
        logger.info("SynchronizePolyvRecordLogJob.execut.polyvUrl={}", polyvUrl);

        Map<String, LogPolyvWatchEntity> cacheMap = new HashMap<>();
        LogPolyvWatchEntity watchEntity = null;
        LogPolyvDetailEntity detailEntityentity = null;
        try {
            String result = HttpClientUtil4_3.get(polyvUrl, new HashMap<String, String>());
            PolyvResultPOJO polyvResultPOJO = JSON.parseObject(result, PolyvResultPOJO.class);
            if (polyvResultPOJO.getCode() != 200) {
                logger.info("SynchronizePolyvRecordLogJob.execut.error,cause={}", polyvResultPOJO.getMessage());
            } else {
                List<LogPolyvDetailPOJO> dataList = polyvResultPOJO.getData();
                logger.info("SynchronizePolyvRecordLogJob.execut.dataList.size={},day={}", dataList.size(), day);
                if (dataList != null && dataList.size() > 0) {
                    for (LogPolyvDetailPOJO logPolyvDetailPOJO : dataList) {
                        //保存日志明细
                        detailEntityentity = saveLogPolyvDetailEntity(logPolyvDetailPOJO);
                        if (detailEntityentity == null) {
                            continue;
                        }
                        watchEntity = cacheMap.get(getMapKey(detailEntityentity.getRecordId(), detailEntityentity.getUserId()));
                        if (watchEntity != null) {
                            watchEntity.setPlayDuration(watchEntity.getPlayDuration() + detailEntityentity.getPlayDuration());
                        } else {
                            watchEntity = getLogPolyvWatchEntity(detailEntityentity);
                            cacheMap.put(getMapKey(detailEntityentity.getRecordId(), detailEntityentity.getUserId()), watchEntity);
                        }
                    }
                    for (Map.Entry<String, LogPolyvWatchEntity> entityEntry : cacheMap.entrySet()) {
                        watchEntity = entityEntry.getValue();
                        try {
                            logPolyvWatchService.saveOrUpdate(watchEntity);
                        } catch (Exception e) {
                            logger.error("SynchronizePolyvRecordLogJob.execut.saveOrUpdateWatchEntity.cause={}", e.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("SynchronizePolyvRecordLogJob.execut.fail.cause={}", e.toString());
        }
    }

    private LogPolyvWatchEntity getLogPolyvWatchEntity(LogPolyvDetailEntity entity) {
        LogPolyvWatchEntity watchEntity;
        watchEntity = new LogPolyvWatchEntity();
        watchEntity.setPlayDuration(entity.getPlayDuration());
        watchEntity.setCourseId(entity.getCourseId());
        watchEntity.setFullDuration(entity.getDuration());
        watchEntity.setRecordId(entity.getRecordId());
        watchEntity.setUserId(entity.getUserId());
        watchEntity.setVid(entity.getVid());
        return watchEntity;
    }

    private LogPolyvDetailEntity saveLogPolyvDetailEntity(LogPolyvDetailPOJO logPolyvDetailPOJO) {

        LogPolyvDetailEntity detailEntity = LogPolyvDetailPOJO.getEntity(logPolyvDetailPOJO);
        String userId = detailEntity.getUserId();
        String recordId = detailEntity.getRecordId();
        Date polyvCreateTime = detailEntity.getPolyvCreateTime();

        //校验日志明细是否重复:userId,recordId,createTime
        if (StringUtils.isNotBlank(userId)
                && StringUtils.isNotBlank(recordId)
                && logPolyvDetailService.checkDetail(userId, recordId, polyvCreateTime)) {
            //根据录播课次id查询课程id,视频总时长
            try {
                CourseRecordDetailPOJO recordDetailPOJO = courseRecordDetailService.queryRecordDetailPojo(detailEntity.getRecordId(), detailEntity.getVid());
                if (recordDetailPOJO != null) {
                    //查询产品线的系数
                    //根据productId查询系数
                    Float recordEfficient = getSysProductService().queryRecordEfficient(recordDetailPOJO.getProductId());
                    detailEntity.setRecordId(recordDetailPOJO.getRecordId() + "");
                    detailEntity.setCourseId(recordDetailPOJO.getCourseId());
                    detailEntity.setDuration(recordDetailPOJO.getDurationS());
                    detailEntity.setPlayDuration((long) (detailEntity.getPlayDuration()*recordEfficient));
                } else {
                    detailEntity.setRecordId("-1");
                    detailEntity.setCourseId(-1L);
                }
                detailEntity.setCreateTime(new Date());
                //保存日志明细
                logPolyvDetailService.save(detailEntity);
                return detailEntity;
            } catch (Exception e) {
                logger.error("SynchronizePolyvRecordLogJob.execut.saveLogPolyvDetailEntity.cause={}", e.toString());
                return null;
            }
        }
        return null;
    }



    private String getMapKey(String recordId, String userId) {
        return "recordId:"+recordId+"userId:"+userId;
    }
}

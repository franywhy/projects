package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.entity.LogGenseeWatchEntity;
import io.renren.modules.job.entity.SysProductEntity;
import io.renren.modules.job.entity.VideoLogDetailEntity;
import io.renren.modules.job.pojo.log.ReplayCallbackForLogGenseeWatchPOJO;
import io.renren.modules.job.service.*;
import io.renren.modules.job.utils.DateUtils;
import io.renren.modules.job.utils.JSONUtil;
//import io.renren.modules.job.utils.http.HttpClientUtil4_3;
import io.renren.modules.job.utils.http.HttpClientUtil4_3;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时器-展视互动录播视频观看记录
 * 观看时长求和
 * @class io.renren.modules.job.task.SynchronizeVideoLogJob2.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月21日
 */
@Component("io.renren.modules.job.task.SynchronizeGenseeVideoLogJob")
public class SynchronizeGenseeVideoLogJob {

	private Logger logger = LoggerFactory.getLogger(getClass());

	
	private VideoLogDetailService _videoLogDetailService;
	private CourseClassplanLivesService _courseClassplanLivesService;
	private CourseUserplanDetailService _courseUserplanDetailService;
	private LogGenseeWatchService _logGenseeWatchService;
	private SysProductService _sysProductService;//产品线
    private GenseeCallbackService _genseeCallbackService;
	
	public void execute(Map<String,Object> params){
		

		String startDate = (String) params.get("startDate");//同步的某一天
		String countStr = (String) params.get("count");
		String productIdStr = (String) params.get("productId");//产品线
		//如果用户ID获取失败-通过用户名获取用户ID 开关 true-开启  false-关闭
		String un = (String) params.get("un");
		Boolean unBoo = StringUtils.isNotBlank(un) && "true".equals(un);
		logger.info("SynchronizeGenseeVideoLogJob startDate:{},countStr:{},productIdStr:{},un:{},unBoo:{} start：{}" , 
				startDate,countStr,productIdStr,un,unBoo,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		try{
            //1.从展示互动获取回放日志list汇总数据到日志总表log_gensee_watch
            //2.从回放实时回调表读取离线视频观看数据offliveList汇总到日志总表log_gensee_watch
            saveOrUpdateLogGenseeWatchByGensee(startDate, countStr, productIdStr, unBoo);
            saveOrUpdateLogGenseeWatchByReplayOfflive(startDate, countStr, productIdStr);
        }catch(Exception ex){
			logger.error("SynchronizeGenseeVideoLogJob execute:{}",ex.toString());
		}
	}

    private void saveOrUpdateLogGenseeWatchByReplayOfflive(String startDate, String countStr, String productIdStr) {
        GenseeCallbackService genseeCallbackService = getGenseeCallbackService();
        LogGenseeWatchService logGenseeWatchService = getLogGenseeWatchService();

        if (StringUtils.isNotBlank(productIdStr)) {
            Long productId = Long.valueOf(productIdStr);
            //根据productId查询系数
            Float coefficient = getSysProductService().queryCoefficient(productId);
            Integer count = Integer.parseInt(countStr);
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isBlank(startDate)) {
                startDate = DateFormatUtils.format(DateUtils.getDateBefore(new Date(System.currentTimeMillis()), count), "yyyy-MM-dd");
                map.put("startTime", DateUtils.parse(startDate + " 00:00:00","yyyy-MM-dd HH:mm:ss").getTime());
                map.put("endTime", DateUtils.parse(startDate + " 23:59:59","yyyy-MM-dd HH:mm:ss").getTime());
            } else {
                if (DateUtils.matchDateString(startDate) && count > 0) {
                    map.put("startTime", DateUtils.parse(startDate + " 00:00:00","yyyy-MM-dd HH:mm:ss").getTime());
                    map.put("endTime", DateUtils.parse(DateUtils.format(DateUtils.getDateAfter(DateUtils.parse(startDate), count - 1)) + " 23:59:59","yyyy-MM-dd HH:mm:ss").getTime());
                } else {
                    logger.error("CountGenseeLiveAttendJob matchDate:{}", "parameter date no match format");
                    return;
                }
            }
        //缓存所有用户:uid - 时长:duration
        Map<String, LogGenseeWatchEntity> tmpUserMap = new HashMap<>();
        Long duration = null;
        Long fullduration = null;
        LogGenseeWatchEntity logGenseeWatchEntity = null;

            //获取离线观看的日志信息
            map.put("isOfflive",0);
            map.put("productId",productId);
        List<ReplayCallbackForLogGenseeWatchPOJO> callbackList = genseeCallbackService.queryReplayCallback(map);
        if (null != callbackList && callbackList.size() > 0) {
            for (ReplayCallbackForLogGenseeWatchPOJO callback : callbackList) {
                duration = (long) ((callback.getLeaveTime() - callback.getJoinTime()) * coefficient);
                fullduration = callback.getEndTime().getTime() - callback.getStartTime().getTime();
                logGenseeWatchEntity = tmpUserMap.get(tmpKeyName(callback.getClassPlanLiveId(), callback.getUserId()));//从缓存中获取同一用户的观看时长
                if (null != logGenseeWatchEntity) {
                    logGenseeWatchEntity.setWatchDur(logGenseeWatchEntity.getWatchDur() + duration);//直播和录播总时长(毫秒)
                    logGenseeWatchEntity.setVideoDur(logGenseeWatchEntity.getVideoDur() + duration);//录播总时长(毫秒)
                } else {
                    logGenseeWatchEntity = new LogGenseeWatchEntity();//观看记录
                    logGenseeWatchEntity.setVideoId(callback.getVideoId());//录播ID
                    logGenseeWatchEntity.setLiveId(null);//直播课ID
                    logGenseeWatchEntity.setUserId(callback.getUserId());//用户ID
                    logGenseeWatchEntity.setBusinessId(callback.getClassPlanLiveId());//TODO 排课ID
                    logGenseeWatchEntity.setFullDur(fullduration);// TODO 应出勤时长(毫秒) 直播结束时间-直播开始时间
                    logGenseeWatchEntity.setWatchDur(duration);// 直播和录播总时长(毫秒)
                    logGenseeWatchEntity.setVideoDur(duration);//录播总时长(毫秒)
                    logGenseeWatchEntity.setLiveDur(0L);//直播总时长(毫秒)
                    logGenseeWatchEntity.setAttendPer(null);//出勤率-service中计算
                    logGenseeWatchEntity.setMId(null);
                    logGenseeWatchEntity.setProductId(productId);
                    tmpUserMap.put(tmpKeyName(callback.getClassPlanLiveId(), callback.getUserId()), logGenseeWatchEntity);//缓存一个用户的观看时长
                }
            }
        }
            for (Map.Entry<String, LogGenseeWatchEntity> entry : tmpUserMap.entrySet()) {
                try {
                    logGenseeWatchEntity = entry.getValue();
                    logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);//保存日志
                } catch (Exception e) {
                    logger.error("save LiveAttend_log:{}", e.toString());
                }
            }
        }
    }

    private void saveOrUpdateLogGenseeWatchByGensee(String startDate, String countStr, String productIdStr, Boolean unBoo) throws IOException {
        if(StringUtils.isNotBlank(productIdStr)){
            Long productId = Long.valueOf(productIdStr);
            //根据productId查询系数
            Float coefficient = getSysProductService().queryCoefficient(productId);
            Map<String , Object> queryProductMap = new HashMap<>();
            queryProductMap.put("productId", productId);
            SysProductEntity sysProductEntity = getSysProductService().queryObject(queryProductMap);
            if(null != sysProductEntity){
                //暂时互动参数
                String genseeLoginname = sysProductEntity.getGenseeLoginname();
                String genseePassword = sysProductEntity.getGenseePassword();
                String genseeWebcastVodLog = sysProductEntity.getGenseeWebcastVodLog();
                if(StringUtils.isNotBlank(genseeLoginname) && StringUtils.isNotBlank(genseePassword) && StringUtils.isNotBlank(genseeWebcastVodLog)){

                    Date dateParam = null;
                    //Integer count = 1;//从dateParam往后几天
                    Integer count = Integer.parseInt(countStr);
                    if(null == startDate || startDate.equals("")){//时间为空 获取前count天的
                        dateParam = DateUtils.getDateBefore(new Date(System.currentTimeMillis()), count);//DateFormatUtils.format(DateUtils.getDateBefore(new Date(), 1), "yyyy-MM-dd 00:00:00");
                    }else{//有时间-则获取开始时间

                        if(DateUtils.matchDateString(startDate)){
                            dateParam = DateUtils.parse(startDate);
                        }else{
                            logger.error("SynchronizeGenseeVideoLogJob matchDate:{}","parameter date no match format");
                            return ;
                        }
                    }

                    int totalPage=1;
                    Map<String,Object> pageMap = null;
                    for(int i=0;i<count;i++){//count天
                        totalPage = 1;
                        int totalCount = 0;
                        String dateParamStr = DateUtils.format(DateUtils.getDateAfter(dateParam, i)) + "%2000:00:00";
                        for(int j=1;j<=totalPage;j++){//分页
                            Map<String, LogGenseeWatchEntity> LogGenseeWatchMap = new HashMap<>();//缓存所有用户:uid - 时长:duration
                            String param = "loginName="+genseeLoginname+"&password="+genseePassword+"&date="+dateParamStr+"&pageNo="+j;
                            //请求展视互动
                            String logResult = HttpClientUtil4_3.get(genseeWebcastVodLog+"?"+param, new HashMap<String,String>());
                            //请求展示互动直播日志接口,得到返回数据
                            Map<String,Object> logResultMap = JSONUtil.jsonToMap(logResult);
                            if(((String)logResultMap.get("code")).equals("0")){

                                pageMap = (Map<String, Object>) logResultMap.get("page");
                                totalPage = (int) pageMap.get("totalPages");

                                VideoLogDetailService videoLogDetailService = getVideoLogDetailService();
                                CourseClassplanLivesService courseClassplanLivesService = getCourseClassplanLivesService();
                                CourseUserplanDetailService courseUserplanDetailService = getCourseUserplanDetailService();
                                LogGenseeWatchService logGenseeWatchService = getLogGenseeWatchService();

                                //从返回结果,提取日志数据
                                List<Map<String,Object>> logList = (List<Map<String, Object>>) logResultMap.get("list");
//									Map<String, LogGenseeWatchEntity> LogGenseeWatchMap = new HashMap<>();//缓存所有用户:uid - 时长:duration
                                LogGenseeWatchEntity logGenseeWatchEntity = null;

                                String videoId = null;//录播视频ID
                                Map<String, Object> classplanLiveParams = null;//查询排课的查询条件
                                Map<String, Object> classplanLive = null;//查询排课信息

                                if(null != logList && logList.size() > 0){
                                    totalCount += logList.size();
                                    logger.error("SynchronizeGenseeVideoLogJob execute,开始时间:{},总页数:{},第{}页的数据量是{}",startDate,totalPage,j,logList.size());
                                    for(Map<String,Object> log : logList){
                                        try{
                                            String userData = null;//用户数据,包含nickName和classPlanLiveId;
                                            String classplanLiveId = null;
                                            String nickName = null;//用户昵称
                                            Long _uid = null;//用户ID
                                            String _videoId = null;//录播课ID
                                            Long _startTime = null;//观看时间
                                            Long _leaveTime = null;//离开时间
                                            VideoLogDetailEntity videoLogDetailEntity = null;//录播课明细对象
                                            Long duration = null;//观看录播时长

                                            _uid = processUid(log.get("uid"));//测试阶段,数据有误,需提取
                                            _videoId = (String)log.get("vodId");
                                            _startTime = (Long)log.get("startTime");
                                            _leaveTime = (Long)log.get("leaveTime");
                                            userData = (String)log.get("name");
                                            if (userData != null && userData.contains("_hq_")){
                                                String[] split = userData.split("_hq_");
                                                nickName = split[0];
                                                classplanLiveId = split[1];
                                            }else {
                                                classplanLiveParams = new HashMap<String, Object>();//查询排课的查询条件
                                                classplanLiveParams.put("backId", videoId);//视频ID
                                                classplanLive = courseClassplanLivesService.queryByBackId(classplanLiveParams);//查询排课信息
                                                nickName = userData;
                                                classplanLiveId = (String) classplanLive.get("classplanLiveId");
                                                logger.error("SynchronizeGenseeVideoLogJob execute userData parseError,userData={}",userData);
                                            }
                                            Integer _device = (Integer)log.get("device");
                                            //如果此行数据name为M_开头且uid大于2000000000,则此数据无效
                                            if((nickName.startsWith("M_") || _uid > 100000000 ) && _device != 256) continue;
                                            //处理录播明细重复的详情-重复有效的详情跳过
                                            if(!videoLogDetailService.checkAddAble(_startTime, _leaveTime, (Integer)log.get("device"), _videoId , _uid,classplanLiveId))continue;
                                            //处理安卓手机没有用户ID的BUG 根据用户名加视频ID获取用户ID
                                            //安卓的设备-用户ID已99开通
                                            if(unBoo && _device == 256 && log.get("uid").toString().indexOf("99") == 0){//用户ID错误  根据用户昵称处理
                                                List<Long> userIdList = courseUserplanDetailService.getUserId(_videoId, _startTime, nickName);
                                                if(null != userIdList && userIdList.size() > 0){
                                                    _uid = userIdList.get(0);
                                                }
                                            }
                                            videoLogDetailEntity = new VideoLogDetailEntity();//录播明细记录
                                            videoLogDetailEntity.setVideoId(_videoId);//视频ID
                                            videoLogDetailEntity.setUserId(_uid);//用户ID
                                            videoLogDetailEntity.setStartTime(_startTime);//观看开始时间
                                            videoLogDetailEntity.setLeaveTime(_leaveTime);//观看结束时间
                                            duration =  (long) ((_leaveTime - _startTime) * coefficient);//观看时长
                                            videoLogDetailEntity.setDuration(duration);//观看时长
                                            videoLogDetailEntity.setDevice((Integer)log.get("device"));//设备
                                            videoLogDetailEntity.setPlatformCode(1);//平台代号 gensee:1
                                            videoLogDetailEntity.setCreateTime(new Date(System.currentTimeMillis()));//时间
                                            videoLogDetailEntity.setProductId(productId);//产品线
                                            videoLogDetailEntity.setCoefficient(coefficient);//系数
                                            videoLogDetailEntity.setClassplanLiveId(classplanLiveId);
                                            videoLogDetailService.save(videoLogDetailEntity);//保存详细日志
                                            //实际观看时长
                                            log.put("actDuration", duration);
                                            logGenseeWatchEntity = LogGenseeWatchMap.get(tmpKeyName(classplanLiveId,_uid));//从缓存中获取同一用户的观看时长
                                            if(null != logGenseeWatchEntity){
                                                logGenseeWatchEntity.setWatchDur(logGenseeWatchEntity.getWatchDur() + duration);//直播和录播总时长(毫秒)
                                                logGenseeWatchEntity.setVideoDur(logGenseeWatchEntity.getVideoDur() + duration);//录播总时长(毫秒)
                                            }else{
                                                logGenseeWatchEntity = new LogGenseeWatchEntity();//观看记录
                                                logGenseeWatchEntity.setVideoId(_videoId);//录播ID
                                                logGenseeWatchEntity.setLiveId(null);//直播课ID
                                                logGenseeWatchEntity.setUserId(_uid);//用户ID
												logGenseeWatchEntity.setBusinessId(classplanLiveId); //直播课次
                                                logGenseeWatchEntity.setWatchDur(duration);// 直播和录播总时长(毫秒)
                                                logGenseeWatchEntity.setVideoDur(duration);//录播总时长(毫秒)
                                                logGenseeWatchEntity.setLiveDur(0L);//直播总时长(毫秒)
                                                logGenseeWatchEntity.setAttendPer(null);//出勤率-service中计算
                                                logGenseeWatchEntity.setMId(null);
                                                logGenseeWatchEntity.setCreateTime(new Date(System.currentTimeMillis()));
//													logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);//保存日志
                                                logGenseeWatchEntity.setProductId(productId);
                                                LogGenseeWatchMap.put(tmpKeyName(classplanLiveId,_uid), logGenseeWatchEntity);//缓存一个用户的观看时长
                                            }
                                        }catch (Exception ex){
                                            logger.error("SynchronizeGenseeVideoLogJob save video_log_detail:{}",ex.toString());
                                        }
                                    }

                                    for (Map.Entry<String, LogGenseeWatchEntity> entry:LogGenseeWatchMap.entrySet()) {
                                        try{

                                            logGenseeWatchEntity = entry.getValue();//日志
                                            classplanLiveParams = new HashMap<String, Object>();//查询排课的查询条件
                                            classplanLiveParams.put("classplanLiveId", logGenseeWatchEntity.getBusinessId());
                                            classplanLive = courseClassplanLivesService.queryByBackId(classplanLiveParams);//查询排课信息

                                            if(null != classplanLive){
                                                logGenseeWatchEntity.setFullDur(((Date)classplanLive.get("endTime")).getTime() - ((Date)classplanLive.get("startTime")).getTime());// 应出勤时长(毫秒) 直播结束时间-直播开始时间
                                                logGenseeWatchEntity.setProductId(productId);
                                                logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);//保存日志
                                            }else {
                                                logger.error("GenseeVideoLogJob save logGenseeWatchEntity error cause classplanLive = null,classplanLiveId = {}",logGenseeWatchEntity.getBusinessId());
                                            }
                                        }catch(Exception ex){
                                            logger.error("SynchronizeGenseeVideoLogJob save video_log:{}",ex.toString());
                                        }
                                    }
                                }
                            }
                        }
                        logger.error("SynchronizeGenseeVideoLogJob execute,时间:{},从展示互动拉取的总数据量是{}",dateParamStr,totalCount);
                    }
                }
            }
        }
    }


    private String tmpKeyName(String classplanLiveId, Long uid){
		return "classplanLiveId:"+classplanLiveId+"&uid:"+uid;
	}
	
	/**
	 * 提取uid
	 */
	private Long processUid(Object obj){
		Integer uidI = null;
		Long uidL = null;
		try{
			uidI = Integer.parseInt(obj.toString());
			if(uidI > 1000000000){
				uidI -= 1000000000;
			}
			uidL = uidI.longValue();
		}catch(Exception e){
			uidL = Long.parseLong(obj.toString()) - 10000000000L;
		}
		
		return uidL;
	}
	
	
	private VideoLogDetailService getVideoLogDetailService(){
		if(null == _videoLogDetailService)_videoLogDetailService = (VideoLogDetailService)SpringContextUtils.getBean("videoLogDetailService");
		return _videoLogDetailService;
	}
	private CourseClassplanLivesService getCourseClassplanLivesService(){
		if(null == _courseClassplanLivesService)_courseClassplanLivesService = (CourseClassplanLivesService)SpringContextUtils.getBean("courseClassplanLivesService");
		return _courseClassplanLivesService;
	}
	private CourseUserplanDetailService getCourseUserplanDetailService(){
		if(null == _courseUserplanDetailService)_courseUserplanDetailService = (CourseUserplanDetailService)SpringContextUtils.getBean("courseUserplanDetailService");
		return _courseUserplanDetailService;
	}
	private LogGenseeWatchService getLogGenseeWatchService(){
		if(null == _logGenseeWatchService)_logGenseeWatchService = (LogGenseeWatchService)SpringContextUtils.getBean("logGenseeWatchService");
		return _logGenseeWatchService;
	}
	private SysProductService getSysProductService(){
		if(null == _sysProductService)_sysProductService = (SysProductService)SpringContextUtils.getBean("sysProductService");
		return _sysProductService;
	}
    private GenseeCallbackService getGenseeCallbackService(){
        if(null == _genseeCallbackService)_genseeCallbackService = (GenseeCallbackService) SpringContextUtils.getBean("genseeCallbackService");
        return _genseeCallbackService;
    }
}

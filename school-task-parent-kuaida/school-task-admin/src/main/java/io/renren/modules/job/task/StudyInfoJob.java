package io.renren.modules.job.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import io.renren.modules.job.entity.StudyInfoEntity;
import io.renren.modules.job.service.StudyInfoService;

@Component("io.renren.modules.job.task.StudyInfoJob")
@PropertySource({"classpath:task.properties"})
public class StudyInfoJob {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${limit}") 
	private Integer limit;
	@Value("${initDate}")
	private String initDate;
	
	@Autowired
	StudyInfoService service;
	 
	@Transactional
	public void execute(Map<String,Object> params) {
		long startTime = System.currentTimeMillis();
		logger.info("听课完成率开始时间：" + startTime);
		String value = service.queryMaxDate();
	 
		value = StringUtils.isBlank(value) ? initDate : value;
	 
		Map<String,Object> vwMap = new HashMap<>();
		vwMap.put("date", value);
		
		//总数据量
		int total = service.queryVideoWatchTotal(vwMap);
		//循环查询次数,分批节省空间
		int count = total / limit + (total % limit == 0 ? 0 : 1);
		logger.info("进入循环：总数据量" + total +"条,循环" + count + "次,");
		for(int i = 0 ; i < count; i++){ 
			long start = System.currentTimeMillis();
			vwMap.put("offset", i * limit);
			vwMap.put("limit", limit); 
			List<StudyInfoEntity> watchs = service.queryVideoWatch(vwMap); 
			//学员Id集合
			Set<Long> userIds = Sets.newHashSet();
			for(StudyInfoEntity entity : watchs){
				userIds.add(entity.getUserId()); 
			}
			
			//查找报读信息 
			List<StudyInfoEntity> infos = service.queryStudyInfos(userIds);
 
			List<StudyInfoEntity> newData = Lists.newArrayList();
			List<StudyInfoEntity> oldData = Lists.newArrayList();
			for(StudyInfoEntity watch : watchs){ 
				//是否为增量新数据 
				for(StudyInfoEntity info : infos){
					//update
					if(info.getUserId().equals(watch.getUserId()) && info.getCourseId().equals(watch.getCourseId())){ 
						watch.setId(info.getId());
						break;  
					} 
				}
				
				//听课完成率  = 观看时长/总时长
				Long fullDur = watch.getFullDur() == null ? 0L : watch.getFullDur();
				Long watchDur = watch.getWatchDur() == null ? 0L : watch.getWatchDur();
			
				BigDecimal persents = new BigDecimal( watchDur )
											.divide(new BigDecimal( fullDur ),4, BigDecimal.ROUND_HALF_UP);
				watch.setStudyPersents(persents); 
				//insert or update?
				if(watch.getId() == null){
					newData.add(watch);
				}else{
					oldData.add(watch);
				}
				
			}
			if(newData.size() > 0){
				service.insert(newData);
			}
			if(oldData.size() > 0){
				service.update(oldData);
			}
			long interval = (System.currentTimeMillis() - start) / 1000;
			logger.info("第" + (i+1) + "次循环结束，耗时(s):" + interval );
		} 
		logger.info("听课完成率结束,总耗时(s)：" + ((System.currentTimeMillis() - startTime)/1000));
	} 
}

package com.hq.bi.offline.task.service;

import com.hq.bi.offline.task.entity.LogStudentAttendEntity;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.LogStudentAttendMapper;
import com.hq.bi.offline.task.mapper.TkMmUserJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author hq
 */
@Slf4j
public class LogStudentAttendService {

	public static void main(String[] args) {
		Long startTime  = System.currentTimeMillis();

		generateLogStudentAttend();

		Long endTime = System.currentTimeMillis();

		log.info("插入学员考勤报表数据总耗时："+ (endTime - startTime) +"毫秒");
	}

	public static void generateLogStudentAttend() {

		Map<String,Object> map = new HashMap<>();
		//获取前一天时间
		LocalDateTime plusOneDay = LocalDateTime.now().plusDays(1);
		String startDate = plusOneDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
		String endDate = plusOneDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));

		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		LogStudentAttendMapper logStudentAttendMapper = sqlSession.getMapper(LogStudentAttendMapper.class);
		TkMmUserJobMapper tkMmUserJobMapper = sqlSession.getMapper(TkMmUserJobMapper.class);

		List<LogStudentAttendEntity> list = logStudentAttendMapper.queryUserplan(map);
		if(null != list && !list.isEmpty()){
			List<LogStudentAttendEntity> result = new ArrayList<>();
			for(LogStudentAttendEntity userplan:list){
				map.put("userplanId", userplan.getUserPlanId());
				map.put("userId", userplan.getUserId());
				map.put("classTypeId", userplan.getClassTypeId());
				List<Map<String,Object>> perMapList = logStudentAttendMapper.queryLivePerByMobile(map);
				if(null != perMapList && !perMapList.isEmpty()){
					for(Map<String,Object> perMap : perMapList){
						LogStudentAttendEntity entity = new LogStudentAttendEntity();

						entity.setUserPlanId(userplan.getUserPlanId());
						entity.setClassplanLiveId((String)perMap.get("classplanliveId"));
						entity.setUserId(userplan.getUserId());
						entity.setClassTypeId(userplan.getClassTypeId());
						entity.setAreaId(userplan.getAreaId());
						entity.setDeptId(userplan.getDeptId());
						entity.setClassId(userplan.getClassId());
						entity.setClassTeacherId(userplan.getClassTeacherId());
						entity.setUserName(userplan.getUserName());
						entity.setMobile(userplan.getMobile());
						entity.setAreaName(userplan.getAreaName());
						entity.setDeptName(userplan.getDeptName());
						entity.setClassName(userplan.getClassName());
						entity.setTeacherName(userplan.getTeacherName());
						entity.setLivePer((BigDecimal)perMap.get("livePer"));
						entity.setAttendPer((BigDecimal)perMap.get("attendPer"));
						entity.setStartClassTime((Date)perMap.get("startClassTime"));
						entity.setMinWatchDur((BigDecimal)perMap.get("minWatchDur"));
						entity.setMinFullDur((BigDecimal)perMap.get("minFullDur"));
						entity.setClassplanLiveName((String)perMap.get("classplanLiveName"));

						String id = DigestUtils.md5Hex(new StringBuilder(entity.getClassplanLiveId()).append(entity.getUserId()).toString());
						entity.setId(id);

						Map<String,Object> userJob = tkMmUserJobMapper.queryUserJobByLessonUser(entity.getClassplanLiveId(), entity.getUserId());
						if(null != userJob) {
							int rightNum = (int)userJob.get("rightNum");
							int errorNum = (int)userJob.get("errorNum");
							BigDecimal compliancePer = new BigDecimal(rightNum).divide(new BigDecimal(rightNum + errorNum),4,RoundingMode.HALF_UP);
							entity.setCompliancePer(compliancePer);
						}
						entity.setCreationTime(new Date());
						entity.setModifiedTime(new Date());

						result.add(entity);
					}
				}
			}
			//为了防止JVM消耗过大，分次插入，每次插入一万条
			int listSize = result.size();
			if(listSize > 0) {
				int toIndex=10000;
				int keyToken = 0;
				for(int i=0; i<listSize; i+=10000){
					if(i+10000 > listSize){
						toIndex = listSize-i;
					}
					List<LogStudentAttendEntity> newList = result.subList(i,i+toIndex);
					keyToken++;
					logStudentAttendMapper.insertBatch(newList);
					log.info("第"+keyToken+"次"+"插入学员考勤报表数据:"+ newList.size() +"条");
				}
			}
		}
		sqlSession.commit();
		sqlSession.close();
	}

}

package io.renren.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.LogStudentAttendDao;
import io.renren.pojo.log.LiveAttendPOJO;
import io.renren.pojo.log.LogStudentAttendPOJO;
import io.renren.service.LogStudentAttendService;
import io.renren.utils.DateUtils;

@Service("logStudentAttendService")
public class LogStudentAttendServiceImpl implements LogStudentAttendService {
	
	@Autowired
	private LogStudentAttendDao logStudentAttendDao; 
	
	@Override
	public List<LogStudentAttendPOJO> queryLiveAttend(Map<String,Object> map) {
		List<LogStudentAttendPOJO> result = new ArrayList<LogStudentAttendPOJO>();
		List<LogStudentAttendPOJO> list = logStudentAttendDao.queryUserplanByClass(map);
		if(null != list && !list.isEmpty()){
			for(LogStudentAttendPOJO pojo:list){
				map.put("userplanId", pojo.getUserplanId());
				map.put("userId", pojo.getUserId());
				map.put("classTypeId", pojo.getClassTypeId());
				Map<String,Object> perMap = logStudentAttendDao.queryLivePer(map);
				if(null != perMap){
					pojo.setLivePer((BigDecimal)perMap.get("livePer"));
					pojo.setAttendPer((BigDecimal)perMap.get("attendPer"));
					pojo.setStartClassTime(DateUtils.format((Date)perMap.get("startClassTime")));
					pojo.setUserStatus("在读");//暂时统一写死在读
					result.add(pojo);
				}
			}
		}
		return result;
	}
	
	@Override
	public List<LogStudentAttendPOJO> queryLiveAttendList(Map<String, Object> map) {
		//总出勤 小计
		BigDecimal attendPer = new BigDecimal(0);
		List<LogStudentAttendPOJO> result = new ArrayList<LogStudentAttendPOJO>();
		List<LogStudentAttendPOJO> list = logStudentAttendDao.queryUserplanByClass(map);
		if(null != list && !list.isEmpty()){
			for(LogStudentAttendPOJO userplan:list){
				map.put("userplanId", userplan.getUserplanId());
				map.put("userId", userplan.getUserId());
				map.put("classTypeId", userplan.getClassTypeId());
				List<Map<String,Object>> perMapList = logStudentAttendDao.queryLivePerByMobile(map);
				if(null != perMapList && !perMapList.isEmpty()){
					for(Map<String,Object> perMap : perMapList){
						LogStudentAttendPOJO pojo = new LogStudentAttendPOJO();
						pojo.setUserplanId(userplan.getUserplanId());
						pojo.setUserId(userplan.getUserId());
						pojo.setUserName(userplan.getUserName());
						pojo.setUserStatus(userplan.getUserStatus());
						pojo.setStartClassTime(userplan.getStartClassTime());
						pojo.setMobile(userplan.getMobile());
						pojo.setAreaName(userplan.getAreaName());
						pojo.setClassName(userplan.getClassName());
						pojo.setTeacherName(userplan.getTeacherName());
						pojo.setLivePer((BigDecimal)perMap.get("livePer"));
						pojo.setAttendPer((BigDecimal)perMap.get("attendPer"));
						pojo.setStartClassTime(DateUtils.format((Date)perMap.get("startClassTime")));
						pojo.setUserStatus("在读");//暂时统一写死在读
						pojo.setMinWatchDur((BigDecimal)perMap.get("minWatchDur")); 
						if(null == perMap.get("minWatchDur")){
							pojo.setMinWatchDur(new BigDecimal(0));
						}
						pojo.setMinFullDur((BigDecimal)perMap.get("minFullDur"));
						pojo.setClassplanLiveName((String)perMap.get("classplanLiveName"));
						pojo.setMinWatchDurTxt(pojo.getMinWatchDur().setScale(0,RoundingMode.CEILING)+"");
						pojo.setMinFullDurTxt(pojo.getMinFullDur().setScale(0,RoundingMode.CEILING)+"");
						pojo.setLivePerTxt(pojo.getLivePer().multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
						pojo.setAttendPerTxt(pojo.getAttendPer().multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
						attendPer = attendPer.add(pojo.getAttendPer());//总出勤 小计
						result.add(pojo);
					}
				}
			}
			LogStudentAttendPOJO totalPOJO = new LogStudentAttendPOJO();
			totalPOJO.setUserId("小计");
			totalPOJO.setUserName(String.valueOf(result.size()));
			totalPOJO.setUserStatus("--");
			totalPOJO.setStartClassTime("--");
			totalPOJO.setMobile("--");
			totalPOJO.setAreaName("--");
			totalPOJO.setClassName("--");
			totalPOJO.setTeacherName("--");
			totalPOJO.setLivePerTxt("--");
			totalPOJO.setAttendPerTxt(attendPer.divide(new BigDecimal(result.size()),6).multiply(new BigDecimal(100)).setScale(2,RoundingMode.CEILING) + "%");
			totalPOJO.setMinWatchDurTxt("--");
			totalPOJO.setMinFullDurTxt("--");
			totalPOJO.setClassplanLiveName("--");
			result.add(totalPOJO);
		}
		return result;
	}

	@Override
	public List<LiveAttendPOJO> queryWeekGroupByClass(Map<String, Object> map) {
		return logStudentAttendDao.queryWeekGroupByClass(map);
	}

	@Override
	public List<LiveAttendPOJO> queryWeekGroupByGoods(Map<String, Object> map) {
		return logStudentAttendDao.queryWeekGroupByGoods(map);
	}

	@Override
	public List<LiveAttendPOJO> queryWeekGroupByClassplan(Map<String, Object> map) {
		return logStudentAttendDao.queryWeekGroupByClassplan(map);
	}

	@Override
	public List<LiveAttendPOJO> queryMonthGroupByClass(Map<String, Object> map) {
		return logStudentAttendDao.queryMonthGroupByClass(map);
	}

	@Override
	public List<LiveAttendPOJO> queryMonthGroupByGoods(Map<String, Object> map) {
		return logStudentAttendDao.queryMonthGroupByGoods(map);
	}

	@Override
	public List<LiveAttendPOJO> queryMonthGroupByClassplan(Map<String, Object> map) {
		return logStudentAttendDao.queryMonthGroupByClassplan(map);
	}

	@Override
	public Long queryClassTotal(Map<String, Object> map) {
		return logStudentAttendDao.queryClassTotal(map);
	}
}

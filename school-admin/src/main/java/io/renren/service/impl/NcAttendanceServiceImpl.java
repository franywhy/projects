package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.NcAttendanceDao;
import io.renren.entity.NcAttendanceEntity;
import io.renren.mongo.dao.IAttendanceDao;
import io.renren.mongo.entity.AttendanceEntity;
import io.renren.service.NcAttendanceService;
import io.renren.task.AttendanceJob;
import io.renren.task.SyncDataJob;
import io.renren.utils.DateUtils;
import io.renren.utils.TaskManager;
import net.sf.json.JSONArray;

@Service("ncAttendanceService")
public class NcAttendanceServiceImpl implements NcAttendanceService {
	
	Logger log = LoggerFactory.getLogger(NcAttendanceServiceImpl.class);

	@Autowired
	private NcAttendanceDao ncAttendanceDao;
	@Autowired
	private SchedulerFactoryBean scheduler;
	@Autowired
	private IAttendanceDao attendanceDao;

	@Override
	public NcAttendanceEntity queryObject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncAttendanceDao.queryObject(map);
	}

	@PostConstruct
	public void init() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//
//				String jobName = "syncData";
//				//String JOB_GROUP_NAME = "SYNCNC_JOBGROUP_NAME";
//				//String TRIGGER_GROUP_NAME = "SYNCNC_TRIGGERGROUP_NAME";
//				String time = DateUtils.getCronByDate("2017-06-12 23:00:01", 0, "");
//				String time1 = DateUtils.getCronByDate("2017-06-13 00:15:00", 0, "");
//				//
//				try {
//					
//					TaskManager.addJob(jobName, SyncDataJob.class, time);
//					TaskManager.addJob("attendanceJob", AttendanceJob.class, time1);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
		

	}

	@Override
	public List<NcAttendanceEntity> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncAttendanceDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncAttendanceDao.queryTotal(map);
	}

	@Override
	public void save(NcAttendanceEntity questionBank) {
		// TODO Auto-generated method stub
		ncAttendanceDao.save(questionBank);
	}

	public void update(NcAttendanceEntity questionBank) {
		// TODO Auto-generated method stub
		ncAttendanceDao.update(questionBank);
	}

	@Override
	public void delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		ncAttendanceDao.delete(map);
	}

	@Override
	@Transactional
	public void saveBatch(List<NcAttendanceEntity> attendances) {
		// TODO Auto-generated method stub
		int size = attendances.size();
		List<NcAttendanceEntity> list = null;
		int num = size / 2000;
		for (int i = 0; i < num; i++) {
			list = new ArrayList<NcAttendanceEntity>();
			for (int j = i * 2000; j < (i + 1) * 2000; j++) {

				list.add(attendances.get(j));

			}
			ncAttendanceDao.saveBatch(list);
		}

	}

	@Override
	public List getAttendanceInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.ncAttendanceDao.getAttendanceInfo(map);
	}

	@Override
	public void syncData() {
		// TODO Auto-generated method stub
		List<AttendanceEntity> attendances = attendanceDao.findAttendanceToday();
		AttendanceEntity attendance = null;
		NcAttendanceEntity ncAttendance = null;
		List<NcAttendanceEntity> ncAttendances = new ArrayList<NcAttendanceEntity>();
		if (null != attendances && attendances.size() > 0) {
			for (int i = 0; i < attendances.size(); i++) {
				attendance = attendances.get(i);

				ncAttendance = new NcAttendanceEntity();
				ncAttendance.setClassPlanId(attendance.getNc_class_plan());
				ncAttendance.setCourseCode(attendance.getNc_course_id());
				ncAttendance.setAttendanceTime(DateUtils.parse(attendance.getAttendance_time(), "yyyy-MM-dd HH:mm:ss"));
				
				//
				ncAttendance.setStartChapter(null==attendance.getStart_chapter()?"":attendance.getStart_chapter());
				ncAttendance.setEndChapter(null==attendance.getEnd_chapter()?"":attendance.getEnd_chapter());
				ncAttendance.setStartSection(null==attendance.getStart_section()?"":attendance.getStart_section());
				ncAttendance.setEndSection(null==attendance.getEnd_section()?"":attendance.getEnd_section());
				
				ncAttendance.setSyncTime(new Date());
				
				
				ncAttendances.add(ncAttendance);

			}
			saveBatch(ncAttendances);
		}
	}

	@Override
	public List getAttendanceData() {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		//map.put("atime", "2016-05-30");
		Long start = System.currentTimeMillis();
		log.info("test start.."+start);
		List list = getAttendanceInfo(map);
		double test = (System.currentTimeMillis()-start);
		test = test/1000;
		
		log.info("test end..cost time is "+test+" s..");
		log.info("list is :"+list);
		String aid = "";
		List results = new ArrayList();
		List result = null;
		List lm = null;
		Map ma = null;
		Map mm = null;
		for (int i = 0; i < list.size(); i++) {
			Map m = (Map) list.get(i);
			if (!aid.equals(m.get("aid").toString())) {
				if (null!=ma) {
					ma.put("data", lm);
					//result.add(ma);
					results.add(ma);
				}
				aid = m.get("aid").toString();
				//result = new ArrayList();
				lm = new ArrayList();
				ma = new HashMap();
				
				ma.put("aid", aid);
				ma.put("time", m.get("attendance_time").toString());
				ma.put("class_plan", (String)m.get("class_plan_id"));
				
			}else{
				
				mm = new HashMap();
				mm.put("title",(String)m.get("kpname")+"("+(String)m.get("mdname")+")");
				mm.put("code", (String)m.get("question_bank_verse"));
				lm.add(mm);
				if (i==list.size()-1) {
					ma.put("data", lm);
					results.add(map);
				}
			}
			
		}
		//System.out.println(results);
		//JSONArray jsonArray = net.sf.json.JSONArray.fromObject(results);
		//System.out.println(jsonArray.toString());
		return results;
	}

}

package io.renren.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.CourseAbnormalOrderDao;
import io.renren.dao.RecordSignDao;
import io.renren.dao.RecordStudyDao;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.entity.RecordSignEntity;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import io.renren.pojo.ExaminationResultPOJO;
import io.renren.pojo.RecordStudyPOJO;
import io.renren.service.ExaminationResultService;
import io.renren.service.LogGenseeWatchService;
import io.renren.service.RecordSignService;
import io.renren.service.RecordStudyService;
@Service("recordStudyService")
public class RecordStudyServiceImpl implements RecordStudyService {
@Autowired
private RecordStudyDao recordStudyDao;

@Autowired
private LogGenseeWatchService logGenseeWatchService;
@Autowired
private ExaminationResultService examinationResultService;


protected Logger logger = LoggerFactory.getLogger(getClass());

@Override
public List<RecordStudyPOJO> queryList(Map<String, Object> queryMap) {
 
	return recordStudyDao.queryList(queryMap);
}

@Override
public void ClassCompletionRateProcess(List<RecordStudyPOJO> list) {
	Map<String, Object> map= new HashMap<String, Object> ();
	for (RecordStudyPOJO p:list) {
		if (p!=null&&p.getUserId()!=null&&p.getCourseId()!=null)
			map.put("userId", p.getUserId());
			map.put("courseId", p.getCourseId());
			 double  rate= 0;
			 try{
				 rate=	logGenseeWatchService.getVideoWatchPersents(map).doubleValue();
			 }catch (Exception es) {
				 es.printStackTrace();
				 
			 }
			 p.setClassCompletionRate(rate);
	}
	
	
	 
}

@Override
public void examinationResultProcess(List<RecordStudyPOJO> list) {
	Map<String, Object> map= new HashMap<String, Object> ();
	for (RecordStudyPOJO p:list) {
		 p.setScoreType(3);
		 p.setStudyPart("");
		  
		 p.setUnExaminationScore(0);
		if (p!=null&&p.getUserId()!=null&&p.getCourseId()!=null)
			map.put("userId", p.getUserId());
			map.put("courseId", p.getCourseId());
			 
			 try{
				 List<ExaminationResultPOJO>  examList=	 	examinationResultService.queryList(map);
				 Date ScheduleDate = null;
				 
				 for (ExaminationResultPOJO e:examList) {
					 if (ScheduleDate==null||ScheduleDate.before( p.ScheduleDateToDate(e.getScheduleDate()))) {
						 p.setScoreType(e.getExamType());				 
						 
						 p.setUnExaminationScore(e.getScore());
						 p.setStudyPart(e.getScheduleDate());
						 ScheduleDate= p.ScheduleDateToDate(e.getScheduleDate());
					 }
					
				 }
				 
				 
				 
			 }catch (Exception es) {
				 es.printStackTrace();
				 
			 }
			 
	}
	
	
	 
}
@Override
public void synchronizeRecordStudy(Map<String, Object> queryMap) {
	List<RecordStudyPOJO> list=  this.getSynchronizeRecordStudyList(queryMap);
	this.runSynchronizeProcess(list);
}




@Override
public List<RecordStudyPOJO> getSynchronizeRecordStudyList(Map<String, Object> queryMap) {
	 
	return 	recordStudyDao.getSynchronizeRecordStudyList(queryMap);
	 
}
@Override
public void runSynchronizeProcess( List<RecordStudyPOJO> list) {
	 if (list==null||list.size()==0) {
		 return ;
	 }
	 /**
	  * 处理流程:1.先以  order_id,course_id, user_id 作为key,pojo作为value组装成sync_map 用以后面的判断 ,
	  * 并且不取dr=1或者inclass=0的,取start_time最大的
	  * 2.以user_id存一份 并以user_id为主键取得全部的学习记录库记录
	  * 3.遍历学习记录库记录,在sync_map中找不到记录的 说明是要删除的 放到del_list中,如果找得到的则将同步map的key和 旧库内容放到 更新Map 
	  * 4.遍历同步map,不在更新map中的全部组成insert_list,在更新map中的 则更新旧库信息并组成 update_list
	  * 5.del_list,insert_list,update_list存库.
	  */
	 Map<String,RecordStudyPOJO>	 sync_map= new HashMap<String,RecordStudyPOJO>();
	 Map<String,RecordStudyPOJO>	 update_map= new HashMap<String,RecordStudyPOJO>();
	 List<RecordStudyPOJO> del_list= new ArrayList<RecordStudyPOJO>();
	 List<RecordStudyPOJO> insert_list= new ArrayList<RecordStudyPOJO>();
	 List<RecordStudyPOJO> update_list= new ArrayList<RecordStudyPOJO>();
	 
	 
	 
	 
	 Map<Long,String>	 userMap= new HashMap<Long,String>();
	 List<RecordStudyPOJO> locals= new ArrayList<RecordStudyPOJO>();
	 for (RecordStudyPOJO s:list) {
		String key= s.getOrderId()+"_"+s.getCourseId()+"_" +s.getUserId();
	 userMap.put(s.getUserId(), key);
	 	if (s.getDr()==0&&s.getInClass()==1) {
	 		RecordStudyPOJO sub= sync_map.get(key);
	 		if (sub==null) {
	 			sync_map.put(key, s);
	 		}else {
	 		if (	sub.getStartTime().after(s.getStartTime())) {
	 			sync_map.put(key, s);
	 		}
	 		}
	 		
	 	}
	 
	 } 
	 //用userid的in语句超过数量的会有问题 修改为1000个一次
	 
	 List<Long> userIdList= new ArrayList<Long>(userMap.keySet());
	 if (userIdList!=null&&userIdList.size()>0) {
	 List<Long> uidTemp= new ArrayList<Long>( ); 
	 int size=0;
	 for ( int i=0;i<userIdList.size();i++ ) {
		 uidTemp.add(userIdList.get(i));
		 size++;
		 if (size==1000||i+1==userIdList.size()) {
			 Map<String, Object> map = new HashMap<String,Object>();
			 map.put("userIds", uidTemp);
			 locals.addAll( recordStudyDao.queryList(map));
			 size =0;
			 uidTemp.clear();
		 }
		 
	 }
	 }
/*	 
	 if (userIdList!=null&&userIdList.size()>0) {
		 Map<String, Object> map = new HashMap<String,Object>();
		 map.put("userIds", userIdList);
		 locals=recordStudyDao.queryList(map);
	 }*/
	 
	 for (RecordStudyPOJO ls:locals) {
		 String key= ls.getOrderId()+"_"+ls.getCourseId()+"_" +ls.getUserId();
		 RecordStudyPOJO sub= sync_map.get(key);
	 		if (sub==null) {
	 			ls.setDr(1);
	 		del_list.add(ls);//找不到说明这个学员的这个订单的这个科目的排课不存在了 放到删除列表中
	 		}else {
	 			update_map.put(key, ls);//如果能找到的则将旧库信息赋到update_map中
	 		}
	 }
	 
	 for (Entry<String, RecordStudyPOJO> sync:sync_map.entrySet()) {
		 if (update_map.get( sync.getKey())!=null) {
			RecordStudyPOJO updatePojo = update_map.get( sync.getKey());
			updatePojo.StudyUpdate(updatePojo, sync.getValue());
			update_list.add(updatePojo);
		 }else {
			 insert_list.add(sync.getValue());
		 }
	 }
	 
	 ListProcess(del_list,1);
	 ListProcess(update_list,1);
	 ListProcess(insert_list,2); 
}

private void ListProcess(List<RecordStudyPOJO> List,int type) {
	 
	 for (RecordStudyPOJO t:List) {
		 try {
		 if (type==1) {
			 recordStudyDao.update(t);
			}else if (type==2) {
		     recordStudyDao.save(t);
			}
		 }catch (Exception es) {
				logger.error("SynchronizeRecordStudyJob has Error.RecordStudyPOJO={},error_message={}",t,es);
				
		 }
	 }
	
	
}



@Override
public int queryTotal(Map<String, Object> queryMap) {
	if(queryMap.get("name") != null || queryMap.get("mobile") != null || queryMap.get("classTeacherId") != null ){
		queryMap.put("condition","yes");
	}
	return recordStudyDao.queryTotal(queryMap);
}
 

}

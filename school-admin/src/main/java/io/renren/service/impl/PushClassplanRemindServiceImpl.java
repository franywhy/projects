package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.PushClassplanRemindDao;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.PushClassplanDetailRemindEntity;
import io.renren.entity.PushClassplanRemindEntity;
import io.renren.pojo.PushClassplanRemindPojo;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.PushClassplanDetailRemindService;
import io.renren.service.PushClassplanRemindService;
import io.renren.utils.DateUtils;



@Service("pushClassplanRemindService")
public class PushClassplanRemindServiceImpl implements PushClassplanRemindService {
	@Autowired
	private PushClassplanRemindDao pushClassplanRemindDao;
	@Autowired
	private PushClassplanDetailRemindService pushClassplanDetailRemindService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	
	@Override
	public PushClassplanRemindPojo queryObjectPojo(Map<String, Object> map){
		return pushClassplanRemindDao.queryObjectPojo(map);
	}
	
	@Override
	public List<PushClassplanRemindPojo> queryListPojo(Map<String, Object> map){
		return pushClassplanRemindDao.queryListPojo(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return pushClassplanRemindDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(PushClassplanRemindEntity pushClassplanRemind){
		//新增推送课次
		pushClassplanRemindDao.save(pushClassplanRemind);
		
		//获取新增的推送课次
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", pushClassplanRemind.getId());
		PushClassplanRemindPojo pushClassplanRemindPojo = this.queryObjectPojo(map);
		
		//获取排课计划明细
		List<CourseClassplanLivesEntity> classplanLives = courseClassplanLivesService.queryListByClassplanId(pushClassplanRemindPojo.getCourseClassplanId());
		for (CourseClassplanLivesEntity classplanLive : classplanLives) {
			
			//推送课次详情对象
			PushClassplanDetailRemindEntity pushClassplanDetailRemind = new PushClassplanDetailRemindEntity();
			//推送课次ID
			pushClassplanDetailRemind.setPushClassplanRemindId(pushClassplanRemindPojo.getId());
			//排课计划ID
			pushClassplanDetailRemind.setCourseClassplanId(pushClassplanRemindPojo.getCourseClassplanId());
			//通知模板ID
			pushClassplanDetailRemind.setPushTimeTemplateId(pushClassplanRemindPojo.getPushTimeTemplateId());
			//排课计划明细ID
			pushClassplanDetailRemind.setCourseClassplanLivesId(classplanLive.getClassplanLiveId());
			//推送内容
			pushClassplanDetailRemind.setPushContent(pushClassplanRemindPojo.getPushTimeTemplateContent().replace("***", classplanLive.getClassplanLiveName()).replace("###", DateUtils.format(classplanLive.getStartTime(), DateUtils.DATE_TIME_PATTERN)));
			//推送时间
			//定点推送
			if(0 == pushClassplanRemindPojo.getType()){
				Date pushDay = DateUtils.getDateAfter(classplanLive.getStartTime(), pushClassplanRemindPojo.getPushTimeTemplateDay());
				pushClassplanDetailRemind.setPushTime(DateUtils.format(pushDay) + " "+pushClassplanRemindPojo.getPushTimeTemplateTime() +":00");
			}else{//定时推送
				Integer addMin = Integer.valueOf(pushClassplanRemindPojo.getPushTimeTemplateTime());
				Date pushDay = DateUtils.addMinute(addMin, classplanLive.getStartTime());
				pushClassplanDetailRemind.setPushTime(DateUtils.format(pushDay, DateUtils.DATE_TIME_PATTERN));
			}
			//审核状态
			pushClassplanDetailRemind.setAuditStatus(1);
			//审核人
			pushClassplanDetailRemind.setAuditor(pushClassplanRemindPojo.getCreater());
			//审核时间
			pushClassplanDetailRemind.setAuditTime(new Date());
			//创建人
			pushClassplanDetailRemind.setCreater(pushClassplanRemindPojo.getCreater());
			//创建时间
			pushClassplanDetailRemind.setCreateTime(new Date());
			
			
			//保存推送课次详情
			pushClassplanDetailRemindService.save(pushClassplanDetailRemind);
		}
		
	}
	
	@Override
	public void update(PushClassplanRemindEntity pushClassplanRemind){
		pushClassplanRemindDao.update(pushClassplanRemind);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		pushClassplanRemindDao.delete(map);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Map<String, Object> map){
		
		pushClassplanDetailRemindService.deleteBatchByIds(map);
		
		pushClassplanRemindDao.deleteBatch(map);
	}

	
}

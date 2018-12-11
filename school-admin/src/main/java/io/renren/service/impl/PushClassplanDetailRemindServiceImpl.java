package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.PushClassplanDetailRemindDao;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.PushClassplanDetailRemindEntity;
import io.renren.pojo.PushClassplanRemindPojo;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.PushClassplanDetailRemindService;
import io.renren.service.PushClassplanRemindService;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;



@Service("pushClassplanDetailRemindService")
public class PushClassplanDetailRemindServiceImpl implements PushClassplanDetailRemindService {
	@Autowired
	private PushClassplanDetailRemindDao pushClassplanDetailRemindDao;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	@Autowired
	private PushClassplanRemindService pushClassplanRemindService;
	
	@Override
	public PushClassplanDetailRemindEntity queryObject(Map<String, Object> map){
		return pushClassplanDetailRemindDao.queryObject(map);
	}
	
	@Override
	public List<PushClassplanDetailRemindEntity> queryList(Map<String, Object> map){
		return pushClassplanDetailRemindDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return pushClassplanDetailRemindDao.queryTotal(map);
	}
	
	@Override
	public void save(PushClassplanDetailRemindEntity pushClassplanDetailRemind){
		pushClassplanDetailRemindDao.save(pushClassplanDetailRemind);
	}
	
	@Override
	public void update(PushClassplanDetailRemindEntity pushClassplanDetailRemind){
		pushClassplanDetailRemindDao.update(pushClassplanDetailRemind);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		pushClassplanDetailRemindDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		pushClassplanDetailRemindDao.deleteBatch(map);
	}

	/**
	 * 审核
	 */
	@Override
	public void theAudit(Map<String, Object> map) {

		pushClassplanDetailRemindDao.theAudit(map);
	}

	@Override
	public void deleteBatchByIds(Map<String, Object> map) {

		pushClassplanDetailRemindDao.deleteBatchByIds(map);
	}

	@Override
	public List<PushClassplanDetailRemindEntity> getListByParentId(Integer pushClassplanRemindId) {

		return pushClassplanDetailRemindDao.getListByParentId(pushClassplanRemindId);
	}

	@Override
	@Transactional
	public void refresh(Map<String, Object> map) {
		//先通过推送课次id删除推送课次详情
		pushClassplanDetailRemindDao.refresh(map);
		
		//通过推送课次id获取的推送课次对象
		Integer pushClassplanRemindId = (Integer) map.get("pushClassplanRemindId");
		Map<String, Object> mapParent = new HashMap<String, Object>();
		mapParent.put("id", pushClassplanRemindId);
		PushClassplanRemindPojo pushClassplanRemindPojo = pushClassplanRemindService.queryObjectPojo(mapParent);

		//通过排课计划id获取排课计划明细
		String classplanId = (String)map.get("classplanId");
		List<CourseClassplanLivesEntity> classplanLives = courseClassplanLivesService.queryListByClassplanId(classplanId);
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
				this.save(pushClassplanDetailRemind);
		}
		
	}
	
	
}

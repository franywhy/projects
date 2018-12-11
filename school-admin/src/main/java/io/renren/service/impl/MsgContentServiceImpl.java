package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MsgContentDao;
import io.renren.entity.MsgContentEntity;
import io.renren.entity.ScheduleJobEntity;
import io.renren.pojo.MsgContentPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseUserplanService;
import io.renren.service.MsgContentCommonService;
import io.renren.service.MsgContentService;
import io.renren.service.MsgContentTopService;
import io.renren.service.ScheduleJobService;
import io.renren.service.XinggePushService;
import io.renren.utils.ClassTypeUtils;
import io.renren.utils.DateUtils;
import io.renren.utils.TaskManager;


@Transactional(readOnly = true)
@Service("msgContentService")
public class MsgContentServiceImpl implements MsgContentService {
	@Autowired
	private MsgContentDao msgContentDao;
	@Autowired
	private MsgContentCommonService msgContentCommonService;
	@Autowired
	private MsgContentTopService  msgContentTopService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private ScheduleJobService scheduleJobService;
	@Autowired
	private XinggePushService xinggePushService;
	@Resource
	KGS msgContentIdKGS;
	//消息通知详情默认跳转地址
	@Value("#{application['msg.content.url']}")
	private String MSG_CONTENT_URL = "";
	
	@Override
	public MsgContentEntity queryObject(Map<String, Object> map){
		return msgContentDao.queryObject(map);
	}
	
	@Override
	public List<MsgContentEntity> queryList(Map<String, Object> map){
		return msgContentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return msgContentDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(MsgContentEntity msgContent){
		//主键
		msgContent.setContentId(((long) msgContentIdKGS.nextId()));
		if(StringUtils.isNotBlank(msgContent.getContentHtml())){
			//生成默认的跳转地址
			msgContent.setContentUrl(defUrl(msgContent.getContentId(), msgContent.getContentUrl(), msgContent.getContentHtml()));
		}
		//创建时间
		msgContent.setCreateTime(new Date());
		//修改时间
		msgContent.setModifyTime(msgContent.getCreateTime());
		//如果为暂不发送，发送时间置空
		if(msgContent.getSendType() == 0){
			msgContent.setTimestamp(null);
		}
		//如果为立即发送，默认发送时间为当前时间
		if(msgContent.getSendType() == 1){
			msgContent.setTimestamp(new Date());
		}
		if(null != msgContent){
			msgContent.setClasstypeIds(ClassTypeUtils.in(msgContent.getClasstypeIds()));
		}
		//保存主表
		msgContentDao.save(msgContent);
		
		//主键
		Long contentId = msgContent.getContentId();
		String schoolId = msgContent.getSchoolId();
		//立即发送 或者  定时发送
		if(msgContent.getSendType() == 1 || msgContent.getSendType() == 2){
			if(msgContent.getType() == 1){//通知对象为 排课
				String classtypeIds = ClassTypeUtils.out(msgContent.getClasstypeIds());
				//TODO 非空校验
				String[] classtypeIdArray = classtypeIds.split(",");
				//遍历排课班型下的用户
				List<Long> userIdList = userListByClassPlan(msgContent.getClassplanId(),
						classtypeIdArray, schoolId);
				if(msgContent.getCommonSend() == 1){//站内消息
					//批量保存
					this.msgContentCommonService.saveBatch(userIdList, contentId, schoolId);
				}
				if(msgContent.getTopSend() == 1){//弹窗消息
					//批量保存
					this.msgContentTopService.saveBatch(userIdList, contentId, schoolId);
				}
				if(msgContent.getPushSend() == 1){//消息推送
					//调用消息发送方法
					sendMsgByTag(msgContent.getPushSend(),msgContent.getSendType(),msgContent.getClasstypeIds(),msgContent.getClassplanId(),
							msgContent.getContentId(),msgContent.getSchoolId(),msgContent.getTimestamp(),msgContent.getContentTitle()
							);
				}
			}
			if(msgContent.getType() == 0){//通知对象为 全员
				List<Long> allUserIdList = allUserList(msgContent.getSchoolId());
				if(msgContent.getCommonSend() == 1){//站内消息
					//批量保存
					this.msgContentCommonService.saveBatch(allUserIdList, contentId, schoolId);
				}
				if(msgContent.getTopSend() == 1){//弹窗消息
					//批量保存
					this.msgContentTopService.saveBatch(allUserIdList, contentId, schoolId);
				}
				if(msgContent.getPushSend() == 1){//消息推送
					sendMsgAllUser(msgContent.getPushSend(),msgContent.getSendType(),msgContent.getContentId(),msgContent.getSchoolId(),
							msgContent.getTimestamp(),msgContent.getContentTitle());
				}
			}
		}
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(MsgContentEntity msgContent){
		//修改时间
		msgContent.setModifyTime(new Date());
		if(null != msgContent){
			msgContent.setClasstypeIds(ClassTypeUtils.in(msgContent.getClasstypeIds()));
		}
		if(StringUtils.isNotBlank(msgContent.getContentHtml())){
			//生成默认的跳转地址
			msgContent.setContentUrl(defUrl(msgContent.getContentId(), msgContent.getContentUrl(), msgContent.getContentHtml()));
		}
		//如果为立即发送，默认发送时间为当前时间
		if(msgContent.getSendType() == 1){
			msgContent.setTimestamp(new Date());
		}
		msgContentDao.update(msgContent);
		
		//主键
		Long contentId = msgContent.getContentId();
		String schoolId = msgContent.getSchoolId();
		//立即发送 或者  定时发送
		if(msgContent.getSendType() == 1 || msgContent.getSendType() == 2){
			if(msgContent.getType() == 1){//通知对象为 排课
				String classtypeIds = ClassTypeUtils.out(msgContent.getClasstypeIds());
				//TODO 非空校验
				String[] classtypeIdArray = classtypeIds.split(",");
				//遍历排课班型下的用户
				List<Long> userIdList = userListByClassPlan(msgContent.getClassplanId(),
						classtypeIdArray, msgContent.getSchoolId());
				if(msgContent.getCommonSend() == 1){//站内消息
					//批量保存
					this.msgContentCommonService.saveBatch(userIdList, contentId, schoolId);
				}
				if(msgContent.getTopSend() == 1){//弹窗消息
					//批量保存
					this.msgContentTopService.saveBatch(userIdList, contentId, schoolId);
				}
				//调用消息发送方法
				sendMsgByTag(msgContent.getPushSend(),msgContent.getSendType(),msgContent.getClasstypeIds(),msgContent.getClassplanId(),
						msgContent.getContentId(),msgContent.getSchoolId(),msgContent.getTimestamp(),msgContent.getContentTitle()
						);
			}
			if(msgContent.getType() == 0){//通知对象为 全员
				List<Long> allUserIdList = allUserList(msgContent.getSchoolId());
				if(msgContent.getCommonSend() == 1){//站内消息
					//批量保存
					this.msgContentCommonService.saveBatch(allUserIdList, contentId, schoolId);
				}
				if(msgContent.getTopSend() == 1){//弹窗消息
					//批量保存
					this.msgContentTopService.saveBatch(allUserIdList, contentId, schoolId);
				}
				if(msgContent.getPushSend() == 1){//消息推送
					sendMsgAllUser(msgContent.getPushSend(),msgContent.getSendType(),msgContent.getContentId(),msgContent.getSchoolId(),
							msgContent.getTimestamp(),msgContent.getContentTitle());
				}
			}
		}
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Map<String, Object> map){
		msgContentDao.delete(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		msgContentDao.deleteBatch(map);
	}

	@Override
	public List<MsgContentPOJO> queryPojoList(Map<String, Object> map) {
		List<MsgContentPOJO> msgContents = this.msgContentDao.queryPojoList(map);
		if(null != msgContents && msgContents.size() > 0){
			for (MsgContentPOJO msgContent : msgContents) {
				msgContent.setClasstypeIds(ClassTypeUtils.out(msgContent.getClasstypeIds()));
			}
		}
		return msgContents;
	}

	@Override
	public MsgContentPOJO queryPojoObject(Map<String, Object> map) {
		MsgContentPOJO msgContent = this.msgContentDao.queryPojoObject(map);
		msgContent.setClasstypeIds(ClassTypeUtils.out(msgContent.getClasstypeIds()));
		return msgContent;
	}

	@Override
	public int checkClassType(long id) {
		return this.msgContentDao.checkClassType(id);
	}
	
	/**
	 * 获取排课下的用户
	 * @param classplanId 排课计划id
	 * @param classtypeIds 班型ids
	 * @param schoolId 平台id
	 * @return
	 */
	private List<Long> userListByClassPlan(String classplanId , String[] classtypeIds , String schoolId){
		List<Long> userIdList=null;
	    userIdList = courseUserplanService.getUsers(classplanId, classtypeIds, schoolId);
		return userIdList;
	}
	/**
	 * 获取所有的用户
	 * @param schoolId 平台id
	 * @return
	 */
	private List<Long> allUserList(String schoolId){
		List<Long> userIdList=null;
		userIdList = courseUserplanService.getAllUsers(schoolId);
		return userIdList;
	}
	
	/**
	 * 消息发送方法(按标签推送)
	 * @param pushSend 推送消息
	 * @param sendType 立即发送/定时发送
	 * @param classtypeIds 班型ids
	 * @param classplanId 排课id
	 * @param contentId 消息主表id
	 * @param schoolId  平台id
	 * @param timestamp 发送时间
	 * @param contentTitle 消息标题
	 */
	private void sendMsgByTag(int pushSend, int sendType, String classtypeIds, String classplanId, Long contentId, String schoolId,  Date timestamp, String contentTitle){
		if(pushSend == 1){//推送消息
			if(sendType == 2){//定时发送
				//获取标签tag
				String[] ClasstypeIds =  ClassTypeUtils.out(classtypeIds).split(",");
				List<String> tagList = new ArrayList<String>();
				for (String ClasstypeId : ClasstypeIds) {
					String tag = classplanId + "_" + ClasstypeId;
					tagList.add(tag);
				}
				//实例化定时任务对象
				ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
				scheduleJob.setBeanName("io.renren.task.PushMsgContentJob");
				scheduleJob.setMethodName("execute");
				StringBuffer sbf = new StringBuffer();
				sbf.append("contentId:");
				sbf.append(contentId+";");
				sbf.append("tagList:");
				sbf.append(StringUtils.strip(tagList.toString(), "[]")+";");
				sbf.append("schoolId:");
				sbf.append(schoolId);
				scheduleJob.setParams(sbf.toString());
//				格式: [秒] [分] [小时] [日] [月] [周] [年]
//				      8   33   11   24   5   ?   2017
				scheduleJob.setCronExpression(DateUtils.format(timestamp, DateUtils.DATE_TIME_PATTERN_FIXEDCRON));
				scheduleJob.setGroupName("消息定时排课推送");
				scheduleJob.setRemark("消息定时排课推送");
				scheduleJobService.save(scheduleJob );
				//添加到消息队列
//				TaskManager.addJob(scheduleJob);
			}
			if(sendType == 1){//立即发送
				//获取标签tag
				String[] ClasstypeIds =  ClassTypeUtils.out(classtypeIds).split(",");
				List<String> tagList = new ArrayList<String>();
				for (String ClasstypeId : ClasstypeIds) {
					String tag = classplanId + "_" + ClasstypeId;
					tagList.add(tag);
				}
				//TODO调用信鸽接口
				xinggePushService.pushTagAndroidOrIos(tagList, contentTitle);
			}
		}
	}
	/**
	 * 消息发送方法(全员推送)
	 * @param pushSend 推送消息
	 * @param sendType 立即发送/定时发送
	 * @param contentId 消息主表id
	 * @param schoolId  平台id
	 * @param timestamp 发送时间
	 * @param contentTitle 消息标题
	 */
	private void sendMsgAllUser(int pushSend, int sendType, Long contentId, String schoolId,  Date timestamp, String contentTitle){
		if(pushSend == 1){//推送消息
			if(sendType == 2){//定时发送
				//实例化定时任务对象
				ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
				scheduleJob.setBeanName("io.renren.task.PushMsgContentJob");
				scheduleJob.setMethodName("execute2");
				StringBuffer sbf = new StringBuffer();
				sbf.append("contentId:");
				sbf.append(contentId+";");
				sbf.append("schoolId:");
				sbf.append(schoolId);
				scheduleJob.setParams(sbf.toString());
//				格式: [秒] [分] [小时] [日] [月] [周] [年]
//			      	  8   33   11   24   5   ?   2017
				scheduleJob.setCronExpression(DateUtils.format(timestamp, DateUtils.DATE_TIME_PATTERN_FIXEDCRON));
				scheduleJob.setGroupName("消息定时全员推送");
				scheduleJob.setRemark("消息定时全员推送");
				scheduleJobService.save(scheduleJob );
				//添加到消息队列
//				TaskManager.addJob(scheduleJob);
			}
			if(sendType == 1){//立即发送
				//TODO调用信鸽接口
				xinggePushService.pushAllAccount(contentTitle);
			}
		}
	}
	private String defUrl(Long id , String url , String html){
		if(StringUtils.isBlank(url)){
			if(StringUtils.isNotBlank(html)){
				return MSG_CONTENT_URL.replace("{id}", id.toString());
			}else{
				return null;
			}
//			return String.format(MSG_CONTENT_URL, id , "%s");
		}
		return url;
	}
}

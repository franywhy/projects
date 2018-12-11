package io.renren.service.impl;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.RecordInfoDao;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.entity.UsersEntity;
import io.renren.pojo.order.OrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.RecordInfoService;
import io.renren.service.RecordReFundsService;
import io.renren.service.RecordSignService;
import io.renren.service.UsersService;
@Service
public class RecordInfoServiceImpl implements RecordInfoService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RecordSignService recordSignService;
	@Autowired
	private RecordReFundsService recordReFundsService;
	@Resource
	KGS userKGS;

	/** 默认密码 hqzk123456 */
	private static String PSW = "1c3f360330c442c3cc62d1608fe7a3a3";
	@Autowired
	private RecordInfoDao recordInfoDao;
	@Autowired
	private UsersService usersService;
	@Override
	public void saveRecordInfo(RecordInfoEntity e) {
		
		//进入保存的有几种情况:1.是导入的学员 在蓝鲸中没有这个学员的 要生成userid 但是ncstudentid为空
		//2.同步来的学员  ncstudentid不为空,但是这个学员之前有导入的 那么在ncstudentid为空, 则导入前要判断userid是否有 有则set userid

		try {
			
			//当usersID为空的 则取users表的 如果电话对得上的 则取userid,如果电话为空的 则创建users
			if (e.getUserId()==null) {
				Map<String,Object> m= new HashMap<String,Object>();
				m.put("mobile", e.getMobile());
				List<UsersEntity> userList = usersService.queryList(m);
				if (userList!=null&&userList.size()>0) {
					 
					e.setUserId(userList.get(0).getUserId());
					if (e.getNcStudentId()==null&&userList.get(0).getNcId()!=null) {
						e.setNcStudentId(userList.get(0).getNcId());
					}
				}else {
					UsersEntity user= new UsersEntity();
					  user.setUserId((long) userKGS.nextId());
                      user.setMobile(e.getMobile());
                      user.setNickName(e.getName());
                      user.setSex(e.getSex());
                      user.setPic("http://zikao.hqjy.com/Avatar.png");
                      user.setEmail(null);
                      user.setPassword(PSW);
                      user.setCreator(null);
                      user.setCreationTime(new Date());
                      user.setModifier(null);
                      user.setModifiedTime(null);
                      user.setLastLoginIp(null);
                      user.setLastLoginTime(null);
                      user.setRemake(null);
                      user.setNcId(e.getNcStudentId());
                      // TODO 部门id
                  //    user.setDeptId(deptEntity.getDeptId());
                      logger.info("create user: {}",user);
                      try {
                      usersService.save(user);
                      e.setUserId(user.getUserId());
                      }catch (Exception es) {
                    	  logger.error("RecordInfoEntity create user has Error.userEntity={},error_message={}", user, es);
                      }
				}
 
			} 
			
			
			recordInfoDao.save(e);
			checkUsers(e);
			logger.info("RecordInfoEntity   saved successfully.RecordSignEntity={} ", e);
		} catch (Exception es) {
			es.printStackTrace();
			logger.error("RecordInfoEntity save has Error.RecordInfoEntity={},error_message={}", e, es);

		}
	}

	@Override
	public List<RecordInfoEntity> queryList(Map<String, Object> queryMap) {
		return  recordInfoDao.queryList(queryMap);
	}
	@Override
	public RecordInfoEntity queryObject(Map<String, Object> queryMap) {
		return  recordInfoDao.queryObject(queryMap);
	}
	

	@Override
	public void upDateRecordInfo(RecordInfoEntity e) {
		try {

			recordInfoDao.update(e);
			checkUsers(e);
			logger.info("RecordInfoEntity   updated successfully.RecordSignEntity={} ", e);
		} catch (Exception es) {
			logger.error("RecordInfoEntity updated has Error.RecordInfoEntity={},error_message={}", e, es);

		}

	}

	public void checkUsers(RecordInfoEntity e) {
		try {
		if (e.getUserId()!=null) {
			String mobile=e.getMobile();
			String nick_name=e.getName();
			int sex=e.getSex()==null?2:e.getSex();
			String ncId=e.getNcStudentId(); 
			UsersEntity users = usersService.queryObject(e.getUserId());
			if (users!=null) {
				boolean isChange=false;
				if (mobile!=null&&!mobile.equals(users.getMobile())) {
					users.setMobile(mobile);
					isChange=true;
				}
				if (nick_name!=null&&!nick_name.equals(users.getNickName())) {
					users.setNickName(nick_name);isChange=true;
				}
				if (sex!=users.getSex()) {
				users.setSex(sex);isChange=true;
				}
				if (ncId!=null&&!ncId.equals(users.getNcId())) {
					users.setNcId(ncId);isChange=true;
				}
				
				if (isChange) {
					usersService.update(users);
				}
				
				
				
			}
			
			
			
			
		}
		}catch(Exception es) {
			logger.error("RecordInfoEntity update usersEntity has errors! the RecordInfoEntity is{},errorMessage is {}",e,es);
		}
	}
	
	
	
	
	@Override
	public String RecordInfoImport(List<String[]> dateList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream RecordInfoExport(List<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordInfoEntity getRecordInfo(MallOrderEntity m, OrderMessageConsumerEntity message) {
		RecordInfoEntity r= new RecordInfoEntity(message,m);
		
		
		
		return r;
	}

	@Override
	public void CheckRecordInfo(MallOrderEntity m, OrderMessageConsumerEntity message) {
/**
 * 1.根据来源报名表消息和订单 生成学员档案基础信息
 * 2.判断学员档案是否已经存在 用nc_student_Id来判断
 */
		boolean isUpdate=false;//用来判断那些根据ncStudentid不存在的但是用userid存在的 
		RecordInfoEntity r=	this.getRecordInfo(m, message);
		Map<String, Object> queryMap= new HashMap<String, Object>();
		if (message!=null&&message.getNc_user_id()!=null) {
			queryMap.put("ncStudentId", message.getNc_user_id());
			List<RecordInfoEntity> old_r=	this.queryList(queryMap);
			if (old_r!=null&&old_r.size()>0) {//说明学员档案已经存在的了 
				for (RecordInfoEntity info:old_r) {
					if (info.getNcStudentId().equals(r.getNcStudentId())) {
						//旧的变更为新的信息 
						info.infoUpdate(info,r);
						isUpdate=true;
						this.upDateRecordInfo(info);
						//报读信息检测并生成
						recordSignService.RecordSignCheck(info, message,m);
						//退费信息检测并生成
						recordReFundsService.RecordRefundCheck(info, message,m);
					}
					
				}
			}
		}
		if (!isUpdate) {
			
			if (r.getUserId()!=null) {
			Map<String, Object> queryMapUser= new HashMap<String, Object>();
			queryMapUser.put("userId", r.getUserId());
			List<RecordInfoEntity> userList=	this.queryList(queryMapUser);
			if (userList!=null&&userList.size()>0) {
				for (RecordInfoEntity rs:userList) {
					if (rs.getUserId()!=null&&r.getUserId()!=null&&rs.getUserId().longValue()== r.getUserId().longValue() ) {
						rs.setUserId(r.getUserId());
						rs.infoUpdate(rs, r);
						this.upDateRecordInfo(rs);
						isUpdate=true;
						r=rs;

						//报读信息检测并生成
						recordSignService.RecordSignCheck(r, message,m);
						//退费信息检测并生成
						recordReFundsService.RecordRefundCheck(r, message,m);
						break;
					}
				}
				
			}
			
			}
			
			
			
			
		}
		if (!isUpdate) { //如果是已经存在的则不再创建了
			this.saveRecordInfo(r);

			//报读信息检测并生成
			recordSignService.RecordSignCheck(r, message,m);
			//退费信息检测并生成
			recordReFundsService.RecordRefundCheck(r, message,m);
		}
		
		
	}

	@Override
	public int queryTotal(Map<String, Object> queryMap) {
		if(queryMap.get("name") != null || queryMap.get("mobile") != null || queryMap.get("teacherId") != null ){
			queryMap.put("condition","yes");
		}
		return recordInfoDao.queryTotal(queryMap);
	}

	@Override
	public int upDateOrSaveByMobile(RecordInfoEntity e) {
		if (e.getMobile()==null) {
			return 0;
		}
		Map<String, Object> queryMap= new HashMap<String, Object>();
		queryMap.put("mobile", e.getMobile());
		List<RecordInfoEntity> old_r=	this.queryList(queryMap);	
		
		//第一种情况 根据号码关联上的
		if (old_r!=null&&old_r.size()>0) {
			for (RecordInfoEntity r:old_r) {
				if (e.getMobile().equals(r.getMobile())) {
					r.infoUpdate(r, e);
					this.upDateRecordInfo(r);
					return 2;
				}
			}
		}else {
			//第二根据号码查不到的 就是有users信息但没有关联recrordinfo的 在saveRecordInfo方法中会再检测的
			this.saveRecordInfo(e);
			return 1;
			
		}
		
	
		return 0;
	}

	@Override
	public void CheckRecordInfoByOrder(MallOrderEntity m) {
		try {
		 
		
		
		this.CheckRecordInfo(m, null);
		}catch (Exception es) {
			es.printStackTrace();
			logger.error("RecordInfoEntity create by MallOreder has errors! the MallOrderEntity is{},errorMessage is {}",m,es);

		}
	}

}

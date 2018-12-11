package io.renren.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

import io.renren.entity.DeptAndSchoolConsumerEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.SysDeptEntity;
import io.renren.entity.SysSchoolEntity;
import io.renren.entity.SysSchoolSyncEntity;
import io.renren.service.SysDeptService;
import io.renren.service.SysSchoolService;
import io.renren.service.SysSchoolSyncService;

public class DeptAndSchoolConsumerController implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysSchoolService sysSchoolService;
	@Autowired
	private SysSchoolSyncService sysSchoolSyncService;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		logger.info("部门-校区同步接收队列DeptAndSchoolConsumer" + " [x] Received '" + message.getBody() + "'");
		DeptAndSchoolConsumerEntity consumerEntity = null;
		SysSchoolSyncEntity sysSchoolSyncEntity = new SysSchoolSyncEntity();
		TransactionStatus status = null;
		// 开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		String body = new String(message.getBody(), "UTF-8");
		try {
			 Gson gson = new Gson();
			 consumerEntity = gson.fromJson(body, DeptAndSchoolConsumerEntity.class);
			 
			 if(consumerEntity.getDr() == 0){//新增
				 
				 SysDeptEntity parentDeptEntity = this.sysDeptService.queryObjectByNcId(consumerEntity.getNc_parent_id());
				 SysDeptEntity deptEntity = this.sysDeptService.queryObjectByNcId(consumerEntity.getNc_id());
				 
				 SysSchoolEntity cityEntity = this.sysSchoolService.queryObjectByCity(consumerEntity.getCity());
				 SysSchoolEntity schoolEntity = this.sysSchoolService.queryObjectByNcId(consumerEntity.getNc_id());
				 //保存异常信息
				 if(null == parentDeptEntity){
					 sysSchoolSyncEntity.setErrType(1);//错误类型1：部门表找不到上级 2：校区表找不到城市 3：系统异常
					 this.saveDeptAndSchoolSync(sysSchoolSyncEntity, consumerEntity, body);
				 }
				 if(null == cityEntity){
					 sysSchoolSyncEntity.setErrType(2);//错误类型1：部门表找不到上级 2：校区表找不到城市 3：系统异常
					 this.saveDeptAndSchoolSync(sysSchoolSyncEntity, consumerEntity, body);
				 }
				 
				 //部门
				 if(null != parentDeptEntity && null == deptEntity){//新增部门
					 SysDeptEntity saveDept = new SysDeptEntity();
					 saveDept.setParentId(parentDeptEntity.getDeptId());
					 saveDept.setName(consumerEntity.getName());
					 saveDept.setOrderNum(0);
					 saveDept.setNcId(consumerEntity.getNc_id());
					 saveDept.setNcCode(consumerEntity.getCode());
					 saveDept.setNcParentId(consumerEntity.getNc_parent_id());
					 this.sysDeptService.save(saveDept);
				 }
				 if(null != parentDeptEntity && null != deptEntity){//更新部门
					 SysDeptEntity updateDept = new SysDeptEntity();
					 updateDept.setDeptId(deptEntity.getDeptId());
					 updateDept.setParentId(parentDeptEntity.getDeptId());
					 updateDept.setName(consumerEntity.getName());
					 updateDept.setOrderNum(0);
					 updateDept.setNcId(consumerEntity.getNc_id());
					 updateDept.setNcCode(consumerEntity.getCode());
					 updateDept.setNcParentId(consumerEntity.getNc_parent_id());
					 this.sysDeptService.update(updateDept);
				 }
				 
				 //校区
				 if(null != cityEntity && null == schoolEntity){//新增校区
					 SysSchoolEntity saveSchool = new SysSchoolEntity();
					 saveSchool.setParentId(cityEntity.getId());
					 saveSchool.setName(consumerEntity.getName());
					 saveSchool.setPic(null);
					 saveSchool.setTelephone(consumerEntity.getTelephone());
					 saveSchool.setAddress(consumerEntity.getAddress());
                     saveSchool.setLongitude(999.00);
                     saveSchool.setLatitude(99.00);
					 saveSchool.setCode(consumerEntity.getCode());
					 saveSchool.setNcId(consumerEntity.getNc_id());
					 saveSchool.setType(2);
					 saveSchool.setBusinessId(consumerEntity.getCompany_type());
					 this.sysSchoolService.save(saveSchool);
				 }
				 if(null != cityEntity && null != schoolEntity){//更新校区
					 SysSchoolEntity updateSchool = new SysSchoolEntity();
					 updateSchool.setId(schoolEntity.getId());
					 updateSchool.setParentId(cityEntity.getId());
					 updateSchool.setName(consumerEntity.getName());
					 updateSchool.setTelephone(consumerEntity.getTelephone());
					 updateSchool.setAddress(consumerEntity.getAddress());
					 updateSchool.setLongitude(consumerEntity.getLocation().get("longitude") == null ? 999.00: (Double) consumerEntity.getLocation().get("longitude"));
					 updateSchool.setLatitude(consumerEntity.getLocation().get("latitude") == null ? 99.00 : (Double) consumerEntity.getLocation().get("latitude"));
					 updateSchool.setCode(consumerEntity.getCode());
					 updateSchool.setNcId(consumerEntity.getNc_id());
					 updateSchool.setBusinessId(consumerEntity.getCompany_type());
					 this.sysSchoolService.update(updateSchool);
				 }
				 
			 }
			 else{//删除
				 this.sysDeptService.deleteByNcId(consumerEntity.getNc_id());
				 this.sysSchoolService.deleteByNcId(consumerEntity.getNc_id());
			 }
			
		} catch (Exception e) {
			try {
				// 回滚
				transactionManager.rollback(status);
				
				sysSchoolSyncEntity.setErrType(3);//错误类型1：部门表找不到上级 2：校区表找不到城市 3：系统异常
				this.saveDeptAndSchoolSync(sysSchoolSyncEntity, consumerEntity, body);
                
				e.printStackTrace();
                logger.error("DeptAndSchoolConsumerController onMessage Cause by:{}", e.getMessage());
			} catch (Exception e2) {
				sysSchoolSyncEntity.setErrType(3);//错误类型1：部门表找不到上级 2：校区表找不到城市 3：系统异常
				this.saveDeptAndSchoolSync(sysSchoolSyncEntity, consumerEntity, body);
				
				e2.printStackTrace();
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		} finally{
			try {
				 // 提交事务
                transactionManager.commit(status);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e2) {
				sysSchoolSyncEntity.setErrType(3);//错误类型1：部门表找不到上级 2：校区表找不到城市 3：系统异常
				this.saveDeptAndSchoolSync(sysSchoolSyncEntity, consumerEntity, body);
				logger.error("DeptAndSchoolConsumerController onMessage Cause by:{}", e2.getMessage());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		}
	}
	
	//保存异常信息
	private void saveDeptAndSchoolSync(SysSchoolSyncEntity syncEntity, DeptAndSchoolConsumerEntity consumerEntity, String body){
		syncEntity.setCode(consumerEntity.getCode());
		syncEntity.setNcId(consumerEntity.getNc_id());
		syncEntity.setName(consumerEntity.getName());
		syncEntity.setNcJson(body);
		
		this.sysSchoolSyncService.save(syncEntity);
	}

}

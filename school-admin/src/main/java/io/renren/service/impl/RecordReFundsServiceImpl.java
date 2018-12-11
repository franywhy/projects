package io.renren.service.impl;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.renren.dao.RecordRefundsDao;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.entity.RecordReFundsEntity;
import io.renren.entity.SysUserEntity;
import io.renren.service.RecordReFundsService;
import io.renren.utils.ExcelReaderJXL;

@Service
public class RecordReFundsServiceImpl implements RecordReFundsService {
	@Autowired
	private RecordRefundsDao recordRefundsDao;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void saveRecordRefund(RecordReFundsEntity re) {
		try {

			recordRefundsDao.save(re);
			logger.info("RecordReFundsEntity   saved successfully.RecordSignEntity={} ", re);
		} catch (Exception es) {
			logger.error("RecordReFundsEntity save has Error.RecordReFundsEntity={},error_message={}", re,
					es);

		}
	}

	@Override
	public List<RecordReFundsEntity> queryList(Map<String, Object> queryMap) {
		
		try {

			return 	recordRefundsDao.queryList(queryMap);
	//		logger.info("RecordReFundsEntity   saved successfully.RecordSignEntity={} ", re);
		} catch (Exception es) {
			es.printStackTrace();
			logger.error("RecordReFundsEntity query has Error.queryMap={},error_message={}", queryMap,
					es);
return null;
		}
		
	
				
			
	}

	@Override
	public void RecordRefundCheck(RecordInfoEntity r, OrderMessageConsumerEntity order, MallOrderEntity m) {
		//1.判断是订单消息是否有退费子表
		if (order!=null&&order.getRecordRefunds()!=null) {
			RecordReFundsEntity re= order.getRecordRefunds();
			 re.setInNCSync(true);
			re.setApplyStatus(0);
			re.setCreateTime(new Date());
			re.setRecordId(r.getRecordId());
			if (m!=null&&m.getOrderId()!=null&&m.getOrderId()>0) {
				re.setOrderId(m.getOrderId());
			}
			
			re.setUserId(r.getUserId());
			
		//2.查询是否已经有存在的退费单  	 如果有存在则更新信息
			Map<String, Object> queryMap= new HashMap<String,Object>();
			queryMap.put("ncId", re.getNcId());
			List<RecordReFundsEntity> old_re=	this.simpleQueryList(queryMap);
			
			if (old_re!=null&&old_re.size()>0) {
				for (RecordReFundsEntity old:old_re) {
					/**
					 * 更新旧定单中的信息
					 */
					old.refundUpdate(old,re);
					this.upDateRecordRefund(old);
				}
				
			}else {
		//3.如果没有信息则说明是第一次同步的退费单 直接生成退费信息		
				this.saveRecordRefund(re);
			}
		} 
	}

	@Override
	public void upDateRecordRefund(RecordReFundsEntity re) {
		try {

			recordRefundsDao.update(re);
			logger.info("RecordReFundsEntity   updated successfully.RecordSignEntity={} ", re);
		} catch (Exception es) {
			logger.error("RecordReFundsEntity updated has Error.RecordReFundsEntity={},error_message={}", re,
					es);

		}

	}

	@Override
	public List<RecordReFundsEntity> findReFunds(String name, String mobile) {
		if (name ==null&&mobile==null) {
			return null;
		}
		Map<String, Object> queryMap= new HashMap<String,Object>();
		queryMap.put("name", name);
		if (mobile!=null&&mobile.trim().length()>10) {

			queryMap.put("mobile", mobile);
		}
	   return this.simpleQueryList(queryMap);
	}
	@Override
	public  List<RecordReFundsEntity> simpleQueryList(Map<String, Object> queryMap) {
		return recordRefundsDao.simpleQueryList(queryMap);
	}

	@Override
	public int queryTotal(Map<String, Object> queryMap) {
		try {
			return  recordRefundsDao.queryTotal(queryMap);
		} catch (Exception es) {
			logger.error("RecordReFundsEntity find queryTotal has Error. queryMap={},error_message={}", queryMap,
					es);
			return 0;
		}
	}

	@Override
	@Transactional
	public int importData(MultipartFile file, SysUserEntity userInfo) throws Exception {
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
		int num = 0;
		for(int i = 1 ; i < dataList.size() ; i++){
			String msg = "第" + i + "行，";
			String[] array = dataList.get(i);
			String orderNo = array[3]; //订单编号
			String refundsResult = array[6]; //退费处理结果
			if(StringUtils.isBlank(orderNo) || orderNo.trim().length() == 0){
				throw new Exception(msg + "订单编号不能为空"); 
			}
			orderNo = orderNo == null ? null : orderNo.trim();
			refundsResult = refundsResult == null ? null : refundsResult.trim(); 
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("orderNo", orderNo);
			List<RecordReFundsEntity> recordRefundsList = queryList(queryMap);
			if(recordRefundsList.size() == 0){
				throw new Exception(msg + "找不到对应的订单编号"); 
			}
			RecordReFundsEntity entity = recordRefundsList.get(0);
			entity.setRefundsResult(refundsResult);
			entity.setTs(new Date());
			recordRefundsDao.update(entity);
			num++;
		}
		return num;
	}
 

}

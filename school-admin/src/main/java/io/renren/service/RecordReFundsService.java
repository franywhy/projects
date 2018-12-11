package io.renren.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.entity.RecordReFundsEntity;
import io.renren.entity.SysUserEntity;

public interface RecordReFundsService {
	void saveRecordRefund(RecordReFundsEntity re);
	void upDateRecordRefund(RecordReFundsEntity re);
	/**
	 *  按条件查询 
	 *@param queryMap
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	List< RecordReFundsEntity> queryList(Map<String,Object> queryMap);
	
	/**
	 * 根据条件查询退费的总数 
	 *@param queryMap
	 *@return 总数数量
	 * @author lintf
	 * 2018年8月13日
	 */
	int queryTotal(Map<String,Object> queryMap);
	
	/**
	 * 根据姓名或者电话查询学员退费信息
	 *@param name 学员名称  
	 *@param mobile 电话号码
	 *@return
	 * @author lintf
	 * 2018年8月11日
	 */
	List< RecordReFundsEntity> findReFunds(String name,String mobile);
	
	
	
	
	/**
	 * 退费信息的总体检测,判断是否要生成退费信息或者修改退费信息
	 *@param r
	 *@param order
	 *@return
	 * @author lintf
	 * 2018年7月31日
	 */
	void RecordRefundCheck(RecordInfoEntity r, OrderMessageConsumerEntity order, MallOrderEntity m);
	/**
	 * 单表查询  只查学员档案中的退费信息  
	 *@param queryMap
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	List<RecordReFundsEntity> simpleQueryList(Map<String, Object> queryMap); 
	
	public int importData(MultipartFile file,SysUserEntity userInfo) throws Exception;	
}

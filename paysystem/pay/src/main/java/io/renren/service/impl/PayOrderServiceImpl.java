package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.PayOrderDao;
import io.renren.entity.PayOrderEntity;
import io.renren.service.PayOrderService;
import io.renren.utils.R;
import io.renren.utils.weixin.RandomStringGenerator;



@Service("payOrderService")
public class PayOrderServiceImpl implements PayOrderService {
	@Autowired
	private PayOrderDao payOrderDao;
	
	@Override
	public PayOrderEntity queryObject(Map<String, Object> map){
		return payOrderDao.queryObject(map);
	}
	
	@Override
	public List<PayOrderEntity> queryList(Map<String, Object> map){
		return payOrderDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return payOrderDao.queryTotal(map);
	}
	
	@Override
	public void save(PayOrderEntity payOrder){
        //nc传过来订单号		
		payOrder.setOrderNo(payOrder.getOrderNo());
		//订单名称
		payOrder.setOrderName(payOrder.getOrderName());
		//支付状态：0：支付成功 1：待支付
		payOrder.setState(1);
		//创建订单时间
		payOrder.setCreateTime(new Date());
	    //交易金额
		payOrder.setTradeMoney(payOrder.getTradeMoney());
		//商品图片
		payOrder.setGoodsPic(payOrder.getGoodsPic());
		//nc参数
	    payOrder.setParam(payOrder.getParam());
	    //时间戳 用于加密作用
	    payOrder.setOrderTimestamp(payOrder.getOrderTimestamp());
	    //平台标志
	    payOrder.setTerrace(payOrder.getTerrace());
		payOrderDao.save(payOrder);
	}
	
	@Override
	public void update(PayOrderEntity payOrder){
		payOrderDao.update(payOrder);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		payOrderDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		payOrderDao.deleteBatch(map);
	}

	@Override
	public PayOrderEntity judgeOrderPaySucceed(PayOrderEntity payOrder) {
		return payOrderDao.judgeOrderPaySucceed(payOrder);
	}

	@Override
	public int judgeWeiXinOrderPaySucceed(PayOrderEntity payOrder) {
		return payOrderDao.judgeWeiXinOrderPaySucceed(payOrder);
	}

	@Override
	public int payOrderExist(String orderNo) {
		return payOrderDao.payOrderExist(orderNo);
	}

}

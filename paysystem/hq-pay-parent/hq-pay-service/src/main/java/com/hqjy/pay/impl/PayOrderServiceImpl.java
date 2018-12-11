package com.hqjy.pay.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqjy.pay.PayOrderEntity;
import com.hqjy.pay.PayOrderService;
import com.hqjy.pay.mapper.PayOrderDao;

import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PayOrderServiceImpl implements PayOrderService {
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PayOrderDao payOrderDao;

	@Override
	public PayOrderEntity queryObject(Map<String, Object> map) {
		return payOrderDao.queryObject(map);
	}

	@Override
	public void save(PayOrderEntity payOrder) {
		payOrder.setCreateTime(sdf.format(new Date()));
		payOrder.setState(1);
		payOrderDao.save(payOrder);
	}

	@Override
	public void update(PayOrderEntity payOrder) {
		// 修改支付成功状态 ：0 ：成功 1：失败
		payOrder.setState(0);
		// 支付成功创建时间
		payOrder.setOrderPaySucceedTime(new Date());
		payOrderDao.update(payOrder);
	}

	@Override
	public PayOrderEntity judgeOrderPaySucceed(PayOrderEntity payOrder) {
		return payOrderDao.judgeOrderPaySucceed(payOrder);
	}

	@Override
	public PayOrderEntity queryOrderNo(Map<String, Object> map) {
		return payOrderDao.queryOrderNo(map);
	}

	@Override
	public void updatePayOrderNo(PayOrderEntity payOrder) {
		payOrder.setUpdateTime(sdf.format(new Date()));
		payOrderDao.updatePayOrderNo(payOrder);
	}

}

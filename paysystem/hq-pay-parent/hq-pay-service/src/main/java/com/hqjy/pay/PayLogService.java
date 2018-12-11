package com.hqjy.pay;


import java.util.List;
import java.util.Map;

import com.hqjy.pay.PayLogEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-11 11:57:44
 */
public interface PayLogService {
	PayLogEntity queryObject(Map<String, Object> map);
	
	void save(PayLogEntity payLog);
	
	void update(PayLogEntity payLog);
	
	boolean queryTotal(String tradeNo);
}

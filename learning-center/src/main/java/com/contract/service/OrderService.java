package com.contract.service;

import java.util.List;
import java.util.Map;

import com.school.pojo.OrderPOJO;

public interface OrderService {
 
	List<OrderPOJO> queryOrderList(Map<String,Object> map);
  
	Integer queryTotalCount(Map<String,Object> map);
 
}

package com.hq.learningcenter.contract.service.impl;

import com.hq.learningcenter.contract.service.OrderService;
import com.hq.learningcenter.school.dao.MallOrderDao;
import com.hq.learningcenter.school.pojo.OrderPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    protected final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    MallOrderDao mallOrderDao;

    @Override
    public List<OrderPOJO> queryOrderList(Map<String,Object> map){
        try{
        	List<OrderPOJO> pojoList = mallOrderDao.queryList(map);
            return pojoList;
        }catch (Exception ex){
            logger.error(ex.toString());
            return null;
        }
    }

    @Override
    public Integer queryTotalCount(Map<String, Object> map) {
        try {
            return mallOrderDao.queryTotalCount(map);
        }catch (Exception ex){
            logger.error(ex.toString());
            return null;
        }
    }
 
}

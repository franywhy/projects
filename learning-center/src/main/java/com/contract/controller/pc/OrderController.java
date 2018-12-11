package com.contract.controller.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contract.service.OrderService;
import com.google.common.collect.Lists;
import com.school.controller.AbstractBaseController;
import com.school.pojo.OrderPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Controller
@Api(description = "个人中心订单列表接口")
@RequestMapping("/order")
public class OrderController extends AbstractBaseController{
	
    @Autowired
    private OrderService orderService;
	 
    @ApiImplicitParams({
        @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
   @ResponseBody
   @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getOrderList(HttpServletRequest request, HttpServletResponse response) {
    	UserInfoPOJO userInfo = this.getUserInfo(request,response);
    	if(userInfo != null && userInfo.getUserId() != null){
    		List<Integer> payStatusList = Lists.newArrayList();
	        payStatusList.add(2); //已支付
	        Map<String,Object> map = new HashMap<>();  
	        
	        map.put("userId", userInfo.getUserId());
	        map.put("payStatus",payStatusList); 
	        
	        List<OrderPOJO> resultList = orderService.queryOrderList(map);
	        Integer totalCount = orderService.queryTotalCount(map);  
	        Map<String,Object> result = new HashMap<>();
	        result.put("resultList", resultList);
	        result.put("totalCount", totalCount);
    	        
    	        return this.success(result); 
    	}else{
    		return this.error("用户信息查询失败");
    	}
   }
}

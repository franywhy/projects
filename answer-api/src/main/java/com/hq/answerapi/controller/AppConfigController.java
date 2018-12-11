package com.hq.answerapi.controller;

import com.hq.answerapi.entity.AppConfigEntity;
import com.hq.answerapi.pojo.AppConfigPOJO;
import com.hq.answerapi.service.AppConfigService;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app常量记录表
 * 
 * @author zhaowenwei
 * @date 2018-10-09 14:58:44
 */
@RestController
@RequestMapping("/api/appconfig")
@Api(description = "获取常量接口")
public class AppConfigController extends AbstractRestController{
	@Autowired
	private AppConfigService appConfigService;
	
	/**
	 * 列表
	 */
	@ApiOperation(value = "获取常量列表")
	 @ApiImplicitParams({
         @ApiImplicitParam(name = "version", value = "版本号(员工端1001开始)", required = true, dataType = "Integer", paramType = "query"),
         @ApiImplicitParam(name = "facility", value = "设备(ios/android)", required = true, dataType = "String", paramType = "query"),
         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
 })
	@PostMapping(value = "/constantValue")
	public ResponseEntity<WrappedResponse> constantValueList(HttpServletRequest request){
		Integer version = ServletRequestUtils.getIntParameter(request, "version", 1001);
		String facility = ServletRequestUtils.getStringParameter(request, "facility", "");
		String token = ServletRequestUtils.getStringParameter(request, "token", "");

		//查询列表数据
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("dr", 0);
		params.put("version", version);
		params.put("facility", facility);
		
		List<AppConfigEntity> appConfigList = appConfigService.queryList(params);
		//把数据格式化后输出
		List<AppConfigPOJO> result = new ArrayList<AppConfigPOJO>();
		for (int i=0;i<appConfigList.size();i++) {
			AppConfigPOJO appConfigPOJO = new AppConfigPOJO();
			appConfigPOJO.setCkey(appConfigList.get(i).getKey());
			appConfigPOJO.setName(appConfigList.get(i).getValue());
            String url = appConfigList.get(i).getUrl();
            appConfigPOJO.setCvalue(url);
			result.add(appConfigPOJO);
		}
		return this.success(result);
	}
}

package com.hq.learningapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.AppConfigEntity;
import com.hq.learningapi.pojo.AppConfigPOJO;
import com.hq.learningapi.service.AppConfigService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * app常量记录表
 * 
 * @author zhaowenwei
 * @date 2018-02-26 14:58:44
 */
@RestController
@RequestMapping("/appconfig")
@Api(description = "获取常量接口")
public class AppConfigController extends AbstractRestController{
	@Autowired
	private AppConfigService appConfigService;
	
	/**
	 * 列表
	 */
	@ApiOperation(value = "获取常量列表")
	 @ApiImplicitParams({
         @ApiImplicitParam(name = "version", value = "版本号", required = true, dataType = "int", paramType = "query"),
         @ApiImplicitParam(name = "facility", value = "设备(ios/android)", required = true, dataType = "String", paramType = "query"),
         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
 })
	@PostMapping(value = "/constantValue")
	public ResponseEntity<WrappedResponse> constantValueList(HttpServletRequest request){
		Integer version = ServletRequestUtils.getIntParameter(request, "version", 1);
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
           //根据token有无拼接不同的地址
           if (StringUtils.isNotBlank(url) && url.contains(";")){
                String[] urlsplit = url.split(";");
                if (StringUtils.isNotBlank(token)){
                   /* url = urlsplit[1];*/
                    url = urlsplit[1]+token;
                }else {
                    url = urlsplit[0];
                }
            }
            appConfigPOJO.setCvalue(url);
			result.add(appConfigPOJO);
		}
		return this.success(result);
	}

	/*
	*//**
	 * 信息
	 *//*
	@ApiOperation(value = "获取常量列表")
	@RequestMapping("/info/{id}")
	public ResponseEntity<WrappedResponse> info(@PathVariable("id") Long id){
		AppConfigEntity appConfig = appConfigService.queryObject(id);
		return this.success(appConfig);
	}
	
	*//**
	 * 保存
	 *//*
	@RequestMapping("/save")
	@RequiresPermissions("generator:appconfig:save")
	public R save(@RequestBody AppConfigEntity appConfig){
		appConfigService.save(appConfig);
		
		return R.ok();
	}
	
	*//**
	 * 修改
	 *//*
	@RequestMapping("/update")
	@RequiresPermissions("generator:appconfig:update")
	public R update(@RequestBody AppConfigEntity appConfig){
		appConfigService.update(appConfig);
		
		return R.ok();
	}
	
	*//**
	 * 删除
	 *//*
	@RequestMapping("/delete")
	@RequiresPermissions("generator:appconfig:delete")
	public R delete(@RequestBody Long[] ids){
		appConfigService.deleteBatch(ids);
		
		return R.ok();
	}
	*/
}

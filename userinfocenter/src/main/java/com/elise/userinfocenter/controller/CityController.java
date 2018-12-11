package com.elise.userinfocenter.controller;

import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.UserInfoPOJO;
import com.elise.userinfocenter.service.CityService;
import com.hq.app.event.AppCityEvent;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 城市(地区)管理接口
 * Created by DL on 2017/12/28.
 */
@Controller
@RequestMapping("/api/")
public class CityController extends AbstractRestController {

    private ApplicationContext applicationContext;
    public CityController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private HttpConnManager httpConnManager;
    @Autowired
    private LocalConfigProperties config;
    @Autowired
    private CityService cityService;


    @ApiOperation(value = "获取城市列表服务器地址")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getCityFileUrl", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Map<String,Object>>> getCityFileUrl(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            return this.success(cityService.getCityFileUrl(),TransactionStatus.OK);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "获取城市列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "syncTime", value = "时间戳", required = false, dataType = "Long", paramType = "query")
    })

    @RequestMapping(value = "/getCityList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Map<String,Object>>> getCityList(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            Long syncTime = ServletRequestUtils.getLongParameter(request, "syncTime", 0L);
            Long maxTsTime = cityService.getMaxTsTime().getTime();
            if (syncTime < maxTsTime){
                Map dataMap = new HashMap();
                dataMap.put("syncTime",maxTsTime);
                dataMap.put("cityList",cityService.getCityList());
                return this.success(dataMap,TransactionStatus.OK);
            }else
                return this.success(null,TransactionStatus.OK);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "获取用户城市")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getCity", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Map<String,Object>>> getCity(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if(token != null) {
                map.put("token", token);
            }
            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
            if(entry.isOK()) {
                Long userId = Long.valueOf(entry.getResult().getUid());
                return this.success(cityService.getCity(userId),TransactionStatus.OK);
            } else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
            return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "保存或修改用户城市")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cityName", value = "城市名称", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/saveOrUpdateCity", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> saveOrUpdate(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String cityCode = ServletRequestUtils.getStringParameter(request, "cityCode", "");
            String cityName = ServletRequestUtils.getStringParameter(request, "cityName", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if(token != null) {
                map.put("token", token);
            }
            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
            if(entry.isOK()) {
                //先判断是否存在
                Long userId = Long.valueOf(entry.getResult().getUid());
                boolean isExist = cityService.isExistCity(userId);
                if (isExist){
                        cityService.update(userId,cityCode,cityName);
                }else {
                        cityService.save(userId,cityCode,cityName);
                }
                //用户城市改变推送事件
                try {
                    AppCityEvent cityEvent = new AppCityEvent(this, applicationContext.getId());
                    cityEvent.setUserId(userId);
                    cityEvent.setCityCode(cityCode);
                    cityEvent.setCityName(cityName);
                    applicationContext.publishEvent(cityEvent);
                    TRACER.info("CityController publishEvent msg: {}",cityEvent.toString());
                }catch (Exception e){
                    TRACER.info("城市事件发布异常: cause {}",e.getMessage());
                }
                return this.success(TransactionStatus.OK);
            } else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
            return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

}

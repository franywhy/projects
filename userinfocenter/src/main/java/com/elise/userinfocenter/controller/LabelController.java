package com.elise.userinfocenter.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.elise.userinfocenter.entity.LabelEntity;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.LabelPOJO;
import com.elise.userinfocenter.pojo.UserInfoPOJO;
import com.elise.userinfocenter.service.LabelService;
import com.hq.app.event.AppLabelEvent;
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
import java.util.HashMap;
import java.util.List;

/**
 * 标签管理接口
 * Created by DL on 2017/12/29.
 */
@Controller
@RequestMapping("/api/")
public class LabelController extends AbstractRestController {

    private ApplicationContext applicationContext;
    public LabelController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @Autowired
    private LabelService labelService;

    @ApiOperation(value = "获取标签列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "产品线", required = true, dataType = "Long", paramType = "query")
    })
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/queryLabelList", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<List<LabelPOJO>>> getCityFileUrl(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long productId = ServletRequestUtils.getLongParameter(request, "productId", 0L);
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
                return this.success(labelService.queryLabelList(productId), TransactionStatus.OK);
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
    
    /*
    @ApiOperation(value = "保存或修改我的标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "labelIdString", value = "标签id列表字符串(使用,做间隔)", required = true, dataType = "String", paramType = "query")
    })
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/saveOrUpdateLabel", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> saveOrUpdate(HttpServletRequest request, HttpServletResponse response){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String labelIdString = ServletRequestUtils.getStringParameter(request, "labelIdString", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            if (StringUtils.isBlank(labelIdString)) {
                return this.error("请至少选择一个标签");
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
                boolean isExist = labelService.isExistLabel(userId);
                if (isExist){
                    labelService.delete(userId);
                    labelService.save(userId,labelIdString);
                }else {
                    labelService.save(userId,labelIdString);
                }
                //用户标签变化推送事件
                try{
                    List<LabelPOJO> label = labelService.getLabel(userId);
                    if (label.size() > 0 ){
                        for (LabelPOJO labelPOJO : label) {
                            AppLabelEvent labelEvent = new AppLabelEvent(this, applicationContext.getId());
                            labelEvent.setUserId(userId);
                            labelEvent.setDr(labelPOJO.getDr());
                            labelEvent.setLabelId(labelPOJO.getId());
                            labelEvent.setLabelName(labelPOJO.getLabelName());
                            labelEvent.setProductId(labelPOJO.getProductId());
                            applicationContext.publishEvent(labelEvent);
                            TRACER.info("LabelController publishEvent msg: {}",labelEvent.toString());
                        }
                    }
                }catch (Exception e){
                    TRACER.error("标签事件发布异常: cause {}", e.getMessage());
                }
                return this.success( TransactionStatus.OK);
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
    */
    
    @ApiOperation(value = "保存或修改我的标签")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "labelIdString", value = "标签id列表字符串(使用,做间隔)", required = true, dataType = "String", paramType = "query")
    })
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/saveOrUpdateLabel", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> saveOrUpdate(HttpServletRequest request, HttpServletResponse response){
    	try {
    		String token = ServletRequestUtils.getStringParameter(request, "token", "");
    		String labelIdString = ServletRequestUtils.getStringParameter(request, "labelIdString", "");
    		if (StringUtils.isBlank(token)) {
    			return this.error("token 不可以为空");
    		}
    		if (StringUtils.isBlank(labelIdString)) {
    			return this.error("请至少选择一个标签");
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
    			List<LabelPOJO> labelList = labelService.getLabel(userId);
    			if (null != labelList && labelList.size() > 0){
    				//用户标签变化推送事件（删除）
        			try{
    					for (LabelPOJO labelPOJO : labelList) {
    						AppLabelEvent labelEvent = new AppLabelEvent(this, applicationContext.getId());
    						labelEvent.setUserId(userId);
    						labelEvent.setDr(1);
    						labelEvent.setLabelId(labelPOJO.getId());
    						labelEvent.setLabelName(labelPOJO.getLabelName());
    						labelEvent.setProductId(labelPOJO.getProductId());
    						applicationContext.publishEvent(labelEvent);
    						TRACER.info("LabelController publishEvent msg: {}",labelEvent.toString());
    					}
        			}catch (Exception e){
        				TRACER.error("标签事件发布异常: cause {}", e.getMessage());
        			}
    				
        			labelService.delete(userId);
    				labelService.save(userId,labelIdString);
    			}else {
    				labelService.save(userId,labelIdString);
    			}
    			//用户标签变化推送事件（新增）
    			try{
    				List<LabelPOJO> label = labelService.getLabel(userId);
    				if (null != label && label.size() > 0 ){
    					for (LabelPOJO labelPOJO : label) {
    						AppLabelEvent labelEvent = new AppLabelEvent(this, applicationContext.getId());
    						labelEvent.setUserId(userId);
    						labelEvent.setDr(labelPOJO.getDr());
    						labelEvent.setLabelId(labelPOJO.getId());
    						labelEvent.setLabelName(labelPOJO.getLabelName());
    						labelEvent.setProductId(labelPOJO.getProductId());
    						applicationContext.publishEvent(labelEvent);
    						TRACER.info("LabelController publishEvent msg: {}",labelEvent.toString());
    					}
    				}
    			}catch (Exception e){
    				TRACER.error("标签事件发布异常: cause {}", e.getMessage());
    			}
    			return this.success( TransactionStatus.OK);
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

    @ApiOperation(value = "获取我的标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getLabel", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<List<LabelPOJO>>> getLabel(HttpServletRequest request, HttpServletResponse response){
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
                return this.success(labelService.getLabel(userId),TransactionStatus.OK);
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

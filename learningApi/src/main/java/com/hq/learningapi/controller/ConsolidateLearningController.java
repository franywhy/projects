package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.StringUtil;
import com.hq.learningapi.entity.ColdStartingEntity;
import com.hq.learningapi.service.AppTodayLearningService;
import com.hq.learningapi.service.ConsolidateLearningService;
import com.hq.learningapi.util.JSONUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by liangdongbin on 2018/1/17 0017.
 */

@Controller
@RequestMapping("/solidatelearning")
public class ConsolidateLearningController extends AbstractRestController {
    @Autowired
    private ConsolidateLearningService consolidateLearningService;
    @Autowired
	private AppTodayLearningService appTodayLearningService;
    /**
     * 做题完成回调接口
     * @date 2018年1月17日
     * @param @param request
     */
    @ApiOperation(value = "做题完成回调接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phaseId", value = "阶段ID", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程ID", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "课次ID", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "businessId", value = "业务线ID", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "trainType", value = "练习类型", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "setFinishReturn", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<WrappedResponse<List<ColdStartingEntity>>> setFinishReturn(HttpServletRequest request){
//        String token = ServletRequestUtils.getStringParameter(request, "token", "");
//        Long phaseId = ServletRequestUtils.getLongParameter(request,"phaseId",0);
//        String multiClassesId = ServletRequestUtils.getStringParameter(request,"multiClassesId","");
//        Long courseId = ServletRequestUtils.getLongParameter(request,"courseId",0);
//        Long trainType = ServletRequestUtils.getLongParameter(request,"trainType",0);
        String result =  consolidateLearningService.process(request);
        if (StringUtil.isNullOrEmpty(result)) {
            return this.success(result);
        }else if(result.equals("未掌握知识点为空")){
            return this.success(result);
        }else{
            return this.fail(result);
        }
    }

    /**
     * 获取知识点接口
     * @date 2018年1月17日
     * @param @param request
     */
    @ApiOperation(value = "获取知识点接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "课次ID", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "examUUID", value = "题库每次做题记录ID", required = false, dataType = "String", paramType = "query")

    })
    @RequestMapping(value = "getKnowledgeFileList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse<List<ColdStartingEntity>>> getKnowledgeFileList(HttpServletRequest request){
    	String classplanLiveId = ServletRequestUtils.getStringParameter(request,"classplanLiveId","");
        String token = ServletRequestUtils.getStringParameter(request,"token","");
        String examUUID = ServletRequestUtils.getStringParameter(request,"examUUID","");
        if(StringUtils.isBlank(classplanLiveId)){
            return this.fail("课次ID(classplanLiveId)不能为空");
        }else if(StringUtils.isBlank(token)){
            return this.fail("token不能为空");
        }else if(StringUtils.isBlank(examUUID)){
            return this.fail("题库做题记录ID不能为空");
        }
        List list = this.consolidateLearningService.getKnowledgeFilesByMultiClassesId(token,classplanLiveId,examUUID,request);
        return this.success(list);
    }
    
    /**
     * 巩固学习视频播放记录
     * @date 2018年1月27日
     * @author zhaowenwei
     * @param @param request
     */
    @ApiOperation(value = "保存巩固学习视频播放记录")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "knowledgeId", value = "知识点ID", required = false, dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "polyvVid", value = "视频ID", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/saveKnowledgePlayLog", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse> knowledgeReturn(HttpServletRequest request){
        
    	String result = this.consolidateLearningService.saveKnowledgePlayLog(request);
    	
    	Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
        Integer code = (Integer) resultMap.get("code");
        
        if (code.equals(200)) {
            return this.success();
        }else{
            TRACER.info("learning-api:ConsolidateLearningController - error - message: {}"+result);
			return this.fail(result);
		}
        
    }
    
    
    /**
     * 获取错题重做地址接口
     * @date 2018年2月3日
     * @param @param request
     */
	@ApiOperation(value = "获取错题重做地址接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "classplanLiveId", value = "课次id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phaseId", value = "阶段id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "courseFk", value = "课程服务id", required = true, dataType = "String", paramType = "query")})
	@RequestMapping(value = "/todayLearing/getWrongTopic", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getWrongTopic(HttpServletRequest request) {
		try {
			String token = ServletRequestUtils.getStringParameter(request, "token", "");
			String classplanLiveId = ServletRequestUtils.getStringParameter(request, "classplanLiveId", "");
			String phaseId = ServletRequestUtils.getStringParameter(request, "phaseId", "");
			String type = ServletRequestUtils.getStringParameter(request, "type", "2");
			String courseFk = ServletRequestUtils.getStringParameter(request, "courseFk", "");
			TRACER.info(String.format("\nRequest Token:%s,\nRequest phaseID:%s,\nRequest type:%s,\nRequest courseFk:%s,\nRequest classplanLiveId:%s",token, phaseId, type, courseFk,classplanLiveId));
			
			String jointUrl = appTodayLearningService.jointURL(token, phaseId, type, courseFk,classplanLiveId,request);
			
			if(StringUtils.isNotBlank(jointUrl)){
				return this.success(jointUrl);
			}
			
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

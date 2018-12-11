package com.school.controller.app;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.school.controller.AbstractBaseController;
import com.school.entity.LogWatchRecordEntity;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LogWatchRecordService;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class AppRecordController extends AbstractBaseController {
	@Autowired
	private LogWatchRecordService logWatchRecordService;
	
	@ApiOperation(value = "记录录播课播放日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recordId", value = "录播课Id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/record/play", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> recordLog(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
		
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String recordIdStr = request.getParameter("recordId");
        if(null == recordIdStr) return this.error("参数提交有误:recordId");
		Long recordId = Long.parseLong(recordIdStr);
		try{
			int logWatchRecordNum = this.logWatchRecordService.queryRecordNum(recordId, userInfo.getUserId());
			
			LogWatchRecordEntity logWatchRecordEntity = new LogWatchRecordEntity();
			if(logWatchRecordNum > 0){
				logWatchRecordEntity.setUserId(userInfo.getUserId());
				logWatchRecordEntity.setRecordId(recordId);
				logWatchRecordEntity.setTs(new Date());
				logWatchRecordService.update(logWatchRecordEntity);
			}else{
				logWatchRecordEntity.setUserId(userInfo.getUserId());
				logWatchRecordEntity.setRecordId(recordId);
				logWatchRecordEntity.setAttend(1);
				logWatchRecordEntity.setTs(new Date());
				logWatchRecordService.save(logWatchRecordEntity);
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/record/play " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/record/play " + (endTime - startTime) + "ms");
        }
		
		return this.success();
	}
}

package com.hq.learningcenter.school.controller.pc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.MaterialPOJO;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.DateUtils;
import com.hq.learningcenter.school.pojo.HopePOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningcenter.school.service.LiveCourseService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/web")
public class YourHopeController extends AbstractBaseController {
	
	@Autowired
	private LiveCourseService liveCourseService;
	
	@ApiOperation(value = "获取直播课程-我希望")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/hope", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseHope(HttpServletRequest request, HttpServletResponse response) {
        //UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        
        String dateStr = request.getParameter("date");
        Date date = new Date();
        if(null != dateStr && !dateStr.equals("")){
        	if(!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
        	date = DateUtils.parse(dateStr);
        }
        
        Long userplanId = Long.parseLong(userplanIdStr);
        List<HopePOJO> hopeResult = liveCourseService.getLiveHopeForWeb(userplanId, businessId, date);
        
        if(null != hopeResult){
        	return this.success(hopeResult);
        }
        return this.fail("服务器内部错误", null);
    }
	
	@ApiOperation(value = "获取-我希望 资料预览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "materialId", value = "资料id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/hope/material", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getHopeMaterial(HttpServletRequest request, HttpServletResponse response) {
		String businessId = BusinessIdUtils.getBusinessId(request);
        String materialIdStr = request.getParameter("materialId");
        if(null == materialIdStr) return this.error("参数提交有误:materialId");
        Long materialId = Long.parseLong(materialIdStr);

        MaterialPOJO materialResult = liveCourseService.getMaterial(materialId,businessId);
        
        if(null != materialResult){
        	return this.success(materialResult);
        }
        return this.fail("服务器内部错误", null);
    }
	
	@ApiOperation(value = "获取-我希望-资料预览-预览内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detailId", value = "资料id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/hope/material/content", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMaterialContent(HttpServletRequest request, HttpServletResponse response) {
        
        String detailIdStr = request.getParameter("detailId");
        if(null == detailIdStr) return this.error("参数提交有误:detailId");
        Long detailId = Long.parseLong(detailIdStr);
        String content = liveCourseService.getMaterialContent(detailId);
        
        if(null != content){
        	Map<String,Object> contentMap = new HashMap<>();
        	contentMap.put("content", content);
        	return this.success(contentMap);
        }
        return this.fail("服务器内部错误", null);
    }
}

package com.school.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LiveService;
import com.school.service.SysUserService;
import com.school.utils.SSOTokenUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
public class AppKValueController extends AbstractBaseController {
	
	@Autowired
	private LiveService liveService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@ApiOperation(value = "获取K值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobileNo", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timeout", value = "k值保存时长", required = false, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getKValue", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getKValue(HttpServletRequest request, HttpServletResponse response) {
		try{
			Long userId = null;
			String userIdStr = request.getParameter("userId");
			String mobileNoStr = request.getParameter("mobileNo");
			if(StringUtils.isNotBlank(userIdStr)){
				userId = Long.valueOf(userIdStr);
			}
			else if(StringUtils.isNotBlank(mobileNoStr)){
				if(StringUtils.isNotBlank(mobileNoStr)){
					Long mobileNo = Long.valueOf(mobileNoStr);
					userId = sysUserService.queryUserIdByMobile(mobileNo);
				}else{
					return this.error("请提供有效参数");
				}
			}else{
				UserInfoPOJO userInfo = this.getUserInfo(request, response);
				if(null != userInfo.getUserId()) userId = userInfo.getUserId();
			}
			if(null != userId){
				return this.success(liveService.buildKValue(userId.toString()));
			}else{
				return this.error("请提供有效参数");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return this.fail("服务器内部错误", null);
		}
    }
}

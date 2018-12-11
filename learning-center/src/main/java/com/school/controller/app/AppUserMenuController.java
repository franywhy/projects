package com.school.controller.app;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.school.service.LcMenuService;
import com.school.utils.BusinessIdUtils;
import com.school.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.school.controller.AbstractBaseController;
import com.school.pojo.LcMenuPOJO;
import com.school.utils.SSOTokenUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class AppUserMenuController extends AbstractBaseController {
    private static final String LCUSERMENU = "lc_usermenu:";

    @Autowired
    private LcMenuService lcMenuService;
	
	@ApiOperation(value = "学员菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/usermenu/list", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> usermenu(HttpServletRequest request, HttpServletResponse response) {
        //业务线ID
        String businessId = BusinessIdUtils.getBusinessId(request);
        Map<String,Object> map = new HashMap();
        map.put("businessId",businessId);
		String token = SSOTokenUtils.getToken(request, response);
        List<LcMenuPOJO> list = new ArrayList<>();
		//从缓存中获取
        List<String> result  = this.getCacheList(LCUSERMENU+businessId);
        if (null != result && result.size() > 0){
            for (String string : result) {
                LcMenuPOJO lcMenuPOJO = JSONUtil.jsonToBean(string, LcMenuPOJO.class);
                lcMenuPOJO.setUrl(lcMenuPOJO.getUrl()+token);
                list.add(lcMenuPOJO);
            }
            return this.success(list);
        }else {
            list = lcMenuService.queryLcUsermenu(map);
            if (null != list){
                this.setCacheList(LCUSERMENU+businessId,list,24*60);
                for (LcMenuPOJO lcMenuPOJO : list) {
                    lcMenuPOJO.setUrl(lcMenuPOJO.getUrl()+token);
                }
                return this.success(list);
            }
        }
        /*List<LcMenuPOJO> list  = lcMenuService.queryLcUsermenu();
        if (null != list){
            for (LcMenuPOJO lcMenuPOJO : list) {
                lcMenuPOJO.setUrl(lcMenuPOJO.getUrl()+token);
            }
            return this.success(list);
        }*/
		return this.fail("服务器内部错误", null);
	}
}

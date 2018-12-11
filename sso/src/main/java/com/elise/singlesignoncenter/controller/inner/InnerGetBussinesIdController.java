package com.elise.singlesignoncenter.controller.inner;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.NcIdEntity;
import com.elise.singlesignoncenter.pojo.MobileNumber;
import com.elise.singlesignoncenter.pojo.UserMobileNoPOJO;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Controller
@RequestMapping(value = "/inner")
public class InnerGetBussinesIdController extends AbstractRestController {


    @RequestMapping(value = "/getBussinessId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<UserToken>> getUserIdByToken(@BusinessId String businessId , HttpServletRequest request) {
        return this.success(businessId);
    }


}

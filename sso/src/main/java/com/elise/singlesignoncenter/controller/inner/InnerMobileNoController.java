package com.elise.singlesignoncenter.controller.inner;

import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.pojo.MobileNo;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.MobileNoRegular;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Glenn on  2017/9/25 0025 16:07.
 */

@Controller
@RequestMapping(value = "/inner")
public class InnerMobileNoController extends AbstractRestController {


    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @ApiOperation(value = "检查手机号是否已经注册")
    @ApiImplicitParam(name = "mobileNo", value = "用户手机号码", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/checkMobileNo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<MobileNo>> logout( HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            /*TRACER.info("\r\nMobile Number:%s,\r\nBusiness Id:%s",mobileNo,businessId);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }*/
            if(StringUtils.isBlank(mobileNo)){
                return this.error("手机号码不可以为空");
            }
            MobileNoRegular mobileNumber = new MobileNoRegular(mobileNo);
            if(mobileNumber.isValidMobileNo()){
                MobileNo mobileNoPOJO = new MobileNo();
                mobileNoPOJO.setIsMobileNumber(userInfoService.checkMobileNo(mobileNo));
                return this.success(mobileNoPOJO);
            }else {
                TRACER.error("Invalid mobile number");
                return this.error("请输入正确的手机号格式", TransactionStatus.INVALID_MOBILE_NUMBER);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

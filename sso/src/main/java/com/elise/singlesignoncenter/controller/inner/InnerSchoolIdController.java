package com.elise.singlesignoncenter.controller.inner;

import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.pojo.SchoolId;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Controller
@RequestMapping("/inner")
public class InnerSchoolIdController extends AbstractRestController {

    @Autowired
    private SysBusinessDao sysBusinessDao;


    @ApiOperation(value = "用户登出")
    @RequestMapping(value = "/checkSchoolId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<SchoolId>> checkSchoolId(@BusinessId String businessId , HttpServletRequest request) {
        try {
            TRACER.info(String.format("\r\nIncoming request,school id is %s", businessId));
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            Integer type = sysBusinessDao.queryType(businessId);
            SchoolId schoolId = new SchoolId();
            schoolId.setMobile(type == 0 ? false : true);
            return this.success(schoolId);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

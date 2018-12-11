package com.elise.singlesignoncenter.service.impl;

import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.service.SysBusinessService;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author shihongjie
 * @date 2018-01-30
 */
@Service("sysBusinessService")
public class SysBusinessServiceImpl implements SysBusinessService {

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Override
    public Integer checkValidationOfBusiness(String businessId) {
        return this.sysBusinessDao.checkValidationOfBusiness(businessId);
    }

    @Override
    public String querySmsSign(String businessId) {
        return this.sysBusinessDao.querySmsSign(businessId);
    }

    @Override
    public Integer queryType(String businessId) {
        return this.sysBusinessDao.queryType(businessId);
    }
}

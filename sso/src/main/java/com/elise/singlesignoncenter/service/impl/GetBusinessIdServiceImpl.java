package com.elise.singlesignoncenter.service.impl;

import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.service.GetBusinessIdService;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("getBusinessIdServiceImpl")
public class GetBusinessIdServiceImpl implements GetBusinessIdService {
    @Autowired
    private SysBusinessDao sysBusinessDao;
    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    /**
     * 查询业务线主键列表
     *
     * @return
     */
    private List<String> queryBusinessList() {
        return this.sysBusinessDao.queryBusinessList();
    }
    /**
     * 获取产品线ID
     *
     * @param request
     * @return
     */
    @Override
    public String getBusinessId(String token) {
        try {
            if (StringUtils.isNotBlank(token)) {
                List<String> businessIdList = queryBusinessList();
                if (!CollectionUtils.isEmpty(businessIdList)) {
                    //根据token获取用户信息
                    UserToken userToken = UserTokenGenerator.parse(token);
                    //根据用户ID获取用户产品线
                    for (int i = 0; i < businessIdList.size(); i++) {
                        String businessId = businessIdList.get(i);
                        String tokenInRedis = this.userInfoRedisService.getTokenByUserId(businessId, userToken.getClientType(), userToken.getUserId());
                        if (token.equals(tokenInRedis)) {
                            return businessId;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

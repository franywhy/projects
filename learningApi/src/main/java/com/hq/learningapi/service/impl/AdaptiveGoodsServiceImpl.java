package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.AdaptiveGoodsDao;
import com.hq.learningapi.pojo.MallGoodDetailPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AdaptiveGoodsService;
import com.hq.learningapi.util.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/10/26.
 */
@Service("adaptiveGoodsService")
public class AdaptiveGoodsServiceImpl implements AdaptiveGoodsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdaptiveGoodsDao adaptiveGoodsDao;

    @Override
    public int isAdaptiveUsers(HttpServletRequest request, String token) {
        if (StringUtils.isNotBlank(token)){
            UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
            if (userInfo != null && userInfo.getUserId() != null ){
                //查询自适应商品
                Map<String,Object> map = new HashMap();
                List<Long> goodsList = adaptiveGoodsDao.queryGoodId(map);
                if (goodsList != null ) {
                    int count = adaptiveGoodsDao.isExistOrder(userInfo.getUserId(), goodsList);
                    return count == 0 ? 0 : 1;
                }
            }
        }
        return 0;
    }
}

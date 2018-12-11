package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.MallGoodDao;
import com.hq.learningapi.dao.SysConfigDao;
import com.hq.learningapi.pojo.MallGoodDetailPOJO;
import com.hq.learningapi.pojo.MallGoodPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.MallGoodService;
import com.hq.learningapi.util.SSOTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by DL on 2018/9/10.
 */
@Service("mallGoodService")
public class MallGoodServiceImpl implements MallGoodService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallGoodDao mallGoodDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public List<MallGoodPOJO> getMallGoodList() {
        //获取IOS内购商品id
        List<Long> goodIdList = mallGoodDao.queryGoodId();
        if (goodIdList != null && goodIdList.size() > 0 ){
            return mallGoodDao.getMallGoodList(goodIdList);
        }
         return null;
    }

    @Override
    public MallGoodDetailPOJO getMallGoodDetail(HttpServletRequest request, String token, Long goodId) {
        //获取用户id
        if (StringUtils.isBlank(token)){
            //throw  new RuntimeException("token 不可为空");
            List<String> notes = sysConfigDao.getPhurchaseNotes("note");
            MallGoodDetailPOJO pojo =  mallGoodDao.getMallGoodDetail(goodId);
            if (pojo != null){
                pojo.setIsExistOrder(0);
                pojo.setPhurchaseNotes(notes);
            }
            return pojo;
        }
        UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
        if (userInfo != null ){
            //查询用户是否已经存在该商品订单
            int count = mallGoodDao.isExistOrder(userInfo.getUserId(),goodId);
            List<String> notes = sysConfigDao.getPhurchaseNotes("note");
            MallGoodDetailPOJO pojo =  mallGoodDao.getMallGoodDetail(goodId);
            if (pojo != null){
                pojo.setIsExistOrder(count > 0 ? 1:0);
                pojo.setPhurchaseNotes(notes);
            }
            return pojo;
        }else {
            logger.error("无效的token值,token={}",token);
            throw new RuntimeException("token 无效,获取用户信息失败");
        }
    }
}

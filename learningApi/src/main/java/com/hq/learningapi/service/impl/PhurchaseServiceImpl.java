package com.hq.learningapi.service.impl;

import com.google.gson.Gson;
import com.hq.learningapi.entity.ClassToTkLogEntity;
import com.hq.learningapi.pojo.BalancePOJO;
import com.hq.learningapi.pojo.PushToLjOrderPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.BalanceService;
import com.hq.learningapi.service.ClassToTkLogService;
import com.hq.learningapi.service.PhurchaseService;
import com.hq.learningapi.util.DateUtils;
import com.hq.learningapi.util.RandomUtils;
import com.hq.learningapi.util.SSOTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by DL on 2018/9/11.
 */
@Service("phurchaseService")
public class PhurchaseServiceImpl implements PhurchaseService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${nc-sign-sync-common}")
    private String PUSHGOODSTOLJ_QUEUE;

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private ClassToTkLogService classToTkLogService;

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
    public String phurchase(HttpServletRequest request, String token, String ncId, Long goodId, double price) {
        Gson gson = new Gson();
        //获取用户信息
        UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
        if (userInfo != null){
            //查询用户恒企币余额
            BalancePOJO balance = balanceService.queryBalance(userInfo.getUserId());
            if (balance == null ){
                return "恒企币余额不足";
            }
            if (balance != null && balance.getHqg() < price){
                return "恒企币余额不足";
            }
            PushToLjOrderPOJO orderPOJO = getPushToLjOrderPOJO(userInfo,goodId,ncId);
            //推送队列
            String pushJson = gson.toJson(orderPOJO).toString();
            amqpTemplate.convertAndSend(PUSHGOODSTOLJ_QUEUE,pushJson);
            logger.error("app购买商品推送到蓝鲸的订单json数据  json={}",pushJson);
            //记录日志
            saveOrderLog(userInfo,goodId,pushJson);
            //修改用户余额
            balance.setHqg(balance.getHqg()-price);
            balanceService.updateHqg(balance);
        }else {
            logger.error("无效的token值,token={}",token);
            throw new RuntimeException("token 无效,获取用户信息失败");
        }
        return null;
    }

    private void saveOrderLog(UserInfoPOJO userInfo, Long goodId, String pushJson) {
        ClassToTkLogEntity entity = new ClassToTkLogEntity();
        entity.setCreatetime(new Date());
        entity.setPushJson(pushJson);
        entity.setUserId(userInfo.getUserId());
        entity.setGoodId(goodId);
        entity.setUserMobile(userInfo.getMobileNo());
        entity.setRemark("app推送到蓝鲸订单:PhurchaseServiceImpl");
        classToTkLogService.save(entity);
    }

    private PushToLjOrderPOJO getPushToLjOrderPOJO(UserInfoPOJO userInfo, Long goodId, String ncId) {
        PushToLjOrderPOJO pojo = new PushToLjOrderPOJO();

        pojo.setCompany_type("kuaiji");
        pojo.setDr(0);
        pojo.setSign_status(1);
        pojo.setSpec_status(1);
        pojo.setStatus(1);
        pojo.setVbill_status(1);
        pojo.setItem_type(1);
        pojo.setProduct_type(7L);
        pojo.setNc_commodity_id(ncId);
        pojo.setCode(getNcCode());
        //默认是上海恒企
        pojo.setNc_school_pk("0001A510000000000KY0");
        //默认为全国默认省份
        pojo.setProvince_pk("0001AAAAAAAAAAAAAA");

        pojo.setClass_type_id("~");
        pojo.setNc_id("~");
        //nc_user_id为空可能造成蓝鲸接收内存溢出
        pojo.setNc_user_id("001");
        pojo.setProvince_name("~");
        pojo.setSchool_name("~");
        pojo.setStudentCourses(new ArrayList<Map<String, Object>>());

        pojo.setSex(userInfo.getGender());
        pojo.setUser_name(userInfo.getNickName());
        pojo.setPhone(userInfo.getMobileNo());

        pojo.setSyn_time(new Date().getTime()+"");
        pojo.setTs(new Date().getTime());
        pojo.setCreate_time(new Date().getTime());

        return pojo;
    }

    private  String getNcCode() {
        //获取时分秒毫秒
        String code = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        String uuid = UUID.randomUUID().toString().substring(0, 2);
        return "HQ05"+code+"-app-"+uuid;
    }
}

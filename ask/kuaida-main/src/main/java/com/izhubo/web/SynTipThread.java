package com.izhubo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.rest.AppProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/17 0017.
 * @author hq
 */
public class SynTipThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SynTipThread.class);

    private static final String LEARNING_API_DOMAIN = AppProperties.get("learning.api.domain");

    private static final Long ONE_HOUR_SECONDS = 1 * 3600L;

    public static final String KUAIJI = "kuaiji";

    public static final String ZIKAO = "zikao";

    private StringRedisTemplate mainRedis;

    private int ssoUserId;

    private String businessId;

    public void setMainRedis(StringRedisTemplate mainRedis) {
        this.mainRedis = mainRedis;
    }

    public void setSsoUserId(int ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public void run() {
        hasCourseNo();
    }

    public boolean hasCourseNo() {
        boolean hasCourseNo = false;
        String tip_key = null;
        if(KUAIJI.equals(businessId)) {
            tip_key = KeyUtils.kuaijiTip(ssoUserId);
        } else if(ZIKAO.equals(businessId)) {
            tip_key = KeyUtils.zikaoTip(ssoUserId);
        }
        String jsonList = mainRedis.opsForValue().get(tip_key);
        if (StringUtils.isBlank(jsonList)) {
            List<String> courseNoList = getCourseNoList();
            if(null != courseNoList && courseNoList.size() > 0) {
                mainRedis.opsForValue().set(tip_key, JSON.toJSONString(courseNoList), ONE_HOUR_SECONDS, TimeUnit.SECONDS);
                hasCourseNo = true;
            }
        } else {
            hasCourseNo = true;
        }
        return hasCourseNo;
    }

    private List<String> getCourseNoList() {
        List<String> courseNoList = null;
        try {
            String url = LEARNING_API_DOMAIN+"api/getCourseNoList?userId="+ssoUserId+"&businessId="+businessId;
            LOG.info("LEARNING_API URL："+url);
            String courseResult = com.izhubo.rest.common.util.http.HttpClientUtil4_3.get(url,null);
            LOG.info("LEARNING_API RESULT："+courseResult);
            if(StringUtils.isNotBlank(courseResult)){
                JSONObject object = JSONObject.parseObject(courseResult);
                if(200 == object.getIntValue("code")) {
                    courseNoList = object.getJSONArray("data").toJavaList(String.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseNoList;
    }
}

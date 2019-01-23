package com.hq.bi.offline.task.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hq.bi.offline.task.entity.AzkabanScheduleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhouyibin
 * @date 2019/1/10
 * @desc
 */
@Slf4j
public class AzkabanUtils {

    private static final String AZKABAN_HOST = "https://10.0.14.243:8443";
    private static final String AZKABAN_SCHEDULE_HOST = "https://10.0.14.243:8443/schedule";
    private static final String SCHEDULE_METHOD = "fetchSchedule";
    private static final String PROJECT_ID = "14";

    private static RestTemplate restTemplate = null;

    static {
        ClassLoader.getSystemResourceAsStream("");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(2000);
        restTemplate = new RestTemplate(requestFactory);
    }

    private static String getSessionId() {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("action", "login");
        linkedMultiValueMap.add("username", "admin");
        linkedMultiValueMap.add("password", "admin");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
        JSONObject j = JSON.parseObject(restTemplate.postForObject(AZKABAN_HOST, httpEntity, String.class));
        String sessionId = j.getString("session.id");
        log.info(">>>>>>>>>>>>>>>>>> [ SessionId =====> {} ] <<<<<<<<<<<<<<<<<", sessionId);
        return sessionId;
    }

    public static Integer getPeriod() {
        try {
            log.info(">>>>>>>>>>>>>>>>>> [ 关闭SSL验证获取登录 SessionId ] <<<<<<<<<<<<<<<<<");
            SSLUtils.turnOffSSLChecking();
            HttpHeaders hs = new HttpHeaders();
            hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            hs.add("X-Requested-With", "XMLHttpRequest");
            LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
            linkedMultiValueMap.add("session.id", getSessionId());
            linkedMultiValueMap.add("ajax", SCHEDULE_METHOD);
            linkedMultiValueMap.add("projectId", PROJECT_ID);
            linkedMultiValueMap.add("flowId", "TeachExamQuality");
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
            String entityJson = restTemplate.postForObject(AZKABAN_SCHEDULE_HOST, httpEntity, String.class);
            AzkabanScheduleEntity schedule = JSON.parseObject(entityJson).getObject("schedule", AzkabanScheduleEntity.class);
            log.info(">>>>>>>>>>>>>>>>>>  [ schedule : {} ] <<<<<<<<<<<<<<<<<", schedule);
            log.info(">>>>>>>>>>>>>>>>>>  [ period : {} ] <<<<<<<<<<<<<<<<<", schedule.getPeriod());
            return 1;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            log.error(">>>>>>>>>>>>>>>>>> [ 获取定时任务执行周期失败 , stackTrace ==> ] <<<<<<<<<<<<<<<<<", e);
            throw new RuntimeException(">>>>>>>>>>>>>>>>>> [ 获取定时任务执行周期失败 , stackTrace ==> ] <<<<<<<<<<<<<<<<<", e);
        }
    }

    public static void main(String[] args) {
        getPeriod();
    }
}

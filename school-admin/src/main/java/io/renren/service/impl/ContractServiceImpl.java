package io.renren.service.impl;

import io.renren.service.ContractService;
import io.renren.utils.http.HttpClientUtil4_3;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("contractService")
public class ContractServiceImpl implements ContractService {
    static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);
    private static String URL = "";
    private static String APP_ID = "";
    private static String APP_KEY = "";
    private static String APP_SIGNERID = "";
    @Resource
    public StringRedisTemplate mainRedis;
    @Value("#{application['online.contract.url']}")
    private void setURL(String str){
        URL = str;
    }

    @Value("#{application['online.contract.appId']}")
    private void setAPPID(String str){
        APP_ID = str;
    }

    @Value("#{application['online.contract.appKey']}")
    private void setAPPKEY(String str){
        APP_KEY = str;
    }

    @Value("#{application['online.contract.appSignerId']}")
    private void setAPP_SIGNERID(String str){
        APP_SIGNERID = str;
    }
    
    
    
    @Override
    public String getToken(boolean signer,Long companyId) {
        Long signerId = 0l;
        if(signer){
            // signerId = Long.parseLong(APP_SIGNERID);
        	signerId=companyId;
        }
        
        String token = mainRedis.opsForValue().get("contractToken:"+signerId);
        if(StringUtils.isBlank(token)){
            String url = URL+"auth/login";
            JSONObject json = new JSONObject();
            json.put("appId", APP_ID);
            json.put("appKey", APP_KEY);
            if(signer){
                json.put("signerId", companyId);
            }
            JSONObject response = null;
            try{
            	
             
                response = HttpClientUtil4_3.doPost(url,json,null);
                token = response.get("token").toString();
                mainRedis.opsForValue().set("contractToken:"+signerId,token ,14, TimeUnit.MINUTES);
            }catch (Exception e){
                logger.error("云合同获取长效令牌失败："+response+"；请求参数"+json);
            }

        }
        return token;
    }

    @Override
    public Long saveUser(String username,String identityRegion,String certifyNum,String phoneRegion,String phoneNo) {
        String url = URL+"user/person";
        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("identityRegion", identityRegion);
        json.put("certifyNum", certifyNum);
        json.put("phoneRegion", phoneRegion);
        json.put("phoneNo", phoneNo);
        json.put("caType", "B2");
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPost(url,json,getToken(false,null));
            int code = response.getInt("code");
            if(code == 200){
                return response.getJSONObject("data").getLong("signerId");
            }else if(code == 20207){
                return getSingerId(certifyNum);
            }
        }catch (Exception e){
            logger.error("云合同注册用户请求失败："+response+"；请求参数："+json);
        }
        return 0l;

    }

    @Override
    public Long getSingerId(String certifyNum) {
        String url = URL+"user/signerId/certifyNums";
        String[] strs = {certifyNum};
        JSONObject json = new JSONObject();
        json.put("certifyNumList", strs);
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPost(url,json,getToken(false,null));
            int code = response.getInt("code");
            if(code == 200){
                List<JSONObject> data =  response.getJSONArray("data");
                return data.get(0).getLong(certifyNum);
            }
        }catch (Exception e){
            logger.error("云合同获取用户id请求失败："+response+"；请求参数"+json);
        }
        return 0l;
    }


    @Override
    public void updateUser(String username,String phoneRegion,String phoneNo) {
        String url = URL+"/user/personNameAndPhone";
        JSONObject json = new JSONObject();
        json.put("userName", username);
        json.put("phoneRegion", phoneRegion);
        json.put("phoneNo", phoneNo);
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPatch(url,json,getToken(false,null));
        }catch (Exception e){
            logger.error("云合同更新用户信息请求失败："+response+"请求参数"+json);
        }

    }
}

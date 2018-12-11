package com.contract.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.contract.dao.ContractRecordDao;
import com.contract.pojo.ContractRecordPOJO;
import com.contract.service.ContractRecordService;
import com.contract.utils.HttpClientUtil4_3;
import com.school.dao.SysConfigDao;

import net.sf.json.JSONObject;

@Service("contractRecordService")
public class ContractRecordServiceImpl implements ContractRecordService {
    private static String URL = "";
    private static String APP_ID = "";
    private static String APP_KEY = "";
    @Autowired
    private ContractRecordDao contractRecordDao;
    
    @Autowired
    private SysConfigDao sysConfigDao;
    
    @Resource
    public StringRedisTemplate adminRedisTemplate;

    static final Logger logger = LoggerFactory.getLogger(ContractRecordServiceImpl.class);

    @Value("#{application['online.contract.url']}")
    private void setURL(String str){
        URL = str;
    }

    @Value("#{application['online.contract.appId']}")
    private void setAPPID(String str){
        APP_ID = str;
    }
    @Override
    public String getAPPID(){
        return APP_ID;
    }

    @Value("#{application['online.contract.appKey']}")
    private void setAPPKEY(String str){
        APP_KEY = str;
    }

    @Override
    public String getToken(Long signerId) {
        String token = adminRedisTemplate.opsForValue().get("contractToken:"+signerId.toString());
        if(StringUtils.isBlank(token)){
            String url = URL+"auth/login";
            JSONObject json = new JSONObject();
            json.put("appId", APP_ID);
            json.put("appKey", APP_KEY);
            if(signerId != 0l){
                json.put("signerId", signerId);
            }
            JSONObject response = null;
            try{
                response = HttpClientUtil4_3.doPost(url,json,null);
                token = response.get("token").toString();
                adminRedisTemplate.opsForValue().set("contractToken:"+signerId.toString(),token ,14, TimeUnit.MINUTES);
            }catch (Exception e){
                logger.error("云合同获取长效令牌失败："+response+"；请求参数："+json);
            }
        }
        return token;
    }

    @Override
    public String getVerifaceToken() {
        StringBuffer  url = new StringBuffer();
        url.append("https://api.yunhetong.com/veriface/getToken");
        url.append("?id="+APP_ID);
        url.append("&&communicatePwd="+APP_KEY);
        JSONObject response = null;
        String token = null;
        try{
            response = HttpClientUtil4_3.doPost(url.toString(),null,"token");
            token = response.get("data").toString();
        }catch (Exception e){
            logger.error("云合同获取人脸识别token失败："+response+"请求参数："+url.toString());
        }
        return token;
    }

    @Override
    public Long querySignerId(Long userId) {
        return contractRecordDao.querySignerId(userId);
    }

    @Override
    public void updateStatus(Long contractId) {
        contractRecordDao.updateStatus(contractId);
    }

    @Override
    public List<ContractRecordPOJO> queryPojoList(Map<String, Object> map) {
        return contractRecordDao.queryPojoList(map);
    }

    @Override
    public Long saveContract(JSONObject json,Long signerId) {
        String url =  URL + "contract/templateContract";
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPost(url,json,getToken(signerId));
            return ((JSONObject) response.get("data")).getLong("contractId");
        }catch (Exception e){
            logger.error("云合同生成在线协议失败："+response+"；请求json："+json);
        }
        return null;
    }

    @Override
    public boolean saveSigner(JSONObject json,Long signerId) {
        String url =  URL + "contract/signer";
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPost(url,json,getToken(0l));
            return true;
        }catch (Exception e){
            logger.error("云合同添加签署者失败："+response+"；请求json："+json);
        }
        return false;
    }

    @Override
    public void update(Map<String, Object> map) {
        contractRecordDao.update(map);
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
            response = HttpClientUtil4_3.doPost(url,json,getToken(0l));
            int code = response.getInt("code");
            if(code == 200){
                Long signerId = response.getJSONObject("data").getLong("signerId");
                return signerId;
            }else if(code == 20209){
                return getSingerId(certifyNum);
            }
        }catch (Exception e){
            logger.error("云合同注册用户失败："+response+"；请求json："+json);
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
            response = HttpClientUtil4_3.doPost(url,json,getToken(0l));
            List<JSONObject> data =  response.getJSONArray("data");
            return data.get(0).getLong(certifyNum);
        }catch (Exception e){
            logger.error("云合同添加签署者失败："+response+"；请求json："+json);
        }
        return 0L;
    }
    
    public int getContractSignNum(Map<String,Object> map){ 
    	//云合同是否开启 (1:open,0:close)
    	String isOpen = sysConfigDao.queryByKey("open_contract");
    	if("0".equals(isOpen)){
    		return 0;
    	}else{
        	return contractRecordDao.getContractSignNum(map);
    	} 
    }
    /**
     * 获取协议签订状态
     * @author linchaokai
     * @date 2018/6/11 17:48
     * @param contractId 协议id
     * @return true:已签订 fasle未签订
     */
    @Override
    public boolean getStatus(Long contractId) {
        String url = URL+"contract/status/0/"+contractId;
        Map<String,String> map = new HashMap<String,String>();
        map.put("token",getToken(0l));
        String response = null;
        try {
            response =  HttpClientUtil4_3.get(url,map);
            JSONObject json = JSONObject.fromObject(response);
            String status = json.getJSONObject("data").getJSONObject("contract").getString("status");
            if(status.equals("已完成")){
                return true;
            }
        }catch (Exception e){
            logger.error("云合同获取协议状态："+response+"；请求url："+url);
        }
        return false;
    }

    @Override
    public void sign(Long contractId) {
        String url = URL+"contract/sign";
        JSONObject json = new JSONObject();
        json.put("idType", 0);
        json.put("idContent", contractId);
        json.put("signerId", contractRecordDao.queryCompanyId(contractId));
        JSONObject response = null;
        try{
            response = HttpClientUtil4_3.doPost(url,json,getToken(0l));
        }catch (Exception e){
            logger.error("云合同企业签订失败："+response+"；请求参数："+json);
        }
    }

    @Override
    public Long queryCompanyId(Long contractId) {
        return null;
    }
}

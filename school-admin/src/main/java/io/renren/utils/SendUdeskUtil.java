package io.renren.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.renren.entity.AgentEntity;
import io.renren.entity.CustomFieldsEntity;
import io.renren.entity.CustomerEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.SuperAgentEntity;
import io.renren.entity.SuperCustomerEntity;
import io.renren.entity.UsersEntity;
import io.renren.utils.http.HttpClientUtil4_3;

/**
 * 
 * @author LiuHai created by 2018/03/02
 *
 */
@Component
public class SendUdeskUtil {
	static final long timestamp = 1520586446L;
	static final String sign = "1ac5ce4942591ef4a02996c297f533ed0b594d90";
    
	protected static Logger logger = LoggerFactory.getLogger(SendUdeskUtil.class);
	
	//udesk接口地址  http://hqjy.udesk.cn/open_api_v1
	private static String UDESK_INTERFACE_DOMAIN = "";
    @Value("#{application['udesk.interface.domain']}")
    private void setUDESK_INTERFACE_DOMAIN(String str){
    	UDESK_INTERFACE_DOMAIN = str;
    }
	
	public static void creatUdeskCustomer(String nick_Name,
										String phone,
										String tags,
										Integer ownerId, 
										String payTime, 
										String province, 
										String level, 
										String className, 
										String schoolName, 
										String profession, 
										String commodity){
		try {
//			String sign = getSign();
			
			String[][] arr = {{null,phone}};
	
			CustomerEntity customer = new CustomerEntity();
			CustomFieldsEntity customFields = new CustomFieldsEntity();
			customFields.setTextField_21507(payTime);
			customFields.setTextField_21508(province);
			customFields.setTextField_21509(level);
			customFields.setTextField_21510(className);
			customFields.setTextField_21511(schoolName);
			customFields.setTextField_21512(profession);
			customFields.setTextField_21513(commodity);
			
			customer.setCustom_fields(customFields);
			customer.setNick_name(nick_Name);
			customer.setOwner_id(null);
			customer.setOwner_group_id(null);//默认客服组id
			customer.setCellphones(arr);
			
			SuperCustomerEntity superCustomerEntity = new SuperCustomerEntity();
			superCustomerEntity.setCustomer(customer);
			superCustomerEntity.setTags(tags);
			
			String postJSON = JSONUtil.beanToJson(superCustomerEntity);
			
			Map<String,String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String result = null;
			result = HttpClientUtil4_3.postStr(UDESK_INTERFACE_DOMAIN+"/customers?email=343383058@qq.com&timestamp="+timestamp+"&sign="+sign, postJSON, headers);
			System.out.println(result);
			logger.error("creatUdeskCustomer result={},phone={}",phone,result);
		} catch (IOException e) {
			logger.error("creatUdeskCustomer error...phone={},errorMsg={}",phone,e.getMessage());
			e.printStackTrace();
		}
		
	}
	public static void updateUdeskCustomer(String nick_Name,
										String phone,
										String tags,
										Integer ownerId, 
										String payTime, 
										String province, 
										String level, 
										String className, 
										String schoolName, 
										String profession, 
										String commodity){
		try {
//			String sign = getSign();
			
//			String[][] arr = {{null,"9999999999"}};
			
			CustomerEntity customer = new CustomerEntity();
			CustomFieldsEntity customFields = new CustomFieldsEntity();
			customFields.setTextField_21507(payTime);
			customFields.setTextField_21508(province);
			customFields.setTextField_21509(level);
			customFields.setTextField_21510(className);
			customFields.setTextField_21511(schoolName);
			customFields.setTextField_21512(profession);
			customFields.setTextField_21513(commodity);
			
			customer.setCustom_fields(customFields);
			customer.setNick_name(nick_Name);
			customer.setOwner_id(null);
			customer.setOwner_group_id(null);//默认客服组id
//			customer.setCellphones(arr);
			
			SuperCustomerEntity superCustomerEntity = new SuperCustomerEntity();
			superCustomerEntity.setCustomer(customer);
			superCustomerEntity.setTags(tags);
			
			String putJSON = JSONUtil.beanToJson(superCustomerEntity);
			
			Map<String,String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String result = null;
			result = HttpClientUtil4_3.putStr(UDESK_INTERFACE_DOMAIN+"/customers/update_customer?type=cellphone&content="+phone+"&email=343383058@qq.com&timestamp="+timestamp+"&sign="+sign, putJSON, headers);
			logger.error("updateUdeskCustomer result={},phone={}",phone,result);
		} catch (IOException e) {
			logger.error("updateUdeskCustomer error...phone={},errorMsg={}",phone,e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static Integer creatUdeskAgent(String phone,String nickName){
		try {
//			String sign = getSign();
			
			String email = phone+"@hqjy.com";//客服初始邮箱
			String password = "hqjy1234";//客服初始密码
			int[] agent_role_ids = {131266};//角色组id
			int[] user_group_ids = {65540};//员工组id
			int[] department_ids = {42674};//部门组id
			
			AgentEntity agent = new AgentEntity();
			agent.setEmail(email);
			agent.setPassword(password);
			agent.setAgent_role_ids(agent_role_ids);
			agent.setUser_group_ids(user_group_ids);
			agent.setDepartment_ids(department_ids);
			agent.setIm_ability_value(1);
			agent.setNick_name(nickName);
			agent.setAliase(nickName+"学习顾问");
			agent.setCellphone(phone);
			
			SuperAgentEntity superAgentEntity = new SuperAgentEntity();
			superAgentEntity.setAgent(agent);
			
			String postJSON = JSONUtil.beanToJson(superAgentEntity);
			Map<String,String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String result = null;
				result = HttpClientUtil4_3.postStr(UDESK_INTERFACE_DOMAIN+"/agents?email=343383058@qq.com&timestamp="+timestamp+"&sign="+sign, postJSON, headers);
				System.out.println(result);
				Map<String,Object> resultMap = null;
				resultMap = JSONUtil.jsonToMap(result);
				if(null != resultMap.get("agent_id")){
					Integer agentId = (int) resultMap.get("agent_id");
					return agentId;
				}
				return null;
				
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void batchImportCustomers(String postJson){
		try {
			Map<String,String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String result = null;
			result = HttpClientUtil4_3.postStr(UDESK_INTERFACE_DOMAIN+"/customers/batch_import_async?email=343383058@qq.com&timestamp="+timestamp+"&sign="+sign, postJson, headers);
			logger.error("batchImportCustomers result={},postJson={}",postJson,result);
		} catch (IOException e) {
			logger.error("batchImportCustomers error...postJson={},errorMsg={}",postJson,e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void deleteCustomer(String cellphone){
		try {
			Map<String,String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json");
			
			String result = null;
			HttpClientUtil4_3.httpDelete(UDESK_INTERFACE_DOMAIN+"/customers/destroy_customer?type=cellphone&content="+cellphone+"&email=343383058@qq.com&timestamp="+timestamp+"&sign="+sign, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	private static String getSign(){
//		Map<String,String> adminMap = new HashMap<String,String>();
//		adminMap.put("email", "343383058@qq.com");
//		adminMap.put("password", "19570113");
//		//long timestamp = System.currentTimeMillis()/1000L;
//		String result = null;
//		try {
//			result = HttpClientUtil4_3.post("http://hqjy.udesk.cn/open_api_v1/log_in", adminMap, null);
//			System.out.println(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		Map<String,Object> resultMap = null;
//		String sign = null;
//		
//		if(null!=result){
//			resultMap = JSONUtil.jsonToMap(result);
//			String token = (String) resultMap.get("open_api_auth_token");
//			sign = EncryptionUtils.shaHex("343383058@qq.com&"+token+"&"+timestamp);
//		}
//		
//		return sign;
//	}
//	
//	public static void main(String[] args) {
//		Map<String,String> adminMap = new HashMap<String,String>();
//		adminMap.put("email", "343383058@qq.com");
//		adminMap.put("password", "19570113");
//		long timestamp = System.currentTimeMillis()/1000L;
//		System.out.println("=============================================");
//		System.out.println(timestamp);
//		String result = null;
//		try {
//			result = HttpClientUtil4_3.post("http://hqjy.udesk.cn/open_api_v1/log_in", adminMap, null);
//			System.out.println(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		Map<String,Object> resultMap = null;
//		String sign = null;
//		
//		if(null!=result){
//			resultMap = JSONUtil.jsonToMap(result);
//			String token = (String) resultMap.get("open_api_auth_token");
//			sign = EncryptionUtils.shaHex("343383058@qq.com&"+token+"&"+timestamp);
//			System.out.println("sign:"+sign);
//		}
//	}
}

//package com.elise.singlesignoncenter;
//
//
//import com.elise.singlesignoncenter.testpojo.*;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//
//public class SingleSignOnCenterApplicationTests {
//
//    private Map<String, String> key = new HashMap<>();
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private JdbcTemplate jdbcT;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void login() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, Token.class);
//        String url = "/inner/login";
//        String requestForm = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        String s = restTemplate.postForObject(url, entity, String.class);
//        WrappedResponse<Token> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        key.put("token", response.getData().getToken());
//    }
//
//    @Test
//    public void getUserInfo() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, UserInfoPOJO.class);
//        String url = "/inner/userInfo?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<UserInfoPOJO> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getData().getAvatar()).isEqualTo("http://zikao.hqjy.com/Avatar.png");
//        assertThat(response.getData().getEmail()).isEqualTo("huxuehan@live.cn");
//        assertThat(response.getData().getGender()).isEqualTo(0);
//        assertThat(response.getData().getNickName()).isEqualTo("宏杰");
//    }
//
//
//    @Test
//    public void logout() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, String.class);
//        String url = "/inner/logout";
//        String requestForm = "token=" + key.get("token");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        String s = restTemplate.postForObject(url, entity, String.class);
//        WrappedResponse<String> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//    }
//
//    @Test
//    public void checkToken() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, TokenStatus.class);
//        String url = "/inner/tokenExpired?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<TokenStatus> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getData().getExpired()).isEqualTo(false);
//    }
//
//    @Test
//    public void checkSchoolId() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, SchoolId.class);
//        String s = restTemplate.getForObject("/inner/checkSchoolId", String.class);
//        WrappedResponse<SchoolId> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//
//    }
//
//    @Test
//    public void checkMobile() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, MobileNo.class);
//        String url = "/inner/checkMobileNo?mobileNo=18620523707";
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<MobileNo> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getData().getIsMobileNumber()).isEqualTo(true);
//    }
//
//    @Test
//    public void getWebToken() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, Token.class);
//        String url = "/inner/webToken?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<Token> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//    }
//
//    @Test
//    public void getTokenDetail() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, UserToken.class);
//        String url = "/inner/userTokenDetail?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<UserToken> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getData().getClientType().getStrType()).isEqualTo("ios");
//        assertThat(response.getData().getOneTimeToken()).isEqualTo(false);
//        assertThat(response.getData().getVersionCode()).isEqualTo(100);
//        assertThat(response.getData().getUserId()).isEqualTo(2);
//    }
//
//    @Test
//    public void getNcId() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, NcId.class);
//        String url = "/inner/ncId?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<NcId> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//    }
//
//    @Test
//    public void getMobileNo() throws IOException{
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, MobileNumber.class);
//        String url = "/inner/userMobileNo?token=" + key.get("token");
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<MobileNumber> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getData().getMobileNo()).isEqualTo("18620523707");
//
//    }
//
//    @Test
//    public void getSms() throws IOException {
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, String.class);
//        String url = "/inner/otpSMS?mobileNo=18620523707";
//        String s = restTemplate.getForObject(url, String.class);
//        WrappedResponse<String> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//    }
//
//    @Test
//    public void register() throws IOException{
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, String.class);
//        String url = "/inner/register";
//        String requestForm = "mobileNo=13813813813&passWord=MTIzNDU2&otp=1111";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        String s = restTemplate.postForObject(url, entity, String.class);
//        WrappedResponse<String> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        String select = "DELETE FROM `users` WHERE mobile = \"13813813813\"";
//        jdbcT.execute(select);
//    }
//
//    @Test
//    public void checkSms() throws IOException{
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, String.class);
//        String url = "/inner/otpSMS";
//        String requestForm = "mobileNo=18620523707&otp=1111";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        String s = restTemplate.postForObject(url, entity, String.class);
//        WrappedResponse<String> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//    }
//
//    @Test
//    public void modifyPassword() throws IOException {
//        //修改密码
//        String url = "/inner/passWord";
//        String requestForm = "token="+key.get("token")+"&activePassWord=MjM0NTY3&inactivePassWord=MTIzNDU2";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        restTemplate.put(url,entity);
//        //登录验证
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, Token.class);
//        String loginUrl = "/inner/login";
//        String logInForm = "mobileNo=18620523707&passWord=MjM0NTY3&clientType=ios&versionCode=100";
//        HttpHeaders loginHeaders = new HttpHeaders();
//        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> loginEntity = new HttpEntity<>(logInForm, loginHeaders);
//        String s = restTemplate.postForObject(loginUrl, loginEntity, String.class);
//        WrappedResponse<Token> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        key.put("token", response.getData().getToken());
//        //恢复用例
//        String rollbackForm = "token="+key.get("token")+"&activePassWord=MTIzNDU2&inactivePassWord=MjM0NTY3";
//        HttpHeaders rollbackHeaders = new HttpHeaders();
//        rollbackHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> rollbackEntity = new HttpEntity<>(rollbackForm, rollbackHeaders);
//        restTemplate.put(url,rollbackEntity);
//    }
//
//    @Test
//    public void resetPassword() throws IOException {
//        //重置密码
//        JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, String.class);
//        String url = "/inner/passWord";
//        String requestForm = "mobileNo=18620523707&passWord=MjM0NTY3&otp=1111";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> entity = new HttpEntity<>(requestForm, headers);
//        String s =restTemplate.postForObject(url,entity,String.class);
//        WrappedResponse<String> response = objectMapper.readValue(s, type);
//        assertThat(response.getCode()).isEqualTo(200);
//        //登录验证
//        JavaType loginType = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class, Token.class);
//        String loginUrl = "/inner/login";
//        String logInForm = "mobileNo=18620523707&passWord=MjM0NTY3&clientType=ios&versionCode=100";
//        HttpHeaders loginHeaders = new HttpHeaders();
//        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<String> loginEntity = new HttpEntity<>(logInForm, loginHeaders);
//        String loginStr = restTemplate.postForObject(loginUrl, loginEntity, String.class);
//        WrappedResponse<Token> loginResponse = objectMapper.readValue(loginStr, loginType);
//        assertThat(loginResponse.getCode()).isEqualTo(200);
//        key.put("token", loginResponse.getData().getToken());
//        //恢复用例
//        String rollbackForm = "mobileNo=18620523707&passWord=MTIzNDU2&otp=1111";
//        HttpEntity<String> rollbackEntity = new HttpEntity<>(rollbackForm, headers);
//        s =restTemplate.postForObject(url,rollbackEntity,String.class);
//        WrappedResponse<String> rollbackResponse = objectMapper.readValue(s, type);
//        assertThat(rollbackResponse.getCode()).isEqualTo(200);
//    }
//
//}

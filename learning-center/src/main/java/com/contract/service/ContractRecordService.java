package com.contract.service;

import com.contract.pojo.ContractRecordPOJO;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @auther linchaokai
 * @description 在线协议接口
 * @date 2018/6/5
 */
public interface ContractRecordService {
    String getAPPID();
    /**
     * 获取token
     * @author linchaokai
     * @date 2018/6/5 11:55
     * @param
     * @return
     */
    public String getToken(Long signerId);
    /**
     * 获取人脸识别token
     * @author linchaokai
     * @date 2018/6/5 11:55
     * @param
     * @return
     */
    public String getVerifaceToken();
    /**
     * 查询在线协议用户id
     * @author linchaokai
     * @date 2018/6/6 14:13
     * @param   userId 用户id
     * @return
     */
    public Long querySignerId(Long userId);
    /**
     * 更新协议记录状态
     * @author linchaokai
     * @date 2018/6/6 14:13
     * @param   contractId 协议id
     * @return
     */
    void updateStatus(Long contractId);
    /**
     * 协议记录信息列表
     * @author linchaokai
     * @date 2018/6/6 16:42
     * @param  * @param null
     * @return
     */
    List<ContractRecordPOJO> queryPojoList(Map<String,Object> map);

    /**
     * 新增协议
     * @author linchaokai
     * @date 2018/6/7 9:06
     * @param  json
     * @return
     */
    Long saveContract(JSONObject json,Long signerId);
    /**
     * 添加签署者
     * @author linchaokai
     * @date 2018/6/7 15:06
     * @return
     */
    boolean saveSigner(JSONObject json,Long signerId);
    /**
     * 更新协议用户或协议id
     * @author linchaokai
     * @date 2018/6/8 15:06
     * @return
     */
    void update(Map<String,Object> map);

    /**
     * 注册用户
     * @author linchaokai
     * @date 2018/6/5 16:08
     * @param username 姓名
     * @param identityRegion 身份证所属地区
     * @param certifyNum 身份证
     * @param phoneRegion 手机号码所属区
     * @param phoneNo 手机号
     * @return
     */
    public Long saveUser(String username,String identityRegion,String certifyNum,String phoneRegion,String phoneNo);

    /**
     * 获取协议用户id
     * @author linchaokai
     * @date 2018/6/8 15:06
     * @return
     */
    Long getSingerId(String certifyNum);
    
    public int getContractSignNum(Map<String,Object> map);
    /**
     * 获取协议签订状态
     * @author linchaokai
     * @date 2018/6/11 17:48
     * @param contractId 协议id
     * @return true:已签订 fasle未签订
     */
    boolean getStatus(Long contractId);
    /**
     * @author linchaokai
     * @date 2018/6/12 15:29
     * @param contractId 协议id
     * @return
     */
    void sign( Long contractId);

    Long queryCompanyId(Long contractId);
}

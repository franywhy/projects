package io.renren.service;

import io.renren.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @auther linchaokai
 * @description 在线协议接口
 * @date 2018/6/5
 */
public interface ContractService {
    /**

     * @author linchaokai
     * @date 2018/6/5 11:55
     * @param
     * @return
     */
    /**
     * 获取平台或用户token
     * @author linchaokai
     * @param signer true:用户 false:平台,companyId 公司id
     * @date 2018/6/8 17:14
     * @return
     */
    public String getToken(boolean signer,Long companyId);
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
     * 获取用户id
     * @author linchaokai
     * @date 2018/6/8 14:41
     * @param  * @param certifyNum 身份证号码
     * @return
     */
    public Long getSingerId(String certifyNum);
    /**
     * 更新用户
     * @author linchaokai
     * @date 2018/6/5 16:06
     * @param username 姓名
     * @param phoneRegion 手机号码所属地区
     * @param phoneNo 手机号码
     * @return
     */
    public void updateUser(String username,String phoneRegion,String phoneNo);
 
}

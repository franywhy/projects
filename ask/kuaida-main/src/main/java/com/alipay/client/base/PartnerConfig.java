package com.alipay.client.base;

import com.izhubo.rest.AppProperties;

public interface PartnerConfig {
    //合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
    String PARTNER = AppProperties.get("ali.pattern_id");
    // 商户收款的支付宝账号
    String SELLER =  AppProperties.get("ali.seller_email");
    // 商户（MD5）KEY
    String KEY= AppProperties.get("ali.pc_key");
    
    //支付成功跳转链接
    String call_back_url = "http://www.izhubo.com/";
    // 未完成支付，用户点击链接返回商户url
    String merchantUrl = "http://www.izhubo.com/";


    String notifyUrl = AppProperties.get("api.domain")+"pay/ali_wap_notify";

}

package com.hqjy.pay.utils;

import com.hqjy.pay.weixin.Configure;
import com.hqjy.pay.zfuconfig.AlipayConfig;

/**
 * 切换赛道支持类
 * 学来学往/自考
 * @author zhaozhiguang
 */
public class SwitchTrackSupport {

    private SwitchTrackSupport(){}

    /**
     * 微信支付
     * @param type 赛道类型
     * @return
     */
    public static Configure WeChatPay(String type){
        switch (type){
            case "zk":
            case "bw":
                return Configure.Exam.getInstance();
            default:
                return Configure.Account.getInstance();
        }
    }

    /**
     * 支付宝支付
     * @param type 赛道类型
     * @return
     */
    public static AlipayConfig AliPay(String type){
        switch (type){
            case "zk":
            case "bw":
                return AlipayConfig.Exam.getInstance();
            default:
                return AlipayConfig.Account.getInstance();
        }
    }

}

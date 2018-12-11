package com.yeepay;

/**
 * 以宝请求参数
 * date: 13-3-30 下午3:21
 *
 * @author: wubinjie@ak.cc
 */
public enum YReq {
    p0_Cmd,
    p1_MerId,
    /**
     * forHex Need.
     */
    p2_Order,//商户订单号,选填 若不为""，提交的订单号必须在自身账户交易中唯一;为""时，易宝支付会自动生成随机的商户订单号.
    p3_Amt,//支付金额,必填.单位:元，精确到分.
    p4_Cur,//交易币种,固定值"CNY".
    p5_Pid,// 商品名称  用于支付时显示在易宝支付网关左侧的订单产品信息
    p6_Pcat,//商品种类 此参数如用到中文，请注意转码.
    p7_Pdesc,//商品描述 此参数如用到中文，请注意转码.
    p8_Url,//商户接收支付成功数据的地址,支付成功后易宝支付会向该地址发送两次成功通知. 'http://show.izhubo.com/uc/pay/yee/callback.php';
    p9_SAF,// 送货地址 为“1”: 需要用户将送货地址留在易宝支付系统;为“0”: 不需要，默认为 ”0”.
    pa_MP,//商户扩展信息 否  Max(200)  返回时原样返回，此参数如用到中文，请注意转码.
    pd_FrpId, // 银行编号 大写
    pr_NeedResponse;//应答机制 默认为"1": 需要应答机制;







}

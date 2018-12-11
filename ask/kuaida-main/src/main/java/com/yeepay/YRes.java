package com.yeepay;

/**
 * date: 13-3-30 下午3:22
 *
 * @author: wubinjie@ak.cc
 */
public enum YRes {
    p1_MerId,// 商户编号
    r0_Cmd,// 业务类型
    r1_Code,// 支付结果
    r2_TrxId,// 易宝支付交易流水号
    r3_Amt,// 支付金额
    r4_Cur,// 交易币种
    r5_Pid,// 商品名称
    r6_Order,// 商户订单号
    r7_Uid,// 易宝支付会员ID
    r8_MP,// 商户扩展信息
    r9_BType;// 交易结果返回类型
}

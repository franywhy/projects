package com.yeepay.nonbankcard;

import com.izhubo.rest.AppProperties;
import com.yeepay.DigestUtil;
import com.yeepay.YRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lu.li
 *
 */
public class NonBankcardService {
    private static Logger log 						= LoggerFactory.getLogger(NonBankcardService.class);
    private static String keyValue 		= AppProperties.get("keyValue"); 		// 商户密钥
    private static String p0_Cmd 				= "ChargeCardDirect"; 												// 请求命令名称
    private static String p1_MerId 		= AppProperties.get("p1_MerId"); 		// 商户编号
    private static String annulCardReqURL 	= AppProperties.get("annulCardReqURL"); 	// 请求地址


    private static final String CALL_BACK = AppProperties.get("api.domain") + "pay/yeepay_wap_callback";

    /**
     * 校验交易结果通知
     * 该方法是根据《易宝支付非银行卡支付专业版接口文档 v3.0》对易宝支付返回扣款数据进行校验
     * 具体参数含义请仔细阅读《易宝支付非银行卡支付专业版接口文档 v3.0》
     * @param hmac
     */
    public static void verifyCallback(String[] args,
                                      String hmac) {
        log.debug("Recv payment result:{} ;hmac : {}", Arrays.toString(args) , hmac);
        if (!args[YRes.r1_Code.ordinal()].equals("1")) {
            throw new RuntimeException("Payment is fail!r1_Code=" + args[1]);
        }
        String newHmac = DigestUtil.getHmac(args, keyValue);
        if (!hmac.equals(newHmac)) {
            String errorMessage = "交易签名被篡改!";
            log.debug(errorMessage);
            throw new RuntimeException(errorMessage);
        }

    }

    /**
     * 消费请求
     * 该方法是根据《易宝支付非银行卡支付专业版接口文档 v3.0》对发起支付请求进行的封装
     * 具体参数含义请仔细阅读《易宝支付非银行卡支付专业版接口文档 v3.0》
     * 商户订单号
     * @param p2_Order
     * 订单金额
     * @param p3_Amt
     * 是否较验订单金额
     * @param p4_verifyAmt
     * 产品名称
     * @param p5_Pid
     * 产品类型
     * @param p6_Pcat
     * 产品描述
     * @param p7_Pdesc
     * 通知地址
     * @param p8_Url
     * 扩展信息
     * @param pa_MP
     * 卡面额组
     * @param pa7_cardAmt
     * 卡号组
     * @param pa8_cardNo
     * 支付方式
     * @param pd_FrpId
     * 通知是否需要应答
     * @param pr_NeedResponse
     * 用户ID
     * @param pz_userId
     * 用户注册时间
     * @param pz1_userRegTime
     * @return
     */
    public static NonBankcardPaymentResult pay(String p2_Order,
                                               String p3_Amt,
                                               String p4_verifyAmt,
                                               String p5_Pid,
                                               String p6_Pcat,
                                               String p7_Pdesc,
                                               String p8_Url,
                                               String pa_MP,
                                               String pa7_cardAmt,
                                               String pa8_cardNo,
                                               String pa9_cardPwd,
                                               String pd_FrpId,
                                               String pr_NeedResponse,
                                               String pz_userId,
                                               String pz1_userRegTime) {

        // 卡号和卡密不得为空
        if(pa8_cardNo == null || pa8_cardNo.equals("") || pa9_cardPwd == null || pa9_cardPwd.equals("")) {
            log.error("pa7_cardNo or pa8_cardPwd is empty.");
            throw new RuntimeException("pa7_cardNo or pa8_cardPwd is empty.");
        }

        // 生成hmac，保证交易信息不被篡改,关于hmac详见《易宝支付非银行卡支付专业版接口文档 v3.0》
        String hmac = "";
        hmac = DigestUtil.getHmac(new String[] { p0_Cmd,
                p1_MerId,
                p2_Order,
                p3_Amt,
                p4_verifyAmt,
                p5_Pid,
                p6_Pcat,
                p7_Pdesc,
                p8_Url,
                pa_MP,
                pa7_cardAmt,
                pa8_cardNo,
                pa9_cardPwd,
                pd_FrpId,
                pr_NeedResponse,
                pz_userId,
                pz1_userRegTime}, keyValue);
        // 封装请求参数，参数说明详见《易宝支付非银行卡支付专业版接口文档 v3.0》
        Map reqParams = new HashMap();
        reqParams.put("p0_Cmd", p0_Cmd);
        reqParams.put("p1_MerId", p1_MerId);
        reqParams.put("p2_Order", p2_Order);
        reqParams.put("p3_Amt", p3_Amt);
        reqParams.put("p4_verifyAmt", p4_verifyAmt);
        reqParams.put("p5_Pid", p5_Pid);
        reqParams.put("p6_Pcat", p6_Pcat);
        reqParams.put("p7_Pdesc", p7_Pdesc);
        reqParams.put("p8_Url", p8_Url);
        reqParams.put("pa_MP", pa_MP);
        reqParams.put("pa7_cardAmt", pa7_cardAmt);
        reqParams.put("pa8_cardNo", pa8_cardNo);
        reqParams.put("pa9_cardPwd", pa9_cardPwd);
        reqParams.put("pd_FrpId", pd_FrpId);
        reqParams.put("pr_NeedResponse", pr_NeedResponse);
        reqParams.put("pz_userId", pz_userId);
        reqParams.put("pz1_userRegTime", pz1_userRegTime);
        reqParams.put("hmac", hmac);
        List responseStr = null;
        try {
            // 发起支付请求
            log.debug("Begin http communications,request params[" + reqParams
                    + "]");
            responseStr = com.yeepay.nonbankcard.HttpUtils.URLPost(annulCardReqURL, reqParams);
            log.debug("End http communications.responseStr:" + responseStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        if (responseStr.size() == 0) {
            log.error("no response.");
            throw new RuntimeException("no response.");
        }
        // 创建非银行卡专业版消费请求结果
        NonBankcardPaymentResult rs = new NonBankcardPaymentResult();
        // 解析易宝支付返回的消费请求结果,关于返回结果数据详见《易宝支付非银行卡支付专业版接口文档 v3.0》
        for (int t = 0; t < responseStr.size(); t++) {
            String currentResult = (String) responseStr.get(t);
            log.debug("responseStr[" + t + "]:" + currentResult);
            if (currentResult == null || currentResult.equals("")) {
                continue;
            }
            int i = currentResult.indexOf("=");
            log.debug("i=" + i);
            if (i >= 0) {
                log.debug("find =.");
                String sKey = currentResult.substring(0, i);
                String sValue = currentResult.substring(i + 1);
                if (sKey.equals("r0_Cmd")) {
                    rs.setR0_Cmd(sValue);
                } else if (sKey.equals("r1_Code")) {
                    rs.setR1_Code(sValue);
                } else if (sKey.equals("r6_Order")) {
                    rs.setR6_Order(sValue);
                } else if (sKey.equals("rq_ReturnMsg")) {
                    rs.setRq_ReturnMsg(sValue);
                } else if (sKey.equals("hmac")) {
                    rs.setHmac(sValue);
                } else {
                    log.error("throw exception:" + currentResult);
                    throw new RuntimeException(currentResult);
                }
            } else {
                log.error("throw exception:" + currentResult);
                throw new RuntimeException(currentResult);
            }
        }
        // 不成功则抛出异常
        if (!rs.getR1_Code().equals("1")) {
            log.error("errorCode:" + rs.getR1_Code() + ";errorMessage:"
                    + rs.getRq_ReturnMsg());
            throw new RuntimeException("errorCode:" + rs.getR1_Code()
                    + ";errorMessage:" + rs.getRq_ReturnMsg());
        }
        String newHmac = DigestUtil.getHmac(new String[] { rs.getR0_Cmd(),
                rs.getR1_Code(),rs.getR6_Order(),
                rs.getRq_ReturnMsg() }, keyValue);
        // hmac不一致则抛出异常
        if (!newHmac.equals(rs.getHmac())) {
            log.error("交易签名被篡改");
            throw new RuntimeException("交易签名被篡改");
        }
        return (rs);
    }

    static String formatString(String text) {return  text == null? "" : text;}

    public static NonBankcardPaymentResult  reqPay(HttpServletRequest request,String p3_Amt,String p2_Order ,String pa_MP, String pd_FrpId ) throws UnsupportedEncodingException {
// 是否较验订单金额
        String p4_verifyAmt = formatString(request.getParameter("p4_verifyAmt"));

// 产品名称
        String p5_Pid = formatString(request.getParameter("p5_Pid"));

// 产品类型
        String p6_Pcat = formatString(request.getParameter("p6_Pcat"));

// 产品描述
        String p7_Pdesc = formatString(request.getParameter("p7_Pdesc"));

// 交易成功通知地址
        String p8_Url =   CALL_BACK; //;formatString(request.getParameter("p8_Url"));

// 卡面额组
        String pa7_cardAmt = formatString(request.getParameter("pa7_cardAmt"));

// 卡号组
        String pa8_cardNo = formatString(request.getParameter("pa8_cardNo"));

// 卡密组
        String pa9_cardPwd = formatString(request.getParameter("pa9_cardPwd"));

// 应答机制
        String pr_NeedResponse = formatString("1");

// 用户唯一标识
        String pz_userId = formatString(request.getParameter("pz_userId"));

// 用户的注册时间
        String pz1_userRegTime  = formatString(request.getParameter("pz1_userRegTime"));
        return NonBankcardService.pay(p2_Order,
                    p3_Amt,
                    p4_verifyAmt,
                    p5_Pid,
                    p6_Pcat,
                    p7_Pdesc,
                    p8_Url,
                    pa_MP,
                    pa7_cardAmt,
                    pa8_cardNo,
                    pa9_cardPwd,
                    pd_FrpId,
                    pr_NeedResponse,
                    pz_userId,
                    pz1_userRegTime);
    }
    /**
     * 校验交易结果通知
     * 该方法是根据《易宝支付非银行卡支付专业版接口文档 v3.0》对易宝支付返回扣款数据进行校验
     * 具体参数含义请仔细阅读《易宝支付非银行卡支付专业版接口文档 v3.0》
     */
    public static boolean verifyCallback(HttpServletRequest request,String p2_Order, String p3_Amt,String p9_MP) {

        // 业务类型
        String r0_Cmd = formatString(request.getParameter("r0_Cmd"));
// 商户编号
        String p1_MerId = formatString(request.getParameter("p1_MerId"));
// 支付方式
        String p4_FrpId = formatString(request.getParameter("p4_FrpId"));
// 卡序列号组
        String p5_CardNo = formatString(request.getParameter("p5_CardNo"));
// 确认金额组
        String p6_confirmAmount = formatString(request.getParameter("p6_confirmAmount"));
// 实际金额组
        String p7_realAmount = formatString(request.getParameter("p7_realAmount"));
// 卡状态组
        String p8_cardStatus = formatString(request.getParameter("p8_cardStatus"));
// 支付余额 注：此项仅为订单成功,并且需要订单较验时才会有值。失败订单的余额返部返回原卡密中
        String pb_BalanceAmt = formatString(request.getParameter("pb_BalanceAmt"));
// 余额卡号  注：此项仅为订单成功,并且需要订单较验时才会有值
        String pc_BalanceAct = formatString(request.getParameter("pc_BalanceAct"));
// 签名数据
        String hmac	= formatString(request.getParameter("hmac"));
        String newHmac = DigestUtil.getHmac(new String[] {  r0_Cmd,
                "1",
                p1_MerId,
                p2_Order,
                p3_Amt,
                p4_FrpId,
                p5_CardNo,
                p6_confirmAmount,
                p7_realAmount,
                p8_cardStatus,
                p9_MP,
                pb_BalanceAmt,
                pc_BalanceAct}, keyValue);
        return hmac.equals(newHmac);
    }
}

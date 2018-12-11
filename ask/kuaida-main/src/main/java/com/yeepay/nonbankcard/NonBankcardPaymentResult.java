package com.yeepay.nonbankcard;

//import lombok.Data;
//import lombok.ToString;
//import lombok.extern.slf4j.Slf4j;

/**
 * 非银行卡支付结果
 * @author lu.li
 *
 */
//@Data
//@ToString
//@Slf4j
public class NonBankcardPaymentResult {
	private String r0_Cmd;			// 业务类型
	private String r1_Code;			// 消费请求结果(该结果代表请求是否成功，不代表实际扣款结果)
	private String r6_Order;		// 商户订单号
	private String rq_ReturnMsg;	// 错误信息
	private String hmac;			// 签名数据


    public static void main(String[] args) {
        NonBankcardPaymentResult t = new NonBankcardPaymentResult();

//        System.out.println(t.getR1_Code());
//        log.debug("hah {}",t);
    }

    


	public String getR0_Cmd() {
		return r0_Cmd;
	}


	public void setR0_Cmd(String r0_Cmd) {
		this.r0_Cmd = r0_Cmd;
	}


	public String getR1_Code() {
		return r1_Code;
	}


	public void setR1_Code(String r1_Code) {
		this.r1_Code = r1_Code;
	}


	public String getR6_Order() {
		return r6_Order;
	}


	public void setR6_Order(String r6_Order) {
		this.r6_Order = r6_Order;
	}


	public String getRq_ReturnMsg() {
		return rq_ReturnMsg;
	}


	public void setRq_ReturnMsg(String rq_ReturnMsg) {
		this.rq_ReturnMsg = rq_ReturnMsg;
	}


	public String getHmac() {
		return hmac;
	}


	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
    
    

}

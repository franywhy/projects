package com.izhubo.web.pay;

public class PayConfig {
	/** PC支付宝账号 */
	private static final String ALIPAY_PC_ACCOUNT = "pay@hengqijy.com";
	/** H5支付宝账号 */
	private static final String ALIPAY_H5_ACCOUNT = ALIPAY_PC_ACCOUNT;
	
	/** PC块钱账号 */
	private static final String _99BILL_PC_ACCOUNT = "pay@hengqijy.com";
	/** H5块钱账号 */
	private static final String _99BILL_H5_ACCOUNT = _99BILL_PC_ACCOUNT;
	
	/** PC微信账号 */
	private static final String WEIXIN_PC_ACCOUNT = "913653377@qq.com";
	/** H5微信账号 */
	private static final String WEIXIN_H5_ACCOUNT = WEIXIN_PC_ACCOUNT;
	
	/**
	 * 获取代收账号
	 * @param type 1.支付宝PC 2.微信PC 3.快钱PC 4.支付宝网银PC 11.支付宝H5 12.微信H5 13.块钱H5
	 * @return
	 */
	public static String getAccount(int type){
		String s = "";
		switch (type) {
		case 1:
			s = ALIPAY_PC_ACCOUNT;
			break;
		case 2:
			s = WEIXIN_PC_ACCOUNT;
			break;
			
		case 3:
			s = _99BILL_PC_ACCOUNT;
			break;
		case 4:
//			s = _99BILL_H5_ACCOUNT;
			break;
			
		case 11:
			s = ALIPAY_H5_ACCOUNT;
			break;
		case 12:
			s = WEIXIN_H5_ACCOUNT;
			break;
		case 13:
			s = _99BILL_H5_ACCOUNT;
			break;

		default:
			break;
		}
		return s;
	}
	
}

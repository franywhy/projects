package com.izhubo.service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import com.cn.b2m.eucp.sdkhttp.Mo;
import com.cn.b2m.eucp.sdkhttp.SDKServiceBindingStub;
import com.cn.b2m.eucp.sdkhttp.SDKServiceLocator;
import com.cn.b2m.eucp.sdkhttp.StatusReport;

public class SmsService{
	private String softwareSerialNo;
	private String key;
	public SmsService(String sn,String key){
		this.softwareSerialNo=sn;
		this.key=key;
		init();
	}
	SDKServiceBindingStub binding;
	public void init(){
		 try {
           binding = (SDKServiceBindingStub)
                         new SDKServiceLocator().getSDKService();
		 }
       catch (javax.xml.rpc.ServiceException jre) {
           if(jre.getLinkedCause()!=null)
               jre.getLinkedCause().printStackTrace();
       }
	}	
	/**
	 * 序列号充值
	 * @param cardNo
	 * @param cardPass
	 * @return
	 * @throws RemoteException
	 */
	public int chargeUp(String cardNo, String cardPass) throws RemoteException {
		int value = -1;
		value = binding.chargeUp(softwareSerialNo, key, cardNo, cardPass);
		return value;
	}
	/**
	 * 查询余额
	 * @return
	 * @throws RemoteException
	 */
	public double getBalance() throws RemoteException {
		double value = 0.0;
		value = binding.getBalance(softwareSerialNo, key);
		return value;
	}
	/**
	 * 查询单价
	 * @return
	 * @throws RemoteException
	 */
	public double getEachFee() throws RemoteException {
		double value = 0.0;
		value = binding.getEachFee(softwareSerialNo, key);
		return value;
	}
	/**
	 * 3.9	接收短信
	 * @return
	 * @throws RemoteException
	 */
	public List<Mo> getMO() throws RemoteException {
		Mo[] mo = binding.getMO(softwareSerialNo, key);

		if (null == mo) {
			return null;
		} else {
			List<Mo> molist = Arrays.asList(mo);
			return molist;
		}
	}
	/**
	 * 3.10	接收状态报告
	 * @return
	 * @throws RemoteException
	 */
	public List<StatusReport> getReport() throws RemoteException {
		StatusReport[] sr = binding.getReport(softwareSerialNo, key);
		if (null != sr) {
			return Arrays.asList(sr);
		} else {
			return null;
		}
	}
	/**
	 * 注销序列号
	 * @return
	 * @throws RemoteException
	 */
	public int logout() throws RemoteException {
		int value = -1;
		value = binding.logout(softwareSerialNo, key);
		return value;
	}
	/**
	 * 注册企业信息
	 * @param eName 企业名称(最多60字节)，必须输入
	 * @param linkMan 联系人姓名(最多20字节)，必须输入
	 * @param phoneNum 联系电话(最多20字节)，必须输入
	 * @param mobile 联系手机(最多15字节)，必须输入
	 * @param email 电子邮件(最多60字节)，必须输入
	 * @param fax 联系传真(最多20字节)，必须输入
	 * @param address 公司地址(最多60字节)，必须输入
	 * @param postcode 邮政编码(最多6字节)，必须输入
	 * @return
	 * @throws RemoteException
	 */
	public int registDetailInfo(String eName, String linkMan, String phoneNum,
			String mobile, String email, String fax, String address,
			String postcode) throws RemoteException {
		int value = -1;
		value = binding.registDetailInfo(softwareSerialNo, key, eName, linkMan,
				phoneNum, mobile, email, fax, address, postcode);
		return value;
	}
	/**
	 * 注册序列号
	 * @param password 软件序列号密码，密码（6位），必须输入
	 * @return
	 * @throws RemoteException
	 */
	public int registEx(String password) throws RemoteException {
		int value = -1;
		value = binding.registEx(softwareSerialNo, key, password);
		return value;
	}
	/**
	 * 发送短信
	 * @param mobiles 手机号码(字符串数组,最多为200个手机号码)
	 * @param smsContent 短信内容(最多500个汉字或1000个纯英文，emay服务器程序能够自动分割；亿美有多个通道为客户提供服务，所以分割原则采用最短字数的通道为分割短信长度的规则，请客户应用程序不要自己分割短信以免造成混乱)
	 * @param addSerial 扩展号码 (长度小于15的字符串) 用户可通过附加码自定义短信类别        扩展号码的功能，需另外申请，当未申请扩展号码功能时，该参数默认为空值即可。
	 * @param smsPriority 短信等级，范围1~5，数值越高优先级越高
	 * @return
	 * @throws RemoteException
	 */
	public int sendSMS(String[] mobiles, String smsContent, String addSerial,
			int smsPriority) throws RemoteException {
		int value = -1;
		value = binding.sendSMS(softwareSerialNo, key, "", mobiles, smsContent,
				addSerial, "gbk", smsPriority, 0);
		return value;
	}

	public int sendScheduledSMSEx(String[] mobiles, String smsContent,
			String sendTime, String srcCharset) throws RemoteException {
		int value = -1;
		value = binding.sendSMS(softwareSerialNo, key, sendTime, mobiles,
				smsContent, "", srcCharset, 3, 0);
		return value;
	}

	public int sendSMSEx(String[] mobiles, String smsContent, String addSerial,
			String srcCharset, int smsPriority, long smsID)
			throws RemoteException {
		int value = -1;
		value = binding.sendSMS(softwareSerialNo, key, "", mobiles, smsContent,
				addSerial, srcCharset, smsPriority, smsID);
		return value;
	}
	/**
	 * 3.8	发送语音验证码
	 * @param mobiles 手机号码(字符串数组,最多为200个手机号码)通常实际应用中只用到了单号码语音验证码,即采用单一手机号码发送
	 * @param smsContent 语音验证码(长度≥4且≤6，格式必须为0~9的全英文半角数字字符)
	 * @param addSerial 发送语音验证码时此项无实际意义可设定为null
	 * @param srcCharset 字符编码，默认为"GBK"
	 * @param smsPriority 语音验证码等级，范围1~5，数值越高优先级越高
	 * @param smsID 语音验证码序列ID，自定义唯一的序列ID，数字位数最大19位，与状态报告ID一一对应，需用户自定义ID规则确保ID的唯一性。如果smsID为0将获取不到相应的状态报告信息。
					该参数与短信smsID作用相同仅在语音验证码支持状态报告时有实际意义,与之对应的语音状态报告与短信状态报告接口共用

	 * @return
	 * @throws RemoteException
	 */
	public String sendVoice(String[] mobiles, String smsContent,
			String addSerial, String srcCharset, int smsPriority, long smsID)
			throws RemoteException {
		String value = null;
		value = binding.sendVoice(softwareSerialNo, key, "", mobiles,
				smsContent, addSerial, srcCharset, smsPriority, smsID);
		return value;
	}
	/**
	 * 3.11	修改密码
	 * @param serialPwd 旧密码
	 * @param serialPwdNew 新密码，6位，必须是数字字符串，必须输入
	 * @return
	 * @throws RemoteException
	 */
	public int serialPwdUpd(String serialPwd, String serialPwdNew)
			throws RemoteException {
		int value = -1;
		value = binding.serialPwdUpd(softwareSerialNo, key, serialPwd,
				serialPwdNew);
		return value;
	}



}

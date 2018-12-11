package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 网校基本配置表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-13 14:58:08
 */
public class SchoolEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//网校ID
	private String schoolid;
	//自定义域名1
	private String domain1;
	//自定义域名2
	private String domain2;
	//网校名称
	private String name;
	//网校LOGO
	private String logo;
	//网校ICON
	private String icon;
	//网校是否已上线
	private String isenabled;
	//点播userId
	private String polyvuserid;
	//点播secretKey
	private String polyvsecretkey;
	//上传视频的授权码
	private String polyvwritetoken;
	//读取视频的授权码
	private String polyvreadtoken;
	//直播userId
	private String liveuserid;
	//直播appId
	private String liveappid;
	//直播secretKey
	private String livesecretkey;
	//计费模式：duration / unlimited
	private String billingplan;
	//校长姓名
	private String webmastername;
	//校长Email
	private String webmasteremail;
	//校长手机号码
	private String webmastermobile;
	//校长联系QQ
	private String webmasterqq;
	//网站ICP备案号
	private String icpnumber;
	//是否开通邮件发送
	private String issmtpenabled;
	//SMTP服务器
	private String smtpserver;
	//SMTP端口
	private Integer smtpport;
	//SMTP验证用户名
	private String smtpusername;
	//SMTP验证密码
	private String smtppassword;
	//邮件是否加密
	private String smtpsslenabled;
	//发信人Email
	private String smtpfromemail;
	//发信人名称
	private String smtpfromname;
	//是否开通VIP会员功能
	private String isvipenabled;
	//发布课程需要管理员审核
	private String isauditcourse;
	//是否开通教师收入分成功能
	private String isroyaltyenabled;
	//默认分成比例：[0-1]
	private BigDecimal defroyaltyrate;
	//是否开通新用户注册功能
	private String isregenabled;
	//是否可以使用手机号注册
	private String isregmobile;
	//是否可以使用Email注册
	private String isregemail;
	//是否可以使用用户名注册
	private String isregusername;
	//注册信息提交给客户系统
	private String isregsharable;
	//客户系统接收信息的URL
	private String regsharableurl;
	//是否同一账号只能一处登录
	private String issinglelogin;
	//是否开通第三方账号登录
	private String isoauthlogin;
	//网校与客户系统同步登录
	private String issynclogin;
	//与客户系统通信的key
	private String syncsecretkey;
	//客户系统同步登录的URL
	private String syncloginurl;
	//客户系统同步退出的URL
	private String synclogouturl;
	//是否开通支付宝即时到账
	private String alipayenabled;
	//支付宝商户ID
	private String alipaypid;
	//支付宝收款密钥
	private String alipaykey;
	//是否登记了微信公众号
	private String weixinenabled;
	//微信公众号应用ID
	private String weixinappid;
	//微信公众号应用密钥
	private String weixinappsecret;
	//微信公众号Token
	private String weixintoken;
	//微信公众号AESKey
	private String weixinaeskey;
	//是否开通微信支付
	private String wxpayenabled;
	//微信支付应用ID
	private String wxpayappid;
	//微信支付商户号
	private String wxpaymchid;
	//微信支付子商户号
	private String wxpaysubmchid;
	//微信支付密钥
	private String wxpaykey;
	//是否开放网校API
	private String isapienabled;
	//API签名认证的Key
	private String apisecretkey;
	//API签名认证的备用Key
	private String apibackupkey;
	//观看点播公开课需要登录
	private String isvodcourselogin;
	//观看直播公开课需要登录
	private String islivecourselogin;
	//网校充值的可选URL
	private String depositurl;
	//JSON格式的网校相关设置
	private String jsonsetting;
	//创建时间
	private Date createdtime;
	//上次修改的时间
	private Date lastmodified;

	/**
	 * 设置：网校ID
	 */
	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}
	/**
	 * 获取：网校ID
	 */
	public String getSchoolid() {
		return schoolid;
	}
	/**
	 * 设置：自定义域名1
	 */
	public void setDomain1(String domain1) {
		this.domain1 = domain1;
	}
	/**
	 * 获取：自定义域名1
	 */
	public String getDomain1() {
		return domain1;
	}
	/**
	 * 设置：自定义域名2
	 */
	public void setDomain2(String domain2) {
		this.domain2 = domain2;
	}
	/**
	 * 获取：自定义域名2
	 */
	public String getDomain2() {
		return domain2;
	}
	/**
	 * 设置：网校名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：网校名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：网校LOGO
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * 获取：网校LOGO
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * 设置：网校ICON
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 获取：网校ICON
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 设置：网校是否已上线
	 */
	public void setIsenabled(String isenabled) {
		this.isenabled = isenabled;
	}
	/**
	 * 获取：网校是否已上线
	 */
	public String getIsenabled() {
		return isenabled;
	}
	/**
	 * 设置：点播userId
	 */
	public void setPolyvuserid(String polyvuserid) {
		this.polyvuserid = polyvuserid;
	}
	/**
	 * 获取：点播userId
	 */
	public String getPolyvuserid() {
		return polyvuserid;
	}
	/**
	 * 设置：点播secretKey
	 */
	public void setPolyvsecretkey(String polyvsecretkey) {
		this.polyvsecretkey = polyvsecretkey;
	}
	/**
	 * 获取：点播secretKey
	 */
	public String getPolyvsecretkey() {
		return polyvsecretkey;
	}
	/**
	 * 设置：上传视频的授权码
	 */
	public void setPolyvwritetoken(String polyvwritetoken) {
		this.polyvwritetoken = polyvwritetoken;
	}
	/**
	 * 获取：上传视频的授权码
	 */
	public String getPolyvwritetoken() {
		return polyvwritetoken;
	}
	/**
	 * 设置：读取视频的授权码
	 */
	public void setPolyvreadtoken(String polyvreadtoken) {
		this.polyvreadtoken = polyvreadtoken;
	}
	/**
	 * 获取：读取视频的授权码
	 */
	public String getPolyvreadtoken() {
		return polyvreadtoken;
	}
	/**
	 * 设置：直播userId
	 */
	public void setLiveuserid(String liveuserid) {
		this.liveuserid = liveuserid;
	}
	/**
	 * 获取：直播userId
	 */
	public String getLiveuserid() {
		return liveuserid;
	}
	/**
	 * 设置：直播appId
	 */
	public void setLiveappid(String liveappid) {
		this.liveappid = liveappid;
	}
	/**
	 * 获取：直播appId
	 */
	public String getLiveappid() {
		return liveappid;
	}
	/**
	 * 设置：直播secretKey
	 */
	public void setLivesecretkey(String livesecretkey) {
		this.livesecretkey = livesecretkey;
	}
	/**
	 * 获取：直播secretKey
	 */
	public String getLivesecretkey() {
		return livesecretkey;
	}
	/**
	 * 设置：计费模式：duration / unlimited
	 */
	public void setBillingplan(String billingplan) {
		this.billingplan = billingplan;
	}
	/**
	 * 获取：计费模式：duration / unlimited
	 */
	public String getBillingplan() {
		return billingplan;
	}
	/**
	 * 设置：校长姓名
	 */
	public void setWebmastername(String webmastername) {
		this.webmastername = webmastername;
	}
	/**
	 * 获取：校长姓名
	 */
	public String getWebmastername() {
		return webmastername;
	}
	/**
	 * 设置：校长Email
	 */
	public void setWebmasteremail(String webmasteremail) {
		this.webmasteremail = webmasteremail;
	}
	/**
	 * 获取：校长Email
	 */
	public String getWebmasteremail() {
		return webmasteremail;
	}
	/**
	 * 设置：校长手机号码
	 */
	public void setWebmastermobile(String webmastermobile) {
		this.webmastermobile = webmastermobile;
	}
	/**
	 * 获取：校长手机号码
	 */
	public String getWebmastermobile() {
		return webmastermobile;
	}
	/**
	 * 设置：校长联系QQ
	 */
	public void setWebmasterqq(String webmasterqq) {
		this.webmasterqq = webmasterqq;
	}
	/**
	 * 获取：校长联系QQ
	 */
	public String getWebmasterqq() {
		return webmasterqq;
	}
	/**
	 * 设置：网站ICP备案号
	 */
	public void setIcpnumber(String icpnumber) {
		this.icpnumber = icpnumber;
	}
	/**
	 * 获取：网站ICP备案号
	 */
	public String getIcpnumber() {
		return icpnumber;
	}
	/**
	 * 设置：是否开通邮件发送
	 */
	public void setIssmtpenabled(String issmtpenabled) {
		this.issmtpenabled = issmtpenabled;
	}
	/**
	 * 获取：是否开通邮件发送
	 */
	public String getIssmtpenabled() {
		return issmtpenabled;
	}
	/**
	 * 设置：SMTP服务器
	 */
	public void setSmtpserver(String smtpserver) {
		this.smtpserver = smtpserver;
	}
	/**
	 * 获取：SMTP服务器
	 */
	public String getSmtpserver() {
		return smtpserver;
	}
	/**
	 * 设置：SMTP端口
	 */
	public void setSmtpport(Integer smtpport) {
		this.smtpport = smtpport;
	}
	/**
	 * 获取：SMTP端口
	 */
	public Integer getSmtpport() {
		return smtpport;
	}
	/**
	 * 设置：SMTP验证用户名
	 */
	public void setSmtpusername(String smtpusername) {
		this.smtpusername = smtpusername;
	}
	/**
	 * 获取：SMTP验证用户名
	 */
	public String getSmtpusername() {
		return smtpusername;
	}
	/**
	 * 设置：SMTP验证密码
	 */
	public void setSmtppassword(String smtppassword) {
		this.smtppassword = smtppassword;
	}
	/**
	 * 获取：SMTP验证密码
	 */
	public String getSmtppassword() {
		return smtppassword;
	}
	/**
	 * 设置：邮件是否加密
	 */
	public void setSmtpsslenabled(String smtpsslenabled) {
		this.smtpsslenabled = smtpsslenabled;
	}
	/**
	 * 获取：邮件是否加密
	 */
	public String getSmtpsslenabled() {
		return smtpsslenabled;
	}
	/**
	 * 设置：发信人Email
	 */
	public void setSmtpfromemail(String smtpfromemail) {
		this.smtpfromemail = smtpfromemail;
	}
	/**
	 * 获取：发信人Email
	 */
	public String getSmtpfromemail() {
		return smtpfromemail;
	}
	/**
	 * 设置：发信人名称
	 */
	public void setSmtpfromname(String smtpfromname) {
		this.smtpfromname = smtpfromname;
	}
	/**
	 * 获取：发信人名称
	 */
	public String getSmtpfromname() {
		return smtpfromname;
	}
	/**
	 * 设置：是否开通VIP会员功能
	 */
	public void setIsvipenabled(String isvipenabled) {
		this.isvipenabled = isvipenabled;
	}
	/**
	 * 获取：是否开通VIP会员功能
	 */
	public String getIsvipenabled() {
		return isvipenabled;
	}
	/**
	 * 设置：发布课程需要管理员审核
	 */
	public void setIsauditcourse(String isauditcourse) {
		this.isauditcourse = isauditcourse;
	}
	/**
	 * 获取：发布课程需要管理员审核
	 */
	public String getIsauditcourse() {
		return isauditcourse;
	}
	/**
	 * 设置：是否开通教师收入分成功能
	 */
	public void setIsroyaltyenabled(String isroyaltyenabled) {
		this.isroyaltyenabled = isroyaltyenabled;
	}
	/**
	 * 获取：是否开通教师收入分成功能
	 */
	public String getIsroyaltyenabled() {
		return isroyaltyenabled;
	}
	/**
	 * 设置：默认分成比例：[0-1]
	 */
	public void setDefroyaltyrate(BigDecimal defroyaltyrate) {
		this.defroyaltyrate = defroyaltyrate;
	}
	/**
	 * 获取：默认分成比例：[0-1]
	 */
	public BigDecimal getDefroyaltyrate() {
		return defroyaltyrate;
	}
	/**
	 * 设置：是否开通新用户注册功能
	 */
	public void setIsregenabled(String isregenabled) {
		this.isregenabled = isregenabled;
	}
	/**
	 * 获取：是否开通新用户注册功能
	 */
	public String getIsregenabled() {
		return isregenabled;
	}
	/**
	 * 设置：是否可以使用手机号注册
	 */
	public void setIsregmobile(String isregmobile) {
		this.isregmobile = isregmobile;
	}
	/**
	 * 获取：是否可以使用手机号注册
	 */
	public String getIsregmobile() {
		return isregmobile;
	}
	/**
	 * 设置：是否可以使用Email注册
	 */
	public void setIsregemail(String isregemail) {
		this.isregemail = isregemail;
	}
	/**
	 * 获取：是否可以使用Email注册
	 */
	public String getIsregemail() {
		return isregemail;
	}
	/**
	 * 设置：是否可以使用用户名注册
	 */
	public void setIsregusername(String isregusername) {
		this.isregusername = isregusername;
	}
	/**
	 * 获取：是否可以使用用户名注册
	 */
	public String getIsregusername() {
		return isregusername;
	}
	/**
	 * 设置：注册信息提交给客户系统
	 */
	public void setIsregsharable(String isregsharable) {
		this.isregsharable = isregsharable;
	}
	/**
	 * 获取：注册信息提交给客户系统
	 */
	public String getIsregsharable() {
		return isregsharable;
	}
	/**
	 * 设置：客户系统接收信息的URL
	 */
	public void setRegsharableurl(String regsharableurl) {
		this.regsharableurl = regsharableurl;
	}
	/**
	 * 获取：客户系统接收信息的URL
	 */
	public String getRegsharableurl() {
		return regsharableurl;
	}
	/**
	 * 设置：是否同一账号只能一处登录
	 */
	public void setIssinglelogin(String issinglelogin) {
		this.issinglelogin = issinglelogin;
	}
	/**
	 * 获取：是否同一账号只能一处登录
	 */
	public String getIssinglelogin() {
		return issinglelogin;
	}
	/**
	 * 设置：是否开通第三方账号登录
	 */
	public void setIsoauthlogin(String isoauthlogin) {
		this.isoauthlogin = isoauthlogin;
	}
	/**
	 * 获取：是否开通第三方账号登录
	 */
	public String getIsoauthlogin() {
		return isoauthlogin;
	}
	/**
	 * 设置：网校与客户系统同步登录
	 */
	public void setIssynclogin(String issynclogin) {
		this.issynclogin = issynclogin;
	}
	/**
	 * 获取：网校与客户系统同步登录
	 */
	public String getIssynclogin() {
		return issynclogin;
	}
	/**
	 * 设置：与客户系统通信的key
	 */
	public void setSyncsecretkey(String syncsecretkey) {
		this.syncsecretkey = syncsecretkey;
	}
	/**
	 * 获取：与客户系统通信的key
	 */
	public String getSyncsecretkey() {
		return syncsecretkey;
	}
	/**
	 * 设置：客户系统同步登录的URL
	 */
	public void setSyncloginurl(String syncloginurl) {
		this.syncloginurl = syncloginurl;
	}
	/**
	 * 获取：客户系统同步登录的URL
	 */
	public String getSyncloginurl() {
		return syncloginurl;
	}
	/**
	 * 设置：客户系统同步退出的URL
	 */
	public void setSynclogouturl(String synclogouturl) {
		this.synclogouturl = synclogouturl;
	}
	/**
	 * 获取：客户系统同步退出的URL
	 */
	public String getSynclogouturl() {
		return synclogouturl;
	}
	/**
	 * 设置：是否开通支付宝即时到账
	 */
	public void setAlipayenabled(String alipayenabled) {
		this.alipayenabled = alipayenabled;
	}
	/**
	 * 获取：是否开通支付宝即时到账
	 */
	public String getAlipayenabled() {
		return alipayenabled;
	}
	/**
	 * 设置：支付宝商户ID
	 */
	public void setAlipaypid(String alipaypid) {
		this.alipaypid = alipaypid;
	}
	/**
	 * 获取：支付宝商户ID
	 */
	public String getAlipaypid() {
		return alipaypid;
	}
	/**
	 * 设置：支付宝收款密钥
	 */
	public void setAlipaykey(String alipaykey) {
		this.alipaykey = alipaykey;
	}
	/**
	 * 获取：支付宝收款密钥
	 */
	public String getAlipaykey() {
		return alipaykey;
	}
	/**
	 * 设置：是否登记了微信公众号
	 */
	public void setWeixinenabled(String weixinenabled) {
		this.weixinenabled = weixinenabled;
	}
	/**
	 * 获取：是否登记了微信公众号
	 */
	public String getWeixinenabled() {
		return weixinenabled;
	}
	/**
	 * 设置：微信公众号应用ID
	 */
	public void setWeixinappid(String weixinappid) {
		this.weixinappid = weixinappid;
	}
	/**
	 * 获取：微信公众号应用ID
	 */
	public String getWeixinappid() {
		return weixinappid;
	}
	/**
	 * 设置：微信公众号应用密钥
	 */
	public void setWeixinappsecret(String weixinappsecret) {
		this.weixinappsecret = weixinappsecret;
	}
	/**
	 * 获取：微信公众号应用密钥
	 */
	public String getWeixinappsecret() {
		return weixinappsecret;
	}
	/**
	 * 设置：微信公众号Token
	 */
	public void setWeixintoken(String weixintoken) {
		this.weixintoken = weixintoken;
	}
	/**
	 * 获取：微信公众号Token
	 */
	public String getWeixintoken() {
		return weixintoken;
	}
	/**
	 * 设置：微信公众号AESKey
	 */
	public void setWeixinaeskey(String weixinaeskey) {
		this.weixinaeskey = weixinaeskey;
	}
	/**
	 * 获取：微信公众号AESKey
	 */
	public String getWeixinaeskey() {
		return weixinaeskey;
	}
	/**
	 * 设置：是否开通微信支付
	 */
	public void setWxpayenabled(String wxpayenabled) {
		this.wxpayenabled = wxpayenabled;
	}
	/**
	 * 获取：是否开通微信支付
	 */
	public String getWxpayenabled() {
		return wxpayenabled;
	}
	/**
	 * 设置：微信支付应用ID
	 */
	public void setWxpayappid(String wxpayappid) {
		this.wxpayappid = wxpayappid;
	}
	/**
	 * 获取：微信支付应用ID
	 */
	public String getWxpayappid() {
		return wxpayappid;
	}
	/**
	 * 设置：微信支付商户号
	 */
	public void setWxpaymchid(String wxpaymchid) {
		this.wxpaymchid = wxpaymchid;
	}
	/**
	 * 获取：微信支付商户号
	 */
	public String getWxpaymchid() {
		return wxpaymchid;
	}
	/**
	 * 设置：微信支付子商户号
	 */
	public void setWxpaysubmchid(String wxpaysubmchid) {
		this.wxpaysubmchid = wxpaysubmchid;
	}
	/**
	 * 获取：微信支付子商户号
	 */
	public String getWxpaysubmchid() {
		return wxpaysubmchid;
	}
	/**
	 * 设置：微信支付密钥
	 */
	public void setWxpaykey(String wxpaykey) {
		this.wxpaykey = wxpaykey;
	}
	/**
	 * 获取：微信支付密钥
	 */
	public String getWxpaykey() {
		return wxpaykey;
	}
	/**
	 * 设置：是否开放网校API
	 */
	public void setIsapienabled(String isapienabled) {
		this.isapienabled = isapienabled;
	}
	/**
	 * 获取：是否开放网校API
	 */
	public String getIsapienabled() {
		return isapienabled;
	}
	/**
	 * 设置：API签名认证的Key
	 */
	public void setApisecretkey(String apisecretkey) {
		this.apisecretkey = apisecretkey;
	}
	/**
	 * 获取：API签名认证的Key
	 */
	public String getApisecretkey() {
		return apisecretkey;
	}
	/**
	 * 设置：API签名认证的备用Key
	 */
	public void setApibackupkey(String apibackupkey) {
		this.apibackupkey = apibackupkey;
	}
	/**
	 * 获取：API签名认证的备用Key
	 */
	public String getApibackupkey() {
		return apibackupkey;
	}
	/**
	 * 设置：观看点播公开课需要登录
	 */
	public void setIsvodcourselogin(String isvodcourselogin) {
		this.isvodcourselogin = isvodcourselogin;
	}
	/**
	 * 获取：观看点播公开课需要登录
	 */
	public String getIsvodcourselogin() {
		return isvodcourselogin;
	}
	/**
	 * 设置：观看直播公开课需要登录
	 */
	public void setIslivecourselogin(String islivecourselogin) {
		this.islivecourselogin = islivecourselogin;
	}
	/**
	 * 获取：观看直播公开课需要登录
	 */
	public String getIslivecourselogin() {
		return islivecourselogin;
	}
	/**
	 * 设置：网校充值的可选URL
	 */
	public void setDepositurl(String depositurl) {
		this.depositurl = depositurl;
	}
	/**
	 * 获取：网校充值的可选URL
	 */
	public String getDepositurl() {
		return depositurl;
	}
	/**
	 * 设置：JSON格式的网校相关设置
	 */
	public void setJsonsetting(String jsonsetting) {
		this.jsonsetting = jsonsetting;
	}
	/**
	 * 获取：JSON格式的网校相关设置
	 */
	public String getJsonsetting() {
		return jsonsetting;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatedtime() {
		return createdtime;
	}
	/**
	 * 设置：上次修改的时间
	 */
	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}
	/**
	 * 获取：上次修改的时间
	 */
	public Date getLastmodified() {
		return lastmodified;
	}
}

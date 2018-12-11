package com.hq.learningcenter.utils;

/**
 * 定义平台需要用到的静态常量。
 * @author XingNing OU
 */
public interface Constants {

    /**
     * 设置Cookie时使用的path.
     */
    public static final String COOKIE_PATH = "/";

    /**
     * 用户登录Cookie的Cookie名称前缀。
     */
    public static final String COOKIE_NAME_PREFIX = "utoken_";

    /**
     * 用户Cookie加密的秘钥。
     */
    public static final String COOKIE_SIGNATURE_SECRET = "CLOUD_SCHOOL";

    /**
     * 超级管理员登录的Session ID。
     */
    public static final String ROOT_SESSION_NAME = "root_sid";

    /**
     * 当前登录用户的SESSION NAME。
     */
    public static final String USER_SESSION_NAME = "curr_user";

    /**
     * 字符串之间用来分隔的特殊字符。
     */
    public static final String STRING_SEPARATOR = "@@";

    /**
     * 系统默认字符串。
     */
    public static final String SYSTEM = "system";

    /**
     * 默认课程分类ID。
     */
    public static final int DEFAULT_CATEGORY_ID = 1;

    /**
     * 默认的排序序号。
     */
    public static final int DEFAULT_ORDERED = 50;

    /**
     * 后台管理员登录信息的SESSION名称。
     */
    public static final String ADMIN_SESSION_NAME = "sid_admin";

    /**
     * 新学员欢迎短信模板
     */
    public static final String SMS_WELCOME = "您已经成功注册为${smsSign}会员，登录帐号为：${mobile}【${smsSign}】";

    /**
     * 密码更改通知短信模板
     */
    public static final String SMS_RESET_PWD = "您好，您的账号${mobile}已于${date}成功更换密码，若非本人操作，请及时利用手机号码找回密码。【${smsSign}】";

    /**
     * 课程预约短信模板
     */
    public static final String SMS_COURSE_BOKKING = "您预约的《${title}》将于今天${date}开始直播，请记得观看哦。【${smsSign}】";
    
    /**
     * 验证码短信模板
     */
    public static final String SMS_CODE = "(${smsSign}验证码，三分钟内有效)【${smsSign}】";
    
    /**
     * 对接学分系统获取我的学分接口MD5加密的拼接字符串
     */
    public static final String CREDIT_MD5_KEY = "6512bd43d9caa6e02c990b$AF>.12**";
}

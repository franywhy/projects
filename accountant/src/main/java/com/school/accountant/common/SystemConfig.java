package com.school.accountant.common;

/**
 * 系统全局配置文件相关工具类
 *
 */
public class SystemConfig {

	/**
     * 设置Cookie时使用的path.
     */
    public static final String COOKIE_PATH = "/";
    
    public static final String COOKIE_DOMAIN = "hqjy.com";

    /**
     * 用户登录Cookie的Cookie名称前缀。
     *//*
    public static final String COOKIE_NAME_PREFIX = "kj_utoken_";

    *//**
     * 用户Cookie加密的秘钥。
     *//*
    public static final String COOKIE_SIGNATURE_SECRET = "KJ_CLOUD_SCHOOL";

    *//**
     * 超级管理员登录的Session ID。
     *//*
    public static final String ROOT_SESSION_NAME = "kj_root_sid";

    *//**
     * 当前登录用户的SESSION NAME。
     */
    public static final String USER_SESSION_NAME = "kj_curr_user";

    /**
     * 字符串之间用来分隔的特殊字符。
     *//*
    public static final String STRING_SEPARATOR = "@@";

    *//**
     * 系统默认字符串。
     *//*
    public static final String SYSTEM = "system";

    *//**
     * 默认课程分类ID。
     *//*
    public static final int DEFAULT_CATEGORY_ID = 1;

    *//**
     * 默认的排序序号。
     *//*
    public static final int DEFAULT_ORDERED = 50;

    *//**
     * 后台管理员登录信息的SESSION名称。
     *//*
    public static final String ADMIN_SESSION_NAME = "kj_sid_admin";*/
    
    
    /**
     * 与实训系统对接单点登录的Session ID。
     */
    public static final String SHIXUN_SessionID = "kjcity_SessionID";
    
    /**
     * redis中实训系统所有账套信息的key
     */
    public static final String SHIXUN_ALL_PRODUCT_KEY = "shixun_all_products";
    
    /**
     * session中token的名称
     */
    public static final String SESSION_TOKEN_KEY = "token";
    
    /**
     * session中userInfo的名称
     */
    public static final String SESSION_USER_KEY = "userInfo";
    
    /**
     * cookie中token的名称
     */
    public static final String COOKIE_TOKEN_NAME = "token";
    
    /**
     * cookie中ssotoken的名称
     */
    public static final String COOKIE_SSOTOKEN_NAME = "SSOTOKEN";
    
    /**
     * cookie中用户的 nickName
     */
    public static final String COOKIE_NICKNAME = "nickName";
    
    /**
     * cookie中用户的头像
     */
    public static final String COOKIE_AVATAR = "avatar";
    
    /**
     * 调用实训系统获取用户已开通账套信息接口的加密KEY
     */
    public static final String KJCITY_MD5_KEY="%^$AF>.12*******";
    
    /**
     * SSO接口返回码（200代表操作成功）
     */
    public static final String SSO_SUCCESS_CODE="200";
}

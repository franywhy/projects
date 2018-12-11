package com.izhubo.model;

/**
 * date: 13-8-5 下午1:56
 *
 * @author: wubinjie@ak.cc
 */
public interface Code {


    Integer OK = 1;
    String OKS = "1";
    String OK_S = "";
    
    
    Integer 系统异常稍后再试 = 99;
    String 系统异常稍后再试S = "99";
    String 系统异常稍后再试_S = "系统异常稍后再试";
    
    Integer 余额不足 = 30412;
    String 余额不足_S = "余额不足";
    
    Integer 权限不足 = 30413;
    String 权限不足_S = "权限不足";
    
    Integer 验证码验证失败 = 30419;
    String 验证码验证失败_S = "验证码验证失败";

    
    
    Integer 参数无效 = 30406;
    String 参数无效S = "30406";
    String 参数无效_S = "参数无效";
    
    Integer 提问保存错误 = 40000;
    String 提问保存错误_S = "提问保存错误";
    
    Integer 已被抢答 = 40001;
    String 已被抢答S = "40001";
    String 已被抢答_S = "已被抢答";
    Integer 红包已打开 = 40002;
    String 红包已打开S = "40002";
    String 红包已打开_S = "红包已打开";
    
    Integer 申请金额超出总金额 = 60001;
    String 申请金额超出总金额S = "60001";
    String 申请金额超出总金额_S = "申请金额超出总金额";
    
    
    Integer 密码不正确 = 60002;
    String 密码不正确S = "60002";
    String 密码不正确_S = "密码不正确";
    
    Integer 文件MD5值校验失败 = 70002;
    String 文件MD5值校验失败S = "70002";
    String 文件MD5值校验失败_S = "文件上传失败";
    
    
    
    
    

}
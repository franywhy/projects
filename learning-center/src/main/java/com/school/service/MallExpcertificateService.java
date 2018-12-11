package com.school.service;

import com.school.entity.MallExpcertificateEntity;

/**
 * Created by DL on 2017/12/19.
 */
public interface MallExpcertificateService {
    //根据用户手机获取证书
    MallExpcertificateEntity queryExpcertificateByMobile(Long userId);
    //根据用户身份证和姓名获取证书
    MallExpcertificateEntity queryExpcertificateByIDCard(String IDCard, String userName);
}
